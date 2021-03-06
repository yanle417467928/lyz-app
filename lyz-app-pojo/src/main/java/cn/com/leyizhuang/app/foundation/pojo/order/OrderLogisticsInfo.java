package cn.com.leyizhuang.app.foundation.pojo.order;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import lombok.*;

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

    /**
     * 订单id
     */
    private Long oid;

    /**
     * 订单号
     */
    private String ordNo;

    /**
     * 配送方式
     */
    private AppDeliveryType deliveryType;


    //************* 自提单信息 ************

    /**
     * 预约门店编码
     */
    private String bookingStoreCode;

    /**
     * 预约门店名称
     */
    private String bookingStoreName;

    /**
     * 预约门店地址
     */
    private String bookingStoreAddress;


    //************* 配送单信息 ***********

    /**
     * 配送时间
     */
    private String deliveryTime;

    /**
     * 收货人姓名
     */
    private String receiver;

    /**
     * 收货人电话
     */
    private String receiverPhone;

    /**
     * 收货地址全称
     */
    private String shippingAddress;
    /**
     * 收货省
     */
    private String deliveryProvince;
    /**
     * 收货城市
     */
    private String deliveryCity;
    /**
     * 收货区县
     */
    private String deliveryCounty;
    /**
     * 收货街道
     */
    private String deliveryStreet;
    /**
     * 小区名
     */
    private String residenceName;
    /**
     * 收货详细地址
     */
    private String detailedAddress;
    /**
     * 是否业主收货
     */
    private Boolean isOwnerReceiving;

    /**
     * 楼盘信息
     */
    private String estateInfo;
    /**
     *  配送员id
     */
    private Long deliveryClerkId;
    /**
     * 配送员编码
     */
    private String deliveryClerkNo;
    /**
     * 配送员姓名
     */
    private String deliveryClerkName;
    /**
     * 配送员电话
     */
    private String deliveryClerkPhone;
    /**
     * 配送仓名称
     */
    private String warehouse;

}
