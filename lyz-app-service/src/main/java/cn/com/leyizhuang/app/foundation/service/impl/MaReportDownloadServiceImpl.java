package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.MaReportDownloadDAO;
import cn.com.leyizhuang.app.foundation.pojo.CusExpiringSoonProductCouponInfo;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.reportDownload.*;
import cn.com.leyizhuang.app.foundation.service.MaReportDownloadService;
import cn.com.leyizhuang.app.foundation.service.MaStoreService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/3/22
 */
@Service
public class MaReportDownloadServiceImpl implements MaReportDownloadService {

    @Autowired
    private MaReportDownloadDAO maReportDownloadDAO;

    @Autowired
    private MaStoreService maStoreService;

    @Override
    public PageInfo<ReceiptsReportDO> findReceiptsReportDOAll(Long cityId, Long storeId, String storeType, String startTime, String endTime, String payType, String keywords, List<Long> storeIds, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        List<ReceiptsReportDO> receiptsReportDOS = this.maReportDownloadDAO.findReceiptsReportDOAll(cityId, storeId, storeType, startTime, endTime, payType, keywords, storeIds);
        return new PageInfo<>(receiptsReportDOS);
    }

    @Override
    public PageInfo<NotPickGoodsReportDO> findNotPickGoodsReportDOAll(Long cityId, Long storeId, String storeType, String buyTime, String endTime, String pickType, List<Long> storeIds, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        if (StringUtils.isNotBlank(buyTime)) {
            buyTime += " 23:59:59";
        }
        if (StringUtils.isNotBlank(endTime)) {
            endTime += " 23:59:59";
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            endTime = format.format(new Date());
        }
        List<NotPickGoodsReportDO> notPickGoodsReportDOS = maReportDownloadDAO.findNotPickGoodsReportDOAllNEW(cityId, storeId, storeType, buyTime, endTime, pickType, storeIds);
        return new PageInfo<>(notPickGoodsReportDOS);
    }

    @Override
    public PageInfo<StorePredepositReportDO> findStorePredepositReportDOAll(Long cityId, Long storeId, String storeType, String startTime, String endTime, List<Long> storeIds, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        if (StringUtils.isNotBlank(startTime)) {
            startTime += " 00:00:00";
        }
        if (StringUtils.isNotBlank(endTime)) {
            endTime += " 23:59:59";
        }
        List<StorePredepositReportDO> storePredepositReportDOS = maReportDownloadDAO.findStorePredepositReportDOAllNEW(cityId, storeId, storeType, startTime, endTime, storeIds);
        return new PageInfo<>(storePredepositReportDOS);
    }

    @Override
    public PageInfo<EmpCreditMoneyChangeReportDO> findEmployeeCreditMoneyReportDOAll(Long cityId, Long storeId, String storeType, String startTime, String endTime, List<Long> storeIds, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        if (StringUtils.isNotBlank(startTime)) {
            startTime += " 00:00:00";
        }
        if (StringUtils.isNotBlank(endTime)) {
            endTime += " 23:59:59";
        }
        List<EmpCreditMoneyChangeReportDO> empCreditMoneyChangeReportDOS = maReportDownloadDAO.findEmployeeCreditMoneyReportDOAll(cityId, storeId, storeType, startTime, endTime, storeIds);
        return new PageInfo<>(empCreditMoneyChangeReportDOS);
    }

    @Override
    public PageInfo<CompanyCreditMoneyChangeReportDO> findCompanyCreditMoneyReportDOAll(Long cityId, Long storeId, String storeType, String startTime, String endTime, List<Long> storeIds, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        if (StringUtils.isNotBlank(startTime)) {
            startTime += " 00:00:00";
        }
        if (StringUtils.isNotBlank(endTime)) {
            endTime += " 23:59:59";
        }
        List<CompanyCreditMoneyChangeReportDO> companyCreditMoneyChangeReportDOS = maReportDownloadDAO.findCompanyCreditMoneyReportDOAll(cityId, storeId, storeType, startTime, endTime, storeIds);
        return new PageInfo<>(companyCreditMoneyChangeReportDOS);
    }

    @Override
    public List<ReceiptsReportDO> downloadReceipts(Long cityId, Long storeId, String storeType, String startTime, String endTime, String payType, String keywords, List<Long> storeIds) {
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        return this.maReportDownloadDAO.findReceiptsReportDOAll(cityId, storeId, storeType, startTime, endTime, payType, keywords, storeIds);
    }

    @Override
    public List<NotPickGoodsReportDO> notPickGoodsDownload(Long cityId, Long storeId, String storeType, String buyTime, String effTime, String pickType, List<Long> storeIds) {
        if (StringUtils.isNotBlank(buyTime)) {
            buyTime += " 23:59:59";
        }
        if (StringUtils.isNotBlank(effTime)) {
            effTime += " 23:59:59";
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            effTime = format.format(new Date());
        }

        return maReportDownloadDAO.findNotPickGoodsReportDOAllNEW(cityId, storeId, storeType, buyTime, effTime, pickType, storeIds);
    }

    @Override
    public List<StorePredepositReportDO> storePredepositDownload(Long cityId, Long storeId, String storeType, String startTime, String endTime, List<Long> storeIds) {
        if (StringUtils.isNotBlank(startTime)) {
            startTime += " 00:00:00";
        }
        if (StringUtils.isNotBlank(endTime)) {
            endTime += " 23:59:59";
        }
        return maReportDownloadDAO.findStorePredepositReportDOAllNEW(cityId, storeId, storeType, startTime, endTime, storeIds);
    }

    @Override
    public List<EmpCreditMoneyChangeReportDO> employeeCreditMoneyDownload(Long cityId, Long storeId, String storeType, String startTime, String endTime, List<Long> storeIds) {
        if (StringUtils.isNotBlank(startTime)) {
            startTime += " 00:00:00";
        }
        if (StringUtils.isNotBlank(endTime)) {
            endTime += " 23:59:59";
        }
        return maReportDownloadDAO.findEmployeeCreditMoneyReportDOAll(cityId, storeId, storeType, startTime, endTime, storeIds);

    }

    @Override
    public List<CompanyCreditMoneyChangeReportDO> companyCreditMoneyDownload(Long cityId, Long storeId, String storeType, String startTime, String endTime, List<Long> storeIds) {
        if (StringUtils.isNotBlank(startTime)) {
            startTime += " 00:00:00";
        }
        if (StringUtils.isNotBlank(endTime)) {
            endTime += " 23:59:59";
        }
        return maReportDownloadDAO.findCompanyCreditMoneyReportDOAll(cityId, storeId, storeType, startTime, endTime, storeIds);

    }

    @Override
    public PageInfo<DistributionDO> findDistributionDOAll(Long cityId, String wareHouseNo, String deliveryClerkNo, String startTime, String endTime, String keywords, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        List<DistributionDO> distributionDOList = this.maReportDownloadDAO.findDistributionDOAll(cityId, wareHouseNo, deliveryClerkNo, startTime, endTime, keywords);
        return new PageInfo<>(distributionDOList);
    }

    @Override
    public List<DistributionDO> downloadDistributionDO(Long cityId, String wareHouseNo, String deliveryClerkNo, String startTime, String endTime, String keywords) {
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        return this.maReportDownloadDAO.findDistributionDOAll(cityId, wareHouseNo, deliveryClerkNo, startTime, endTime, keywords);
    }

    @Override
    public PageInfo<PhotoOrderCheckDO> findPhotoOrderCheckDOAll(Long cityId, String startTime, String endTime, String keywords, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        List<PhotoOrderCheckDO> photoOrderCheckDOList = this.maReportDownloadDAO.findPhotoOrderCheckDOAll(cityId, startTime, endTime, keywords);
        return new PageInfo<>(photoOrderCheckDOList);
    }

    @Override
    public List<PhotoOrderCheckDO> downloadPhotoOrderCheckDO(Long cityId, String startTime, String endTime, String keywords) {
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        return this.maReportDownloadDAO.findPhotoOrderCheckDOAll(cityId, startTime, endTime, keywords);
    }

    @Override
    public PageInfo<AccountGoodsItemsDO> findAccountGoodsItemsDOAll(Long cityId, Long storeId, String storeType, String startTime, String endTime, String keywords, List<Long> storeIds, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        List<AccountGoodsItemsDO> accountGoodsItemsDOS = this.maReportDownloadDAO.findAccountGoodsItemsDOAll(cityId, storeId, storeType, startTime, endTime, keywords, storeIds);
        return new PageInfo<>(accountGoodsItemsDOS);
    }

    @Override
    public List<AccountGoodsItemsDO> downloadAccountGoodsItems(Long cityId, Long storeId, String storeType, String startTime, String endTime, String keywords, List<Long> storeIds) {
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        return this.maReportDownloadDAO.findAccountGoodsItemsDOAll(cityId, storeId, storeType, startTime, endTime, keywords, storeIds);
    }

    @Override
    public PageInfo<BillingItemsDO> findBillingItemsDOAll(Long cityId, Long storeId, String storeType, String startTime, String endTime, String keywords, List<Long> storeIds, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        List<BillingItemsDO> billingItemsDOList = this.maReportDownloadDAO.findBillingItemsDOAll(cityId, storeId, storeType, startTime, endTime, keywords, storeIds);
        return new PageInfo<>(billingItemsDOList);
    }

    @Override
    public List<BillingItemsDO> downloadBillingItems(Long cityId, Long storeId, String storeType, String startTime, String endTime, String keywords, List<Long> storeIds) {
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        return this.maReportDownloadDAO.findBillingItemsDOAll(cityId, storeId, storeType, startTime, endTime, keywords, storeIds);
    }

    @Override
    public PageInfo<AgencyFundDO> findAgencyFundDOAll(Long cityId, Long storeId, String storeType, String startTime, String endTime, String keywords, List<Long> storeIds, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        List<AgencyFundDO> agencyFundDOList = this.maReportDownloadDAO.findAgencyFundDOAll(cityId, storeId, storeType, startTime, endTime, keywords, storeIds);
        return new PageInfo<>(agencyFundDOList);
    }


    @Override
    public PageInfo<ShipmentAndReturnGoods> findGoodsShipmentAndReturnOrder(Long cityId, Long storeId, String storeType, String startTime, String endTime, String keywords, List<Long> storeIds, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        if (null != startTime && !("".equals(startTime))) {
            startTime += " 00:00:00";
        }
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        if (null != cityId && -1 == cityId) {
            cityId = null;
        }
        if (null != storeId && -1 == storeId) {
            storeId = null;
        }
        if (null != storeType && "-1".equals(storeType)) {
            storeType = null;
        }
        List<ShipmentAndReturnGoods> shipmentAndReturnGoodsList = maReportDownloadDAO.queryGoodsShipmentAndReturnOrder(cityId, storeId, storeType, startTime, endTime, keywords, storeIds);
        return new PageInfo<>(ShipmentAndReturnGoods.transformList(shipmentAndReturnGoodsList));
    }


    @Override
    public List<ShipmentAndReturnGoods> downShipmentAndReturnOrder(Long cityId, Long storeId, String storeType, String startTime, String endTime, String keywords, List<Long> storeIds) {
        if (null != startTime && !("".equals(startTime))) {
            startTime += " 00:00:00";
        }
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        if (null != cityId && -1 == cityId) {
            cityId = null;
        }
        if (null != storeId && -1 == storeId) {
            storeId = null;
        }
        if (null != storeType && "-1".equals(storeType)) {
            storeType = null;
        }
        List<ShipmentAndReturnGoods> shipmentAndReturnGoodsList = maReportDownloadDAO.queryGoodsShipmentAndReturnOrder(cityId, storeId, storeType, startTime, endTime, keywords, storeIds);
        return ShipmentAndReturnGoods.transformList(shipmentAndReturnGoodsList);
    }


    @Override
    public PageInfo<SalesReportDO> findSalesList(String companyCode, String storeType,
                                                 String startTime, String endTime, Boolean isProductCoupon, List<Long> storeIds,String productType, Long storeId, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        if (null != startTime && !("".equals(startTime))) {
            startTime += " 00:00:00";
        }
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        if (-1L == storeId) {
            storeId = null;
        }
        if ("-1".equals(storeType) ) {
            storeType = null;
        }
        List<SalesReportDO> shipmentAndReturnGoodsList = new ArrayList<>();
        if (isProductCoupon) {
            //平铺产品劵
            shipmentAndReturnGoodsList = maReportDownloadDAO.findProductSalesList(companyCode, storeType, startTime, endTime, storeIds,productType, storeId);
        } else {
            //不平铺产品劵
            //shipmentAndReturnGoodsList = maReportDownloadDAO.findNoProductSalesList(companyCode, storeType, startTime, endTime, storeIds);
        }
        return new PageInfo<>(shipmentAndReturnGoodsList);
    }


    @Override
    public PageInfo<ArrearsReportDO> findArrearsList(String companyCode, String storeType, List<Long> storeIds, Long storeId, Integer page, Integer size, String endTime) {
            if(null ==storeIds||storeIds.size()==0){
                return null;
            }
        PageHelper.startPage(page, size);
        List<ArrearsReportDO> shipmentAndReturnGoodsList = new ArrayList<>();
        if ("-1".equals(storeType)) {
            storeType = null;
        }
        if (-1L == storeId) {
            storeId = null;
        }
        shipmentAndReturnGoodsList = maReportDownloadDAO.findArrearsList(companyCode, storeType, storeIds, storeId,endTime);
        return new PageInfo<>(shipmentAndReturnGoodsList);
    }


    @Override
    public List<ArrearsReportDO> downArrearsList(String companyCode, String storeType, List<Long> storeIds, Long storeId, String endTime) {
        if(null ==storeIds||storeIds.size()==0){
            return null;
        }
        List<ArrearsReportDO> shipmentAndReturnGoodsList = new ArrayList<>();
        if ("-1".equals(storeType)) {
            storeType = null;
        }
        if (-1L == storeId) {
            storeId = null;
        }
        shipmentAndReturnGoodsList = maReportDownloadDAO.findArrearsList(companyCode, storeType, storeIds, storeId,endTime);
        return shipmentAndReturnGoodsList;
    }


    @Override
    public List<SalesReportDO> downSalesReport(String companyCode, String storeType,
                                               String startTime, String endTime, Boolean isProductCoupon, List<Long> storeIds,String productType, Long storeId) {
        if (null != startTime && !("".equals(startTime))) {
            startTime += " 00:00:00";
        }
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        if ("-1".equals(storeType)) {
            storeType = null;
        }
        if (-1L == storeId) {
            storeId = null;
        }
        List<SalesReportDO> shipmentAndReturnGoodsList = new ArrayList<>();
        if (isProductCoupon) {
            shipmentAndReturnGoodsList = maReportDownloadDAO.findProductSalesList(companyCode, storeType, startTime, endTime, storeIds,productType,storeId);
        } else {
            //shipmentAndReturnGoodsList = maReportDownloadDAO.findNoProductSalesList(companyCode, storeType, startTime, endTime, storeIds);
        }
        return shipmentAndReturnGoodsList;
    }


    @Override
    public List<AgencyFundDO> downloadAgencyFund(Long cityId, Long storeId, String storeType, String startTime, String endTime, String keywords, List<Long> storeIds) {
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        return this.maReportDownloadDAO.findAgencyFundDOAll(cityId, storeId, storeType, startTime, endTime, keywords, storeIds);
    }

    @Override
    public List<StoreInventory> downloadStoreInventorys(Long storeId, List<Long> storeIds) {
        if (-1 == storeId) {
            storeId = null;
        }
        return this.maReportDownloadDAO.findStoreInventorys(storeId, storeIds);
    }

    @Override
    public AccountGoodsItemsDO getJxPriceByOrderNoAndSku(String orderNumber, String sku,String goodsLineType) {
        return this.maReportDownloadDAO.getJxPriceByOrderNoAndSku(orderNumber, sku,goodsLineType);
    }

    @Override
    public PageInfo<AccountGoodsItemsDO> findAccountZGGoodsItemsDOAll(Long cityId, Long storeId, String startTime, String endTime, String keywords, List<Long> storeIds, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        List<AccountGoodsItemsDO> accountGoodsItemsDOS = this.maReportDownloadDAO.findAccountZGGoodsItemsDOAll(cityId, storeId, startTime, endTime, keywords, storeIds);
        return new PageInfo<>(accountGoodsItemsDOS);
    }

    @Override
    public List<AccountGoodsItemsDO> downloadAccountZGGoodsItems(Long cityId, Long storeId, String startTime, String endTime, String keywords, List<Long> storeIds) {
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        return this.maReportDownloadDAO.findAccountZGGoodsItemsDOAll(cityId, storeId, startTime, endTime, keywords, storeIds);
    }

    @Override
    public PageInfo<AccountGoodsItemsDO> findAccountGoodsItemsDOHR(Long cityId, Long storeId, String storeType, String startTime, String endTime, String keywords, List<Long> storeIds, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        List<AccountGoodsItemsDO> accountGoodsItemsDOS = this.maReportDownloadDAO.findAccountGoodsItemsDOHR(cityId, storeId, storeType, startTime, endTime, keywords, storeIds);
        return new PageInfo<>(accountGoodsItemsDOS);
    }

    @Override
    public List<AccountGoodsItemsDO> downloadAccountGoodsItemsHR(Long cityId, Long storeId, String storeType, String startTime, String endTime, String keywords, List<Long> storeIds) {
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        return this.maReportDownloadDAO.findAccountGoodsItemsDOHR(cityId, storeId, storeType, startTime, endTime, keywords, storeIds);
    }


    @Override
    public PageInfo<CusExpiringSoonProductCouponInfo> findExpiringSoonProductAll(Long cityId, Long storeId, String storeType, String cusName, Integer page, Integer size, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<CusExpiringSoonProductCouponInfo> cusExpiringSoonProductCouponInfoList = this.maReportDownloadDAO.findExpiringSoonProductAll(cityId, storeId, storeType,cusName, storeIds);
        return new PageInfo<>(cusExpiringSoonProductCouponInfoList);
    }

    @Override
    public List<CusExpiringSoonProductCouponInfo> downloadExpiringSoonProduct(Long cityId, Long storeId, String storeType, String cusName, List<Long> storeIds) {
        List<CusExpiringSoonProductCouponInfo> cusExpiringSoonProductCouponInfoList = this.maReportDownloadDAO.findExpiringSoonProductAll(cityId, storeId, storeType,cusName, storeIds);
        return cusExpiringSoonProductCouponInfoList;
    }

    @Override
    public PageInfo<EmpCreditDO> empCreditMoneySituationPage(Long cityId, String storeType, List<Long> storeIds, Integer page, Integer size,String keywords) {
        PageHelper.startPage(page, size);

        List<EmpCreditDO> itemsDOS = this.maReportDownloadDAO.empCreditMoneySituation(cityId,storeType,storeIds,keywords);
        return new PageInfo<>(itemsDOS);

    }

    @Override
    public PageInfo<StCreditDO> stCreditMoneySituationPage(Long cityId, List<Long> storeIds, Integer page, Integer size,String keywords) {
        PageHelper.startPage(page, size);

        List<StCreditDO> itemsDOS = this.maReportDownloadDAO.stCreditMoneySituation(cityId,storeIds,keywords);
        return new PageInfo<>(itemsDOS);

    }

    @Override
    public List<EmpCreditDO> empCreditMoneySituation(Long cityId, String storeType, List<Long> storeIds,String keywords) {

        return  this.maReportDownloadDAO.empCreditMoneySituation(cityId,storeType,storeIds,keywords);
    }

    @Override
    public List<StCreditDO> stCreditMoneySituation(Long cityId, List<Long> storeIds,String keywords) {
        return this.maReportDownloadDAO.stCreditMoneySituation(cityId,storeIds,keywords);
    }

    @Override
    public PageInfo<CusProductCouponSummary> findCusProductCouponSummaryPage(Long cityId, Long storeId, List<Long> storeIds, Integer page, Integer size,String keywords,String endTime, String productType) {
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        PageHelper.startPage(page, size);
        List<CusProductCouponSummary> itemsDOS = this.maReportDownloadDAO.findCusProductCouponSummaryList(cityId,storeId,storeIds,keywords,endTime,productType);
        return new PageInfo<>(itemsDOS);

    }

    @Override
    public List<CusProductCouponSummary> findCusProductCouponSummaryList(Long cityId, Long storeId, List<Long> storeIds,String keywords,String endTime, String productType) {
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        return this.maReportDownloadDAO.findCusProductCouponSummaryList(cityId,storeId,storeIds,keywords,endTime,productType);
    }

    @Override
    public PageInfo<CusProductCouponChangeLogReportDO> findCusProductCouponChangeLogPage(Long cityId, Long storeId, List<Long> storeIds, Integer page, Integer size,String keywords,String endTime,String startTime, String productType) {
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        PageHelper.startPage(page, size);
        List<CusProductCouponChangeLogReportDO> itemsDOS = this.maReportDownloadDAO.findCusProductCouponChangeLog(cityId,storeId,storeIds,keywords,endTime,startTime,productType);
        return new PageInfo<>(itemsDOS);

    }

    @Override
    public List<CusProductCouponChangeLogReportDO> findCusProductCouponChangeLogList(Long cityId, Long storeId, List<Long> storeIds,String keywords,String endTime, String startTime, String productType) {
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        return this.maReportDownloadDAO.findCusProductCouponChangeLog(cityId,storeId,storeIds,keywords,endTime,startTime,productType);
    }

    @Override
    public PageInfo<StInventoryRealChangeLogReportDO> findStInventoryRealChangeLogPage(Long cityId, Long storeId, List<Long> storeIds, Integer page, Integer size, String endTime, String startTime) {
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        PageHelper.startPage(page, size);
        List<StInventoryRealChangeLogReportDO> itemsDOS = this.maReportDownloadDAO.findStoreInventoryRealChangeLog(cityId,storeId,storeIds,endTime,startTime);
        return new PageInfo<>(itemsDOS);
    }

    @Override
    public List<StInventoryRealChangeLogReportDO> findStInventoryRealChangeLogList(Long cityId, Long storeId, List<Long> storeIds, String endTime, String startTime) {
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        return this.maReportDownloadDAO.findStoreInventoryRealChangeLog(cityId,storeId,storeIds,endTime,startTime);
    }

}
