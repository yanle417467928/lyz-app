package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.*;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * 拆单服务接口
 *
 * @author Richard
 * @create 2018/01/04.
 */
public interface AppSeparateOrderService {


    Boolean isOrderExist(String orderNumber);

    void saveOrderBaseInf(OrderBaseInf baseInf);

    void saveOrderGoodsInf(OrderGoodsInf goodsInf);

    void separateOrder(String orderNumber);

    void saveOrderCouponInf(OrderCouponInf couponInf);

    void saveOrderReceiptInf(OrderReceiptInf receiptInf);

    void updateOrderBaseInfSendFlagAndErrorMessageAndSendTime(String orderNumber, AppWhetherFlag flag, String errorMsg, Date sendTime);

    void sendOrderBaseInfAndOrderGoodsInf(String orderNumber);

    void sendOrderBaseInfAndOrderNotXQGoodsInf(String orderNumber);

    void sendOrderReceiptInf(String orderNumber);

    void sendOrderCouponInf(String orderNumber);

    void sendOrderReceivableInf(String orderNumber);

    void updateOrderCouponFlagAndSendTimeAndErrorMsg(List<Long> couponInfIds, String msg, Date sendTime, AppWhetherFlag flag);

    void updateOrderReceivableFlagAndSendTimeAndErrorMsg(List<Long> couponInfIds, String msg, Date sendTime, AppWhetherFlag flag);

    void updateOrderGoodsInfByOrderNumber(String orderNumber, AppWhetherFlag flag, String message, Date sendTime);

    void updateOrderReceiptFlagAndSendTimeAndErrorMsg(List<Long> receiptInfIds, String msg, Date sendTime, AppWhetherFlag flag);

    Boolean isRechargeReceiptExist(String rechargeNo);

    void separateRechargeReceipt(String rechargeNo);

    void sendRechargeReceiptInf(String rechargeNo);

    void updateRechargeReceiptFlagAndSendTimeAndErrorMsg(Long receiptId, String msg, Date sendTime, AppWhetherFlag flag);

    void saveOrderJxPriceDifferenceReturnInf(OrderJxPriceDifferenceReturnInf returnInf);

    void saveOrderJxPriceDifferenceRefundInf(ReturnOrderJxPriceDifferenceRefundInf refundInf);

    void sendOrderJxPriceDifferenceReturnInf(String orderNumber);

   /* void sendOrderJxPriceDifferenceRefundInf(String returnNumber);*/

    void updateOrderJxPriceDifferenceReturnInf(List<Long> returnInfIds, String msg, Date sendTime, AppWhetherFlag flag);

    Boolean isReturnOrderExist(String returnNumber);

    void separateReturnOrder(String returnNumber);

    void updateOrderJxPriceDifferenceRefundInf(List<Long> refundInfIds, String msg, Date sendTime, AppWhetherFlag flag);

    void updateOrderReceiveFlagAndSendTimeAndErrorMsg(Long receiveInfsId, String msg, Date sendTime, AppWhetherFlag flag);

    void updateReturnOrderFlagAndSendTimeAndErrorMsg(Long rtHeaderId, String msg, Date sendTime, AppWhetherFlag flag);

    void saveReturnOrderBaseInf(ReturnOrderBaseInf returnOrderBaseInf);

    void saveReturnOrderGoodsInf(ReturnOrderGoodsInf returnOrderGoodsInf);

    void saveReturnOrderCouponInf(ReturnOrderCouponInf returnOrderCouponInf);

    void saveReturnOrderRefundInf(ReturnOrderRefundInf p);

    void sendReturnOrderBaseInfAndReturnOrderGoodsInf(String returnNumber);

    void updateReturnOrderBaseInf(String returnNumber, AppWhetherFlag flag, String errorMsg, Date sendTime);

    void updateReturnOrderGoodsInf(String returnNumber, AppWhetherFlag flag,String errorMsg, Date sendTime);

    void sendReturnOrderCouponInf(String returnNumber);

    void sendReturnOrderReceivableInf(String returnNumber);

    void updateReturnOrderCouponInf(List<Long> returnCouponInfLineId, String msg, Date sendTime, AppWhetherFlag flag);

    void sendReturnOrderRefundInf(String returnNumber);

    void updateReturnOrderRefundInf(List<Long> refundInfIds, String msg, Date sendTime, AppWhetherFlag flag);

    void sendReturnOrderJxPriceDifferenceRefundInf(String returnNumber);

    void updateReturnOrderJxPriceDifferenceRefundInf(List<Long> refundInfIds, String msg, Date sendTime, AppWhetherFlag flag);

    void saveOrderKeyInf(OrderKeyInf orderKeyInf);

    void sendOrderKeyInf(String orderNumber);

    void updateOrderKeyInfFlagAndSendTimeAndErrorMsg(Long id, String msg, Date sendTime, AppWhetherFlag sendFlag);

    Boolean isWithdrawRefundExist(String refundNo);

    void separateWithdrawRefund(String refundNo) throws UnsupportedEncodingException;

    void sendWithdrawRefundInf(String refundNo);

    void updateWithdrawRefundFlagAndSendTimeAndErrorMsg(Long refundId, String msg, Date sendTime, AppWhetherFlag flag);

    Boolean isReceiptExist(String receiptNumber);

    void separateOrderReceipt(String receiptNumber);

    void sendOrderReceiptInfByReceiptNumber(String receiptNumber);

    void separateOrderRefund(String returnNumber);

    void saveOrderRefundInf(ReturnOrderRefundInf refundInf);

    Boolean isCreditRechargeReceiptExist(String receiptNumber);

    void separateCreditRechargeReceipt(String receiptNumber);

    void sendCreditRechargeReceiptInf(String receiptNumber);

    void separateOrderAndGoodsInf(String orderNumber);

    void separateOrderCouponInf(String orderNumber);

    void separateOrderReceiptInf(String orderNumber);

    void separateOrderJxPriceInf(String orderNumber);

    void separateOrderKeyInfInf(String orderNumber);

    void separateReturnOrderAndGoodsInf(String returnNumber);

    void separateReturnOrderCouponInf(String returnNumber);

    void separateReturnOrderRefundInf(String returnNumber);

    void separateReturnOrderJxPriceInf(String returnNumber);

    void saveOrderReceivableInf(OrderReceivablePriceInf orderReceivablePriceInf);

    List<OrderReceivablePriceInf> findOrderReceivableInfByMainOrderNumber(String mainOrderNumber);

    List<KdSell> getOrderKdSellByMainOrderNumber(String mainOrderNumber);

    void saveKdSellList(List<KdSell> kdSellList);

    List<KdSell> getReturnOrderKdSellByMainOrderNumber(String mainOrderNumber);

    void sendKdSell(String mainOrderNumber);

    void updateKdSellFlagAndSendTimeAndErrorMsg(List<Long> kdSellIds, String msg, Date sendTime, AppWhetherFlag flag);

    List<String> separateAllNotSplitOrder();
}
