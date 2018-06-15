package cn.com.leyizhuang.app.foundation.pojo.response;


import lombok.*;

/**
 * Created by caiyu on 2018/6/8.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaOrderCalulatedAmountResponse {
    /**
     * 商品总金额
     */
    private Double totalGoodsAmount;
    /**
     * 会员折扣
     */
    private Double memberDiscount;
    /**
     * 订单折扣
     */
    private Double promotionDiscount;
    /**
     * 客户预存款
     */
    private Double stPreDeposit;
    /**
     * 信用金
     */
    private Double stCreditMoney;
    /**
     * 现金返利
     */
    private Double stSubvention;
    /**
     * 运费
     */
    private Double freight;
}
