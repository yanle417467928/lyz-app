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
    public void saveWtaShippingOrderHeader(WtaShippingOrderHeader header) {
        if (AssertUtil.isNotEmpty(header)) {
            wmsToAppOrderDAO.saveWtaShippingOrderHeader(header);
        }
    }

    @Override
    public void saveWtaShippingOrderGoods(WtaShippingOrderGoods goods) {
        if (AssertUtil.isNotEmpty(goods)) {
            wmsToAppOrderDAO.saveWtaShippingOrderGoods(goods);
        }
    }

    @Override
    public void saveWtaReturningOrderHeader(WtaReturningOrderHeader header) {
        if (AssertUtil.isNotEmpty(header)) {
            wmsToAppOrderDAO.saveWtaReturningOrderHeader(header);
        }
    }

    @Override
    public void saveWtaReturningOrderGoods(WtaReturningOrderGoods goods) {
        if (AssertUtil.isNotEmpty(goods)) {
            wmsToAppOrderDAO.saveWtaReturningOrderGoods(goods);
        }
    }

    @Override
    public void saveWtaReturnOrderDeliveryClerk(WtaReturnOrderDeliveryClerk deliveryClerk) {
        if (AssertUtil.isNotEmpty(deliveryClerk)) {
            wmsToAppOrderDAO.saveWtaReturnOrderDeliveryClerk(deliveryClerk);
        }
    }

    @Override
    public void saveWtaCancelOrderResultEnter(WtaCancelOrderResultEnter orderResultEnter) {
        if (AssertUtil.isNotEmpty(orderResultEnter)) {
            wmsToAppOrderDAO.saveWtaCancelOrderResultEnter(orderResultEnter);
        }
    }

    @Override
    public void saveWtaCancelReturnOrderResultEnter(WtaCancelReturnOrderResultEnter returnOrderResultEnter) {
        if (AssertUtil.isNotEmpty(returnOrderResultEnter)) {
            wmsToAppOrderDAO.saveWtaCancelReturnOrderResultEnter(returnOrderResultEnter);
        }
    }

    @Override
    public void saveWtaWarehouseWholeSplitToUnit(WtaWarehouseWholeSplitToUnit wholeSplitToUnit) {
        if (AssertUtil.isNotEmpty(wholeSplitToUnit)) {
            wmsToAppOrderDAO.saveWtaWarehouseWholeSplitToUnit(wholeSplitToUnit);
        }
    }

    @Override
    public void saveWtaWarehouseAllocationHeader(WtaWarehouseAllocationHeader header) {
        if (AssertUtil.isNotEmpty(header)) {
            wmsToAppOrderDAO.saveWtaWarehouseAllocationHeader(header);
        }
    }

    @Override
    public void saveWtaWarehouseAllocationGoods(WtaWarehouseAllocationGoods allocationGoods) {
        if (AssertUtil.isNotEmpty(allocationGoods)) {
            wmsToAppOrderDAO.saveWtaWarehouseAllocationGoods(allocationGoods);
        }
    }

    @Override
    public void saveWtaWarehousePurchaseHeader(WtaWarehousePurchaseHeader purchaseHeader) {
        if (AssertUtil.isNotEmpty(purchaseHeader)) {
            wmsToAppOrderDAO.saveWtaWarehousePurchaseHeader(purchaseHeader);
        }
    }

    @Override
    public void saveWtaWarehousePurchaseGoods(WtaWarehousePurchaseGoods purchaseGoods) {
        if (AssertUtil.isNotEmpty(purchaseGoods)) {
            wmsToAppOrderDAO.saveWtaWarehousePurchaseGoods(purchaseGoods);
        }
    }

    @Override
    public void saveWtaWarehouseReportDamageAndOverflow(WtaWarehouseReportDamageAndOverflow damageAndOverflow) {
        if (AssertUtil.isNotEmpty(damageAndOverflow)) {
            wmsToAppOrderDAO.saveWtaWarehouseReportDamageAndOverflow(damageAndOverflow);
        }
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
}
