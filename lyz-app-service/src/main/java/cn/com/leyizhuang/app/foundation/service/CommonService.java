package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.core.constant.OnlinePayType;
import cn.com.leyizhuang.app.core.exception.*;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.DeliverySimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.GoodsSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.ProductCouponSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.vo.UserVO;

import java.io.IOException;
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

    void customerSign(Long userId, Integer identityType);


    void saveAndUpdateMaterialList(List<MaterialListDO> materialListSave, List<MaterialListDO> materialListUpdate);

    void reduceInventoryAndMoney(DeliverySimpleInfo deliverySimpleInfo, Map<Long, Integer> inventoryCheckMap, Long cityId, Integer identityType,
                                 Long userId, Long customerId, List<Long> cashCouponIds, List<OrderCouponInfo> productCouponList, OrderBillingDetails billingDetails, String orderNumber, String ipAddress) throws LockStoreInventoryException, LockCityInventoryException, LockCustomerCashCouponException,
            LockCustomerLebiException, LockCustomerPreDepositException, LockStorePreDepositException, LockEmpCreditMoneyException, LockStoreCreditMoneyException,
            LockStoreSubventionException, SystemBusyException;

    void saveAndHandleOrderRelevantInfo(OrderBaseInfo orderBaseInfo, OrderLogisticsInfo orderLogisticsInfo, List<OrderGoodsInfo> orderGoodsInfoList,
                                        List<OrderCouponInfo> orderCouponInfoList, OrderBillingDetails orderBillingDetails, List<OrderBillingPaymentDetails> paymentDetails)
            throws OrderSaveException;

    /**
     * 订单付清后执行的相关操作
     *
     * @param orderNumber 订单号
     */
    void handleOrderRelevantBusinessAfterOnlinePayUp(String orderNumber, String tradeNo, String tradeStatus, OnlinePayType onlinePayType) throws IOException;

    void clearOrderGoodsInMaterialList(Long userId, Integer identityType, List<GoodsSimpleInfo> goodsList, List<ProductCouponSimpleInfo> productCouponList);

    List<OrderCouponInfo> createOrderCashCouponInfo(OrderBaseInfo orderBaseInfo, List<Long> cashCouponList);

    List<OrderCouponInfo> createOrderProductCouponInfo(OrderBaseInfo orderBaseInfo, List<OrderGoodsInfo> productCouponList);

    CreateOrderGoodsSupport createOrderGoodsInfo(List<GoodsSimpleInfo> goodsList, Long userId, Integer identityType, Long customerId,
                                                 List<ProductCouponSimpleInfo> productCouponList, String orderNumber);

    List<OrderBillingPaymentDetails> createOrderBillingPaymentDetails(OrderBaseInfo orderBaseInfo, OrderBillingDetails orderBillingDetails);
}
