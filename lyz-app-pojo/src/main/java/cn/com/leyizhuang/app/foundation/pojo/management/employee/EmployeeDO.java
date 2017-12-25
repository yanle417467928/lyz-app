package cn.com.leyizhuang.app.foundation.pojo.management.employee;

import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.management.city.SimpleCityParam;
import cn.com.leyizhuang.app.foundation.pojo.management.store.SimpleStoreParam;
import cn.com.leyizhuang.app.foundation.vo.management.employee.EmployeeDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.store.StoreVO;
import lombok.*;

import java.util.Date;
/**
 * Created with IntelliJ IDEA.
 * 后台员工DO
 *
 * @author liuh
 * Date: 2017/11/23.
 * Time: 10:41.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDO {

    //员工id
    private Long empId;
    //登录名
    private String loginName;
    //姓名
    private String name;
    //密码
    private String password;
    //密码加盐
    private String salt;
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
    //头像路径
    private String picUrl;
    //经理id
    private Long managerId;
    //城市id
    private SimpleCityParam cityId;
    //门店id
    private SimpleStoreParam storeId;
    //创建时间
    private Date createTime;
    // 配送员编码
    private String deliveryClerkNo;
    //入职时间
    private Date entryTime;

    public static final EmployeeDO  transform(EmployeeDetailVO employeeVO) {
        if (null != employeeVO) {
            EmployeeDO employeeDO = new EmployeeDO();
            employeeDO.setEmpId(employeeVO.getId());
            employeeDO.setLoginName(employeeVO.getLoginName());
            employeeDO.setName(employeeVO.getName());
            employeeDO.setIdentityType(employeeVO.getIdentityType());
            employeeDO.setSellerType(employeeVO.getSellerType());
            employeeDO.setMobile(employeeVO.getMobile());
            employeeDO.setBirthday(employeeVO.getBirthday());
            employeeDO.setStatus(employeeVO.getStatus());
            employeeDO.setSex(employeeVO.getSex());
            employeeDO.setCityId(employeeVO.getCityId());
            employeeDO.setStoreId(employeeVO.getStoreId());
            employeeDO.setPicUrl(employeeVO.getPicUrl());
            employeeDO.setCreateTime(employeeVO.getCreateTime());
            employeeDO.setDeliveryClerkNo(employeeVO.getDeliveryClerkNo());
            employeeDO.setEntryTime(employeeVO.getEntryTime());
            return employeeDO;
        } else {
            return null;
        }
    }

}
