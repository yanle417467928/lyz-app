package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.AppCustomerDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.response.CashCouponResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.ProductCouponResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.CustomerListResponse;
import cn.com.leyizhuang.app.foundation.service.IAppCustomerService;
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

    @Override
    public List<CashCouponResponse> findCashCouponByCustomerId(Long userId) {
        if(null != userId){
            return customerDAO.findCashCouponByCustomerId(userId);
        }
        return null;
    }

    @Override
    public List<ProductCouponResponse> findProductCouponByCustomerId(Long userId) {
        if(null != userId){
            return customerDAO.findProductCouponByCustomerId(userId);
        }
        return null;
    }

    @Override
    public List<CustomerListResponse> findListByUserIdAndIdentityType(Long userId, Integer identityType) {
        if (null != userId && null != identityType && identityType == 0){
           List<AppCustomer> appCustomerList = customerDAO.findListBySalesConsultId(userId);
           return CustomerListResponse.transform(appCustomerList);
        }
        return null;
    }

    @Override
    public List<CustomerListResponse> searchByUserIdAndKeywordsAndIdentityType(Long userId, String keywords, Integer identityType) {
        if (StringUtils.isNotBlank(keywords) && null != userId && null != identityType && identityType == 0) {
            List<AppCustomer> appCustomerList = customerDAO.searchBySalesConsultIdAndKeywords(userId,keywords);
            return CustomerListResponse.transform(appCustomerList);
        }
        return null;
    }

    @Override
    @Transactional
    public void modifyMobileByCustomerId(Long userId, String mobile) {
        if (null != userId && StringUtils.isNotBlank(mobile)){
            customerDAO.modifyMobileById(userId,mobile);
        }
    }

}
