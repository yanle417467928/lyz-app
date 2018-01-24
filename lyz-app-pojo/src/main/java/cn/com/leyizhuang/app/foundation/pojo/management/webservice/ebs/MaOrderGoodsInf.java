package cn.com.leyizhuang.app.foundation.pojo.management.webservice.ebs;

import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import cn.com.leyizhuang.app.core.constant.ProductType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 订单商品行
 *
 * @author liuh
 * Created on 2018-01-23 9:32
 **/
@Getter
@Setter
@ToString
public class MaOrderGoodsInf {

    private Long orderLineId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否传输成功
     */
    private AppWhetherFlag sendFlag;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 传输成功时间
     */
    private Date sendTime;

    /**
     * 原主单产品行id
     */
    private Long mainOrderLineId;

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
     * APP单据产品类型
     */
    private ProductType productType;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 零售价
     */
    private Double lsPrice;

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
    private AppWhetherFlag giftFlag;

    /**
     * 促销id
     */
    private String promotionId;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String attribute4;

}
