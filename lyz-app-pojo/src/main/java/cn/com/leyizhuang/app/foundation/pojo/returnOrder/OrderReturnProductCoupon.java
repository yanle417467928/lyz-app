package cn.com.leyizhuang.app.foundation.pojo.returnOrder;

import lombok.*;

/**
 * 退产品券
 * Created by caiyu on 2017/12/2.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderReturnProductCoupon {
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
     * 退单编号
     */
    private String returnNo;
    /**
     * 商品id
     */
    private Long gid;
    /**
     * 产品券id
     */
    private Long pcid;
    /**
     *  券数量（默认为1）
     */
    private int qty;
    /**
     * 退券数量
     */
    private int returnQty;
    /**
     * 是否已退
     */
    private Boolean isReturn;
}
