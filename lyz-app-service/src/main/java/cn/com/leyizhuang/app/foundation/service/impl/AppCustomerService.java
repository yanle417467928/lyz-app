package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.AppCustomerDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppCustomer;
import cn.com.leyizhuang.app.foundation.service.IAppCustomerService;
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
public class AppCustomerService implements IAppCustomerService {

    @Autowired
    private AppCustomerDAO customerDAO;

    @Override
    public AppCustomer save(AppCustomer appCustomer) {
        if(null != appCustomer){
            return customerDAO.save(appCustomer);
        }
        return null;
    }
    @Override
    public AppCustomer findByOpenId(String openId) {
        if(null != openId && !"".equalsIgnoreCase(openId)){
            return customerDAO.findByOpenId(openId);
        }
        return null;
    }

    @Override
    public AppCustomer findByMobile(String phone) {
        if(null != phone){
            return customerDAO.findByMobile(phone);
        }
        return null;
    }

    @Override
    @Transactional
    public void update(AppCustomer phoneUser) {
        if (null != phoneUser){
            customerDAO.update(phoneUser);
        }
    }

    @Override
    public AppCustomer findById(Long userId) {
        if (null != userId){
            return customerDAO.findById(userId);
        }
        return null;
    }
}
