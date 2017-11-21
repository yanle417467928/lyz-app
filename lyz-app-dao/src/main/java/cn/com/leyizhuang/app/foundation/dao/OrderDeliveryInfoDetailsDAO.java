package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.response.LogisticsInformationResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.WaitDeliveryResponse;
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

    LogisticsInformationResponse getDeliveryByOperatorNo(@Param("operatorNo") String operatorNo);

    //获取配送员待配送列表
    List<WaitDeliveryResponse> getOrderBeasInfoByOperatorNo(@Param("operatorNo") String operatorNo);
}
