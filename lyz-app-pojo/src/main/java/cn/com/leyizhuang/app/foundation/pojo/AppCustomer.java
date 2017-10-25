package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.AppCustomerCreateType;
import cn.com.leyizhuang.app.core.constant.AppCustomerLightStatus;
import cn.com.leyizhuang.app.core.constant.AppCustomerType;
import cn.com.leyizhuang.app.core.constant.SexType;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
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
@NoArgsConstructor
@AllArgsConstructor
public class AppCustomer implements Serializable {

    private static final long serialVersionUID = -5749739135096612483L;

    private Long cusId;

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

    //顾客灯号
    private AppCustomerLightStatus light;

    //注册时间
    private LocalDateTime createTime;

    //是否允许货到付款
    private Boolean isCashOnDelivery;

    //顾客创建类型
    private AppCustomerCreateType createType;

    //顾客类型
    private AppCustomerType customerType;


}
