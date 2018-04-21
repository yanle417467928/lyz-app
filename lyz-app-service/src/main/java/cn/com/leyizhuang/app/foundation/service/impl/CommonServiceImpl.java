package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.exception.*;
import cn.com.leyizhuang.app.core.utils.*;
import cn.com.leyizhuang.app.core.utils.csrf.EncryptUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.management.User;
import cn.com.leyizhuang.app.foundation.pojo.management.UserRole;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaActGoodsMapping;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwRequisitionOrder;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwRequisitionOrderGoods;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.DeliverySimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.GoodsSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.ProductCouponSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.PromotionSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponseGoods;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.CusSignLog;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.vo.OrderGoodsVO;
import cn.com.leyizhuang.app.foundation.vo.UserVO;
import cn.com.leyizhuang.common.foundation.pojo.SmsAccount;
import cn.com.leyizhuang.common.util.AssertUtil;
import cn.com.leyizhuang.common.util.CountUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 通用方法实现
 *
 * @author Richard
 * Created on 2017-09-12 15:44
 **/
@Service
public class CommonServiceImpl implements CommonService {

    // private ExecutorService executorService = Executors.newFixedThreadPool(20);
    private static final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

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

    @Resource
    private AppOrderService orderService;

    @Resource
    private SmsAccountService smsAccountService;

    @Resource
    private PaymentDataService paymentDataService;

    @Resource
    private LeBiVariationLogService leBiVariationLogService;

    @Resource
    private GoodsService goodsService;

    @Resource
    private AppToWmsOrderService appToWmsOrderService;

    @Resource
    private OrderDeliveryInfoDetailsService deliveryInfoDetailsService;

    @Resource
    private ReturnOrderService returnOrderService;

    @Resource
    private ProductCouponService productCouponService;

    @Autowired
    private GoodsPriceService goodsPriceService;

    @Autowired
    private ArrearsAuditService arrearsAuditServiceImpl;

    @Autowired
    private OrderAgencyFundService orderAgencyFundServiceImpl;

    @Autowired
    private AppActService actService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public User saveUserAndUserRoleByUserVO(UserVO userVO) {
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
        return user;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public User updateUserAndUserRoleByUserVO(UserVO userVO) {
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
            return user;
        }
        return null;
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
    public AppCustomer saveCustomerInfo(AppCustomer customer, CustomerLeBi leBi, CustomerPreDeposit preDeposit) throws UnsupportedEncodingException {
        if (null != customer.getNickName()) {
            String utf8NickName = URLEncoder.encode(customer.getNickName(), "utf-8");
            customer.setNickName(utf8NickName);
        }
        customerService.save(customer);
        leBi.setCusId(customer.getCusId());
        leBi.setQuantity(leBi.getQuantity() == null ? 0 : leBi.getQuantity());
        customerService.saveLeBi(leBi);
        preDeposit.setCusId(customer.getCusId());
        preDeposit.setBalance(preDeposit.getBalance() == null ? 0 : preDeposit.getBalance());
        customerService.savePreDeposit(preDeposit);
        return customer;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void customerSign(Long userId, Integer identityType) throws UnsupportedEncodingException {
        if (null != userId) {
            AppCustomer customer = customerService.findById(userId);
            if (null != customer) {
                //更新顾客乐币数量并添加乐币变动日志
                customerService.addLeBiQuantityByUserIdAndIdentityType(userId, identityType, AppConstant.SIGN_AWARD_LEBI_QTY);

                CustomerLeBiVariationLog customerLeBiVariationLog = new CustomerLeBiVariationLog();
                customerLeBiVariationLog.setCusId(userId);
                customerLeBiVariationLog.setLeBiVariationType(LeBiVariationType.SIGN);
                customerLeBiVariationLog.setVariationQuantity(AppConstant.SIGN_AWARD_LEBI_QTY);
                customerLeBiVariationLog.setVariationTime(new Date());
                customerLeBiVariationLog.setAfterVariationQuantity(customerService.findLeBiQuantityByUserIdAndIdentityType(userId, identityType));
                leBiVariationLogService.addCustomerLeBiVariationLog(customerLeBiVariationLog);

                //更新顾客签到信息并添加签到日志
                int consecutiveSignDays = 0;
                if (null != customer.getLastSignTime()) {
                    int differDays = DateUtil.getDifferDays(customer.getLastSignTime(), new Date());
                    if (!(differDays > 1)) {
                        consecutiveSignDays = (null != customer.getConsecutiveSignDays() ? customer.getConsecutiveSignDays() : 0) + 1;
                    } else {
                        consecutiveSignDays = 1;
                    }
                } else {
                    consecutiveSignDays = 1;
                }
                customerService.updateCustomerSignInfoByCustomerId(userId, new Date(), consecutiveSignDays);

                CusSignLog log = new CusSignLog();
                log.setAwardLebiQty(1);
                log.setCusId(userId);
                log.setCusName(customer.getName());
                log.setDescription("赏你" + AppConstant.SIGN_AWARD_LEBI_QTY + "乐币!");
                log.setSignTime(new Date());
                customerService.saveSignLog(log);
            }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAndUpdateMaterialList(List<MaterialListDO> materialListSave, List<MaterialListDO> materialListUpdate) {
        if ((null != materialListSave && materialListSave.size() > 0) || (null != materialListUpdate && materialListUpdate.size() > 0)) {
            materialListService.batchSave(materialListSave);
            for (MaterialListDO materialListDO : materialListUpdate) {
                materialListService.modifyQty(materialListDO.getId(), materialListDO.getQty());
            }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reduceInventoryAndMoney(DeliverySimpleInfo deliverySimpleInfo, Map<Long, Integer> inventoryCheckMap, Long cityId,
                                        Integer identityType, Long userId, Long customerId, List<Long> cashCouponIds,
                                        List<OrderCouponInfo> productCouponList, OrderBillingDetails billingDetails,
                                        String orderNumber, String ipAddress) throws
            LockStoreInventoryException, LockCityInventoryException, LockCustomerCashCouponException, LockCustomerLebiException,
            LockCustomerPreDepositException, LockStorePreDepositException, LockEmpCreditMoneyException, LockStoreCreditMoneyException,
            LockStoreSubventionException, SystemBusyException {
        //扣减商品库存
        if (deliverySimpleInfo.getDeliveryType().equalsIgnoreCase(AppDeliveryType.SELF_TAKE.getValue())) {
            if (identityType.equals(AppIdentityType.CUSTOMER.getValue()) ||
                    identityType.equals(AppIdentityType.SELLER.getValue())) {
                for (Map.Entry<Long, Integer> entry : inventoryCheckMap.entrySet()) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        StoreInventory storeInventory = storeService.findStoreInventoryByStoreIdAndGoodsId(deliverySimpleInfo.getBookingStoreId(), entry.getKey());
                        if (null != storeInventory) {
                            if (storeInventory.getAvailableIty() < entry.getValue()) {
                                //  2018年4月8日 Jerry 修改提示语商品名称库存不足
                                throw new LockStoreInventoryException("该门店下商品为" + storeInventory.getSkuName() + "的库存不足!");
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
                            throw new LockCityInventoryException("该城市下商品为" + cityInventory.getSkuName() + "库存不足!");
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
                            log.setChangeTypeDesc(CityInventoryAvailableQtyChangeType.HOUSE_DELIVERY_ORDER.getDescription());
                            log.setReferenceNumber(orderNumber);
                            cityService.addCityInventoryAvailableQtyChangeLog(log);
                            break;
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
        //扣减顾客产品券、优惠券及乐币
        if (identityType.equals(AppIdentityType.CUSTOMER.getValue()) ||
                identityType.equals(AppIdentityType.SELLER.getValue())) {
            Long customerIdTemp;
            if (identityType.equals(AppIdentityType.CUSTOMER.getValue())) {
                customerIdTemp = userId;
            } else {
                customerIdTemp = customerId;
            }
            //扣减顾客产品券
            if (null != productCouponList && productCouponList.size() > 0) {
                for (OrderCouponInfo couponInfo : productCouponList) {
                    Integer affectLine = customerService.lockCustomerProductCouponById(couponInfo.getCouponId(), orderNumber);
                    if (affectLine > 0) {
                        CustomerProductCouponChangeLog changeLog = new CustomerProductCouponChangeLog();
                        changeLog.setCusId(customerIdTemp);
                        changeLog.setCouponId(couponInfo.getCouponId());
                        changeLog.setChangeType(CustomerProductCouponChangeType.PLACE_ORDER);
                        changeLog.setChangeTypeDesc(CustomerProductCouponChangeType.PLACE_ORDER.getDescription());
                        changeLog.setReferenceNumber(orderNumber);
                        changeLog.setOperatorId(userId);
                        changeLog.setOperatorIp(ipAddress);
                        changeLog.setOperatorType(AppIdentityType.getAppIdentityTypeByValue(identityType));
                        changeLog.setUseTime(new Date());

                        productCouponService.addCustomerProductCouponChangeLog(changeLog);
                    } else {
                        throw new LockCustomerProductCouponException("该客户id为" + couponInfo.getCouponId() + "的产品券已使用或已失效!");
                    }
                }
            }
            //todo 扣减产品券时更新买券订单可退商品数量

            //扣减优惠券
            if (null != cashCouponIds && cashCouponIds.size() > 0) {
                for (Long id : cashCouponIds) {
                    Integer affectLine = customerService.lockCustomerCashCouponById(id, orderNumber);
                    if (affectLine > 0) {
                        CustomerCashCouponChangeLog log = new CustomerCashCouponChangeLog();
                        log.setCusId(customerIdTemp);
                        log.setCouponId(id);
                        log.setChangeType(CustomerCashCouponChangeType.PLACE_ORDER);
                        log.setChangeTypeDesc(CustomerCashCouponChangeType.PLACE_ORDER.getDescription());
                        log.setUseTime(Calendar.getInstance().getTime());
                        log.setOperatorId(userId);
                        log.setOperatorType(AppIdentityType.getAppIdentityTypeByValue(identityType));
                        log.setOperatorIp(ipAddress);
                        log.setUseTime(new Date());
                        customerService.addCustomerCashCouponChangeLog(log);
                    } else {
                        throw new LockCustomerCashCouponException("该客户id为" + id + "的优惠券已使用或已失效!");
                    }
                }
            }
            //扣减乐币
            if (null != billingDetails.getLebiQuantity() && billingDetails.getLebiQuantity() > 0) {
                for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                    CustomerLeBi customerLeBi = customerService.findCustomerLebiByCustomerId(customerIdTemp);
                    if (null != customerLeBi) {
                        if (customerLeBi.getQuantity() < billingDetails.getLebiQuantity()) {
                            throw new LockCustomerLebiException("该客户的乐币数量不足！");
                        }
                        Integer affectLine = customerService.lockCustomerLebiByUserIdAndQty(customerIdTemp,
                                billingDetails.getLebiQuantity(), customerLeBi.getLastUpdateTime());
                        if (affectLine > 0) {
                            CustomerLeBiVariationLog log = new CustomerLeBiVariationLog();
                            log.setCusId(customerIdTemp);
                            log.setVariationTime(Calendar.getInstance().getTime());
                            log.setVariationQuantity(billingDetails.getLebiQuantity());
                            log.setAfterVariationQuantity(customerLeBi.getQuantity() - billingDetails.getLebiQuantity());
                            log.setOrderNum(orderNumber);
                            log.setLeBiVariationType(LeBiVariationType.ORDER);
                            log.setVariationTypeDesc(LeBiVariationType.ORDER.getDescription());
                            customerService.addCustomerLeBiVariationLog(log);
                            break;
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
            if (null != billingDetails.getCusPreDeposit() && billingDetails.getCusPreDeposit() > 0) {
                for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                    CustomerPreDeposit preDeposit = customerService.findByCusId(userId);
                    if (null != preDeposit) {
                        if (preDeposit.getBalance() < billingDetails.getCusPreDeposit()) {
                            throw new LockCustomerPreDepositException("该客户预存款余额不足!");
                        }
                        Integer affectLine = customerService.lockCustomerDepositByUserIdAndDeposit(
                                userId, billingDetails.getCusPreDeposit(), preDeposit.getLastUpdateTime());
                        if (affectLine > 0) {
                            CusPreDepositLogDO cusPreDepositLogDO = new CusPreDepositLogDO();
                            cusPreDepositLogDO.setCusId(userId);
                            cusPreDepositLogDO.setCreateTime(LocalDateTime.now());
                            cusPreDepositLogDO.setChangeMoney(billingDetails.getCusPreDeposit());
                            cusPreDepositLogDO.setBalance(preDeposit.getBalance() - billingDetails.getCusPreDeposit());
                            cusPreDepositLogDO.setOrderNumber(orderNumber);
                            cusPreDepositLogDO.setChangeType(CustomerPreDepositChangeType.PLACE_ORDER);
                            cusPreDepositLogDO.setChangeTypeDesc(CustomerPreDepositChangeType.PLACE_ORDER.getDescription());
                            cusPreDepositLogDO.setOperatorType(AppIdentityType.getAppIdentityTypeByValue(identityType));
                            cusPreDepositLogDO.setOperatorId(userId);
                            cusPreDepositLogDO.setOperatorIp(ipAddress);
                            customerService.addCusPreDepositLog(cusPreDepositLogDO);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    } else {
                        throw new LockCustomerPreDepositException("没有找到该客户的预存款信息");
                    }
                }
            }
        }
        //扣减门店预存款
        if (identityType == AppIdentityType.SELLER.getValue() ||
                (identityType == AppIdentityType.DECORATE_MANAGER.getValue())) {
            if (null != billingDetails.getStPreDeposit() && billingDetails.getStPreDeposit() > 0) {
                for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                    StorePreDeposit preDeposit = storeService.findStorePreDepositByUserIdAndIdentityType(userId, identityType);
                    if (null != preDeposit) {
                        if (preDeposit.getBalance() < billingDetails.getStPreDeposit()) {
                            throw new LockStorePreDepositException("导购所属门店预存款余额不足!");
                        }
                        int affectLine = storeService.updateStoreDepositByStoreIdAndStoreDeposit(
                                preDeposit.getStoreId(), billingDetails.getStPreDeposit(), preDeposit.getLastUpdateTime());
                        if (affectLine > 0) {
                            StPreDepositLogDO log = new StPreDepositLogDO();
                            log.setStoreId(preDeposit.getStoreId());
                            log.setChangeMoney(billingDetails.getStPreDeposit());
                            log.setBalance(preDeposit.getBalance() - billingDetails.getStPreDeposit());
                            log.setCreateTime(LocalDateTime.now());
                            log.setOrderNumber(orderNumber);
                            log.setOperatorId(userId);
                            log.setOperatorType(AppIdentityType.getAppIdentityTypeByValue(identityType));
                            log.setOperatorIp(ipAddress);
                            log.setChangeType(StorePreDepositChangeType.PLACE_ORDER);
                            log.setChangeTypeDesc(StorePreDepositChangeType.PLACE_ORDER.getDescription());
                            storeService.addStPreDepositLog(log);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    } else {
                        throw new LockStorePreDepositException("没有找到该导购所在门店的预存款信息!");
                    }
                }
            }
        }
        //扣减导购信用额度
        if (identityType == AppIdentityType.SELLER.getValue()) {
            if (null != billingDetails.getEmpCreditMoney() && billingDetails.getEmpCreditMoney() > 0) {
                for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                    EmpCreditMoney empCreditMoney = employeeService.findEmpCreditMoneyByEmpId(userId);
                    if (null != empCreditMoney) {
                        if (empCreditMoney.getCreditLimitAvailable() < billingDetails.getEmpCreditMoney()) {
                            throw new LockEmpCreditMoneyException("导购信用额度不足!");
                        }
                        int affectLine = employeeService.lockGuideCreditByUserIdAndCredit(
                                userId, billingDetails.getEmpCreditMoney(), empCreditMoney.getLastUpdateTime());
                        if (affectLine > 0) {
                            EmpCreditMoneyChangeLog log = new EmpCreditMoneyChangeLog();
                            log.setEmpId(userId);
                            log.setTempCreditLimitChangeAmount(0D);
                            log.setTempCreditLimitAfterChange(empCreditMoney.getTempCreditLimit());
                            log.setCreditLimitAvailableChangeAmount(billingDetails.getEmpCreditMoney());
                            log.setCreditLimitAvailableAfterChange(empCreditMoney.getCreditLimitAvailable() - billingDetails.getEmpCreditMoney());
                            log.setChangeType(EmpCreditMoneyChangeType.PLACE_ORDER);
                            log.setChangeTypeDesc(EmpCreditMoneyChangeType.PLACE_ORDER.getDescription());
                            log.setCreateTime(Calendar.getInstance().getTime());
                            log.setOperatorId(userId);
                            log.setOperatorType(AppIdentityType.getAppIdentityTypeByValue(identityType));
                            log.setOperatorIp(ipAddress);
                            log.setReferenceNumber(orderNumber);
                            employeeService.addEmpCreditMoneyChangeLog(log);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    } else {
                        throw new LockEmpCreditMoneyException("没有找到该导购信用额度信息!");
                    }
                }
            }
        }
        //扣减门店信用金及现金返利
        if (identityType == AppIdentityType.DECORATE_MANAGER.getValue()) {
            //扣减门店信用金
            if (null != billingDetails.getStoreCreditMoney() && billingDetails.getStoreCreditMoney() > 0) {
                for (int i = 0; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                    StoreCreditMoney storeCreditMoney = storeService.findStoreCreditMoneyByEmpId(userId);
                    if (null != storeCreditMoney) {
                        if (storeCreditMoney.getCreditLimitAvailable() < billingDetails.getStoreCreditMoney()) {
                            throw new LockStoreCreditMoneyException("该门店（装饰公司）信用额度不足!");
                        }
                        int affectLine = storeService.lockStoreCreditByUserIdAndCredit(
                                userId, billingDetails.getStoreCreditMoney(), storeCreditMoney.getLastUpdateTime());
                        if (affectLine > 0) {
                            StoreCreditMoneyChangeLog log = new StoreCreditMoneyChangeLog();
                            log.setStoreId(storeCreditMoney.getStoreId());
                            log.setChangeAmount(billingDetails.getStoreCreditMoney());
                            log.setCreditLimitAvailableAfterChange(storeCreditMoney.getCreditLimitAvailable() - billingDetails.getStoreCreditMoney());
                            log.setCreateTime(Calendar.getInstance().getTime());
                            log.setChangeType(StoreCreditMoneyChangeType.PLACE_ORDER);
                            log.setChangeTypeDesc(StoreCreditMoneyChangeType.PLACE_ORDER.getDescription());
                            log.setOperatorId(userId);
                            log.setOperatorType(AppIdentityType.getAppIdentityTypeByValue(identityType));
                            log.setOperatorIp(ipAddress);
                            log.setReferenceNumber(orderNumber);
                            storeService.addStoreCreditMoneyChangeLog(log);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    } else {
                        throw new LockStoreCreditMoneyException("没有找到该门店（装饰公司）信用额度信息!");
                    }
                }

            }
            //扣减现金返利
            if (null != billingDetails.getStoreSubvention() && billingDetails.getStoreSubvention() > 0) {
                for (int i = 0; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                    StoreSubvention subvention = storeService.findStoreSubventionByEmpId(userId);
                    if (null != subvention) {
                        if (subvention.getBalance() < billingDetails.getStoreSubvention()) {
                            throw new LockStoreSubventionException("该门店现金返利余额不足！");
                        }
                        int affectLine = storeService.lockStoreSubventionByUserIdAndSubvention(
                                userId, billingDetails.getStoreSubvention(), subvention.getLastUpdateTime());
                        if (affectLine > 0) {
                            StoreSubventionChangeLog log = new StoreSubventionChangeLog();
                            log.setStoreId(subvention.getStoreId());
                            log.setChangeAmount(billingDetails.getStoreSubvention());
                            log.setBalance(subvention.getBalance() - billingDetails.getStoreSubvention());
                            log.setCreateTime(Calendar.getInstance().getTime());
                            log.setChangeType(StoreSubventionChangeType.PLACE_ORDER);
                            log.setChangeTypeDesc(StoreSubventionChangeType.PLACE_ORDER.getDescription());
                            log.setOperatorId(userId);
                            log.setOperatorType(AppIdentityType.getAppIdentityTypeByValue(identityType));
                            log.setOperatorIp(ipAddress);
                            log.setReferenceNumber(orderNumber);
                            storeService.addStoreSubventionChangeLog(log);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    } else {
                        throw new LockStoreSubventionException("该门店现金返利余额不足!");
                    }
                }
            }
        }
        //经销差价返还
        if (null != billingDetails.getJxPriceDifferenceAmount() && billingDetails.getJxPriceDifferenceAmount() > AppConstant.DOUBLE_ZERO) {
            for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                StorePreDeposit preDeposit = storeService.findStorePreDepositByUserIdAndIdentityType(userId, identityType);
                if (null != preDeposit) {
                    int affectLine = storeService.updateStoreDepositByStoreIdAndStoreDeposit(
                            preDeposit.getStoreId(), -billingDetails.getJxPriceDifferenceAmount(), preDeposit.getLastUpdateTime());
                    if (affectLine > 0) {
                        StPreDepositLogDO log = new StPreDepositLogDO();
                        log.setStoreId(preDeposit.getStoreId());
                        log.setChangeMoney(billingDetails.getJxPriceDifferenceAmount());
                        log.setBalance(preDeposit.getBalance() + billingDetails.getJxPriceDifferenceAmount());
                        log.setCreateTime(LocalDateTime.now());
                        log.setOrderNumber(orderNumber);
                        log.setOperatorId(userId);
                        log.setOperatorType(AppIdentityType.getAppIdentityTypeByValue(identityType));
                        log.setOperatorIp(ipAddress);
                        log.setChangeType(StorePreDepositChangeType.JX_PRICE_DIFFERENCE_RETURN);
                        log.setChangeTypeDesc(StorePreDepositChangeType.JX_PRICE_DIFFERENCE_RETURN.getDescription());
                        storeService.addStPreDepositLog(log);
                        break;
                    } else {
                        if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                            throw new SystemBusyException("系统繁忙，请稍后再试!");
                        }
                    }
                } else {
                    throw new LockStorePreDepositException("没有找到该导购所在门店的预存款信息!");
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAndHandleOrderRelevantInfo(OrderBaseInfo orderBaseInfo, OrderLogisticsInfo orderLogisticsInfo,
                                               List<OrderGoodsInfo> orderGoodsInfoList, List<OrderCouponInfo> orderCouponInfoList,
                                               OrderBillingDetails orderBillingDetails, List<OrderBillingPaymentDetails> paymentDetails,
                                               List<OrderJxPriceDifferenceReturnDetails> jxPriceDifferenceReturnDetails) throws UnsupportedEncodingException {

        if (null != orderBaseInfo) {
            if (null != orderBillingDetails && orderBillingDetails.getAmountPayable() <= AppConstant.PAY_UP_LIMIT) {
                orderBillingDetails.setPayUpTime(new Date());
                //发送提货码给顾客,及提示导购顾客下单信息
                String pickUpCode = this.sendPickUpCodeAndRemindMessageAfterPayUp(orderBaseInfo);
                orderBaseInfo.setPickUpCode(pickUpCode);

                //修改顾客上一次下单时间
                AppCustomer customer = new AppCustomer();
                Long cusId = null;
                if (orderBaseInfo.getCreatorIdentityType() != AppIdentityType.DECORATE_MANAGER) {
                    if (orderBaseInfo.getCreatorIdentityType() == AppIdentityType.SELLER) {
                        cusId = orderBaseInfo.getCustomerId();
                    } else {
                        cusId = orderBaseInfo.getCreatorId();
                    }
                    customer = customerService.findById(cusId);
                    customer.setLastConsumptionTime(new Date());
                }
                // 更新顾客归属门店及导购
                updateCustomerAttachedStoreAndSeller(orderBaseInfo, customer);

                //更新顾客信息
                if (null != customer.getCusId()) {
                    customerService.update(customer);
                }

                //更新订单状态及物流状态
                if (orderBaseInfo.getDeliveryType() == AppDeliveryType.HOUSE_DELIVERY) {
                    orderBaseInfo.setStatus(AppOrderStatus.PENDING_SHIPMENT);
                    orderBaseInfo.setDeliveryStatus(LogisticStatus.INITIAL);
                    //保存传wms配送单头档
                    AppStore store = storeService.findStoreByUserIdAndIdentityType(orderBaseInfo.getCreatorId(),
                            orderBaseInfo.getCreatorIdentityType().getValue());
                    AtwRequisitionOrder requisitionOrder = AtwRequisitionOrder.transform(orderBaseInfo, orderLogisticsInfo,
                            store, orderBillingDetails, orderGoodsInfoList.size());
                    appToWmsOrderService.saveAtwRequisitionOrder(requisitionOrder);
                    //保存传wms配送单商品信息
                    if (orderGoodsInfoList.size() > 0) {
                        for (OrderGoodsInfo goodsInfo : orderGoodsInfoList) {
                            AtwRequisitionOrderGoods requisitionOrderGoods = AtwRequisitionOrderGoods.transform(goodsInfo.getOrderNumber(),
                                    goodsInfo.getSku(), goodsInfo.getSkuName(), goodsInfo.getRetailPrice(), goodsInfo.getOrderQuantity(), goodsInfo.getCompanyFlag());
                            appToWmsOrderService.saveAtwRequisitionOrderGoods(requisitionOrderGoods);
                        }
                    }
                    //初始化订单物流信息
                    OrderDeliveryInfoDetails deliveryInfoDetails = new OrderDeliveryInfoDetails();
                    deliveryInfoDetails.setDeliveryInfo(orderBaseInfo.getOrderNumber(), LogisticStatus.INITIAL, LogisticStatus.INITIAL.getDescription(),
                            null, null, null, null, null);
                    deliveryInfoDetailsService.addOrderDeliveryInfoDetails(deliveryInfoDetails);
                } else if (orderBaseInfo.getDeliveryType() == AppDeliveryType.SELF_TAKE) {
                    orderBaseInfo.setStatus(AppOrderStatus.PENDING_RECEIVE);
                }

                //TODO 返还经销差价

            }
            /* ******************* 保存订单相关一系列信息 ******************* */
            //保存订单基础信息
            orderService.saveOrderBaseInfo(orderBaseInfo);

            if (null != orderBaseInfo.getId()) {
                //保存订单物流信息
                if (null != orderLogisticsInfo) {
                    orderLogisticsInfo.setOid(orderBaseInfo.getId());
                    orderService.saveOrderLogisticsInfo(orderLogisticsInfo);
                }
                //保存订单商品信息
                if (null != orderGoodsInfoList && orderGoodsInfoList.size() > 0) {
                    for (OrderGoodsInfo goodsInfo : orderGoodsInfoList) {
                        goodsInfo.setOid(orderBaseInfo.getId());
                        orderService.saveOrderGoodsInfo(goodsInfo);
                    }
                }
                //保存订单券信息
                if (null != orderCouponInfoList && orderCouponInfoList.size() > 0) {
                    for (OrderCouponInfo couponInfo : orderCouponInfoList) {
                        couponInfo.setOid(orderBaseInfo.getId());
                        orderService.saveOrderCouponInfo(couponInfo);
                    }
                }
                //保存订单账单信息
                if (null != orderBillingDetails) {
                    orderBillingDetails.setOid(orderBaseInfo.getId());
                    orderService.saveOrderBillingDetails(orderBillingDetails);
                }
                //保存订单账单支付明细信息
                if (null != paymentDetails && paymentDetails.size() > AppConstant.INTEGER_ZERO) {
                    for (OrderBillingPaymentDetails paymentDetail : paymentDetails) {
                        paymentDetail.setOrderId(orderBaseInfo.getId());
                        orderService.saveOrderBillingPaymentDetail(paymentDetail);
                    }
                }
                //保存订单经销差价返还明细
                if (null != jxPriceDifferenceReturnDetails && jxPriceDifferenceReturnDetails.size() > AppConstant.INTEGER_ZERO) {
                    for (OrderJxPriceDifferenceReturnDetails returnDetails : jxPriceDifferenceReturnDetails) {
                        returnDetails.setOid(orderBaseInfo.getId());
                        orderService.saveOrderJxPriceDifferenceReturnDetails(returnDetails);
                    }
                }
            } else {
                throw new OrderSaveException("订单主键生成失败!");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleOrderRelevantBusinessAfterOnlinePayUp(String orderNumber, String tradeNo, String tradeStatus, OnlinePayType onlinePayType) throws IOException {
        if (StringUtils.isNotBlank(orderNumber)) {
            //更新订单第三方支付信息
            List<PaymentDataDO> paymentDataList = paymentDataService.findByOutTradeNoAndTradeStatus(orderNumber, PaymentDataStatus.TRADE_SUCCESS);
            PaymentDataDO paymentData = paymentDataList.get(0);

            OrderBaseInfo baseInfo = orderService.getOrderByOrderNumber(orderNumber);

            //更新订单账单信息
            OrderBillingDetails billingDetails = orderService.getOrderBillingDetail(orderNumber);
            billingDetails.setOnlinePayType(onlinePayType);
            billingDetails.setOnlinePayAmount(paymentData.getTotalFee());
            billingDetails.setOnlinePayTime(paymentData.getNotifyTime());
            /*2018-04-17 genaration 导购只支付运费的业务逻辑修改*/
//            billingDetails.setArrearage(billingDetails.getArrearage());
//            billingDetails.setIsPayUp(true);
//            billingDetails.setPayUpTime(paymentData.getNotifyTime());
            billingDetails.setArrearage(CountUtil.sub(billingDetails.getArrearage(), paymentData.getTotalFee()));
            if (null != billingDetails.getArrearage() && billingDetails.getArrearage() <= 0) {
                billingDetails.setIsPayUp(true);
                billingDetails.setPayUpTime(paymentData.getNotifyTime());
            }
            /**/

            //新增订单账单支付明细
            OrderBillingPaymentDetails paymentDetails = new OrderBillingPaymentDetails();
            paymentDetails.setOrderId(baseInfo.getId());
            paymentDetails.setAmount(paymentData.getTotalFee());
            paymentDetails.setOrderNumber(baseInfo.getOrderNumber());
            paymentDetails.setPayTime(paymentData.getNotifyTime());

            if (OnlinePayType.ALIPAY == onlinePayType) {
                paymentDetails.setPayType(OrderBillingPaymentType.ALIPAY);
                paymentDetails.setPayTypeDesc(OrderBillingPaymentType.ALIPAY.getDescription());
            } else if (OnlinePayType.WE_CHAT == onlinePayType) {
                paymentDetails.setPayType(OrderBillingPaymentType.WE_CHAT);
                paymentDetails.setPayTypeDesc(OrderBillingPaymentType.WE_CHAT.getDescription());
            } else if (OnlinePayType.UNION_PAY == onlinePayType) {
                paymentDetails.setPayType(OrderBillingPaymentType.UNION_PAY);
                paymentDetails.setPayTypeDesc(OrderBillingPaymentType.UNION_PAY.getDescription());
            }
            if (baseInfo.getCreatorIdentityType() == AppIdentityType.CUSTOMER) {
                paymentDetails.setPaymentSubjectType(PaymentSubjectType.CUSTOMER);
                paymentDetails.setPaymentSubjectTypeDesc(PaymentSubjectType.CUSTOMER.getDescription());
            } else if (baseInfo.getCreatorIdentityType() == AppIdentityType.SELLER) {
                paymentDetails.setPaymentSubjectType(PaymentSubjectType.SELLER);
                paymentDetails.setPaymentSubjectTypeDesc(PaymentSubjectType.SELLER.getDescription());
            } else if (baseInfo.getCreatorIdentityType() == AppIdentityType.DECORATE_MANAGER) {
                paymentDetails.setPaymentSubjectType(PaymentSubjectType.DECORATE_MANAGER);
                paymentDetails.setPaymentSubjectTypeDesc(PaymentSubjectType.DECORATE_MANAGER.getDescription());
            }
            paymentDetails.setReceiptNumber(OrderUtils.generateReceiptNumber(baseInfo.getCityId()));
            paymentDetails.setReplyCode(tradeStatus);
            paymentDetails.setCreateTime(paymentData.getNotifyTime());

            //发送提货码给顾客,及提示导购顾客下单信息
            String pickUpCode = this.sendPickUpCodeAndRemindMessageAfterPayUp(baseInfo);
            baseInfo.setPickUpCode(pickUpCode);

            //修改顾客上一次下单时间
            Long cusId = null;
            AppCustomer customer = new AppCustomer();
            if (baseInfo.getCreatorIdentityType() != AppIdentityType.DECORATE_MANAGER) {
                if (baseInfo.getCreatorIdentityType() == AppIdentityType.SELLER) {
                    cusId = baseInfo.getCustomerId();
                } else {
                    cusId = baseInfo.getCreatorId();
                }
            }
            if (null != cusId) {
                customer = customerService.findById(cusId);
                customer.setLastConsumptionTime(new Date());
            }

            //更新顾客归属门店及导购
            updateCustomerAttachedStoreAndSeller(baseInfo, customer);

            //更新订单状态及物流状态
            if (baseInfo.getDeliveryType() == AppDeliveryType.SELF_TAKE) {
                baseInfo.setStatus(AppOrderStatus.PENDING_RECEIVE);
            } else if (baseInfo.getDeliveryType() == AppDeliveryType.HOUSE_DELIVERY) {
                baseInfo.setStatus(AppOrderStatus.PENDING_SHIPMENT);
                baseInfo.setDeliveryStatus(LogisticStatus.INITIAL);

                //物流信息初始化
                OrderDeliveryInfoDetails deliveryInfoDetails = new OrderDeliveryInfoDetails();
                deliveryInfoDetails.setDeliveryInfo(baseInfo.getOrderNumber(), LogisticStatus.INITIAL, LogisticStatus.INITIAL.getDescription(),
                        null, null, null, null, null);
                deliveryInfoDetailsService.addOrderDeliveryInfoDetails(deliveryInfoDetails);

                // ***********************发送WMS 在微信和支付宝完成支付回调方法中已发送***************************
                //保存传wms配送单头档
                AppStore store = storeService.findStoreByUserIdAndIdentityType(baseInfo.getCreatorId(),
                        baseInfo.getCreatorIdentityType().getValue());
                List<OrderGoodsInfo> orderGoodsInfoList = orderService.getOrderGoodsInfoByOrderNumber(orderNumber);
                int orderGoodsSize = orderGoodsInfoList.size();
                OrderLogisticsInfo orderLogisticsInfo = orderService.getOrderLogistice(orderNumber);
                AtwRequisitionOrder requisitionOrder = AtwRequisitionOrder.transform(baseInfo, orderLogisticsInfo,
                        store, billingDetails, orderGoodsSize);
                appToWmsOrderService.saveAtwRequisitionOrder(requisitionOrder);
                //保存传wms配送单商品信息
                if (orderGoodsSize > 0) {
                    for (OrderGoodsInfo goodsInfo : orderGoodsInfoList) {
                        AtwRequisitionOrderGoods requisitionOrderGoods = AtwRequisitionOrderGoods.transform(goodsInfo.getOrderNumber(),
                                goodsInfo.getSku(), goodsInfo.getSkuName(), goodsInfo.getRetailPrice(), goodsInfo.getOrderQuantity(), goodsInfo.getCompanyFlag());
                        appToWmsOrderService.saveAtwRequisitionOrderGoods(requisitionOrderGoods);
                    }
                }

            }

            //更新订单基础信息
            orderService.updateOrderBaseInfo(baseInfo);
            //第三方支付信息
            //paymentDataService.updateByTradeStatusIsWaitPay(paymentData);
            //更新订单账单信息
            orderService.updateOrderBillingDetails(billingDetails);
            //保存新增订单账单支付明细
            orderService.savePaymentDetails(paymentDetails);
            //更新顾客信息
            if (customer.getCusId() != null) {
                customerService.update(customer);
            }
            //增加订单生命周期信息
            orderService.addOrderLifecycle(OrderLifecycleType.PAYED, orderNumber);
        }
    }

    private void updateCustomerAttachedStoreAndSeller(OrderBaseInfo baseInfo, AppCustomer customer) {
        if (baseInfo.getCreatorIdentityType() == AppIdentityType.SELLER) {
            AppStore originalStore = storeService.findStoreByUserIdAndIdentityType(baseInfo.getCustomerId(),
                    AppIdentityType.CUSTOMER.getValue());
            if (originalStore.getIsDefault()) {
                AppStore newStore = storeService.findStoreByUserIdAndIdentityType(baseInfo.getCreatorId(),
                        AppIdentityType.SELLER.getValue());
                customer.setStoreId(newStore.getStoreId());
                customer.setSalesConsultId(baseInfo.getCreatorId());
                customer.setBindingTime(new Date());
            }

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearOrderGoodsInMaterialList(Long userId, Integer identityType, List<GoodsSimpleInfo> goodsList,
                                              List<ProductCouponSimpleInfo> productCouponList) {
        if (null != userId && null != identityType) {
            if (null != goodsList && goodsList.size() > 0) {
                Set<Long> goodsIds = new HashSet<>();
                for (GoodsSimpleInfo goods : goodsList) {
                    goodsIds.add(goods.getId());
                }
                materialListService.deleteMaterialListByUserIdAndIdentityTypeAndGoodsIds(
                        userId, AppIdentityType.getAppIdentityTypeByValue(identityType), goodsIds);
            }
            if (null != productCouponList) {
                Set<Long> couponGoodsIds = new HashSet<>();
                for (ProductCouponSimpleInfo couponGoods : productCouponList) {
                    couponGoodsIds.add(couponGoods.getId());
                }
                materialListService.deleteMaterialListProductCouponGoodsByUserIdAndIdentityTypeAndGoodsIds(
                        userId, AppIdentityType.getAppIdentityTypeByValue(identityType), couponGoodsIds);
            }
        }
    }

    @Override
    public List<OrderCouponInfo> createOrderCashCouponInfo(OrderBaseInfo orderBaseInfo, List<Long> cashCouponList) {
        List<OrderCouponInfo> orderCouponInfoList = new ArrayList<>();
        if (null != orderBaseInfo) {
            if (null != cashCouponList && cashCouponList.size() > 0) {
                List<CustomerCashCoupon> cashCoupons = customerService.findCashCouponsByids(cashCouponList);
                for (CustomerCashCoupon coupon :
                        cashCoupons) {
                    OrderCouponInfo couponInfo = new OrderCouponInfo();
                    couponInfo.setOrderNumber(orderBaseInfo.getOrderNumber());
                    couponInfo.setCouponId(coupon.getId());
                    couponInfo.setCouponType(OrderCouponType.CASH_COUPON);
                    couponInfo.setGetType(coupon.getGetType());
                    couponInfo.setPurchasePrice(coupon.getPurchasePrice());
                    couponInfo.setCostPrice(coupon.getDenomination());
                    orderCouponInfoList.add(couponInfo);
                }
            }
        }
        return orderCouponInfoList;
    }

    @Override
    public List<OrderCouponInfo> createOrderProductCouponInfo(OrderBaseInfo orderBaseInfo, List<OrderGoodsInfo> productCouponList) {
        List<OrderCouponInfo> orderCouponInfoList = new ArrayList<>();
        if (null != orderBaseInfo) {
            Long customerIdTemp;
            if (orderBaseInfo.getCreatorIdentityType() == AppIdentityType.CUSTOMER) {
                customerIdTemp = orderBaseInfo.getCreatorId();
            } else {
                customerIdTemp = orderBaseInfo.getCustomerId();
            }
            if (null != productCouponList && productCouponList.size() > 0) {
                for (OrderGoodsInfo couponGoodsInfo : productCouponList) {
                    List<CustomerProductCoupon> productCoupons;
                    if (orderBaseInfo.getCreatorIdentityType() == AppIdentityType.SELLER) {
                        productCoupons = customerService.findProductCouponsByCustomerIdAndGoodsIdAndQty(
                                customerIdTemp, orderBaseInfo.getSalesConsultId(), couponGoodsInfo.getGid(), couponGoodsInfo.getOrderQuantity());
                    } else if (orderBaseInfo.getCreatorIdentityType() == AppIdentityType.CUSTOMER) {
                        productCoupons = customerService.findProductCouponsByCustomerIdAndGoodsIdAndQty(
                                customerIdTemp, null, couponGoodsInfo.getGid(), couponGoodsInfo.getOrderQuantity());
                    } else {
                        productCoupons = null;
                    }

                    if (null == productCoupons || productCoupons.size() < couponGoodsInfo.getOrderQuantity()) {
                        throw new LockCustomerProductCouponException(couponGoodsInfo.getSkuName() + "产品券数量不足!");
                    }
                    for (CustomerProductCoupon productCoupon : productCoupons) {
                        OrderCouponInfo couponInfo = new OrderCouponInfo();
                        couponInfo.setCouponType(OrderCouponType.PRODUCT_COUPON);
                        couponInfo.setCouponId(productCoupon.getId());
                        couponInfo.setOrderNumber(orderBaseInfo.getOrderNumber());
                        couponInfo.setPurchasePrice(productCoupon.getBuyPrice());
                        couponInfo.setCostPrice(couponGoodsInfo.getSettlementPrice());
                        couponInfo.setGetType(productCoupon.getGetType());
                        couponInfo.setSku(couponGoodsInfo.getSku());
                        couponInfo.setGoodsId(productCoupon.getGoodsId());
                        orderCouponInfoList.add(couponInfo);
                    }
                }
            }
        }
        return orderCouponInfoList;
    }

    @Override
    public CreateOrderGoodsSupport createOrderGoodsInfo(List<GoodsSimpleInfo> goodsList, Long userId, Integer identityType, Long customerId,
                                                        List<ProductCouponSimpleInfo> productCouponList, String orderNumber) throws UnsupportedEncodingException {
        List<OrderGoodsInfo> orderGoodsInfoList = new ArrayList<>();
        //新建一个map,用来存放最终要检核库存的商品和商品数量
        Map<Long, Integer> inventoryCheckMap = new HashMap<>();
        //定义订单商品零售总价
        Double goodsTotalPrice = 0D;
        //定义订单会员折扣
        Double memberDiscount = 0D;
        //定义订单促销折扣
        Double promotionDiscount = 0D;
        //处理订单本品商品信息
        //获取当单顾客信息
        AppCustomer customer = new AppCustomer();
        if (identityType == AppIdentityType.CUSTOMER.getValue()) {
            customer = customerService.findById(userId);
        } else if (identityType == AppIdentityType.SELLER.getValue()) {
            customer = customerService.findById(customerId);
        }
        if (null != goodsList && goodsList.size() > 0) {
            //获取本品id集合
            Set<Long> goodsIdSet = new HashSet<>();
            for (GoodsSimpleInfo goods : goodsList) {
                goodsIdSet.add(goods.getId());
            }
            //根据本品id集合查询商品信息
            List<OrderGoodsVO> goodsVOList = goodsService.findOrderGoodsVOListByUserIdAndIdentityTypeAndGoodsIds(
                    userId, identityType, goodsIdSet);
            //定义当前有唯一价格的本品id集合
            Set<Long> hasPriceGoodsIdSet = new HashSet<>();
            //循环处理查询到的商品信息
            for (OrderGoodsVO goodsVO : goodsVOList) {
                if (hasPriceGoodsIdSet.contains(goodsVO.getGid())) {
                    throw new GoodsMultipartPriceException("商品 '" + goodsVO.getSkuName() + "'在当前门店下存在多个价格!");
                } else {
                    hasPriceGoodsIdSet.add(goodsVO.getGid());
                }
                for (GoodsSimpleInfo info : goodsList) {
                    if (info.getId().equals(goodsVO.getGid())) {
                        if (null == info.getQty() || info.getQty().equals(0)) {
                            throw new GoodsQtyErrorException("商品 '"+goodsVO.getSkuName() + "'数量出现异常(0或不存在)!");
                        }
                        goodsVO.setQty(info.getQty());
                    }
                }
                //加总商品零售价总额
                goodsTotalPrice += goodsVO.getRetailPrice() * goodsVO.getQty();
                //将本品数量加入库存检核map
                if (inventoryCheckMap.containsKey(goodsVO.getGid())) {
                    inventoryCheckMap.put(goodsVO.getGid(), inventoryCheckMap.get(goodsVO.getGid()) + goodsVO.getQty());
                } else {
                    inventoryCheckMap.put(goodsVO.getGid(), goodsVO.getQty());
                }
                //设置本品会员折扣
                if (identityType == AppIdentityType.DECORATE_MANAGER.getValue()) {
                    memberDiscount += (goodsVO.getRetailPrice() - goodsVO.getVipPrice()) * goodsVO.getQty();
                } else {
                    if (null == customer) {
                        throw new OrderCustomerException("订单顾客信息异常!");
                    }
                    if (customer.getCustomerType() == AppCustomerType.MEMBER) {
                        memberDiscount += (goodsVO.getRetailPrice() - goodsVO.getVipPrice()) * goodsVO.getQty();
                    } else {
                        memberDiscount += 0D;
                    }
                }
                OrderGoodsInfo goodsInfo = new OrderGoodsInfo();
                goodsInfo.setOrderNumber(orderNumber);
                goodsInfo.setGoodsLineType(AppGoodsLineType.GOODS);
                goodsInfo.setRetailPrice(goodsVO.getRetailPrice());
                goodsInfo.setVIPPrice(goodsVO.getVipPrice());
                goodsInfo.setWholesalePrice(goodsVO.getWholesalePrice());
                goodsInfo.setIsPriceShare(Boolean.FALSE);
                goodsInfo.setPromotionSharePrice(0D);
                goodsInfo.setLbSharePrice(0D);
                goodsInfo.setCashCouponSharePrice(0D);
                goodsInfo.setCashReturnSharePrice(0D);
                goodsInfo.setIsReturnable(Boolean.TRUE);
                if (identityType == AppIdentityType.DECORATE_MANAGER.getValue()) {
                    goodsInfo.setSettlementPrice(goodsVO.getVipPrice());
                    goodsInfo.setReturnPrice(goodsVO.getVipPrice());
                } else {
                    if (null == customer) {
                        throw new OrderCustomerException("订单顾客信息异常!");
                    }
                    if (customer.getCustomerType() == AppCustomerType.MEMBER) {
                        goodsInfo.setSettlementPrice(goodsVO.getVipPrice());
                        goodsInfo.setReturnPrice(goodsVO.getVipPrice());
                    } else {
                        goodsInfo.setSettlementPrice(goodsVO.getRetailPrice());
                        goodsInfo.setReturnPrice(goodsVO.getRetailPrice());
                    }
                }
                goodsInfo.setGid(goodsVO.getGid());
                goodsInfo.setSku(goodsVO.getSku());
                goodsInfo.setSkuName(goodsVO.getSkuName());
                goodsInfo.setOrderQuantity(goodsVO.getQty());
                goodsInfo.setShippingQuantity(0);
                goodsInfo.setReturnableQuantity(goodsVO.getQty());
                goodsInfo.setReturnQuantity(0);
                goodsInfo.setReturnPriority(1);
                goodsInfo.setPriceItemId(goodsVO.getPriceItemId());
                goodsInfo.setCompanyFlag(goodsVO.getCompanyFlag());
                goodsInfo.setCoverImageUri(goodsVO.getCoverImageUri());
                orderGoodsInfoList.add(goodsInfo);
            }

            if (goodsIdSet.size() != hasPriceGoodsIdSet.size()) {
                StringBuilder ids = new StringBuilder();
                for (Long id : goodsIdSet) {
                    if (!hasPriceGoodsIdSet.contains(id)) {
                        ids.append(id).append(",");
                    }
                }
                throw new GoodsNoPriceException("id为 '" + ids + "'的商品在当前门店下没有找到价格!");
            }
        }
        //将本品信息零存入本品商品列表
        List<OrderGoodsInfo> pureOrderGoodsInfo = new ArrayList<>();
        pureOrderGoodsInfo.addAll(orderGoodsInfoList);

        //处理订单产品券商品信息
        List<OrderGoodsInfo> productCouponGoodsList = new ArrayList<>();
        if (null != productCouponList && productCouponList.size() > 0) {
            //获取本品id集合
            Set<Long> couponGoodsIdSet = new HashSet<>();
            for (ProductCouponSimpleInfo productCouponGoods : productCouponList) {
                couponGoodsIdSet.add(productCouponGoods.getId());
            }
            //根据本品id集合查询商品信息
            List<OrderGoodsVO> couponGoodsList = goodsService.findOrderGoodsVOListByUserIdAndIdentityTypeAndGoodsIds(
                    userId, identityType, couponGoodsIdSet);
            //定义当前有唯一价格的本品id集合
            Set<Long> hasPriceCouponGoodsIdSet = new HashSet<>();

            for (OrderGoodsVO couponGoods : couponGoodsList) {
                if (hasPriceCouponGoodsIdSet.contains(couponGoods.getGid())) {
                    throw new GoodsMultipartPriceException("产品券商品 '" + couponGoods.getSkuName() + "'在当前门店下存在多个价格!");
                } else {
                    hasPriceCouponGoodsIdSet.add(couponGoods.getGid());
                }
                for (ProductCouponSimpleInfo info : productCouponList) {
                    if (null == info.getQty() || info.getQty().equals(0)) {
                        throw new GoodsQtyErrorException("产品券商品 '"+couponGoods.getSkuName() + "'数量出现异常(0或不存在)!");
                    }
                    if (info.getId().equals(couponGoods.getGid())) {
                        couponGoods.setQty(info.getQty());
                    }
                }
                //加总商品零售价总额
                goodsTotalPrice += couponGoods.getRetailPrice() * couponGoods.getQty();
                //将产品券数量加入库存检核map
                if (inventoryCheckMap.containsKey(couponGoods.getGid())) {
                    inventoryCheckMap.put(couponGoods.getGid(), inventoryCheckMap.get(couponGoods.getGid()) + couponGoods.getQty());
                } else {
                    inventoryCheckMap.put(couponGoods.getGid(), couponGoods.getQty());
                }
                //设置产品券商品会员折扣
                if (null == customer) {
                    throw new OrderCustomerException("订单顾客信息异常!");
                }
                if (customer.getCustomerType() == AppCustomerType.MEMBER) {
                    memberDiscount += (couponGoods.getRetailPrice() - couponGoods.getVipPrice()) * couponGoods.getQty();
                } else {
                    memberDiscount += 0D;
                }
                OrderGoodsInfo couponGoodsInfo = new OrderGoodsInfo();
                couponGoodsInfo.setOrderNumber(orderNumber);
                couponGoodsInfo.setGoodsLineType(AppGoodsLineType.PRODUCT_COUPON);
                couponGoodsInfo.setRetailPrice(couponGoods.getRetailPrice());
                couponGoodsInfo.setVIPPrice(couponGoods.getVipPrice());
                couponGoodsInfo.setWholesalePrice(couponGoods.getWholesalePrice());
                couponGoodsInfo.setIsPriceShare(Boolean.FALSE);
                couponGoodsInfo.setPromotionSharePrice(0D);
                couponGoodsInfo.setLbSharePrice(0D);
                couponGoodsInfo.setCashReturnSharePrice(0D);
                couponGoodsInfo.setCashCouponSharePrice(0D);
                couponGoodsInfo.setReturnPrice(0D);
                couponGoodsInfo.setIsReturnable(Boolean.TRUE);
                if (customer.getCustomerType() == AppCustomerType.MEMBER) {
                    couponGoodsInfo.setSettlementPrice(couponGoods.getVipPrice());
                } else {
                    couponGoodsInfo.setSettlementPrice(couponGoods.getRetailPrice());
                }
                couponGoodsInfo.setGid(couponGoods.getGid());
                couponGoodsInfo.setSku(couponGoods.getSku());
                couponGoodsInfo.setSkuName(couponGoods.getSkuName());
                couponGoodsInfo.setOrderQuantity(couponGoods.getQty());
                couponGoodsInfo.setShippingQuantity(0);
                couponGoodsInfo.setReturnableQuantity(couponGoods.getQty());
                couponGoodsInfo.setReturnQuantity(0);
                couponGoodsInfo.setReturnPriority(1);
                couponGoodsInfo.setPriceItemId(couponGoods.getPriceItemId());
                couponGoodsInfo.setCompanyFlag(couponGoods.getCompanyFlag());
                couponGoodsInfo.setCoverImageUri(couponGoods.getCoverImageUri());
                productCouponGoodsList.add(couponGoodsInfo);
            }
            if (couponGoodsIdSet.size() != hasPriceCouponGoodsIdSet.size()) {
                //StringBuilder ids = new StringBuilder();
                List<Long> noPriceGoodsIdList = new ArrayList<>();
                for (Long id : couponGoodsIdSet) {
                    if (!hasPriceCouponGoodsIdSet.contains(id)) {
                        noPriceGoodsIdList.add(id);
                    }
                }
                List<String> noPriceGoodsSkuNameList = goodsService.getGoodsSkuNameListByGoodsIdList(noPriceGoodsIdList);
               /* for (Long id : noPriceGoodsIdList) {
                    noPriceGoodsSkuNameList.add(productCouponList.stream().filter(p -> p.getId().equals(id)).collect(Collectors.toList()).get(0).getSkuName());
                }*/

                throw new GoodsNoPriceException("产品券商品:" + noPriceGoodsSkuNameList.toString() + "在当前门店下没有找到价格!");
            }
        }
        if (productCouponGoodsList.size() > 0) {
            orderGoodsInfoList.addAll(productCouponGoodsList);
        }
        CreateOrderGoodsSupport support = new CreateOrderGoodsSupport();
        support.setGoodsTotalPrice(goodsTotalPrice);
        support.setInventoryCheckMap(inventoryCheckMap);
        support.setMemberDiscount(CountUtil.HALF_UP_SCALE_2(memberDiscount));
        support.setOrderGoodsInfoList(orderGoodsInfoList);
        support.setProductCouponGoodsList(productCouponGoodsList);
        support.setPromotionDiscount(promotionDiscount);
        support.setPureOrderGoodsInfo(pureOrderGoodsInfo);
        return support;
    }

    @Override
    public List<OrderBillingPaymentDetails> createOrderBillingPaymentDetails(OrderBaseInfo orderBaseInfo, OrderBillingDetails orderBillingDetails) {
        List<OrderBillingPaymentDetails> billingPaymentDetails = new ArrayList<>();
        if (null != orderBaseInfo && null != orderBillingDetails) {
            if (null != orderBillingDetails.getCusPreDeposit() && orderBillingDetails.getCusPreDeposit() > AppConstant.DOUBLE_ZERO) {
                OrderBillingPaymentDetails details = new OrderBillingPaymentDetails();
                details.generateOrderBillingPaymentDetails(OrderBillingPaymentType.CUS_PREPAY, orderBillingDetails.getCusPreDeposit(),
                        PaymentSubjectType.CUSTOMER, orderBaseInfo.getOrderNumber(), OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
                billingPaymentDetails.add(details);
            }
            if (null != orderBillingDetails.getStPreDeposit() && orderBillingDetails.getStPreDeposit() > AppConstant.DOUBLE_ZERO) {
                OrderBillingPaymentDetails details = new OrderBillingPaymentDetails();
                details.generateOrderBillingPaymentDetails(OrderBillingPaymentType.ST_PREPAY, orderBillingDetails.getStPreDeposit(),
                        PaymentSubjectType.SELLER, orderBaseInfo.getOrderNumber(), OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
                billingPaymentDetails.add(details);
            }
            if (null != orderBillingDetails.getStoreCreditMoney() && orderBillingDetails.getStoreCreditMoney() > AppConstant.DOUBLE_ZERO) {
                OrderBillingPaymentDetails details = new OrderBillingPaymentDetails();
                details.generateOrderBillingPaymentDetails(OrderBillingPaymentType.STORE_CREDIT_MONEY, orderBillingDetails.getStoreCreditMoney(),
                        PaymentSubjectType.DECORATE_MANAGER, orderBaseInfo.getOrderNumber(), OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
                billingPaymentDetails.add(details);
            }
            if (null != orderBillingDetails.getLebiCashDiscount() && orderBillingDetails.getStoreCreditMoney() > AppConstant.DOUBLE_ZERO) {
                OrderBillingPaymentDetails details = new OrderBillingPaymentDetails();
                details.generateOrderBillingPaymentDetails(OrderBillingPaymentType.LE_BI, orderBillingDetails.getLebiCashDiscount(),
                        PaymentSubjectType.CUSTOMER, orderBaseInfo.getOrderNumber(), OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
                billingPaymentDetails.add(details);
            }
        }
        return billingPaymentDetails;
    }

    @Override
    public List<OrderJxPriceDifferenceReturnDetails> createOrderJxPriceDifferenceReturnDetails(OrderBaseInfo orderBaseInfo, List<OrderGoodsInfo> orderGoodsInfoList) {
        AppStore store = storeService.findById(orderBaseInfo.getStoreId());
        if (null != store && null != store.getStoreType()) {
            if (store.getStoreType() == StoreType.FX || store.getStoreType() == StoreType.JM) {
                if (null != orderGoodsInfoList && orderGoodsInfoList.size() > 0) {
                    List<OrderJxPriceDifferenceReturnDetails> detailsList = new ArrayList<>(20);
                    for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                        if (orderGoodsInfo.getGoodsLineType() == AppGoodsLineType.GOODS) {
                            OrderJxPriceDifferenceReturnDetails details = new OrderJxPriceDifferenceReturnDetails();
                            details.setAmount((orderGoodsInfo.getSettlementPrice() - orderGoodsInfo.getWholesalePrice()) * orderGoodsInfo.getOrderQuantity());
                            details.setCreateTime(new Date());
                            details.setOrderNumber(orderBaseInfo.getOrderNumber());
                            details.setQuantity(orderGoodsInfo.getOrderQuantity());
                            details.setUnitPrice(orderGoodsInfo.getSettlementPrice() - orderGoodsInfo.getWholesalePrice());
                            details.setReceiptNumber(OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
                            details.setSku(orderGoodsInfo.getSku());
                            details.setStoreId(orderBaseInfo.getStoreId());
                            details.setStoreCode(orderBaseInfo.getStoreCode());
                            detailsList.add(details);
                        }
                    }
                    return detailsList;
                }
            }
        }
        return null;
    }

    @Override
    public List<OrderJxPriceDifferenceReturnDetails> createOrderJxPriceDifferenceReturnDetails(OrderBaseInfo orderBaseInfo, List<OrderGoodsInfo> orderGoodsInfoList, List<PromotionSimpleInfo> promotionSimpleInfos) {
        Map<Long, Double> map = actService.returnGcActIdAndJXDiscunt(promotionSimpleInfos);

        AppStore store = storeService.findById(orderBaseInfo.getStoreId());
        if (null != store && null != store.getStoreType()) {
            if (store.getStoreType() == StoreType.FX || store.getStoreType() == StoreType.JM) {
                if (null != orderGoodsInfoList && orderGoodsInfoList.size() > 0) {
                    List<OrderJxPriceDifferenceReturnDetails> detailsList = new ArrayList<>(20);
                    for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {

                        if (orderGoodsInfo.getGoodsLineType() == AppGoodsLineType.GOODS) {
                            OrderJxPriceDifferenceReturnDetails details = new OrderJxPriceDifferenceReturnDetails();
                            if (null != orderGoodsInfo.getPromotionId()) {
                                Long actId = Long.valueOf(orderGoodsInfo.getPromotionId());
                                if (map.containsKey(actId)) {
                                    Double gcDiscount = map.get(actId);
                                    details.setAmount((orderGoodsInfo.getSettlementPrice() - orderGoodsInfo.getWholesalePrice() - gcDiscount) * orderGoodsInfo.getOrderQuantity());
                                    details.setUnitPrice(orderGoodsInfo.getSettlementPrice() - orderGoodsInfo.getWholesalePrice() - gcDiscount);
                                } else {
                                    details.setAmount((orderGoodsInfo.getSettlementPrice() - orderGoodsInfo.getWholesalePrice()) * orderGoodsInfo.getOrderQuantity());
                                    details.setUnitPrice(orderGoodsInfo.getSettlementPrice() - orderGoodsInfo.getWholesalePrice());
                                }
                            } else {
                                details.setAmount((orderGoodsInfo.getSettlementPrice() - orderGoodsInfo.getWholesalePrice()) * orderGoodsInfo.getOrderQuantity());
                                details.setUnitPrice(orderGoodsInfo.getSettlementPrice() - orderGoodsInfo.getWholesalePrice());
                            }
                            details.setCreateTime(new Date());
                            details.setOrderNumber(orderBaseInfo.getOrderNumber());
                            details.setQuantity(orderGoodsInfo.getOrderQuantity());
                            details.setReceiptNumber(OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
                            details.setSku(orderGoodsInfo.getSku());
                            details.setStoreId(orderBaseInfo.getStoreId());
                            details.setStoreCode(orderBaseInfo.getStoreCode());
                            detailsList.add(details);
                        }
                    }
                    return detailsList;
                }
            }
        }
        return null;
    }

    /**
     * 订单经销差价退还
     *
     * @param returnOrderBaseInfo
     * @param orderBaseInfo
     * @param goodsInfos          可传也可查，看实际情况
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deductionOrderJxPriceDifferenceRefund(ReturnOrderBaseInfo returnOrderBaseInfo, OrderBaseInfo orderBaseInfo,
                                                      List<ReturnOrderGoodsInfo> goodsInfos) {

        List<OrderJxPriceDifferenceReturnDetails> detailsList = orderService.getOrderJxPriceDifferenceReturnDetailsByOrderNumber(returnOrderBaseInfo.getOrderNo());

        List<ReturnOrderJxPriceDifferenceRefundDetails> returnDetailsList = new ArrayList<>(20);
        double jxPrice = 0.00;

        if (AssertUtil.isNotEmpty(detailsList)) {
            for (ReturnOrderGoodsInfo goodsInfo : goodsInfos) {
                for (OrderJxPriceDifferenceReturnDetails details : detailsList) {
                    if (goodsInfo.getSku().equals(details.getSku()) &&
                            AppGoodsLineType.GOODS.equals(goodsInfo.getGoodsLineType())) {
                        Double returnGoodsJxPriceAmount = CountUtil.mul(goodsInfo.getReturnQty(), details.getUnitPrice());
                        jxPrice = CountUtil.add(jxPrice, returnGoodsJxPriceAmount);
                        ReturnOrderJxPriceDifferenceRefundDetails returnDetails = new ReturnOrderJxPriceDifferenceRefundDetails();
                        returnDetails.setAmount(returnGoodsJxPriceAmount);
                        returnDetails.setCreateTime(new Date());
                        returnDetails.setRoid(returnOrderBaseInfo.getRoid());
                        returnDetails.setOrderNumber(returnOrderBaseInfo.getOrderNo());
                        returnDetails.setReturnNumber(returnOrderBaseInfo.getReturnNo());
                        returnDetails.setReturnQty(goodsInfo.getReturnQty());
                        returnDetails.setSku(details.getSku());
                        returnDetails.setStoreCode(details.getStoreCode());
                        returnDetails.setStoreId(details.getStoreId());
                        returnDetails.setUnitPrice(details.getUnitPrice());
                        returnDetails.setRefundNumber(OrderUtils.getRefundNumber());
                        returnDetailsList.add(returnDetails);
                        returnOrderService.saveReturnOrderJxPriceDifferenceRefundDetails(returnDetails);
                    }
                }
            }

            for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                StorePreDeposit preDeposit = storeService.findStorePreDepositByStoreId(returnOrderBaseInfo.getStoreId());
                if (null != preDeposit) {
                    int affectLine = storeService.updateStoreDepositByStoreIdAndStoreDeposit(
                            returnOrderBaseInfo.getStoreId(), jxPrice, preDeposit.getLastUpdateTime());
                    if (affectLine > 0) {
                        StPreDepositLogDO log = new StPreDepositLogDO();
                        log.setStoreId(preDeposit.getStoreId());
                        log.setChangeMoney(jxPrice);
                        log.setBalance(preDeposit.getBalance() - jxPrice);
                        log.setCreateTime(LocalDateTime.now());
                        log.setOrderNumber(returnOrderBaseInfo.getReturnNo());
                        log.setOperatorId(returnOrderBaseInfo.getCreatorId());
                        log.setOperatorType(returnOrderBaseInfo.getCreatorIdentityType());
                        //TODO 退单中是否加退单操作人Ip
                        log.setOperatorIp("");
                        log.setChangeType(StorePreDepositChangeType.JX_PRICE_DIFFERENCE_DEDUCTION);
                        log.setChangeTypeDesc(StorePreDepositChangeType.JX_PRICE_DIFFERENCE_DEDUCTION.getDescription());
                        storeService.addStPreDepositLog(log);
                        break;
                    } else {
                        if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                            throw new SystemBusyException("系统繁忙，请稍后再试!");
                        }
                    }
                } else {
                    throw new LockStorePreDepositException("没有找到该导购所在门店的预存款信息!");
                }
            }
        }
        /*transactionalSupportService.handleOrderJxPriceDifferenceRefundInfoAndSendToEbs(returnOrderBaseInfo, orderBaseInfo, returnDetailsList);*/
    }

    public String sendPickUpCodeAndRemindMessageAfterPayUp(OrderBaseInfo orderBaseInfo) {
        String pickUpCode = "";
        if (orderBaseInfo.getDeliveryType() == AppDeliveryType.HOUSE_DELIVERY) {
            //发送提货码给顾客
            pickUpCode = RandomUtil.randomNumCode(4);
            SmsAccount account = smsAccountService.findOne();
            String info = "【乐易装】您订单"
                    + orderBaseInfo.getOrderNumber()
                    + "的提货码为"
                    + pickUpCode
                    + "，请在“确认收货”时出示信息，在此之前请勿删除此信息。为了您的商品安全，请妥善保管提货码。";
            String content = null;
            try {
                content = URLEncoder.encode(info, "GB2312");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String mobile = null;
            if (orderBaseInfo.getCreatorIdentityType() == AppIdentityType.CUSTOMER || orderBaseInfo.getCreatorIdentityType() == AppIdentityType.DECORATE_MANAGER) {
                mobile = orderBaseInfo.getCreatorPhone();
            } else {
                mobile = orderBaseInfo.getCustomerPhone();
            }
            try {
                String returnCode = SmsUtils.sendMessageQrCode(account.getEncode(), account.getEnpass(), account.getUserName(), mobile, content);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //顾客下单发送短信给导购
            if (orderBaseInfo.getCreatorIdentityType() == AppIdentityType.CUSTOMER) {
                String tips = "【乐易装】亲爱的用户,您的会员:"
                        + orderBaseInfo.getCreatorName()
                        + "在App下单了,订单号:"
                        + orderBaseInfo.getOrderNumber() +
                        ",请及时跟进。";
                try {
                    String code = SmsUtils.sendMessageQrCode(account.getEncode(), account.getEnpass(), account.getUserName(),
                            orderBaseInfo.getSalesConsultPhone(), URLEncoder.encode(tips, "GB2312"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (orderBaseInfo.getDeliveryType() == AppDeliveryType.SELF_TAKE) {
            if (orderBaseInfo.getCreatorIdentityType() == AppIdentityType.CUSTOMER) {
                //发送提货码给顾客
                pickUpCode = RandomUtil.randomNumCode(4);
                SmsAccount account = smsAccountService.findOne();
                String info = "【乐易装】您订单"
                        + orderBaseInfo.getOrderNumber()
                        + "的提货码为"
                        + pickUpCode
                        + "，请在“到店取货”时出示信息，在此之前请勿删除此信息。为了您的商品安全，请妥善保管提货码。";
                String content = null;
                try {
                    content = URLEncoder.encode(info, "GB2312");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String mobile;
                if (orderBaseInfo.getCreatorIdentityType() == AppIdentityType.CUSTOMER || orderBaseInfo.getCreatorIdentityType() == AppIdentityType.DECORATE_MANAGER) {
                    mobile = orderBaseInfo.getCreatorPhone();
                } else {
                    mobile = orderBaseInfo.getCustomerPhone();
                }
                try {
                    String returnCode = SmsUtils.sendMessageQrCode(account.getEncode(), account.getEnpass(), account.getUserName(), mobile, content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //保存提货码
                orderBaseInfo.setPickUpCode(pickUpCode);

                String tips = "【乐易装】亲爱的用户,您的会员:"
                        + orderBaseInfo.getCreatorName()
                        + "在App下单了,订单号:"
                        + orderBaseInfo.getOrderNumber() +
                        ",请及时跟进。";
                try {
                    String code = SmsUtils.sendMessageQrCode(account.getEncode(), account.getEnpass(), account.getUserName(),
                            orderBaseInfo.getSalesConsultPhone(), URLEncoder.encode(tips, "GB2312"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return pickUpCode;
    }

    @Override
    public CreateOrderGoodsSupport createMaOrderGoodsInfo(List<MaActGoodsMapping> goodsList, AppCustomer customer, Long userId, Integer identityType, String orderNumber) {
        List<OrderGoodsInfo> orderGoodsInfoList = new ArrayList<>();
        //新建一个map,用来存放最终要检核库存的商品和商品数量
        Map<Long, Integer> inventoryCheckMap = new HashMap<>();
        //定义订单商品零售总价
        Double goodsTotalPrice = 0D;
        //定义订单会员折扣
        Double memberDiscount = 0D;
        //定义订单促销折扣
        Double promotionDiscount = 0D;
        //处理订单本品商品信息
        if (null != goodsList && goodsList.size() > 0) {
            //获取本品id集合
            Set<Long> goodsIdSet = new HashSet<>();
            for (MaActGoodsMapping goods : goodsList) {
                goodsIdSet.add(goods.getGid());
            }
            //根据本品id集合查询商品信息
            List<OrderGoodsVO> goodsVOList = goodsService.findOrderGoodsVOListByUserIdAndIdentityTypeAndGoodsIds(
                    userId, identityType, goodsIdSet);
            //定义当前有唯一价格的本品id集合
            Set<Long> hasPriceGoodsIdSet = new HashSet<>();
            //循环处理查询到的商品信息
            for (OrderGoodsVO goodsVO : goodsVOList) {
                if (hasPriceGoodsIdSet.contains(goodsVO.getGid())) {
                    throw new GoodsMultipartPriceException("商品 '" + goodsVO.getSkuName() + "'在当前门店下存在多个价格!");
                } else {
                    hasPriceGoodsIdSet.add(goodsVO.getGid());
                }
                for (MaActGoodsMapping info : goodsList) {
                    if (info.getGid().equals(goodsVO.getGid())) {
                        goodsVO.setQty(info.getQty());
                    }
                }
                //加总商品零售价总额
                goodsTotalPrice += goodsVO.getRetailPrice() * goodsVO.getQty();

                //设置本品会员折扣
                if (null == customer) {
                    throw new OrderCustomerException("订单顾客信息异常!");
                }
                if (customer.getCustomerType() == AppCustomerType.MEMBER) {
                    memberDiscount += (goodsVO.getRetailPrice() - goodsVO.getVipPrice()) * goodsVO.getQty();
                } else {
                    memberDiscount += 0D;
                }

                OrderGoodsInfo goodsInfo = new OrderGoodsInfo();
                goodsInfo.setOrderNumber(orderNumber);
                goodsInfo.setGoodsLineType(AppGoodsLineType.GOODS);
                goodsInfo.setRetailPrice(goodsVO.getRetailPrice());
                goodsInfo.setVIPPrice(goodsVO.getVipPrice());
                goodsInfo.setWholesalePrice(goodsVO.getWholesalePrice());
                goodsInfo.setIsPriceShare(Boolean.FALSE);
                goodsInfo.setPromotionSharePrice(0D);
                goodsInfo.setLbSharePrice(0D);
                goodsInfo.setCashCouponSharePrice(0D);
                goodsInfo.setCashReturnSharePrice(0D);
                goodsInfo.setIsReturnable(Boolean.TRUE);

                if (null == customer) {
                    throw new OrderCustomerException("订单顾客信息异常!");
                }
                if (customer.getCustomerType() == AppCustomerType.MEMBER) {
                    goodsInfo.setSettlementPrice(goodsVO.getVipPrice());
                    goodsInfo.setReturnPrice(goodsVO.getVipPrice());
                } else {
                    goodsInfo.setSettlementPrice(goodsVO.getRetailPrice());
                    goodsInfo.setReturnPrice(goodsVO.getRetailPrice());
                }
                goodsInfo.setGid(goodsVO.getGid());
                goodsInfo.setSku(goodsVO.getSku());
                goodsInfo.setSkuName(goodsVO.getSkuName());
                goodsInfo.setOrderQuantity(goodsVO.getQty());
                goodsInfo.setShippingQuantity(0);
                goodsInfo.setReturnableQuantity(goodsVO.getQty());
                goodsInfo.setReturnQuantity(0);
                goodsInfo.setReturnPriority(1);
                goodsInfo.setPriceItemId(goodsVO.getPriceItemId());
                goodsInfo.setCompanyFlag(goodsVO.getCompanyFlag());
                orderGoodsInfoList.add(goodsInfo);
            }

            if (goodsIdSet.size() != hasPriceGoodsIdSet.size()) {
                StringBuilder ids = new StringBuilder();
                for (Long id : goodsIdSet) {
                    if (!hasPriceGoodsIdSet.contains(id)) {
                        ids.append(id).append(",");
                    }
                }
                throw new GoodsNoPriceException("id为 '" + ids + "'的商品在当前门店下没有找到价格!");
            }
        }
        CreateOrderGoodsSupport support = new CreateOrderGoodsSupport();
        support.setGoodsTotalPrice(goodsTotalPrice);
        support.setInventoryCheckMap(inventoryCheckMap);
        support.setMemberDiscount(memberDiscount);
        support.setOrderGoodsInfoList(orderGoodsInfoList);
        support.setProductCouponGoodsList(null);
        support.setPromotionDiscount(promotionDiscount);
        return support;
    }


    @Override
    public Map<Long, Integer> createInventoryCheckMap(List<OrderGoodsInfo> orderGoodsInfoList) {

        //新建一个map,用来存放最终要检核库存的商品和商品数量
        Map<Long, Integer> inventoryCheckMap = new HashMap<>();
        for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
            if (inventoryCheckMap.containsKey(orderGoodsInfo.getGid())) {
                inventoryCheckMap.put(orderGoodsInfo.getGid(), inventoryCheckMap.get(orderGoodsInfo.getGid()) + orderGoodsInfo.getOrderQuantity());
            } else {
                inventoryCheckMap.put(orderGoodsInfo.getGid(), orderGoodsInfo.getOrderQuantity());
            }
        }
        return inventoryCheckMap;
    }

    @Override
    public Boolean checkCashDelivery(List<OrderGoodsInfo> orderGoodsInfoList, Long userId, AppIdentityType identityType) {
        logger.info("checkCashDelivery CALLED,判断是否可选择货到付款,入参:orderGoodsInfoList:{}, orderProductCouponInfoList:{}, userId:{},identityType:{}",
                JSON.toJSONString(orderGoodsInfoList), userId, identityType);
        List<Long> goodsIdList = new ArrayList<>();
        try {
            for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                goodsIdList.add(orderGoodsInfo.getGid());
            }

            if (goodsIdList != null && goodsIdList.size() > 0 && userId != null && identityType != null) {
                if (identityType.getValue() == AppIdentityType.CUSTOMER.getValue()) {
                    List<GiftListResponseGoods> goodsList = this.goodsPriceService.findGoodsPriceListByGoodsIdsAndUserId(goodsIdList, userId, identityType);
                    if (null != goodsList && goodsList.size() > 0) {
                        logger.info("checkCashDelivery OUT,判断是否可选择货到付款,出参 result:{}", Boolean.TRUE);
                        return Boolean.TRUE;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("checkCashDelivery EXCEPTION,判断是否可选择货到付款,出参 resultDTO:{}", Boolean.FALSE);
            logger.warn("{}", e);
            return Boolean.FALSE;
        }
        logger.info("checkCashDelivery OUT,判断是否可选择货到付款,出参 result:{}", Boolean.FALSE);
        return Boolean.FALSE;
    }

    @Transactional
    @Override
    public void handleOrderRelevantBusinessAfterOnlinePayCashDelivery(String orderNumber, OnlinePayType payType) throws UnsupportedEncodingException {
        if (StringUtils.isNotBlank(orderNumber)) {
            OrderBaseInfo baseInfo = orderService.getOrderByOrderNumber(orderNumber);

            //更新订单账单信息
            OrderBillingDetails billingDetails = orderService.getOrderBillingDetail(orderNumber);
            billingDetails.setOnlinePayType(payType);
            billingDetails.setCollectionAmount(billingDetails.getArrearage());
            //发送提货码给顾客,及提示导购顾客下单信息
            String pickUpCode = this.sendPickUpCodeAndRemindMessageAfterPayUp(baseInfo);
            baseInfo.setPickUpCode(pickUpCode);

            //修改顾客上一次下单时间
            Long cusId = null;
            AppCustomer customer = new AppCustomer();
            if (baseInfo.getCreatorIdentityType() != AppIdentityType.DECORATE_MANAGER) {
                if (baseInfo.getCreatorIdentityType() == AppIdentityType.SELLER) {
                    cusId = baseInfo.getCustomerId();
                } else {
                    cusId = baseInfo.getCreatorId();
                }
            }
            if (null != cusId) {
                customer = customerService.findById(cusId);
                customer.setLastConsumptionTime(new Date());
            }

            //更新顾客归属门店及导购
            updateCustomerAttachedStoreAndSeller(baseInfo, customer);

            //更新订单状态及物流状态
            if (baseInfo.getDeliveryType() == AppDeliveryType.SELF_TAKE) {
                baseInfo.setStatus(AppOrderStatus.PENDING_RECEIVE);
            } else if (baseInfo.getDeliveryType() == AppDeliveryType.HOUSE_DELIVERY) {
                baseInfo.setStatus(AppOrderStatus.PENDING_SHIPMENT);
                baseInfo.setDeliveryStatus(LogisticStatus.INITIAL);

                //物流信息初始化
                OrderDeliveryInfoDetails deliveryInfoDetails = new OrderDeliveryInfoDetails();
                deliveryInfoDetails.setDeliveryInfo(baseInfo.getOrderNumber(), LogisticStatus.INITIAL, LogisticStatus.INITIAL.getDescription(),
                        null, null, null, null, null);
                deliveryInfoDetailsService.addOrderDeliveryInfoDetails(deliveryInfoDetails);

                // ***********************发送WMS 在微信和支付宝完成支付回调方法中已发送***************************
                // 2018-04-07 generation 传wms重复，创建订单已传wms，支付时不用再传，   注释代码
//                //保存传wms配送单头档
                AppStore store = storeService.findStoreByUserIdAndIdentityType(baseInfo.getCreatorId(),
                        baseInfo.getCreatorIdentityType().getValue());
                List<OrderGoodsInfo> orderGoodsInfoList = orderService.getOrderGoodsInfoByOrderNumber(orderNumber);
                int orderGoodsSize = orderGoodsInfoList.size();
                OrderLogisticsInfo orderLogisticsInfo = orderService.getOrderLogistice(orderNumber);
                AtwRequisitionOrder requisitionOrder = AtwRequisitionOrder.transform(baseInfo, orderLogisticsInfo,
                        store, billingDetails, orderGoodsSize);
                appToWmsOrderService.saveAtwRequisitionOrder(requisitionOrder);
//                //保存传wms配送单商品信息
                if (orderGoodsSize > 0) {
                    for (OrderGoodsInfo goodsInfo : orderGoodsInfoList) {
                        AtwRequisitionOrderGoods requisitionOrderGoods = AtwRequisitionOrderGoods.transform(goodsInfo.getOrderNumber(),
                                goodsInfo.getSku(), goodsInfo.getSkuName(), goodsInfo.getRetailPrice(), goodsInfo.getOrderQuantity(), goodsInfo.getCompanyFlag());
                        appToWmsOrderService.saveAtwRequisitionOrderGoods(requisitionOrderGoods);
                    }
                }

            }

            //更新订单基础信息
            orderService.updateOrderBaseInfo(baseInfo);
            //更新订单账单信息
            orderService.updateOrderBillingDetails(billingDetails);
            //更新顾客信息
            if (customer.getCusId() != null) {
                customerService.update(customer);
            }
            //增加订单生命周期信息
            orderService.addOrderLifecycle(OrderLifecycleType.PAYED, orderNumber);
        }
    }

    /**
     * 支付 0 元订单
     *
     * @param orderNumber
     * @throws UnsupportedEncodingException
     */
    public void payZeroOrder(String orderNumber) throws UnsupportedEncodingException {
        if (StringUtils.isNotBlank(orderNumber)) {
            OrderBaseInfo baseInfo = orderService.getOrderByOrderNumber(orderNumber);

            if (baseInfo != null) {
                //更新订单账单信息
                OrderBillingDetails billingDetails = orderService.getOrderBillingDetail(orderNumber);
                billingDetails.setIsPayUp(true);
                billingDetails.setPayUpTime(new Date());

                //发送提货码给顾客,及提示导购顾客下单信息
                String pickUpCode = this.sendPickUpCodeAndRemindMessageAfterPayUp(baseInfo);
                baseInfo.setPickUpCode(pickUpCode);

                //修改顾客上一次下单时间
                Long cusId = null;
                AppCustomer customer = new AppCustomer();
                if (baseInfo.getCreatorIdentityType() != AppIdentityType.DECORATE_MANAGER) {
                    if (baseInfo.getCreatorIdentityType() == AppIdentityType.SELLER) {
                        cusId = baseInfo.getCustomerId();
                    } else {
                        cusId = baseInfo.getCreatorId();
                    }
                }
                if (null != cusId) {
                    customer = customerService.findById(cusId);
                    customer.setLastConsumptionTime(new Date());
                }

                //更新顾客归属门店及导购
                updateCustomerAttachedStoreAndSeller(baseInfo, customer);

                //更新订单状态及物流状态
                if (baseInfo.getDeliveryType() == AppDeliveryType.SELF_TAKE) {
                    baseInfo.setStatus(AppOrderStatus.PENDING_RECEIVE);
                } else if (baseInfo.getDeliveryType() == AppDeliveryType.HOUSE_DELIVERY) {
                    baseInfo.setStatus(AppOrderStatus.PENDING_SHIPMENT);
                    baseInfo.setDeliveryStatus(LogisticStatus.INITIAL);

                    //物流信息初始化
                    OrderDeliveryInfoDetails deliveryInfoDetails = new OrderDeliveryInfoDetails();
                    deliveryInfoDetails.setDeliveryInfo(baseInfo.getOrderNumber(), LogisticStatus.INITIAL, LogisticStatus.INITIAL.getDescription(),
                            null, null, null, null, null);
                    deliveryInfoDetailsService.addOrderDeliveryInfoDetails(deliveryInfoDetails);

                    // ***********************发送WMS 在微信和支付宝完成支付回调方法中已发送***************************
                    //保存传wms配送单头档
                    AppStore store = storeService.findStoreByUserIdAndIdentityType(baseInfo.getCreatorId(),
                            baseInfo.getCreatorIdentityType().getValue());
                    List<OrderGoodsInfo> orderGoodsInfoList = orderService.getOrderGoodsInfoByOrderNumber(orderNumber);
                    int orderGoodsSize = orderGoodsInfoList.size();
                    OrderLogisticsInfo orderLogisticsInfo = orderService.getOrderLogistice(orderNumber);
                    AtwRequisitionOrder requisitionOrder = AtwRequisitionOrder.transform(baseInfo, orderLogisticsInfo,
                            store, billingDetails, orderGoodsSize);
                    appToWmsOrderService.saveAtwRequisitionOrder(requisitionOrder);
                    //保存传wms配送单商品信息
                    if (orderGoodsSize > 0) {
                        for (OrderGoodsInfo goodsInfo : orderGoodsInfoList) {
                            AtwRequisitionOrderGoods requisitionOrderGoods = AtwRequisitionOrderGoods.transform(goodsInfo.getOrderNumber(),
                                    goodsInfo.getSku(), goodsInfo.getSkuName(), goodsInfo.getRetailPrice(), goodsInfo.getOrderQuantity(), goodsInfo.getCompanyFlag());
                            appToWmsOrderService.saveAtwRequisitionOrderGoods(requisitionOrderGoods);
                        }
                    }

                }

                //更新订单基础信息
                orderService.updateOrderBaseInfo(baseInfo);
                //更新订单账单信息
                orderService.updateOrderBillingDetails(billingDetails);
                //更新顾客信息
                if (customer.getCusId() != null) {
                    customerService.update(customer);
                }
                //增加订单生命周期信息
                orderService.addOrderLifecycle(OrderLifecycleType.PAYED, orderNumber);
            }
        }
    }

    @Transactional
    @Override
    public void confirmOrderArrive(OrderBillingPaymentDetails paymentDetails,
                                   OrderBillingDetails orderBillingDetails, EmpCreditMoneyChangeLog empCreditMoneyChangeLog,
                                   OrderAgencyFundDO orderAgencyFundDO, OrderDeliveryInfoDetails orderDeliveryInfoDetails,
                                   OrderBaseInfo orderBaseInfo, Long sellerId, Double credit, Timestamp lastUpdateTime) {
        if (null != paymentDetails) {
            this.orderService.savePaymentDetails(paymentDetails);
        }
        if (null != orderBillingDetails) {
            this.orderService.updateOwnMoneyByOrderNo(orderBillingDetails);
        }
        if (credit > 0D) {
            //修改导购信用额度
            Integer affectLine = this.employeeService.unlockGuideCreditByUserIdAndGuideCreditAndVersion(sellerId, credit, lastUpdateTime);
            if (affectLine > 0 && null != empCreditMoneyChangeLog) {
                this.employeeService.addEmpCreditMoneyChangeLog(empCreditMoneyChangeLog);
            }
        }
        if (null != orderAgencyFundDO) {
            this.orderAgencyFundServiceImpl.save(orderAgencyFundDO);
        }
        if (null != orderDeliveryInfoDetails) {
            this.deliveryInfoDetailsService.addOrderDeliveryInfoDetails(orderDeliveryInfoDetails);
        }
        if (null != orderBaseInfo) {
            this.orderService.updateOrderStatusByOrderNo(orderBaseInfo);
        }
    }

    @Transactional
    @Override
    public void sellerAudit(OrderAgencyFundDO orderAgencyFundDO, OrderBillingPaymentDetails paymentDetails, OrderBillingDetails orderBillingDetails,
                            EmpCreditMoneyChangeLog empCreditMoneyChangeLog, OrderDeliveryInfoDetails orderDeliveryInfoDetails,
                            OrderBaseInfo orderBaseInfo, OrderArrearsAuditDO orderArrearsAuditDO, Long sellerId,
                            Double collectionAmount, Timestamp lastUpdateTime) {

        if (null != orderAgencyFundDO) {
            this.orderAgencyFundServiceImpl.save(orderAgencyFundDO);
        }
        if (null != paymentDetails) {
            this.orderService.savePaymentDetails(paymentDetails);
        }
        if (null != orderBillingDetails) {
            this.orderService.updateOwnMoneyByOrderNo(orderBillingDetails);
        }
        if (collectionAmount > 0D) {
            //修改导购信用额度
            Integer affectLine = this.employeeService.unlockGuideCreditByUserIdAndGuideCreditAndVersion(sellerId, collectionAmount, lastUpdateTime);
            if (affectLine > 0 && null != empCreditMoneyChangeLog) {
                this.employeeService.addEmpCreditMoneyChangeLog(empCreditMoneyChangeLog);
            }
        }


        if (null != orderDeliveryInfoDetails) {
            this.deliveryInfoDetailsService.addOrderDeliveryInfoDetails(orderDeliveryInfoDetails);
        }
        if (null != orderBaseInfo) {
            this.orderService.updateOrderStatusByOrderNo(orderBaseInfo);
        }
        if (null != orderArrearsAuditDO) {
            this.arrearsAuditServiceImpl.updateStatusById(orderArrearsAuditDO);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void originalCustomerRegistry(AppCustomer phoneUser) throws UnsupportedEncodingException {
        if (null != phoneUser) {
            customerService.update(phoneUser);
            CustomerLeBi customerLeBi = customerService.findCustomerLebiByCustomerId(phoneUser.getCusId());
            if (null == customerLeBi) {
                customerService.createCustomerLeBiAccount(phoneUser.getCusId());
            }
            CustomerPreDeposit customerPreDeposit = customerService.findCustomerPreDepositByCustomerId(phoneUser.getCusId());
            if (null == customerPreDeposit) {
                customerService.createCustomerPreDepositAccount(phoneUser.getCusId());
            }
        }
    }

    @Override
    public void saveOrderRelevantInfo(OrderBaseInfo orderBaseInfo, OrderLogisticsInfo orderLogisticsInfo, List<OrderGoodsInfo> orderGoodsInfoList, OrderBillingDetails orderBillingDetails) throws UnsupportedEncodingException {
        if (null != orderBaseInfo) {
            if (null != orderBillingDetails && orderBillingDetails.getAmountPayable() <= AppConstant.PAY_UP_LIMIT) {
                orderBillingDetails.setPayUpTime(new Date());
                //发送提货码给顾客,及提示导购顾客下单信息
                String pickUpCode = this.sendPickUpCodeAndRemindMessageAfterPayUp(orderBaseInfo);
                orderBaseInfo.setPickUpCode(pickUpCode);

                //修改顾客上一次下单时间
                AppCustomer customer = new AppCustomer();
                Long cusId = null;
                if (orderBaseInfo.getCreatorIdentityType() != AppIdentityType.DECORATE_MANAGER) {
                    if (orderBaseInfo.getCreatorIdentityType() == AppIdentityType.SELLER) {
                        cusId = orderBaseInfo.getCustomerId();
                    } else {
                        cusId = orderBaseInfo.getCreatorId();
                    }
                    customer = customerService.findById(cusId);
                    customer.setLastConsumptionTime(new Date());
                }
                // 更新顾客归属门店及导购
                updateCustomerAttachedStoreAndSeller(orderBaseInfo, customer);

                //更新顾客信息
                if (null != customer.getCusId()) {
                    customerService.update(customer);
                }

                //TODO 返还经销差价

            }
            /* ******************* 保存订单相关一系列信息 ******************* */
            //保存订单基础信息
            orderService.saveOrderBaseInfo(orderBaseInfo);

            if (null != orderBaseInfo.getId()) {
                //保存订单物流信息
                if (null != orderLogisticsInfo) {
                    orderLogisticsInfo.setOid(orderBaseInfo.getId());
                    orderService.saveOrderLogisticsInfo(orderLogisticsInfo);
                }
                //保存订单商品信息
                if (null != orderGoodsInfoList && orderGoodsInfoList.size() > 0) {
                    for (OrderGoodsInfo goodsInfo : orderGoodsInfoList) {
                        goodsInfo.setOid(orderBaseInfo.getId());
                        orderService.saveOrderGoodsInfo(goodsInfo);
                    }
                }
                //保存订单账单信息
                if (null != orderBillingDetails) {
                    orderBillingDetails.setOid(orderBaseInfo.getId());
                    orderService.saveOrderBillingDetails(orderBillingDetails);
                }
            } else {
                throw new OrderSaveException("订单主键生成失败!");
            }
        }
    }

}

