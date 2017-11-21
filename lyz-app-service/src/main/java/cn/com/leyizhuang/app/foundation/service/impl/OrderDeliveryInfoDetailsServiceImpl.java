package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.OrderDeliveryInfoDetailsDAO;
import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.response.LogisticsInformationResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.WaitDeliveryResponse;
import cn.com.leyizhuang.app.foundation.service.OrderDeliveryInfoDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by caiyu on 2017/11/20.
 */
@Service
public class OrderDeliveryInfoDetailsServiceImpl implements OrderDeliveryInfoDetailsService {

    @Autowired
    private OrderDeliveryInfoDetailsDAO orderDeliveryInfoDetailsDAO;
    @Override
    public void addOrderDeliveryInfoDetails(OrderDeliveryInfoDetails orderDeliveryInfoDetails) {
        orderDeliveryInfoDetailsDAO.addOrderDeliveryInfoDetails(orderDeliveryInfoDetails);
    }

    @Override
    public List<OrderDeliveryInfoDetails> queryListByOrderNumber(String orderNumber) {
        return orderDeliveryInfoDetailsDAO.queryListByOrderNumber(orderNumber);
    }

    @Override
    public LogisticsInformationResponse getDeliveryByOperatorNoAndOrderNumber(String operatorNo,String orderNumber) {
        return orderDeliveryInfoDetailsDAO.getDeliveryByOperatorNoAndOrderNumber(operatorNo,orderNumber);
    }

    @Override
    public List<WaitDeliveryResponse> getOrderBeasInfoByOperatorNo(String operatorNo) {
        return orderDeliveryInfoDetailsDAO.getOrderBeasInfoByOperatorNo(operatorNo);
    }
}
