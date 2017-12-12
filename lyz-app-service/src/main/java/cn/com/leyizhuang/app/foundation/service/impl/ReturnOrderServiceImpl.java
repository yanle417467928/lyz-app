package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.exception.OrderSaveException;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dao.ReturnOrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderCouponInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.ReturnDeliverySimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponseGoods;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.common.util.TimeTransformUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by caiyu on 2017/12/4.
 */
@Service
public class ReturnOrderServiceImpl implements ReturnOrderService {
    @Resource
    private ReturnOrderDAO returnOrderDAO;
    @Resource
    private AppStoreService appStoreService;
    @Resource
    private StorePreDepositLogService storePreDepositLogService;
    @Resource
    private StoreCreditMoneyLogService storeCreditMoneyLogService;
    @Resource
    private AppEmployeeService appEmployeeService;
    @Resource
    private AppCustomerService appCustomerService;
    @Resource
    private ProductCouponService productCouponService;
    @Resource
    private LeBiVariationLogService leBiVariationLogService;
    @Resource
    private CashCouponService cashCouponService;
    @Resource
    private ReturnOrderService returnOrderService;

    @Override
    public ReturnOrderBaseInfo createReturnOrderBaseInfo(Long orderId, String orderNo, Date orderTime, String remarksInfo, Long creatorId,
                                                         Integer creatorIdentityType, String reasonInfo, String returnPic, AppOrderType orderType) {

        ReturnOrderBaseInfo baseInfo = new ReturnOrderBaseInfo();
        baseInfo.setOrderId(orderId);
        baseInfo.setOrderNo(orderNo);
        baseInfo.setOrderTime(orderTime);
        baseInfo.setOrderType(orderType);
        baseInfo.setRemarksInfo(remarksInfo);
        baseInfo.setCreatorId(creatorId);
        baseInfo.setCreatorIdentityType(AppIdentityType.getAppIdentityTypeByValue(creatorIdentityType));
        baseInfo.setReasonInfo(reasonInfo);
        baseInfo.setReturnPic(returnPic);
        baseInfo.setReturnTime(Calendar.getInstance().getTime());
        baseInfo.setReturnNo(OrderUtils.getReturnNumber());
        baseInfo.setReturnType(ReturnOrderType.NORMAL_RETURN);
        baseInfo.setReturnStatus(AppReturnOrderStatus.PENDING_PICK_UP);
        if (creatorIdentityType == 6) {
            AppCustomer customer = appCustomerService.findById(creatorId);
            baseInfo.setCustomerType(customer.getCustomerType());
            baseInfo.setCreatorPhone(customer.getMobile());
        } else if (creatorIdentityType == 2) {
            AppEmployee employee = appEmployeeService.findById(creatorId);
            baseInfo.setCreatorPhone(employee.getMobile());
        }
        return baseInfo;
    }

    @Override
    public ReturnOrderLogisticInfo createReturnOrderLogisticInfo(ReturnDeliverySimpleInfo returnDeliveryInfo) {
        ReturnOrderLogisticInfo returnOrderLogisticInfo = new ReturnOrderLogisticInfo();
        if (returnDeliveryInfo.getDeliveryType().equalsIgnoreCase(AppDeliveryType.RETURN_STORE.getValue())) {
            returnOrderLogisticInfo.setDeliveryType(AppDeliveryType.RETURN_STORE);
            returnOrderLogisticInfo.setReturnStoreCode(returnDeliveryInfo.getReturnStoreCode());
            returnOrderLogisticInfo.setReturnStoreName(returnDeliveryInfo.getReturnStoreName());
            returnOrderLogisticInfo.setReturnStoreAddress(returnDeliveryInfo.getReturnStoreAddress());
        } else if (returnDeliveryInfo.getDeliveryType().equalsIgnoreCase(AppDeliveryType.HOUSE_PICK.getValue())) {
            returnOrderLogisticInfo.setDeliveryType(AppDeliveryType.HOUSE_PICK);
            returnOrderLogisticInfo.setDeliveryTime(returnDeliveryInfo.getDeliveryTime());
            returnOrderLogisticInfo.setRejecter(returnDeliveryInfo.getRejecter());
            returnOrderLogisticInfo.setRejecterPhone(returnDeliveryInfo.getRejecterPhone());
            returnOrderLogisticInfo.setDeliveryCity(returnDeliveryInfo.getDeliveryCity());
            returnOrderLogisticInfo.setDeliveryCounty(returnDeliveryInfo.getDeliveryCounty());
            returnOrderLogisticInfo.setDeliveryStreet(returnDeliveryInfo.getDeliveryStreet());
            returnOrderLogisticInfo.setResidenceName(returnDeliveryInfo.getResidenceName());
            returnOrderLogisticInfo.setDetailedAddress(returnDeliveryInfo.getDetailedAddress());
            returnOrderLogisticInfo.setReturnFullAddress(returnDeliveryInfo.getDeliveryCity() +
                    returnDeliveryInfo.getDeliveryCounty() +
                    returnDeliveryInfo.getDeliveryStreet() +
                    returnDeliveryInfo.getResidenceName() +
                    returnDeliveryInfo.getDetailedAddress());

        }

        return returnOrderLogisticInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveReturnOrderRelevantInfo(ReturnOrderBaseInfo returnOrderBaseInfo, ReturnOrderLogisticInfo returnOrderLogisticInfo,
                                            List<ReturnOrderGoodsInfo> returnOrderGoodsInfos, ReturnOrderBilling returnOrderBilling,
                                            List<ReturnOrderProductCoupon> productCouponList) throws OrderSaveException {

        if (null != returnOrderBaseInfo) {
            returnOrderDAO.saveReturnOrderBaseInfo(returnOrderBaseInfo);
            if (null != returnOrderBaseInfo.getRoid()) {
                Long roid = returnOrderBaseInfo.getRoid();
                if (null != returnOrderLogisticInfo) {
                    returnOrderLogisticInfo.setRoid(roid);
                    returnOrderDAO.saveReturnOrderLogisticsInfo(returnOrderLogisticInfo);
                }
                if (null != returnOrderGoodsInfos && !returnOrderGoodsInfos.isEmpty()) {
                    for (ReturnOrderGoodsInfo goodsInfo : returnOrderGoodsInfos) {
                        goodsInfo.setRoid(roid);
                        returnOrderDAO.saveReturnOrderGoodsInfo(goodsInfo);
                    }
                }
                if (null != returnOrderBilling) {
                    returnOrderBilling.setRoid(roid);
                    returnOrderDAO.saveReturnOrderBilling(returnOrderBilling);
                }
                if (null != productCouponList && !productCouponList.isEmpty()) {
                    for (ReturnOrderProductCoupon productCoupon : productCouponList) {
                        productCoupon.setRoid(roid);
                        returnOrderDAO.saveReturnOrderProductCoupon(productCoupon);
                    }
                }
            } else {
                throw new OrderSaveException("退单主键生成失败!");
            }
        }
    }

    @Override
    public void saveReturnOrderBaseInfo(ReturnOrderBaseInfo returnOrderBaseInfo) {
        returnOrderDAO.saveReturnOrderBaseInfo(returnOrderBaseInfo);
    }

    @Override
    public void modifyReturnOrderBaseInfo(ReturnOrderBaseInfo returnOrderBaseInfo) {
        returnOrderDAO.modifyReturnOrderBaseInfo(returnOrderBaseInfo);
    }

    @Override
    public ReturnOrderBaseInfo queryByReturnNo(String returnNo) {
        return returnOrderDAO.queryByReturnNo(returnNo);
    }

    @Override
    public void saveReturnOrderBilling(ReturnOrderBilling returnOrderBilling) {
        returnOrderDAO.saveReturnOrderBilling(returnOrderBilling);
    }

    @Override
    public void saveReturnOrderBillingDetail(ReturnOrderBillingDetail returnOrderBillingDetail) {
        returnOrderDAO.saveReturnOrderBillingDetail(returnOrderBillingDetail);
    }

    @Override
    public void saveReturnOrderCashCoupon(ReturnOrderCashCoupon returnOrderCashCoupon) {
        returnOrderDAO.saveReturnOrderCashCoupon(returnOrderCashCoupon);
    }

    @Override
    public void saveReturnOrderProductCoupon(ReturnOrderProductCoupon returnOrderProductCoupon) {
        returnOrderDAO.saveReturnOrderProductCoupon(returnOrderProductCoupon);
    }

    @Override
    public void saveReturnOrderGoodsInfo(ReturnOrderGoodsInfo returnOrderGoodsInfo) {
        returnOrderDAO.saveReturnOrderGoodsInfo(returnOrderGoodsInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void canselOrder(List<OrderGoodsInfo> orderGoodsInfoList, Long returnOrderId, Long userId, Integer identityType,
                            String returnNumber, String orderNumber, OrderBillingDetails orderBillingDetails, OrderBaseInfo orderBaseInfo) {
        Date date = new Date();
        //创建退货商品实体类
        ReturnOrderGoodsInfo returnGoodsInfo = new ReturnOrderGoodsInfo();
        for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
            //记录退单商品
            returnGoodsInfo.setRoid(returnOrderId);
            returnGoodsInfo.setReturnNo(returnNumber);
            returnGoodsInfo.setSku(orderGoodsInfo.getSku());
            returnGoodsInfo.setSkuName(orderGoodsInfo.getSkuName());
            returnGoodsInfo.setRetailPrice(orderGoodsInfo.getRetailPrice());
            returnGoodsInfo.setVipPrice(orderGoodsInfo.getVIPPrice());
            returnGoodsInfo.setWholesalePrice(orderGoodsInfo.getWholesalePrice());
            returnGoodsInfo.setReturnPrice(orderGoodsInfo.getReturnPrice());
            returnGoodsInfo.setReturnQty(orderGoodsInfo.getOrderQuantity());
            //保存退单商品信息
            returnOrderService.saveReturnOrderGoodsInfo(returnGoodsInfo);
        }
        //创建退单退款总记录实体
        ReturnOrderBilling returnOrderBilling = new ReturnOrderBilling();
        returnOrderBilling.setRoid(returnOrderId);
        returnOrderBilling.setReturnNo(returnNumber);
        returnOrderBilling.setPreDeposit(orderBillingDetails.getCusPreDeposit() == null ? 0.00 : orderBillingDetails.getCusPreDeposit());
        returnOrderBilling.setCreditMoney(orderBillingDetails.getEmpCreditMoney() == null ? 0.00 : orderBillingDetails.getEmpCreditMoney());
        returnOrderBilling.setStPreDeposit(orderBillingDetails.getStPreDeposit() == null ? 0.00 : orderBillingDetails.getStPreDeposit());
        returnOrderBilling.setStCreditMoney(orderBillingDetails.getStoreCreditMoney() == null ? 0.00 : orderBillingDetails.getStoreCreditMoney());
        returnOrderBilling.setStSubvention(orderBillingDetails.getStoreSubvention() == null ? 0.00 : orderBillingDetails.getStoreSubvention());
        returnOrderBilling.setOnlinePay(orderBillingDetails.getOnlinePayAmount() == null ? 0.00 : orderBillingDetails.getOnlinePayAmount());
        returnOrderBilling.setCash(0.00);
        //添加保存退单退款总记录
        returnOrderService.saveReturnOrderBilling(returnOrderBilling);

        //********************************返还虚拟货币********************************
        if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.CUSTOMER)) {
            //返回乐币
            if (orderBillingDetails.getLebiQuantity() != null && orderBillingDetails.getLebiQuantity() > 0) {
                //获取顾客当前乐币数量
                CustomerLeBi customerLeBi = appCustomerService.findCustomerLebiByCustomerId(userId);
                //返还乐币后顾客乐币数量
                Integer lebiTotal = (customerLeBi.getQuantity() + orderBillingDetails.getLebiQuantity());
                //更改顾客乐币数量
                leBiVariationLogService.updateLeBiQtyByUserId(lebiTotal, date, userId);
                //记录乐币日志
                CustomerLeBiVariationLog leBiVariationLog = new CustomerLeBiVariationLog();
                leBiVariationLog.setCusId(userId);
                leBiVariationLog.setVariationQuantity(orderBillingDetails.getLebiQuantity());
                leBiVariationLog.setAfterVariationQuantity(lebiTotal);
                leBiVariationLog.setVariationTime(date);
                leBiVariationLog.setLeBiVariationType(LeBiVariationType.CANCEL_ORDER);
                leBiVariationLog.setVariationTypeDesc("取消订单");
                leBiVariationLog.setOrderNum(orderNumber);
                //保存日志
                leBiVariationLogService.addCustomerLeBiVariationLog(leBiVariationLog);
            }
            //返回顾客预存款
            if (orderBillingDetails.getCusPreDeposit() != null && orderBillingDetails.getCusPreDeposit() > 0) {
                //获取顾客预存款
                CustomerPreDeposit customerPreDeposit = appCustomerService.findByCusId(userId);
                //返还预存款后顾客预存款金额
                Double cusPreDeposit = (customerPreDeposit.getBalance() + orderBillingDetails.getCusPreDeposit());
                //更改顾客预存款金额
                appCustomerService.unlockCustomerDepositByUserIdAndDeposit(userId, orderBillingDetails.getCusPreDeposit());
                //记录预存款日志
                CusPreDepositLogDO cusPreDepositLogDO = new CusPreDepositLogDO();
                cusPreDepositLogDO.setCreateTime(TimeTransformUtils.UDateToLocalDateTime(date));
                cusPreDepositLogDO.setChangeMoney(orderBillingDetails.getCusPreDeposit());
                cusPreDepositLogDO.setOrderNumber(orderNumber);
                cusPreDepositLogDO.setChangeType(CustomerPreDepositChangeType.CANCEL_ORDER);
                cusPreDepositLogDO.setChangeTypeDesc("取消订单返还");
                cusPreDepositLogDO.setCusId(userId);
                cusPreDepositLogDO.setOperatorId(userId);
                cusPreDepositLogDO.setOperatorType(AppIdentityType.CUSTOMER);
                cusPreDepositLogDO.setBalance(cusPreDeposit);
                cusPreDepositLogDO.setDetailReason("取消订单");
                cusPreDepositLogDO.setTransferTime(TimeTransformUtils.UDateToLocalDateTime(date));
                cusPreDepositLogDO.setMerchantOrderNumber(null);
                //保存日志
                appCustomerService.addCusPreDepositLog(cusPreDepositLogDO);
            }
        }
        if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.SELLER)) {
            //返回门店预存款
            if (orderBillingDetails.getStPreDeposit() != null && orderBillingDetails.getStPreDeposit() > 0) {
                //获取门店预存款
                StorePreDeposit storePreDeposit = storePreDepositLogService.findStoreByUserId(userId);
                //返还预存款后门店预存款金额
                Double stPreDeposit = (storePreDeposit.getBalance() + orderBillingDetails.getStPreDeposit());
                //修改门店预存款
                storePreDepositLogService.updateStPreDepositByUserId(stPreDeposit, userId);
                //记录门店预存款变更日志
                StPreDepositLogDO stPreDepositLogDO = new StPreDepositLogDO();
                stPreDepositLogDO.setCreateTime(TimeTransformUtils.UDateToLocalDateTime(date));
                stPreDepositLogDO.setChangeMoney(orderBillingDetails.getStPreDeposit());
                stPreDepositLogDO.setRemarks("取消订单返还门店预存款");
                stPreDepositLogDO.setOrderNumber(orderNumber);
                stPreDepositLogDO.setChangeType(StorePreDepositChangeType.CANCEL_ORDER);
                stPreDepositLogDO.setStoreId(storePreDeposit.getStoreId());
                stPreDepositLogDO.setOperatorId(userId);
                stPreDepositLogDO.setOperatorType(AppIdentityType.SELLER);
                stPreDepositLogDO.setBalance(stPreDeposit);
                stPreDepositLogDO.setDetailReason("取消订单");
                stPreDepositLogDO.setTransferTime(TimeTransformUtils.UDateToLocalDateTime(date));
                //保存日志
                storePreDepositLogService.save(stPreDepositLogDO);
            }
            //返回导购信用额度
            if (orderBillingDetails.getEmpCreditMoney() != null && orderBillingDetails.getEmpCreditMoney() > 0) {
                //获取导购信用金
                EmpCreditMoney empCreditMoney = appEmployeeService.findEmpCreditMoneyByEmpId(userId);
                //返还信用金后导购信用金额度
                Double creditMoney = (empCreditMoney.getCreditLimitAvailable() + orderBillingDetails.getEmpCreditMoney());
                //修改导购信用额度
                appEmployeeService.unlockGuideCreditByUserIdAndCredit(userId, orderBillingDetails.getEmpCreditMoney());
                //记录导购信用金变更日志
                EmpCreditMoneyChangeLog empCreditMoneyChangeLog = new EmpCreditMoneyChangeLog();
                empCreditMoneyChangeLog.setEmpId(userId);
                empCreditMoneyChangeLog.setCreateTime(date);
                empCreditMoneyChangeLog.setCreditLimitAvailableChangeAmount(orderBillingDetails.getEmpCreditMoney());
                empCreditMoneyChangeLog.setCreditLimitAvailableAfterChange(creditMoney);
                empCreditMoneyChangeLog.setReferenceNumber(orderNumber);
                empCreditMoneyChangeLog.setChangeType(EmpCreditMoneyChangeType.CANCEL_ORDER);
                empCreditMoneyChangeLog.setChangeTypeDesc("取消订单返还信用金");
                empCreditMoneyChangeLog.setOperatorId(userId);
                empCreditMoneyChangeLog.setOperatorType(AppIdentityType.SELLER);
                //保存日志
                appEmployeeService.addEmpCreditMoneyChangeLog(empCreditMoneyChangeLog);
            }
        }
        if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.DECORATE_MANAGER)) {
            //返回门店预存款
            if (orderBillingDetails.getStPreDeposit() != null && orderBillingDetails.getStPreDeposit() > 0) {
                //获取门店预存款
                StorePreDeposit storePreDeposit = storePreDepositLogService.findStoreByUserId(userId);
                //返还预存款后门店预存款金额
                Double stPreDeposit = (storePreDeposit.getBalance() + orderBillingDetails.getStPreDeposit());
                //修改门店预存款
                storePreDepositLogService.updateStPreDepositByUserId(stPreDeposit, userId);
                //记录门店预存款变更日志
                StPreDepositLogDO stPreDepositLogDO = new StPreDepositLogDO();
                stPreDepositLogDO.setCreateTime(TimeTransformUtils.UDateToLocalDateTime(date));
                stPreDepositLogDO.setChangeMoney(orderBillingDetails.getStPreDeposit());
                stPreDepositLogDO.setRemarks("取消订单返还门店预存款");
                stPreDepositLogDO.setOrderNumber(orderNumber);
                stPreDepositLogDO.setChangeType(StorePreDepositChangeType.CANCEL_ORDER);
                stPreDepositLogDO.setStoreId(storePreDeposit.getStoreId());
                stPreDepositLogDO.setOperatorId(userId);
                stPreDepositLogDO.setOperatorType(AppIdentityType.DECORATE_MANAGER);
                stPreDepositLogDO.setBalance(stPreDeposit);
                stPreDepositLogDO.setDetailReason("取消订单");
                stPreDepositLogDO.setTransferTime(TimeTransformUtils.UDateToLocalDateTime(date));
                //保存日志
                storePreDepositLogService.save(stPreDepositLogDO);
            }
            //返回门店信用金（装饰公司）
            if (orderBillingDetails.getStoreCreditMoney() != null && orderBillingDetails.getStoreCreditMoney() > 0) {
                //查询门店信用金
                StoreCreditMoney storeCreditMoney = storeCreditMoneyLogService.findStoreCreditMoneyByUserId(userId);
                //返还后门店信用金额度
                Double creditMoney = (storeCreditMoney.getCreditLimitAvailable() + orderBillingDetails.getStoreCreditMoney());
                //修改门店可用信用金
                appStoreService.unlockStoreCreditByUserIdAndCredit(userId, orderBillingDetails.getStoreCreditMoney());
                //记录门店信用金变更日志
                StoreCreditMoneyChangeLog storeCreditMoneyChangeLog = new StoreCreditMoneyChangeLog();
                storeCreditMoneyChangeLog.setStoreId(storeCreditMoney.getStoreId());
                storeCreditMoneyChangeLog.setCreateTime(date);
                storeCreditMoneyChangeLog.setChangeAmount(orderBillingDetails.getStoreCreditMoney());
                storeCreditMoneyChangeLog.setCreditLimitAvailableAfterChange(creditMoney);
                storeCreditMoneyChangeLog.setReferenceNumber(orderNumber);
                storeCreditMoneyChangeLog.setChangeType(StoreCreditMoneyChangeType.CANCEL_ORDER);
                storeCreditMoneyChangeLog.setChangeTypeDesc("取消订单返还门店信用金");
                storeCreditMoneyChangeLog.setOperatorId(userId);
                storeCreditMoneyChangeLog.setOperatorType(AppIdentityType.DECORATE_MANAGER);
                storeCreditMoneyChangeLog.setRemark("取消订单");
                //保存日志
                appStoreService.addStoreCreditMoneyChangeLog(storeCreditMoneyChangeLog);
            }
            //返回门店现金返利（装饰公司）
            if (orderBillingDetails.getStoreSubvention() != null && orderBillingDetails.getStoreSubvention() > 0) {
                //获取门店现金返利
                StoreSubvention storeSubvention = appStoreService.findStoreSubventionByEmpId(userId);
                //返还后门店现金返利余额
                Double subvention = (storeSubvention.getBalance() + orderBillingDetails.getStoreSubvention());
                //修改门店现金返利
                appStoreService.unlockStoreSubventionByUserIdAndSubvention(userId, orderBillingDetails.getStoreSubvention());
                //记录门店现金返利变更日志
                StoreSubventionChangeLog storeSubventionChangeLog = new StoreSubventionChangeLog();
                storeSubventionChangeLog.setStoreId(storeSubvention.getStoreId());
                storeSubventionChangeLog.setCreateTime(date);
                storeSubventionChangeLog.setChangeAmount(orderBillingDetails.getStoreSubvention());
                storeSubventionChangeLog.setBalance(subvention);
                storeSubventionChangeLog.setReferenceNumber(orderNumber);
                storeSubventionChangeLog.setChangeType(StoreSubventionChangeType.CANCEL_ORDER);
                storeSubventionChangeLog.setChangeTypeDesc("取消订单返还门店现金返利");
                storeSubventionChangeLog.setOperatorId(userId);
                storeSubventionChangeLog.setOperatorType(AppIdentityType.DECORATE_MANAGER);
                storeSubventionChangeLog.setRemark("取消订单");
                //保存日志
                appStoreService.addStoreSubventionChangeLog(storeSubventionChangeLog);
            }
        }
        //*******************************退券*********************************
        //获取订单使用产品券
        List<OrderCouponInfo> orderProductCouponList = productCouponService.findOrderCouponByCouponTypeAndUserId(userId, OrderCouponType.PRODUCT_COUPON);
        if (orderProductCouponList != null && orderProductCouponList.size() > 0) {
            for (OrderCouponInfo orderProductCoupon : orderProductCouponList) {
                //查询使用产品券信息
                CustomerProductCoupon customerProductCoupon = productCouponService.findCusProductCouponByCouponId(orderProductCoupon.getCouponId());
                //创建新的产品券
                CustomerProductCoupon newCusProductCoupon = new CustomerProductCoupon();
                newCusProductCoupon.setCustomerId(customerProductCoupon.getCustomerId());
                newCusProductCoupon.setGoodsId(customerProductCoupon.getGoodsId());
                newCusProductCoupon.setQuantity(customerProductCoupon.getQuantity());
                newCusProductCoupon.setGetType(ProductCouponGetType.CANCEL_ORDER);
                newCusProductCoupon.setGetTime(date);
                newCusProductCoupon.setEffectiveStartTime(customerProductCoupon.getEffectiveStartTime());
                newCusProductCoupon.setEffectiveEndTime(customerProductCoupon.getEffectiveEndTime());
                newCusProductCoupon.setIsUsed(false);
                newCusProductCoupon.setGetOrderNumber(customerProductCoupon.getGetOrderNumber());
                newCusProductCoupon.setBuyPrice(customerProductCoupon.getBuyPrice());
                newCusProductCoupon.setStoreId(customerProductCoupon.getStoreId());
                newCusProductCoupon.setSellerId(customerProductCoupon.getSellerId());
                productCouponService.addCustomerProductCoupon(newCusProductCoupon);
                //TODO   增加日志
            }
        }
        //获取订单使用现金券
        List<OrderCouponInfo> orderCashCouponList = productCouponService.findOrderCouponByCouponTypeAndUserId(userId, OrderCouponType.CASH_COUPON);
        if (orderCashCouponList != null && orderCashCouponList.size() > 0) {
            for (OrderCouponInfo orderCashCoupon : orderCashCouponList) {
                //查询现金券原信息
                CashCoupon cashCoupon = cashCouponService.findCashCouponByOrderNumber(orderCashCoupon.getCouponId());
                if (null != cashCoupon) {
                    //添加新的现金券
                    CashCoupon newCashCoupon = new CashCoupon();
                    newCashCoupon.setCreateTime(date);
                    newCashCoupon.setDenomination(cashCoupon.getDenomination());
                    newCashCoupon.setEffectiveStartTime(cashCoupon.getEffectiveStartTime());
                    newCashCoupon.setEffectiveEndTime(cashCoupon.getEffectiveEndTime());
                    newCashCoupon.setInitialQuantity(1);
                    newCashCoupon.setRemainingQuantity(1);
                    newCashCoupon.setDescription(cashCoupon.getDescription());
                    newCashCoupon.setTitle(cashCoupon.getTitle());
                    newCashCoupon.setCondition(cashCoupon.getCondition());
                    //保存新现金优惠券
                    cashCouponService.addCashCoupon(newCashCoupon);
                    //获取现金券id
                    Long couponId = newCashCoupon.getId();
                    //给顾客返回现金券
                    CustomerCashCoupon newCustomerCashCoupon = new CustomerCashCoupon();
                    if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.CUSTOMER)) {
                        newCustomerCashCoupon.setCusId(userId);
                    } else if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.SELLER)) {
                        newCustomerCashCoupon.setCusId(orderBaseInfo.getCustomerId());
                    }
                    newCustomerCashCoupon.setCcid(couponId);
                    newCustomerCashCoupon.setQty(1);
                    newCustomerCashCoupon.setIsUsed(false);
                    newCustomerCashCoupon.setGetTime(date);
                    newCustomerCashCoupon.setCondition(cashCoupon.getCondition());
                    newCustomerCashCoupon.setDenomination(cashCoupon.getDenomination());
                    newCustomerCashCoupon.setEffectiveStartTime(cashCoupon.getEffectiveStartTime());
                    newCustomerCashCoupon.setEffectiveEndTime(cashCoupon.getEffectiveEndTime());
                    newCustomerCashCoupon.setDescription(cashCoupon.getDescription());
                    newCustomerCashCoupon.setTitle(cashCoupon.getTitle());
                    newCustomerCashCoupon.setStatus(true);
                    //保存
                    cashCouponService.addCustomerCashCoupon(newCustomerCashCoupon);

                    //记录现金券变更日志
                    CustomerCashCouponChangeLog customerCashCouponChangeLog = new CustomerCashCouponChangeLog();
                    if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.CUSTOMER)) {
                        customerCashCouponChangeLog.setCusId(userId);
                    } else if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.SELLER)) {
                        customerCashCouponChangeLog.setCusId(orderBaseInfo.getCustomerId());
                    }
                    customerCashCouponChangeLog.setUseTime(date);
                    customerCashCouponChangeLog.setCouponId(orderCashCoupon.getCouponId());
                    customerCashCouponChangeLog.setReferenceNumber(orderNumber);
                    customerCashCouponChangeLog.setChangeType(CustomerCashCouponChangeType.CANCEL_ORDER);
                    customerCashCouponChangeLog.setChangeTypeDesc("取消订单返还");
                    customerCashCouponChangeLog.setOperatorId(userId);
                    customerCashCouponChangeLog.setOperatorType(AppIdentityType.getAppIdentityTypeByValue(identityType));
                    customerCashCouponChangeLog.setRemark("取消订单");
                    //保存日志
                    appCustomerService.addCustomerCashCouponChangeLog(customerCashCouponChangeLog);

                }
            }
        }
    }

    @Override
    public void refusedOrder(List<OrderGoodsInfo> orderGoodsInfoList, Long userId, Long returnOrderId, String returnNumber, String orderNumber,
                             OrderBillingDetails orderBillingDetails, OrderBaseInfo orderBaseInfo) {
        Date date = new Date();
        //创建退货商品实体类
        ReturnOrderGoodsInfo returnGoodsInfo = new ReturnOrderGoodsInfo();
        for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
            //记录退单商品
            returnGoodsInfo.setRoid(returnOrderId);
            returnGoodsInfo.setReturnNo(returnNumber);
            returnGoodsInfo.setSku(orderGoodsInfo.getSku());
            returnGoodsInfo.setSkuName(orderGoodsInfo.getSkuName());
            returnGoodsInfo.setRetailPrice(orderGoodsInfo.getRetailPrice());
            returnGoodsInfo.setVipPrice(orderGoodsInfo.getVIPPrice());
            returnGoodsInfo.setWholesalePrice(orderGoodsInfo.getWholesalePrice());
            returnGoodsInfo.setReturnPrice(orderGoodsInfo.getReturnPrice());
            returnGoodsInfo.setReturnQty(orderGoodsInfo.getOrderQuantity());
            //保存退单商品信息
            returnOrderService.saveReturnOrderGoodsInfo(returnGoodsInfo);
        }
        //创建退单退款总记录实体
        ReturnOrderBilling returnOrderBilling = new ReturnOrderBilling();
        returnOrderBilling.setRoid(returnOrderId);
        returnOrderBilling.setReturnNo(returnNumber);
        returnOrderBilling.setPreDeposit(orderBillingDetails.getCusPreDeposit() == null ? 0.00 : orderBillingDetails.getCusPreDeposit());
        returnOrderBilling.setCreditMoney(orderBillingDetails.getEmpCreditMoney() == null ? 0.00 : orderBillingDetails.getEmpCreditMoney());
        returnOrderBilling.setStPreDeposit(orderBillingDetails.getStPreDeposit() == null ? 0.00 : orderBillingDetails.getStPreDeposit());
        returnOrderBilling.setStCreditMoney(orderBillingDetails.getStoreCreditMoney() == null ? 0.00 : orderBillingDetails.getStoreCreditMoney());
        returnOrderBilling.setStSubvention(orderBillingDetails.getStoreSubvention() == null ? 0.00 : orderBillingDetails.getStoreSubvention());
        returnOrderBilling.setOnlinePay(orderBillingDetails.getOnlinePayAmount() == null ? 0.00 : orderBillingDetails.getOnlinePayAmount());
        returnOrderBilling.setCash(0.00);
        //添加保存退单退款总记录
        returnOrderService.saveReturnOrderBilling(returnOrderBilling);

        //********************************返还虚拟货币********************************
        if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.CUSTOMER)) {
            //返回乐币
            if (orderBillingDetails.getLebiQuantity() != null && orderBillingDetails.getLebiQuantity() > 0) {
                //获取顾客当前乐币数量
                CustomerLeBi customerLeBi = appCustomerService.findCustomerLebiByCustomerId(orderBaseInfo.getCreatorId());
                //返还乐币后顾客乐币数量
                Integer lebiTotal = (customerLeBi.getQuantity() + orderBillingDetails.getLebiQuantity());
                //更改顾客乐币数量
                leBiVariationLogService.updateLeBiQtyByUserId(lebiTotal, date, orderBaseInfo.getCreatorId());
                //记录乐币日志
                CustomerLeBiVariationLog leBiVariationLog = new CustomerLeBiVariationLog();
                leBiVariationLog.setCusId(orderBaseInfo.getCreatorId());
                leBiVariationLog.setVariationQuantity(orderBillingDetails.getLebiQuantity());
                leBiVariationLog.setAfterVariationQuantity(lebiTotal);
                leBiVariationLog.setVariationTime(date);
                leBiVariationLog.setLeBiVariationType(LeBiVariationType.RETURN_ORDER);
                leBiVariationLog.setVariationTypeDesc("拒签退货");
                leBiVariationLog.setOrderNum(orderNumber);
                //保存日志
                leBiVariationLogService.addCustomerLeBiVariationLog(leBiVariationLog);
            }
            //返回顾客预存款
            if (orderBillingDetails.getCusPreDeposit() != null && orderBillingDetails.getCusPreDeposit() > 0) {
                //获取顾客预存款
                CustomerPreDeposit customerPreDeposit = appCustomerService.findByCusId(orderBaseInfo.getCreatorId());
                //返还预存款后顾客预存款金额
                Double cusPreDeposit = (customerPreDeposit.getBalance() + orderBillingDetails.getCusPreDeposit());
                //更改顾客预存款金额
                appCustomerService.unlockCustomerDepositByUserIdAndDeposit(orderBaseInfo.getCreatorId(), orderBillingDetails.getCusPreDeposit());
                //记录预存款日志
                CusPreDepositLogDO cusPreDepositLogDO = new CusPreDepositLogDO();
                cusPreDepositLogDO.setCreateTime(TimeTransformUtils.UDateToLocalDateTime(date));
                cusPreDepositLogDO.setChangeMoney(orderBillingDetails.getCusPreDeposit());
                cusPreDepositLogDO.setOrderNumber(orderNumber);
                cusPreDepositLogDO.setChangeType(CustomerPreDepositChangeType.RETURN_ORDER);
                cusPreDepositLogDO.setChangeTypeDesc("拒签退货返还");
                cusPreDepositLogDO.setCusId(orderBaseInfo.getCreatorId());
                cusPreDepositLogDO.setOperatorId(userId);
                cusPreDepositLogDO.setOperatorType(AppIdentityType.DELIVERY_CLERK);
                cusPreDepositLogDO.setBalance(cusPreDeposit);
                cusPreDepositLogDO.setDetailReason("拒签退货");
                cusPreDepositLogDO.setTransferTime(TimeTransformUtils.UDateToLocalDateTime(date));
                cusPreDepositLogDO.setMerchantOrderNumber(null);
                //保存日志
                appCustomerService.addCusPreDepositLog(cusPreDepositLogDO);
            }
        }
        if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.SELLER)) {
            //返回门店预存款
            if (orderBillingDetails.getStPreDeposit() != null && orderBillingDetails.getStPreDeposit() > 0) {
                //获取门店预存款
                StorePreDeposit storePreDeposit = storePreDepositLogService.findStoreByUserId(orderBaseInfo.getSalesConsultId());
                //返还预存款后门店预存款金额
                Double stPreDeposit = (storePreDeposit.getBalance() + orderBillingDetails.getStPreDeposit());
                //修改门店预存款
                storePreDepositLogService.updateStPreDepositByUserId(stPreDeposit, orderBaseInfo.getSalesConsultId());
                //记录门店预存款变更日志
                StPreDepositLogDO stPreDepositLogDO = new StPreDepositLogDO();
                stPreDepositLogDO.setCreateTime(TimeTransformUtils.UDateToLocalDateTime(date));
                stPreDepositLogDO.setChangeMoney(orderBillingDetails.getStPreDeposit());
                stPreDepositLogDO.setRemarks("拒签退货返还门店预存款");
                stPreDepositLogDO.setOrderNumber(orderNumber);
                stPreDepositLogDO.setChangeType(StorePreDepositChangeType.RETURN_ORDER);
                stPreDepositLogDO.setStoreId(storePreDeposit.getStoreId());
                stPreDepositLogDO.setOperatorId(userId);
                stPreDepositLogDO.setOperatorType(AppIdentityType.DELIVERY_CLERK);
                stPreDepositLogDO.setBalance(stPreDeposit);
                stPreDepositLogDO.setDetailReason("拒签退货");
                stPreDepositLogDO.setTransferTime(TimeTransformUtils.UDateToLocalDateTime(date));
                //保存日志
                storePreDepositLogService.save(stPreDepositLogDO);
            }
            //返回导购信用额度
            if (orderBillingDetails.getEmpCreditMoney() != null && orderBillingDetails.getEmpCreditMoney() > 0) {
                //获取导购信用金
                EmpCreditMoney empCreditMoney = appEmployeeService.findEmpCreditMoneyByEmpId(orderBaseInfo.getSalesConsultId());
                //返还信用金后导购信用金额度
                Double creditMoney = (empCreditMoney.getCreditLimitAvailable() + orderBillingDetails.getEmpCreditMoney());
                //修改导购信用额度
                appEmployeeService.unlockGuideCreditByUserIdAndCredit(orderBaseInfo.getSalesConsultId(), orderBillingDetails.getEmpCreditMoney());
                //记录导购信用金变更日志
                EmpCreditMoneyChangeLog empCreditMoneyChangeLog = new EmpCreditMoneyChangeLog();
                empCreditMoneyChangeLog.setEmpId(orderBaseInfo.getSalesConsultId());
                empCreditMoneyChangeLog.setCreateTime(date);
                empCreditMoneyChangeLog.setCreditLimitAvailableChangeAmount(orderBillingDetails.getEmpCreditMoney());
                empCreditMoneyChangeLog.setCreditLimitAvailableAfterChange(creditMoney);
                empCreditMoneyChangeLog.setReferenceNumber(orderNumber);
                empCreditMoneyChangeLog.setChangeType(EmpCreditMoneyChangeType.RETURN_ORDER);
                empCreditMoneyChangeLog.setChangeTypeDesc("拒签退货返还信用金");
                empCreditMoneyChangeLog.setOperatorId(userId);
                empCreditMoneyChangeLog.setOperatorType(AppIdentityType.DELIVERY_CLERK);
                //保存日志
                appEmployeeService.addEmpCreditMoneyChangeLog(empCreditMoneyChangeLog);
            }
        }
        if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.DECORATE_MANAGER)) {
            //返回门店预存款
            if (orderBillingDetails.getStPreDeposit() != null && orderBillingDetails.getStPreDeposit() > 0) {
                //获取门店预存款
                StorePreDeposit storePreDeposit = storePreDepositLogService.findStoreByUserId(orderBaseInfo.getCreatorId());
                //返还预存款后门店预存款金额
                Double stPreDeposit = (storePreDeposit.getBalance() + orderBillingDetails.getStPreDeposit());
                //修改门店预存款
                storePreDepositLogService.updateStPreDepositByUserId(stPreDeposit, orderBaseInfo.getCreatorId());
                //记录门店预存款变更日志
                StPreDepositLogDO stPreDepositLogDO = new StPreDepositLogDO();
                stPreDepositLogDO.setCreateTime(TimeTransformUtils.UDateToLocalDateTime(date));
                stPreDepositLogDO.setChangeMoney(orderBillingDetails.getStPreDeposit());
                stPreDepositLogDO.setRemarks("拒签退货返还门店预存款");
                stPreDepositLogDO.setOrderNumber(orderNumber);
                stPreDepositLogDO.setChangeType(StorePreDepositChangeType.RETURN_ORDER);
                stPreDepositLogDO.setStoreId(storePreDeposit.getStoreId());
                stPreDepositLogDO.setOperatorId(userId);
                stPreDepositLogDO.setOperatorType(AppIdentityType.DELIVERY_CLERK);
                stPreDepositLogDO.setBalance(stPreDeposit);
                stPreDepositLogDO.setDetailReason("取消订单");
                stPreDepositLogDO.setTransferTime(TimeTransformUtils.UDateToLocalDateTime(date));
                //保存日志
                storePreDepositLogService.save(stPreDepositLogDO);
            }
            //返回门店信用金（装饰公司）
            if (orderBillingDetails.getStoreCreditMoney() != null && orderBillingDetails.getStoreCreditMoney() > 0) {
                //查询门店信用金
                StoreCreditMoney storeCreditMoney = storeCreditMoneyLogService.findStoreCreditMoneyByUserId(orderBaseInfo.getCreatorId());
                //返还后门店信用金额度
                Double creditMoney = (storeCreditMoney.getCreditLimitAvailable() + orderBillingDetails.getStoreCreditMoney());
                //修改门店可用信用金
                appStoreService.unlockStoreCreditByUserIdAndCredit(orderBaseInfo.getCreatorId(), orderBillingDetails.getStoreCreditMoney());
                //记录门店信用金变更日志
                StoreCreditMoneyChangeLog storeCreditMoneyChangeLog = new StoreCreditMoneyChangeLog();
                storeCreditMoneyChangeLog.setStoreId(storeCreditMoney.getStoreId());
                storeCreditMoneyChangeLog.setCreateTime(date);
                storeCreditMoneyChangeLog.setChangeAmount(orderBillingDetails.getStoreCreditMoney());
                storeCreditMoneyChangeLog.setCreditLimitAvailableAfterChange(creditMoney);
                storeCreditMoneyChangeLog.setReferenceNumber(orderNumber);
                storeCreditMoneyChangeLog.setChangeType(StoreCreditMoneyChangeType.RETURN_ORDER);
                storeCreditMoneyChangeLog.setChangeTypeDesc("拒签退货返还门店信用金");
                storeCreditMoneyChangeLog.setOperatorId(userId);
                storeCreditMoneyChangeLog.setOperatorType(AppIdentityType.DELIVERY_CLERK);
                storeCreditMoneyChangeLog.setRemark("拒签退货");
                //保存日志
                appStoreService.addStoreCreditMoneyChangeLog(storeCreditMoneyChangeLog);
            }
            //返回门店现金返利（装饰公司）
            if (orderBillingDetails.getStoreSubvention() != null && orderBillingDetails.getStoreSubvention() > 0) {
                //获取门店现金返利
                StoreSubvention storeSubvention = appStoreService.findStoreSubventionByEmpId(orderBaseInfo.getCreatorId());
                //返还后门店现金返利余额
                Double subvention = (storeSubvention.getBalance() + orderBillingDetails.getStoreSubvention());
                //修改门店现金返利
                appStoreService.unlockStoreSubventionByUserIdAndSubvention(orderBaseInfo.getCreatorId(), orderBillingDetails.getStoreSubvention());
                //记录门店现金返利变更日志
                StoreSubventionChangeLog storeSubventionChangeLog = new StoreSubventionChangeLog();
                storeSubventionChangeLog.setStoreId(storeSubvention.getStoreId());
                storeSubventionChangeLog.setCreateTime(date);
                storeSubventionChangeLog.setChangeAmount(orderBillingDetails.getStoreSubvention());
                storeSubventionChangeLog.setBalance(subvention);
                storeSubventionChangeLog.setReferenceNumber(orderNumber);
                storeSubventionChangeLog.setChangeType(StoreSubventionChangeType.RETURN_ORDER);
                storeSubventionChangeLog.setChangeTypeDesc("拒签退货返还门店现金返利");
                storeSubventionChangeLog.setOperatorId(userId);
                storeSubventionChangeLog.setOperatorType(AppIdentityType.DELIVERY_CLERK);
                storeSubventionChangeLog.setRemark("拒签退货");
                //保存日志
                appStoreService.addStoreSubventionChangeLog(storeSubventionChangeLog);
            }
        }
        //*******************************退券*********************************
        //获取订单使用产品券
        List<OrderCouponInfo> orderProductCouponList = productCouponService.findOrderCouponByCouponTypeAndUserId(userId, OrderCouponType.PRODUCT_COUPON);
        if (orderProductCouponList != null && orderProductCouponList.size() > 0) {
            for (OrderCouponInfo orderProductCoupon : orderProductCouponList) {
                //查询使用产品券信息
                CustomerProductCoupon customerProductCoupon = productCouponService.findCusProductCouponByCouponId(orderProductCoupon.getCouponId());
                //创建新的产品券
                CustomerProductCoupon newCusProductCoupon = new CustomerProductCoupon();
                newCusProductCoupon.setCustomerId(customerProductCoupon.getCustomerId());
                newCusProductCoupon.setGoodsId(customerProductCoupon.getGoodsId());
                newCusProductCoupon.setQuantity(customerProductCoupon.getQuantity());
                newCusProductCoupon.setGetType(ProductCouponGetType.RETURN_ORDER);
                newCusProductCoupon.setGetTime(date);
                newCusProductCoupon.setEffectiveStartTime(customerProductCoupon.getEffectiveStartTime());
                newCusProductCoupon.setEffectiveEndTime(customerProductCoupon.getEffectiveEndTime());
                newCusProductCoupon.setIsUsed(false);
                newCusProductCoupon.setGetOrderNumber(customerProductCoupon.getGetOrderNumber());
                newCusProductCoupon.setBuyPrice(customerProductCoupon.getBuyPrice());
                newCusProductCoupon.setStoreId(customerProductCoupon.getStoreId());
                newCusProductCoupon.setSellerId(customerProductCoupon.getSellerId());
                productCouponService.addCustomerProductCoupon(newCusProductCoupon);
                //TODO   增加日志
            }
        }
        //获取订单使用现金券
        List<OrderCouponInfo> orderCashCouponList = productCouponService.findOrderCouponByCouponTypeAndUserId(orderBaseInfo.getCreatorId(), OrderCouponType.CASH_COUPON);
        if (orderCashCouponList != null && orderCashCouponList.size() > 0) {
            for (OrderCouponInfo orderCashCoupon : orderCashCouponList) {
                //查询现金券原信息
                CashCoupon cashCoupon = cashCouponService.findCashCouponByOrderNumber(orderCashCoupon.getCouponId());
                if (null != cashCoupon) {
                    //添加新的现金券
                    CashCoupon newCashCoupon = new CashCoupon();
                    newCashCoupon.setCreateTime(date);
                    newCashCoupon.setDenomination(cashCoupon.getDenomination());
                    newCashCoupon.setEffectiveStartTime(cashCoupon.getEffectiveStartTime());
                    newCashCoupon.setEffectiveEndTime(cashCoupon.getEffectiveEndTime());
                    newCashCoupon.setInitialQuantity(1);
                    newCashCoupon.setRemainingQuantity(1);
                    newCashCoupon.setDescription(cashCoupon.getDescription());
                    newCashCoupon.setTitle(cashCoupon.getTitle());
                    newCashCoupon.setCondition(cashCoupon.getCondition());
                    //保存新现金优惠券
                    cashCouponService.addCashCoupon(newCashCoupon);
                    //获取现金券id
                    Long couponId = newCashCoupon.getId();
                    //给顾客返回现金券
                    CustomerCashCoupon newCustomerCashCoupon = new CustomerCashCoupon();
                    if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.CUSTOMER)) {
                        newCustomerCashCoupon.setCusId(orderBaseInfo.getCreatorId());
                    } else if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.SELLER)) {
                        newCustomerCashCoupon.setCusId(orderBaseInfo.getCustomerId());
                    }
                    newCustomerCashCoupon.setCcid(couponId);
                    newCustomerCashCoupon.setQty(1);
                    newCustomerCashCoupon.setIsUsed(false);
                    newCustomerCashCoupon.setGetTime(date);
                    newCustomerCashCoupon.setCondition(cashCoupon.getCondition());
                    newCustomerCashCoupon.setDenomination(cashCoupon.getDenomination());
                    newCustomerCashCoupon.setEffectiveStartTime(cashCoupon.getEffectiveStartTime());
                    newCustomerCashCoupon.setEffectiveEndTime(cashCoupon.getEffectiveEndTime());
                    newCustomerCashCoupon.setDescription(cashCoupon.getDescription());
                    newCustomerCashCoupon.setTitle(cashCoupon.getTitle());
                    newCustomerCashCoupon.setStatus(true);
                    //保存
                    cashCouponService.addCustomerCashCoupon(newCustomerCashCoupon);

                    //记录现金券变更日志
                    CustomerCashCouponChangeLog customerCashCouponChangeLog = new CustomerCashCouponChangeLog();
                    if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.CUSTOMER)) {
                        customerCashCouponChangeLog.setCusId(orderBaseInfo.getCreatorId());
                    } else if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.SELLER)) {
                        customerCashCouponChangeLog.setCusId(orderBaseInfo.getCustomerId());
                    }
                    customerCashCouponChangeLog.setUseTime(date);
                    customerCashCouponChangeLog.setCouponId(orderCashCoupon.getCouponId());
                    customerCashCouponChangeLog.setReferenceNumber(orderNumber);
                    customerCashCouponChangeLog.setChangeType(CustomerCashCouponChangeType.CANCEL_ORDER);
                    customerCashCouponChangeLog.setChangeTypeDesc("拒签退单返还");
                    customerCashCouponChangeLog.setOperatorId(userId);
                    customerCashCouponChangeLog.setOperatorType(AppIdentityType.DELIVERY_CLERK);
                    customerCashCouponChangeLog.setRemark("拒签退单");
                    //保存日志
                    appCustomerService.addCustomerCashCouponChangeLog(customerCashCouponChangeLog);

                }
            }
        }
    }

    @Override
    public void saveReturnOrderLogisticsInfo(ReturnOrderLogisticInfo returnOrderLogisticInfo) {
        returnOrderDAO.saveReturnOrderLogisticsInfo(returnOrderLogisticInfo);
    }

    @Override
    public void modifyReturnOrderBillingDetail(ReturnOrderBillingDetail orderReturnBillingDetail) {
        if (orderReturnBillingDetail != null) {
            returnOrderDAO.modifyReturnOrderBillingDetail(orderReturnBillingDetail);
        }
    }

    @Override
    public List<ReturnOrderBaseInfo> findReturnOrderListByUserIdAndIdentityType(Long userId, Integer identityType, Integer showStatus) {
        if (userId != null && identityType != null && showStatus != null) {
            return returnOrderDAO.findReturnOrderListByUserIdAndIdentityType(userId,
                    AppIdentityType.getAppIdentityTypeByValue(identityType), showStatus);
        }
        return null;
    }

    @Override
    public List<ReturnOrderGoodsInfo> findReturnOrderGoodsInfoByOrderNumber(String returnNo) {
        if (returnNo != null) {
            return returnOrderDAO.findReturnOrderGoodsInfoByOrderNumber(returnNo);
        }
        return null;
    }

    @Override
    public List<GiftListResponseGoods> getReturnOrderGoodsDetails(String returnNumber) {
        if (StringUtils.isNotBlank(returnNumber)) {
            return returnOrderDAO.getReturnOrderGoodsDetails(returnNumber);
        }
        return null;
    }

}
