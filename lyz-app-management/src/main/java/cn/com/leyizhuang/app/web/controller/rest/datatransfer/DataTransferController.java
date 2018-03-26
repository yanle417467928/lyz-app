package cn.com.leyizhuang.app.web.controller.rest.datatransfer;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrder;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.datatransfer.DataTransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by panjie on 2018/3/24.
 */
@RestController
@Slf4j
public class DataTransferController {

    @Resource
    private DataTransferService dataTransferService;

    @Resource
    private AppStoreService storeService;

    @Resource
    private AppOrderService orderService;

    @RequestMapping(value = "/data/transfer", method = RequestMethod.GET)
    public String dataTransfer() {
        log.info("开始处理订单导入job,当前时间:{}", new Date());
        // *********************** 订单迁移处理 ***************
        List<String> storeMainOrderNumberList;
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 10, 01, 0, 0, 0);
        Date startTime = calendar.getTime();
        Date endTime = new Date();
        //查询所有待处理的订单号
        storeMainOrderNumberList = dataTransferService.getTransferStoreMainOrderNumber(startTime, endTime);

        // *********************** 处理 订单基础表 ord_base_info *****************
        for (String mainOrderNumber : storeMainOrderNumberList) {
            TdOrder tdOrder = dataTransferService.getMainOrderInfoByMainOrderNumber(mainOrderNumber);

            log.info("开始处理订单导入job,");

            if (null != tdOrder) {
                OrderBaseInfo orderBaseInfo = new OrderBaseInfo();
                orderBaseInfo.setOrderNumber(mainOrderNumber);
                orderBaseInfo.setOrderType(AppOrderType.SHIPMENT);
                orderBaseInfo.setCreateTime(null != tdOrder.getPayTime() ? tdOrder.getPayTime() : tdOrder.getOrderTime());
                switch (tdOrder.getStatusId().intValue()) {
                    case 3:
                        orderBaseInfo.setStatus(AppOrderStatus.PENDING_RECEIVE);
                        break;
                    case 4:
                        orderBaseInfo.setStatus(AppOrderStatus.FINISHED);
                        break;
                    case 5:
                        orderBaseInfo.setStatus(AppOrderStatus.FINISHED);
                        break;
                    case 6:
                        orderBaseInfo.setStatus(AppOrderStatus.FINISHED);
                        break;
                    default:
                        break;
                }
                //todo 设置订单物流状态

                orderBaseInfo.setPickUpCode("0000");
                switch (tdOrder.getDeliverTypeTitle()) {
                    case "送货上门":
                        orderBaseInfo.setDeliveryType(AppDeliveryType.HOUSE_DELIVERY);
                        break;
                    case "门店自提":
                        orderBaseInfo.setDeliveryType(AppDeliveryType.SELF_TAKE);
                        break;
                    default:
                        break;
                }

                if (mainOrderNumber.contains("FIT")) {
                    orderBaseInfo.setOrderSubjectType(AppOrderSubjectType.FIT);
                } else {
                    orderBaseInfo.setOrderSubjectType(AppOrderSubjectType.STORE);
                }
                //设置订单创建者身份类型
                if (mainOrderNumber.contains("FIT")) {
                    orderBaseInfo.setCreatorIdentityType(AppIdentityType.DECORATE_MANAGER);
                } else {
                    if (tdOrder.getIsSellerOrder()) {
                        orderBaseInfo.setCreatorIdentityType(AppIdentityType.SELLER);
                    } else {
                        orderBaseInfo.setCreatorIdentityType(AppIdentityType.CUSTOMER);
                    }
                }
                //设置订单创建者信息
                switch (orderBaseInfo.getCreatorIdentityType()) {
                    case DECORATE_MANAGER:
                        AppEmployee fitEmployee = dataTransferService.findFitEmployeeInfoById(tdOrder.getUserId());
                        if (null != fitEmployee) {
                            orderBaseInfo.setCreatorId(fitEmployee.getEmpId());
                            orderBaseInfo.setCreatorName(fitEmployee.getName());
                            orderBaseInfo.setCreatorPhone(fitEmployee.getMobile());
                        } else {
                            continue;
                        }
                        break;
                    case SELLER:
                        AppEmployee storeEmployee = dataTransferService.findStoreEmployeeById(tdOrder.getSellerId());
                        if (null != storeEmployee) {
                            orderBaseInfo.setCreatorId(storeEmployee.getEmpId());
                            orderBaseInfo.setCreatorName(storeEmployee.getName());
                            orderBaseInfo.setCreatorPhone(storeEmployee.getMobile());
                            orderBaseInfo.setSalesConsultId(orderBaseInfo.getCreatorId());
                            orderBaseInfo.setSalesConsultName(orderBaseInfo.getSalesConsultName());
                            orderBaseInfo.setSalesConsultPhone(orderBaseInfo.getCreatorPhone());
                            AppCustomer customer = dataTransferService.findCustomerById(tdOrder.getUserId());
                            orderBaseInfo.setCustomerId(customer.getCusId());
                            orderBaseInfo.setCustomerName(customer.getName());
                            orderBaseInfo.setCustomerPhone(customer.getMobile());
                        } else {
                            continue;
                        }
                        break;
                    case CUSTOMER:
                        AppCustomer customer = dataTransferService.findCustomerById(tdOrder.getUserId());
                        if (null != customer) {
                            orderBaseInfo.setCreatorId(customer.getCusId());
                            orderBaseInfo.setCreatorName(customer.getName());
                            orderBaseInfo.setCreatorPhone(customer.getMobile());
                            orderBaseInfo.setCustomerId(orderBaseInfo.getCreatorId());
                            orderBaseInfo.setCustomerName(orderBaseInfo.getCreatorName());
                            orderBaseInfo.setCustomerPhone(orderBaseInfo.getCreatorPhone());
                            AppEmployee sellerEmployee = dataTransferService.findStoreEmployeeById(tdOrder.getSellerId());
                            orderBaseInfo.setSalesConsultId(sellerEmployee.getEmpId());
                            orderBaseInfo.setSalesConsultName(sellerEmployee.getName());
                            orderBaseInfo.setSalesConsultPhone(sellerEmployee.getMobile());
                        } else {
                            continue;
                        }
                        break;
                    default:
                        break;
                }
                //设置门店信息
                AppStore store = storeService.findByStoreCode(tdOrder.getDiySiteCode());
                orderBaseInfo.setStoreId(store.getStoreId());
                orderBaseInfo.setStoreCode(store.getStoreCode());
                orderBaseInfo.setStoreOrgId(store.getStoreOrgId());
                orderBaseInfo.setStoreStructureCode(store.getStoreStructureCode());
                orderBaseInfo.setTotalGoodsPrice(tdOrder.getTotalGoodsPrice());
                orderBaseInfo.setIsEvaluated(Boolean.FALSE);
                orderBaseInfo.setRemark(tdOrder.getRemark());
                orderBaseInfo.setCityName(tdOrder.getCity());
                if (orderBaseInfo.getCityName().equals("成都市")) {
                    orderBaseInfo.setCityId(1L);
                    orderBaseInfo.setSobId(2121L);
                } else {
                    orderBaseInfo.setCityId(2L);
                    orderBaseInfo.setSobId(2033L);
                }
                orderBaseInfo.setSalesNumber(tdOrder.getPaperSalesNumber());
                orderBaseInfo.setIsRecordSales(Boolean.TRUE);

                System.out.println("orderBaseInfo:\n" + orderBaseInfo);
                orderService.saveOrderBaseInfo(orderBaseInfo);
            }
        }
        log.info("订单导入job处理完成,当前时间:{}", new Date());
        return "success";
    }

    @RequestMapping(value = "/data/transfer/arrearsAudit", method = RequestMethod.GET)
    public String transferArrearsAudit() {
        log.info("开始处理订单审核信息导入job,当前时间:{}", new Date());
        // *********************** 订单迁移处理 ***************
        List<OrderBaseInfo> orderNumberList = this.dataTransferService.findNewOrderNumber();
        if (null != orderNumberList && orderNumberList.size() > 0) {
            for (int i = 0; i < orderNumberList.size(); i++) {
                String orderNumber = orderNumberList.get(i).getOrderNumber();
                try {
                    this.dataTransferService.transferArrearsAudit(orderNumber);
                }catch (Exception e){
                    e.printStackTrace();
                    log.info("订单卷信息导入错误,订单号:{}", orderNumber);
                }

            }
        }
        log.info("订单审核信息导入job处理完成,当前时间:{}", new Date());
        return "success";
    }

    @RequestMapping(value = "/data/transfer/orderCoupon", method = RequestMethod.GET)
    public String transferCoupon() {
        log.info("开始处理订单卷信息导入job,当前时间:{}", new Date());
        // *********************** 订单迁移处理 ***************
        List<OrderBaseInfo> orderNumberList = this.dataTransferService.findNewOrderNumber();
        if (null != orderNumberList && orderNumberList.size() > 0) {
            for (int i = 0; i < orderNumberList.size(); i++) {
                try {
                    this.dataTransferService.transferCoupon(orderNumberList.get(i));
                }catch (Exception e){
                    e.printStackTrace();
                    log.info("订单卷信息导入错误,订单号:{}", orderNumberList.get(i).getOrderNumber());
                }

            }
        }
        log.info("订单卷信息导入job处理完成,当前时间:{}", new Date());
        return "success";
    }

    @RequestMapping(value = "/data/transfer/orderbilling", method = RequestMethod.GET)
    public String dataTransferOrderBillingDeatails() {
        log.info("开始处理订单账单导入,当前时间:{}", new Date());
        dataTransferService.transferOrderBillingDetails();
        return "success";
    }
}
