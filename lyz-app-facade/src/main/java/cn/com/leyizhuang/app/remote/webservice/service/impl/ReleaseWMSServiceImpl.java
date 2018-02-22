package cn.com.leyizhuang.app.remote.webservice.service.impl;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.pay.wechat.refund.OnlinePayRefundService;
import cn.com.leyizhuang.app.core.utils.DateUtil;
import cn.com.leyizhuang.app.core.utils.SmsUtils;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.CancelOrderParametersDO;
import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.WareHouseDO;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.*;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBillingDetail;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderDeliveryDetail;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.remote.queue.SellDetailsSender;
import cn.com.leyizhuang.app.remote.queue.SinkSender;
import cn.com.leyizhuang.app.remote.webservice.TestUser;
import cn.com.leyizhuang.app.remote.webservice.service.ReleaseWMSService;
import cn.com.leyizhuang.app.remote.webservice.utils.AppXmlUtil;
import cn.com.leyizhuang.common.foundation.pojo.SmsAccount;
import cn.com.leyizhuang.common.util.AssertUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Created on 2017-12-19 11:24
 **/
@WebService(targetNamespace = "http://cn.com.leyizhuang.app.remote.webservice.service",
        endpointInterface = "cn.com.leyizhuang.app.remote.webservice.service.ReleaseWMSService")
public class ReleaseWMSServiceImpl implements ReleaseWMSService {

    private static final Logger logger = LoggerFactory.getLogger(ReleaseWMSServiceImpl.class);

    @Resource
    private WmsToAppOrderService wmsToAppOrderService;
    @Resource
    private OrderDeliveryInfoDetailsService orderDeliveryInfoDetailsService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private ReturnOrderDeliveryDetailsService returnOrderDeliveryDetailsService;
    @Resource
    private StatisticsSellDetailsService statisticsSellDetailsService;
    @Resource
    private SellDetailsSender sellDetailsSender;
    @Resource
    private AppOrderService appOrderService;
    @Resource
    private ReturnOrderService returnOrderService;
    @Resource
    private SinkSender sinkSender;
    @Resource
    private CancelOrderParametersService cancelOrderParametersService;
    @Resource
    private OnlinePayRefundService onlinePayRefundService;
    @Resource
    private SmsAccountService smsAccountService;
    @Resource
    private WareHouseService wareHouseService;
    /**
     * 获取wms信息
     *
     * @param strTable 表
     * @param strType  类型
     * @param xml      内容主体
     * @return 结果
     */
    @Override
    public String GetWMSInfo(String strTable, String strType, String xml) {

        logger.info("GetWMSInfo CALLED,获取wms信息，入参 strTable:{},strType:{},xml:{}", strTable, strType, xml);

        if (StringUtils.isBlank(strTable) || "?".equals(strTable)) {
            logger.info("GetWMSInfo OUT,获取wms信息失败 出参 strTable:{}", strTable);
            return AppXmlUtil.resultStrXml(1, "STRTABLE参数错误");
        }

        if (StringUtils.isBlank(xml) || "?".equals(xml)) {
            logger.info("GetWMSInfo OUT,获取wms信息失败 出参 strTable:{}", xml);
            return AppXmlUtil.resultStrXml(1, "XML参数错误");
        }

        try {
            Document document = AppXmlUtil.parseStrXml(xml);

            if (null == document) {
                logger.info("GetWMSInfo OUT,获取wms信息失败");
                return AppXmlUtil.resultStrXml(1, "解密后XML数据为空");
            }

            NodeList nodeList = document.getElementsByTagName("TABLE");
            //出货订单头
            if ("tbw_send_task_m".equalsIgnoreCase(strTable)) {
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node node = nodeList.item(i);
                    NodeList childNodeList = node.getChildNodes();
                    WtaShippingOrderHeader header = new WtaShippingOrderHeader();
                    for (int j = 0; j < childNodeList.getLength(); j++) {

                        Node childNode = childNodeList.item(j);
                        header = mapping(header, childNode);
                    }
                    if (StringUtils.isBlank(header.getDriver())) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,配送员不能为空,任务编号 出参 c_task_no:{}", header.getTaskNo());
                        return AppXmlUtil.resultStrXml(1, "配送员编号不能为空,任务编号" + header.getTaskNo() + "");
                    }
                    header.setCreateTime(new Date());
                    wmsToAppOrderService.saveWtaShippingOrderHeader(header);
                    //查询是否存在
                    List<OrderDeliveryInfoDetails> deliveryInfoDetailsList = orderDeliveryInfoDetailsService.queryListByOrderNumber(header.getOrderNo());
                    if (AssertUtil.isEmpty(deliveryInfoDetailsList)) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,该订单不存在 出参 order_no:{}", header.getOrderNo());
                        return AppXmlUtil.resultStrXml(1, "App没有找到该订单");
                    }
                    WareHouseDO wareHouse = wareHouseService.findByWareHouseNo(header.getWhNo());
                    //保存物流信息
                    OrderDeliveryInfoDetails deliveryInfoDetails = OrderDeliveryInfoDetails.transform(header,
                            null != wareHouse ? wareHouse.getWareHouseName() : header.getWhNo());
                    orderDeliveryInfoDetailsService.addOrderDeliveryInfoDetails(deliveryInfoDetails);
                    //修改订单配送信息加入配送员
                    appOrderService.updateOrderLogisticInfoByDeliveryClerkNo(header.getDriver(), header.getWhNo(), header.getOrderNo());
                    //修改订单头状态
                    appOrderService.updateOrderStatusAndDeliveryStatusByOrderNo(AppOrderStatus.PENDING_RECEIVE, LogisticStatus.SEALED_CAR, header.getOrderNo());
                    // rabbit 记录下单销量
                    sellDetailsSender.sendOrderSellDetailsTOManagement(header.getOrderNo());
                }
                logger.info("GetWMSInfo OUT,获取wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "NORMAL");
            }

            //出货订单商品明细
            else if ("tbw_send_task_d".equalsIgnoreCase(strTable)) {

                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node node = nodeList.item(i);
                    NodeList childNodeList = node.getChildNodes();
                    WtaShippingOrderGoods goods = new WtaShippingOrderGoods();
                    for (int j = 0; j < childNodeList.getLength(); j++) {

                        Node childNode = childNodeList.item(j);
                        goods = mapping(goods, childNode);
                    }
                    GoodsDO goodsDO = goodsService.queryBySku(goods.getGCode());
                    if (goodsDO == null) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,商品不存在 出参 c_gcode:{}", goods.getGCode());
                        return AppXmlUtil.resultStrXml(1, "编码为" + goods.getGCode() + "的商品不存在");
                    }
                    wmsToAppOrderService.saveWtaShippingOrderGoods(goods);
                    //跟新订单的出货数量
                    appOrderService.updateOrderGoodsShippingQuantity(goods.getOrderNo(), goods.getGCode(), goods.getDAckQty());
                }
                logger.info("GetWMSInfo OUT,获取wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "NORMAL");

                //退货单返配上架头
            } else if ("tbw_back_rec_m".equalsIgnoreCase(strTable)) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    NodeList childNodeList = node.getChildNodes();
                    WtaReturningOrderHeader header = new WtaReturningOrderHeader();
                    for (int idx = 0; idx < childNodeList.getLength(); idx++) {
                        Node childNode = childNodeList.item(idx);
                        header = mapping(header, childNode);
                    }
//                    if (StringUtils.isBlank(header.get())) {
//                        logger.info("GetWMSInfo OUT,获取wms信息失败,配送员不能为空,任务编号 出参 c_rec_no{}", header.getRecNo());
//                        return AppXmlUtil.resultStrXml(1, "配送员编号不能为空,验收单号" + header.getRecNo() + "");
//                    }
                    header.setCreateTime(new Date());
                    wmsToAppOrderService.saveWtaReturningOrderHeader(header);

                    //保存物流信息
                    ReturnOrderDeliveryDetail returnOrderDeliveryDetail = ReturnOrderDeliveryDetail.transform(header);
                    returnOrderDeliveryDetailsService.addReturnOrderDeliveryInfoDetails(returnOrderDeliveryDetail);


                    // rabbitMq 记录退单销量
                    sellDetailsSender.sendReturnOrderSellDetailsTOManagement(header.getPoNo());
                }
                logger.info("GetWMSInfo OUT,获取返配单wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "NORMAL");

                //退货单返配上架明细
            } else if ("tbw_back_rec_d".equalsIgnoreCase(strTable)) {
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node node = nodeList.item(i);
                    NodeList childNodeList = node.getChildNodes();
                    WtaReturningOrderGoods goods = new WtaReturningOrderGoods();
                    for (int j = 0; j < childNodeList.getLength(); j++) {

                        Node childNode = childNodeList.item(j);
                        goods = mapping(goods, childNode);
                    }
                    wmsToAppOrderService.saveWtaReturningOrderGoods(goods);
                }
                logger.info("GetWMSInfo OUT,获取返配单商品wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "NORMAL");

                //获取退货单配送取货配送员
            } else if ("tbw_back_m".equalsIgnoreCase(strTable)) {
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node node = nodeList.item(i);
                    NodeList childNodeList = node.getChildNodes();

                    WtaReturnOrderDeliveryClerk deliveryClerk = new WtaReturnOrderDeliveryClerk();
                    for (int idx = 0; idx < childNodeList.getLength(); idx++) {
                        Node childNode = childNodeList.item(idx);
                        deliveryClerk = mapping(deliveryClerk, childNode);
                    }
                    deliveryClerk.setCreateTime(new Date());
                    wmsToAppOrderService.saveWtaReturnOrderDeliveryClerk(deliveryClerk);

                    if (AssertUtil.isEmpty(deliveryClerk.getDriver())) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,未查询到该配送单,退单号 出参 return_no{}", deliveryClerk.getReturnNo());
                        return AppXmlUtil.resultStrXml(1, "配送员(c_driver)不可为空,退单号： " + deliveryClerk.getReturnNo() + "");
                    }
                    returnOrderService.updateReturnOrderStatus(deliveryClerk.getReturnNo(), AppReturnOrderStatus.RETURNING);

                    ReturnOrderDeliveryDetail returnOrderDeliveryDetail = new ReturnOrderDeliveryDetail();
                    returnOrderDeliveryDetail.setReturnNo(deliveryClerk.getReturnNo());
                    returnOrderDeliveryDetail.setReturnLogisticStatus(ReturnLogisticStatus.PICKING_GOODS);
                    returnOrderDeliveryDetail.setDescription("配送员正在取货途中");
                    returnOrderDeliveryDetail.setCreateTime(new Date());
                    returnOrderDeliveryDetail.setPickersNumber(deliveryClerk.getDriver());
                    returnOrderDeliveryDetail.setWarehouseNo(deliveryClerk.getWarehouseNo());
                    returnOrderDeliveryDetailsService.addReturnOrderDeliveryInfoDetails(returnOrderDeliveryDetail);
                    //修改配送物流信息的配送员信息
                    returnOrderService.updateReturnLogisticInfo(deliveryClerk.getDriver(), deliveryClerk.getReturnNo());
                }
                logger.info("GetWMSInfo OUT,修改配送员信息wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "NORMAL");
                //取消订单结果确认
            } else if ("tbw_out_m_cancel".equalsIgnoreCase(strTable)) {
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node node = nodeList.item(i);
                    NodeList childNodeList = node.getChildNodes();
                    WtaCancelOrderResultEnter orderResultEnter = new WtaCancelOrderResultEnter();
                    for (int j = 0; j < childNodeList.getLength(); j++) {

                        Node childNode = childNodeList.item(j);
                        orderResultEnter = mapping(orderResultEnter, childNode);
                    }
                    wmsToAppOrderService.saveWtaCancelOrderResultEnter(orderResultEnter);

                    //获取订单头信息
                    OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(orderResultEnter.getOrderNo());
                    //获取订单账目明细
                    OrderBillingDetails orderBillingDetails = appOrderService.getOrderBillingDetail(orderResultEnter.getOrderNo());
                    //获取取消订单相关参数
                    CancelOrderParametersDO cancelOrderParametersDO = cancelOrderParametersService.findCancelOrderParametersByOrderNumber(orderResultEnter.getOrderNo());
                    if (orderResultEnter.getIsCancel()) {
                        //调用取消订单通用方法
                        Map<Object, Object> maps = returnOrderService.cancelOrderUniversal(cancelOrderParametersDO.getUserId(), cancelOrderParametersDO.getIdentityType(), cancelOrderParametersDO.getOrderNumber(), cancelOrderParametersDO.getReasonInfo(), cancelOrderParametersDO.getRemarksInfo(), orderBaseInfo, orderBillingDetails);
                        ReturnOrderBaseInfo returnOrderBaseInfo = (ReturnOrderBaseInfo) maps.get("returnOrderBaseInfo");
                        if (maps.get("code").equals("SUCCESS")) {

                            //如果是待收货、门店自提单则需要返回第三方支付金额
                            if (orderBaseInfo.getDeliveryStatus().equals(AppDeliveryType.SELF_TAKE) && orderBaseInfo.getStatus().equals(AppOrderStatus.PENDING_RECEIVE)) {
                                if (null != orderBillingDetails.getOnlinePayType()) {
                                    if ("支付宝".equals(orderBillingDetails.getOnlinePayType().getDescription())) {
                                        //支付宝退款
                                        onlinePayRefundService.alipayRefundRequest(cancelOrderParametersDO.getUserId(), cancelOrderParametersDO.getIdentityType(), cancelOrderParametersDO.getOrderNumber(), returnOrderBaseInfo.getReturnNo(), orderBillingDetails.getOnlinePayAmount());

                                    } else if ("微信".equals(orderBillingDetails.getOnlinePayType().getDescription())) {
                                        //微信退款方法类
                                        Map<String, String> map = onlinePayRefundService.wechatReturnMoney(cancelOrderParametersDO.getUserId(), cancelOrderParametersDO.getIdentityType(), orderBillingDetails.getOnlinePayAmount(), cancelOrderParametersDO.getOrderNumber(), returnOrderBaseInfo.getReturnNo());
                                    } else if ("银联".equals(orderBillingDetails.getOnlinePayType().getDescription())) {
                                        //创建退单退款详情实体
                                        ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                                        returnOrderBillingDetail.setCreateTime(new Date());
                                        returnOrderBillingDetail.setRoid(returnOrderBaseInfo.getRoid());
                                        returnOrderBillingDetail.setRefundNumber(returnOrderBaseInfo.getReturnNo());
                                        //TODO 时间待定
                                        returnOrderBillingDetail.setIntoAmountTime(new Date());
                                        //TODO 第三方回复码
                                        returnOrderBillingDetail.setReplyCode("");
                                        returnOrderBillingDetail.setReturnMoney(orderBillingDetails.getOnlinePayAmount());
                                        returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.UNION_PAY);
                                    }
                                }
                            }

                            //发送退单拆单消息到拆单消息队列
                            sinkSender.sendReturnOrder(returnOrderBaseInfo.getReturnNo());
                            //修改取消订单处理状态
                            cancelOrderParametersService.updateCancelStatusByOrderNumber(orderResultEnter.getOrderNo());
                            logger.info("cancelOrderToWms OUT,取消订单成功");
                            return AppXmlUtil.resultStrXml(1, "取消订单业务逻辑处理失败!");
                        } else {
                            logger.info("getReturnOrderList OUT,取消订单失败");
                        }
                    } else {
                        logger.info("cancelOrderToWms CALLED,发送提货码，入参 mobile:{}", orderBaseInfo.getCreatorPhone());
                        if (null == orderBaseInfo.getCreatorPhone() || orderBaseInfo.getCreatorPhone().equalsIgnoreCase("") || orderBaseInfo.getCreatorPhone().trim().length() != 11) {
                            logger.info("cancelOrderToWms OUT,发送提货码失败，出参 ResultDTO:{}");
                        }
                        String info = "您取消的订单" + orderResultEnter.getOrderNo() + "，取消失败，请联系管理人员！";
                        logger.info("取消失败订单号:{}", orderResultEnter.getOrderNo());
                        String content = null;
                        try {
                            content = URLEncoder.encode(info, "GB2312");
                            System.err.println(content);
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.info("cancelOrderToWms EXCEPTION，取消订单失败信息发送失败，出参 ResultDTO:{}");
                        }

                        SmsAccount account = smsAccountService.findOne();
                        String returnCode = null;
                        try {
                            returnCode = SmsUtils.sendMessageQrCode(account.getEncode(), account.getEnpass(), account.getUserName(), orderBaseInfo.getCreatorPhone(), content);
                        } catch (IOException e) {
                            logger.info("cancelOrderToWms EXCEPTION，取消订单失败信息发送失败，出参 ResultDTO:{}");
                            logger.warn("{}", e);
                        } catch (Exception e) {
                            logger.info("cancelOrderToWms EXCEPTION，取消订单失败信息发送失败，出参 ResultDTO:{}");
                            logger.warn("{}", e);
                        }
                        if (returnCode.equalsIgnoreCase("00")) {
                            logger.info("cancelOrderToWms OUT，取消订单失败信息发送成功，出参 ResultDTO:{}");
                        } else {
                            logger.info("cancelOrderToWms OUT，取消订单失败信息发送失败，出参 ResultDTO:{}");
                        }
                    }
                }
                logger.info("GetWMSInfo OUT,获取返配单商品wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "NORMAL");
                //取消退单结果确认
            } else if ("tbw_back_m_cancel".equalsIgnoreCase(strTable)) {
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node node = nodeList.item(i);
                    NodeList childNodeList = node.getChildNodes();
                    WtaCancelReturnOrderResultEnter returnOrderResultEnter = new WtaCancelReturnOrderResultEnter();
                    for (int j = 0; j < childNodeList.getLength(); j++) {

                        Node childNode = childNodeList.item(j);
                        returnOrderResultEnter = mapping(returnOrderResultEnter, childNode);
                    }
                    wmsToAppOrderService.saveWtaCancelReturnOrderResultEnter(returnOrderResultEnter);
                    if (returnOrderResultEnter.getIsCancel()) {
                        // 修改回原订单的可退和已退！
                        List<ReturnOrderGoodsInfo> returnOrderGoodsInfoList = returnOrderService.findReturnOrderGoodsInfoByOrderNumber(returnOrderResultEnter.getReturnNumber());
                        returnOrderGoodsInfoList.forEach(returnOrderGoodsInfo -> appOrderService.updateReturnableQuantityAndReturnQuantityById(
                                returnOrderGoodsInfo.getReturnQty(), returnOrderGoodsInfo.getOrderGoodsId()));

                        //修改退货单状态
                        returnOrderService.updateReturnOrderStatus(returnOrderResultEnter.getReturnNumber(), AppReturnOrderStatus.CANCELED);
                    }
                }
                logger.info("GetWMSInfo OUT,获取返配单商品wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "NORMAL");
                //配送单物流详情
            } else if ("tbw_out_m".equalsIgnoreCase(strTable)) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    NodeList childNodeList = node.getChildNodes();

                    OrderDeliveryInfoDetails orderDeliveryInfoDetails = new OrderDeliveryInfoDetails();
                    for (int idx = 0; idx < childNodeList.getLength(); idx++) {
                        Node childNode = childNodeList.item(idx);
                        orderDeliveryInfoDetails = mapping(orderDeliveryInfoDetails, childNode);
                    }
                    if (null == orderDeliveryInfoDetails.getLogisticStatus()) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,获取物流状态失败,任务编号 出参 c_task_no:{}", orderDeliveryInfoDetails.getTaskNo());
                        return AppXmlUtil.resultStrXml(1, "获取物流状态(c_value3)失败,c_out_no:{" + orderDeliveryInfoDetails.getTaskNo() + "}");
                    }
                    String description = "商家" + orderDeliveryInfoDetails.getLogisticStatus().getDescription() + "完成！";
                    orderDeliveryInfoDetails.setDescription(description);
                    orderDeliveryInfoDetails.setCreateTime(new Date());
                    orderDeliveryInfoDetailsService.addOrderDeliveryInfoDetails(orderDeliveryInfoDetails);

                    //修改订单状态
                    appOrderService.updateOrderStatusAndDeliveryStatusByOrderNo(AppOrderStatus.PENDING_RECEIVE,
                            orderDeliveryInfoDetails.getLogisticStatus(), orderDeliveryInfoDetails.getOrderNo());
                }
                logger.info("GetWMSInfo OUT,获取wms物流信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "NORMAL");
                //整转零
            } else if ("tbw_whole_sep_direction".equalsIgnoreCase(strTable)) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    NodeList childNodeList = node.getChildNodes();

                    WtaWarehouseWholeSplitToUnit wholeSplitToUnit = new WtaWarehouseWholeSplitToUnit();
                    for (int idx = 0; idx < childNodeList.getLength(); idx++) {
                        Node childNode = childNodeList.item(idx);
                        wholeSplitToUnit = mapping(wholeSplitToUnit, childNode);
                    }


                }
            }

        } catch (ParserConfigurationException e) {
            logger.warn("GetWMSInfo EXCEPTION,解密后xml参数错误");
            logger.warn("{}", e);
            return AppXmlUtil.resultStrXml(1, "解密后xml参数错误");

        } catch (IOException | SAXException e) {
            logger.warn("GetWMSInfo EXCEPTION,解密后xml格式不对");
            logger.warn("{}", e);
            return AppXmlUtil.resultStrXml(1, "解密后xml格式不对");
        }
        return AppXmlUtil.resultStrXml(1, "不支持该表数据传输：" + strTable);
    }

    /**
     * 整转零结果集映射
     *
     * @param wholeSplitToUnit
     * @param childNode
     * @return
     */
    private WtaWarehouseWholeSplitToUnit mapping(WtaWarehouseWholeSplitToUnit wholeSplitToUnit, Node childNode) {

        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            if (childNode.getNodeName().equalsIgnoreCase("c_wh_no")) {
                if (null != childNode.getChildNodes().item(0)) {
                    wholeSplitToUnit.setWarehouseNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if (childNode.getNodeName().equalsIgnoreCase("c_owner_no")) {
                if (null != childNode.getChildNodes().item(0)) {
                    wholeSplitToUnit.setOwnerNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if (childNode.getNodeName().equalsIgnoreCase("c_direct_no")) {
                if (null != childNode.getChildNodes().item(0)) {
                    wholeSplitToUnit.setDirectNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if (childNode.getNodeName().equalsIgnoreCase("c_gcode")) {
                if (null != childNode.getChildNodes().item(0)) {
                    wholeSplitToUnit.setSku(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if (childNode.getNodeName().equalsIgnoreCase("c_d_gcode")) {
                if (null != childNode.getChildNodes().item(0)) {
                    wholeSplitToUnit.setDSku(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if (childNode.getNodeName().equalsIgnoreCase("c_qty")) {
                if (null != childNode.getChildNodes().item(0)) {
                    wholeSplitToUnit.setQty(Double.parseDouble(childNode.getChildNodes().item(0).getNodeValue()));
                }
            } else if (childNode.getNodeName().equalsIgnoreCase("c_in_qty")) {
                if (null != childNode.getChildNodes().item(0)) {
                    wholeSplitToUnit.setDQty(Double.parseDouble(childNode.getChildNodes().item(0).getNodeValue()));
                }
            } else if (childNode.getNodeName().equalsIgnoreCase("c_status")) {
                if (null != childNode.getChildNodes().item(0)) {
                    wholeSplitToUnit.setStatus(childNode.getChildNodes().item(0).getNodeValue());
                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("c_reserved1")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    cReserved1 = childNode.getChildNodes().item(0).getNodeValue();
//                }
            } else if (childNode.getNodeName().equalsIgnoreCase("c_mk_userno")) {
                if (null != childNode.getChildNodes().item(0)) {
                    wholeSplitToUnit.setCreatorNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if (childNode.getNodeName().equalsIgnoreCase("c_mk_dt")) {
                if (null != childNode.getChildNodes().item(0)) {
                    wholeSplitToUnit.setCreateTime(DateUtil.dateFromString(childNode.getChildNodes().item(0).getNodeValue()));
                }
            } else if (childNode.getNodeName().equalsIgnoreCase("c_company_id")) {
                if (null != childNode.getChildNodes().item(0)) {
                    wholeSplitToUnit.setCompanyId(Long.parseLong(childNode.getChildNodes().item(0).getNodeValue()));
                }
            }
        }
        return wholeSplitToUnit;
    }

    /**
     * 将wms结果集映射到实体上
     *
     * @param childNode 信息节点
     * @return 出货头档
     */
    private WtaShippingOrderHeader mapping(WtaShippingOrderHeader wtaShippingOrderHeader, Node childNode) {

        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            /*
            <ERP><TABLE><C_OWNER_NO>001</C_OWNER_NO><C_TASK_NO>SU13021801310001</C_TASK_NO><C_TASK_ID>1</C_TASK_ID>
            <C_TASK_TYPE>一般出货</C_TASK_TYPE><C_OP_TYPE>C</C_OP_TYPE><C_S_LOCATION_NO>F1F0111</C_S_LOCATION_NO>
            <C_S_LOCATION_ID>28670</C_S_LOCATION_ID>
            <C_S_CONTAINER_NO>OU13021801310003</C_S_CONTAINER_NO>
           <C_GCODE>SJWT4503-25</C_GCODE>
            <C_STOCKATTR_ID>1</C_STOCKATTR_ID><C_PACK_QTY>1</C_PACK_QTY><C_D_REQUEST_QTY>1.00</C_D_REQUEST_QTY>
            <C_D_ACK_BAD_QTY>0.00</C_D_ACK_BAD_QTY><C_D_ACK_QIFT_QTY>0.00</C_D_ACK_QIFT_QTY><C_D_ACK_QTY>1.00</C_D_ACK_QTY>
            <C_OP_USER>0000</C_OP_USER><C_OP_TOOLS>表单</C_OP_TOOLS><C_OP_STATUS>已出车</C_OP_STATUS><C_WAVE_NO>WA13021801310001</C_WAVE_NO>
            <C_SOURCE_NO>OU13021801310003</C_SOURCE_NO><C_RESERVED1>LYZ</C_RESERVED1><C_RESERVED2></C_RESERVED2>
            <C_RESERVED3></C_RESERVED3><C_RESERVED4>CD_XN20180131161050468455</C_RESERVED4><C_RESERVED5></C_RESERVED5>
            <C_NOTE></C_NOTE><C_MK_DT>2018/1/31 18:38:10</C_MK_DT><C_MK_USERNO>0000</C_MK_USERNO>
            <C_MODIFIED_DT>2018/1/31 18:38:37</C_MODIFIED_DT><C_MODIFIED_USERNO>0000</C_MODIFIED_USERNO>
            <C_UPLOAD_STATUS></C_UPLOAD_STATUS><C_SEND_FALG>否</C_SEND_FALG></TABLE></ERP>
             */
            // 比较字段名
            if ("c_task_no".equalsIgnoreCase(childNode.getNodeName())) {
                // 有值
                if (null != childNode.getChildNodes().item(0)) {
                    wtaShippingOrderHeader.setTaskNo(childNode.getChildNodes().item(0).getNodeValue());
                }
//            } else if ("c_begin_dt".equalsIgnoreCase(childNode.getNodeName())) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    c_begin_dt = childNode.getChildNodes().item(0).getNodeValue();
//                }
            } else if ("c_end_dt".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wtaShippingOrderHeader.setEndDt(DateUtil.dateFromString(childNode.getChildNodes().item(0).getNodeValue()));
                }
            } else if ("c_wh_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wtaShippingOrderHeader.setWhNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_op_status".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wtaShippingOrderHeader.setOpStatus(childNode.getChildNodes().item(0).getNodeValue());
                }
//            } else if ("c_op_user".equalsIgnoreCase(childNode.getNodeName())) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    c_op_user = childNode.getChildNodes().item(0).getNodeValue();
//                }
//            } else if ("c_modified_userno".equalsIgnoreCase(childNode.getNodeName())) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    c_modified_userno = childNode.getChildNodes().item(0).getNodeValue();
//                }
            } else if ("c_owner_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wtaShippingOrderHeader.setOwnerNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("C_RESERVED1".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wtaShippingOrderHeader.setOrderNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_Driver".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wtaShippingOrderHeader.setDriver(childNode.getChildNodes().item(0).getNodeValue());
                }
//            } else if ("c_company_id".equalsIgnoreCase(childNode.getNodeName())) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    cCompanyId = Long.parseLong(childNode.getChildNodes().item(0).getNodeValue());
//                }
            } else if ("c_task_type".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wtaShippingOrderHeader.setCTaskType(childNode.getChildNodes().item(0).getNodeValue());
                }
            }
        }
        return wtaShippingOrderHeader;
    }

    private WtaShippingOrderGoods mapping(WtaShippingOrderGoods shippingOrderGoods, Node childNode) {

        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            // 比较字段名
            if ("c_task_no".equalsIgnoreCase(childNode.getNodeName())) {
                // 有值
                if (null != childNode.getChildNodes().item(0)) {
                    shippingOrderGoods.setTaskNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_reserved1".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    shippingOrderGoods.setOrderNo(childNode.getChildNodes().item(0).getNodeValue());
                }
//            } else if ("c_begin_dt".equalsIgnoreCase(childNode.getNodeName())) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    c_begin_dt = childNode.getChildNodes().item(0).getNodeValue();
//                }
//            } else if ("c_end_dt".equalsIgnoreCase(childNode.getNodeName())) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    shippingOrderGoods.set = childNode.getChildNodes().item(0).getNodeValue();
//                }
//            } else if ("c_wh_no".equalsIgnoreCase(childNode.getNodeName())) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    shippingOrderGoods.set = childNode.getChildNodes().item(0).getNodeValue();
//                }
            } else if ("c_op_status".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    shippingOrderGoods.setOpStatus(childNode.getChildNodes().item(0).getNodeValue());
                }
//            } else if ("c_op_user".equalsIgnoreCase(childNode.getNodeName())) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    c_op_user = childNode.getChildNodes().item(0).getNodeValue();
//                }
//            } else if ("c_modified_userno".equalsIgnoreCase(childNode.getNodeName())) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    c_modified_userno = childNode.getChildNodes().item(0).getNodeValue();
//                }
//            } else if ("c_owner_no".equalsIgnoreCase(childNode.getNodeName())) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    c_owner_no = childNode.getChildNodes().item(0).getNodeValue();
//                }
            } else if ("c_gcode".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    shippingOrderGoods.setGCode(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_d_ack_qty".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    Double parseDouble = Double.parseDouble(childNode.getChildNodes().item(0).getNodeValue());
                    shippingOrderGoods.setDAckQty(parseDouble.intValue());
                }
//            } else if ("c_d_request_qty".equalsIgnoreCase(childNode.getNodeName())) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    String string = childNode.getChildNodes().item(0).getNodeValue();
//                    c_d_request_qty = Double.parseDouble(string);
//                }
            } else if ("c_task_type".equalsIgnoreCase(childNode.getNodeName())) {
                shippingOrderGoods.setTaskType(childNode.getChildNodes().item(0).getNodeValue());
            } else if ("C_SOURCE_NO".equalsIgnoreCase(childNode.getNodeName())) {
                shippingOrderGoods.setSourceNo(childNode.getChildNodes().item(0).getNodeValue());
            }
        }
        return shippingOrderGoods;
    }

    private WtaReturningOrderHeader mapping(WtaReturningOrderHeader returningOrderHeader, Node childNode) {
        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            // 比较字段名
            if ("c_wh_no".equalsIgnoreCase(childNode.getNodeName())) {
                // 有值
                if (null != childNode.getChildNodes().item(0)) {
                    returningOrderHeader.setWhNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_owner_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    returningOrderHeader.setOwnerNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_rec_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    returningOrderHeader.setRecNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_back_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    returningOrderHeader.setBackNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_note".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    returningOrderHeader.setNote(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_po_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    returningOrderHeader.setPoNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_company_id".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    String companyId = childNode.getChildNodes().item(0).getNodeValue();
                    returningOrderHeader.setCompanyId(companyId != null ? Long.parseLong(companyId) : 0L);
                }
            }
        }
        return returningOrderHeader;
    }

    private WtaReturningOrderGoods mapping(WtaReturningOrderGoods goods, Node childNode) {
        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            // 比较字段名
            if ("c_rec_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    goods.setRecNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_rec_id".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    goods.setRecId(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_gcode".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    goods.setGcode(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_rec_qty".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    goods.setRecQty(childNode.getChildNodes().item(0).getNodeValue());
                }
            }
        }
        return goods;
    }

    private WtaReturnOrderDeliveryClerk mapping(WtaReturnOrderDeliveryClerk returnOrderDeliveryClerk, Node childNode) {
        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            // 比较字段名
            if ("c_note".equalsIgnoreCase(childNode.getNodeName())) {
                // 有值
                if (null != childNode.getChildNodes().item(0)) {
                    returnOrderDeliveryClerk.setNote(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_wh_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    returnOrderDeliveryClerk.setWarehouseNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_po_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    returnOrderDeliveryClerk.setReturnNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_driver".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    returnOrderDeliveryClerk.setDriver(childNode.getChildNodes().item(0).getNodeValue());
                }
            }
        }
        return returnOrderDeliveryClerk;
    }

    private OrderDeliveryInfoDetails mapping(OrderDeliveryInfoDetails orderDeliveryInfoDetails, Node childNode) {
        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            /*
            <ERP><TABLE><C_COMPANY_ID>2121</C_COMPANY_ID><C_OUT_NO>OU13021801310003</C_OUT_NO><C_WH_NO>1302</C_WH_NO>
            <C_WH_NAME>航天仓</C_WH_NAME><C_ID>217367</C_ID><C_DESCRIPTION>WMS作业任务状态ToERP</C_DESCRIPTION><C_DEST>
            </C_DEST><C_DT>2018/1/31 18:37:43</C_DT><C_COLUMN1>c_id</C_COLUMN1><C_VALUE1>CD_XN20180131161050468455</C_VALUE1>
            <C_COLUMN2>c_bill_type</C_COLUMN2><C_VALUE2>拣货单</C_VALUE2><C_COLUMN3>c_type</C_COLUMN3><C_VALUE3>已拣货</C_VALUE3>
            <C_COLUMN4>c_customer_no</C_COLUMN4><C_VALUE4>FZM007</C_VALUE4><C_COLUMN5>c_gcode</C_COLUMN5><C_VALUE5>SJWT4503-25</C_VALUE5>
            <C_COLUMN6>c_qty</C_COLUMN6><C_VALUE6>1.00</C_VALUE6><C_COLUMN7>c_out_no</C_COLUMN7><C_VALUE7>OU13021801310003</C_VALUE7>
            <C_COLUMN8>c_d_request_qty</C_COLUMN8><C_VALUE8>1.00</C_VALUE8></TABLE></ERP>
             */
            if ("C_COMPANY_ID".equalsIgnoreCase(childNode.getNodeName())) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    tbwOutM.setcCompanyId(Long.parseLong(childNode.getChildNodes().item(0).getNodeValue()));
//                }
            } else if ("C_OUT_NO".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    orderDeliveryInfoDetails.setTaskNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("C_WH_NO".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    orderDeliveryInfoDetails.setWarehouseNo(childNode.getChildNodes().item(0).getNodeValue());
                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("C_WH_NAME")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    tbwOutM.setcWhName(childNode.getChildNodes().item(0).getNodeValue());
//                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("C_ID")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    tbwOutM.setcId(Long.parseLong(childNode.getChildNodes().item(0).getNodeValue()));
//                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("C_DESCRIPTION")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    tbwOutM.setcDescription(childNode.getChildNodes().item(0).getNodeValue());
//                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("C_DEST")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    tbwOutM.setcDest(childNode.getChildNodes().item(0).getNodeValue());
//                }
            } else if ("C_DT".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    orderDeliveryInfoDetails.setCreateTime(DateUtil.dateFromString(childNode.getChildNodes().item(0).getNodeValue()));
                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("C_COLUMN1")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    tbwOutM.setcColumn1(childNode.getChildNodes().item(0).getNodeValue());
//                }
            } else if ("C_VALUE1".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    orderDeliveryInfoDetails.setOrderNo(childNode.getChildNodes().item(0).getNodeValue());
                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("C_COLUMN2")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    tbwOutM.setcColumn2(childNode.getChildNodes().item(0).getNodeValue());
//                }
            } else if ("C_VALUE2".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    orderDeliveryInfoDetails.setOperationType(childNode.getChildNodes().item(0).getNodeValue());
                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("C_COLUMN3")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    tbwOutM.setcColumn3(childNode.getChildNodes().item(0).getNodeValue());
//                }
            } else if ("C_VALUE3".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    String cValue3 = childNode.getChildNodes().item(0).getNodeValue();
                    if (LogisticStatus.RECEIVED.getDescription().equals(cValue3)) {
                        orderDeliveryInfoDetails.setLogisticStatus(LogisticStatus.RECEIVED);
                    } else if (LogisticStatus.ALREADY_POSITIONED.getDescription().equals(cValue3)) {
                        orderDeliveryInfoDetails.setLogisticStatus(LogisticStatus.ALREADY_POSITIONED);
                    } else if (LogisticStatus.PICKING_GOODS.getDescription().equals(cValue3)) {
                        orderDeliveryInfoDetails.setLogisticStatus(LogisticStatus.PICKING_GOODS);
                    } else if (LogisticStatus.LOADING.getDescription().equals(cValue3)) {
                        orderDeliveryInfoDetails.setLogisticStatus(LogisticStatus.LOADING);
                    }
                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("C_COLUMN4")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    tbwOutM.setcColumn4(childNode.getChildNodes().item(0).getNodeValue());
//                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("C_VALUE4")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    tbwOutM.setcValue4(childNode.getChildNodes().item(0).getNodeValue());
//                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("C_COLUMN5")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    tbwOutM.setcColumn5(childNode.getChildNodes().item(0).getNodeValue());
//                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("C_VALUE5")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    tbwOutM.setcValue5(childNode.getChildNodes().item(0).getNodeValue());
//                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("C_COLUMN6")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    tbwOutM.setcColumn6(childNode.getChildNodes().item(0).getNodeValue());
//                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("C_VALUE6")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    tbwOutM.setcValue6(childNode.getChildNodes().item(0).getNodeValue());
//                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("C_COLUMN7")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    tbwOutM.setcColumn7(childNode.getChildNodes().item(0).getNodeValue());
//                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("C_VALUE7")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    orderDeliveryInfoDetails.setTaskNo(childNode.getChildNodes().item(0).getNodeValue());
//                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("C_COLUMN8")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    tbwOutM.setcColumn8(childNode.getChildNodes().item(0).getNodeValue());
//                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("C_VALUE8")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    tbwOutM.setcValue8(childNode.getChildNodes().item(0).getNodeValue());
//                }
            }
        }
        return orderDeliveryInfoDetails;
    }

    private WtaCancelReturnOrderResultEnter mapping(WtaCancelReturnOrderResultEnter returnOrderResultEnter, Node childNode) {
        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            // 比较字段名
            if ("C_MK_DT".equalsIgnoreCase(childNode.getNodeName())) {
                // 有值
                if (null != childNode.getChildNodes().item(0)) {
                    returnOrderResultEnter.setCreateTime(new Date());
                }
            } else if ("C_OP_STATUS".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    if ("已作废".equals(childNode.getChildNodes().item(0).getNodeValue())) {
                        returnOrderResultEnter.setIsCancel(Boolean.TRUE);
                    } else if ("结案".equals(childNode.getChildNodes().item(0).getNodeValue())) {
                        returnOrderResultEnter.setIsCancel(Boolean.FALSE);
                    } else if ("验收中".equals(childNode.getChildNodes().item(0).getNodeValue())) {
                        returnOrderResultEnter.setIsCancel(Boolean.FALSE);
                    }
                }
            } else if ("C_PO_NO".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    returnOrderResultEnter.setReturnNumber(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("C_NOTE".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    returnOrderResultEnter.setErrorMessage(childNode.getChildNodes().item(0).getNodeValue());
                }
            }
            //TODO  加5个备用字段:<C_RESERVED1></C_RESERVED1><C_RESERVED2></C_RESERVED2><C_RESERVED3></C_RESERVED3><C_RESERVED4></C_RESERVED4><C_RESERVED5></C_RESERVED5>
        }
        return returnOrderResultEnter;
    }

    private WtaCancelOrderResultEnter mapping(WtaCancelOrderResultEnter orderResultEnter, Node childNode) {
        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            // 比较字段名
            if ("C_MK_DT".equalsIgnoreCase(childNode.getNodeName())) {
                // 有值
                if (null != childNode.getChildNodes().item(0)) {
                    orderResultEnter.setCreateTime(new Date());
                }
            } else if ("C_OP_STATUS".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    if ("已作废".equals(childNode.getChildNodes().item(0).getNodeValue())) {
                        orderResultEnter.setIsCancel(Boolean.TRUE);
                    } else {
                        orderResultEnter.setIsCancel(Boolean.FALSE);
                    }
                }
            } else if ("C_PO_NO".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    orderResultEnter.setOrderNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("C_NOTE".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    orderResultEnter.setErrorMessage(childNode.getChildNodes().item(0).getNodeValue());
                }
            }
            //TODO  加5个备用字段:<C_RESERVED1></C_RESERVED1><C_RESERVED2></C_RESERVED2><C_RESERVED3></C_RESERVED3><C_RESERVED4></C_RESERVED4><C_RESERVED5></C_RESERVED5>
        }
        return orderResultEnter;
    }


    //***************************下面是调用测试***********************************
    @Override
    public String getName(String userId) {
        return "liyd-" + userId;
    }

    @Override
    public String getUser(String userId) {
        TestUser testUser = new TestUser();
        testUser.setUsername("YL");
        testUser.setAge("20");
        testUser.setUserId("1");
        return JSON.toJSONString(testUser);
    }
}
