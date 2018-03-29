package cn.com.leyizhuang.app.foundation.service.datatransfer.impl;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.exception.DataTransferException;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dao.TimingTaskErrorMessageDAO;
import cn.com.leyizhuang.app.foundation.dao.transferdao.TransferDAO;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.*;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.AppCustomerService;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.datatransfer.DataTransferService;
import cn.com.leyizhuang.app.foundation.service.datatransfer.DataTransferSupportService;
import cn.com.leyizhuang.app.foundation.service.datatransfer.OrderGoodsTransferService;
import cn.com.leyizhuang.common.core.constant.ArrearsAuditStatus;
import cn.com.leyizhuang.common.util.AssertUtil;
import cn.com.leyizhuang.common.util.CountUtil;
import cn.com.leyizhuang.common.util.TimeTransformUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 订单商品转换类
 *
 * @author Richard
 * @date 2018/3/24
 */
@Service
@Slf4j
public class DataTransferServiceImpl implements DataTransferService {

    @Resource
    private TransferDAO transferDAO;

    @Resource
    private TimingTaskErrorMessageDAO timingTaskErrorMessageDAO;

    @Resource
    private AppEmployeeService employeeService;

    @Resource
    private AppCustomerService customerService;

    @Resource
    private AppStoreService storeService;

    @Resource
    private DataTransferSupportService dataTransferSupportService;

    @Resource
    private OrderGoodsTransferService orderGoodsTransferService;

    @Resource
    private AppOrderService appOrderService;

    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    @Resource
    private DataTransferService dataTransferService;

    @Resource
    private AppStoreService appStoreService;

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
        if (null != mainOrderNumber) {
            return transferDAO.getMainOrderInfoByMainOrderNumber(mainOrderNumber);
        }
        return null;
    }

    @Override
    public TdOrderLogistics queryOrderLogistcsByOrderNumber(String orderNumber) {
        return transferDAO.queryOrderLogistcsByOrderNumber(orderNumber);
    }

    @Override
    public OrderLogisticsInfo transferOrderLogisticsInfo(TdOrderSmall tdOrder, List<AppEmployee> employeeList, List<AppStore> storeList) {
        TdOrderLogistics tdOrderLogistics = this.queryOrderLogistcsByOrderNumber(tdOrder.getMainOrderNumber());
        OrderLogisticsInfo orderLogisticsInfo = new OrderLogisticsInfo();

        if ("门店自提".equals(tdOrder.getDeliverTypeTitle())) {
            orderLogisticsInfo.setReceiver(null);
            orderLogisticsInfo.setReceiverPhone(null);
            orderLogisticsInfo.setShippingAddress(null);

            List<AppStore> filterStoreList = storeList.stream().filter(p -> p.getStoreCode().equals(tdOrder.getDiySiteCode())).
                    collect(Collectors.toList());
            AppStore store = filterStoreList.get(0);
            if (null != store) {
                orderLogisticsInfo.setBookingStoreCode(tdOrder.getDiySiteCode());
                orderLogisticsInfo.setBookingStoreAddress(store.getDetailedAddress());
                orderLogisticsInfo.setBookingStoreName(store.getStoreName());
            } else {
                log.warn("门店信息没找到,订单：{}", tdOrder.getMainOrderNumber());
                throw new DataTransferException("该订单没有找到门店信息没", DataTransferExceptionType.STNF);
            }
        } else if ("送货上门".equals(tdOrder.getDeliverTypeTitle())) {
            List<TdDeliveryInfoDetails> tdDeliveryInfoDetailsList = this.queryDeliveryInfoDetailByOrderNumber(tdOrder.getMainOrderNumber());
            TdDeliveryInfoDetails tdDeliveryInfoDetails = tdDeliveryInfoDetailsList.get(0);
            orderLogisticsInfo.setReceiver(tdOrderLogistics.getShippingName());
            orderLogisticsInfo.setReceiverPhone(tdOrderLogistics.getShippingPhone());
            orderLogisticsInfo.setShippingAddress(tdOrderLogistics.getShippingAddress());

            if (null == tdDeliveryInfoDetailsList || tdDeliveryInfoDetailsList.size() == 0) {
                log.warn("物流信息没找到,订单：{}", tdOrder.getMainOrderNumber());
                throw new DataTransferException("该订单没有找到物流信息", DataTransferExceptionType.DENF);
            } else {
                String clerkNoCode = tdDeliveryInfoDetails.getDriver();
                AppEmployee employee = employeeService.findDeliveryByClerkNo(clerkNoCode);
                orderLogisticsInfo.setDeliveryClerkId(tdOrderLogistics.getEmpId());
                orderLogisticsInfo.setWarehouse(tdDeliveryInfoDetails.getWhNo());
                if (null != employee) {
                    orderLogisticsInfo.setDeliveryClerkName(employee.getName());
                    orderLogisticsInfo.setDeliveryClerkNo(employee.getDeliveryClerkNo());
                    orderLogisticsInfo.setDeliveryClerkPhone(employee.getMobile());
                } else {
                    log.warn("员工信息没找到,员工编码：{}", clerkNoCode);
                    throw new DataTransferException("该订单没有找到员工信息", DataTransferExceptionType.ENF);
                }
            }
        }

        if ("成都市".equals(tdOrder.getCity())) {
            orderLogisticsInfo.setDeliveryProvince("四川省");
        } else if ("郑州市".equals(tdOrder.getCity())) {
            orderLogisticsInfo.setDeliveryProvince("河南省");
        }

        orderLogisticsInfo.setDetailedAddress(tdOrderLogistics.getDetailedAddress());
        orderLogisticsInfo.setDeliveryType(AppDeliveryType.getAppDeliveryTypeByDescription(tdOrder.getDeliverTypeTitle()));
        orderLogisticsInfo.setOrdNo(tdOrder.getMainOrderNumber());
        orderLogisticsInfo.setDeliveryCity(tdOrder.getCity());
        orderLogisticsInfo.setDeliveryCounty(tdOrderLogistics.getDisctrict());
        orderLogisticsInfo.setDeliveryStreet(tdOrderLogistics.getSubdistrict());
        orderLogisticsInfo.setReceiver(tdOrderLogistics.getShippingName());
        orderLogisticsInfo.setReceiverPhone(tdOrderLogistics.getShippingPhone());
        orderLogisticsInfo.setShippingAddress(tdOrderLogistics.getShippingAddress());
        orderLogisticsInfo.setDeliveryTime(tdOrderLogistics.getDeliveryDate());
        orderLogisticsInfo.setIsOwnerReceiving(false);
        orderLogisticsInfo.setResidenceName(null);
        return orderLogisticsInfo;
    }

    @Override
    public TdOrderDeliveryTimeSeqDetail findDeliveryStatusByMainOrderNumber(String mainOrderNumber) {
        if (null != mainOrderNumber) {
            return transferDAO.findDeliveryStatusByMainOrderNumber(mainOrderNumber);
        }
        return null;
    }

    @Override
    public List<OrderBaseInfo> findNewOrderNumber() {
        return this.transferDAO.findNewOrderNumber();
    }

    @Override
    public AppCustomer findCustomerByCustomerMobile(String realUserUsername) {
        if (null != realUserUsername) {
            return transferDAO.findCustomerByCustomerMobile(realUserUsername);
        }
        return null;
    }

    @Override
    public List<TdOrderSmall> getPendingTransferOrder(Date startTime, Date endTime) {
        if (null != startTime && null != endTime) {
            return transferDAO.getPendingTransferOrder(startTime, endTime);
        }
        return null;
    }

    @Override
    @Transactional
    public OrderArrearsAuditDO transferArrearsAudit(String orderNumber, List<AppEmployee> employeeList) {

//        Boolean exist = this.transferDAO.existArrearsAudit(orderNumber);
//        if (exist) {
//            return null;
//        }
        List<TdOwnMoneyRecord> ownMoneyRecords = this.transferDAO.findOwnMoneyRecordByOrderNumber(orderNumber);
        if (null == ownMoneyRecords || ownMoneyRecords.size() == 0) {
            return null;
        }
        OrderArrearsAuditDO auditDO = new OrderArrearsAuditDO();
        for (int j = 0; j < ownMoneyRecords.size(); j++) {
            Boolean exists = this.transferDAO.existArrearsAudit(orderNumber);
            if (exists) {
                return null;
            }
            TdOwnMoneyRecord ownMoneyRecord = ownMoneyRecords.get(j);
            List<TdOrder> orders = this.transferDAO.findOrderByOrderNumber(orderNumber);
            if (null == orders || orders.size() == 0) {
                log.warn("未查到此订单头信息！订单号：", orderNumber);
                throw new DataTransferException("订单审核未查到此订单头信息", DataTransferExceptionType.NDT);
            }
            TdOrderData agencyRefund = this.transferDAO.findOrderDataByOrderNumber(orderNumber);
            if (null == agencyRefund) {

                log.warn("未查到此订单代收款信息！订单号：", orderNumber);
                throw new DataTransferException("订单审核未查到此订单代收款信息", DataTransferExceptionType.NOTORDERDATA);
            }
            TdOrder order = orders.get(0);
//            Long employeeId = this.transferDAO.findEmployeeByMobile(order.getSellerUsername());
            Long employeeId = null;
            List<AppEmployee> filterSellerList = employeeList.stream().filter(p -> p.getMobile().equals(order.getSellerUsername())).
                    collect(Collectors.toList());
            if (null != filterSellerList && !filterSellerList.isEmpty()) {
                employeeId = filterSellerList.get(0).getEmpId();
            }
            if (null == employeeId) {
                log.warn("未查到此订单导购信息！订单号：", orderNumber);
                throw new DataTransferException("订单审核未查到此订单导购信息", DataTransferExceptionType.SNF);
            }

            String clerkNo = null;
            for (int k = 0; k < orders.size(); k++) {
                clerkNo = this.transferDAO.findDeliveryInfoByOrderNumber(orders.get(k).getOrderNumber());
                if (null != clerkNo && !"".equals(clerkNo)) {
                    break;
                }
            }
            Long deliveryId = this.transferDAO.findDeliveryInfoByClerkNo(clerkNo);
            if (null != deliveryId){
                log.warn("未查到此订单配送员信息！订单号：", orderNumber);
                throw new DataTransferException("订单审核未查到此订单配送员信息", DataTransferExceptionType.ENF);
            }
            auditDO.setUserId(deliveryId);
            auditDO.setOrderNumber(orderNumber);
            auditDO.setCustomerName(order.getRealUserRealName());
            auditDO.setCustomerPhone(order.getRealUserUsername());
            auditDO.setSellerId(employeeId);
            auditDO.setSellerName(order.getSellerRealName());
            auditDO.setSellerphone(order.getSellerUsername());
            auditDO.setDistributionAddress(order.getShippingAddress().replaceAll("null", ""));
            auditDO.setDistributionTime(TimeTransformUtils.UDateToLocalDateTime(ownMoneyRecord.getCreateTime()));
            if (null != agencyRefund.getAgencyRefund() && agencyRefund.getAgencyRefund() > 0D) {
                auditDO.setAgencyMoney(agencyRefund.getAgencyRefund());
            } else {
                auditDO.setAgencyMoney(ownMoneyRecord.getPayed());
            }
            auditDO.setOrderMoney(ownMoneyRecord.getOwned());
            auditDO.setRealMoney(ownMoneyRecord.getPayed());
            if (null != ownMoneyRecord.getPos() && ownMoneyRecord.getPos() > 0D) {
                auditDO.setPaymentMethod("POS");
            } else {
                auditDO.setPaymentMethod("现金");
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
//            this.transferDAO.insertArrearsAudit(auditDO);
        }
        return auditDO;
    }

    @Override
    public List<TdDeliveryInfoDetails> queryDeliveryTimeSeqByOrderNo(String orderNO) {
        return transferDAO.queryDeliveryTimeSeqByOrderNo(orderNO);
    }

    @Override
    public List<TdDeliveryInfoDetails> queryDeliveryInfoDetailByOrderNumber(String orderNo) {
        return transferDAO.queryDeliveryInfoDetailByOrderNumber(orderNo);
    }

    @Override
    public List<TdDeliveryInfoDetails> queryTdOrderListByOrderNo(String orderNo) {
        return transferDAO.queryTdOrderListByOrderNo(orderNo);
    }

    @Override
    public List<TdDeliveryInfoDetails> queryOrderGoodsListByOrderNumber(Long id) {
        return transferDAO.queryOrderGoodsListByOrderNumber(id);
    }

    @Override
    @Transactional
    public Map<String, Object> transferCoupon(OrderBaseInfo baseInfo) {

        Map<String, Object> map = new HashMap<>();
        CashCoupon cashCoupon = new CashCoupon();
        CashCouponCompany cashCouponCompany = new CashCouponCompany();
        CustomerCashCoupon customerCashCoupon = new CustomerCashCoupon();
        OrderCouponInfo cashCouponInfo = new OrderCouponInfo();
        List<Map<String, Object>> listCoupon = new ArrayList<>();


        String orderNumber = baseInfo.getOrderNumber();
        TdOrderData orderData = this.transferDAO.findOrderDataByOrderNumber(orderNumber);
        //OrderBaseInfo orderBaseInfo = this.transferDAO.findNewOrderByOrderNumber(orderNumber);
        if (null != orderData && null != orderData.getCashCouponFee() && orderData.getCashCouponFee() > 0D) {
            List<OrderCouponInfo> list = this.transferDAO.findCouponInfoListByType(orderNumber, OrderCouponType.CASH_COUPON);
            if (null != list && list.size() > 0) {
                return null;
            }

            cashCoupon.setCreateTime(new Date());
            cashCoupon.setDenomination(orderData.getCashCouponFee());
            cashCoupon.setEffectiveStartTime(new Date());
            cashCoupon.setDescription("无条件使用");
            cashCoupon.setInitialQuantity(1);
            cashCoupon.setRemainingQuantity(0);
            cashCoupon.setTitle("优惠券");
            cashCoupon.setCityId(baseInfo.getCityId());
            cashCoupon.setCityName(baseInfo.getCityName());
            cashCoupon.setType(AppCashCouponType.COMPANY);
            cashCoupon.setIsSpecifiedStore(false);
            cashCoupon.setStatus(true);
            cashCoupon.setSortId(999);
//            this.transferDAO.addCashCoupon(cashCoupon);

            cashCouponCompany.setCcid(cashCoupon.getId());
            cashCouponCompany.setCompanyName("华润");
            cashCouponCompany.setCompanyFlag("HR");
//            this.transferDAO.addCashCouponCompany(cashCouponCompany);

            customerCashCoupon.setCusId(baseInfo.getCustomerId());
            customerCashCoupon.setCcid(cashCoupon.getId());
            customerCashCoupon.setQty(1);
            customerCashCoupon.setIsUsed(true);
            customerCashCoupon.setUseTime(baseInfo.getCreateTime());
            customerCashCoupon.setUseOrderNumber(orderNumber);
            customerCashCoupon.setGetTime(baseInfo.getCreateTime());
            customerCashCoupon.setCondition(orderData.getCashCouponFee());
            customerCashCoupon.setDenomination(orderData.getCashCouponFee());
            customerCashCoupon.setPurchasePrice(0D);
            customerCashCoupon.setEffectiveStartTime(new Date());
            customerCashCoupon.setDescription("无条件使用");
            customerCashCoupon.setTitle("优惠券");
            customerCashCoupon.setStatus(true);
            customerCashCoupon.setGetType(CouponGetType.HISTORY_IMPORT);
            customerCashCoupon.setCityId(baseInfo.getCityId());
            customerCashCoupon.setCityName(baseInfo.getCityName());
            customerCashCoupon.setType(AppCashCouponType.COMPANY);
            customerCashCoupon.setIsSpecifiedStore(false);
//            this.transferDAO.addCustomerCashCoupon(customerCashCoupon);

            cashCouponInfo.setOid(baseInfo.getId());
            cashCouponInfo.setOrderNumber(orderNumber);
            cashCouponInfo.setCouponId(customerCashCoupon.getId());
            cashCouponInfo.setCouponType(OrderCouponType.CASH_COUPON);
            cashCouponInfo.setPurchasePrice(0D);
            cashCouponInfo.setCostPrice(orderData.getCashCouponFee());
            cashCouponInfo.setGetType(CouponGetType.HISTORY_IMPORT);
//            this.transferDAO.saveOrderCouponInfo(orderCouponInfo);
        }
        List<TdOrder> orders = this.transferDAO.findOrderInfoByOrderNumber(orderNumber);
        if (null == orders || orders.size() == 0) {
            log.warn("未查到此订单信息！订单号：", orderNumber);
            throw new DataTransferException("订单卷同步未查到此订单信息", DataTransferExceptionType.NDT);
        }
        List<OrderCouponInfo> list = this.transferDAO.findCouponInfoListByType(orderNumber, OrderCouponType.PRODUCT_COUPON);
        if (null != list && list.size() > 0) {
            return null;
        }
        for (int j = 0; j < orders.size(); j++) {
            List<TdOrderGoods> orderGoodsList = this.transferDAO.getTdOrderGoodsByOrderNumber(orders.get(j).getId());
            if (null == orderGoodsList || orderGoodsList.size() == 0) {
                continue;
            }
            for (int k = 0; k < orderGoodsList.size(); k++) {
                TdOrderGoods tdOrderGoods = orderGoodsList.get(k);
                if (null != tdOrderGoods.getCouponNumber() && tdOrderGoods.getCouponNumber() > 0) {

                    List<TdCoupon> couponList = this.transferDAO.getCouponListBySkuAndOrderNumber(tdOrderGoods.getSku(), orders.get(j).getOrderNumber());
                    if (null == couponList || couponList.size() == 0) {
                        log.warn("未查到此订单卷信息！订单号：", orderNumber);
                        throw new DataTransferException("订单卷同步未查到此订单卷信息", DataTransferExceptionType.COUPON);
                    }
                    for (int l = 0; l < couponList.size(); l++) {
                        TdCoupon tdCoupon = couponList.get(l);
                        Map<String, Object> productMap = new HashMap<>();
                        CustomerProductCoupon productCoupon = new CustomerProductCoupon();
                        GoodsDO goodsDO = this.transferDAO.getGoodsBySku(tdOrderGoods.getSku());
                        productCoupon.setCustomerId(baseInfo.getCustomerId());
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
                        productCoupon.setStoreId(baseInfo.getStoreId());
                        productCoupon.setSellerId(baseInfo.getSalesConsultId());
                        productCoupon.setStatus(true);
//                        this.transferDAO.addCustomerProductCoupon(productCoupon);
                        productMap.put("productCoupon", productCoupon);

                        OrderCouponInfo orderCouponInfo = new OrderCouponInfo();
                        orderCouponInfo.setOid(baseInfo.getId());
                        orderCouponInfo.setOrderNumber(orderNumber);
                        orderCouponInfo.setCouponId(productCoupon.getId());
                        orderCouponInfo.setCouponType(OrderCouponType.PRODUCT_COUPON);
                        orderCouponInfo.setPurchasePrice(tdCoupon.getBuyPrice());
                        orderCouponInfo.setCostPrice(tdOrderGoods.getRealPrice());
                        orderCouponInfo.setGetType(CouponGetType.HISTORY_IMPORT);
                        orderCouponInfo.setSku(tdOrderGoods.getSku());
//                        this.transferDAO.saveOrderCouponInfo(orderCouponInfo);
                        productMap.put("orderCoupon", orderCouponInfo);
                        listCoupon.add(productMap);
                    }
                }
            }
        }
        map.put("cashCoupon", cashCoupon);
        map.put("cashCouponCompany", cashCouponCompany);
        map.put("customerCashCoupon", customerCashCoupon);
        map.put("cashCouponInfo", cashCouponInfo);
        map.put("listCoupon", listCoupon);

        return map;
    }

    @Override
    public OrderBillingDetails transferOrderBillingDetails(OrderBaseInfo orderBaseInfo) {
        OrderBillingDetails orderBillingDetails = null;
        TdOrderData tdOrderData = this.transferDAO.findOrderDataByOrderNumber(orderBaseInfo.getOrderNumber());
        TdOwnMoneyRecord tdOwnMoneyRecord = this.transferDAO.getOwnMoneyRecordByOrderNumber(orderBaseInfo.getOrderNumber());
        if (null != tdOrderData) {
            Boolean b = this.transferDAO.existOrderBillingDetails(orderBaseInfo.getOrderNumber());
            if (b) {
                log.warn("此订单号账单已生成请检查！订单号：", orderBaseInfo.getOrderNumber());
                throw new DataTransferException("此订单号账单已生成请检查", DataTransferExceptionType.UNKNOWN);
            }
            if (orderBaseInfo.getOrderSubjectType() == AppOrderSubjectType.STORE) {
                orderBillingDetails = new OrderBillingDetails();
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
                orderBillingDetails.setAmountPayable(CountUtil.sub(orderAmountSubTotal, stPreDepsit,tdOrderData.getLeftPrice()==null?0D:tdOrderData.getLeftPrice()));
                orderBillingDetails.setCollectionAmount(tdOrderData.getAgencyRefund());
                orderBillingDetails.setArrearage(tdOrderData.getDue());
                orderBillingDetails.setIsOwnerReceiving(Boolean.FALSE);
                orderBillingDetails.setIsPayUp(tdOrderData.getDue() > 0 ? Boolean.FALSE : Boolean.TRUE);
                if (tdOrderData.getDue() > 0) {
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
                    log.warn("未查到此订单头信息！订单号：", orderBaseInfo.getOrderNumber());
                    throw new DataTransferException("未查到此订单头信息", DataTransferExceptionType.NDT);
                }
                orderBillingDetails.setJxPriceDifferenceAmount(jxTotalPrice);
                orderBillingDetails.setStoreCash(tdOrderData.getSellerCash());
                orderBillingDetails.setStoreOtherMoney(tdOrderData.getSellerOther());
                orderBillingDetails.setStorePosMoney(tdOrderData.getSellerPos());
                orderBillingDetails.setStorePosNumber(tdOwnMoneyRecord == null ? null : tdOwnMoneyRecord.getSerialNumber());
                orderBillingDetails.setDeliveryCash(tdOrderData.getDeliveryCash());
                orderBillingDetails.setDeliveryPos(tdOrderData.getDeliveryPos());
                //this.transferDAO.saveOrderBillingDetails(orderBillingDetails);
            } else {
                orderBillingDetails = new OrderBillingDetails();
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
                    log.warn("未查到此订单头信息！订单号：", orderBaseInfo.getOrderNumber());
                    throw new DataTransferException("未查到此订单头信息", DataTransferExceptionType.NDT);
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
                orderBillingDetails.setJxPriceDifferenceAmount(0D);
                orderBillingDetails.setStoreCash(0D);
                orderBillingDetails.setStoreOtherMoney(0D);
                orderBillingDetails.setStorePosMoney(tdOrderData.getSellerPos());
                orderBillingDetails.setStorePosNumber(null);
                orderBillingDetails.setDeliveryCash(0D);
                orderBillingDetails.setDeliveryPos(0D);
                //this.transferDAO.saveOrderBillingDetails(orderBillingDetails);
            }
            return orderBillingDetails;
        } else {
            log.warn("订单账单没有找到，订单号：{}", orderBaseInfo.getOrderNumber());
            throw new DataTransferException("订单账单没有找到，订单号：{}", DataTransferExceptionType.NOTORDERDATA);
        }
    }

    @Override
    public AppEmployee findFitEmployeeInfoById(Long userId) {
        if (null != userId) {
            return transferDAO.findFitEmployeeInfoById(userId);
        }
        return null;
    }

    @Override
    public AppEmployee findStoreEmployeeById(Long sellerId) {
        if (null != sellerId) {
            return transferDAO.findStoreEmployeeById(sellerId);
        }
        return null;
    }

    @Override
    public AppCustomer findCustomerById(Long userId) {
        if (null != userId) {
            return transferDAO.findCustomerById(userId);
        }
        return null;
    }

    @Override
    public List<TdOrderData> queryTdOrderDataListByOrderNo(String orderNo) {
        return transferDAO.queryTdOrderDataListByOrderNo(orderNo);
    }

    @Override
    public List<OrderBaseInfo> findNewOrderNumberByDeliveryType() {
        return this.transferDAO.findNewOrderNumberByDeliveryType();
    }

    @Override
    public List<OrderBaseInfo> queryOrderBaseInfoBySize(int size) {
        return transferDAO.queryOrderBaseInfoBySize(size);
    }

    @Override
    public List<TdOrder> queryTdOrderByOrderNumber(String orderNumber) {
        return transferDAO.queryTdOrderByOrderNumber(orderNumber);
    }

    @Override
    public OrderBaseInfo transferOrderBaseInfo(TdOrderSmall tdOrder, List<AppEmployee> employeeList, List<AppCustomer> customerList,
                                               List<AppStore> storeList) {
        //TdOrder tdOrder = dataTransferService.getMainOrderInfoByMainOrderNumber(mainOrderNumber);
        OrderBaseInfo orderBaseInfo = new OrderBaseInfo();
        orderBaseInfo.setOrderNumber(tdOrder.getMainOrderNumber());
        orderBaseInfo.setOrderType(AppOrderType.SHIPMENT);
        orderBaseInfo.setCreateTime(null != tdOrder.getPayTime() ? tdOrder.getPayTime() : tdOrder.getOrderTime());
        switch (tdOrder.getDeliverTypeTitle()) {
            case "送货上门":
                orderBaseInfo.setDeliveryType(AppDeliveryType.HOUSE_DELIVERY);
                break;
            case "门店自提":
                orderBaseInfo.setDeliveryType(AppDeliveryType.SELF_TAKE);
                break;
            default:
                break;
        }
        orderBaseInfo.setPickUpCode("0000");
        if (orderBaseInfo.getDeliveryType() == AppDeliveryType.SELF_TAKE) {
            switch (tdOrder.getStatusId().intValue()) {
                case 3:
                    orderBaseInfo.setStatus(AppOrderStatus.PENDING_RECEIVE);
                    break;
                case 4:
                    orderBaseInfo.setStatus(AppOrderStatus.FINISHED);
                    break;
                case 5:
                    orderBaseInfo.setStatus(AppOrderStatus.FINISHED);
                    break;
                case 6:
                    orderBaseInfo.setStatus(AppOrderStatus.FINISHED);
                    break;
                default:
                    break;
            }
        } else {
            switch (tdOrder.getStatusId().intValue()) {
                case 3:
                    orderBaseInfo.setStatus(AppOrderStatus.PENDING_SHIPMENT);
                    break;
                case 4:
                    orderBaseInfo.setStatus(AppOrderStatus.PENDING_RECEIVE);
                    break;
                case 5:
                    orderBaseInfo.setStatus(AppOrderStatus.FINISHED);
                    break;
                case 6:
                    orderBaseInfo.setStatus(AppOrderStatus.FINISHED);
                    break;
                default:
                    break;
            }
        }
        //todo 设置订单物流状态
        if (orderBaseInfo.getDeliveryType() == AppDeliveryType.SELF_TAKE) {
            orderBaseInfo.setDeliveryStatus(LogisticStatus.INITIAL);
        } else {
            if (orderBaseInfo.getStatus() == AppOrderStatus.PENDING_SHIPMENT) {
                TdOrderDeliveryTimeSeqDetail detail = this.findDeliveryStatusByMainOrderNumber(tdOrder.getMainOrderNumber());
                if (null != detail) {
                    switch (detail.getOperationType()) {
                        case "处理中":
                            orderBaseInfo.setDeliveryStatus(LogisticStatus.INITIAL);
                            break;
                        case "定位":
                            orderBaseInfo.setDeliveryStatus(LogisticStatus.ALREADY_POSITIONED);
                            break;
                        case "拣货":
                            orderBaseInfo.setDeliveryStatus(LogisticStatus.PICKING_GOODS);
                            break;
                        case "装车":
                            orderBaseInfo.setDeliveryStatus(LogisticStatus.LOADING);
                            break;
                        default:
                            break;
                    }
                }
            } else if (orderBaseInfo.getStatus() == AppOrderStatus.PENDING_RECEIVE || orderBaseInfo.getStatus() == AppOrderStatus.FINISHED) {
                orderBaseInfo.setDeliveryStatus(LogisticStatus.SEALED_CAR);
            }

        }
        if (tdOrder.getMainOrderNumber().contains("FIT")) {
            orderBaseInfo.setOrderSubjectType(AppOrderSubjectType.FIT);
        } else {
            orderBaseInfo.setOrderSubjectType(AppOrderSubjectType.STORE);
        }
        //设置订单创建者身份类型
        if (tdOrder.getMainOrderNumber().contains("FIT")) {
            orderBaseInfo.setCreatorIdentityType(AppIdentityType.DECORATE_MANAGER);
        } else {
            if (tdOrder.getIsSellerOrder()) {
                orderBaseInfo.setCreatorIdentityType(AppIdentityType.SELLER);
            } else {
                orderBaseInfo.setCreatorIdentityType(AppIdentityType.CUSTOMER);
            }
        }
        //设置订单创建者信息
        switch (orderBaseInfo.getCreatorIdentityType()) {
            case DECORATE_MANAGER:
                AppEmployee fitEmployee = this.findFitEmployeeInfoById(tdOrder.getRealUserId());
                if (null != fitEmployee) {
                    orderBaseInfo.setCreatorId(fitEmployee.getEmpId());
                    orderBaseInfo.setCreatorName(fitEmployee.getName());
                    orderBaseInfo.setCreatorPhone(fitEmployee.getMobile());
                } else {
                    //unhandledMap.put(tdOrder.getMainOrderNumber(), "该订单没有找到装饰经理信息");
                    log.warn("装饰公司订单：{} 经理信息没找到", tdOrder.getMainOrderNumber());
                    throw new DataTransferException("该订单没有找到装饰经理信息", DataTransferExceptionType.DMNF);
                    //throw new RuntimeException("装饰公司订单：" + mainOrderNumber + "经理信息没找到");
                    //continue;
                }
                break;
            case SELLER:
                //AppEmployee storeEmployee = dataTransferService.findStoreEmployeeById(tdOrder.getSellerId());
                List<AppEmployee> filterEmployeeList = employeeList.stream().filter(p -> p.getMobile().equals(tdOrder.getSellerUsername())).
                        collect(Collectors.toList());
                if (null != filterEmployeeList && !filterEmployeeList.isEmpty()) {
                    AppEmployee storeEmployee = filterEmployeeList.get(0);
                    orderBaseInfo.setCreatorId(storeEmployee.getEmpId());
                    orderBaseInfo.setCreatorName(storeEmployee.getName());
                    orderBaseInfo.setCreatorPhone(storeEmployee.getMobile());
                    orderBaseInfo.setSalesConsultId(orderBaseInfo.getCreatorId());
                    orderBaseInfo.setSalesConsultName(orderBaseInfo.getCreatorName());
                    orderBaseInfo.setSalesConsultPhone(orderBaseInfo.getCreatorPhone());
                    //AppCustomer customer = dataTransferService.findCustomerById(tdOrder.getUserId());
                    // AppCustomer customer = dataTransferService.findCustomerByCustomerMobile(tdOrder.getRealUserUsername());
                    List<AppCustomer> filterCustomerList = customerList.stream().filter(p -> p.getMobile().equals(tdOrder.getRealUserUsername())).
                            collect(Collectors.toList());
                    if (null != filterCustomerList && !filterCustomerList.isEmpty()) {
                        AppCustomer customer = filterCustomerList.get(0);
                        orderBaseInfo.setCustomerId(customer.getCusId());
                        orderBaseInfo.setCustomerName(customer.getName());
                        orderBaseInfo.setCustomerPhone(customer.getMobile());
                        orderBaseInfo.setCustomerType(customer.getCustomerType());
                    } else {
                        //unhandledMap.put(tdOrder.getMainOrderNumber(), "该订单没有找到顾客信息");
                        log.warn("订单：{} 顾客信息没找到", tdOrder.getMainOrderNumber());
                        throw new DataTransferException("该订单没有找到顾客信息", DataTransferExceptionType.CNF);
                        //throw new RuntimeException("订单：" + mainOrderNumber + " 顾客信息没找到");
                    }
                } else {
                    //unhandledMap.put(tdOrder.getMainOrderNumber(), "该订单没有找到导购信息");
                    log.warn("门店订单：{} 导购信息没找到", tdOrder.getMainOrderNumber());
                    throw new DataTransferException("该订单没有找到导购信息", DataTransferExceptionType.SNF);
                }
                break;
            case CUSTOMER:
                //AppCustomer customer = dataTransferService.findCustomerById(tdOrder.getUserId());
                // AppCustomer customer = dataTransferService.findCustomerByCustomerMobile(tdOrder.getRealUserUsername());
                List<AppCustomer> filterCustomerList = customerList.stream().filter(p -> p.getMobile().equals(tdOrder.getRealUserUsername())).
                        collect(Collectors.toList());
                if (null != filterCustomerList && !filterCustomerList.isEmpty()) {
                    AppCustomer customer = filterCustomerList.get(0);
                    orderBaseInfo.setCreatorId(customer.getCusId());
                    orderBaseInfo.setCreatorName(customer.getName());
                    orderBaseInfo.setCreatorPhone(customer.getMobile());
                    orderBaseInfo.setCustomerId(orderBaseInfo.getCreatorId());
                    orderBaseInfo.setCustomerName(orderBaseInfo.getCreatorName());
                    orderBaseInfo.setCustomerPhone(orderBaseInfo.getCreatorPhone());
                    orderBaseInfo.setCustomerType(customer.getCustomerType());
                    //AppEmployee sellerEmployee = dataTransferService.findStoreEmployeeById(tdOrder.getSellerId());
                    List<AppEmployee> filterSellerList = employeeList.stream().filter(p -> p.getMobile().equals(tdOrder.getSellerUsername())).
                            collect(Collectors.toList());
                    if (null != filterSellerList && !filterSellerList.isEmpty()) {
                        AppEmployee sellerEmployee = filterSellerList.get(0);

                        orderBaseInfo.setSalesConsultId(sellerEmployee.getEmpId());
                        orderBaseInfo.setSalesConsultName(sellerEmployee.getName());
                        orderBaseInfo.setSalesConsultPhone(sellerEmployee.getMobile());
                    } else {
                        //unhandledMap.put(tdOrder.getMainOrderNumber(), "该订单没有找到导购信息");
                        log.warn("门店订单：{} 导购信息没找到", tdOrder.getMainOrderNumber());
                        throw new DataTransferException("该订单没有找到导购信息", DataTransferExceptionType.SNF);
                        //throw new RuntimeException("门店订单：" + mainOrderNumber + "导购信息没找到");
                    }
                } else {
                    //unhandledMap.put(tdOrder.getMainOrderNumber(), "该订单没有找到顾客信息");
                    log.warn("门店订单：{} 顾客信息没找到", tdOrder.getMainOrderNumber());
                    throw new DataTransferException("该订单没有找到顾客信息", DataTransferExceptionType.CNF);
                    //throw new RuntimeException("门店订单：" + mainOrderNumber + "顾客信息没找到");
                }
                break;
            default:
                break;
        }
        //设置门店信息
        List<AppStore> filterStoreList = storeList.stream().filter(p -> p.getStoreCode().equals(tdOrder.getDiySiteCode())).
                collect(Collectors.toList());
        AppStore store = filterStoreList.get(0);
        if (null != store) {
            orderBaseInfo.setStoreId(store.getStoreId());
            orderBaseInfo.setStoreCode(store.getStoreCode());
            orderBaseInfo.setStoreOrgId(store.getStoreOrgId());
            orderBaseInfo.setStoreStructureCode(store.getStoreStructureCode());
        } else {
            //unhandledMap.put(tdOrder.getMainOrderNumber(), "该订单没有找到门店信息");
            log.warn("门店订单：{} 门店信息没找到", tdOrder.getMainOrderNumber());
            throw new DataTransferException("该订单没有找到门店信息", DataTransferExceptionType.STNF);
            // continue;
        }
        orderBaseInfo.setTotalGoodsPrice(tdOrder.getTotalGoodsPrice());
        orderBaseInfo.setIsEvaluated(Boolean.FALSE);
        orderBaseInfo.setRemark(tdOrder.getRemark());
        orderBaseInfo.setCityName(tdOrder.getCity());
        if (orderBaseInfo.getCityName().equals("成都市")) {
            orderBaseInfo.setCityId(1L);
            orderBaseInfo.setSobId(2121L);
        } else {
            orderBaseInfo.setCityId(2L);
            orderBaseInfo.setSobId(2033L);
        }
        orderBaseInfo.setSalesNumber(tdOrder.getPaperSalesNumber());
        orderBaseInfo.setIsRecordSales(Boolean.TRUE);
        return orderBaseInfo;

        //System.out.println("orderBaseInfo:\n" + orderBaseInfo);
        //System.out.println(countLine);
        // orderService.saveOrderBaseInfo(orderBaseInfo);
    }


    @Override
    public Queue<DataTransferErrorLog> transferOrderRelevantInfo() throws ExecutionException, InterruptedException {
        // *********************** 订单迁移处理 ***************
        Queue<DataTransferErrorLog> errorLogQueue = new ConcurrentLinkedDeque<>();
        List<TdOrderSmall> storeMainOrderNumberList;
        List<AppEmployee> employeeList = employeeService.findAllSeller();
        List<AppCustomer> customerList = customerService.findAllCustomer();
        List<AppStore> storeList = storeService.findAll();

        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, Calendar.NOVEMBER, 1, 0, 0, 0);
        Date startTime = calendar.getTime();
        Date endTime = new Date();
        //查询所有待处理的主单
        storeMainOrderNumberList = this.getPendingTransferOrder(startTime, endTime);
        //多线程处理任务集
        if (storeMainOrderNumberList == null || storeMainOrderNumberList.isEmpty()) {
            throw new DataTransferException("没有要迁移的数据", DataTransferExceptionType.NDT);
        }
        int size = storeMainOrderNumberList.size();
        int nThreads = 6;
        AtomicInteger countLine = new AtomicInteger();
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        List<Future<Queue<DataTransferErrorLog>>> futures = new ArrayList<>(nThreads);

        for (int i = 0; i < nThreads; i++) {
            final List<TdOrderSmall> subList = storeMainOrderNumberList.subList(size / nThreads * i, size / nThreads * (i + 1));
            Callable<Queue<DataTransferErrorLog>> task = () -> {
                Queue<DataTransferErrorLog> dataTransferErrorLogQueue = new ConcurrentLinkedDeque<>();
                for (TdOrderSmall tdOrder : subList) {
                    try {
                        countLine.addAndGet(1);
                        //处理订单头
                        OrderBaseInfo orderBaseInfo = this.transferOrderBaseInfo(tdOrder, employeeList, customerList, storeList);

                        // 根据主单号 找到旧订单分单
                        List<TdOrder> tdOrders = transferDAO.findOrderAllFieldByOrderNumber(orderBaseInfo.getOrderNumber());
                        if (tdOrders == null || tdOrders.size() == 0) {
                            //throw new Exception("订单商品转行异常，找不到旧订单 订单号："+ orderBaseInfo.getOrderNumber());
                            throw new DataTransferException("找不到旧订单 订单号：" + orderBaseInfo.getOrderNumber(), DataTransferExceptionType.NDT);
                        }

                        // 转换订单商品
                        List<OrderGoodsInfo> orderGoodsInfoList = orderGoodsTransferService.transferOne(orderBaseInfo, tdOrders);

                        //处理订单账单信息
                        OrderBillingDetails orderBillingDetails = this.transferOrderBillingDetails(orderBaseInfo);

                        //****物流进度明细 begin****/
                        List<OrderDeliveryInfoDetails> deliveryInfoDetailsList = new ArrayList<>();
                        if (AppDeliveryType.HOUSE_DELIVERY.equals(orderBaseInfo.getDeliveryType())) {
                            deliveryInfoDetailsList = this.saveOrderDeliveryInfoDetails(orderBaseInfo);
                        }

                        //****经销差价返还 begin****/
                        List<OrderJxPriceDifferenceReturnDetails> jxPriceDifferenceReturnDetailsList = new ArrayList<>();
                        if (!orderBaseInfo.getOrderNumber().contains("YF")) {
                            jxPriceDifferenceReturnDetailsList = this.saveOrderJxPriceDifference(orderBaseInfo, tdOrders);
                        }
                        List<OrderBillingPaymentDetails> paymentDetailsList = new ArrayList<>();
                        //****装饰公司账单支付明细转换 begin****/
                        if (AppOrderSubjectType.FIT.equals(orderBaseInfo.getOrderSubjectType())) {
                            paymentDetailsList = this.saveFixDiySiteBillingPaymentDetail(orderBaseInfo, tdOrders);
                        } else {
                            //****普通客户账单支付明细转换 begin****/
                            paymentDetailsList = this.saveOrderBillingPaymentDetail(orderBaseInfo, tdOrder);
                        }

                        //处理订单物流信息
                        OrderLogisticsInfo orderLogisticsInfo = this.transferOrderLogisticsInfo(tdOrder, employeeList, storeList);

                        //订单券信息处理
                        Map<String, Object> map = this.transferCoupon(orderBaseInfo);

                        //订单欠款审核信息
                        OrderArrearsAuditDO orderArrearsAuditDO = this.transferArrearsAudit(orderBaseInfo.getOrderNumber(), employeeList);

                        //持久化订单相关信息
                        dataTransferSupportService.saveOrderRelevantInfo(orderBaseInfo, orderGoodsInfoList, orderBillingDetails, deliveryInfoDetailsList,
                                jxPriceDifferenceReturnDetailsList, paymentDetailsList, orderLogisticsInfo, map,
                                orderArrearsAuditDO);
                    } catch (DataTransferException e) {
                        dataTransferErrorLogQueue.add(new DataTransferErrorLog(null, tdOrder.getMainOrderNumber(), e.getType().getDesc(), new Date()));
                    } catch (Exception e) {
                        log.warn(e.getMessage());
                        dataTransferErrorLogQueue.add(new DataTransferErrorLog(null, tdOrder.getMainOrderNumber(), e.getMessage(),
                                new Date()));
                    }
                }
                return dataTransferErrorLogQueue;
            };
            futures.add(executorService.submit(task));
        }
        for (Future<Queue<DataTransferErrorLog>> future : futures) {
            errorLogQueue.addAll(future.get());
        }
        dataTransferSupportService.saveDataTransferErrorLog(errorLogQueue);
        executorService.shutdown();
        System.out.println("未处理或处理失败的数据条数:" + errorLogQueue.size());
        log.info("订单导入job处理完成,当前时间:{}", new Date());
        return errorLogQueue;
    }

    private List<OrderDeliveryInfoDetails> saveOrderDeliveryInfoDetails(OrderBaseInfo orderBaseInfo) {
        List<TdDeliveryInfoDetails> entityList = transferDAO.queryDeliveryTimeSeqByOrderNo(orderBaseInfo.getOrderNumber());
        List<OrderDeliveryInfoDetails> deliveryInfoDetailsList;
        if (AssertUtil.isNotEmpty(entityList)) {
            deliveryInfoDetailsList = new ArrayList<>();
            for (TdDeliveryInfoDetails tdDeliveryInfoDetails : entityList) {

                OrderDeliveryInfoDetails deliveryInfoDetails = new OrderDeliveryInfoDetails();

                switch (tdDeliveryInfoDetails.getOperationType()) {
                    case "处理中": {
                        deliveryInfoDetails.setLogisticStatus(LogisticStatus.INITIAL);
                        break;
                    }
                    case "定位": {
                        deliveryInfoDetails.setLogisticStatus(LogisticStatus.ALREADY_POSITIONED);
                        break;
                    }
                    case "拣货": {
                        deliveryInfoDetails.setLogisticStatus(LogisticStatus.PICKING_GOODS);
                        break;
                    }
                    case "装车": {
                        deliveryInfoDetails.setLogisticStatus(LogisticStatus.LOADING);
                        break;
                    }
                    case "封车": {
                        deliveryInfoDetails.setLogisticStatus(LogisticStatus.SEALED_CAR);
                        List<TdDeliveryInfoDetails> deliveryInfoDetailList = dataTransferService.queryDeliveryInfoDetailByOrderNumber(tdDeliveryInfoDetails.getMainOrderNumber());
                        if (AssertUtil.isNotEmpty(deliveryInfoDetailList)) {
                            TdDeliveryInfoDetails deliveryInfoDetail = deliveryInfoDetailList.get(0);
                            deliveryInfoDetails.setOperatorNo(deliveryInfoDetail.getDriver());
                            deliveryInfoDetails.setTaskNo(deliveryInfoDetail.getTaskNo());
                            deliveryInfoDetails.setWarehouseNo(deliveryInfoDetail.getWhNo());
                        }
                        break;
                    }
                    default: {
                        break;
                    }
                }
                deliveryInfoDetails.setDescription(tdDeliveryInfoDetails.getOperationDescription());
                deliveryInfoDetails.setOrderNo(tdDeliveryInfoDetails.getMainOrderNumber());
                deliveryInfoDetails.setCreateTime(tdDeliveryInfoDetails.getOperationTime());
                deliveryInfoDetails.setIsRead(true);

                deliveryInfoDetailsList.add(deliveryInfoDetails);
            }

            if (AppOrderStatus.PENDING_RECEIVE.equals(orderBaseInfo.getStatus())) {
                OrderDeliveryInfoDetails orderDeliveryInfoDetails = new OrderDeliveryInfoDetails();
                orderDeliveryInfoDetails.setDeliveryInfo(orderBaseInfo.getOrderNumber(), LogisticStatus.SEALED_CAR, "商家已封车完成！", "已封车",
                        "", "", "", "");
                orderDeliveryInfoDetails.setIsRead(true);
                deliveryInfoDetailsList.add(orderDeliveryInfoDetails);
            } else if (AppOrderStatus.FINISHED.equals(orderBaseInfo.getStatus())) {
                OrderDeliveryInfoDetails orderDeliveryInfoDetails = new OrderDeliveryInfoDetails();
                orderDeliveryInfoDetails.setDeliveryInfo(orderBaseInfo.getOrderNumber(), LogisticStatus.CONFIRM_ARRIVAL, "确认到货！", "送达",
                        "", "", "", "");
                orderDeliveryInfoDetails.setIsRead(true);
                deliveryInfoDetailsList.add(orderDeliveryInfoDetails);
            }
            return deliveryInfoDetailsList;
        } else {
            throw new DataTransferException("此配送订单物流明细没有找到", DataTransferExceptionType.DDNF);
        }
    }

    private List<OrderJxPriceDifferenceReturnDetails> saveOrderJxPriceDifference(OrderBaseInfo orderBaseInfo, List<TdOrder> tdOrderList) {

        List<OrderJxPriceDifferenceReturnDetails> jxPriceDifferenceReturnDetailsList = new ArrayList<>();
        for (TdOrder tdOrder : tdOrderList) {
            if (!tdOrder.getMainOrderNumber().contains("YF")) {
                List<TdDeliveryInfoDetails> tdOrderGoodsList = dataTransferService.queryOrderGoodsListByOrderNumber(tdOrder.getId());
                if (AssertUtil.isNotEmpty(tdOrderGoodsList)) {
                    for (TdDeliveryInfoDetails tdOrderGoods : tdOrderGoodsList) {

                        OrderJxPriceDifferenceReturnDetails returnDetails = new OrderJxPriceDifferenceReturnDetails();

                        returnDetails.setOid(orderBaseInfo.getId());
                        returnDetails.setOrderNumber(tdOrder.getMainOrderNumber());
                        returnDetails.setCreateTime(tdOrder.getPayTime());
                        returnDetails.setStoreCode(orderBaseInfo.getStoreCode());
                        returnDetails.setStoreId(orderBaseInfo.getStoreId());
                        returnDetails.setSku(tdOrderGoods.getSku());
                        returnDetails.setUnitPrice(tdOrderGoods.getJxDif());
                        returnDetails.setQuantity(tdOrderGoods.getQuantity());
                        returnDetails.setAmount(tdOrderGoods.getDifTotal());

                        jxPriceDifferenceReturnDetailsList.add(returnDetails);
                    }
                } else {
                    throw new DataTransferException("订单的商品明细没有找到,无法转换经销差价", DataTransferExceptionType.OGNF);
                }
            }
        }
        return jxPriceDifferenceReturnDetailsList;
    }

    private List<OrderBillingPaymentDetails> saveOrderBillingPaymentDetail(OrderBaseInfo orderBaseInfo, TdOrderSmall tdOrder) {

        List<TdOrderData> orderDataList = transferDAO.queryTdOrderDataListByOrderNo(orderBaseInfo.getOrderNumber());
        List<OrderBillingPaymentDetails> paymentDetailsList;
        if (AssertUtil.isNotEmpty(orderDataList)) {
            paymentDetailsList = new ArrayList<>();
            for (TdOrderData tdOrderData : orderDataList) {
                if ((null != tdOrderData.getOnlinePay() && tdOrderData.getOnlinePay() > AppConstant.PAY_UP_LIMIT)
                        || (null != tdOrderData.getBalanceUsed() && tdOrderData.getBalanceUsed() > AppConstant.PAY_UP_LIMIT)) {
                    OrderBillingPaymentDetails paymentDetails = new OrderBillingPaymentDetails();
                    paymentDetails.setOrderId(orderBaseInfo.getId());
                    paymentDetails.setOrderNumber(tdOrderData.getMainOrderNumber());
                    paymentDetails.setCreateTime(tdOrder.getPayTime());
                    paymentDetails.setPayTime(tdOrder.getPayTime());
                    paymentDetails.setPayType(OrderBillingPaymentType.ST_PREPAY);
                    paymentDetails.setPayTypeDesc(OrderBillingPaymentType.ST_PREPAY.getDescription());

                    if (tdOrder.getIsSellerOrder()) {
                        paymentDetails.setPaymentSubjectType(PaymentSubjectType.SELLER);
                        paymentDetails.setPaymentSubjectTypeDesc(PaymentSubjectType.SELLER.getDescription());
                    } else {
                        paymentDetails.setPaymentSubjectType(PaymentSubjectType.CUSTOMER);
                        paymentDetails.setPaymentSubjectTypeDesc(PaymentSubjectType.CUSTOMER.getDescription());
                    }
                    Double amount = CountUtil.add(null == tdOrderData.getOnlinePay() ? 0D : tdOrderData.getOnlinePay(),
                            null == tdOrderData.getBalanceUsed() ? 0D : tdOrderData.getBalanceUsed());
                    paymentDetails.setAmount(amount);

                    paymentDetails.setReceiptNumber(OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));

                    paymentDetailsList.add(paymentDetails);
                } else if (null != tdOrderData.getDeliveryCash() && tdOrderData.getDeliveryCash() > AppConstant.PAY_UP_LIMIT) {
                    OrderBillingPaymentDetails paymentDetails = new OrderBillingPaymentDetails();
                    paymentDetails.setOrderId(orderBaseInfo.getId());
                    paymentDetails.setOrderNumber(tdOrderData.getMainOrderNumber());
                    paymentDetails.setCreateTime(tdOrder.getPayTime());
                    paymentDetails.setPayTime(tdOrder.getPayTime());
                    paymentDetails.setPayType(OrderBillingPaymentType.CASH);
                    paymentDetails.setPayTypeDesc(OrderBillingPaymentType.CASH.getDescription());

                    paymentDetails.setPaymentSubjectType(PaymentSubjectType.DELIVERY_CLERK);
                    paymentDetails.setPaymentSubjectTypeDesc(PaymentSubjectType.DELIVERY_CLERK.getDescription());
                    paymentDetails.setAmount(tdOrderData.getDeliveryCash());

                    paymentDetails.setReceiptNumber(OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
                    paymentDetailsList.add(paymentDetails);

                } else if (null != tdOrderData.getSellerCash() && tdOrderData.getSellerCash() != 0) {
                    OrderBillingPaymentDetails paymentDetails = new OrderBillingPaymentDetails();
                    paymentDetails.setOrderId(orderBaseInfo.getId());
                    paymentDetails.setOrderNumber(tdOrderData.getMainOrderNumber());
                    paymentDetails.setCreateTime(tdOrder.getPayTime());
                    paymentDetails.setPayTime(tdOrder.getPayTime());
                    paymentDetails.setPayType(OrderBillingPaymentType.CASH);
                    paymentDetails.setPayTypeDesc(OrderBillingPaymentType.CASH.getDescription());

                    paymentDetails.setPaymentSubjectType(PaymentSubjectType.SELLER);
                    paymentDetails.setPaymentSubjectTypeDesc(PaymentSubjectType.SELLER.getDescription());
                    paymentDetails.setAmount(tdOrderData.getSellerCash());
                    paymentDetails.setReceiptNumber(OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
                    paymentDetailsList.add(paymentDetails);

                } else if (null != tdOrderData.getDeliveryPos() && tdOrderData.getDeliveryPos() > AppConstant.PAY_UP_LIMIT) {
                    OrderBillingPaymentDetails paymentDetails = new OrderBillingPaymentDetails();
                    paymentDetails.setOrderId(orderBaseInfo.getId());
                    paymentDetails.setOrderNumber(tdOrderData.getMainOrderNumber());
                    paymentDetails.setCreateTime(tdOrder.getPayTime());
                    paymentDetails.setPayTime(tdOrder.getPayTime());
                    paymentDetails.setPayType(OrderBillingPaymentType.POS);
                    paymentDetails.setPayTypeDesc(OrderBillingPaymentType.POS.getDescription());

                    paymentDetails.setPaymentSubjectType(PaymentSubjectType.DELIVERY_CLERK);
                    paymentDetails.setPaymentSubjectTypeDesc(PaymentSubjectType.DELIVERY_CLERK.getDescription());
                    paymentDetails.setAmount(tdOrderData.getDeliveryPos());

                    paymentDetails.setReceiptNumber(OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));

                    paymentDetailsList.add(paymentDetails);
                } else if (null != tdOrderData.getSellerPos() && tdOrderData.getSellerPos() > AppConstant.PAY_UP_LIMIT) {
                    OrderBillingPaymentDetails paymentDetails = new OrderBillingPaymentDetails();
                    paymentDetails.setOrderId(orderBaseInfo.getId());
                    paymentDetails.setOrderNumber(tdOrderData.getMainOrderNumber());
                    paymentDetails.setCreateTime(tdOrder.getPayTime());
                    paymentDetails.setPayTime(tdOrder.getPayTime());
                    paymentDetails.setPayType(OrderBillingPaymentType.POS);
                    paymentDetails.setPayTypeDesc(OrderBillingPaymentType.POS.getDescription());

                    paymentDetails.setPaymentSubjectType(PaymentSubjectType.STORE);
                    paymentDetails.setPaymentSubjectTypeDesc(PaymentSubjectType.STORE.getDescription());
                    paymentDetails.setAmount(tdOrderData.getSellerPos());
                    paymentDetails.setReceiptNumber(OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
                    paymentDetailsList.add(paymentDetails);

                } else if (null != tdOrderData.getSellerOther() && tdOrderData.getSellerOther() > AppConstant.PAY_UP_LIMIT) {
                    OrderBillingPaymentDetails paymentDetails = new OrderBillingPaymentDetails();
                    paymentDetails.setOrderId(orderBaseInfo.getId());
                    paymentDetails.setOrderNumber(tdOrderData.getMainOrderNumber());
                    paymentDetails.setCreateTime(tdOrder.getPayTime());
                    paymentDetails.setPayTime(tdOrder.getPayTime());
                    paymentDetails.setPayType(OrderBillingPaymentType.OTHER);
                    paymentDetails.setPayTypeDesc(OrderBillingPaymentType.OTHER.getDescription());

                    paymentDetails.setPaymentSubjectType(PaymentSubjectType.STORE);
                    paymentDetails.setPaymentSubjectTypeDesc(PaymentSubjectType.STORE.getDescription());
                    paymentDetails.setAmount(tdOrderData.getSellerOther());
                    paymentDetails.setReceiptNumber(OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
                    paymentDetailsList.add(paymentDetails);
                }
            }
            return paymentDetailsList;
        } else {
            throw new DataTransferException("订单账单明细没有找到,无法转化订单支付明细", DataTransferExceptionType.ODNF);
        }
    }

    private List<OrderBillingPaymentDetails> saveFixDiySiteBillingPaymentDetail(OrderBaseInfo orderBaseInfo, List<TdOrder> orderList) {


        List<OrderBillingPaymentDetails> paymentDetailsList = new ArrayList<>();
        Double storeCredit = 0D;
        Double storePrepay = 0D;
        for (TdOrder tdOrder : orderList) {
            if (null != tdOrder.getCredit() && tdOrder.getCredit() > AppConstant.PAY_UP_LIMIT) {

                storeCredit = CountUtil.add(storeCredit, tdOrder.getCredit());

            } else if ((null != tdOrder.getWalletMoney() && tdOrder.getWalletMoney() > AppConstant.PAY_UP_LIMIT)
                    || null != tdOrder.getAlipayMoney() && tdOrder.getAlipayMoney() > AppConstant.PAY_UP_LIMIT) {

                Double amount = CountUtil.add(null == tdOrder.getWalletMoney() ? 0D : tdOrder.getWalletMoney(),
                        null == tdOrder.getAlipayMoney() ? 0D : tdOrder.getAlipayMoney());

                storePrepay = CountUtil.add(amount, storePrepay);
            }
        }
        if (storeCredit > AppConstant.PAY_UP_LIMIT) {
            OrderBillingPaymentDetails paymentDetails = new OrderBillingPaymentDetails();
            paymentDetails.setOrderId(orderBaseInfo.getId());
            paymentDetails.setOrderNumber(orderBaseInfo.getOrderNumber());
            paymentDetails.setCreateTime(orderList.get(0).getPayTime());
            paymentDetails.setPayTime(orderList.get(0).getPayTime());
            paymentDetails.setPayType(OrderBillingPaymentType.STORE_CREDIT_MONEY);
            paymentDetails.setPayTypeDesc(OrderBillingPaymentType.STORE_CREDIT_MONEY.getDescription());

            paymentDetails.setPaymentSubjectType(PaymentSubjectType.DECORATE_MANAGER);
            paymentDetails.setPaymentSubjectTypeDesc(PaymentSubjectType.DECORATE_MANAGER.getDescription());
            paymentDetails.setAmount(storeCredit);
            paymentDetails.setReceiptNumber(OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
            paymentDetailsList.add(paymentDetails);
        }
        if (storePrepay > AppConstant.PAY_UP_LIMIT) {
            OrderBillingPaymentDetails paymentDetails = new OrderBillingPaymentDetails();
            paymentDetails.setOrderId(orderBaseInfo.getId());
            paymentDetails.setOrderNumber(orderBaseInfo.getOrderNumber());
            paymentDetails.setCreateTime(orderList.get(0).getPayTime());
            paymentDetails.setPayTime(orderList.get(0).getPayTime());
            paymentDetails.setPayType(OrderBillingPaymentType.ST_PREPAY);
            paymentDetails.setPayTypeDesc(OrderBillingPaymentType.ST_PREPAY.getDescription());

            paymentDetails.setPaymentSubjectType(PaymentSubjectType.DECORATE_MANAGER);
            paymentDetails.setPaymentSubjectTypeDesc(PaymentSubjectType.DECORATE_MANAGER.getDescription());

            paymentDetails.setAmount(storePrepay);
            paymentDetails.setReceiptNumber(OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
            paymentDetailsList.add(paymentDetails);
        }
        return paymentDetailsList;
    }
}
