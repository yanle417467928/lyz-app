package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.PaymentDataStatus;
import cn.com.leyizhuang.app.core.constant.PaymentDataType;
import lombok.*;

/**
 * @author GenerationRoad
 * @date 2017/11/17
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDataDO {

    private Long id;

    //支付人id
    private Long userId;

    private AppIdentityType appIdentityType;

    //商户订单号
    private String outTradeNo;

    //支付类型
    private PaymentDataType paymentType;

    //异步回调地址
    private String notifyUrl;

    //支付说明
    private String subject;

    //交易流水号
    private String tradeNo;

    //支付金额
    private Double totalFee;

    //支付状态
    private PaymentDataStatus tradeStatus;

    //支付方式
    private String paymentMethod;
    //备注
    private String remarks;

    public PaymentDataDO(Long userId, String outTradeNo, Integer paymentType, String notifyUrl, String subject,
                         Double totalFee, PaymentDataStatus tradeStatus, String paymentMethod, String remarks) {
        this.userId = userId;
        this.outTradeNo = outTradeNo;
        if (null != paymentType && paymentType == 0) {
            this.paymentType = PaymentDataType.ST_PRE_DEPOSIT;
        } else if (null != paymentType && paymentType == 2) {
            this.paymentType = PaymentDataType.DEC_PRE_DEPOSIT;
        } else if (null != paymentType && paymentType == 6) {
            this.paymentType = PaymentDataType.CUS_PRE_DEPOSIT;
        }
        this.appIdentityType = AppIdentityType.getAppIdentityTypeByValue(paymentType);
        this.notifyUrl = notifyUrl;
        this.subject = subject;
        this.totalFee = totalFee;
        this.tradeStatus = tradeStatus;
        this.paymentMethod = paymentMethod;
        this.remarks = remarks;
    }
}
