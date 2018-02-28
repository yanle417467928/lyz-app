package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Jerry.Ren
 * Notes:WMS~APP订单出货单头持久层
 * Created with IntelliJ IDEA.
 * Date: 2017/12/26.
 * Time: 18:11.
 */
@Repository
public interface WmsToAppOrderDAO {
    /**
     * 持久化订单出货头档
     *
     * @param header 订单出货头档
     */
    void saveWtaShippingOrderHeader(WtaShippingOrderHeader header);

    /**
     * 保存订单出货商品明细
     *
     * @param goods 出货商品
     */
    void saveWtaShippingOrderGoods(WtaShippingOrderGoods goods);

    /**
     * 保存返配商品头档
     *
     * @param header 返配单头
     */
    void saveWtaReturningOrderHeader(WtaReturningOrderHeader header);

    /**
     * 保存返配单商品
     *
     * @param goods 返配单商品
     */
    void saveWtaReturningOrderGoods(WtaReturningOrderGoods goods);

    /**
     * 保存退货单配送员
     *
     * @param deliveryClerk
     */
    void saveWtaReturnOrderDeliveryClerk(WtaReturnOrderDeliveryClerk deliveryClerk);

    /**
     * 保存取消订单结果
     *
     * @param orderResultEnter
     */
    void saveWtaCancelOrderResultEnter(WtaCancelOrderResultEnter orderResultEnter);

    /**
     * 保存取消退单结果
     *
     * @param returnOrderResultEnter
     */
    void saveWtaCancelReturnOrderResultEnter(WtaCancelReturnOrderResultEnter returnOrderResultEnter);

    /**
     * 保存整转零信息
     *
     * @param wholeSplitToUnit
     */
    void saveWtaWarehouseWholeSplitToUnit(WtaWarehouseWholeSplitToUnit wholeSplitToUnit);

    /**
     * 保存仓库调拨头档
     *
     * @param header
     */
    void saveWtaWarehouseAllocationHeader(WtaWarehouseAllocationHeader header);

    /**
     * 保存仓库调拨明细
     *
     * @param allocationGoods
     */
    void saveWtaWarehouseAllocationGoods(WtaWarehouseAllocationGoods allocationGoods);

    /**
     * 保存仓库采购头档
     *
     * @param purchaseHeader
     */
    void saveWtaWarehousePurchaseHeader(WtaWarehousePurchaseHeader purchaseHeader);

    /**
     * 保存仓库采购明细
     *
     * @param purchaseGoods
     */
    void saveWtaWarehousePurchaseGoods(WtaWarehousePurchaseGoods purchaseGoods);

    /**
     * 保存仓库报损报溢
     *
     * @param damageAndOverflow
     */
    void saveWtaWarehouseReportDamageAndOverflow(WtaWarehouseReportDamageAndOverflow damageAndOverflow);

    /**
     * 根据调拨单号查询调拨单商品
     *
     * @param allocationNo
     * @return
     */
    List<WtaWarehouseAllocationGoods> findWtaWarehouseAllocationGoodsListByAllocationNo(String allocationNo);

    /**
     * 根据采购单查询调采购商品
     *
     * @param purchaseNo
     * @return
     */
    List<WtaWarehousePurchaseGoods> findWtaWarehousePurchaseGoodsListByPurchaseNo(String purchaseNo);

    /**
     * 根据退货单号查询返配商品
     *
     * @param returnOrderNumber
     * @return
     */
    List<WtaReturningOrderGoods> findWtaReturningOrderGoodsByReturnOrderNo(String returnOrderNumber);
}
