package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.management.guide.GuideCreditChangeDetail;
import cn.com.leyizhuang.app.foundation.pojo.management.order.*;
import cn.com.leyizhuang.app.foundation.pojo.management.webservice.ebs.MaOrderReceiveInf;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeOrder;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeReceiptInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaCompanyOrderVORequest;
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaOrderVORequest;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.GoodsSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.MaGoodsSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.ProductCouponSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.PromotionSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.vo.DetailFitOrderVO;
import cn.com.leyizhuang.app.foundation.vo.FitOrderVO;
import cn.com.leyizhuang.app.foundation.vo.MaOrderVO;
import cn.com.leyizhuang.app.foundation.vo.OrderGoodsVO;
import cn.com.leyizhuang.app.foundation.vo.management.goodscategory.MaOrderGoodsDetailResponse;
import cn.com.leyizhuang.app.foundation.vo.management.order.*;
import com.github.pagehelper.PageInfo;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by caiyu on 2017/12/16.
 */
public interface MaOrderService {
    /**
     * 后台查看所有订单
     *
     * @return 订单列表
     */
    List<MaOrderVO> findMaOrderVOAll(List<Long> storeIds);

    /**
     * 分页查看城市订单
     *
     * @param cityId 城市id
     * @return 订单列表
     */
    List<MaOrderVO> findMaOrderVOByCityId(Long cityId,List<Long> storeIds);

    /**
     * 分页查看门店订单
     *
     * @param storeId 门店id
     * @return 订单列表
     */
    List<MaOrderVO> findMaOrderVOByStoreId(Long storeId);

    /**
     * 根据门店、城市、配送方式查看订单列表
     *
     * @param deliveryType 配送方式
     * @param cityId       城市id
     * @param storeId      门店id
     * @return 订单列表
     */
    List<MaOrderVO> findMaOrderVOByStoreIdAndCityIdAndDeliveryType(String deliveryType, Long cityId, Long storeId);

    /**
     * 根据订单号模糊查询订单
     *
     * @param orderNumber 订单号
     * @return 订单列表
     */
    List<MaOrderVO> findMaOrderVOByOrderNumber(String orderNumber);

    /**
     * 多条件查询订单列表
     *
     * @param maOrderVORequest 查询条件参数类
     * @return 订单列表
     */
    List<MaOrderVO> findMaOrderVOByCondition(MaOrderVORequest maOrderVORequest,List<Long> storeIds);

    /**
     * 分页查看装饰公司所有订单列表
     *
     * @return 订单列表
     */
    List<MaOrderVO> findCompanyOrderAll(List<Long> storeIds,String company);

    /**
     * 根据订单号模糊查询装饰公司订单
     *
     * @param orderNumber 订单号
     * @return 订单列表
     */
    List<MaOrderVO> findCompanyOrderByOrderNumber(String orderNumber);

    /**
     * 多条件查询装饰公司订单列表
     *
     * @param maCompanyOrderVORequest 查询条件参数类
     * @return 订单列表
     */
    List<MaOrderVO> findCompanyOrderByCondition(MaCompanyOrderVORequest maCompanyOrderVORequest);

    /**
     * 获取待发货订单列表
     *
     * @return 订单列表
     */
    List<MaOrderVO> findPendingShipmentOrder();

    /**
     * 多条件查询待发货订单列表
     *
     * @param maCompanyOrderVORequest 查询条件参数
     * @return 订单列表
     */
    List<MaOrderVO> findPendingShipmentOrderByCondition(MaCompanyOrderVORequest maCompanyOrderVORequest);

    /**
     * 根据订单号查询待发货订单
     *
     * @param orderNumber 订单号
     * @return 订单列表
     */
    List<MaOrderVO> findPendingShipmentOrderByOrderNumber(String orderNumber);

    /**
     * 后台根据订单号查看门店订单详情
     *
     * @param orderNmber 订单号
     * @return 门店订单详情
     */
    MaOrderDetailResponse findMaOrderDetailByOrderNumber(String orderNmber);

    /**
     * 后台根据订单号查看装饰公司订单详情
     *
     * @param orderNmber 订单号
     * @return 装饰公司订单详情
     */
    MaCompanyOrderDetailResponse findMaCompanyOrderDetailByOrderNumber(String orderNmber);

    /**
     * 后台根据订单号查看订单账单明细
     *
     * @param orderNmber 订单号
     * @return 订单账单明细
     */
    MaOrderBillingDetailResponse getMaOrderBillingDetailByOrderNumber(String orderNmber);

    /**
     * 后台根据订单号查看支付明细
     *
     * @param orderNmber 订单号
     * @return 支付明细列表
     */
    List<MaOrderBillingPaymentDetailResponse> getMaOrderBillingPaymentDetailByOrderNumber(String orderNmber);

    /**
     * 获取欠款应付金额
     *
     * @param orderNmber 订单号
     * @return
     */
    Double queryRepaymentAmount(String orderNmber);

    /**
     * 后台根据订单号获取物流详情
     *
     * @param orderNmber 订单号
     * @return 物流详情
     */
    MaOrderDeliveryInfoResponse getDeliveryInfoByOrderNumber(String orderNmber);


    PageInfo<MaSelfTakeOrderVO> findSelfTakeOrderList(Integer page, Integer size, List<Long> storeIds);

    PageInfo<MaSelfTakeOrderVO> findSelfTakeOrderListByScreen(Integer page, Integer size, Long cityId, Long storeId, Integer status, Integer isPayUp,List<Long> storeIds);

    PageInfo<MaSelfTakeOrderVO> findSelfTakeOrderListByInfo(Integer page, Integer size, String info, List<Long> storeIds);

    PageInfo<MaSelfTakeOrderVO> findSelfTakeOrderByCondition(Integer page, Integer size, MaOrderVORequest maOrderVORequest, List<Long> storeIds);

    MaOrderTempInfo getOrderInfoByOrderNo(String orderNo);

    void orderShipping(String orderNumber, ShiroUser shiroUser, MaOrderTempInfo maOrderTempInfo) throws Exception;

    void saveOrderShipping(OrderShipping orderShipping);


    List<String> orderReceivables(MaOrderAmount maOrderAmount, MaOrderBillingDetailResponse maOrderBillingDetailResponse, GuideCreditChangeDetail guideCreditChangeDetail);

    /**
     * 更新订单基础表订单状态
     *
     * @param orderNumber 订单号
     * @return
     */
    void updateOrderStatus(String orderNumber,String status);

    /**
     * 更新订单费用表订单状态
     *
     * @param  maOrderAmount
     * @return
     */
    void updateOrderReceivablesStatus(MaOrderAmount maOrderAmount);

    List<MaOrderGoodsInfo> findOrderGoodsList(String orderNumber,Long storeId);

    Boolean judgmentVerification(String code, String orderNumber);

    /**
     * 后台判断订单是否已收款
     *
     * @param orderNumber 订单号
     * @return
     */
    Boolean isPayUp(String orderNumber);


    String getShippingTime(String orderNumber);


    /**
     * 新增订单收款记录
     *
     * @param maOrderBillingPaymentDetails 订单号
     * @return
     */
    void saveOrderBillingPaymentDetails(MaOrderBillingPaymentDetails maOrderBillingPaymentDetails);

    /**
     * 新增自提单接口表数据
     *
     * @param maOrderReceiveInf
     * @return
     */
    void saveAppToEbsOrderReceiveInf(MaOrderReceiveInf maOrderReceiveInf);

    /**
     * 根据查询自提单接口表数据
     */
    MaOrderReceiveInf queryOrderReceiveInf(String orderNumber);

    /**
     * 发送自提单接口表数据到ebs
     */
    void sendOrderReceiveInfAndRecord(String orderNumber);


    PageInfo<MaAgencyAndArrearsOrderVO> findArrearsAndAgencyOrderList(Integer page, Integer size, List<Long> storeIds);


    PageInfo<MaAgencyAndArrearsOrderVO> findMaAgencyAndArrearsOrderListByScreen(Integer page, Integer size, Long cityId, Long storeId, Integer status, Integer isPayUp,List<Long> storeIds);


    PageInfo<MaAgencyAndArrearsOrderVO> findMaAgencyAndArrearsOrderListByInfo(Integer page, Integer size, String info, List<Long> storeIds);

    /**
     * 得到订单商品详情
     */
    List<MaOrderGoodsDetailResponse> getOrderGoodsDetailResponseList(String orderNumber);

    /**
     * 审核订单
     */
    String auditOrderStatus(String orderNumber, String status,Long auditId);

    /**
     * 通过订单id得到销售id
     */
    Long querySellerIdByOrderNumber(String orderNumber);


    List<String> arrearsOrderRepayment(MaOrderAmount maOrderAmount,MaOrderBillingDetailResponse maOrderBillingDetailResponses ,GuideCreditChangeDetail guideCreditChangeDetail);

    List<String> selfTakeOrderReceivables(MaOrderAmount maOrderAmount,MaOrderBillingDetailResponse maOrderBillingDetailResponse,GuideCreditChangeDetail guideCreditChangeDetail);

    /**
     * 后台买券订单创建订单账单信息
     *
     * @param orderBillingDetails
     * @param preDeposit
     * @param cash
     * @param posMoney
     * @param otherMoney
     * @param posNumber
     * @param payTime
     * @return
     */
    OrderBillingDetails createMaOrderBillingDetails(OrderBillingDetails orderBillingDetails, Double preDeposit, Double cash, Double posMoney, Double otherMoney, String posNumber, String payTime);

    /**
     * 后台买券订单创建支付明细信息
     *
     * @param orderBaseInfo
     * @param orderBillingDetails
     * @return
     */
   Map<Object,Object> createMaOrderBillingPaymentDetails(OrderBaseInfo orderBaseInfo, OrderBillingDetails orderBillingDetails,AppStore store,AppCustomer customer,Long creatorId);

    /**
     * 后台买券订单扣减门店预存款
     *
     * @param identityType
     * @param userId
     * @param billingDetails
     * @param orderNumber
     * @param ipAddress
     */
    void deductionsStPreDeposit(Integer identityType, Long userId, OrderBillingDetails billingDetails, String orderNumber, String ipAddress);

    /**
     * 后台买券订单保存相关实体信息
     *
     * @param orderBaseInfo
     * @param orderGoodsInfoList
     * @param orderBillingDetails
     * @param paymentDetails
     */
    void saveAndHandleMaOrderRelevantInfo(OrderBaseInfo orderBaseInfo, List<OrderGoodsInfo> orderGoodsInfoList,
                                          OrderBillingDetails orderBillingDetails, List<OrderBillingPaymentDetails> paymentDetails,
                                          OrderLogisticsInfo orderLogisticsInfo,Long operatorId, List<RechargeReceiptInfo> rechargeReceiptInfoList,
                                          List<RechargeOrder> rechargeOrderList,List<PromotionSimpleInfo> promotionSimpleInfos) throws UnsupportedEncodingException;

    /**
     * 后台买券订单持久化调用方法
     *
     * @param identityType
     * @param userId
     * @param orderBillingDetails
     * @param orderBaseInfo
     * @param orderGoodsInfoList
     * @param paymentDetails
     * @param ipAddress
     */
    void createMaOrderBusiness(Integer identityType, Long userId, OrderBillingDetails orderBillingDetails, OrderBaseInfo orderBaseInfo,
                               List<OrderGoodsInfo> orderGoodsInfoList, List<OrderBillingPaymentDetails> paymentDetails, String ipAddress,
                               OrderLogisticsInfo orderLogisticsInfo, Long operatorId, List<RechargeReceiptInfo> rechargeReceiptInfoList,
                               List<RechargeOrder> rechargeOrderList, List<PromotionSimpleInfo> promotionSimpleInfoList) throws UnsupportedEncodingException;

    /**
     * 后台买券订单创建订单基础信息
     *
     * @param appCustomer
     * @param city
     * @param appStore
     * @param appEmployee
     * @param preDepositMoney
     * @param remarks
     * @param preDepositRemarks
     * @param totalMoney
     * @return
     */
    OrderBaseInfo createMaOrderBaseInfo(AppCustomer appCustomer, City city, AppStore appStore, AppEmployee appEmployee,
                                        Double preDepositMoney, String remarks, String preDepositRemarks, Double totalMoney, String orderNumber, String salesNumber);

    /**
     * 查询当天所有待付款订单
     */
    void findScanningUnpaidOrder();

    String scanningUnpaidOrder(OrderBaseInfo orderBaseInfo);

    /**
     * 查询该订单的支付信息
     *
     * @param orderNumber
     * @return
     */
    List<MaPaymentData> findPaymentDataByOrderNo(String orderNumber);

    OrderLogisticsInfo createMaOrderLogisticsInfo(AppStore appStore, String orderNumber);


    /**
     * 查询订单审核信息
     *
     * @param orderNumber
     * @return
     */
    MaOrderArrearsAudit getArrearsAuditInfo(String orderNumber);

    /**
     * 查询订单审核信息
     *
     * @param id
     * @return
     */
    MaOrderArrearsAudit getArrearsAuditInfoById(Long id);


    /**
     * 更新欠款审核表
     * @param orderNumber
     * @param orderNumber
     * @return
     */
    void updateOrderArrearsAudit(String orderNumber,Date date);

    /**
     * 定时查找待付款订单
     * @return
     */
    List<OrderBaseInfo> scanningUnpaidOrder(String findDate);

    PageInfo<MaOrderVO> findMaOrderVOPageInfo(Integer page, Integer size, List<Long> storeIds);

    /**
     * 查询装饰公司订单`
     * @return
     */
    PageInfo<FitOrderVO> findFitOrderVOPageInfo(Integer page, Integer size, List<Long> storeIds);

    /**
     * 筛选装饰公司订单`
     * @return
     */
    PageInfo<FitOrderVO> findFitOrderListByScreen(Integer page, Integer size,Long cityId,Long storeId, List<Long> storeIds);
    /**
     * 筛选装饰公司订单`
     * @return
     */
    PageInfo<FitOrderVO> findFitOrderListByInfo(Integer page, Integer size, String info, List<Long> storeIds);

    /**
     * 据条件信息筛选装饰公司订单`
     * @return
     */
    List<FitOrderVO> findFitOrderByCondition(MaCompanyOrderVORequest maCompanyOrderVORequest);
    /**
     * 查询装饰公司订单详情
     * @return
     */
    DetailFitOrderVO findFitOrderByOrderNumber(String ordNumber);

    CreateOrderGoodsSupport createMaOrderGoodsInfo(List<MaGoodsSimpleInfo> goodsList, Long userId, Integer identityType, Long customerId,
                                                 List<ProductCouponSimpleInfo> productCouponList, String orderNumber) throws UnsupportedEncodingException;

    List<OrderGoodsVO> findMaOrderGoodsVOListByUserIdAndIdentityTypeAndGoodsSkus(Long userId, Integer identityType, Set<String> goodsIdSet);

    void clearOrderGoodsInMaterialList(Long userId, Integer identityType, List<MaGoodsSimpleInfo> goodsList, List<ProductCouponSimpleInfo> productCouponList);

    void deleteMaterialListByUserIdAndIdentityTypeAndGoodsSkus(Long userId, AppIdentityType identityType, Set<String> goodsSkus);

    void deleteMaterialListProductCouponGoodsByUserIdAndIdentityTypeAndGoodsIds(Long userId, AppIdentityType identityType, Set<Long> couponGoodsIds);

}
