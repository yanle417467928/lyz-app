package cn.com.leyizhuang.app.foundation.vo.management.employee;

import cn.com.leyizhuang.app.foundation.pojo.management.city.SimpleCityParam;
import cn.com.leyizhuang.app.foundation.pojo.management.employee.EmployeeDO;
import cn.com.leyizhuang.app.foundation.pojo.management.store.SimpleStoreParam;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 装饰公司员工VO
 *
 * @author liuh
 * Created on 2017-11-03 14:14
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DecorativeEmployeeVO {
    //员工id
    private Long id;
    //登录名
    private String loginName;
    //姓名
    private String name;
    //身份类型：装饰经理.装饰工人
    private String identityType;
    //状态：启用 停用
    private Boolean status;
    //城市id
    private SimpleCityParam cityId;
    //门店id
    private SimpleStoreParam storeId;

    public static final DecorativeEmployeeVO  transform(EmployeeDO employeeDO) {
        if (null != employeeDO) {
            DecorativeEmployeeVO decorativeEmployeeVO = new DecorativeEmployeeVO();
            decorativeEmployeeVO.setId(employeeDO.getEmpId());
            decorativeEmployeeVO.setLoginName(employeeDO.getLoginName());
            decorativeEmployeeVO.setName(employeeDO.getName());
            if("SELLER".equals(employeeDO.getIdentityType().toString())){
                decorativeEmployeeVO.setIdentityType("导购");
            }else if("DELIVERY_CLERK".equals(employeeDO.getIdentityType().toString())){
                decorativeEmployeeVO.setIdentityType("配送员");
            }else if("DECORATE_MANAGER".equals(employeeDO.getIdentityType().toString())){
                decorativeEmployeeVO.setIdentityType("装饰经理");
            }else if("DECORATE_EMPLOYEE".equals(employeeDO.getIdentityType().toString())){
                decorativeEmployeeVO.setIdentityType("装饰工人");
            }else{
                decorativeEmployeeVO.setIdentityType("");
            }
            decorativeEmployeeVO.setStatus(employeeDO.getStatus());
            decorativeEmployeeVO.setCityId(employeeDO.getCityId());
            decorativeEmployeeVO.setStoreId(employeeDO.getStoreId());
            return decorativeEmployeeVO;
        } else {
            return null;
        }
    }

    public static final List<DecorativeEmployeeVO> transform(List<EmployeeDO> employeeDOList) {
        List<DecorativeEmployeeVO> decorativeEmployeeVO;
        if (null != employeeDOList && employeeDOList.size() > 0) {
            decorativeEmployeeVO = new ArrayList<>(employeeDOList.size());
            employeeDOList.forEach(employeeDO -> decorativeEmployeeVO.add(transform(employeeDO)));
        } else {
            decorativeEmployeeVO = new ArrayList<>(0);
        }
        return decorativeEmployeeVO;
    }
}
