package cn.com.leyizhuang.app.foundation.service.datatransfer.impl;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.transferdao.TransferDAO;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.DataTransferErrorLog;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.OrderDeliveryInfoDetailsService;
import cn.com.leyizhuang.app.foundation.service.datatransfer.DataTransferSupportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * 订单商品转换类
 *
 * @author Richard
 * @date 2018/3/24
 */
@Service
@Slf4j
public class DataTransferSupportServiceImpl implements DataTransferSupportService {

    @Resource
    private AppOrderService orderService;

    @Resource
    private TransferDAO transferDAO;

    @Resource
    private OrderDeliveryInfoDetailsService deliveryInfoDetailsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderRelevantInfo(OrderBaseInfo orderBaseInfo, List<OrderGoodsInfo> orderGoodsInfoList, OrderBillingDetails orderBillingDetails,
                                      List<OrderDeliveryInfoDetails> deliveryInfoDetailsList, List<OrderJxPriceDifferenceReturnDetails> jxPriceDifferenceReturnDetailsList,
                                      List<OrderBillingPaymentDetails> paymentDetailsList, OrderLogisticsInfo orderLogisticsInfo,
                                      Map<String, Object> map, OrderArrearsAuditDO orderArrearsAuditDO) {

        //保存订单基础信息
        orderService.saveOrderBaseInfo(orderBaseInfo);

        //保存订单商品信息
        if (null != orderGoodsInfoList && !orderGoodsInfoList.isEmpty()) {
            orderGoodsInfoList.forEach(p -> {
                p.setOid(orderBaseInfo.getId());
                orderService.saveOrderGoodsInfo(p);
            });
        }
        //保存订单账单信息
        orderBillingDetails.setOid(orderBaseInfo.getId());
        transferDAO.saveOrderBillingDetails(orderBillingDetails);

        //保存订单账单支付明细信息
        if (null != paymentDetailsList && !paymentDetailsList.isEmpty()) {
            paymentDetailsList.forEach(p -> {
                p.setOrderId(orderBaseInfo.getId());
                orderService.saveOrderBillingPaymentDetail(p);
            });
        }

        //保存订单物流信息
        orderLogisticsInfo.setOid(orderBaseInfo.getId());
        orderService.saveOrderLogisticsInfo(orderLogisticsInfo);
        //保存订单欠款审核信息
        if (null != orderArrearsAuditDO && StringUtils.isNotBlank(orderArrearsAuditDO.getOrderNumber())) {
            this.transferDAO.insertArrearsAudit(orderArrearsAuditDO);
        }
        //保存订单券信息
        if (null != map && !map.isEmpty()) {
            CashCoupon cashCoupon = (CashCoupon) map.get("cashCoupon");
            CashCouponCompany cashCouponCompany = (CashCouponCompany) map.get("cashCouponCompany");
            CustomerCashCoupon customerCashCoupon = (CustomerCashCoupon) map.get("customerCashCoupon");
            OrderCouponInfo cashCouponInfo = (OrderCouponInfo) map.get("cashCouponInfo");
            if (null != cashCoupon && null != customerCashCoupon && null != cashCouponCompany && null !=
                    cashCouponInfo && StringUtils.isNotBlank(cashCouponInfo.getOrderNumber())) {
                this.transferDAO.addCashCoupon(cashCoupon);
                cashCouponCompany.setCcid(cashCoupon.getId());
                this.transferDAO.addCashCouponCompany(cashCouponCompany);
                customerCashCoupon.setCcid(cashCoupon.getId());
                this.transferDAO.addCustomerCashCoupon(customerCashCoupon);
                cashCouponInfo.setCouponId(customerCashCoupon.getId());
                cashCouponInfo.setOid(orderBaseInfo.getId());
                this.transferDAO.saveOrderCouponInfo(cashCouponInfo);
            }
            List<Map<String, Object>> listCoupon = (List) map.get("listCoupon");
            if (null != listCoupon && !listCoupon.isEmpty()) {
                for (int i = 0; i < listCoupon.size(); i++) {
                    Map<String, Object> productMap = listCoupon.get(i);
                    if (null != productMap && !productMap.isEmpty()) {
                        CustomerProductCoupon productCoupon = (CustomerProductCoupon) productMap.get("productCoupon");
                        OrderCouponInfo orderCouponInfo = (OrderCouponInfo) productMap.get("orderCoupon");
                        if (null != productCoupon && null != orderCouponInfo && StringUtils.isNotBlank(orderCouponInfo.getOrderNumber())) {
                            this.transferDAO.addCustomerProductCoupon(productCoupon);
                            orderCouponInfo.setOid(orderBaseInfo.getId());
                            cashCouponInfo.setCouponId(productCoupon.getId());
                            this.transferDAO.saveOrderCouponInfo(cashCouponInfo);
                        }
                    }
                }
            }

        }
        //保存订单物流明细信息
        if (null != deliveryInfoDetailsList && !deliveryInfoDetailsList.isEmpty()) {
            deliveryInfoDetailsList.forEach(p -> deliveryInfoDetailsService.addOrderDeliveryInfoDetails(p));
        }
        //保存订单经销差价信息
        if (null != jxPriceDifferenceReturnDetailsList && !jxPriceDifferenceReturnDetailsList.isEmpty()) {
            jxPriceDifferenceReturnDetailsList.forEach(p -> {
                p.setOid(orderBaseInfo.getId());
                orderService.saveOrderJxPriceDifferenceReturnDetails(p);
            });
        }
    }


    @Override
    public void saveDataTransferErrorLog(Queue<DataTransferErrorLog> errorLogQueue) {
        if (null != errorLogQueue && !errorLogQueue.isEmpty()) {
            List<DataTransferErrorLog> errorLogList = new ArrayList<>(errorLogQueue);
            transferDAO.saveDataTransferErrorLogList(errorLogList);
        }


    }
}
