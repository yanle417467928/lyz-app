package cn.com.leyizhuang.app.foundation.pojo.returnorder;

import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import lombok.*;

import java.util.Date;

/**
 * 退款明细
 *
 * @author caiyu
 * @date 2017/12/2
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReturnOrderBillingDetail {

    private Long id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 退款单id
     */
    private Long roid;

    /**
     * 退单号
     */
    private String returnNo;
    /**
     * 退款类型
     */
    private OrderBillingPaymentType returnPayType;
    /**
     * 退款金额
     */
    private Double returnMoney;
    /**
     * 退款到账时间
     */
    private Date intoAmountTime;
    /**
     * 第三方回复码
     */
    private String replyCode;
    /**
     * 退款单据号
     */
    private String refundNumber;

    public ReturnOrderBillingDetail(String tradeNo, Date now) {
        this.replyCode = tradeNo;
        this.intoAmountTime = now;
    }
}
