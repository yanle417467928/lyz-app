package cn.com.leyizhuang.app.foundation.pojo.order;

import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.core.constant.PaymentSubjectType;
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
     * 顾客预存款、门店预存款、支付宝、微信、银联、POS、现金、其它
     */
    private OrderBillingPaymentType payType;
    /**
     * 支付类型说明
     */
    private String payTypeDesc;
    /**
     * 关联单号
     */
    private String orderNumber;
    /**
     * 支付人id
     */
    private Long paymentSubjectId;
    /**
     * 支付主体类型
     */
    private PaymentSubjectType paymentSubjectType;
    /**
     * 支付主体类型说明
     */
    private String paymentSubjectTypeDesc;
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

    /**
     * 产生订单收款记录
     *
     * @param payType       支付类型
     * @param amount        金额
     * @param subjectType   支付主体类型
     * @param orderNumber   订单号
     * @param receiptNumber 收款号
     */
    public void generateOrderBillingPaymentDetails(OrderBillingPaymentType payType, Double amount, PaymentSubjectType subjectType,
                                                   String orderNumber, String receiptNumber) {
        this.setPayType(payType);
        this.setPayTypeDesc(payType.getDescription());
        this.setAmount(amount);
        this.setOrderNumber(orderNumber);
        this.setCreateTime(new Date());
        this.setPayTime(this.getCreateTime());
        this.setReceiptNumber(receiptNumber);
        this.setPaymentSubjectType(subjectType);
        this.setPaymentSubjectTypeDesc(subjectType.getDescription());
    }

    public OrderBillingPaymentDetails(Date createTime, Long orderId, Date payTime, OrderBillingPaymentType payType, String payTypeDesc,
                                      String orderNumber, PaymentSubjectType paymentSubjectType, String paymentSubjectTypeDesc, Double amount,
                                      String replyCode, String receiptNumber){
        this.createTime = createTime;
        this.orderId = orderId;
        this.payTime = payTime;
        this.payType = payType;
        this.payTypeDesc = payTypeDesc;
        this.orderNumber = orderNumber;
        this.paymentSubjectType = paymentSubjectType;
        this.paymentSubjectTypeDesc = paymentSubjectTypeDesc;
        this.amount = amount;
        this.replyCode = replyCode;
        this.receiptNumber = receiptNumber;
    }

}
