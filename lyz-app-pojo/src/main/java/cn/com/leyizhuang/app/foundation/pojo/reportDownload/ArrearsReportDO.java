package cn.com.leyizhuang.app.foundation.pojo.reportDownload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liuh
 * @date 2018/4/3
 */
@Getter
@Setter
@ToString
public class ArrearsReportDO {



    //城市
    private String cityName;
    //storeId
    private Long storeId;
    //门店编码
    private String storeCode;
    //门店名称
    private String storeName;
    //导购名称
     private String name;
     //顾客名称
    private String customerName;
    //订单号
    private String ordNo;
   //订单类型
    private String orderType;
    //订单状态
    private String orderStatus;
    //自提提货日期
    private String selfTakeOrderTime;
    //订单日期
    private String createTime;
    //出货日期
    private String shippingDate;
    //订单小计
    private Double orderAmount;
    //订单使用额度
    private Double orderCreditMoney;
    //第一次还款前的欠款
    private Double orderArrearageBefore;
    //订单欠款
    private Double orderArrearage;
    //订单已支付总金额
    private Double payUpMoney;
   //是否结清
    private String isPayUp;
    //订单还清日期
    private String payUpTime;
    //审核状态
    private String auditStatus;

}
