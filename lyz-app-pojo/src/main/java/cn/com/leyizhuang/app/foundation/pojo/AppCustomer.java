package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.SexType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * lyz-app-facade 用户抽象
 *
 * @author Richard
 * Created on 2017-09-19 11:00
 **/
@Getter
@Setter
@ToString
public class AppCustomer implements Serializable {

    private static final long serialVersionUID = -5749739135096612483L;

    private Long id;

    //真实姓名
    private String name;

    //手机号码
    private String mobile;

    //生日
    private Date birthday;

    //状态 禁用，启用
    private Boolean status;

    //性别
    private SexType sex;

    //微信openId
    private String openId;

    //头像路径
    private String picUrl;

    //昵称
    private String nickName;

    //用户所在城市id
    private Long cityId;

    //销售顾问Id
    private Long salesConsultId;

    //门店Id
    private Long storeId;

    public AppCustomer() {
    }

}
