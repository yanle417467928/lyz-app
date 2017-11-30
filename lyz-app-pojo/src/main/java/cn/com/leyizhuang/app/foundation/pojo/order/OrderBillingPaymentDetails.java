package cn.com.leyizhuang.app.foundation.pojo.order;

import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
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

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 支付时间
     */
    private Date payTime;
    /**
     * 支付类型:
     *   支付宝、微信、银联、POS、现金、其它
     */
    private OrderBillingPaymentType payType;
    /**
     * 关联单号
     */
    private String orderNumber;

    /**
     * 金额
     */
    private Double amount;

    /**
     * 第三方支付接口回复码
     */
    private String replyCode;
    /**
     * 收款单号
     */
    private String receiptNumber;

}
