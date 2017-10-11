package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 商品业务实体
 *
 * @author Richard
 *         Created on 2017-07-12 17:26
 **/
@Getter
@Setter
@ToString
public class GoodsDO{

    private Long id;

    //商品名称
    private String skuName;

    //商品编码
    private String sku;

    //封面图片
    private String coverImageUri;

    //轮播展示图片，多张图片以,隔开
    private String rotationImageUri;

    //商品规格
    private  String goodsSpecification;

    //单位名称
    private String goodsUnit;

    //产品分类id
    private Long categoryId;

    //产品分类名称
    private String categoryName;

    //品牌id
    private Long brandId;

    //品牌名称
    private Long brandName;

    //类型id
    private Long typeId;

    //类型名称
    private Long typeName;

    //是否首页推荐
    private Boolean isRecommendIndex;

    //是否热销
    private Boolean isHot;

    //是否新品
    private Boolean isNew;

    //排序号
    private Long sortId;

    //是否调色产品
    private Boolean isColorMixing;

}
