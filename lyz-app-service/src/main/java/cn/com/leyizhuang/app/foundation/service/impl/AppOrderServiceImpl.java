package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.foundation.dao.AppStoreDAO;
import cn.com.leyizhuang.app.foundation.dao.ArrearsAuditDAO;
import cn.com.leyizhuang.app.foundation.dao.CityDAO;
import cn.com.leyizhuang.app.foundation.dao.OrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.request.OrderLockExpendRequest;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponseGoods;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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

    @Autowired
    private ArrearsAuditDAO arrearsAuditDAO;

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
    public List<OrderBaseInfo> getOrderListByUserIDAndIdentityType(Long userID, Integer identityType, Integer showStatus) {
        return orderDAO.getOrderListByUserIDAndIdentityType(userID, AppIdentityType.getAppIdentityTypeByValue(identityType),showStatus);
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

    @Override
    @Transactional
    public void saveOrderBillingPaymentDetails(String orderNumber, Double money, String replyNumber,String receiptNumber) {
        OrderBillingPaymentDetails orderBillingPaymentDetails = new OrderBillingPaymentDetails();
        OrderBaseInfo orderBaseInfo = orderDAO.getOrderDetail(orderNumber);
        if (null != orderBaseInfo){
            orderBillingPaymentDetails.setOrderId(orderBaseInfo.getId());
        }
        orderBillingPaymentDetails.setOrderNumber(orderNumber);
        Date repaymentTime = new Date();
        orderBillingPaymentDetails.setPayTime(repaymentTime);
        orderBillingPaymentDetails.setPayType(OrderBillingPaymentType.ALIPAY);
        //orderBillingPaymentDetails.setCurrencyType("实际货币");
        orderBillingPaymentDetails.setAmount(money);
        orderBillingPaymentDetails.setReplyCode(replyNumber);
        orderBillingPaymentDetails.setReceiptNumber(receiptNumber);
        //保存还款记录
        orderDAO.savePaymentDetails(orderBillingPaymentDetails);
        //导购欠款还款后修改欠款审核表
        arrearsAuditDAO.updateStatusAndrRepaymentTimeByOrderNumber(repaymentTime,orderNumber);
    }
}
