package cn.com.leyizhuang.app.foundation.pojo.recharge;

import cn.com.leyizhuang.app.core.constant.*;
import lombok.*;

import java.util.Date;

/**
 * 充值单
 *
 * @author Richard
 * Created on 2018-01-11 13:44
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RechargeOrder {

    private Long id;

    /**
     * 充值单号
     */
    private String rechargeNo;
    /**
     * 充值单状态
     */
    private AppRechargeOrderStatus status;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建者身份类型
     */
    private AppIdentityType creatorIdentityType;

    /**
     * 创建者id
     */
    private Long creatorId;

    /**
     * 充值账户类型
     */
    private RechargeAccountType rechargeAccountType;

    /**
     * 充值账户类型描述
     */
    private String rechargeAccountTypeDesc;

    /**
     * 顾客id
     */
    private Long customerId;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 充值金额
     */
    private Double amount;

    /**
     * 支付方式
     */
    private OrderBillingPaymentType payType;

    /**
     * 支付方式描述
     */
    private  String payTypeDesc;

    /**
     * 支付主体
     */
    private PaymentSubjectType paymentSubjectType;

    /**
     * 支付主体描述
     */
    private String paymentSubjectTypeDesc;

    /**
     * 付清时间
     */
    private Date payUpTime;
}
