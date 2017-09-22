package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.AppConstant;
import cn.com.leyizhuang.app.core.constant.AppUserType;
import cn.com.leyizhuang.app.core.constant.SexType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * lyz-app-facade用户抽象
 *
 * @author Richard
 * Created on 2017-09-19 11:00
 **/
@Getter
@Setter
@ToString
public class AppUser implements Serializable {

    private static final long serialVersionUID = -5749739135096612483L;

    private Long id;

    //登录名
    private String loginName;
    //真实姓名
    private String name;
    //用户密码
    private String password;
    //盐
    private String salt;
    //app用户身份类型
    private AppUserType userType;

    private String mobile;

    private Date birthday;

    private Boolean status;

    private SexType sex;

    public String generateSalt(){
        return DigestUtils.md5Hex(loginName + AppConstant.APP_USER_SALT);
    }

}
