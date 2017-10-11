package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GenerationRoad
 * @date 2017/9/29
 */
@Setter
@Getter
@ToString
public class ProcessGoodsResponse {
    //商品ID
    private Long id;
    //商品名称
    private String skuName;
    //封面图片
    private String coverImageUri;
    //商品规格
    private  String goodsSpecification;
    //单位名称
    private String goodsUnit;
    //会员价
    private Double memberPrice;
    //零售价
    private Double retailPrice;
    //二级分类编码
    private String categoryCode;
    //商品品牌分类编码
    private String brandCode;
    //商品类型分类编码
    private String typeCode;

}
