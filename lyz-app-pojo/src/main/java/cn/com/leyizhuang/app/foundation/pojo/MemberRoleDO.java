package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author CrazyApeDX
 *         Created on 2017/3/24.
 */
@Getter
@Setter
@ToString
public class MemberRoleDO extends BaseDO {

    private static final long serialVersionUID = -7689232560379303914L;

    public MemberRoleDO() {
        super();
    }

    public MemberRoleDO(String title, Boolean isDefault) {
        super();
        this.title = title;
        this.isDefault = isDefault;
    }

    private String title;
    private Boolean isDefault = Boolean.FALSE;


}
