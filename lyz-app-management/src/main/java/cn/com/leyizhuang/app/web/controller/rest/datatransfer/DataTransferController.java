package cn.com.leyizhuang.app.web.controller.rest.datatransfer;

import cn.com.leyizhuang.app.foundation.pojo.datatransfer.DataTransferErrorLog;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.service.datatransfer.DataTransferService;
import cn.com.leyizhuang.app.foundation.service.datatransfer.OrderBillingTransferService;
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
    private OrderBillingTransferService orderBillingTransferService;

    @Resource
    private AppStoreService storeService;

    @Resource
    private AppOrderService orderService;

    @Resource
    private AppEmployeeService employeeService;

    @Resource
    private AppCustomerService customerService;


    private static final Date JOB_END_TIME;

    static {

        Calendar c = Calendar.getInstance();
        c.set(2018, Calendar.MAY, 30, 0, 0, 0);
        JOB_END_TIME = c.getTime();
    }

    @RequestMapping(value = "/data/transfer/orderBaseInfo", method = RequestMethod.GET)
    public String dataTransfer() throws ExecutionException, InterruptedException {
        Date startTime = new Date();
        if (startTime.after(JOB_END_TIME)) {
            return "当前时间不在该任务作业周期内!";
        }
        Date endTime;
        log.info("开始处理订单导入job,当前时间:{}", startTime);
        Queue<DataTransferErrorLog> queue = dataTransferService.transferOrderRelevantInfo();
        endTime = new Date();
        log.info("订单导入job执行完成", endTime);
        long from = startTime.getTime();
        long to = endTime.getTime();
        int seconds = (int) ((to - from) / (1000));
        log.info("导入耗时: {} 秒", seconds);
        return "订单导入完成,耗时: {} 秒" + seconds + "\n错误信息如下:\n" + queue.toString();
    }

    @RequestMapping(value = "/data/transfer/orderBaseInfo/one", method = RequestMethod.GET)
    public String dataTransferOne(String orderNo) throws ExecutionException, InterruptedException {
        Date startTime = new Date();
        if (startTime.after(JOB_END_TIME)) {
            return "当前时间不在该任务作业周期内!";
        }
        Date endTime;
        log.info("开始处理订单导入job,当前时间:{}", startTime);
        if (orderNo == null || orderNo.equals("")){
            log.info("订单号不正确");

            return "订单号不正确";
        }
        try {
            dataTransferService.transferOrderRelevantInfo(orderNo);
        }catch (java.lang.Exception e){
            log.info(e.getMessage());
        }

        endTime = new Date();
        log.info("订单导入job执行完成", endTime);
        long from = startTime.getTime();
        long to = endTime.getTime();
        int seconds = (int) ((to - from) / (1000));
        log.info("导入耗时: {} 秒", seconds);
        return "订单导入完成,耗时: {} 秒" + seconds ;
    }

    @RequestMapping(value = "/data/transfer/arrearsAudit", method = RequestMethod.GET)
    public String transferArrearsAudit() {
        log.info("开始处理订单审核信息导入job,当前时间:{}", new Date());
        // *********************** 订单迁移处理 ***************
        List<OrderBaseInfo> orderNumberList = this.dataTransferService.findNewOrderNumberByDeliveryType();
        List<String> error = new ArrayList<>();
        if (null != orderNumberList && orderNumberList.size() > 0) {
            log.info("无订单信息 orderNumberList:{}", orderNumberList);
        }
        int size = orderNumberList.size();
        int nThreads = 6;
        AtomicInteger countLine = new AtomicInteger();
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        List<Future<List<String>>> futures = new ArrayList<>(nThreads);

        for (int i = 0; i < nThreads; i++) {
            final List<OrderBaseInfo> subList = orderNumberList.subList(size / nThreads * i, size / nThreads * (i + 1));
            Callable<List<String>> task = () -> {
                List<String> errorOrderNumber = new ArrayList<>();
                for (int j = 0; j < subList.size(); j++) {
                    String orderNumber = subList.get(j).getOrderNumber();
                    try {
//                        Integer flag = this.dataTransferService.transferArrearsAudit(orderNumber);
//                        if (flag > 0) {
//                            errorOrderNumber.add(orderNumber + "--" + flag);
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        error.add(e.getMessage());
                        errorOrderNumber.add(orderNumber + "--e");
                        log.info("订单审核信息导入错误,订单号:{}", orderNumber);
                    }
                    countLine.addAndGet(1);
                }
                return errorOrderNumber;
            };
            futures.add(executorService.submit(task));
        }
        for (Future<List<String>> future : futures) {
            try {
                error.addAll(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
        log.info("订单审核信息err:{}", error);
        log.info("订单审核信息导入执行订单数num:{}", countLine);
        log.info("订单审核信息导入job处理完成,当前时间:{}", new Date());
        return "success";
    }

    @RequestMapping(value = "/data/transfer/orderCoupon", method = RequestMethod.GET)
    public String transferCoupon() {
        log.info("开始处理订单卷信息导入job,当前时间:{}", new Date());
        // *********************** 订单迁移处理 ***************
        List<OrderBaseInfo> orderNumberList = this.dataTransferService.findNewOrderNumber();
        List<String> error = new ArrayList<>();
        if (null != orderNumberList && orderNumberList.size() > 0) {
            log.info("无订单信息 orderNumberList:{}", orderNumberList);
        }
        int size = orderNumberList.size();
        int nThreads = 6;
        AtomicInteger countLine = new AtomicInteger();
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        List<Future<List<String>>> futures = new ArrayList<>(nThreads);

        for (int i = 0; i < nThreads; i++) {
            final List<OrderBaseInfo> subList = orderNumberList.subList(size / nThreads * i, size / nThreads * (i + 1));
            Callable<List<String>> task = () -> {
                List<String> errorOrderNumber = new ArrayList<>();
                for (int j = 0; j < subList.size(); j++) {
                    OrderBaseInfo orderBaseInfo = subList.get(j);
                    try {
//                        Integer flag = this.dataTransferService.transferCoupon(orderBaseInfo);
//                        if (flag > 0) {
//                            errorOrderNumber.add(orderBaseInfo.getOrderNumber() + "--" + flag);
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        error.add(e.getMessage());
                        errorOrderNumber.add(orderBaseInfo.getOrderNumber() + "--e");
                        log.info("订单卷信息导入错误,订单号:{}", orderBaseInfo);
                    }
                    countLine.addAndGet(1);
                }
                return errorOrderNumber;
            };
            futures.add(executorService.submit(task));
        }
        for (Future<List<String>> future : futures) {
            try {
                error.addAll(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
        log.info("订单卷信息err:{}", error);
        log.info("订单卷信息导入执行订单数num:{}", countLine);
        log.info("订单卷信息导入job处理完成,当前时间:{}", new Date());
        return "success";
    }

    @RequestMapping(value = "/data/transfer/orderbilling", method = RequestMethod.GET)
    public String dataTransferOrderBillingDeatails() {
        log.info("开始处理订单账单导入,当前时间:{}", new Date());
        return "success";
    }

    @RequestMapping(value = "/data/transfer/orderbilling/async", method = RequestMethod.GET)
    public String dataTransferOrderBillingDeatailsAsync() {
        log.info("开始处理订单账单导入,当前时间:{}", new Date());
        orderBillingTransferService.transferOrderBillingDetails();
        log.info("处理订单账单导入结束!时间：{}", new Date());
        return "success";
    }

    @RequestMapping(value = "/data/transfer/orderGoodsInfo", method = RequestMethod.GET)
    public void orderGoodsInfoTransfer() {

        orderGoodsTransferService.transferAll();
    }


    @RequestMapping(value = "/data/transfer/goodsInfo", method = RequestMethod.GET)
    public void goodsInfoTransfer() {
        dataTransferService.goodsInfoTransfer();
    }

    @RequestMapping(value = "/data/transfer/storeInventoryInfo", method = RequestMethod.GET)
    public void storeInventoryInfoTransfer() {
        dataTransferService.storeInventoryInfoTransfer();
    }
}
