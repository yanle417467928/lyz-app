package cn.com.leyizhuang.app.foundation.pojo.management.coupon;

import lombok.*;

import java.math.BigDecimal;

/**
 * 产品劵
 *
 * @author liuh
 * Created on 2017-10-10 11:49
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MaCashCouponInfo {
    private Long id;

    /**
     * 订单号
     */
    private String ordNo;
    /**
     * 退单id
     */
    private Long roid;

    /**
     * 现金券Id
     */
    private Long ccid;

    /**
     * 购买时价值
     */
    private BigDecimal purchasePrice;


    /**
     * 是否已退
     */
    private Boolean isReturn;

}
