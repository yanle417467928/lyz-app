package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.OrderReturnDAO;
import cn.com.leyizhuang.app.foundation.pojo.returnOrder.*;
import cn.com.leyizhuang.app.foundation.service.OrderReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by caiyu on 2017/12/4.
 */
@Service
public class OrderReturnServiceImpl implements OrderReturnService {
    @Autowired
    private OrderReturnDAO orderReturnDAO;

    @Override
    public void saveOrderReturnBaseInfo(OrderReturnBaseInfo orderReturnBaseInfo) {
        orderReturnDAO.saveOrderReturnBaseInfo(orderReturnBaseInfo);
    }

    @Override
    public void modifyOrderReturnBaseInfo(OrderReturnBaseInfo orderReturnBaseInfo) {
        orderReturnDAO.modifyOrderReturnBaseInfo(orderReturnBaseInfo);
    }

    @Override
    public OrderReturnBaseInfo queryByReturnNo(String returnNo) {
        return orderReturnDAO.queryByReturnNo(returnNo);
    }

    @Override
    public void saveOrderReturnBilling(OrderReturnBilling orderReturnBilling) {
        orderReturnDAO.saveOrderReturnBilling(orderReturnBilling);
    }

    @Override
    public void saveOrderReturnBillingDetail(OrderReturnBillingDetail orderReturnBillingDetail) {
        orderReturnDAO.saveOrderReturnBillingDetail(orderReturnBillingDetail);
    }

    @Override
    public void saveOrderReturnCashCoupon(OrderReturnCashCoupon orderReturnCashCoupon) {
        orderReturnDAO.saveOrderReturnCashCoupon(orderReturnCashCoupon);
    }

    @Override
    public void saveOrderReturnProductCoupon(OrderReturnProductCoupon orderReturnProductCoupon) {
        orderReturnDAO.saveOrderReturnProductCoupon(orderReturnProductCoupon);
    }

    @Override
    public void saveOrderReturnGoodsInfo(OrderReturnGoodsInfo orderReturnGoodsInfo) {
        orderReturnDAO.saveOrderReturnGoodsInfo(orderReturnGoodsInfo);
    }

    @Override
    public void modifyOrderReturnBillingDetail(OrderReturnBillingDetail orderReturnBillingDetail) {
        if (orderReturnBillingDetail != null) {
            orderReturnDAO.modifyOrderReturnBillingDetail(orderReturnBillingDetail);
        }
    }
}
