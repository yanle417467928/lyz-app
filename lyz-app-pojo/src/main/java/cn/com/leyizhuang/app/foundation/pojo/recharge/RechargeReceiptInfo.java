package cn.com.leyizhuang.app.foundation.pojo.recharge;

import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.core.constant.PaymentSubjectType;
import cn.com.leyizhuang.app.core.constant.RechargeAccountType;
import cn.com.leyizhuang.app.core.constant.remote.webservice.ebs.ChargeType;
import lombok.*;

import java.util.Date;

/**
 * 充值收款信息
 *
 * @author Richard
 * Created on 2018-01-11 15:40
 **/
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RechargeReceiptInfo {

    private Long id;

    /**
     * 充值单号
     */
    private String rechargeNo;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 支付方式
     */
    private OrderBillingPaymentType payType;

    /**
     * 支付方式描述
     */
    private String payTypeDesc;

    /**
     * 支付主体类型
     */
    private PaymentSubjectType paymentSubjectType;

    /**
     * 支付主体类型描述
     */
    private String paymentSubjectTypeDesc;

    /**
     * 充值账户类型
     */
    private RechargeAccountType rechargeAccountType;

    /**
     * 充值账户类型描述
     */
    private String rechargeAccountTypeDesc;

    /**
     * 充值银行
     */
    private ChargeType chargeType;

    /**
     * 充值银行描述
     */
    private String chargeTypeDesc;
    /**
     * 金额
     */
    private Double amount;

    /**
     * 第三方支付回复码
     */
    private String replyCode;

    /**
     * 收款编号
     */
    private String receiptNumber;
}
