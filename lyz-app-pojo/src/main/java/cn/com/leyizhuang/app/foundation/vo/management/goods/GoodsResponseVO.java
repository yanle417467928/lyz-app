package cn.com.leyizhuang.app.foundation.vo.management.goods;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GenerationRoad
 * @date 2018/1/30
 */

@Getter
@Setter
@ToString
public class GoodsResponseVO {
    //商品ID
    private Long id;

    //商品名称
    private String goodsName;

    //商品编码
    private String sku;

    //封面图片
    private String coverImageUri;

    //商品规格
    private String goodsSpecification;

    //单位名称
    private String goodsUnit;

    //会员价
    private Double vipPrice;

    //零售价
    private Double retailPrice;

    //二级分类
    private String categoryName;

    //商品品牌分类编码id
    private Long brandId;

    //商品类型
    private String typeName;
}
