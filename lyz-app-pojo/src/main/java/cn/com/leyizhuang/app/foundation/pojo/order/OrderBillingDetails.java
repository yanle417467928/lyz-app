package cn.com.leyizhuang.app.foundation.pojo.order;

import lombok.*;

/**
 * 订单账款明细
 *
 * @author Richard
 * Created on 2017-10-10 11:27
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderBillingDetails {

    private Long id;

    //订单号
    private String orderNumber;

    //是否业主收货
    private Boolean isOwnerReceiving;

    //订单金额小计
    private Double orderAmount;

    //会员折扣
    private Double memberDiscount;

    //促销折扣
    private Double promotionDiscount;

    //运费
    private Double freight;

    //上楼费
    private Double upstairsFee;

    //乐币数量
    private Integer lebiQuantity;

    //乐币抵现金额
    private Double leBiCashDiscount;

    //优惠券抵现金额
    private Double couponDiscount;

    //产品券抵现金额
    private Double productCouponDiscount;

    //订单金额小计
    private Double orderAmountSubtotal;

    //应付款
    private Double amountPayable;

    //代收金额
    private Double collectionAmount;

    //欠款
    private Double arrearage;
    //现金返利
    private Double subvention;
    //信用金
    private Double creditMoney;
}
