package cn.com.leyizhuang.app.foundation.pojo.order;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * 简单商品信息，用于前后台商品信息交互
 * @author Jerry.Ren
 * Date: 2017/11/2.
 * Time: 10:55.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeliverySimpleInfo implements Serializable {

    //配送方式
    private AppDeliveryType deliveryType;

    /* ********************* 门店自提 *********************** */

    //预约门店id
    private String bookingStoreId;

    //预约门店名称
    private String bookingStoreName;

    //预约提货时间
    private Date bookingTime;

    /* ********************* 送货上门 ************************ */
    //收货人姓名
    private String receiver;
    //收货人电话
    private String receiverPhone;
    //收货城市
    private String deliveryCity;
    //收货县
    private String deliveryCounty;
    //收货街道
    private String deliveryStreet;
    //小区名
    private String residenceName;
    //收货详细地址
    private String detailedAddress;

    //配送时间
    private Date deliveryTime;
}
