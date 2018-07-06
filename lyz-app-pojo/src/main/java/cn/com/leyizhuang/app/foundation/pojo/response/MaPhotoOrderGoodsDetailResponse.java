package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

/**
 * @Author Richard
 * @Date 2018/6/26 13:35
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaPhotoOrderGoodsDetailResponse {
    /**
     * 商品id
     */
    private Long gid;

    /**
     * SKU
     */
    private String sku;

    /**
     * 商品名称
     */
    private String skuName;

    /**
     * 商品类型(本品(GOODS)，赠品(GIFTS))
     */
    private String goodsType;

    /**
     * 零售价
     */
    private Double retailPrice;

    /**
     * 会员价
     */
    private Double vipPrice;

    /**
     * 数量
     */
    private Integer qty;
}
