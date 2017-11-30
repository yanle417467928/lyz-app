package cn.com.leyizhuang.app.foundation.pojo.inventory;

import cn.com.leyizhuang.app.core.constant.StoreInventoryAvailableQtyChangeType;
import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;


/**
 * 门店库存 可用量变更日志
 *
 * @author Richard
 * create_time 2017-11-02 14:55:54
 */

@ToString
@Getter
@Setter
public class StoreInventoryAvailableQtyChangeLog{

    private Long id;
    /**
     * 城市id
     */
    private Long cityId;
    /**
     * 城市名称
     */
    private String cityName;
    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 门店名称
     */
    private String storeName;
    /**
     * 门店编码
     */
    private String storeCode;
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
     * 变更时间
     */
    private Date changeTime;
    /**
     * 变更数量
     */
    private Integer changeQty;
    /**
     * 相关单号
     */
    private String referenceNumber;
    /**
     * 变更后数量
     */
    private Integer afterChangeQty;
    /**
     * 变更类型
     */
    private StoreInventoryAvailableQtyChangeType changeType;

    /**
     * 变更类型描述
     */
    private String changeTypeDesc;


}
