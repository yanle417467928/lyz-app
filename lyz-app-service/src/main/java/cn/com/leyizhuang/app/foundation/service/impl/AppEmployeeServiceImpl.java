package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.SexType;
import cn.com.leyizhuang.app.foundation.dao.AppEmployeeDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.request.UserSetInformationReq;
import cn.com.leyizhuang.app.foundation.pojo.response.EmployeeListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.UserHomePageResponse;
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
public class AppEmployeeServiceImpl implements cn.com.leyizhuang.app.foundation.service.AppEmployeeService {

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
            List<AppEmployee> appEmployeeList = employeeDAO.findDecorateEmployeeListByManagerId(userId);
            return EmployeeListResponse.transform(appEmployeeList);
        }
        return null;
    }

    @Override
    public void modifyEmployeeInformation(UserSetInformationReq userInformation) {
        if (null != userInformation){
            employeeDAO.update(transform(userInformation));
        }
    }

    @Override
    public Double findCreditMoneyBalanceByUserIdAndIdentityType(Long userId, Integer identityType) {
        if (null != userId && null != identityType && identityType == 0){
            return employeeDAO.findCreditMoneyBalanceByUserId(userId);
        }
        return null;
    }

    @Override
    public UserHomePageResponse findEmployeeInfoByUserId(Long userId) {
        if (null != userId){
            return employeeDAO.findEmployeeInfoByUserId(userId);
        }
        return null;
    }

    public AppEmployee transform(UserSetInformationReq userInformation){
        AppEmployee appEmployee = new AppEmployee();
        appEmployee.setEmpId(userInformation.getUserId());
        appEmployee.setBirthday(userInformation.getBirthday());
        appEmployee.setMobile(userInformation.getMobile());
        appEmployee.setPicUrl(userInformation.getPicUrl());
        appEmployee.setName(userInformation.getName());
        appEmployee.setSex(SexType.getSexTypeByValue(userInformation.getSex()));
        return appEmployee;
    }
}
