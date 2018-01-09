package cn.com.leyizhuang.app.foundation.pojo.management.order;

import lombok.*;

/**
 * 简化订单账款明细
 *
 * @author liuh
 * Created on 2017-12-26 11:27
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SimpleOrderBillingDetails {

    private Long id;

    /**
     * 订单id
     */
    private Long oid;
    /**
     * 运费
     */
    private Double freight;
}
