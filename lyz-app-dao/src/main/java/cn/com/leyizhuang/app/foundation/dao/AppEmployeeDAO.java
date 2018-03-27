package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.response.EmployeeHomePageResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.SellerCreditMoneyResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.SellerResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * lyz-app-facade 员工数据仓库
 *
 * @author Richard
 * Created on 2017-09-19 11:26
 **/
@Repository
public interface AppEmployeeDAO {

    void save(AppEmployee appEmployee);

    AppEmployee findByLoginName(String loginName);

    AppEmployee findByMobile(String mobile);

    void update(AppEmployee newEmployee);

    AppEmployee findByUserId(Long userId);

    AppEmployee findById(Long id);

    AppEmployee findByIdAndStatusIsTrue(Long id);

    List<AppEmployee> findDecorateEmployeeListByManagerId(Long userId);

    List<AppEmployee> searchBySalesConsultIdAndKeywords(@Param("userId") Long userId, @Param("keywords") String keywords);

    SellerCreditMoneyResponse findCreditMoneyBalanceByUserId(Long userId);

    EmployeeHomePageResponse findEmployeeInfoByUserIdAndIdentityType(
            @Param("userId") Long userId, @Param("type") Integer identityType);

    void updateEmployeeMobileByUserId(@Param("userId") Long userId, @Param("mobile") String mobile);

    Boolean existsSellerCreditByUserId(Long userId);

    int lockGuideCreditByUserIdAndGuideCredit(@Param("userId") Long userId, @Param("credit") Double guideCredit,
                                              @Param(value = "version") Timestamp version);

    void updateByLoginName(AppEmployee appEmployee);

    void deleteByLoginName(String loginName);

    void unlockGuideCreditByUserIdAndGuideCredit(@Param("userId") Long userId, @Param("credit") Double guideCredit);

    Integer unlockGuideCreditByUserIdAndGuideCreditAndVersion(@Param("userId") Long userId, @Param("credit") Double guideCredit, @Param("version")Date version);

    List<SellerResponse> findSellerByStoreIdAndIdentityType(@Param("storeId") Long storeId, @Param("type") AppIdentityType type);

    AppEmployee findDeliveryClerkNoByUserId(Long userId);

    /**
     * 查询导购二维码
     */
    String getQrCodeByUserID(@Param("userID") Long userID);

    EmpCreditMoney findEmpCreditMoneyByEmpId(Long empId);

    void addEmpCreditMoneyChangeLog(EmpCreditMoneyChangeLogDO log);

    Long saveCreditLimitAvailableChange(EmpAvailableCreditMoneyChangeLog empAvailableCreditMoneyChangeLog);

    Long saveTempCreditLimitChange(EmpTempCreditMoneyChangeLog empTempCreditMoneyChangeLog);

    String isSupervisor(Long id);

    SalesConsult findSellerByEmpId(Long userId);

    SalesConsult findSellerByCustomerId(Long userId);

    AppEmployee findDeliveryByClerkNo(String driver);

    List<AppEmployee> findQrcodeIsNull();

    List<AppEmployee> findAllSeller();
}
