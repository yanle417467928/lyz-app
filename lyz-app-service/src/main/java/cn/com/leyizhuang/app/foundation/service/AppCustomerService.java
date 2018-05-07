package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.core.constant.CustomerPreDepositChangeType;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.request.UserSetInformationReq;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.pojo.user.*;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
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

    AppCustomer findByOpenId(String openId) throws UnsupportedEncodingException;

    AppCustomer findByMobile(String phone) throws UnsupportedEncodingException;

    void update(AppCustomer phoneUser) throws UnsupportedEncodingException;

    AppCustomer findById(Long cusId) throws UnsupportedEncodingException;

    List<CashCouponResponse> findCashCouponByCustomerId(Long userId);

    List<ProductCouponResponse> findProductCouponByCustomerId(Long userId);

    PageInfo<AppCustomer> findListByUserIdAndIdentityType(Long userId, Integer identityType, Integer page, Integer size);

    List<CustomerListResponse> searchByUserIdAndKeywordsAndIdentityType(Long userId, String keywords, Integer identityType);

    CustomerHomePageResponse findCustomerInfoByUserId(Long userId);

    void modifyCustomerInformation(UserSetInformationReq userInformation);

    Boolean existsByCustomerId(Long userId);

    Double findPreDepositBalanceByUserIdAndIdentityType(Long userId, Integer identityType);

    Integer findLeBiQuantityByUserIdAndIdentityType(Long userId, Integer identityType);

    Map<String, Object> findLeBiByUserIdAndGoodsMoney(Long userId, Double goodsMoney);

    void addLeBiQuantityByUserIdAndIdentityType(Long userId, Integer identityType, int qty);

    void modifyCustomerMobileByUserId(Long userId, String mobile);

    void saveLeBi(CustomerLeBi leBi);

    void savePreDeposit(CustomerPreDeposit preDeposit);

    void updateCustomerSignInfoByCustomerId(Long cusId, Date date, int consecutiveSignDays);

    int lockCustomerDepositByUserIdAndDeposit(Long userId, Double customerDeposit, Timestamp version);

    int lockCustomerLebiByUserIdAndQty(Long userId, Integer lebiQty, Timestamp version);

    int lockCustomerProductCouponByUserIdAndProductCoupons(Long userId, Map<Long, Integer> productCoupon);

    int lockCustomerCashCouponByUserIdAndCashCoupons(Long userId, Map<Long, Integer> cashCoupon);

    void unlockCustomerDepositByUserIdAndDeposit(Long userId, Double customerDeposit);

    void unlockCustomerLebiByUserIdAndQty(Long userId, Integer lebiQty);

    void unlockCustomerProductCouponByUserIdAndProductCoupons(Long userId, Map<Long, Integer> productCoupon);

    void unlockCustomerCashCouponByUserIdAndCashCoupons(Long userId, Map<Long, Integer> cashCoupon);

    AppCustomer findStoreSellerByCustomerId(Long userId);

    CashCouponResponse findCashCouponByCcIdAndUserIdAndQty(Long id, Long userId, Integer integer);

    CustomerPreDeposit findByCusId(Long cusId);

    void preDepositRecharge(PaymentDataDO paymentDataDO, CustomerPreDepositChangeType type);

    List<CashCouponResponse> findCashCouponUseableByCustomerId(Long customerId, Double totalOrderAmount);

    List<ProductCouponResponse> findProductCouponBySellerIdAndCustomerId(Long sellerId, Long cusId);

    List<ProductCouponCustomer> findProductCouponCustomerBySellerId(Long userId, String keywords);

    CustomerCashCoupon findCashCouponByCcid(Long id);

    Integer lockCustomerCashCouponById(Long id, String orderNumber);

    CustomerLeBi findCustomerLebiByCustomerId(Long customerId);

    void addCustomerCashCouponChangeLog(CustomerCashCouponChangeLog log);

    void addCustomerLeBiVariationLog(CustomerLeBiVariationLog log);

    void addCusPreDepositLog(CusPreDepositLogDO cusPreDepositLogDO);

    Integer findCashCouponAvailQtyByCustomerId(Long userId);

    Integer findProductCouponAvailQtyByCustomerId(Long userId);

    Integer countSignDaysByCusId(Long cusId, Date startTimeOfThisMonth, Date date);

    Integer countTotalSignDaysByCusId(Long cusId);

    Integer countSignAwardLebiQtyByCusId(Long cusId);

    PageInfo<CustomerSignLogBrief> findCustomerSignDetailByCusIdWithPageable(Long cusId, Integer page, Integer size);

    void saveSignLog(CusSignLog log);

    List<CustomerCashCoupon> findCashCouponsByCcids(List<Long> cashCouponList);

    List<CustomerProductCoupon> findProductCouponsByCustomerIdAndGoodsIdAndQty(Long customerIdTemp, Long salesConsultId, Long id, Integer qty);

    Integer lockCustomerProductCouponById(Long couponId, String orderNumber);

    List<CustomerProfession> getCustomerProfessionListByStatus(String status);

    Integer updateDepositByUserIdAndVersion(Long userId, Double customerDeposit, Date version);

    List<CustomerCashCoupon> findCashCouponsByids(List<Long> cashCouponList);

    CustomerProfession findCustomerProfessionByTitle(String customerProfession);

    void unbindingCustomerWeChat(Long userId);

    List<SupportHotlineResponse> getCustomerSupportHotline(Long userId);

    List<CustomerProductCoupon> findProductCouponsByGetOrderNumber(String orderNumber);

    CustomerRankInfoResponse findCusRankinfoByCusId(Long cusId);
    /**
     * 导购代下单根据电话号码查询顾客
     * @param cityId    城市id
     * @param mobile    电话号码
     * @return  顾客list
     */
    List<FindCustomerResponse> findCustomerByCusPhone(Long cityId, String mobile);
    /**
     * 导购代下单根据姓名查询顾客
     * @param storeId    门店id
     * @param keywords    姓名
     * @return  顾客list
     */
    List<FindCustomerResponse> findCustomerByCusNameOrPhone(Long storeId,String keywords);
    /**
     * 修改顾客类型
     * @param userId 用户id
     */
    void updateCustomerTypeByUserId(Long userId);

    CustomerPreDeposit findCustomerPreDepositByCustomerId(Long cusId);

    List<AppCustomer> findAllCustomer();

    void createCustomerPreDepositAccount(Long cusId);

    void createCustomerLeBiAccount(Long cusId);

    void updateCustomerSellerIdStoreIdByCusId(Long cusId,Long storeId,Long salesConsultId);
}
