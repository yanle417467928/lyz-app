package cn.com.leyizhuang.app.foundation.pojo.activity;

import lombok.*;

import java.io.Serializable;

/**
 * 促销加价购商品实体
 * Created by panjie on 2017/11/22.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActAddSaleDO implements Serializable{
    private static final long serialVersionUID;

    static {
        serialVersionUID = 1L;
    }

    private Long id;

    // 促销ID
    private Long actId;

    // 促销代号
    private String actCode;

    // 赠品id
    private Long goodsId;

    // 赠品sku
    private String goodsSku;

    // 赠品标题
    private String goodsTitle;


}
