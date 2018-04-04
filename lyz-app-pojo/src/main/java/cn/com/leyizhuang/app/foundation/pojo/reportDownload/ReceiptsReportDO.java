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
    //付款/退款时间
    private String payTime;
    //支付方式
    private String payType;
    //支付金额
    private Double money;
    //订/退单号
    private String orderNumber;
    //备注
    private String remarks;

    private String payTypes;

    private String paymentSubjectType;

    public void setPayType(OrderBillingPaymentType payType){
        this.payType = payType.getDescription();
    }

    public void setStoreType(StoreType storeType){
        this.storeType = storeType.getDescription();
    }

}
