package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.OrderCouponType;
import cn.com.leyizhuang.app.foundation.pojo.CustomerProductCoupon;
import cn.com.leyizhuang.app.foundation.pojo.CustomerProductCouponChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.ProductCoupon;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderCouponInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderUsableProductCouponResponse;
import com.github.pagehelper.PageInfo;

import java.time.LocalDateTime;
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
     * @param id    id
     * @param isReturn 是否已退
     * @return
     */
     void updateProductCouponIsReturn(Long id,Boolean isReturn);
    /**
     * 查询订单使用券
     *
     * @param orderId    用户id
     * @param couponType 券类型
     * @return 订单券
     */
    List<OrderCouponInfo> findOrderCouponByCouponTypeAndOrderId(Long orderId, OrderCouponType couponType);

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


    void addProductCoupon(ProductCoupon productCoupon);
    void updateProductCoupon(ProductCoupon productCoupon);
    void deletedProductCoupon(List<Long> ids);

    /**
     * 产品券模版 分页查询
     * @param page
     * @param size
     * @param keywords
     * @return
     */
    PageInfo<ProductCoupon> queryPage(Integer page, Integer size, String keywords);

    /**
     * 根据id返回产品券模版结果
     * @param id
     * @return
     */
    ProductCoupon queryProductCouponById(Long id);

    /**
     * 取消订单修改产品券信息
     */
    void updateCustomerProductCoupon(CustomerProductCoupon customerProductCoupon);

    /**
     * 添加顾客产品券变更日志
     * @param customerProductCouponChangeLog
     */
    void addCustomerProductCouponChangeLog(CustomerProductCouponChangeLog customerProductCouponChangeLog);

    /**
     * 根据订单号激活产品券
     * @param ordNo
     */
    void activateCusProductCoupon(String ordNo);
}
