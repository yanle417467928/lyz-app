package cn.com.leyizhuang.app.foundation.pojo.bill;

import cn.com.leyizhuang.app.core.constant.BillStatusEnum;
import cn.com.leyizhuang.app.foundation.pojo.response.BillInfoResponse;
import lombok.*;

import java.util.Date;

/**
 * 账单表
 * @author GenerationRoad
 * @date 2018/6/26
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BillInfoDO {

    private Long id;
    //门店ID
    private Long storeId;
    //账单单号
    private String billNo;
    //账单名称
    private String billName;
    //记账周期开始时间
    private Date billStartDate;
    //记账周期结束时间
    private Date billEndDate;
    //还款截至日
    private Date repaymentDeadlineDate;
    //账单总金额(本期账单金额+上期未还账单金额+上期滞纳金+本期调整金额)
    private Double billTotalAmount;
    //本期账单金额(账单日内出货订单正向金额求和)
    private Double currentBillAmount;
    //本期调整金额(账单日内退货订单（包含退货、取消、拒签）负向金额求和)
    private Double currentAdjustmentAmount;
    //本期已还金额
    private Double currentPaidAmount;
    //本期未还金额
    private Double currentUnpaidAmount;
    //已还上期账单金额
    private Double priorPaidBillAmount;
    //已还上期滞纳金
    private Double priorPaidInterestAmount;
    //账单状态(0：未出帐；1：出帐；2 ：历史账单)
    private BillStatusEnum status;
    //出账时间
    private Date billTime;
    //创建时间
    private Date createTime;
    //创建者id
    private Long createUserId;
    //创建者
    private String createUserName;

    public BillInfoResponse transfer(BillInfoDO DO){
        BillInfoResponse response =  new BillInfoResponse();



        return null;
    }
}
