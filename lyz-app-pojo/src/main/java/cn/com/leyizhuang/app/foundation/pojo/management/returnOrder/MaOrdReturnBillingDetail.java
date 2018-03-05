package cn.com.leyizhuang.app.foundation.pojo.management.returnOrder;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 *  退款详情
 * Created by liuh on 2017/12/2.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaOrdReturnBillingDetail {
    private Long id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 退单id
     */
    private Long roid;

    /**
     * 退单号
     */
    private String returnNo;
    /**
     * 退款类型（预存款，支付宝等）
     */
    private String returnPayType;
    /**
     * 退款金额
     */
    private BigDecimal returnMoney;
    /**
     * 退款到账时间
     */
    private Date intoAmountTime;
    /**
     * 第三方回复码
     */
    private String replyCode;
    /**
     * 退款单据号
     */
    private String refundNumber;

}
