package cn.com.leyizhuang.app.foundation.pojo.management.store;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Date;


/**
 * 商店库存
 *
 * @author liuh
 * create_time 2018-1-15 14:19:50
 */

@ToString
@Getter
@Setter
public class  MaStoreInventory {

    private Long id;
    /**
     * 城市id
     */
    private Long cityId;
    /**
     * 城市编码
     */
    private String cityCode;
    /**
     * 城市名称
     */
    private String cityName;
    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 门店编码
     */
    private String storeCode;
    /**
     * 门店名称
     */
    private String storeName;
    /**
     * 商品id
     */
    private Long gid;
    /**
     * 商品编码
     */
    private String sku;
    /**
     * 商品名称
     */
    private String skuName;
    /**
     * 可售门店库存
     */
    private Integer availableIty;
    /**
     * 真实门店库存
     */
    private Integer realIty;

    /**
     * 上次修改时间
     */
    private Timestamp lastUpdateTime;

    /**
     * 创建时间
     */
    private Date createTime;
}
