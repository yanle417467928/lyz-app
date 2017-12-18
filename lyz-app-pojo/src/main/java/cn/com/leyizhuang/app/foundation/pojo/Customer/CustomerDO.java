package cn.com.leyizhuang.app.foundation.pojo.Customer;

import cn.com.leyizhuang.app.core.constant.AppCustomerLightStatus;
import cn.com.leyizhuang.app.foundation.vo.CityVO;
import cn.com.leyizhuang.app.foundation.vo.CustomerVO;
import cn.com.leyizhuang.app.foundation.vo.EmployeeVO;
import cn.com.leyizhuang.app.foundation.vo.StoreVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Getter
@Setter
@ToString
public class CustomerDO {
    //顾客ID
    private Long cusId;
    //城市
    private CityVO cityId;
    //门店
    private StoreVO storeId;
    //销售顾问
    private EmployeeVO salesConsultId;
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
    //灯号
    private String light;
    //创建类型（APP注册、后台添加）
    private String createType;
    //顾客类型（零售、会员）
    private String customerType;
    //上次签到时间
    private Date lastSignTime;
    //绑定时间
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date bindingTime;

    public static final CustomerDO transform(CustomerVO customerVO) {
        if (null != customerVO) {
            CustomerDO customerDO = new CustomerDO();
            customerDO.setCusId(customerVO.getCusId());
            customerDO.setCityId(customerVO.getCity());
            customerDO.setName(customerVO.getName());
            customerDO.setMobile(customerVO.getMobile());
            customerDO.setBirthday(customerVO.getBirthday());
            customerDO.setStatus(customerVO.getStatus());
            customerDO.setSex(customerDO.getSex());
            customerDO.setPicUrl(customerVO.getPicUrl());
            customerDO.setCreateTime(customerVO.getCreateTime());
            if ("GREEN".equals(customerDO.getLight())) {
                customerDO.setLight(AppCustomerLightStatus.GREEN.getValue());
            } else if ("YELLOW".equals(customerDO.getLight())) {
                customerDO.setLight(AppCustomerLightStatus.YELLOW.getValue());
            } else if ("RED".equals(customerDO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.RED.getValue());
            } else if ("CLOSE".equals(customerDO.getLight())) {
                customerDO.setLight(AppCustomerLightStatus.CLOSE.getValue());
            }
            customerDO.setLight(customerVO.getLight());
            customerDO.setBindingTime(customerVO.getBindingTime());
            customerDO.setCreateType(customerVO.getCreateType());
            customerDO.setIsCashOnDelivery(customerVO.getIsCashOnDelivery());
            customerDO.setLastSignTime(customerVO.getLastSignTime());
            customerDO.setNickName(customerVO.getNickName());
            customerDO.setOpenId(customerVO.getOpenId());
            customerDO.setStoreId(customerVO.getStore());
            customerDO.setSalesConsultId(customerVO.getSalesConsultId());
            return customerDO;
        } else {
            return null;
        }
    }
}
