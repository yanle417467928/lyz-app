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
import cn.com.leyizhuang.app.remote.webservice.service.ReleaseEBSService;
import cn.com.leyizhuang.app.remote.webservice.service.ReleaseWMSService;
import cn.com.leyizhuang.app.remote.webservice.utils.AppXmlUtil;
import cn.com.leyizhuang.common.foundation.pojo.SmsAccount;
import cn.com.leyizhuang.common.util.AssertUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
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
import java.util.List;
import java.util.Map;

/**
 * @author liuh
 * Created on 2017-12-19 11:24
 **/
@WebService(targetNamespace = "http://cn.com.leyizhuang.app.remote.webservice.service",
        endpointInterface = "cn.com.leyizhuang.app.remote.webservice.service.ReleaseEBSService")
public class ReleaseEBSServiceImpl implements ReleaseEBSService {

    private static final Logger logger = LoggerFactory.getLogger(ReleaseEBSServiceImpl.class);

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
    public String GetEBSInfo(String strTable, String strType, String xml) {

        logger.info("GetEBSInfo CALLED,获取wms信息，入参 strTable:{},strType:{},xml:{}", strTable, strType, xml);

        if (StringUtils.isBlank(strTable) || "?".equals(strTable)) {
            logger.info("GetEBSInfo OUT,获取wms信息失败 出参 strTable:{}", strTable);
            return AppXmlUtil.resultStrXml(1, "STRTABLE参数错误");
        }

        if (StringUtils.isBlank(xml) || "?".equals(xml)) {
            logger.info("GetEBSInfo OUT,获取wms信息失败 出参 strTable:{}", xml);
            return AppXmlUtil.resultStrXml(1, "XML参数错误");
        }

        try {
            Document document = AppXmlUtil.parseStrXml(xml);

            if (null == document) {
                logger.info("GetEBSInfo OUT,获取wms信息失败");
                return AppXmlUtil.resultStrXml(1, "解密后XML数据为空");
            }

            NodeList nodeList = document.getElementsByTagName("TABLE");
            //直营要货
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
                        logger.info("GetEBSInfo OUT,获取wms信息失败,配送员不能为空,任务编号 出参 c_task_no:{}", header.getTaskNo());
                        return AppXmlUtil.resultStrXml(1, "配送员编号不能为空,任务编号" + header.getTaskNo() + "");
                    }
                    header.setCreateTime(Calendar.getInstance().getTime());
                    int result = wmsToAppOrderService.saveWtaShippingOrderHeader(header);
                    if (result == 0) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,该单已存在 出参 order_no:{}", header.getOrderNo());
                        return AppXmlUtil.resultStrXml(1, "重复传输,该单已存在!");
                    }
                    //查询是否存在
                    List<OrderDeliveryInfoDetails> deliveryInfoDetailsList = orderDeliveryInfoDetailsService.queryListByOrderNumber(header.getOrderNo());
                    if (AssertUtil.isEmpty(deliveryInfoDetailsList)) {
                        logger.info("GetEBSInfo OUT,获取wms信息失败,该订单不存在 出参 order_no:{}", header.getOrderNo());
                        return AppXmlUtil.resultStrXml(1, "App没有找到该订单");
                    }
                    WareHouseDO wareHouse = wareHouseService.findByWareHouseNo(header.getWhNo());
                    try {
                        //保存物流信息
                        OrderDeliveryInfoDetails deliveryInfoDetails = OrderDeliveryInfoDetails.transform(header,
                                null != wareHouse ? wareHouse.getWareHouseName() : header.getWhNo());
                        orderDeliveryInfoDetailsService.addOrderDeliveryInfoDetails(deliveryInfoDetails);
                        //修改订单配送信息加入配送员
                        appOrderService.updateOrderLogisticInfoByDeliveryClerkNo(header.getDriver(), header.getWhNo(), header.getOrderNo());
                        //修改订单头状态
                        appOrderService.updateOrderStatusAndDeliveryStatusByOrderNo(AppOrderStatus.PENDING_RECEIVE, LogisticStatus.SEALED_CAR, header.getOrderNo());
                    } catch (Exception e) {
                        logger.info("GetEBSInfo OUT,获取出货单头档wms信息失败,发生未知异常 出参 e:{}", e);
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return AppXmlUtil.resultStrXml(1, "处理出货单头档事务失败!");
                    }
                    //推送物流信息
                    NoticePushUtils.pushOrderLogisticInfo(header.getOrderNo());
                    // rabbit 记录下单销量
                    sellDetailsSender.sendOrderSellDetailsTOManagement(header.getOrderNo());
                }
                logger.info("GetEBSInfo OUT,获取wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "NORMAL");
            }

            //直营退货
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
                        logger.info("GetEBSInfo OUT,获取wms信息失败,商品不存在 出参 c_gcode:{}", goods.getGCode());
                        return AppXmlUtil.resultStrXml(1, "编码为" + goods.getGCode() + "的商品不存在");
                    }
                    int result = wmsToAppOrderService.saveWtaShippingOrderGoods(goods);
                    if (result == 0) {
                        logger.info("GetEBSInfo OUT,获取wms信息失败,商品已存在 出参 c_gcode:{}", goods.getGCode());
                        return AppXmlUtil.resultStrXml(1, "重复传输,编码为" + goods.getGCode() + "的商品已存在");
                    }
                    //跟新订单的出货数量
                    appOrderService.updateOrderGoodsShippingQuantity(goods.getOrderNo(), goods.getGCode(), goods.getDAckQty());
                }
                logger.info("GetEBSInfo OUT,获取wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "NORMAL");
            }
        }catch (ParserConfigurationException e) {
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
                    returnOrderResultEnter.setCreateTime(Calendar.getInstance().getTime());
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
                    orderResultEnter.setCreateTime(Calendar.getInstance().getTime());
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
