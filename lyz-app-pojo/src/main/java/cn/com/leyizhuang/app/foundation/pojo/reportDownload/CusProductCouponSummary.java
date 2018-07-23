package cn.com.leyizhuang.app.foundation.pojo.reportDownload;

import lombok.*;

/**
 * @Author Richard
 * @Date 2018/7/18 11:58
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CusProductCouponSummary {
    /**
     * 城市
     */
    public String city;

    /**
     * 门店
     */
    public String storeName;

    /**
     * 导购
     */
    public String sellerName;

    /**
     * 顾客
     */
    public String cusName;

    /**
     * 商品名称
     */
    public String skuName;

    /**
     * 商品编码
     */
    public String sku;

    /**
     * 未提货数量
     */
    public Integer notPickedUp;

    /**
     * 提货数量
     */
    public Integer pickedUp;

    /**
     * 退货数量
     */
    public Integer returnQty;

    /**
     * 一级分类
     */
    public String classifyOne;

    /**
     * 二级分类
     */
    public String classifyTow;

    /**
     * 品牌
     */
    public String brand;

    /**
     * 规格
     */
    public String specification;

    /**
     * 类型
     */
    public String type;
}
