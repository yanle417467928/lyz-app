package cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author GenerationRoad
 * @date 2018/3/19
 */
@Setter
@Getter
@ToString
public class DecorationCompanyCreditBillingVO {
    private Long id;
    //门店名称
    private String storeName;
    //记账周期
    private String cycleTime;
    //账单名称
    private String billName;
    //账单总额
    private Double billAmount;
    //已还金额
    private Double repaidAmount;
    //是否还清
    private Boolean isPayOff;
    //装饰公司信用金账单单号
    private String creditBillingNo;
    //创建时间
    private String createTime;
    //修改时间
    private String updateTime;
    //操作人id
    private Long operationId;

}
