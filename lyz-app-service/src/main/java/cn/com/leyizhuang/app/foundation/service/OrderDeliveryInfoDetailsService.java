package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.response.LogisticsInformationResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.ShipperDetailResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.WaitDeliveryResponse;

import java.util.Date;
import java.util.List;

/**
 * Created by caiyu on 2017/11/20.
 */
public interface OrderDeliveryInfoDetailsService {
    void addOrderDeliveryInfoDetails(OrderDeliveryInfoDetails orderDeliveryInfoDetails);

    List<OrderDeliveryInfoDetails> queryListByOrderNumber(String orderNumber);

    LogisticsInformationResponse getDeliveryByOperatorNoAndOrderNumber(String operatorNo, String orderNumber);

    //获取配送员待配送列表
    List<WaitDeliveryResponse> getOrderBeasInfoByOperatorNo(String operatorNo);

    //获取出货单详情
    ShipperDetailResponse getOrderDeliveryInfoDetailsByOperatorNoAndOrderNumber(String operatorNo, String orderNumber);

    //获取推送的物流消息
    List<OrderDeliveryInfoDetails> getLogisticsMessageByUserId(Long userID, Date createTime ,Integer identityType);
}
