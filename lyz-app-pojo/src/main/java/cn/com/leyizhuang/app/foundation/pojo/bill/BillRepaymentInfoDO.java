package cn.com.leyizhuang.app.foundation.pojo.bill;

import cn.com.leyizhuang.app.core.constant.OnlinePayType;
import lombok.*;

import java.util.Date;

/**
 * 账单还款表
 * @author GenerationRoad
 * @date 2018/6/26
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BillRepaymentInfoDO {

    private Long id;
    //账单头id
    private Long BillId;
    //账单单号
    private String billingNo;
    //还款人id
    private Long repaymentUserId;
    //还款人姓名
    private String repaymentUserName;
    //还款系统(app,manage)
    private String repaymentSystem;
    //还款时间
    private Date repaymentTime;
    //第三方支付类型
    private OnlinePayType onlinePayType;
    //第三方支付金额
    private Double onlinePayAmount;
    //预存款支付金额
    private Double preDeposit;
    //还款总金额
    private Double totalRepaymentAmount;
    //是否支付
    private Boolean isPaid;
    //利率
    private Double interestRate;
    //总滞纳金
    private Double totalInterestAmount;
    //创建时间
    private Date createTime;
}
