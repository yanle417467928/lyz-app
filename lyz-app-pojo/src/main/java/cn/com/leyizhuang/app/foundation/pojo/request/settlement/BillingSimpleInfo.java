package cn.com.leyizhuang.app.foundation.pojo.request.settlement;

import lombok.*;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *  生成订单时 账单相关信息封装类
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

    private Double cusPreDeposit;

    private Double stPreDeposit;

    private Double empCreditMoney;

    private Double storeCreditMoney;

    private Double storeSubvention;

    private Double collectionAmount;

}
