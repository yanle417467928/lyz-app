package cn.com.leyizhuang.app.foundation.pojo.activity;

import lombok.*;

import java.io.Serializable;

/**
 * 参与促销商品实体
 * Created by panjie on 2017/11/22.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActGoodsMappingDO implements Serializable {

    private static final long serialVersionUID;

    static {
        serialVersionUID = 1L;
    }

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
    private String title;

    // 商品数量
    private Integer qty;




}
