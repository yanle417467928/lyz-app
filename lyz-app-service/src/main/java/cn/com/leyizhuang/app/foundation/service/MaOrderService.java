package cn.com.leyizhuang.app.foundation.service;

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
     * @param maOrderVORequest  查询条件参数类
     * @return  订单列表
     */
    List<MaOrderVO> findMaOrderVOByCondition(MaOrderVORequest maOrderVORequest);

    /**
     *  分页查看装饰公司所有订单列表
     * @return  订单列表
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
     * @param maCompanyOrderVORequest  查询条件参数类
     * @return  订单列表
     */
    List<MaOrderVO> findCompanyOrderByCondition(MaCompanyOrderVORequest maCompanyOrderVORequest);

    /**
     * 获取待发货订单列表
     * @return  订单列表
     */
    List<MaOrderVO> findPendingShipmentOrder();
    /**
     * 多条件查询待发货订单列表
     * @param maCompanyOrderVORequest   查询条件参数
     * @return  订单列表
     */
    List<MaOrderVO> findPendingShipmentOrderByCondition(MaCompanyOrderVORequest maCompanyOrderVORequest);

    /**
     * 根据订单号查询待发货订单
     * @param orderNumber   订单号
     * @return  订单列表
     */
    List<MaOrderVO> findPendingShipmentOrderByOrderNumber(String orderNumber);

    /**
     * 后台根据订单号查看订单详情
     * @param orderNmber    订单号
     * @return  订单详情
     */
    MaOrderDetailResponse findMaOrderDetailByOrderNumber(String orderNmber);


    /**
     * 后台根据订单号查看订单账单明细
     * @param orderNmber    订单号
     * @return  订单账单明细
     */
    MaOrderBillingDetailResponse getMaOrderBillingDetailByOrderNumber(String orderNmber);

    /**
     * 后台根据订单号查看支付明细
     * @param orderNmber    订单号
     * @return  支付明细列表
     */
    List<MaOrderBillingPaymentDetailResponse> getMaOrderBillingPaymentDetailByOrderNumber(String orderNmber);

    /**
     * 后台根据订单号获取物流详情
     * @param orderNmber    订单号
     * @return  物流详情
     */
    MaOrderDeliveryInfoResponse getDeliveryInfoByOrderNumber(String orderNmber);



    PageInfo<MaSelfTakeOrderVO> findSelfTakeOrderShippingList(Integer page, Integer size);
}
