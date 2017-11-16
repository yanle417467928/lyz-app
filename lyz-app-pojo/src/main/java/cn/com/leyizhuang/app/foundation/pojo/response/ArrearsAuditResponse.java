package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.common.core.constant.ArrearsAuditStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GenerationRoad
 * @date 2017/11/15
 */
@Setter
@Getter
@ToString
public class ArrearsAuditResponse {

    //订单编号
    private String orderNumber;
    //会员姓名
    private String customerName;
    //会员电话
    private String customerPhone;
    //导购姓名
    private String sellerName;
    //导购电话
    private String sellerphone;
    //配送地址
    private String distributionAddress;
    //配送时间
    private String distributionTime;
    //代收金额
    private Double agencyMoney;
    //订单欠款
    private Double orderMoney;
    //实收金额
    private Double realMoney;
    //收款方式
    private String paymentMethod;
    //状态
    private String status;

    public void setType(ArrearsAuditStatus arrearsAuditStatus){
        this.status = arrearsAuditStatus.getValue();
    }
}
