package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.employee.EmployeeDO;
import cn.com.leyizhuang.app.foundation.vo.DecorativeEmployeeVO;
import cn.com.leyizhuang.app.foundation.vo.EmployeeVO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MaEmployeeService {
    PageInfo<EmployeeDO> queryPageVO(Integer page, Integer size);

    EmployeeVO queryEmployeeById(Long id);


    List<EmployeeVO> findGuideListById(Long storeId);

    List<EmployeeVO> findEmpTypeByStoreId(Long storeId);

    List<EmployeeVO> findEmpTypeByCityId( Long cityId);

    PageInfo<EmployeeDO> queryPageVOByCondition(Integer page, Integer size,String identityType,Long storeId,Long cityId,String enabled);

    PageInfo<EmployeeDO> queryPageVOByInfo(Integer page, Integer size,String queryEmpInfo);

    PageInfo<EmployeeDO> queryDecorativeEmpPageVO(Integer page, Integer size);

    DecorativeEmployeeVO queryDecorativeEmployeeById(Long id);

    List<EmployeeVO> findEmpTypeList();

    PageInfo<EmployeeDO>  queryDecorativeEmpPageVOByInfo(Integer page, Integer size,String queryEmpInfo);

    PageInfo<EmployeeDO> findDecorativeEmpByCondition(Integer page, Integer size,String enabled,String diyId,String identityType);
}
