package cn.com.leyizhuang.app.foundation.pojo.order;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderShipping {

    private Long id;
    //订单id
    private Long oid;
    //订单id
    private String ordNo;
    //订单id
    private String shippingNo;
    //订单id
    private Date shippingTime;
}
