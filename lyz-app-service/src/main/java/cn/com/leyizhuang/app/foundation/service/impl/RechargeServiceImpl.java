package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dao.RechargeDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.PaymentDataDO;
import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeOrder;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeReceiptInfo;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.CityService;
import cn.com.leyizhuang.app.foundation.service.RechargeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 充值API实现
 *
 * @author Richard
 * Created on 2018-01-11 14:44
 **/
@Service
public class RechargeServiceImpl implements RechargeService {

    @Resource
    private AppStoreService storeService;

    @Resource
    private RechargeDAO rechargeDAO;

    @Resource
    private CityService cityService;


    @Override
    public RechargeOrder createRechargeOrder(Integer identityType, Long userId, Double money, String rechargeNo) {
        RechargeOrder rechargeOrder = new RechargeOrder();
        rechargeOrder.setCreateTime(new Date());
        rechargeOrder.setCreatorId(userId);
        rechargeOrder.setCreatorIdentityType(AppIdentityType.
                getAppIdentityTypeByValue(identityType));
        if (identityType == AppIdentityType.CUSTOMER.getValue()) {
            rechargeOrder.setRechargeAccountType(RechargeAccountType.CUS_PREPAY);
            rechargeOrder.setCustomerId(userId);
            rechargeOrder.setPaymentSubjectType(PaymentSubjectType.CUSTOMER);

        } else if (identityType == AppIdentityType.SELLER.getValue()) {
            rechargeOrder.setRechargeAccountType(RechargeAccountType.ST_PREPAY);
            AppStore store = storeService.findStoreByUserIdAndIdentityType(userId, identityType);
            rechargeOrder.setStoreId(store.getStoreId());
            rechargeOrder.setPaymentSubjectType(PaymentSubjectType.SELLER);
        } else if (identityType == AppIdentityType.DECORATE_MANAGER.getValue()) {
            rechargeOrder.setRechargeAccountType(RechargeAccountType.ST_PREPAY);
            AppStore store = storeService.findStoreByUserIdAndIdentityType(userId, identityType);
            rechargeOrder.setStoreId(store.getStoreId());
            rechargeOrder.setPaymentSubjectType(PaymentSubjectType.DECORATE_MANAGER);
        }
        rechargeOrder.setPaymentSubjectTypeDesc(rechargeOrder.getPaymentSubjectType().getDescription());
        rechargeOrder.setRechargeAccountTypeDesc(rechargeOrder.getRechargeAccountType().getDescription());
        rechargeOrder.setPayType(OrderBillingPaymentType.ALIPAY);
        rechargeOrder.setPayTypeDesc(rechargeOrder.getPayType().getDescription());
        rechargeOrder.setAmount(money);
        rechargeOrder.setRechargeNo(rechargeNo);
        rechargeOrder.setStatus(AppRechargeOrderStatus.UNPAID);
        return rechargeOrder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRechargeOrder(RechargeOrder rechargeOrder) {
        if (null != rechargeOrder) {
            rechargeDAO.saveRechargeOrder(rechargeOrder);
        }
    }

    @Override
    public RechargeReceiptInfo createOnlinePayRechargeReceiptInfo(PaymentDataDO paymentDataDO, String tradeStatus) {
        RechargeReceiptInfo receiptInfo = new RechargeReceiptInfo();
        receiptInfo.setCreateTime(new Date());
        receiptInfo.setPayTime(new Date());
        receiptInfo.setAmount(paymentDataDO.getTotalFee());
        switch (paymentDataDO.getAppIdentityType()) {
            case CUSTOMER:
                receiptInfo.setPaymentSubjectType(PaymentSubjectType.CUSTOMER);
                receiptInfo.setRechargeAccountType(RechargeAccountType.CUS_PREPAY);
                break;
            case DECORATE_MANAGER:
                receiptInfo.setPaymentSubjectType(PaymentSubjectType.DECORATE_MANAGER);
                receiptInfo.setRechargeAccountType(RechargeAccountType.ST_PREPAY);
                break;
            case SELLER:
                receiptInfo.setPaymentSubjectType(PaymentSubjectType.SELLER);
                receiptInfo.setRechargeAccountType(RechargeAccountType.ST_PREPAY);
                break;
            default:
                break;
        }
        receiptInfo.setPaymentSubjectTypeDesc(receiptInfo.getPaymentSubjectType().getDescription());
        receiptInfo.setRechargeAccountTypeDesc(receiptInfo.getRechargeAccountType().getDescription());
        switch (paymentDataDO.getOnlinePayType()) {
            case ALIPAY:
                receiptInfo.setPayType(OrderBillingPaymentType.ALIPAY);
                break;
            case WE_CHAT:
                receiptInfo.setPayType(OrderBillingPaymentType.WE_CHAT);
                break;
            case UNION_PAY:
                receiptInfo.setPayType(OrderBillingPaymentType.UNION_PAY);
            default:
                break;
        }
        receiptInfo.setPayTypeDesc(receiptInfo.getPayType().getDescription());
        receiptInfo.setRechargeNo(paymentDataDO.getOutTradeNo());
        City city = cityService.findCityByUserIdAndIdentityType(paymentDataDO.getUserId(), paymentDataDO.getAppIdentityType());
        receiptInfo.setReceiptNumber(OrderUtils.generateReceiptNumber(city.getCityId()));
        receiptInfo.setReplyCode(tradeStatus);
        return receiptInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRechargeReceiptInfo(RechargeReceiptInfo receiptInfo) {
        if (null != receiptInfo) {
            rechargeDAO.saveRechargeReceiptInfo(receiptInfo);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRechargeOrderStatusAndPayUpTime(String rechargeNo, Date payUpTime, AppRechargeOrderStatus status) {
        if (null != rechargeNo) {
            rechargeDAO.updateRechargeOrderStatusAndPayUpTime(rechargeNo, payUpTime, status);
        }
    }

    @Override
    public RechargeReceiptInfo findRechargeReceiptInfoByRechargeNo(String rechargeNo) {
        if (null != rechargeNo) {
            return rechargeDAO.findRechargeReceiptInfoByRechargeNo(rechargeNo);
        }
        return null;
    }

    @Override
    public RechargeOrder findRechargeOrderByRechargeNo(String rechargeNo) {
        if (null != rechargeNo) {
            return rechargeDAO.findRechargeOrderByRechargeNo(rechargeNo);
        }
        return null;
    }
}
