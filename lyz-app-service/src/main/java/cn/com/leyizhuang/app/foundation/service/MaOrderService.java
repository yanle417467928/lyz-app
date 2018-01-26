package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderAmount;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderBillingPaymentDetails;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderTempInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.webservice.ebs.MaOrderReceiveInf;
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaCompanyOrderVORequest;
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaOrderVORequest;
import cn.com.leyizhuang.app.foundation.vo.MaOrderVO;
import cn.com.leyizhuang.app.foundation.vo.management.order.*;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by caiyu on 2017/12/16.
 */
public interface MaOrderService {
    /**
     * 后台查看所有订单
     *
     * @return 订单列表
     */
    List<MaOrderVO> findMaOrderVOAll();

    /**
     * 分页查看城市订单
     *
     * @param cityId 城市id
     * @return 订单列表
     */
    List<MaOrderVO> findMaOrderVOByCityId(Long cityId);

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
    List<MaOrderVO> findMaOrderVOByCondition(MaOrderVORequest maOrderVORequest);

    /**
     * 分页查看装饰公司所有订单列表
     *
     * @return 订单列表
     */
    List<MaOrderVO> findCompanyOrderAll();

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
     * 后台根据订单号获取物流详情
     *
     * @param orderNmber 订单号
     * @return 物流详情
     */
    MaOrderDeliveryInfoResponse getDeliveryInfoByOrderNumber(String orderNmber);


    PageInfo<MaSelfTakeOrderVO> findSelfTakeOrderList(Integer page, Integer size);

    PageInfo<MaSelfTakeOrderVO> findSelfTakeOrderListByScreen(Integer page, Integer size,Long cityId,Long storeId,Integer status,Integer isPayUp);

    PageInfo<MaSelfTakeOrderVO> findSelfTakeOrderListByInfo(Integer page, Integer size,String info);

    PageInfo<MaSelfTakeOrderVO> findSelfTakeOrderByCondition(Integer page, Integer size,MaOrderVORequest maOrderVORequest);

    MaOrderTempInfo getOrderInfoByOrderNo(String orderNo);

    void orderShipping(String orderNumber,ShiroUser shiroUser,MaOrderTempInfo maOrderTempInfo);

    void orderReceivables(MaOrderAmount maOrderAmount);

    /**
     * 更新订单基础表订单状态
     *
     * @param orderNumber 订单号
     * @return
     */
    void updateOrderStatus(String orderNumber);

    /**
     * 更新订单费用表订单状态
     *
     * @param orderNumber 订单号
     * @return
     */
    void updateorderReceivablesStatus(String orderNumber);

    List<MaOrderGoodsInfo> findOrderGoodsList(String orderNumber);

    Boolean judgmentVerification(String code,String orderNumber);
    /**
     * 后台判断订单是否已收款
     *
     * @param orderNumber 订单号
     * @return 物流详情
     */
    Boolean isPayUp(String orderNumber);

    /**
     * 新增订单收款记录
     *
     * @param maOrderBillingPaymentDetails 订单号
     * @return
     */
    void saveOrderBillingPaymentDetails( MaOrderBillingPaymentDetails maOrderBillingPaymentDetails);

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


    PageInfo<MaSelfTakeOrderVO> findArrearsAndAgencyOrderList(Integer page, Integer size);

}
