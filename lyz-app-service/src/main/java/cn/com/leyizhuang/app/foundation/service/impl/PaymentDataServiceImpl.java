package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.PaymentDataStatus;
import cn.com.leyizhuang.app.foundation.dao.PaymentDataDAO;
import cn.com.leyizhuang.app.foundation.pojo.PaymentDataDO;
import cn.com.leyizhuang.app.foundation.service.PaymentDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/20
 */
@Service
public class PaymentDataServiceImpl implements PaymentDataService {

    @Autowired
    private PaymentDataDAO paymentDataDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentDataDO save(PaymentDataDO paymentDataDO) {
        this.paymentDataDAO.save(paymentDataDO);
        return paymentDataDO;
    }

    @Override
    public List<PaymentDataDO> findByOutTradeNoAndTradeStatus(String outTradeNo, PaymentDataStatus tradeStatus) {
        return this.paymentDataDAO.findByOutTradeNoAndTradeStatus(outTradeNo, tradeStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByTradeStatusIsWaitPay(PaymentDataDO paymentDataDO) {
        this.paymentDataDAO.updateByTradeStatusIsWaitPay(paymentDataDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByTradeStatusIsWaitRefund(PaymentDataDO paymentDataDO) {
        this.paymentDataDAO.updateByTradeStatusIsWaitRefund(paymentDataDO);
    }

    @Override
    public PaymentDataDO findPaymentDataDOByOutTradeNo(String outTradeNo) {
        return paymentDataDAO.findPaymentDataDOByOutTradeNo(outTradeNo);
    }

    @Override
    public PaymentDataDO findPaymentDataDOByOrderNumber(String orderNumber) {
        return paymentDataDAO.findPaymentDataDOByOrderNumber(orderNumber);
    }
}
