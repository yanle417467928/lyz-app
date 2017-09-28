package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.CashCoupon;
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

    AppCustomer save(AppCustomer newUser);

    AppCustomer findById(Long userId);


    List<CashCoupon> findCashCouponByCustomerId(Long userId);
}
