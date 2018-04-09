package cn.com.leyizhuang.app.foundation.service.impl;


import cn.com.leyizhuang.app.core.constant.AppCustomerCreateType;
import cn.com.leyizhuang.app.core.constant.AppCustomerLightStatus;
import cn.com.leyizhuang.app.foundation.dao.MaCustomerDAO;
import cn.com.leyizhuang.app.foundation.dto.CusLebiDTO;
import cn.com.leyizhuang.app.foundation.dto.CusPreDepositDTO;
import cn.com.leyizhuang.app.foundation.pojo.management.customer.CustomerDO;
import cn.com.leyizhuang.app.foundation.pojo.management.customer.MaCustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.service.MaCusLebiLogService;
import cn.com.leyizhuang.app.foundation.service.MaCusPreDepositLogService;
import cn.com.leyizhuang.app.foundation.service.MaCustomerService;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerLebiVO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerPreDepositVO;
import cn.com.leyizhuang.common.core.exception.AppConcurrentExcp;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class MaCustomerServiceImpl implements MaCustomerService {

    @Resource
    private MaCustomerDAO maCustomerDAO;

    @Autowired
    private MaCusPreDepositLogService maCusPreDepositLogService;

    @Autowired
    private MaCusLebiLogService maCusLebiLogService;

    @Override
    public PageInfo<CustomerDO> queryPageVO(Integer page, Integer size, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<CustomerDO> CustmoerList = maCustomerDAO.findAllVO(storeIds);
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
    public PageInfo<CustomerDO> queryCustomerVOByCityId(Integer page, Integer size, Long cityId,List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<CustomerDO> CustmoerList = maCustomerDAO.queryCustomerVOByCityId(cityId,storeIds);
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
    public PageInfo<CustomerDO> queryCustomerVOByPhone(Integer page, Integer size, Long queryCusInfo,List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<CustomerDO> CustmoerList = maCustomerDAO.queryCustomerVOByPhone(queryCusInfo,storeIds);
        return new PageInfo<>(CustmoerList);
    }

    @Override
    public PageInfo<CustomerDO> queryCustomerVOByName(Integer page, Integer size, String queryCusInfo,List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<CustomerDO> CustmoerList = maCustomerDAO.queryCustomerVOByName(queryCusInfo,storeIds);
        return new PageInfo<>(CustmoerList);
    }

    @Override
    public void saveCustomer(CustomerDetailVO customer){
        if(null!=customer){
            Date date = new Date();
            customer.setCreateTime(date);
            customer.setLight(AppCustomerLightStatus.GREEN);
            customer.setCreateType(AppCustomerCreateType.ADMIN_CREATE);
            if(null!=customer.getSalesConsultId()){
                customer.setBindingTime(date);
            }
            CustomerDO customerDO =CustomerDO.transform(customer);
            maCustomerDAO.save(customerDO);
        }
    }


    @Override
    public void updateCustomer(CustomerDetailVO customer){
        if(null!=customer){
            CustomerDO customerDO = CustomerDO.transform(customer);
            maCustomerDAO.updateCustomer(customerDO);
        }
    }
    @Override
    public Boolean isExistPhoneNumber(Long moblie) {
        return maCustomerDAO.isExistPhoneNumber(moblie);
    }

    @Override
    public Boolean isExistPhoneNumberByCusId(Long mobile,Long cusId) {
        return maCustomerDAO.isExistPhoneNumberByCusId(mobile,cusId);
    }

    @Override
    public PageInfo<CustomerPreDepositVO> findAllCusPredeposit(Integer page, Integer size, Long cityId, Long storeId, String keywords, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<CustomerPreDepositVO> list = this.maCustomerDAO.findAllCusPredeposit(cityId, storeId, keywords, storeIds);
        return new PageInfo<>(list);
    }

    @Override
    public CustomerPreDepositVO queryCusPredepositByCusId(Long cusId) {
        return this.maCustomerDAO.queryCusPredepositByCusId(cusId);
    }

    @Override
    public void changeCusPredepositByCusId(CusPreDepositDTO cusPreDepositDTO) throws Exception {
        Long userId = cusPreDepositDTO.getCusId();
        Double money = cusPreDepositDTO.getChangeMoney();
        CustomerPreDeposit customerPreDeposit = this.maCustomerDAO.findPreDepositByCusId(userId);
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
        this.maCusPreDepositLogService.save(cusPreDepositDTO);
    }

    @Override
    public PageInfo<CustomerLebiVO> findAllCusLebi(Integer page, Integer size, Long cityId, Long storeId, String keywords, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<CustomerLebiVO> list = this.maCustomerDAO.findAllCusLebi(cityId, storeId, keywords, storeIds);
        return new PageInfo<>(list);
    }

    @Override
    public CustomerLebiVO queryCusLebiByCusId(Long cusId) {
        return this.maCustomerDAO.queryCusLebiByCusId(cusId);
    }

    @Override
    public void changeCusLebiByCusId(CusLebiDTO cusLebiDTO) throws Exception{
        Long userId = cusLebiDTO.getCusId();
        Integer quantity = cusLebiDTO.getChangeNum();
        CustomerLeBi customerLeBi = this.maCustomerDAO.findLebiByCusId(userId);
        if (null == customerLeBi) {
            customerLeBi = new CustomerLeBi();
            customerLeBi.setQuantity(quantity);
            customerLeBi.setCusId(userId);
            customerLeBi.setCreateTime(new Date());
            customerLeBi.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
            this.maCustomerDAO.saveLebi(customerLeBi);
        } else {
            int row = this.maCustomerDAO.updateLebiByUserId(userId, quantity, new Timestamp(System.currentTimeMillis()), customerLeBi.getLastUpdateTime());
            if (1 != row) {
                throw new AppConcurrentExcp("账号余额信息过期！");
            }
        }
        this.maCusLebiLogService.save(cusLebiDTO);
    }

    @Override
    public List<CustomerDO> findCustomerByCityIdAndStoreId(List<Long> storeIds) {
        return this.maCustomerDAO.findCustomerByCityIdAndStoreId(storeIds);
    }

    @Override
    public List<CustomerDO> findCustomerByCityIdAndStoreIdAndCustomerNameAndCustomerPhone(String customerQueryConditions, List<Long> storeIds) {
        List<CustomerDO> customerDOList = null;
        if (customerQueryConditions.matches("[0-9]{11}")){
            customerDOList = this.maCustomerDAO.findCustomerByCustomerPhone(customerQueryConditions);
        }else {
            customerDOList = this.maCustomerDAO.findCustomerByCityIdAndStoreIdAndCustomerNameAndCustomerPhone(customerQueryConditions,storeIds);
        }
        return customerDOList;
    }

    @Override
    public Integer updateDepositByUserIdAndVersion(Long userId, Double customerDeposit, Date version) {
        return maCustomerDAO.updateDepositByUserIdAndVersion(userId, customerDeposit, version);
    }


    @Override
    public void saveCusPreDepositLog( MaCustomerPreDeposit customerPreDeposit) {
        this.maCustomerDAO.saveCusPreDepositLog(customerPreDeposit);
    }

    @Override
    public Long findCityIdByCusId(Long cusId) {
        return this.maCustomerDAO.findCityIdByCusId(cusId);
    }
}

