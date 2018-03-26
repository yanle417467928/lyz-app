package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.core.utils.DateUtil;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.reportDownload.ReceiptsReportDO;
import cn.com.leyizhuang.app.foundation.service.AdminUserStoreService;
import cn.com.leyizhuang.app.foundation.service.MaReportDownloadService;
import com.github.pagehelper.PageInfo;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
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
import java.io.*;
import java.util.*;

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

    private static final int maxRowNum = 100;

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

    @GetMapping(value = "/receipts/download")
    public String receiptsDownload(HttpServletRequest request, HttpServletResponse response, Long cityId, Long storeId, String storeType, String startTime, String endTime,
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
        //创建文件
        String fileurl = "D://收款报表-"+ DateUtil.getCurrentTimeStr("yyyyMMddHHmmss") +".xls";//如  D:/xx/xx/xxx.xls
        WritableWorkbook wwb = null;

        try {

            wwb = this.getWorkbook(fileurl);

            //excel单表最大行数是65535
            int maxSize = 0;
            if (receiptsReportDOS != null) {
                maxSize = receiptsReportDOS.size();
            }
            int sheets = maxSize / maxRowNum + 1;
            //设置excel的sheet数
            for (int i = 0; i < sheets; i++) {
                WritableCellFormat titleFormat = this.setStyle();

                //工作表，参数0表示这是第一页
                WritableSheet ws = wwb.createSheet("第" + (i + 1) + "页", i);

                //筛选条件
                Map<String, String> map = new HashMap<>();
                if (null != cityId && !(cityId.equals(-1L))){
                    map.put("城市", receiptsReportDOS.get(0).getCityName());
                } else {
                    map.put("城市", "无");
                }
                if (null != storeId && !(storeId.equals(-1L))){
                    map.put("门店", receiptsReportDOS.get(0).getStoreName());
                } else {
                    map.put("门店", "无");
                }
                if (null != storeType && !("".equals(storeType))){
                    map.put("门店类型", receiptsReportDOS.get(0).getStoreType());
                } else {
                    map.put("门店类型", "无");
                }
                if (null != payType && !("".equals(payType))){
                    map.put("支付方式", receiptsReportDOS.get(0).getPayType());
                } else {
                    map.put("支付方式", "无");
                }
                if (null != startTime && !("".equals(startTime))){
                    map.put("开始时间", startTime);
                } else {
                    map.put("开始时间", "无");
                }
                if (null != endTime && !("".equals(endTime))){
                    map.put("结束时间", endTime);
                } else {
                    map.put("结束时间", "无");
                }
                if (null != keywords && !("".equals(keywords))){
                    map.put("关键字", keywords);
                } else {
                    map.put("关键字", "无");
                }
                //设置筛选条件
                ws = this.setCondition(ws, map, titleFormat, shiroName);
                //列宽
                int[] columnView = {10, 13, 10, 20, 12, 10, 30, 10};
                //列标题
                String[] titles = {"城市", "门店名称", "门店类型", "付款/退款时间", "支付方式", "支付金额", "订/退单号", "备注"};
                //计算标题开始行号
                int row = 1;
                if (null != map && map.size() > 0){
                    row = (map.size() + 1)/2 + 2;
                }
                //设置标题
                ws = this.setHeader(ws, titleFormat, columnView, titles, row);



                row += 1;
                //填写表体数据
                for (int j = 0; j < maxRowNum; j++) {
                    if (j + i * maxRowNum >= maxSize) {
                        break;
                    }
                    ReceiptsReportDO receiptsReportDO = receiptsReportDOS.get(j + i * maxRowNum);
                    ws.addCell(new Label(0, j + row, receiptsReportDO.getCityName()));
                    ws.addCell(new Label(1, j + row, receiptsReportDO.getStoreName()));
                    ws.addCell(new Label(2, j + row, receiptsReportDO.getStoreType()));
                    ws.addCell(new Label(3, j + row, receiptsReportDO.getPayTime()));
                    ws.addCell(new Label(4, j + row, receiptsReportDO.getPayType()));
                    ws.addCell(new Number(5, j + row, receiptsReportDO.getMoney(), new WritableCellFormat(new NumberFormat("0.00"))));
                    ws.addCell(new Label(6, j + row, receiptsReportDO.getOrderNumber()));
                    ws.addCell(new Label(7, j + row, receiptsReportDO.getRemarks()));
                }
            }
            return fileurl;
        }catch(Exception e) {
            System.out.println(e);
        }finally {
            if(wwb != null) {
                try {
                    wwb.write();//刷新（或写入），生成一个excel文档
                    wwb.close();//关闭
//                        exportXML(fileurl, response);//下载excel文件
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return "";
    }


    public void exportXML(String fileName, HttpServletResponse response)
            throws IOException {
        String filename = "table";
        try {
            filename = new String(fileName.getBytes("GBK"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e1) {
            System.out.println("下载文件名格式转换错误！");
        }
        try {
            OutputStream os;
            response.reset();
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + filename +".xls");
            response.setContentType("application/octet-stream; charset=utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public WritableSheet setCondition(WritableSheet ws, Map<String,String> map, WritableCellFormat titleFormat, String shiroName){
        try {
            ws.addCell(new Label(0, 0, "筛选条件:", titleFormat));

            int row = 0;
            int column = 1;
            for (String key : map.keySet()) {
                if (column == 1) {
                    ws.addCell(new Label(1, row, key, titleFormat));
                    ws.addCell(new Label(2, row, map.get(key)));
                    column += 1;
                    continue;
                } else {
                    ws.addCell(new Label(4, row, key, titleFormat));
                    ws.addCell(new Label(5, row, map.get(key)));
                    column = 1;
                }
                row += 1;
             }
            row += 1;
            ws.addCell(new Label(3, row, "导出时间:", titleFormat));
            ws.addCell(new Label(4, row, DateUtil.getDateTimeStr(new Date())));

            ws.addCell(new Label(6, row, "导出人:", titleFormat));
            ws.addCell(new Label(7, row, shiroName));

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
    public WritableCellFormat setStyle(){
        try {
            WritableFont titleFont = new WritableFont(WritableFont.createFont("黑体"),12,WritableFont.BOLD,false,
                    UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            WritableCellFormat titleFormat=new WritableCellFormat();
            //设置字体格式
            titleFormat.setFont(titleFont);
            //设置文本水平居中对齐
            titleFormat.setAlignment(Alignment.CENTRE);
            //设置文本垂直居中对齐
            titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            return titleFormat;
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return null;
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
