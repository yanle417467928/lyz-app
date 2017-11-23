package cn.com.leyizhuang.app.foundation.pojo.request.settlement;

import lombok.*;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * 生成订单时 账单相关信息封装类
 *
 * @author Richard
 * Date: 2017/11/13.
 * Time: 10:55.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BillingSimpleInfo implements Serializable {

    private static final long serialVersionUID = 8532606023597121783L;

    /**
     * 顾客预存款
     */
    private Double cusPreDeposit;
    /**
     * 门店预存款
     */
    private Double stPreDeposit;
    /**
     * 导购信用额度
     */
    private Double empCreditMoney;
    /**
     * 门店信用额度
     */
    private Double storeCreditMoney;
    /**
     * 门店现金返利
     */
    private Double storeSubvention;
    /**
     * 代收款
     */
    private Double collectionAmount;

}
