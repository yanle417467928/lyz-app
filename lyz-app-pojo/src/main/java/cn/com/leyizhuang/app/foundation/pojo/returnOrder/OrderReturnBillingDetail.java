package cn.com.leyizhuang.app.foundation.pojo.returnOrder;

import lombok.*;

import java.util.Date;

/**
 * 退款明细
 * Created by caiyu on 2017/12/2.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderReturnBillingDetail {
    private Long id;
    /**
     * 退款单id
     */
    private Long roid;
    /**
     * 退款类型（预存款，支付宝等）
     */
    private String returnPayType;
    /**
     * 退款金额
     */
    private Double returnMoney;
    /**
     * 退款到账时间
     */
    private Date intoAmountTime;
    /**
     * 币种
     */
    private String currencyType;
    /**
     * 第三方回复码
     */
    private String replyCode;
    /**
     * 退款单据号
     */
    private String refundNumber;
}
