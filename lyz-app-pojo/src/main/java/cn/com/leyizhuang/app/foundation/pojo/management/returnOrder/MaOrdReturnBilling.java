package cn.com.leyizhuang.app.foundation.pojo.management.returnOrder;

import lombok.*;


/**
 *  退款总表
 * Created by liuh on 2017/12/2.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaOrdReturnBilling {
    private Long id;
    /**
     * 退单id
     */
    private Long roid;
    /**
     * 退单号
     */
    private String returnNo;
    /**
     * 退预存款
     */
    private Double preDeposit;
    /**
     * 退信用金
     */
    private Double creditMoney;
    /**
     * 退门店预存款
     */
    private Double stPreDeposit;
    /**
     * 退门店信用金
     */
    private Double stCreditMoney;
    /**
     * 退门店现金返利
     */
    private Double stSubvention;
    /**
     * 线上支付金额
     */
    private Double onlinePay;
    /**
     * 退现金
     */
    private Double cash;
    /**
     * 使用乐币数量
     */
    private Double LeBiQuantity;

    /**
     * 导购代支付门店预存款
     */
    private Double sellerStoreDeposit;

}
