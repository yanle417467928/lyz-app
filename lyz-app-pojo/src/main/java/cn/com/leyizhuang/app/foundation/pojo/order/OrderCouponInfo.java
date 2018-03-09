package cn.com.leyizhuang.app.foundation.pojo.order;

import cn.com.leyizhuang.app.core.constant.OrderCouponType;
import cn.com.leyizhuang.app.core.constant.CouponGetType;
import cn.com.leyizhuang.app.core.constant.ProductType;
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

    /**
     * 订单id
     */
    private Long oid;
    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 券类型: 产品券、现金券
     */
    private OrderCouponType couponType;

    /**
     * 券id
     */
    private Long couponId;

    /**
     * 券购买时价值
     */
    private Double purchasePrice;

    /**
     * 券使用时价值
     */
    private Double costPrice;

    /**
     * 券获取方式
     */
    private CouponGetType getType;

    /**
     * 商品编码（仅当券类型是产品券时这个属性才有值）
     */
    private String sku;

    /**
     * 商品id
     */
    private Long goodsId;

}
