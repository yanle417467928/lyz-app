package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.foundation.pojo.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.vo.UserVO;

/**
 * 通用方法
 *
 * @author Richard
 * Created on 2017-09-12 15:42
 **/
public interface CommonService {

    void saveUserAndUserRoleByUserVO(UserVO userVO);

    void updateUserAndUserRoleByUserVO(UserVO userVO);

    void deleteUserAndUserRoleByUserId(Long uid);

    AppCustomer saveCustomerInfo(AppCustomer customer, CustomerLeBi leBi, CustomerPreDeposit preDeposit);

    void updateCustomerSignTimeAndCustomerLeBiByUserId(Long userId,Integer identityType);

}
