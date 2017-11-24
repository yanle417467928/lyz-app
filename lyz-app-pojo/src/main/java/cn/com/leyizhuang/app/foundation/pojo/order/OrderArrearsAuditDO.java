package cn.com.leyizhuang.app.foundation.pojo.order;

import cn.com.leyizhuang.common.core.constant.ArrearsAuditStatus;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 欠款审核类
 * @author GenerationRoad
 * @date 2017/11/23
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderArrearsAuditDO {
    private Long id;
    //配送员ID
    private Long userId;
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
    private LocalDateTime distributionTime;
    //代收金额
    private Double agencyMoney;
    //订单欠款
    private Double orderMoney;
    //实收金额
    private Double realMoney;
    //收款方式
    private String paymentMethod;
    //状态
    private ArrearsAuditStatus status;
    //实收现金金额
    private Double cashMoney;
    //实收pos金额
    private Double posMoney;
    //实收支付宝金额
    private Double alipayMoney;
    //实收微信金额
    private Double wechatMoney;
    //remarks
    private String remarks;
    //创建时间
    private LocalDateTime createTime;
    //修改时间
    private LocalDateTime updateTime;

    public void setCustomerAndSeller(String customerName, String customerPhone, String sellerName,
                                     String sellerphone){
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.sellerName = sellerName;
        this.sellerphone = sellerphone;
    }

    public void setDistributionInfo(String distributionAddress, LocalDateTime distributionTime){
        this.distributionAddress = distributionAddress;
        this.distributionTime = distributionTime;
    }

    public void setOrderInfo(Long userId, String orderNumber, Double agencyMoney, Double orderMoney){
        this.userId = userId;
        this.orderNumber = orderNumber;
        this.agencyMoney = agencyMoney;
        this.orderMoney = orderMoney;
    }

    public void setArrearsAuditInfo(String paymentMethod, Double realMoney, String remarks,ArrearsAuditStatus status){
        this.paymentMethod = paymentMethod;
        this.realMoney = realMoney;
        if(paymentMethod.indexOf("现金")!=-1) {
            this.cashMoney = realMoney;
            this.posMoney = 0D;
            this.alipayMoney = 0D;
            this.wechatMoney = 0D;
        }
        if(paymentMethod.indexOf("POS")!=-1) {
            this.cashMoney = 0D;
            this.posMoney = realMoney;
            this.alipayMoney = 0D;
            this.wechatMoney = 0D;
        }
        if(paymentMethod.indexOf("支付宝")!=-1) {
            this.cashMoney = 0D;
            this.posMoney = 0D;
            this.alipayMoney = realMoney;
            this.wechatMoney = 0D;
        }
        if(paymentMethod.indexOf("微信")!=-1) {
            this.cashMoney = 0D;
            this.posMoney = 0D;
            this.alipayMoney = 0D;
            this.wechatMoney = realMoney;
        }
        this.remarks = remarks;
        this.status = status;
        this.createTime = LocalDateTime.now();
    }
}
