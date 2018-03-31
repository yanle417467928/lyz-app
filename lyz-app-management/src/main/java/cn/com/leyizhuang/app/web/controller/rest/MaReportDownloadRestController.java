package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.core.utils.DateUtil;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.reportDownload.NotPickGoodsReportDO;
import cn.com.leyizhuang.app.foundation.pojo.reportDownload.ReceiptsReportDO;
import cn.com.leyizhuang.app.foundation.service.AdminUserStoreService;
import cn.com.leyizhuang.app.foundation.service.MaReportDownloadService;
import cn.com.leyizhuang.common.util.CountUtil;
import com.github.pagehelper.PageInfo;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.*;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import jxl.write.Number;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GenerationRoad
 * @date 2018/3/22
 */
@RestController
@RequestMapping(value = MaReportDownloadRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaReportDownloadRestController extends BaseRestController{

    protected static final String PRE_URL = "/rest/reportDownload";

    private final Logger logger = LoggerFactory.getLogger(MaReportDownloadRestController.class);

    @Autowired
    private AdminUserStoreService adminUserStoreService;

    @Autowired
    private MaReportDownloadService maReportDownloadService;

    private static final int maxRowNum = 60000;

    @GetMapping(value = "/receipts/page/grid")
    public GridDataVO<ReceiptsReportDO> restStorePreDepositPageGird(Integer offset, Integer size, Long cityId, Long storeId, String storeType,
                                                                    String startTime, String endTime, String payType, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getNotZsType());
        PageInfo<ReceiptsReportDO> receiptsReportDOPageInfo = this.maReportDownloadService.findReceiptsReportDOAll(cityId, storeId, storeType, startTime,
                endTime, payType, keywords, storeIds, page, size);
        return new GridDataVO<ReceiptsReportDO>().transform(receiptsReportDOPageInfo.getList(), receiptsReportDOPageInfo.getTotal());
    }

    @GetMapping(value = "/not/pickGoods/page/grid")
    public GridDataVO<NotPickGoodsReportDO> restNotPickGoodsPageGird(Integer offset, Integer size, Long cityId, Long storeId, String storeType,
                                                                     String startTime, String endTime, String pickType) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getNotZsType());
        PageInfo<NotPickGoodsReportDO> notPickGoodsReportDOAll = this.maReportDownloadService.findNotPickGoodsReportDOAll(cityId, storeId, storeType, startTime,
                endTime, pickType, storeIds, page, size);
        return new GridDataVO<NotPickGoodsReportDO>().transform(notPickGoodsReportDOAll.getList(), notPickGoodsReportDOAll.getTotal());
    }

    @GetMapping(value = "/receipts/download")
    public void receiptsDownload(HttpServletRequest request, HttpServletResponse response, Long cityId, Long storeId, String storeType, String startTime, String endTime,
                                 String payType, String keywords) {
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getNotZsType());
        List<ReceiptsReportDO> receiptsReportDOS = this.maReportDownloadService.receiptsDownload(cityId, storeId, storeType, startTime,
                endTime, payType, keywords, storeIds);
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String shiroName = "";
        if (null != shiroUser) {
            shiroName = shiroUser.getName();
        }

        response.setContentType("text/html;charset=UTF-8");
        //创建名称
        String fileurl = "收款报表-"+ DateUtil.getCurrentTimeStr("yyyyMMddHHmmss") +".xls";//如  D:/xx/xx/xxx.xls

        WritableWorkbook wwb = null;
        try {
            //创建文件
            wwb = exportXML(fileurl, response);

            //excel单表最大行数是65535
            int maxSize = 0;
            if (receiptsReportDOS != null) {
                maxSize = receiptsReportDOS.size();
            }
            int sheets = maxSize / maxRowNum + 1;
            //设置excel的sheet数
            for (int i = 0; i < sheets; i++) {
                //标题格式
                WritableCellFormat titleFormat = this.setTitleStyle();
                //正文格式
                WritableCellFormat textFormat = this.setTextStyle();

                //工作表，参数0表示这是第一页
                WritableSheet ws = wwb.createSheet("第" + (i + 1) + "页", i);

                //筛选条件
                Map<String, String> map = new HashMap<>();
                if (null != cityId && !(cityId.equals(-1L)) && null != receiptsReportDOS && receiptsReportDOS.size() > 0){
                    map.put("城市", receiptsReportDOS.get(0).getCityName());
                } else {
                    map.put("城市", "无");
                }
                if (null != storeId && !(storeId.equals(-1L)) && null != receiptsReportDOS && receiptsReportDOS.size() > 0){
                    map.put("门店", receiptsReportDOS.get(0).getStoreName());
                } else {
                    map.put("门店", "无");
                }
                if (null != storeType && !("".equals(storeType)) && null != receiptsReportDOS && receiptsReportDOS.size() > 0){
                    map.put("门店类型", receiptsReportDOS.get(0).getStoreType());
                } else {
                    map.put("门店类型", "无");
                }
                if (null != payType && !("".equals(payType)) && null != receiptsReportDOS && receiptsReportDOS.size() > 0){
                    map.put("支付方式", receiptsReportDOS.get(0).getPayType());
                } else {
                    map.put("支付方式", "无");
                }
                if (null != startTime && !("".equals(startTime))){
                    map.put("开始时间", startTime);
                } else {
                    map.put("开始时间", "无");
                }
                if (null != keywords && !("".equals(keywords))){
                    map.put("关键字", keywords);
                } else {
                    map.put("关键字", "无");
                }
                if (null != endTime && !("".equals(endTime))){
                    map.put("结束时间", endTime);
                } else {
                    map.put("结束时间", "无");
                }
                //设置筛选条件
                ws = this.setCondition(ws, map, titleFormat, shiroName, textFormat);
                //列宽
                int[] columnView = {10, 13, 10, 20, 12, 10, 30, 15, 20};
                //列标题
                String[] titles = {"城市", "门店名称", "门店类型", "付款/退款时间", "支付方式", "支付金额", "订/退单号", "备注"};
                //计算标题开始行号
                int row = 1;
                if (null != map && map.size() > 0){
                    row = (map.size() + 1)/2 + 4;
                }


                ws.addCell(new Label(0, row, "微信", titleFormat));
                ws.addCell(new Label(1, row, "支付宝", titleFormat));
                ws.addCell(new Label(2, row, "银联", titleFormat));
                ws.addCell(new Label(3, row, "现金", titleFormat));
                ws.addCell(new Label(4, row, "POS", titleFormat));
                ws.addCell(new Label(5, row, "其他", titleFormat));
                ws.addCell(new Label(6, row, "门店预存款", titleFormat));
                ws.addCell(new Label(7, row, "顾客预存款", titleFormat));
                ws.addCell(new Label(8, row, "支付总额", titleFormat));

                int collectRow = row + 1;

                row += 5;
                //设置标题
                ws = this.setHeader(ws, titleFormat, columnView, titles, row);

                row += 1;
                Double cusPrepay = 0D;
                Double stPrepay = 0D;
                Double alipay = 0D;
                Double weChat = 0D;
                Double unionPay = 0D;
                Double pos = 0D;
                Double cash = 0D;
                Double other = 0D;
                Double totle = 0D;
                WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"),9,WritableFont.NO_BOLD,false,
                        UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                //填写表体数据
                for (int j = 0; j < maxRowNum; j++) {
                    if (j + i * maxRowNum >= maxSize) {
                        break;
                    }
                    ReceiptsReportDO receiptsReportDO = receiptsReportDOS.get(j + i * maxRowNum);
                    ws.addCell(new Label(0, j + row, receiptsReportDO.getCityName(), textFormat));
                    ws.addCell(new Label(1, j + row, receiptsReportDO.getStoreName(), textFormat));
                    ws.addCell(new Label(2, j + row, receiptsReportDO.getStoreType(), textFormat));
                    ws.addCell(new Label(3, j + row, receiptsReportDO.getPayTime(), textFormat));
                    ws.addCell(new Label(4, j + row, receiptsReportDO.getPayType(), textFormat));
                    ws.addCell(new Number(5, j + row, receiptsReportDO.getMoney(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Label(6, j + row, receiptsReportDO.getOrderNumber(), textFormat));
                    ws.addCell(new Label(7, j + row, receiptsReportDO.getRemarks(), textFormat));
                    if ("CUS_PREPAY".equals(receiptsReportDO.getPayTypes())){
                        cusPrepay = CountUtil.add(cusPrepay, null==receiptsReportDO.getMoney()?0D:receiptsReportDO.getMoney());
                    } else if ("ST_PREPAY".equals(receiptsReportDO.getPayTypes())){
                        stPrepay = CountUtil.add(stPrepay, null==receiptsReportDO.getMoney()?0D:receiptsReportDO.getMoney());
                    } else if ("ALIPAY".equals(receiptsReportDO.getPayTypes())){
                        alipay = CountUtil.add(alipay, null==receiptsReportDO.getMoney()?0D:receiptsReportDO.getMoney());
                    } else if ("WE_CHAT".equals(receiptsReportDO.getPayTypes())){
                        weChat = CountUtil.add(weChat, null==receiptsReportDO.getMoney()?0D:receiptsReportDO.getMoney());
                    } else if ("UNION_PAY".equals(receiptsReportDO.getPayTypes())){
                        unionPay = CountUtil.add(unionPay, null==receiptsReportDO.getMoney()?0D:receiptsReportDO.getMoney());
                    } else if ("POS".equals(receiptsReportDO.getPayTypes())){
                        pos = CountUtil.add(pos, null==receiptsReportDO.getMoney()?0D:receiptsReportDO.getMoney());
                    } else if ("CASH".equals(receiptsReportDO.getPayTypes())){
                        cash = CountUtil.add(cash, null==receiptsReportDO.getMoney()?0D:receiptsReportDO.getMoney());
                    } else {
                        other = CountUtil.add(other, null==receiptsReportDO.getMoney()?0D:receiptsReportDO.getMoney());
                    }
                    totle = CountUtil.add(totle, null==receiptsReportDO.getMoney()?0D:receiptsReportDO.getMoney());
                }
                ws.addCell(new Number(0, collectRow, weChat, new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                ws.addCell(new Number(1, collectRow, alipay, new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                ws.addCell(new Number(2, collectRow, unionPay, new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                ws.addCell(new Number(3, collectRow, cash, new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                ws.addCell(new Number(4, collectRow, pos, new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                ws.addCell(new Number(5, collectRow, other, new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                ws.addCell(new Number(6, collectRow, stPrepay, new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                ws.addCell(new Number(7, collectRow, cusPrepay, new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                ws.addCell(new Number(8, collectRow, totle, new WritableCellFormat(textFont, new NumberFormat("0.00"))));
            }
        }catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }finally {
            if(wwb != null) {
                try {
                    wwb.write();//刷新（或写入），生成一个excel文档
                    wwb.close();//关闭
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 未提货报表
     *
     * @param request
     * @param response
     * @param cityId
     * @param storeId
     * @param storeType
     * @param startTime
     * @param endTime
     * @param pickType
     */
    @GetMapping(value = "/not/pickGoods/download")
    public void notPickGoodsDownload(HttpServletRequest request, HttpServletResponse response, Long cityId, Long storeId, String storeType, String startTime, String endTime,
                                     String pickType) {
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getNotZsType());
        List<NotPickGoodsReportDO> notPickGoodsReportDOS = this.maReportDownloadService.notPickGoodsDownload(cityId, storeId, storeType, startTime,
                endTime, pickType, storeIds);
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String shiroName = "";
        if (null != shiroUser) {
            shiroName = shiroUser.getName();
        }

        response.setContentType("text/html;charset=UTF-8");
        //创建名称
        String fileurl = "未提货报表-" + DateUtil.getCurrentTimeStr("yyyyMMddHHmmss") + ".xls";//如  D:/xx/xx/xxx.xls

        WritableWorkbook wwb = null;
        try {
            //创建文件
            wwb = exportXML(fileurl, response);

            //excel单表最大行数是65535
            int maxSize = 0;
            if (notPickGoodsReportDOS != null) {
                maxSize = notPickGoodsReportDOS.size();
            }
            int sheets = maxSize / maxRowNum + 1;
            //设置excel的sheet数
            for (int i = 0; i < sheets; i++) {
                //标题格式
                WritableCellFormat titleFormat = this.setTitleStyle();
                //正文格式
                WritableCellFormat textFormat = this.setTextStyle();

                //工作表，参数0表示这是第一页
                WritableSheet ws = wwb.createSheet("第" + (i + 1) + "页", i);

                //筛选条件
                Map<String, String> map = new HashMap<>();
                if (null != cityId && !(cityId.equals(-1L)) && null != notPickGoodsReportDOS && notPickGoodsReportDOS.size() > 0) {
                    map.put("城市", notPickGoodsReportDOS.get(0).getCity());
                } else {
                    map.put("城市", "无");
                }
                if (null != storeId && !(storeId.equals(-1L)) && null != notPickGoodsReportDOS && notPickGoodsReportDOS.size() > 0) {
                    map.put("门店", notPickGoodsReportDOS.get(0).getStoreName());
                } else {
                    map.put("门店", "无");
                }
                if (null != storeType && !("".equals(storeType)) && null != notPickGoodsReportDOS && notPickGoodsReportDOS.size() > 0) {
                    map.put("门店类型", notPickGoodsReportDOS.get(0).getStoreType());
                } else {
                    map.put("门店类型", "无");
                }
                if (null != pickType && !("".equals(pickType)) && null != notPickGoodsReportDOS && notPickGoodsReportDOS.size() > 0) {
                    map.put("未提货类型", notPickGoodsReportDOS.get(0).getPickType());
                } else {
                    map.put("未提货类型", "无");
                }
                if (null != startTime && !("".equals(startTime))) {
                    map.put("购买时间", startTime);
                } else {
                    map.put("购买时间", "无");
                }
                if (null != endTime && !("".equals(endTime))) {
                    map.put("过期时间", endTime);
                } else {
                    map.put("过期时间", "无");
                }
                //设置筛选条件
                ws = this.setCondition(ws, map, titleFormat, shiroName, textFormat);
                //列宽
                int[] columnView = {10, 13, 10, 20, 20, 10, 10, 15, 20, 10, 10, 20, 30, 10, 10, 10, 30};
                //列标题
                String[] titles = {"城市", "门店名称", "门店类型", "未提货类型", "购买日期", "过期时间", "顾客编号", "顾客姓名	",
                        "顾客电话", "顾客类型", "销顾姓名", "商品编码", "商品名称", "数量", "购买单价", "购买总价", "相关单号"};
                //计算标题开始行号
                int row = 1;
                if (null != map && map.size() > 0) {
                    row = (map.size() + 1) / 2 + 4;
                }

                int collectRow = row + 1;
//
                row += 5;
//                //设置标题
                ws = this.setHeader(ws, titleFormat, columnView, titles, row);
//
                row += 1;
                WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"), 9, WritableFont.NO_BOLD, false,
                        UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                //填写表体数据
                for (int j = 0; j < maxRowNum; j++) {
                    if (j + i * maxRowNum >= maxSize) {
                        break;
                    }
                    NotPickGoodsReportDO notPickGoodsReportDO = notPickGoodsReportDOS.get(j + i * maxRowNum);
                    ws.addCell(new Label(0, j + row, notPickGoodsReportDO.getCity(), textFormat));
                    ws.addCell(new Label(1, j + row, notPickGoodsReportDO.getStoreName(), textFormat));
                    ws.addCell(new Label(2, j + row, notPickGoodsReportDO.getStoreType(), textFormat));
                    ws.addCell(new Label(3, j + row, notPickGoodsReportDO.getPickType(), textFormat));
                    ws.addCell(new Label(4, j + row, notPickGoodsReportDO.getBuyTime(), textFormat));
                    ws.addCell(new Label(5, j + row, notPickGoodsReportDO.getEffectiveTime(), textFormat));
                    ws.addCell(new Number(6, j + row, notPickGoodsReportDO.getCustomerId()));
                    ws.addCell(new Label(7, j + row, notPickGoodsReportDO.getCustomerName(), textFormat));
                    ws.addCell(new Label(8, j + row, notPickGoodsReportDO.getCustomerPhone(), textFormat));
                    ws.addCell(new Label(9, j + row, notPickGoodsReportDO.getCustomerType(), textFormat));
                    ws.addCell(new Label(10, j + row, notPickGoodsReportDO.getSellerName(), textFormat));
                    ws.addCell(new Label(11, j + row, notPickGoodsReportDO.getSku(), textFormat));
                    ws.addCell(new Label(12, j + row, notPickGoodsReportDO.getSkuName(), textFormat));
                    ws.addCell(new Number(13, j + row, notPickGoodsReportDO.getQuantity(), textFormat));
                    ws.addCell(new Number(14, j + row, notPickGoodsReportDO.getBuyPrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(15, j + row, notPickGoodsReportDO.getTotalBuyPrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Label(16, j + row, notPickGoodsReportDO.getReferenceNumber(), textFormat));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        } finally {
            if (wwb != null) {
                try {
                    wwb.write();//刷新（或写入），生成一个excel文档
                    wwb.close();//关闭
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public WritableWorkbook exportXML(String fileurl, HttpServletResponse response)
            throws IOException {
        response.reset();
        fileurl = new String(fileurl.getBytes("GBK"), "ISO-8859-1");
        response.setHeader("Content-Disposition",
                "attachment; filename=" + fileurl);
        response.setContentType("application/octet-stream; charset=utf-8");
        OutputStream out = response.getOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(out);
        return wwb;
    }

    /**
     * @title   创建文件
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/3/24
     */
    public WritableWorkbook getWorkbook(String fileurl){
        WritableWorkbook wwb = null;
        try {
            File file=new File(fileurl);
            if (!file.exists()) {
                file.createNewFile();
            }
            //打开文件
            wwb = Workbook.createWorkbook(file);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return wwb;
    }

    /**
     * @title   添加筛选条件
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/3/24
     */
    public WritableSheet setCondition(WritableSheet ws, Map<String,String> map, WritableCellFormat titleFormat, String shiroName, WritableCellFormat textFormat){
        try {
            ws.addCell(new Label(0, 0, "筛选条件:", titleFormat));

            int row = 0;
            int column = 1;
            for (String key : map.keySet()) {
                if (column == 1) {
                    ws.addCell(new Label(1, row, key, titleFormat));
                    ws.addCell(new Label(2, row, map.get(key), textFormat));
                    column += 1;
                    continue;
                } else {
                    ws.addCell(new Label(4, row, key, titleFormat));
                    ws.addCell(new Label(5, row, map.get(key), textFormat));
                    column = 1;
                }
                row += 1;
             }
            row += 1;
            ws.addCell(new Label(3, row, "导出时间:", titleFormat));
            ws.addCell(new Label(4, row, DateUtil.getDateTimeStr(new Date()), textFormat));

            ws.addCell(new Label(6, row, "导出人:", titleFormat));
            ws.addCell(new Label(7, row, shiroName, textFormat));

        } catch (WriteException e) {
            e.printStackTrace();
        }

        return ws;
    }

    /**
     * @title   设置样式
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/3/24
     */
    public WritableCellFormat setTitleStyle(){
        try {
            WritableFont titleFont = new WritableFont(WritableFont.createFont("微软雅黑"),9,WritableFont.BOLD,false,
                    UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            WritableCellFormat titleFormat=new WritableCellFormat();
            //设置字体格式
            titleFormat.setFont(titleFont);
            //设置文本水平居中对齐
            titleFormat.setAlignment(Alignment.CENTRE);
            //设置文本垂直居中对齐
            titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            //设置背景颜色
            titleFormat.setBackground(Colour.ICE_BLUE);
            titleFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
            return titleFormat;
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public WritableCellFormat setTextStyle(){
        WritableFont titleFont = new WritableFont(WritableFont.createFont("微软雅黑"),9,WritableFont.NO_BOLD,false,
                UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
        WritableCellFormat titleText = new WritableCellFormat();
        //设置字体格式
        titleText.setFont(titleFont);

        return titleText;
    }

    /**
     * @title   添加标题
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/3/24
     */
    public WritableSheet setHeader(WritableSheet ws, WritableCellFormat titleFormat, int[] columnView, String[] titles, int row){
        try {
            //设置列宽
            if (null != columnView){
                for (int i = 0; i < columnView.length; i++) {
                    ws.setColumnView(i, columnView[i]);
                }
            }
            //设置标题
            if (null != titles) {
                for (int i = 0; i < titles.length; i++) {
                    ws.addCell(new Label(i, row, titles[i], titleFormat));
                }
            }
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return ws;
    }

}
