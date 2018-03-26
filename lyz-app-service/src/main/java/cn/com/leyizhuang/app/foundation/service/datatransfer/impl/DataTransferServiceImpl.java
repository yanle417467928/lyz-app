package cn.com.leyizhuang.app.foundation.service.datatransfer.impl;

import cn.com.leyizhuang.app.core.constant.OnlinePayType;
import cn.com.leyizhuang.app.foundation.dao.TimingTaskErrorMessageDAO;
import cn.com.leyizhuang.app.foundation.dao.transferdao.TransferDAO;
import cn.com.leyizhuang.app.foundation.pojo.TimingTaskErrorMessageDO;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrder;
import cn.com.leyizhuang.app.foundation.pojo.CustomerProductCoupon;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.*;
import cn.com.leyizhuang.app.core.constant.AppCashCouponType;
import cn.com.leyizhuang.app.core.constant.CouponGetType;
import cn.com.leyizhuang.app.core.constant.OrderCouponType;
import cn.com.leyizhuang.app.foundation.pojo.CashCoupon;
import cn.com.leyizhuang.app.foundation.pojo.CashCouponCompany;
import cn.com.leyizhuang.app.foundation.pojo.CustomerCashCoupon;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrderLogistics;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrder;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrderData;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrderGoods;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOwnMoneyRecord;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderArrearsAuditDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderCouponInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderLogisticsInfo;
import cn.com.leyizhuang.app.foundation.service.datatransfer.DataTransferService;
import cn.com.leyizhuang.common.util.CountUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.com.leyizhuang.common.core.constant.ArrearsAuditStatus;
import cn.com.leyizhuang.common.util.TimeTransformUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    @Resource
    private TimingTaskErrorMessageDAO timingTaskErrorMessageDAO;

    public OrderGoodsInfo transferOne(TdOrderGoods tdOrderGoods) {
        OrderGoodsInfo goodsInfo = new OrderGoodsInfo();
        return goodsInfo;
    }

    @Override
    public List<String> getTransferStoreMainOrderNumber(Date startTime, Date endTime) {
        if (null != startTime && null != endTime) {
            return transferDAO.getTransferStoreMainOrderNumber(startTime, endTime);
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
    public List<TdOrderLogistics> queryOrderLogistcs(int size) {
        return transferDAO.queryOrderLogistcs(size);
    }

    @Override
    public void saveOrderLogisticsInfo(OrderLogisticsInfo orderLogisticsInfo) {
        transferDAO.saveOrderLogisticsInfo(orderLogisticsInfo);
    }

    @Override
    public List<OrderBaseInfo> findNewOrderNumber() {
        return this.transferDAO.findNewOrderNumber();
    }

    @Override
    @Transactional
    public void transferArrearsAudit(String orderNumber) {

        Boolean exist = this.transferDAO.existArrearsAudit(orderNumber);
        if (exist) {
            return;
        }
        List<TdOwnMoneyRecord> ownMoneyRecords = this.transferDAO.findOwnMoneyRecordByOrderNumber(orderNumber);
        if (null == ownMoneyRecords || ownMoneyRecords.size() == 0) {
            return;
        }
        for (int j = 0; j < ownMoneyRecords.size(); j++) {
            Boolean exists = this.transferDAO.existArrearsAudit(orderNumber);
            if (exists) {
                return;
            }
            TdOwnMoneyRecord ownMoneyRecord = ownMoneyRecords.get(j);
            List<TdOrder> orders = this.transferDAO.findOrderByOrderNumber(orderNumber);
            if (null == orders || orders.size() == 0) {

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
                if (null != clerkNo && !"".equals(clerkNo)) {
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
            if (null != ownMoneyRecord.getMoney() && ownMoneyRecord.getMoney() > 0D) {
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
    @Transactional
    public void transferCoupon(OrderBaseInfo baseInfo) {

        String orderNumber = baseInfo.getOrderNumber();
        TdOrderData orderData = this.transferDAO.findOrderDataByOrderNumber(orderNumber);
        OrderBaseInfo orderBaseInfo = this.transferDAO.findNewOrderByOrderNumber(orderNumber);
        if(null != orderData && null != orderData.getCashCouponFee() && orderData.getCashCouponFee() > 0D){
            List<OrderCouponInfo> list = this.transferDAO.findCouponInfoListByType(orderNumber, OrderCouponType.CASH_COUPON);
            if (null != list && list.size() > 0) {
                return;
            }

            CashCoupon cashCoupon = new CashCoupon();
            cashCoupon.setCreateTime(new Date());
            cashCoupon.setDenomination(orderData.getCashCouponFee());
            cashCoupon.setEffectiveStartTime(new Date());
            cashCoupon.setDescription("无条件使用");
            cashCoupon.setInitialQuantity(1);
            cashCoupon.setRemainingQuantity(0);
            cashCoupon.setTitle("优惠券");
            cashCoupon.setCityId(orderBaseInfo.getCityId());
            cashCoupon.setCityName(orderBaseInfo.getCityName());
            cashCoupon.setType(AppCashCouponType.COMPANY);
            cashCoupon.setIsSpecifiedStore(false);
            cashCoupon.setStatus(true);
            cashCoupon.setSortId(999);
            this.transferDAO.addCashCoupon(cashCoupon);

            CashCouponCompany cashCouponCompany = new CashCouponCompany();
            cashCouponCompany.setCcid(cashCoupon.getId());
            cashCouponCompany.setCompanyName("华润");
            cashCouponCompany.setCompanyFlag("HR");
            this.transferDAO.addCashCouponCompany(cashCouponCompany);

            CustomerCashCoupon customerCashCoupon = new CustomerCashCoupon();
            customerCashCoupon.setCusId(orderBaseInfo.getCustomerId());
            customerCashCoupon.setCcid(cashCoupon.getId());
            customerCashCoupon.setQty(1);
            customerCashCoupon.setIsUsed(true);
            customerCashCoupon.setUseTime(orderBaseInfo.getCreateTime());
            customerCashCoupon.setUseOrderNumber(orderNumber);
            customerCashCoupon.setGetTime(orderBaseInfo.getCreateTime());
            customerCashCoupon.setDenomination(orderData.getCashCouponFee());
            customerCashCoupon.setPurchasePrice(0D);
            customerCashCoupon.setEffectiveStartTime(new Date());
            customerCashCoupon.setDescription("无条件使用");
            customerCashCoupon.setTitle("优惠券");
            customerCashCoupon.setStatus(true);
            customerCashCoupon.setGetType(CouponGetType.HISTORY_IMPORT);
            customerCashCoupon.setCityId(orderBaseInfo.getCityId());
            customerCashCoupon.setCityName(orderBaseInfo.getCityName());
            customerCashCoupon.setType(AppCashCouponType.COMPANY);
            customerCashCoupon.setIsSpecifiedStore(false);
            this.transferDAO.addCustomerCashCoupon(customerCashCoupon);

            OrderCouponInfo orderCouponInfo = new OrderCouponInfo();
            orderCouponInfo.setOid(baseInfo.getId());
            orderCouponInfo.setOrderNumber(orderNumber);
            orderCouponInfo.setCouponId(customerCashCoupon.getId());
            orderCouponInfo.setCouponType(OrderCouponType.CASH_COUPON);
            orderCouponInfo.setPurchasePrice(0D);
            orderCouponInfo.setCostPrice(orderData.getCashCouponFee());
            orderCouponInfo.setGetType(CouponGetType.HISTORY_IMPORT);
            this.transferDAO.saveOrderCouponInfo(orderCouponInfo);
        }
        List<TdOrder> orders = this.transferDAO.findOrderInfoByOrderNumber(orderNumber);
        if (null == orders || orders.size() ==0){
            return;
        }
        List<OrderCouponInfo> list = this.transferDAO.findCouponInfoListByType(orderNumber, OrderCouponType.PRODUCT_COUPON);
        if (null != list && list.size() > 0) {
            return;
        }
        for (int j = 0; j < orders.size(); j++) {
            List<TdOrderGoods> orderGoodsList = this.transferDAO.getTdOrderGoodsByOrderNumber(orders.get(j).getId());
            if (null == orderGoodsList || orderGoodsList.size() ==0){
                return;
            }
            for (int k = 0; k < orderGoodsList.size(); k++) {
                TdOrderGoods tdOrderGoods = orderGoodsList.get(k);
                if (null != tdOrderGoods.getCouponNumber() && tdOrderGoods.getCouponNumber() > 0){

                    List<TdCoupon> couponList = this.transferDAO.getCouponListBySkuAndOrderNumber(tdOrderGoods.getSku(), orders.get(j).getOrderNumber());
                    if (null == couponList || couponList.size() ==0){
                        return;
                    }
                    for (int l = 0; l < couponList.size(); l++) {
                        TdCoupon tdCoupon = couponList.get(l);

                        CustomerProductCoupon productCoupon = new CustomerProductCoupon();
                        GoodsDO goodsDO = this.transferDAO.getGoodsBySku(tdOrderGoods.getSku());
                        productCoupon.setCustomerId(orderBaseInfo.getCustomerId());
                        productCoupon.setGoodsId(goodsDO.getGid());
                        productCoupon.setQuantity(1);
                        if (null != tdCoupon.getIsBuy() && tdCoupon.getIsBuy()) {
                            productCoupon.setGetType(CouponGetType.BUY);
                        } else {
                            productCoupon.setGetType(CouponGetType.MANUAL_GRANT);
                        }
                        productCoupon.setGetTime(new Date());
                        productCoupon.setEffectiveStartTime(new Date());
                        productCoupon.setIsUsed(true);
                        productCoupon.setUseTime(new Date());
                        productCoupon.setUseOrderNumber(orderNumber);
                        productCoupon.setBuyPrice(tdCoupon.getBuyPrice());
                        productCoupon.setStoreId(orderBaseInfo.getStoreId());
                        productCoupon.setSellerId(orderBaseInfo.getSalesConsultId());
                        productCoupon.setStatus(true);
                        this.transferDAO.addCustomerProductCoupon(productCoupon);

                        OrderCouponInfo orderCouponInfo = new OrderCouponInfo();
                        orderCouponInfo.setOid(baseInfo.getId());
                        orderCouponInfo.setOrderNumber(orderNumber);
                        orderCouponInfo.setCouponId(productCoupon.getId());
                        orderCouponInfo.setCouponType(OrderCouponType.PRODUCT_COUPON);
                        orderCouponInfo.setPurchasePrice(tdCoupon.getBuyPrice());
                        orderCouponInfo.setCostPrice(tdOrderGoods.getRealPrice());
                        orderCouponInfo.setGetType(CouponGetType.HISTORY_IMPORT);
                        orderCouponInfo.setSku(tdOrderGoods.getSku());
                        this.transferDAO.saveOrderCouponInfo(orderCouponInfo);
                    }
                }
            }
        }
    }

    @Override
    public void transferOrderBillingDetails() {
        List<OrderBaseInfo> orderBaseInfoList = this.transferDAO.findNewOrderNumber();
        TimingTaskErrorMessageDO timingTaskErrorMessageDO = null;
        if (null != orderBaseInfoList && orderBaseInfoList.size() > 0) {
            for (OrderBaseInfo orderBaseInfo : orderBaseInfoList) {
                TdOrderData tdOrderData = this.transferDAO.findOrderDataByOrderNumber(orderBaseInfo.getOrderNumber());
                TdOwnMoneyRecord tdOwnMoneyRecord = this.transferDAO.getOwnMoneyRecordByOrderNumber(orderBaseInfo.getOrderNumber());
                //获取错误信息
                timingTaskErrorMessageDO = timingTaskErrorMessageDAO.findTimingTaskErrorMessageByOrderNumber(orderBaseInfo.getOrderNumber());
                try {
                    Boolean b = this.transferDAO.existOrderBillingDetails(orderBaseInfo.getOrderNumber());
                    if (b){
                        throw new Exception("此订单号账单已生成请检查！订单号："+ orderBaseInfo.getOrderNumber());
                    }
                    OrderBillingDetails orderBillingDetails = new OrderBillingDetails();
                    orderBillingDetails.setOid(orderBaseInfo.getId());
                    orderBillingDetails.setCreateTime(tdOrderData.getCreateTime());
                    orderBillingDetails.setOrderNumber(tdOrderData.getMainOrderNumber());
                    orderBillingDetails.setTotalGoodsPrice(tdOrderData.getTotalGoodsPrice());
                    orderBillingDetails.setMemberDiscount(tdOrderData.getMemberDiscount());
                    orderBillingDetails.setPromotionDiscount(tdOrderData.getActivitySub());
                    orderBillingDetails.setFreight(tdOrderData.getDeliveryFee());
                    orderBillingDetails.setUpstairsFee(0D);
                    orderBillingDetails.setLebiCashDiscount(0D);
                    orderBillingDetails.setLebiQuantity(0);
                    orderBillingDetails.setCashCouponDiscount(tdOrderData.getCashCouponFee());
                    orderBillingDetails.setProductCouponDiscount(tdOrderData.getProCouponFee());
                    orderBillingDetails.setCusPreDeposit(0D);
                    orderBillingDetails.setOnlinePayType(OnlinePayType.NO);
                    orderBillingDetails.setOnlinePayAmount(0D);
                    orderBillingDetails.setOnlinePayTime(null);
                    Double stPreDepsit = CountUtil.add(tdOrderData.getBalanceUsed() == null ? 0D : tdOrderData.getBalanceUsed(), tdOrderData.getOnlinePay() == null ? 0D : tdOrderData.getOnlinePay());
                    orderBillingDetails.setStPreDeposit(stPreDepsit);
                    orderBillingDetails.setEmpCreditMoney(tdOrderData.getLeftPrice());
                    orderBillingDetails.setStoreCreditMoney(0D);
                    orderBillingDetails.setStoreSubvention(0D);
                    Double totalGoodsPrice = CountUtil.sub(tdOrderData.getTotalGoodsPrice() == null ? 0D : tdOrderData.getTotalGoodsPrice(), tdOrderData.getMemberDiscount() == null ? 0D : tdOrderData.getMemberDiscount(),
                            tdOrderData.getActivitySub() == null ? 0D : tdOrderData.getActivitySub(), tdOrderData.getCashCouponFee() == null ? 0D : tdOrderData.getCashCouponFee(), tdOrderData.getProCouponFee() == null ? 0D : tdOrderData.getProCouponFee());
                    Double orderAmountSubTotal = CountUtil.add(totalGoodsPrice == null ? 0d : totalGoodsPrice, tdOrderData.getDeliveryFee());
                    orderBillingDetails.setOrderAmountSubtotal(orderAmountSubTotal);
                    orderBillingDetails.setAmountPayable(CountUtil.sub(orderAmountSubTotal, stPreDepsit));
                    orderBillingDetails.setCollectionAmount(tdOrderData.getAgencyRefund());
                    orderBillingDetails.setArrearage(tdOrderData.getDue());
                    orderBillingDetails.setIsOwnerReceiving(Boolean.FALSE);
                    orderBillingDetails.setIsPayUp(tdOwnMoneyRecord.getIsPayed());
                    if (tdOwnMoneyRecord.getIsPayed()) {
                        orderBillingDetails.setPayUpTime(new Date());
                    } else {
                        orderBillingDetails.setPayUpTime(null);
                    }
                    orderBillingDetails.setJxPriceDifferenceAmount(0D);
                    orderBillingDetails.setStoreCash(tdOrderData.getSellerCash());
                    orderBillingDetails.setStoreOtherMoney(tdOrderData.getSellerOther());
                    orderBillingDetails.setStorePosMoney(tdOrderData.getSellerPos());
                    orderBillingDetails.setStorePosNumber(tdOwnMoneyRecord.getSerialNumber());
                    orderBillingDetails.setDeliveryCash(tdOrderData.getDeliveryCash());
                    orderBillingDetails.setDeliveryPos(tdOrderData.getDeliveryPos());

                    this.transferDAO.saveOrderBillingDetails(orderBillingDetails);
                }catch (Exception e){
                    System.out.println(e);
                    System.out.println("订单账单创建失败请检查，订单号："+orderBaseInfo.getOrderNumber());
                    if (null  == timingTaskErrorMessageDO) {
                        timingTaskErrorMessageDO = new TimingTaskErrorMessageDO();
                        timingTaskErrorMessageDO.setMessage("订单账单创建失败请检查，订单号："+orderBaseInfo.getOrderNumber());
                        timingTaskErrorMessageDO.setOrderNumber(orderBaseInfo.getOrderNumber());
                        timingTaskErrorMessageDO.setRecordTime(new Date());
                        timingTaskErrorMessageDAO.saveTimingTaskErrorMessage(timingTaskErrorMessageDO);
                    }
                }
            }
        }

    }

    @Override
    public AppEmployee findFitEmployeeInfoById(Long userId) {
        return null;
    }

    @Override
    public AppEmployee findStoreEmployeeById(Long sellerId) {
        return null;
    }

    @Override
    public AppCustomer findCustomerById(Long userId) {
        return null;
    }
}
