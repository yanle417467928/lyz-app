package cn.com.leyizhuang.app.foundation.service;

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
}
