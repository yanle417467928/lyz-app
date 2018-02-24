package cn.com.leyizhuang.app.foundation.pojo.request;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 创建订单接口入参
 *
 * @author Ricahrd
 * Created on 2017-10-25 9:17
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateParam implements Serializable {

    private static final long serialVersionUID = 2270780578977688132L;

    /**
     * 城市id
     */
    private Long cityId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户身份类型
     */
    private Integer identityType;
    /**
     * 本单顾客id
     */
    private Long customerId;
    /**
     * 备注信息
     */
    private String remark;
    /**
     * 商品信息
     */
    private String goodsInfo;

    /**
     * 产品券商品信息
     */
    private String productCouponInfo;

    /**
     * 促销明细信息
     */
    private String promotionInfo;

    /**
     * 配送信息
     */
    private String deliveryInfo;

    /**
     * 物料审核单号
     */
    private String auditNo;
    /**
     * 现金券id
     */
    private String cashCouponIds;

    /**
     * 账单信息
     */
    private String billingInfo;

    /**
     * 销售纸质单号（后台四川直营门店购买产品券）
     */
    private String salesNumber;

}
