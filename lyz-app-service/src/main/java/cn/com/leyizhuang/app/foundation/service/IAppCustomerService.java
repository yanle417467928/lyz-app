package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.foundation.pojo.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.response.CashCouponResponse;

import java.util.List;

/**
 * lyz-app-facade用户服务接口
 *
 * @author Richard
 * Created on 2017-09-19 11:18
 **/
public interface IAppCustomerService {

    AppCustomer save(AppCustomer appUser);

    AppCustomer findByOpenId(String openId);

    AppCustomer findByMobile(String phone);

    void update(AppCustomer phoneUser);

    AppCustomer findById(Long userId);

    List<CashCouponResponse> findCashCouponByCustomerId(Long userId);
}
