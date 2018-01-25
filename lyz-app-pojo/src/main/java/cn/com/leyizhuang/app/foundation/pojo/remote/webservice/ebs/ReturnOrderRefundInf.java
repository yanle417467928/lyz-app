package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.core.constant.ReturnOrderType;
import lombok.*;

import java.util.Date;

/**
 * 退单退款信息
 *
 * @author Richard
 * Created on 2018-01-02 13:39
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ReturnOrderRefundInf {

    /**
     * 退款id
     */
    private Long refundId;


    /**
     * 主退单号
     */
    private String mainReturnNumber;


    /**
     * 原主单号
     */
    private String mainOrderNumber;

    /**
     * 退款单号
     */
    private String refundNumber;

    /**
     * 顾客id
     */
    private Long userId;

    /**
     * 门店编码
     */
    private String diySiteCode;


    /**
     * 门店组织编码
     */
    private String storeOrgCode;

    /**
     * 退款类型
     */
    private OrderBillingPaymentType refundType;

    /**
     * 退款日期
     */
    private Date refundDate;

    /**
     * 退款金额
     */
    private Double amount;

    /**
     * 描述
     */
    private String description;

    /**
     * 分公司 id
     */
    private Long sobId;

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

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String attribute4;

    private String attribute5;

}
