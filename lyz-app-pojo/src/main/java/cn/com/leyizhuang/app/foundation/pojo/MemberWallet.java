package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author CrazyApeDX
 * Created on 2017/3/24.
 * <section>会员资产信息表</section>
 */
@Getter
@Setter
@ToString
public class MemberWallet extends BaseDO {

    private static final long serialVersionUID = 886328763602198092L;
    //会员主键
    private Long memberId;
    //会员预存款
    private Long balance;
    //会员乐易宝
    private Long treasure;
    public MemberWallet() {
    }
    public MemberWallet(Long memberId, Long balance, Long treasure) {
        this.memberId = memberId;
        this.balance = balance;
        this.treasure = treasure;
    }


}
