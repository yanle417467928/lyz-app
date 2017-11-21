package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.response.LogisticsInformationResponse;

import java.util.List;

/**
 * Created by caiyu on 2017/11/20.
 */
public interface OrderDeliveryInfoDetailsService {
    void addOrderDeliveryInfoDetails(OrderDeliveryInfoDetails orderDeliveryInfoDetails);

    List<OrderDeliveryInfoDetails> queryListByOrderNumber(String orderNumber);

    LogisticsInformationResponse getDeliveryBydeliveryClerkNo(String deliveryClerkNo);
}
