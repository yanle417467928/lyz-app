package cn.com.leyizhuang.app.foundation.vo.management.city;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2017/12/28.
 * Time: 14:06.
 */
@Getter
@Setter
@ToString
public class CityInventoryVO {

    /**
     * 城市ID
     */
    private Long cityId;
    /**
     * 城市名称（唯一）
     */
    private String name;
    /**
     * 城市编码（唯一）
     */
    private String code;
    /**
     * 城市可用库存量
     */
    private Integer availableIty;
    /**
     * 商品编码
     */
    private String sku;
    /**
     * 商品名称
     */
    private String skuName;
}
