package cn.com.leyizhuang.app.foundation.vo.management;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author GenerationRoad
 * @date 2018/7/14
 */
@Getter
@Setter
@ToString
public class BillRuleLogVO {
    private Long id;
    //出账日
    private Integer billDate;
    //还款截至日
    private Integer repaymentDeadlineDate;
    //利率(单位：万分之一/天 )
    private Double interestRate;
    //门店id
    private Long storeId;
    //门店名称
    private String storeName;
    //变更时间
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    //变更人
    private String updateUserName;

}
