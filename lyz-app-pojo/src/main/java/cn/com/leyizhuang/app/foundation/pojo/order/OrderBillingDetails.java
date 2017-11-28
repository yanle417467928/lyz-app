package cn.com.leyizhuang.app.foundation.pojo.order;

import cn.com.leyizhuang.app.core.constant.OnlinePayType;
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

    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 订单商品总金额
     */
    private Double orderAmount;

    /**
     * 会员折扣
     */
    private Double memberDiscount;

    /**
     * 订单折扣（促销立减）
     */
    private Double promotionDiscount;

    /**
     * 运费
     */
    private Double freight;

    /**
     * 上楼费
     */
    private Double upstairsFee;

    /**
     * 乐币数量
     */
    private Integer lebiQuantity;

    /**
     * 乐币抵现金额
     */
    private Double lebiCashDiscount;


    /**
     * 优惠券抵现金额
     */
    private Double cashCouponDiscount;

    /**
     * 产品券抵现金额
     */
    private Double productCouponDiscount;

    /**
     * 顾客预存款
     */
    private Double cusPreDeposit;

    /**
     * 第三方支付方式
     */
    private OnlinePayType onlinePayType;

    /**
     * 第三方支付金额
     */
    private Double onlinePayAmount;

    /**
     * 门店(装饰公司)预存款
     */
    private Double stPreDeposit;

    /**
     * 导购信用额度
     */
    private Double empCreditMoney;

    /**
     * 门店(装饰公司)信用额度
     */
    private Double storeCreditMoney;

    /**
     * 门店（装饰公司）现金返利
     */
    private Double storeSubvention;


    /**
     * 订单金额小计
     */
    private Double orderAmountSubtotal;

    /**
     * 应付款
     */
    private Double amountPayable;

    /**
     * 代收金额
     */
    private Double collectionAmount;

    /**
     * 欠款
     */
    private Double arrearage;

    /**
     * 是否业主收货
     */
    private Boolean isOwnerReceiving;

}
