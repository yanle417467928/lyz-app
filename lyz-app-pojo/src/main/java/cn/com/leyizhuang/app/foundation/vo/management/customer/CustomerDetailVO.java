package cn.com.leyizhuang.app.foundation.vo.management.customer;


import cn.com.leyizhuang.app.core.constant.AppCustomerCreateType;
import cn.com.leyizhuang.app.core.constant.AppCustomerLightStatus;
import cn.com.leyizhuang.app.foundation.pojo.management.city.SimpleCityParam;
import cn.com.leyizhuang.app.foundation.pojo.management.customer.CustomerDO;
import cn.com.leyizhuang.app.foundation.pojo.management.employee.SimpleEmployeeParam;
import cn.com.leyizhuang.app.foundation.pojo.management.store.SimpleStoreParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 顾客详情VO
 *
 * @author liuh
 * Created on 2017-11-03 14:14
 **/

@Getter
@Setter
@ToString

public class CustomerDetailVO {

    //顾客ID
    private Long cusId;
    //城市id
    private SimpleCityParam city;
    //门店id
    private SimpleStoreParam store;
    //销售顾问id
    private SimpleEmployeeParam salesConsultId;
    //真实姓名
    private String name;
    //手机号码
    private String mobile;
    //生日
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date birthday;
    //状态：禁用、启用
    private Boolean status;
    //性别：男、女
    private String sex;
    //微信openid
    private String openId;
    //头像路径
    private String picUrl;
    //昵称
    private String nickName;

    //是否货到付款
    private Boolean isCashOnDelivery;
    //创建时间
    private Date createTime;
    //灯号:绿灯 黄灯 红灯 熄灯
    private AppCustomerLightStatus light;
    //创建类型（APP注册、后台添加）
    private AppCustomerCreateType createType;
    //顾客类型（零售、会员）
    private String customerType;
    //上次签到时间
    private Date lastSignTime;
    //绑定时间
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date bindingTime;

    public static final CustomerDetailVO transform(CustomerDO customerDO) {
        if (null != customerDO) {
            CustomerDetailVO customerVO = new CustomerDetailVO();
            customerVO.setCusId(customerDO.getCusId());
            customerVO.setCity(customerDO.getCityId());
            customerVO.setName(customerDO.getName());
            customerVO.setMobile(customerDO.getMobile());
            customerVO.setBirthday(customerDO.getBirthday());
            customerVO.setStatus(customerDO.getStatus());
            customerVO.setSex(customerDO.getSex());
            customerVO.setPicUrl(customerDO.getPicUrl());
            customerVO.setCreateTime(customerDO.getCreateTime());
            customerVO.setLight(customerDO.getLight());
            customerVO.setBindingTime(customerDO.getBindingTime());
            customerVO.setCreateType(customerDO.getCreateType());
            customerVO.setIsCashOnDelivery(customerDO.getIsCashOnDelivery());
            customerVO.setLastSignTime(customerDO.getLastSignTime());
            customerVO.setNickName(customerDO.getNickName());
            customerVO.setOpenId(customerDO.getOpenId());
            customerVO.setStore(customerDO.getStoreId());
            customerVO.setSalesConsultId(customerDO.getSalesConsultId());
            return customerVO;
        } else {
            return null;
        }
    }

    public static final List<CustomerDetailVO> transform(List<CustomerDO> customerList) {
        List<CustomerDetailVO> customerVOList;
        if (null != customerList && customerList.size() > 0) {
            customerVOList = new ArrayList<>(customerList.size());
            customerList.forEach(customerDO -> customerVOList.add(transform(customerDO)));
        } else {
            customerVOList = new ArrayList<>(0);
        }
        return customerVOList;
    }
}
