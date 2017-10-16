package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.response.CashCouponResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.ProductCouponResponse;
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

    List<AppCustomer> searchBySalesConsultIdAndKeywords(@Param("userId") Long userId,
                                                                 @Param("keywords") String keywords);

    void modifyMobileById(@Param("id") Long userId,@Param("mobile") String mobile);
}
