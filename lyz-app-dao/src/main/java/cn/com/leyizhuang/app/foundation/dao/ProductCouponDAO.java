package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.OrderCouponType;
import cn.com.leyizhuang.app.foundation.pojo.CustomerProductCoupon;
import cn.com.leyizhuang.app.foundation.pojo.CustomerProductCouponChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.ProductCoupon;
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
    List<OrderCouponInfo> findOrderCouponByCouponTypeAndOrderId(@Param("orderId")Long orderId, @Param("couponType")OrderCouponType couponType);

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


    /*** 产品券模版 ****/

    /**
     * 返回产品券模版集合
     * @param keywords
     * @return
     */
    List<ProductCoupon> queryByKeywords(@Param("keywords") String keywords);

    /**
     * 根据id返回结果
     */
    ProductCoupon queryProductCouponById(@Param("id") Long id);

    /**
     * 新增产品券模版
     */
    void addProductCoupon(ProductCoupon productCoupon);

    /**
     * 更新产品券
     */
    void updateProductCoupon(ProductCoupon productCoupon);


    /**
     * 删除产品券模版
     */
    void deleteProductCoupon(@Param("ids") List<Long> ids);

    /**
     * 取消订单修改产品券信息
     */
    void updateCustomerProductCoupon(CustomerProductCoupon customerProductCoupon);


    /**
     * 更新产品劵是否已退
     */
    void updateProductCouponIsReturn(@Param(value = "id") Long id,@Param(value = "isReturn") Boolean isReturn);

    /**
     * 添加顾客产品券变更日志
     * @param customerProductCouponChangeLog
     */
    void addCustomerProductCouponChangeLog(CustomerProductCouponChangeLog customerProductCouponChangeLog);
}
