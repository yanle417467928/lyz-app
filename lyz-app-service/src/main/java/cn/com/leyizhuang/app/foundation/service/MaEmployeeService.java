package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.management.employee.EmployeeDO;
import cn.com.leyizhuang.app.foundation.pojo.management.employee.EmployeeType;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaEmployeeResponse;
import cn.com.leyizhuang.app.foundation.vo.management.employee.DecorativeEmployeeDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.employee.EmployeeDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.employee.EmployeeLogVo;
import cn.com.leyizhuang.app.foundation.vo.management.employee.EmployeeVO;
import cn.com.leyizhuang.app.foundation.vo.management.guide.GuideVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface MaEmployeeService {
    PageInfo<EmployeeDO> queryPageVO(Integer page, Integer size, List<Long> storeIds);

    EmployeeDetailVO queryEmployeeById(Long id);


    List<EmployeeVO> findGuideListById(Long storeId);

    List<EmployeeType> findEmpTypeByStoreId(Long storeId);

    List<EmployeeType> findEmpTypeByCityId( Long cityId);

    PageInfo<EmployeeDO> queryPageVOByCondition(Integer page, Integer size,String identityType,Long storeId,Long cityId,String enabled, List<Long> storeIds);

    PageInfo<EmployeeDO> queryPageVOByInfo(Integer page, Integer size,String queryEmpInfo, List<Long> storeIds);

    PageInfo<EmployeeDO> queryDecorativeEmpPageVO(Integer page, Integer size);

    DecorativeEmployeeDetailVO queryDecorativeEmployeeById(Long id);

    List<EmployeeType> findEmpTypeList();

    PageInfo<EmployeeDO>  queryDecorativeEmpPageVOByInfo(Integer page, Integer size,String queryEmpInfo);

    PageInfo<EmployeeDO> findDecorativeEmpByCondition(Integer page, Integer size,String enabled,String diyId,String identityType);

    PageInfo<GuideVO> queryGuideVOPage(Integer page, Integer size, List<Long> storeIds);

    GuideVO queryGuideVOById(Long id);

    PageInfo<GuideVO> queryGuideVOByCondition(Integer page, Integer size,Long cityId, Long storeId, List<Long> storeIds);

    PageInfo<GuideVO>  queryGuideVOByInfo(Integer page,Integer size,String queryGuideVOInfo, List<Long> storeIds);

    EmployeeDO findEmployeeDOByEmpId(Long id);

    /**
     * 后台购买产品券选择导购查询
     * @return
     */
    PageInfo<MaEmployeeResponse> findMaEmployeeByCityIdAndStoreId(Integer page, Integer size,List<Long> storeIds);

    /**
     * 后台购买产品券条件查询导购
     *
     * @return
     */
    List<MaEmployeeResponse> findEmployeeByCityIdAndStoreIdAndSellerNameAndSellerPhone(String sellerQueryConditions,Long cityId,Long storeId);

    PageInfo<GuideVO> queryGuideArrearsPage(Integer page, Integer size);

    PageInfo<GuideVO> queryGuideRepaymentPage(Integer page, Integer size);

    void updateQrcode(String qrcodeUrl , Long empId);

    void updatePhoto(String url , Long empId);

    List<EmployeeVO> findEmployeeListByStoreId(Long storeId);

    List<EmployeeVO> findSellerListByStoreId(Long storeId);

    List<EmployeeDO> findEmpployeeByCityIdAndIdentityType(Long cityId, AppIdentityType type);

    EmployeeLogVo queryLastDecorativeCreditChange(Long id);
}
