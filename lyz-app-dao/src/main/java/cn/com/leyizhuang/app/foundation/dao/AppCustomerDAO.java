package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.pojo.user.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * lyz-app-facade用户数据仓库
 *
 * @author Richard
 * Created on 2017-09-19 11:26
 **/
@Repository
public interface AppCustomerDAO {
    AppCustomer findByLoginName(String loginName);

    AppCustomer findByOpenId(@Param(value = "openId") String openId);

    AppCustomer findByMobile(String mobile);

    void update(AppCustomer newUser);

    Long save(AppCustomer newUser);

    AppCustomer findById(@Param(value = "cusId") Long cusId);


    List<CashCouponResponse> findCashCouponByCustomerId(Long userId);

    List<ProductCouponResponse> findProductCouponByCustomerId(
            @Param(value = "userId") Long userId);

    List<AppCustomer> findListBySalesConsultId(Long userId);

    List<AppCustomer> searchBySalesConsultIdAndKeywords(
            @Param("userId") Long userId, @Param("keywords") String keywords);

    CustomerHomePageResponse findCustomerInfoByUserId(Long userId);

    Double findPreDepositBalanceByUserId(Long userId);

    Integer findLeBiQuantityByUserId(Long userId);

    void updateLeBiQuantityByUserId(@Param(value = "userId") Long userId,
                                    @Param(value = "qty") int qty);

    Boolean existsByCustomerId(Long userId);

    void updateCustomerMobileByUserId(@Param("userId") Long userId, @Param("mobile") String mobile);

    int updateLeBiQuantityByUserIdAndQty(@Param("userId") Long userId, @Param("qty") Integer quantity, @Param(value = "version") Date version);

    void saveLeBi(CustomerLeBi leBi);

    void savePreDeposit(CustomerPreDeposit preDeposit);

    void updateCustomerSignInfoByCustomerId(@Param(value = "cusId") Long cusId, @Param(value = "date") Date date,
                                            @Param(value = "consecutiveSignDays") int consecutiveSignDays);

    int updateDepositByUserIdAndDeposit(@Param("userId") Long userId, @Param("deposit") Double customerDeposit, @Param(value = "version") Date version);

    int updateProductCouponByUserIdAndProductCoupons(
            @Param("userId") Long userId, @Param("gid") Long goodsId, @Param("qty") Integer qty);

    int updateCashCouponByUserIdAndCashCoupons(
            @Param("userId") Long userId, @Param("gid") Long goodsId, @Param("qty") Integer qty);

    int updateDepositByUserId(@Param("userId") Long userId, @Param("deposit") Double customerDeposit);

    int updateDepositByUserIdAndLastUpdateTime(@Param("userId") Long userId, @Param("deposit") Double customerDeposit, @Param("lastUpdateTime") Timestamp lastUpdateTime, @Param("oldLastUpdateTime") Timestamp oldLastUpdateTime);

    void updateLeBiByUserIdAndQuantity(@Param("userId") Long userId, @Param("qty") Integer qty);

    void updateProductCouponByUserIdAndGoodsIdAndProductCoupons(
            @Param("userId") Long userId, @Param("gid") Long index, @Param("qty") Integer integer);

    void updateCashCouponByUserIdAndGoodsIdAndCashCoupons(
            @Param("userId") Long userId, @Param("ccid") Long index, @Param("qty") Integer integer);

    AppCustomer findStoreSellerByCustomerId(@Param("userId") Long userId);

    CashCouponResponse findCashCouponByCcIdAndUserIdAndQty(
            @Param("ccId") Long id, @Param("cusId") Long userId, @Param("qty") Integer qty);

    CustomerPreDeposit findByCusId(Long cusId);

    List<CashCouponResponse> findCashCouponUseableByCustomerId(@Param("userId") Long customerId,
                                                               @Param("totalAmount") Double totalOrderAmount);

    List<ProductCouponResponse> findProductCouponBySellerIdAndCustomerId(@Param(value = "sellerId") Long sellerId,
                                                                         @Param(value = "cusId") Long cusId);

    List<ProductCouponCustomer> findProductCouponCustomerBySellerId(@Param(value = "userId") Long userId,
                                                                    @Param(value = "keywords") String keywords);

    CustomerCashCoupon findCashCouponByCcid(@Param(value = "id") Long id);

    Integer updateCustomerCashCouponById(@Param(value = "id") Long id,
                                         @Param(value = "orderNumber") String orderNumber);

    CustomerLeBi findCustomerLebiByCustomerId(Long customerId);

    void addCustomerCashCouponChangeLog(CustomerCashCouponChangeLog log);

    void addCustomerLeBiVariationLog(CustomerLeBiVariationLog log);

    void addCusPreDepositLog(CusPreDepositLogDO cusPreDepositLogDO);

    Integer findCashCouponAvailQtyByCustomerId(Long userId);

    Integer findProductCouponAvailQtyByCustomerId(Long userId);

    Integer countSignDaysByCusId(@Param(value = "cusId") Long cusId,
                                 @Param(value = "startDate") Date startDate,
                                 @Param(value = "endDate") Date endDate);

    Integer countTotalSignDaysByCusId(Long cusId);

    Integer countSignAwardLebiQtyByCusId(Long cusId);

    List<CustomerSignLogBrief> findCustomerSignDetailByCusId(Long cusId);

    void saveSignLog(CusSignLog log);

    List<CustomerCashCoupon> findCashCouponsByCcids(@Param(value = "cashCouponList") List<Long> cashCouponList);

    List<CustomerProductCoupon> findProductCouponsByCustomerIdAndGoodsIdAndQty(@Param(value = "customerId") Long customerId,
                                                                               @Param(value = "id") Long id,
                                                                               @Param(value = "qty") Integer qty);

    Integer updateCustomerProductCouponById(@Param(value = "couponId") Long couponId,
                                            @Param(value = "orderNumber") String orderNumber);

    List<CustomerProfession> getCustomerProfessionListByStatus(String status);
}
