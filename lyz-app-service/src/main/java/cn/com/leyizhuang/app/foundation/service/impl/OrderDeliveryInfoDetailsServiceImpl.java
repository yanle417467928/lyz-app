package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.OrderDeliveryInfoDetailsDAO;
import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.response.LogisticsInformationResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.ShipperDetailResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.WaitDeliveryResponse;
import cn.com.leyizhuang.app.foundation.service.OrderDeliveryInfoDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    public LogisticsInformationResponse getDeliveryByOperatorNoAndOrderNumber(String operatorNo, String orderNumber) {
        return orderDeliveryInfoDetailsDAO.getDeliveryByOperatorNoAndOrderNumber(operatorNo, orderNumber);
    }

    @Override
    public List<WaitDeliveryResponse> getOrderBeasInfoByOperatorNo(String operatorNo) {
        List<WaitDeliveryResponse> waitDeliveryResponseList = orderDeliveryInfoDetailsDAO.getOrderBeasInfoByOperatorNo(operatorNo);
        return waitDeliveryResponseList;
    }

    @Override
    public ShipperDetailResponse getOrderDeliveryInfoDetailsByOperatorNoAndOrderNumber(String operatorNo, String orderNumber) {
        return orderDeliveryInfoDetailsDAO.getOrderDeliveryInfoDetailsByOperatorNoAndOrderNumber(operatorNo, orderNumber);
    }

    @Override
    public List<OrderDeliveryInfoDetails> getLogisticsMessageByUserId(Long userID, Date createTime) {
        return orderDeliveryInfoDetailsDAO.getLogisticsMessageByUserId(userID, createTime);
    }
}
