package cn.com.leyizhuang.app.quartz.task;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.utils.ApplicationContextUtil;
import cn.com.leyizhuang.app.foundation.pojo.CancelOrderParametersDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwCancelOrderRequest;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.AppToWmsOrderService;
import cn.com.leyizhuang.app.foundation.service.CancelOrderParametersService;
import cn.com.leyizhuang.app.foundation.service.ReturnOrderService;
import cn.com.leyizhuang.app.remote.queue.MaSinkSender;
import cn.com.leyizhuang.app.remote.wms.MaICallWms;

import cn.com.leyizhuang.common.util.CountUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 自动取消 6个月未提货自提单 7天未出货配送单
 * Created by 12421 on 2018/7/23.
 */
@Component
@Slf4j
public class AutoCancelNotShippingOrder implements Job {
    ReturnOrderService returnOrderService = (ReturnOrderService) ApplicationContextUtil.getBean("returnOrderService");
    MaICallWms maICallWms = (MaICallWms) ApplicationContextUtil.getBean("maICallWms");
    AppOrderService appOrderService = (AppOrderService) ApplicationContextUtil.getBean("appOrderService");
    CancelOrderParametersService cancelOrderParametersService = (CancelOrderParametersService) ApplicationContextUtil.getBean("cancelOrderParametersService");
    AppToWmsOrderService appToWmsOrderService = (AppToWmsOrderService) ApplicationContextUtil.getBean("appToWmsOrderService");
    MaSinkSender maSinkSender = (MaSinkSender) ApplicationContextUtil.getBean("maSinkSender");

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {


        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateTime = LocalDateTime.of(2018,8,1,0,0,59);

        if (now.isAfter(dateTime)){

            // 找到 超过6个月未出货的自提单
//            LocalDateTime sixMonthAfterTime = LocalDateTime.now().minusMonths(6);
//            List<OrderBaseInfo> sixMonthNotShippingList = returnOrderService.findOrderByStatusAndTypeAndCreateTime(AppOrderStatus.PENDING_RECEIVE,
//                    AppDeliveryType.SELF_TAKE,sixMonthAfterTime);
//
//            for (OrderBaseInfo baseInfo : sixMonthNotShippingList){
//                this.cancelOrder(baseInfo.getCreatorId(),baseInfo.getCreatorIdentityType().getValue(),
//                        baseInfo.getOrderNumber(),"自提单超过6个月未提货，自动取消","");
//            }

            // 找到 超过7天没有出货的配送单
            LocalDateTime sevenDayAfterTime = LocalDateTime.now().minusDays(7);
            List<OrderBaseInfo> sevenDayNotShiooingList = returnOrderService.findOrderByStatusAndTypeAndCreateTime(AppOrderStatus.PENDING_SHIPMENT,
                    AppDeliveryType.HOUSE_DELIVERY,sevenDayAfterTime);

            for (OrderBaseInfo baseInfo : sevenDayNotShiooingList){
                this.cancelOrder(baseInfo.getCreatorId(),baseInfo.getCreatorIdentityType().getValue(),
                        baseInfo.getOrderNumber(),"配送单超过7天未出货，自动取消","");
            }
        }
    }

    public void cancelOrder(Long userId, Integer identityType,
                            String orderNumber, String reasonInfo, String remarksInfo) {
        //获取订单头信息
        OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
        String orderStatus = orderBaseInfo.getStatus().getValue();
        //获取订单账目明细
        OrderBillingDetails orderBillingDetails = appOrderService.getOrderBillingDetail(orderNumber);
        if (null == orderBaseInfo) {
            System.out.println("取消订单失败，找不到订单，单号："+orderNumber);
        }
        if (null == orderBillingDetails) {
            System.out.println("取消订单失败，找不到收款信息，单号："+orderNumber);
        }
        Double storePos = orderBillingDetails.getStorePosMoney() == null ? 0 : orderBillingDetails.getStorePosMoney();
        Double storeOther = orderBillingDetails.getStoreOtherMoney() == null ? 0 : orderBillingDetails.getStoreOtherMoney();
        Double storeCash = orderBillingDetails.getStoreCash() == null ? 0 : orderBillingDetails.getStoreCash();
        Double storeTotalMoney = CountUtil.add(storeCash, storeOther, storePos);

            /*2018-04-08 generation 取消订单判断状态为待支付、配送代发货，自提待取货*/
        if (AppOrderStatus.UNPAID.equals(orderBaseInfo.getStatus())
                || (orderBaseInfo.getDeliveryType().equals(AppDeliveryType.HOUSE_DELIVERY) && AppOrderStatus.PENDING_SHIPMENT.equals(orderBaseInfo.getStatus()))
                || (orderBaseInfo.getDeliveryType().equals(AppDeliveryType.SELF_TAKE) && AppOrderStatus.PENDING_RECEIVE.equals(orderBaseInfo.getStatus()))) {
            //判断收货类型和订单状态
            if (orderBaseInfo.getDeliveryType().equals(AppDeliveryType.HOUSE_DELIVERY) && AppOrderStatus.PENDING_SHIPMENT.equals(orderBaseInfo.getStatus())) {
                //创建取消订单参数存储类
                CancelOrderParametersDO cancelOrderParametersDO = new CancelOrderParametersDO();
                cancelOrderParametersDO.setOrderNumber(orderNumber);
                cancelOrderParametersDO.setIdentityType(identityType);
                cancelOrderParametersDO.setUserId(userId);
                cancelOrderParametersDO.setReasonInfo(reasonInfo);
                cancelOrderParametersDO.setRemarksInfo(remarksInfo);
                cancelOrderParametersDO.setCancelStatus(CancelProcessingStatus.SEND_WMS);
                cancelOrderParametersService.addCancelOrderParameters(cancelOrderParametersDO);

                // 发送到wms通知WMS
                AtwCancelOrderRequest atwCancelOrderRequest = AtwCancelOrderRequest.transform(reasonInfo, orderBaseInfo);
                appToWmsOrderService.saveAtwCancelOrderRequest(atwCancelOrderRequest);
                maICallWms.sendToWmsCancelOrder(orderNumber);
                //修改订单状态为取消中
                orderBaseInfo.setStatus(AppOrderStatus.CANCELING);
                appOrderService.updateOrderStatusByOrderNo(orderBaseInfo);

                System.out.println("取消订单提交成功，等待确认");
            }else if (orderBaseInfo.getDeliveryType().equals(AppDeliveryType.SELF_TAKE) && AppOrderStatus.PENDING_SHIPMENT.equals(orderBaseInfo.getStatus())){
                //如果是待收货、门店自提单则需要返回第三方支付金额
                if (orderBaseInfo.getDeliveryStatus().equals(AppDeliveryType.SELF_TAKE)) {
                    if (null != orderBillingDetails.getOnlinePayType()) {
                        Double onlinePayAmount = orderBillingDetails.getOnlinePayAmount();
                        if (onlinePayAmount > 0D){
                            // 第三方支付已经超过3个月，只能分别退顾客或者门店预存款
                            if (orderBaseInfo.getCreatorIdentityType().equals(0)){
                                // 退门店预存款 把第三方支付金额加至门店预存款
                                Double stPredipostAmount = orderBillingDetails.getStPreDeposit();

                                stPredipostAmount = CountUtil.add(stPredipostAmount,onlinePayAmount);
                                orderBillingDetails.setStPreDeposit(stPredipostAmount);
                            }else if(orderBaseInfo.getCreatorIdentityType().equals(6)){
                                // 退顾客预存款 把第三方支付加至顾客预存款
                                Double cusPredipostAmount = orderBillingDetails.getCusPreDeposit();

                                cusPredipostAmount = CountUtil.add(cusPredipostAmount,onlinePayAmount);
                                orderBillingDetails.setCusPreDeposit(cusPredipostAmount);
                            }
                            orderBillingDetails.setOnlinePayAmount(0D);
                        }
                    }
                }

                //调用取消订单通用方法
                Map<Object, Object> maps = returnOrderService.cancelOrderUniversal(userId, identityType, orderNumber, reasonInfo, remarksInfo, orderBaseInfo, orderBillingDetails);
                //获取退单基础表信息
                ReturnOrderBaseInfo returnOrderBaseInfo = (ReturnOrderBaseInfo) maps.get("returnOrderBaseInfo");
                String code = (String) maps.get("code");
                Date date = new Date();
                if ("SUCCESS".equals(code)) {
                    //如果是待发货的门店自提单发送退单拆单消息到拆单消息队列
                    /*2018-04-08 generation 改为待收货的自提单*/
//                    if (orderBaseInfo.getStatus().equals(AppOrderStatus.PENDING_SHIPMENT) && orderBaseInfo.getDeliveryType().equals(AppDeliveryType.SELF_TAKE)){
                    if (orderStatus.equals(AppOrderStatus.PENDING_RECEIVE.getValue()) && orderBaseInfo.getDeliveryType().equals(AppDeliveryType.SELF_TAKE)) {
                        maSinkSender.sendReturnOrder(returnOrderBaseInfo.getReturnNo());
                    }

                } else {
                    System.out.println("取消订单失败，单号："+orderNumber);
                }
            }

        } else {

           System.out.println("取消订单失败，单号："+orderNumber);

        }
    }
}
