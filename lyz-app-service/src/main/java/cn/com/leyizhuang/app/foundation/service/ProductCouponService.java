package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.OrderCouponType;
import cn.com.leyizhuang.app.foundation.pojo.CustomerProductCoupon;
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
     *
     * @param orderId    用户id
     * @param couponType 券类型
     * @return 订单券
     */
    List<OrderCouponInfo> findOrderCouponByCouponTypeAndUserId(Long orderId, OrderCouponType couponType);

    /**
     * 获取顾客产品券信息
     *
     * @param cusProductCouponId 产品券id
     * @return 产品券信息
     */
    CustomerProductCoupon findCusProductCouponByCouponId(Long cusProductCouponId);

    /**
     * 添加产品券
     * @param customerProductCoupon 产品券
     */
    void addCustomerProductCoupon(CustomerProductCoupon customerProductCoupon);
}
