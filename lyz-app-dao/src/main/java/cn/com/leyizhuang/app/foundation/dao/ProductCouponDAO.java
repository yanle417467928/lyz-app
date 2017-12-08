package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.OrderCouponType;
import cn.com.leyizhuang.app.foundation.pojo.CustomerProductCoupon;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderCouponInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderUsableProductCouponResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/10/19
 */
@Repository
public interface ProductCouponDAO {

    List<OrderUsableProductCouponResponse> findProductCouponByCustomerIdAndGoodsId(
            @Param(value = "userId") Long userId, @Param("list") List<Long> goodsIds);

    Long findGoodsIdByUserIdAndProductCouponId(@Param("userId") Long userId, @Param("pcId") Long pcId);

    /**
     * 查询订单使用券
     * @param orderId   用户id
     * @param couponType    券类型
     * @return  订单券
     */
    List<OrderCouponInfo> findOrderCouponByCouponTypeAndUserId(@Param("orderId")Long orderId, @Param("couponType")OrderCouponType couponType);

    /**
     * 获取顾客产品券信息
     * @param cusProductCouponId    产品券id
     * @return  产品券信息
     */
    CustomerProductCoupon findCusProductCouponByCouponId(@Param("cusProductCouponId")Long cusProductCouponId);

    /**
     * 添加产品券
     * @param customerProductCoupon 产品券
     */
    void addCustomerProductCoupon(CustomerProductCoupon customerProductCoupon);
}
