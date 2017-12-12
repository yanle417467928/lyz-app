package cn.com.leyizhuang.app.foundation.pojo.request;

import lombok.*;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2017/12/8.
 * Time: 15:37.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReturnDeliverySimpleInfo {

    /**
     * 配送方式
     */
    private String deliveryType;

    //************* 自提单信息 ************

    /**
     * 预约门店编码
     */
    private String returnStoreCode;

    /**
     * 预约门店名称
     */
    private String returnStoreName;

    /**
     * 预约门店地址
     */
    private String returnStoreAddress;


    //************* 配送单信息 ***********

    /**
     * 配送时间
     */
    private String deliveryTime;

    /**
     * 退货人姓名
     */
    private String rejecter;

    /**
     * 退货人电话
     */
    private String rejecterPhone;

    /**
     * 退货地址全称
     */
    private String returnFullAddress;
    /**
     * 退货城市
     */
    private String deliveryCity;
    /**
     * 退货区县
     */
    private String deliveryCounty;
    /**
     * 退货街道
     */
    private String deliveryStreet;
    /**
     * 小区名
     */
    private String residenceName;
    /**
     * 退货详细地址
     */
    private String detailedAddress;
}
