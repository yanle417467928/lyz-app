package cn.com.leyizhuang.app.foundation.pojo.bill;

import cn.com.leyizhuang.app.core.constant.OnlinePayType;
import cn.com.leyizhuang.app.core.constant.PaymentDataStatus;
import cn.com.leyizhuang.app.core.constant.PaymentDataType;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 账单支付信息类
 *
 * @author liuh
 * @date 2018/02/06
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BillPaymentData {


    /**
     * 账单号
     */
    private String billNo;


    /**
     * 预存款
     */
    private Double storePreDeposit;

    /**
     * 线上支付金额
     */
    private Double onlinePayFee;

    /**
     * 线上支付方式
     */
    private OnlinePayType onlinePayType;

}
