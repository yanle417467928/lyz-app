package cn.com.leyizhuang.app.foundation.pojo.request.management;

import cn.com.leyizhuang.app.foundation.pojo.request.settlement.PromotionSimpleInfo;
import lombok.*;

/**
 * Created by caiyu on 2018/6/8.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaCreateOrderRequest {
    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 用户身份类型
     */
    private Integer identityType;
    /**
     * 代下单客户id
     */
    private Long customerId;
    /**
     * 传输的商品本品集合
     */
    private String goodsList;
    /**
     * 收货地址信息
     */
    private String deliveryMsg;
    /**
     * 收款信息
     */
    private String billingMsg;
    /**
     * 系统判定配送方式
     */
    private String sysDeliveryType;
    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 城市id
     */
    private Long cityId;
    /**
     * 备注
     */
    private String remark;

    //来源
    private String source;

    //促销信息
    private String PromotionInfo;
}
