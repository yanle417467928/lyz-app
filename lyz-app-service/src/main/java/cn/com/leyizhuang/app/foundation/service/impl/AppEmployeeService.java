package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.AppEmployeeDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.IAppEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if(null != newEmployee && null != newEmployee.getId()){
            employeeDAO.update(newEmployee);
        }
    }
}
