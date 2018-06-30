package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.CouponGetType;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品券
 *
 * @author Richard
 * Created on 2017-09-29 10:00
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerProductCoupon implements Serializable {

    private static final long serialVersionUID = -7523497825553900797L;

    private Long id;

    /**
     * 用户id
     */
    private Long customerId;

    /**
     * 商品Id
     */
    private Long goodsId;

    /**
     *  数量
     */
    private Integer quantity;

    /**
     * 产品券获取途径
     */
    private CouponGetType getType;

    /**
     * 获取时间
     */
    private Date getTime;

    /**
     *  生效开始时间
     */
    private Date effectiveStartTime;

    /**
     * 生效结束时间
     */
    private Date effectiveEndTime;

    /**
     * 是否使用
     */
    private Boolean isUsed;

    /**
     * 使用时间
     */
    private Date useTime;

    /**
     * 使用订单号
     */
    private String useOrderNumber;

    /**
     * 产生券的相关订单号
     */
    private String getOrderNumber;

    /**
     *  购买价格
     */
    private Double buyPrice;

    /**
     * 购买门店id
     */
    private Long storeId;

    /**
     * 购买时归属导购id
     */
    private Long sellerId;

    /**
     * 状态 ：可用、禁用
     */
    private Boolean status;

    /**
     * 禁用时间
     */
    private Date disableTime;

    /**
     * 券订单商品行id
     */
    private Long goodsLineId;

    /**
     * 修改时间
     */
    private Date lastUpdateTime;

    /**
     * 产生赠送此券的原商品 sku
     */
    private String bindSku;
    /**
     * 商品标志（普通、专供）
     */
    private String goodsSign;

    /**
     * 结算价
     */
    private Double settlementPrice;

    /**
     * 经销价
     */
    private Double wholesalePrice;
}
