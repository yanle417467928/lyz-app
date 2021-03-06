package cn.com.leyizhuang.app.web.controller.rest.datatransfer;

import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.datatransfer.DataTransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jerry.Ren
 * create 2018-03-26 14:11
 * desc:
 **/

@RestController
@Slf4j
public class OrderBillingPaymentDetailController {

    @Autowired
    private DataTransferService dataTransferService;
    @Autowired
    private AppStoreService appStoreService;
    @Autowired
    private AppOrderService appOrderService;

    // 线程池
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     * 运行此方法需要订单基础表(ord_base_info)和门店基础表(st_store)数据支持
     */
    @RequestMapping(value = "/app/resend/wms/test/billingPayment/detail", method = RequestMethod.GET)
    public String transform() {

        int size = 0;

//        for (int i = 0; i < 200; i++) {
//            Boolean hasNotDatas = this.saveOrderBillingPaymentDetail(size);
//            if (hasNotDatas) {
//                return "********************转换账单支付明细表结束*************************";
//            }
//            size += 1000;
//        }
        log.info("********************转换账单支付明细表结束*************************");
        return "按理说不应该执行到这儿!";
    }

    /**
     * 运行此方法需要订单基础表(ord_base_info)数据支持
     */
    @RequestMapping(value = "/app/billingPayment/detail/fix", method = RequestMethod.GET)
    public String transformFix() {

        int size = 0;

        for (int i = 0; i < 200; i++) {
//            Boolean hasNotDatas = this.saveFixDiySiteBillingPaymentDetail(size);
//            if (hasNotDatas) {
//                return "********************转换装饰公司账单支付明细表结束*************************";
//            }
//            size += 1000;
        }
        log.info("********************转换装饰公司账单支付明细表结束*************************");
        return "按理说不应该执行到这儿!";
    }

//    private Boolean saveOrderBillingPaymentDetail(int size) {
//
//        List<TdOrderData> orderDataList = dataTransferService.queryTdOrderDataListBySize(size);
//        try {
//            if (AssertUtil.isNotEmpty(orderDataList)) {
//                for (TdOrderData tdOrderData : orderDataList) {
//                    TdOrder tdOrder = dataTransferService.getMainOrderInfoByMainOrderNumber(tdOrderData.getMainOrderNumber());
//                    AppStore appStore = appStoreService.findByStoreCode(tdOrderData.getStoreCode());
//                    OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(tdOrderData.getMainOrderNumber());
//                    if (AssertUtil.isNotEmpty(tdOrder) && null != orderBaseInfo) {
//                        if ((null != tdOrderData.getOnlinePay() && tdOrderData.getOnlinePay() > AppConstant.PAY_UP_LIMIT)
//                                || (null != tdOrderData.getBalanceUsed() && tdOrderData.getBalanceUsed() > AppConstant.PAY_UP_LIMIT)) {
//                            OrderBillingPaymentDetails paymentDetails = new OrderBillingPaymentDetails();
//                            paymentDetails.setOrderId(orderBaseInfo.getId());
//                            paymentDetails.setOrderNumber(tdOrderData.getMainOrderNumber());
//                            paymentDetails.setCreateTime(tdOrder.getPayTime());
//                            paymentDetails.setPayTime(tdOrder.getPayTime());
//                            paymentDetails.setPayType(OrderBillingPaymentType.ST_PREPAY);
//                            paymentDetails.setPayTypeDesc(OrderBillingPaymentType.ST_PREPAY.getDescription());
//
//                            if (tdOrder.getIsSellerOrder()) {
//                                paymentDetails.setPaymentSubjectType(PaymentSubjectType.SELLER);
//                                paymentDetails.setPaymentSubjectTypeDesc(PaymentSubjectType.SELLER.getDescription());
//                            } else {
//                                paymentDetails.setPaymentSubjectType(PaymentSubjectType.CUSTOMER);
//                                paymentDetails.setPaymentSubjectTypeDesc(PaymentSubjectType.CUSTOMER.getDescription());
//                            }
//                            Double amount = CountUtil.add(null == tdOrderData.getOnlinePay() ? 0D : tdOrderData.getOnlinePay(),
//                                    null == tdOrderData.getBalanceUsed() ? 0D : tdOrderData.getBalanceUsed());
//                            paymentDetails.setAmount(amount);
//
//                            if (null != appStore) {
//                                paymentDetails.setReceiptNumber(OrderUtils.generateReceiptNumber(appStore.getCityId()));
//                            }
//                            executorService.execute(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    appOrderService.saveOrderBillingPaymentDetail(paymentDetails);
//                                }
//                            });
//                        } else if (null != tdOrderData.getDeliveryCash() && tdOrderData.getDeliveryCash() > AppConstant.PAY_UP_LIMIT) {
//                            OrderBillingPaymentDetails paymentDetails = new OrderBillingPaymentDetails();
//                            paymentDetails.setOrderId(orderBaseInfo.getId());
//                            paymentDetails.setOrderNumber(tdOrderData.getMainOrderNumber());
//                            paymentDetails.setCreateTime(tdOrder.getPayTime());
//                            paymentDetails.setPayTime(tdOrder.getPayTime());
//                            paymentDetails.setPayType(OrderBillingPaymentType.CASH);
//                            paymentDetails.setPayTypeDesc(OrderBillingPaymentType.CASH.getDescription());
//
//                            paymentDetails.setPaymentSubjectType(PaymentSubjectType.DELIVERY_CLERK);
//                            paymentDetails.setPaymentSubjectTypeDesc(PaymentSubjectType.DELIVERY_CLERK.getDescription());
//                            paymentDetails.setAmount(tdOrderData.getDeliveryCash());
//
//                            if (null != appStore) {
//                                paymentDetails.setReceiptNumber(OrderUtils.generateReceiptNumber(appStore.getCityId()));
//                            }
//                            executorService.execute(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    appOrderService.saveOrderBillingPaymentDetail(paymentDetails);
//                                }
//                            });
//
//                        } else if (null != tdOrderData.getSellerCash() && tdOrderData.getSellerCash() != 0) {
//                            OrderBillingPaymentDetails paymentDetails = new OrderBillingPaymentDetails();
//                            paymentDetails.setOrderId(orderBaseInfo.getId());
//                            paymentDetails.setOrderNumber(tdOrderData.getMainOrderNumber());
//                            paymentDetails.setCreateTime(tdOrder.getPayTime());
//                            paymentDetails.setPayTime(tdOrder.getPayTime());
//                            paymentDetails.setPayType(OrderBillingPaymentType.CASH);
//                            paymentDetails.setPayTypeDesc(OrderBillingPaymentType.CASH.getDescription());
//
//                            paymentDetails.setPaymentSubjectType(PaymentSubjectType.SELLER);
//                            paymentDetails.setPaymentSubjectTypeDesc(PaymentSubjectType.SELLER.getDescription());
//                            paymentDetails.setAmount(tdOrderData.getSellerCash());
//                            if (null != appStore) {
//                                paymentDetails.setReceiptNumber(OrderUtils.generateReceiptNumber(appStore.getCityId()));
//                            }
//                            executorService.execute(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    appOrderService.saveOrderBillingPaymentDetail(paymentDetails);
//                                }
//                            });
//
//                        } else if (null != tdOrderData.getDeliveryPos() && tdOrderData.getDeliveryPos() > AppConstant.PAY_UP_LIMIT) {
//                            OrderBillingPaymentDetails paymentDetails = new OrderBillingPaymentDetails();
//                            paymentDetails.setOrderId(orderBaseInfo.getId());
//                            paymentDetails.setOrderNumber(tdOrderData.getMainOrderNumber());
//                            paymentDetails.setCreateTime(tdOrder.getPayTime());
//                            paymentDetails.setPayTime(tdOrder.getPayTime());
//                            paymentDetails.setPayType(OrderBillingPaymentType.POS);
//                            paymentDetails.setPayTypeDesc(OrderBillingPaymentType.POS.getDescription());
//
//                            paymentDetails.setPaymentSubjectType(PaymentSubjectType.DELIVERY_CLERK);
//                            paymentDetails.setPaymentSubjectTypeDesc(PaymentSubjectType.DELIVERY_CLERK.getDescription());
//                            paymentDetails.setAmount(tdOrderData.getDeliveryPos());
//
//                            if (null != appStore) {
//                                paymentDetails.setReceiptNumber(OrderUtils.generateReceiptNumber(appStore.getCityId()));
//                            }
//                            executorService.execute(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    appOrderService.saveOrderBillingPaymentDetail(paymentDetails);
//                                }
//                            });
//                        } else if (null != tdOrderData.getSellerPos() && tdOrderData.getSellerPos() > AppConstant.PAY_UP_LIMIT) {
//                            OrderBillingPaymentDetails paymentDetails = new OrderBillingPaymentDetails();
//                            paymentDetails.setOrderId(orderBaseInfo.getId());
//                            paymentDetails.setOrderNumber(tdOrderData.getMainOrderNumber());
//                            paymentDetails.setCreateTime(tdOrder.getPayTime());
//                            paymentDetails.setPayTime(tdOrder.getPayTime());
//                            paymentDetails.setPayType(OrderBillingPaymentType.POS);
//                            paymentDetails.setPayTypeDesc(OrderBillingPaymentType.POS.getDescription());
//
//                            paymentDetails.setPaymentSubjectType(PaymentSubjectType.STORE);
//                            paymentDetails.setPaymentSubjectTypeDesc(PaymentSubjectType.STORE.getDescription());
//                            paymentDetails.setAmount(tdOrderData.getSellerPos());
//                            if (null != appStore) {
//                                paymentDetails.setReceiptNumber(OrderUtils.generateReceiptNumber(appStore.getCityId()));
//                            }
//                            executorService.execute(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    appOrderService.saveOrderBillingPaymentDetail(paymentDetails);
//                                }
//                            });
//
//                        } else if (null != tdOrderData.getSellerOther() && tdOrderData.getSellerOther() > AppConstant.PAY_UP_LIMIT) {
//                            OrderBillingPaymentDetails paymentDetails = new OrderBillingPaymentDetails();
//                            paymentDetails.setOrderId(orderBaseInfo.getId());
//                            paymentDetails.setOrderNumber(tdOrderData.getMainOrderNumber());
//                            paymentDetails.setCreateTime(tdOrder.getPayTime());
//                            paymentDetails.setPayTime(tdOrder.getPayTime());
//                            paymentDetails.setPayType(OrderBillingPaymentType.OTHER);
//                            paymentDetails.setPayTypeDesc(OrderBillingPaymentType.OTHER.getDescription());
//
//                            paymentDetails.setPaymentSubjectType(PaymentSubjectType.STORE);
//                            paymentDetails.setPaymentSubjectTypeDesc(PaymentSubjectType.STORE.getDescription());
//                            paymentDetails.setAmount(tdOrderData.getSellerOther());
//                            if (null != appStore) {
//                                paymentDetails.setReceiptNumber(OrderUtils.generateReceiptNumber(appStore.getCityId()));
//                            }
//                            executorService.execute(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    appOrderService.saveOrderBillingPaymentDetail(paymentDetails);
//                                }
//                            });
//                        }
//                    }
//                }
//            } else {
//                return true;
//            }
//        } catch (Exception e) {
//            log.debug("{}", e);
//            return true;
//        }
//        return false;
//    }

//    private Boolean saveFixDiySiteBillingPaymentDetail(int size) {
//
//
//        List<OrderBaseInfo> orderBaseInfoList = dataTransferService.queryOrderBaseInfoBySize(size);
//
//        if (AssertUtil.isNotEmpty(orderBaseInfoList)) {
//            try {
//                for (OrderBaseInfo orderBaseInfo : orderBaseInfoList) {
//                    if (AppOrderSubjectType.FIT.equals(orderBaseInfo.getOrderSubjectType())) {
//                        List<TdOrder> orderList = dataTransferService.queryTdOrderByOrderNumber(orderBaseInfo.getOrderNumber());
//                        if (AssertUtil.isNotEmpty(orderList)) {
//
//                            Double storeCredit = 0D;
//                            Double storePrepay = 0D;
//                            for (TdOrder tdOrder : orderList) {
//                                if (null != tdOrder.getCredit() && tdOrder.getCredit() > AppConstant.PAY_UP_LIMIT) {
//
//                                    storeCredit = CountUtil.add(storeCredit, tdOrder.getCredit());
//
//                                } else if ((null != tdOrder.getWalletMoney() && tdOrder.getWalletMoney() > AppConstant.PAY_UP_LIMIT)
//                                        || null != tdOrder.getAlipayMoney() && tdOrder.getAlipayMoney() > AppConstant.PAY_UP_LIMIT) {
//
//                                    Double amount = CountUtil.add(null == tdOrder.getWalletMoney() ? 0D : tdOrder.getWalletMoney(),
//                                            null == tdOrder.getAlipayMoney() ? 0D : tdOrder.getAlipayMoney());
//
//                                    storePrepay = CountUtil.add(amount, storePrepay);
//                                }
//                            }
//                            if (storeCredit > AppConstant.PAY_UP_LIMIT) {
//                                OrderBillingPaymentDetails paymentDetails = new OrderBillingPaymentDetails();
//                                paymentDetails.setOrderId(orderBaseInfo.getId());
//                                paymentDetails.setOrderNumber(orderBaseInfo.getOrderNumber());
//                                paymentDetails.setCreateTime(orderList.get(0).getPayTime());
//                                paymentDetails.setPayTime(orderList.get(0).getPayTime());
//                                paymentDetails.setPayType(OrderBillingPaymentType.STORE_CREDIT_MONEY);
//                                paymentDetails.setPayTypeDesc(OrderBillingPaymentType.STORE_CREDIT_MONEY.getDescription());
//
//                                paymentDetails.setPaymentSubjectType(PaymentSubjectType.DECORATE_MANAGER);
//                                paymentDetails.setPaymentSubjectTypeDesc(PaymentSubjectType.DECORATE_MANAGER.getDescription());
//                                paymentDetails.setAmount(storeCredit);
//                                paymentDetails.setReceiptNumber(OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
//                                executorService.execute(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                        appOrderService.saveOrderBillingPaymentDetail(paymentDetails);
//                                    }
//                                });
//                            }
//                            if (storePrepay > AppConstant.PAY_UP_LIMIT) {
//                                OrderBillingPaymentDetails paymentDetails = new OrderBillingPaymentDetails();
//                                paymentDetails.setOrderId(orderBaseInfo.getId());
//                                paymentDetails.setOrderNumber(orderBaseInfo.getOrderNumber());
//                                paymentDetails.setCreateTime(orderList.get(0).getPayTime());
//                                paymentDetails.setPayTime(orderList.get(0).getPayTime());
//                                paymentDetails.setPayType(OrderBillingPaymentType.ST_PREPAY);
//                                paymentDetails.setPayTypeDesc(OrderBillingPaymentType.ST_PREPAY.getDescription());
//
//                                paymentDetails.setPaymentSubjectType(PaymentSubjectType.DECORATE_MANAGER);
//                                paymentDetails.setPaymentSubjectTypeDesc(PaymentSubjectType.DECORATE_MANAGER.getDescription());
//
//                                paymentDetails.setAmount(storePrepay);
//                                paymentDetails.setReceiptNumber(OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
//                                executorService.execute(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                        appOrderService.saveOrderBillingPaymentDetail(paymentDetails);
//                                    }
//                                });
//                            }
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                log.debug("{}", e);
//                return true;
//            }
//        } else {
//            return true;
//        }
//        return false;
//    }
}
