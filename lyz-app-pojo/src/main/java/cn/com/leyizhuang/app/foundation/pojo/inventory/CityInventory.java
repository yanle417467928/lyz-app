package cn.com.leyizhuang.app.foundation.pojo.inventory;

import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Calendar;
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
    private String cityCode;
    /**
     * 城市名称
     */
    private String cityName;
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
    private Timestamp lastUpdateTime;

    public static CityInventory transform(GoodsDO goodsDO, City city) {
        CityInventory cityInventory = new CityInventory();
        cityInventory.setAvailableIty(0);
        cityInventory.setCityId(city.getCityId());
        cityInventory.setCityName(city.getName());
        cityInventory.setCityCode(city.getNumber());
        cityInventory.setCreateTime(Calendar.getInstance().getTime());
        cityInventory.setGid(goodsDO.getGid());
        cityInventory.setSku(goodsDO.getSku());
        cityInventory.setSkuName(goodsDO.getSkuName());
        cityInventory.setLastUpdateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        return cityInventory;
    }
}
