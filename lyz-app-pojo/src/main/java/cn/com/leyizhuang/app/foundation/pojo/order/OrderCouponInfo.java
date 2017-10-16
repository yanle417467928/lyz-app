package cn.com.leyizhuang.app.foundation.pojo.order;

import lombok.*;

/**
 * 订单用券明细
 *
 * @author Richard
 * Created on 2017-10-10 11:49
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderCouponInfo {

    private Long id;

    //订单号
    private String orderNumber;

    //券类型: 产品券、现金券
    private String couponType;

    //券id
    private Long couponId;

    //券购买时价值
    private Double purchasePrice;

    //券使用时价值
    private Double costPrice;
}