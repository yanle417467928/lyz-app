package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponseGoods;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by caiyu on 2017/12/4.
 */
public interface ReturnOrderService {
    /**
     * 添加保存退单基础信息
     * @param returnOrderBaseInfo   退单基础信息类
     */
    void saveReturnOrderBaseInfo(ReturnOrderBaseInfo returnOrderBaseInfo);

    /**
     * 修改退单基础信息
     * @param returnOrderBaseInfo   退单基础信息类
     */
    void modifyReturnOrderBaseInfo(ReturnOrderBaseInfo returnOrderBaseInfo);

    /**
     * 根据退单号查询退单基础信息
     * @param returnNo  退单号
     * @return  退单基础信息
     */
    ReturnOrderBaseInfo queryByReturnNo(@Param("returnNo") String returnNo);

    /**
     * 添加保存退单退款总情况
     * @param returnOrderBilling    退款总和
     */
    void saveReturnOrderBilling(ReturnOrderBilling returnOrderBilling);

    /**
     * 添加保存退单退款明细
     * @param returnOrderBillingDetail  退单退款明细
     */
    void saveReturnOrderBillingDetail(ReturnOrderBillingDetail returnOrderBillingDetail);

    /**
     * 保存退单现金券明细
     * @param returnOrderCashCoupon 退单现金券
     */
    void saveReturnOrderCashCoupon(ReturnOrderCashCoupon returnOrderCashCoupon);

    /**
     * 保存退单产品券明细
     * @param returnOrderProductCoupon  退单产品券
     */
    void saveReturnOrderProductCoupon(ReturnOrderProductCoupon returnOrderProductCoupon);

    /**
     * 保存退单商品明细
     * @param returnOrderGoodsInfo  退单商品
     */
    void saveReturnOrderGoodsInfo(ReturnOrderGoodsInfo returnOrderGoodsInfo);

    /**
     * 取消订单
     * @param orderGoodsInfoList    订单商品列表
     * @param returnOrderId 退单id
     * @param userId    用户id
     * @param identityType  用户类型
     * @param returnNumber  退单号
     * @param orderNumber   订单号
     * @param orderBillingDetails   订单账目明细
     * @param orderBaseInfo 订单头
     */
    void canselOrder(List<OrderGoodsInfo> orderGoodsInfoList, Long returnOrderId, Long userId, Integer identityType,
                     String returnNumber, String orderNumber, OrderBillingDetails orderBillingDetails, OrderBaseInfo orderBaseInfo);

    /**
     * 拒签退货
     * @param orderGoodsInfoList    订单商品列表
     * @param returnOrderId 退单id
     * @param userId    用户id
     * @param returnNumber  退单号
     * @param orderNumber   订单号
     * @param orderBillingDetails   订单账目明细
     * @param orderBaseInfo 订单头
     */
    void refusedOrder(List<OrderGoodsInfo> orderGoodsInfoList, Long userId, Long returnOrderId, String returnNumber, String orderNumber,
                      OrderBillingDetails orderBillingDetails, OrderBaseInfo orderBaseInfo);

    void modifyReturnOrderBillingDetail(ReturnOrderBillingDetail orderReturnBillingDetail);

    List<ReturnOrderBaseInfo> findReturnOrderListByUserIdAndIdentityType(Long userId, Integer identityType, Integer showStatus);

    List<ReturnOrderGoodsInfo> findReturnOrderGoodsInfoByOrderNumber(String returnNo);

    List<GiftListResponseGoods> getReturnOrderGoodsDetails(String returnNumber);

}
