package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderBaseInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderGoodsInf;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.DeliverySimpleInfo;

import java.util.List;
import java.util.Map;

/**
 * @author Created on 2017-12-28 15:02
 **/
public interface TransactionalSupportService {

    void createOrderBusiness(DeliverySimpleInfo deliverySimpleInfo, Map<Long, Integer> inventoryCheckMap, Long cityId, Integer identityType,
                             Long userId, Long customerId, List<Long> cashCouponList, List<OrderCouponInfo> orderProductCouponInfoList,
                             OrderBillingDetails orderBillingDetails, OrderBaseInfo orderBaseInfo, OrderLogisticsInfo orderLogisticsInfo,
                             List<OrderGoodsInfo> orderGoodsInfoList, List<OrderCouponInfo> orderCouponInfoList,
                             List<OrderBillingPaymentDetails> paymentDetails, String ipAddress);

    void saveSeparateOrderInfAndGoodsInf(List<OrderBaseInf> orderBaseInfList,List<OrderGoodsInf> orderGoodsInfList );
}
