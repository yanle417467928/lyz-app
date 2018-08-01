package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

/**
 * 导购账单明细返回类
 * Created by caiyu on 2017/11/18.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SellerBillingDetailResponse {

    //商品总金额
    private Double totalPrice;

    //运费
    private Double freight;

    //会员折扣
    private Double memberDiscount;

    //促销折扣
    private Double promotionDiscount;

    //产品券抵现金额
    private Double productCouponDiscount;

    //优惠券抵现金额
    private Double couponDiscount;

    //应付款
    private Double amountPayable;

    //信用金
    private Double creditMoney;

    //预存款
    private Double preDeposit;
    
    //代收金额
    private Double collectionAmount;

    /**
     * 现金金额
     */
    private Double storeCash;

    /**
     * POS金额
     */
    private Double storePosMoney;


    /**
     *  配送现金
     */
    private Double deliveryCash;

    /**
     * 配送pos
     */
    private Double deliveryPos;

}
