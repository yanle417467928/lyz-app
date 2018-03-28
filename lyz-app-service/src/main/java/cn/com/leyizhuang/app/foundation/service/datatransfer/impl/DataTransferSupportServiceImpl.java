package cn.com.leyizhuang.app.foundation.service.datatransfer.impl;

import cn.com.leyizhuang.app.foundation.dao.transferdao.TransferDAO;
import cn.com.leyizhuang.app.foundation.pojo.CashCoupon;
import cn.com.leyizhuang.app.foundation.pojo.CashCouponCompany;
import cn.com.leyizhuang.app.foundation.pojo.CustomerCashCoupon;
import cn.com.leyizhuang.app.foundation.pojo.CustomerProductCoupon;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.DataTransferErrorLog;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderRelevantInfo(OrderBaseInfo orderBaseInfo, OrderBillingDetails orderBillingDetails,OrderLogisticsInfo orderLogisticsInfo, Map<String, Object> map,
                                      OrderArrearsAuditDO orderArrearsAuditDO) {
        orderService.saveOrderBaseInfo(orderBaseInfo);
        transferDAO.saveOrderBillingDetails(orderBillingDetails);
        orderLogisticsInfo.setOid(orderBaseInfo.getId());
        orderService.saveOrderLogisticsInfo(orderLogisticsInfo);

        if (null != orderArrearsAuditDO) {
            this.transferDAO.insertArrearsAudit(orderArrearsAuditDO);
        }
        if (null != map) {
            CashCoupon cashCoupon = (CashCoupon) map.get("cashCoupon");
            CashCouponCompany cashCouponCompany = (CashCouponCompany) map.get("cashCouponCompany");
            CustomerCashCoupon customerCashCoupon = (CustomerCashCoupon) map.get("customerCashCoupon");
            OrderCouponInfo cashCouponInfo = (OrderCouponInfo) map.get("cashCouponInfo");
            if (null != cashCoupon && null != customerCashCoupon && null != cashCouponCompany && null != cashCouponInfo){
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
            if (null != listCoupon){
                for (int i = 0; i < listCoupon.size(); i++) {
                    Map<String, Object> productMap = listCoupon.get(i);
                    if (null != productMap) {
                        CustomerProductCoupon productCoupon = (CustomerProductCoupon)productMap.get("productCoupon");
                        OrderCouponInfo orderCouponInfo = (OrderCouponInfo)productMap.get("orderCoupon");
                        if (null != productCoupon && null != orderCouponInfo){

                            this.transferDAO.addCustomerProductCoupon(productCoupon);
                            orderCouponInfo.setOid(orderBaseInfo.getId());
                            cashCouponInfo.setCouponId(productCoupon.getId());
                            this.transferDAO.saveOrderCouponInfo(cashCouponInfo);
                        }
                    }
                }
            }

        }
    }


    @Override
    public void saveDataTransferErrorLog(Queue<DataTransferErrorLog> errorLogQueue) {
        if (null != errorLogQueue && !errorLogQueue.isEmpty()){
            List<DataTransferErrorLog> errorLogList = new ArrayList<>(errorLogQueue);
            transferDAO.saveDataTransferErrorLogList(errorLogList);
        }


    }
}
