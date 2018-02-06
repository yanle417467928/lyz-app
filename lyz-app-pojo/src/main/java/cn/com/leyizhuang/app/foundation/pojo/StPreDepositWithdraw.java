package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.core.constant.PreDepositWithdrawStatus;
import lombok.*;

import java.util.Date;

/**
 * 门店预存款提现 申请单
 * Created by panjie on 2018/2/5.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StPreDepositWithdraw {

    private Long id;

    /**
     * 申请单号
     */
    private String applyNo;

    /**
     * 申请时间
     */
    private Date createTime;

    /**
     * 申请门店id
     */
    private Long applyStId;

    /**
     * 申请门店名称
     */
    private Long applyStName;

    /**
     * 申请门店电话
     */
    private String applyStPhone;

    /**
     * 提现帐号类型
     */
    private OrderBillingPaymentType accountType;

    /**
     * 提现帐号
     */
    private String account;

    /**
     * 提现金额
     */
    private Double withdrawAmount;

    /**
     * 状态
     */
    private PreDepositWithdrawStatus status;

    /**
     * 审核人id
     */
    private Long checkId;

    /**
     * 审核人名称
     */
    private String checkName;

    /**
     * 审核人编码
     */
    private String checkCode;
}
