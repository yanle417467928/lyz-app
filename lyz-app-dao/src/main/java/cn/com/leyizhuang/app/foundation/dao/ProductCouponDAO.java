package cn.com.leyizhuang.app.foundation.dao;

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

    Long findGoodsIdByUserIdAndProductCouponId(@Param("userId") Long userId,@Param("pcId") Long pcId);
}
