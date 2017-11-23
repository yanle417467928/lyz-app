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
     * 商品信息
     */
    private String goodsInfo;
    /**
     * 配送信息
     */
    private String deliveryInfo;
    /**
     * 乐币数量
     */
    private Integer leBiQuantity;
    /**
     * 现金券id
     */
    private List<Long> cashCouponIds;
    /**
     * 产品券信息
     */
    private String productCouponInfo;
    /**
     * 账单信息
     */
    private String billingInfo;

}
