package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.foundation.pojo.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.response.EmployeeListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.UserHomePageResponse;

import java.util.List;

/**
 * lyz-app-facade用户服务接口
 *
 * @author Richard
 * Created on 2017-09-19 11:18
 **/
public interface IAppEmployeeService {

    void save(AppEmployee appEmployee);

    AppEmployee findByLoginName(String name);

    AppEmployee findByMobile(String mobile);

    void update(AppEmployee newEmployee);

    AppEmployee findByUserId(Long userId);

    AppEmployee findById(Long userId);

    List<EmployeeListResponse> findDecorateEmployeeListByUserIdAndIdentityType(Long userId, Integer identityType);

    void modifyMobileByEmployeeId(Long userId, String mobile);

    Double findCreditMoneyBalanceByUserIdAndIdentityType(Long userId, Integer identityType);

    UserHomePageResponse findEmployeeInfoByUserId(Long userId);
}
