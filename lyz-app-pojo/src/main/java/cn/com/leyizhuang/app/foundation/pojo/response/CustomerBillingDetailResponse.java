package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

/**
 * 顾客账单明细返回类
 * Created by caiyu on 2017/11/18.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerBillingDetailResponse {

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

    //乐币抵现金额
    private Double leBiCashDiscount;

    //应付款
    private Double amountPayable;

    //预存款
    private Double PreDeposit;

    //代付金额
    private Double payForAnotherMoney;

    //代付方式
    private String payType;
    //代收金额
    private Double collectionAmount;

    //pos金额
    private Double posMoney;

    //现金金额
    private Double cashMoney;

}
