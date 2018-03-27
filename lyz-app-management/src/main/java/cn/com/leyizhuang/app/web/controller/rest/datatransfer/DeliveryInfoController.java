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

    /**
     * 转换物流明细表
     */
    @RequestMapping(value = "/app/resend/wms/test/deliveryInfo", method = RequestMethod.GET)
    public void transformDeliveryInfoDetails() {

        int size = 0;
        for (int i = 0; i < 200; i++) {
            Boolean hasNotDatas = this.saveOrderDeliveryInfoDetails(size);
            if (hasNotDatas) {
                return;
            }
            size += 1000;
        }
        log.info("********************转换物流明细表结束*************************");
    }

    @RequestMapping(value = "/app/resend/wms/test/logistcsInfo", method = RequestMethod.GET)
    public void transformOrderLogistcs() {
        int size = 0;
        for (int i = 0; i < 200; i++) {
            Boolean hasNotDatas = this.saveOrderLogistcs(size);
            if (hasNotDatas) {
                return;
            }
            size += 1000;
        }
        log.info("********************转换物流地址信息表结束*************************");
    }

    /**
     * 运行此方法需要订单基础表(ord_base_info)门店基础表(st_store)数据支持
     */
    @RequestMapping(value = "/app/resend/wms/test/JxPrice", method = RequestMethod.GET)
    public void transformJxPriceDifferenceReturnDetails() {

        int size = 0;

        for (int i = 0; i < 200; i++) {
            Boolean hasNotDatas = this.saveOrderJxPriceDifference(size);
            if (hasNotDatas) {
                return;
            }
            size += 1000;
        }
        log.info("********************转换返还经销差价信息表结束*************************");
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
                    orderDeliveryInfoDetailsService.addOrderDeliveryInfoDetails(deliveryInfoDetails);
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
                                appOrderService.saveOrderJxPriceDifferenceReturnDetails(returnDetails);
                            }
                        }
                        //转物流明细需要订单状态,所以拿到这里添加
                        if (tdOrder.getStatusId().intValue() == 3) {
                            OrderDeliveryInfoDetails orderDeliveryInfoDetails = new OrderDeliveryInfoDetails();
                            orderDeliveryInfoDetails.setDeliveryInfo(tdOrder.getMainOrderNumber(), LogisticStatus.SEALED_CAR, "商家已封车完成！", "已封车",
                                    "", "", "", "");
                            orderDeliveryInfoDetails.setIsRead(true);
                            orderDeliveryInfoDetailsService.addOrderDeliveryInfoDetails(orderDeliveryInfoDetails);
                        } else if (tdOrder.getStatusId().intValue() == 4 || tdOrder.getStatusId().intValue() == 5 || tdOrder.getStatusId().intValue() == 6) {
                            OrderDeliveryInfoDetails orderDeliveryInfoDetails = new OrderDeliveryInfoDetails();
                            orderDeliveryInfoDetails.setDeliveryInfo(tdOrder.getMainOrderNumber(), LogisticStatus.CONFIRM_ARRIVAL, "确认到货！", "送达",
                                    "", "", "", "");
                            orderDeliveryInfoDetails.setIsRead(true);
                            orderDeliveryInfoDetailsService.addOrderDeliveryInfoDetails(orderDeliveryInfoDetails);
                        }
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


    public Boolean saveOrderLogistcs(int size) {
        List<TdOrderLogistics> tdOrderLogisticsList = dataTransferService.queryOrderLogistcs(size);
        try {
            if (null == tdOrderLogisticsList || tdOrderLogisticsList.size() == 0) {
                return true;
            } else {
                for (TdOrderLogistics tdOrderLogistics : tdOrderLogisticsList) {
                    OrderLogisticsInfo orderLogisticsInfo = new OrderLogisticsInfo();
                    //判断该信息是否存在
                    OrderLogisticsInfo orderLogisticsInfoisExit = appOrderService.getOrderLogistice(tdOrderLogistics.getMainOrderNumber());
                    if (null != orderLogisticsInfoisExit) {
                        break;
                    }
                    if ("门店自提".equals(tdOrderLogistics.getDeliverTypeTitle())) {
                        orderLogisticsInfo.setBookingStoreAddress(tdOrderLogistics.getDetailedAddress());
                        orderLogisticsInfo.setBookingStoreName(tdOrderLogistics.getDiySiteName());
                        orderLogisticsInfo.setBookingStoreCode(tdOrderLogistics.getDiySiteCode());
                    }
                    orderLogisticsInfo.setDeliveryType(AppDeliveryType.getAppDeliveryTypeByDescription(tdOrderLogistics.getDeliverTypeTitle()));
                    orderLogisticsInfo.setOrdNo(tdOrderLogistics.getMainOrderNumber());
                    orderLogisticsInfo.setDeliveryCity(tdOrderLogistics.getCity());
                    orderLogisticsInfo.setDeliveryCounty(tdOrderLogistics.getDisctrict());
                    orderLogisticsInfo.setDeliveryStreet(tdOrderLogistics.getSubdistrict());
                    orderLogisticsInfo.setReceiver(tdOrderLogistics.getShippingName());
                    orderLogisticsInfo.setReceiverPhone(tdOrderLogistics.getShippingPhone());
                    orderLogisticsInfo.setResidenceName(null);
                    orderLogisticsInfo.setShippingAddress(tdOrderLogistics.getShippingAddress());
                    orderLogisticsInfo.setDeliveryClerkId(tdOrderLogistics.getEmpId());
                    orderLogisticsInfo.setDeliveryClerkName(tdOrderLogistics.getName());
                    orderLogisticsInfo.setDeliveryClerkNo(tdOrderLogistics.getDriver());
                    orderLogisticsInfo.setDeliveryClerkPhone(tdOrderLogistics.getMobile());
                    orderLogisticsInfo.setDeliveryTime(tdOrderLogistics.getDeliveryDate());
                    orderLogisticsInfo.setIsOwnerReceiving(false);
                    orderLogisticsInfo.setWarehouse(tdOrderLogistics.getWhNo());
                    orderLogisticsInfo.setOid(tdOrderLogistics.getOid());
                    orderLogisticsInfo.setDetailedAddress(tdOrderLogistics.getDetailedAddress());
                    dataTransferService.saveOrderLogisticsInfo(orderLogisticsInfo);
                }
            }
        } catch (Exception e) {
            log.debug("{}", e);
        }
        return false;
    }
}
