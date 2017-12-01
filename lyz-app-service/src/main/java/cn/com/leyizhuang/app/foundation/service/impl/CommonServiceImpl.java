package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.exception.*;
import cn.com.leyizhuang.app.core.utils.BeanUtils;
import cn.com.leyizhuang.app.core.utils.csrf.EncryptUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.CustomerCashCoupon;
import cn.com.leyizhuang.app.foundation.pojo.CustomerLeBiVariationLog;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.management.User;
import cn.com.leyizhuang.app.foundation.pojo.management.UserRole;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderLogisticsInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.BillingSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.DeliverySimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 通用方法实现
 *
 * @author Richard
 * Created on 2017-09-12 15:44
 **/
@Service
public class CommonServiceImpl implements CommonService {


    @Resource
    private UserRoleService userRoleService;

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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveUserAndUserRoleByUserVO(UserVO userVO) {
        User user = BeanUtils.copy(userVO, User.class);
        user.setCreateTime(new Date());
        Map<String, String> paramMap = EncryptUtils.getPasswordAndSalt(userVO.getLoginName(), userVO.getPassword());
        user.setSalt(paramMap.get("salt"));
        user.setPassword(paramMap.get("encodedPassword"));
        userService.save(user);
        Long id = user.getUid();
        Long[] roles = userVO.getRoleIds();
        if (null != roles && roles.length > 0) {
            UserRole userRole = new UserRole();
            for (Long role : roles) {
                userRole.setUserId(id);
                userRole.setRoleId(role);
                userRoleService.save(userRole);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateUserAndUserRoleByUserVO(UserVO userVO) {
        if (null != userVO) {
            User user = userVO.convert2User();
            if (null != user.getPassword() && !"".equalsIgnoreCase(user.getPassword())) {
                Map<String, String> paramMap = EncryptUtils.getPasswordAndSalt(user.getLoginName(), user.getPassword());
                user.setSalt(paramMap.get("salt"));
                user.setPassword(paramMap.get("encodedPassword"));
            }
            userService.update(user);
            userRoleService.deleteUserRoleByUserId(userVO.getId());
            Long[] roles = userVO.getRoleIds();
            if (null != roles && roles.length > 0) {
                UserRole userRole = new UserRole();
                for (Long role : roles) {
                    userRole.setUserId(userVO.getId());
                    userRole.setRoleId(role);
                    userRoleService.save(userRole);
                }
            }
        }
    }

    @Transactional
    @Override
    public void deleteUserAndUserRoleByUserId(Long uid) {
        if (null != uid) {
            userRoleService.deleteUserRoleByUserId(uid);
            this.userService.delete(uid);
        }
    }

    @Transactional
    @Override
    public AppCustomer saveCustomerInfo(AppCustomer customer, CustomerLeBi leBi, CustomerPreDeposit preDeposit) {
        customerService.save(customer);
        leBi.setCusId(customer.getCusId());
        leBi.setQuantity(leBi.getQuantity() == null ? 0 : leBi.getQuantity());
        customerService.saveLeBi(leBi);
        preDeposit.setCusId(customer.getCusId());
        preDeposit.setBalance(preDeposit.getBalance() == null ? 0 : preDeposit.getBalance());
        customerService.savePreDeposit(preDeposit);
        return customer;
    }

    @Transactional
    @Override
    public void updateCustomerSignTimeAndCustomerLeBiByUserId(Long userId, Integer identityType) {
        if (null != userId) {
            customerService.addLeBiQuantityByUserIdAndIdentityType(userId, identityType);
            customerService.updateLastSignTimeByCustomerId(userId, new Date());
        }
    }

    @Transactional
    @Override
    public void saveAndUpdateMaterialList(List<MaterialListDO> materialListSave, List<MaterialListDO> materialListUpdate) {
        if ((null != materialListSave && materialListSave.size() > 0) || (null != materialListUpdate && materialListUpdate.size() > 0)) {
            materialListService.batchSave(materialListSave);
            for (MaterialListDO materialListDO : materialListUpdate) {
                materialListService.modifyQty(materialListDO.getId(), materialListDO.getQty());
            }
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
        //设置订单类型 买券、出货
        tempOrder.setOrderType(AppOrderType.SHIPMENT);
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
        }
        logisticsInfo.setIsOwnerReceiving(deliverySimpleInfo.getIsOwnerReceiving());
        return logisticsInfo;
    }

    @Override
    public OrderBillingDetails createOrderBillingDetails(OrderBillingDetails orderBillingDetails, Long userId, Integer identityType, BillingSimpleInfo billing, List<Long> cashCouponIds) {


        orderBillingDetails.setCollectionAmount(billing.getCollectionAmount());
        orderBillingDetails.setFreight(billing.getFreight());
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
        }
        //设置导购信用额度
        if (identityType == AppIdentityType.SELLER.getValue()) {
            Double empCreditMoney = 0D;
            if (null != billing.getEmpCreditMoney()) {
                empCreditMoney = billing.getEmpCreditMoney();
            }
            orderBillingDetails.setEmpCreditMoney(empCreditMoney);
        }
        //设置门店信用额度
        if (identityType == AppIdentityType.DECORATE_MANAGER.getValue() ||
                identityType == AppIdentityType.SELLER.getValue()) {
            Double stCreditMoney = 0D;
            if (null != billing.getStoreCreditMoney()) {
                stCreditMoney = billing.getStoreCreditMoney();
            }
            orderBillingDetails.setStoreCreditMoney(stCreditMoney);
        }
        //设置门店信用额度
        if (identityType == AppIdentityType.DECORATE_MANAGER.getValue()) {
            Double stSubvention = 0D;
            if (null != billing.getStoreSubvention()) {
                stSubvention = billing.getStoreSubvention();
            }
            orderBillingDetails.setStoreSubvention(stSubvention);
        }


        //计算订单金额小计以及应付款
        Double orderAmountSubtotal;
        Double amountPayable;
        switch (identityType) {
            case 6:
                orderAmountSubtotal = orderBillingDetails.getOrderAmount() + orderBillingDetails.getFreight()
                        - orderBillingDetails.getMemberDiscount() - orderBillingDetails.getPromotionDiscount()
                        - orderBillingDetails.getLebiCashDiscount() - orderBillingDetails.getCashCouponDiscount();
                orderBillingDetails.setOrderAmountSubtotal(orderAmountSubtotal);
                amountPayable = orderBillingDetails.getOrderAmountSubtotal() - orderBillingDetails.getCusPreDeposit();
                orderBillingDetails.setAmountPayable(amountPayable);
                break;
            case 0:
                orderAmountSubtotal = orderBillingDetails.getOrderAmount() + orderBillingDetails.getFreight()
                        - orderBillingDetails.getMemberDiscount() - orderBillingDetails.getPromotionDiscount()
                        - orderBillingDetails.getLebiCashDiscount() - orderBillingDetails.getCashCouponDiscount();
                orderBillingDetails.setOrderAmountSubtotal(orderAmountSubtotal);
                amountPayable = orderBillingDetails.getOrderAmountSubtotal() - orderBillingDetails.getStPreDeposit()
                        - orderBillingDetails.getEmpCreditMoney();
                orderBillingDetails.setAmountPayable(amountPayable);
                break;
            case 2:
                orderAmountSubtotal = orderBillingDetails.getOrderAmount() + orderBillingDetails.getFreight()
                        - orderBillingDetails.getMemberDiscount() - orderBillingDetails.getPromotionDiscount();
                orderBillingDetails.setOrderAmountSubtotal(orderAmountSubtotal);
                amountPayable = orderBillingDetails.getOrderAmountSubtotal() - orderBillingDetails.getStPreDeposit()
                        - orderBillingDetails.getStoreCreditMoney() - orderBillingDetails.getStoreSubvention();
                orderBillingDetails.setAmountPayable(amountPayable);
                break;
            default:
                break;
        }
        orderBillingDetails.setArrearage(orderBillingDetails.getAmountPayable());
        return orderBillingDetails;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reduceInventoryAndMoney(DeliverySimpleInfo deliverySimpleInfo, Map<Long, Integer> inventoryCheckMap, Long cityId,
                                        Integer identityType, Long userId, Long customerId, List<Long> cashCouponIds, OrderBillingDetails billingDetails,
                                        String orderNumber) {
        //扣减商品库存
        if (deliverySimpleInfo.getDeliveryType().equalsIgnoreCase(AppDeliveryType.SELF_TAKE.getValue())) {
            if (identityType.equals(AppIdentityType.CUSTOMER.getValue()) ||
                    identityType.equals(AppIdentityType.SELLER.getValue())) {
                for (Map.Entry<Long, Integer> entry : inventoryCheckMap.entrySet()) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        StoreInventory storeInventory = storeService.findStoreInventoryByStoreIdAndGoodsId(deliverySimpleInfo.getBookingStoreId(), entry.getKey());
                        if (null != storeInventory) {
                            if (storeInventory.getAvailableIty() < entry.getValue()) {
                                throw new LockStoreInventoryException("该门店下id为" + entry.getKey() + "的商品库存不足!");
                            }
                            Integer affectLine = storeService.lockStoreInventoryByStoreIdAndGoodsIdAndInventory(deliverySimpleInfo.getBookingStoreId(), entry.getKey(),
                                    entry.getValue(), storeInventory.getLastUpdateTime());
                            if (affectLine > 0) {
                                StoreInventoryAvailableQtyChangeLog log = new StoreInventoryAvailableQtyChangeLog();
                                log.setCityId(storeInventory.getCityId());
                                log.setCityName(storeInventory.getCityName());
                                log.setStoreId(storeInventory.getStoreId());
                                log.setStoreName(storeInventory.getStoreName());
                                log.setStoreCode(storeInventory.getStoreCode());
                                log.setGid(storeInventory.getGid());
                                log.setSku(storeInventory.getSku());
                                log.setSkuName(storeInventory.getSkuName());
                                log.setChangeQty(entry.getValue());
                                log.setAfterChangeQty(storeInventory.getAvailableIty() - entry.getValue());
                                log.setChangeTime(Calendar.getInstance().getTime());
                                log.setChangeType(StoreInventoryAvailableQtyChangeType.SELF_TAKE_ORDER);
                                log.setChangeTypeDesc(StoreInventoryAvailableQtyChangeType.SELF_TAKE_ORDER.getDescription());
                                log.setReferenceNumber(orderNumber);
                                storeService.addStoreInventoryAvailableQtyChangeLog(log);
                                break;
                            } else {
                                if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                    throw new SystemBusyException("系统繁忙，请稍后再试!");
                                }
                            }
                        } else {
                            throw new LockStoreInventoryException("该门店下没有找到id为" + entry.getKey() + "的商品库存信息!");
                        }
                    }
                }
            }
        } else if (deliverySimpleInfo.getDeliveryType().equalsIgnoreCase(AppDeliveryType.HOUSE_DELIVERY.getValue())) {
            for (Map.Entry<Long, Integer> entry : inventoryCheckMap.entrySet()) {
                for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                    CityInventory cityInventory = cityService.findCityInventoryByCityIdAndGoodsId(cityId, entry.getKey());
                    if (null != cityInventory) {
                        if (cityInventory.getAvailableIty() < entry.getValue()) {
                            throw new LockCityInventoryException("该城市下id为" + entry.getKey() + "的商品库存不足!");
                        }
                        Integer affectLine = cityService.lockCityInventoryByCityIdAndGoodsIdAndInventory(cityId, entry.getKey(), entry.getValue(), cityInventory.getLastUpdateTime());
                        if (affectLine > 0) {
                            CityInventoryAvailableQtyChangeLog log = new CityInventoryAvailableQtyChangeLog();
                            log.setCityId(cityInventory.getCityId());
                            log.setCityName(cityInventory.getCityName());
                            log.setGid(cityInventory.getGid());
                            log.setSku(cityInventory.getSku());
                            log.setSkuName(cityInventory.getSkuName());
                            log.setChangeQty(entry.getValue());
                            log.setAfterChangeQty(cityInventory.getAvailableIty() - entry.getValue());
                            log.setChangeTime(Calendar.getInstance().getTime());
                            log.setChangeType(CityInventoryAvailableQtyChangeType.HOUSE_DELIVERY_ORDER);
                            log.setReferenceNumber(orderNumber);
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    } else {
                        throw new LockCityInventoryException("该城市下没有找到id为" + entry.getKey() + "的商品库存信息!");
                    }

                }
            }
        }
        //扣减优惠券及乐币
        if (identityType.equals(AppIdentityType.CUSTOMER.getValue()) ||
                identityType.equals(AppIdentityType.SELLER.getValue())) {
            if (null != cashCouponIds && cashCouponIds.size() > 0) {
                for (Long id : cashCouponIds) {
                    Integer affectLine = customerService.lockCustomerCashCouponById(id, orderNumber);
                    if (affectLine == 0) {
                        throw new LockCustomerCashCouponException("该客户id为" + id + "的优惠券已使用或已失效!");
                    }
                }
            }
            if (billingDetails.getLebiQuantity() > 0) {
                Long customerIdTemp;
                if (identityType.equals(AppIdentityType.CUSTOMER.getValue())) {
                    customerId = userId;
                } else {
                    customerId = customerId;
                }
                for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                    CustomerLeBi customerLeBi = customerService.findCustomerLebiByCustomerId(customerId);
                    if (null != customerLeBi) {
                        if (customerLeBi.getQuantity() < billingDetails.getLebiQuantity()) {
                            throw new LockCustomerLebiException("该客户的乐币数量不足！");
                        }
                        Integer affectLine = customerService.lockCustomerLebiByUserIdAndQty(customerId,
                                billingDetails.getLebiQuantity(),customerLeBi.getLastUpdateTime());
                        if (affectLine > 0) {
                            CustomerLeBiVariationLog log = new CustomerLeBiVariationLog();
                            log.setCusID(customerId);
                            log.setVariationTime(Calendar.getInstance().getTime());
                            log.setVariationQuantity(billingDetails.getLebiQuantity());
                            log.setAfterVariationQuantity(customerLeBi.getQuantity()-billingDetails.getLebiQuantity());
                            log.setOrderNum(orderNumber);
                            log.setLeBiVariationType(LeBiVariationType.ORDER);
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    } else {
                        throw new LockCustomerLebiException("没有找到该客户的乐币账户!");
                    }

                }

            }
        }
        //扣减客户预存款
        if (identityType == AppIdentityType.CUSTOMER.getValue()) {
            Integer affectLine = customerService.lockCustomerDepositByUserIdAndDeposit(
                    userId, billingDetails.getCusPreDeposit());
            if (affectLine == 0) {
                throw new LockCustomerPreDepositException("该客户预存款余额不足!");
            }
        }
        //扣减门店预存款
        if (identityType == AppIdentityType.SELLER.getValue() ||
                (identityType == AppIdentityType.DECORATE_MANAGER.getValue())) {
            int affectLine = storeService.lockStoreDepositByUserIdAndStoreDeposit(
                    userId, billingDetails.getStPreDeposit());
            if (affectLine == 0) {
                throw new LockStorePreDepositException("用户所属门店预存款余额不足!");
            }

        }
        //扣减导购信用额度
        if (identityType == AppIdentityType.SELLER.getValue()) {
            int affectLine = employeeService.lockGuideCreditByUserIdAndCredit(
                    userId, billingDetails.getEmpCreditMoney());
            if (affectLine == 0) {
                throw new LockEmpCreditMoneyException("导购信用额度不足!");
            }
        }
        //扣减门店信用金及现金返利
        if (identityType == AppIdentityType.DECORATE_MANAGER.getValue()) {
            int affectLine = storeService.lockStoreCreditByUserIdAndCredit(
                    userId, billingDetails.getStoreCreditMoney());
            if (affectLine == 0) {
                throw new LockStoreCreditMoneyException("用户所属门店信用额度余额不足!");
            }
            int result = storeService.lockStoreSubventionByUserIdAndSubvention(
                    userId, billingDetails.getStoreSubvention());
            if (result == 0) {
                throw new LockStoreSubventionException("用户所属门店现金返利余额不足!");
            }
        }
    }
}

