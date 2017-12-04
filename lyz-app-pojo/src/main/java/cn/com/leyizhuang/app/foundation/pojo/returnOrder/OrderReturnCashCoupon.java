package cn.com.leyizhuang.app.foundation.pojo.returnOrder;

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
public class OrderReturnCashCoupon {
    private Long id;
    /**
     * 退单id
     */
    private Long roid;
    /**
     * 订单号
     */
    private String ordNo;
    /**
     * 现金券id
     */
    private String ccid;
    /**
     * 是否已退
     */
    private Boolean isReturn;
}
