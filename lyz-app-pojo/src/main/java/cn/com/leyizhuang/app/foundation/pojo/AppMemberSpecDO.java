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
public class AppMemberSpecDO extends BaseDO {

    private static final long serialVersionUID = 4271481571572555629L;

    public AppMemberSpecDO() {
    }

    public AppMemberSpecDO(Long memberId, Long effectiveConsumption, Integer effectiveOrderCount) {
        this.memberId = memberId;
        this.effectiveConsumption = effectiveConsumption;
        this.effectiveOrderCount = effectiveOrderCount;
    }

    private Long memberId;
    private Long effectiveConsumption;
    private Integer effectiveOrderCount;


}
