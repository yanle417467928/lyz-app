package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.SexType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.AppCustomerDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.request.UserSetInformationReq;
import cn.com.leyizhuang.app.foundation.pojo.response.CashCouponResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.CustomerListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.ProductCouponResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.UserHomePageResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * lyz-app-facade用户服务实现类
 *
 * @author Richard
 * Created on 2017-09-19 11:23
 **/
@Service
public class AppCustomerServiceImpl implements cn.com.leyizhuang.app.foundation.service.AppCustomerService {

    @Resource
    private AppCustomerDAO customerDAO;

    @Override
    @Transactional
    public AppCustomer save(AppCustomer appCustomer) {
        if(null != appCustomer){
            customerDAO.save(appCustomer);
            return appCustomer;
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
    public AppCustomer findById(Long cusId) {
        if (null != cusId){
            return customerDAO.findById(cusId);
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
    public void modifyCustomerInformation(UserSetInformationReq userInformation) {
        if (null != userInformation){
            customerDAO.update(transform(userInformation));
        }
    }

    @Override
    public UserHomePageResponse findCustomerInfoByUserId(Long userId) {
        if (null != userId){
            return customerDAO.findCustomerInfoByUserId(userId);
        }
        return null;
    }

    public AppCustomer transform(UserSetInformationReq userInformation){
        AppCustomer appCustomer = new AppCustomer();
        appCustomer.setPicUrl(userInformation.getPicUrl());
        appCustomer.setBirthday(userInformation.getBirthday());
        appCustomer.setMobile(userInformation.getMobile());
        appCustomer.setSex(SexType.getSexTypeByValue(userInformation.getSex()));
        appCustomer.setName(userInformation.getName());
        return appCustomer;
    }
}
