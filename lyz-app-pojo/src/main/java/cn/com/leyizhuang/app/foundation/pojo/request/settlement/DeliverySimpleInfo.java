package cn.com.leyizhuang.app.foundation.pojo.request.settlement;

import lombok.*;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * 生成订单时配送信息参数封装类
 *
 * @author Richard
 * Date: 2017/11/13.
 * Time: 10:55.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeliverySimpleInfo implements Serializable {

    private static final long serialVersionUID = 6314671804291929229L;
    /**
     * 配送方式
     */
    private String deliveryType;

    /* ********************* 门店自提 *********************** */

    /**
     *  预约门店id
     */
    private Long bookingStoreId;
    /**
     * 预约门店名称
     */
    private String bookingStoreName;
    /**
     * 预约门店名称
     */
    private String bookingStoreCode;
    /**
     * 门店地址
     */
    private String bookingStoreAddress;

    /* ********************* 送货上门 ************************ */
    /**
     * 收货人姓名
     */
    private String receiver;
    /**
     * 收货人电话
     */
    private String receiverPhone;
    /**
     * 收货省
     */
    private String deliveryProvince;
    /**
     *  收货城市
     */
    private String deliveryCity;
    /**
     * 收货县
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
     *  收货详细地址
     */
    private String detailedAddress;

    /**
     * 配送时间段
     */
    private String deliveryTime;

    /**
     * 是否业主收货
     */
    private Boolean  isOwnerReceiving;
    /**
     * 楼盘信息
     */
    private String estateInfo;
}
