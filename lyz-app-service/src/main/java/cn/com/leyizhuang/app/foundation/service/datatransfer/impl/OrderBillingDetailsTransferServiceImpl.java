package cn.com.leyizhuang.app.foundation.service.datatransfer.impl;

import cn.com.leyizhuang.app.core.constant.AppOrderSubjectType;
import cn.com.leyizhuang.app.core.constant.OnlinePayType;
import cn.com.leyizhuang.app.foundation.dao.GoodsDAO;
import cn.com.leyizhuang.app.foundation.dao.OrderDAO;
import cn.com.leyizhuang.app.foundation.dao.TimingTaskErrorMessageDAO;
import cn.com.leyizhuang.app.foundation.dao.transferdao.TransferDAO;
import cn.com.leyizhuang.app.foundation.pojo.TimingTaskErrorMessageDO;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrder;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrderData;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrderGoods;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOwnMoneyRecord;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.service.datatransfer.OrderBillingTransferService;
import cn.com.leyizhuang.common.util.CountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by caiyu on 2018/3/27.
 */
@Service
public class OrderBillingDetailsTransferServiceImpl implements OrderBillingTransferService {
    private static final Logger logger = LoggerFactory.getLogger(OrderGoodsInoTransferServiceImpl.class);

    @Autowired
    private TransferDAO transferDAO;
    @Resource
    private TimingTaskErrorMessageDAO timingTaskErrorMessageDAO;

    private volatile Long transferNum = 0L;

    // 线程池
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public void transferOrderBillingDetails() {

        List<OrderBillingDetails> orderBillingDetailsList = new ArrayList<>();

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
                    if (b) {
                        throw new Exception("此订单号账单已生成请检查！订单号：" + orderBaseInfo.getOrderNumber());
                    }
                    if (orderBaseInfo.getOrderSubjectType() == AppOrderSubjectType.STORE) {
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
                        orderBillingDetails.setIsPayUp(tdOrderData.getDue()>0?Boolean.FALSE:Boolean.TRUE);
                        if (tdOrderData.getDue()>0) {
                            orderBillingDetails.setPayUpTime(null);
                        } else {
                            orderBillingDetails.setPayUpTime(new Date());
                        }
                        Double jxTotalPrice = 0.00;
                        List<TdOrder> tdOrderList = this.transferDAO.findOrderInfoByOrderNumber(orderBaseInfo.getOrderNumber());
                        if (null != tdOrderList) {
                            for (TdOrder tdOrder : tdOrderList) {
                                jxTotalPrice = CountUtil.add(jxTotalPrice, tdOrder.getJxTotalPrice() == null ? 0D : tdOrder.getJxTotalPrice());
                            }
                        } else {
                            throw new Exception("未查到此订单头信息！订单号：" + orderBaseInfo.getOrderNumber());
                        }
                        orderBillingDetails.setJxPriceDifferenceAmount(jxTotalPrice);
                        orderBillingDetails.setStoreCash(tdOrderData.getSellerCash());
                        orderBillingDetails.setStoreOtherMoney(tdOrderData.getSellerOther());
                        orderBillingDetails.setStorePosMoney(tdOrderData.getSellerPos());
                        orderBillingDetails.setStorePosNumber(tdOwnMoneyRecord==null?null:tdOwnMoneyRecord.getSerialNumber());
                        orderBillingDetails.setDeliveryCash(tdOrderData.getDeliveryCash());
                        orderBillingDetails.setDeliveryPos(tdOrderData.getDeliveryPos());
                        orderBillingDetailsList.add(orderBillingDetails);
                    } else {
                        OrderBillingDetails orderBillingDetails = new OrderBillingDetails();
                        Double totalGoodsPrice = 0.00;
                        Double storePreDeposit = 0.00;
                        Double creditMoney = 0.00;
                        Double memberMoney = 0.00;
                        Double memberTotalMoney = 0.00;
                        Date date = null;
                        List<TdOrder> tdOrderList = this.transferDAO.findOrderInfoByOrderNumber(orderBaseInfo.getOrderNumber());
                        if (null != tdOrderList) {
                            for (TdOrder tdOrder : tdOrderList) {
                                totalGoodsPrice = CountUtil.add(totalGoodsPrice, tdOrder.getTotalGoodsPrice() == null ? 0D : tdOrder.getTotalGoodsPrice());
                                storePreDeposit = CountUtil.add(storePreDeposit, tdOrder.getWalletMoney() == null ? 0D : tdOrder.getWalletMoney());
                                creditMoney = CountUtil.add(creditMoney, tdOrder.getCredit() == null ? 0D : tdOrder.getCredit());
                                date = tdOrder.getPayTime() == null ? new Date() : tdOrder.getPayTime();
                                List<TdOrderGoods> goodsList = this.transferDAO.getTdOrderGoodsByOrderNumber(tdOrder.getId());
                                if (null != goodsList && goodsList.size() > 0) {
                                    for (TdOrderGoods goods : goodsList) {
                                        memberMoney = CountUtil.sub(goods.getPrice() == null ? 0D : goods.getPrice(), goods.getRealPrice() == null ? 0D : goods.getRealPrice());
                                        memberTotalMoney = CountUtil.add(memberTotalMoney, memberMoney);
                                    }
                                }
                            }
                        } else {
                            throw new Exception("未查到此订单头信息！订单号：" + orderBaseInfo.getOrderNumber());
                        }
                        orderBillingDetails.setOid(orderBaseInfo.getId());
                        orderBillingDetails.setCreateTime(date);
                        orderBillingDetails.setOrderNumber(orderBaseInfo.getOrderNumber());
                        orderBillingDetails.setTotalGoodsPrice(totalGoodsPrice);
                        orderBillingDetails.setMemberDiscount(memberTotalMoney);
                        orderBillingDetails.setPromotionDiscount(0D);
                        orderBillingDetails.setFreight(0D);
                        orderBillingDetails.setUpstairsFee(0D);
                        orderBillingDetails.setLebiCashDiscount(0D);
                        orderBillingDetails.setLebiQuantity(0);
                        orderBillingDetails.setCashCouponDiscount(0D);
                        orderBillingDetails.setProductCouponDiscount(0D);
                        orderBillingDetails.setCusPreDeposit(0D);
                        orderBillingDetails.setOnlinePayType(OnlinePayType.NO);
                        orderBillingDetails.setOnlinePayAmount(0D);
                        orderBillingDetails.setOnlinePayTime(null);
                        orderBillingDetails.setStPreDeposit(storePreDeposit);
                        orderBillingDetails.setEmpCreditMoney(0D);
                        orderBillingDetails.setStoreCreditMoney(creditMoney);
                        orderBillingDetails.setStoreSubvention(0D);
                        Double orderAmountSubtotal = CountUtil.sub(totalGoodsPrice, memberTotalMoney);
                        orderBillingDetails.setOrderAmountSubtotal(orderAmountSubtotal);
                        orderBillingDetails.setAmountPayable(CountUtil.sub(orderAmountSubtotal, storePreDeposit, creditMoney));
                        orderBillingDetails.setCollectionAmount(0D);
                        orderBillingDetails.setArrearage(0D);
                        orderBillingDetails.setIsOwnerReceiving(Boolean.FALSE);
                        orderBillingDetails.setIsPayUp(Boolean.TRUE);
                        orderBillingDetails.setPayUpTime(date);
                        Double jxTotalPrice = 0.00;
                        orderBillingDetails.setJxPriceDifferenceAmount(0D);
                        orderBillingDetails.setStoreCash(0D);
                        orderBillingDetails.setStoreOtherMoney(0D);
                        orderBillingDetails.setStorePosMoney(tdOrderData.getSellerPos());
                        orderBillingDetails.setStorePosNumber(null);
                        orderBillingDetails.setDeliveryCash(0D);
                        orderBillingDetails.setDeliveryPos(0D);
                        orderBillingDetailsList.add(orderBillingDetails);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("订单账单创建失败请检查，订单号：" + orderBaseInfo.getOrderNumber());
                    if (null == timingTaskErrorMessageDO) {
                        timingTaskErrorMessageDO = new TimingTaskErrorMessageDO();
                        timingTaskErrorMessageDO.setMessage("订单账单创建失败请检查，订单号：" + orderBaseInfo.getOrderNumber());
                        timingTaskErrorMessageDO.setOrderNumber(orderBaseInfo.getOrderNumber());
                        timingTaskErrorMessageDO.setRecordTime(new Date());
                        timingTaskErrorMessageDAO.saveTimingTaskErrorMessage(timingTaskErrorMessageDO);
                    }
                    throw  new  RuntimeException();
                }
            }
            this.saveOrderBillingDetailsAsync(orderBillingDetailsList);
        }
    }



    private void saveOrderBillingDetailsAsync(final List<OrderBillingDetails> orderBillingDetailsList) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // 插入
                for (OrderBillingDetails orderBillingDetails : orderBillingDetailsList) {
                    transferDAO.saveOrderBillingDetails(orderBillingDetails);
                }
                logger.info("处理订单账单导入总条数："+ transferNum);
            }
        });
    }
}
