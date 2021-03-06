package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.AppOrderStatus;
import cn.com.leyizhuang.app.core.constant.AppOrderType;
import cn.com.leyizhuang.app.core.constant.AppReturnOrderStatus;
import cn.com.leyizhuang.app.core.exception.OrderSaveException;
import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwReturnOrder;
import cn.com.leyizhuang.app.foundation.pojo.request.ReturnDeliverySimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponseGoods;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.map.HashedMap;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by caiyu on 2017/12/4.
 */
public interface ReturnOrderService {
    /**
     * 添加保存退单基础信息
     *
     * @param returnOrderBaseInfo 退单基础信息类
     */
    void saveReturnOrderBaseInfo(ReturnOrderBaseInfo returnOrderBaseInfo);

    /**
     * 修改退单基础信息
     *
     * @param returnOrderBaseInfo 退单基础信息类
     */
    void modifyReturnOrderBaseInfo(ReturnOrderBaseInfo returnOrderBaseInfo);

    /**
     * 根据退单号查询退单基础信息
     *
     * @param returnNo 退单号
     * @return 退单基础信息
     */
    ReturnOrderBaseInfo queryByReturnNo(String returnNo);

    /**
     * 根据订单号查询退单基础信息
     *
     * @param ordNo 订单号
     * @return 退单基础信息
     */
    ReturnOrderBaseInfo queryByOrdNo(String ordNo);

    /**
     * 添加保存退单退款总情况
     *
     * @param returnOrderBilling 退款总和
     */
    void saveReturnOrderBilling(ReturnOrderBilling returnOrderBilling);

    /**
     * 添加保存退单退款明细
     *
     * @param returnOrderBillingDetail 退单退款明细
     */
    void saveReturnOrderBillingDetail(ReturnOrderBillingDetail returnOrderBillingDetail);

    /**
     * 保存退单现金券明细
     *
     * @param returnOrderCashCoupon 退单现金券
     */
    void saveReturnOrderCashCoupon(ReturnOrderCashCoupon returnOrderCashCoupon);

    /**
     * 保存退单产品券明细
     *
     * @param returnOrderProductCoupon 退单产品券
     */
    void saveReturnOrderProductCoupon(ReturnOrderProductCoupon returnOrderProductCoupon);

    /**
     * 保存退单商品明细
     *
     * @param returnOrderGoodsInfo 退单商品
     */
    void saveReturnOrderGoodsInfo(ReturnOrderGoodsInfo returnOrderGoodsInfo);

    /**
     * 保存退单物流信息
     *
     * @param returnOrderLogisticInfo 退单物流信息
     */
    void saveReturnOrderLogisticsInfo(ReturnOrderLogisticInfo returnOrderLogisticInfo);

    void modifyReturnOrderBillingDetail(ReturnOrderBillingDetail orderReturnBillingDetail);

    /**
     * 查所有退货单列表
     *
     * @param userId       id
     * @param identityType 身份
     * @return 退货单列表
     */
    PageInfo<ReturnOrderBaseInfo> findReturnOrderListByUserIdAndIdentityType(Long userId, Integer identityType, Integer page, Integer size, String keywords);

    /**
     * 查退货单商品信息
     *
     * @param returnNo 退货单号
     * @return 退货商品信息
     */
    List<ReturnOrderGoodsInfo> findReturnOrderGoodsInfoByOrderNumber(String returnNo);

    /**
     * 获取退货单商品详情
     *                 注意!retailPrice字段取的returnPrice值
     * @param returnNumber 退货单号
     * @return 退货商品详细信息
     */
    List<GiftListResponseGoods> getReturnOrderGoodsDetails(String returnNumber);

    /**
     * 创建退货单
     *
     * @param orderId             原单id
     * @param orderNo             原单编号
     * @param orderTime           原单创单时间
     * @param remarksInfo         退货备注信息
     * @param creatorId           创建者Id
     * @param creatorIdentityType 创建者身份
     * @param reasonInfo          退货原因
     * @param returnPic           退货问题描述图片
     * @param orderType           原单类型
     * @return 退货单
     */
    ReturnOrderBaseInfo createReturnOrderBaseInfo(Long orderId, String orderNo, Date orderTime, String remarksInfo, Long creatorId,
                                                  Integer creatorIdentityType, String reasonInfo, String returnPic, AppOrderType orderType,
                                                  Long storeId, String storeCode, String storeStructureCode);

    /**
     * 创建退货配送物流信息
     *
     * @param returnDeliveryInfo 物流信息
     * @return 配送物流信息
     */
    ReturnOrderLogisticInfo createReturnOrderLogisticInfo(ReturnDeliverySimpleInfo returnDeliveryInfo);

    /**
     * 保存退货单相关实体
     *
     * @param returnOrderBaseInfo     退货单基础信息
     * @param returnOrderLogisticInfo 退货单物流信息
     * @param returnOrderGoodsInfos   退货单商品信息
     * @param returnOrderBilling      退货单退款信息
     * @param productCouponList       退货单产品券信息
     * @throws OrderSaveException 订单异常
     */
    void saveReturnOrderRelevantInfo(ReturnOrderBaseInfo returnOrderBaseInfo, ReturnOrderLogisticInfo returnOrderLogisticInfo, List<ReturnOrderGoodsInfo> returnOrderGoodsInfos,
                                     ReturnOrderBilling returnOrderBilling, List<ReturnOrderProductCoupon> productCouponList, List<OrderGoodsInfo> orderGoodsInfoList,AtwReturnOrder atwReturnOrder)
            throws OrderSaveException;

    ReturnOrderLogisticInfo getReturnOrderLogisticeInfo(String returnNumber);

    /**
     * 单独更新退单状态
     *
     * @param outRefundNo
     * @param finished
     */
    void updateReturnOrderStatus(String outRefundNo, AppReturnOrderStatus finished);

    /**
     * 取消、拒签订单修改订单商品可退数量和已退数量
     *
     * @param returnQty     已退数量
     * @param returnableQty 可退数量
     * @param id            订单商品行id
     */
    void updateReturnableQuantityAndReturnQuantityById(Integer returnQty, Integer returnableQty, Long id);

    List<ReturnOrderCashCoupon> getReturnOrderCashCouponByRoid(Long roid);

    List<ReturnOrderProductCoupon> getReturnOrderProductCouponByRoid(Long roid);

    List<ReturnOrderBillingDetail> getReturnOrderBillingDetailByRoid(Long roid);

    List<ReturnOrderJxPriceDifferenceRefundDetails> getReturnOrderJxPriceDifferenceRefundDetailsByReturnNumber(String returnNo);

    void saveReturnOrderJxPriceDifferenceRefundDetails(ReturnOrderJxPriceDifferenceRefundDetails refundDetails);
    /**
     * 取消订单通用方法
     *
     * @param userId
     * @param identityType
     * @param orderNumber
     * @param reasonInfo
     * @param remarksInfo
     * @return
     */
    Map<Object, Object> cancelOrderUniversal(Long userId, Integer identityType,
                                             String orderNumber, String reasonInfo, String remarksInfo, OrderBaseInfo orderBaseInfo, OrderBillingDetails orderBillingDetails);

    /**
     * 拒签退货退钱
     * @param orderNumber
     * @return
     */
    HashedMap refusedOrder(String orderNumber, OrderBaseInfo orderBaseInfo,
                                     OrderBillingDetails orderBillingDetails,ReturnOrderBaseInfo returnOrderBaseInfo ,List<ReturnOrderGoodsInfo> returnOrderGoodsInfos);

    /**
     * 退货保存实体
     * @param userId
     * @param identityType
     * @param orderNumber
     * @param reasonInfo
     * @param remarksInfo
     * @param orderBaseInfo
     * @param orderBillingDetails
     * @param returnPic
     * @return
     */
     Map<Object, Object> saveRefusedOrder(Long userId, Integer identityType, String orderNumber, String reasonInfo,
                                                String remarksInfo, OrderBaseInfo orderBaseInfo, OrderBillingDetails orderBillingDetails, String returnPic);

    HashedMap normalReturnOrderProcessing(String returnOrderNumber, String cityCode);

    HashedMap couponReturnOrderProcessing(String returnOrderNumber, String cityCode);

    void updateReturnLogisticInfo(AppEmployee employee, String returnNo);

    ReturnOrderBilling getReturnOrderBillingByReturnNo(String returnOrderNumber);

    List<ReturnOrderBillingDetail> getReturnOrderBillingDetailByRefundNumber(String refundNumber);

    List<ReturnOrderBillingDetail> getReturnOrderBillingDetailByReturndNumber(String returnNo);
    /**
     * 修改退单状态
     * @param returnNo  退单号
     * @param returnStatus  退单状态
     */
    void updateReturnOrderBaseInfoByReturnNo(String returnNo,AppReturnOrderStatus returnStatus);

    /**
     * 单独更新物流表的返配上架时间
     *
     * @param returnNo
     */
    void updateReturnLogisticInfoOfBackTime(String returnNo);

    List<String> getNotReturnDetailsReturnNos(Boolean flag);

    /**
     * 拒签退货退钱
     * @param orderNumber
     * @return
     */
    HashedMap refusedOrder(String orderNumber, OrderBaseInfo orderBaseInfo, OrderBillingDetails orderBillingDetails,
                           ReturnOrderBaseInfo returnOrderBaseInfo ,List<ReturnOrderGoodsInfo> returnOrderGoodsInfos, City city);

    HashedMap normalReturnOrderProcessing(String returnOrderNumber, City city);

    /**
     * 保存订单生命周期记录
     * @param orderLifecycle
     */
    void saveOrderLifecycle(OrderLifecycle orderLifecycle);

    /**
     * 保存退单生命周期记录
     * @param returnOrderLifecycle
     */
    void saveReturnOrderLifecycle(ReturnOrderLifecycle returnOrderLifecycle);

    ReturnOrderBilling getAllReturnPriceByOrderNo(String orderNo);

    List<ReturnOrderBaseInfo> getReturnBaseinfoByOrderNo(String ordNo);

    List<OrderBaseInfo>  findOrderByStatusAndTypeAndCreateTime(AppOrderStatus status, AppDeliveryType type, LocalDateTime endTime);
}
