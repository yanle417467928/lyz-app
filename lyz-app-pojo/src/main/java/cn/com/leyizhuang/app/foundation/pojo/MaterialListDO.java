package cn.com.leyizhuang.app.foundation.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 下料清单
 * @author GenerationRoad
 * @date 2017/10/17
 */
@Setter
@Getter
@ToString
public class MaterialListDO {
    private Long id;
    //用户id
    private Long userId;
    //身份类型
    private Integer identityType;
    //商品编码
    private String sku;
    //商品名称
    private String skuName;
    //商品规格
    private String goodsSpecification;
    //封面图片路径
    private String coverImageUri;
    //商品单位
    private String goodsUnit;
    //商品数量
    private Integer qty;
}
