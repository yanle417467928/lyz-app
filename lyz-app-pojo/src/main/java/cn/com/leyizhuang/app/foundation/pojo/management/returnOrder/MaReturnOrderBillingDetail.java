package cn.com.leyizhuang.app.foundation.pojo.management.returnOrder;

import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import lombok.*;

import java.util.Date;

/**
 *  退款总表
 * Created by caiyu on 2017/12/2.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaReturnOrderBillingDetail {

    private Long id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 退款类型
     */
    private OrderBillingPaymentType returnPayType;
    /**
     * 退款金额
     */
    private Double returnMoney;




}
