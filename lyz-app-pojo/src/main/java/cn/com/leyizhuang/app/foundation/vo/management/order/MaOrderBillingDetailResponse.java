package cn.com.leyizhuang.app.foundation.vo.management.order;

import lombok.*;

/**
 * 后台订单账单明细返回类
 * Created by caiyu on 2017/12/29.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaOrderBillingDetailResponse {
    /**
     * 商品总金额
     */
    private Double totalGoodsPrice;
    /**
     * 运费
     */
    private Double freight;
    /**
     * 乐币折扣
     */
    private Double lebiCashDiscount;
    /**
     *  优惠券折扣
     */
    private Double cashCouponDiscount;
    /**
     * 会员折扣
     */
    private Double memberDiscount;
    /**
     * 订单金额小计
     */
    private Double orderAmountSubtotal;
    /**
     * 应付金额
     */
    private Double amountPayable;
    /**
     * 促销折扣
     */
    private Double promotionDiscount;
    /**
     * 现金返利折扣
     */
    private Double subvention;
    /**
     * 产品券抵现金额
     */
    private Double productCouponDiscount;
    /**
     * 欠款金额
     */
    private Double arrearage;

}
