package cn.com.leyizhuang.app.foundation.pojo.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@AllArgsConstructor
public class CustomerBindingSellerResponse implements Serializable{

    //openId是否存在
    private Boolean isExist;
    //用户id
    private String guideName;

    private String guideStore;
}
