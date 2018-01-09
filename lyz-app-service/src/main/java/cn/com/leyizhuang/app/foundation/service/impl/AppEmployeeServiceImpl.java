package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.AppEmployeeDAO;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.request.UserSetInformationReq;
import cn.com.leyizhuang.app.foundation.pojo.response.EmployeeHomePageResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.EmployeeListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.SellerCreditMoneyResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.SellerResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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
        if (null != appEmployee) {
            employeeDAO.save(appEmployee);
        }
    }

    @Override
    public AppEmployee findByLoginName(String loginName) {
        if (null != loginName && !"".equalsIgnoreCase(loginName)) {
            return employeeDAO.findByLoginName(loginName);
        }
        return null;
    }

    @Override
    public AppEmployee findByMobile(String mobile) {
        if (null != mobile && !"".equalsIgnoreCase(mobile)) {
            return employeeDAO.findByMobile(mobile);
        }
        return null;
    }

    @Override
    @Transactional
    public void update(AppEmployee newEmployee) {
        if (null != newEmployee && null != newEmployee.getEmpId()) {
            employeeDAO.update(newEmployee);
        }
    }

    @Override
    public AppEmployee findByUserId(Long userId) {
        if (null != userId) {
            return employeeDAO.findByUserId(userId);
        }
        return null;
    }

    @Override
    public AppEmployee findById(Long userId) {
        if (null != userId) {
            return employeeDAO.findById(userId);
        }
        return null;
    }

    @Override
    public AppEmployee findByIdAndStatusIsTrue(Long userId) {
        if (null != userId) {
            return employeeDAO.findByIdAndStatusIsTrue(userId);
        }
        return null;
    }

    @Override
    public List<EmployeeListResponse> findDecorateEmployeeListByUserIdAndIdentityType(Long userId, Integer identityType) {
        if (null != userId && null != identityType && identityType == 2) {
            List<AppEmployee> appEmployeeList = employeeDAO.findDecorateEmployeeListByManagerId(userId);
            return EmployeeListResponse.transform(appEmployeeList);
        }
        return null;
    }

    @Override
    public List<EmployeeListResponse> searchBySalesConsultIdAndKeywords(Long userId, String keywords, Integer identityType) {
        if (StringUtils.isNotBlank(keywords) && null != userId && null != identityType && identityType == 2) {
            List<AppEmployee> appCustomerList = employeeDAO.searchBySalesConsultIdAndKeywords(userId, keywords);
            return EmployeeListResponse.transform(appCustomerList);
        }
        return null;
    }

    @Override
    @Transactional
    public void modifyEmployeeInformation(UserSetInformationReq userInformation) {
        if (null != userInformation) {
//            employeeDAO.update(transform(userInformation));
        }
    }

    @Override
    public SellerCreditMoneyResponse findCreditMoneyBalanceByUserIdAndIdentityType(Long userId, Integer identityType) {
        if (null != userId && null != identityType && identityType == 0) {
            return employeeDAO.findCreditMoneyBalanceByUserId(userId);
        }
        return null;
    }

    @Override
    public EmployeeHomePageResponse findEmployeeInfoByUserIdAndIdentityType(Long userId, Integer identityType) {
        if (null != userId && null != identityType) {
            return employeeDAO.findEmployeeInfoByUserIdAndIdentityType(userId, identityType);
        }
        return null;
    }

    @Override
    @Transactional
    public void modifyEmployeeMobileByUserId(Long userId, String mobile) {
        if (null != userId) {
            employeeDAO.updateEmployeeMobileByUserId(userId, mobile);
        }
    }

    @Override
    public Boolean existsSellerCreditByUserId(Long userId) {
        if (null != userId) {
            return employeeDAO.existsSellerCreditByUserId(userId);
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int lockGuideCreditByUserIdAndCredit(Long userId, Double guideCredit, Timestamp version) {
        if (null != userId && null != guideCredit) {
            return employeeDAO.lockGuideCreditByUserIdAndGuideCredit(userId, guideCredit,version);
        }
        return 0;
    }

    @Override
    public void updateByLoginName(AppEmployee appEmployee) {
        if (null != appEmployee && null != appEmployee.getEmpId()) {
            employeeDAO.updateByLoginName(appEmployee);
        }
    }

    @Override
    public void deleteByLoginName(String loginName) {
        if (null != loginName) {
            employeeDAO.deleteByLoginName(loginName);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unlockGuideCreditByUserIdAndCredit(Long userId, Double guideCredit) {
        if (null != userId && null != guideCredit) {
            employeeDAO.unlockGuideCreditByUserIdAndGuideCredit(userId, guideCredit);
        }
    }

    @Override
    public List<SellerResponse> findSellerByStoreIdAndIdentityType(Long storeId, AppIdentityType type) {
        return this.employeeDAO.findSellerByStoreIdAndIdentityType(storeId, type);
    }

    @Override
    public AppEmployee findDeliveryClerkNoByUserId(Long userId) {
        return this.employeeDAO.findDeliveryClerkNoByUserId(userId);
    }

    @Override
    public String getQrCodeByUserID(Long userID) {
        return employeeDAO.getQrCodeByUserID(userID);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EmpCreditMoney findEmpCreditMoneyByEmpId(Long empId) {
        if (null != empId){
            return employeeDAO.findEmpCreditMoneyByEmpId(empId);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addEmpCreditMoneyChangeLog(EmpCreditMoneyChangeLog log) {
        if (null !=log){
            EmpCreditMoneyChangeLogDO  empCreditMoneyChangeLogDO = EmpCreditMoneyChangeLogDO.transform(log);
            if(null!=log.getCreditLimitAvailableChangeAmount() && 0!=log.getCreditLimitAvailableChangeAmount()){
                EmpAvailableCreditMoneyChangeLog empAvailableCreditMoneyChangeLog =new EmpAvailableCreditMoneyChangeLog();
                empAvailableCreditMoneyChangeLog.setCreditLimitAvailableAfterChange(log.getCreditLimitAvailableAfterChange());
                empAvailableCreditMoneyChangeLog.setCreditLimitAvailableChangeAmount(log.getCreditLimitAvailableChangeAmount());
                Long id = employeeDAO.saveCreditLimitAvailableChange(empAvailableCreditMoneyChangeLog);
                empCreditMoneyChangeLogDO.setAvailableCreditChangId(empAvailableCreditMoneyChangeLog.getId());
            }
            if(null!=log.getTempCreditLimitChangeAmount()&& 0!=log.getCreditLimitAvailableChangeAmount()){
                EmpTempCreditMoneyChangeLog empTempCreditMoneyChangeLog = new EmpTempCreditMoneyChangeLog();
                empTempCreditMoneyChangeLog.setTempCreditLimitAfterChange(log.getTempCreditLimitAfterChange());
                empTempCreditMoneyChangeLog.setTempCreditLimitChangeAmount(log.getTempCreditLimitChangeAmount());
                Long id  = employeeDAO.saveTempCreditLimitChange(empTempCreditMoneyChangeLog);
                empCreditMoneyChangeLogDO.setTempCreditChangeId(empTempCreditMoneyChangeLog.getId());
           }
            employeeDAO.addEmpCreditMoneyChangeLog(empCreditMoneyChangeLogDO);
        }
    }
}
