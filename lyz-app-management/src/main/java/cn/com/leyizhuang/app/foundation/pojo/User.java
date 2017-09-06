package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.SexType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 后台用户
 *
 * @author Richard
 * Created on 2017-07-28 11:24
 **/
@Getter
@Setter
@ToString
public class User implements Serializable {
    private static final long serialVersionUID = -3170398616740331337L;

    private Long id;

    @NotNull(message = "登录名不允许为空")
    @Length(max = 20,min = 2,message = "登录名长度必须在2-20之间")
    private String loginName;

    @NotNull(message = "姓名不允许为空")
    @Length(max = 20,min = 2,message = "姓名长度必须在2-20之间")
    private String name;

    @Length(max = 20,min = 2,message = "密码长度必须在2-20之间")
    private String password;

    private SexType sex;

    private Integer age;

    private String phone;
    //1：超级管理员 2：普通用户
    private Integer userType;

    private Integer status;

    private Date createTime;


}
