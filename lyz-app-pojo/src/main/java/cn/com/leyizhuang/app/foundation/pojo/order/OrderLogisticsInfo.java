package cn.com.leyizhuang.app.foundation.pojo.order;

import lombok.*;

import java.util.Date;

/**
 * 订单物流相关信息
 *
 * @author Richard
 * Created on 2017-10-10 11:23
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderLogisticsInfo {

    private Long id;

    //订单号
    private String orderNumber;

    //配送方式
    private String deliveryType;

    //预约门店编码
    private String bookingStoreCode;

    //预约门店名称
    private String bookingStoreName;

    //预约提货时间
    private Date bookingTime;

    //配送时间
    private Date deliveryTime;

    //配送员id
    private Long deliveryClerkId;

    //配送员姓名
    private String deliveryClerkName;

    //配送仓名称
    private String warehouse;

    //是否业主收货
    private Boolean isOwnerReceiving;

    //收货人
    private String receiver;

    //收货人电话
    private String receiverPhone;

    //收货人地址
    private String shippingAddress;
}