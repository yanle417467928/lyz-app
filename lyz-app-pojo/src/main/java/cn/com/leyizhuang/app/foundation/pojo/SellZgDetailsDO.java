package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

import java.util.Date;

/**
 * 专供产品销量表
 * Created by panjie on 2018/3/9.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SellZgDetailsDO {
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
     * 创建时间
     */
    private Date createTime;

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
     * 顾客id
     */
    private Long customerId;

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
     * 商品id
     */
    private Long goodsId;

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


}
