package cn.com.leyizhuang.app.foundation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;


/**
 * @author GenerationRoad
 * @date 2018/3/19
 */
@Getter
@Setter
@ToString
public class DecorationCompanyCreditBillingDTO {
    //门店ID
    private Long storeId;
    //门店名称
    private String storeName;
    //记账周期开始时间
    @NotEmpty(message = "'记账周期开始日期'不能为空")
    private String startTime;
    //记账周期结束时间
    @NotEmpty(message = "'记账周期结束日期'不能为空")
    private String endTime;
    //账单名称
    @Length(min = 2, max = 20, message = "'账单名称'的长度必须在2~20位之间")
    private String billName;
    //账单总额
    private Double billAmount;
}
