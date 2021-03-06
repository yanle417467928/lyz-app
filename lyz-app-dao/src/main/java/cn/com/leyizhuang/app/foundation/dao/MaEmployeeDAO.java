package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.management.employee.EmployeeDO;
import cn.com.leyizhuang.app.foundation.pojo.management.guide.GuideCreditChangeDetailDO;
import cn.com.leyizhuang.app.foundation.pojo.management.guide.GuideCreditChangeDetailDO4Simple;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaEmployeeResponse;
import cn.com.leyizhuang.app.foundation.vo.management.employee.EmployeeLogVo;
import cn.com.leyizhuang.app.foundation.vo.management.guide.GuideVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaEmployeeDAO {

    List<EmployeeDO> findAllEmployee(@Param("list") List<Long> storeIds);

    List<MaEmployeeResponse> findAllEmployee4Message(@Param("keywords")String keywords);

    EmployeeDO queryEmployeeById(Long id);


    List<EmployeeDO> findGuideListById(Long storeId);

    List<EmployeeDO> findEmpTypeByStoreId(Long storeId);

    List<EmployeeDO> findEmpTypeByCityId(Long cityId);

    List<EmployeeDO> findEmpTypeList();

    List<EmployeeDO> queryPageVOByStoreCondition(@Param(value = "identityType") String identityType, @Param(value = "storeId") Long storeId, @Param(value = "enabled") String enabled,@Param("list") List<Long> storeIds);

    List<EmployeeDO> queryPageVOByCityCondition(@Param(value = "identityType") String identityType, @Param(value = "cityId") Long cityId, @Param(value = "enabled") String enabled,@Param("list") List<Long> storeIds);

    List<EmployeeDO> findEmployeeByInfo(@Param(value = "queryEmpInfo") String queryEmpInfo,@Param("list") List<Long> storeIds);

    List<EmployeeDO> queryDecorativeEmpPageVO();

    List<EmployeeDO> queryDecorativeEmpPageVOByInfo(String queryEmpInfo);

    List<EmployeeDO> findDecorativeEmpByCondition(@Param(value = "enabled") String enabled, @Param(value = "diyId") String diyId, @Param(value = "identityType") String identityType);

    List<GuideVO> findAllGuide(@Param("list") List<Long> storeIds);

    GuideVO queryGuideVOById(Long id);

    List<GuideVO> queryGuideVOByCondition(@Param(value = "cityId") Long cityId, @Param(value = "storeId") Long storeId,@Param("list") List<Long> storeIds);

    List<GuideVO> queryGuideVOByInfo(@Param("queryGuideVOInfo")String queryGuideVOInfo,@Param("list") List<Long> storeIds);

    EmployeeDO findEmployeeDOByEmpId(Long id);

    /**
     * 后台购买产品券选择导购查询
     *
     * @return
     */
    List<MaEmployeeResponse> findEmployeeByCityIdAndStoreId(@Param("list") List<Long> storeIds);

    /**
     * 后台购买产品券条件查询导购
     *
     * @return
     */
    List<MaEmployeeResponse> findEmployeeByCityIdAndStoreIdAndSellerNameAndSellerPhone(@Param(value = "sellerQueryConditions") String sellerQueryConditions,
                                                                               @Param(value = "cityId") Long cityId, @Param(value = "storeId") Long storeId);

    List<GuideVO> queryGuideArrears();

    List<GuideVO> queryGuideRepayment();

    List<EmployeeDO> findEmpployeeByCityIdAndIdentityType(@Param("cityId")Long cityId, @Param("type") AppIdentityType type);

    GuideCreditChangeDetailDO4Simple queryLastDecorativeCreditChange(Long id);

}
