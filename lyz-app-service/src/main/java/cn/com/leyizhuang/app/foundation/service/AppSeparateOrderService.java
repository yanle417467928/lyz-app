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

    void sendOrderReceiptInf(String orderNumber);

    void sendOrderCouponInf(String orderNumber);

    void updateOrderCouponFlagAndSendTimeAndErrorMsg(List<Long> couponInfIds, String msg, Date sendTime, AppWhetherFlag flag);

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
}
