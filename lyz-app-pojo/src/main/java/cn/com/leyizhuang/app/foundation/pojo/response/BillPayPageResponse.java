package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.foundation.pojo.request.BillorderDetailsRequest;
import lombok.*;

import java.util.List;

/**
 * Created by 12421 on 2018/7/4.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BillPayPageResponse {
    /**
     * 帐单号
     */
    private String billNo;
    /**
     * 应付金额
     */
    private Double totalPayAmount;

    /**
     * 可用预存款
     */
    private Double stPreDiposit;

    /**
     * 当前信用金
     */
    private Double currentCreditMoney;

    /**
     * 还款后信用金
     */
    private Double repaymentCreditMoney;

    /**
     * 用户选择的订单详情
     */
    private List<BillorderDetailsRequest> billorderDetailsRequests;

}
