package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.core.utils.DateUtil;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.management.User;
import cn.com.leyizhuang.app.foundation.pojo.reportDownload.*;
import cn.com.leyizhuang.app.foundation.service.AdminUserStoreService;
import cn.com.leyizhuang.app.foundation.service.MaDecorationCompanyCreditBillingService;
import cn.com.leyizhuang.app.foundation.service.MaReportDownloadService;
import cn.com.leyizhuang.app.foundation.service.UserService;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorationCompanyCreditBillingDetailsVO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorationCompanyCreditBillingVO;
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

import javax.annotation.Resource;
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

    @Autowired
    private MaDecorationCompanyCreditBillingService maDecorationCompanyCreditBillingService;

    @Resource
    private UserService userService;

    private static final int maxRowNum = 60000;

    /**
     * @title   订单收款报表查询
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/4/2
     */
    @GetMapping(value = "/receipts/page/grid")
    public GridDataVO<ReceiptsReportDO> restReceiptsReportDOPageGird(Integer offset, Integer size, Long cityId, Long storeId, String storeType,
                                                                    String startTime, String endTime, String payType, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
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
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        PageInfo<NotPickGoodsReportDO> notPickGoodsReportDOAll = this.maReportDownloadService.findNotPickGoodsReportDOAll(cityId, storeId, storeType, startTime,
                endTime, pickType, storeIds, page, size);
        return new GridDataVO<NotPickGoodsReportDO>().transform(notPickGoodsReportDOAll.getList(), notPickGoodsReportDOAll.getTotal());
    }

    @GetMapping(value = "/store/predeposit/page/grid")
    public GridDataVO<StorePredepositReportDO> restStorePredepositPageGird(Integer offset, Integer size, Long cityId, Long storeId, String storeType,
                                                                           String startTime, String endTime) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        PageInfo<StorePredepositReportDO> storePredepositReportDOAll = this.maReportDownloadService.findStorePredepositReportDOAll(cityId, storeId, storeType, startTime,
                endTime, storeIds, page, size);
        return new GridDataVO<StorePredepositReportDO>().transform(storePredepositReportDOAll.getList(), storePredepositReportDOAll.getTotal());
    }

    /**  
     * @title   对账商品明细报表查询
     * @descripe
     * @param 
     * @return 
     * @throws 
     * @author GenerationRoad
     * @date 2018/4/2
     */
    @GetMapping(value = "/accountGoodsItems/page/grid")
    public GridDataVO<AccountGoodsItemsDO> restAccountGoodsItemsDOPageGird(Integer offset, Integer size, Long cityId, Long storeId, String storeType,
                                                                       String startTime, String endTime, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        PageInfo<AccountGoodsItemsDO> accountGoodsItemsDOPageInfo = this.maReportDownloadService.findAccountGoodsItemsDOAll(cityId, storeId, storeType, startTime,
                endTime, keywords, storeIds, page, size);
        return new GridDataVO<AccountGoodsItemsDO>().transform(accountGoodsItemsDOPageInfo.getList(), accountGoodsItemsDOPageInfo.getTotal());
    }

    /**  
     * @title   账单明细报表查询
     * @descripe
     * @param 
     * @return 
     * @throws 
     * @author GenerationRoad
     * @date 2018/4/3
     */
    @GetMapping(value = "/billingItems/page/grid")
    public GridDataVO<BillingItemsDO> restBillingItemsDOPageGird(Integer offset, Integer size, Long cityId, Long storeId, String storeType,
                                                                 String startTime, String endTime, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        PageInfo<BillingItemsDO> billingItemsDOPageInfo = this.maReportDownloadService.findBillingItemsDOAll(cityId, storeId, storeType, startTime,
                endTime, keywords, storeIds, page, size);
        return new GridDataVO<BillingItemsDO>().transform(billingItemsDOPageInfo.getList(), billingItemsDOPageInfo.getTotal());
    }
    
    /**  
     * @title   配送员代收款报表查询
     * @descripe
     * @param
     * @return 
     * @throws 
     * @author GenerationRoad
     * @date 2018/4/3
     */
    @GetMapping(value = "/agencyFund/page/grid")
    public GridDataVO<AgencyFundDO> restAgencyFundDOPageGird(Integer offset, Integer size, Long cityId, Long storeId, String storeType,
                                                                 String startTime, String endTime, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getNotZsType());
        PageInfo<AgencyFundDO> agencyFundDOPageInfo = this.maReportDownloadService.findAgencyFundDOAll(cityId, storeId, storeType, startTime,
                endTime, keywords, storeIds, page, size);
        return new GridDataVO<AgencyFundDO>().transform(agencyFundDOPageInfo.getList(), agencyFundDOPageInfo.getTotal());
    }

    /**
     * @title   订单收款报表下载
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/4/2
     */
    @GetMapping(value = "/receipts/download")
    public void downloadReceipts(HttpServletRequest request, HttpServletResponse response, Long cityId, Long storeId, String storeType, String startTime, String endTime,
                                 String payType, String keywords) {
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        List<ReceiptsReportDO> receiptsReportDOS = this.maReportDownloadService.downloadReceipts(cityId, storeId, storeType, startTime,
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
                int[] columnView = {10, 13, 10, 20, 12, 10, 30, 15, 15, 15, 25};
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
                ws.addCell(new Label(8, row, "配送POS", titleFormat));
                ws.addCell(new Label(9, row, "配送现金", titleFormat));
                ws.addCell(new Label(10, row, "支付总额", titleFormat));

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
                Double deliveryPos = 0D;
                Double deliveryCash = 0D;
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
                    if ("DELIVERY_CLERK".equals(receiptsReportDO.getPaymentSubjectType())) {
                        if ("CASH".equals(receiptsReportDO.getPayTypes())){
                            ws.addCell(new Label(4, j + row, "配送现金", textFormat));
                        }
                        if ("POS".equals(receiptsReportDO.getPayTypes())){
                            ws.addCell(new Label(4, j + row, "配送POS", textFormat));
                        }
                    }
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
                        if ("DELIVERY_CLERK".equals(receiptsReportDO.getPaymentSubjectType())) {
                            deliveryPos = CountUtil.add(deliveryPos, null==receiptsReportDO.getMoney()?0D:receiptsReportDO.getMoney());
                        } else {
                            pos = CountUtil.add(pos, null==receiptsReportDO.getMoney()?0D:receiptsReportDO.getMoney());
                        }

                    } else if ("CASH".equals(receiptsReportDO.getPayTypes())){
                        if ("DELIVERY_CLERK".equals(receiptsReportDO.getPaymentSubjectType())) {
                            deliveryCash = CountUtil.add(deliveryCash, null == receiptsReportDO.getMoney()?0D : receiptsReportDO.getMoney());
                        } else {
                            cash = CountUtil.add(cash, null == receiptsReportDO.getMoney()?0D : receiptsReportDO.getMoney());
                        }
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
                ws.addCell(new Number(6, collectRow, stPrepay, new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                ws.addCell(new Number(7, collectRow, cusPrepay, new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                ws.addCell(new Number(8, collectRow, deliveryPos, new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                ws.addCell(new Number(9, collectRow, deliveryCash, new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                ws.addCell(new Number(10, collectRow, totle, new WritableCellFormat(textFont, new NumberFormat("0.00"))));
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
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
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

    @GetMapping(value = "/store/predeposit/download")
    public void storePredepositDownload(HttpServletRequest request, HttpServletResponse response, Long cityId, Long storeId, String storeType, String startTime, String endTime) {
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        List<StorePredepositReportDO> storePredepositReportDOS = this.maReportDownloadService.storePredepositDownload(cityId, storeId, storeType, startTime, endTime, storeIds);
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String shiroName = "";
        if (null != shiroUser) {
            shiroName = shiroUser.getName();
        }

        response.setContentType("text/html;charset=UTF-8");
        //创建名称
        String fileurl = "门店预存款-" + DateUtil.getCurrentTimeStr("yyyyMMddHHmmss") + ".xls";//如  D:/xx/xx/xxx.xls

        WritableWorkbook wwb = null;
        try {
            //创建文件
            wwb = exportXML(fileurl, response);

            //excel单表最大行数是65535
            int maxSize = 0;
            if (storePredepositReportDOS != null) {
                maxSize = storePredepositReportDOS.size();
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
                if (null != cityId && !(cityId.equals(-1L)) && null != storePredepositReportDOS && storePredepositReportDOS.size() > 0) {
                    map.put("城市", storePredepositReportDOS.get(0).getCityName());
                } else {
                    map.put("城市", "无");
                }
                if (null != storeId && !(storeId.equals(-1L)) && null != storePredepositReportDOS && storePredepositReportDOS.size() > 0) {
                    map.put("门店", storePredepositReportDOS.get(0).getStoreName());
                } else {
                    map.put("门店", "无");
                }
                if (null != storeType && !("".equals(storeType)) && null != storePredepositReportDOS && storePredepositReportDOS.size() > 0) {
                    map.put("门店类型", storePredepositReportDOS.get(0).getStoreType());
                } else {
                    map.put("门店类型", "无");
                }
                if (null != startTime && !("".equals(startTime))) {
                    map.put("开始时间", startTime);
                } else {
                    map.put("开始时间", "无");
                }
                if (null != endTime && !("".equals(endTime))) {
                    map.put("结束时间", endTime);
                } else {
                    map.put("结束时间", "无");
                }
                //设置筛选条件
                ws = this.setCondition(ws, map, titleFormat, shiroName, textFormat);
                //列宽
                int[] columnView = {10, 20, 13, 20, 10, 10, 15, 20, 20};
                //列标题
                String[] titles = {"城市", "门店名称", "门店类型", "变动类型", "变动金额", "变更后总金额", "变更时间", "相关单号", "备注"};
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
                    StorePredepositReportDO storePredepositReportDO = storePredepositReportDOS.get(j + i * maxRowNum);
                    ws.addCell(new Label(0, j + row, storePredepositReportDO.getCityName(), textFormat));
                    ws.addCell(new Label(1, j + row, storePredepositReportDO.getStoreName(), textFormat));
                    ws.addCell(new Label(2, j + row, storePredepositReportDO.getStoreType(), textFormat));
                    ws.addCell(new Label(3, j + row, storePredepositReportDO.getChangeType(), textFormat));
                    ws.addCell(new Number(4, j + row, storePredepositReportDO.getChangeMoney(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(5, j + row, storePredepositReportDO.getBalance(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Label(6, j + row, storePredepositReportDO.getChangeTime(), textFormat));
                    ws.addCell(new Label(7, j + row, storePredepositReportDO.getReferenceNumber(), textFormat));
                    ws.addCell(new Label(8, j + row, storePredepositReportDO.getRemarks(), textFormat));
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


    /**
     * @title   对账商品明细报表下载
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/4/2
     */
    @GetMapping(value = "/accountGoodsItems/download")
    public void downloadAccountGoodsItems(HttpServletRequest request, HttpServletResponse response, Long cityId, Long storeId, String storeType,
                                          String startTime, String endTime, String keywords) {
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        List<AccountGoodsItemsDO> accountGoodsItemsDOList = this.maReportDownloadService.downloadAccountGoodsItems(cityId, storeId, storeType, startTime,
                endTime, keywords, storeIds);
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String shiroName = "";
        if (null != shiroUser) {
            shiroName = shiroUser.getName();
        }

        response.setContentType("text/html;charset=UTF-8");
        //创建名称
        String fileurl = "对账商品明细报表-"+ DateUtil.getCurrentTimeStr("yyyyMMddHHmmss") +".xls";//如  D:/xx/xx/xxx.xls

        WritableWorkbook wwb = null;
        try {
            //创建文件
            wwb = exportXML(fileurl, response);

            //excel单表最大行数是65535
            int maxSize = 0;
            if (accountGoodsItemsDOList != null) {
                maxSize = accountGoodsItemsDOList.size();
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
                if (null != cityId && !(cityId.equals(-1L)) && null != accountGoodsItemsDOList && accountGoodsItemsDOList.size() > 0){
                    map.put("城市", accountGoodsItemsDOList.get(0).getCityName());
                } else {
                    map.put("城市", "无");
                }
                if (null != storeId && !(storeId.equals(-1L)) && null != accountGoodsItemsDOList && accountGoodsItemsDOList.size() > 0){
                    map.put("门店", accountGoodsItemsDOList.get(0).getStoreName());
                } else {
                    map.put("门店", "无");
                }
                if (null != storeType && !("".equals(storeType)) && null != accountGoodsItemsDOList && accountGoodsItemsDOList.size() > 0){
                    map.put("门店类型", accountGoodsItemsDOList.get(0).getStoreType());
                } else {
                    map.put("门店类型", "无");
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
                int[] columnView = {10, 13, 10, 20, 30, 15, 15, 10, 10, 50, 20, 20, 10, 10, 10, 15, 15, 15, 15, 15, 15};
                //列标题
                String[] titles = {"城市", "门店名称", "门店类型", "下单/退单时间", "单号", "顾客", "导购姓名", "配送/退货方式",
                        "出/退货状态", "送/退货地址", "产品编码", "产品名称", "产品标识", "产品类型", "数量", "结算单价", "结算总价",
                        "成交单价", "成交总价", "单个产品经销差价", "总经销差价"};
                //计算标题开始行号
                int row = 1;
                if (null != map && map.size() > 0){
                    row = (map.size() + 1)/2 + 4;
                }

                //设置标题
                ws = this.setHeader(ws, titleFormat, columnView, titles, row);
                row += 1;
                WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"),9,WritableFont.NO_BOLD,false,
                        UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                //填写表体数据
                for (int j = 0; j < maxRowNum; j++) {
                    if (j + i * maxRowNum >= maxSize) {
                        break;
                    }
                    AccountGoodsItemsDO accountGoodsItemsDO = accountGoodsItemsDOList.get(j + i * maxRowNum);
                    ws.addCell(new Label(0, j + row, accountGoodsItemsDO.getCityName(), textFormat));
                    ws.addCell(new Label(1, j + row, accountGoodsItemsDO.getStoreName(), textFormat));
                    ws.addCell(new Label(2, j + row, accountGoodsItemsDO.getStoreType(), textFormat));
                    ws.addCell(new Label(3, j + row, accountGoodsItemsDO.getOrderTime(), textFormat));
                    ws.addCell(new Label(4, j + row, accountGoodsItemsDO.getOrderNumber(), textFormat));
                    ws.addCell(new Label(5, j + row, accountGoodsItemsDO.getCustomerName(), textFormat));
                    ws.addCell(new Label(6, j + row, accountGoodsItemsDO.getSellerName(), textFormat));
                    ws.addCell(new Label(7, j + row, accountGoodsItemsDO.getDeliveryType(), textFormat));
                    ws.addCell(new Label(8, j + row, accountGoodsItemsDO.getDeliveryStatus(), textFormat));
                    ws.addCell(new Label(9, j + row, accountGoodsItemsDO.getShippingAddress(), textFormat));
                    ws.addCell(new Label(10, j + row, accountGoodsItemsDO.getSku(), textFormat));
                    ws.addCell(new Label(11, j + row, accountGoodsItemsDO.getSkuName(), textFormat));
                    ws.addCell(new Label(12, j + row, accountGoodsItemsDO.getCompanyFlag(), textFormat));
                    ws.addCell(new Label(13, j + row, accountGoodsItemsDO.getGoodsLineType(), textFormat));
                    ws.addCell(new Label(14, j + row, accountGoodsItemsDO.getQuantity().toString(), textFormat));
                    ws.addCell(new Number(15, j + row, accountGoodsItemsDO.getSettlementPrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(16, j + row, accountGoodsItemsDO.getSettlementTotlePrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(17, j + row, accountGoodsItemsDO.getPromotionSharePrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(18, j + row, accountGoodsItemsDO.getPromotionShareTotlePrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(19, j + row, accountGoodsItemsDO.getWholesalePrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(20, j + row, accountGoodsItemsDO.getWholesaleTotlePrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));

                }
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
     * @title   账单明细报表下载
     * @descripe
     * @param 
     * @return 
     * @throws 
     * @author GenerationRoad
     * @date 2018/4/3
     */
    @GetMapping(value = "/billingItems/download")
    public void downloadBillingItems(HttpServletRequest request, HttpServletResponse response, Long cityId, Long storeId, String storeType,
                                          String startTime, String endTime, String keywords) {
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        List<BillingItemsDO> billingItemsDOList = this.maReportDownloadService.downloadBillingItems(cityId, storeId, storeType, startTime,
                endTime, keywords, storeIds);
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String shiroName = "";
        if (null != shiroUser) {
            shiroName = shiroUser.getName();
        }

        response.setContentType("text/html;charset=UTF-8");
        //创建名称
        String fileurl = "账单明细报表-"+ DateUtil.getCurrentTimeStr("yyyyMMddHHmmss") +".xls";//如  D:/xx/xx/xxx.xls

        WritableWorkbook wwb = null;
        try {
            //创建文件
            wwb = exportXML(fileurl, response);

            //excel单表最大行数是65535
            int maxSize = 0;
            if (billingItemsDOList != null) {
                maxSize = billingItemsDOList.size();
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
                if (null != cityId && !(cityId.equals(-1L)) && null != billingItemsDOList && billingItemsDOList.size() > 0){
                    map.put("城市", billingItemsDOList.get(0).getCityName());
                } else {
                    map.put("城市", "无");
                }
                if (null != storeId && !(storeId.equals(-1L)) && null != billingItemsDOList && billingItemsDOList.size() > 0){
                    map.put("门店", billingItemsDOList.get(0).getStoreName());
                } else {
                    map.put("门店", "无");
                }
                if (null != storeType && !("".equals(storeType)) && null != billingItemsDOList && billingItemsDOList.size() > 0){
                    map.put("门店类型", billingItemsDOList.get(0).getStoreType());
                } else {
                    map.put("门店类型", "无");
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
                int[] columnView = {10, 13, 10, 20, 30, 15, 15, 10, 10, 50, 15, 15, 15, 10, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15};
                //列标题
                String[] titles = {"城市", "门店名称", "门店类型", "下单/退单时间", "单号", "顾客", "导购姓名", "配送/退货方式",
                        "出/退货状态", "送/退货地址", "商品总额", "会员折扣", "订单折扣", "配送费", "优惠券折扣", "产品券折扣", "应付总额",
                        "微信", "支付宝", "银联", "门店现金", "门店POS", "配送现金", "配送POS", "其他", "门店预存款", "顾客预存款", "支付总额"};
                //计算标题开始行号
                int row = 1;
                if (null != map && map.size() > 0){
                    row = (map.size() + 1)/2 + 4;
                }

                //设置标题
                ws = this.setHeader(ws, titleFormat, columnView, titles, row);
                row += 1;
                WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"),9,WritableFont.NO_BOLD,false,
                        UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                //填写表体数据
                for (int j = 0; j < maxRowNum; j++) {
                    if (j + i * maxRowNum >= maxSize) {
                        break;
                    }
                    BillingItemsDO billingItemsDO = billingItemsDOList.get(j + i * maxRowNum);
                    ws.addCell(new Label(0, j + row, billingItemsDO.getCityName(), textFormat));
                    ws.addCell(new Label(1, j + row, billingItemsDO.getStoreName(), textFormat));
                    ws.addCell(new Label(2, j + row, billingItemsDO.getStoreType(), textFormat));
                    ws.addCell(new Label(3, j + row, billingItemsDO.getOrderTime(), textFormat));
                    ws.addCell(new Label(4, j + row, billingItemsDO.getOrderNumber(), textFormat));
                    ws.addCell(new Label(5, j + row, billingItemsDO.getCustomerName(), textFormat));
                    ws.addCell(new Label(6, j + row, billingItemsDO.getSellerName(), textFormat));
                    ws.addCell(new Label(7, j + row, billingItemsDO.getDeliveryType(), textFormat));
                    ws.addCell(new Label(8, j + row, billingItemsDO.getDeliveryStatus(), textFormat));
                    ws.addCell(new Label(9, j + row, billingItemsDO.getShippingAddress(), textFormat));
                    ws.addCell(new Number(10, j + row, billingItemsDO.getTotalGoodsPrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(11, j + row, billingItemsDO.getMemberDiscount(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(12, j + row, billingItemsDO.getPromotionDiscount(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(13, j + row, billingItemsDO.getFreight(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(14, j + row, billingItemsDO.getCashCouponDiscount(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(15, j + row, billingItemsDO.getProductCouponDiscount(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(16, j + row, billingItemsDO.getAmountPayable(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(17, j + row, billingItemsDO.getWeChat(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(18, j + row, billingItemsDO.getAlipay(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(19, j + row, billingItemsDO.getUnionPay(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(20, j + row, billingItemsDO.getStoreCash(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(21, j + row, billingItemsDO.getStorePosMoney(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(22, j + row, billingItemsDO.getDeliveryCash(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(23, j + row, billingItemsDO.getDeliveryPos(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(24, j + row, billingItemsDO.getStoreOtherMoney(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(25, j + row, billingItemsDO.getStPreDeposit(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(26, j + row, billingItemsDO.getCusPreDeposit(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(27, j + row, billingItemsDO.getTotalPay(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));

                }
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
     * @title   配送员代收款报表下载
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/4/3
     */
    @GetMapping(value = "/agencyFund/download")
    public void downloadAgencyFund(HttpServletRequest request, HttpServletResponse response, Long cityId, Long storeId, String storeType,
                                     String startTime, String endTime, String keywords) {
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getNotZsType());
        List<AgencyFundDO> agencyFundDOList = this.maReportDownloadService.downloadAgencyFund(cityId, storeId, storeType, startTime,
                endTime, keywords, storeIds);
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String shiroName = "";
        if (null != shiroUser) {
            shiroName = shiroUser.getName();
        }

        response.setContentType("text/html;charset=UTF-8");
        //创建名称
        String fileurl = "配送员代收款报表-"+ DateUtil.getCurrentTimeStr("yyyyMMddHHmmss") +".xls";//如  D:/xx/xx/xxx.xls

        WritableWorkbook wwb = null;
        try {
            //创建文件
            wwb = exportXML(fileurl, response);

            //excel单表最大行数是65535
            int maxSize = 0;
            if (agencyFundDOList != null) {
                maxSize = agencyFundDOList.size();
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
                if (null != cityId && !(cityId.equals(-1L)) && null != agencyFundDOList && agencyFundDOList.size() > 0){
                    map.put("城市", agencyFundDOList.get(0).getCityName());
                } else {
                    map.put("城市", "无");
                }
                if (null != storeId && !(storeId.equals(-1L)) && null != agencyFundDOList && agencyFundDOList.size() > 0){
                    map.put("门店", agencyFundDOList.get(0).getStoreName());
                } else {
                    map.put("门店", "无");
                }
                if (null != storeType && !("".equals(storeType)) && null != agencyFundDOList && agencyFundDOList.size() > 0){
                    map.put("门店类型", agencyFundDOList.get(0).getStoreType());
                } else {
                    map.put("门店类型", "无");
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
                int[] columnView = {10, 13, 13, 10, 20, 30, 50, 10, 10, 15, 15, 15, 25, 15, 15, 25};
                //列标题
                String[] titles = {"城市", "仓库", "门店名称", "门店类型", "封车出库时间", "单号", "收货地址", "导购姓名", "配送员姓名",
                        "订单代收金额", "配送员实际收款现金", "配送员实际收款POS", "配送员备注", "应退门店", "仓库应存回公司货款", "订单备注"};
                //计算标题开始行号
                int row = 1;
                if (null != map && map.size() > 0){
                    row = (map.size() + 1)/2 + 4;
                }

                //设置标题
                ws = this.setHeader(ws, titleFormat, columnView, titles, row);
                row += 1;
                WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"),9,WritableFont.NO_BOLD,false,
                        UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                //填写表体数据
                for (int j = 0; j < maxRowNum; j++) {
                    if (j + i * maxRowNum >= maxSize) {
                        break;
                    }
                    AgencyFundDO agencyFundDO = agencyFundDOList.get(j + i * maxRowNum);
                    ws.addCell(new Label(0, j + row, agencyFundDO.getCityName(), textFormat));
                    ws.addCell(new Label(1, j + row, agencyFundDO.getWarehouse(), textFormat));
                    ws.addCell(new Label(2, j + row, agencyFundDO.getStoreName(), textFormat));
                    ws.addCell(new Label(3, j + row, agencyFundDO.getStoreType(), textFormat));
                    ws.addCell(new Label(4, j + row, agencyFundDO.getOrderTime(), textFormat));
                    ws.addCell(new Label(5, j + row, agencyFundDO.getOrderNumber(), textFormat));
                    ws.addCell(new Label(6, j + row, agencyFundDO.getShippingAddress(), textFormat));
                    ws.addCell(new Label(7, j + row, agencyFundDO.getSellerName(), textFormat));
                    ws.addCell(new Label(8, j + row, agencyFundDO.getDeliveryName(), textFormat));
                    ws.addCell(new Number(9, j + row, agencyFundDO.getAgencyMoney(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(10, j + row, agencyFundDO.getCashMoney(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(11, j + row, agencyFundDO.getPosMoney(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Label(12, j + row, agencyFundDO.getRemarks(), textFormat));
                    ws.addCell(new Number(13, j + row, agencyFundDO.getReturnMoney(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(14, j + row, agencyFundDO.getRealMoney(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Label(15, j + row, agencyFundDO.getOrderRemark(), textFormat));
                }
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
     * @title   对账商品明细报表下载
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/4/2
     */
    @GetMapping(value = "/storeInventory/download")
    public void downloadStoreInventory(HttpServletRequest request, HttpServletResponse response, Long storeId){
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        List<StoreInventory> storeInventoryList = this.maReportDownloadService.downloadStoreInventorys(storeId,storeIds);
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String shiroName = "";
        if (null != shiroUser) {
            shiroName = shiroUser.getName();
        }

        response.setContentType("text/html;charset=UTF-8");
        //创建名称
        String fileurl = "门店库存明细报表-"+ DateUtil.getCurrentTimeStr("yyyyMMddHHmmss") +".xls"; //如  D:/xx/xx/xxx.xls

        WritableWorkbook wwb = null;
        try {
            //创建文件
            wwb = exportXML(fileurl, response);

            //excel单表最大行数是65535
            int maxSize = 0;
            if (storeInventoryList != null) {
                maxSize = storeInventoryList.size();
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

                if (null != storeId && !(storeId.equals(-1L)) && null != storeInventoryList && storeInventoryList.size() > 0){
                    map.put("门店", storeInventoryList.get(0).getStoreName());
                } else {
                    map.put("门店", "无");
                }
                //设置筛选条件
                ws = this.setCondition(ws, map, titleFormat, shiroName, textFormat);
                //列宽
                int[] columnView = {10, 40, 20, 20, 30, 15, 15};
                //列标题
                String[] titles = {"门店名称","商品名称","商品编码","真实库存","可售库存"};
                //计算标题开始行号
                int row = 1;
                if (null != map && map.size() > 0){
                    row = (map.size() + 1)/2 + 4;
                }

                //设置标题
                ws = this.setHeader(ws, titleFormat, columnView, titles, row);
                row += 1;
                WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"),9,WritableFont.NO_BOLD,false,
                        UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                //填写表体数据
                for (int j = 0; j < maxRowNum; j++) {
                    if (j + i * maxRowNum >= maxSize) {
                        break;
                    }
                    StoreInventory storeInventory = storeInventoryList.get(j + i * maxRowNum);
                    ws.addCell(new Label(0, j + row, storeInventory.getStoreName(), textFormat));
                    ws.addCell(new Label(1, j + row, storeInventory.getSkuName(), textFormat));
                    ws.addCell(new Label(2, j + row, storeInventory.getSku(), textFormat));
                    ws.addCell(new Label(3, j + row, storeInventory.getRealIty().toString(), textFormat));
                    ws.addCell(new Label(4, j + row, storeInventory.getAvailableIty().toString(), textFormat));
                }
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
     * @title   装饰公司对账单下载
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/4/19
     */
    @GetMapping(value = "/creditBilling/download")
    public void downloadCompanyCreditBilling(HttpServletRequest request, HttpServletResponse response, Long id) {
        DecorationCompanyCreditBillingVO creditBillingVO = this.maDecorationCompanyCreditBillingService.getDecorationCompanyCreditBillingById(id);
        List<DecorationCompanyCreditBillingDetailsVO> creditBillingDetailsVOS = null;
        if (null != creditBillingVO) {
            creditBillingDetailsVOS = this.maDecorationCompanyCreditBillingService.getDecorationCompanyCreditBillingDetailsByCreditBillingNo(creditBillingVO.getCreditBillingNo());
        }
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String shiroName = "";
        if (null != shiroUser) {
            shiroName = shiroUser.getName();
        }

        response.setContentType("text/html;charset=UTF-8");
        //创建名称
        String fileurl = "装饰公司对账单-"+ DateUtil.getCurrentTimeStr("yyyyMMddHHmmss") +".xls";//如  D:/xx/xx/xxx.xls

        WritableWorkbook wwb = null;
        try {
            //创建文件
            wwb = exportXML(fileurl, response);

            //excel单表最大行数是65535
            int maxSize = 0;
            if (creditBillingDetailsVOS != null) {
                maxSize = creditBillingDetailsVOS.size();
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
                User user = userService.queryById(id);
                ws.mergeCells(0,0,6,0);
                ws.addCell(new Label(0, 0, creditBillingVO.getBillName(), titleFormat));
                ws.addCell(new Label(0, 1, "账单编号", textFormat));
                ws.addCell(new Label(1, 1, creditBillingVO.getCreditBillingNo(), textFormat));
                ws.addCell(new Label(3, 1, "制单时间", textFormat));
                ws.addCell(new Label(4, 1, creditBillingVO.getCreateTime(), textFormat));
                ws.addCell(new Label(6, 1, "制单人", textFormat));
                ws.addCell(new Label(7, 1, user.getName(), textFormat));

                //列宽
                int[] columnView = {10, 30, 15, 15, 50, 10, 15};
                //列标题
                String[] titles = {"序号", "订单号", "出&退货日期", "收货人姓名", "收货人地址", "商品总数", "总金额"};
                //计算标题开始行号
                int row = 3;

                //设置标题
                ws = this.setHeader(ws, titleFormat, columnView, titles, row);
                row += 1;
                WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"),9,WritableFont.NO_BOLD,false,
                        UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                Double credit = 0D;
                Integer goodsQty = 0;
                Integer rows = row;
                //填写表体数据
                for (int j = 0; j < maxRowNum; j++) {
                    if (j + i * maxRowNum >= maxSize) {
                        break;
                    }
                    DecorationCompanyCreditBillingDetailsVO creditBillingDetailsVO = creditBillingDetailsVOS.get(j + i * maxRowNum);
                    ws.addCell(new Label(0, j + row, (j + 1) + "", textFormat));
                    ws.addCell(new Label(1, j + row, creditBillingDetailsVO.getOrderNumber(), textFormat));
                    ws.addCell(new Label(2, j + row, creditBillingDetailsVO.getCreateTime(), textFormat));
                    ws.addCell(new Label(3, j + row, creditBillingDetailsVO.getReceiver(), textFormat));
                    ws.addCell(new Label(4, j + row, creditBillingDetailsVO.getDeliveryAddress(), textFormat));
                    ws.addCell(new Number(5, j + row, creditBillingDetailsVO.getGoodsQty()));
                    ws.addCell(new Number(6, j + row, creditBillingDetailsVO.getCreditMoney(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    credit = CountUtil.add(credit, null==creditBillingDetailsVO.getCreditMoney()?0D:creditBillingDetailsVO.getCreditMoney());
                    goodsQty += null==creditBillingDetailsVO.getGoodsQty()?0:creditBillingDetailsVO.getGoodsQty();
                    rows += 1;
                }
                ws.addCell(new Label(4, rows, "总计", titleFormat));
                ws.addCell(new Number(5, rows, goodsQty, textFormat));
                ws.addCell(new Number(6, rows, credit, new WritableCellFormat(textFont, new NumberFormat("0.00"))));
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
