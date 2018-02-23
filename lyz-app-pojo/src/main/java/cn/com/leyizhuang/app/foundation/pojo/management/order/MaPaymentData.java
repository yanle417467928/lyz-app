package cn.com.leyizhuang.app.foundation.pojo.management.order;

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
 * @author liuh
 * @date 2018/02/06
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MaPaymentData {

    private Long id;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


    /**
     * 支付状态
     */
    private PaymentDataStatus tradeStatus;

    /**
     * 支付类型
     */
    private PaymentDataType paymentType;

    /**
     * 支付金额
     */
    private Double totalFee;

    /**
     * 支付方式
     */
    private OnlinePayType onlinePayType;

}
