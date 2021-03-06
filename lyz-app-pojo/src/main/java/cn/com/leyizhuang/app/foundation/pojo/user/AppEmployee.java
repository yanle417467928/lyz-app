package cn.com.leyizhuang.app.foundation.pojo.user;

import cn.com.leyizhuang.app.core.constant.AppConstant;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.AppSellerType;
import cn.com.leyizhuang.app.core.constant.SexType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDateTime;
import java.util.Date;


/**
 * 员工
 *
 * @author Richard
 * Created on 2017-09-23 9:04
 **/
@Getter
@Setter
@ToString
public class AppEmployee {

    private Long empId;

    //员工登录名
    private String loginName;

    //真实姓名
    private String name;

    //密码
    private String password;

    //盐
    private String salt;

    //app员工身份类型
    private AppIdentityType identityType;

    //导购类型
    private AppSellerType sellerType;

    //员工手机号
    private String mobile;

    //生日
    private Date birthday;

    //状态
    private Boolean status;

    //性别
    private SexType sex;

    //头像路径
    private String picUrl;

    //归属经理id
    private Long managerId;

    //员工所在城市id
    private Long cityId;

    //员工所属门店id
    private Long storeId;

    //配送员编码
    private String deliveryClerkNo;

    //注册时间
    private LocalDateTime createTime;

    /**
     * 二维码
     */
    private String qrCode;



    public AppEmployee() {
    }

    public String generateSalt() {
        return DigestUtils.md5Hex(loginName + AppConstant.APP_USER_SALT);
    }

}
