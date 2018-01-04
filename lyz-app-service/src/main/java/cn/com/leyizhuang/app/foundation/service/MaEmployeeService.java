package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.management.employee.EmployeeDO;
import cn.com.leyizhuang.app.foundation.pojo.management.employee.EmployeeType;
import cn.com.leyizhuang.app.foundation.vo.management.employee.DecorativeEmployeeDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.employee.EmployeeDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.guide.GuideVO;
import cn.com.leyizhuang.app.foundation.vo.management.employee.EmployeeVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface MaEmployeeService {
    PageInfo<EmployeeDO> queryPageVO(Integer page, Integer size);

    EmployeeDetailVO queryEmployeeById(Long id);


    List<EmployeeVO> findGuideListById(Long storeId);

    List<EmployeeType> findEmpTypeByStoreId(Long storeId);

    List<EmployeeType> findEmpTypeByCityId( Long cityId);

    PageInfo<EmployeeDO> queryPageVOByCondition(Integer page, Integer size,String identityType,Long storeId,Long cityId,String enabled);

    PageInfo<EmployeeDO> queryPageVOByInfo(Integer page, Integer size,String queryEmpInfo);

    PageInfo<EmployeeDO> queryDecorativeEmpPageVO(Integer page, Integer size);

    DecorativeEmployeeDetailVO queryDecorativeEmployeeById(Long id);

    List<EmployeeType> findEmpTypeList();

    PageInfo<EmployeeDO>  queryDecorativeEmpPageVOByInfo(Integer page, Integer size,String queryEmpInfo);

    PageInfo<EmployeeDO> findDecorativeEmpByCondition(Integer page, Integer size,String enabled,String diyId,String identityType);

    PageInfo<GuideVO> queryGuideVOPage(Integer page, Integer size);

    GuideVO queryGuideVOById(Long id);

    PageInfo<GuideVO> queryGuideVOByCondition(Integer page, Integer size,Long cityId, Long storeId);

    PageInfo<GuideVO>  queryGuideVOByInfo(Integer page,Integer size,String queryGuideVOInfo);
}
