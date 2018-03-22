package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.AppRechargeOrderStatus;
import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.foundation.dto.CreditBillingDTO;
import cn.com.leyizhuang.app.foundation.dto.CusPreDepositDTO;
import cn.com.leyizhuang.app.foundation.dto.StorePreDepositDTO;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.PaymentDataDO;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorationCompanyCreditBillingDO;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeOrder;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeReceiptInfo;

import java.util.Date;
import java.util.List;

/**
 * 充值API
 *
 * @author Richard
 * Created on 2018-01-11 14:43
 **/
public interface RechargeService {

    RechargeOrder createRechargeOrder(Integer identityType, Long userId, Double money, String rechargeNo);

    void saveRechargeOrder(RechargeOrder rechargeOrder);

    RechargeReceiptInfo createOnlinePayRechargeReceiptInfo(PaymentDataDO paymentDataDO, String tradeStatus);

    void saveRechargeReceiptInfo(RechargeReceiptInfo receiptInfo);

    void updateRechargeOrderStatusAndPayUpTime(String rechargeNo, Date payUpTime, AppRechargeOrderStatus status);

    List<RechargeReceiptInfo> findRechargeReceiptInfoByRechargeNo(String rechargeNo);

    List<RechargeOrder> findRechargeOrderByRechargeNo(String rechargeNo);

    RechargeReceiptInfo createCusPayRechargeReceiptInfo(Integer identityType, CusPreDepositDTO cusPreDepositDTO, String rechargeNo);

    RechargeOrder createCusRechargeOrder(Integer identityType, Long userId, Double money, String rechargeNo, OrderBillingPaymentType payType);

    RechargeOrder createStoreRechargeOrder(Integer identityType, Long storeId, Double money, String rechargeNo, OrderBillingPaymentType payType);

    RechargeReceiptInfo createStorePayRechargeReceiptInfo(Integer identityType, StorePreDepositDTO storePreDepositDTO, String rechargeNo, AppStore store);

    RechargeReceiptInfo createCreditRechargeReceiptInfo(DecorationCompanyCreditBillingDO creditBillingDO, CreditBillingDTO creditBillingDTO, String paymentType);

    RechargeOrder createCreditRechargeOrder(DecorationCompanyCreditBillingDO creditBillingDO, Double amount, String paymentType);

    List<RechargeReceiptInfo> findRechargeReceiptInfoByReceiptNumber(String receiptNumber);

    List<RechargeOrder> findRechargeOrderByWithdrawNo(String withdrawNo);

}
