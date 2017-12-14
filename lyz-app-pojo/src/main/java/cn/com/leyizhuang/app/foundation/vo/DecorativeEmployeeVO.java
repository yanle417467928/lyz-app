package cn.com.leyizhuang.app.foundation.vo;

import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.employee.EmployeeDO;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    //身份类型
    private String identityType;
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
    //入职时间
    private Date entryTime;

    public static final DecorativeEmployeeVO  transform(EmployeeDO employeeDO) {
        if (null != employeeDO) {
            DecorativeEmployeeVO decorativeEmployeeVO = new DecorativeEmployeeVO();
            decorativeEmployeeVO.setId(employeeDO.getEmpId());
            decorativeEmployeeVO.setLoginName(employeeDO.getLoginName());
            decorativeEmployeeVO.setName(employeeDO.getName());
            if("SELLER".equals(employeeDO.getIdentityType())){
                decorativeEmployeeVO.setIdentityType("导购");
            }else if("DELIVERY_CLERK".equals(employeeDO.getIdentityType())){
                decorativeEmployeeVO.setIdentityType("配送员");
            }else if("DECORATE_MANAGER".equals(employeeDO.getIdentityType())){
                decorativeEmployeeVO.setIdentityType("装饰经理");
            }else if("DECORATE_EMPLOYEE".equals(employeeDO.getIdentityType())){
                decorativeEmployeeVO.setIdentityType("装饰工人");
            }else{
                decorativeEmployeeVO.setIdentityType("");
            }
            decorativeEmployeeVO.setMobile(employeeDO.getMobile());
            decorativeEmployeeVO.setBirthday(employeeDO.getBirthday());
            decorativeEmployeeVO.setStatus(employeeDO.getStatus());
            if("MALE".equals(employeeDO.getSex())){
                decorativeEmployeeVO.setSex("男");
            }else if("FEMALE".equals(employeeDO.getSex())){
                decorativeEmployeeVO.setSex("女");
            }else{
                decorativeEmployeeVO.setSex("保密");
            }
            decorativeEmployeeVO.setCityId(employeeDO.getCityId());
            decorativeEmployeeVO.setStoreId(employeeDO.getStoreId());
            decorativeEmployeeVO.setPicUrl(employeeDO.getPicUrl());
            decorativeEmployeeVO.setCreateTime(employeeDO.getCreateTime());
            decorativeEmployeeVO.setEntryTime(employeeDO.getEntryTime());
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
