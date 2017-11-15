package cn.com.leyizhuang.app.foundation.pojo.inventory;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 商店库存
 * @author liuh
 * create_time 2017-11-02 14:55:54
 */

@ToString
@Getter
@Setter
public class StoreInventory {

    private Long id;
    // 城市id
    private Long cityId;
    // 城市编码
    private String cityCode;
    // 城市名称
    private String cityName;
    // 门店id
    private Long storeId;
    // 门店编码
    private String storeCode;
    // 门店名称
    private String storeName;
    // 商品id
    private Long gid;
    // 商品编码
    private String sku;
    // 商品名称
    private String skuName;
    // 可售门店库存
    private Integer availableIty;
    // 真实门店库存
    private Integer realIty;

}
