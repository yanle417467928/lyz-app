package cn.com.leyizhuang.app.foundation.pojo.response.materialList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *  从产品券转入的下料清单
 * @author GenerationRoad
 * @date 2017/10/25
 */
@Getter
@Setter
@ToString
public class CouponMaterialListResponse {

    /**
     * 顾客id
     */
    private Long cusId;

    /**
     * 下料清单id
     */
    private Long id;
    /**
     *  商品id
     */
    private Long goodsId;
    /**
     * 商品名称
     */
    private String skuName;
    /**
     * 商品规格
     */
    private String goodsSpecification;
    /**
     * 封面图片路径
     */
    private String coverImageUri;
    /**
     * 商品单位
     */
    private String goodsUnit;
    /**
     * 商品数量
     */
    private Integer qty;
    /**
     *  商品单价
     */
    private Double retailPrice;

    /**
     *  商品vip价
     */
    private Double vipPrice;


}
