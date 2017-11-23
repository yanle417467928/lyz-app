package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.response.EmployeeHomePageResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.SellerCreditMoney;
import cn.com.leyizhuang.app.foundation.pojo.response.SellerResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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

    List<AppEmployee> searchBySalesConsultIdAndKeywords(@Param("userId") Long userId,@Param("keywords") String keywords);

    SellerCreditMoney findCreditMoneyBalanceByUserId(Long userId);

    EmployeeHomePageResponse findEmployeeInfoByUserIdAndIdentityType(
            @Param("userId") Long userId,@Param("type") Integer identityType);

    void updateEmployeeMobileByUserId(@Param("userId") Long userId,@Param("mobile") String mobile);

    Boolean existsSellerCreditByUserId(Long userId);

    int lockGuideCreditByUserIdAndGuideCredit(@Param("userId") Long userId,@Param("credit") Double guideCredit);

    void updateByLoginName(AppEmployee appEmployee);

    void deleteByLoginName(String loginName);

    void unlockGuideCreditByUserIdAndGuideCredit(@Param("userId") Long userId,@Param("credit") Double guideCredit);

    List<SellerResponse> findSellerByStoreIdAndIdentityType(@Param("storeId") Long storeId, @Param("type")AppIdentityType type);
}
