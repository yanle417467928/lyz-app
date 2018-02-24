package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

/**
 * @author GenerationRoad
 * @date 2018/2/24
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QuickOrderRelationDO {

    private Long id;
    /**
     * /编码
     */
    private String number;
    /**
     * /商品sku
     */
    private String goodsSku;
    /**
     * /商品名称
     */
    private String goodsName;
}
