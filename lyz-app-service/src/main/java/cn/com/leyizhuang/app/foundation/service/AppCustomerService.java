package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.foundation.pojo.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.request.UserSetInformationReq;
import cn.com.leyizhuang.app.foundation.pojo.response.CashCouponResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.ProductCouponResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.CustomerListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.CustomerHomePageResponse;

import java.util.List;

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

    void addLeBiQuantityByUserIdAndIdentityType(Long userId, Integer identityType);

    void modifyCustomerMobileByUserId(Long userId, String mobile);

    void modifyLeBiQuantityByUserIdAndQty(Long userId, Integer quantity);

    void saveLeBi(CustomerLeBi leBi);

    void savePreDeposit(CustomerPreDeposit preDeposit);
}
