package cn.com.leyizhuang.app.foundation.vo;

import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.employee.EmployeeDO;
import lombok.*;
import org.apache.catalina.Store;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    //导购类型
    private String sellerType;
    //手机号
    private String mobile;
    //生日
    private Date birthday;
    //状态
    private Boolean status;
    //性别
    private String sex;
    //城市id
    private City cityId;
    //门店id
    private StoreVO storeId;
    //头像路径
    private String picUrl;
    //创建时间
    private Date createTime;
    // 配送员编码
    private String deliveryClerkNo;
    //入职时间
    private Date entryTime;

    public static final EmployeeVO  transform(EmployeeDO employeeDO) {
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
            employeeVO.setSellerType(employeeDO.getSellerType());
            employeeVO.setMobile(employeeDO.getMobile());
            employeeVO.setBirthday(employeeDO.getBirthday());
            employeeVO.setStatus(employeeDO.getStatus());
            if("MALE".equals(employeeDO.getSex())){
                employeeVO.setSex("男");
            }else if("FEMALE".equals(employeeDO.getSex())){
                employeeVO.setSex("女");
            }else{
                employeeVO.setSex("保密");
            }
            employeeVO.setCityId(employeeDO.getCityId());
            employeeVO.setStoreId(employeeDO.getStoreId());
            employeeVO.setPicUrl(employeeDO.getPicUrl());
            employeeVO.setCreateTime(employeeDO.getCreateTime());
            employeeVO.setDeliveryClerkNo(employeeDO.getDeliveryClerkNo());
            employeeVO.setEntryTime(employeeDO.getEntryTime());
            return employeeVO;
        } else {
            return null;
        }
    }

    public static final List<EmployeeVO> transform(List<EmployeeDO> employeeDOList) {
        List<EmployeeVO> employeeVOList;
        if (null != employeeDOList && employeeDOList.size() > 0) {
            employeeVOList = new ArrayList<>(employeeDOList.size());
            employeeDOList.forEach(cityDeliveryTime -> employeeVOList.add(transform(cityDeliveryTime)));
        } else {
            employeeVOList = new ArrayList<>(0);
        }
        return employeeVOList;
    }
}
