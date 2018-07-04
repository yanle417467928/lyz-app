package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.core.constant.BillStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * 账单响应类
 * Created by 12421 on 2018/6/28.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BillInfoResponse {
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
    //出账时间
    private Date billTime;


    //账单总金额(本期账单金额+上期未还账单金额+上期滞纳金+本期调整金额)
    private Double billTotalAmount;
    //本期账单金额(账单日内出货订单正向金额求和)
    private Double currentBillAmount;
    //本期调整金额(账单日内退货订单（包含退货、拒签）负向金额求和)
    private Double currentAdjustmentAmount;
    //本期已还金额
    private Double currentPaidAmount;
    //本期未还金额
    private Double currentUnpaidAmount;
    //上期未还账单金额
    private Double priorNotPaidBillAmount;
    //上期未还滞纳金
    private Double priorNotPaidInterestAmount;
    //已还上期账单金额
    private Double priorPaidBillAmount;
    //已还上期滞纳金
    private Double priorPaidInterestAmount;

    //账单状态(0：未出帐；1：出帐；2 ：历史账单)
    private String status;
    // 未还订单明细
    List<BillRepaymentGoodsInfoResponse> notPayOrderDetails;
    // 已支付订单那明细
    List<BillRepaymentGoodsInfoResponse> paidOrderDetails;

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    public Date getBillStartDate() {
        return billStartDate;
    }
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    public Date getBillEndDate() {
        return billEndDate;
    }
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    public Date getRepaymentDeadlineDate() {
        return repaymentDeadlineDate;
    }
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    public Date getBillTime() {
        return billTime;
    }
}
