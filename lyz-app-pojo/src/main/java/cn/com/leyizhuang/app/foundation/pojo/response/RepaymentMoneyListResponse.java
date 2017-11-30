package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

/**
 * 查询还款记录列表返回类
 * Created by caiyu on 2017/11/28.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RepaymentMoneyListResponse {
    /**
     * 还款时间
     */
    private String repaymentTime;
    /**
     * 还款金额
     */
    private Double repaymentMoney;
}
