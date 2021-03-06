package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * 确认订单页面简单商品信息响应
 *
 * @author Jerry.Ren
 * Date: 2017/11/1.
 * Time: 15:53.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderGoodsSimpleResponse implements Serializable {

    /**
     * 商品ID
     */
    private Long id;

    /**
     * sku
     */
    private String sku;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 封面图片
     */
    private String coverImageUri;

    /**
     * 商品规格
     */
    private String goodsSpecification;

    /**
     * 单位名称
     */
    private String goodsUnit;

    /**
     * 会员价
     */
    private Double vipPrice;

    /**
     * 零售价
     */
    private Double retailPrice;

    /**
     * 二级分类编码id
     */
    private Long categoryId;

    /**
     * 商品品牌分类编码id
     */
    private Long brandId;

    /**
     * 商品类型分类编码 id
     */
    private Long typeId;
    /**
     * 商品行类型(GOODS 本品,PRESENT 赠品, PRODUCT_COUPON 产品券)
     */
    private String goodsLineType;

    /**
     * 商品数量
     */
    private Integer goodsQty = 0;

    /**
     * 公司标识
     */
    private String companyFlag;

}
