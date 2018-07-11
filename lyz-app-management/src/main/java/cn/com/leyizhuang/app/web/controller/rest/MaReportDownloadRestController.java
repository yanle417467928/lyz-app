package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.core.utils.DateUtil;
import cn.com.leyizhuang.app.core.utils.DateUtils;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.CusExpiringSoonProductCouponInfo;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.management.User;
import cn.com.leyizhuang.app.foundation.pojo.reportDownload.*;
import cn.com.leyizhuang.app.foundation.service.*;
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
import java.lang.Boolean;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author GenerationRoad
 * @date 2018/3/22
 */
@RestController
@RequestMapping(value = MaReportDownloadRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaReportDownloadRestController extends BaseRestController {

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

    @Autowired
    private MaStoreService maStoreService;

    @Autowired
    private MaEmployeeService maEmployeeService;

    private static final int maxRowNum = 60000;

    /**
     * @param
     * @return
     * @throws
     * @title 订单收款报表查询
     * @descripe
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
     * 导购信用额度变更记录
     *
     * @param offset
     * @param size
     * @param cityId
     * @param storeId
     * @param storeType
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping(value = "/employee/creditMoney/page/grid")
    public GridDataVO<EmpCreditMoneyChangeReportDO> restEmployeeCreditMoneyPageGird(Integer offset, Integer size, Long cityId, Long storeId, String storeType,
                                                                                    String startTime, String endTime) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        PageInfo<EmpCreditMoneyChangeReportDO> creditMoneyChangeReportDOPageInfo = this.maReportDownloadService.findEmployeeCreditMoneyReportDOAll(cityId, storeId, storeType, startTime,
                endTime, storeIds, page, size);
        return new GridDataVO<EmpCreditMoneyChangeReportDO>().transform(creditMoneyChangeReportDOPageInfo.getList(), creditMoneyChangeReportDOPageInfo.getTotal());
    }

    /**
     * @param
     * @return
     * @throws
     * @title 订单明细报表查询
     * @descripe
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
     * @param
     * @return
     * @throws
     * @title 订单汇总明细报表查询
     * @descripe
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
     * @param
     * @return
     * @throws
     * @title 配送员代收款报表查询
     * @descripe
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
     * @param
     * @return
     * @throws
     * @title 商品要退货报表
     * @descripe
     * @author GenerationRoad
     * @date 2018/4/3
     */
    @GetMapping(value = "/goodsShipmentAndReturn/page/grid")
    public GridDataVO<ShipmentAndReturnGoods> restGoodsShipmentAndReturnPageGird(Integer offset, Integer size, Long cityId, Long storeId, String storeType,
                                                                                 String startTime, String endTime, String keywords) {
        if (StringUtils.isBlank(startTime)) {
            startTime = "2018-04-01 00:00:00";
        }
        size = getSize(size);
        Integer page = getPage(offset, size);
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        PageInfo<ShipmentAndReturnGoods> shipmentAndReturnGoodsList = this.maReportDownloadService.findGoodsShipmentAndReturnOrder(cityId, storeId, storeType, startTime,
                endTime, keywords, storeIds, page, size);
        logger.info("查询商品要退货成功");

        return new GridDataVO<ShipmentAndReturnGoods>().transform(shipmentAndReturnGoodsList.getList(), shipmentAndReturnGoodsList.getTotal());
    }

    @GetMapping(value = "/empcredit/page/grid")
    public GridDataVO<EmpCreditDO> restEmpCreditDOPageGrid(Integer offset, Integer size, Long cityId, Long storeId, String storeType,String keywords){
        size = getSize(size);
        Integer page = getPage(offset, size);
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        PageInfo<EmpCreditDO> pageInfo =maReportDownloadService.empCreditMoneySituationPage(cityId,storeType,storeIds,page,size,keywords);
        return new GridDataVO<EmpCreditDO>().transform(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * @param
     * @return
     * @throws
     * @title 每月销量报表
     * @descripe
     * @author GenerationRoad
     * @date 2018/4/3
     */
    @GetMapping(value = "/salesReport/page/grid")
    public GridDataVO<SalesReportDO> restSalesReportPageGird(Integer offset, Integer size, String companyCode, String storeType,
                                                             String startTime, String endTime, Boolean isProductCoupon, String productType, Long storeId) {
        if (StringUtils.isBlank(startTime)) {
            startTime = "2017-10-01 00:00:00";
        }
        if (StringUtils.isBlank(endTime)) {
            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
            endTime = format.format(now);
        }

        size = getSize(size);

        Integer page = getPage(offset, size);

        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        if ("ALL".equals(companyCode)) {
            // 不限分公司
        } else {
            if("JZSYBM".equals(companyCode)){
                companyCode="|JZC001|";
            }
            List<Long> storeIdInCompany = maStoreService.findStoresIdByStructureCode(companyCode);
            storeIds.retainAll(storeIdInCompany);
        }
        if (null != storeId && -1L != storeId) {
            storeIds.clear();
            storeIds.add(storeId);
        }
        PageInfo<SalesReportDO> SalesList = this.maReportDownloadService.findSalesList(companyCode, storeType, startTime,
                endTime, isProductCoupon, storeIds, productType, storeId, page, size);
        return new GridDataVO<SalesReportDO>().transform(SalesList.getList(), SalesList.getTotal());
    }


    /**
     * @param
     * @return
     * @throws
     * @title 欠款报表
     * @descripe
     * @author GenerationRoad
     * @date 2018/4/3
     */
    @GetMapping(value = "/arrearsReport/page/grid")
    public GridDataVO<ArrearsReportDO> restArrearsReportPageGird(Integer offset, Integer size, String companyCode, String storeType, Long storeId) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getNotZsType());
        List<Long> storeIdInCompany = maStoreService.findStoresIdByStructureCodeAndStoreType(companyCode, storeType);
        storeIds.retainAll(storeIdInCompany);
        if (null != storeId && -1L != storeId) {
            storeIds.clear();
            storeIds.add(storeId);
        }
        //查询关联小型装饰公司
        List<Long> fitId = maStoreService.findFitCompanyIdByStoreId(storeIds);
        storeIds.addAll(fitId);
        PageInfo<ArrearsReportDO> salesList = this.maReportDownloadService.findArrearsList(companyCode, storeType
                , storeIds, storeId, page, size);
        return new GridDataVO<ArrearsReportDO>().transform(salesList.getList(), salesList.getTotal());
    }

    @GetMapping(value = "/accountZGGoodsItems/page/grid")
    public GridDataVO<AccountGoodsItemsDO> restAccountZGGoodsItemsDOPageGird(Integer offset, Integer size, Long cityId, Long storeId,
                                                                             String startTime, String endTime, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        PageInfo<AccountGoodsItemsDO> accountGoodsItemsDOPageInfo = this.maReportDownloadService.findAccountZGGoodsItemsDOAll(cityId, storeId, startTime,
                endTime, keywords, storeIds, page, size);
        return new GridDataVO<AccountGoodsItemsDO>().transform(accountGoodsItemsDOPageInfo.getList(), accountGoodsItemsDOPageInfo.getTotal());
    }


    @GetMapping(value = "/distribution/page/grid")
    public GridDataVO<DistributionDO> restDistributionPageGird(Integer offset, Integer size, Long cityId, String wareHouseNo, String deliveryClerkNo,
                                                               String startTime, String endTime, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        //查询登录用户门店权限的门店ID
//        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        PageInfo<DistributionDO> distributionDOPageInfo = this.maReportDownloadService.findDistributionDOAll(cityId, wareHouseNo, deliveryClerkNo, startTime,
                endTime, keywords, page, size);
        return new GridDataVO<DistributionDO>().transform(distributionDOPageInfo.getList(), distributionDOPageInfo.getTotal());
    }


    @GetMapping(value = "/expiringSoonProduct/page/grid")
    public GridDataVO<CusExpiringSoonProductCouponInfo> restExpiringSoonProductPageGird(Integer offset, Integer size, Long cityId, Long storeId, String storeType, String cusName) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        if(null !=storeId && -1L !=storeId){
            storeIds.clear();
            storeIds.add(storeId);
        }
        PageInfo<CusExpiringSoonProductCouponInfo> expiringSoonProductPageInfo = this.maReportDownloadService.findExpiringSoonProductAll(cityId, storeId, storeType, cusName, page, size,storeIds);
        return new GridDataVO<CusExpiringSoonProductCouponInfo>().transform(expiringSoonProductPageInfo.getList(), expiringSoonProductPageInfo.getTotal());
    }



    /**
     * @param
     * @return
     * @throws
     * @title 订单收款报表下载
     * @descripe
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
        String fileurl = "收款报表-" + DateUtils.getCurrentTimeStr("yyyyMMddHHmmss") + ".xls";//如  D:/xx/xx/xxx.xls

        WritableWorkbook wwb = null;
        try {
            //创建文件
            wwb = exportXML(fileurl, response);

            //excel单表最大行数是65535
            int maxSize = 0;
            if (null != receiptsReportDOS) {
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
                if (null != cityId && !(cityId.equals(-1L)) && null != receiptsReportDOS && receiptsReportDOS.size() > 0) {
                    map.put("城市", receiptsReportDOS.get(0).getCityName());
                } else {
                    map.put("城市", "无");
                }
                if (null != storeId && !(storeId.equals(-1L)) && null != receiptsReportDOS && receiptsReportDOS.size() > 0) {
                    map.put("门店", receiptsReportDOS.get(0).getStoreName());
                } else {
                    map.put("门店", "无");
                }
                if (null != storeType && !("".equals(storeType)) && null != receiptsReportDOS && receiptsReportDOS.size() > 0) {
                    map.put("门店类型", receiptsReportDOS.get(0).getStoreType());
                } else {
                    map.put("门店类型", "无");
                }
                if (null != payType && !("".equals(payType)) && null != receiptsReportDOS && receiptsReportDOS.size() > 0) {
                    map.put("支付方式", receiptsReportDOS.get(0).getPayType());
                } else {
                    map.put("支付方式", "无");
                }
                if (null != startTime && !("".equals(startTime))) {
                    map.put("开始时间", startTime);
                } else {
                    map.put("开始时间", "无");
                }
                if (null != keywords && !("".equals(keywords))) {
                    map.put("关键字", keywords);
                } else {
                    map.put("关键字", "无");
                }
                if (null != endTime && !("".equals(endTime))) {
                    map.put("结束时间", endTime);
                } else {
                    map.put("结束时间", "无");
                }
                //设置筛选条件
                ws = this.setCondition(ws, map, titleFormat, shiroName, textFormat);
                //列宽
                int[] columnView = {10, 13, 10, 15, 15, 20, 20, 12, 10, 30,10, 30, 15, 15, 15, 35,35, 25};
                //列标题
                String[] titles = {"城市", "门店名称", "门店类型", "导购姓名", "顾客姓名", "付款时间", "退款时间", "支付方式", "支付金额", "订单号","订单类型", "退单号", "第三方支付流水号","商户订单号", "pos交易流水单号"};
                //计算标题开始行号
                int row = 1;
                if (null != map && map.size() > 0) {
                    row = (map.size() + 1) / 2 + 4;
                }

                String str = "参数范围内 \n 包含：①变更时间内所有的收款变动情况②变动金额包含运费";
                ws.mergeCells(0, map.size(), 5, map.size());
                WritableCellFormat textFormat1 = this.setTextStyle();
                textFormat1.setWrap(true);
                ws.addCell(new Label(0, map.size(), str, textFormat1));
                ws.setRowView(row - 1, 1000);

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
                WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"), 9, WritableFont.NO_BOLD, false,
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
                    ws.addCell(new Label(3, j + row, receiptsReportDO.getSellerName(), textFormat));
                    ws.addCell(new Label(4, j + row, receiptsReportDO.getCustomerName(), textFormat));
                    ws.addCell(new Label(5, j + row, receiptsReportDO.getPayTime(), textFormat));
                    ws.addCell(new Label(6, j + row, receiptsReportDO.getReturnPayTime(), textFormat));
                    ws.addCell(new Label(7, j + row, receiptsReportDO.getPayType(), textFormat));
                    if ("DELIVERY_CLERK".equals(receiptsReportDO.getPaymentSubjectType())) {
                        if ("CASH".equals(receiptsReportDO.getPayTypes())) {
                            ws.addCell(new Label(7, j + row, "配送现金", textFormat));
                        }
                        if ("POS".equals(receiptsReportDO.getPayTypes())) {
                            ws.addCell(new Label(7, j + row, "配送POS", textFormat));
                        }
                    }
                    ws.addCell(new Number(8, j + row, receiptsReportDO.getMoney(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Label(9, j + row, receiptsReportDO.getOrderNumber(), textFormat));
                    ws.addCell(new Label(10, j + row, receiptsReportDO.getOrderType(), textFormat));
                    ws.addCell(new Label(11, j + row, receiptsReportDO.getReturnOrderNumber(), textFormat));
                    ws.addCell(new Label(12, j + row, receiptsReportDO.getTradeNo() == null ? "" : receiptsReportDO.getTradeNo(), textFormat));
                    ws.addCell(new Label(13, j + row, receiptsReportDO.getOutTradeNo(), textFormat));
                    ws.addCell(new Label(14, j + row, receiptsReportDO.getRemarks(), textFormat));

                    if ("CUS_PREPAY".equals(receiptsReportDO.getPayTypes())) {
                        cusPrepay = CountUtil.add(cusPrepay, null == receiptsReportDO.getMoney() ? 0D : receiptsReportDO.getMoney());
                    } else if ("ST_PREPAY".equals(receiptsReportDO.getPayTypes())) {
                        stPrepay = CountUtil.add(stPrepay, null == receiptsReportDO.getMoney() ? 0D : receiptsReportDO.getMoney());
                    } else if ("ALIPAY".equals(receiptsReportDO.getPayTypes())) {
                        alipay = CountUtil.add(alipay, null == receiptsReportDO.getMoney() ? 0D : receiptsReportDO.getMoney());
                    } else if ("WE_CHAT".equals(receiptsReportDO.getPayTypes())) {
                        weChat = CountUtil.add(weChat, null == receiptsReportDO.getMoney() ? 0D : receiptsReportDO.getMoney());
                    } else if ("UNION_PAY".equals(receiptsReportDO.getPayTypes())) {
                        unionPay = CountUtil.add(unionPay, null == receiptsReportDO.getMoney() ? 0D : receiptsReportDO.getMoney());
                    } else if ("POS".equals(receiptsReportDO.getPayTypes())) {
                        if ("DELIVERY_CLERK".equals(receiptsReportDO.getPaymentSubjectType())) {
                            deliveryPos = CountUtil.add(deliveryPos, null == receiptsReportDO.getMoney() ? 0D : receiptsReportDO.getMoney());
                        } else {
                            pos = CountUtil.add(pos, null == receiptsReportDO.getMoney() ? 0D : receiptsReportDO.getMoney());
                        }

                    } else if ("CASH".equals(receiptsReportDO.getPayTypes())) {
                        if ("DELIVERY_CLERK".equals(receiptsReportDO.getPaymentSubjectType())) {
                            deliveryCash = CountUtil.add(deliveryCash, null == receiptsReportDO.getMoney() ? 0D : receiptsReportDO.getMoney());
                        } else {
                            cash = CountUtil.add(cash, null == receiptsReportDO.getMoney() ? 0D : receiptsReportDO.getMoney());
                        }
                    } else {
                        other = CountUtil.add(other, null == receiptsReportDO.getMoney() ? 0D : receiptsReportDO.getMoney());
                    }
                    totle = CountUtil.add(totle, null == receiptsReportDO.getMoney() ? 0D : receiptsReportDO.getMoney());
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
        String fileurl = "未提货报表-" + DateUtils.getCurrentTimeStr("yyyyMMddHHmmss") + ".xls";//如  D:/xx/xx/xxx.xls

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
                    map.put("城市", notPickGoodsReportDOS.get(0).getCityName());
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
                if (null != startTime && !("".equals(startTime)) && !("undefined".equals(startTime))) {
                    map.put("购买时间", startTime);
                } else {
                    map.put("购买时间", "无");
                }
                if (null != endTime && !("".equals(endTime)) && !("undefined".equals(endTime))) {
                    map.put("截至时间", endTime);
                } else {
                    map.put("截至时间", "无");
                }
                //设置筛选条件
                ws = this.setCondition(ws, map, titleFormat, shiroName, textFormat);

                //列宽
                int[] columnView = {10, 13, 10, 20, 20, 10, 10, 15, 20, 10, 10, 20, 30, 10, 10, 10, 10, 10, 30};
                //列标题
                String[] titles = {"城市", "门店名称", "门店类型", "未提货类型", "购买日期", "过期时间", "顾客编号", "顾客姓名	",
                        "顾客电话", "顾客类型", "销顾姓名", "商品编码", "商品名称", "品牌", "数量", "购买单价", "经销价", "购买总价", "相关单号"};

                //计算标题开始行号
                int row = 1;
                if (null != map && map.size() > 0) {
                    row = (map.size() + 1) / 2 + 4;
                }

                String str = "订单参数范围内\n包含：①截止日期前未出货的订单和未提产品劵\n不包含： ①截止日期前已取消订单②截止日期前已拒签的订单③金额不包含运费";
                ws.mergeCells(0, map.size(), 5, map.size());
                WritableCellFormat textFormat1 = this.setTextStyle();
                textFormat1.setWrap(true);
                ws.addCell(new Label(0, map.size(), str, textFormat1));
                ws.setRowView(row - 1, 1000);

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
//                    notPickGoodsReportDO.setPickType(notPickGoodsReportDO.getPickType());
                    ws.addCell(new Label(0, j + row, notPickGoodsReportDO.getCityName(), textFormat));
                    ws.addCell(new Label(1, j + row, notPickGoodsReportDO.getStoreName(), textFormat));
                    ws.addCell(new Label(2, j + row, notPickGoodsReportDO.getStoreType(), textFormat));
                    ws.addCell(new Label(3, j + row, notPickGoodsReportDO.getPickType(), textFormat));
                    ws.addCell(new Label(4, j + row, notPickGoodsReportDO.getBuyTime(), textFormat));
                    ws.addCell(new Label(5, j + row, notPickGoodsReportDO.getEffectiveTime(), textFormat));
                    ws.addCell(new Label(6, j + row, notPickGoodsReportDO.getCustomerId() + "", textFormat));
                    ws.addCell(new Label(7, j + row, notPickGoodsReportDO.getCustomerName(), textFormat));
                    ws.addCell(new Label(8, j + row, notPickGoodsReportDO.getCustomerPhone(), textFormat));
                    ws.addCell(new Label(9, j + row, notPickGoodsReportDO.getCustomerType(), textFormat));
                    ws.addCell(new Label(10, j + row, notPickGoodsReportDO.getSellerName(), textFormat));
                    ws.addCell(new Label(11, j + row, notPickGoodsReportDO.getSku(), textFormat));
                    ws.addCell(new Label(12, j + row, notPickGoodsReportDO.getSkuName(), textFormat));
                    ws.addCell(new Label(13, j + row, notPickGoodsReportDO.getBrandName(), textFormat));
                    ws.addCell(new Number(14, j + row, notPickGoodsReportDO.getQuantity(), textFormat));
                    ws.addCell(new Number(15, j + row, notPickGoodsReportDO.getBuyPrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(16, j + row, notPickGoodsReportDO.getWholesalePrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(17, j + row, notPickGoodsReportDO.getTotalBuyPrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Label(18, j + row, notPickGoodsReportDO.getReferenceNumber(), textFormat));

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
        String fileurl = "门店预存款-" + DateUtils.getCurrentTimeStr("yyyyMMddHHmmss") + ".xls";//如  D:/xx/xx/xxx.xls

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
                int[] columnView = {10, 20, 13, 20, 10, 10, 15, 20, 20, 20};
                //列标题
                String[] titles = {"城市", "门店名称", "门店类型", "变动类型", "变动金额", "变更后总金额", "变更时间", "订单号", "退单号", "备注"};
                //计算标题开始行号
                int row = 1;
                if (null != map && map.size() > 0) {
                    row = (map.size() + 1) / 2 + 4;
                }
                String str = "参数范围内\n包含：①变更时间内所有的预存款变动情况②变动金额包含运费";
                ws.mergeCells(0, map.size(), 5, map.size());
                WritableCellFormat textFormat1 = this.setTextStyle();
                textFormat1.setWrap(true);
                ws.addCell(new Label(0, map.size(), str, textFormat1));
                ws.setRowView(row -2, 1000);

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
                    ws.addCell(new Label(8, j + row, storePredepositReportDO.getReturnNo(), textFormat));
                    ws.addCell(new Label(9, j + row, storePredepositReportDO.getRemarks(), textFormat));
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

    @GetMapping(value = "/employee/creditMoney/download")
    public void employeeCreditMoneyDownload(HttpServletRequest request, HttpServletResponse response, Long cityId, Long storeId, String storeType, String startTime, String endTime) {
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        List<EmpCreditMoneyChangeReportDO> empCreditMoneyChangeReportDOS = this.maReportDownloadService.employeeCreditMoneyDownload(cityId, storeId, storeType, startTime, endTime, storeIds);
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String shiroName = "";
        if (null != shiroUser) {
            shiroName = shiroUser.getName();
        }

        response.setContentType("text/html;charset=UTF-8");
        //创建名称
        String fileurl = "导购信用额度变动-" + DateUtils.getCurrentTimeStr("yyyyMMddHHmmss") + ".xls";//如  D:/xx/xx/xxx.xls

        WritableWorkbook wwb = null;
        try {
            //创建文件
            wwb = exportXML(fileurl, response);

            //excel单表最大行数是65535
            int maxSize = 0;
            if (empCreditMoneyChangeReportDOS != null) {
                maxSize = empCreditMoneyChangeReportDOS.size();
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
                if (null != cityId && !(cityId.equals(-1L)) && null != empCreditMoneyChangeReportDOS && empCreditMoneyChangeReportDOS.size() > 0) {
                    map.put("城市", empCreditMoneyChangeReportDOS.get(0).getCityName());
                } else {
                    map.put("城市", "无");
                }
                if (null != storeId && !(storeId.equals(-1L)) && null != empCreditMoneyChangeReportDOS && empCreditMoneyChangeReportDOS.size() > 0) {
                    map.put("门店", empCreditMoneyChangeReportDOS.get(0).getStoreName());
                } else {
                    map.put("门店", "无");
                }
                if (null != storeType && !("".equals(storeType)) && null != empCreditMoneyChangeReportDOS && empCreditMoneyChangeReportDOS.size() > 0) {
                    map.put("门店类型", empCreditMoneyChangeReportDOS.get(0).getStoreType());
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
                int[] columnView = {10, 20, 13, 20, 10, 10, 15, 20, 20, 20};
                //列标题
                String[] titles = {"城市", "门店名称", "门店类型", "导购", "变动类型", "变动金额", "变更后总金额", "变更时间", "订单号", "退单号"};
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
                    EmpCreditMoneyChangeReportDO empCreditMoneyChangeReportDO = empCreditMoneyChangeReportDOS.get(j + i * maxRowNum);
                    ws.addCell(new Label(0, j + row, empCreditMoneyChangeReportDO.getCityName(), textFormat));
                    ws.addCell(new Label(1, j + row, empCreditMoneyChangeReportDO.getStoreName(), textFormat));
                    ws.addCell(new Label(2, j + row, empCreditMoneyChangeReportDO.getStoreType(), textFormat));
                    ws.addCell(new Label(3, j + row, empCreditMoneyChangeReportDO.getEmployeeName(), textFormat));
                    ws.addCell(new Label(4, j + row, empCreditMoneyChangeReportDO.getChangeTypeDesc(), textFormat));
                    ws.addCell(new Number(5, j + row, empCreditMoneyChangeReportDO.getChangeMoney(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(6, j + row, empCreditMoneyChangeReportDO.getBalance(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Label(7, j + row, empCreditMoneyChangeReportDO.getChangeTime(), textFormat));
                    ws.addCell(new Label(8, j + row, empCreditMoneyChangeReportDO.getOrderNumber(), textFormat));
                    ws.addCell(new Label(9, j + row, empCreditMoneyChangeReportDO.getReturnNumber(), textFormat));
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
     * @param
     * @return
     * @throws
     * @title 订单明细报表下载
     * @descripe
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
        String fileurl = "订单明细报表-" + DateUtils.getCurrentTimeStr("yyyyMMddHHmmss") + ".xls";//如  D:/xx/xx/xxx.xls

        WritableWorkbook wwb = null;
        try {
            //创建文件
            wwb = exportXML(fileurl, response);

            //excel单表最大行数是65535
            int maxSize = 0;
            if (null != accountGoodsItemsDOList) {
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
                if (null != cityId && !(cityId.equals(-1L)) && null != accountGoodsItemsDOList && accountGoodsItemsDOList.size() > 0) {
                    map.put("城市", accountGoodsItemsDOList.get(0).getCityName());
                } else {
                    map.put("城市", "无");
                }
                if (null != storeId && !(storeId.equals(-1L)) && null != accountGoodsItemsDOList && accountGoodsItemsDOList.size() > 0) {
                    map.put("门店", accountGoodsItemsDOList.get(0).getStoreName());
                } else {
                    map.put("门店", "无");
                }
                if (null != storeType && !("".equals(storeType)) && null != accountGoodsItemsDOList && accountGoodsItemsDOList.size() > 0) {
                    map.put("门店类型", accountGoodsItemsDOList.get(0).getStoreType());
                } else {
                    map.put("门店类型", "无");
                }
                if (null != startTime && !("".equals(startTime))) {
                    map.put("开始时间", startTime);
                } else {
                    map.put("开始时间", "无");
                }
                if (null != keywords && !("".equals(keywords))) {
                    map.put("关键字", keywords);
                } else {
                    map.put("关键字", "无");
                }
                if (null != endTime && !("".equals(endTime))) {
                    map.put("结束时间", endTime);
                } else {
                    map.put("结束时间", "无");
                }
                //设置筛选条件
                ws = this.setCondition(ws, map, titleFormat, shiroName, textFormat);
                //列宽
                int[] columnView = {10, 13, 10, 20, 30, 25, 15, 15, 10, 10, 20, 20, 50, 20, 20, 10, 10, 10, 15, 15, 15, 15, 15, 15, 15, 10, 5, 30, 30, 5};
                //列标题
                String[] titles = {"城市", "门店名称", "门店类型", "下单/反配上架时间", "订单号", "退单号", "顾客", "导购姓名", "配送/退货方式",
                        "出/退货状态", "收货/退货人", "收货/退货人电话", "送/退货地址", "产品编码", "产品名称", "产品标识", "产品类型", "数量", "结算单价", "结算总价",
                        "成交单价", "成交总价", "经销单价", "总经销价", "单个产品经销差价", "总经销差价", "下单人", "单位", "备注", "楼盘信息", "是否结清"};
                //计算标题开始行号
                int row = 1;
                if (null != map && map.size() > 0) {
                    row = (map.size() + 1) / 2 + 4;
                }

                String str = "订单参数范围 \n " +
                        "包含：①本月已出货的订单②本月已反配上架的订单③本月下单下月出货的订单 \n " +
                        "不包含：①本月“已取消订单” ②本月待付款订单  ③订单中运费金额\n" ;
                ws.mergeCells(0, map.size(), 5, map.size());
                WritableCellFormat textFormat1 = this.setTextStyle();
                textFormat1.setWrap(true);
                ws.addCell(new Label(0, map.size(), str, textFormat1));
                ws.setRowView(row - 1, 2000);

                //设置标题
                ws = this.setHeader(ws, titleFormat, columnView, titles, row);
                row += 1;
                WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"), 9, WritableFont.NO_BOLD, false,
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
                    ws.addCell(new Label(5, j + row, accountGoodsItemsDO.getReturnNumber(), textFormat));
                    ws.addCell(new Label(6, j + row, accountGoodsItemsDO.getCustomerName(), textFormat));
                    ws.addCell(new Label(7, j + row, accountGoodsItemsDO.getSellerName(), textFormat));
                    ws.addCell(new Label(8, j + row, accountGoodsItemsDO.getDeliveryType(), textFormat));
                    ws.addCell(new Label(9, j + row, accountGoodsItemsDO.getDeliveryStatus(), textFormat));
                    ws.addCell(new Label(10, j + row, accountGoodsItemsDO.getReceiver(), textFormat));
                    ws.addCell(new Label(11, j + row, accountGoodsItemsDO.getReceiverPhone(), textFormat));
                    ws.addCell(new Label(12, j + row, accountGoodsItemsDO.getShippingAddress(), textFormat));
                    ws.addCell(new Label(13, j + row, accountGoodsItemsDO.getSku(), textFormat));
                    ws.addCell(new Label(14, j + row, accountGoodsItemsDO.getSkuName(), textFormat));
                    ws.addCell(new Label(15, j + row, accountGoodsItemsDO.getCompanyFlag(), textFormat));
                    ws.addCell(new Label(16, j + row, accountGoodsItemsDO.getGoodsLineType(), textFormat));
                    ws.addCell(new Label(17, j + row, accountGoodsItemsDO.getQuantity().toString(), textFormat));
                    ws.addCell(new Number(18, j + row, accountGoodsItemsDO.getSettlementPrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(19, j + row, accountGoodsItemsDO.getSettlementTotlePrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(20, j + row, accountGoodsItemsDO.getPromotionSharePrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(21, j + row, accountGoodsItemsDO.getPromotionShareTotlePrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(22, j + row, accountGoodsItemsDO.getJxPrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(23, j + row, accountGoodsItemsDO.getTotalJxPrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));

                    if (StoreType.JM == accountGoodsItemsDO.getStoreTypes() || StoreType.FX == accountGoodsItemsDO.getStoreTypes()) {
                        if (accountGoodsItemsDO.getGoodsLineType().equals("本品") || accountGoodsItemsDO.getGoodsLineType().equals("产品券")) {
                            String goodsLineType = "";

                            if (accountGoodsItemsDO.getGoodsLineType().equals("本品")){
                                goodsLineType = "GOODS";
                            }else {
                                goodsLineType = "PRODUCT_COUPON";
                            }

                            String orderNumber = accountGoodsItemsDO.getOrderNumber();

                            if (null != accountGoodsItemsDO.getReturnNumber()) {
                                orderNumber = accountGoodsItemsDO.getReturnNumber();
                            }

                            AccountGoodsItemsDO accountGoods = this.maReportDownloadService.getJxPriceByOrderNoAndSku(orderNumber, accountGoodsItemsDO.getSku(),goodsLineType);

                            if (null == accountGoods) {
                                accountGoods = new AccountGoodsItemsDO();
                            }
                            ws.addCell(new Number(24, j + row, null == accountGoods.getWholesalePrice() ? 0D : accountGoods.getWholesalePrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                            ws.addCell(new Number(25, j + row, null == accountGoods.getWholesalePrice() ? 0D : accountGoods.getWholesalePrice() * accountGoodsItemsDO.getQuantity(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                        } else {
                            ws.addCell(new Number(24, j + row, 0D, new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                            ws.addCell(new Number(25, j + row, 0D, new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                        }
                    } else {
                        ws.addCell(new Number(24, j + row, 0D, new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                        ws.addCell(new Number(25, j + row, 0D, new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    }
                    ws.addCell(new Label(26, j + row, accountGoodsItemsDO.getCreatorName(), textFormat));
                    ws.addCell(new Label(27, j + row, accountGoodsItemsDO.getGoodsUnit(), textFormat));
                    ws.addCell(new Label(28, j + row, accountGoodsItemsDO.getRemark(), textFormat));
                    ws.addCell(new Label(29, j + row, accountGoodsItemsDO.getEstateInfo(), textFormat));
                    ws.addCell(new Label(30, j + row, accountGoodsItemsDO.getIsPayUp(), textFormat));
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
     * @param
     * @return
     * @throws
     * @title 订单汇总报表下载
     * @descripe
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
        String fileurl = "订单汇总报表-" + DateUtils.getCurrentTimeStr("yyyyMMddHHmmss") + ".xls";//如  D:/xx/xx/xxx.xls

        WritableWorkbook wwb = null;
        try {
            //创建文件
            wwb = exportXML(fileurl, response);

            //excel单表最大行数是65535
            int maxSize = 0;
            if (null != billingItemsDOList) {
                maxSize = billingItemsDOList.size();
            }
            int sheets = maxSize / maxRowNum + 1;
            //设置excel的sheet数
            for (int i = 0; i < sheets; i++) {
                //标题格式
                WritableCellFormat titleFormat = this.setTitleStyle();
                //正文格式
                WritableCellFormat textFormat = this.setTextStyle();
                //数字
                WritableCellFormat numberFormat = this.setNumberStyle();

                //工作表，参数0表示这是第一页
                WritableSheet ws = wwb.createSheet("第" + (i + 1) + "页", i);

                //筛选条件
                Map<String, String> map = new HashMap<>();
                if (null != cityId && !(cityId.equals(-1L)) && null != billingItemsDOList && billingItemsDOList.size() > 0) {
                    map.put("城市", billingItemsDOList.get(0).getCityName());
                } else {
                    map.put("城市", "无");
                }
                if (null != storeId && !(storeId.equals(-1L)) && null != billingItemsDOList && billingItemsDOList.size() > 0) {
                    map.put("门店", billingItemsDOList.get(0).getStoreName());
                } else {
                    map.put("门店", "无");
                }
                if (null != storeType && !("".equals(storeType)) && null != billingItemsDOList && billingItemsDOList.size() > 0) {
                    map.put("门店类型", billingItemsDOList.get(0).getStoreType());
                } else {
                    map.put("门店类型", "无");
                }
                if (null != startTime && !("".equals(startTime))) {
                    map.put("开始时间", startTime);
                } else {
                    map.put("开始时间", "无");
                }
                if (null != keywords && !("".equals(keywords))) {
                    map.put("关键字", keywords);
                } else {
                    map.put("关键字", "无");
                }
                if (null != endTime && !("".equals(endTime))) {
                    map.put("结束时间", endTime);
                } else {
                    map.put("结束时间", "无");
                }
                //设置筛选条件
                ws = this.setCondition(ws, map, titleFormat, shiroName, textFormat);
                //列宽
                int[] columnView = {10, 13, 10, 20, 30, 25, 15,10,15, 15, 15, 10, 10, 50, 15, 15, 15, 15, 10, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15,30,30};
                //列标题
                String[] titles = {"城市", "门店名称", "门店类型", "下单/退单时间", "订单号", "订单状态", "是否结清","退单号", "退单状态", "顾客", "导购姓名", "配送/退货方式",
                        "出/退货状态", "送/退货地址", "商品总额", "会员折扣", "订单折扣", "配送费", "优惠券折扣", "产品券折扣", "乐币折扣", "应付总额",
                        "微信", "支付宝", "银联", "门店现金", "门店POS", "配送现金", "配送POS", "其他", "门店预存款", "顾客预存款", "支付总额", "退回门店","商户订单号","第三方流水号"};
                //计算标题开始行号
                int row = 1;
                if (null != map && map.size() > 0) {
                    row = (map.size() + 1) / 2 + 4;
                }

                String str = "订单参数范围内\n包含：①本月已出货的订单②本月已反配上架的订单③本月下单下月出货的订单④本月“已取消订单“⑤本月待付款订单⑥订单中运费金额   ";
                ws.mergeCells(0, map.size(), 5, map.size());
                WritableCellFormat textFormat1 = this.setTextStyle();
                textFormat1.setWrap(true);
                ws.addCell(new Label(0, map.size(), str, textFormat1));
                ws.setRowView(row - 1, 1000);


                //设置标题
                ws = this.setHeader(ws, titleFormat, columnView, titles, row);
                row += 1;
                WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"), 9, WritableFont.NO_BOLD, false,
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
                    ws.addCell(new Label(5, j + row, billingItemsDO.getOrderStatus(), textFormat));
                    ws.addCell(new Label(6, j + row, billingItemsDO.getIsPayUp(), textFormat));

                    ws.addCell(new Label(7, j + row, billingItemsDO.getReturnNumber(), textFormat));
                    ws.addCell(new Label(8, j + row, billingItemsDO.getReturnStatus(), textFormat));

                    ws.addCell(new Label(9, j + row, billingItemsDO.getCustomerName(), textFormat));
                    ws.addCell(new Label(10, j + row, billingItemsDO.getSellerName(), textFormat));
                    ws.addCell(new Label(11, j + row, billingItemsDO.getDeliveryType(), textFormat));
                    ws.addCell(new Label(12, j + row, billingItemsDO.getDeliveryStatus(), textFormat));
                    ws.addCell(new Label(13, j + row, billingItemsDO.getShippingAddress(), textFormat));
                    ws.addCell(new Number(14, j + row, billingItemsDO.getTotalGoodsPrice(), numberFormat));
                    ws.addCell(new Number(15, j + row, billingItemsDO.getMemberDiscount(), numberFormat));
                    ws.addCell(new Number(16, j + row, billingItemsDO.getPromotionDiscount(), numberFormat));
                    ws.addCell(new Number(17, j + row, billingItemsDO.getFreight(), numberFormat));
                    ws.addCell(new Number(18, j + row, billingItemsDO.getCashCouponDiscount(), numberFormat));
                    ws.addCell(new Number(19, j + row, billingItemsDO.getProductCouponDiscount(), numberFormat));
                    ws.addCell(new Number(20, j + row, billingItemsDO.getLbDiscount(), numberFormat));
                    ws.addCell(new Number(21, j + row, billingItemsDO.getAmountPayable(), numberFormat));
                    ws.addCell(new Number(22, j + row, billingItemsDO.getWeChat(), numberFormat));
                    ws.addCell(new Number(23, j + row, billingItemsDO.getAlipay(), numberFormat));
                    ws.addCell(new Number(24, j + row, billingItemsDO.getUnionPay(), numberFormat));
                    ws.addCell(new Number(25, j + row, billingItemsDO.getStoreCash(), numberFormat));
                    ws.addCell(new Number(26, j + row, billingItemsDO.getStorePosMoney(), numberFormat));
                    ws.addCell(new Number(27, j + row, billingItemsDO.getDeliveryCash(), numberFormat));
                    ws.addCell(new Number(28, j + row, billingItemsDO.getDeliveryPos(), numberFormat));
                    ws.addCell(new Number(29, j + row, billingItemsDO.getStoreOtherMoney(), numberFormat));
                    ws.addCell(new Number(30, j + row, billingItemsDO.getStPreDeposit(), numberFormat));
                    ws.addCell(new Number(31, j + row, billingItemsDO.getCusPreDeposit(), numberFormat));
                    ws.addCell(new Number(32, j + row, billingItemsDO.getTotalPay(), numberFormat));
                    ws.addCell(new Number(33, j + row, billingItemsDO.getReturnStore(), numberFormat));
                    ws.addCell(new Label(34, j + row, billingItemsDO.getOutTradeNo(), textFormat));
                    ws.addCell(new Label(35, j + row, billingItemsDO.getTradeNo(), textFormat));

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
     * @param
     * @return
     * @throws
     * @title 配送员代收款报表下载
     * @descripe
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
        String fileurl = "配送员代收款报表-" + DateUtils.getCurrentTimeStr("yyyyMMddHHmmss") + ".xls";//如  D:/xx/xx/xxx.xls

        WritableWorkbook wwb = null;
        try {
            //创建文件
            wwb = exportXML(fileurl, response);

            //excel单表最大行数是65535
            int maxSize = 0;
            if (null != agencyFundDOList) {
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
                if (null != cityId && !(cityId.equals(-1L)) && null != agencyFundDOList && agencyFundDOList.size() > 0) {
                    map.put("城市", agencyFundDOList.get(0).getCityName());
                } else {
                    map.put("城市", "无");
                }
                if (null != storeId && !(storeId.equals(-1L)) && null != agencyFundDOList && agencyFundDOList.size() > 0) {
                    map.put("门店", agencyFundDOList.get(0).getStoreName());
                } else {
                    map.put("门店", "无");
                }
                if (null != storeType && !("".equals(storeType)) && null != agencyFundDOList && agencyFundDOList.size() > 0) {
                    map.put("门店类型", agencyFundDOList.get(0).getStoreType());
                } else {
                    map.put("门店类型", "无");
                }
                if (null != startTime && !("".equals(startTime))) {
                    map.put("开始时间", startTime);
                } else {
                    map.put("开始时间", "无");
                }
                if (null != keywords && !("".equals(keywords))) {
                    map.put("关键字", keywords);
                } else {
                    map.put("关键字", "无");
                }
                if (null != endTime && !("".equals(endTime))) {
                    map.put("结束时间", endTime);
                } else {
                    map.put("结束时间", "无");
                }
                //设置筛选条件
                ws = this.setCondition(ws, map, titleFormat, shiroName, textFormat);
                //列宽
                int[] columnView = {10, 13, 13, 10, 20, 30, 50, 10, 10, 15, 15, 15, 25, 15, 15, 25,10};
                //列标题
                String[] titles = {"城市", "仓库", "门店名称", "门店类型", "封车出库时间", "单号", "收货地址", "导购姓名", "配送员姓名",
                        "订单代收金额", "配送员实际收款现金", "配送员实际收款POS", "配送员备注", "应退门店", "仓库应存回公司货款", "订单备注","是否结清"};
                //计算标题开始行号
                int row = 1;
                if (null != map && map.size() > 0) {
                    row = (map.size() + 1) / 2 + 4;
                }

                String str = "参数范围内 \n " +
                        "包含：①已出货需配送员代收的订单 \n " +
                        "订单代收金额：导购在APP上填写的需配送员代收的金额（不一定等于订单金额）\n" +
                        "应退门店：配送员帮导购代收与订单无关的款项 \n" +
                        "仓库应存回公司货款：配送员代收的订单金额（小于定于订单金额）";
                ws.mergeCells(0, map.size(), 5, map.size());
                WritableCellFormat textFormat1 = this.setTextStyle();
                textFormat1.setWrap(true);
                ws.addCell(new Label(0, map.size(), str, textFormat1));
                ws.setRowView(row - 1, 2000);

                //设置标题
                ws = this.setHeader(ws, titleFormat, columnView, titles, row);
                row += 1;
                WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"), 9, WritableFont.NO_BOLD, false,
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
                    ws.addCell(new Label(15, j + row, agencyFundDO.getIsPayUp(), textFormat));
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
     * @param
     * @return
     * @throws
     * @title
     * @descripe
     * @author GenerationRoad
     * @date 2018/4/2
     */
    @GetMapping(value = "/storeInventory/download")
    public void downloadStoreInventory(HttpServletRequest request, HttpServletResponse response, Long storeId) {
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        List<StoreInventory> storeInventoryList = this.maReportDownloadService.downloadStoreInventorys(storeId, storeIds);
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String shiroName = "";
        if (null != shiroUser) {
            shiroName = shiroUser.getName();
        }

        response.setContentType("text/html;charset=UTF-8");
        //创建名称
        String fileurl = "门店库存明细报表-" + DateUtils.getCurrentTimeStr("yyyyMMddHHmmss") + ".xls"; //如  D:/xx/xx/xxx.xls

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

                if (null != storeId && !(storeId.equals(-1L)) && null != storeInventoryList && storeInventoryList.size() > 0) {
                    map.put("门店", storeInventoryList.get(0).getStoreName());
                } else {
                    map.put("门店", "无");
                }
                //设置筛选条件
                ws = this.setCondition(ws, map, titleFormat, shiroName, textFormat);
                //列宽
                int[] columnView = {10, 40, 20, 20, 30, 15, 15};
                //列标题
                String[] titles = {"门店名称", "商品名称", "商品编码", "真实库存", "可售库存"};
                //计算标题开始行号
                int row = 1;
                if (null != map && map.size() > 0) {
                    row = (map.size() + 1) / 2 + 4;
                }

                //设置标题
                ws = this.setHeader(ws, titleFormat, columnView, titles, row);
                row += 1;
                WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"), 9, WritableFont.NO_BOLD, false,
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
     * @param
     * @return
     * @throws
     * @title 商品要货退货报表下载
     * @descripe
     * @author GenerationRoad
     * @date 2018/4/2
     */
    @GetMapping(value = "/goodsShipmentAndReturn/download")
    public void downloadGoodsShipmentAndReturn(HttpServletRequest request, HttpServletResponse response, Long cityId, Long storeId, String storeType,
                                               String startTime, String endTime, String keywords) {
        if (StringUtils.isBlank(startTime)) {
            startTime = "2018-04-01 00:00:00";
        }
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        List<ShipmentAndReturnGoods> shipmentAndReturnGoodsList = this.maReportDownloadService.downShipmentAndReturnOrder(cityId, storeId, storeType, startTime, endTime, keywords, storeIds);
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String shiroName = "";
        if (null != shiroUser) {
            shiroName = shiroUser.getName();
        }


        response.setContentType("text/html;charset=UTF-8");
        //创建名称
        String fileurl = "商品出退货明细报表-" + DateUtils.getCurrentTimeStr("yyyyMMddHHmmss") + ".xls"; //如  D:/xx/xx/xxx.xls

        WritableWorkbook wwb = null;
        try {
            //创建文件
            wwb = exportXML(fileurl, response);

            //excel单表最大行数是65535
            int maxSize = 0;
            if (shipmentAndReturnGoodsList != null) {
                maxSize = shipmentAndReturnGoodsList.size();
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

                if (null != cityId && -1 != cityId && !(cityId.equals(-1L)) && null != shipmentAndReturnGoodsList && shipmentAndReturnGoodsList.size() > 0) {
                    map.put("城市", shipmentAndReturnGoodsList.get(0).getCityName());
                } else {
                    map.put("城市", "无");
                }
                if (null != storeId && -1 != storeId && !(storeId.equals(-1L)) && null != shipmentAndReturnGoodsList && shipmentAndReturnGoodsList.size() > 0) {
                    map.put("门店", shipmentAndReturnGoodsList.get(0).getStoreName());
                } else {
                    map.put("门店", "无");
                }
                if (null != storeType && !"-1".equals(storeType) && !("".equals(storeType)) && null != shipmentAndReturnGoodsList && shipmentAndReturnGoodsList.size() > 0) {
                    map.put("门店类型", shipmentAndReturnGoodsList.get(0).getStoreType());
                } else {
                    map.put("门店类型", "无");
                }
                if (null != startTime && !("".equals(startTime))) {
                    map.put("开始时间", startTime);
                } else {
                    map.put("开始时间", "无");
                }
                if (null != keywords && !("".equals(keywords))) {
                    map.put("关键字", keywords);
                } else {
                    map.put("关键字", "无");
                }
                if (null != endTime && !("".equals(endTime))) {
                    map.put("结束时间", endTime);
                } else {
                    map.put("结束时间", "无");
                }
                //设置筛选条件
                ws = this.setCondition(ws, map, titleFormat, shiroName, textFormat);
                //列宽
                int[] columnView = {10, 40, 20, 20, 30, 15, 15};
                //列标题城市

                String[] titles = {"城市", "门店名称", "门店类型", "项目", "订单号", "原订单类型", "退单类型", "出退货日期", "原订单日期", "客户编号", "客户电话", "客户姓名", "客户类型", "销顾姓名", "公司标识", "商品编码", "商品名称", "产品类型", "商品类型","数量", "成交单价", "成交总价", "仓库信息"};
                //计算标题开始行号
                int row = 1;
                if (null != map && map.size() > 0) {
                    row = (map.size() + 1) / 2 + 4;
                }
                String str = "订单参数范围内\n包含：①本月已配送出货的订单②本月已门店提货的订单③本月已反配上架的订单④本月已门店确认收货的订单";
                ws.mergeCells(0, map.size(), 5, map.size());
                WritableCellFormat textFormat1 = this.setTextStyle();
                textFormat1.setWrap(true);
                ws.addCell(new Label(0, map.size(), str, textFormat1));
                ws.setRowView(row - 1, 1000);

                //设置标题
                ws = this.setHeader(ws, titleFormat, columnView, titles, row);
                row += 1;
                WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"), 9, WritableFont.NO_BOLD, false,
                        UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                //填写表体数据
                for (int j = 0; j < maxRowNum; j++) {
                    if (j + i * maxRowNum >= maxSize) {
                        break;
                    }
                    ShipmentAndReturnGoods shipmentAndReturnGoods = shipmentAndReturnGoodsList.get(j + i * maxRowNum);
                    ws.addCell(new Label(0, j + row, shipmentAndReturnGoods.getCityName(), textFormat));
                    ws.addCell(new Label(1, j + row, shipmentAndReturnGoods.getStoreName(), textFormat));
                    ws.addCell(new Label(2, j + row, shipmentAndReturnGoods.getStoreType(), textFormat));
                    ws.addCell(new Label(3, j + row, shipmentAndReturnGoods.getOrderType(), textFormat));
                    ws.addCell(new Label(4, j + row, shipmentAndReturnGoods.getOrdNo(), textFormat));
                    ws.addCell(new Label(5, j + row, shipmentAndReturnGoods.getOrderDeliveryType(), textFormat));
                    ws.addCell(new Label(6, j + row, shipmentAndReturnGoods.getReturnOrderDeliveryType(), textFormat));
                    ws.addCell(new Label(7, j + row, shipmentAndReturnGoods.getOperationTime(), textFormat));
                    ws.addCell(new Label(8, j + row, shipmentAndReturnGoods.getCreateTime(), textFormat));
                    if (null != shipmentAndReturnGoods.getCustomerId()) {
                        ws.addCell(new Label(9, j + row, shipmentAndReturnGoods.getCustomerId().toString(), textFormat));
                    }
                    ws.addCell(new Label(10, j + row, shipmentAndReturnGoods.getCustomerPhone(), textFormat));
                    ws.addCell(new Label(11, j + row, shipmentAndReturnGoods.getCustomerName(), textFormat));
                    ws.addCell(new Label(12, j + row, shipmentAndReturnGoods.getCustomerType(), textFormat));
                    ws.addCell(new Label(13, j + row, shipmentAndReturnGoods.getSalesConsultName(), textFormat));
                    if (null != shipmentAndReturnGoods.getCompanyFlag()) {
                        ws.addCell(new Label(14, j + row, shipmentAndReturnGoods.getCompanyFlag(), textFormat));
                    }
                    if (null != shipmentAndReturnGoods.getSku()) {
                        ws.addCell(new Label(15, j + row, shipmentAndReturnGoods.getSku(), textFormat));
                    }
                    if (null != shipmentAndReturnGoods.getSkuName()) {
                        ws.addCell(new Label(16, j + row, shipmentAndReturnGoods.getSkuName(), textFormat));
                    }
                    if (null != shipmentAndReturnGoods.getGoodsLineType()) {
                        ws.addCell(new Label(17, j + row, shipmentAndReturnGoods.getGoodsLineType(), textFormat));
                    }

                    if (null != shipmentAndReturnGoods.getCategoryName()) {
                        ws.addCell(new Label(18, j + row, shipmentAndReturnGoods.getCategoryName(), textFormat));
                    }

                    if (null != shipmentAndReturnGoods.getOrderQty()) {
                        ws.addCell(new Number(19, j + row, shipmentAndReturnGoods.getOrderQty(), new WritableCellFormat(textFont, new NumberFormat("0"))));
                    }
                    if (null != shipmentAndReturnGoods.getReturnPrice()) {
                        ws.addCell(new Number(20, j + row, shipmentAndReturnGoods.getReturnPrice().doubleValue(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    }
                    if (null != shipmentAndReturnGoods.getAmount()) {
                        ws.addCell(new Number(21, j + row, shipmentAndReturnGoods.getAmount().doubleValue(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    }
                    ws.addCell(new Label(22, j + row, shipmentAndReturnGoods.getWareHouse(), textFormat));
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
     * @param
     * @return
     * @throws
     * @title 欠款报表下载
     * @descripe
     * @author GenerationRoad
     * @date 2018/4/3
     */
    @GetMapping(value = "/arrearsReport/download")
    public void downArrearsReportDown(HttpServletRequest request, HttpServletResponse response, String companyCode, String storeType, Long storeId) {
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getNotZsType());
        List<Long> storeIdInCompany = maStoreService.findStoresIdByStructureCodeAndStoreType(companyCode, storeType);
        storeIds.retainAll(storeIdInCompany);
        if (null != storeId && -1L != storeId) {
            storeIds.clear();
            storeIds.add(storeId);
        }
        //查询关联小型装饰公司
        List<Long> fitId = maStoreService.findFitCompanyIdByStoreId(storeIds);
        storeIds.addAll(fitId);
        List<ArrearsReportDO> salesList = this.maReportDownloadService.downArrearsList(companyCode, storeType, storeIds, storeId);
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String shiroName = "";
        if (null != shiroUser) {
            shiroName = shiroUser.getName();
        }
        response.setContentType("text/html;charset=UTF-8");
        //创建名称
        String fileurl = "欠款报表-" + DateUtils.getCurrentTimeStr("yyyyMMddHHmmss") + ".xls"; //如  D:/xx/xx/xxx.xls
        //查询筛选门店信息
        String storeName = null;
        if (null != storeId && -1L != storeId) {
            AppStore store = maStoreService.findAppStoreByStoreId(storeId);
            if (null != store) {
                storeName = store.getStoreName();
            }
        }
        WritableWorkbook wwb = null;
        try {
            //创建文件
            wwb = exportXML(fileurl, response);

            //excel单表最大行数是65535
            int maxSize = 0;
            if (salesList != null) {
                maxSize = salesList.size();
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

                if (null != companyCode && !"-1".equals(companyCode) && null != salesList && salesList.size() > 0) {
                    map.put("城市", salesList.get(0).getCityName());
                } else {
                    map.put("城市", "无");
                }
                if (null != storeType && !"-1".equals(storeType) && !("".equals(storeType)) && null != salesList && salesList.size() > 0) {
                    map.put("门店类型", StoreType.getStoreTypeByValue(storeType).getDescription());
                } else {
                    map.put("门店类型", "无");
                }
                if (null != storeId && -1L != storeId) {
                    map.put("门店名称", storeName);
                } else {
                    map.put("门店名称", "无");
                }
                //设置筛选条件
                ws = this.setCondition(ws, map, titleFormat, shiroName, textFormat);


                //列宽
                int[] columnView = {10, 20, 15, 10, 10, 30, 10, 10, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15};
                //列标题城市

                String[] titles = {"城市", "门店编码", "门店", "导购名称", "会员名称", "订单号", "配送/自提", "订单状态", "自提提货日期", "订单日期", "出货时间", "审核状态", "订单小计", "订单使用额度", "订单欠款", "订单已支付总金额", "是否结清", "订单还清日期"
                };


                //计算标题开始行号
                int row = 1;
                if (null != map && map.size() > 0) {
                    row = (map.size() + 1) / 2 + 4;
                }

                String str = "订单参数范围内\n包含：订单状态为“待发货”“待收货”“拒签”“已取消”“已完成”的单据";
                ws.mergeCells(0, map.size(), 5, map.size());
                WritableCellFormat textFormat1 = this.setTextStyle();
                textFormat1.setWrap(true);
                ws.addCell(new Label(0, map.size(), str, textFormat1));
                ws.setRowView(row - 3, 1000);
                //计算标题开始行号

                //设置标题
                ws = this.setHeader(ws, titleFormat, columnView, titles, row);
                row += 1;
                WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"), 9, WritableFont.NO_BOLD, false,
                        UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                //填写表体数据
                for (int j = 0; j < maxRowNum; j++) {
                    if (j + i * maxRowNum >= maxSize) {
                        break;
                    }
                    ArrearsReportDO arrearsReportDO = salesList.get(j + i * maxRowNum);
                    ws.addCell(new Label(0, j + row, arrearsReportDO.getCityName(), textFormat));
                    ws.addCell(new Label(1, j + row, arrearsReportDO.getStoreCode(), textFormat));
                    ws.addCell(new Label(2, j + row, arrearsReportDO.getStoreName(), textFormat));
                    ws.addCell(new Label(3, j + row, arrearsReportDO.getName(), textFormat));
                    ws.addCell(new Label(4, j + row, arrearsReportDO.getCustomerName(), textFormat));
                    ws.addCell(new Label(5, j + row, arrearsReportDO.getOrdNo(), textFormat));
                    ws.addCell(new Label(6, j + row, arrearsReportDO.getOrderType(), textFormat));
                    ws.addCell(new Label(7, j + row, arrearsReportDO.getOrderStatus(), textFormat));
                    ws.addCell(new Label(8, j + row, arrearsReportDO.getSelfTakeOrderTime(), textFormat));
                    ws.addCell(new Label(9, j + row, arrearsReportDO.getCreateTime(), textFormat));
                    ws.addCell(new Label(10, j + row, arrearsReportDO.getShippingDate(), textFormat));
                    ws.addCell(new Label(11, j + row, arrearsReportDO.getAuditStatus(), textFormat));
                    if (null != arrearsReportDO.getOrderAmount()) {
                        ws.addCell(new Number(12, j + row, arrearsReportDO.getOrderAmount(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    }
                    if (null != arrearsReportDO.getOrderCreditMoney()) {
                        ws.addCell(new Number(13, j + row, arrearsReportDO.getOrderCreditMoney(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    }
                    if (null != arrearsReportDO.getOrderArrearage()) {
                        ws.addCell(new Number(14, j + row, arrearsReportDO.getOrderArrearage(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    }
                    if (null != arrearsReportDO.getPayUpMoney()) {
                        ws.addCell(new Number(15, j + row, arrearsReportDO.getPayUpMoney(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    }
                    ws.addCell(new Label(16, j + row, arrearsReportDO.getIsPayUp(), textFormat));
                    ws.addCell(new Label(17, j + row, arrearsReportDO.getPayUpTime(), textFormat));
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
     * @param
     * @return
     * @throws
     * @title 当月销量报表下载
     * @descripe
     * @author GenerationRoad
     * @date 2018/4/2
     */
    @GetMapping(value = "/salesReport/download")
    public void downloadSalesReport(HttpServletRequest request, HttpServletResponse response, String companyCode, String storeType,
                                    String startTime, String endTime, Boolean isProductCoupon, String productType, Long storeId) {
        if (StringUtils.isBlank(startTime)) {
            startTime = "2017-10-01 00:00:00";
        }
        if (StringUtils.isBlank(endTime)) {
            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
            endTime = format.format(now);
        }
        //查询筛选门店信息
        String storeName = null;
        if (null != storeId && -1L != storeId) {
            AppStore store = maStoreService.findAppStoreByStoreId(storeId);
            if (null != store) {
                storeName = store.getStoreName();
            }
        }
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        if (companyCode.equals("ALL")) {
            // 不限分公司
        } else {
            if("JZSYBM".equals(companyCode)){
                companyCode="|JZC001|";
            }
            List<Long> storeIdInCompany = maStoreService.findStoresIdByStructureCode(companyCode);
            storeIds.retainAll(storeIdInCompany);
        }

        if (null != storeId && -1L != storeId) {
            storeIds.clear();
            storeIds.add(storeId);
        }

        List<SalesReportDO> salesList = this.maReportDownloadService.downSalesReport(companyCode, storeType,
                startTime, endTime, isProductCoupon, storeIds, productType, storeId);
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String shiroName = "";
        if (null != shiroUser) {
            shiroName = shiroUser.getName();
        }

        response.setContentType("text/html;charset=UTF-8");
        //创建名称
        String fileurl = "销量报表-" + DateUtils.getCurrentTimeStr("yyyyMMddHHmmss") + ".xls"; //如  D:/xx/xx/xxx.xls

        WritableWorkbook wwb = null;
        try {
            //创建文件
            wwb = exportXML(fileurl, response);

            //excel单表最大行数是65535
            int maxSize = 0;
            if (salesList != null) {
                maxSize = salesList.size();
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

                if (null != companyCode && !"-1".equals(companyCode) && null != salesList && salesList.size() > 0) {
                    map.put("城市", salesList.get(0).getCityName());
                } else {
                    map.put("城市", "无");
                }
                if (null != storeType && !"-1".equals(storeType) && !("".equals(storeType)) && null != salesList && salesList.size() > 0) {
                    map.put("门店类型", StoreType.getStoreTypeByValue(storeType).getDescription());
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
                if (isProductCoupon) {
                    map.put("是否包含产品劵", "是");
                } else {
                    map.put("是否包含产品劵", "否");
                }
                if (null != storeId && -1L != storeId) {
                    map.put("门店名称", storeName);
                } else {
                    map.put("门店名称", "无");
                }

                String str1 = "订单参数范围内：　\n" +
                        "包含：①本月出货+本月收全款 ②上月出货+本月收款 ③上月收款+本月出货 ④本月退货反配上架\n" +
                        "不包含： ①本月已取消订单②订单中运费金额⑤本月拒签 \n";

                //设置筛选条件
                ws = this.setCondition(ws, map, titleFormat, shiroName, textFormat);
                ws = this.setExample(ws,titleFormat,textFormat);
                if (storeType.equals("JM") ) {
                    // 加盟

                    //列宽
                    int[] columnView = {10, 20, 15, 10, 10, 30, 10, 15, 15, 15, 15, 15, 20, 15, 15, 15, 15, 15, 15, 15, 15, 15};
                    //列标题城市

                    String[] titles = {"城市", "门店", "门店编码", "名称", "会员名称", "订单号", "退单号", "配送/自提", "订单状态", "订单日期", "出货时间", "是否结清", "订单还清日期", "编号", "商品名称", "品牌", "下单数量", "本赠品", "经销财务销量", "经销单价", "原单价", "产品券类型", "顾客唯一标识", "顾客类型"
                    };
                    //计算标题开始行号
                    int row = 1;
                    if (null != map && map.size() > 0) {
                        row = map.size() / 2 + 4;
                    }

//                    String str1 = "配送单：　\n" +
//                            "1： 订单还清日期 >＝ 挑选日期   并且为已出货\n" +
//                            "2： 订单还清日期 <   挑选日期   并且本月出货\n" +
//                            "3:  出货定义：已完成／待收货\n" +
//                            "\n" +
//                            "自提单：　\n" +
//                            "1： 订单还清日期 >＝ 挑选日期   \n" +
//                            "并且为已出货\n" +
//                            "2： 订单还清日期 <   挑选日期   并且本月出货\n" +
//                            "3:  出货定义：已完成（按出货确认键）";
//                    String str2 = "非产品券 \n" +
//                            "1.财务销量    =[结算价－（折扣或者赠品分摊+现金券）] * 下单数量  \n" +
//                            "2.经销财务销量=[经销价 * 下单数量]  \n" +
//                            "\n" +
//                            "产品券  \n" +
//                            "1. 一笔数据＝一张产品券  \n" +
//                            "2. 财务销量=当时购买产品券的价格，原价及结算价＝当下此产品的单价  \n" +
//                            "若退货则下单数量＝负数";

                    ws.mergeCells(0, map.size(), 5, map.size());
                    WritableCellFormat textFormat1 = this.setTextStyle();
                    textFormat1.setWrap(true);
                    ws.addCell(new Label(0, map.size(), str1, textFormat1));
                    ws.setRowView(row - 1, 2000);

                    ws.mergeCells(6, map.size(), 11, map.size());
                    WritableCellFormat textFormat2 = this.setTextStyle();
                    textFormat2.setWrap(true);
                    //ws.addCell(new Label(6, map.size(), str2, textFormat2));
                    ws.setRowView(row - 1, 2000);

                    row += 8;
                    //设置标题
                    ws = this.setHeader(ws, titleFormat, columnView, titles, row);
                    row += 1;
                    WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"), 9, WritableFont.NO_BOLD, false,
                            UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                    //填写表体数据
                    for (int j = 0; j < maxRowNum; j++) {
                        if (j + i * maxRowNum >= maxSize) {
                            break;
                        }
                        SalesReportDO salesReportDO = salesList.get(j + i * maxRowNum);

                        ws.addCell(new Label(0, j + row, salesReportDO.getCityName(), textFormat));
                        ws.addCell(new Label(1, j + row, salesReportDO.getStoreName(), textFormat));
                        ws.addCell(new Label(2, j + row, salesReportDO.getStoreCode(), textFormat));
                        ws.addCell(new Label(3, j + row, salesReportDO.getName(), textFormat));
                        ws.addCell(new Label(4, j + row, salesReportDO.getCustomerName(), textFormat));
                        ws.addCell(new Label(5, j + row, salesReportDO.getOrdNo(), textFormat));
                        ws.addCell(new Label(6, j + row, salesReportDO.getReturnNo(), textFormat));
                        ws.addCell(new Label(7, j + row, salesReportDO.getOrderType(), textFormat));
                        ws.addCell(new Label(8, j + row, salesReportDO.getOrderStatus(), textFormat));
                        ws.addCell(new Label(9, j + row, salesReportDO.getCreateTime(), textFormat));
                        ws.addCell(new Label(10, j + row, salesReportDO.getShippingDate(), textFormat));
                        ws.addCell(new Label(11, j + row, salesReportDO.getIsPayUp(), textFormat));
                        ws.addCell(new Label(12, j + row, salesReportDO.getPayUpTime(), textFormat));
                        ws.addCell(new Label(13, j + row, salesReportDO.getSku(), textFormat));
                        ws.addCell(new Label(14, j + row, salesReportDO.getSkuName(), textFormat));
                        ws.addCell(new Label(15, j + row, salesReportDO.getCompanyFlag(), textFormat));
                        if (null != salesReportDO.getOrderQty()) {
                            ws.addCell(new Number(16, j + row, salesReportDO.getOrderQty(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                        }
                        if (null != salesReportDO.getGoodsType()) {
                            ws.addCell(new Label(17, j + row, salesReportDO.getGoodsType().toString(), textFormat));
                        }

                        ws.addCell(new Number(18, j + row, Double.parseDouble(salesReportDO.getDistributionSales()), new WritableCellFormat(textFont, new NumberFormat("0.00"))));

                        if (null != salesReportDO.getWholesalePrice()) {
                            ws.addCell(new Number(19, j + row, salesReportDO.getWholesalePrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                        }
                        if (null != salesReportDO.getRetailPrice()) {
                            ws.addCell(new Number(20, j + row, salesReportDO.getRetailPrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                        }

                        if (null != salesReportDO.getCouponType()) {
                            ws.addCell(new Label(21, j + row, salesReportDO.getCouponType().toString(), textFormat));
                        }
                        ws.addCell(new Label(22, j + row, salesReportDO.getCusId().toString(), textFormat));
                        ws.addCell(new Label(23, j + row, salesReportDO.getCusType(), textFormat));
                    }

                } else if (storeType.equals("FX")) {
                    // 分销

                    //列宽
                    int[] columnView = {10, 20, 15, 10, 10, 30, 10, 15, 15, 15, 15, 15, 20, 15, 15, 15, 15, 15, 15, 15, 15,15,15};
                    //列标题城市

                    String[] titles = {"城市", "门店", "门店编码", "名称", "会员名称", "订单号", "退单号", "配送/自提", "订单状态", "订单日期", "出货时间", "是否结清", "订单还清日期", "编号", "商品名称", "品牌", "下单数量", "本赠品", "分销财务销量", "经销单价", "原单价", "产品券类型", "分销门店编码","所属分销仓库", "顾客类型"
                    };
                    //计算标题开始行号
                    int row = 1;
                    if (null != map && map.size() > 0) {
                        row = map.size() / 2 + 4;
                    }
//                    String str1 = "配送单：　\n" +
//                            "1： 订单还清日期 >＝ 挑选日期   并且为已出货\n" +
//                            "2： 订单还清日期 <   挑选日期   并且本月出货\n" +
//                            "3:  出货定义：已完成／待收货\n" +
//                            "\n" +
//                            "自提单：　\n" +
//                            "1： 订单还清日期 >＝ 挑选日期   \n" +
//                            "并且为已出货\n" +
//                            "2： 订单还清日期 <   挑选日期   并且本月出货\n" +
//                            "3:  出货定义：已完成（按出货确认键）";
//                    String str2 = "非产品券 \n" +
//                            "1.财务销量    =[结算价－（折扣或者赠品分摊+现金券）] * 下单数量  \n" +
//                            "2.经销财务销量=[经销价 * 下单数量]  \n" +
//                            "\n" +
//                            "产品券  \n" +
//                            "1. 一笔数据＝一张产品券  \n" +
//                            "2. 财务销量=当时购买产品券的价格，原价及结算价＝当下此产品的单价  \n" +
//                            "若退货则下单数量＝负数";
                    ws.mergeCells(0, (map.size()), 5, (map.size()));
                    WritableCellFormat textFormat1 = this.setTextStyle();
                    textFormat1.setWrap(true);
                    ws.addCell(new Label(0, map.size(), str1, textFormat1));
                    ws.setRowView(row - 1, 2000);

                    ws.mergeCells(6, (map.size()), 11, (map.size()));
                    WritableCellFormat textFormat2 = this.setTextStyle();
                    textFormat2.setWrap(true);
                    //ws.addCell(new Label(6, map.size(), str2, textFormat2));
                    ws.setRowView(row - 1, 2000);
                    row += 8;
                    //设置标题
                    ws = this.setHeader(ws, titleFormat, columnView, titles, row);
                    row += 1;
                    WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"), 9, WritableFont.NO_BOLD, false,
                            UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                    //填写表体数据
                    for (int j = 0; j < maxRowNum; j++) {
                        if (j + i * maxRowNum >= maxSize) {
                            break;
                        }
                        SalesReportDO salesReportDO = salesList.get(j + i * maxRowNum);

                        ws.addCell(new Label(0, j + row, salesReportDO.getCityName(), textFormat));
                        ws.addCell(new Label(1, j + row, salesReportDO.getStoreName(), textFormat));
                        ws.addCell(new Label(2, j + row, salesReportDO.getStoreCode(), textFormat));
                        ws.addCell(new Label(3, j + row, salesReportDO.getName(), textFormat));
                        ws.addCell(new Label(4, j + row, salesReportDO.getCustomerName(), textFormat));
                        ws.addCell(new Label(5, j + row, salesReportDO.getOrdNo(), textFormat));
                        ws.addCell(new Label(6, j + row, salesReportDO.getReturnNo(), textFormat));
                        ws.addCell(new Label(7, j + row, salesReportDO.getOrderType(), textFormat));
                        ws.addCell(new Label(8, j + row, salesReportDO.getOrderStatus(), textFormat));
                        ws.addCell(new Label(9, j + row, salesReportDO.getCreateTime(), textFormat));
                        ws.addCell(new Label(10, j + row, salesReportDO.getShippingDate(), textFormat));
                        ws.addCell(new Label(11, j + row, salesReportDO.getIsPayUp(), textFormat));
                        ws.addCell(new Label(12, j + row, salesReportDO.getPayUpTime(), textFormat));
                        ws.addCell(new Label(13, j + row, salesReportDO.getSku(), textFormat));
                        ws.addCell(new Label(14, j + row, salesReportDO.getSkuName(), textFormat));
                        ws.addCell(new Label(15, j + row, salesReportDO.getCompanyFlag(), textFormat));
                        if (null != salesReportDO.getOrderQty()) {
                            ws.addCell(new Number(16, j + row, salesReportDO.getOrderQty(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                        }
                        if (null != salesReportDO.getGoodsType()) {
                            ws.addCell(new Label(17, j + row, salesReportDO.getGoodsType().toString(), textFormat));
                        }
                        String storeTypeStr = salesReportDO.getStoreType();

                        if (StringUtils.isNotBlank(storeTypeStr) && storeTypeStr.equals("FX")) {
                            ws.addCell(new Number(18, j + row, Double.parseDouble(salesReportDO.getDistributionSales()), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                            if (null != salesReportDO.getWholesalePrice()) {
                                ws.addCell(new Number(19, j + row, salesReportDO.getWholesalePrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                            }
                        } else {
                            ws.addCell(new Number(18, j + row, Double.parseDouble(salesReportDO.getFinancialSales()), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                            if (null != salesReportDO.getSettlementPrice()) {
                                ws.addCell(new Number(19, j + row, salesReportDO.getSettlementPrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                            }
                        }

                        if (null != salesReportDO.getRetailPrice()) {
                            ws.addCell(new Number(20, j + row, salesReportDO.getRetailPrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                        }
                        if (null != salesReportDO.getCouponType()) {
                            ws.addCell(new Label(21, j + row, salesReportDO.getCouponType().toString(), textFormat));
                        }
                        if (null != salesReportDO.getFxStoreCode()) {
                            ws.addCell(new Label(22, j + row, salesReportDO.getFxStoreCode().toString(), textFormat));
                        }
                        ws.addCell(new Label(23, j + row, salesReportDO.getFxCkName(), textFormat));
                        ws.addCell(new Label(24, j + row, salesReportDO.getCusType(), textFormat));
                    }
                }else if (storeType.equals("ZS")){

                    //列宽
                    int[] columnView = {10, 20, 15, 10, 10, 30, 15, 15, 15, 15, 15, 20, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15,15,15,15};
                    //列标题城市

                    String[] titles = {"城市", "门店", "门店编码", "名称", "会员名称", "订单号", "退货单号", "配送/自提", "订单状态", "订单日期", "出货时间", "是否结清", "订单还清日期", "编号", "商品名称", "品牌", "下单数量", "本赠品", "财务销量","经销财务销量", "原单价", "结算单价", "会员折扣", "折扣或者赠品分摊", "现金券", "产品券类型"
                            ,"顾客唯一标识","顾客类型"};
                    //计算标题开始行号
                    int row = 1;
                    if (null != map && map.size() > 0) {
                        row = map.size()  / 2 + 4;
                    }
//                    String str1 = "配送单：　\n" +
//                            "1： 订单还清日期 >＝ 挑选日期   并且为已出货\n" +
//                            "2： 订单还清日期 <   挑选日期   并且本月出货\n" +
//                            "3:  出货定义：已完成／待收货\n" +
//                            "\n" +
//                            "自提单：　\n" +
//                            "1： 订单还清日期 >＝ 挑选日期   \n" +
//                            "并且为已出货\n" +
//                            "2： 订单还清日期 <   挑选日期   并且本月出货\n" +
//                            "3:  出货定义：已完成（按出货确认键）";
//                    String str2 = "非产品券 \n" +
//                            "1.财务销量    =[结算价－（折扣或者赠品分摊+现金券）] * 下单数量  \n" +
//                            "2.经销财务销量=[经销价 * 下单数量]  \n" +
//                            "\n" +
//                            "产品券  \n" +
//                            "1. 一笔数据＝一张产品券  \n" +
//                            "2. 财务销量=当时购买产品券的价格，原价及结算价＝当下此产品的单价  \n" +
//                            "若退货则下单数量＝负数";
                    ws.mergeCells(0, map.size() , 5, map.size());
                    WritableCellFormat textFormat1 = this.setTextStyle();
                    textFormat1.setWrap(true);
                    ws.addCell(new Label(0, map.size() , str1, textFormat1));
                    ws.setRowView(row - 1, 2000);

                    ws.mergeCells(6, map.size() , 11, map.size());
                    WritableCellFormat textFormat2 = this.setTextStyle();
                    textFormat2.setWrap(true);
                    //ws.addCell(new Label(6, map.size(), str2, textFormat2));
                    ws.setRowView(row - 1, 2000);
                    row += 8;
                    //设置标题
                    ws = this.setHeader(ws, titleFormat, columnView, titles, row);
                    row += 1;
                    WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"), 9, WritableFont.NO_BOLD, false,
                            UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                    //填写表体数据
                    for (int j = 0; j < maxRowNum; j++) {
                        if (j + i * maxRowNum >= maxSize) {
                            break;
                        }
                        SalesReportDO salesReportDO = salesList.get(j + i * maxRowNum);

                        ws.addCell(new Label(0, j + row, salesReportDO.getCityName(), textFormat));
                        ws.addCell(new Label(1, j + row, salesReportDO.getStoreName(), textFormat));
                        ws.addCell(new Label(2, j + row, salesReportDO.getStoreCode(), textFormat));
                        ws.addCell(new Label(3, j + row, salesReportDO.getName(), textFormat));
                        ws.addCell(new Label(4, j + row, salesReportDO.getCustomerName(), textFormat));
                        ws.addCell(new Label(5, j + row, salesReportDO.getOrdNo(), textFormat));
                        ws.addCell(new Label(6, j + row, salesReportDO.getReturnNo(), textFormat));
                        ws.addCell(new Label(7, j + row, salesReportDO.getOrderType(), textFormat));
                        ws.addCell(new Label(8, j + row, salesReportDO.getOrderStatus(), textFormat));
                        ws.addCell(new Label(9, j + row, salesReportDO.getCreateTime(), textFormat));
                        ws.addCell(new Label(10, j + row, salesReportDO.getShippingDate(), textFormat));
                        ws.addCell(new Label(11, j + row, salesReportDO.getIsPayUp(), textFormat));
                        ws.addCell(new Label(12, j + row, salesReportDO.getPayUpTime(), textFormat));
                        ws.addCell(new Label(13, j + row, salesReportDO.getSku(), textFormat));
                        ws.addCell(new Label(14, j + row, salesReportDO.getSkuName(), textFormat));
                        ws.addCell(new Label(15, j + row, salesReportDO.getCompanyFlag(), textFormat));
                        if (null != salesReportDO.getOrderQty()) {
                            ws.addCell(new Number(16, j + row, salesReportDO.getOrderQty(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                        }
                        if (null != salesReportDO.getGoodsType()) {
                            ws.addCell(new Label(17, j + row, salesReportDO.getGoodsType().toString(), textFormat));
                        }

                        ws.addCell(new Number(18, j + row, Double.parseDouble(salesReportDO.getFinancialSales()), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                        ws.addCell(new Number(19, j + row, Double.parseDouble(salesReportDO.getDistributionSales()), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                        if (null != salesReportDO.getRetailPrice()) {
                            ws.addCell(new Number(20, j + row, salesReportDO.getRetailPrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                        }
                        if (null != salesReportDO.getSettlementPrice()) {
                            ws.addCell(new Number(21, j + row, salesReportDO.getSettlementPrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                        }
                        if (null != salesReportDO.getMemberDiscount()) {
                            ws.addCell(new Number(22, j + row, salesReportDO.getMemberDiscount(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                        }
                        if (null != salesReportDO.getPromotionSharePrice()) {
                            ws.addCell(new Number(23, j + row, salesReportDO.getPromotionSharePrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                        }
                        if (null != salesReportDO.getCashCouponSharePrice()) {
                            ws.addCell(new Number(24, j + row, salesReportDO.getCashCouponSharePrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                        }
                        if (null != salesReportDO.getCouponType()) {
                            ws.addCell(new Label(25, j + row, salesReportDO.getCouponType().toString(), textFormat));
                        }
                        ws.addCell(new Label(26, j + row, salesReportDO.getCusId().toString(), textFormat));
                        ws.addCell(new Label(27, j + row, salesReportDO.getCusType(), textFormat));
                    }

                } else {

                    //列宽
                    int[] columnView = {10, 20, 15, 10, 10, 30, 15, 15, 15, 15, 15, 20, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15};
                    //列标题城市

                    String[] titles = {"城市", "门店", "门店编码", "名称", "会员名称", "订单号", "退货单号", "配送/自提", "订单状态", "订单日期", "出货时间", "是否结清", "订单还清日期", "编号", "商品名称", "品牌", "下单数量", "本赠品", "财务销量", "原单价", "结算单价", "会员折扣", "折扣或者赠品分摊", "现金券", "产品券类型"
                            , "顾客唯一标识", "顾客类型"};
                    //计算标题开始行号
                    int row = 1;
                    if (null != map && map.size() > 0) {
                        row = map.size() / 2 + 4;
                    }
//                    String str1 = "配送单：　\n" +
//                            "1： 订单还清日期 >＝ 挑选日期   并且为已出货\n" +
//                            "2： 订单还清日期 <   挑选日期   并且本月出货\n" +
//                            "3:  出货定义：已完成／待收货\n" +
//                            "\n" +
//                            "自提单：　\n" +
//                            "1： 订单还清日期 >＝ 挑选日期   \n" +
//                            "并且为已出货\n" +
//                            "2： 订单还清日期 <   挑选日期   并且本月出货\n" +
//                            "3:  出货定义：已完成（按出货确认键）";
//                    String str2 = "非产品券 \n" +
//                            "1.财务销量    =[结算价－（折扣或者赠品分摊+现金券）] * 下单数量  \n" +
//                            "2.经销财务销量=[经销价 * 下单数量]  \n" +
//                            "\n" +
//                            "产品券  \n" +
//                            "1. 一笔数据＝一张产品券  \n" +
//                            "2. 财务销量=当时购买产品券的价格，原价及结算价＝当下此产品的单价  \n" +
//                            "若退货则下单数量＝负数";
                    ws.mergeCells(0, map.size(), 5, map.size());
                    WritableCellFormat textFormat1 = this.setTextStyle();
                    textFormat1.setWrap(true);
                    ws.addCell(new Label(0, map.size(), str1, textFormat1));
                    ws.setRowView(row - 1, 2000);

                    ws.mergeCells(6, map.size(), 11, map.size());
                    WritableCellFormat textFormat2 = this.setTextStyle();
                    textFormat2.setWrap(true);
                    //ws.addCell(new Label(6, map.size(), str2, textFormat2));
                    ws.setRowView(row - 1, 2000);
                    row += 8;
                    //设置标题
                    ws = this.setHeader(ws, titleFormat, columnView, titles, row);
                    row += 1;
                    WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"), 9, WritableFont.NO_BOLD, false,
                            UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                    //填写表体数据
                    for (int j = 0; j < maxRowNum; j++) {
                        if (j + i * maxRowNum >= maxSize) {
                            break;
                        }
                        SalesReportDO salesReportDO = salesList.get(j + i * maxRowNum);

                        ws.addCell(new Label(0, j + row, salesReportDO.getCityName(), textFormat));
                        ws.addCell(new Label(1, j + row, salesReportDO.getStoreName(), textFormat));
                        ws.addCell(new Label(2, j + row, salesReportDO.getStoreCode(), textFormat));
                        ws.addCell(new Label(3, j + row, salesReportDO.getName(), textFormat));
                        ws.addCell(new Label(4, j + row, salesReportDO.getCustomerName(), textFormat));
                        ws.addCell(new Label(5, j + row, salesReportDO.getOrdNo(), textFormat));
                        ws.addCell(new Label(6, j + row, salesReportDO.getReturnNo(), textFormat));
                        ws.addCell(new Label(7, j + row, salesReportDO.getOrderType(), textFormat));
                        ws.addCell(new Label(8, j + row, salesReportDO.getOrderStatus(), textFormat));
                        ws.addCell(new Label(9, j + row, salesReportDO.getCreateTime(), textFormat));
                        ws.addCell(new Label(10, j + row, salesReportDO.getShippingDate(), textFormat));
                        ws.addCell(new Label(11, j + row, salesReportDO.getIsPayUp(), textFormat));
                        ws.addCell(new Label(12, j + row, salesReportDO.getPayUpTime(), textFormat));
                        ws.addCell(new Label(13, j + row, salesReportDO.getSku(), textFormat));
                        ws.addCell(new Label(14, j + row, salesReportDO.getSkuName(), textFormat));
                        ws.addCell(new Label(15, j + row, salesReportDO.getCompanyFlag(), textFormat));
                        if (null != salesReportDO.getOrderQty()) {
                            ws.addCell(new Number(16, j + row, salesReportDO.getOrderQty(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                        }
                        if (null != salesReportDO.getGoodsType()) {
                            ws.addCell(new Label(17, j + row, salesReportDO.getGoodsType().toString(), textFormat));
                        }

                        ws.addCell(new Number(18, j + row, Double.parseDouble(salesReportDO.getFinancialSales()), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                        if (null != salesReportDO.getRetailPrice()) {
                            ws.addCell(new Number(19, j + row, salesReportDO.getRetailPrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                        }
                        if (null != salesReportDO.getSettlementPrice()) {
                            ws.addCell(new Number(20, j + row, salesReportDO.getSettlementPrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                        }
                        if (null != salesReportDO.getMemberDiscount()) {
                            ws.addCell(new Number(21, j + row, salesReportDO.getMemberDiscount(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                        }
                        if (null != salesReportDO.getPromotionSharePrice()) {
                            ws.addCell(new Number(22, j + row, salesReportDO.getPromotionSharePrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                        }
                        if (null != salesReportDO.getCashCouponSharePrice()) {
                            ws.addCell(new Number(23, j + row, salesReportDO.getCashCouponSharePrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                        }
                        if (null != salesReportDO.getCouponType()) {
                            ws.addCell(new Label(24, j + row, salesReportDO.getCouponType().toString(), textFormat));
                        }
                        ws.addCell(new Label(25, j + row, salesReportDO.getCusId().toString(), textFormat));
                        ws.addCell(new Label(26, j + row, salesReportDO.getCusType(), textFormat));
                    }

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
     * @param
     * @return
     * @throws
     * @title 装饰公司对账单下载
     * @descripe
     * @author GenerationRoad
     * @date 2018/4/19
     */
    @GetMapping(value = "/creditBilling/download")
    public void downloadCompanyCreditBilling(HttpServletRequest request, HttpServletResponse response, Long id) {
        DecorationCompanyCreditBillingVO creditBillingVO = this.maDecorationCompanyCreditBillingService.getDecorationCompanyCreditBillingById(id);
        List<DecorationCompanyCreditBillingDetailsVO> creditBillingDetailsVOS = null;
        List<AccountGoodsItemsDO> goodsItemsDOS = null;
        if (null != creditBillingVO) {
            creditBillingDetailsVOS = this.maDecorationCompanyCreditBillingService.getDecorationCompanyCreditBillingDetailsByCreditBillingNo(creditBillingVO.getCreditBillingNo());
            goodsItemsDOS = this.maDecorationCompanyCreditBillingService.findGoodsItemsDOAll(creditBillingVO.getCreditBillingNo());
        }

        response.setContentType("text/html;charset=UTF-8");
        //创建名称
        String fileurl = "装饰公司对账单-" + DateUtils.getCurrentTimeStr("yyyyMMddHHmmss") + ".xls";//如  D:/xx/xx/xxx.xls

        WritableWorkbook wwb = null;
        try {
            //创建文件
            wwb = exportXML(fileurl, response);

            //标题格式
            WritableCellFormat titleFormat = this.setTitleStyle();
            //正文格式
            WritableCellFormat textFormat = this.setTextStyle();

            //excel单表最大行数是65535
            int maxSize = 0;
            if (null != goodsItemsDOS) {
                maxSize = goodsItemsDOS.size();
            }
            int sheets2 = maxSize / maxRowNum + 1;
            //设置excel的sheet数
            for (int i = 0; i < sheets2; i++) {

                //工作表，参数0表示这是第一页
                WritableSheet ws = wwb.createSheet("账单明细（第" + (i + 1) + "页）", i);

                //列宽
                int[] columnView = {30, 15, 15, 15, 15, 50, 15, 20, 15, 15, 20,15,20};
                //列标题
                String[] titles = {"订单号", "订单日期", "出货日期", "退货日期", "收货人姓名", "收货人地址", "SKU", "商品名称", "数量", "结算单价", "结算总价","成交单价","成交总价"};
                //计算标题开始行号
                int row = 0;

                //设置标题
                ws = this.setHeader(ws, titleFormat, columnView, titles, row);
                row += 1;
                WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"), 9, WritableFont.NO_BOLD, false,
                        UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                //填写表体数据
                for (int j = 0; j < maxRowNum; j++) {
                    if (j + i * maxRowNum >= maxSize) {
                        break;
                    }
                    AccountGoodsItemsDO goodsItemsDO = goodsItemsDOS.get(j + i * maxRowNum);
                    ws.addCell(new Label(0, j + row, goodsItemsDO.getOrderNumber(), textFormat));
                    ws.addCell(new Label(1, j + row, goodsItemsDO.getOrderTime(), textFormat));
                    ws.addCell(new Label(2, j + row, goodsItemsDO.getShippingTime(), textFormat));
                    ws.addCell(new Label(3, j + row, goodsItemsDO.getReturnTime(), textFormat));
                    ws.addCell(new Label(4, j + row, goodsItemsDO.getCustomerName(), textFormat));
                    ws.addCell(new Label(5, j + row, goodsItemsDO.getShippingAddress(), textFormat));
                    ws.addCell(new Label(6, j + row, goodsItemsDO.getSku(), textFormat));
                    ws.addCell(new Label(7, j + row, goodsItemsDO.getSkuName(), textFormat));
                    ws.addCell(new Number(8, j + row, goodsItemsDO.getQuantity(), textFormat));
                    ws.addCell(new Number(9, j + row, goodsItemsDO.getSettlementPrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(10, j + row, goodsItemsDO.getSettlementTotlePrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(11, j + row, goodsItemsDO.getJxPrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(12, j + row, goodsItemsDO.getTotalJxPrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                }
            }

            //excel单表最大行数是65535
            maxSize = 0;
            if (null != creditBillingDetailsVOS) {
                creditBillingDetailsVOS.forEach(p -> {
                    if (p.getOrderNumber().startsWith("T")) {
                        p.setCreditMoney(p.getCreditMoney() < 0 ? p.getCreditMoney() : (p.getCreditMoney() * -1));
                        p.setGoodsQty(p.getGoodsQty() < 0 ? p.getGoodsQty() : (p.getGoodsQty() * -1));
                    }
                });
                maxSize = creditBillingDetailsVOS.size();
            }
            int sheets = maxSize / maxRowNum + 1;
            //设置excel的sheet数
            for (int i = 0; i < sheets; i++) {


                //工作表，参数0表示这是第一页
                WritableSheet ws = wwb.createSheet("账单（第" + (i + 1) + "页）", i);
                User user = new User();
                user = userService.queryById(creditBillingVO.getOperationId());
                ws.mergeCells(0, 0, 6, 0);
                ws.addCell(new Label(0, 0, creditBillingVO.getBillName(), titleFormat));
                ws.addCell(new Label(0, 1, "账单编号", textFormat));
                ws.addCell(new Label(1, 1, creditBillingVO.getCreditBillingNo(), textFormat));
                ws.addCell(new Label(3, 1, "制单时间", textFormat));
                ws.addCell(new Label(4, 1, creditBillingVO.getCreateTime(), textFormat));
                ws.addCell(new Label(6, 1, "制单人", textFormat));
                ws.addCell(new Label(7, 1, user.getName(), textFormat));

                //列宽
                int[] columnView = {10, 30, 15, 15, 50, 40,40, 10, 15,15, 10,};
                //列标题
                String[] titles = {"序号", "订单号", "出&退货日期", "收货人姓名", "收货人地址","楼盘信息","备注", "商品总数", "结算总价","成交总价", "装饰经理"};
                //计算标题开始行号
                int row = 3;

                //设置标题
                ws = this.setHeader(ws, titleFormat, columnView, titles, row);
                row += 1;
                WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"), 9, WritableFont.NO_BOLD, false,
                        UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                Double credit = 0D;
                Double totalAccountMoney = 0D;
                Integer goodsQty = 0;
                Integer rows = row;
                //填写表体数据
                for (int j = 0; j < maxRowNum; j++) {
                    if (j + i * maxRowNum >= maxSize) {
                        break;
                    }
                    DecorationCompanyCreditBillingDetailsVO creditBillingDetailsVO = creditBillingDetailsVOS.get(j + i * maxRowNum);
                    ws.addCell(new Number(0, j + row, (j + 1), textFormat));
                    ws.addCell(new Label(1, j + row, creditBillingDetailsVO.getOrderNumber(), textFormat));
                    ws.addCell(new Label(2, j + row, creditBillingDetailsVO.getCreateTime(), textFormat));
                    ws.addCell(new Label(3, j + row, creditBillingDetailsVO.getReceiver(), textFormat));
                    ws.addCell(new Label(4, j + row, creditBillingDetailsVO.getDeliveryAddress(), textFormat));
                    ws.addCell(new Label(5, j + row, creditBillingDetailsVO.getEstateInfo(), textFormat));
                    ws.addCell(new Label(6, j + row, creditBillingDetailsVO.getRemark(), textFormat));
                    ws.addCell(new Number(7, j + row, creditBillingDetailsVO.getGoodsQty()));
                    ws.addCell(new Number(8, j + row, creditBillingDetailsVO.getCreditMoney(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(9, j + row, creditBillingDetailsVO.getAccountMoney(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Label(10, j + row, creditBillingDetailsVO.getSalesManagerName(), textFormat));
                    credit = CountUtil.add(credit, null == creditBillingDetailsVO.getCreditMoney() ? 0D : creditBillingDetailsVO.getCreditMoney());
                    totalAccountMoney = CountUtil.add(totalAccountMoney, null == creditBillingDetailsVO.getAccountMoney() ? 0D : creditBillingDetailsVO.getAccountMoney());
                    goodsQty += null == creditBillingDetailsVO.getGoodsQty() ? 0 : creditBillingDetailsVO.getGoodsQty();
                    rows += 1;
                }
                ws.addCell(new Label(6, rows, "总计", titleFormat));
                ws.addCell(new Number(7, rows, goodsQty, textFormat));
                ws.addCell(new Number(8, rows, credit, new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                ws.addCell(new Number(9, rows, totalAccountMoney, new WritableCellFormat(textFont, new NumberFormat("0.00"))));
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
     * 未提货报表
     *
     * @param request
     * @param response
     * @param cityId
     * @param storeId
     * @param storeType

     */
    @GetMapping(value = "/expiringSoonProduct/download")
    public void notPickGoodsDownload(HttpServletRequest request, HttpServletResponse response, Long cityId, Long storeId, String storeType, String cusName) {
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        if(null !=storeId && -1L !=storeId){
            storeIds.clear();
            storeIds.add(storeId);
        }
        List<CusExpiringSoonProductCouponInfo> cusExpiringSoonProductCouponList = this.maReportDownloadService.downloadExpiringSoonProduct(cityId, storeId, storeType, cusName, storeIds);
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String shiroName = "";
        if (null != shiroUser) {
            shiroName = shiroUser.getName();
        }
        response.setContentType("text/html;charset=UTF-8");
        //创建名称
        String fileurl = "产品券将逾期报表-" + DateUtil.getCurrentTimeStr("yyyyMMddHHmmss") + ".xls";//如  D:/xx/xx/xxx.xls

        WritableWorkbook wwb = null;
        try {
            //创建文件
            wwb = exportXML(fileurl, response);

            //excel单表最大行数是65535
            int maxSize = 0;
            if (cusExpiringSoonProductCouponList != null) {
                maxSize = cusExpiringSoonProductCouponList.size();
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
                if (null != cityId && !(cityId.equals(-1L)) && null != cusExpiringSoonProductCouponList && cusExpiringSoonProductCouponList.size() > 0) {
                    map.put("城市", cusExpiringSoonProductCouponList.get(0).getCityName());
                } else {
                    map.put("城市", "无");
                }
                if (null != storeId && !(storeId.equals(-1L)) && null != cusExpiringSoonProductCouponList && cusExpiringSoonProductCouponList.size() > 0) {
                    map.put("门店", cusExpiringSoonProductCouponList.get(0).getStoreName());
                } else {
                    map.put("门店", "无");
                }
                if (null != storeType && !("".equals(storeType)) && null != cusExpiringSoonProductCouponList && cusExpiringSoonProductCouponList.size() > 0) {
                    map.put("门店类型", cusExpiringSoonProductCouponList.get(0).getStoreType());
                } else {
                    map.put("门店类型", "无");
                }
                if (null != cusName && !("".equals(cusName)) && null != cusExpiringSoonProductCouponList && cusExpiringSoonProductCouponList.size() > 0) {
                    map.put("顾客姓名", cusExpiringSoonProductCouponList.get(0).getCustomerName());
                } else {
                    map.put("顾客姓名", "无");
                }
                //设置筛选条件
                ws = this.setCondition(ws, map, titleFormat, shiroName, textFormat);

                //列宽
                int[] columnView = {10, 15, 10, 10, 10, 15, 20, 20, 15, 20, 20, 20, 10, 10, 10, 10, 10, 10, 30};
                //列标题
                String[] titles = {"城市","门店类型", "门店名称", "顾客","导购","sku", "商品名称", "获取时间","产品券获取途径","买劵单号", "生效开始时间", "生效结束时间",
                        "购买价格","购买数量", "购买总价","商品标志"};

                //计算标题开始行号
                int row = 1;
                if (null != map && map.size() > 0) {
                    row = (map.size() + 1) / 2 + 4;
                }

                int collectRow = row + 1;
//
                row += 2;
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
                    CusExpiringSoonProductCouponInfo cusExpiringSoonProductCouponInfo = cusExpiringSoonProductCouponList.get(j + i * maxRowNum);
                    ws.addCell(new Label(0, j + row, cusExpiringSoonProductCouponInfo.getCityName(), textFormat));
                    ws.addCell(new Label(1, j + row, cusExpiringSoonProductCouponInfo.getStoreType(), textFormat));
                    ws.addCell(new Label(2, j + row, cusExpiringSoonProductCouponInfo.getStoreName(), textFormat));
                    ws.addCell(new Label(3, j + row, cusExpiringSoonProductCouponInfo.getCustomerName(), textFormat));
                    ws.addCell(new Label(4, j + row, cusExpiringSoonProductCouponInfo.getSellerName(), textFormat));
                    ws.addCell(new Label(5, j + row, cusExpiringSoonProductCouponInfo.getSku(), textFormat));
                    ws.addCell(new Label(6, j + row, cusExpiringSoonProductCouponInfo.getSkuName(), textFormat));
                    ws.addCell(new Label(7, j + row, cusExpiringSoonProductCouponInfo.getGetTime(), textFormat));
                    ws.addCell(new Label(8, j + row, cusExpiringSoonProductCouponInfo.getGetType(), textFormat));
                    ws.addCell(new Label(9, j + row, cusExpiringSoonProductCouponInfo.getGetOrderNumber(), textFormat));
                    ws.addCell(new Label(10, j + row, cusExpiringSoonProductCouponInfo.getEffectiveStartTime(), textFormat));
                    ws.addCell(new Label(11, j + row, cusExpiringSoonProductCouponInfo.getEffectiveEndTime().toString(), textFormat));
                    ws.addCell(new Number(12, j + row, cusExpiringSoonProductCouponInfo.getBuyPrice(), textFormat));
                    ws.addCell(new Number(13, j + row, cusExpiringSoonProductCouponInfo.getQuantity(), textFormat));
                    if(null !=cusExpiringSoonProductCouponInfo.getBuyPrice() && null!=cusExpiringSoonProductCouponInfo.getQuantity()){
                        ws.addCell(new Number(14, j + row, cusExpiringSoonProductCouponInfo.getBuyPrice()*cusExpiringSoonProductCouponInfo.getQuantity(), textFormat));
                    }
                    if("COMMON".equals(cusExpiringSoonProductCouponInfo.getGoodsSign())){
                        ws.addCell(new Label(14, j + row, "普通", textFormat));
                    }else {
                        ws.addCell(new Label(14, j + row, "专供", textFormat));
                    }

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
     * @param
     * @return
     * @throws
     * @title 创建文件
     * @descripe
     * @author GenerationRoad
     * @date 2018/3/24
     */
    public WritableWorkbook getWorkbook(String fileurl) {
        WritableWorkbook wwb = null;
        try {
            File file = new File(fileurl);
            if (!file.exists()) {
                file.createNewFile();
            }
            //打开文件
            wwb = Workbook.createWorkbook(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wwb;
    }

    /**
     * @param
     * @return
     * @throws
     * @title 添加筛选条件
     * @descripe
     * @author GenerationRoad
     * @date 2018/3/24
     */
    public WritableSheet setCondition(WritableSheet ws, Map<String, String> map, WritableCellFormat titleFormat, String shiroName, WritableCellFormat textFormat) {
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
            ws.addCell(new Label(4, row, DateUtils.getDateTimeStr(new Date()), textFormat));

            ws.addCell(new Label(6, row, "导出人:", titleFormat));
            ws.addCell(new Label(7, row, shiroName, textFormat));

        } catch (WriteException e) {
            e.printStackTrace();
        }

        return ws;
    }

    /**
     * 案列
     * @param ws
     * @param titleFormat
     * @param textFormat
     * @return
     */
    public WritableSheet setExample(WritableSheet ws, WritableCellFormat titleFormat, WritableCellFormat textFormat) {
        try {
            ws.addCell(new Label(0, 8, "案例说明:", titleFormat));
            int row = 0;
            int column = 1;
            ws.addCell(new Label(1, 8, "", titleFormat));
            ws.addCell(new Label(2, 8, "零售价 \n100", titleFormat));
            ws.addCell(new Label(3, 8, "VIP价\n \n90", titleFormat));
            ws.addCell(new Label(4, 8, "经销价 \n80", titleFormat));
            ws.addCell(new Label(5, 8, "结算价 ", titleFormat));
            ws.addCell(new Label(6, 8, "成交价 ", titleFormat));
            ws.addCell(new Label(7, 8, "经销差价 ", titleFormat));

            ws.addCell(new Label(1, 9, "直营会员下单", titleFormat));
            ws.addCell(new Label(2, 9, "100", textFormat));
            ws.addCell(new Label(3, 9, "90", textFormat));
            ws.addCell(new Label(4, 9, "无", textFormat));
            ws.addCell(new Label(5, 9, "90", textFormat));
            ws.addCell(new Label(6, 9, "90 ", textFormat));
            ws.addCell(new Label(7, 9, "无 ", textFormat));

            ws.addCell(new Label(1, 10, "加盟会员下单", titleFormat));
            ws.addCell(new Label(2, 10, "100", textFormat));
            ws.addCell(new Label(3, 10, "90", textFormat));
            ws.addCell(new Label(4, 10, "80", textFormat));
            ws.addCell(new Label(5, 10, "90", textFormat));
            ws.addCell(new Label(6, 10, "80 ", textFormat));
            ws.addCell(new Label(7, 10, "10 ", textFormat));

            ws.addCell(new Label(1, 11, "直营零售会员下单", titleFormat));
            ws.addCell(new Label(2, 11, "100", textFormat));
            ws.addCell(new Label(3, 11, "无", textFormat));
            ws.addCell(new Label(4, 11, "无", textFormat));
            ws.addCell(new Label(5, 11, "100", textFormat));
            ws.addCell(new Label(6, 11, "100 ", textFormat));
            ws.addCell(new Label(7, 11, "无 ", textFormat));

            ws.addCell(new Label(1, 12, "加盟零售会员下单", titleFormat));
            ws.addCell(new Label(2, 12, "100", textFormat));
            ws.addCell(new Label(3, 12, "90", textFormat));
            ws.addCell(new Label(4, 12, "80", textFormat));
            ws.addCell(new Label(5, 12, "100", textFormat));
            ws.addCell(new Label(6, 12, "80 ", textFormat));
            ws.addCell(new Label(7, 12, "20 ", textFormat));

        } catch (WriteException e) {
            e.printStackTrace();
        }

        return ws;
    }

    /**
     * @param
     * @return
     * @throws
     * @title 设置样式
     * @descripe
     * @author GenerationRoad
     * @date 2018/3/24
     */
    public WritableCellFormat setTitleStyle() {
        try {
            WritableFont titleFont = new WritableFont(WritableFont.createFont("微软雅黑"), 9, WritableFont.BOLD, false,
                    UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            WritableCellFormat titleFormat = new WritableCellFormat();
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

    public WritableCellFormat setTextStyle() {
        WritableFont titleFont = new WritableFont(WritableFont.createFont("微软雅黑"), 9, WritableFont.NO_BOLD, false,
                UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
        WritableCellFormat titleText = new WritableCellFormat();
        //设置字体格式
        titleText.setFont(titleFont);

        return titleText;
    }

    public WritableCellFormat setNumberStyle() {
        WritableFont titleFont = new WritableFont(WritableFont.createFont("微软雅黑"), 9, WritableFont.NO_BOLD, false,
                UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
        WritableCellFormat text = new WritableCellFormat(new NumberFormat("0.00"));
        //设置字体格式
        text.setFont(titleFont);
        return text;
    }

    /**
     * @param
     * @return
     * @throws
     * @title 添加标题
     * @descripe
     * @author GenerationRoad
     * @date 2018/3/24
     */
    public WritableSheet setHeader(WritableSheet ws, WritableCellFormat titleFormat, int[] columnView, String[] titles, int row) {
        try {
            //设置列宽
            if (null != columnView) {
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

    /**
     * @param
     * @return
     * @throws
     * @title 专供订单明细报表
     * @descripe
     * @author GenerationRoad
     * @date 2018/6/8
     */
    @GetMapping(value = "/accountZGGoodsItems/download")
    public void downloadAccountZGGoodsItems(HttpServletRequest request, HttpServletResponse response, Long cityId, Long storeId,
                                            String startTime, String endTime, String keywords) {
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        List<AccountGoodsItemsDO> accountGoodsItemsDOList = this.maReportDownloadService.downloadAccountZGGoodsItems(cityId, storeId, startTime,
                endTime, keywords, storeIds);
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String shiroName = "";
        if (null != shiroUser) {
            shiroName = shiroUser.getName();
        }

        response.setContentType("text/html;charset=UTF-8");
        //创建名称
        String fileurl = "专供对账商品明细报表-" + DateUtils.getCurrentTimeStr("yyyyMMddHHmmss") + ".xls";//如  D:/xx/xx/xxx.xls

        WritableWorkbook wwb = null;
        try {
            //创建文件
            wwb = exportXML(fileurl, response);

            //excel单表最大行数是65535
            int maxSize = 0;
            if (null != accountGoodsItemsDOList) {
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
                if (null != cityId && !(cityId.equals(-1L)) && null != accountGoodsItemsDOList && accountGoodsItemsDOList.size() > 0) {
                    map.put("城市", accountGoodsItemsDOList.get(0).getCityName());
                } else {
                    map.put("城市", "无");
                }
                if (null != storeId && !(storeId.equals(-1L)) && null != accountGoodsItemsDOList && accountGoodsItemsDOList.size() > 0) {
                    map.put("门店", accountGoodsItemsDOList.get(0).getStoreName());
                } else {
                    map.put("门店", "无");
                }
                if (null != startTime && !("".equals(startTime))) {
                    map.put("开始时间", startTime);
                } else {
                    map.put("开始时间", "无");
                }
                if (null != keywords && !("".equals(keywords))) {
                    map.put("关键字", keywords);
                } else {
                    map.put("关键字", "无");
                }
                if (null != endTime && !("".equals(endTime))) {
                    map.put("结束时间", endTime);
                } else {
                    map.put("结束时间", "无");
                }
                //设置筛选条件
                ws = this.setCondition(ws, map, titleFormat, shiroName, textFormat);
                //列宽
                int[] columnView = {10, 13, 15, 20, 30, 25, 15, 15, 10, 10, 20, 20, 20, 50, 20, 10, 10, 10, 15, 15, 15, 15, 15, 15};
                //列标题
                String[] titles = {"城市", "门店名称", "公司", "下单/反配上架时间", "订单号", "退单号", "顾客", "顾客电话", "导购姓名", "配送/退货方式",
                        "出/退货状态", "收货/退货人", "收货/退货人电话", "送/退货地址", "产品编码", "产品名称", "产品标识", "产品类型", "数量", "结算单价", "结算总价",
                        "成交单价", "成交总价"};

                //计算标题开始行号
                int row = 1;
                if (null != map && map.size() > 0) {
                    row = (map.size() + 1) / 2 + 4;
                }

                String str = " 订单参数范围内\n包含：①本月已出货的订单②本月已反配上架的订单③本月下单下月出货的订单④本月“已取消订单” \n不包含： ①本月待付款订单  ②订单中运费金额";
                ws.mergeCells(0, map.size(), 5, map.size());
                WritableCellFormat textFormat1 = this.setTextStyle();
                textFormat1.setWrap(true);
                ws.addCell(new Label(0, map.size(), str, textFormat1));
                ws.setRowView(row - 2, 1000);
                //设置标题
                ws = this.setHeader(ws, titleFormat, columnView, titles, row);
                row += 1;
                WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"), 9, WritableFont.NO_BOLD, false,
                        UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                //填写表体数据
                for (int j = 0; j < maxRowNum; j++) {
                    if (j + i * maxRowNum >= maxSize) {
                        break;
                    }
                    AccountGoodsItemsDO accountGoodsItemsDO = accountGoodsItemsDOList.get(j + i * maxRowNum);
                    ws.addCell(new Label(0, j + row, accountGoodsItemsDO.getCityName(), textFormat));
                    ws.addCell(new Label(1, j + row, accountGoodsItemsDO.getStoreName(), textFormat));
                    ws.addCell(new Label(2, j + row, accountGoodsItemsDO.getCompanyName(), textFormat));
                    ws.addCell(new Label(3, j + row, accountGoodsItemsDO.getOrderTime(), textFormat));
                    ws.addCell(new Label(4, j + row, accountGoodsItemsDO.getOrderNumber(), textFormat));
                    ws.addCell(new Label(5, j + row, accountGoodsItemsDO.getReturnNumber(), textFormat));
                    ws.addCell(new Label(6, j + row, accountGoodsItemsDO.getCustomerName(), textFormat));
                    ws.addCell(new Label(7, j + row, accountGoodsItemsDO.getCustomerPhone(), textFormat));
                    ws.addCell(new Label(8, j + row, accountGoodsItemsDO.getSellerName(), textFormat));
                    ws.addCell(new Label(9, j + row, accountGoodsItemsDO.getDeliveryType(), textFormat));
                    ws.addCell(new Label(10, j + row, accountGoodsItemsDO.getDeliveryStatus(), textFormat));
                    ws.addCell(new Label(11, j + row, accountGoodsItemsDO.getReceiver(), textFormat));
                    ws.addCell(new Label(12, j + row, accountGoodsItemsDO.getReceiverPhone(), textFormat));
                    ws.addCell(new Label(13, j + row, accountGoodsItemsDO.getShippingAddress(), textFormat));
                    ws.addCell(new Label(14, j + row, accountGoodsItemsDO.getSku(), textFormat));
                    ws.addCell(new Label(15, j + row, accountGoodsItemsDO.getSkuName(), textFormat));
                    ws.addCell(new Label(16, j + row, accountGoodsItemsDO.getCompanyFlag(), textFormat));
                    ws.addCell(new Label(17, j + row, accountGoodsItemsDO.getGoodsLineType(), textFormat));
                    ws.addCell(new Label(18, j + row, accountGoodsItemsDO.getQuantity().toString(), textFormat));
                    ws.addCell(new Number(19, j + row, accountGoodsItemsDO.getSettlementPrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(20, j + row, accountGoodsItemsDO.getSettlementTotlePrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(21, j + row, accountGoodsItemsDO.getPromotionSharePrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(22, j + row, accountGoodsItemsDO.getPromotionShareTotlePrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
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
     * @param
     * @return
     * @throws
     * @title 配送报表
     * @descripe
     * @author GenerationRoad
     * @date 2018/6/8
     */
    @GetMapping(value = "/distribution/download")
    public void downloadDistribution(HttpServletRequest request, HttpServletResponse response, Long cityId, String wareHouseNo, String deliveryClerkNo,
                                     String startTime, String endTime, String keywords) {
        //查询登录用户门店权限的门店ID
//        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        List<DistributionDO> distributionDOList = this.maReportDownloadService.downloadDistributionDO(cityId, wareHouseNo, deliveryClerkNo, startTime,
                endTime, keywords);
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String shiroName = "";
        if (null != shiroUser) {
            shiroName = shiroUser.getName();
        }

        response.setContentType("text/html;charset=UTF-8");
        //创建名称
        String fileurl = "配送报表-" + DateUtils.getCurrentTimeStr("yyyyMMddHHmmss") + ".xls";//如  D:/xx/xx/xxx.xls

        WritableWorkbook wwb = null;
        try {
            //创建文件
            wwb = exportXML(fileurl, response);

            //excel单表最大行数是65535
            int maxSize = 0;
            if (null != distributionDOList) {
                maxSize = distributionDOList.size();
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
                if (null != cityId && !(cityId.equals(-1L)) && null != distributionDOList && distributionDOList.size() > 0) {
                    map.put("城市", distributionDOList.get(0).getCity());
                } else {
                    map.put("城市", "无");
                }
                if (null != wareHouseNo && !("".equals(wareHouseNo)) && null != distributionDOList && distributionDOList.size() > 0) {
                    map.put("仓库", distributionDOList.get(0).getWhName());
                } else {
                    map.put("仓库", "无");
                }
                if (null != deliveryClerkNo && !("".equals(deliveryClerkNo)) && null != distributionDOList && distributionDOList.size() > 0) {
                    map.put("配送员", distributionDOList.get(0).getDeliveryClerkName());
                } else {
                    map.put("配送员", "无");
                }
                if (null != startTime && !("".equals(startTime))) {
                    map.put("开始时间", startTime);
                } else {
                    map.put("开始时间", "无");
                }
                if (null != keywords && !("".equals(keywords))) {
                    map.put("关键字", keywords);
                } else {
                    map.put("关键字", "无");
                }
                if (null != endTime && !("".equals(endTime))) {
                    map.put("结束时间", endTime);
                } else {
                    map.put("结束时间", "无");
                }
                //设置筛选条件
                ws = this.setCondition(ws, map, titleFormat, shiroName, textFormat);
                //列宽
                int[] columnView = {10, 13, 15, 15, 30, 25, 15, 25, 20, 10, 30, 30, 30, 30, 50, 35};
                //列标题
                String[] titles = {"仓库", "配送员名称", "配送员电话", "城市", "门店", "门店编码", "导购名称", "订单号", "退单号", "物流状态",
                        "订单时间/退货申请时间", "出货时间/确认收货时间/拒签时间", "配送完成时间/反配时间", "APP处理出货时间", "送/退货地址", "备注"};

                //计算标题开始行号
                int row = 1;
                if (null != map && map.size() > 0) {
                    row = (map.size() + 1) / 2 + 4;
                }

                //设置标题
                ws = this.setHeader(ws, titleFormat, columnView, titles, row);
                row += 1;
                WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"), 9, WritableFont.NO_BOLD, false,
                        UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                //填写表体数据
                for (int j = 0; j < maxRowNum; j++) {
                    if (j + i * maxRowNum >= maxSize) {
                        break;
                    }
                    DistributionDO distributionDO = distributionDOList.get(j + i * maxRowNum);
                    ws.addCell(new Label(0, j + row, distributionDO.getWhName(), textFormat));
                    ws.addCell(new Label(1, j + row, distributionDO.getDeliveryClerkName(), textFormat));
                    ws.addCell(new Label(2, j + row, distributionDO.getDeliveryClerkPhone(), textFormat));
                    ws.addCell(new Label(3, j + row, distributionDO.getCity(), textFormat));
                    ws.addCell(new Label(4, j + row, distributionDO.getStoreName(), textFormat));
                    ws.addCell(new Label(5, j + row, distributionDO.getStoreCode(), textFormat));
                    ws.addCell(new Label(6, j + row, distributionDO.getSellerName(), textFormat));
                    ws.addCell(new Label(7, j + row, distributionDO.getOrderNo(), textFormat));
                    ws.addCell(new Label(8, j + row, distributionDO.getReturnNo(), textFormat));
                    ws.addCell(new Label(9, j + row, distributionDO.getStatus(), textFormat));
                    ws.addCell(new Label(10, j + row, distributionDO.getOrderTime(), textFormat));
                    ws.addCell(new Label(11, j + row, distributionDO.getShipmentsTime(), textFormat));
                    ws.addCell(new Label(12, j + row, distributionDO.getFinishTime(), textFormat));
                    ws.addCell(new Label(13, j + row, distributionDO.getAppShipmentsTime(), textFormat));
                    ws.addCell(new Label(14, j + row, distributionDO.getShippingAddress(), textFormat));
                    ws.addCell(new Label(15, j + row, distributionDO.getRemark(), textFormat));
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


    @GetMapping(value = "/photoOrderCheck/download")
    public void downloadPhotoOrderCheck(HttpServletRequest request, HttpServletResponse response, Long cityId,
                                        String startTime, String endTime, String keywords) {
        //查询登录用户门店权限的门店ID
//        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        List<PhotoOrderCheckDO> photoOrderCheckDOList = this.maReportDownloadService.downloadPhotoOrderCheckDO(cityId, startTime,
                endTime, keywords);
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String shiroName = "";
        if (null != shiroUser) {
            shiroName = shiroUser.getName();
        }

        response.setContentType("text/html;charset=UTF-8");
        //创建名称
        String fileurl = "客服考核报表-" + DateUtils.getCurrentTimeStr("yyyyMMddHHmmss") + ".xls";//如  D:/xx/xx/xxx.xls

        WritableWorkbook wwb = null;
        try {
            //创建文件
            wwb = exportXML(fileurl, response);

            //excel单表最大行数是65535
            int maxSize = 0;
            if (null != photoOrderCheckDOList) {
                maxSize = photoOrderCheckDOList.size();
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
                if (null != cityId && !(cityId.equals(-1L)) && null != photoOrderCheckDOList && photoOrderCheckDOList.size() > 0) {
                    map.put("城市", photoOrderCheckDOList.get(0).getCityName());
                } else {
                    map.put("城市", "无");
                }
                if (null != startTime && !("".equals(startTime))) {
                    map.put("开始时间", startTime);
                } else {
                    map.put("开始时间", "无");
                }
                if (null != keywords && !("".equals(keywords))) {
                    map.put("关键字", keywords);
                } else {
                    map.put("关键字", "无");
                }
                if (null != endTime && !("".equals(endTime))) {
                    map.put("结束时间", endTime);
                } else {
                    map.put("结束时间", "无");
                }
                //设置筛选条件
                ws = this.setCondition(ws, map, titleFormat, shiroName, textFormat);
                //列宽
                int[] columnView = {10, 25, 15, 15, 15, 10, 10, 20, 30, 15, 15, 15, 15, 15, 15, 15, 50};
                //列标题
                String[] titles = {"城市", "单号", "下单人姓名", "下单人电话", "下单人身份类型", "状态", "订单类型", "门店名称", "备注",
                        "创建时间", "处理时间", "处理人", "完结时间", "完结人", "收货人姓名", "收货人号码", "收货详细地址"};

                //计算标题开始行号
                int row = 1;
                if (null != map && map.size() > 0) {
                    row = (map.size() + 1) / 2 + 4;
                }

                //设置标题
                ws = this.setHeader(ws, titleFormat, columnView, titles, row);
                row += 1;
                WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"), 9, WritableFont.NO_BOLD, false,
                        UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                //填写表体数据
                for (int j = 0; j < maxRowNum; j++) {
                    if (j + i * maxRowNum >= maxSize) {
                        break;
                    }
                    PhotoOrderCheckDO photoOrderCheckDO = photoOrderCheckDOList.get(j + i * maxRowNum);
                    ws.addCell(new Label(0, j + row, photoOrderCheckDO.getCityName(), textFormat));
                    ws.addCell(new Label(1, j + row, photoOrderCheckDO.getPhotoOrderNo(), textFormat));
                    ws.addCell(new Label(2, j + row, photoOrderCheckDO.getUserName(), textFormat));
                    ws.addCell(new Label(3, j + row, photoOrderCheckDO.getMobile(), textFormat));
                    ws.addCell(new Label(4, j + row, photoOrderCheckDO.getIdentityType(), textFormat));
                    ws.addCell(new Label(5, j + row, photoOrderCheckDO.getStatus(), textFormat));
                    ws.addCell(new Label(6, j + row, photoOrderCheckDO.getOrderType(), textFormat));
                    ws.addCell(new Label(7, j + row, photoOrderCheckDO.getStoreName(), textFormat));
                    ws.addCell(new Label(8, j + row, photoOrderCheckDO.getRemark(), textFormat));
                    ws.addCell(new Label(9, j + row, photoOrderCheckDO.getCreateTime(), textFormat));
                    ws.addCell(new Label(10, j + row, photoOrderCheckDO.getUpdateTime(), textFormat));
                    ws.addCell(new Label(11, j + row, photoOrderCheckDO.getUpdateUser(), textFormat));
                    ws.addCell(new Label(12, j + row, photoOrderCheckDO.getFinishTime(), textFormat));
                    ws.addCell(new Label(13, j + row, photoOrderCheckDO.getOperationUser(), textFormat));
                    ws.addCell(new Label(14, j + row, photoOrderCheckDO.getReceiver(), textFormat));
                    ws.addCell(new Label(15, j + row, photoOrderCheckDO.getReceiverPhone(), textFormat));
                    ws.addCell(new Label(16, j + row, photoOrderCheckDO.getDetailedAddress(), textFormat));
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
     * @param
     * @return
     * @throws
     * @title 郑州华润产品销顾终端出货销量报表
     * @descripe
     * @author GenerationRoad
     * @date 2018/4/2
     */
    @GetMapping(value = "/account/hr/page/grid")
    public GridDataVO<AccountGoodsItemsDO> restAccountGoodsItemsDOHRPageGird(Integer offset, Integer size, Long cityId, Long storeId, String storeType,
                                                                             String startTime, String endTime, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        PageInfo<AccountGoodsItemsDO> accountGoodsItemsDOPageInfo = this.maReportDownloadService.findAccountGoodsItemsDOHR(cityId, storeId, storeType, startTime,
                endTime, keywords, storeIds, page, size);
        return new GridDataVO<AccountGoodsItemsDO>().transform(accountGoodsItemsDOPageInfo.getList(), accountGoodsItemsDOPageInfo.getTotal());
    }

    /**
     * @param
     * @return
     * @throws
     * @title 郑州华润产品销顾终端出货销量报表
     * @descripe
     * @author GenerationRoad
     * @date 2018/4/2
     */
    @GetMapping(value = "/account/hr/download")
    public void downloadAccountGoodsItemsHR(HttpServletRequest request, HttpServletResponse response, Long cityId, Long storeId, String storeType,
                                            String startTime, String endTime, String keywords) {
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        List<AccountGoodsItemsDO> accountGoodsItemsDOList = this.maReportDownloadService.downloadAccountGoodsItemsHR(cityId, storeId, storeType, startTime,
                endTime, keywords, storeIds);
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String shiroName = "";
        if (null != shiroUser) {
            shiroName = shiroUser.getName();
        }

        response.setContentType("text/html;charset=UTF-8");
        //创建名称
        String fileurl = "郑州华润产品销顾终端出货销量报表-" + DateUtils.getCurrentTimeStr("yyyyMMddHHmmss") + ".xls";//如  D:/xx/xx/xxx.xls

        WritableWorkbook wwb = null;
        try {
            //创建文件
            wwb = exportXML(fileurl, response);

            //excel单表最大行数是65535
            int maxSize = 0;
            if (null != accountGoodsItemsDOList) {
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
                if (null != cityId && !(cityId.equals(-1L)) && null != accountGoodsItemsDOList && accountGoodsItemsDOList.size() > 0) {
                    map.put("城市", accountGoodsItemsDOList.get(0).getCityName());
                } else {
                    map.put("城市", "无");
                }
                if (null != storeId && !(storeId.equals(-1L)) && null != accountGoodsItemsDOList && accountGoodsItemsDOList.size() > 0) {
                    map.put("门店", accountGoodsItemsDOList.get(0).getStoreName());
                } else {
                    map.put("门店", "无");
                }
                if (null != storeType && !("".equals(storeType)) && null != accountGoodsItemsDOList && accountGoodsItemsDOList.size() > 0) {
                    map.put("门店类型", accountGoodsItemsDOList.get(0).getStoreType());
                } else {
                    map.put("门店类型", "无");
                }
                if (null != startTime && !("".equals(startTime))) {
                    map.put("开始时间", startTime);
                } else {
                    map.put("开始时间", "无");
                }
                if (null != keywords && !("".equals(keywords))) {
                    map.put("关键字", keywords);
                } else {
                    map.put("关键字", "无");
                }
                if (null != endTime && !("".equals(endTime))) {
                    map.put("结束时间", endTime);
                } else {
                    map.put("结束时间", "无");
                }
                //设置筛选条件
                ws = this.setCondition(ws, map, titleFormat, shiroName, textFormat);
                //列宽
                int[] columnView = {10, 13, 10, 20, 30, 25, 15, 15, 10, 10, 20, 20, 50, 20, 20, 10, 10, 10, 10, 10, 10, 10, 10, 5};
                //列标题
                String[] titles = {"城市", "门店名称", "门店类型", "下单/反配上架时间", "订单号", "退单号", "顾客", "导购姓名", "配送/退货方式",
                        "出/退货状态", "收货/退货人", "收货/退货人电话", "送/退货地址", "产品编码", "产品名称", "产品标识", "产品类型", "数量",
                        "会员价", "会员总价", "零售价", "零售总价", "是否结清"};
                //计算标题开始行号
                int row = 1;
                if (null != map && map.size() > 0) {
                    row = (map.size() + 1) / 2 + 4;
                }

                //设置标题
                ws = this.setHeader(ws, titleFormat, columnView, titles, row);
                row += 1;
                WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"), 9, WritableFont.NO_BOLD, false,
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
                    ws.addCell(new Label(5, j + row, accountGoodsItemsDO.getReturnNumber(), textFormat));
                    ws.addCell(new Label(6, j + row, accountGoodsItemsDO.getCustomerName(), textFormat));
                    ws.addCell(new Label(7, j + row, accountGoodsItemsDO.getSellerName(), textFormat));
                    ws.addCell(new Label(8, j + row, accountGoodsItemsDO.getDeliveryType(), textFormat));
                    ws.addCell(new Label(9, j + row, accountGoodsItemsDO.getDeliveryStatus(), textFormat));
                    ws.addCell(new Label(10, j + row, accountGoodsItemsDO.getReceiver(), textFormat));
                    ws.addCell(new Label(11, j + row, accountGoodsItemsDO.getReceiverPhone(), textFormat));
                    ws.addCell(new Label(12, j + row, accountGoodsItemsDO.getShippingAddress(), textFormat));
                    ws.addCell(new Label(13, j + row, accountGoodsItemsDO.getSku(), textFormat));
                    ws.addCell(new Label(14, j + row, accountGoodsItemsDO.getSkuName(), textFormat));
                    ws.addCell(new Label(15, j + row, accountGoodsItemsDO.getCompanyFlag(), textFormat));
                    ws.addCell(new Label(16, j + row, accountGoodsItemsDO.getGoodsLineType(), textFormat));
                    ws.addCell(new Label(17, j + row, accountGoodsItemsDO.getQuantity().toString(), textFormat));
                    ws.addCell(new Number(18, j + row, accountGoodsItemsDO.getVipPrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(19, j + row, accountGoodsItemsDO.getTotalVipPrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(20, j + row, accountGoodsItemsDO.getRetailPrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(21, j + row, accountGoodsItemsDO.getTotalRetailPrice(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Label(22, j + row, accountGoodsItemsDO.getIsPayUp(), textFormat));
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
     * 导购信用额度
     * @param request
     * @param response
     * @param cityId
     * @param storeId
     * @param storeType
     * @param keywords
     */
    @GetMapping(value = "/emp/credit")
    public void downloadEmpCreditMoney(HttpServletRequest request, HttpServletResponse response, Long cityId, Long storeId, String storeType,String keywords) {
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdByUidAndStoreType(StoreType.getStoreTypeList());
        List<EmpCreditDO> itemsDOList = maReportDownloadService.empCreditMoneySituation(cityId,storeType,storeIds,keywords);
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String shiroName = "";
        if (null != shiroUser) {
            shiroName = shiroUser.getName();
        }

        response.setContentType("text/html;charset=UTF-8");
        //创建名称
        String fileurl = "导购信用额度报表-" + DateUtils.getCurrentTimeStr("yyyyMMddHHmmss") + ".xls";//如  D:/xx/xx/xxx.xls

        WritableWorkbook wwb = null;
        try {
            //创建文件
            wwb = exportXML(fileurl, response);

            //excel单表最大行数是65535
            int maxSize = 0;
            if (null != itemsDOList) {
                maxSize = itemsDOList.size();
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
                if (null != cityId && !(cityId.equals(-1L)) && null != itemsDOList && itemsDOList.size() > 0) {
                    map.put("城市", itemsDOList.get(0).getCity());
                } else {
                    map.put("城市", "无");
                }
                if (null != storeId && !(storeId.equals(-1L)) && null != itemsDOList && itemsDOList.size() > 0) {
                    map.put("门店", itemsDOList.get(0).getStoreName());
                } else {
                    map.put("门店", "无");
                }

                if (null != keywords && !("".equals(keywords))) {
                    map.put("关键字", keywords);
                } else {
                    map.put("关键字", "无");
                }

                //设置筛选条件
                ws = this.setCondition(ws, map, titleFormat, shiroName, textFormat);
                //列宽
                int[] columnView = {10, 13, 10, 20, 30, 25, 15, 15, 20};
                //列标题
                String[] titles = {"城市", "门店名称","导购名称","导购电话","信用额度","临时额度","可用余额","最后一次变更时间"};
                //计算标题开始行号
                int row = 1;
                if (null != map && map.size() > 0) {
                    row = (map.size() + 1) / 2 + 4;
                }

                //设置标题
                ws = this.setHeader(ws, titleFormat, columnView, titles, row);
                row += 1;
                WritableFont textFont = new WritableFont(WritableFont.createFont("微软雅黑"), 9, WritableFont.NO_BOLD, false,
                        UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                //填写表体数据
                for (int j = 0; j < maxRowNum; j++) {
                    if (j + i * maxRowNum >= maxSize) {
                        break;
                    }
                    EmpCreditDO itemsDO = itemsDOList.get(j + i * maxRowNum);
                    ws.addCell(new Label(0, j + row, itemsDO.getCity(), textFormat));
                    ws.addCell(new Label(1, j + row, itemsDO.getStoreName(), textFormat));
                    ws.addCell(new Label(2, j + row, itemsDO.getSellerName(), textFormat));
                    ws.addCell(new Label(3, j + row, itemsDO.getSellerMobile(), textFormat));
                    ws.addCell(new Number(4, j + row, itemsDO.getMaxCreditMoney(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(5, j + row, itemsDO.getTempCreditLimit(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Number(6, j + row, itemsDO.getAvaliableCreditMoney(), new WritableCellFormat(textFont, new NumberFormat("0.00"))));
                    ws.addCell(new Label(7, j + row, itemsDO.getLastChangeTime(), textFormat));
                }
            }
        } catch (Exception e) {
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
}
