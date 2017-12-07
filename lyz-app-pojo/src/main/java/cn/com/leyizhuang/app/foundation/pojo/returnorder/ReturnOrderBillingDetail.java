package cn.com.leyizhuang.app.foundation.pojo.returnorder;

import cn.com.leyizhuang.app.core.constant.OnlinePayType;
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
public class ReturnOrderBillingDetail {
    private Long id;
    /**
     * 退款单id
     */
    private Long roid;
    /**
     * 退款类型（ ALIPAY(0, "支付宝"), WE_CHAT(1, "微信"), UNION_PAY(2, "银联"), NO(3, "无");）
     */
    private OnlinePayType returnPayType;
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
