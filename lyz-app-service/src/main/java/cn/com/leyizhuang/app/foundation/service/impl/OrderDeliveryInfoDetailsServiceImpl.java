package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.OrderDeliveryInfoDetailsDAO;
import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
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
    public LogisticsInformationResponse getDeliveryByOperatorNoAndOrderNumber(String operatorNo,String orderNumber) {
        return orderDeliveryInfoDetailsDAO.getDeliveryByOperatorNoAndOrderNumber(operatorNo,orderNumber);
    }

    @Override
    public OrderDeliveryInfoDetails queryByOrderNumberAndOperatorNumber(String orderNumber, String operatorNumber) {
        return orderDeliveryInfoDetailsDAO.queryByOrderNumberAndOperatorNumber(orderNumber, operatorNumber);
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
    public List<OrderDeliveryInfoDetails> getLogisticsMessageByUserId(Long userID, Date createTime , Integer identityType) {
        return orderDeliveryInfoDetailsDAO.getLogisticsMessageByUserId(userID,createTime, AppIdentityType.getAppIdentityTypeByValue(identityType));
    }

    @Override
    public List<WaitPickUpResponse> getWaitPickUpListByOperatorNo(String operatorNo) {
        return orderDeliveryInfoDetailsDAO.getWaitPickUpListByOperatorNo(operatorNo);
    }

    @Override
    public PickUpDetailResponse getPickUpDetailByOperatorNoAndReturnNo(String operatorNo, String returnNumber) {
        return orderDeliveryInfoDetailsDAO.getPickUpDetailByOperatorNoAndReturnNo(operatorNo, returnNumber);
    }

    @Override
    public List<GiftListResponseGoods> getReturnGoods(String returnNumber) {
        return orderDeliveryInfoDetailsDAO.getReturnGoods(returnNumber);
    }
}
