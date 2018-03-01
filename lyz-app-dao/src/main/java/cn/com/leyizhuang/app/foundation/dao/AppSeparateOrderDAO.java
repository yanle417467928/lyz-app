package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingPaymentDetails;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomerFxStoreRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 拆单数据仓库
 *
 * @author Richard
 * @date 2018/01/04
 */
@Repository
public interface AppSeparateOrderDAO {


    Boolean isOrderExist(@Param(value = "orderNumber") String orderNumber);

    void saveOrderBaseInf(OrderBaseInf baseInf);

    void saveOrderGoodsInf(OrderGoodsInf goodsInf);

    void saveOrderCouponInf(OrderCouponInf couponInf);

    void saveOrderReceiptInf(OrderReceiptInf receiptInf);

    void updateOrderBaseInfSendFlagAndErrorMessageAndSendTime(@Param(value = "orderNumber") String orderNumber,
                                                              @Param(value = "flag") AppWhetherFlag flag,
                                                              @Param(value = "errorMsg") String errorMsg,
                                                              @Param(value = "sendTime") Date sendTime);

    void updateOrderGoodsInfoSendFlagAndErrorMessageAndSendTime(@Param(value = "orderLineId") Long orderLineId,
                                                                @Param(value = "flag") AppWhetherFlag flag,
                                                                @Param(value = "errorMsg") String errorMsg,
                                                                @Param(value = "sendTime") Date sendTime);

    List<OrderBaseInf> getPendingSendOrderBaseInf(String orderNumber);

    List<OrderGoodsInf> getOrderGoodsInfByOrderNumber(String orderNumber);

    List<OrderBillingPaymentDetails> getOrderBillingPaymentDetailsByOrderNumber(String orderNumber);

    List<OrderReceiptInf> getOrderReceiptInf(String orderNumber);

    List<OrderCouponInf> getOrderCouponInf(String orderNumber);

    void updateOrderCouponFlagAndSendTimeAndErrorMsg(@Param(value = "couponInfIds") List<Long> couponInfIds,
                                                     @Param(value = "msg") String msg,
                                                     @Param(value = "sendTime") Date sendTime,
                                                     @Param(value = "flag") AppWhetherFlag flag);

    void updateOrderGoodsInfByOrderNumber(@Param(value = "orderNumber") String orderNumber,
                                          @Param(value = "flag") AppWhetherFlag flag,
                                          @Param(value = "errorMsg") String message,
                                          @Param(value = "sendTime") Date sendTime);

    void updateOrderReceiptFlagAndSendTimeAndErrorMsg(@Param(value = "receiptInfIds") List<Long> receiptInfIds,
                                                      @Param(value = "errorMsg") String msg,
                                                      @Param(value = "sendTime") Date sendTime,
                                                      @Param(value = "flag") AppWhetherFlag flag);

    Boolean isRechargeReceiptExist(String rechargeNo);

    void saveRechargeReceiptInf(RechargeReceiptInf rechargeReceiptInf);

    RechargeReceiptInf getRechargeReceiptInfByRechargeNo(String rechargeNo);

    void updateRechargeReceiptFlagAndSendTimeAndErrorMsg(@Param(value = "receiptId") Long receiptId,
                                                         @Param(value = "msg") String msg,
                                                         @Param(value = "sendTime") Date sendTime,
                                                         @Param(value = "flag") AppWhetherFlag flag);

    void saveOrderJxPriceDifferenceReturnInf(OrderJxPriceDifferenceReturnInf returnInf);

    List<OrderJxPriceDifferenceReturnInf> getOrderJxPriceDifferenceReturnInf(String orderNumber);

    void updateOrderJxPriceDifferenceReturnInf(@Param(value = "returnInfIds") List<Long> returnInfIds,
                                               @Param(value = "msg") String msg,
                                               @Param(value = "sendTime") Date sendTime,
                                               @Param(value = "flag") AppWhetherFlag flag);

    Boolean isReturnOrderExist(String returnNumber);

    void updateOrderJxPriceDifferenceRefundInf(@Param(value = "refundInfIds") List<Long> refundInfIds,
                                               @Param(value = "msg") String msg,
                                               @Param(value = "sendTime") Date sendTime,
                                               @Param(value = "flag") AppWhetherFlag flag);

    void saveOrderJxPriceDifferenceRefundInf(ReturnOrderJxPriceDifferenceRefundInf refundInf);

    List<ReturnOrderJxPriceDifferenceRefundInf> getOrderJxPriceDifferenceRefundInf(String returnNumber);


    void updateOrderReceiveFlagAndSendTimeAndErrorMsg(@Param(value = "receiveInfsId") Long receiveInfsId,
                                                      @Param(value = "msg") String msg,
                                                      @Param(value = "sendTime") Date sendTime,
                                                      @Param(value = "flag") AppWhetherFlag flag);


    void updateReturnOrderFlagAndSendTimeAndErrorMsg(@Param(value = "rtHeaderId") Long rtHeaderId,
                                                     @Param(value = "msg") String msg,
                                                     @Param(value = "sendTime") Date sendTime,
                                                     @Param(value = "flag") AppWhetherFlag flag);

    OrderBaseInf getOrderBaseInfByMainOrderNumberAndCompanFlag(@Param(value = "orderNumber") String orderNumber,
                                                               @Param(value = "flag") String flag);

    //分退单号
    void saveReturnOrderBaseInf(ReturnOrderBaseInf returnOrderBaseInf);

    void saveReturnOrderGoodsInf(ReturnOrderGoodsInf returnOrderGoodsInf);

    void saveReturnOrderCouponInf(ReturnOrderCouponInf returnOrderCouponInf);

    void saveReturnOrderRefundInf(ReturnOrderRefundInf returnOrderRefundInf);

    List<ReturnOrderBaseInf> getReturnOrderBaseInfByReturnNumber(String returnNumber);

    List<ReturnOrderGoodsInf> getReturnOrderGoodsInfByReturnNumber(String returnNumber);

    void updateReturnOrderBaseInf(@Param(value = "returnNumber") String returnNumber,
                                  @Param(value = "flag") AppWhetherFlag flag,
                                  @Param(value = "errorMsg") String errorMsg,
                                  @Param(value = "sendTime") Date sendTime);

    void updateReturnOrderGoodsInf(@Param(value = "returnNumber") String returnNumber,
                                   @Param(value = "flag") AppWhetherFlag flag,
                                   @Param(value = "errorMsg") String errorMsg,
                                   @Param(value = "sendTime") Date sendTime);

    List<ReturnOrderCouponInf> getReturnOrderCouponInf(String returnNumber);

    void updateReturnOrderCouponInf(@Param(value = "returnCouponInfLineId") List<Long> returnCouponInfLineId,
                                    @Param(value = "msg") String msg,
                                    @Param(value = "sendTime") Date sendTime,
                                    @Param(value = "flag") AppWhetherFlag flag);

    List<ReturnOrderRefundInf> getReturnOrderRefundInf(String returnNumber);

    void updateReturnOrderRefundInf(@Param(value = "refundInfIds") List<Long> refundInfIds,
                                    @Param(value = "msg") String msg,
                                    @Param(value = "sendTime") Date sendTime,
                                    @Param(value = "flag") AppWhetherFlag flag);

    List<ReturnOrderJxPriceDifferenceRefundInf> getReturnOrderJxPriceDifferenceRefundInf(String returnNumber);

    void updateReturnOrderJxPriceDifferenceRefundInf(@Param(value = "refundInfIds") List<Long> refundInfIds,
                                                     @Param(value = "msg") String msg,
                                                     @Param(value = "sendTime") Date sendTime,
                                                     @Param(value = "flag") AppWhetherFlag flag);

    void saveOrderKeyInf(OrderKeyInf orderKeyInf);

    OrderKeyInf getOrderKeyInfByMainOrderNumber(@Param(value = "orderNumber") String orderNumber);

    void updateOrderKeyInfFlagAndSendTimeAndErrorMsg(@Param(value = "id") Long id,
                                                     @Param(value = "msg") String msg,
                                                     @Param(value = "sendTime") Date sendTime,
                                                     @Param(value = "sendFlag") AppWhetherFlag sendFlag);

    AppCustomerFxStoreRelation getCustomerFxStoreRelationByCusId(Long customerIdTemp);

    void isWithdrawRefundExist(@Param(value = "refundNo") String refundNo);

    void saveWithdrawRefundInf(WithdrawRefundInf withdrawRefundInf);

    WithdrawRefundInf getWithdrawRefundInfByRefundNo(@Param(value = "refundNo") String refundNo);

    void updateWithdrawRefundFlagAndSendTimeAndErrorMsg(@Param(value = "refundId") Long refundId,
                                                        @Param(value = "msg") String msg,
                                                        @Param(value = "sendTime") Date sendTime,
                                                        @Param(value = "flag") AppWhetherFlag flag);
}
