package cn.com.leyizhuang.app.foundation.vo.management.order;

import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import lombok.*;

import java.util.Date;

/**
 * 后台订单支付明细返回类
 * Created by caiyu on 2017/12/29.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaOrderBillingPaymentDetailResponse {
    /**
     * 支付时间
     */
    private Date payTime;
    /**
     * 支付类型
     */
    private String paymentType;
    /**
     * 支付金额
     */
    private Double amount;

    public void setPaymentType(OrderBillingPaymentType paymentType) {
        this.paymentType = paymentType.getDescription();
    }
}
