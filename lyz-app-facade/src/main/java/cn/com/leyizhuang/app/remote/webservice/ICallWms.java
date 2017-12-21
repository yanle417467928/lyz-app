package cn.com.leyizhuang.app.remote.webservice;

import cn.com.leyizhuang.app.core.constant.AppApplicationConstant;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.wms.AtwCancelOrderRequest;
import cn.com.leyizhuang.app.foundation.pojo.wms.AtwRequisitionOrder;
import cn.com.leyizhuang.app.foundation.service.AppToWmsOrderService;
import cn.com.leyizhuang.app.remote.webservice.utils.AppXmlUtil;
import cn.com.leyizhuang.common.util.AssertUtil;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.namespace.QName;
import java.util.Date;

/**
 * @author Created on 2017-12-19 13:18
 **/
@Service
public class ICallWms {

    private static final Logger logger = LoggerFactory.getLogger(ICallWms.class);


    private String wmsUrl = AppApplicationConstant.wmsUrl;


    private QName wmsName = new QName("http://tempuri.org/", "GetErpInfo");

    private JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
    private Client wmsClient = dcf.createClient(wmsUrl);


    @Resource
    private AppToWmsOrderService appToWmsOrderService;

    public static void main(String[] args) throws Exception {
        ICallWms callWms = new ICallWms();
        AtwRequisitionOrder order = new AtwRequisitionOrder();
        order.setDiySiteId("FZM007");
        order.setDiySiteTel("028-83551646");
        order.setDiySiteTitle("富森富之美");
        order.setCustomerName("测试");
        order.setReceiveTimeQuantum("2017-12-20 14:30");
        order.setOrderNumber("CD_XN20171220102259255115");
        order.setReceiveAddress("成都市新都区大丰街道订单");
        order.setReceiveName("测试");
        order.setReceivePhone("13408698552");
        order.setCity("成都市");
        order.setDetailAddress("订单");
        order.setDisctrict("新都区");
        order.setSubdistrict("大丰街道");
        order.setSellerTel("18280285992");
        order.setGoodsQuantity(1);
        order.setUpstairsAll(0D);
        order.setSellerName("樊云霞");
        order.setDeliveryFee(30.0);
        order.setColorFee(0D);
        order.setDiscount(0D);
        order.setOtherPayed(0D);
        order.setBalanceUsed(60.0);
        order.setMemberReceiver("FALSE");
        order.setUnpayed(0D);
        order.setTotalGoodsPrice(30.0);
        order.setAgencyRefund(0.00);
        callWms.appToWmsOrderService.save(order);
        callWms.sendToWmsRequisitionOrderAndGoods(order.getOrderNumber());
    }

    /**
     * 发送取消订单到WMS
     *
     * @param orderNumber 取消订单生成的退单信息
     * @throws Exception parseException
     */
    public void sendToWmsCancelOrder(String orderNumber) throws Exception {
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
        logger.info("XML拼装完毕：{}", xml);
        //发送到WMS
        //TODO wms确定td_return_note参数
        Object[] objects = wmsClient.invoke(wmsName, "td_return_note", "1", xml);

        //修改发送状态
        String errorMsg = AppXmlUtil.checkReturnXml(objects);
        logger.error("发送取消订单出现异常: errorMsg{}", errorMsg);
        cancelOrderRequest.setErrorStatus(errorMsg);
        appToWmsOrderService.modify(cancelOrderRequest);
        logger.info("sendMsgToWMS, OUT");
    }

    /**
     * 发送要货单及商品信息
     *
     * @param orderNumber
     * @return
     */
    public void sendToWmsRequisitionOrderAndGoods(String orderNumber) throws Exception {

        if (StringUtils.isBlank(orderNumber)) {
            return;
        }
//        //查询所有要货单商品明细
//        List<AtwRequisitionOrderGoods> requisitionOrderGoodsList = appToWmsOrderService.findAtwRequisitionOrderGoodsByOrderNo(orderNumber);
//        if (AssertUtil.isEmpty(requisitionOrderGoodsList)){
//            return;
//        }
//        for (AtwRequisitionOrderGoods requisitionOrderGoods : requisitionOrderGoodsList) {
//            //如果发现此商品已发送成功则不再重复发送
//            if (null != requisitionOrderGoods.getSendFlag() && requisitionOrderGoods.getSendFlag()){
//                continue;
//            }
//            logger.info("============================要货单商品XML开始拼装===============================");
//            String xml = AppXmlUtil.getRequisitionOrderGoodsXml(requisitionOrderGoods);
//            logger.info("要货单商品XML拼装完毕：{}", xml);
//            //发送到WMS
//            Object[] objects = wmsClient.invoke(wmsName, "td_requisition", "1", xml);
//            //解析返回信息
//            String errorMsg = AppXmlUtil.checkReturnXml(objects);
//            if (errorMsg != null) {
//                //如果发送失败修改发送状态
//                requisitionOrderGoods.setSendTime(new Date());
//                requisitionOrderGoods.setSendFlag(false);
//                requisitionOrderGoods.setErrorMessage(errorMsg);
//                logger.error("发送要货单明细出现异常: errorMsg{}",errorMsg);
//            }else {
//                requisitionOrderGoods.setSendTime(new Date());
//                requisitionOrderGoods.setSendFlag(true);
//            }
//            appToWmsOrderService.modify(requisitionOrderGoods);
//        }
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
        logger.info("XML拼装完毕：{}", xml);
        //发送到WMS
        Object[] objects = wmsClient.invoke(wmsName, "td_requisition", "1", xml);
        //解析返回信息
        String errorMsg = AppXmlUtil.checkReturnXml(objects);
        //修改发送状态
        if (errorMsg != null) {
            requisitionOrder.setSendTime(new Date());
            requisitionOrder.setSendFlag(false);
            requisitionOrder.setErrorMessage(errorMsg);
            logger.error("发送要货单出现异常: errorMsg{}", errorMsg);
        } else {
            requisitionOrder.setSendTime(new Date());
            requisitionOrder.setSendFlag(true);
        }
        appToWmsOrderService.modify(requisitionOrder);
        logger.info("sendMsgToWMS, OUT");
    }
}
