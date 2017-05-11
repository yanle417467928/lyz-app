package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.IdentityType;
import cn.com.leyizhuang.app.core.constant.RegistryType;
import cn.com.leyizhuang.app.core.constant.Sex;
import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author CrazyApeDX
 *         Created on 2017/3/24.
 * <section>会员信息主表</section>
 */
@Getter
@Setter
@ToString
public class MemberDO extends BaseDO {

    private static final long serialVersionUID = -6463709968307792496L;

    //会员的归属门店
    private Store store;
    //会员的客服经理
    private Manager manager;
    //会员等级
    private MemberLevelDO level;
    //会员角色
    private MemberRoleDO role;
    //会员身份鉴权信息
    private MemberAuthDO auth;
    //会员资产账户
    private MemberWalletDO wallet;
    //会员特定属性
    private MemberSpecificPropertyDO specProp;
    //会员姓名
    private String name;
    //会员头像路径
    private String headImageUri;
    //会员生日
    private Date birthday;
    //会员性别
    private Sex sex;
    //会员有效消费额
    private Long effectiveConsumption;
    //会员有效单量
    private Integer effectiveOrderCount;
    //上次登录时间
    private Date lastLoginTime;
    //注册时间
    private Date registryTime;
    //注册途径
    private RegistryType registryType;
    //会员性质:会员，准会员（零售）
    private IdentityType identityType;

    /**
     * 空构造方法
     */
    public MemberDO() {
    }

    /**
     * 自定义构造方法
     * @param store
     * @param manager
     * @param level
     * @param role
     * @param auth
     * @param wallet
     * @param specProp
     * @param name
     * @param headImageUri
     * @param birthday
     * @param sex
     * @param effectiveConsumption
     * @param effectiveOrderCount
     * @param lastLoginTime
     * @param registryTime
     * @param registryType
     * @param identityType
     */
    public MemberDO(Store store, Manager manager, MemberLevelDO level, MemberRoleDO role, MemberAuthDO auth, MemberWalletDO wallet, MemberSpecificPropertyDO specProp, String name, String headImageUri, Date birthday, Sex sex, Long effectiveConsumption, Integer effectiveOrderCount, Date lastLoginTime, Date registryTime, RegistryType registryType, IdentityType identityType) {
        this.store = store;
        this.manager = manager;
        this.level = level;
        this.role = role;
        this.auth = auth;
        this.wallet = wallet;
        this.specProp = specProp;
        this.name = name;
        this.headImageUri = headImageUri;
        this.birthday = birthday;
        this.sex = sex;
        this.effectiveConsumption = effectiveConsumption;
        this.effectiveOrderCount = effectiveOrderCount;
        this.lastLoginTime = lastLoginTime;
        this.registryTime = registryTime;
        this.registryType = registryType;
        this.identityType = identityType;
    }
}
