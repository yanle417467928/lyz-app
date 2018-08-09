package cn.com.leyizhuang.app.foundation.pojo.management.order;

import cn.com.leyizhuang.app.core.constant.FreightChangeType;
import lombok.*;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 订单运费变更类
 * @author liuh
 * @date 2018/1/15
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderFreightChange {
    private Long id;
    //订单id
    private Long orderId;
    //修改人
    private String  modifier;
    //修改时间
    private Date modifyTime;
    //运费修改前价格
    private BigDecimal freight;
    //运费改变后价格
    private BigDecimal freightChangeAfter;
    //运费改变量
    private BigDecimal changeAmount;
    //修改原因
    private String modifyReason;
    //变更类型
    private String changeType;

    public void setChangeTypes(FreightChangeType changeTypes){
        this.changeType = changeTypes.getDescription();
    }
}
