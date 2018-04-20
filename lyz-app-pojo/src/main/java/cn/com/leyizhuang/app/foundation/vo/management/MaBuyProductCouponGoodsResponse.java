package cn.com.leyizhuang.app.foundation.vo.management;

import lombok.*;

/**
 * 后台购买产品券选择商品返回类
 * Created by caiyu on 2018/1/23.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaBuyProductCouponGoodsResponse {
    /**
     * 商品id
     */
    private Long gid;

    /**
     * 商品名称
     */
    private String skuName;

    /**
     * 商品编码
     */
    private String sku;

    /**
     * 商品规格
     */
    private String goodsSpecification;

    /**
     * 品牌名称
     */
    private String brdName;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     *  商品公司标识
     */
    private String companyFlag;

    /**
     * 商品零售价
     */
    private Double retailPrice;

    /**
     * 商品会员价
     */
    private Double vipPrice;

    /**
     * 价目表类型
     */
    private String priceType;
}
