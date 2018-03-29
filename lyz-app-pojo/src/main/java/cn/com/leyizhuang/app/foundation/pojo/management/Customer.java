package cn.com.leyizhuang.app.foundation.pojo.management;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 顾客
 *
 * @author liuh
 * Created on 2017-11-03 14:14
 **/

@Getter
@Setter
@ToString
public class Customer {

    //顾客ID
    private Long cusId;
    //城市id
    private Long cityId;
    //门店id
    private Long storeId;
    //销售顾问id
    private Long salesConsultId;
    //真实姓名
    private String name;
    //手机号码
    private String mobile;
    //生日
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    //状态：禁用、启用
    private Boolean status;
    //性别：男、女
    private String sex;
    //微信openid
    private String openId;
    //头像路径
    private String picUrl;
    //昵称
    private String nickName;

    //是否货到付款
    private Boolean isCashOnDelivery;
    //创建时间
    private Date createTime;
    //灯号
    private String light;
    //创建类型（APP注册、后台添加）
    private String createType;
    //顾客类型（零售、会员）
    private String customerType;
    //上次签到时间
    private Date lastSignTime;
    //绑定时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bindingTime;
}
