package cn.com.leyizhuang.app.foundation.pojo.deliveryFeeRule;

import lombok.*;

/**
 * 运费规则关联特殊商品，此范围商品 不计算运费
 * Created by panjie on 2018/1/12.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryFeeRuleSpecailGoods {

    private Long id;

    /**
     * 规则id
     */
    private Long ruleId;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品sku
     */
    private String sku;
}
