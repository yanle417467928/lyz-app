package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 订单商品行
 *
 * @author Richard
 * Created on 2018-01-03 9:32
 **/
@Getter
@Setter
@ToString
public class OrderGoodsInf {

    private Long id;

    /**
     * 原主单产品行id
     */
    private Long orderLineId;

    /**
     * 分单头id
     */
    private Long orderHeaderId;

    /**
     * 主单号
     */
    private String mainOrderNumber;

    /**
     * 分单号
     */
    private String orderNumber;

    /**
     * 商品编码
     */
    private String sku;

    /**
     * 商品名称
     */
    private String goodsTitle;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 零售价
     */
    private Double retailPrice;

    /**
     * 会员价
     */
    private Double hyPrice;

    /**
     * 经销价
     */
    private Double jxPrice;

    /**
     * 结算价
     */
    private Double settlementPrice;

    /**
     * 退货价
     */
    private Double returnPrice;

    /**
     * 乐币折扣
     */
    private Double lebiDiscount;

    /**
     * 现金返利折扣
     */
    private Double subventionDiscount;

    /**
     * 优惠券折扣
     */
    private Double cashCouponDiscount;

    /**
     * 促销折扣
     */
    private Double promotionDiscount;

    /**
     * 折扣总额
     */
    private Double discountTotalPrice;

    /**
     * 赠品标识
     */
    private String giftFlag;

    /**
     * 促销id
     */
    private Long promotionId;

}
