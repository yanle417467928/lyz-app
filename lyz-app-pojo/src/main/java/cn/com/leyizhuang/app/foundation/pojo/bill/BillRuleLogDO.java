package cn.com.leyizhuang.app.foundation.pojo.bill;

import lombok.*;

/**
 * 账单规则变更日志表
 * @author GenerationRoad
 * @date 2018/6/26
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BillRuleLogDO {
    private Long id;
    //
    private Long ruleId;
    //出账日
    private Long billDate;
    //还款截至日
    private Long repaymentDeadlineDate;
    //利率
    private Long interestRate;
    //门店id
    private Long storeId;
    //变更时间
    private Long updateTime;
    //变更人id
    private Long updateUserId;
}
