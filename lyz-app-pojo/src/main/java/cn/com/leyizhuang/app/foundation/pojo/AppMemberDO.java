package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.IdentityType;
import cn.com.leyizhuang.app.core.constant.RegistryType;
import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author CrazyApeDX
 *         Created on 2017/3/24.
 */
@Getter
@Setter
@ToString
public class AppMemberDO extends BaseDO {

    private static final long serialVersionUID = -6463709968307792496L;

    public AppMemberDO() {
    }

    public AppMemberDO(Store store, Area area, Manager manager, AppMemberLevelDO level,
                       AppMemberRoleDO role, AppMemberInfoDO info, AppMemberAuthDO auth,
                       AppMemberWalletDO wallet, AppMemberSpecDO spec, AppMemberTempDO temp,
                       Date lastLoginTime, Date registryTime, RegistryType registryType,
                       IdentityType identityType) {
        this.store = store;
        this.area = area;
        this.manager = manager;
        this.level = level;
        this.role = role;
        this.info = info;
        this.auth = auth;
        this.wallet = wallet;
        this.spec = spec;
        this.temp = temp;
        this.lastLoginTime = lastLoginTime;
        this.registryTime = registryTime;
        this.registryType = registryType;
        this.identityType = identityType;
    }

    private Store store;
    private Area area;
    private Manager manager;
    private AppMemberLevelDO level;
    private AppMemberRoleDO role;
    private AppMemberInfoDO info;
    private AppMemberAuthDO auth;
    private AppMemberWalletDO wallet;
    private AppMemberSpecDO spec;
    private AppMemberTempDO temp;
    private Date lastLoginTime;
    private Date registryTime;
    private RegistryType registryType;
    private IdentityType identityType;


}
