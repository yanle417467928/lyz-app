package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppUserType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.AppEmployeeDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.response.EmployeeListResponse;
import cn.com.leyizhuang.app.foundation.service.IAppEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * lyz-app-facade用户服务实现类
 *
 * @author Richard
 * Created on 2017-09-19 11:23
 **/
@Service
public class AppEmployeeService implements IAppEmployeeService {

    @Autowired
    private AppEmployeeDAO employeeDAO;

    @Override
    @Transactional
    public void save(AppEmployee appEmployee) {
        if(null != appEmployee){
            employeeDAO.save(appEmployee);
        }
    }

    @Override
    public AppEmployee findByLoginName(String loginName) {
        if(null != loginName && !"".equalsIgnoreCase(loginName)){
            return employeeDAO.findByLoginName(loginName);
        }
        return null;
    }

    @Override
    public AppEmployee findByMobile(String mobile) {
        if(null != mobile && !"".equalsIgnoreCase(mobile)){
            return employeeDAO.findByMobile(mobile);
        }
        return null;
    }

    @Override
    @Transactional
    public void update(AppEmployee newEmployee) {
        if(null != newEmployee && null != newEmployee.getEmpId()){
            employeeDAO.update(newEmployee);
        }
    }

    @Override
    public AppEmployee findByUserId(Long userId) {
        if (null != userId){
            return employeeDAO.findByUserId(userId);
        }
        return null;
    }

    @Override
    public AppEmployee findById(Long userId) {
        if (null != userId){
            return employeeDAO.findById(userId);
        }
        return null;
    }

    @Override
    public List<EmployeeListResponse> findDecorateEmployeeListByUserIdAndIdentityType(Long userId, Integer identityType) {
        if (null != userId && null != identityType && identityType == 2){
            List<AppEmployee> appEmployeeList = employeeDAO.findDecorateEmployeeListByParentId(userId);
            return EmployeeListResponse.transform(appEmployeeList);
        }
        return null;
    }

    @Override
    @Transactional
    public void modifyMobileByEmployeeId(Long userId, String mobile) {
        if (null != userId && StringUtils.isNotBlank(mobile)){
            employeeDAO.modifyMobileById(userId,mobile);
        }
    }
}
