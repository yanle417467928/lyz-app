package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.PaymentDataStatus;
import cn.com.leyizhuang.app.foundation.pojo.PaymentDataDO;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/20
 */
public interface PaymentDataService {

    PaymentDataDO save(PaymentDataDO paymentDataDO);

    List<PaymentDataDO> findByOutTradeNoAndTradeStatus(String outTradeNo, PaymentDataStatus tradeStatus);

    void updateByTradeStatusIsWaitPay(PaymentDataDO paymentDataDO);

    void updateByTradeStatusIsWaitRefund(PaymentDataDO paymentDataDO);

    /**
     * 查询还款记录详情
     * @param outTradeNo    还款号
     * @return  还款记录详情
     */
    PaymentDataDO findPaymentDataDOByOutTradeNo(String outTradeNo);

    /**
     * 查询还款记录详情
     * @param orderNumber 订单号
     * @return 还款记录详情
     */
    PaymentDataDO findPaymentDataDOByOrderNumber(String orderNumber);

    List<PaymentDataDO> findByOrderNoAndTradeStatus(String ordNo, PaymentDataStatus tradeStatus);
}
