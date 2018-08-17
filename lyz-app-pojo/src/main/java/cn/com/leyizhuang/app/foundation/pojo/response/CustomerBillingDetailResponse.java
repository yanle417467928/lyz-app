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

    //商品总价
    private Double totalPrice;
    //运费
    private Double freight;
    //冲账户余额
    private Double memberDiscount;
    //促销抵扣
    private Double promotionDiscount;
    //产品券抵扣
    private Double productCouponDiscount;
    //优惠券抵扣
    private Double couponDiscount;
    //订单总价
    private Double orderAmountSubtotal;

    //上楼费
    private Double upstairsFee;

    //信用额度抵扣
    private Double empCreditMoney;
    //额度使用合计
    private Double totalCreditMoney;

    //会员预存款
    private Double PreDeposit;
    //门店POS
    private Double posMoney;
    //门店现金
    private Double cashMoney;
    //门店其他
    private Double storeOtherMoney;
    //门店预存款
    private Double stPreDeposit;
    //配送现金
    private Double deliveryCash;
    //配送pos
    private Double deliveryPos;
    //支付宝
    private Double alipayMoney;
    //微信
    private Double wechatpayMoney;
    //银联
    private Double unionpayMoney;
    //实付款合计
    private Double totalPay;
    //是否已结清
    private Boolean isPayUp;

    //应付款
    private Double amountPayable;
    //乐币抵现金额
    private Double leBiCashDiscount;
    //代付金额
    private Double payForAnotherMoney;
    //代付方式
    private String payType;
    //代收金额
    private Double collectionAmount;
}
