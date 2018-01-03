package cn.com.leyizhuang.app.foundation.vo.management.order;

import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import lombok.*;

import java.util.List;

/**
 * 后台订单物流详情返回类
 * Created by caiyu on 2017/12/29.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaOrderDeliveryInfoResponse {
    /**
     * 配送仓库
     */
    private String warehouse;
    /**
     * 配送员姓名
     */
    private String deliveryClerkName;
    /**
     * 出货单号
     */
    private String shipmentNumber;
    /**
     * 预约配送时间
     */
    private String deliveryTime;
    /**
     *  物流状态
     */
    private List<OrderDeliveryInfoDetails> orderDeliveryInfoDetailsList;
}
