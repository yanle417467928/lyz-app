package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.employee.EmployeeDO;
import cn.com.leyizhuang.app.foundation.vo.EmployeeVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface MaEmployeeService {
    PageInfo<EmployeeDO> queryPageVO(Integer page, Integer size);

    EmployeeVO queryEmployeeById(Long id);

    PageInfo<EmployeeDO> queryPageVOByCityId(Integer page, Integer size,Long cityId);

    List<EmployeeVO> findGuideListById(Long storeId);

    List<EmployeeVO> findEmpTypeListById(Long id);

    PageInfo<EmployeeDO> queryPageVOByStoreId(Integer page, Integer size,Long storeId);

    PageInfo<EmployeeDO> queryPageVOByType(Integer page, Integer size,String identityType,Long storeId);

    PageInfo<EmployeeDO> queryPageVOByInfo(Integer page, Integer size,String queryEmpInfo);
}
