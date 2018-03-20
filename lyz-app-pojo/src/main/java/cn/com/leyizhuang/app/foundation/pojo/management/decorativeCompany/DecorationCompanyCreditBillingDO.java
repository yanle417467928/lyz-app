package cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany;

import cn.com.leyizhuang.app.foundation.dto.DecorationCompanyCreditBillingDTO;
import cn.com.leyizhuang.common.util.TimeTransformUtils;
import lombok.*;
import org.springframework.core.annotation.OrderUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author GenerationRoad
 * @date 2018/3/19
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DecorationCompanyCreditBillingDO implements Serializable {
    private Long id;
    //门店ID
    private Long storeId;
    //门店名称
    private String storeName;
    //记账周期开始时间
    private Date startTime;
    //记账周期结束时间
    private Date endTime;
    //账单名称
    private String billName;
    //账单总额
    private Double billAmount;
    //已还金额
    private Double repaidAmount;
    //是否还清
    private Boolean isPayOff;
    //操作人id
    private Long operationId;
    //还款人操作人id
    private String payOffOperationId;
    //装饰公司信用金账单单号
    private String creditBillingNo;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;


    public static DecorationCompanyCreditBillingDO transform(DecorationCompanyCreditBillingDTO creditBillingDTO){
        DecorationCompanyCreditBillingDO creditBillingDO = null;
        if (null != creditBillingDTO) {
            creditBillingDO = new DecorationCompanyCreditBillingDO();
            creditBillingDO.setStoreId(creditBillingDTO.getStoreId());
            creditBillingDO.setStoreName(creditBillingDTO.getStoreName());
            creditBillingDO.setStartTime(TimeTransformUtils.parseDate(creditBillingDTO.getStartTime()));
            creditBillingDO.setEndTime(TimeTransformUtils.parseDate(creditBillingDTO.getEndTime()));
            creditBillingDO.setBillName(creditBillingDTO.getBillName());
            creditBillingDO.setBillAmount(0D);
            creditBillingDO.setRepaidAmount(0D);
            creditBillingDO.setIsPayOff(false);
            creditBillingDO.setCreateTime(new Date());
        }

        return creditBillingDO;
    }




}
