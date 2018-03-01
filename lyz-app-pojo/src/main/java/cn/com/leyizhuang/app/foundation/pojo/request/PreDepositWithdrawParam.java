package cn.com.leyizhuang.app.foundation.pojo.request;

import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import lombok.*;

/**
 * 预存款提现参数类
 * Created by panjie on 2018/2/6.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PreDepositWithdrawParam {

    /**
     * id
     */
    private Long id;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 真实电话
     */
    private String realPhone;

    /**
     * 帐号类型
     */
    private OrderBillingPaymentType accountType;

    /**
     * 帐号
     */
    private String account;

    /**
     * 提现金额
     */
    private Double amount;

}
