package cn.com.leyizhuang.app.foundation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GenerationRoad
 * @date 2018/3/20
 */
@Getter
@Setter
@ToString
public class CreditBillingDTO {
    private Long id;
    private Double amount;
    private String paymentType;
    private String bankCode;
}
