package cn.com.leyizhuang.app.foundation.dto;

import cn.com.leyizhuang.app.core.constant.CustomerPreDepositChangeType;
import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * @author GenerationRoad
 * @date 2018/1/10
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CusPreDepositDTO {
    /**
     * 客户id
     */
    private Long cusId;
    /**
     * 变更类型
     */
    private CustomerPreDepositChangeType changeType;

    /**
     * 支付类型:
     * 顾客预存款、门店预存款、支付宝、微信、银联、POS、现金、其它
     */
    private OrderBillingPaymentType payType;

    /**
     * 变更金额
     */
    private Double changeMoney;
    /**
     * 到账时间
     */
    private String transferTime;
    /**
     * 商户订单号
     */
    @Length(min = 0, max = 6, message = "请输入6位的'商户订单号'")
    private String merchantOrderNumber;
    /**
     * 备注
     */
    @Length(min = 0, max = 50, message = "'备注'的长度必须在0~50字之间")
    private String remarks;

    //提现类型（银行清单）
    private String bankCode;

}
