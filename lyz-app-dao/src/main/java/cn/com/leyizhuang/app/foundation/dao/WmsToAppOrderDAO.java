package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.*;
import org.springframework.stereotype.Repository;

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

    void saveWtaReturnOrderDeliveryClerk(WtaReturnOrderDeliveryClerk deliveryClerk);

    void saveWtaCancelOrderResultEnter(WtaCancelOrderResultEnter orderResultEnter);

    void saveWtaCancelReturnOrderResultEnter(WtaCancelReturnOrderResultEnter returnOrderResultEnter);
}
