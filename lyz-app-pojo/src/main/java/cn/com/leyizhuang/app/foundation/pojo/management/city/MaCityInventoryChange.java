package cn.com.leyizhuang.app.foundation.pojo.management.city;

import cn.com.leyizhuang.app.core.constant.CityInventoryAvailableQtyChangeType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 城市库存可用量变更日志
 *
 * @author liuh
 * create_time 2017-11-02 14:50:51
 */

@ToString
@Getter
@Setter
public class MaCityInventoryChange {

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
     * 商品id
     */
    private Long gid;
    /**
     *  商品编码
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
     * 变更后数量
     */
    private Integer afterChangeQty;

    /**
     * 修改类型
     */
    private CityInventoryAvailableQtyChangeType changeType;

    /**
     * 修改类型描述
     */
    private String changeTypeDesc;

    /**
     * 相关单号
     */
    private String referenceNumber;
}
