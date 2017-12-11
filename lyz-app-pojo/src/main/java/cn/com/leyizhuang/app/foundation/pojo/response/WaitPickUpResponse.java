package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

/**
 * 配送员待取货返回类
 * Created by caiyu on 2017/11/20.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WaitPickUpResponse {
    /**
     * 退货单号
     */
    private String returnNumber;
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
     * 取货地址
     */
    private String PickUpAddress;
    /**
     * 取货时间
     */
    private String pickUpTime;
}
