package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.OnlinePayType;
import cn.com.leyizhuang.app.core.exception.*;
import cn.com.leyizhuang.app.foundation.pojo.EmpCreditMoneyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.management.User;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaActGoodsMapping;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.DeliverySimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.GoodsSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.ProductCouponSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.PromotionSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.vo.UserVO;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 通用方法
 *
 * @author Richard
 * Created on 2017-09-12 15:42
 **/
public interface CommonService {

    User saveUserAndUserRoleByUserVO(UserVO userVO);

    User updateUserAndUserRoleByUserVO(UserVO userVO);

    void deleteUserAndUserRoleByUserId(Long uid);

    AppCustomer saveCustomerInfo(AppCustomer customer, CustomerLeBi leBi, CustomerPreDeposit preDeposit) throws UnsupportedEncodingException;

    void customerSign(Long userId, Integer identityType) throws UnsupportedEncodingException;


    void saveAndUpdateMaterialList(List<MaterialListDO> materialListSave, List<MaterialListDO> materialListUpdate);

    void reduceInventoryAndMoney(DeliverySimpleInfo deliverySimpleInfo, Map<Long, Integer> inventoryCheckMap, Long cityId, Integer identityType,
                                 Long userId, Long customerId, List<Long> cashCouponIds, List<OrderCouponInfo> productCouponList, OrderBillingDetails billingDetails, String orderNumber, String ipAddress) throws LockStoreInventoryException, LockCityInventoryException, LockCustomerCashCouponException,
            LockCustomerLebiException, LockCustomerPreDepositException, LockStorePreDepositException, LockEmpCreditMoneyException, LockStoreCreditMoneyException,
            LockStoreSubventionException, SystemBusyException;

    void saveAndHandleOrderRelevantInfo(OrderBaseInfo orderBaseInfo, OrderLogisticsInfo orderLogisticsInfo, List<OrderGoodsInfo> orderGoodsInfoList,
                                        List<OrderCouponInfo> orderCouponInfoList, OrderBillingDetails orderBillingDetails,
                                        List<OrderBillingPaymentDetails> paymentDetails, List<OrderJxPriceDifferenceReturnDetails> jxPriceDifferenceReturnDetails)
            throws OrderSaveException, UnsupportedEncodingException;

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
                                                 List<ProductCouponSimpleInfo> productCouponList, String orderNumber) throws UnsupportedEncodingException;

    List<OrderBillingPaymentDetails> createOrderBillingPaymentDetails(OrderBaseInfo orderBaseInfo, OrderBillingDetails orderBillingDetails);

    List<OrderJxPriceDifferenceReturnDetails> createOrderJxPriceDifferenceReturnDetails(OrderBaseInfo orderBaseInfo, List<OrderGoodsInfo> orderGoodsInfoList);

    List<OrderJxPriceDifferenceReturnDetails> createOrderJxPriceDifferenceReturnDetails(OrderBaseInfo orderBaseInfo, List<OrderGoodsInfo> orderGoodsInfoList, List<PromotionSimpleInfo> promotionSimpleInfos);

    void deductionOrderJxPriceDifferenceRefund(ReturnOrderBaseInfo returnOrderBaseInfo, OrderBaseInfo orderBaseInfo, List<ReturnOrderGoodsInfo> goodsInfos);

    /**
     * 后台买券订单创建商品信息
     *
     * @param goodsList
     * @param customer
     * @param userId
     * @param identityType
     * @param orderNumber
     * @return
     */
    CreateOrderGoodsSupport createMaOrderGoodsInfo(List<MaActGoodsMapping> goodsList, AppCustomer customer, Long userId, Integer identityType, String orderNumber);

    /**
     * 创建订单需检核的库存列表
     *
     * @param orderGoodsInfoList 订单商品列表
     * @return 需检核的库存列表
     */
    public Map<Long, Integer> createInventoryCheckMap(List<OrderGoodsInfo> orderGoodsInfoList);

    Boolean checkCashDelivery(List<OrderGoodsInfo> orderGoodsInfoList, Long userId, AppIdentityType identityType);

    void handleOrderRelevantBusinessAfterOnlinePayCashDelivery(String orderNumber, OnlinePayType payType) throws UnsupportedEncodingException;

    void payZeroOrder(String orderNumber) throws UnsupportedEncodingException;

    void confirmOrderArrive(OrderBillingPaymentDetails paymentDetails, OrderBillingDetails orderBillingDetails,
                            EmpCreditMoneyChangeLog empCreditMoneyChangeLog, OrderAgencyFundDO orderAgencyFundDO, OrderDeliveryInfoDetails orderDeliveryInfoDetails,
                            OrderBaseInfo orderBaseInfo, Long sellerId, Double credit, Timestamp lastUpdateTime);

    void sellerAudit(OrderAgencyFundDO orderAgencyFundDO, OrderBillingPaymentDetails paymentDetails, OrderBillingDetails orderBillingDetails,
                     EmpCreditMoneyChangeLog empCreditMoneyChangeLog, OrderDeliveryInfoDetails orderDeliveryInfoDetails,
                     OrderBaseInfo orderBaseInfo, OrderArrearsAuditDO orderArrearsAuditDO, Long sellerId,
                     Double collectionAmount, Timestamp lastUpdateTime);

    void originalCustomerRegistry(AppCustomer phoneUser) throws UnsupportedEncodingException;

    void saveOrderRelevantInfo(OrderBaseInfo orderBaseInfo, OrderLogisticsInfo orderLogisticsInfo,List<OrderGoodsInfo> orderGoodsInfoList,
                               OrderBillingDetails orderBillingDetails) throws UnsupportedEncodingException;

    List<OrderGoodsInfo> addGoodsSign(List<OrderGoodsInfo> orderGoodsInfoList, OrderBaseInfo orderBaseInfo);
    void handleOrderRelevantBusinessAfterPayForAnother(String orderNumber, Long userId, Integer identityType, String payType, String ipAddress) throws IOException;

}
