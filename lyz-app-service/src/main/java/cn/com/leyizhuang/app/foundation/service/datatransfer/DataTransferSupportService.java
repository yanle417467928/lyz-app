package cn.com.leyizhuang.app.foundation.service.datatransfer;

import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.DataTransferErrorLog;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBilling;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderDeliveryDetail;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderGoodsInfo;

import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * @author Created on 2018-03-24 15:15
 **/
public interface DataTransferSupportService {


    void saveDataTransferErrorLog(Queue<DataTransferErrorLog> errorLogQueue);

    void saveOneDataTransferErrolog(DataTransferErrorLog log);

    void saveOrderRelevantInfo(OrderBaseInfo orderBaseInfo, List<OrderGoodsInfo> orderGoodsInfoList, OrderBillingDetails orderBillingDetails,
                               List<OrderDeliveryInfoDetails> deliveryInfoDetailsList, List<OrderJxPriceDifferenceReturnDetails> jxPriceDifferenceReturnDetailsList,
                               List<OrderBillingPaymentDetails> paymentDetailsList,
                               OrderLogisticsInfo orderLogisticsInfo, Map<String, Object> map, OrderArrearsAuditDO orderArrearsAuditDO);

    void saveReturnOrderRelevantInfo(ReturnOrderBaseInfo returnOrderBaseInfo, List<ReturnOrderGoodsInfo> returnOrderGoodsInfos,
                                     List<ReturnOrderJxPriceDifferenceRefundDetails> jxPriceDifferenceRefundDetails,
                                     List<Map<String, Object>> returnOrderProductCouponsMapList, ReturnOrderBilling returnOrderBilling,
                                     ReturnOrderDeliveryDetail returnOrderDeliveryDetail);

    Integer transferAllCustomerByTemplate();

    Integer transferAllProductByTemplate();
}
