package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author CrazyApeDX
 *         Created on 2017/3/24.
 * <section>会员角色信息表</section>
 */
@Getter
@Setter
@ToString
public class MemberRoleDO extends BaseDO {

    private static final long serialVersionUID = -7689232560379303914L;

    public MemberRoleDO() {
        super();
    }

    public MemberRoleDO(String title) {
        super();
        this.title = title;
    }
    //会员角色名称
    private String title;


}
