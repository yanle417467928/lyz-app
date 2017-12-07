package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.OrderCouponType;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderCouponInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderUsableProductCouponResponse;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/10/19
 */
public interface ProductCouponService {

    List<OrderUsableProductCouponResponse> findProductCouponByCustomerIdAndGoodsId(
            Long userId, List<Long> goodsIds);

    Long findGoodsIdByUserIdAndProductCouponId(Long userId, Long id);

    /**
     * 查询订单使用券
     * @param orderId   用户id
     * @param couponType    券类型
     * @return  订单券
     */
    List<OrderCouponInfo> findOrderCouponByCouponTypeAndUserId(Long orderId,OrderCouponType couponType);
}
