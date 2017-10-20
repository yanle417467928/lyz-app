package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.response.CustomerHomePageResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.EmployeeHomePageResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * lyz-app-facade 员工数据仓库
 *
 * @author Richard
 * Created on 2017-09-19 11:26
 **/
@Repository
public interface AppEmployeeDAO {

    void save(AppEmployee appEmployee);

    AppEmployee findByLoginName(String loginName);

    AppEmployee findByMobile(String mobile);

    void update(AppEmployee newEmployee);

    AppEmployee findByUserId(Long userId);

    AppEmployee findById(Long id);

    List<AppEmployee> findDecorateEmployeeListByManagerId(Long userId);

    Double findCreditMoneyBalanceByUserId(Long userId);

    EmployeeHomePageResponse findEmployeeInfoByUserIdAndIdentityType(
            @Param("userId") Long userId,@Param("type") Integer identityType);

    void updateEmployeeMobileByUserId(@Param("userId") Long userId,@Param("mobile") String mobile);
}
