package cn.com.leyizhuang.app.foundation.pojo.reportDownload;

import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.core.constant.PaymentSubjectType;
import cn.com.leyizhuang.app.core.constant.StoreType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GenerationRoad
 * @date 2018/3/22
 */
@Getter
@Setter
@ToString
public class ReceiptsReportDO {
    //城市
    private String cityName;
    //门店名称
    private String storeName;
    //门店类型
    private String storeType;
    //付款时间
    private String payTime;
    //退款时间
    private String returnPayTime;
    //支付方式
    private String payType;
    //支付金额
    private Double money;
    //订/退单号
    private String orderNumber;
    //退单号
    private String returnOrderNumber;
    //备注
    private String remarks;

    private String payTypes;

    private String paymentSubjectType;

    //导购姓名
    private String sellerName;

    //订单交易单号
    private String outTradeNo;

    //流水单号
    private String tradeNo;

    //顾客姓名
    private String customerName;
    //订单类型
    private String orderType;

    public void setPayType(OrderBillingPaymentType payType){
        this.payType = payType.getDescription();
    }

    public void setStoreType(StoreType storeType){
        this.storeType = storeType.getDescription();
    }

}
