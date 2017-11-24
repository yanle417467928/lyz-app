package cn.com.leyizhuang.app.foundation.pojo.order;

import cn.com.leyizhuang.common.core.constant.ArrearsAuditStatus;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author GenerationRoad
 * @date 2017/11/23
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderAgencyFundDO {

    private Long id;
    //配送员ID
    private Long userId;
    //订单编号
    private String orderNumber;
    //会员姓名
    private String customerName;
    //会员电话
    private String customerPhone;
    //导购id
    private Long sellerId;
    //导购姓名
    private String sellerName;
    //导购电话
    private String sellerphone;
    //代收金额
    private Double agencyMoney;
    //实收金额
    private Double realMoney;
    //应退门店金额
    private Double returnMoney;
    //实收现金金额
    private Double cashMoney;
    //实收pos金额
    private Double posMoney;
    //实收支付宝金额
    private Double alipayMoney;
    //实收微信金额
    private Double wechatMoney;
    //收款方式
    private String paymentMethod;
    //remarks
    private String remarks;
    //创建时间
    private LocalDateTime createTime;
    //公司到账时间
    private LocalDateTime realTime;

    public void setCustomerAndSeller(String customerName, String customerPhone, Long sellerId, String sellerName,
                                     String sellerphone){
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.sellerphone = sellerphone;
    }

    public void setOrderInfo(Long userId, String orderNumber, Double agencyMoney){
        this.userId = userId;
        this.orderNumber = orderNumber;
        this.agencyMoney = agencyMoney;
    }

    public void setAgencyFundInfo(String paymentMethod, Double realMoney, Double returnMoney, String remarks){
        this.paymentMethod = paymentMethod;
        this.realMoney = realMoney;
        this.returnMoney = returnMoney;
        if(paymentMethod.indexOf("现金")!=-1) {
            this.cashMoney = returnMoney;
            this.posMoney = 0D;
            this.alipayMoney = 0D;
            this.wechatMoney = 0D;
        }
        if(paymentMethod.indexOf("POS")!=-1) {
            this.cashMoney = 0D;
            this.posMoney = returnMoney;
            this.alipayMoney = 0D;
            this.wechatMoney = 0D;
        }
        if(paymentMethod.indexOf("支付宝")!=-1) {
            this.cashMoney = 0D;
            this.posMoney = 0D;
            this.alipayMoney = returnMoney;
            this.wechatMoney = 0D;
        }
        if(paymentMethod.indexOf("微信")!=-1) {
            this.cashMoney = 0D;
            this.posMoney = 0D;
            this.alipayMoney = 0D;
            this.wechatMoney = returnMoney;
        }
        this.remarks = remarks;
        this.createTime = LocalDateTime.now();
    }
}
