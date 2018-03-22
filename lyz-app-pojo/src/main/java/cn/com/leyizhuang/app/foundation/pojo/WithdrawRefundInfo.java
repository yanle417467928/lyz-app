package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.core.constant.PaymentSubjectType;
import cn.com.leyizhuang.app.core.constant.RechargeAccountType;
import lombok.*;

import java.util.Date;

/**
 * 提现退款
 *
 * @author Richard
 * Created on 2018-02-28 17:17
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawRefundInfo {

    private Long id;

    /**
     * 提现单号
     */
    private String withdrawNo;

    private Date createTime;

    /**
     * 提现到账渠道
     */
    private OrderBillingPaymentType withdrawChannel;

    /**
     * 提现到账渠道说明
     */
    private String withdrawChannelDesc;

    /**
     * 提现主体类型
     */
    private PaymentSubjectType withdrawSubjectType;

    /**
     * 提现主体类型描述
     */
    private String withdrawSubjectTypeDesc;

    /**
     * 提现金额
     */
    private Double withdrawAmount;
    /**
     * 第三方支付渠道回复码
     */
    private String replyCode;

    /**
     * 提现单号
     */
    private String refundNumber;

    /**
     * 提出账号类型
     */
    private RechargeAccountType withdrawAccountType;
    /**
     * 提出账号类型描述
     */
    private String withdrawAccountTypeDesc;

    //提现类型（银行清单）
    private String withdrawType;

}
