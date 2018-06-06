package cn.com.leyizhuang.app.foundation.pojo.management.order;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 后台订单收款明细
 *
 * @author liuh
 * Created on 2018-1-18
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MaOrderBillingPaymentDetails {

    private Long id;
   //订单id
    private Long oid;
    //订单号
    private String ordNo;
    //创建时间
    private Date createTime;
    //支付时间
    private String payTime;
    //收款类型
    private OrderBillingPaymentType payType;
    //支付类型说明
    private String payTypeDesc;
    //支付人id
    private Long paymentSubjectId;
    //付款主体类型
    private AppIdentityType paymentSubjectType;
    //付款主体类型说明
    private String paymentSubjectTypeDesc;
    //金额
    private BigDecimal amount;
    //第三方支付回复码
    private String replyCode;
    //收款单号
    private String receiptNumber;
}
