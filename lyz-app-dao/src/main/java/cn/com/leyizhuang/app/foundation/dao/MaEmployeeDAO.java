package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.employee.EmployeeDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaEmployeeDAO {

    List<EmployeeDO> findAllEmployee();

    EmployeeDO queryEmployeeById(Long id);


    List<EmployeeDO> findGuideListById(Long storeId);

    List<EmployeeDO> findEmpTypeByStoreId(Long storeId);

    List<EmployeeDO> findEmpTypeByCityId(Long cityId);

    List<EmployeeDO> findEmpTypeList();

    List<EmployeeDO> queryPageVOByStoreCondition(@Param(value = "identityType") String identityType, @Param(value = "storeId") Long storeId,@Param(value = "enabled") String enabled);

    List<EmployeeDO> queryPageVOByCityCondition(@Param(value = "identityType") String identityType, @Param(value = "cityId") Long cityId,@Param(value = "enabled") String enabled);

    List<EmployeeDO> findEmployeeByInfo(String queryEmpInfo);

    List<EmployeeDO> queryDecorativeEmpPageVO();

    List<EmployeeDO> queryDecorativeEmpPageVOByInfo(String queryEmpInfo);

    List<EmployeeDO> findDecorativeEmpByCondition(@Param(value = "enabled") String enabled, @Param(value = "diyId") String diyId,@Param(value = "identityType") String identityType);
}