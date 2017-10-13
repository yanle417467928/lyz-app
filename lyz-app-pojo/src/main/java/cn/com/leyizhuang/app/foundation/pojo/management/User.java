package cn.com.leyizhuang.app.foundation.pojo.management;

import cn.com.leyizhuang.app.core.constant.SexType;
import cn.com.leyizhuang.app.foundation.vo.UserVO;
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

    private String password;

    private String salt;

    private SexType sex;

    private Integer age;

    private String phone;
    //1：超级管理员 2：普通用户
    private Integer userType;

    private Boolean status;

    private Date createTime;

    public String getCredentialsSalt() {
        return loginName + salt;
    }

    public UserVO convert2UserVO(){
        UserVO userVO = new UserVO();
        userVO.setLoginName(loginName);
        userVO.setName(name);
        userVO.setId(id);
        userVO.setPhone(phone);
        userVO.setAge(age);
        userVO.setSex(sex);
        userVO.setStatus(status);
        userVO.setUserType(userType);
        return userVO;
    }

}
