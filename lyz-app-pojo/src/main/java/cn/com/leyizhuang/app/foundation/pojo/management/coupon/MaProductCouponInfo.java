package cn.com.leyizhuang.app.foundation.pojo.management.coupon;

import cn.com.leyizhuang.app.core.constant.CouponGetType;
import cn.com.leyizhuang.app.core.constant.OrderCouponType;
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
public class MaProductCouponInfo {


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
     * 订单号
     */
    private String returnNo;

    /**
     * 商品id
     */
    private Long gid;

    /**
     * 券id
     */
    private Long pcid;

    /**
     * 商品编码
     */
    private String sku;

    /**
     *
     */
    private Integer qty;

    /**
     * 购买时价值
     */
    private BigDecimal purchasePrice;
    /**
     * 退货数量
     */
    private Integer returnQty;

    /**
     * 是否已退
     */
    private Boolean isReturn;

}
