package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GenerationRoad
 * @date 2017/10/19
 */
@Getter
@Setter
@ToString
public class OrderUsableProductCouponResponse {

    //商品名称
    private String skuName;
    //商品规格
    private String goodsSpecification;
    //商品单位名称
    private String goodsUnit;
    //差品卷剩余数量
    private Integer leftNumber;
    //商品封面图片
    private String coverImageUri;
    //产品卷可使用数量
    private Integer usableNumber;
}
