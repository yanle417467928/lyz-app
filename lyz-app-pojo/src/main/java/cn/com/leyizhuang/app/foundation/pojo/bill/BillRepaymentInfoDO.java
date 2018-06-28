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
    //还款单号（BL_RC开头）
    private String repaymentNo;
    //账单头id
    private Long billId;
    //账单单号
    private String billNo;
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
    //现金金额
    private Double cashMoney;
    //其他金额
    private Double otherMoney;
    //POS金额
    private Double posMoney;
    //pos流水号
    private String posNumber;
    //还款总金额
    private Double totalRepaymentAmount;
    //是否支付
    private Boolean isPaid;
    //利率(单位：万分之一/天 )
    private Double interestRate;
    //总滞纳金
    private Double totalInterestAmount;
    //创建时间
    private Date createTime;
}
