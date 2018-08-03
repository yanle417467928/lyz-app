package cn.com.leyizhuang.app.foundation.pojo.reportDownload;

import lombok.*;

/**
 * @Author Richard
 * @Date 2018/7/18 17:04
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CusProductCouponChangeLogReportDO {

    /**
     * 城市
     */
    public String city;

    /**
     * 门店
     */
    public String store;

    /**
     * 导购
     */
    public String seller;

    /**
     * 顾客
     */
    public String customer;

    /**
     * 获取券订单号
     */
    public String getOrderNumber;

    /**
     * 商品名称
     */
    public String skuName;

    /**
     * 商品编码
     */
    public String sku;

    /**
     * 变更类型
     */
    public String changeType;

    /**
     * 变更类型描述
     */
    public String changeTypeDesc;

    /**
     * 获取时间
     */
    public String getTime;

    /**
     * 变更时间
     */
    public String changeTime;

    /**
     * 变更相关单号
     */
    public String changeNumber;

    /**
     * 购买价格
     */
    public Double buyPrice;

    /**
     * 是否使用
     */
    public String isUse;

    /**
     * 券状态（可用、禁用）
     */
    public String status;

    /**
     * 变更数量
     */
    public Integer changeQty;

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

    /**
     * 是否是专供
     */
    public String isZG;
}
