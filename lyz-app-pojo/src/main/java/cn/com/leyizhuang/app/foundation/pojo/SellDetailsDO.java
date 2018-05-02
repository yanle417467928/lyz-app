package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.AppGoodsLineType;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.AppOrderSubjectType;
import lombok.*;

import java.util.Date;

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
     * 创建时间
     */
    private Date createTime;

    /**
     * 真实出货/反配上架时间
     */
    private Date realTime;

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


    /**
     * 订单下单主体类型，装饰公司、门店
     */
    private AppOrderSubjectType orderSubjectType;

    /**
     * 下单人身份类型
     */
    private AppIdentityType creatorIdentityType;

    /**
     * 下单人id
     */
    private Long creatorId;

    /**
     * 下单人姓名
     */
    private String creatorName;

    /**
     * 订单标志： 0：下单，1：退单
     */
    private Integer sellDetalsFlag;

    /**
     * 公司编码
     */
    private String companyFlag;

    /**
     * 商品行类型
     */
    private AppGoodsLineType goodsLineType;
}
