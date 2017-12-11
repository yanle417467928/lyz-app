package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.core.exception.*;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.DeliverySimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.vo.UserVO;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * 通用方法
 *
 * @author Richard
 * Created on 2017-09-12 15:42
 **/
public interface CommonService {

    void saveUserAndUserRoleByUserVO(UserVO userVO);

    void updateUserAndUserRoleByUserVO(UserVO userVO);

    void deleteUserAndUserRoleByUserId(Long uid);

    AppCustomer saveCustomerInfo(AppCustomer customer, CustomerLeBi leBi, CustomerPreDeposit preDeposit);

    void updateCustomerSignTimeAndCustomerLeBiByUserId(Long userId, Integer identityType);


    void saveAndUpdateMaterialList(List<MaterialListDO> materialListSave, List<MaterialListDO> materialListUpdate);

    void reduceInventoryAndMoney(DeliverySimpleInfo deliverySimpleInfo, Map<Long, Integer> inventoryCheckMap, Long cityId, Integer identityType,
                                 Long userId, Long customerId, List<Long> cashCouponIds, OrderBillingDetails billingDetails, String orderNumber, String ipAddress) throws LockStoreInventoryException, LockCityInventoryException, LockCustomerCashCouponException,
            LockCustomerLebiException, LockCustomerPreDepositException, LockStorePreDepositException, LockEmpCreditMoneyException, LockStoreCreditMoneyException,
            LockStoreSubventionException, SystemBusyException;

    void saveAndHandleOrderRelevantInfo(OrderBaseInfo orderBaseInfo, OrderLogisticsInfo orderLogisticsInfo, List<OrderGoodsInfo> orderGoodsInfoList,
                               OrderBillingDetails orderBillingDetails, List<OrderBillingPaymentDetails> paymentDetails)
            throws OrderSaveException, IOException;

   /* *//**
     * 订单付清后执行的相关操作
     * @param orderNumber 订单号
     *//*
    void handleOrderRelevantBusinessAfterPrePayUp(String orderNumber) throws IOException;*/
}
