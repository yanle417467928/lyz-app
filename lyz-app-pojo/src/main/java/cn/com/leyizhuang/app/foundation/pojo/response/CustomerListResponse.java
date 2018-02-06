package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Created by Jerry.Ren
 * Date: 2017/10/8.
 * Time: 15:57.
 */
@Getter
@Setter
@ToString
public class CustomerListResponse implements Serializable {

    private Long cusId;
    //真实姓名
    private String name;

    //手机号码
    private String mobile;

    //头像路径
    private String picUrl;

    //用户创建时间
    private String createTime;

    //用户灯号
    private String light;

    //顾客身份类型
    private String customerType;

    /**
     * 顾客工种
     */
    private String profession;

    public CustomerListResponse() {
    }

    public static CustomerListResponse transform(AppCustomer appCustomer) {

        CustomerListResponse customer = new CustomerListResponse();

        customer.setCusId(appCustomer.getCusId());
        customer.setName(appCustomer.getName());
        customer.setPicUrl(appCustomer.getPicUrl());
        customer.setMobile(appCustomer.getMobile());
        if (appCustomer.getLight() != null) {
            customer.setLight(appCustomer.getLight().getValue());
        }
        if (appCustomer.getCustomerType() != null) {
            customer.setCustomerType(appCustomer.getCustomerType().getValue());
        }
        if (null != appCustomer.getCreateTime()) {
            String time = appCustomer.getCreateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            customer.setCreateTime(time);
        }
        customer.setProfession(appCustomer.getCustomerProfessionDesc());
        return customer;
    }

    public static List<CustomerListResponse> transform(List<AppCustomer> appCustomerList) {

        List<CustomerListResponse> customerListResponses;

        if (!appCustomerList.isEmpty() && appCustomerList.size() > 0) {
            customerListResponses = new ArrayList<>(appCustomerList.size());
            appCustomerList.forEach(appCustomer -> customerListResponses.add(transform(appCustomer)));
        } else {
            customerListResponses = new ArrayList<>(0);
        }

        return customerListResponses;
    }
}



