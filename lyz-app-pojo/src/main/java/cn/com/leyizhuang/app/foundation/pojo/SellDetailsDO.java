package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

/**
 * 销售明细
 * Created by panjie on 2018/1/24.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SellDetailsDO {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 明细行Id
     */
    private Long goodsLineId;

    /**
     * 分公司id
     */
    private Long companyId;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 城市id
     */
    private Long cityId;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 导购Id
     */
    private Long sellerId;

    /**
     * 导购名称
     */
    private String sellerName;

    /**
     * 顾客电话
     */
    private String customerPhone;

    /**
     * 顾客姓名
     */
    private String customerName;

    /**
     * 订单号
     */
    private String number;

    /**
     * 商品档次
     */
    private String goodsGrade;

    /**
     * 编码
     */
    private String sku;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 金额
     */
    private Double Amount;

    /**
     * 单品奖励金额
     */
    private Double singleRewards;
}
