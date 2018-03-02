package cn.com.leyizhuang.app.remote.webservice.service.impl;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.getui.NoticePushUtils;
import cn.com.leyizhuang.app.core.pay.wechat.refund.OnlinePayRefundService;
import cn.com.leyizhuang.app.core.utils.DateUtil;
import cn.com.leyizhuang.app.core.utils.SmsUtils;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.CancelOrderParametersDO;
import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.WareHouseDO;
import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.*;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.*;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.remote.queue.SellDetailsSender;
import cn.com.leyizhuang.app.remote.queue.SinkSender;
import cn.com.leyizhuang.app.remote.webservice.TestUser;
import cn.com.leyizhuang.app.remote.webservice.service.ReleaseWMSService;
import cn.com.leyizhuang.app.remote.webservice.utils.AppXmlUtil;
import cn.com.leyizhuang.common.foundation.pojo.SmsAccount;
import cn.com.leyizhuang.common.util.AssertUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.map.HashedMap;
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
import java.util.Calendar;
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
    @Resource
    private CityService cityService;

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
                    //推送物流信息
                    NoticePushUtils.pushOrderLogisticInfo(header.getOrderNo());
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
            }

            //退货单返配上架头
            else if ("tbw_back_rec_m".equalsIgnoreCase(strTable)) {
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
                    City city = cityService.findByCityNumber(header.getCompanyId());
                    if (null == city) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,获取退货单返配上架头失败,任务编号 城市信息中没有查询到城市code为" + header.getCompanyId() + "的数据!");
                        return AppXmlUtil.resultStrXml(1, "城市信息中没有查询到城市code为" + header.getCompanyId() + "的数据!");
                    }
                    header.setCreateTime(Calendar.getInstance().getTime());
                    wmsToAppOrderService.saveWtaReturningOrderHeader(header);

                    HashedMap maps = returnOrderService.normalReturnOrderProcessing(header.getPoNo(), header.getCompanyId());

                    ReturnOrderBilling returnOrderBilling = (ReturnOrderBilling) maps.get("returnOrderBilling");

                    ReturnOrderBaseInfo returnOrderBaseInfo = (ReturnOrderBaseInfo) maps.get("returnOrderBaseInfo");

                    if ("SUCCESS".equals(maps.get("code"))) {
                        if ((Boolean) maps.get("hasReturnOnlinePay")) {
                            //返回第三方支付金额
                            if (null != returnOrderBilling.getOnlinePay() && returnOrderBilling.getOnlinePay() > AppConstant.PAY_UP_LIMIT) {
                                if (OnlinePayType.ALIPAY.equals(returnOrderBilling.getOnlinePayType())) {
                                    //支付宝退款
                                    onlinePayRefundService.alipayRefundRequest(
                                            returnOrderBaseInfo.getCreatorId(), returnOrderBaseInfo.getCreatorIdentityType().getValue(), returnOrderBaseInfo.getOrderNo(), returnOrderBaseInfo.getReturnNo(), returnOrderBilling.getOnlinePay());

                                } else if (OnlinePayType.WE_CHAT.equals(returnOrderBilling.getOnlinePayType())) {
                                    //微信退款方法类
                                    onlinePayRefundService.wechatReturnMoney(
                                            returnOrderBaseInfo.getCreatorId(), returnOrderBaseInfo.getCreatorIdentityType().getValue(), returnOrderBilling.getOnlinePay(), returnOrderBaseInfo.getOrderNo(), returnOrderBaseInfo.getReturnNo());

                                } else if (OnlinePayType.UNION_PAY.equals(returnOrderBilling.getOnlinePayType())) {
                                    //创建退单退款详情实体
                                    ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                                    returnOrderBillingDetail.setCreateTime(Calendar.getInstance().getTime());
                                    returnOrderBillingDetail.setRoid(returnOrderBaseInfo.getRoid());
                                    returnOrderBillingDetail.setRefundNumber(returnOrderBaseInfo.getReturnNo());
                                    //TODO 时间待定
                                    returnOrderBillingDetail.setIntoAmountTime(Calendar.getInstance().getTime());
                                    //TODO 第三方回复码
                                    returnOrderBillingDetail.setReplyCode("");
                                    returnOrderBillingDetail.setReturnMoney(returnOrderBilling.getOnlinePay());
                                    returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.UNION_PAY);
                                }
                            }
                        }
                        //修改取消订单处理状态
                        returnOrderService.updateReturnOrderStatus(returnOrderBaseInfo.getReturnNo(), AppReturnOrderStatus.FINISHED);
                        logger.info("cancelOrderToWms OUT,正常退货成功");
                    } else {
                        logger.info("getReturnOrderList OUT,正常退货失败,业务处理出现异常!");
                        return AppXmlUtil.resultStrXml(1, "正常退货业务逻辑处理失败!");
                    }
                    //保存物流信息
                    ReturnOrderDeliveryDetail returnOrderDeliveryDetail = ReturnOrderDeliveryDetail.transform(header);
                    returnOrderDeliveryDetailsService.addReturnOrderDeliveryInfoDetails(returnOrderDeliveryDetail);

                    // rabbitMq 记录退单销量
                    sellDetailsSender.sendReturnOrderSellDetailsTOManagement(header.getPoNo());
                }
                logger.info("GetWMSInfo OUT,获取返配单wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "NORMAL");
            }

            //退货单返配上架明细
            else if ("tbw_back_rec_d".equalsIgnoreCase(strTable)) {
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
            }

            //获取退货单配送取货配送员
            else if ("tbw_back_m".equalsIgnoreCase(strTable)) {
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node node = nodeList.item(i);
                    NodeList childNodeList = node.getChildNodes();

                    WtaReturnOrderDeliveryClerk deliveryClerk = new WtaReturnOrderDeliveryClerk();
                    for (int idx = 0; idx < childNodeList.getLength(); idx++) {
                        Node childNode = childNodeList.item(idx);
                        deliveryClerk = mapping(deliveryClerk, childNode);
                    }
                    if (AssertUtil.isEmpty(deliveryClerk.getDriver())) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,配送员(c_driver)不可为空,退单号 出参 return_no{}", deliveryClerk.getReturnNo());
                        return AppXmlUtil.resultStrXml(1, "配送员(c_driver)不可为空,退单号： " + deliveryClerk.getReturnNo() + "");
                    }
                    if (AssertUtil.isEmpty(deliveryClerk.getWarehouseNo())) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,仓库编号(c_whNo)不可为空,退单号 出参 return_no{}", deliveryClerk.getReturnNo());
                        return AppXmlUtil.resultStrXml(1, "仓库编号(c_whNo)不可为空,退单号： " + deliveryClerk.getReturnNo() + "");
                    }
                    deliveryClerk.setCreateTime(new Date());
                    wmsToAppOrderService.saveWtaReturnOrderDeliveryClerk(deliveryClerk);

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
            }

            //取消订单结果确认
            else if ("tbw_out_m_cancel".equalsIgnoreCase(strTable)) {
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
                logger.info("GetWMSInfo OUT,获取取消订单结果确认wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "NORMAL");
            }

            //取消退单结果确认
            else if ("tbw_back_m_cancel".equalsIgnoreCase(strTable)) {
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
                    } else {
                        //如果申请取消退货单失败退回为原来的退货申请状态
                        returnOrderService.updateReturnOrderStatus(returnOrderResultEnter.getReturnNumber(), AppReturnOrderStatus.PENDING_PICK_UP);
                    }
                }
                logger.info("GetWMSInfo OUT,获取取消退单结果确认wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "NORMAL");
            }

            //配送单物流详情
            else if ("tbw_out_m".equalsIgnoreCase(strTable)) {
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
            }

            //整转零
            else if ("tbw_whole_sep_direction".equalsIgnoreCase(strTable)) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    NodeList childNodeList = node.getChildNodes();

                    WtaWarehouseWholeSplitToUnit wholeSplitToUnit = new WtaWarehouseWholeSplitToUnit();
                    for (int idx = 0; idx < childNodeList.getLength(); idx++) {
                        Node childNode = childNodeList.item(idx);
                        wholeSplitToUnit = mapping(wholeSplitToUnit, childNode);
                    }

                    if (null == wholeSplitToUnit.getDQty()) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,获取整转零失败,任务编号 出参 DirectNo:{}", wholeSplitToUnit.getDirectNo());
                        return AppXmlUtil.resultStrXml(1, "cInQty不能为空!");
                    }
                    if (null == wholeSplitToUnit.getCompanyId()) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,获取整转零失败,任务编号 出参 DirectNo:{}", wholeSplitToUnit.getDirectNo());
                        return AppXmlUtil.resultStrXml(1, "cCompanyId不能为空!");
                    }
                    if (null == wholeSplitToUnit.getSku()) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,获取整转零失败,任务编号 出参 DirectNo:{}", wholeSplitToUnit.getDirectNo());
                        return AppXmlUtil.resultStrXml(1, "商品编码cGcode不能不为空！");
                    }
                    if (null == wholeSplitToUnit.getDSku()) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,获取整转零失败,任务编号 出参 DirectNo:{}", wholeSplitToUnit.getDirectNo());
                        return AppXmlUtil.resultStrXml(1, "商品编码cDGcode不能不为空！");
                    }
                    City city = cityService.findByCityNumber(wholeSplitToUnit.getCompanyId());
                    if (null == city) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,获取整转零失败,任务编号 城市信息中没有查询到城市code为" + wholeSplitToUnit.getCompanyId() + "的数据!");
                        return AppXmlUtil.resultStrXml(1, "城市信息中没有查询到城市code为" + wholeSplitToUnit.getCompanyId() + "的数据!");
                    }
                    GoodsDO goodsDO = goodsService.queryBySku(wholeSplitToUnit.getSku());
                    if (null == goodsDO) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,获取整转零失败,任务编号 商品资料中没有查询到sku为" + wholeSplitToUnit.getSku() + "的商品信息!");
                        return AppXmlUtil.resultStrXml(1, "商品资料中没有查询到sku为" + wholeSplitToUnit.getSku() + "的商品信息!");
                    }
                    GoodsDO dGoodsDO = goodsService.queryBySku(wholeSplitToUnit.getDSku());
                    if (null == dGoodsDO) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,获取整转零失败,任务编号 商品资料中没有查询到sku为" + wholeSplitToUnit.getDSku() + "的商品信息!");
                        return AppXmlUtil.resultStrXml(1, "商品资料中没有查询到sku为" + wholeSplitToUnit.getDSku() + "的商品信息!");
                    }

                    wmsToAppOrderService.saveWtaWarehouseWholeSplitToUnit(wholeSplitToUnit);
                    //扣整商品仓库数量
                    for (int j = 1; j <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; j++) {
                        CityInventory cityInventory = cityService.findCityInventoryByCityCodeAndSku(wholeSplitToUnit.getCompanyId(), wholeSplitToUnit.getSku());
                        if (null == cityInventory) {
                            cityInventory = CityInventory.transform(goodsDO, city);
                            cityService.saveCityInventory(cityInventory);
                        }
                        if (cityInventory.getAvailableIty() < wholeSplitToUnit.getQty()) {
                            logger.info("GetWMSInfo OUT,获取wms信息失败,获取整转零失败,任务编号 出参 DirectNo:{}", wholeSplitToUnit.getDirectNo());
                            return AppXmlUtil.resultStrXml(1, "该城市下sku为" + wholeSplitToUnit.getSku() + "的商品库存不足!");
                        }
                        Integer affectLine = cityService.lockCityInventoryByCityCodeAndSkuAndInventory(
                                wholeSplitToUnit.getCompanyId(), wholeSplitToUnit.getSku(), -wholeSplitToUnit.getQty(), cityInventory.getLastUpdateTime());
                        if (affectLine > 0) {
                            CityInventoryAvailableQtyChangeLog log = new CityInventoryAvailableQtyChangeLog();
                            log.setCityId(cityInventory.getCityId());
                            log.setCityName(cityInventory.getCityName());
                            log.setGid(cityInventory.getGid());
                            log.setSku(cityInventory.getSku());
                            log.setSkuName(cityInventory.getSkuName());
                            log.setChangeQty(wholeSplitToUnit.getQty());
                            log.setAfterChangeQty(cityInventory.getAvailableIty() - wholeSplitToUnit.getQty());
                            log.setChangeTime(Calendar.getInstance().getTime());
                            log.setChangeType(CityInventoryAvailableQtyChangeType.WHOLE_TO_SCRAPPY);
                            log.setChangeTypeDesc(CityInventoryAvailableQtyChangeType.WHOLE_TO_SCRAPPY.getDescription());
                            log.setReferenceNumber(wholeSplitToUnit.getDirectNo());
                            cityService.addCityInventoryAvailableQtyChangeLog(log);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                logger.info("GetWMSInfo OUT,获取wms信息失败,获取整转零失败,任务编号 出参 DirectNo:{}", wholeSplitToUnit.getDirectNo());
                                return AppXmlUtil.resultStrXml(1, "网络原因可能造成事务异常!");
                            }
                        }
                    }
                    //增加零商品仓库数量
                    for (int j = 1; j <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; j++) {
                        CityInventory cityInventory = cityService.findCityInventoryByCityCodeAndSku(wholeSplitToUnit.getCompanyId(), wholeSplitToUnit.getDSku());
                        if (null == cityInventory) {
                            cityInventory = CityInventory.transform(dGoodsDO, city);
                            cityService.saveCityInventory(cityInventory);
                        }
                        Integer affectLine = cityService.lockCityInventoryByCityCodeAndSkuAndInventory(
                                wholeSplitToUnit.getCompanyId(), wholeSplitToUnit.getDSku(), wholeSplitToUnit.getDQty(), cityInventory.getLastUpdateTime());
                        if (affectLine > 0) {
                            CityInventoryAvailableQtyChangeLog log = new CityInventoryAvailableQtyChangeLog();
                            log.setCityId(cityInventory.getCityId());
                            log.setCityName(cityInventory.getCityName());
                            log.setGid(cityInventory.getGid());
                            log.setSku(cityInventory.getSku());
                            log.setSkuName(cityInventory.getSkuName());
                            log.setChangeQty(wholeSplitToUnit.getDQty());
                            log.setAfterChangeQty(cityInventory.getAvailableIty() + wholeSplitToUnit.getDQty());
                            log.setChangeTime(Calendar.getInstance().getTime());
                            log.setChangeType(CityInventoryAvailableQtyChangeType.WHOLE_TO_SCRAPPY);
                            log.setChangeTypeDesc(CityInventoryAvailableQtyChangeType.WHOLE_TO_SCRAPPY.getDescription());
                            log.setReferenceNumber(wholeSplitToUnit.getDirectNo());
                            cityService.addCityInventoryAvailableQtyChangeLog(log);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                logger.info("GetWMSInfo OUT,获取wms信息失败,获取整转零失败,任务编号 出参 DirectNo:{}", wholeSplitToUnit.getDirectNo());
                                return AppXmlUtil.resultStrXml(1, "网络原因可能造成事务异常!");
                            }
                        }
                    }
                }
                logger.info("GetWMSInfo OUT,获取仓库整转零wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "NORMAL");
            }

            // 仓库调拨主档
            else if ("tbw_om_m".equalsIgnoreCase(strTable)) {

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    NodeList childNodeList = node.getChildNodes();

                    WtaWarehouseAllocationHeader allocation = new WtaWarehouseAllocationHeader();
                    for (int idx = 0; idx < childNodeList.getLength(); idx++) {
                        Node childNode = childNodeList.item(idx);
                        allocation = mapping(allocation, childNode);
                    }

                    if (StringUtils.isBlank(allocation.getUploadStatus())
                            || (!"in".equalsIgnoreCase(allocation.getUploadStatus()) && !"out".equalsIgnoreCase(allocation.getUploadStatus()))) {
                        return "<RESULTS><STATUS><CODE>1</CODE><MESSAGE>c_upload_status：" + allocation.getUploadStatus()
                                + " 为空或者不属于\"in\"且不属于\"out\"</MESSAGE></STATUS></RESULTS>";
                    }
                    if (null == allocation.getCompanyId()) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,获取仓库调拨失败,任务编号 出参 AllocationNo:{}", allocation.getAllocationNo());
                        return AppXmlUtil.resultStrXml(1, "cCompanyId不能为空!");
                    }
                    City city = cityService.findByCityNumber(allocation.getCompanyId());
                    if (null == city) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,获取仓库调拨失败,任务编号 城市信息中没有查询到城市code为" + allocation.getCompanyId() + "的数据!");
                        return AppXmlUtil.resultStrXml(1, "城市信息中没有查询到城市code为" + allocation.getCompanyId() + "的数据!");
                    }
                    List<WtaWarehouseAllocationGoods> allocationGoodsList = wmsToAppOrderService.findWtaWarehouseAllocationGoodsListByAllocationNo(allocation.getAllocationNo());
                    for (WtaWarehouseAllocationGoods allocationGoods : allocationGoodsList) {
                        GoodsDO goodsDO = goodsService.queryBySku(allocationGoods.getSku());
                        if (null == goodsDO) {
                            logger.info("GetWMSInfo OUT,获取wms信息失败,获取仓库调拨失败,任务编号 商品资料中没有查询到sku为" + allocationGoods.getSku() + "的商品信息!");
                            return AppXmlUtil.resultStrXml(1, "商品资料中没有查询到sku为" + allocationGoods.getSku() + "的商品信息!");
                        }
                        Integer changeInventory = 0;
                        CityInventoryAvailableQtyChangeType changeType = null;
                        if ("in".equalsIgnoreCase(allocation.getUploadStatus())) {
                            changeType = CityInventoryAvailableQtyChangeType.ALLOCATE_INBOUND;
                            changeInventory = allocationGoods.getCheckQty();
                        } else if ("out".equalsIgnoreCase(allocation.getUploadStatus())) {
                            changeInventory = -allocationGoods.getCheckQty();
                            changeType = CityInventoryAvailableQtyChangeType.ALLOCATE_OUTBOUND;
                        }
                        for (int j = 1; j <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; j++) {
                            CityInventory cityInventory = cityService.findCityInventoryByCityCodeAndSku(allocation.getCompanyId(), allocationGoods.getSku());
                            if (null == cityInventory) {
                                cityInventory = CityInventory.transform(goodsDO, city);
                                cityService.saveCityInventory(cityInventory);
                            }
                            if (cityInventory.getAvailableIty() < allocationGoods.getCheckQty() && changeInventory < 0) {
                                logger.info("GetWMSInfo OUT,获取wms信息失败,获取仓库调拨失败,任务编号 出参 AllocationNo:{}", allocation.getAllocationNo());
                                return AppXmlUtil.resultStrXml(1, "该城市下sku为" + allocationGoods.getSku() + "的商品库存不足!");
                            }
                            Integer affectLine = cityService.lockCityInventoryByCityCodeAndSkuAndInventory(
                                    allocation.getCompanyId(), allocationGoods.getSku(), changeInventory, cityInventory.getLastUpdateTime());
                            if (affectLine > 0) {
                                CityInventoryAvailableQtyChangeLog log = new CityInventoryAvailableQtyChangeLog();
                                log.setCityId(cityInventory.getCityId());
                                log.setCityName(cityInventory.getCityName());
                                log.setGid(cityInventory.getGid());
                                log.setSku(cityInventory.getSku());
                                log.setSkuName(cityInventory.getSkuName());
                                log.setChangeQty(allocationGoods.getCheckQty());
                                log.setAfterChangeQty(cityInventory.getAvailableIty() + changeInventory);
                                log.setChangeTime(Calendar.getInstance().getTime());
                                log.setChangeType(changeType);
                                log.setChangeTypeDesc(changeType.getDescription());
                                log.setReferenceNumber(allocation.getAllocationNo());
                                cityService.addCityInventoryAvailableQtyChangeLog(log);
                                break;
                            } else {
                                if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                    logger.info("GetWMSInfo OUT,获取wms信息失败,获取仓库调拨失败,任务编号 出参 AllocationNo:{}", allocation.getAllocationNo());
                                    return AppXmlUtil.resultStrXml(1, "网络原因可能造成事务异常!");
                                }
                            }
                        }
                    }
                    wmsToAppOrderService.saveWtaWarehouseAllocationHeader(allocation);
                }
                logger.info("GetWMSInfo OUT,获取仓库调拨头档wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "NORMAL");
            }

            // 仓库调拨明细
            else if ("tbw_om_d".equalsIgnoreCase(strTable)) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    NodeList childNodeList = node.getChildNodes();

                    WtaWarehouseAllocationGoods allocationGoods = new WtaWarehouseAllocationGoods();
                    for (int idx = 0; idx < childNodeList.getLength(); idx++) {
                        Node childNode = childNodeList.item(idx);
                        allocationGoods = mapping(allocationGoods, childNode);
                    }
                    wmsToAppOrderService.saveWtaWarehouseAllocationGoods(allocationGoods);
                }
                logger.info("GetWMSInfo OUT,获取仓库调拨明细wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "NORMAL");
            }

            //仓库采购主档
            else if ("tbw_rec_m".equalsIgnoreCase(strTable)) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    NodeList childNodeList = node.getChildNodes();

                    WtaWarehousePurchaseHeader purchaseHeader = new WtaWarehousePurchaseHeader();
                    for (int idx = 0; idx < childNodeList.getLength(); idx++) {
                        Node childNode = childNodeList.item(idx);
                        purchaseHeader = mapping(purchaseHeader, childNode);
                    }
                    City city = cityService.findByCityNumber(purchaseHeader.getCompanyId());
                    if (null == city) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,获取仓库采购入库失败,任务编号 城市信息中没有查询到城市code为" + purchaseHeader.getCompanyId() + "的数据!");
                        return AppXmlUtil.resultStrXml(1, "城市信息中没有查询到城市code为" + purchaseHeader.getCompanyId() + "的数据!");
                    }

                    List<WtaWarehousePurchaseGoods> purchaseGoodsList = wmsToAppOrderService.findWtaWarehousePurchaseGoodsListByPurchaseNo(purchaseHeader.getRecNo());
                    for (WtaWarehousePurchaseGoods purchaseGoods : purchaseGoodsList) {
                        GoodsDO goodsDO = goodsService.queryBySku(purchaseGoods.getSku());
                        if (null == goodsDO) {
                            logger.info("GetWMSInfo OUT,获取wms信息失败,获取仓库采购入库失败,任务编号 商品资料中没有查询到sku为" + purchaseGoods.getSku() + "的商品信息!");
                            return AppXmlUtil.resultStrXml(1, "商品资料中没有查询到sku为" + purchaseGoods.getSku() + "的商品信息!");
                        }
                        for (int j = 1; j <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; j++) {
                            CityInventory cityInventory = cityService.findCityInventoryByCityCodeAndSku(purchaseHeader.getCompanyId(), purchaseGoods.getSku());
                            if (null == cityInventory) {
                                cityInventory = CityInventory.transform(goodsDO, city);
                                cityService.saveCityInventory(cityInventory);
                            }
                            Integer affectLine = cityService.lockCityInventoryByCityCodeAndSkuAndInventory(
                                    purchaseHeader.getCompanyId(), purchaseGoods.getSku(), purchaseGoods.getRecQty(), cityInventory.getLastUpdateTime());
                            if (affectLine > 0) {
                                CityInventoryAvailableQtyChangeLog log = new CityInventoryAvailableQtyChangeLog();
                                log.setCityId(cityInventory.getCityId());
                                log.setCityName(cityInventory.getCityName());
                                log.setGid(cityInventory.getGid());
                                log.setSku(cityInventory.getSku());
                                log.setSkuName(cityInventory.getSkuName());
                                log.setChangeQty(purchaseGoods.getRecQty());
                                log.setAfterChangeQty(cityInventory.getAvailableIty() + purchaseGoods.getRecQty());
                                log.setChangeTime(Calendar.getInstance().getTime());
                                log.setChangeType(CityInventoryAvailableQtyChangeType.CITY_PURCHASE_INBOUND);
                                log.setChangeTypeDesc(CityInventoryAvailableQtyChangeType.CITY_PURCHASE_INBOUND.getDescription());
                                log.setReferenceNumber(purchaseHeader.getPurchaseNo());
                                cityService.addCityInventoryAvailableQtyChangeLog(log);
                                break;
                            } else {
                                if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                    logger.info("GetWMSInfo OUT,获取wms信息失败,获取仓库采购失败,任务编号 出参 PurchaseNo:{}", purchaseHeader.getPurchaseNo());
                                    return AppXmlUtil.resultStrXml(1, "网络原因可能造成事务异常!");
                                }
                            }
                        }
                    }
                    wmsToAppOrderService.saveWtaWarehousePurchaseHeader(purchaseHeader);
                }
                logger.info("GetWMSInfo OUT,获取仓库采购主档wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "NORMAL");
            }

            // 仓库采购入库明细
            else if ("tbw_rec_d".equalsIgnoreCase(strTable)) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    NodeList childNodeList = node.getChildNodes();

                    WtaWarehousePurchaseGoods purchaseGoods = new WtaWarehousePurchaseGoods();
                    for (int idx = 0; idx < childNodeList.getLength(); idx++) {
                        Node childNode = childNodeList.item(idx);
                        purchaseGoods = mapping(purchaseGoods, childNode);
                    }
                    wmsToAppOrderService.saveWtaWarehousePurchaseGoods(purchaseGoods);
                }
                logger.info("GetWMSInfo OUT,获取仓库采购入库明细wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "NORMAL");
            }

            // 仓库报损报溢
            else if ("tbw_waste_view".equalsIgnoreCase(strTable)) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    NodeList childNodeList = node.getChildNodes();

                    WtaWarehouseReportDamageAndOverflow damageAndOverflow = new WtaWarehouseReportDamageAndOverflow();
                    for (int idx = 0; idx < childNodeList.getLength(); idx++) {
                        Node childNode = childNodeList.item(idx);
                        damageAndOverflow = mapping(damageAndOverflow, childNode);
                    }
                    if (damageAndOverflow.getWasteNo() == null) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,获取报损报溢失败,任务编号 损溢单号不能为空!");
                        return AppXmlUtil.resultStrXml(1, "损溢单号不能为空");
                    }
                    if (damageAndOverflow.getWasteId() == null) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,获取报损报溢失败,任务编号 损溢单号id不能为空!");
                        return AppXmlUtil.resultStrXml(1, "损溢单号id不能为空");
                    }
                    City city = cityService.findByCityNumber(damageAndOverflow.getCompanyId());
                    if (null == city) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,获取报损报溢失败,任务编号 城市信息中没有查询到城市code为" + damageAndOverflow.getCompanyId() + "的数据!");
                        return AppXmlUtil.resultStrXml(1, "城市信息中没有查询到城市code为" + damageAndOverflow.getCompanyId() + "的数据!");
                    }
                    GoodsDO goodsDO = goodsService.queryBySku(damageAndOverflow.getSku());
                    if (null == goodsDO) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,获取报损报溢失败,任务编号 商品资料中没有查询到sku为" + damageAndOverflow.getSku() + "的商品信息!");
                        return AppXmlUtil.resultStrXml(1, "商品资料中没有查询到sku为" + damageAndOverflow.getSku() + "的商品信息!");
                    }
                    wmsToAppOrderService.saveWtaWarehouseReportDamageAndOverflow(damageAndOverflow);

                    Integer changeInventory = 0;
                    CityInventoryAvailableQtyChangeType changeType = null;
                    String cityCode = damageAndOverflow.getCompanyId();
                    String sku = damageAndOverflow.getSku();
                    Integer qty = damageAndOverflow.getQty();
                    if (damageAndOverflow.getWasteType().contains("一般报溢")) {
                        changeType = CityInventoryAvailableQtyChangeType.CITY_OVERFLOW;
                        changeInventory = qty;
                    } else {
                        changeInventory = -qty;
                        changeType = CityInventoryAvailableQtyChangeType.CITY_WASTAGE;
                    }
                    for (int j = 1; j <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; j++) {
                        CityInventory cityInventory = cityService.findCityInventoryByCityCodeAndSku(cityCode, sku);
                        if (null == cityInventory) {
                            cityInventory = CityInventory.transform(goodsDO, city);
                            cityService.saveCityInventory(cityInventory);
                        }
                        if (cityInventory.getAvailableIty() < qty && changeInventory < 0) {
                            logger.info("GetWMSInfo OUT,获取wms信息失败,获取报损报溢失败,任务编号 出参 WasteNo:{}", damageAndOverflow.getWasteNo());
                            return AppXmlUtil.resultStrXml(1, "该城市下sku为" + sku + "的商品库存不足!");
                        }
                        Integer affectLine = cityService.lockCityInventoryByCityCodeAndSkuAndInventory(cityCode, sku, changeInventory, cityInventory.getLastUpdateTime());
                        if (affectLine > 0) {
                            CityInventoryAvailableQtyChangeLog log = new CityInventoryAvailableQtyChangeLog();
                            log.setCityId(cityInventory.getCityId());
                            log.setCityName(cityInventory.getCityName());
                            log.setGid(cityInventory.getGid());
                            log.setSku(cityInventory.getSku());
                            log.setSkuName(cityInventory.getSkuName());
                            log.setChangeQty(damageAndOverflow.getQty());
                            log.setAfterChangeQty(cityInventory.getAvailableIty() + changeInventory);
                            log.setChangeTime(Calendar.getInstance().getTime());
                            log.setChangeType(changeType);
                            log.setChangeTypeDesc(changeType.getDescription());
                            log.setReferenceNumber(damageAndOverflow.getWasteNo());
                            cityService.addCityInventoryAvailableQtyChangeLog(log);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                logger.info("GetWMSInfo OUT,获取wms信息失败,获取报损报溢失败,任务编号 出参 WasteNo:{}", damageAndOverflow.getWasteNo());
                                return AppXmlUtil.resultStrXml(1, "网络原因可能造成事务异常!");
                            }
                        }
                    }
                }
                logger.info("GetWMSInfo OUT,获取仓库报损报溢wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "NORMAL");
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


    //*************************************下面是字段映射匹配,无业务逻辑****************************************

    private WtaWarehouseReportDamageAndOverflow mapping(WtaWarehouseReportDamageAndOverflow damageAndOverflow, Node childNode) {
        if (childNode.getNodeType() == Node.ELEMENT_NODE) {

            if ("c_wh_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    damageAndOverflow.setWarehouseNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_owner_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    damageAndOverflow.setOwnerNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_waste_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    damageAndOverflow.setWasteNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_waste_id".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    damageAndOverflow.setWasteId(Long.parseLong(childNode.getChildNodes().item(0).getNodeValue()));
                }
            } else if ("c_waste_type".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    damageAndOverflow.setWasteType(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_gcode".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    damageAndOverflow.setSku(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_qty".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    damageAndOverflow.setQty((int) Double.parseDouble(childNode.getChildNodes().item(0).getNodeValue()));
                }
            } else if ("c_op_status".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    damageAndOverflow.setWasteStatus(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("C_COMPANY_ID".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    damageAndOverflow.setCompanyId(childNode.getChildNodes().item(0).getNodeValue());
                }
            }
        }
        return damageAndOverflow;
    }

    private WtaWarehousePurchaseGoods mapping(WtaWarehousePurchaseGoods purchaseGoods, Node childNode) {

        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            if ("c_rec_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    purchaseGoods.setRecNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_rec_id".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    purchaseGoods.setRecId(Long.parseLong(childNode.getChildNodes().item(0).getNodeValue()));
                }
            } else if ("c_gcode".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    purchaseGoods.setSku(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_rec_qty".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    purchaseGoods.setRecQty((int) Double.parseDouble(childNode.getChildNodes().item(0).getNodeValue()));
                }
            }
        }
        return purchaseGoods;
    }

    private WtaWarehousePurchaseHeader mapping(WtaWarehousePurchaseHeader purchaseHeader, Node childNode) {

        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            if ("c_wh_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    purchaseHeader.setWarehouseNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_rec_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    purchaseHeader.setRecNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_gather_rec_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    purchaseHeader.setGatherRecNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_gather_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    purchaseHeader.setGatherNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_in_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    purchaseHeader.setInNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_note".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    purchaseHeader.setNote(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_po_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    purchaseHeader.setPurchaseNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_company_id".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    purchaseHeader.setCompanyId(childNode.getChildNodes().item(0).getNodeValue());
                }
            }
        }
        return purchaseHeader;
    }

    private WtaWarehouseAllocationGoods mapping(WtaWarehouseAllocationGoods allocationGoods, Node childNode) {

        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            if ("c_om_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    allocationGoods.setAllocationNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_om_id".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    allocationGoods.setAllocationId(Long.parseLong(childNode.getChildNodes().item(0).getNodeValue()));
                }
            } else if ("c_gcode".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    allocationGoods.setSku(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_ack_qty".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    allocationGoods.setAckQty((int) Double.parseDouble(childNode.getChildNodes().item(0).getNodeValue()));
                }
            } else if ("c_check_qty".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    allocationGoods.setCheckQty((int) Double.parseDouble(childNode.getChildNodes().item(0).getNodeValue()));
                }
            } else if ("c_note".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    allocationGoods.setNote(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_check_dt".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    allocationGoods.setCheckTime(DateUtil.dateFromString(childNode.getChildNodes().item(0).getNodeValue()));
                }
            }
        }
        return allocationGoods;
    }

    private WtaWarehouseAllocationHeader mapping(WtaWarehouseAllocationHeader allocation, Node childNode) {
        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            if ("c_wh_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    allocation.setWarehouseNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_owner_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    allocation.setOwnerNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_om_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    allocation.setAllocationNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_om_type".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    allocation.setAllocationType(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_d_wh_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    allocation.setShippingWarehouseNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_po_type".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    allocation.setPoType(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_po_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    allocation.setPoNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_op_status".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    allocation.setAllocationStatus(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_note".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    allocation.setNote(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_mk_dt".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    allocation.setCreateTime(DateUtil.dateFromString(childNode.getChildNodes().item(0).getNodeValue()));
                }
            } else if ("c_modified_dt".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    allocation.setModifyTime(DateUtil.dateFromString(childNode.getChildNodes().item(0).getNodeValue()));
                }
            } else if ("c_upload_status".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    allocation.setUploadStatus(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_company_id".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    allocation.setCompanyId(childNode.getChildNodes().item(0).getNodeValue());
                }
            }
        }
        return allocation;
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
            if ("c_wh_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wholeSplitToUnit.setWarehouseNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_owner_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wholeSplitToUnit.setOwnerNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_direct_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wholeSplitToUnit.setDirectNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_gcode".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wholeSplitToUnit.setSku(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_d_gcode".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wholeSplitToUnit.setDSku(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_qty".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wholeSplitToUnit.setQty(((int) Double.parseDouble(childNode.getChildNodes().item(0).getNodeValue())));
                }
            } else if ("c_in_qty".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wholeSplitToUnit.setDQty(((int) Double.parseDouble(childNode.getChildNodes().item(0).getNodeValue())));
                }
            } else if ("c_status".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wholeSplitToUnit.setStatus(childNode.getChildNodes().item(0).getNodeValue());
                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("c_reserved1")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    cReserved1 = childNode.getChildNodes().item(0).getNodeValue();
//                }
            } else if ("c_mk_userno".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wholeSplitToUnit.setCreatorNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_mk_dt".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wholeSplitToUnit.setCreateTime(DateUtil.dateFromString(childNode.getChildNodes().item(0).getNodeValue()));
                }
            } else if ("c_company_id".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wholeSplitToUnit.setCompanyId(childNode.getChildNodes().item(0).getNodeValue());
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
                    returningOrderHeader.setCompanyId(childNode.getChildNodes().item(0).getNodeValue());
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
                    goods.setRecQty((int) Double.parseDouble(childNode.getChildNodes().item(0).getNodeValue()));
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
