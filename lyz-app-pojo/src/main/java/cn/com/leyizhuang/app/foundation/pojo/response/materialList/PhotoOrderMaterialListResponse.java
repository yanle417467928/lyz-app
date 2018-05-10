package cn.com.leyizhuang.app.foundation.pojo.response.materialList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GenerationRoad
 * @date 2018/1/16
 */
@Getter
@Setter
@ToString
public class PhotoOrderMaterialListResponse {
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
     *  拍照订单单号
     */
    private String photoOrderNo;
    /**
     *  商品vip价
     */
    private Double vipPrice;

}
