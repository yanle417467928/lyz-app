package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.WmsToAppOrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.*;
import cn.com.leyizhuang.app.foundation.service.WmsToAppOrderService;
import cn.com.leyizhuang.common.util.AssertUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2017/12/26.
 * Time: 18:09.
 */
@Service
public class WmsToAppOrderServiceImpl implements WmsToAppOrderService {

    @Resource
    private WmsToAppOrderDAO wmsToAppOrderDAO;

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
    public void updateWtaShippingOrderHeader(WtaShippingOrderHeader header) {
        if (AssertUtil.isNotEmpty(header)) {
            wmsToAppOrderDAO.updateWtaShippingOrderHeader(header);
        }
    }

    @Override
    public WtaShippingOrderHeader getWtaShippingOrderHeaderNotHandling(String orderNo) {
        if (AssertUtil.isNotEmpty(orderNo)) {
            return wmsToAppOrderDAO.getWtaShippingOrderHeaderNotHandling(orderNo);
        }
        return null;
    }

    @Override
    public WtaShippingOrderHeader getWtaShippingOrderHeader(String orderNo){
        if (AssertUtil.isNotEmpty(orderNo)){
            return wmsToAppOrderDAO.getWtaShippingOrderHeader(orderNo);
        }
        return  null;
    }

    @Override
    public WtaReturningOrderHeader getWtaReturningOrderHeaderByReturnNumber(String returnNo) {
        if (AssertUtil.isNotEmpty(returnNo)) {
            return wmsToAppOrderDAO.getWtaReturningOrderHeaderByReturnNumber(returnNo);
        }
        return null;
    }
}
