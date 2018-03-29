package cn.com.leyizhuang.app.foundation.vo.management.customer;


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
 * 顾客VO
 *
 * @author liuh
 * Created on 2017-11-03 14:14
 **/

@Getter
@Setter
@ToString

public class CustomerVO {
    //顾客ID
    private Long cusId;
    //门店id
    private SimpleStoreParam store;
    //真实姓名
    private String name;
    //手机号码
    private String mobile;
    //状态：禁用、启用
    private Boolean status;
    //灯号:绿灯 黄灯 红灯 熄灯
    private AppCustomerLightStatus light;


    public static final CustomerVO transform(CustomerDO customerDO) {
        if (null != customerDO) {
            CustomerVO customerVO = new CustomerVO();
            customerVO.setCusId(customerDO.getCusId());
            customerVO.setName(customerDO.getName());
            customerVO.setMobile(customerDO.getMobile());
            customerVO.setStatus(customerDO.getStatus());
            customerVO.setLight(customerDO.getLight());
            customerVO.setStore(customerDO.getStoreId());
            return customerVO;
        } else {
            return null;
        }
    }

    public static final List<CustomerVO> transform(List<CustomerDO> customerList) {
        List<CustomerVO> customerVOList;
        if (null != customerList && customerList.size() > 0) {
            customerVOList = new ArrayList<>(customerList.size());
            customerList.forEach(customerDO -> customerVOList.add(transform(customerDO)));
        } else {
            customerVOList = new ArrayList<>(0);
        }
        return customerVOList;
    }
}
