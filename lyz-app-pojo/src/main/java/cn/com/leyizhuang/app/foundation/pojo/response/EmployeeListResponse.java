package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.foundation.pojo.AppEmployee;
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
 * Time: 16:10.
 */
@Getter
@Setter
@ToString
public class EmployeeListResponse implements Serializable {

    private Long empId;
    //真实姓名
    private String name;

    //手机号码
    private String mobile;

    //头像路径
    private String picUrl;

    //用户创建时间
    private String createTime;

    public EmployeeListResponse() {
    }

    public static EmployeeListResponse transform(AppEmployee appEmployee){
        EmployeeListResponse response = new EmployeeListResponse();
        response.setEmpId(appEmployee.getEmpId());
        response.setName(appEmployee.getName());
        response.setMobile(appEmployee.getMobile());
        response.setPicUrl(appEmployee.getPicUrl());
        if (null != appEmployee.getCreateTime()){
            String time = appEmployee.getCreateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            response.setCreateTime(time);
        }
        return response;
    }

    public static List<EmployeeListResponse> transform(List<AppEmployee> appEmployeeList){

        List<EmployeeListResponse> listResponses;
        if (appEmployeeList.size() > 0 && !appEmployeeList.isEmpty()){
            listResponses = new ArrayList<>(appEmployeeList.size());
            appEmployeeList.forEach(appEmployee -> listResponses.add(transform(appEmployee)));
        }else {
            listResponses = new ArrayList<>(0);
        }
        return listResponses;
    }
}
