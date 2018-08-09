package cn.com.leyizhuang.app.foundation.service.datatransfer.impl;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.AppCustomerDAO;
import cn.com.leyizhuang.app.foundation.dao.ProductCouponDAO;
import cn.com.leyizhuang.app.foundation.dao.transferdao.TransferDAO;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.DataTransferErrorLog;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TransferCusPreDepositTemplate;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TransferCusProductTemplate;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TransferCusTemplate;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.management.city.SimpleCityParam;
import cn.com.leyizhuang.app.foundation.pojo.management.customer.CustomerDO;
import cn.com.leyizhuang.app.foundation.pojo.management.employee.SimpleEmployeeParam;
import cn.com.leyizhuang.app.foundation.pojo.management.store.SimpleStoreParam;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.service.datatransfer.DataTransferSupportService;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerDetailVO;
import cn.com.leyizhuang.common.core.exception.AppConcurrentExcp;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    @Resource
    private MaCustomerService maCustomerService;

    @Resource
    private MaStoreService maStoreService;

    @Resource
    private AppEmployeeService appEmployeeService;

    @Resource
    private AppCustomerDAO customerDAO;

    @Resource
    private GoodsService goodsService;

    @Resource
    private ProductCouponDAO productCouponDAO;

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

    public Integer transferAllCustomerByTemplate(){
        // 查询所以未转换的模板
        List<TransferCusTemplate> transferCusTemplateList = transferDAO.findCusTem();

        for (TransferCusTemplate template : transferCusTemplateList){
            this.transferCustomerByTemplate(template);
        }
        return null;
    }

    public Integer transferAllProductByTemplate(){
        // 查询所有未转换模板
        List<TransferCusProductTemplate> templateList = transferDAO.findCusProductDepositTem();

        for (TransferCusProductTemplate template : templateList){
                this.transferCusProduct(template);
        }

        return null;
    }

    @Transactional
    public int transferCustomerByTemplate(TransferCusTemplate template){
        CustomerDetailVO customerDO = new CustomerDetailVO();

        customerDO.setName(template.getCusName());
        SimpleCityParam cityParam = new SimpleCityParam();
        cityParam.setCityId(template.getCityId());
        customerDO.setCity(cityParam);
        SimpleStoreParam storeParam = new SimpleStoreParam();

        storeParam.setStoreId(template.getStoreId());
        storeParam.setStoreName(template.getStoreName());
        customerDO.setMobile(template.getCusMobile());
        customerDO.setStatus(true);
        customerDO.setSex(SexType.SECRET.getValue());
        customerDO.setIsCashOnDelivery(false);
        customerDO.setCreateType(AppCustomerCreateType.IMPORT);
        customerDO.setCustomerType(AppCustomerType.MEMBER.getValue());

        AppEmployee employee = appEmployeeService.findByLoginName(template.getEmpCode());

        if (employee == null) {
            log.info("编码为 "+ template.getEmpCode() + "导购找不到");
            return  0;
        }
        SimpleEmployeeParam employeeParam = new SimpleEmployeeParam();
        employeeParam.setId(employee.getEmpId());
        employeeParam.setIdentityType(AppIdentityType.SELLER);
        employeeParam.setName(employee.getName());
        customerDO.setSalesConsultId(employeeParam);

        maCustomerService.saveCustomer(customerDO);

        AppCustomer customer = new AppCustomer();

        try {
            customer = appCustomerService.findByMobile(template.getCusMobile());
            template.setStatus(true);
            transferDAO.updateCusTem(template);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (template.getCusMobile() != null && customer != null){
            // 设置预存款
            log.info("顾客 "+customer.getMobile() + "转换成功！");

            TransferCusPreDepositTemplate preDepositTemplate = transferDAO.findCusPreDeposit(template.getCusMobile());
            Double preDeposit = preDepositTemplate.getCusPreDepost() == null ? 0D : preDepositTemplate.getCusPreDepost();

            if (preDeposit > 0.00){
                // 更新

                CustomerPreDeposit customerPreDeposit = this.customerDAO.findByCusId(customer.getCusId());
                if (null == customerPreDeposit) {
                    customerPreDeposit = new CustomerPreDeposit();
                    customerPreDeposit.setBalance(preDeposit);
                    customerPreDeposit.setCusId(customer.getCusId());
                    customerPreDeposit.setCreateTime(new Date());
                    customerPreDeposit.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
                    this.customerDAO.savePreDeposit(customerPreDeposit);
                } else {
                    int row = this.customerDAO.updateDepositByUserIdAndLastUpdateTime(customer.getCusId(), preDeposit, new Timestamp(System.currentTimeMillis()), customerPreDeposit.getLastUpdateTime());
                    if (1 != row) {
                        throw new AppConcurrentExcp("账号余额信息过期！");
                    }else {
                        log.info(template.getCusMobile()+" 预存款初始成功！"+preDeposit+"元");
                        preDepositTemplate.setStatus(true);
                        transferDAO.updateCusPreDepositTemplate(preDepositTemplate);
                    }
                }

            }
        }else {
            log.info("手机号为："+template.getCusMobile()+"顾客保存失败！");
        }

        return 1;
    }

    @Transactional
    public int transferCusProduct(TransferCusProductTemplate template){

        AppCustomer customer = new AppCustomer();
        try {
            customer = appCustomerService.findByMobile(template.getCusMobile());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.info("手机号为 "+ template.getCusMobile() + "的顾客找不到");
        }

        GoodsDO goodsDO = goodsService.findBySku(template.getSku());

        if (goodsDO == null){
            log.info("sku: "+ template.getSku() + "找不到");
            return 0;
        }
        // 创建一张券
        CustomerProductCoupon customerProductCoupon = new CustomerProductCoupon();
        customerProductCoupon.setCustomerId(customer.getCusId());
        customerProductCoupon.setGoodsId(goodsDO.getGid());
        customerProductCoupon.setQuantity(1);
        customerProductCoupon.setGetType(CouponGetType.BUY);

        Date now = new Date();
        customerProductCoupon.setGetTime(now);
        customerProductCoupon.setEffectiveStartTime(now);

        Calendar c = Calendar.getInstance();//获得一个日历的实例
        c.setTime(now);//设置日历时间
        c.add(Calendar.MONTH,6);
        customerProductCoupon.setEffectiveEndTime(c.getTime());

        customerProductCoupon.setIsUsed(false);
        customerProductCoupon.setBuyPrice(template.getBuyPrice());

        AppEmployee employee = appEmployeeService.findByLoginName(template.getEmpCode());
        customerProductCoupon.setStoreId(employee.getStoreId());
        customerProductCoupon.setSellerId(employee.getEmpId());
        customerProductCoupon.setStatus(true);
        customerProductCoupon.setGoodsSign("COMMON");

        for (int i = 0;i < template.getQuantity() ; i++){
            productCouponDAO.addCustomerProductCoupon(customerProductCoupon);
        }

        template.setStatus(true);
        transferDAO.updateCusProductTemplate(template);
        return 1;
    }
}
