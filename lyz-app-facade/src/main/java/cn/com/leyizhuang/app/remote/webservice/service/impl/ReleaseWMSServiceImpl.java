package cn.com.leyizhuang.app.remote.webservice.service.impl;

import cn.com.leyizhuang.app.core.constant.LogisticStatus;
import cn.com.leyizhuang.app.core.utils.DateUtil;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.*;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderDeliveryDetail;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.remote.queue.SellDetailsSender;
import cn.com.leyizhuang.app.remote.webservice.TestUser;
import cn.com.leyizhuang.app.remote.webservice.service.ReleaseWMSService;
import cn.com.leyizhuang.app.remote.webservice.utils.AppXmlUtil;
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
import java.util.List;

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
                    wmsToAppOrderService.saveWtaShippingOrderHeader(header);
                    //查询是否存在
                    List<OrderDeliveryInfoDetails> deliveryInfoDetailsList = orderDeliveryInfoDetailsService.queryListByOrderNumber(header.getOrderNo());
                    if (AssertUtil.isEmpty(deliveryInfoDetailsList)) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,该订单不存在 出参 order_no:{}", header.getOrderNo());
                        return AppXmlUtil.resultStrXml(1, "App没有找到该订单");
                    }
                    //保存物流信息
                    OrderDeliveryInfoDetails deliveryInfoDetails = OrderDeliveryInfoDetails.transform(header);
                    orderDeliveryInfoDetailsService.addOrderDeliveryInfoDetails(deliveryInfoDetails);

                    // rabbit 记录下单销量
                    sellDetailsSender.sendOrderSellDetailsTOManagement(header.getOrderNo());
                }
                logger.info("GetWMSInfo OUT,获取wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "");
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
                }
                logger.info("GetWMSInfo OUT,获取wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "");

                //退货单返配上架头
            } else if ("tbw_back_m".equalsIgnoreCase(strTable)) {
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

                    wmsToAppOrderService.saveWtaReturningOrderHeader(header);

                    //保存物流信息
                    ReturnOrderDeliveryDetail returnOrderDeliveryDetail = ReturnOrderDeliveryDetail.transform(header);
                    returnOrderDeliveryDetailsService.addReturnOrderDeliveryInfoDetails(returnOrderDeliveryDetail);


                    // rabbitMq 记录退单销量
                    sellDetailsSender.sendReturnOrderSellDetailsTOManagement(header.getPoNo());
                }
                logger.info("GetWMSInfo OUT,获取返配单wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "");

                //退货单返配上架明细
            } else if ("tbw_back_d".equalsIgnoreCase(strTable)) {
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
                return AppXmlUtil.resultStrXml(0, "");

                //获取退货单配送取货配送员
            } else if ("tbw_send_task_Driver".equalsIgnoreCase(strTable)) {
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node node = nodeList.item(i);
                    NodeList childNodeList = node.getChildNodes();

                    WtaReturnOrderDeliveryClerk deliveryClerk = new WtaReturnOrderDeliveryClerk();
                    for (int idx = 0; idx < childNodeList.getLength(); idx++) {
                        Node childNode = childNodeList.item(idx);
                        deliveryClerk = mapping(deliveryClerk, childNode);
                    }
                    wmsToAppOrderService.saveWtaReturnOrderDeliveryClerk(deliveryClerk);
//                    ReturnOrderDeliveryDetail deliveryInfoDetails = returnOrderDeliveryDetailsService.findReturnNumber(deliveryClerk.getReturnNo());
//
//                    if (AssertUtil.isEmpty(deliveryInfoDetails)) {
//                        logger.info("GetWMSInfo OUT,获取wms信息失败,未查询到该配送单,退单号 出参 return_no{}", deliveryClerk.getReturnNo());
//                        return AppXmlUtil.resultStrXml(1, "未找到该任务的配送单,退单号： " + deliveryClerk.getReturnNo() + "");
//                    }
//                    deliveryInfoDetails.setWarehouseNo(wtaUpdateDeliveryInfo.getWarehouseNo());
//                    deliveryInfoDetails.setOperationType(wtaUpdateDeliveryInfo.getOperatorStatus());
//                    deliveryInfoDetails.setOperatorNo(wtaUpdateDeliveryInfo.getDriver());
//                    deliveryInfoDetails.setOrderNo(wtaUpdateDeliveryInfo.getReserved1());
//                    returnOrderDeliveryDetailsService.modifyReturnOrderDeliveryInfoDetails(deliveryInfoDetails);
                }
                logger.info("GetWMSInfo OUT,修改配送员信息wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "");
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
                }
                logger.info("GetWMSInfo OUT,获取返配单商品wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "");
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
                }
                logger.info("GetWMSInfo OUT,获取返配单商品wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "");
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
                    orderDeliveryInfoDetailsService.addOrderDeliveryInfoDetails(orderDeliveryInfoDetails);
                }
                logger.info("GetWMSInfo OUT,获取wms物流信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "");
                //获取仓库列表
            } else if ("".equalsIgnoreCase(strTable)) {

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
     * 将wms结果集映射到实体上
     *
     * @param childNode 信息节点
     * @return 出货头档
     */
    private WtaShippingOrderHeader mapping(WtaShippingOrderHeader wtaShippingOrderHeader, Node childNode) {

        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
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
                    wtaShippingOrderHeader.setEndDt(DateUtil.parseDate(childNode.getChildNodes().item(0).getNodeValue()));
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
            } else if ("c_reserved1".equalsIgnoreCase(childNode.getNodeName())) {
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
            }
        }
        return returningOrderHeader;
    }

    private WtaReturningOrderGoods mapping(WtaReturningOrderGoods goods, Node childNode) {
        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            // 比较字段名
            if ("c_owner_no".equalsIgnoreCase(childNode.getNodeName())) {
                // 有值
//                if (null != childNode.getChildNodes().item(0)) {
//                    goods.setOwnerNo(childNode.getChildNodes().item(0).getNodeValue());
//                }
            }
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
//            } else if (childNode.getNodeName().equalsIgnoreCase("c_plat_no")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    platNo = childNode.getChildNodes().item(0).getNodeValue();
//                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("c_op_tools")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    opTools = childNode.getChildNodes().item(0).getNodeValue();
//                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("c_op_status")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    opStatus = childNode.getChildNodes().item(0).getNodeValue();
//                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("c_reserved1")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    reserved1 = childNode.getChildNodes().item(0).getNodeValue();
//                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("c_reserved2")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    reserved2 = childNode.getChildNodes().item(0).getNodeValue();
//                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("c_reserved3")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    reserved3 = childNode.getChildNodes().item(0).getNodeValue();
//                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("c_reserved4")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    reserved4 = childNode.getChildNodes().item(0).getNodeValue();
//                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("c_reserved5")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    reserved5 = childNode.getChildNodes().item(0).getNodeValue();
//                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("c_note")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    note = childNode.getChildNodes().item(0).getNodeValue();
//                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("c_mk_userno")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    mkUserno = childNode.getChildNodes().item(0).getNodeValue();
//                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("c_mk_dt")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    c_mk_dt = childNode.getChildNodes().item(0).getNodeValue();
//                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("c_modified_userno")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    c_mk_dt = childNode.getChildNodes().item(0).getNodeValue();
//                }
//            } else if (childNode.getNodeName().equalsIgnoreCase("c_modified_dt")) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    c_modified_dt = childNode.getChildNodes().item(0).getNodeValue();
//                }
            }
        }
        return goods;
    }

    private WtaReturnOrderDeliveryClerk mapping(WtaReturnOrderDeliveryClerk returnOrderDeliveryClerk, Node childNode) {
        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            // 比较字段名
            if ("create_time".equalsIgnoreCase(childNode.getNodeName())) {
                // 有值
                if (null != childNode.getChildNodes().item(0)) {
                    returnOrderDeliveryClerk.setCreateTime(DateUtil.parseDate(childNode.getChildNodes().item(0).getNodeValue()));
                }
            } else if ("c_wh_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    returnOrderDeliveryClerk.setWarehouseNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("return_number".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    returnOrderDeliveryClerk.setReturnNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_Driver".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    returnOrderDeliveryClerk.setDriver(childNode.getChildNodes().item(0).getNodeValue());
                }
            }
        }
        return returnOrderDeliveryClerk;
    }

    private OrderDeliveryInfoDetails mapping(OrderDeliveryInfoDetails orderDeliveryInfoDetails, Node childNode) {
        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
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
                    orderDeliveryInfoDetails.setCreateTime(DateUtil.parseDate(childNode.getChildNodes().item(0).getNodeValue()));
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
                    returnOrderResultEnter.setCreateTime(DateUtil.parseDate(childNode.getChildNodes().item(0).getNodeValue()));
                }
            } else if ("C_OP_STATUS".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    if ("已作废".equals(childNode.getChildNodes().item(0).getNodeValue())) {
                        returnOrderResultEnter.setIsCancel(Boolean.TRUE);
                    } else if ("结案/验收中".equals(childNode.getChildNodes().item(0).getNodeValue())) {
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
                    orderResultEnter.setCreateTime(DateUtil.parseDate(childNode.getChildNodes().item(0).getNodeValue()));
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
