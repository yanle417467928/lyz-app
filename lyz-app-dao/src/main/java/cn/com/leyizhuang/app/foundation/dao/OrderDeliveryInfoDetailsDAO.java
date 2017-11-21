package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.response.LogisticsInformationResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by caiyu on 2017/11/20.
 */
@Repository
public interface OrderDeliveryInfoDetailsDAO {

    void addOrderDeliveryInfoDetails(OrderDeliveryInfoDetails orderDeliveryInfoDetails);

    List<OrderDeliveryInfoDetails> queryListByOrderNumber(@Param("orderNumber") String orderNumber);

    LogisticsInformationResponse getDeliveryBydeliveryClerkNo(@Param("deliveryClerkNo") String deliveryClerkNo);
}
