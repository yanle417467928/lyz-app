package cn.com.leyizhuang.app.foundation.pojo.management.employee;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Created with IntelliJ IDEA.
 * 后台员工类型
 *
 * @author liuh
 * Date: 2017/12/24.
 * Time: 10:55.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeType implements Serializable {
    private Long empId;
    //身份类型
    private String identityType;

    public static final EmployeeType  transform(EmployeeDO employeeDO) {
        if (null != employeeDO) {
            EmployeeType employeeType = new EmployeeType();
            employeeType.setEmpId(employeeDO.getEmpId());
            if("SELLER".equals(employeeDO.getIdentityType())){
                employeeType.setIdentityType("导购");
            }else if("DELIVERY_CLERK".equals(employeeDO.getIdentityType())){
                employeeType.setIdentityType("配送员");
            }else if("DECORATE_MANAGER".equals(employeeDO.getIdentityType())){
                employeeType.setIdentityType("装饰经理");
            }else if("DECORATE_EMPLOYEE".equals(employeeDO.getIdentityType())){
                employeeType.setIdentityType("装饰工人");
            }else{
                employeeType.setIdentityType("");
            }
            return employeeType;
        } else {
            return null;
        }
    }

    public static final List<EmployeeType> transform(List<EmployeeDO> employeeDOList) {
        List<EmployeeType> employeeTypeList;
        if (null != employeeDOList && employeeDOList.size() > 0) {
            employeeTypeList = new ArrayList<>(employeeDOList.size());
            employeeDOList.forEach(employeeDO -> employeeTypeList.add(transform(employeeDO)));
        } else {
            employeeTypeList = new ArrayList<>(0);
        }
        return employeeTypeList;
    }

}
