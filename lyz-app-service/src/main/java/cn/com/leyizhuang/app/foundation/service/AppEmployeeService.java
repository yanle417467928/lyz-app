package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.EmpCreditMoney;
import cn.com.leyizhuang.app.foundation.pojo.EmpCreditMoneyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.request.UserSetInformationReq;
import cn.com.leyizhuang.app.foundation.pojo.response.EmployeeHomePageResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.EmployeeListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.SellerCreditMoney;
import cn.com.leyizhuang.app.foundation.pojo.response.SellerResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;

import java.sql.Timestamp;
import java.util.List;

/**
 * lyz-app-facade用户服务接口
 *
 * @author Richard
 * Created on 2017-09-19 11:18
 **/
public interface AppEmployeeService {

    void save(AppEmployee appEmployee);

    AppEmployee findByLoginName(String name);

    AppEmployee findByMobile(String mobile);

    void update(AppEmployee newEmployee);

    AppEmployee findByUserId(Long userId);

    AppEmployee findById(Long userId);

    AppEmployee findByIdAndStatusIsTrue(Long userId);

    List<EmployeeListResponse> findDecorateEmployeeListByUserIdAndIdentityType(Long userId, Integer identityType);

    List<EmployeeListResponse> searchBySalesConsultIdAndKeywords(Long userId, String keywords, Integer identityType);

    SellerCreditMoney findCreditMoneyBalanceByUserIdAndIdentityType(Long userId, Integer identityType);

    void modifyEmployeeInformation(UserSetInformationReq userInformation);

    EmployeeHomePageResponse findEmployeeInfoByUserIdAndIdentityType(Long userId, Integer identityType);

    void modifyEmployeeMobileByUserId(Long userId, String mobile);

    Boolean existsSellerCreditByUserId(Long userId);

    int lockGuideCreditByUserIdAndCredit(Long userId, Double guideCredit, Timestamp version);

    void updateByLoginName(AppEmployee appEmployee);

    void deleteByLoginName(String loginName);

    void unlockGuideCreditByUserIdAndCredit(Long userId, Double guideCredit);

    List<SellerResponse> findSellerByStoreIdAndIdentityType(Long storeId, AppIdentityType type);

    AppEmployee findDeliveryClerkNoByUserId(Long userId);

    /**
     * 查询导购二维码
     */
    String getQrCodeByUserID(Long userID);

    EmpCreditMoney findEmpCreditMoneyByEmpId(Long empId);

    void addEmpCreditMoneyChangeLog(EmpCreditMoneyChangeLog log);
}
