package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

import java.util.Date;

/**
 * 商品业务实体
 *
 * @author Richard
 *         Created on 2017-07-12 17:26
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDO{

    private Long gid;

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
    private Long cId;

    //产品分类名称
    private String categoryName;

    //品牌id
    private Long brdId;

    //品牌名称
    private Long brdName;

    //类型id
    private Long gtid;

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

    //创建时间
    private Date createTime;

}
