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
public class MemberWalletDO extends BaseDO {

    private static final long serialVersionUID = 886328763602198092L;

    public MemberWalletDO() {
    }

    public MemberWalletDO(Long memberId, Long balance, Long treasure) {
        this.memberId = memberId;
        this.balance = balance;
        this.treasure = treasure;
    }

    private Long memberId;
    private Long balance;
    private Long treasure;


}
