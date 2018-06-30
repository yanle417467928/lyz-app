package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

import java.util.Date;

/**
 * @author GenerationRoad
 * @date 2018/6/30
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BillHistoryListResponse {
    //年份
    private String years;
    //账单名称
    private String billName;
    //记账周期开始时间
    private Date billStartDate;
    //记账周期结束时间
    private Date billEndDate;
    //本期已还金额
    private Double currentPaidAmount;
}
