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
@Transactional
public class PaymentDataServiceImpl implements PaymentDataService {

    @Autowired
    private PaymentDataDAO paymentDataDAO;

    @Override
    public PaymentDataDO save(PaymentDataDO paymentDataDO) {
        this.paymentDataDAO.save(paymentDataDO);
        return paymentDataDO;
    }

    @Override
    public List<PaymentDataDO> findByOutTradeNoAndTradeStatus(String outTradeNo, PaymentDataStatus tradeStatus) {
        return this.paymentDataDAO.findByOutTradeNoAndTradeStatus(outTradeNo, tradeStatus);
    }

    @Override
    public void updateByTradeStatusIsWaitPay(PaymentDataDO paymentDataDO) {
        this.paymentDataDAO.updateByTradeStatusIsWaitPay(paymentDataDO);
    }
}