package cn.com.leyizhuang.app.foundation.pojo.management.order;

import lombok.*;

/**
 * Created by caiyu on 2018/1/24.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaActGoodsMapping {
    /**
     * 促销ID
     */
    private Long actId;

    /**
     * 促销代号
     */
    private String actCode;

    /**
     * 商品id
     */
    private Long gid;

    /**
     * 商品sku
     */
    private String sku;

    /**
     * 商品标题
     */
    private String goodsTitile;

    /**
     * 商品数量
     */
    private Integer qty;

    /**
     * 会员价
     */
    private Double vipPrice;

    /**
     * 零售价
     */
    private Double retailPrice;
}
