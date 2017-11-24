package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.response.CashCouponResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.CustomerHomePageResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.ProductCouponCustomer;
import cn.com.leyizhuang.app.foundation.pojo.response.ProductCouponResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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

    void updateLeBiQuantityByUserId(Long userId);

    Boolean existsByCustomerId(Long userId);

    void updateCustomerMobileByUserId(@Param("userId") Long userId, @Param("mobile") String mobile);

    int updateLeBiQuantityByUserIdAndQty(@Param("userId") Long userId, @Param("qty") Integer quantity);

    void saveLeBi(CustomerLeBi leBi);

    void savePreDeposit(CustomerPreDeposit preDeposit);

    void updateLastSignTimeByCustomerId(@Param(value = "cusId") Long cusId, @Param(value = "date") Date date);

    int updateDepositByUserIdAndDeposit(@Param("userId") Long userId, @Param("deposit") Double customerDeposit);

    int updateProductCouponByUserIdAndProductCoupons(
            @Param("userId") Long userId, @Param("gid") Long goodsId, @Param("qty") Integer qty);

    int updateCashCouponByUserIdAndCashCoupons(
            @Param("userId") Long userId, @Param("gid") Long goodsId, @Param("qty") Integer qty);

    void updateDepositByUserId(@Param("userId") Long userId, @Param("deposit") Double customerDeposit);

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

    List<ProductCouponCustomer> findProductCouponCustomerBySellerId(Long userId);
}
