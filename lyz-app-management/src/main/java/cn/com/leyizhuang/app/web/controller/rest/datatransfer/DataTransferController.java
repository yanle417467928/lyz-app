package cn.com.leyizhuang.app.web.controller.rest.datatransfer;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrderDeliveryTimeSeqDetail;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrderSmall;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.AppCustomerService;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.datatransfer.DataTransferService;
import cn.com.leyizhuang.app.foundation.service.datatransfer.OrderGoodsTransferService;
import com.tinify.Exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by panjie on 2018/3/24.
 */
@RestController
@Slf4j
public class DataTransferController {

    @Resource
    private DataTransferService dataTransferService;

    @Resource
    private OrderGoodsTransferService orderGoodsTransferService;

    @Resource
    private AppStoreService storeService;

    @Resource
    private AppOrderService orderService;

    @Resource
    private AppEmployeeService employeeService;

    @Resource
    private AppCustomerService customerService;


    @RequestMapping(value = "/data/transfer/orderBaseInfo", method = RequestMethod.GET)
    public String dataTransfer() throws ExecutionException, InterruptedException {
        log.info("开始处理订单导入job,当前时间:{}", new Date());
        // *********************** 订单迁移处理 ***************
        Map<String, String> unhandledOrderNumberMap = new HashMap<>(100);
        List<TdOrderSmall> storeMainOrderNumberList;
        List<AppEmployee> employeeList = employeeService.findAllSeller();
        List<AppCustomer> customerList = customerService.findAllCustomer();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, Calendar.NOVEMBER, 1, 0, 0, 0);
        Date startTime = calendar.getTime();
        Date endTime = new Date();
        //查询所有待处理的订单号
        //storeMainOrderNumberList = dataTransferService.getTransferStoreMainOrderNumber(startTime, endTime);
        storeMainOrderNumberList = dataTransferService.getPendingTransferOrder(startTime, endTime);
        //多线程处理任务集
        if (storeMainOrderNumberList == null || storeMainOrderNumberList.isEmpty()) {
            return "success with no data processed!";
        }
        int size = storeMainOrderNumberList.size();
        int nThreads = 6;
        AtomicInteger countLine = new AtomicInteger();
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        List<Future<Map<String, String>>> futures = new ArrayList<>(nThreads);

        for (int i = 0; i < nThreads; i++) {
            final List<TdOrderSmall> subList = storeMainOrderNumberList.subList(size / nThreads * i, size / nThreads * (i + 1));
            Callable<Map<String, String>> task = () -> {
                Map<String, String> unhandledMap = new HashMap<>(100);
                for (TdOrderSmall tdOrder : subList) {
                    try {
                        countLine.addAndGet(1);
                        //TdOrder tdOrder = dataTransferService.getMainOrderInfoByMainOrderNumber(mainOrderNumber);
                        OrderBaseInfo orderBaseInfo = new OrderBaseInfo();
                        orderBaseInfo.setOrderNumber(tdOrder.getMainOrderNumber());
                        orderBaseInfo.setOrderType(AppOrderType.SHIPMENT);
                        orderBaseInfo.setCreateTime(null != tdOrder.getPayTime() ? tdOrder.getPayTime() : tdOrder.getOrderTime());
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
                        orderBaseInfo.setPickUpCode("0000");
                        if (orderBaseInfo.getDeliveryType() == AppDeliveryType.SELF_TAKE) {
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
                        } else {
                            switch (tdOrder.getStatusId().intValue()) {
                                case 3:
                                    orderBaseInfo.setStatus(AppOrderStatus.PENDING_SHIPMENT);
                                    break;
                                case 4:
                                    orderBaseInfo.setStatus(AppOrderStatus.PENDING_RECEIVE);
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
                        }
                        //todo 设置订单物流状态
                        if (orderBaseInfo.getDeliveryType() == AppDeliveryType.SELF_TAKE) {
                            orderBaseInfo.setDeliveryStatus(LogisticStatus.INITIAL);
                        } else {
                            if (orderBaseInfo.getStatus() == AppOrderStatus.PENDING_SHIPMENT) {
                                TdOrderDeliveryTimeSeqDetail detail = dataTransferService.findDeliveryStatusByMainOrderNumber(tdOrder.getMainOrderNumber());
                                if (null != detail) {
                                    switch (detail.getOperationType()) {
                                        case "处理中":
                                            orderBaseInfo.setDeliveryStatus(LogisticStatus.INITIAL);
                                            break;
                                        case "定位":
                                            orderBaseInfo.setDeliveryStatus(LogisticStatus.ALREADY_POSITIONED);
                                            break;
                                        case "拣货":
                                            orderBaseInfo.setDeliveryStatus(LogisticStatus.PICKING_GOODS);
                                            break;
                                        case "装车":
                                            orderBaseInfo.setDeliveryStatus(LogisticStatus.LOADING);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            } else if (orderBaseInfo.getStatus() == AppOrderStatus.PENDING_RECEIVE || orderBaseInfo.getStatus() == AppOrderStatus.FINISHED) {
                                orderBaseInfo.setDeliveryStatus(LogisticStatus.SEALED_CAR);
                            }

                        }
                        if (tdOrder.getMainOrderNumber().contains("FIT")) {
                            orderBaseInfo.setOrderSubjectType(AppOrderSubjectType.FIT);
                        } else {
                            orderBaseInfo.setOrderSubjectType(AppOrderSubjectType.STORE);
                        }
                        //设置订单创建者身份类型
                        if (tdOrder.getMainOrderNumber().contains("FIT")) {
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
                                AppEmployee fitEmployee = dataTransferService.findFitEmployeeInfoById(tdOrder.getRealUserId());
                                if (null != fitEmployee) {
                                    orderBaseInfo.setCreatorId(fitEmployee.getEmpId());
                                    orderBaseInfo.setCreatorName(fitEmployee.getName());
                                    orderBaseInfo.setCreatorPhone(fitEmployee.getMobile());
                                } else {
                                    unhandledMap.put(tdOrder.getMainOrderNumber(), "该订单没有找到装饰经理信息");
                                    log.warn("装饰公司订单：{} 经理信息没找到", tdOrder.getMainOrderNumber());
                                    //throw new RuntimeException("装饰公司订单：" + mainOrderNumber + "经理信息没找到");
                                    continue;
                                }
                                break;
                            case SELLER:
                                //AppEmployee storeEmployee = dataTransferService.findStoreEmployeeById(tdOrder.getSellerId());
                                List<AppEmployee> filterEmployeeList = employeeList.stream().filter(p -> p.getMobile().equals(tdOrder.getSellerUsername())).
                                        collect(Collectors.toList());
                                if (null != filterEmployeeList && !filterEmployeeList.isEmpty()) {
                                    AppEmployee storeEmployee = filterEmployeeList.get(0);
                                    orderBaseInfo.setCreatorId(storeEmployee.getEmpId());
                                    orderBaseInfo.setCreatorName(storeEmployee.getName());
                                    orderBaseInfo.setCreatorPhone(storeEmployee.getMobile());
                                    orderBaseInfo.setSalesConsultId(orderBaseInfo.getCreatorId());
                                    orderBaseInfo.setSalesConsultName(orderBaseInfo.getSalesConsultName());
                                    orderBaseInfo.setSalesConsultPhone(orderBaseInfo.getCreatorPhone());
                                    //AppCustomer customer = dataTransferService.findCustomerById(tdOrder.getUserId());
                                    // AppCustomer customer = dataTransferService.findCustomerByCustomerMobile(tdOrder.getRealUserUsername());
                                    List<AppCustomer> filterCustomerList = customerList.stream().filter(p -> p.getMobile().equals(tdOrder.getRealUserUsername())).
                                            collect(Collectors.toList());
                                    if (null != filterCustomerList && !filterCustomerList.isEmpty()) {
                                        AppCustomer customer = filterCustomerList.get(0);
                                        orderBaseInfo.setCustomerId(customer.getCusId());
                                        orderBaseInfo.setCustomerName(customer.getName());
                                        orderBaseInfo.setCustomerPhone(customer.getMobile());
                                        orderBaseInfo.setCustomerType(customer.getCustomerType());
                                    } else {
                                        unhandledMap.put(tdOrder.getMainOrderNumber(), "该订单没有找到顾客信息");
                                        log.warn("订单：{} 顾客信息没找到", tdOrder.getMainOrderNumber());
                                        //throw new RuntimeException("订单：" + mainOrderNumber + " 顾客信息没找到");
                                    }
                                } else {
                                    unhandledMap.put(tdOrder.getMainOrderNumber(), "该订单没有找到导购信息");
                                    log.warn("门店订单：{} 导购信息没找到", tdOrder.getMainOrderNumber());
                                }
                                break;
                            case CUSTOMER:
                                //AppCustomer customer = dataTransferService.findCustomerById(tdOrder.getUserId());
                                // AppCustomer customer = dataTransferService.findCustomerByCustomerMobile(tdOrder.getRealUserUsername());
                                List<AppCustomer> filterCustomerList = customerList.stream().filter(p -> p.getMobile().equals(tdOrder.getRealUserUsername())).
                                        collect(Collectors.toList());
                                if (null != filterCustomerList && !filterCustomerList.isEmpty()) {
                                    AppCustomer customer = filterCustomerList.get(0);
                                    orderBaseInfo.setCreatorId(customer.getCusId());
                                    orderBaseInfo.setCreatorName(customer.getName());
                                    orderBaseInfo.setCreatorPhone(customer.getMobile());
                                    orderBaseInfo.setCustomerId(orderBaseInfo.getCreatorId());
                                    orderBaseInfo.setCustomerName(orderBaseInfo.getCreatorName());
                                    orderBaseInfo.setCustomerPhone(orderBaseInfo.getCreatorPhone());
                                    orderBaseInfo.setCustomerType(customer.getCustomerType());
                                    //AppEmployee sellerEmployee = dataTransferService.findStoreEmployeeById(tdOrder.getSellerId());
                                    List<AppEmployee> filterSellerList = employeeList.stream().filter(p -> p.getMobile().equals(tdOrder.getSellerUsername())).
                                            collect(Collectors.toList());
                                    if (null != filterSellerList && !filterSellerList.isEmpty()) {
                                        AppEmployee sellerEmployee = filterSellerList.get(0);

                                        orderBaseInfo.setSalesConsultId(sellerEmployee.getEmpId());
                                        orderBaseInfo.setSalesConsultName(sellerEmployee.getName());
                                        orderBaseInfo.setSalesConsultPhone(sellerEmployee.getMobile());
                                    } else {
                                        unhandledMap.put(tdOrder.getMainOrderNumber(), "该订单没有找到导购信息");
                                        log.warn("门店订单：{} 导购信息没找到", tdOrder.getMainOrderNumber());
                                        //throw new RuntimeException("门店订单：" + mainOrderNumber + "导购信息没找到");
                                    }
                                } else {
                                    unhandledMap.put(tdOrder.getMainOrderNumber(), "该订单没有找到顾客信息");
                                    log.warn("门店订单：{} 顾客信息没找到", tdOrder.getMainOrderNumber());
                                    //throw new RuntimeException("门店订单：" + mainOrderNumber + "顾客信息没找到");
                                }
                                break;
                            default:
                                break;
                        }
                        //设置门店信息
                        AppStore store = storeService.findByStoreCode(tdOrder.getDiySiteCode());
                        if (null != store) {
                            orderBaseInfo.setStoreId(store.getStoreId());
                            orderBaseInfo.setStoreCode(store.getStoreCode());
                            orderBaseInfo.setStoreOrgId(store.getStoreOrgId());
                            orderBaseInfo.setStoreStructureCode(store.getStoreStructureCode());
                        } else {
                            unhandledMap.put(tdOrder.getMainOrderNumber(), "该订单没有找到顾客信息");
                            log.warn("门店订单：{} 顾客信息没找到", tdOrder.getMainOrderNumber());
                            continue;
                        }
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

                        //System.out.println("orderBaseInfo:\n" + orderBaseInfo);
                        System.out.println(countLine);
                        orderService.saveOrderBaseInfo(orderBaseInfo);
                    } catch (Exception e) {
                        log.warn(e.getMessage());
                        log.warn("相关订单号:{}", tdOrder.getMainOrderNumber());
                    }
                }
                return unhandledMap;
            };
            futures.add(executorService.submit(task));
        }
        for (Future<Map<String, String>> future : futures) {
            unhandledOrderNumberMap.putAll(future.get());
        }
        executorService.shutdown();
        System.out.println("未处理或处理失败的数据条数:" + unhandledOrderNumberMap.size());
        for (Map.Entry<String, String> entry : unhandledOrderNumberMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        log.info("订单导入job处理完成,当前时间:{}", new Date());
        return unhandledOrderNumberMap.toString();

        // *********************** 处理 订单基础表 ord_base_info *****************
        /*for (String mainOrderNumber : storeMainOrderNumberList) {
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
                if (orderBaseInfo.getDeliveryType() == AppDeliveryType.SELF_TAKE) {
                    orderBaseInfo.setDeliveryStatus(LogisticStatus.INITIAL);
                } else {
                    if (orderBaseInfo.getStatus() == AppOrderStatus.PENDING_SHIPMENT) {
                        TdOrderDeliveryTimeSeqDetail detail = dataTransferService.findDeliveryStatusByMainOrderNumber(mainOrderNumber);
                        if (null != detail) {
                            switch (detail.getOperationType()) {
                                case "处理中":
                                    orderBaseInfo.setDeliveryStatus(LogisticStatus.INITIAL);
                                    break;
                                case "定位":
                                    orderBaseInfo.setDeliveryStatus(LogisticStatus.ALREADY_POSITIONED);
                                    break;
                                case "拣货":
                                    orderBaseInfo.setDeliveryStatus(LogisticStatus.PICKING_GOODS);
                                    break;
                                case "装车":
                                    orderBaseInfo.setDeliveryStatus(LogisticStatus.LOADING);
                                    break;
                                default:
                                    break;
                            }
                        }
                    } else if (orderBaseInfo.getStatus() == AppOrderStatus.PENDING_RECEIVE || orderBaseInfo.getStatus() == AppOrderStatus.FINISHED) {
                        orderBaseInfo.setDeliveryStatus(LogisticStatus.SEALED_CAR);
                    }

                }
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
                            unhandledOrderNumberMap.put(mainOrderNumber, "该订单没有找到装饰经理信息");
                            log.warn("装饰公司订单：{} 经理信息没找到", mainOrderNumber);
                            //throw new RuntimeException("装饰公司订单：" + mainOrderNumber + "经理信息没找到");
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
                            //AppCustomer customer = dataTransferService.findCustomerById(tdOrder.getUserId());
                            AppCustomer customer = dataTransferService.findCustomerByCustomerMobile(tdOrder.getRealUserUsername());
                            if (null != customer) {
                                orderBaseInfo.setCustomerId(customer.getCusId());
                                orderBaseInfo.setCustomerName(customer.getName());
                                orderBaseInfo.setCustomerPhone(customer.getMobile());
                                orderBaseInfo.setCustomerType(customer.getCustomerType());
                            } else {
                                unhandledOrderNumberMap.put(mainOrderNumber, "该订单没有找到顾客信息");
                                log.warn("订单：{} 顾客信息没找到", mainOrderNumber);
                                //throw new RuntimeException("订单：" + mainOrderNumber + " 顾客信息没找到");
                            }

                        } else {
                            unhandledOrderNumberMap.put(mainOrderNumber, "该订单没有找到导购信息");
                            log.warn("门店订单：{} 导购信息没找到", mainOrderNumber);
                            //throw new RuntimeException("门店订单：" + mainOrderNumber + "导购信息没找到");
                        }
                        break;
                    case CUSTOMER:
                        //AppCustomer customer = dataTransferService.findCustomerById(tdOrder.getUserId());
                        AppCustomer customer = dataTransferService.findCustomerByCustomerMobile(tdOrder.getRealUserUsername());
                        if (null != customer) {
                            orderBaseInfo.setCreatorId(customer.getCusId());
                            orderBaseInfo.setCreatorName(customer.getName());
                            orderBaseInfo.setCreatorPhone(customer.getMobile());
                            orderBaseInfo.setCustomerId(orderBaseInfo.getCreatorId());
                            orderBaseInfo.setCustomerName(orderBaseInfo.getCreatorName());
                            orderBaseInfo.setCustomerPhone(orderBaseInfo.getCreatorPhone());
                            orderBaseInfo.setCustomerType(customer.getCustomerType());
                            AppEmployee sellerEmployee = dataTransferService.findStoreEmployeeById(tdOrder.getSellerId());
                            if (null != sellerEmployee) {
                                orderBaseInfo.setSalesConsultId(sellerEmployee.getEmpId());
                                orderBaseInfo.setSalesConsultName(sellerEmployee.getName());
                                orderBaseInfo.setSalesConsultPhone(sellerEmployee.getMobile());
                            } else {
                                unhandledOrderNumberMap.put(mainOrderNumber, "该订单没有找到导购信息");
                                log.warn("门店订单：{} 导购信息没找到", mainOrderNumber);
                                //throw new RuntimeException("门店订单：" + mainOrderNumber + "导购信息没找到");
                            }

                        } else {
                            unhandledOrderNumberMap.put(mainOrderNumber, "该订单没有找到顾客信息");
                            log.warn("门店订单：{} 顾客信息没找到", mainOrderNumber);
                            //throw new RuntimeException("门店订单：" + mainOrderNumber + "顾客信息没找到");
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
            } else {
                unhandledOrderNumberMap.put(mainOrderNumber, "该订单td_order_copy信息没有找到");
            }
        }*/

    }

    @RequestMapping(value = "/data/transfer/arrearsAudit", method = RequestMethod.GET)
    public String transferArrearsAudit() {
        log.info("开始处理订单审核信息导入job,当前时间:{}", new Date());
        // *********************** 订单迁移处理 ***************
        List<OrderBaseInfo> orderNumberList = this.dataTransferService.findNewOrderNumber();
        List<String> errorOrderNumber = new ArrayList<>();
        Integer num = 0;
        List<String> error = new ArrayList<>();
        if (null != orderNumberList && orderNumberList.size() > 0) {
            for (int i = 0; i < orderNumberList.size(); i++) {
                String orderNumber = orderNumberList.get(i).getOrderNumber();
                try {
                    Integer flag = this.dataTransferService.transferArrearsAudit(orderNumber);
                    if (flag > 0) {
                        errorOrderNumber.add(orderNumber + "--" + flag);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    error.add(e.getMessage());
                    errorOrderNumber.add(orderNumber + "--e");
                    log.info("订单卷信息导入错误,订单号:{}", orderNumber);
                }
                num += 1;
            }
        }
        log.info("订单卷信息err:{}", error);
        log.info("订单卷信息导入执行订单数num:{}", num);
        log.info("订单审核信息导入未成功订单errorOrderNumber:{}", errorOrderNumber);
        log.info("订单审核信息导入job处理完成,当前时间:{}", new Date());
        return "success";
    }

    @RequestMapping(value = "/data/transfer/orderCoupon", method = RequestMethod.GET)
    public String transferCoupon() {
        log.info("开始处理订单卷信息导入job,当前时间:{}", new Date());
        // *********************** 订单迁移处理 ***************
        List<OrderBaseInfo> orderNumberList = this.dataTransferService.findNewOrderNumber();
        List<String> errorOrderNumber = new ArrayList<>();
        Integer num = 0;
        List<String> error = new ArrayList<>();
        if (null != orderNumberList && orderNumberList.size() > 0) {
            for (int i = 0; i < orderNumberList.size(); i++) {
                try {
                    Integer flag = this.dataTransferService.transferCoupon(orderNumberList.get(i));
                    if (flag > 0) {
                        errorOrderNumber.add(orderNumberList.get(i).getOrderNumber() + "--" + flag);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    error.add(e.getMessage());
                    errorOrderNumber.add(orderNumberList.get(i).getOrderNumber() + "--e");
                    log.info("订单卷信息导入错误,订单号:{}", orderNumberList.get(i).getOrderNumber());
                }
                num += 1;
            }
        }
        log.info("订单卷信息err:{}", error);
        log.info("订单卷信息导入执行订单数num:{}", num);
        log.info("订单卷信息导入未成功订单errorOrderNumber:{}", errorOrderNumber);
        log.info("订单卷信息导入job处理完成,当前时间:{}", new Date());
        return "success";
    }

    @RequestMapping(value = "/data/transfer/orderbilling", method = RequestMethod.GET)
    public String dataTransferOrderBillingDeatails() {
        log.info("开始处理订单账单导入,当前时间:{}", new Date());
        Integer num = dataTransferService.transferOrderBillingDetails();
        log.info("开始处理订单账单导入单数num:{}", num);
        return "success";
    }

    @RequestMapping(value = "/data/transfer/orderGoodsInfo", method = RequestMethod.GET)
    public void orderGoodsInfoTransfer() {

        orderGoodsTransferService.transferAll();
    }
}
