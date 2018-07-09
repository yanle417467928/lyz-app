package cn.com.leyizhuang.app.foundation.pojo.request;

import lombok.*;

/**
 * Created by 12421 on 2018/7/4.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BillPayRequest {

    private static final long serialVersionUID = 6037339634254711069L;
    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 用户身份类型
     */
    private Integer identityType;

    /**
     * 支付订单详情
     */
    private String orderDetails;

    /**
     * 支付预存款金额
     */
    private Double stPreDepositAmount;
    /**
     * 账单号
     */
    private String billNo;
}
