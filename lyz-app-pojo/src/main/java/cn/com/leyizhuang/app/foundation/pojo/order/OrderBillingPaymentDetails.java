package cn.com.leyizhuang.app.foundation.pojo.order;

import lombok.*;

import java.util.Date;

/**
 * 订单账款支付明细
 *
 * @author Richard
 * Created on 2017-10-10 11:35
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderBillingPaymentDetails {

    private Long id;

    //货币类型:虚拟货币、实际货币
    private String currencyType;

    //支付时间
    private Date payTime;

    /**
     * 支付类型: 导购信用额度、信用金、现金返利、门店预存款、
     * 会员预存款、支付宝、微信、银联、POS、现金
     */
    private String payType;

    //关联单号
    private String orderNumber;

    //金额
    private Double amount;

    //第三方支付接口回复码
    private String replyCode;
}
