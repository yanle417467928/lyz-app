package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.AppOrderStatus;
import cn.com.leyizhuang.app.core.constant.LogisticStatus;
import cn.com.leyizhuang.app.foundation.pojo.CustomerProductCoupon;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.PayhelperOrder;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderGoodsInf;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/10
 */
@Repository
public interface OrderDAO {

    List<MaterialListDO> getGoodsInfoByOrderNumber(String orderNumber);

    //用户获取我的订单列表
    List<OrderBaseInfo> getOrderListByUserIDAndIdentityType(@Param("userID") Long userID,
                                                            @Param("identityType") AppIdentityType identityType,
                                                            @Param("showStatus") Integer showStatus);

    //获取订单所有商品
    List<OrderGoodsInfo> getOrderGoodsInfoByOrderNumber(@Param("orderNumber") String orderNumber);

    //根据头id获取商品信息
    OrderGoodsInfo getOrderGoodsInfoById(Long id);

    //获取订单应付/实付金额
    Double getAmountPayableByOrderNumber(@Param("orderNumber") String orderNumber);

    //获取订单总金额
    Double getTotalGoodsPriceByOrderNumber(@Param("orderNumber") String orderNumber);

    //计算获取订单所有商品数量
    Integer querySumQtyByOrderNumber(@Param("orderNumber") String orderNumber);

    //模糊查询订单
    List<OrderBaseInfo> getFuzzyQuery(@Param("userID") Long userID,
                                      @Param("identityType") AppIdentityType identityType,
                                      @Param("condition") String condition,
                                      @Param("sellerType") String sellerType,
                                      @Param("storeId") Long storeId);

    OrderBaseInfo findByOrderNumber(@Param("orderNumber") String orderNumber);

    List<OrderBaseInfo> findErrorOrderData();

    List<OrderBaseInfo> findErrorCouponOrderData();

    List<Long> findOrderPromotionId(@Param("ordNo") String ordNo);

    OrderGoodsInf findOrderGoodsInfByLineId(@Param("lineId") Long lineId);

    List<OrderGoodsInfo> getOrderGoodsByOrderNumberAndSkuAndGoodsLineType(@Param("ordNo") String ordNo,@Param("sku") String sku,@Param("GoodsLineType") String GoodsLineType);

    List<CustomerProductCoupon> getCusProductCouponByOrderNumber(@Param("ordNo") String ordNo);

    void updateOrderGoodsPrice(OrderGoodsInfo orderGoodsInfo);

    void updateOrderGoodsInfPrice(OrderGoodsInf orderGoodsInf);

    List<OrderGoodsInfo> findOrderGoodsAndPresentLine(@Param("ordNo") String ordNo,@Param("actId") Long actId);

    //获取订单头详情
    OrderBaseInfo getOrderDetail(@Param("orderNumber") String orderNumber);

    //获取订单收货/自提门店地址
    OrderLogisticsInfo getOrderLogistice(@Param("orderNumber") String orderNumber);

    //获取订单账目明细
    OrderBillingDetails getOrderBillingDetail(@Param("orderNumber") String orderNumber);

    //获取订单商品
    List<GiftListResponseGoods> getOrderGoodsDetails(@Param("orderNumber") String orderNumber);

    OrderTempInfo getOrderInfoByOrderNo(String orderNo);

    List<OrderGoodsZgInfo> getOrderGoodsZgInfoByUserId(@Param("userId") Long userId);

    void savePaymentDetails(OrderBillingPaymentDetails orderBillingPaymentDetails);

    void updateOwnMoneyByOrderNo(OrderBillingDetails orderBillingDetails);

    void updateOrderStatusByOrderNo(OrderBaseInfo orderBaseInfo);

    void saveOrderBaseInfo(OrderBaseInfo orderBaseInfo);

    void saveOrderLogisticsInfo(OrderLogisticsInfo orderLogisticsInfo);

    void saveOrderGoodsInfo(OrderGoodsInfo goodsInfo);

    void saveOrderGoodsZgInfo(OrderGoodsZgInfo orderGoodsZgInfo);

    void saveOrderBillingDetails(OrderBillingDetails orderBillingDetails);

    void saveOrderBillingPaymentDetail(OrderBillingPaymentDetails paymentDetail);

    void updateOrderBaseInfo(OrderBaseInfo baseInfo);

    void updateOrderGoodsInfo(OrderGoodsInfo baseInfo);

    void updateOrderBillingDetails(OrderBillingDetails billingDetails);

    void deleteOrderGoodsInfo(@Param("id") Long id);

    /**
     * 根据订单号修改订单状态
     *
     * @param status      订单状态
     * @param orderNumber 订单号
     */
    void updateOrderStatusAndDeliveryStatusByOrderNo(@Param("status") AppOrderStatus status,
                                                     @Param("deliveryStatus") LogisticStatus deliveryStatus, @Param("orderNumber") String orderNumber);

    /**
     * 获取支付明细
     *
     * @param orderNo 订单号
     * @return 所有支付明细
     */
    List<OrderBillingPaymentDetails> getOrderBillingDetailListByOrderNo(@Param("orderNo") String orderNo);

    OrderArrearageInfoResponse getOrderArrearageInfo(String orderNo);

    List<OrderGoodsListResponse> getOrderGoodsList(@Param("orderNumber") String orderNumber);

    void saveOrderCouponInfo(OrderCouponInfo couponInfo);

    List<OrderListResponse> getPendingEvaluationOrderListByUserIDAndIdentityType(@Param(value = "userID") Long userID,
                                                                                 @Param(value = "identityType") AppIdentityType identityType);

    List<OrderCouponInfo> getOrderCouponInfoByOrderNumber(String orderNumber);

    Integer getUnpaidOrderQuantityByEmpId(Long id);

    Integer getUnpaidOrderQuantityByCusId(Long id);


    Integer getPendingReceiveOrderQuantityByEmpId(Long id);

    Integer getPendingReceiveOrderQuantityByCusId(Long id);

    Integer getIsEvaluatedOrderQuantityByEmpId(Long id);

    Integer getIsEvaluatedOrderQuantityByCusId(Long id);

    Integer getReturningOrderQuantityByEmpId(Long id);

    Integer getReturningOrderQuantityByCusId(Long id);

    void saveOrderJxPriceDifferenceReturnDetails(OrderJxPriceDifferenceReturnDetails returnDetails);

    List<OrderJxPriceDifferenceReturnDetails> getOrderJxPriceDifferenceReturnDetailsByOrderNumber(String orderNumber);

    void updateReturnableQuantityAndReturnQuantityById(@Param("qty") int returnQty,@Param("ogi") Long orderGoodsId);

    void updateOrderGoodsShippingQuantity(@Param("orderNo") String orderNo, @Param("sku") String gCode, @Param("qty") Integer dAckQty);

    void updateOrderLogisticInfo(OrderLogisticsInfo logisticsInfo);

    List<OrderBillingPaymentDetails> getOrderBillingDetailListByReceiptNumber(String receiptNumber);

    //用户获取查看物流单列表
    List<OrderBaseInfo> getPendingShipmentAndPendingReceive(@Param("userId") Long userId,
                                                            @Param("identityType") AppIdentityType identityType);

    void saveOrderLifecycle(OrderLifecycle lifecycle);

    List<OrderPageInfoVO> getOrderListPageInfoByUserIdAndIdentityType(@Param(value = "userID") Long userID,
                                                                      @Param(value = "identityType") AppIdentityType identityType,
                                                                      @Param(value = "showStatus") Integer showStatus,
                                                                      @Param(value = "sellerType") String sellerType,
                                                                      @Param(value = "storeId") Long storeId);

    List<OrderBaseInfo> getSendToWMSFailedOrder();

    List<String> getNotSellDetailsOrderNOs(@Param("flag") Boolean flag, @Param("fristDay") LocalDateTime fristDay);

    List<OrderGoodsInfo> getOrderGoodsQtyInfoByOrderNumber(String orderNumber);

    void updateOrderGoodsShippingQuantityByid(OrderGoodsInfo orderGoodsInfo);

    void updateOrderBaseInfoStatus(OrderBaseInfo baseInfo);

    /**
     * 查询订单下 产品券购买价之和
     * @param ordNo
     * @return
     */
    Double getOrderProductCouponPurchasePrice(@Param("ordNo") String ordNo,@Param("sku") String sku);

    void saveOrderShipping(OrderShipping orderShipping);

    List<OrderPageInfoVO> getFitOrderListPageInfoByUserIdAndIdentityType(@Param(value = "userId") Long userId,
                                                                         @Param(value = "identityType") AppIdentityType identityType,
                                                                         @Param(value = "keywords") String keywords);

    PayhelperOrder findPayhelperOrderByOrdNo(String ordNo);

    Boolean existOrder(String orderNo);
    int savePayhelperOrder(PayhelperOrder payhelperOrder);

    /**
     * 客户经理查看自己支付的订单
     *
     * @param userId
     * @return
     */
    List<OrderPageInfoVO> findSellerManagerPayForOrderList(@Param("userId") Long userId);
}
