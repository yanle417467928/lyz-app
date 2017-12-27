package cn.com.leyizhuang.app.remote.webservice;

import cn.com.leyizhuang.app.core.constant.AppApplicationConstant;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.wms.AtwCancelOrderRequest;
import cn.com.leyizhuang.app.foundation.pojo.wms.AtwRequisitionOrder;
import cn.com.leyizhuang.app.foundation.pojo.wms.AtwRequisitionOrderGoods;
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
import java.util.List;

/**
 * @author Created on 2017-12-19 13:18
 **/
@Service
public class ICallWms {

    @Resource
    private AppToWmsOrderService appToWmsOrderService;

    private static final Logger logger = LoggerFactory.getLogger(ICallWms.class);

    private static QName wmsName = new QName("http://tempuri.org/", "GetErpInfo");

    private static Client wmsClient;


    public static Client getWmsClient() {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        return dcf.createClient(AppApplicationConstant.wmsUrl);
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
        logger.info("XML拼装完毕 OUT, xml:{}", xml);
        //发送到WMS
        //TODO wms确定td_return_note参数
        wmsClient = getWmsClient();
        Object[] objects = wmsClient.invoke(wmsName, "td_return_note", "1", xml);

        //修改发送状态
        String errorMsg = AppXmlUtil.checkReturnXml(objects);
        logger.error("发送取消订单出现异常 EXCEPTION, errorMsg:{}", errorMsg);
        cancelOrderRequest.setErrorStatus(errorMsg);
        appToWmsOrderService.modifyAtwCancelOrderRequest(cancelOrderRequest);
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
        //查询所有要货单商品明细
        List<AtwRequisitionOrderGoods> requisitionOrderGoodsList = appToWmsOrderService.findAtwRequisitionOrderGoodsByOrderNo(orderNumber);
        if (AssertUtil.isEmpty(requisitionOrderGoodsList)) {
            return;
        }
        for (AtwRequisitionOrderGoods requisitionOrderGoods : requisitionOrderGoodsList) {
            //如果发现此商品已发送成功则不再重复发送
            if (null != requisitionOrderGoods.getSendFlag() && requisitionOrderGoods.getSendFlag()) {
                continue;
            }
            logger.info("============================要货单商品XML开始拼装===============================");
            String xml = AppXmlUtil.getRequisitionOrderGoodsXml(requisitionOrderGoods);
            logger.info("要货单商品XML拼装完毕 OUT, XML:{}", xml);
            //发送到WMS
            Client wmsClient = getWmsClient();
            Object[] objects = wmsClient.invoke(wmsName, "td_requisition_goods", "1", xml);
            //解析返回信息
            String errorMsg = AppXmlUtil.checkReturnXml(objects);
            if (errorMsg != null) {
                //如果发送失败修改发送状态
                requisitionOrderGoods.setSendTime(new Date());
                requisitionOrderGoods.setSendFlag(false);
                requisitionOrderGoods.setErrorMessage(errorMsg);
                logger.error("发送要货单明细出现异常 EXCEPTION, errorMsg:{}", errorMsg);
            } else {
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
        logger.info("XML拼装完毕 OUT, xml:{}", xml);
        //发送到WMS
        wmsClient = getWmsClient();
        Object[] objects = wmsClient.invoke(wmsName, "td_requisition", "1", xml);
        //解析返回信息
        String errorMsg = AppXmlUtil.checkReturnXml(objects);
        //修改发送状态
        if (errorMsg != null) {
            requisitionOrder.setSendTime(new Date());
            requisitionOrder.setSendFlag(false);
            requisitionOrder.setErrorMessage(errorMsg);
            logger.error("发送要货单出现异常 EXCEPTION, errorMsg:{}", errorMsg);
        } else {
            requisitionOrder.setSendTime(new Date());
            requisitionOrder.setSendFlag(true);
        }
        appToWmsOrderService.modifyAtwRequisitionOrder(requisitionOrder);
        logger.info("sendMsgToWMS, OUT");
    }
}
