package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.AppRechargeOrderStatus;
import cn.com.leyizhuang.app.foundation.pojo.PaymentDataDO;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeOrder;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeReceiptInfo;

import java.util.Date;

/**
 * 充值API
 *
 * @author Richard
 * Created on 2018-01-11 14:43
 **/
public interface RechargeService {

    RechargeOrder createRechargeOrder(Integer identityType, Long userId, Double money, String rechargeNo);

    void saveRechargeOrder(RechargeOrder rechargeOrder);

    RechargeReceiptInfo createRechargeReceiptInfo(PaymentDataDO paymentDataDO, String tradeStatus);

    void saveRechargeReceiptInfo(RechargeReceiptInfo receiptInfo);

    void updateRechargeOrderStatusAndPayUpTime(String rechargeNo, Date payUpTime, AppRechargeOrderStatus status);
}
