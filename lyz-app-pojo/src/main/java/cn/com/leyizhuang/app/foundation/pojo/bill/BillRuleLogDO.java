package cn.com.leyizhuang.app.foundation.pojo.bill;

import lombok.*;

import java.util.Date;

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
    private Integer billDate;
    //还款截至日
    private Integer repaymentDeadlineDate;
    //利率(单位：万分之一/天 )
    private Double interestRate;
    //门店id
    private Long storeId;
    //变更时间
    private Date updateTime;
    //变更人id
    private Long updateUserId;
}
