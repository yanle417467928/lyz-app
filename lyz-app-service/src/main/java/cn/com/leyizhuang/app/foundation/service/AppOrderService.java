package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.AppOrderStatus;
import cn.com.leyizhuang.app.core.constant.LogisticStatus;
import cn.com.leyizhuang.app.core.constant.OrderLifecycleType;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.PayhelperOrder;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.request.GoodsIdQtyParam;
import cn.com.leyizhuang.app.foundation.pojo.request.OrderLockExpendRequest;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.BillingSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.DeliverySimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Created by Jerry.Ren
 * Date: 2017/10/30.
 * Time: 18:07.
 *
 * @author Jerry
 */

public interface AppOrderService {


    int lockUserExpendOfOrder(OrderLockExpendRequest lockExpendRequest);

    Boolean existGoodsStoreInventory(Long storeId, Long gid, Integer qty);

    Boolean existGoodsCityInventory(Long cityId, Long gid, Integer qty);

    List<Long> existOrderGoodsInventory(Long cityId, List<GoodsIdQtyParam> goodsList, List<GoodsIdQtyParam> giftList, List<GoodsIdQtyParam> couponList);

    //用户获取我的订单列表
    PageInfo<OrderBaseInfo> getOrderListByUserIDAndIdentityType(Long userID, Integer identityType, Integer showStatus, Integer page, Integer size);

    //获取物流单列表
    PageInfo<OrderBaseInfo> getPendingShipmentAndPendingReceive(Long userId, Integer identityType, Integer page, Integer size);

    //获取订单所有商品
    List<OrderGoodsInfo> getOrderGoodsInfoByOrderNumber(String orderNumber);

    //获取订单应付/实付金额
    Double getAmountPayableByOrderNumber(String orderNumber);

    //获取订单总金额
    Double getTotalGoodsPriceByOrderNumber(String orderNumber);

    //计算获取订单所有商品数量
    Integer querySumQtyByOrderNumber(String orderNumber);

    //模糊查询订单
    List<OrderBaseInfo> getFuzzyQuery(Long userID, Integer identityType, String condition);

    OrderBaseInfo getOrderByOrderNumber(String orderNumber);

    //获取订单头详情
    OrderBaseInfo getOrderDetail(String orderNumber);

    //获取订单收货/自提门店地址
    OrderLogisticsInfo getOrderLogistice(String orderNumber);

    //获取订单账目明细
    OrderBillingDetails getOrderBillingDetail(String orderNumber);

    //获取订单商品
    List<GiftListResponseGoods> getOrderGoodsDetails(String orderNumber);

    List<MaterialListDO> getGoodsInfoByOrderNumber(String orderNumber);

    OrderTempInfo getOrderInfoByOrderNo(String orderNo);

    OrderBillingPaymentDetails savePaymentDetails(OrderBillingPaymentDetails orderBillingPaymentDetails);

    OrderBillingDetails updateOwnMoneyByOrderNo(OrderBillingDetails orderBillingDetails);

    OrderBaseInfo updateOrderStatusByOrderNo(OrderBaseInfo orderBaseInfo);

    void saveOrderBillingPaymentDetails(String orderNumber, Double money, String replyNumber,String receiptNumber);

    void saveOrderBaseInfo(OrderBaseInfo orderBaseInfo);

    void saveOrderLogisticsInfo(OrderLogisticsInfo orderLogisticsInfo);

    void saveOrderGoodsInfo(OrderGoodsInfo goodsInfo);

    void saveOrderBillingDetails(OrderBillingDetails orderBillingDetails);

    void saveOrderBillingPaymentDetail(OrderBillingPaymentDetails paymentDetail);
    /**
     * 创建订单基础信息 OrderBaseInfo
     *
     * @param cityId       城市id
     * @param userId       用户id
     * @param identityType 用户身份类型
     * @param customerId   顾客id
     * @param deliveryType 配送方式
     * @return OrderBaseInfo 订单基础信息
     */
    OrderBaseInfo createOrderBaseInfo(Long cityId, Long userId, Integer identityType, Long customerId, String deliveryType, String remark, String salesNumber) throws UnsupportedEncodingException;

    /**
     * 生成订单物流信息 OrderLogisticInfo
     *
     * @param deliverySimpleInfo 页面填写的物流相关信息
     * @return OrderLogisticInfo 订单物流信息
     */
    OrderLogisticsInfo createOrderLogisticInfo(DeliverySimpleInfo deliverySimpleInfo);

    OrderBillingDetails createOrderBillingDetails(OrderBillingDetails orderBillingDetails, Long userId, Integer identityType,
                                                  BillingSimpleInfo billing, List<Long> cashCouponIds,List<OrderGoodsInfo> productCouponGoodsList);

    void updateOrderBaseInfo(OrderBaseInfo baseInfo);

    void updateOrderBillingDetails(OrderBillingDetails billingDetails);

    /**
     * 根据订单号修改订单状态
     * @param status    订单状态
     * @param orderNumber   订单号
     */
    void updateOrderStatusAndDeliveryStatusByOrderNo(AppOrderStatus status,LogisticStatus deliveryStatus,String orderNumber);


    List<OrderBillingPaymentDetails> getOrderBillingDetailListByOrderNo(String orderNo);

    OrderArrearageInfoResponse getOrderArrearageInfo(String orderNo);


    List<OrderGoodsListResponse> getOrderGoodsList(String orderNumber);

    void saveOrderCouponInfo(OrderCouponInfo couponInfo);

    PageInfo<OrderListResponse> getPendingEvaluationOrderListByUserIDAndIdentityType(Long userID, Integer identityType, Integer page, Integer size);

    List<OrderCouponInfo> getOrderCouponInfoByOrderNumber(String orderNumber);

    Map<String,Integer> getAppOrderQuantityByEmpId(Long id);

    Map<String,Integer> getAppOrderQuantityByCusId(Long id);

    void saveOrderJxPriceDifferenceReturnDetails(OrderJxPriceDifferenceReturnDetails returnDetails);

    List<OrderJxPriceDifferenceReturnDetails> getOrderJxPriceDifferenceReturnDetailsByOrderNumber(String orderNumber);
    void updateReturnableQuantityAndReturnQuantityById(int returnQty, Long orderGoodsId);

    void updateOrderGoodsShippingQuantity(String orderNo, String gCode, Integer dAckQty);

    void saveWeChatOrderBillingPaymentDetails(String orderNumber, Double money, String replyNumber,String receiptNumber);

    void saveAliPayOrderBillingPaymentDetails(String orderNumber, Double money, String replyNumber, String receiptNumber);

    void updateOrderLogisticInfoByDeliveryClerkNo(AppEmployee clerk, String warehouse, String orderNo);

    List<OrderBillingPaymentDetails> getOrderBillingDetailListByReceiptNumber(String receiptNumber);

    OrderGoodsInfo getOrderGoodsInfoById(Long id);

    void addOrderLifecycle(OrderLifecycleType payed, String orderNumber);

    void saveOrderLifecycle(OrderLifecycle lifecycle);

    PageInfo<OrderPageInfoVO> getOrderListPageInfoByUserIdAndIdentityType(Long userID, Integer identityType, Integer showStatus, Integer page, Integer size);

    List<OrderBaseInfo> getSendToWMSFailedOrder();

    List<String> getNotSellDetailsOrderNOs( Boolean flag);

    List<OrderGoodsInfo> getOrderGoodsQtyInfoByOrderNumber(String orderNumber);

    void updateOrderGoodsShippingQuantityByid(OrderGoodsInfo orderGoodsInfo);

    void updateOrderBaseInfoStatus(OrderBaseInfo baseInfo);

    /**
     * 查询订单下 产品券购买价之和
     * @param ordNo
     * @param sku
     * @return
     */
    Double getOrderProductCouponPurchasePrice(String ordNo,String sku);

    void addAllOrderLifecycle(OrderLifecycleType lifecycleType, AppOrderStatus orderStatus, String orderNumber);

    void addAllOrderShipping(String shippingNo, String orderNumber);

    void saveOrderShipping(OrderShipping orderShipping);

    ResultDTO<GiftListResponse> checkGoodsCompanyFlag(List<Long> goodsIds, Long userId, Integer identityType);

    String returnType(List<Long> goodsIds,Long userId,Integer identityType);

    PageInfo<OrderPageInfoVO> getFitOrderListPageInfoByUserIdAndIdentityType(Long userId, Integer identityType, String keywords, Integer page, Integer size);

    PayhelperOrder findPayhelperOrderByOrdNo(String ordNo);

    int savePayhelperOrder(PayhelperOrder payhelperOrder);
}
