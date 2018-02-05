package cn.com.leyizhuang.app.remote.webservice;

import cn.com.leyizhuang.app.core.constant.AppApplicationConstant;
import cn.com.leyizhuang.app.core.constant.LogisticStatus;
import cn.com.leyizhuang.app.core.constant.ReturnLogisticStatus;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.*;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderDeliveryDetail;
import cn.com.leyizhuang.app.foundation.service.AppToWmsOrderService;
import cn.com.leyizhuang.app.foundation.service.OrderDeliveryInfoDetailsService;
import cn.com.leyizhuang.app.foundation.service.ReturnOrderDeliveryDetailsService;
import cn.com.leyizhuang.app.foundation.service.SmsAccountService;
import cn.com.leyizhuang.app.remote.webservice.utils.AppXmlUtil;
import cn.com.leyizhuang.common.util.AssertUtil;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.namespace.QName;
import java.util.Date;
import java.util.List;

/**
 * @author Created on 2017-12-19 13:18
 **/
@Service
public class ICallWms {

    @Resource
    private AppToWmsOrderService appToWmsOrderService;

    @Resource
    private SmsAccountService smsAccountService;

    @Resource
    private OrderDeliveryInfoDetailsService orderDeliveryInfoDetailsService;

    @Resource
    private ReturnOrderDeliveryDetailsService returnOrderDeliveryDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(ICallWms.class);

    private static QName wmsName = new QName("http://tempuri.org/", "GetErpInfo");

    private static Client wmsClient;


    private static Client getWmsClient() {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        return dcf.createClient(AppApplicationConstant.wmsUrl);
    }

    /**
     * 发送取消订单或者取消退单到WMS
     *
     * @param orderNumber 取消订单生成的退单信息
     *
     */
    @Async
    public void sendToWmsCancelOrder(String orderNumber) {
        if (StringUtils.isBlank(orderNumber)) {
            return;
        }
        AtwCancelOrderRequest cancelOrderRequest = appToWmsOrderService.findAtwCancelOrderByOrderNo(orderNumber);
        if (null == cancelOrderRequest) {
            return;
        }
        if (null != cancelOrderRequest.getSendFlag() && cancelOrderRequest.getSendFlag()) {
            return;
        }
        logger.info("============================取消订单XML开始拼装===============================");
        String xml = AppXmlUtil.getCancelOrderXml(cancelOrderRequest);
        logger.info("取消订单XML拼装完毕 OUT, xml:{}", AppXmlUtil.format(xml));
        //发送到WMS
        wmsClient = getWmsClient();
        Object[] objects = new Object[0];
        try {
            objects = wmsClient.invoke(wmsName, "inter_atw_order_cancel_request", "1", xml);
        } catch (Exception e) {
            // TODO 发送短信通知传输失败
//            smsAccountService.commonSendSms();
            cancelOrderRequest.setSendFlag(false);
            cancelOrderRequest.setErrorMessage("订单号：" + orderNumber + "发送取消订单调用接口失败！");
            logger.error("发送取消订单出现异常 EXCEPTION, errorMsg:{}", e);
            e.printStackTrace();
        }
        //修改发送状态
        String errorMsg = AppXmlUtil.checkReturnXml(objects);
        logger.info("*****WMS返回发送取消订单信息***** 出参 OUT, XML:{}", objects);
        if (errorMsg != null) {
            //如果发送失败修改发送状态
            cancelOrderRequest.setErrorStatus(errorMsg);
            logger.error("发送取消订单出现异常 EXCEPTION, errorMsg:{}", errorMsg);
        } else if (null == cancelOrderRequest.getErrorMessage()) {
            cancelOrderRequest.setErrorStatus(null);
        }
        appToWmsOrderService.modifyAtwCancelOrderRequest(cancelOrderRequest);
        logger.info("sendMsgToWMS, OUT");
    }

    /**
     * 发送取消退货单到WMS
     *
     * @param returnNumber 取消退货单单号
     */
    @Async
    public void sendToWmsCancelReturnOrder(String returnNumber) {
        if (StringUtils.isBlank(returnNumber)) {
            return;
        }
        AtwCancelReturnOrderRequest returnOrderRequest = appToWmsOrderService.findAtwCancelReturnOrderByReturnNo(returnNumber);
        if (null == returnOrderRequest) {
            return;
        }
        if (null != returnOrderRequest.getSendFlag() && returnOrderRequest.getSendFlag()) {
            return;
        }
        logger.info("============================取消退货单XML开始拼装===============================");
        String xml = AppXmlUtil.getCancelReturnOrderXml(returnOrderRequest);
        logger.info("取消退货单XML拼装完毕 OUT, xml:{}", AppXmlUtil.format(xml));
        //发送到WMS
        //TODO wms确定td_return_note参数
        wmsClient = getWmsClient();
        Object[] objects = new Object[0];
        try {
            objects = wmsClient.invoke(wmsName, "td_tbw_back_m_cancel", "1", xml);
        } catch (Exception e) {
            // TODO 发送短信通知传输失败
            returnOrderRequest.setSendFlag(false);
            returnOrderRequest.setErrorMessage("退货单：" + returnNumber + "发送取消退单调用接口失败！");
            logger.error("发送取消订单出现异常 EXCEPTION, errorMsg:{}", e);
            e.printStackTrace();
        }
        //修改发送状态
        String errorMsg = AppXmlUtil.checkReturnXml(objects);
        logger.info("*****WMS返回发送取消退货订单信息***** 出参 OUT, XML:{}", objects);
        if (errorMsg != null) {
            //如果发送失败修改发送状态
            returnOrderRequest.setErrorStatus(errorMsg);
            logger.error("发送取消退货单出现异常 EXCEPTION, errorMsg:{}", errorMsg);
        } else if (null == returnOrderRequest.getErrorMessage()) {
            returnOrderRequest.setErrorStatus(null);
        }
        appToWmsOrderService.modifyAtwCancelReturnOrderRequest(returnOrderRequest);
        logger.info("sendMsgToWMS, OUT");
    }

    /**
     * 发送退货单收货确认
     *
     * @param returnNumber 退货单单号
     */
    @Async
    public void sendToWmsReturnOrderCheck(String returnNumber) {
        if (StringUtils.isBlank(returnNumber)) {
            return;
        }
        AtwReturnOrderCheckEnter atwReturnOrderCheckEnter = appToWmsOrderService.findAtwReturnOrderCheckEnterByReturnNo(returnNumber);
        if (null == atwReturnOrderCheckEnter) {
            return;
        }
        if (null != atwReturnOrderCheckEnter.getSendFlag() && atwReturnOrderCheckEnter.getSendFlag()) {
            return;
        }
        logger.info("============================退货单收货确认XML开始拼装===============================");
        String xml = AppXmlUtil.getReturnOrderCheckEnterXml(atwReturnOrderCheckEnter);
        logger.info("退货单收货确认XML拼装完毕 OUT, xml:{}", AppXmlUtil.format(xml));
        //发送到WMS
        wmsClient = getWmsClient();
        Object[] objects = new Object[0];
        try {
            objects = wmsClient.invoke(wmsName, "inter_atw_order_check", "1", xml);
        } catch (Exception e) {
            // TODO 发送短信通知传输失败
            atwReturnOrderCheckEnter.setSendFlag(false);
            atwReturnOrderCheckEnter.setErrorMessage("退货单：" + returnNumber + "发送退货单收货确认调用接口失败！");
            logger.error("发送取消订单出现异常 EXCEPTION, errorMsg:{}", e);
            e.printStackTrace();
        }
        //修改发送状态
        String errorMsg = AppXmlUtil.checkReturnXml(objects);
        logger.info("*****WMS返回发送退货单收货确认订单信息***** 出参 OUT, XML:{}", objects);
        if (errorMsg != null) {
            //如果发送失败修改发送状态
            atwReturnOrderCheckEnter.setSendFlag(Boolean.FALSE);
            atwReturnOrderCheckEnter.setErrorMessage(errorMsg);
            logger.error("发送退货单收货确认出现异常 EXCEPTION, errorMsg:{}", errorMsg);
        } else if (null == atwReturnOrderCheckEnter.getErrorMessage()) {
            atwReturnOrderCheckEnter.setSendFlag(Boolean.TRUE);
            atwReturnOrderCheckEnter.setSendTime(new Date());
        }
        appToWmsOrderService.modifyAtwReturnOrderCheckEnterRequest(atwReturnOrderCheckEnter);
        logger.info("sendMsgToWMS, OUT");
    }

    /**
     * 发送要货单及商品信息
     *
     * @param orderNumber 订单号
     *
     */
    @Async
    public void sendToWmsRequisitionOrderAndGoods(String orderNumber) {

        if (StringUtils.isBlank(orderNumber)) {
            logger.info("发送要货单及商品信息失败 OUT, 订单号不可为空！ orderNumber:{}", orderNumber);
            return;
        }
        //查询所有要货单商品明细
        List<AtwRequisitionOrderGoods> requisitionOrderGoodsList = appToWmsOrderService.findAtwRequisitionOrderGoodsByOrderNo(orderNumber);
        if (AssertUtil.isEmpty(requisitionOrderGoodsList)) {
            logger.info("发送要货单及商品信息失败 OUT, 未查到要货单商品明细 orderNumber:{}", orderNumber);
            return;
        }
        for (AtwRequisitionOrderGoods requisitionOrderGoods : requisitionOrderGoodsList) {
            //如果发现此商品已发送成功则不再重复发送
            if (null != requisitionOrderGoods.getSendFlag() && requisitionOrderGoods.getSendFlag()) {
                continue;
            }
            logger.info("============================要货单商品XML开始拼装===============================");
            String xml = AppXmlUtil.getRequisitionOrderGoodsXml(requisitionOrderGoods);
            logger.info("要货单商品XML拼装完毕 OUT, XML:{}", AppXmlUtil.format(xml));
            //发送到WMS
            Client wmsClient = getWmsClient();
            Object[] objects = new Object[0];
            try {
                objects = wmsClient.invoke(wmsName, "td_requisition_goods", "1", xml);
            } catch (Exception e) {
                // TODO 发送短信通知传输失败
                requisitionOrderGoods.setSendFlag(false);
                requisitionOrderGoods.setErrorMessage("订单号：" + orderNumber + "发送要货单明细调用接口失败！");
                logger.error("发送要货单明细出现异常 EXCEPTION, errorMsg:{}", e);
                e.printStackTrace();
            }
            //解析返回信息
            String errorMsg = AppXmlUtil.checkReturnXml(objects);
            logger.info("*****WMS返回发送要货单商品信息***** 出参 OUT, XML:{}", objects);
            if (errorMsg != null) {
                //如果发送失败修改发送状态
                requisitionOrderGoods.setSendFlag(false);
                requisitionOrderGoods.setErrorMessage(errorMsg);
                logger.error("发送要货单明细出现异常 EXCEPTION, errorMsg:{}", errorMsg);
            } else if (null == requisitionOrderGoods.getErrorMessage()) {
                requisitionOrderGoods.setSendTime(new Date());
                requisitionOrderGoods.setSendFlag(true);
            }
            appToWmsOrderService.modifyAtwRequisitionOrderGoods(requisitionOrderGoods);
        }
        //查询要货单头
        AtwRequisitionOrder requisitionOrder = appToWmsOrderService.findAtwRequisitionOrderByOrderNo(orderNumber);
        //如果发现此出货单已发送成功则不再重复发送
        if (AssertUtil.isEmpty(requisitionOrder)) {
            return;
        }
        if (null != requisitionOrder.getSendFlag() && requisitionOrder.getSendFlag()) {
            return;
        }
        logger.info("============================要货单XML开始拼装===============================");
        String xml = AppXmlUtil.getRequisitionOrderXml(requisitionOrder);
        logger.info("XML拼装完毕 OUT, xml:{}", AppXmlUtil.format(xml));
        //发送到WMS
        wmsClient = getWmsClient();
        Object[] objects = new Object[0];
        try {
            objects = wmsClient.invoke(wmsName, "td_requisition", "1", xml);
        } catch (Exception e) {
//            smsAccountService.commonSendSms(requisitionOrder);
            requisitionOrder.setSendFlag(false);
            requisitionOrder.setErrorMessage("订单号：" + orderNumber + "发送要货单头档调用接口失败！");
            logger.error("发送要货单明细出现异常 EXCEPTION, errorMsg:{}", e);
            e.printStackTrace();
        }
        //解析返回信息
        String errorMsg = AppXmlUtil.checkReturnXml(objects);
        logger.info("*****WMS返回发送要货单头档信息***** 出参 OUT, XML:{}", objects);
        //修改发送状态
        if (errorMsg != null) {
            requisitionOrder.setSendFlag(false);
            requisitionOrder.setErrorMessage(errorMsg);
            logger.error("发送要货单出现异常 EXCEPTION, errorMsg:{}", errorMsg);
        } else if (null == requisitionOrder.getErrorMessage()) {
            requisitionOrder.setSendTime(new Date());
            requisitionOrder.setSendFlag(true);

            OrderDeliveryInfoDetails deliveryInfoDetails = new OrderDeliveryInfoDetails();
            deliveryInfoDetails.setCreateTime(new Date());
            deliveryInfoDetails.setOrderNo(requisitionOrder.getOrderNumber());
            deliveryInfoDetails.setDescription("物流已接收");
            deliveryInfoDetails.setLogisticStatus(LogisticStatus.RECEIVED);
            orderDeliveryInfoDetailsService.addOrderDeliveryInfoDetails(deliveryInfoDetails);

        }
        appToWmsOrderService.modifyAtwRequisitionOrder(requisitionOrder);
        logger.info("sendMsgToWMS, OUT");
    }

    /**
     * 发送退货单及商品信息
     *
     * @param returnNumber 退货单号
     */
    @Async
    public void sendToWmsReturnOrderAndGoods(String returnNumber) {

        if (StringUtils.isBlank(returnNumber)) {
            logger.info("发送退货单及商品信息失败 OUT, 订单号不可为空！ returnNumber:{}", returnNumber);
            return;
        }
        //查询所有退货单商品明细
        List<AtwRequisitionOrderGoods> returnOrderGoodsList = appToWmsOrderService.findAtwRequisitionOrderGoodsByOrderNo(returnNumber);
        if (AssertUtil.isEmpty(returnOrderGoodsList)) {
            logger.info("发送退货单及商品信息失败 OUT, 未查到退货单商品明细 returnNumber:{}", returnNumber);
            return;
        }
        for (AtwRequisitionOrderGoods returnOrderGoods : returnOrderGoodsList) {
            //如果发现此商品已发送成功则不再重复发送
            if (null != returnOrderGoods.getSendFlag() && returnOrderGoods.getSendFlag()) {
                continue;
            }
            logger.info("============================退货单商品XML开始拼装===============================");
            String xml = AppXmlUtil.getRequisitionOrderGoodsXml(returnOrderGoods);
            logger.info("退货单商品XML拼装完毕 OUT, XML:{}", AppXmlUtil.format(xml));
            //发送到WMS
            Client wmsClient = getWmsClient();
            Object[] objects = new Object[0];
            try {
                objects = wmsClient.invoke(wmsName, "td_requisition_goods_return", "1", xml);
            } catch (Exception e) {
                // TODO 发送短信通知传输失败
                returnOrderGoods.setSendFlag(false);
                returnOrderGoods.setErrorMessage("订单号：" + returnNumber + "发送退货单明细调用接口失败！");
                logger.error("发送退货单明细出现异常 EXCEPTION, errorMsg:{}", e);
                e.printStackTrace();
            }
            //解析返回信息
            String errorMsg = AppXmlUtil.checkReturnXml(objects);
            logger.info("*****WMS返回发送退货单商品信息***** 出参 OUT, XML:{}", objects);
            if (errorMsg != null) {
                //如果发送失败修改发送状态
                returnOrderGoods.setSendFlag(false);
                returnOrderGoods.setErrorMessage(errorMsg);
                logger.error("发送退货单明细出现异常 EXCEPTION, errorMsg:{}", errorMsg);
            } else if (null == returnOrderGoods.getErrorMessage()) {
                returnOrderGoods.setSendTime(new Date());
                returnOrderGoods.setSendFlag(true);
            }
            appToWmsOrderService.modifyAtwRequisitionOrderGoods(returnOrderGoods);
        }
        //查询退货单头
        AtwReturnOrder returnOrder = appToWmsOrderService.findReturnOrderByReturnOrderNo(returnNumber);
        //如果发现此出货单已发送成功则不再重复发送
        if (AssertUtil.isEmpty(returnOrder)) {
            return;
        }
        if (null != returnOrder.getSendFlag() && returnOrder.getSendFlag()) {
            return;
        }
        logger.info("============================退货单XML开始拼装===============================");
        String xml = AppXmlUtil.getReturnOrderXml(returnOrder);
        logger.info("XML拼装完毕 OUT, xml:{}", AppXmlUtil.format(xml));
        //发送到WMS
        wmsClient = getWmsClient();
        Object[] objects = new Object[0];
        try {
            objects = wmsClient.invoke(wmsName, "td_return_note_return", "1", xml);
        } catch (Exception e) {
            // TODO 发送短信通知传输失败
            returnOrder.setSendFlag(false);
            returnOrder.setErrorMessage("订单号：" + returnNumber + "发送退货单头档调用接口失败！");
            logger.error("发送退货单明细出现异常 EXCEPTION, errorMsg:{}", e);
            e.printStackTrace();
        }
        //解析返回信息
        String errorMsg = AppXmlUtil.checkReturnXml(objects);
        logger.info("*****WMS返回发送退货单头档信息***** 出参 OUT, XML:{}", objects);
        //修改发送状态
        if (errorMsg != null) {
            returnOrder.setSendFlag(false);
            returnOrder.setErrorMessage(errorMsg);
            logger.error("发送退货单出现异常 EXCEPTION, errorMsg:{}", errorMsg);
        } else if (null == returnOrder.getErrorMessage()) {
            returnOrder.setSendTime(new Date());
            returnOrder.setSendFlag(true);

            ReturnOrderDeliveryDetail returnOrderDeliveryDetail = new ReturnOrderDeliveryDetail();
            returnOrderDeliveryDetail.setDescription("物流已接收");
            returnOrderDeliveryDetail.setReturnLogisticStatus(ReturnLogisticStatus.RECEIVED);
            returnOrderDeliveryDetail.setReturnNo(returnOrder.getReturnNumber());
            returnOrderDeliveryDetail.setCreateTime(new Date());
            returnOrderDeliveryDetailsService.addReturnOrderDeliveryInfoDetails(returnOrderDeliveryDetail);

        }
        appToWmsOrderService.modifyAtwReturnOrder(returnOrder);
        logger.info("sendMsgToWMS, OUT");
    }
}
