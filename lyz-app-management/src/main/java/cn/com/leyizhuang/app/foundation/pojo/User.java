package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.SexType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    private String loginName;

    private String name;

    private String password;

    private SexType sex;

    private Integer age;

    private String phone;

    private Integer userType;

    private Integer status;

    private Date createTime;


}
