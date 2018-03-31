package cn.com.leyizhuang.app.foundation.pojo.returnorder;

import cn.com.leyizhuang.app.core.constant.AppOrderStatus;
import cn.com.leyizhuang.app.core.constant.AppReturnOrderStatus;
import cn.com.leyizhuang.app.core.constant.OrderLifecycleType;
import lombok.*;

import java.util.Date;

/**
 * 退单生命周期类
 * Created by caiyu on 2018/3/31.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReturnOrderLifecycle {
    private Long id;

    //退单ID
    private Long roid;

    //退单号
    private String returnNo;

    //退单操作
    private OrderLifecycleType operation;

    //操作后退单状态
    private AppReturnOrderStatus postStatus;

    //操作时间
    private Date operationTime;
}
