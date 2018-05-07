package cn.com.leyizhuang.app.foundation.pojo.management.store;

import cn.com.leyizhuang.app.core.constant.StoreInventoryAvailableQtyChangeType;
import cn.com.leyizhuang.app.core.constant.StoreInventoryRealQtyChangeType;
import lombok.*;

import java.util.Date;

/**
 * 门店库存变更信息
 *
 * @author liuh
 * Created on 2018-1-10 10:48
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MaStoreRealInventoryChange {
    //城市id
    private Long cityId;
    // 城市名称
    private String cityName;
    //门店id
    private Long storeId;
    //门店名称
    private String storeName;
    // 门店编码
    private String storeCode;
    //商品id
    private Long gid;
    //商品编码
    private String sku;
    //商品名称
    private String skuName;
    //变更时间
    private Date changeTime;
    //改变数量'
    private Integer changeQty;
    //变更后余量
    private Integer afterChangeQty;
    //相关单号
    private String referenceNumber;
    //改变类型
    private StoreInventoryRealQtyChangeType changeType;
    //改变类型描述
    private String changeTypeDesc;

}
