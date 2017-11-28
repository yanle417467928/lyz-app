package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.utils.BeanUtils;
import cn.com.leyizhuang.app.core.utils.csrf.EncryptUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.management.User;
import cn.com.leyizhuang.app.foundation.pojo.management.UserRole;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderLogisticsInfo;
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
    public OrderBaseInfo createOrderBaseInfo(Long cityId, Long userId, Integer identityType, Long customerId, String deliveryType) {
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
        if (deliverySimpleInfo.getDeliveryType().equalsIgnoreCase(AppDeliveryType.SELF_TAKE.getValue())){
            logisticsInfo.setDeliveryType(AppDeliveryType.SELF_TAKE);
            AppStore bookingStore = storeService.findById(deliverySimpleInfo.getBookingStoreId());
            if (null != bookingStore){
                logisticsInfo.setBookingStoreCode(bookingStore.getStoreCode());
                logisticsInfo.setBookingStoreName(bookingStore.getStoreName());
                logisticsInfo.setBookingStoreAddress(bookingStore.getDetailedAddress());
            }else{
                throw new RuntimeException("未找到该门店!");
            }
        }else if (deliverySimpleInfo.getDeliveryType().equalsIgnoreCase(AppDeliveryType.HOUSE_DELIVERY.getValue())){
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
}

