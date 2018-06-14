package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.MaReportDownloadDAO;
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
    public PageInfo<ArrearsReportDO> findArrearsList(String companyCode, String storeType, List<Long> storeIds, Long storeId, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<ArrearsReportDO> shipmentAndReturnGoodsList = new ArrayList<>();
        if ("-1".equals(storeType)) {
            storeType = null;
        }
        if (-1L == storeId) {
            storeId = null;
        }
        shipmentAndReturnGoodsList = maReportDownloadDAO.findArrearsList(companyCode, storeType, storeIds, storeId);
        return new PageInfo<>(shipmentAndReturnGoodsList);
    }


    @Override
    public List<ArrearsReportDO> downArrearsList(String companyCode, String storeType, List<Long> storeIds, Long storeId) {
        List<ArrearsReportDO> shipmentAndReturnGoodsList = new ArrayList<>();
        if ("-1".equals(storeType)) {
            storeType = null;
        }
        if (-1L == storeId) {
            storeId = null;
        }
        shipmentAndReturnGoodsList = maReportDownloadDAO.findArrearsList(companyCode, storeType, storeIds, storeId);
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
    public AccountGoodsItemsDO getJxPriceByOrderNoAndSku(String orderNumber, String sku) {
        return this.maReportDownloadDAO.getJxPriceByOrderNoAndSku(orderNumber, sku);
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
}
