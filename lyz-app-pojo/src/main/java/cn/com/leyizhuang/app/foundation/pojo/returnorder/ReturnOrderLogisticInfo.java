package cn.com.leyizhuang.app.foundation.pojo.returnorder;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import lombok.*;

/**
 * @author Jerry.Ren
 * Notes: 退单物流状态
 * Created with IntelliJ IDEA.
 * Date: 2017/12/8.
 * Time: 15:10.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReturnOrderLogisticInfo {

    private Long id;

    /**
     * 退单id
     */
    private Long roid;

    /**
     * 退单号
     */
    private String returnNO;

    /**
     * 配送方式
     */
    private AppDeliveryType deliveryType;


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
     * 退货省
     */
    private String deliveryProvince;
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

    /**
     * 配送员id
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

}
