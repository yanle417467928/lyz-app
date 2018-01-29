package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.*;

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
}
