package cn.com.leyizhuang.app.foundation.pojo.request.management;

import lombok.*;

/**
 * 后台多条件查询装饰公司订单请求参数类
 * Created by caiyu on 2017/12/21.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaCompanyOrderVORequest {
    /**
     * 城市id
     */
    private Long cityId;
    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 开始时间
     */
    private String beginTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 下单人姓名
     */
    private String creatorName;
    /**
     * 收货地址
     */
    private String shippingAddress;
    /**
     * 下单人电话
     */
    private String creatorPhone;
    /**
     * 收货人姓名
     */
    private String receiverName;
    /**
     * 收货人电话
     */
    private String receiverPhone;
}
