package cn.com.leyizhuang.app.foundation.pojo.rest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 员工登录接口返回对象
 *
 * @author Richard
 * Created on 2017-09-19 13:57
 **/
@Getter
@Setter
@ToString
public class EmployeeLoginResponse implements Serializable{
    //导购身份类型
    private int type;

    public EmployeeLoginResponse(int type) {
        this.type = type;
    }
}
