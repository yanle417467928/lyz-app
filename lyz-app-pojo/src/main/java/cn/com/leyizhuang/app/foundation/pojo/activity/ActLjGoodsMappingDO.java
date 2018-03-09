package cn.com.leyizhuang.app.foundation.pojo.activity;

import lombok.*;

/**
 * 累积促销产品关联表
 * Created by panjie on 2018/3/8.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActLjGoodsMappingDO {

    private Long id;

    // 促销ID
    private Long actId;

    // 促销代号
    private String actCode;

    // 商品id
    private Long gid;

    // 商品sku
    private String sku;

    // 商品标题
    private String goodsTitile;
}
