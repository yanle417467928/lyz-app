package cn.com.leyizhuang.app.foundation.pojo.management;

import lombok.*;

/**
 * Created by caiyu on 2018/6/16.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaterialChangeDetailLog {
    private Long id;

    private Long gid;

    private String sku;

    private String skuName;

    private Integer qty;

    private Long updateHeadId;
}
