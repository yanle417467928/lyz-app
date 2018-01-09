package cn.com.leyizhuang.app.foundation.vo.management.goodscategory;

import lombok.*;

/**
 * 订单商品详情返回类
 * Created by caiyu on 2017/12/28.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MaOrderGoodsDetailResponse {
    /**
     * 商品编码
     */
    private String sku;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 数量
     */
    private int qty;
    /**
     * 单价
     */
    private Double unitPrice;
    /**
     * 小计金额
     */
    private Double subTotalPrice;
    /**
     * 实付金额（分摊金额）
     */
    private Double realPayment;
    /**
     * 商品类型（正品、赠品、产品券）
     */
    private String goodsType;
}
