package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs;

import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.core.constant.remote.webservice.ebs.ChargeObjType;
import cn.com.leyizhuang.app.core.constant.remote.webservice.ebs.ChargeType;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 充值收款信息
 *
 * @author Richard
 * Created on 2018-01-02 13:39
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RechargeReceiptInf implements Serializable {

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

    private Long receiptId;

    /**
     * 充值单号
     */
    private String chargeNumber;

    /**
     * 分公司id
     */
    private Long sobId;

    /**
     * 充值对象
     */
    private ChargeObjType chargeObj;
    /**
     * 充值类型（银行清单）
     */
    private ChargeType chargeType;

    /**
     * 收款方式
     */
    private OrderBillingPaymentType receiptType;

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
    private Date receiptDate;

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
    private String receiptNumber;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String attribute4;

    private String attribute5;

}
