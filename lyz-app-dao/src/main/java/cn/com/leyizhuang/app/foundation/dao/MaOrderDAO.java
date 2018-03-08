package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.order.*;
import cn.com.leyizhuang.app.foundation.pojo.management.webservice.ebs.MaOrderReceiveInf;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderShipping;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderBaseInf;
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaCompanyOrderVORequest;
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaOrderVORequest;
import cn.com.leyizhuang.app.foundation.pojo.response.ArrearsAuditResponse;
import cn.com.leyizhuang.app.foundation.vo.MaOrderVO;
import cn.com.leyizhuang.app.foundation.vo.management.order.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @author caiyu
 * @date 2017/12/16
 */
@Repository
public interface MaOrderDAO {

    /**
     * 后台查看所有门店订单
     *
     * @return 订单列表
     */
    List<MaOrderVO> findMaOrderVOAll();

    /**
     * 分页查看城市门店订单
     *
     * @param cityId 城市id
     * @return 订单列表
     */
    List<MaOrderVO> findMaOrderVOByCityId(@Param("cityId") Long cityId);

    /**
     * 分页查看门店订单
     *
     * @param storeId 门店id
     * @return 订单列表
     */
    List<MaOrderVO> findMaOrderVOByStoreId(@Param("storeId") Long storeId);

    /**
     * 根据门店、城市、配送方式查看门店订单列表
     *
     * @param deliveryType 配送方式
     * @param cityId       城市id
     * @param storeId      门店id
     * @return 订单列表
     */
    List<MaOrderVO> findMaOrderVOByStoreIdAndCityIdAndDeliveryType(@Param("deliveryType") String deliveryType,
                                                                   @Param("cityId") Long cityId, @Param("storeId") Long storeId);


    /**
     * 根据订单号模糊查询门店订单
     *
     * @param orderNumber 订单号
     * @return 订单列表
     */
    List<MaOrderVO> findMaOrderVOByOrderNumber(@Param("orderNumber") String orderNumber);

    /**
     * 多条件查询门店订单列表
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
    List<MaOrderVO> findCompanyOrderByOrderNumber(@Param("orderNumber") String orderNumber);

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
    List<MaOrderVO> findPendingShipmentOrderByOrderNumber(@Param("orderNumber") String orderNumber);

    /**
     * 后台根据订单号查看门店订单详情
     *
     * @param orderNmber 订单号
     * @return 门店订单详情
     */
    MaOrderDetailResponse findMaOrderDetailByOrderNumber(@Param("orderNumber") String orderNmber);

    /**
     * 后台根据订单号查看装饰公司订单详情
     *
     * @param orderNmber 订单号
     * @return 装饰公司订单详情
     */
    MaCompanyOrderDetailResponse findMaCompanyOrderDetailByOrderNumber(@Param("orderNumber") String orderNmber);

    /**
     * 后台根据订单号查看订单账单明细
     *
     * @param orderNmber 订单号
     * @return 订单账单明细
     */
    MaOrderBillingDetailResponse getMaOrderBillingDetailByOrderNumber(@Param("orderNumber") String orderNmber);

    /**
     * 后台根据订单号查看支付明细
     *
     * @param orderNmber 订单号
     * @return 支付明细列表
     */
    List<MaOrderBillingPaymentDetailResponse> getMaOrderBillingPaymentDetailByOrderNumber(@Param("orderNumber") String orderNmber);


    Double queryRepaymentAmount(String orderNmber);

    /**
     * 后台根据订单号获取物流详情
     *
     * @param orderNmber 订单号
     * @return 物流详情
     */
    MaOrderDeliveryInfoResponse getDeliveryInfoByOrderNumber(@Param("orderNumber") String orderNmber);


    List<MaSelfTakeOrderVO> findSelfTakeOrderList();

    List<MaSelfTakeOrderVO> findSelfTakeOrderListByScreen(@Param("cityId")Long cityId,@Param("storeId")Long storeId,@Param("status")Integer status,@Param("isPayUp")Integer isPayUp);


    List<MaSelfTakeOrderVO> findSelfTakeOrderListByInfo(String info);

    List<MaSelfTakeOrderVO> findSelfTakeOrderListByCondition(MaOrderVORequest maOrderVORequest);

    MaOrderTempInfo getOrderInfoByOrderNo(String orderNo);

    void updateOrderStatus(@Param(value = "orderNo")String orderNo,@Param(value = "status")String status);

    void updateOrderReceivablesStatus(MaOrderAmount maOrderAmount);

    List<MaOrderGoodsInfo> findOrderGoodsList(@Param(value = "orderNo") String orderNo,@Param(value = "storeId") Long storeId);

    Boolean isPayUp(String orderNo);

    String getShippingTime(String orderNo);

    void saveOrderShipping(OrderShipping orderShipping);

    void saveOrderBillingPaymentDetails ( MaOrderBillingPaymentDetails maOrderBillingPaymentDetails);

    void saveAppToEbsOrderReceiveInf (MaOrderReceiveInf maOrderReceiveInf);

    MaOrderReceiveInf queryOrderReceiveInf(String orderNumber);

    List<MaAgencyAndArrearsOrderVO> findArrearsAndAgencyOrderList();


    List<MaAgencyAndArrearsOrderVO> findMaAgencyAndArrearsOrderListByScreen(@Param("cityId")Long cityId,@Param("storeId")Long storeId,@Param("status")Integer status,@Param("isPayUp")Integer isPayUp);

    List<MaAgencyAndArrearsOrderVO> findMaAgencyAndArrearsOrderListByInfo(String info);

    void auditOrderStatus(@Param(value = "orderNumber")String orderNumber,@Param(value = "status")String status);

    Long querySellerIdByOrderNumber(String orderNumber);

    /**
     * 定时查找待付款订单
     * @return
     */
    List<OrderBaseInfo> scanningUnpaidOrder(@Param(value = "findDate") String findDate);

    List<MaPaymentData> findPaymentDataByOrderNo(String orderNumber);

    MaOrderArrearsAudit getArrearsAuditInfo(String orderNumber);

    void  updateOrderArrearsAudit(@Param(value = "orderNumber") String orderNumber,@Param(value = "date") Date repaymentTime);
}
