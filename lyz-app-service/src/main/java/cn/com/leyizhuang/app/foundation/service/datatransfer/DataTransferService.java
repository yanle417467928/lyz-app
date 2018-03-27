package cn.com.leyizhuang.app.foundation.service.datatransfer;

import cn.com.leyizhuang.app.foundation.pojo.datatransfer.*;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderLogisticsInfo;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;

import java.util.Date;
import java.util.List;

/**
 * @author Created on 2018-03-24 15:15
 **/
public interface DataTransferService {

    List<TdOrderLogistics> queryOrderLogistcs(int size);

    List<String> getTransferStoreMainOrderNumber(Date startTime, Date endTime);

    TdOrder getMainOrderInfoByMainOrderNumber(String mainOrderNumber);

    Integer transferArrearsAudit(String orderNumber);

    List<TdDeliveryInfoDetails> queryDeliveryTimeSeqBySize(int size);

    List<TdDeliveryInfoDetails> queryDeliveryInfoDetailByOrderNumber(String orderNo);

    List<TdDeliveryInfoDetails> queryTdOrderListBySize(int size);

    List<TdDeliveryInfoDetails> queryOrderGoodsListByOrderNumber(Long id);

    Integer transferCoupon(OrderBaseInfo baseInfo);

    Integer transferOrderBillingDetails();

    AppEmployee findFitEmployeeInfoById(Long userId);

    AppEmployee findStoreEmployeeById(Long sellerId);

    AppCustomer findCustomerById(Long userId);

    void saveOrderLogisticsInfo(OrderLogisticsInfo orderLogisticsInfo);

    TdOrderDeliveryTimeSeqDetail findDeliveryStatusByMainOrderNumber(String mainOrderNumber);

    List<OrderBaseInfo> findNewOrderNumber();

    List<TdOrderData> queryTdOrderDataListBySize(int size);

}
