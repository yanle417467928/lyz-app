package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GenerationRoad
 * @date 2017/10/25
 */
@Getter
@Setter
@ToString
public class MaterialListResponse {

    private Long id;
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
    //商品单价
    private Double retailPrice;

}
