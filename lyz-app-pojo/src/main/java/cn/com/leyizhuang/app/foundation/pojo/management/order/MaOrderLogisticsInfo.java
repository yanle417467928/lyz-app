package cn.com.leyizhuang.app.foundation.pojo.management.order;

import lombok.*;
/**
 * 订单物流信息
 *
 * @author liuh
 * Created on 2017-11-03 14:14
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaOrderLogisticsInfo {
    private Long id;
    // 收货人
    private String receiver;
   // 收货人电话
    private String receiverPhone;
    //收货人地址
    private String shippingAddress;
}
