package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

/**
 * 配送员待配送列表返回类
 * Created by caiyu on 2017/11/20.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WaitDeliveryResponse {
    /**
     * 出货单号
     */
    private String shipperNumber;
    /**
     * 顾客/收货人姓名
     */
    private String receiver;
    /**
     * 顾客/收货人电话
     */
    private String receiverPhone;
    /**
     * 导购姓名
     */
    private String sellerName;
    /**
     * 导购电话
     */
    private String sellerPhone;
    /**
     * 收货地址
     */
    private String shippingAddress;
    /**
     * 配送日期时间
     */
    private String deliveryTime;
    /**
     * 代收金额
     */
    private Double collectionAmount;
}
