package cn.com.leyizhuang.app.foundation.pojo.request.settlement;

import lombok.*;

/**
 * Created by caiyu on 2018/6/8.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MaGoodsSimpleInfo {
    /**
     * 商品sku
     */
    private String sku;

    /**
     * 商品数量
     */
    private Integer qty;
    /**
     * 商品行类型(GOODS 本品,PRESENT 赠品, PRODUCT_COUPON 产品券)
     */
    private String goodsLineType;
}
