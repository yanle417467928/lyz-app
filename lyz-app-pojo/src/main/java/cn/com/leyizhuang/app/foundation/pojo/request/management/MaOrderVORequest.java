package cn.com.leyizhuang.app.foundation.pojo.request.management;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import lombok.*;

/**
 * 后台多条件查询门店订单请求参数类
 * Created by caiyu on 2017/12/19.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaOrderVORequest {
    /**
     * 城市id
     */
    private Long cityId;
    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 配送方式
     */
    private AppDeliveryType appDeliveryType;
    /**
     * 开始时间
     */
    private String beginTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 会员姓名
     */
    private String memberName;
    /**
     * 收货地址
     */
    private String shippingAddress;
    /**
     * 导购姓名
     */
    private String sellerName;
    /**
     * 会员电话
     */
    private String memberPhone;
    /**
     * 收货人姓名
     */
    private String receiverName;
    /**
     * 收货人电话
     */
    private String receiverPhone;
}
