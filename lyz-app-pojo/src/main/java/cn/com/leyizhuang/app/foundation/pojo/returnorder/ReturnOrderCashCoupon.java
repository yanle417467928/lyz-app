package cn.com.leyizhuang.app.foundation.pojo.returnorder;

import lombok.*;

/**
 * 退现金券
 * Created by caiyu on 2017/12/2.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReturnOrderCashCoupon {
    private Long id;
    /**
     * 退单id
     */
    private Long roid;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 现金券id
     */
    private Long ccid;
    /**
     * 购买时价格
     */
    private Double purchasePrice;
    /**
     * 是否已退
     */
    private Boolean isReturn;
}
