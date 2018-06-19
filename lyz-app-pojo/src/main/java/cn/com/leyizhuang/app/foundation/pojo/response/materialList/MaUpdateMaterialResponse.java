package cn.com.leyizhuang.app.foundation.pojo.response.materialList;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.MaterialListType;
import lombok.*;
import sun.rmi.runtime.Log;

/**
 * Created by caiyu on 2018/6/15.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaUpdateMaterialResponse {
    private Long userId;

    private AppIdentityType identityType;

    private Long gid;

    private String sku;

    private Double retailPrice;

    private Integer qty;

    private String typeName;

    private String skuName;

    private MaterialListType materialListType;

    private String isGenerateOrder;
}
