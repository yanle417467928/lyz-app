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

    //商品总价
    private Double totalPrice;
    //运费
    private Double freight;
    //冲账户余额
    private Double memberDiscount;
    //促销折扣
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
    private Double creditMoney;
    //信用金抵扣
    private Double storeCreditMoney;
    //额度使用合计
    private Double totalCreditMoney;

    //会员预存款
    private Double preDeposit;
    //门店现金
    private Double storeCash;
    //门店POS
    private Double storePosMoney;
    //门店其他
    private Double storeOtherMoney;
    //配送现金
    private Double deliveryCash;
    //配送POS
    private Double deliveryPos;
    //客户预存款
    private Double stPreDeposit;
    //门店预存款
    private Double sellerStoreDeposit;
    //支付宝
    private Double alipayMoney;
    //微信
    private Double wechatpayMoney;
    //银联
    private Double unionpayMoney;
    //现金返利
    private Double subvention;
    //实付款合计
    private Double totalPay;
    //是否已结清
    private Boolean isPayUp;

    //代收金额
    private Double collectionAmount;
    //应付款
    private Double amountPayable;
}
