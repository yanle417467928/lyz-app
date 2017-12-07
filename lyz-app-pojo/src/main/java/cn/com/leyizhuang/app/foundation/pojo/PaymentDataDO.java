package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.OnlinePayType;
import cn.com.leyizhuang.app.core.constant.PaymentDataStatus;
import cn.com.leyizhuang.app.core.constant.PaymentDataType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 第三方支付信息类
 *
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

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 支付人id
     */
    private Long userId;

    /**
     * 支付人身份类型
     */
    private AppIdentityType appIdentityType;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 支付状态
     */
    private PaymentDataStatus tradeStatus;

    /**
     * 支付类型
     */
    private PaymentDataType paymentType;
    /**
     * 支付类型说明
     */
    private String paymentTypeDesc;

    /**
     * 支付金额
     */
    private Double totalFee;

    /**
     * 支付方式
     */
    private OnlinePayType onlinePayType;

    /**
     * 异步回调地址
     */
    private String notifyUrl;

    /**
     * 异步回调时间
     */
    private Date notifyTime;

    /**
     * 交易流水号
     */
    private String tradeNo;

    /**
     * 备注
     */
    private String remarks;


    public PaymentDataDO(Long userId, String outTradeNo, Integer paymentType, String notifyUrl, String paymentTypeDesc,
                         Double totalFee, PaymentDataStatus tradeStatus, OnlinePayType onlinePayType, String remarks) {
        this.userId = userId;
        this.outTradeNo = outTradeNo;
        if (outTradeNo.contains("CZ_")) {
            if (null != paymentType && paymentType == 0) {
                this.paymentType = PaymentDataType.ST_PRE_DEPOSIT;
            } else if (null != paymentType && paymentType == 2) {
                this.paymentType = PaymentDataType.DEC_PRE_DEPOSIT;
            } else if (null != paymentType && paymentType == 6) {
                this.paymentType = PaymentDataType.CUS_PRE_DEPOSIT;
            }
        } else {
            this.paymentType = PaymentDataType.ORDER;
        }
        this.appIdentityType = AppIdentityType.getAppIdentityTypeByValue(paymentType);
        this.notifyUrl = notifyUrl;
        this.paymentTypeDesc = paymentTypeDesc;
        this.totalFee = totalFee;
        this.tradeStatus = tradeStatus;
        this.onlinePayType = onlinePayType;
        this.remarks = remarks;
        this.createTime = LocalDateTime.now();
    }

    public PaymentDataDO(Long userId, String outTradeNo, Integer paymentType, String notifyUrl,
                         Double totalFee, PaymentDataStatus tradeStatus, OnlinePayType onlinePayType, String remarks) {
        this.userId = userId;
        this.outTradeNo = outTradeNo;
        if (outTradeNo.contains("CZ_")) {
            if (null != paymentType && paymentType == 0) {
                this.paymentType = PaymentDataType.ST_PRE_DEPOSIT;
            } else if (null != paymentType && paymentType == 2) {
                this.paymentType = PaymentDataType.DEC_PRE_DEPOSIT;
            } else if (null != paymentType && paymentType == 6) {
                this.paymentType = PaymentDataType.CUS_PRE_DEPOSIT;
            }
        } else {
            this.paymentType = PaymentDataType.ORDER;
        }
        this.appIdentityType = AppIdentityType.getAppIdentityTypeByValue(paymentType);
        this.notifyUrl = notifyUrl;
        this.paymentTypeDesc = this.paymentType.getDescription();
        this.totalFee = totalFee;
        this.tradeStatus = tradeStatus;
        this.onlinePayType = onlinePayType;
        this.remarks = remarks;
        this.createTime = LocalDateTime.now();
    }
}
