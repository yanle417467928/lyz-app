package cn.com.leyizhuang.app.foundation.pojo.inventory;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 城市库存
 *
 * @author Richard
 *
 * create_time 2017-11-02 14:50:51
 */

@ToString
@Getter
@Setter
public class CityInventory {

    private Long id;
    /**
     * 城市id
     */
    private Long cityId;
    /**
     * 城市编码
     */
    private String city_code;
    /**
     * 城市名称
     */
    private String city_name;
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
    private String sku_name;
    /**
     * 可售库存
     */
    private Integer availableIty;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 上次更新时间
     */
    private Date lastUpdateTime;
}
