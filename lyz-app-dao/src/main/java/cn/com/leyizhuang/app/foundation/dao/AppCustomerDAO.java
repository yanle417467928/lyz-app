package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.response.CashCouponResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.ProductCouponResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.CustomerHomePageResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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

    AppCustomer  findByOpenId(@Param(value = "openId") String openId);

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

    void updateCustomerMobileByUserId(@Param("userId") Long userId,@Param("mobile") String mobile);

    void updateLeBiQuantityByUserIdAndQty(@Param("userId") Long userId,@Param("qty") Integer quantity);

    void saveLeBi(CustomerLeBi leBi);

    void savePreDeposit(CustomerPreDeposit preDeposit);
}
