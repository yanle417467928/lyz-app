package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaReportDownloadDAO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.reportDownload.*;
import cn.com.leyizhuang.app.foundation.service.MaReportDownloadService;
import cn.com.leyizhuang.app.foundation.vo.management.order.MaOrderDetailResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/3/22
 */
@Service
public class MaReportDownloadServiceImpl implements MaReportDownloadService {

    @Autowired
    private MaReportDownloadDAO maReportDownloadDAO;


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
    public PageInfo<NotPickGoodsReportDO> findNotPickGoodsReportDOAll(Long cityId, Long storeId, String storeType, String startTime, String endTime, String pickType, List<Long> storeIds, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<NotPickGoodsReportDO> notPickGoodsReportDOS = maReportDownloadDAO.findNotPickGoodsReportDOAll(cityId, storeId, storeType, startTime, endTime, pickType, storeIds);
        return new PageInfo<>(notPickGoodsReportDOS);
    }

    @Override
    public PageInfo<StorePredepositReportDO> findStorePredepositReportDOAll(Long cityId, Long storeId, String storeType, String startTime, String endTime, List<Long> storeIds, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<StorePredepositReportDO> storePredepositReportDOS = maReportDownloadDAO.findStorePredepositReportDOAll(cityId, storeId, storeType, startTime, endTime, storeIds);
        return new PageInfo<>(storePredepositReportDOS);
    }

    @Override
    public List<ReceiptsReportDO> downloadReceipts(Long cityId, Long storeId, String storeType, String startTime, String endTime, String payType, String keywords, List<Long> storeIds) {
        if (null != endTime && !("".equals(endTime))) {
            endTime += " 23:59:59";
        }
        return this.maReportDownloadDAO.findReceiptsReportDOAll(cityId, storeId, storeType, startTime, endTime, payType, keywords, storeIds);
    }

    @Override
    public List<NotPickGoodsReportDO> notPickGoodsDownload(Long cityId, Long storeId, String storeType, String startTime, String endTime, String pickType, List<Long> storeIds) {
        return maReportDownloadDAO.findNotPickGoodsReportDOAll(cityId, storeId, storeType, startTime, endTime, pickType, storeIds);
    }

    @Override
    public List<StorePredepositReportDO> storePredepositDownload(Long cityId, Long storeId, String storeType, String startTime, String endTime, List<Long> storeIds) {
        return maReportDownloadDAO.findStorePredepositReportDOAll(cityId, storeId, storeType, startTime, endTime, storeIds);
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
        List<ShipmentAndReturnGoods> shipmentAndReturnGoodsList = maReportDownloadDAO.queryGoodsShipmentAndReturnOrder(cityId, storeId, storeType, startTime, endTime, keywords, storeIds);
        return new PageInfo<>( ShipmentAndReturnGoods.transformList(shipmentAndReturnGoodsList));
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
        List<ShipmentAndReturnGoods> shipmentAndReturnGoodsList = maReportDownloadDAO.queryGoodsShipmentAndReturnOrder(cityId, storeId, storeType, startTime, endTime, keywords, storeIds);
        return ShipmentAndReturnGoods.transformList(shipmentAndReturnGoodsList);
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
}
