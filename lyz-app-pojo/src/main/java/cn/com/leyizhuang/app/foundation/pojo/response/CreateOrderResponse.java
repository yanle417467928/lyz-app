package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

import java.io.Serializable;

/**
 * 创建订单接口返回参数
 *
 * @author Richard
 * Created on 2017-12-04 13:57
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderResponse implements Serializable {

    private static final long serialVersionUID = -1113453594351872095L;
    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 应付金额
     */
    private Double amountPayable;
    /**
     * 是否已付清
     */
    private Boolean isPayUp;
}
