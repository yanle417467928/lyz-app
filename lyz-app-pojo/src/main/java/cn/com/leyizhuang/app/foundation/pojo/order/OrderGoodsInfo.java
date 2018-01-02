package cn.com.leyizhuang.app.foundation.pojo.order;

import cn.com.leyizhuang.app.core.constant.AppGoodsLineType;
import lombok.*;

/**
 * 订单商品明细
 *
 * @author Richard
 *         Created on 2017-10-10 11:45
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderGoodsInfo implements Cloneable{

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
     * 商品id
     */
    private Long gid;
    /**
     * 商品编码
     */
    private String sku;

    /**
     * 商品名称
     */
    private String skuName;
    /**
     * 零售价
     */
    private Double retailPrice;

    /**
     * 会员价
     */
    private Double VIPPrice;

    /**
     * 结算价
     */
    private Double settlementPrice;

    /**
     * 经销价
     */
    private Double wholesalePrice;

    /**
     * 促销id
     */
    private String promotionId;

    /**
     * 是否参与分摊
     */
    private Boolean isPriceShare;

    /**
     * 促销分摊金额
     */
    private Double promotionSharePrice;

    /**
     * 乐币分摊金额
     */
    private Double lbSharePrice;

    /**
     * 现金券分摊金额
     */
    private Double cashCouponSharePrice;

    /**
     * 现金返利分摊金额
     */
    private Double cashReturnSharePrice;

    /**
     * 退货金额
     */
    private Double returnPrice;

    /**
     * 是否可以退货
     */
    private Boolean isReturnable;
    /**
     * 退货优先级
     */
    private Integer returnPriority;
    /**
     * 订单数量
     */
    private Integer orderQuantity;

    /**
     * 出货数量
     */
    private Integer shippingQuantity;

    /**
     * 已退货数量
     */
    private Integer returnQuantity;

    /**
     * 可退数量
     */
    private Integer returnableQuantity;

    /**
     * 商品行类型
     */
    private AppGoodsLineType goodsLineType;

    /**
     * 价目表行id
     */
    private Long priceItemId;

    /**
     * 公司编码
     */
    private String companyFlag;
    /**
     * 是否评价
     */
    private Boolean isEvaluation;

    /**
     * 浅克隆方法
     * @return
     */
    @Override
    public OrderGoodsInfo clone() {
        OrderGoodsInfo obj = null;
        try{
            obj = (OrderGoodsInfo)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
