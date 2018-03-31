package cn.com.leyizhuang.app.foundation.pojo.order;

import cn.com.leyizhuang.app.core.constant.OrderLifecycleType;
import lombok.*;

import java.util.Date;

/**
 * 订单生命周期
 *
 * @author Richard
 * Created on 2017-10-10 11:54
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderLifecycle {

    private Long id;

    //订单ID
    private Long oid;

    //订单号
    private String orderNumber;

    //订单操作
    private OrderLifecycleType operation;

    //操作后订单状态
    private OrderLifecycleType postStatus;

    //操作时间
    private Date operationTime;

}
