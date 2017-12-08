package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

import java.io.Serializable;

/**
 * @author Jerry.Ren
 * Notes: 退单返回商品详情实体
 * Created with IntelliJ IDEA.
 * Date: 2017/12/7.
 * Time: 10:48.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReturnOrderGoodsResponse implements Serializable {

    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 商品编码
     */
    private String sku;
    /**
     * 商品名称
     */
    private String skuName;
    /**
     * 封面图片路径
     */
    private String coverImageUri;
    /**
     * 订单数量
     */
    private Integer orderQuantity;
    /**
     * 可退数量
     */
    private Integer returnableQuantity;
    /**
     * 退货优先级
     */
    private Integer returnPriority;
    /**
     * /商品单价
     */
    private Double retailPrice;
    /**
     * 退货金额
     */
    private Double returnPrice;
    /**
     * 是否赠品
     */
    private Boolean isGift;
    /**
     * 促销id
     */
    private String promotionId;
    /**
     * 促销标题
     */
    private String promotionTitle;


}
