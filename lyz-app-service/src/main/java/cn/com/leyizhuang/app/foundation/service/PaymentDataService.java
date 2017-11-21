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
}
