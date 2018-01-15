package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.LogisticStatus;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.OrderDeliveryInfoDetailsDAO;
import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.service.OrderDeliveryInfoDetailsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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

    @Override
    public OrderDeliveryInfoDetails findByOrderNumberAndLogisticStatus(String orderNumber, LogisticStatus logisticStatus) {
        return orderDeliveryInfoDetailsDAO.findByOrderNumberAndLogisticStatus(orderNumber, logisticStatus);
    }

    @Override
    public PageInfo<AuditFinishResponse> getAuditFinishOrderByOperatorNo(Long userId, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<AuditFinishResponse> auditFinishResponseList = orderDeliveryInfoDetailsDAO.getAuditFinishOrderByOperatorNo(userId);
        return new PageInfo<>(auditFinishResponseList);
    }

    @Override
    public int countUnreadLogisticsMessage(Long userId, Integer identityType) {
        return orderDeliveryInfoDetailsDAO.countUnreadLogisticsMessage(userId, AppIdentityType.getAppIdentityTypeByValue(identityType));
    }

    @Override
    public void modifyOrderDeliveryInfoDetails(OrderDeliveryInfoDetails orderDeliveryInfoDetails) {
        orderDeliveryInfoDetailsDAO.modifyOrderDeliveryInfoDetails(orderDeliveryInfoDetails);
    }

    @Override
    public int countAuditFinishOrderByOperatorNo(Long userId) {
        return orderDeliveryInfoDetailsDAO.countAuditFinishOrderByOperatorNo(userId);
    }

    @Override
    public OrderDeliveryInfoDetails findByTaskNo(String taskNo) {
        if (StringUtils.isNotBlank(taskNo)) {
            return orderDeliveryInfoDetailsDAO.findByTaskNo(taskNo);
        }
        return null;
    }
}