package cn.com.leyizhuang.app.web.controller.rest.datatransfer;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.LogisticStatus;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrderLogistics;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderJxPriceDifferenceReturnDetails;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderLogisticsInfo;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.OrderDeliveryInfoDetailsService;
import cn.com.leyizhuang.app.foundation.service.datatransfer.DataTransferService;
import cn.com.leyizhuang.common.util.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Jerry.Ren
 * create 2018-03-24 18:00
 * desc:
 **/
@RestController
public class DeliveryInfoController {

    @Resource
    private OrderDeliveryInfoDetailsService orderDeliveryInfoDetailsService;

    @Autowired
    private DataTransferService dataTransferService;
    @Autowired
    private AppStoreService appStoreService;
    @Autowired
    private AppOrderService appOrderService;

    @RequestMapping("/app/resend/wms/test/deliveryInfo")
    public void transformDeliveryInfoDetails() {

        int size = 0;
        for (int i = 0; i < 200; i++) {
            Boolean hasNotDatas = this.saveOrderDeliveryInfoDetails(size);
            if (hasNotDatas) {
                return;
            }
            size += 1000;
        }
    }

    @RequestMapping("/app/resend/wms/test/logistcsInfo")
    public void transformOrderLogistcs() {
        int size = 0;
        for (int i = 0; i < 200; i++) {
            Boolean hasNotDatas = this.saveOrderLogistcs(size);
            if (hasNotDatas) {
                return;
            }
            size += 1000;
        }
    }

    @RequestMapping("/app/resend/wms/test/JxPrice")
    public void transformJxPriceDifferenceReturnDetails() {

        int size = 0;

        for (int i = 0; i < 200; i++) {
            Boolean hasNotDatas = this.saveOrderJxPriceDifference(size);
            if (hasNotDatas) {
                return;
            }
            size += 1000;
        }
    }

    private Boolean saveOrderDeliveryInfoDetails(int size) {
        List<TdDeliveryInfoDetails> entityList = dataTransferService.queryDeliveryTimeSeqBySize(size);

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
                        deliveryInfoDetails.setLogisticStatus(LogisticStatus.ALREADY_POSITIONED);
                        break;
                    }
                    case "装车": {
                        deliveryInfoDetails.setLogisticStatus(LogisticStatus.ALREADY_POSITIONED);
                        break;
                    }
                    case "封车": {
                        deliveryInfoDetails.setLogisticStatus(LogisticStatus.ALREADY_POSITIONED);
                        TdDeliveryInfoDetails deliveryInfoDetail = dataTransferService.queryDeliveryInfoDetailByOrderNumber(tdDeliveryInfoDetails.getMainOrderNumber());
                        if (deliveryInfoDetail != null) {
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
                deliveryInfoDetails.setIsRead(false);
                orderDeliveryInfoDetailsService.addOrderDeliveryInfoDetails(deliveryInfoDetails);
            }
        } else {
            return true;
        }
        return false;
    }

    private Boolean saveOrderJxPriceDifference(int size) {
        List<TdDeliveryInfoDetails> tdOrderList = dataTransferService.queryTdOrderListBySize(size);

        if (AssertUtil.isNotEmpty(tdOrderList)) {
            for (TdDeliveryInfoDetails tdOrder : tdOrderList) {
                List<TdDeliveryInfoDetails> tdOrderGoodsList = dataTransferService.queryOrderGoodsListByOrderNumber(tdOrder.getId());
                AppStore store = appStoreService.findByStoreCode(tdOrder.getDiySiteCode());
                if (AssertUtil.isNotEmpty(tdOrderGoodsList)) {
                    for (TdDeliveryInfoDetails tdOrderGoods : tdOrderGoodsList) {

                        OrderJxPriceDifferenceReturnDetails returnDetails = new OrderJxPriceDifferenceReturnDetails();

                        returnDetails.setOid(tdOrder.getId());
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
            }
        } else {
            return true;
        }
        return false;
    }


    public Boolean saveOrderLogistcs(int size) {
        List<TdOrderLogistics> tdOrderLogisticsList = dataTransferService.queryOrderLogistcs(size);
        if (null == tdOrderLogisticsList && tdOrderLogisticsList.size() == 0) {
            return false;
        }
        for (TdOrderLogistics tdOrderLogistics : tdOrderLogisticsList) {
            OrderLogisticsInfo orderLogisticsInfo = new OrderLogisticsInfo();
            orderLogisticsInfo.setDeliveryType(AppDeliveryType.getAppDeliveryTypeByDescription(tdOrderLogistics.getDeliverTypeTitle()));
            orderLogisticsInfo.setBookingStoreAddress(tdOrderLogistics.getDetailedAddress());
            orderLogisticsInfo.setBookingStoreName(tdOrderLogistics.getDiySiteName());
            orderLogisticsInfo.setBookingStoreCode(tdOrderLogistics.getDiySiteCode());
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
            dataTransferService.saveOrderLogisticsInfo(orderLogisticsInfo);
        }
        return true;
    }
}
