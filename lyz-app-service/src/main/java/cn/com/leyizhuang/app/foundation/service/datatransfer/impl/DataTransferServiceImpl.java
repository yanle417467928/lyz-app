package cn.com.leyizhuang.app.foundation.service.datatransfer.impl;

import cn.com.leyizhuang.app.foundation.dao.transferdao.TransferDAO;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrder;
import cn.com.leyizhuang.app.core.constant.CouponGetType;
import cn.com.leyizhuang.app.core.constant.OrderCouponType;
import cn.com.leyizhuang.app.foundation.dao.transferdao.TransferDAO;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrder;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrderData;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrderGoods;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOwnMoneyRecord;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderArrearsAuditDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderCouponInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.service.datatransfer.DataTransferService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import cn.com.leyizhuang.common.core.constant.ArrearsAuditStatus;
import cn.com.leyizhuang.common.util.TimeTransformUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单商品转换类
 *
 * @author Richard
 * @date 2018/3/24
 */
@Service
public class DataTransferServiceImpl implements DataTransferService {

    @Resource
    private TransferDAO transferDAO;

    @Autowired
    private TransferDAO transferDAO;

    public OrderGoodsInfo transferOne(TdOrderGoods tdOrderGoods){
        OrderGoodsInfo goodsInfo = new OrderGoodsInfo();
        return goodsInfo;
    }

    @Override
    public List<String> getTransferStoreMainOrderNumber(Date startTime, Date endTime) {
        if (null != startTime && null != endTime){
            return transferDAO.getTransferStoreMainOrderNumber(startTime,endTime);
        }
        return null;
    }

    @Override
    public TdOrder getMainOrderInfoByMainOrderNumber(String mainOrderNumber) {
        if (null != mainOrderNumber){
            return transferDAO.getMainOrderInfoByMainOrderNumber(mainOrderNumber);
        }
        return null;
    }



    @Override
    public void TransferArrearsAudit() {
        List<OrderBaseInfo> orderNumberList = this.transferDAO.findNewOrderNumber();
        if (null == orderNumberList && orderNumberList.size() == 0) {
            return;
        }
        for (int i = 0; i < orderNumberList.size(); i++) {
            String orderNumber = orderNumberList.get(i).getOrderNumber();
            Boolean exist = this.transferDAO.existArrearsAudit(orderNumber);
            if (exist) {
                return;
            }
            List<TdOwnMoneyRecord> ownMoneyRecords = this.transferDAO.findOwnMoneyRecordByOrderNumber(orderNumber);
            if (null == ownMoneyRecords && ownMoneyRecords.size() == 0) {
                return;
            }
            for (int j = 0; j < ownMoneyRecords.size(); j++) {
                TdOwnMoneyRecord ownMoneyRecord = ownMoneyRecords.get(j);
                List<TdOrder> orders = this.transferDAO.findOrderByOrderNumber(orderNumber);
                if (null == orders && orders.size() == 0) {

                    return;
                }
                TdOrderData agencyRefund = this.transferDAO.findOrderDataByOrderNumber(orderNumber);
                if (null == agencyRefund) {

                    return;
                }
                TdOrder order = orders.get(0);
                Long employeeId = this.transferDAO.findEmployeeByMobile(order.getSellerUsername());
                if (null == employeeId) {

                    return;
                }
                OrderArrearsAuditDO auditDO = new OrderArrearsAuditDO();
                String clerkNo = null;
                for (int k = 0; k < orders.size(); k++) {
                    clerkNo = this.transferDAO.findDeliveryInfoByOrderNumber(orders.get(k).getOrderNumber());
                    if (null != clerkNo && "".equals(clerkNo)) {
                        break;
                    }
                }
                Long deliveryId = this.transferDAO.findDeliveryInfoByClerkNo(clerkNo);
                auditDO.setUserId(deliveryId);
                auditDO.setOrderNumber(orderNumber);
                auditDO.setCustomerName(order.getRealUserRealName());
                auditDO.setCustomerPhone(order.getRealUserUsername());
                auditDO.setSellerId(employeeId);
                auditDO.setSellerName(order.getSellerRealName());
                auditDO.setSellerphone(order.getSellerUsername());
                auditDO.setDistributionAddress(order.getShippingAddress());
                auditDO.setDistributionTime(TimeTransformUtils.UDateToLocalDateTime(ownMoneyRecord.getCreateTime()));
                auditDO.setAgencyMoney(agencyRefund.getAgencyRefund());
                auditDO.setOrderMoney(ownMoneyRecord.getOwned());
                auditDO.setRealMoney(ownMoneyRecord.getPayed());
                if (null != ownMoneyRecord.getMoney() && ownMoneyRecord.getMoney() > 0D){
                    auditDO.setPaymentMethod("现金");
                } else {
                    auditDO.setPaymentMethod("POS");
                }
                if (null == ownMoneyRecord.getIspassed()) {
                    auditDO.setStatus(ArrearsAuditStatus.AUDITING);
                } else if (ownMoneyRecord.getIspassed()) {
                    auditDO.setStatus(ArrearsAuditStatus.AUDIT_PASSED);
                } else {
                    auditDO.setStatus(ArrearsAuditStatus.AUDIT_NO);
                }
                auditDO.setCashMoney(ownMoneyRecord.getMoney());
                auditDO.setPosMoney(ownMoneyRecord.getPos());
                auditDO.setAlipayMoney(0D);
                auditDO.setWechatMoney(0D);
                auditDO.setCreateTime(LocalDateTime.now());
                auditDO.setWhetherRepayments(ownMoneyRecord.getIsPayed());
                this.transferDAO.insertArrearsAudit(auditDO);
            }
        }
    }

    @Override
    public List<TdDeliveryInfoDetails> queryDeliveryTimeSeqBySize(int size) {
        return transferDAO.queryDeliveryTimeSeqBySize(size);
    }

    @Override
    public TdDeliveryInfoDetails queryDeliveryInfoDetailByOrderNumber(String orderNo) {
        return transferDAO.queryDeliveryInfoDetailByOrderNumber(orderNo);
    }

    @Override
    public List<TdDeliveryInfoDetails> queryTdOrderListBySize(int size) {
        return transferDAO.queryTdOrderListBySize(size);
    }

    @Override
    public List<TdDeliveryInfoDetails> queryOrderGoodsListByOrderNumber(Long id) {
        return transferDAO.queryOrderGoodsListByOrderNumber(id);
    }

    @Override
    public void TransferCoupon() {
        List<OrderBaseInfo> orderNumberList = this.transferDAO.findNewOrderNumber();
        if (null == orderNumberList && orderNumberList.size() == 0) {
            return;
        }

        for (int i = 0; i < orderNumberList.size(); i++) {
            String orderNumber = orderNumberList.get(i).getOrderNumber();
            TdOrderData orderData = this.transferDAO.findOrderDataByOrderNumber(orderNumber);
            if(null != orderData && null != orderData.getCashCouponFee() && orderData.getCashCouponFee() > 0){
                OrderCouponInfo orderCouponInfo = new OrderCouponInfo();
                orderCouponInfo.setOid(orderNumberList.get(i).getId());
                orderCouponInfo.setOrderNumber(orderNumber);
                orderCouponInfo.setCouponType(OrderCouponType.CASH_COUPON);
                orderCouponInfo.setPurchasePrice(0D);
                orderCouponInfo.setCostPrice(orderData.getCashCouponFee());
                orderCouponInfo.setGetType(CouponGetType.HISTORY_IMPORT);


            }

        }

    }
}
