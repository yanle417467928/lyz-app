package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.request.OrderLockExpendRequest;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.BillingSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.DeliverySimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponseGoods;

import java.util.List;

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

    //用户获取我的订单列表
    List<OrderBaseInfo> getOrderListByUserIDAndIdentityType(Long userID, Integer identityType, Integer showStatus);

    //获取订单所有商品
    List<OrderGoodsInfo> getOrderGoodsInfoByOrderNumber(String orderNumber);

    //获取订单应付/实付金额
    Double getAmountPayableByOrderNumber(String orderNumber);

    //计算获取订单所有商品数量
    Integer querySumQtyByOrderNumber(String orderNumber);

    //模糊查询订单
    List<OrderBaseInfo> getFuzzyQuery(Long userID, Integer identityType, String condition);

    OrderBaseInfo getOrderByOrderNumber(String outTradeNo);

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
    OrderBaseInfo createOrderBaseInfo(Long cityId, Long userId, Integer identityType, Long customerId, String deliveryType, String remark);

    /**
     * 生成订单物流信息 OrderLogisticInfo
     *
     * @param deliverySimpleInfo 页面填写的物流相关信息
     * @return OrderLogisticInfo 订单物流信息
     */
    OrderLogisticsInfo createOrderLogisticInfo(DeliverySimpleInfo deliverySimpleInfo);

    OrderBillingDetails createOrderBillingDetails(OrderBillingDetails orderBillingDetails, Long userId, Integer identityType,
                                                  BillingSimpleInfo billing, List<Long> cashCouponIds);
}
