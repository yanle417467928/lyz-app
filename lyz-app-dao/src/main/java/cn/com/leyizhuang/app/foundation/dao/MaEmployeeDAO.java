package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.employee.EmployeeDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaEmployeeDAO {

    List<EmployeeDO> findAllEmployee();

    EmployeeDO queryEmployeeById(Long id);

    List<EmployeeDO> findEmployeeByCityId(Long cityId);

    List<EmployeeDO> findGuideListById(Long storeId);

    List<EmployeeDO> findEmpTypeListById(Long id);

    List<EmployeeDO> findEmployeeByStoreId(Long storeId);

    List<EmployeeDO> queryPageVOByType(@Param(value = "identityType") String identityType, @Param(value = "storeId") Long storeId);

    List<EmployeeDO> findEmployeeByInfo(String queryEmpInfo);
}
