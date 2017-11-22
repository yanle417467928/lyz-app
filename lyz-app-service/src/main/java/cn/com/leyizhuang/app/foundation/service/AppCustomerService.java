package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.foundation.pojo.PaymentDataDO;
import cn.com.leyizhuang.app.foundation.pojo.request.UserSetInformationReq;
import cn.com.leyizhuang.app.foundation.pojo.response.CashCouponResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.CustomerHomePageResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.CustomerListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.ProductCouponResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.common.core.constant.PreDepositChangeType;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * lyz-app-facade用户服务接口
 *
 * @author Richard
 * Created on 2017-09-19 11:18
 **/
public interface AppCustomerService {

    AppCustomer save(AppCustomer appUser);

    AppCustomer findByOpenId(String openId);

    AppCustomer findByMobile(String phone);

    void update(AppCustomer phoneUser);

    AppCustomer findById(Long cusId);

    List<CashCouponResponse> findCashCouponByCustomerId(Long userId);

    List<ProductCouponResponse> findProductCouponByCustomerId(Long userId);

    List<CustomerListResponse> findListByUserIdAndIdentityType(Long userId, Integer identityType);

    List<CustomerListResponse> searchByUserIdAndKeywordsAndIdentityType(Long userId, String keywords, Integer identityType);

    CustomerHomePageResponse findCustomerInfoByUserId(Long userId);

    void modifyCustomerInformation(UserSetInformationReq userInformation);

    Boolean existsByCustomerId(Long userId);

    Double findPreDepositBalanceByUserIdAndIdentityType(Long userId, Integer identityType);

    Integer findLeBiQuantityByUserIdAndIdentityType(Long userId, Integer identityType);

    CustomerLeBi findLeBiByUserIdAndGoodsMoney(Long userId,Double goodsMoney);

    void addLeBiQuantityByUserIdAndIdentityType(Long userId, Integer identityType);

    void modifyCustomerMobileByUserId(Long userId, String mobile);

    void saveLeBi(CustomerLeBi leBi);

    void savePreDeposit(CustomerPreDeposit preDeposit);

    void updateLastSignTimeByCustomerId(Long cusId, Date date);

    int lockCustomerDepositByUserIdAndDeposit(Long userId, Double customerDeposit);

    int lockCustomerLebiByUserIdAndQty(Long userId, Integer lebiQty);

    int lockCustomerProductCouponByUserIdAndProductCoupons(Long userId, Map<Long,Integer> productCoupon);

    int lockCustomerCashCouponByUserIdAndCashCoupons(Long userId, Map<Long,Integer> cashCoupon);

    void unlockCustomerDepositByUserIdAndDeposit(Long userId, Double customerDeposit);

    void unlockCustomerLebiByUserIdAndQty(Long userId, Integer lebiQty);

    void unlockCustomerProductCouponByUserIdAndProductCoupons(Long userId, Map<Long,Integer> productCoupon);

    void unlockCustomerCashCouponByUserIdAndCashCoupons(Long userId, Map<Long,Integer> cashCoupon);

    AppCustomer findStoreSellerByCustomerId(Long userId);

    CashCouponResponse findCashCouponByCcIdAndUserIdAndQty(Long id, Long userId, Integer integer);

    CustomerPreDeposit findByCusId(Long cusId);

    void preDepositRecharge(PaymentDataDO paymentDataDO, PreDepositChangeType type);

}
