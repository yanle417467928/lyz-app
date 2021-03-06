package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.AppSellerType;
import cn.com.leyizhuang.app.foundation.pojo.EmpCreditMoney;
import cn.com.leyizhuang.app.foundation.pojo.EmpCreditMoneyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.SalesConsult;
import cn.com.leyizhuang.app.foundation.pojo.request.UserSetInformationReq;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;

import java.sql.Timestamp;
import java.util.Date;
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

    PageInfo<AppEmployee> findDecorateEmployeeListByUserIdAndIdentityType(Long userId, Integer identityType, Integer page, Integer size);

    List<EmployeeListResponse> searchBySalesConsultIdAndKeywords(Long userId, String keywords, Integer identityType);

    SellerCreditMoneyResponse findCreditMoneyBalanceByUserIdAndIdentityType(Long userId, Integer identityType);

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

    String isSupervisor(Long id);

    SalesConsult findSellerByUserIdAndIdentityType(Long userId, Integer identityType);

    Integer unlockGuideCreditByUserIdAndGuideCreditAndVersion( Long userId, Double guideCredit, Date version);

    AppEmployee findDeliveryByClerkNo(String driver);

    List<AppEmployee> findQrcodeIsNull();


    List<AppEmployee> findAllSeller();

    List<SellerResponse> querySellerByStructureCode(String structureCode);

    List<StoreRankClassification> getStoreRankClassification(Long userId, AppIdentityType identityType);

    /**
     * 得到销售经理电话
     * @param storeCode
     * @return
     */
    String getSalesManagerSupportHotline(String storeCode);

    ResultDTO repairCreditMoneyChangeLog(Long empId, String flag);

    AppEmployee findSellerByMobile(String mobile);
}
