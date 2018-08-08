package cn.com.leyizhuang.app.foundation.service.datatransfer.impl;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.transferdao.TransferDAO;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.DataTransferErrorLog;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TransferCusTemplate;
import cn.com.leyizhuang.app.foundation.pojo.management.customer.CustomerDO;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.*;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.service.datatransfer.DataTransferSupportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

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

    @Resource
    private ArrearsAuditService arrearsAuditService;
    @Resource
    private ReturnOrderService returnOrderService;

    @Resource
    private ReturnOrderDeliveryDetailsService returnOrderDeliveryDetailsService;
    @Resource
    private AppCustomerService appCustomerService;

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
                Boolean flag = transferDAO.isExitTdOrderGoodsLine(p.getOrderNumber(), p.getGid(), p.getGoodsLineType().getValue());
                if (!flag) {
                    orderService.saveOrderGoodsInfo(p);
                }
            });
        }
        //保存订单账单信息
        if (orderBillingDetails != null) {
            orderBillingDetails.setOid(orderBaseInfo.getId());
            orderService.saveOrderBillingDetails(orderBillingDetails);
        }

        //保存订单账单支付明细信息
        if (null != paymentDetailsList && !paymentDetailsList.isEmpty()) {
            paymentDetailsList.forEach(p -> {
                p.setOrderId(orderBaseInfo.getId());
                orderService.saveOrderBillingPaymentDetail(p);
            });
        }

        //保存订单物流信息
        if (orderLogisticsInfo != null) {
            orderLogisticsInfo.setOid(orderBaseInfo.getId());
            orderService.saveOrderLogisticsInfo(orderLogisticsInfo);
        }
        //保存订单欠款审核信息
        if (null != orderArrearsAuditDO && StringUtils.isNotBlank(orderArrearsAuditDO.getOrderNumber())) {
            arrearsAuditService.save(orderArrearsAuditDO);
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
                orderService.saveOrderCouponInfo(cashCouponInfo);
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
                            orderCouponInfo.setCouponId(productCoupon.getId());
                            orderService.saveOrderCouponInfo(orderCouponInfo);
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

        /** 记录订单转换成功时间 **/
        transferDAO.updateTransferDate(new Date(),orderBaseInfo.getOrderNumber());
    }


    @Override
    public void saveDataTransferErrorLog(Queue<DataTransferErrorLog> errorLogQueue) {
        if (null != errorLogQueue && !errorLogQueue.isEmpty()) {
            List<DataTransferErrorLog> errorLogList = new ArrayList<>(errorLogQueue);
            transferDAO.saveDataTransferErrorLogList(errorLogList);
        }
    }

    @Override
    public void saveOneDataTransferErrolog(DataTransferErrorLog log){
        if (log != null){
            transferDAO.saveDataTransferErrorLog(log);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveReturnOrderRelevantInfo(ReturnOrderBaseInfo returnOrderBaseInfo, List<ReturnOrderGoodsInfo> returnOrderGoodsInfos,
                                            List<ReturnOrderJxPriceDifferenceRefundDetails> jxPriceDifferenceRefundDetails,
                                            List<Map<String, Object>> returnOrderProductCouponsMapList, ReturnOrderBilling returnOrderBilling,
                                            ReturnOrderDeliveryDetail returnOrderDeliveryDetail) {
        returnOrderService.saveReturnOrderBaseInfo(returnOrderBaseInfo);

        //保存订单商品信息
        if (null != returnOrderGoodsInfos && !returnOrderGoodsInfos.isEmpty()) {
            returnOrderGoodsInfos.forEach(p -> {
                p.setRoid(returnOrderBaseInfo.getRoid());
                Boolean flag = transferDAO.isExitTdReturnOrderGoodsLine(p.getReturnNo(), p.getGid(), p.getGoodsLineType().getValue());
                if (!flag) {
                    returnOrderService.saveReturnOrderGoodsInfo(p);
                }
            });
        }

        //保存订单经销差价信息
        if (null != jxPriceDifferenceRefundDetails && !jxPriceDifferenceRefundDetails.isEmpty()) {
            jxPriceDifferenceRefundDetails.forEach(p -> {
                p.setRoid(returnOrderBaseInfo.getRoid());
                returnOrderService.saveReturnOrderJxPriceDifferenceRefundDetails(p);
            });
        }

        //保存订单账单信息
        returnOrderBilling.setRoid(returnOrderBaseInfo.getRoid());
        returnOrderService.saveReturnOrderBilling(returnOrderBilling);

        //保存物流信息
        returnOrderDeliveryDetail.setRoid(returnOrderBaseInfo.getRoid());
        returnOrderDeliveryDetailsService.addReturnOrderDeliveryInfoDetails(returnOrderDeliveryDetail);

        //保存顾客产品券信息
        if (null != returnOrderProductCouponsMapList && !returnOrderProductCouponsMapList.isEmpty()) {
            for (int i = 0; i < returnOrderProductCouponsMapList.size(); i++) {
                Map<String, Object> productMap = returnOrderProductCouponsMapList.get(i);
                if (null != productMap && !productMap.isEmpty()) {
                    CustomerProductCoupon productCoupon = (CustomerProductCoupon) productMap.get("customerProductCoupon");
                    ReturnOrderProductCoupon returnOrderProductCoupon = (ReturnOrderProductCoupon) productMap.get("returnOrderProductCoupon");
                    if (null != productCoupon && null != returnOrderProductCoupon && StringUtils.isNotBlank(returnOrderProductCoupon.getReturnNo())) {
                        this.transferDAO.addCustomerProductCoupon(productCoupon);
                        returnOrderProductCoupon.setRoid(returnOrderBaseInfo.getRoid());
                        returnOrderProductCoupon.setPcid(productCoupon.getId());
                        returnOrderService.saveReturnOrderProductCoupon(returnOrderProductCoupon);
                    }
                }
            }
        }

        /** 记录退单转换成功时间 **/
        transferDAO.updateReturnTransferDate(new Date(), returnOrderBaseInfo.getReturnNo());

    }

    public void transferCustomerByTemplate(TransferCusTemplate template){
        CustomerDO customerDO = new CustomerDO();



    };
}
