package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dao.WmsToAppOrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.WareHouseDO;
import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderLifecycle;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.*;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.common.util.AssertUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.scheduling.annotation.Async;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2017/12/26.
 * Time: 18:09.
 */
@Service
@Slf4j
public class WmsToAppOrderServiceImpl implements WmsToAppOrderService {

    @Resource
    private WmsToAppOrderDAO wmsToAppOrderDAO;

    @Resource
    private OrderDeliveryInfoDetailsService orderDeliveryInfoDetailsService;

    @Resource
    private AppOrderService appOrderService;

    @Resource
    private GoodsService goodsService;

    @Resource
    private CityService cityService;

    @Resource
    private SmsAccountService smsAccountService;

    @Resource
    private AppEmployeeService appEmployeeService;

    @Resource
    private WareHouseService wareHouseService;

    @Resource
    private ReturnOrderService returnOrderService;

    @Override
    public int saveWtaShippingOrderHeader(WtaShippingOrderHeader header) {
        if (AssertUtil.isNotEmpty(header)) {
            return wmsToAppOrderDAO.saveWtaShippingOrderHeader(header);
        }
        return -1;
    }

    @Override
    public int saveWtaShippingOrderGoods(WtaShippingOrderGoods goods) {
        if (AssertUtil.isNotEmpty(goods)) {
            return wmsToAppOrderDAO.saveWtaShippingOrderGoods(goods);
        }
        return -1;
    }

    @Override
    public int saveWtaReturningOrderHeader(WtaReturningOrderHeader header) {
        if (AssertUtil.isNotEmpty(header)) {
            return wmsToAppOrderDAO.saveWtaReturningOrderHeader(header);
        }
        return -1;
    }

    @Override
    public int saveWtaReturningOrderGoods(WtaReturningOrderGoods goods) {
        if (AssertUtil.isNotEmpty(goods)) {
            return wmsToAppOrderDAO.saveWtaReturningOrderGoods(goods);
        }
        return -1;
    }

    @Override
    public int saveWtaReturnOrderDeliveryClerk(WtaReturnOrderDeliveryClerk deliveryClerk) {
        if (AssertUtil.isNotEmpty(deliveryClerk)) {
            return wmsToAppOrderDAO.saveWtaReturnOrderDeliveryClerk(deliveryClerk);
        }
        return -1;
    }

    @Override
    public int saveWtaCancelOrderResultEnter(WtaCancelOrderResultEnter orderResultEnter) {
        if (AssertUtil.isNotEmpty(orderResultEnter)) {
            return wmsToAppOrderDAO.saveWtaCancelOrderResultEnter(orderResultEnter);
        }
        return -1;
    }

    @Override
    public int saveWtaCancelReturnOrderResultEnter(WtaCancelReturnOrderResultEnter returnOrderResultEnter) {
        if (AssertUtil.isNotEmpty(returnOrderResultEnter)) {
            return wmsToAppOrderDAO.saveWtaCancelReturnOrderResultEnter(returnOrderResultEnter);
        }
        return -1;
    }

    @Override
    public int saveWtaWarehouseWholeSplitToUnit(WtaWarehouseWholeSplitToUnit wholeSplitToUnit) {
        if (AssertUtil.isNotEmpty(wholeSplitToUnit)) {
            return wmsToAppOrderDAO.saveWtaWarehouseWholeSplitToUnit(wholeSplitToUnit);
        }
        return -1;
    }


    /**
     * 异步处理整转零逻辑
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlingWtaWarehouseWholeSplitToUnitAsync(String directNo, String sku, String dsku) {
        WtaWarehouseWholeSplitToUnit wholeSplitToUnit = this.wmsToAppOrderDAO.findWtaWarehouseWholeSplitToUnit(directNo, sku, dsku);
        //扣整商品仓库数量
        try {
            if (null != wholeSplitToUnit) {
                City city = cityService.findCityByWarehouseNo(wholeSplitToUnit.getWarehouseNo());
                //sCity city = cityService.findByCityNumber(wholeSplitToUnit.getCompanyId());
                if (null == city) {
                    wholeSplitToUnit.setErrMessage("城市信息中没有查询到仓库编号为" + wholeSplitToUnit.getWarehouseNo() + "的数据!");
                    wholeSplitToUnit.setHandleFlag("0");
                    wholeSplitToUnit.setHandleTime(new Date());
                    this.updateWarehouseWholeSplitToUnit(wholeSplitToUnit);
                    return;
                }
                GoodsDO goodsDO = goodsService.queryBySku(wholeSplitToUnit.getSku());
                if (null == goodsDO) {
                    wholeSplitToUnit.setErrMessage("商品资料中没有查询到sku为" + wholeSplitToUnit.getSku() + "的商品信息!");
                    wholeSplitToUnit.setHandleFlag("0");
                    wholeSplitToUnit.setHandleTime(new Date());
                    this.updateWarehouseWholeSplitToUnit(wholeSplitToUnit);
                    return;
                }
                GoodsDO dGoodsDO = goodsService.queryBySku(wholeSplitToUnit.getDSku());
                if (null == dGoodsDO) {
                    wholeSplitToUnit.setErrMessage("商品资料中没有查询到sku为" + wholeSplitToUnit.getDSku() + "的商品信息!");
                    wholeSplitToUnit.setHandleFlag("0");
                    wholeSplitToUnit.setHandleTime(new Date());
                    this.updateWarehouseWholeSplitToUnit(wholeSplitToUnit);
                    return;
                }
                for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                    CityInventory cityInventory = cityService.findCityInventoryByCityIdAndSku(city.getCityId(), wholeSplitToUnit.getSku());
                    if (null == cityInventory) {
                        cityInventory = CityInventory.transform(goodsDO, city);
                        cityService.saveCityInventory(cityInventory);
                    }
                    if (cityInventory.getAvailableIty() < wholeSplitToUnit.getQty()) {
                        wholeSplitToUnit.setErrMessage("该城市下sku为" + wholeSplitToUnit.getSku() + "的商品库存不足!");
                        wholeSplitToUnit.setHandleFlag("0");
                        wholeSplitToUnit.setHandleTime(new Date());
                        smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "获取wms信息整转零失败!" + wholeSplitToUnit.getDirectNo() +
                                "该城市下sku为" + wholeSplitToUnit.getSku() + "的商品库存不足!");
                        this.updateWarehouseWholeSplitToUnit(wholeSplitToUnit);
                        return;
                    }
                    Integer affectLine = cityService.lockCityInventoryByCityIdAndSkuAndInventory(
                            city.getCityId(), wholeSplitToUnit.getSku(), -wholeSplitToUnit.getQty(), cityInventory.getLastUpdateTime());
                    if (affectLine > 0) {
                        CityInventoryAvailableQtyChangeLog log = new CityInventoryAvailableQtyChangeLog();
                        log.setCityId(cityInventory.getCityId());
                        log.setCityName(cityInventory.getCityName());
                        log.setGid(cityInventory.getGid());
                        log.setSku(cityInventory.getSku());
                        log.setSkuName(cityInventory.getSkuName());
                        log.setChangeQty(-wholeSplitToUnit.getQty());
                        log.setAfterChangeQty(cityInventory.getAvailableIty() - wholeSplitToUnit.getQty());
                        log.setChangeTime(Calendar.getInstance().getTime());
                        log.setChangeType(CityInventoryAvailableQtyChangeType.WHOLE_TO_SCRAPPY);
                        log.setChangeTypeDesc(CityInventoryAvailableQtyChangeType.WHOLE_TO_SCRAPPY.getDescription());
                        log.setReferenceNumber(wholeSplitToUnit.getDirectNo());
                        cityService.addCityInventoryAvailableQtyChangeLog(log);
                        wholeSplitToUnit.setHandleFlag("1");
                        wholeSplitToUnit.setHandleTime(new Date());
                        this.updateWarehouseWholeSplitToUnit(wholeSplitToUnit);
                        break;
                    } else {
                        if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                            smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "获取wms整转零信息失败,扣整商品仓库数量失败!任务编号" + wholeSplitToUnit.getDirectNo());
                            wholeSplitToUnit.setErrMessage("整转零失败!");
                            wholeSplitToUnit.setHandleFlag("0");
                            wholeSplitToUnit.setHandleTime(new Date());
                            this.updateWarehouseWholeSplitToUnit(wholeSplitToUnit);
                            return;
                        }
                    }
                }
                //增加零商品仓库数量
                for (int j = 1; j <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; j++) {
                    CityInventory cityInventory = cityService.findCityInventoryByCityIdAndSku(city.getCityId(), wholeSplitToUnit.getDSku());
                    if (null == cityInventory) {
                        cityInventory = CityInventory.transform(dGoodsDO, city);
                        cityService.saveCityInventory(cityInventory);
                    }
                    Integer affectLine = cityService.lockCityInventoryByCityIdAndSkuAndInventory(
                            city.getCityId(), wholeSplitToUnit.getDSku(), wholeSplitToUnit.getDQty(), cityInventory.getLastUpdateTime());
                    if (affectLine > 0) {
                        CityInventoryAvailableQtyChangeLog log = new CityInventoryAvailableQtyChangeLog();
                        log.setCityId(cityInventory.getCityId());
                        log.setCityName(cityInventory.getCityName());
                        log.setGid(cityInventory.getGid());
                        log.setSku(cityInventory.getSku());
                        log.setSkuName(cityInventory.getSkuName());
                        log.setChangeQty(wholeSplitToUnit.getDQty());
                        log.setAfterChangeQty(cityInventory.getAvailableIty() + wholeSplitToUnit.getDQty());
                        log.setChangeTime(Calendar.getInstance().getTime());
                        log.setChangeType(CityInventoryAvailableQtyChangeType.WHOLE_TO_SCRAPPY);
                        log.setChangeTypeDesc(CityInventoryAvailableQtyChangeType.WHOLE_TO_SCRAPPY.getDescription());
                        log.setReferenceNumber(wholeSplitToUnit.getDirectNo());
                        cityService.addCityInventoryAvailableQtyChangeLog(log);
                        wholeSplitToUnit.setHandleFlag("1");
                        wholeSplitToUnit.setHandleTime(new Date());
                        this.updateWarehouseWholeSplitToUnit(wholeSplitToUnit);
                        break;
                    } else {
                        if (j == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                            smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "获取wms整转零信息失败,增加零商品仓库数量失败!任务编号" + wholeSplitToUnit.getDirectNo());
                            wholeSplitToUnit.setErrMessage("整转零失败!");
                            wholeSplitToUnit.setHandleFlag("0");
                            wholeSplitToUnit.setHandleTime(new Date());
                            this.updateWarehouseWholeSplitToUnit(wholeSplitToUnit);
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            wholeSplitToUnit.setHandleFlag("0");
            wholeSplitToUnit.setHandleTime(new Date());
            wholeSplitToUnit.setErrMessage(e.getMessage());
            this.updateWarehouseWholeSplitToUnit(wholeSplitToUnit);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlingWtaWarehouseReportDamageAndOverflowAsync(String wasteNo, Long wasteId) {
        WtaWarehouseReportDamageAndOverflow damageAndOverflow = this.wmsToAppOrderDAO.findWtaWarehouseReportDamageAndOverflow(wasteNo, wasteId);
        try {
            if (null != damageAndOverflow) {
                City city = cityService.findCityByWarehouseNo(damageAndOverflow.getWarehouseNo());
                if (null == city) {
                    damageAndOverflow.setErrMessage("城市信息中没有查询到仓库编号为" + damageAndOverflow.getWarehouseNo() + "的数据!");
                    damageAndOverflow.setHandleFlag("0");
                    damageAndOverflow.setHandleTime(new Date());
                    this.updateWarehouseWholeOverflow(damageAndOverflow);
                    return;
                }
                GoodsDO goodsDO = goodsService.queryBySku(damageAndOverflow.getSku());
                if (null == goodsDO) {
                    damageAndOverflow.setErrMessage("商品资料中没有查询到sku为" + damageAndOverflow.getSku() + "的商品信息!");
                    damageAndOverflow.setHandleFlag("0");
                    damageAndOverflow.setHandleTime(new Date());
                    this.updateWarehouseWholeOverflow(damageAndOverflow);
                    return;
                }
                Integer changeInventory = 0;
                CityInventoryAvailableQtyChangeType changeType = null;
                String cityCode = damageAndOverflow.getCompanyId();
                String sku = damageAndOverflow.getSku();
                Integer qty = damageAndOverflow.getQty();
                if (damageAndOverflow.getWasteType().contains("一般报溢")) {
                    changeType = CityInventoryAvailableQtyChangeType.CITY_OVERFLOW;
                    changeInventory = qty;
                } else {
                    changeInventory = -qty;
                    changeType = CityInventoryAvailableQtyChangeType.CITY_WASTAGE;
                }
                for (int j = 1; j <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; j++) {
                    CityInventory cityInventory = cityService.findCityInventoryByCityIdAndSku(city.getCityId(), sku);
                    if (null == cityInventory) {
                        cityInventory = CityInventory.transform(goodsDO, city);
                        cityService.saveCityInventory(cityInventory);
                    }
                    if (cityInventory.getAvailableIty() < qty && changeInventory < 0) {
                        damageAndOverflow.setErrMessage("该城市下sku为" + damageAndOverflow.getSku() + "的商品库存不足!");
                        damageAndOverflow.setHandleFlag("0");
                        damageAndOverflow.setHandleTime(new Date());
                        this.updateWarehouseWholeOverflow(damageAndOverflow);
                        smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "获取wms信息失败,获取报损报溢失败,该城市下sku为" + damageAndOverflow.getSku() + "的商品库存不足!");
                        return;
                    }
                    Integer affectLine = cityService.lockCityInventoryByCityIdAndSkuAndInventory(city.getCityId(), sku, changeInventory, cityInventory.getLastUpdateTime());
                    if (affectLine > 0) {
                        CityInventoryAvailableQtyChangeLog log = new CityInventoryAvailableQtyChangeLog();
                        log.setCityId(cityInventory.getCityId());
                        log.setCityName(cityInventory.getCityName());
                        log.setGid(cityInventory.getGid());
                        log.setSku(cityInventory.getSku());
                        log.setSkuName(cityInventory.getSkuName());
                        log.setChangeQty(changeInventory);
                        log.setAfterChangeQty(cityInventory.getAvailableIty() + changeInventory);
                        log.setChangeTime(Calendar.getInstance().getTime());
                        log.setChangeType(changeType);
                        log.setChangeTypeDesc(changeType.getDescription());
                        log.setReferenceNumber(damageAndOverflow.getWasteNo());
                        cityService.addCityInventoryAvailableQtyChangeLog(log);
                        damageAndOverflow.setHandleFlag("1");
                        damageAndOverflow.setHandleTime(new Date());
                        this.updateWarehouseWholeOverflow(damageAndOverflow);
                        break;
                    } else {
                        if (j == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                            damageAndOverflow.setErrMessage("获取wms报损报溢失败!任务编号" + damageAndOverflow.getWasteNo());
                            damageAndOverflow.setHandleFlag("0");
                            damageAndOverflow.setHandleTime(new Date());
                            this.updateWarehouseWholeOverflow(damageAndOverflow);
                            smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "获取wms报损报溢失败!任务编号" + damageAndOverflow.getWasteNo());
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            damageAndOverflow.setErrMessage(e.getMessage());
            damageAndOverflow.setHandleFlag("0");
            damageAndOverflow.setHandleTime(new Date());
            this.updateWarehouseWholeOverflow(damageAndOverflow);
        }
    }

    @Override
    public int saveWtaWarehouseAllocationHeader(WtaWarehouseAllocationHeader header) {
        if (AssertUtil.isNotEmpty(header)) {
            return wmsToAppOrderDAO.saveWtaWarehouseAllocationHeader(header);
        }
        return -1;
    }

    @Override
    public int saveWtaWarehouseAllocationGoods(WtaWarehouseAllocationGoods allocationGoods) {
        if (AssertUtil.isNotEmpty(allocationGoods)) {
            return wmsToAppOrderDAO.saveWtaWarehouseAllocationGoods(allocationGoods);
        }
        return -1;
    }

    @Override
    public int saveWtaWarehousePurchaseHeader(WtaWarehousePurchaseHeader purchaseHeader) {
        if (AssertUtil.isNotEmpty(purchaseHeader)) {
            return wmsToAppOrderDAO.saveWtaWarehousePurchaseHeader(purchaseHeader);
        }
        return -1;
    }

    @Override
    public int saveWtaWarehousePurchaseGoods(WtaWarehousePurchaseGoods purchaseGoods) {
        if (AssertUtil.isNotEmpty(purchaseGoods)) {
            return wmsToAppOrderDAO.saveWtaWarehousePurchaseGoods(purchaseGoods);
        }
        return -1;
    }

    @Override
    public int saveWtaWarehouseReportDamageAndOverflow(WtaWarehouseReportDamageAndOverflow damageAndOverflow) {
        if (AssertUtil.isNotEmpty(damageAndOverflow)) {
            return wmsToAppOrderDAO.saveWtaWarehouseReportDamageAndOverflow(damageAndOverflow);
        }
        return -1;
    }

    @Override
    public List<WtaWarehouseAllocationGoods> findWtaWarehouseAllocationGoodsListByAllocationNo(String allocationNo) {
        if (AssertUtil.isNotEmpty(allocationNo)) {
            return wmsToAppOrderDAO.findWtaWarehouseAllocationGoodsListByAllocationNo(allocationNo);
        }
        return null;
    }

    @Override
    public List<WtaWarehousePurchaseGoods> findWtaWarehousePurchaseGoodsListByPurchaseNo(String purchaseNo) {
        if (AssertUtil.isNotEmpty(purchaseNo)) {
            return wmsToAppOrderDAO.findWtaWarehousePurchaseGoodsListByPurchaseNo(purchaseNo);
        }
        return null;
    }

    @Override
    public List<WtaReturningOrderGoods> findWtaReturningOrderGoodsByReturnOrderNo(String returnOrderNumber) {
        if (AssertUtil.isNotEmpty(returnOrderNumber)) {
            return wmsToAppOrderDAO.findWtaReturningOrderGoodsByReturnOrderNo(returnOrderNumber);
        }
        return null;
    }

    @Override
    public void updateWtaReturnOrderDeliveryClerk(WtaReturnOrderDeliveryClerk deliveryClerk) {
        if (AssertUtil.isNotEmpty(deliveryClerk)) {
            wmsToAppOrderDAO.updateWtaReturnOrderDeliveryClerk(deliveryClerk);
        }
    }

    @Override
    @Transactional
    public void updateWtaShippingOrderHeader(WtaShippingOrderHeader header) {
        if (AssertUtil.isNotEmpty(header)) {
            wmsToAppOrderDAO.updateWtaShippingOrderHeader(header);
        }
    }


    @Override
    @Transactional
    public void updateWarehouseWholeSplitToUnit(WtaWarehouseWholeSplitToUnit wholeSplitToUnit) {
        if (AssertUtil.isNotEmpty(wholeSplitToUnit)) {
            wmsToAppOrderDAO.updateWarehouseWholeSplitToUnit(wholeSplitToUnit);
        }
    }

    @Override
    @Transactional
    public void updateWarehouseWholeOverflow(WtaWarehouseReportDamageAndOverflow wtaWarehouseReportDamageAndOverflow) {
        if (AssertUtil.isNotEmpty(wtaWarehouseReportDamageAndOverflow)) {
            wmsToAppOrderDAO.updateWarehouseWholeOverflow(wtaWarehouseReportDamageAndOverflow);
        }
    }


    @Override
    public WtaShippingOrderHeader getWtaShippingOrderHeaderNotHandling(String orderNo, String taskNo) {
        if (AssertUtil.isNotEmpty(orderNo)) {
            return wmsToAppOrderDAO.getWtaShippingOrderHeaderNotHandling(orderNo, taskNo);
        }
        return null;
    }

    @Override
    public WtaShippingOrderHeader getWtaShippingOrderHeader(String orderNo) {
        if (AssertUtil.isNotEmpty(orderNo)) {
            return wmsToAppOrderDAO.getWtaShippingOrderHeader(orderNo);
        }
        return null;
    }

    @Override
    public WtaReturningOrderHeader getWtaReturningOrderHeaderByReturnNumber(String returnNo) {
        if (AssertUtil.isNotEmpty(returnNo)) {
            return wmsToAppOrderDAO.getWtaReturningOrderHeaderByReturnNumber(returnNo);
        }
        return null;
    }

    @Override
    public int saveWtaOrderLogistics(WtaOrderLogistics wtaOrderLogistics) {
        if (AssertUtil.isNotEmpty(wtaOrderLogistics)) {
            return this.wmsToAppOrderDAO.saveWtaOrderLogistics(wtaOrderLogistics);
        }
        return 0;
    }

    @Override
    @Transactional
    public void handleWtaOrderLogistics(String orderNo) {
        List<WtaOrderLogistics> wtaOrderLogisticsList = this.wmsToAppOrderDAO.getWtaOrderLogistics(orderNo);
        try {
            if (null != wtaOrderLogisticsList) {
                AppOrderStatus status = null;
                LogisticStatus deliveryStatus = null;
                for (WtaOrderLogistics wtaOrderLogistics : wtaOrderLogisticsList) {
                    OrderDeliveryInfoDetails orderDeliveryInfoDetails = new OrderDeliveryInfoDetails();
                    orderDeliveryInfoDetails.setOrderNo(wtaOrderLogistics.getOrderNo());
                    orderDeliveryInfoDetails.setLogisticStatus(LogisticStatus.getLogisticStatusByDescription(wtaOrderLogistics.getLogisticStatus()));
                    orderDeliveryInfoDetails.setCreateTime(wtaOrderLogistics.getCreateTime());
                    orderDeliveryInfoDetails.setOperationType(wtaOrderLogistics.getOperationType());
                    orderDeliveryInfoDetails.setWarehouseNo(wtaOrderLogistics.getWarehouseNo());
                    orderDeliveryInfoDetails.setTaskNo(wtaOrderLogistics.getTaskNo());
                    orderDeliveryInfoDetails.setDescription("商家" + wtaOrderLogistics.getLogisticStatus() + "完成！");
                    orderDeliveryInfoDetailsService.addOrderDeliveryInfoDetails(orderDeliveryInfoDetails);
                    if (orderDeliveryInfoDetails.getLogisticStatus() == LogisticStatus.LOADING) {
                        status = AppOrderStatus.PENDING_RECEIVE;
                        deliveryStatus = LogisticStatus.LOADING;
                    } else if (orderDeliveryInfoDetails.getLogisticStatus() == LogisticStatus.PICKING_GOODS && deliveryStatus != LogisticStatus.LOADING) {
                        deliveryStatus = LogisticStatus.PICKING_GOODS;
                        status = AppOrderStatus.PENDING_SHIPMENT;
                    } else if (orderDeliveryInfoDetails.getLogisticStatus() == LogisticStatus.ALREADY_POSITIONED && deliveryStatus != LogisticStatus.LOADING
                            && deliveryStatus != LogisticStatus.PICKING_GOODS) {
                        deliveryStatus = LogisticStatus.ALREADY_POSITIONED;
                        status = AppOrderStatus.PENDING_SHIPMENT;
                    }
                    wtaOrderLogistics.setHandleFlag("1");
                    wtaOrderLogistics.setHandleTime(new Date());
                    this.wmsToAppOrderDAO.updateWtaOrderLogistics(wtaOrderLogistics);
                }
                OrderBaseInfo orderBaseInfo = new OrderBaseInfo();
                orderBaseInfo.setDeliveryStatus(deliveryStatus);
                orderBaseInfo.setStatus(status);
                orderBaseInfo.setOrderNumber(orderNo);
                appOrderService.updateOrderBaseInfoStatus(orderBaseInfo);

            }
        } catch (Exception e) {
            e.printStackTrace();
            for (WtaOrderLogistics wtaOrderLogistics : wtaOrderLogisticsList) {
                wtaOrderLogistics.setHandleFlag("0");
                wtaOrderLogistics.setErrMessage(e.getMessage());
                wtaOrderLogistics.setHandleTime(new Date());
                this.updateWtaOrderLogistics(wtaOrderLogistics);
            }
        }
    }

    @Override
    @Transactional
    public Boolean handleWtaShippingOrder(String orderNo, String taskNo) {
        WtaShippingOrderHeader wtaShippingOrderHeader = this.wmsToAppOrderDAO.getWtaShippingOrderHeaderByOrderNoAndTaskNo(orderNo, taskNo);
        try {
            if (null != wtaShippingOrderHeader) {
                List<WtaShippingOrderGoods> wtaShippingOrderGoodsList = null;
                if (OrderUtils.validationOrderNumber(orderNo)) {
                    wtaShippingOrderGoodsList = this.wmsToAppOrderDAO.getWtaShippingOrderGoodsByOrderNo(orderNo);
                } else {
                    wtaShippingOrderGoodsList = this.wmsToAppOrderDAO.getWtaShippingOrderGoods(orderNo, taskNo);
                }
                if (null != wtaShippingOrderGoodsList && wtaShippingOrderGoodsList.size() > 0) {
                    List<OrderGoodsInfo> orderGoodsInfoList = null;
                    City city = null;
                    for (WtaShippingOrderGoods wtaShippingOrderGoods : wtaShippingOrderGoodsList) {
                        GoodsDO goodsDO = goodsService.queryBySku(wtaShippingOrderGoods.getGCode());
                        if (goodsDO == null) {
                            //手动回滚
                            wtaShippingOrderHeader.setErrorMessage("编码为" + wtaShippingOrderGoods.getGCode() + "的商品不存在");
                            wtaShippingOrderHeader.setSendFlag("0");
                            wtaShippingOrderHeader.setSendTime(new Date());
                            this.updateWtaShippingOrderHeader(wtaShippingOrderHeader);
                            throw new RuntimeException();
                        }
                        //这里判断是否是WMS的自己的单子,非APP订单,只做扣减城市库存操作
                        if (OrderUtils.validationOrderNumber(wtaShippingOrderGoods.getOrderNo())) {
                            if (null == orderGoodsInfoList) {
                                orderGoodsInfoList = this.appOrderService.getOrderGoodsQtyInfoByOrderNumber(wtaShippingOrderGoods.getOrderNo());
                                if (null == orderGoodsInfoList || orderGoodsInfoList.size() == 0) {
                                    wtaShippingOrderHeader.setErrorMessage("单号为" + wtaShippingOrderGoods.getOrderNo() + "的商品信息未查到");
                                    wtaShippingOrderHeader.setSendFlag("0");
                                    wtaShippingOrderHeader.setSendTime(new Date());
                                    this.updateWtaShippingOrderHeader(wtaShippingOrderHeader);
                                    throw new RuntimeException();
                                }
                            }
                            Integer dAckQty = null == wtaShippingOrderGoods.getDAckQty() ? 0 : wtaShippingOrderGoods.getDAckQty();
                            for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                                if (wtaShippingOrderGoods.getGCode().equals(orderGoodsInfo.getSku()) && (dAckQty + orderGoodsInfo.getShippingQuantity()) <= orderGoodsInfo.getOrderQuantity()) {
                                    orderGoodsInfo.setShippingQuantity(dAckQty + orderGoodsInfo.getShippingQuantity());
                                    dAckQty = 0;
                                    break;
                                } else if (wtaShippingOrderGoods.getGCode().equals(orderGoodsInfo.getSku()) && (dAckQty + orderGoodsInfo.getShippingQuantity()) > orderGoodsInfo.getOrderQuantity()) {
                                    dAckQty = dAckQty - (orderGoodsInfo.getOrderQuantity() - orderGoodsInfo.getShippingQuantity());
                                    orderGoodsInfo.setShippingQuantity(orderGoodsInfo.getOrderQuantity());
                                }
                            }
                            if (dAckQty > 0) {
                                wtaShippingOrderHeader.setErrorMessage("编码为" + wtaShippingOrderGoods.getGCode() + "的商品出货数量大于订单商品数量");
                                wtaShippingOrderHeader.setSendFlag("0");
                                wtaShippingOrderHeader.setSendTime(new Date());
                                this.updateWtaShippingOrderHeader(wtaShippingOrderHeader);
                                throw new RuntimeException();
                            }
                        } else {
                            if (null == city) {
                                city = new City();
                                WareHouseDO wareHouse = wareHouseService.findByWareHouseNo(wtaShippingOrderHeader.getWhNo());
//                                city = cityService.findByCityNumber(wareHouse.getCityId());
                                city = cityService.findById(Long.parseLong(wareHouse.getCityId()));
                            }
                            //wms扣减城市库存
                            for (int w = 1; w <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; w++) {
                                CityInventory cityInventory = cityService.findCityInventoryByCityIdAndSku(city.getCityId(), wtaShippingOrderGoods.getGCode());
                                if (null == cityInventory) {
                                    cityInventory = CityInventory.transform(goodsDO, city);
                                    cityService.saveCityInventory(cityInventory);
                                }
                                if (cityInventory.getAvailableIty() < wtaShippingOrderGoods.getDAckQty()) {
                                    smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "获取wms出货信息扣减城市库存失败!" + wtaShippingOrderGoods.getTaskNo() +
                                            "该城市下sku为" + wtaShippingOrderGoods.getGCode() + "的商品库存不足!");
                                    wtaShippingOrderHeader.setErrorMessage("该城市下sku为" + wtaShippingOrderGoods.getGCode() + "的商品库存不足!");
                                    wtaShippingOrderHeader.setSendFlag("0");
                                    wtaShippingOrderHeader.setSendTime(new Date());
                                    this.updateWtaShippingOrderHeader(wtaShippingOrderHeader);
                                    throw new RuntimeException();
                                }
                                Integer affectLine = cityService.lockCityInventoryByCityIdAndSkuAndInventory(
                                        city.getCityId(), wtaShippingOrderGoods.getGCode(), -wtaShippingOrderGoods.getDAckQty(), cityInventory.getLastUpdateTime());
                                if (affectLine > 0) {
                                    CityInventoryAvailableQtyChangeLog log = new CityInventoryAvailableQtyChangeLog();
                                    log.setCityId(cityInventory.getCityId());
                                    log.setCityName(cityInventory.getCityName());
                                    log.setGid(cityInventory.getGid());
                                    log.setSku(cityInventory.getSku());
                                    log.setSkuName(cityInventory.getSkuName());
                                    log.setChangeQty(-1 * wtaShippingOrderGoods.getDAckQty());
                                    log.setAfterChangeQty(cityInventory.getAvailableIty() - wtaShippingOrderGoods.getDAckQty());
                                    log.setChangeTime(Calendar.getInstance().getTime());
                                    log.setChangeType(CityInventoryAvailableQtyChangeType.HOUSE_DELIVERY_ORDER);
                                    log.setChangeTypeDesc(CityInventoryAvailableQtyChangeType.HOUSE_DELIVERY_ORDER.getDescription());
                                    log.setReferenceNumber(wtaShippingOrderGoods.getTaskNo());
                                    cityService.addCityInventoryAvailableQtyChangeLog(log);
                                    break;
                                } else {
                                    if (w == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                        smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "获取wms仓库出货信息失败,扣减城市库存失败!任务编号" + wtaShippingOrderGoods.getTaskNo());
                                        wtaShippingOrderHeader.setErrorMessage("获取wms仓库出货信息失败,扣减城市库存失败!任务编号" + wtaShippingOrderGoods.getTaskNo());
                                        wtaShippingOrderHeader.setSendFlag("0");
                                        wtaShippingOrderHeader.setSendTime(new Date());
                                        this.updateWtaShippingOrderHeader(wtaShippingOrderHeader);
                                        throw new RuntimeException();
                                    }
                                }
                            }
                        }
                    }
                    if (OrderUtils.validationOrderNumber(wtaShippingOrderHeader.getOrderNo())) {
                        for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                            if (orderGoodsInfo.getOrderQuantity().equals(orderGoodsInfo.getShippingQuantity())) {
                                this.appOrderService.updateOrderGoodsShippingQuantityByid(orderGoodsInfo);
                            } else {
                                wtaShippingOrderHeader.setErrorMessage("编码为" + orderGoodsInfo.getSku() + "的商品出货数量小于订单商品数量");
                                wtaShippingOrderHeader.setSendFlag("0");
                                wtaShippingOrderHeader.setSendTime(new Date());
                                this.updateWtaShippingOrderHeader(wtaShippingOrderHeader);
                                throw new RuntimeException();
                            }
                        }
                        AppEmployee clerk = appEmployeeService.findDeliveryByClerkNo(wtaShippingOrderHeader.getDriver());
                        if (null == clerk) {
                            wtaShippingOrderHeader.setErrorMessage("未查询该配送员,配送员编号" + wtaShippingOrderHeader.getDriver());
                            wtaShippingOrderHeader.setSendFlag("0");
                            wtaShippingOrderHeader.setSendTime(new Date());
                            this.updateWtaShippingOrderHeader(wtaShippingOrderHeader);
                            throw new RuntimeException();
                        }
                        WareHouseDO wareHouse = wareHouseService.findByWareHouseNo(wtaShippingOrderHeader.getWhNo());
                        //保存物流信息
                        OrderDeliveryInfoDetails deliveryInfoDetails = OrderDeliveryInfoDetails.transform(wtaShippingOrderHeader,
                                null != wareHouse ? wareHouse.getWareHouseName() : wtaShippingOrderHeader.getWhNo());
                        orderDeliveryInfoDetailsService.addOrderDeliveryInfoDetails(deliveryInfoDetails);
                        //修改订单配送信息加入配送员
                        appOrderService.updateOrderLogisticInfoByDeliveryClerkNo(clerk, wtaShippingOrderHeader.getWhNo(), wtaShippingOrderHeader.getOrderNo());
                        //修改订单头状态
                        OrderBaseInfo orderBaseInfo = new OrderBaseInfo();
                        orderBaseInfo.setDeliveryStatus(LogisticStatus.SEALED_CAR);
                        orderBaseInfo.setStatus(AppOrderStatus.PENDING_RECEIVE);
                        orderBaseInfo.setOrderNumber(wtaShippingOrderHeader.getOrderNo());
                        appOrderService.updateOrderBaseInfoStatus(orderBaseInfo);
                    }
                    // 处理完这里逻辑需要修改出货头表的处理状态(暂时使用sendFlag字段代替)
                    wtaShippingOrderHeader.setSendFlag("1");
                    wtaShippingOrderHeader.setSendTime(Calendar.getInstance().getTime());
                    this.wmsToAppOrderDAO.updateWtaShippingOrderHeader(wtaShippingOrderHeader);

                    //保存出货生命周期和出货记录
                    this.appOrderService.addAllOrderLifecycle(OrderLifecycleType.SEALED_CAR, AppOrderStatus.PENDING_RECEIVE, wtaShippingOrderHeader.getOrderNo());
                    this.appOrderService.addAllOrderShipping(wtaShippingOrderHeader.getTaskNo(), wtaShippingOrderHeader.getOrderNo());

                    if (OrderUtils.validationOrderNumber(wtaShippingOrderHeader.getOrderNo())) {
                        return Boolean.TRUE;
                    }
                } else {
                    wtaShippingOrderHeader.setErrorMessage("未查到出货商品！");
                    wtaShippingOrderHeader.setSendFlag("0");
                    wtaShippingOrderHeader.setSendTime(new Date());
                    this.wmsToAppOrderDAO.updateWtaShippingOrderHeader(wtaShippingOrderHeader);
                }
            }
        } catch (Exception e) {
            wtaShippingOrderHeader.setErrorMessage(e.getMessage());
            wtaShippingOrderHeader.setSendFlag("0");
            wtaShippingOrderHeader.setSendTime(new Date());
            this.updateWtaShippingOrderHeader(wtaShippingOrderHeader);
            smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "获取出货单头档wms信息失败!处理出货单头档事务失败!order:" + wtaShippingOrderHeader.getOrderNo());
            return Boolean.FALSE;
        }
        return Boolean.FALSE;
    }

    @Override
    @Transactional
    public void updateWtaOrderLogistics(WtaOrderLogistics wtaOrderLogistics) {
        this.wmsToAppOrderDAO.updateWtaOrderLogistics(wtaOrderLogistics);
    }

    @Override
    @Transactional
    public void handleWtaWarehouseAllocation(String allocationNo) {
        log.info("开始异步处理调拨单业务,调拨单号:{}", allocationNo);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WtaWarehouseAllocationHeader warehouseAllocationHeader = this.wmsToAppOrderDAO.getWtaWarehouseAllocationHeader(allocationNo);
        try {
            if (null != warehouseAllocationHeader) {
                List<WtaWarehouseAllocationGoods> allocationGoodsList = this.wmsToAppOrderDAO.findWtaWarehouseAllocationGoodsListByAllocationNo(warehouseAllocationHeader.getAllocationNo());
                //调出城市
                City outboundCity = cityService.findCityByWarehouseNo(warehouseAllocationHeader.getWarehouseNo());
                //调入城市
                City inboundCity = cityService.findCityByWarehouseNo(warehouseAllocationHeader.getShippingWarehouseNo());
                //调入仓库没有找到对应的城市信息
                if (null == inboundCity) {
                    warehouseAllocationHeader.setErrMessage("仓库编码:" + warehouseAllocationHeader.getShippingWarehouseNo() + "没有找到对应的城市信息!");
                    warehouseAllocationHeader.setHandleFlag("0");
                    warehouseAllocationHeader.setHandleTime(new Date());
                    this.wmsToAppOrderDAO.updateWtaWarehouseAllocation(warehouseAllocationHeader);
                    throw new RuntimeException();
                }
                if (null != allocationGoodsList && allocationGoodsList.size() > 0) {
                    for (WtaWarehouseAllocationGoods allocationGoods : allocationGoodsList) {
                        GoodsDO goodsDO = goodsService.queryBySku(allocationGoods.getSku());
                        if (null == goodsDO) {
                            warehouseAllocationHeader.setErrMessage("商品资料中没有查询到sku为" + allocationGoods.getSku() + "的商品信息!");
                            warehouseAllocationHeader.setHandleFlag("0");
                            warehouseAllocationHeader.setHandleTime(new Date());
                            this.wmsToAppOrderDAO.updateWtaWarehouseAllocation(warehouseAllocationHeader);
                            smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "获取wms信息失败,获取仓库调拨失败,商品资料中没有查询到sku为" + allocationGoods.getSku() + "的商品信息!");
                            throw new RuntimeException();
                        }
                        //调入数量
                        Integer changeInventoryInbound = 0;
                        //调出数量
                        Integer changeInventoryOutbound = 0;
                        CityInventoryAvailableQtyChangeType changeType = null;
                        changeInventoryInbound = allocationGoods.getCheckQty();
                        changeInventoryOutbound = -allocationGoods.getCheckQty();
                        //************************************ 开始处理调出仓库可用量扣减 **************************************
                        if (null != outboundCity) {
                            CityInventory outboundCityInventory = cityService.findCityInventoryByCityIdAndSku(outboundCity.getCityId(), allocationGoods.getSku());
                            if (null == outboundCityInventory) {
                                warehouseAllocationHeader.setErrMessage("调出仓库对应城市下没有找到" + allocationGoods.getSku() + "的城市可用库存信息!");
                                warehouseAllocationHeader.setHandleFlag("0");
                                warehouseAllocationHeader.setHandleTime(new Date());
                                this.wmsToAppOrderDAO.updateWtaWarehouseAllocation(warehouseAllocationHeader);
                                smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "调拨单: " + warehouseAllocationHeader.getAllocationNo()
                                        + " 处理失败， "
                                        + "编码为: " + warehouseAllocationHeader.getWarehouseNo() + " 的仓库对应的城市下没有找到商品:"
                                        + allocationGoods.getSku()
                                        + " 的城市可用库存信息 ");
                                throw new RuntimeException();
                            } else if (outboundCityInventory.getAvailableIty() < allocationGoods.getCheckQty()) {
                                warehouseAllocationHeader.setErrMessage("编码为:" + warehouseAllocationHeader.getWarehouseNo() + "的仓库对应的城市下商品:"
                                        + allocationGoods.getSku() + "可用库存不足!");
                                warehouseAllocationHeader.setHandleFlag("0");
                                warehouseAllocationHeader.setHandleTime(new Date());
                                this.wmsToAppOrderDAO.updateWtaWarehouseAllocation(warehouseAllocationHeader);
                                smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "调拨单:"
                                        + warehouseAllocationHeader.getAllocationNo()
                                        + "处理失败,"
                                        + "编码为:" + warehouseAllocationHeader.getWarehouseNo() + "的仓库对应的城市下商品:"
                                        + allocationGoods.getSku() + "可用库存不足!");
                                throw new RuntimeException();
                            } else {
                                for (int j = 1; j <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; j++) {
                                    Integer affectLine = cityService.lockCityInventoryByCityIdAndSkuAndInventory(
                                            outboundCity.getCityId(), allocationGoods.getSku(), changeInventoryOutbound,
                                            outboundCityInventory.getLastUpdateTime());
                                    if (affectLine > 0) {
                                        CityInventoryAvailableQtyChangeLog log = new CityInventoryAvailableQtyChangeLog();
                                        log.setCityId(outboundCityInventory.getCityId());
                                        log.setCityName(outboundCityInventory.getCityName());
                                        log.setGid(outboundCityInventory.getGid());
                                        log.setSku(outboundCityInventory.getSku());
                                        log.setSkuName(outboundCityInventory.getSkuName());
                                        log.setChangeQty(allocationGoods.getCheckQty());
                                        log.setAfterChangeQty(outboundCityInventory.getAvailableIty() + changeInventoryOutbound);
                                        log.setChangeTime(Calendar.getInstance().getTime());
                                        log.setChangeType(CityInventoryAvailableQtyChangeType.ALLOCATE_OUTBOUND);
                                        log.setChangeTypeDesc(CityInventoryAvailableQtyChangeType.ALLOCATE_OUTBOUND.getDescription());
                                        log.setReferenceNumber(warehouseAllocationHeader.getAllocationNo());
                                        cityService.addCityInventoryAvailableQtyChangeLog(log);
                                        break;
                                    } else {
                                        if (j == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                            smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE,
                                                    "调拨单:" + warehouseAllocationHeader.getAllocationNo() + "处理失败，" +
                                                            "业务繁忙，请稍后处理");
                                            warehouseAllocationHeader.setErrMessage("业务高并发，调拨失败!");
                                            warehouseAllocationHeader.setHandleFlag("0");
                                            warehouseAllocationHeader.setHandleTime(new Date());
                                            this.wmsToAppOrderDAO.updateWtaWarehouseAllocation(warehouseAllocationHeader);
                                            throw new RuntimeException();
                                        }
                                    }
                                }
                            }
                        }

                        //************************************ 调出仓库可用量扣减处理完毕 **************************************

                        //************************************ 开始处理调入仓库可用量增加 **************************************
                        CityInventory cityInventoryInbound = cityService.findCityInventoryByCityIdAndSku(inboundCity.getCityId(), allocationGoods.getSku());
                        if (null == cityInventoryInbound) {
                            cityInventoryInbound = CityInventory.transform(goodsDO, inboundCity);
                            cityInventoryInbound.setAvailableIty(allocationGoods.getCheckQty());
                            cityService.saveCityInventory(cityInventoryInbound);
                        } else {
                            for (int j = 1; j <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; j++) {
                                Integer affectLine = cityService.lockCityInventoryByCityIdAndSkuAndInventory(
                                        inboundCity.getCityId(), allocationGoods.getSku(), changeInventoryInbound, cityInventoryInbound.getLastUpdateTime());
                                if (affectLine > 0) {
                                    CityInventoryAvailableQtyChangeLog log = new CityInventoryAvailableQtyChangeLog();
                                    log.setCityId(cityInventoryInbound.getCityId());
                                    log.setCityName(cityInventoryInbound.getCityName());
                                    log.setGid(cityInventoryInbound.getGid());
                                    log.setSku(cityInventoryInbound.getSku());
                                    log.setSkuName(cityInventoryInbound.getSkuName());
                                    log.setChangeQty(allocationGoods.getCheckQty());
                                    log.setAfterChangeQty(cityInventoryInbound.getAvailableIty() + changeInventoryInbound);
                                    log.setChangeTime(Calendar.getInstance().getTime());
                                    log.setChangeType(CityInventoryAvailableQtyChangeType.ALLOCATE_INBOUND);
                                    log.setChangeTypeDesc(CityInventoryAvailableQtyChangeType.ALLOCATE_INBOUND.getDescription());
                                    log.setReferenceNumber(warehouseAllocationHeader.getAllocationNo());
                                    cityService.addCityInventoryAvailableQtyChangeLog(log);
                                    break;
                                } else {
                                    if (j == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                        smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE,
                                                "调拨单:" + warehouseAllocationHeader.getAllocationNo() + "处理失败，" +
                                                        "业务繁忙，请稍后处理");
                                        warehouseAllocationHeader.setErrMessage("业务高并发，调拨失败!");
                                        warehouseAllocationHeader.setHandleFlag("0");
                                        warehouseAllocationHeader.setHandleTime(new Date());
                                        this.wmsToAppOrderDAO.updateWtaWarehouseAllocation(warehouseAllocationHeader);
                                        smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "调拨单:"
                                                + warehouseAllocationHeader.getAllocationNo() + "处理失败，"
                                                + "业务繁忙，请稍后处理");
                                        throw new RuntimeException();
                                    }
                                }
                            }
                        }
                    }
                    warehouseAllocationHeader.setHandleFlag("1");
                    warehouseAllocationHeader.setHandleTime(new Date());
                    this.wmsToAppOrderDAO.updateWtaWarehouseAllocation(warehouseAllocationHeader);
                } else {
                    warehouseAllocationHeader.setErrMessage("未查询到调拨明细!");
                    warehouseAllocationHeader.setHandleFlag("0");
                    warehouseAllocationHeader.setHandleTime(new Date());
                    this.wmsToAppOrderDAO.updateWtaWarehouseAllocation(warehouseAllocationHeader);
                    smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "调拨单:"
                            + warehouseAllocationHeader.getAllocationNo() + "处理失败，"
                            + "未查询到调拨明细!");
                    throw new RuntimeException("未查询到调拨明细!");
                }
            } else {
                log.info("没有找到该调拨单号信息:{}", allocationNo);
            }
            log.info("调拨单异步处理完毕,单号:{}", allocationNo);

        } catch (Exception e) {
            e.printStackTrace();
            log.info("调拨单:{} 处理出现未知异常，{}", allocationNo, e);
            warehouseAllocationHeader.setHandleFlag("0");
            warehouseAllocationHeader.setErrMessage(e.getMessage());
            warehouseAllocationHeader.setHandleTime(new Date());
            this.wmsToAppOrderDAO.updateWtaWarehouseAllocation(warehouseAllocationHeader);
            smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "调拨单:"
                    + warehouseAllocationHeader.getAllocationNo() + "处理失败，"
                    + "出现未知异常");
            throw new RuntimeException("调拨单:{} 处理出现未知异常，{}\n" + e);
        }
    }

    @Override
    @Transactional
    public void handleWtaWarehousePurchase(String recNo) {
        WtaWarehousePurchaseHeader purchaseHeader = this.wmsToAppOrderDAO.getWtaWarehousePurchaseHeader(recNo);
        try {
            if (null != purchaseHeader) {
                List<WtaWarehousePurchaseGoods> purchaseHeaders = this.wmsToAppOrderDAO.findWtaWarehousePurchaseGoodsListByPurchaseNo(purchaseHeader.getRecNo());
                City city = cityService.findCityByWarehouseNo(purchaseHeader.getWarehouseNo());
                if (null == city) {
                    purchaseHeader.setErrMessage("城市信息中没有查询到城市code为" + purchaseHeader.getCompanyId() + "的数据!");
                    purchaseHeader.setHandleFlag("0");
                    purchaseHeader.setHandleTime(new Date());
                    this.wmsToAppOrderDAO.updateWtaWarehousePurchaseHeader(purchaseHeader);
                    throw new RuntimeException();
                }
                if (null != purchaseHeaders && purchaseHeaders.size() > 0) {
                    for (WtaWarehousePurchaseGoods purchaseGoods : purchaseHeaders) {
                        GoodsDO goodsDO = goodsService.queryBySku(purchaseGoods.getSku());
                        if (null == goodsDO) {
                            smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "获取wms信息失败,获取仓库采购入库失败,商品资料中没有查询到sku为" + purchaseGoods.getSku() + "的商品信息!");
                            purchaseHeader.setErrMessage("商品资料中没有查询到sku为" + purchaseGoods.getSku() + "的商品信息!");
                            purchaseHeader.setHandleFlag("0");
                            purchaseHeader.setHandleTime(new Date());
                            this.wmsToAppOrderDAO.updateWtaWarehousePurchaseHeader(purchaseHeader);
                            throw new RuntimeException();
                        }
                        for (int j = 1; j <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; j++) {
                            CityInventory cityInventory = cityService.findCityInventoryByCityIdAndSku(city.getCityId(), purchaseGoods.getSku());
                            if (null == cityInventory) {
                                cityInventory = CityInventory.transform(goodsDO, city);
                                cityService.saveCityInventory(cityInventory);
                            }
                            Integer affectLine = cityService.lockCityInventoryByCityIdAndSkuAndInventory(
                                    city.getCityId(), purchaseGoods.getSku(), purchaseGoods.getRecQty(), cityInventory.getLastUpdateTime());
                            if (affectLine > 0) {
                                CityInventoryAvailableQtyChangeLog log = new CityInventoryAvailableQtyChangeLog();
                                log.setCityId(cityInventory.getCityId());
                                log.setCityName(cityInventory.getCityName());
                                log.setGid(cityInventory.getGid());
                                log.setSku(cityInventory.getSku());
                                log.setSkuName(cityInventory.getSkuName());
                                log.setChangeQty(purchaseGoods.getRecQty());
                                log.setAfterChangeQty(cityInventory.getAvailableIty() + purchaseGoods.getRecQty());
                                log.setChangeTime(Calendar.getInstance().getTime());
                                log.setChangeType(CityInventoryAvailableQtyChangeType.CITY_PURCHASE_INBOUND);
                                log.setChangeTypeDesc(CityInventoryAvailableQtyChangeType.CITY_PURCHASE_INBOUND.getDescription());
                                log.setReferenceNumber(purchaseHeader.getPurchaseNo());
                                cityService.addCityInventoryAvailableQtyChangeLog(log);
                                break;
                            } else {
                                if (j == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                    smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "获取wms仓库采购失败!任务编号" + purchaseHeader.getPurchaseNo());
                                    purchaseHeader.setErrMessage("仓库采购失败!");
                                    purchaseHeader.setHandleFlag("0");
                                    purchaseHeader.setHandleTime(new Date());
                                    this.wmsToAppOrderDAO.updateWtaWarehousePurchaseHeader(purchaseHeader);
                                    throw new RuntimeException();
                                }
                            }
                        }
                    }
                    purchaseHeader.setHandleFlag("1");
                    purchaseHeader.setHandleTime(new Date());
                    this.wmsToAppOrderDAO.updateWtaWarehousePurchaseHeader(purchaseHeader);
                } else {
                    purchaseHeader.setErrMessage("未查询到采购明细!");
                    purchaseHeader.setHandleFlag("0");
                    purchaseHeader.setHandleTime(new Date());
                    this.wmsToAppOrderDAO.updateWtaWarehousePurchaseHeader(purchaseHeader);
                    throw new RuntimeException();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            purchaseHeader.setHandleFlag("0");
            purchaseHeader.setErrMessage(e.getMessage());
            purchaseHeader.setHandleTime(new Date());
            this.wmsToAppOrderDAO.updateWtaWarehousePurchaseHeader(purchaseHeader);
        }
    }

    @Override
    public WtaCancelOrderResultEnter getWtaCancelOrderResult(String orderNo) {
        return this.wmsToAppOrderDAO.getWtaCancelOrderResult(orderNo);
    }

    @Override
    @Transactional
    public void updateWtaCancelOrderResult(WtaCancelOrderResultEnter cancelOrderResultEnter) {
        this.wmsToAppOrderDAO.updateWtaCancelOrderResult(cancelOrderResultEnter);
    }

    @Override
    public List<WtaShippingOrderHeader> getAllWtaShippingOrderHeader() {
        return this.wmsToAppOrderDAO.getAllWtaShippingOrderHeader();
    }

    @Override
    @Transactional
    public HashedMap handleReturningOrderHeader(String returnNo, String recNo) {
        HashedMap maps = new HashedMap();
        WtaReturningOrderHeader returningOrderHeader = this.wmsToAppOrderDAO.getReturningOrderHeaderByReturnNo(returnNo, recNo);
        try {
            if (null != returningOrderHeader) {
                List<WtaReturningOrderGoods> returningOrderGoodsList = wmsToAppOrderDAO.findWtaReturningOrderGoodsByReturnOrderNo(returningOrderHeader.getPoNo());
                if (null != returningOrderGoodsList && returningOrderGoodsList.size() > 0) {
                    List<ReturnOrderGoodsInfo> returnOrderGoodsInfoList = returnOrderService.findReturnOrderGoodsInfoByOrderNumber(returnNo);
                    //验证反配数量正确
                    for (WtaReturningOrderGoods goods : returningOrderGoodsList) {
                        Boolean flag = Boolean.FALSE;
                        for (ReturnOrderGoodsInfo orderGoodsInfo : returnOrderGoodsInfoList) {
                            if (goods.getGcode().equals(orderGoodsInfo.getSku()) && goods.getRecQty().equals(orderGoodsInfo.getReturnQty())) {
                                flag = Boolean.TRUE;
                            }
                        }
                        if (!flag) {
                            returningOrderHeader.setErrMessage("商品sku为" + goods.getGcode() + "的商品反配数量错误!");
                            returningOrderHeader.setHandleFlag("0");
                            returningOrderHeader.setHandleTime(new Date());
                            this.wmsToAppOrderDAO.updateReturningOrderHeaderByOrderNo(returningOrderHeader);
                            throw new RuntimeException();
                        }
                    }
                    //变更库存、退非第三方金额
                    ReturnOrderBaseInfo returnOrder = returnOrderService.queryByReturnNo(returnNo);
                    City city = cityService.findCityByWarehouseNo(returningOrderHeader.getWhNo());
                    if (null == city) {
                        returningOrderHeader.setErrMessage("城市信息中没有查询到仓库为" + returningOrderHeader.getWhNo() + "的数据!");
                        returningOrderHeader.setHandleFlag("0");
                        returningOrderHeader.setHandleTime(new Date());
                        this.wmsToAppOrderDAO.updateReturningOrderHeaderByOrderNo(returningOrderHeader);
                        throw new RuntimeException();
                    }
                    if (returnOrder.getReturnType().equals(ReturnOrderType.REFUSED_RETURN)) {
                        OrderBaseInfo orderBaseInfo = appOrderService.getOrderDetail(returnOrder.getOrderNo());
                        OrderBillingDetails orderBillingDetails = appOrderService.getOrderBillingDetail(orderBaseInfo.getOrderNumber());
                        ReturnOrderBaseInfo returnOrderBaseInfo = returnOrderService.queryByReturnNo(returnOrder.getReturnNo());

                        maps = returnOrderService.refusedOrder(orderBaseInfo.getOrderNumber(), orderBaseInfo, orderBillingDetails, returnOrderBaseInfo, returnOrderGoodsInfoList, city);
                    } else if (ReturnOrderType.NORMAL_RETURN.equals(returnOrder.getReturnType())) {
                        maps = returnOrderService.normalReturnOrderProcessing(returnNo, city);
                    }

                    return maps;
                } else {
                    returningOrderHeader.setErrMessage("未查询到反配明细!");
                    returningOrderHeader.setHandleFlag("0");
                    returningOrderHeader.setHandleTime(new Date());
                    this.wmsToAppOrderDAO.updateReturningOrderHeaderByOrderNo(returningOrderHeader);
                    throw new RuntimeException();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            returningOrderHeader.setHandleFlag("0");
            returningOrderHeader.setErrMessage(e.getMessage());
            returningOrderHeader.setHandleTime(new Date());
            this.wmsToAppOrderDAO.updateReturningOrderHeaderByOrderNo(returningOrderHeader);
            throw new RuntimeException();
        }
        return null;
    }

    @Override
    public WtaReturningOrderHeader getReturningOrderHeaderByReturnNo(String returnNo, String recNo) {
        return this.wmsToAppOrderDAO.getReturningOrderHeaderByReturnNo(returnNo, recNo);
    }

    @Override
    public void updateReturningOrderHeaderByOrderNo(WtaReturningOrderHeader returningOrderHeader) {
        this.wmsToAppOrderDAO.updateReturningOrderHeaderByOrderNo(returningOrderHeader);
    }

}
