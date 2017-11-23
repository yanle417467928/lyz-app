package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 配送员收款方式
 * @author GenerationRoad
 * @date 2017/11/16
 */
@Setter
@Getter
@ToString
public class PaymentMethodResponse {
    //支付方式
    private String method;
}
