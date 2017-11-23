package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.IdentityType;
import cn.com.leyizhuang.app.core.constant.RegistrationType;
import cn.com.leyizhuang.app.core.constant.SexType;
import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author CrazyApeDX
 * Created on 2017/3/24.
 * <section>会员信息主表</section>
 */
@Getter
@Setter
@ToString
public class Member extends BaseDO {

    private static final long serialVersionUID = -6463709968307792496L;

    //会员姓名
    private String memberName;

    //会员性别
    private SexType sex;

    //会员生日
    private Date birthday;

    //会员的归属门店ID
    private Long storeId;

    //会员的销售顾问ID
    private Long consultId;

    /*//会员鉴权信息ID
    private Long authId;*/

    //会员头像路径
    private String headImageUri;

    //会员有效消费额
    private Long effectiveConsumption;

    //会员有效单量
    private Integer effectiveOrderCount;

    //上次登录时间
    private Date lastLoginTime;

    //用户当前是否已登录
    private Boolean isLogin;

    //注册时间
    private Date registrationTime;

    //注册途径
    private RegistrationType registrationType;

    //会员性质:会员，准会员（零售）
    private IdentityType identityType;


    /**
     * 空构造方法
     */
    public Member() {
    }

    public Member(String memberName, SexType sex, Date birthday, Long storeId, Long consultId, String headImageUri, Long effectiveConsumption, Integer effectiveOrderCount, Date lastLoginTime, Boolean isLogin, Date registrationTime, RegistrationType registrationType, IdentityType identityType) {
        this.memberName = memberName;
        this.sex = sex;
        this.birthday = birthday;
        this.storeId = storeId;
        this.consultId = consultId;
        this.headImageUri = headImageUri;
        this.effectiveConsumption = effectiveConsumption;
        this.effectiveOrderCount = effectiveOrderCount;
        this.lastLoginTime = lastLoginTime;
        this.isLogin = isLogin;
        this.registrationTime = registrationTime;
        this.registrationType = registrationType;
        this.identityType = identityType;
    }
}
