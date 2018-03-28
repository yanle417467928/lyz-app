package cn.com.leyizhuang.app.web.controller.rest.datatransfer;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.LogisticStatus;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrderLogistics;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderJxPriceDifferenceReturnDetails;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderLogisticsInfo;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.OrderDeliveryInfoDetailsService;
import cn.com.leyizhuang.app.foundation.service.datatransfer.DataTransferService;
import cn.com.leyizhuang.common.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jerry.Ren
 * create 2018-03-24 18:00
 * desc:
 **/
@RestController
@Slf4j
public class DeliveryInfoController {

    @Resource
    private OrderDeliveryInfoDetailsService orderDeliveryInfoDetailsService;

    @Autowired
    private DataTransferService dataTransferService;
    @Autowired
    private AppStoreService appStoreService;
    @Autowired
    private AppOrderService appOrderService;

    // 线程池
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     * 转换物流明细表
     */
    @RequestMapping(value = "/app/resend/wms/test/deliveryInfo", method = RequestMethod.GET)
    public String transformDeliveryInfoDetails() {

        int size = 0;
        for (int i = 0; i < 200; i++) {
            Boolean hasNotDatas = this.saveOrderDeliveryInfoDetails(size);
            if (hasNotDatas) {
                return "********************转换物流明细表结束*************************";
            }
            size += 1000;
        }
        log.info("********************转换物流明细表结束*************************");
        return "按理说不应该执行到这儿!";
    }


    /**
     * 运行此方法需要订单基础表(ord_base_info)门店基础表(st_store)数据支持
     */
    @RequestMapping(value = "/app/resend/wms/test/JxPrice", method = RequestMethod.GET)
    public String transformJxPriceDifferenceReturnDetails() {

        int size = 0;

        for (int i = 0; i < 200; i++) {
            Boolean hasNotDatas = this.saveOrderJxPriceDifference(size);
            if (hasNotDatas) {
                return "********************转换返还经销差价信息表结束*************************";
            }
            size += 1000;
        }
        log.info("********************转换返还经销差价信息表结束*************************");
        return "按理说不应该执行到这儿!";
    }

    private Boolean saveOrderDeliveryInfoDetails(int size) {
        List<TdDeliveryInfoDetails> entityList = dataTransferService.queryDeliveryTimeSeqBySize(size);

        try {
            if (AssertUtil.isNotEmpty(entityList)) {
                for (TdDeliveryInfoDetails tdDeliveryInfoDetails : entityList) {

                    OrderDeliveryInfoDetails deliveryInfoDetails = new OrderDeliveryInfoDetails();

                    switch (tdDeliveryInfoDetails.getOperationType()) {
                        case "处理中": {
                            deliveryInfoDetails.setLogisticStatus(LogisticStatus.INITIAL);
                            break;
                        }
                        case "定位": {
                            deliveryInfoDetails.setLogisticStatus(LogisticStatus.ALREADY_POSITIONED);
                            break;
                        }
                        case "拣货": {
                            deliveryInfoDetails.setLogisticStatus(LogisticStatus.PICKING_GOODS);
                            break;
                        }
                        case "装车": {
                            deliveryInfoDetails.setLogisticStatus(LogisticStatus.LOADING);
                            break;
                        }
                        case "封车": {
                            deliveryInfoDetails.setLogisticStatus(LogisticStatus.SEALED_CAR);
                            List<TdDeliveryInfoDetails> deliveryInfoDetailList = dataTransferService.queryDeliveryInfoDetailByOrderNumber(tdDeliveryInfoDetails.getMainOrderNumber());
                            if (AssertUtil.isNotEmpty(deliveryInfoDetailList)) {
                                TdDeliveryInfoDetails deliveryInfoDetail = deliveryInfoDetailList.get(0);
                                deliveryInfoDetails.setOperatorNo(deliveryInfoDetail.getDriver());
                                deliveryInfoDetails.setTaskNo(deliveryInfoDetail.getTaskNo());
                                deliveryInfoDetails.setWarehouseNo(deliveryInfoDetail.getWhNo());
                            }
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                    deliveryInfoDetails.setDescription(tdDeliveryInfoDetails.getOperationDescription());
                    deliveryInfoDetails.setOrderNo(tdDeliveryInfoDetails.getMainOrderNumber());
                    deliveryInfoDetails.setCreateTime(tdDeliveryInfoDetails.getOperationTime());
                    deliveryInfoDetails.setIsRead(true);

                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            orderDeliveryInfoDetailsService.addOrderDeliveryInfoDetails(deliveryInfoDetails);
                        }
                    });
                }
            } else {
                return true;
            }
        } catch (Exception e) {
            log.debug("{}", e);
            return true;
        }
        return false;
    }

    private Boolean saveOrderJxPriceDifference(int size) {
        List<TdDeliveryInfoDetails> tdOrderList = dataTransferService.queryTdOrderListBySize(size);
        try {
            if (AssertUtil.isNotEmpty(tdOrderList)) {
                for (TdDeliveryInfoDetails tdOrder : tdOrderList) {
                    OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(tdOrder.getMainOrderNumber());
                    if (AssertUtil.isNotEmpty(orderBaseInfo)) {
                        List<TdDeliveryInfoDetails> tdOrderGoodsList = dataTransferService.queryOrderGoodsListByOrderNumber(tdOrder.getId());
                        AppStore store = appStoreService.findByStoreCode(tdOrder.getDiySiteCode());
                        if (AssertUtil.isNotEmpty(tdOrderGoodsList)) {
                            for (TdDeliveryInfoDetails tdOrderGoods : tdOrderGoodsList) {

                                OrderJxPriceDifferenceReturnDetails returnDetails = new OrderJxPriceDifferenceReturnDetails();

                                returnDetails.setOid(orderBaseInfo.getId());
                                returnDetails.setOrderNumber(tdOrder.getMainOrderNumber());
                                returnDetails.setCreateTime(tdOrder.getPayTime());
                                if (store != null) {
                                    returnDetails.setStoreCode(store.getStoreCode());
                                    returnDetails.setStoreId(store.getStoreId());
                                }
                                returnDetails.setSku(tdOrderGoods.getSku());
                                returnDetails.setUnitPrice(tdOrderGoods.getJxDif());
                                returnDetails.setQuantity(tdOrderGoods.getQuantity());
                                returnDetails.setAmount(tdOrderGoods.getDifTotal());

                                executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {

                                        appOrderService.saveOrderJxPriceDifferenceReturnDetails(returnDetails);
                                    }
                                });
                            }
                        }
                        //转物流明细需要订单状态,所以拿到这里添加
                        int status = tdOrder.getStatusId().intValue();
                        String orderNumber = tdOrder.getMainOrderNumber();
                        this.addOtherDeliveryInfoByOrderStatus(status, orderNumber);
                    }
                }
            } else {
                return true;
            }
        } catch (Exception e) {
            log.debug("{}", e);
            return true;
        }
        return false;
    }




    private void addOtherDeliveryInfoByOrderStatus(int status, String orderNumber) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (status == 3) {
                    OrderDeliveryInfoDetails orderDeliveryInfoDetails = new OrderDeliveryInfoDetails();
                    orderDeliveryInfoDetails.setDeliveryInfo(orderNumber, LogisticStatus.SEALED_CAR, "商家已封车完成！", "已封车",
                            "", "", "", "");
                    orderDeliveryInfoDetails.setIsRead(true);
                    orderDeliveryInfoDetailsService.addOrderDeliveryInfoDetails(orderDeliveryInfoDetails);
                } else if (status == 4 || status == 5 || status == 6) {
                    OrderDeliveryInfoDetails orderDeliveryInfoDetails = new OrderDeliveryInfoDetails();
                    orderDeliveryInfoDetails.setDeliveryInfo(orderNumber, LogisticStatus.CONFIRM_ARRIVAL, "确认到货！", "送达",
                            "", "", "", "");
                    orderDeliveryInfoDetails.setIsRead(true);
                    orderDeliveryInfoDetailsService.addOrderDeliveryInfoDetails(orderDeliveryInfoDetails);
                }
            }
        });
    }
}
