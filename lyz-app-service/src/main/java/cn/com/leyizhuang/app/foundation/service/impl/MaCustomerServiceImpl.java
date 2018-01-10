package cn.com.leyizhuang.app.foundation.service.impl;


import cn.com.leyizhuang.app.foundation.dao.MaCustomerDAO;
import cn.com.leyizhuang.app.foundation.dto.CusPreDepositLogDTO;
import cn.com.leyizhuang.app.foundation.pojo.management.customer.CustomerDO;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.service.MaCustomerService;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerPreDepositVO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerVO;
import cn.com.leyizhuang.common.core.exception.AppConcurrentExcp;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class MaCustomerServiceImpl implements MaCustomerService {

    @Resource
    private MaCustomerDAO maCustomerDAO;

    @Override
    public PageInfo<CustomerDO> queryPageVO(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<CustomerDO> CustmoerList = maCustomerDAO.findAllVO();
        return new PageInfo<>(CustmoerList);
    }

    @Override
    public CustomerDO queryCustomerVOById(Long cusId) {
        if (cusId != null) {
            CustomerDO customerDO = maCustomerDAO.queryCustomerVOById(cusId);
            return customerDO;
        }
        return null;
    }

    @Override
    public PageInfo<CustomerDO> queryCustomerVOByCityId(Integer page, Integer size, Long cityId) {
        PageHelper.startPage(page, size);
        List<CustomerDO> CustmoerList = maCustomerDAO.queryCustomerVOByCityId(cityId);
        return new PageInfo<>(CustmoerList);
    }


    @Override
    public PageInfo<CustomerDO> queryCustomerVOByStoreId(Integer page, Integer size, Long storeId) {
        PageHelper.startPage(page, size);
        List<CustomerDO> CustmoerList = maCustomerDAO.queryCustomerVOByStoreId(storeId);
        return new PageInfo<>(CustmoerList);
    }

    @Override
    public PageInfo<CustomerDO> queryCustomerVOByGuideId(Integer page, Integer size, Long guideId) {
        PageHelper.startPage(page, size);
        List<CustomerDO> CustmoerList = maCustomerDAO.queryCustomerVOByGuideId(guideId);
        return new PageInfo<>(CustmoerList);
    }

    @Override
    public PageInfo<CustomerDO> queryCustomerVOByPhone(Integer page, Integer size, Long queryCusInfo) {
        PageHelper.startPage(page, size);
        List<CustomerDO> CustmoerList = maCustomerDAO.queryCustomerVOByPhone(queryCusInfo);
        return new PageInfo<>(CustmoerList);
    }

    @Override
    public PageInfo<CustomerDO> queryCustomerVOByName(Integer page, Integer size, String queryCusInfo) {
        PageHelper.startPage(page, size);
        List<CustomerDO> CustmoerList = maCustomerDAO.queryCustomerVOByName(queryCusInfo);
        return new PageInfo<>(CustmoerList);
    }

    @Override
    public void saveCustomer(CustomerDetailVO customer){
        if(null!=customer){
            customer.setCreateTime(new Date());
            customer.setLight("GREEN");
            customer.setCreateType("Background add");
            if(null!=customer.getSalesConsultId()){
                customer.setBindingTime(new Date());
            }
            CustomerDO customerDO =CustomerDO.transform(customer);
            maCustomerDAO.save(customerDO);
        }
    }

    @Override
    public Boolean isExistPhoneNumber(Long moblie) {
        return maCustomerDAO.isExistPhoneNumber(moblie);
    }

    @Override
    public PageInfo<CustomerPreDepositVO> findAllCusPredeposit(Integer page, Integer size, Long cityId, Long storeId, String keywords) {
        PageHelper.startPage(page, size);
        List<CustomerPreDepositVO> list = this.maCustomerDAO.findAllCusPredeposit(cityId, storeId, keywords);
        return new PageInfo<>(list);
    }

    @Override
    public CustomerPreDepositVO queryCusPredepositByCusId(Long cusId) {
        return this.maCustomerDAO.queryCusPredepositByCusId(cusId);
    }

    @Override
    public void changeCusPredepositByCusId(CusPreDepositLogDTO cusPreDepositLogDTO) {
        Long userId = cusPreDepositLogDTO.getCusId();
        Double money = cusPreDepositLogDTO.getChangeMoney();
        CustomerPreDeposit customerPreDeposit = this.maCustomerDAO.findByCusId(userId);
        if (null == customerPreDeposit) {
            customerPreDeposit = new CustomerPreDeposit();
            customerPreDeposit.setBalance(money);
            customerPreDeposit.setCusId(userId);
            customerPreDeposit.setCreateTime(new Date());
            customerPreDeposit.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
            this.maCustomerDAO.savePreDeposit(customerPreDeposit);
        } else {
            int row = this.maCustomerDAO.updateDepositByUserId(userId, money, new Timestamp(System.currentTimeMillis()), customerPreDeposit.getLastUpdateTime());
            if (1 != row) {
                throw new AppConcurrentExcp("账号余额信息过期！");
            }
        }
    }
}

