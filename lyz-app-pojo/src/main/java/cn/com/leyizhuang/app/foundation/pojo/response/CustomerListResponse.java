package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.foundation.pojo.AppCustomer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
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

    public CustomerListResponse() {
    }

    public static CustomerListResponse transform(AppCustomer appCustomer){

        CustomerListResponse customerList = new CustomerListResponse();

        customerList.setName(appCustomer.getName());
        customerList.setPicUrl(appCustomer.getPicUrl());
        customerList.setMobile(appCustomer.getMobile());
        customerList.setLight(appCustomer.getLight().getValue());
        if (null != appCustomer.getCreateTime()){
            String time = appCustomer.getCreateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            customerList.setCreateTime(time);
        }
        return customerList;
    }

    public static List<CustomerListResponse> transform(List<AppCustomer> appCustomerList){

        List<CustomerListResponse> customerListResponses;

        if (appCustomerList.size() > 0 && !appCustomerList.isEmpty()){
            customerListResponses = new ArrayList<>(appCustomerList.size());
            appCustomerList.forEach(appCustomer -> customerListResponses.add(transform(appCustomer)));
        }else {
            customerListResponses = new ArrayList<>(0);
        }

        return customerListResponses;
    }
}


