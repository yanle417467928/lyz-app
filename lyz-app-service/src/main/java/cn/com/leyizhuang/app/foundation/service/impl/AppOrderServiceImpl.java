package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.exception.OrderPayableAmountException;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dao.AppStoreDAO;
import cn.com.leyizhuang.app.foundation.dao.ArrearsAuditDAO;
import cn.com.leyizhuang.app.foundation.dao.CityDAO;
import cn.com.leyizhuang.app.foundation.dao.OrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.CustomerCashCoupon;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.request.GoodsIdQtyParam;
import cn.com.leyizhuang.app.foundation.pojo.request.OrderLockExpendRequest;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.BillingSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.DeliverySimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponseGoods;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderArrearageInfoResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderGoodsListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderListResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jerry.Ren
 * Date: 2017/10/30.
 * Time: 18:17.
 */
@Service
public class AppOrderServiceImpl implements AppOrderService {


    @Resource
    private AppStoreDAO appStoreDAO;

    @Resource
    private CityDAO cityDAO;

    @Resource
    private OrderDAO orderDAO;

    @Resource
    private ArrearsAuditDAO arrearsAuditDAO;

    @Resource
    private UserService userService;

    @Resource
    private AppCustomerService customerService;

    @Resource
    private AppEmployeeService employeeService;

    @Resource
    private MaterialListService materialListService;

    @Resource
    private AppStoreService storeService;

    @Resource
    private CityService cityService;

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
    public Long existOrderGoodsInventory(Long cityId, List<GoodsIdQtyParam> goodsList, List<GoodsIdQtyParam> giftList, List<GoodsIdQtyParam> couponList) {

        if (null == cityId || null == goodsList) {
            return null;
        }
        //如果赠品不为空合并赠品
        if (null != giftList && !giftList.isEmpty()) {
            goodsList.addAll(giftList);
        }
        //如果券商品不为空合并券商品
        if (null != couponList && !couponList.isEmpty()) {
            goodsList.addAll(couponList);
        }
        //合并所有商品，相同商品数量相加
        Map<Long, Integer> mergeMap = new HashMap<Long, Integer>();
        for (GoodsIdQtyParam param : goodsList) {
            if (mergeMap.containsKey(param.getId())) {
                mergeMap.put(param.getId(), param.getQty() + mergeMap.get(param.getId()));
            } else {
                mergeMap.put(param.getId(), param.getQty());
            }
        }
        //遍历判断库存
        for (Long id : mergeMap.keySet()) {
            Boolean isHaveInventory = cityDAO.existGoodsCityInventory(cityId, id, mergeMap.get(id));
            if (!isHaveInventory) {
                return id;
            }
        }
        return null;
    }

    @Override
    public PageInfo<OrderBaseInfo> getOrderListByUserIDAndIdentityType(Long userID, Integer identityType, Integer showStatus, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<OrderBaseInfo> orderBaseInfoList = orderDAO.getOrderListByUserIDAndIdentityType(userID, AppIdentityType.getAppIdentityTypeByValue(identityType), showStatus);
        return new PageInfo<>(orderBaseInfoList);
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
    public OrderBaseInfo getOrderByOrderNumber(String orderNumber) {
        return orderDAO.findByOrderNumber(orderNumber);
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
    @Transactional(rollbackFor = Exception.class)
    public OrderBillingPaymentDetails savePaymentDetails(OrderBillingPaymentDetails orderBillingPaymentDetails) {
        this.orderDAO.savePaymentDetails(orderBillingPaymentDetails);
        return orderBillingPaymentDetails;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderBillingDetails updateOwnMoneyByOrderNo(OrderBillingDetails orderBillingDetails) {
        this.orderDAO.updateOwnMoneyByOrderNo(orderBillingDetails);
        return orderBillingDetails;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderBaseInfo updateOrderStatusByOrderNo(OrderBaseInfo orderBaseInfo) {
        this.orderDAO.updateOrderStatusByOrderNo(orderBaseInfo);
        return orderBaseInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderBillingPaymentDetails(String orderNumber, Double money, String replyNumber, String receiptNumber) {
        OrderBillingPaymentDetails orderBillingPaymentDetails = new OrderBillingPaymentDetails();
        OrderBaseInfo orderBaseInfo = orderDAO.getOrderDetail(orderNumber);
        Date repaymentTime = new Date();
        if (null != orderBaseInfo) {
            orderBillingPaymentDetails.setOrderId(orderBaseInfo.getId());
        }
        orderBillingPaymentDetails.setOrderNumber(orderNumber);

        orderBillingPaymentDetails.setPayTime(repaymentTime);
        orderBillingPaymentDetails.setPayType(OrderBillingPaymentType.ALIPAY);
        orderBillingPaymentDetails.setAmount(money);
        orderBillingPaymentDetails.setReplyCode(replyNumber);
        orderBillingPaymentDetails.setReceiptNumber(receiptNumber);
        //保存还款记录
        orderDAO.savePaymentDetails(orderBillingPaymentDetails);
        //导购欠款还款后修改欠款审核表
        arrearsAuditDAO.updateStatusAndrRepaymentTimeByOrderNumber(repaymentTime, orderNumber);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderBaseInfo(OrderBaseInfo orderBaseInfo) {
        if (null != orderBaseInfo) {
            orderDAO.saveOrderBaseInfo(orderBaseInfo);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderLogisticsInfo(OrderLogisticsInfo orderLogisticsInfo) {
        if (null != orderLogisticsInfo) {
            orderDAO.saveOrderLogisticsInfo(orderLogisticsInfo);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderGoodsInfo(OrderGoodsInfo goodsInfo) {
        if (null != goodsInfo) {
            orderDAO.saveOrderGoodsInfo(goodsInfo);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderBillingDetails(OrderBillingDetails orderBillingDetails) {
        if (null != orderBillingDetails) {
            orderDAO.saveOrderBillingDetails(orderBillingDetails);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderBillingPaymentDetail(OrderBillingPaymentDetails paymentDetail) {
        if (null != paymentDetail) {
            orderDAO.saveOrderBillingPaymentDetail(paymentDetail);
        }
    }

    @Override
    public OrderBaseInfo createOrderBaseInfo(Long cityId, Long userId, Integer identityType, Long customerId, String deliveryType, String remark) {
        OrderBaseInfo tempOrder = new OrderBaseInfo();
        //设置订单创建时间
        Calendar calendar = Calendar.getInstance();
        tempOrder.setCreateTime(calendar.getTime());
        //设置订单过期时间
        calendar.add(Calendar.MINUTE, AppConstant.ORDER_EFFECTIVE_MINUTE);
        tempOrder.setEffectiveEndTime(calendar.getTime());
        //设置订单状态
        tempOrder.setStatus(AppOrderStatus.UNPAID);
        //设置订单物流状态
        tempOrder.setDeliveryStatus(LogisticStatus.INITIAL);
        //设置订单类型 买券、出货
        tempOrder.setOrderType(AppOrderType.SHIPMENT);
        //设置是否已评价
        tempOrder.setIsEvaluated(Boolean.FALSE);
        //生成并设置订单号
        String orderNumber = OrderUtils.generateOrderNumber(cityId);
        tempOrder.setOrderNumber(orderNumber);
        //设置订单备注信息
        tempOrder.setRemark(remark);
        //设置订单配送方式
        if (deliveryType.equalsIgnoreCase(AppDeliveryType.HOUSE_DELIVERY.getValue())) {
            tempOrder.setDeliveryType(AppDeliveryType.HOUSE_DELIVERY);
        } else if (deliveryType.equalsIgnoreCase(AppDeliveryType.SELF_TAKE.getValue())) {
            tempOrder.setDeliveryType(AppDeliveryType.SELF_TAKE);
        }
        //设置下单人所在门店信息
        AppStore userStore = storeService.findStoreByUserIdAndIdentityType(userId, identityType);
        tempOrder.setStoreId(userStore.getStoreId());
        tempOrder.setStoreCode(userStore.getStoreCode());
        tempOrder.setStoreStructureCode(userStore.getStoreStructureCode());
        //设置城市信息
        tempOrder.setCityId(userStore.getCityId());
        if (null != userStore.getCity()) {
            tempOrder.setCityName(userStore.getCity());
        } else {
            City city = cityService.findById(cityId);
            tempOrder.setCityName(city.getName());
        }
        tempOrder.setSobId(userStore.getSobId());
        tempOrder.setStoreOrgId(userStore.getStoreId());

        switch (identityType) {
            //导购代下单
            case 0:
                tempOrder.setOrderSubjectType(AppOrderSubjectType.STORE);
                tempOrder.setCreatorIdentityType(AppIdentityType.SELLER);
                AppEmployee employee = employeeService.findById(userId);
                //写入创单人信息
                tempOrder.setCreatorId(employee.getEmpId());
                tempOrder.setCreatorName(employee.getName());
                tempOrder.setCreatorPhone(employee.getMobile());
                //写入导购信息
                tempOrder.setSalesConsultId(employee.getEmpId());
                tempOrder.setSalesConsultName(employee.getName());
                tempOrder.setSalesConsultPhone(employee.getMobile());
                AppCustomer customer = customerService.findById(customerId);
                //写入顾客信息
                tempOrder.setCustomerId(customer.getCusId());
                tempOrder.setCustomerName(customer.getName());
                tempOrder.setCustomerPhone(customer.getMobile());
                tempOrder.setCustomerType(customer.getCustomerType());
                break;
            //顾客下单
            case 6:
                tempOrder.setOrderSubjectType(AppOrderSubjectType.STORE);
                tempOrder.setCreatorIdentityType(AppIdentityType.CUSTOMER);
                AppCustomer appCustomer = customerService.findById(userId);
                //写入创单人信息
                tempOrder.setCreatorId(appCustomer.getCusId());
                tempOrder.setCreatorName(appCustomer.getName());
                tempOrder.setCreatorPhone(appCustomer.getMobile());
                //写入导购信息
                if (null != appCustomer.getSalesConsultId()) {
                    AppEmployee seller = employeeService.findById(appCustomer.getSalesConsultId());
                    tempOrder.setSalesConsultId(seller.getEmpId());
                    tempOrder.setSalesConsultName(seller.getName());
                    tempOrder.setSalesConsultPhone(seller.getMobile());
                }
                //写入顾客信息
                tempOrder.setCustomerId(appCustomer.getCusId());
                tempOrder.setCustomerName(appCustomer.getName());
                tempOrder.setCustomerPhone(appCustomer.getMobile());
                tempOrder.setCustomerType(appCustomer.getCustomerType());
                break;
            //装饰公司经理下单
            case 2:
                tempOrder.setOrderSubjectType(AppOrderSubjectType.FIT);
                tempOrder.setCreatorIdentityType(AppIdentityType.DECORATE_MANAGER);
                AppEmployee decoratorManager = employeeService.findById(userId);
                //写入创单人信息
                tempOrder.setCreatorId(decoratorManager.getEmpId());
                tempOrder.setCreatorName(decoratorManager.getName());
                tempOrder.setCustomerPhone(decoratorManager.getMobile());
                break;
            default:
                break;
        }
        return tempOrder;
    }

    @Override
    public OrderLogisticsInfo createOrderLogisticInfo(DeliverySimpleInfo deliverySimpleInfo) {
        OrderLogisticsInfo logisticsInfo = new OrderLogisticsInfo();
        if (deliverySimpleInfo.getDeliveryType().equalsIgnoreCase(AppDeliveryType.SELF_TAKE.getValue())) {
            logisticsInfo.setDeliveryType(AppDeliveryType.SELF_TAKE);
            AppStore bookingStore = storeService.findById(deliverySimpleInfo.getBookingStoreId());
            if (null != bookingStore) {
                logisticsInfo.setBookingStoreCode(bookingStore.getStoreCode());
                logisticsInfo.setBookingStoreName(bookingStore.getStoreName());
                logisticsInfo.setBookingStoreAddress(bookingStore.getDetailedAddress());
            } else {
                throw new RuntimeException("未找到该门店!");
            }
        } else if (deliverySimpleInfo.getDeliveryType().equalsIgnoreCase(AppDeliveryType.HOUSE_DELIVERY.getValue())) {
            logisticsInfo.setDeliveryType(AppDeliveryType.HOUSE_DELIVERY);
            logisticsInfo.setDeliveryTime(deliverySimpleInfo.getDeliveryTime());
            logisticsInfo.setReceiver(deliverySimpleInfo.getReceiver());
            logisticsInfo.setReceiverPhone(deliverySimpleInfo.getReceiverPhone());
            logisticsInfo.setDeliveryCity(deliverySimpleInfo.getDeliveryCity());
            logisticsInfo.setDeliveryCounty(deliverySimpleInfo.getDeliveryCounty());
            logisticsInfo.setDeliveryStreet(deliverySimpleInfo.getDeliveryStreet());
            logisticsInfo.setResidenceName(deliverySimpleInfo.getResidenceName());
            logisticsInfo.setDetailedAddress(deliverySimpleInfo.getDetailedAddress());
            logisticsInfo.setShippingAddress(logisticsInfo.getDeliveryCity().trim() + logisticsInfo.getDeliveryCounty().trim()
                    + logisticsInfo.getDeliveryStreet().trim()
                    + logisticsInfo.getResidenceName().trim() + logisticsInfo.getDetailedAddress().trim());
        }
        logisticsInfo.setIsOwnerReceiving(deliverySimpleInfo.getIsOwnerReceiving());
        return logisticsInfo;
    }


    @Override
    public OrderBillingDetails createOrderBillingDetails(OrderBillingDetails orderBillingDetails, Long userId, Integer identityType,
                                                         BillingSimpleInfo billing, List<Long> cashCouponIds, List<OrderGoodsInfo> productCouponGoodsList) {

        orderBillingDetails.setCreateTime(Calendar.getInstance().getTime());
        orderBillingDetails.setCollectionAmount(OrderUtils.replaceNullWithZero(billing.getCollectionAmount()));
        orderBillingDetails.setFreight(OrderUtils.replaceNullWithZero(billing.getFreight()));

        //计算并设置产品券折扣
        if (null != productCouponGoodsList && productCouponGoodsList.size() > 0) {
            Double productCouponDiscount = 0D;
            for (OrderGoodsInfo goodsInfo : productCouponGoodsList) {
                productCouponDiscount += goodsInfo.getSettlementPrice() * goodsInfo.getOrderQuantity();
            }
            orderBillingDetails.setProductCouponDiscount(productCouponDiscount);
        } else {
            orderBillingDetails.setProductCouponDiscount(0D);
        }
        //计算并设置乐币折扣
        orderBillingDetails.setLebiQuantity(billing.getLeBiQuantity());
        if (null != billing.getLeBiQuantity()) {
            orderBillingDetails.setLebiCashDiscount(billing.getLeBiQuantity() / AppConstant.RMB_TO_LEBI_RATIO);
            orderBillingDetails.setLebiQuantity(billing.getLeBiQuantity());
        }
        //计算并设置优惠券减少金额
        if (identityType == AppIdentityType.CUSTOMER.getValue() ||
                identityType == AppIdentityType.SELLER.getValue()) {
            Double cashCouponDiscount = 0D;
            if (null != cashCouponIds && cashCouponIds.size() > 0) {
                for (Long id : cashCouponIds) {
                    CustomerCashCoupon cashCoupon = customerService.findCashCouponByCcid(id);
                    if (null != cashCoupon) {
                        if (cashCoupon.getStatus() && !cashCoupon.getIsUsed() && cashCoupon.getEffectiveStartTime().before(new Date()) &&
                                (cashCoupon.getEffectiveEndTime().after(new Date()) || cashCoupon.getEffectiveEndTime() == null)) {
                            cashCouponDiscount += cashCoupon.getDenomination();
                        }
                    } else {
                        throw new RuntimeException("优惠券不存在！");
                    }
                }
            }
            orderBillingDetails.setCashCouponDiscount(cashCouponDiscount);
        }
        //设置顾客预存款
        if (identityType == AppIdentityType.CUSTOMER.getValue()) {
            Double cusPreDeposit = 0D;
            if (null != billing.getCusPreDeposit()) {
                cusPreDeposit = billing.getCusPreDeposit();
            }
            orderBillingDetails.setCusPreDeposit(cusPreDeposit);
        }
        //设置门店预存款
        if (identityType == AppIdentityType.SELLER.getValue() ||
                identityType == AppIdentityType.DECORATE_MANAGER.getValue()) {
            Double stPreDeposit = 0D;
            if (null != billing.getStPreDeposit()) {
                stPreDeposit = billing.getStPreDeposit();
            }
            orderBillingDetails.setStPreDeposit(stPreDeposit);
        } else {
            orderBillingDetails.setStPreDeposit(0D);
        }
        //设置导购信用额度
        if (identityType == AppIdentityType.SELLER.getValue()) {
            Double empCreditMoney = 0D;
            if (null != billing.getEmpCreditMoney()) {
                empCreditMoney = billing.getEmpCreditMoney();
            }
            orderBillingDetails.setEmpCreditMoney(empCreditMoney);
        } else {
            orderBillingDetails.setEmpCreditMoney(0D);
        }
        //设置门店信用额度
        if (identityType == AppIdentityType.DECORATE_MANAGER.getValue() ||
                identityType == AppIdentityType.SELLER.getValue()) {
            Double stCreditMoney = 0D;
            if (null != billing.getStoreCreditMoney()) {
                stCreditMoney = billing.getStoreCreditMoney();
            }
            orderBillingDetails.setStoreCreditMoney(stCreditMoney);
        } else {
            orderBillingDetails.setStoreCreditMoney(0D);
        }
        //设置门店现金返利
        if (identityType == AppIdentityType.DECORATE_MANAGER.getValue()) {
            Double stSubvention = 0D;
            if (null != billing.getStoreSubvention()) {
                stSubvention = billing.getStoreSubvention();
            }
            orderBillingDetails.setStoreSubvention(stSubvention);
        } else {
            orderBillingDetails.setStoreSubvention(0D);
        }

        //计算订单金额小计以及应付款
        Double orderAmountSubtotal;
        Double amountPayable = 0D;
        switch (identityType) {
            case 6:
                orderAmountSubtotal = orderBillingDetails.getTotalGoodsPrice()
                        + OrderUtils.replaceNullWithZero(orderBillingDetails.getFreight())
                        - OrderUtils.replaceNullWithZero(orderBillingDetails.getProductCouponDiscount())
                        - OrderUtils.replaceNullWithZero(orderBillingDetails.getMemberDiscount())
                        - OrderUtils.replaceNullWithZero(orderBillingDetails.getPromotionDiscount())
                        - OrderUtils.replaceNullWithZero(orderBillingDetails.getLebiCashDiscount())
                        - OrderUtils.replaceNullWithZero(orderBillingDetails.getCashCouponDiscount());
                orderBillingDetails.setOrderAmountSubtotal(orderAmountSubtotal);

                amountPayable = orderBillingDetails.getOrderAmountSubtotal()
                        - OrderUtils.replaceNullWithZero(orderBillingDetails.getCusPreDeposit());
                orderBillingDetails.setAmountPayable(amountPayable);
                orderBillingDetails.setArrearage(orderBillingDetails.getAmountPayable());
                break;
            case 0:
                orderAmountSubtotal = orderBillingDetails.getTotalGoodsPrice()
                        + OrderUtils.replaceNullWithZero(orderBillingDetails.getFreight())
                        - OrderUtils.replaceNullWithZero(orderBillingDetails.getProductCouponDiscount())
                        - OrderUtils.replaceNullWithZero(orderBillingDetails.getMemberDiscount())
                        - OrderUtils.replaceNullWithZero(orderBillingDetails.getPromotionDiscount())
                        - OrderUtils.replaceNullWithZero(orderBillingDetails.getLebiCashDiscount())
                        - OrderUtils.replaceNullWithZero(orderBillingDetails.getCashCouponDiscount());
                orderBillingDetails.setOrderAmountSubtotal(orderAmountSubtotal);

                amountPayable = orderBillingDetails.getOrderAmountSubtotal()
                        - OrderUtils.replaceNullWithZero(orderBillingDetails.getStPreDeposit())
                        - OrderUtils.replaceNullWithZero(orderBillingDetails.getEmpCreditMoney());
                orderBillingDetails.setAmountPayable(amountPayable);
                orderBillingDetails.setArrearage(orderBillingDetails.getAmountPayable() + orderBillingDetails.getEmpCreditMoney());
                break;
            case 2:
                orderAmountSubtotal = orderBillingDetails.getTotalGoodsPrice()
                        + OrderUtils.replaceNullWithZero(orderBillingDetails.getFreight())
                        - OrderUtils.replaceNullWithZero(orderBillingDetails.getMemberDiscount())
                        - OrderUtils.replaceNullWithZero(orderBillingDetails.getPromotionDiscount());
                orderBillingDetails.setOrderAmountSubtotal(orderAmountSubtotal);

                amountPayable = orderBillingDetails.getOrderAmountSubtotal()
                        - OrderUtils.replaceNullWithZero(orderBillingDetails.getStPreDeposit())
                        - OrderUtils.replaceNullWithZero(orderBillingDetails.getStoreCreditMoney())
                        - OrderUtils.replaceNullWithZero(orderBillingDetails.getStoreSubvention());
                orderBillingDetails.setAmountPayable(amountPayable);
                orderBillingDetails.setArrearage(orderBillingDetails.getAmountPayable() + orderBillingDetails.getStoreCreditMoney());
                break;
            default:
                break;
        }
        if (amountPayable < 0D) {
            throw new OrderPayableAmountException("订单应付款金额异常(<0)");
        }
       // orderBillingDetails.setArrearage(orderBillingDetails.getAmountPayable());
        //根据应付金额判断订单账单是否已付清
        if (orderBillingDetails.getArrearage() <= AppConstant.PAY_UP_LIMIT) {
            orderBillingDetails.setIsPayUp(true);
        } else {
            orderBillingDetails.setIsPayUp(false);
        }
        return orderBillingDetails;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderBaseInfo(OrderBaseInfo baseInfo) {
        if (null != baseInfo) {
            orderDAO.updateOrderBaseInfo(baseInfo);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderBillingDetails(OrderBillingDetails billingDetails) {
        if (null != billingDetails) {
            orderDAO.updateOrderBillingDetails(billingDetails);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderStatusAndDeliveryStatusByOrderNo(AppOrderStatus status, LogisticStatus deliveryStatus, String orderNumber) {
        orderDAO.updateOrderStatusAndDeliveryStatusByOrderNo(status, deliveryStatus, orderNumber);
    }

    @Override
    public List<OrderBillingPaymentDetails> getOrderBillingDetailListByOrderNo(String orderNo) {
        if (StringUtils.isNotBlank(orderNo)) {
            return orderDAO.getOrderBillingDetailListByOrderNo(orderNo);
        }
        return null;
    }

    @Override
    public List<OrderGoodsListResponse> getOrderGoodsList(String orderNumber) {
        return orderDAO.getOrderGoodsList(orderNumber);
    }

    @Override
    public void saveOrderCouponInfo(OrderCouponInfo couponInfo) {
        if (null != couponInfo) {
            orderDAO.saveOrderCouponInfo(couponInfo);
        }
    }

    @Override
    public PageInfo<OrderListResponse> getPendingEvaluationOrderListByUserIDAndIdentityType(Long userID, Integer identityType, Integer page, Integer size) {
        if (null != userID && null != identityType && null != page && null != size) {
            PageHelper.startPage(page, size);
            List<OrderListResponse> orderListResponses = orderDAO.getPendingEvaluationOrderListByUserIDAndIdentityType(userID, AppIdentityType.getAppIdentityTypeByValue(identityType));
            return new PageInfo<>(orderListResponses);
        }
        return null;
    }

    @Override
    public List<OrderCouponInfo> getOrderCouponInfoByOrderNumber(String orderNumber) {
        if (StringUtils.isNotBlank(orderNumber)) {
            return orderDAO.getOrderCouponInfoByOrderNumber(orderNumber);
        }
        return null;
    }

    @Override
    public OrderArrearageInfoResponse getOrderArrearageInfo(String orderNo) {
        return this.orderDAO.getOrderArrearageInfo(orderNo);
    }

    @Override
    public Map<String, Integer> getAppOrderQuantityByEmpId(Long id) {
        Integer unpaidOrderQuantity = this.orderDAO.getUnpaidOrderQuantityByEmpId(id);
        Integer pendingReceiveOrderQuantity = this.orderDAO.getPendingReceiveOrderQuantityByEmpId(id);
        Integer isEvaluatedOrderQuantity = this.orderDAO.getIsEvaluatedOrderQuantityByEmpId(id);
        Integer returningOrderQuantity = this.orderDAO.getReturningOrderQuantityByEmpId(id);
        Map quantityMap = new HashMap();
        quantityMap.put("unpaidOrderQuantity", unpaidOrderQuantity);
        quantityMap.put("pendingReceiveOrderQuantity", pendingReceiveOrderQuantity);
        quantityMap.put("isEvaluatedOrderQuantity", isEvaluatedOrderQuantity);
        quantityMap.put("returningOrderQuantity", returningOrderQuantity);
        return quantityMap;
    }

    @Override
    public Map<String, Integer> getAppOrderQuantityByCusId(Long id) {
        Integer unpaidOrderQuantity = this.orderDAO.getUnpaidOrderQuantityByCusId(id);
        Integer pendingReceiveOrderQuantity = this.orderDAO.getPendingReceiveOrderQuantityByCusId(id);
        Integer isEvaluatedOrderQuantity = this.orderDAO.getIsEvaluatedOrderQuantityByCusId(id);
        Integer returningOrderQuantity = this.orderDAO.getReturningOrderQuantityByCusId(id);
        Map quantityMap = new HashMap();
        quantityMap.put("unpaidOrderQuantity", unpaidOrderQuantity);
        quantityMap.put("pendingReceiveOrderQuantity", pendingReceiveOrderQuantity);
        quantityMap.put("isEvaluatedOrderQuantity", isEvaluatedOrderQuantity);
        quantityMap.put("returningOrderQuantity", returningOrderQuantity);
        return quantityMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderJxPriceDifferenceReturnDetails(OrderJxPriceDifferenceReturnDetails returnDetails) {
        if (null != returnDetails) {
            orderDAO.saveOrderJxPriceDifferenceReturnDetails(returnDetails);
        }
    }

    @Override
    public List<OrderJxPriceDifferenceReturnDetails> getOrderJxPriceDifferenceReturnDetailsByOrderNumber(String orderNumber) {
        if (null != orderNumber) {
            return orderDAO.getOrderJxPriceDifferenceReturnDetailsByOrderNumber(orderNumber);
        }
        return null;
    }

    @Override
    public void updateReturnableQuantityAndReturnQuantityById(int returnQty, Long orderGoodsId) {
        orderDAO.updateReturnableQuantityAndReturnQuantityById(returnQty, orderGoodsId);
    }

    @Override
    public void updateOrderGoodsShippingQuantity(String orderNo, String gCode, Integer dAckQty) {
        if (StringUtils.isNotBlank(orderNo) && StringUtils.isNotBlank(gCode)) {
            orderDAO.updateOrderGoodsShippingQuantity(orderNo, gCode, dAckQty);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveWeChatOrderBillingPaymentDetails(String orderNumber, Double money, String replyNumber, String receiptNumber) {
        OrderBillingPaymentDetails orderBillingPaymentDetails = new OrderBillingPaymentDetails();
        OrderBaseInfo orderBaseInfo = orderDAO.getOrderDetail(orderNumber);
        OrderBillingDetails orderBillingDetails = orderDAO.getOrderBillingDetail(orderNumber);
        Date repaymentTime = new Date();
        if (null != orderBillingDetails) {
            orderBillingDetails.setOnlinePayType(OnlinePayType.WE_CHAT);
            orderBillingDetails.setOnlinePayAmount(money);
            orderBillingDetails.setOnlinePayTime(repaymentTime);
            orderBillingDetails.setIsPayUp(Boolean.TRUE);
            orderBillingDetails.setPayUpTime(repaymentTime);
            orderBillingDetails.setArrearage(0D);
            orderDAO.updateOrderBillingDetails(orderBillingDetails);

        }
        if (null != orderBaseInfo) {
            orderBillingPaymentDetails.setOrderId(orderBaseInfo.getId());
        }
        orderBillingPaymentDetails.setOrderNumber(orderNumber);

        orderBillingPaymentDetails.setPayTime(repaymentTime);
        orderBillingPaymentDetails.setPayType(OrderBillingPaymentType.WE_CHAT);
        orderBillingPaymentDetails.setAmount(money);
        orderBillingPaymentDetails.setReplyCode(replyNumber);
        orderBillingPaymentDetails.setReceiptNumber(receiptNumber);
        //保存还款记录
        orderDAO.savePaymentDetails(orderBillingPaymentDetails);
        //导购欠款还款后修改欠款审核表
        arrearsAuditDAO.updateStatusAndrRepaymentTimeByOrderNumber(repaymentTime, orderNumber);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAliPayOrderBillingPaymentDetails(String orderNumber, Double money, String replyNumber, String receiptNumber) {
        OrderBillingPaymentDetails orderBillingPaymentDetails = new OrderBillingPaymentDetails();
        OrderBaseInfo orderBaseInfo = orderDAO.getOrderDetail(orderNumber);
        OrderBillingDetails orderBillingDetails = orderDAO.getOrderBillingDetail(orderNumber);
        Date repaymentTime = new Date();
        if (null != orderBillingDetails) {
            orderBillingDetails.setOnlinePayType(OnlinePayType.ALIPAY);
            orderBillingDetails.setOnlinePayAmount(money);
            orderBillingDetails.setOnlinePayTime(repaymentTime);
            orderBillingDetails.setIsPayUp(Boolean.TRUE);
            orderBillingDetails.setPayUpTime(repaymentTime);
            orderBillingDetails.setArrearage(0D);
            orderDAO.updateOrderBillingDetails(orderBillingDetails);

        }
        if (null != orderBaseInfo) {
            orderBillingPaymentDetails.setOrderId(orderBaseInfo.getId());
        }
        orderBillingPaymentDetails.setOrderNumber(orderNumber);

        orderBillingPaymentDetails.setPayTime(repaymentTime);
        orderBillingPaymentDetails.setPayType(OrderBillingPaymentType.ALIPAY);
        orderBillingPaymentDetails.setAmount(money);
        orderBillingPaymentDetails.setReplyCode(replyNumber);
        orderBillingPaymentDetails.setReceiptNumber(receiptNumber);
        //保存还款记录
        orderDAO.savePaymentDetails(orderBillingPaymentDetails);
        //导购欠款还款后修改欠款审核表
        arrearsAuditDAO.updateStatusAndrRepaymentTimeByOrderNumber(repaymentTime, orderNumber);
    }

    @Override
    public void updateOrderLogisticInfoByDeliveryClerkNo(String driver, String warehouse, String orderNo) {
        if (StringUtils.isNotBlank(driver)) {
            AppEmployee clerk = employeeService.findDeliveryByClerkNo(driver);

            OrderLogisticsInfo logisticsInfo = new OrderLogisticsInfo();
            logisticsInfo.setOrdNo(orderNo);
            logisticsInfo.setDeliveryClerkId(clerk.getEmpId());
            logisticsInfo.setDeliveryClerkNo(driver);
            logisticsInfo.setDeliveryClerkName(clerk.getName());
            logisticsInfo.setDeliveryClerkPhone(clerk.getMobile());
            logisticsInfo.setWarehouse(warehouse);

            orderDAO.updateOrderLogisticInfo(logisticsInfo);
        }
    }
}
