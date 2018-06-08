package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.AppSellerType;
import cn.com.leyizhuang.app.core.constant.EmpCreditMoneyChangeType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.AppEmployeeDAO;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.request.UserSetInformationReq;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
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
    public PageInfo<AppEmployee> findDecorateEmployeeListByUserIdAndIdentityType(Long userId, Integer identityType, Integer page, Integer size) {
        if (null != userId && null != identityType && identityType == 2) {
            PageHelper.startPage(page, size);
            List<AppEmployee> appEmployeeList = employeeDAO.findDecorateEmployeeListByManagerId(userId);
            return new PageInfo<>(appEmployeeList);
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
            if(null!=log.getCreditLimitAvailableChangeAmount() && 0D!=log.getCreditLimitAvailableChangeAmount()){
                EmpAvailableCreditMoneyChangeLog empAvailableCreditMoneyChangeLog =new EmpAvailableCreditMoneyChangeLog();
                empAvailableCreditMoneyChangeLog.setChangeType(log.getChangeType());
                empAvailableCreditMoneyChangeLog.setChangeTypeDesc(log.getChangeType().getDescription());
                empAvailableCreditMoneyChangeLog.setCreditLimitAvailableAfterChange(log.getCreditLimitAvailableAfterChange());
                empAvailableCreditMoneyChangeLog.setCreditLimitAvailableChangeAmount(log.getCreditLimitAvailableChangeAmount());
                Long id = employeeDAO.saveCreditLimitAvailableChange(empAvailableCreditMoneyChangeLog);
                empCreditMoneyChangeLogDO.setAvailableCreditChangId(empAvailableCreditMoneyChangeLog.getId());
            }
            if(null!=log.getTempCreditLimitChangeAmount()&& 0D!=log.getTempCreditLimitChangeAmount()){
                EmpTempCreditMoneyChangeLog empTempCreditMoneyChangeLog = new EmpTempCreditMoneyChangeLog();
                empTempCreditMoneyChangeLog.setTempCreditLimitAfterChange(log.getTempCreditLimitAfterChange());
                empTempCreditMoneyChangeLog.setTempCreditLimitChangeAmount(log.getTempCreditLimitChangeAmount());
                Long id  = employeeDAO.saveTempCreditLimitChange(empTempCreditMoneyChangeLog);
                empCreditMoneyChangeLogDO.setTempCreditChangeId(empTempCreditMoneyChangeLog.getId());
           }
            employeeDAO.addEmpCreditMoneyChangeLog(empCreditMoneyChangeLogDO);
        }
    }


    @Override
    public String isSupervisor(Long id) {
        String isSupervisor = employeeDAO.isSupervisor(id);
        return isSupervisor;
    }

    @Override
    public SalesConsult findSellerByUserIdAndIdentityType(Long userId, Integer identityType) {
        if (identityType == 0) {
            return employeeDAO.findSellerByEmpId(userId);
        } else if (identityType == 6) {
            return employeeDAO.findSellerByCustomerId(userId);
        }
        return null;
    }

    @Override
    public Integer unlockGuideCreditByUserIdAndGuideCreditAndVersion(Long userId, Double guideCredit, Date version) {
        if (null != userId && null != guideCredit){
            return employeeDAO.unlockGuideCreditByUserIdAndGuideCreditAndVersion(userId, guideCredit, version);
        }
        return null;
    }

    @Override
    public AppEmployee findDeliveryByClerkNo(String driver) {
        if (StringUtils.isNotBlank(driver)) {
            return employeeDAO.findDeliveryByClerkNo(driver);
        }
        return null;
    }

    @Override
    public List<AppEmployee> findQrcodeIsNull(){
        return employeeDAO.findQrcodeIsNull();
    }

    @Override
    public List<AppEmployee> findAllSeller() {
        return employeeDAO.findAllSeller();
    }

    @Override
    public List<SellerResponse> querySellerByStructureCode(String structureCode){
        return employeeDAO.querySellerByStructureCode(structureCode);
    }

    @Override
    public List<StoreRankClassification> getStoreRankClassification(Long userId, AppIdentityType identityType) {
        return employeeDAO.getStoreRankClassification(userId, identityType);
    }

    @Override
    public String getSalesManagerSupportHotline(String storeCode) {
        return employeeDAO.getSalesManagerSupportHotline(storeCode);
    }

    @Override
    public AppEmployee findSellerByMobile(String mobile) {
        if (null != mobile && !"".equalsIgnoreCase(mobile)) {
            return employeeDAO.findByMobile(mobile);
        }
        return null;
    }
}
