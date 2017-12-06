package cn.com.leyizhuang.app.foundation.vo;

import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/6
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class GoodsVO {

    //商品ID
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
    private String goodsSpecification;

    //单位名称
    private String goodsUnit;

    //产品分类id
    private Long categoryId;

    //品牌id
    private Long brandId;

    //类型id
    private Long typeId;

    //会员价
    private Double vipPrice;

    //零售价
    private Double retailPrice;


}
