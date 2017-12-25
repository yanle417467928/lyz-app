package cn.com.leyizhuang.app.foundation.vo.management.employee;

import cn.com.leyizhuang.app.foundation.pojo.management.city.SimpleCityParam;
import cn.com.leyizhuang.app.foundation.pojo.management.employee.EmployeeDO;
import cn.com.leyizhuang.app.foundation.pojo.management.store.SimpleStoreParam;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
/**
 * 员工VO
 *
 * @author liuh
 * Created on 2017-11-03 14:14
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeVO {
    //员工id
    private Long id;
    //登录名
    private String loginName;
    //姓名
    private String name;
    //身份类型
    private String identityType;
    //状态：启用 停用
    private Boolean status;
    //城市id
    private SimpleCityParam cityId;
    //门店id
    private SimpleStoreParam storeId;

    public static final EmployeeVO transform(EmployeeDO employeeDO) {
        if (null != employeeDO) {
            EmployeeVO employeeVO = new EmployeeVO();
            employeeVO.setId(employeeDO.getEmpId());
            employeeVO.setLoginName(employeeDO.getLoginName());
            employeeVO.setName(employeeDO.getName());
            if("SELLER".equals(employeeDO.getIdentityType())){
                employeeVO.setIdentityType("导购");
            }else if("DELIVERY_CLERK".equals(employeeDO.getIdentityType())){
                employeeVO.setIdentityType("配送员");
            }else if("DECORATE_MANAGER".equals(employeeDO.getIdentityType())){
                employeeVO.setIdentityType("装饰经理");
            }else if("DECORATE_EMPLOYEE".equals(employeeDO.getIdentityType())){
                employeeVO.setIdentityType("装饰工人");
            }else{
                employeeVO.setIdentityType("");
            }
            employeeVO.setStatus(employeeDO.getStatus());
            employeeVO.setCityId(employeeDO.getCityId());
            employeeVO.setStoreId(employeeDO.getStoreId());
            return employeeVO;
        } else {
            return null;
        }
    }

    public static final List<EmployeeVO> transform(List<EmployeeDO> employeeDOList) {
        List<EmployeeVO> employeeVOList;
        if (null != employeeDOList && employeeDOList.size() > 0) {
            employeeVOList = new ArrayList<>(employeeDOList.size());
            employeeDOList.forEach(employeeDO -> employeeVOList.add(transform(employeeDO)));
        } else {
            employeeVOList = new ArrayList<>(0);
        }
        return employeeVOList;
    }
}
