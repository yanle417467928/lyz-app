package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs;

import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.core.constant.PaymentSubjectType;
import cn.com.leyizhuang.app.core.constant.remote.webservice.ebs.ChargeObjType;
import cn.com.leyizhuang.app.core.constant.remote.webservice.ebs.ChargeType;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 提现退款信息
 *
 * @author Richard
 * Created on 2018-01-02 13:39
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawRefundInf implements Serializable {

    private static final long serialVersionUID = 8411090798418359988L;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否传输成功
     */
    private AppWhetherFlag sendFlag;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 传输成功时间
     */
    private Date sendTime;

    private Long refundId;

    /**
     * 提现单号
     */
    private String withdrawNumber;

    /**
     * 分公司id
     */
    private Long sobId;

    /**
     * 提现对象
     */
    private PaymentSubjectType withdrawObj;
    /**
     * 充值类型（银行清单）
     */
    private OrderBillingPaymentType withdrawType;

    /**
     * 收款方式
     */
    private OrderBillingPaymentType refundType;

    /**
     * 用户id
     */
    private Long userid;
    /**
     * 门店组织编码
     */
    private String storeOrgCode;

    /**
     * 门店编码
     */
    private String diySiteCode;

    /**
     * 收款日期
     */
    private Date refundDate;

    /**
     * 收款金额
     */
    private Double amount;

    /**
     * 描述
     */
    private String description;

    /**
     * 收款编号
     */
    private String refundNumber;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String attribute4;

    private String attribute5;

}
