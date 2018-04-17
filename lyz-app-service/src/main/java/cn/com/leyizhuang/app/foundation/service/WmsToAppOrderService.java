package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.*;

import java.util.List;

/**
 * @author Jerry.Ren
 * Notes: WMS~APP订单服务
 * Created with IntelliJ IDEA.
 * Date: 2017/12/26.
 * Time: 18:07.
 */

public interface WmsToAppOrderService {

    /**
     * 保存订单出货头档
     *
     * @param header 订单出货头档
     * @return 返回所影响行数
     */
    int saveWtaShippingOrderHeader(WtaShippingOrderHeader header);

    /**
     * 保存订单出货商品明细
     *
     * @param goods 出货商品
     * @return 返回所影响行数
     */
    int saveWtaShippingOrderGoods(WtaShippingOrderGoods goods);

    /**
     * 保存返配商品头档
     *
     * @param header 返配单头
     * @return 返回所影响行数
     */
    int saveWtaReturningOrderHeader(WtaReturningOrderHeader header);

    /**
     * 保存返配单商品
     *
     * @param goods 返配单商品
     * @return 返回所影响行数
     */
    int saveWtaReturningOrderGoods(WtaReturningOrderGoods goods);

    /**
     * 保存退货单配送员
     *
     * @param deliveryClerk
     * @return 返回所影响行数
     */
    int saveWtaReturnOrderDeliveryClerk(WtaReturnOrderDeliveryClerk deliveryClerk);

    /**
     * 保存取消订单结果
     *
     * @param orderResultEnter
     * @return 返回所影响行数
     */
    int saveWtaCancelOrderResultEnter(WtaCancelOrderResultEnter orderResultEnter);

    /**
     * 保存取消退单结果
     *
     * @param returnOrderResultEnter
     * @return 返回所影响行数
     */
    int saveWtaCancelReturnOrderResultEnter(WtaCancelReturnOrderResultEnter returnOrderResultEnter);

    /**
     * 保存整转零信息
     *
     * @param wholeSplitToUnit
     * @return 返回所影响行数
     */
    int saveWtaWarehouseWholeSplitToUnit(WtaWarehouseWholeSplitToUnit wholeSplitToUnit);

    /**
     * 保存仓库调拨头档
     *
     * @param allocation
     * @return 返回所影响行数
     */
    int saveWtaWarehouseAllocationHeader(WtaWarehouseAllocationHeader allocation);

    /**
     * 保存仓库调拨明细
     *
     * @param allocationGoods
     * @return 返回所影响行数
     */
    int saveWtaWarehouseAllocationGoods(WtaWarehouseAllocationGoods allocationGoods);

    /**
     * 保存仓库采购头档
     *
     * @param purchaseHeader
     * @return 返回所影响行数
     */
    int saveWtaWarehousePurchaseHeader(WtaWarehousePurchaseHeader purchaseHeader);

    /**
     * 保存仓库采购明细
     *
     * @param purchaseGoods
     * @return 返回所影响行数
     */
    int saveWtaWarehousePurchaseGoods(WtaWarehousePurchaseGoods purchaseGoods);

    /**
     * 保存仓库报损报溢
     *
     * @param damageAndOverflow
     * @return 返回所影响行数
     */
    int saveWtaWarehouseReportDamageAndOverflow(WtaWarehouseReportDamageAndOverflow damageAndOverflow);

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

    /**
     * 根据退单号修改分配配送员信息
     *
     * @param deliveryClerk
     */
    void updateWtaReturnOrderDeliveryClerk(WtaReturnOrderDeliveryClerk deliveryClerk);

    /**
     * 更新出货单的处理状态
     *
     * @param header
     */
    void updateWtaShippingOrderHeader(WtaShippingOrderHeader header);

    /**
     * 查询未处理的出货单
     *
     * @param orderNo
     * @return
     */
    WtaShippingOrderHeader getWtaShippingOrderHeaderNotHandling(String orderNo, String taskNo);

    WtaShippingOrderHeader getWtaShippingOrderHeader(String orderNo);

    /**
     * 查询返配单号
     *
     * @param returnNo
     * @return
     */
    WtaReturningOrderHeader getWtaReturningOrderHeaderByReturnNumber(String returnNo);
}
