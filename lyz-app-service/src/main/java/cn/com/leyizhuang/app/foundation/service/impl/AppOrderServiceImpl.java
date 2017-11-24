package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.AppStoreDAO;
import cn.com.leyizhuang.app.foundation.dao.CityDAO;
import cn.com.leyizhuang.app.foundation.dao.OrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.request.OrderLockExpendRequest;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponseGoods;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jerry.Ren
 * Date: 2017/10/30.
 * Time: 18:17.
 */
@Service
public class AppOrderServiceImpl implements AppOrderService {


    @Autowired
    private AppStoreDAO appStoreDAO;

    @Autowired
    private CityDAO cityDAO;

    @Autowired
    private OrderDAO orderDAO;

    @Override
    public int lockUserExpendOfOrder(OrderLockExpendRequest lockExpendRequest) {
        return 0;
    }

    @Override
    public Boolean existGoodsStoreInventory(Long storeId, Long gid, Integer qty) {
        return appStoreDAO.existGoodsStoreInventory(storeId, gid, qty);
    }

    @Override
    public Boolean existGoodsCityInventory(Long cityId, Long gid, Integer qty) {
        return cityDAO.existGoodsCityInventory(cityId, gid, qty);
    }

    @Override
    public List<OrderBaseInfo> getOrderListByUserIDAndIdentityType(Long userID, Integer identityType) {
        return orderDAO.getOrderListByUserIDAndIdentityType(userID, AppIdentityType.getAppIdentityTypeByValue(identityType));
    }

    @Override
    public List<OrderGoodsInfo> getOrderGoodsInfoByOrderNumber(String orderNumber) {
        return orderDAO.getOrderGoodsInfoByOrderNumber(orderNumber);
    }

    @Override
    public Double getAmountPayableByOrderNumber(String orderNumber) {
        return orderDAO.getAmountPayableByOrderNumber(orderNumber);
    }

    @Override
    public Integer querySumQtyByOrderNumber(String orderNumber) {
        return orderDAO.querySumQtyByOrderNumber(orderNumber);
    }

    @Override
    public List<OrderBaseInfo> getFuzzyQuery(Long userID, Integer identityType, String condition) {
        return orderDAO.getFuzzyQuery(userID, AppIdentityType.getAppIdentityTypeByValue(identityType), condition);
    }

    @Override
    public OrderBaseInfo getOrderByOrderNumber(String outTradeNo) {
        return orderDAO.findByOrderName(outTradeNo);
    }

    @Override
    public OrderBaseInfo getOrderDetail(String orderNumber) {
        return orderDAO.getOrderDetail(orderNumber);
    }

    @Override
    public OrderLogisticsInfo getOrderLogistice(String orderNumber) {
        return orderDAO.getOrderLogistice(orderNumber);
    }

    @Override
    public OrderBillingDetails getOrderBillingDetail(String orderNumber) {
        return orderDAO.getOrderBillingDetail(orderNumber);
    }

    @Override
    public List<GiftListResponseGoods> getOrderGoodsDetails(String orderNumber) {
        return orderDAO.getOrderGoodsDetails(orderNumber);
    }
    @Override
    public List<MaterialListDO> getGoodsInfoByOrderNumber(String orderNumber) {
        return this.orderDAO.getGoodsInfoByOrderNumber(orderNumber);
    }

    @Override
    public OrderTempInfo getOrderInfoByOrderNo(String orderNo) {
        return this.orderDAO.getOrderInfoByOrderNo(orderNo);
    }

    @Override
    public OrderBillingPaymentDetails savePaymentDetails(OrderBillingPaymentDetails orderBillingPaymentDetails) {
        this.orderDAO.savePaymentDetails(orderBillingPaymentDetails);
        return orderBillingPaymentDetails;
    }

    @Override
    public OrderBillingDetails updateOwnMoneyByOrderNo(OrderBillingDetails orderBillingDetails) {
        this.orderDAO.updateOwnMoneyByOrderNo(orderBillingDetails);
        return orderBillingDetails;
    }

    @Override
    public OrderBaseInfo updateOrderStatusByOrderNo(OrderBaseInfo orderBaseInfo) {
        this.orderDAO.updateOrderStatusByOrderNo(orderBaseInfo);
        return orderBaseInfo;
    }
}
