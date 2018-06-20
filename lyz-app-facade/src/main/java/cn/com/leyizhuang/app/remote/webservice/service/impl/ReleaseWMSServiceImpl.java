package cn.com.leyizhuang.app.remote.webservice.service.impl;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.getui.NoticePushUtils;
import cn.com.leyizhuang.app.core.lock.RedisLock;
import cn.com.leyizhuang.app.core.pay.wechat.refund.OnlinePayRefundService;
import cn.com.leyizhuang.app.core.utils.DateUtil;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dao.ReturnOrderDAO;
import cn.com.leyizhuang.app.foundation.dao.WmsToAppOrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.CancelOrderParametersDO;
import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.WareHouseDO;
import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderLifecycle;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.ReturnOrderBaseInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.*;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.remote.queue.SellDetailsSender;
import cn.com.leyizhuang.app.remote.queue.SinkSender;
import cn.com.leyizhuang.app.remote.webservice.TestUser;
import cn.com.leyizhuang.app.remote.webservice.service.ReleaseWMSService;
import cn.com.leyizhuang.app.remote.webservice.utils.AppXmlUtil;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.util.AssertUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Created on 2017-12-19 11:24
 **/
@Slf4j
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
    @Resource
    private AppEmployeeService appEmployeeService;

    @Resource
    private ReturnOrderDAO returnOrderDAO;
    @Resource
    private WmsToAppOrderDAO wmsToAppOrderDAO;

    @Resource
    private RedisLock redisLock;

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
                    if (redisLock.lock(AppLock.ORDER_SHIPPING, header.getTaskNo(), 30)) {
                        Boolean isAppOrder = appOrderService.existOrder(header.getOrderNo());
                        if (null != isAppOrder && isAppOrder) {
                            if (StringUtils.isBlank(header.getDriver())) {
                                logger.info("GetWMSInfo OUT,获取wms信息失败,配送员不能为空,任务编号 出参 c_task_no:{}", header.getTaskNo());
                                return AppXmlUtil.resultStrXml(1, "配送员编号不能为空,任务编号" + header.getTaskNo() + "");
                            }
                        }
                        header.setCreateTime(Calendar.getInstance().getTime());
                        header.setSendFlag("0");
                        int result = wmsToAppOrderDAO.saveWtaShippingOrderHeader(header);
                        if (result == 0) {
                            logger.info("GetWMSInfo OUT,获取wms信息失败,该单已存在 出参 order_no:{}", header.getOrderNo());
                            return AppXmlUtil.resultStrXml(1, "重复传输,该单已存在!");
                        }
                        WtaShippingOrderHeader finalHeader = header;
                        new Thread(() -> handleWtaShippingOrderAsync(finalHeader.getOrderNo(), finalHeader.getTaskNo())).start();
                    } else {
                        logger.warn("tbw_send_task_m OUT,出货单接口重复传输，task_no:{}", header.getTaskNo());
                        return AppXmlUtil.resultStrXml(1, "正在处理该" + header.getTaskNo() + "出货单，请勿重复传输!");
                    }
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

                    int result = wmsToAppOrderService.saveWtaShippingOrderGoods(goods);

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
                    if (redisLock.lock(AppLock.BACK_ORDER, header.getPoNo(), 30)) {
                        header.setHandleFlag("0");
                        header.setCreateTime(Calendar.getInstance().getTime());
                        wmsToAppOrderDAO.saveWtaReturningOrderHeader(header);
                        WtaReturningOrderHeader finalHeader = header;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                handleReturningOrderHeaderAsync(finalHeader.getPoNo(), finalHeader.getRecNo());
                            }
                        }).start();
                    } else {
                        logger.warn("cancelOrder OUT,返配订单重复提交，出参 returnNo:{}", header.getPoNo());
                        return AppXmlUtil.resultStrXml(1, "正在处理该" + header.getPoNo() + "订单!");
                    }
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
                    goods.setReceiveTime(new Date());
                    int result = wmsToAppOrderService.saveWtaReturningOrderGoods(goods);
                    if (result == 0) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,商品已存在 出参 c_gcode:{}", goods.getGcode());
                        return AppXmlUtil.resultStrXml(1, "重复传输,编码为" + goods.getGcode() + "的商品已存在");
                    }
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
                    AppEmployee clerk = appEmployeeService.findDeliveryByClerkNo(deliveryClerk.getDriver());
                    if (null == clerk) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,未查询该配送员,配送员编号 出参 c_driver:{}", deliveryClerk.getDriver());
                        return AppXmlUtil.resultStrXml(1, "未查询该配送员,配送员编号" + deliveryClerk.getDriver());
                    }
                    if (AssertUtil.isEmpty(deliveryClerk.getWarehouseNo())) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,仓库编号(c_whNo)不可为空,退单号 出参 return_no{}", deliveryClerk.getReturnNo());
                        return AppXmlUtil.resultStrXml(1, "仓库编号(c_whNo)不可为空,退单号： " + deliveryClerk.getReturnNo() + "");
                    }
                    deliveryClerk.setCreateTime(Calendar.getInstance().getTime());
                    int result = wmsToAppOrderService.saveWtaReturnOrderDeliveryClerk(deliveryClerk);
                    if (result == 0) {
                        wmsToAppOrderService.updateWtaReturnOrderDeliveryClerk(deliveryClerk);
                    }
                    ReturnOrderDeliveryDetail returnOrderDeliveryDetail;
                    returnOrderDeliveryDetail = returnOrderDeliveryDetailsService.getReturnOrderDeliveryDetailByReturnNoAndStatus(deliveryClerk.getReturnNo(), ReturnLogisticStatus.PICKING_GOODS);
                    if (AssertUtil.isNotEmpty(returnOrderDeliveryDetail)) {
                        returnOrderDeliveryDetail.setPickersNumber(deliveryClerk.getDriver());
                        returnOrderDeliveryDetail.setWarehouseNo(deliveryClerk.getWarehouseNo());
                    } else {
                        returnOrderDeliveryDetail = new ReturnOrderDeliveryDetail();
                        returnOrderDeliveryDetail.setReturnNo(deliveryClerk.getReturnNo());
                        returnOrderDeliveryDetail.setReturnLogisticStatus(ReturnLogisticStatus.PICKING_GOODS);
                        returnOrderDeliveryDetail.setDescription("配送员正在取货途中");
                        returnOrderDeliveryDetail.setCreateTime(Calendar.getInstance().getTime());
                        returnOrderDeliveryDetail.setPickersNumber(deliveryClerk.getDriver());
                        returnOrderDeliveryDetail.setWarehouseNo(deliveryClerk.getWarehouseNo());
                    }
                    //已改变下面插入语句的SQL,如果存在则更新,不存在就新增
                    returnOrderDeliveryDetailsService.addReturnOrderDeliveryInfoDetails(returnOrderDeliveryDetail);
                    //修改配送物流信息的配送员信息
                    returnOrderService.updateReturnLogisticInfo(clerk, deliveryClerk.getReturnNo());
                    //修改退单头信息
                    returnOrderService.updateReturnOrderStatus(deliveryClerk.getReturnNo(), AppReturnOrderStatus.RETURNING);
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
                    String orderNo = orderResultEnter.getOrderNo();
                    if (redisLock.lock(AppLock.CANCEL_ORDER, orderNo, 30)) {
                        if (null != orderResultEnter.getIsCancel() && orderResultEnter.getIsCancel()) {
                            orderResultEnter.setHandleFlag("0");
                        } else {
                            orderResultEnter.setHandleFlag("1");
                        }
                        int result = wmsToAppOrderService.saveWtaCancelOrderResultEnter(orderResultEnter);
                        if (result == 0) {
                            logger.info("GetWMSInfo OUT,获取wms信息失败,该单已存在 出参 order_no:{}", orderResultEnter.getOrderNo());
                            return AppXmlUtil.resultStrXml(1, "重复传输,该单" + orderResultEnter.getOrderNo() + "已存在!");
                        }
                        OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(orderResultEnter.getOrderNo());
                        if (AppOrderStatus.CANCELED.equals(orderBaseInfo.getStatus())) {
                            logger.warn("cancelOrder OUT,该订单已取消不能重复取消，出参 orderNo:{}", orderNo);
                            return AppXmlUtil.resultStrXml(1, "该订单" + orderNo + "已取消不能重复取消");
                        }
                        this.handlingWtaCancelOrderResultEnterAsync(orderResultEnter);
                    } else {
                        logger.warn("cancelOrder OUT,取消订单重复提交，出参 orderNo:{}", orderNo);
                        return AppXmlUtil.resultStrXml(1, "正在处理该" + orderNo + "订单!");
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
                    String returnNo = returnOrderResultEnter.getReturnNumber();
                    if (AssertUtil.isEmpty(returnNo)) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,退单号不可为空,退单号 出参 return_no{}");
                        return AppXmlUtil.resultStrXml(1, "退单号(return_no)不可为空");
                    }
                    returnOrderResultEnter.setReceiveTime(new Date());
                    returnOrderResultEnter.setHandleFlag("0");
                    int result = wmsToAppOrderService.saveWtaCancelReturnOrderResultEnter(returnOrderResultEnter);
                    if (result == 0) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,该单已存在 出参 order_no:{}", returnNo);
                        return AppXmlUtil.resultStrXml(1, "重复传输,该单" + returnNo + "已存在!");
                    }
                    this.handleWtaCancelReturnOrderResultEnter(returnOrderResultEnter, returnNo);
                }
                logger.info("GetWMSInfo OUT,获取取消退单结果确认wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "NORMAL");
            }

            //配送单物流详情
            else if ("tbw_out_m".equalsIgnoreCase(strTable)) {
                String orderNumber = null;

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    NodeList childNodeList = node.getChildNodes();

                    WtaOrderLogistics orderDeliveryInfoDetails = new WtaOrderLogistics();
                    for (int idx = 0; idx < childNodeList.getLength(); idx++) {
                        Node childNode = childNodeList.item(idx);
                        orderDeliveryInfoDetails = mapping(orderDeliveryInfoDetails, childNode);
                    }
                    orderDeliveryInfoDetails.setReceiveTime(new Date());
                    orderDeliveryInfoDetails.setHandleFlag("0");
                    int result = wmsToAppOrderService.saveWtaOrderLogistics(orderDeliveryInfoDetails);
                    orderNumber = orderDeliveryInfoDetails.getOrderNo();
                }
                this.handleWtaOrderLogisticsAsync(orderNumber);
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

                    wholeSplitToUnit.setHandleFlag("0");
                    wholeSplitToUnit.setReceiveTime(new Date());
                    int result = wmsToAppOrderService.saveWtaWarehouseWholeSplitToUnit(wholeSplitToUnit);
                    this.handWtaWarehouseWholeSplitToUnitAsync(wholeSplitToUnit.getDirectNo(), wholeSplitToUnit.getSku(), wholeSplitToUnit.getDSku());
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
                    allocation.setReceiveTime(new Date());
                    allocation.setHandleFlag("0");
                    wmsToAppOrderDAO.saveWtaWarehouseAllocationHeader(allocation);
                    this.handleWtaWarehouseAllocationAsync(allocation.getAllocationNo());
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
                    allocationGoods.setReceiveTime(new Date());
                    int result = wmsToAppOrderService.saveWtaWarehouseAllocationGoods(allocationGoods);
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
                    purchaseHeader.setReceiveTime(Calendar.getInstance().getTime());
                    purchaseHeader.setHandleFlag("0");
                    wmsToAppOrderDAO.saveWtaWarehousePurchaseHeader(purchaseHeader);
                    this.handleWtaWarehousePurchaseAsync(purchaseHeader.getRecNo());
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
                    purchaseGoods.setReceiveTime(new Date());
                    wmsToAppOrderDAO.saveWtaWarehousePurchaseGoods(purchaseGoods);
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
                    damageAndOverflow.setCreateTime(Calendar.getInstance().getTime());
                    damageAndOverflow.setHandleFlag("0");
                    damageAndOverflow.setReceiveTime(new Date());
                    wmsToAppOrderDAO.saveWtaWarehouseReportDamageAndOverflow(damageAndOverflow);
                    try {
                        this.handleWtaWarehouseReportDamageAndOverflowAsync(damageAndOverflow.getWasteNo(), damageAndOverflow.getWasteId());
                    } catch (Exception e) {
                        return AppXmlUtil.resultStrXml(1, e.getMessage());
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
            // 比较字段名
            if ("c_task_no".equalsIgnoreCase(childNode.getNodeName())) {
                // 有值
                if (null != childNode.getChildNodes().item(0)) {
                    wtaShippingOrderHeader.setTaskNo(childNode.getChildNodes().item(0).getNodeValue());
                }
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
            } else if ("c_reserved4".equalsIgnoreCase(childNode.getNodeName())) {
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
            } else if ("c_end_dt".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    returningOrderHeader.setCEndDt(DateUtil.dateFromString(childNode.getChildNodes().item(0).getNodeValue()));
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

    private WtaOrderLogistics mapping(WtaOrderLogistics orderDeliveryInfoDetails, Node childNode) {
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
                    orderDeliveryInfoDetails.setLogisticStatus(childNode.getChildNodes().item(0).getNodeValue());
//                    String cValue3 = childNode.getChildNodes().item(0).getNodeValue();
//                    if (LogisticStatus.RECEIVED.getDescription().equals(cValue3)) {
//                        orderDeliveryInfoDetails.setLogisticStatus(LogisticStatus.RECEIVED);
//                    } else if (LogisticStatus.ALREADY_POSITIONED.getDescription().equals(cValue3)) {
//                        orderDeliveryInfoDetails.setLogisticStatus(LogisticStatus.ALREADY_POSITIONED);
//                    } else if (LogisticStatus.PICKING_GOODS.getDescription().equals(cValue3)) {
//                        orderDeliveryInfoDetails.setLogisticStatus(LogisticStatus.PICKING_GOODS);
//                    } else if (LogisticStatus.LOADING.getDescription().equals(cValue3)) {
//                        orderDeliveryInfoDetails.setLogisticStatus(LogisticStatus.LOADING);
//                    }
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

    /**
     * 异步处理出货后逻辑
     *
     * @param header 出货单头
     * @param clerk  配送员
     */
    @Async
    @SuppressWarnings("WeakerAccess")
    @Transactional(rollbackFor = Exception.class)
    protected void handlingWtaShippingOrderHeaderAsync(WtaShippingOrderHeader header, AppEmployee clerk) {
        //查询是否存在
        List<OrderDeliveryInfoDetails> deliveryInfoDetailsList = orderDeliveryInfoDetailsService.queryListByOrderNumber(header.getOrderNo());
        if (AssertUtil.isEmpty(deliveryInfoDetailsList)) {
            logger.info("GetWMSInfo OUT,获取wms信息失败,该订单不存在 出参 order_no:{}", header.getOrderNo());
            smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "获取wms出货信息失败!App没有找到该订单" + header.getOrderNo());
            return;
        }
        WareHouseDO wareHouse = wareHouseService.findByWareHouseNo(header.getWhNo());
        try {
            //保存物流信息
            OrderDeliveryInfoDetails deliveryInfoDetails = OrderDeliveryInfoDetails.transform(header,
                    null != wareHouse ? wareHouse.getWareHouseName() : header.getWhNo());
            orderDeliveryInfoDetailsService.addOrderDeliveryInfoDetails(deliveryInfoDetails);
            //修改订单配送信息加入配送员
            appOrderService.updateOrderLogisticInfoByDeliveryClerkNo(clerk, header.getWhNo(), header.getOrderNo());
            //修改订单头状态
            OrderBaseInfo orderBaseInfo = new OrderBaseInfo();
            orderBaseInfo.setDeliveryStatus(LogisticStatus.SEALED_CAR);
            orderBaseInfo.setStatus(AppOrderStatus.PENDING_RECEIVE);
            orderBaseInfo.setOrderNumber(header.getOrderNo());
            appOrderService.updateOrderBaseInfoStatus(orderBaseInfo);
//            appOrderService.updateOrderStatusAndDeliveryStatusByOrderNo(AppOrderStatus.PENDING_RECEIVE, LogisticStatus.SEALED_CAR, header.getOrderNo());
            //推送物流信息
            NoticePushUtils.pushOrderLogisticInfo(header.getOrderNo());
            // rabbit 记录下单销量
            sellDetailsSender.sendOrderSellDetailsTOManagement(header.getOrderNo());
            // 处理完这里逻辑需要修改出货头表的处理状态(暂时使用sendFlag字段代替)
            header.setSendFlag("1");
            header.setSendTime(Calendar.getInstance().getTime());
            wmsToAppOrderService.updateWtaShippingOrderHeader(header);
        } catch (Exception e) {
            logger.info("GetWMSInfo OUT,获取出货单头档wms信息失败,发生未知异常 出参 e:{}", e);
            smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "获取出货单头档wms信息失败!处理出货单头档事务失败!order:" + header.getOrderNo());
        }
    }

    /**
     * 异步处理返配上架逻辑
     *
     * @param header 返配上架头
     */
    @Async
    @SuppressWarnings("WeakerAccess")
    @Transactional(rollbackFor = Exception.class)
    protected void handlingWtaReturningOrderHeaderAsync(WtaReturningOrderHeader header) {
        HashedMap maps = new HashedMap();
        ReturnOrderBaseInfo returnOrder = returnOrderService.queryByReturnNo(header.getPoNo());
        if (returnOrder.getReturnType().equals(ReturnOrderType.REFUSED_RETURN)) {
            OrderBaseInfo orderBaseInfo = appOrderService.getOrderDetail(returnOrder.getOrderNo());
            OrderBillingDetails orderBillingDetails = appOrderService.getOrderBillingDetail(orderBaseInfo.getOrderNumber());
            ReturnOrderBaseInfo returnOrderBaseInfo = returnOrderService.queryByReturnNo(returnOrder.getReturnNo());
            List<ReturnOrderGoodsInfo> returnOrderGoodsInfoList = returnOrderService.findReturnOrderGoodsInfoByOrderNumber(returnOrder.getReturnNo());
            maps = returnOrderService.refusedOrder(orderBaseInfo.getOrderNumber(), orderBaseInfo, orderBillingDetails, returnOrderBaseInfo, returnOrderGoodsInfoList);
        } else if (ReturnOrderType.NORMAL_RETURN.equals(returnOrder.getReturnType())) {
            maps = returnOrderService.normalReturnOrderProcessing(header.getPoNo(), header.getCompanyId());
        }

        ReturnOrderBilling returnOrderBilling = (ReturnOrderBilling) maps.get("returnOrderBilling");

        ReturnOrderBaseInfo returnOrderBaseInfo = (ReturnOrderBaseInfo) maps.get("returnOrderBaseInfo");

        OrderBaseInfo orderBaseInfo = (OrderBaseInfo) maps.get("orderBaseInfo");

        Boolean a = true;
        if ("SUCCESS".equals(maps.get("code"))) {
            if ((Boolean) maps.get("hasReturnOnlinePay")) {
                //返回第三方支付金额
                if (null != returnOrderBilling.getOnlinePay() && returnOrderBilling.getOnlinePay() > AppConstant.PAY_UP_LIMIT) {
                    if (OnlinePayType.ALIPAY.equals(returnOrderBilling.getOnlinePayType())) {
                        //支付宝退款
                        Map<String, String> map = onlinePayRefundService.alipayRefundRequest(
                                returnOrderBaseInfo.getCreatorId(), returnOrderBaseInfo.getCreatorIdentityType().getValue(), returnOrderBaseInfo.getOrderNo(), returnOrderBaseInfo.getReturnNo(), returnOrderBilling.getOnlinePay(), returnOrderBaseInfo.getRoid());
                        if ("FAILURE".equals(map.get("code"))) {
                            a = false;
                        }
                    } else if (OnlinePayType.WE_CHAT.equals(returnOrderBilling.getOnlinePayType())) {
                        //微信退款方法类
                        Map<String, String> map = onlinePayRefundService.wechatReturnMoney(
                                returnOrderBaseInfo.getCreatorId(), returnOrderBaseInfo.getCreatorIdentityType().getValue(), returnOrderBilling.getOnlinePay(), returnOrderBaseInfo.getOrderNo(), returnOrderBaseInfo.getReturnNo(), returnOrderBaseInfo.getRoid());
                        if ("FAILURE".equals(map.get("code"))) {
                            a = false;
                        }
                    } else if (OnlinePayType.UNION_PAY.equals(returnOrderBilling.getOnlinePayType())) {
                        //创建退单退款详情实体
                        ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                        returnOrderBillingDetail.setCreateTime(Calendar.getInstance().getTime());
                        returnOrderBillingDetail.setRoid(returnOrderBaseInfo.getRoid());
                        returnOrderBillingDetail.setReturnNo(returnOrderBaseInfo.getReturnNo());
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
            try {
                if (a) {
                    //修改订单处理状态
                    returnOrderService.updateReturnOrderStatus(returnOrderBaseInfo.getReturnNo(), AppReturnOrderStatus.FINISHED);
                    //*****************************保存订单生命周期信息***************************
                    if (returnOrderBaseInfo.getReturnType().equals(ReturnOrderType.REFUSED_RETURN)) {
                        OrderLifecycle orderLifecycle = new OrderLifecycle();
                        orderLifecycle.setOid(orderBaseInfo.getId());
                        orderLifecycle.setOrderNumber(orderBaseInfo.getOrderNumber());
                        orderLifecycle.setOperation(OrderLifecycleType.REJECTED);
                        orderLifecycle.setPostStatus(AppOrderStatus.REJECTED);
                        orderLifecycle.setOperationTime(new Date());
                        returnOrderDAO.saveOrderLifecycle(orderLifecycle);
                        //********************************保存退单生命周期信息***********************
                        ReturnOrderLifecycle returnOrderLifecycle = new ReturnOrderLifecycle();
                        returnOrderLifecycle.setRoid(returnOrderBaseInfo.getRoid());
                        returnOrderLifecycle.setReturnNo(returnOrderBaseInfo.getReturnNo());
                        returnOrderLifecycle.setOperation(OrderLifecycleType.REJECTED);
                        returnOrderLifecycle.setPostStatus(AppReturnOrderStatus.FINISHED);
                        returnOrderLifecycle.setOperationTime(new Date());
                        returnOrderDAO.saveReturnOrderLifecycle(returnOrderLifecycle);
                    } else if (returnOrderBaseInfo.getReturnType().equals(ReturnOrderType.NORMAL_RETURN)) {
                        //********************************保存退单生命周期信息***********************
                        ReturnOrderLifecycle returnOrderLifecycle = new ReturnOrderLifecycle();
                        returnOrderLifecycle.setRoid(returnOrderBaseInfo.getRoid());
                        returnOrderLifecycle.setReturnNo(returnOrderBaseInfo.getReturnNo());
                        returnOrderLifecycle.setOperation(OrderLifecycleType.NORMAL_RETURN);
                        returnOrderLifecycle.setPostStatus(AppReturnOrderStatus.FINISHED);
                        returnOrderLifecycle.setOperationTime(new Date());
                        returnOrderDAO.saveReturnOrderLifecycle(returnOrderLifecycle);
                    }
                    //****************************更新物流表的返配上架时间*******************************
                    returnOrderService.updateReturnLogisticInfoOfBackTime(returnOrderBaseInfo.getReturnNo());
                } else {
                    //修改订单处理状态
                    returnOrderService.updateReturnOrderStatus(returnOrderBaseInfo.getReturnNo(), AppReturnOrderStatus.PENDING_REFUND);
                }

                //保存物流信息
                ReturnOrderDeliveryDetail returnOrderDeliveryDetail = ReturnOrderDeliveryDetail.transform(header);
                returnOrderDeliveryDetailsService.addReturnOrderDeliveryInfoDetails(returnOrderDeliveryDetail);

                // rabbitMq 记录退单销量
                sellDetailsSender.sendReturnOrderSellDetailsTOManagement(header.getPoNo());
                //发送退单拆单消息到拆单消息队列
                sinkSender.sendReturnOrder(returnOrderBaseInfo.getReturnNo());
                logger.info("cancelOrderToWms OUT,正常退货成功");
            } catch (Exception e) {
                logger.info("GetWMSInfo OUT,获取返配单头档wms信息失败,发生未知异常 出参 e:{}", e);
                smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "处理退货业务逻辑事务失败!order:" + returnOrderBaseInfo.getReturnNo());
            }
        } else {
            logger.info("getReturnOrderList OUT,正常退货失败,业务处理出现异常!");
            smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "正常退货业务逻辑处理失败!order:" + returnOrderBaseInfo.getReturnNo());
        }
    }

    /**
     * 异步处理取消订单逻辑
     *
     * @param orderResultEnter 取消订单确认
     */
    @Async
    @SuppressWarnings("WeakerAccess")
    @Transactional(rollbackFor = Exception.class)
    public void handlingWtaCancelOrderResultEnterAsync(WtaCancelOrderResultEnter orderResultEnter) {

        //获取订单头信息
        OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(orderResultEnter.getOrderNo());
        if (null != orderResultEnter.getIsCancel() && orderResultEnter.getIsCancel()) {
            try {

                //获取订单账目明细
                OrderBillingDetails orderBillingDetails = appOrderService.getOrderBillingDetail(orderResultEnter.getOrderNo());
                //获取取消订单相关参数
                CancelOrderParametersDO cancelOrderParametersDO = cancelOrderParametersService.findCancelOrderParametersByOrderNumber(orderResultEnter.getOrderNo());
                //调用取消订单通用方法
                Map<Object, Object> maps = returnOrderService.cancelOrderUniversal(cancelOrderParametersDO.getUserId(), cancelOrderParametersDO.getIdentityType(), cancelOrderParametersDO.getOrderNumber(), cancelOrderParametersDO.getReasonInfo(), cancelOrderParametersDO.getRemarksInfo(), orderBaseInfo, orderBillingDetails);
                ReturnOrderBaseInfo returnOrderBaseInfo = (ReturnOrderBaseInfo) maps.get("returnOrderBaseInfo");
                //如果是待收货、门店自提单则需要返回第三方支付金额
                if (null != orderBillingDetails.getOnlinePayType()) {
                    if (OnlinePayType.ALIPAY.equals(orderBillingDetails.getOnlinePayType())) {
                        //支付宝退款
                        onlinePayRefundService.alipayRefundRequest(cancelOrderParametersDO.getUserId(), cancelOrderParametersDO.getIdentityType(), cancelOrderParametersDO.getOrderNumber(), returnOrderBaseInfo.getReturnNo(), orderBillingDetails.getOnlinePayAmount(), returnOrderBaseInfo.getRoid());
                    } else if (OnlinePayType.WE_CHAT.equals(orderBillingDetails.getOnlinePayType())) {
                        //微信退款方法类
                        onlinePayRefundService.wechatReturnMoney(cancelOrderParametersDO.getUserId(), cancelOrderParametersDO.getIdentityType(), orderBillingDetails.getOnlinePayAmount(), cancelOrderParametersDO.getOrderNumber(), returnOrderBaseInfo.getReturnNo(), returnOrderBaseInfo.getRoid());
                    } else if (OnlinePayType.UNION_PAY.equals(orderBillingDetails.getOnlinePayType())) {
                        //创建退单退款详情实体
                        ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                        returnOrderBillingDetail.setCreateTime(Calendar.getInstance().getTime());
                        returnOrderBillingDetail.setRoid(returnOrderBaseInfo.getRoid());
                        returnOrderBillingDetail.setReturnNo(returnOrderBaseInfo.getReturnNo());
                        returnOrderBillingDetail.setRefundNumber(returnOrderBaseInfo.getReturnNo());
                        //TODO 时间待定
                        returnOrderBillingDetail.setIntoAmountTime(Calendar.getInstance().getTime());
                        //TODO 第三方回复码
                        returnOrderBillingDetail.setReplyCode("");
                        returnOrderBillingDetail.setReturnMoney(orderBillingDetails.getOnlinePayAmount());
                        returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.UNION_PAY);
                    }
                }
                //修改取消订单处理状态
                cancelOrderParametersService.updateCancelStatusByOrderNumber(orderResultEnter.getOrderNo());

                orderResultEnter.setHandleFlag("1");
                orderResultEnter.setHandleTime(new Date());
                this.wmsToAppOrderService.updateWtaCancelOrderResult(orderResultEnter);

                //发送退单拆单消息到拆单消息队列
                sinkSender.sendReturnOrder(returnOrderBaseInfo.getReturnNo());
                logger.info("cancelOrderToWms OUT,取消订单成功");
            } catch (Exception e) {
                orderResultEnter.setHandleFlag("0");
                orderResultEnter.setHandleTime(new Date());
                orderResultEnter.setErrorMessage(e.getMessage());
                this.wmsToAppOrderService.updateWtaCancelOrderResult(orderResultEnter);
            }
        } else {
            String info = "您取消的订单" + orderResultEnter.getOrderNo() + "，取消失败，请联系管理人员！";
            logger.info("取消失败订单号:{}", orderResultEnter.getOrderNo());
            smsAccountService.commonSendSms(orderBaseInfo.getCreatorPhone(), info);
        }
    }

    /**
     * 异步处理整转零逻辑
     *
     * @param wholeSplitToUnit 整转零
     * @param goodsDO          整商品
     * @param dGoodsDO         零商品
     * @param city             城市
     */
    @Async
    @SuppressWarnings("WeakerAccess")
    @Transactional(rollbackFor = Exception.class)
    protected void handlingWtaWarehouseWholeSplitToUnitAsync(WtaWarehouseWholeSplitToUnit wholeSplitToUnit, GoodsDO goodsDO, GoodsDO dGoodsDO, City city) {
        //扣整商品仓库数量
        for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
            CityInventory cityInventory = cityService.findCityInventoryByCityCodeAndSku(wholeSplitToUnit.getCompanyId(), wholeSplitToUnit.getSku());
            if (null == cityInventory) {
                cityInventory = CityInventory.transform(goodsDO, city);
                cityService.saveCityInventory(cityInventory);
            }
            if (cityInventory.getAvailableIty() < wholeSplitToUnit.getQty()) {
                logger.warn("GetWMSInfo OUT,获取wms信息失败,获取整转零失败,任务编号 出参 DirectNo:{}", wholeSplitToUnit.getDirectNo());
                smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "获取wms信息整转零失败!" + wholeSplitToUnit.getDirectNo() +
                        "该城市下sku为" + wholeSplitToUnit.getSku() + "的商品库存不足!");
                return;
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
                    logger.warn("GetWMSInfo OUT,获取wms整转零信息失败,扣整商品仓库数量失败,任务编号 出参 DirectNo:{}", wholeSplitToUnit.getDirectNo());
                    smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "获取wms整转零信息失败,扣整商品仓库数量失败!任务编号" + wholeSplitToUnit.getDirectNo());
                    return;
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
                if (j == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                    logger.warn("GetWMSInfo OUT,获取wms整转零信息失败,增加零商品仓库数量失败,任务编号 出参 DirectNo:{}", wholeSplitToUnit.getDirectNo());
                    smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "获取wms整转零信息失败,增加零商品仓库数量失败!任务编号" + wholeSplitToUnit.getDirectNo());
                    return;
                }
            }
        }
    }

    /**
     * 异步处理城市采购逻辑
     */
    @Async
    @SuppressWarnings("WeakerAccess")
    @Transactional(rollbackFor = Exception.class)
    protected void handlingWtaWarehousePurchaseHeaderAsync(WtaWarehousePurchaseHeader purchaseHeader, City city) {
        List<WtaWarehousePurchaseGoods> purchaseGoodsList = wmsToAppOrderService.findWtaWarehousePurchaseGoodsListByPurchaseNo(purchaseHeader.getRecNo());
        for (WtaWarehousePurchaseGoods purchaseGoods : purchaseGoodsList) {
            GoodsDO goodsDO = goodsService.queryBySku(purchaseGoods.getSku());
            if (null == goodsDO) {
                logger.info("GetWMSInfo OUT,获取wms信息失败,获取仓库采购入库失败,任务编号 商品资料中没有查询到sku为" + purchaseGoods.getSku() + "的商品信息!");
                smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "获取wms信息失败,获取仓库采购入库失败,商品资料中没有查询到sku为" + purchaseGoods.getSku() + "的商品信息!");
                return;
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
                    if (j == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,获取仓库采购失败,任务编号 出参 PurchaseNo:{}", purchaseHeader.getPurchaseNo());
                        smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "获取wms仓库采购失败!任务编号" + purchaseHeader.getPurchaseNo());
                        return;
                    }
                }
            }
        }
    }

    /**
     * 异步处理仓库调拨逻辑
     */
    @Async
    @SuppressWarnings("WeakerAccess")
    @Transactional(rollbackFor = Exception.class)
    protected void handlingWtaWarehouseAllocationHeaderAsync(WtaWarehouseAllocationHeader allocation, City city) {
        List<WtaWarehouseAllocationGoods> allocationGoodsList = wmsToAppOrderService.findWtaWarehouseAllocationGoodsListByAllocationNo(allocation.getAllocationNo());
        for (WtaWarehouseAllocationGoods allocationGoods : allocationGoodsList) {
            GoodsDO goodsDO = goodsService.queryBySku(allocationGoods.getSku());
            if (null == goodsDO) {
                logger.info("GetWMSInfo OUT,获取wms信息失败,获取仓库调拨失败,任务编号 商品资料中没有查询到sku为" + allocationGoods.getSku() + "的商品信息!");
                smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "获取wms信息失败,获取仓库调拨失败,商品资料中没有查询到sku为" + allocationGoods.getSku() + "的商品信息!");
                return;
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
                    smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "获取wms信息失败,获取仓库调拨失败,该城市下sku为" + allocationGoods.getSku() + "的商品库存不足!");
                    return;
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
                    log.setChangeTypeDesc(changeType != null ? changeType.getDescription() : null);
                    log.setReferenceNumber(allocation.getAllocationNo());
                    cityService.addCityInventoryAvailableQtyChangeLog(log);
                    break;
                } else {
                    if (j == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,获取仓库调拨失败,任务编号 出参 AllocationNo:{}", allocation.getAllocationNo());
                        smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "获取wms仓库调拨失败!任务编号" + allocation.getAllocationNo());
                        return;
                    }
                }
            }
        }
    }

    /**
     * 异步处理仓库报损报溢逻辑
     */
    @Async
    @SuppressWarnings("WeakerAccess")
    @Transactional(rollbackFor = Exception.class)
    protected void handlingWtaWarehouseReportDamageAndOverflowAsync(WtaWarehouseReportDamageAndOverflow damageAndOverflow, City city, GoodsDO goodsDO) {
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
                smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "获取wms信息失败,获取报损报溢失败,该城市下sku为" + damageAndOverflow.getSku() + "的商品库存不足!");
                return;
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
                if (j == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                    logger.info("GetWMSInfo OUT,获取wms信息失败,获取报损报溢失败,任务编号 出参 WasteNo:{}", damageAndOverflow.getWasteNo());
                    smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "获取wms报损报溢失败!任务编号" + damageAndOverflow.getWasteNo());
                    return;
                }
            }
        }
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

    @Async
    @SuppressWarnings("WeakerAccess")
    public void handleWtaOrderLogisticsAsync(String orderNo) {
        this.wmsToAppOrderService.handleWtaOrderLogistics(orderNo);
    }

    @Async
    @SuppressWarnings("WeakerAccess")
    public void handleWtaShippingOrderAsync(String orderNo, String taskNo) {
        Boolean flag = this.wmsToAppOrderService.handleWtaShippingOrder(orderNo, taskNo);
        //flag 标识 是否为app内部订单
        if (flag) {
            //推送物流信息
            NoticePushUtils.pushOrderLogisticInfo(orderNo);
            // rabbit 记录下单销量
            sellDetailsSender.sendOrderSellDetailsTOManagement(orderNo);
            //发送金蝶销退明细表到EBS
            sinkSender.sendKdSell(orderNo);
        }
    }


    @Async
    @SuppressWarnings("WeakerAccess")
    protected void handleWtaWarehouseReportDamageAndOverflowAsync(String wasteNo, Long wasteId) {
        this.wmsToAppOrderService.handlingWtaWarehouseReportDamageAndOverflowAsync(wasteNo, wasteId);
    }


    @Async
    @SuppressWarnings("WeakerAccess")
    public void handWtaWarehouseWholeSplitToUnitAsync(String directNo, String sku, String dsku) {
        this.wmsToAppOrderService.handlingWtaWarehouseWholeSplitToUnitAsync(directNo, sku, dsku);
    }


    @Async
    @SuppressWarnings("WeakerAccess")
    public void handleWtaWarehouseAllocationAsync(String allocationNo) {
        this.wmsToAppOrderService.handleWtaWarehouseAllocation(allocationNo);
    }

    @Async
    @SuppressWarnings("WeakerAccess")
    public void handleWtaWarehousePurchaseAsync(String recNo) {
        this.wmsToAppOrderService.handleWtaWarehousePurchase(recNo);
    }

    @Async
    @Transactional
    public void handleReturningOrderHeaderAsync(String returnNo, String recNo) {
        try {
            log.info("\n//***************** 异步处理返配上架相关业务********************");
            //线程睡5s，等待返配明细传输完成
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WtaReturningOrderHeader returningOrderHeader = this.wmsToAppOrderService.getReturningOrderHeaderByReturnNo(returnNo, recNo);
        try {
            if (null != returningOrderHeader) {
                ReturnOrderBaseInfo returnOrderBaseInfoTemp = returnOrderService.queryByReturnNo(returningOrderHeader.getPoNo());
                //*************** 如果退单存在，则按App正常退单返配上架逻辑处理 ********************
                if (null != returnOrderBaseInfoTemp) {
                    HashedMap maps = new HashedMap();
                    maps = this.wmsToAppOrderService.handleReturningOrderHeader(returnNo, recNo);
                    if (null != maps && maps.size() > 0) {

                        ReturnOrderBilling returnOrderBilling = (ReturnOrderBilling) maps.get("returnOrderBilling");

                        ReturnOrderBaseInfo returnOrderBaseInfo = (ReturnOrderBaseInfo) maps.get("returnOrderBaseInfo");

                        OrderBaseInfo orderBaseInfo = (OrderBaseInfo) maps.get("orderBaseInfo");

                        if ("SUCCESS".equals(maps.get("code"))) {
                            if ((Boolean) maps.get("hasReturnOnlinePay")) {
                                //返回第三方支付金额
                                if (null != returnOrderBilling.getOnlinePay() && returnOrderBilling.getOnlinePay() > AppConstant.PAY_UP_LIMIT) {
                                    if (OnlinePayType.ALIPAY.equals(returnOrderBilling.getOnlinePayType())) {
                                        //支付宝退款
                                        Map<String, String> map = onlinePayRefundService.alipayRefundRequest(
                                                returnOrderBaseInfo.getCreatorId(), returnOrderBaseInfo.getCreatorIdentityType().getValue(), returnOrderBaseInfo.getOrderNo(), returnOrderBaseInfo.getReturnNo(), returnOrderBilling.getOnlinePay(), returnOrderBaseInfo.getRoid());
                                    } else if (OnlinePayType.WE_CHAT.equals(returnOrderBilling.getOnlinePayType())) {
                                        //微信退款方法类
                                        Map<String, String> map = onlinePayRefundService.wechatReturnMoney(
                                                returnOrderBaseInfo.getCreatorId(), returnOrderBaseInfo.getCreatorIdentityType().getValue(), returnOrderBilling.getOnlinePay(), returnOrderBaseInfo.getOrderNo(), returnOrderBaseInfo.getReturnNo(), returnOrderBaseInfo.getRoid());
                                    } else if (OnlinePayType.UNION_PAY.equals(returnOrderBilling.getOnlinePayType())) {
                                        //银联支付退款
                                        Map<String, String> map = onlinePayRefundService.unionPayReturnMoney(returnOrderBaseInfo.getCreatorId(), returnOrderBaseInfo.getCreatorIdentityType().getValue(),
                                                returnOrderBilling.getOnlinePay(), returnOrderBaseInfo.getOrderNo(), returnOrderBaseInfo.getReturnNo(),
                                                returnOrderBaseInfo.getRoid());
                                        if ("FAILURE".equals(map.get("code"))) {
                                            returnOrderService.updateReturnOrderBaseInfoByReturnNo(returnOrderBaseInfo.getReturnNo(), AppReturnOrderStatus.PENDING_REFUND);
                                        }
                                    }
                                }
                            }
                            //修改订单处理状态
                            returnOrderService.updateReturnOrderStatus(returnOrderBaseInfo.getReturnNo(), AppReturnOrderStatus.FINISHED);
                            //*****************************保存订单生命周期信息***************************
                            OrderLifecycle orderLifecycle = new OrderLifecycle();
                            orderLifecycle.setOid(orderBaseInfo.getId());
                            orderLifecycle.setOrderNumber(orderBaseInfo.getOrderNumber());
                            if (returnOrderBaseInfo.getReturnType().equals(ReturnOrderType.REFUSED_RETURN)) {
                                orderLifecycle.setOperation(OrderLifecycleType.REJECTED);
                                orderLifecycle.setPostStatus(AppOrderStatus.REJECTED);
                            } else if (returnOrderBaseInfo.getReturnType().equals(ReturnOrderType.NORMAL_RETURN)) {
                                orderLifecycle.setOperation(OrderLifecycleType.NORMAL_RETURN);
                                orderLifecycle.setPostStatus(AppOrderStatus.FINISHED);
                            }
                            orderLifecycle.setOperationTime(new Date());
                            returnOrderDAO.saveOrderLifecycle(orderLifecycle);
                            //********************************保存退单生命周期信息***********************
                            ReturnOrderLifecycle returnOrderLifecycle = new ReturnOrderLifecycle();
                            returnOrderLifecycle.setRoid(returnOrderBaseInfo.getRoid());
                            returnOrderLifecycle.setReturnNo(returnOrderBaseInfo.getReturnNo());
                            if (returnOrderBaseInfo.getReturnType().equals(ReturnOrderType.REFUSED_RETURN)) {
                                returnOrderLifecycle.setOperation(OrderLifecycleType.REJECTED);
                                orderLifecycle.setPostStatus(AppOrderStatus.REJECTED);
                            } else if (returnOrderBaseInfo.getReturnType().equals(ReturnOrderType.NORMAL_RETURN)) {
                                returnOrderLifecycle.setOperation(OrderLifecycleType.NORMAL_RETURN);
                                orderLifecycle.setPostStatus(AppOrderStatus.FINISHED);
                            }
                            returnOrderLifecycle.setPostStatus(AppReturnOrderStatus.FINISHED);
                            returnOrderLifecycle.setOperationTime(new Date());
                            returnOrderDAO.saveReturnOrderLifecycle(returnOrderLifecycle);

                            //****************************更新物流表的返配上架时间*******************************
                            returnOrderService.updateReturnLogisticInfoOfBackTime(returnOrderBaseInfo.getReturnNo());

                            //保存物流信息
                            ReturnOrderDeliveryDetail returnOrderDeliveryDetail = ReturnOrderDeliveryDetail.transform(returningOrderHeader);
                            returnOrderDeliveryDetailsService.addReturnOrderDeliveryInfoDetails(returnOrderDeliveryDetail);

                            // rabbitMq 记录退单销量
                            sellDetailsSender.sendReturnOrderSellDetailsTOManagement(returningOrderHeader.getPoNo());
                            //发送退单拆单消息到拆单消息队列
                            sinkSender.sendReturnOrder(returnOrderBaseInfo.getReturnNo());
                            //等待拆单完成
                            Thread.sleep(2000);
                            //发送金蝶销退明细表到EBS
                            sinkSender.sendKdSell(returnOrderBaseInfo.getReturnNo());
                            logger.info("cancelOrderToWms OUT,正常退货成功");
                            returningOrderHeader.setHandleFlag("1");
                            returningOrderHeader.setHandleTime(new Date());
                            this.wmsToAppOrderDAO.updateReturningOrderHeaderByOrderNo(returningOrderHeader);
                        } else {
                            logger.info("getReturnOrderList OUT,正常退货失败,业务处理出现异常!");
                            smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "正常退货业务逻辑处理失败!order:" + returnNo);
                            returningOrderHeader.setHandleFlag("0");
                            returningOrderHeader.setErrMessage("正常退货失败,业务处理出现异常");
                            returningOrderHeader.setHandleTime(new Date());
                            this.wmsToAppOrderDAO.updateReturningOrderHeaderByOrderNo(returningOrderHeader);
                            throw new RuntimeException();
                        }
                    } else {
                        logger.info("getReturnOrderList OUT,正常退货失败,业务处理出现异常!");
                        smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "正常退货业务逻辑处理失败!order:" + returnNo);
                        returningOrderHeader.setHandleFlag("0");
                        returningOrderHeader.setErrMessage("正常退货失败,业务处理出现异常");
                        returningOrderHeader.setHandleTime(new Date());
                        this.wmsToAppOrderDAO.updateReturningOrderHeaderByOrderNo(returningOrderHeader);
                        throw new RuntimeException();
                    }
                }
                //*************** 如果退单不存在，则按WMS自建退单返配上架逻辑处理 ********************
                else {
                    City city = cityService.findCityByWarehouseNo(returningOrderHeader.getWhNo());
                    if (null == city) {
                        returningOrderHeader.setErrMessage("城市信息中没有查询到仓库为" + returningOrderHeader.getWhNo() + "的数据!");
                        returningOrderHeader.setHandleFlag("0");
                        returningOrderHeader.setHandleTime(new Date());
                        this.wmsToAppOrderDAO.updateReturningOrderHeaderByOrderNo(returningOrderHeader);
                        throw new RuntimeException();
                    }
                    List<WtaReturningOrderGoods> wtaReturningOrderGoods = wmsToAppOrderService.findWtaReturningOrderGoodsByReturnOrderNo(returnNo);
                    for (WtaReturningOrderGoods returningOrderGoods : wtaReturningOrderGoods) {
                        GoodsDO goodsDO = goodsService.queryBySku(returningOrderGoods.getGcode());
                        if (null == goodsDO) {
                            logger.info("GetWMSInfo OUT,获取wms信息失败,获取退货返配增加城市库存失败,任务编号 商品资料中没有查询到sku为" + returningOrderGoods.getGcode() + "的商品信息!");
                            returningOrderHeader.setErrMessage("编码为" + returningOrderGoods.getGcode() + "的商品不存在");
                            returningOrderHeader.setHandleFlag("0");
                            returningOrderHeader.setHandleTime(new Date());
                            this.wmsToAppOrderService.updateReturningOrderHeaderByOrderNo(returningOrderHeader);
                            throw new RuntimeException();
                        }
                        for (int j = 1; j <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; j++) {
                            //获取现有库存量
                            CityInventory cityInventory = cityService.findCityInventoryByCityIdAndSku(city.getCityId(), returningOrderGoods.getGcode());
                            if (null == cityInventory) {
                                cityInventory = CityInventory.transform(goodsDO, city);
                                cityService.saveCityInventory(cityInventory);
                            }
                            Integer affectLine = cityService.lockCityInventoryByCityIdAndSkuAndInventory(
                                    city.getCityId(), returningOrderGoods.getGcode(), returningOrderGoods.getRecQty(), cityInventory.getLastUpdateTime());
                            if (affectLine > 0) {
                                CityInventoryAvailableQtyChangeLog log = new CityInventoryAvailableQtyChangeLog();
                                log.setCityId(cityInventory.getCityId());
                                log.setCityName(cityInventory.getCityName());
                                log.setGid(cityInventory.getGid());
                                log.setSku(cityInventory.getSku());
                                log.setSkuName(cityInventory.getSkuName());
                                log.setChangeQty(returningOrderGoods.getRecQty());
                                log.setAfterChangeQty(cityInventory.getAvailableIty() + returningOrderGoods.getRecQty());
                                log.setChangeTime(Calendar.getInstance().getTime());
                                log.setChangeType(CityInventoryAvailableQtyChangeType.PICKING_ORDER_RETURN);
                                log.setChangeTypeDesc(CityInventoryAvailableQtyChangeType.PICKING_ORDER_RETURN.getDescription());
                                log.setReferenceNumber(returningOrderHeader.getBackNo());
                                cityService.addCityInventoryAvailableQtyChangeLog(log);
                                break;
                            } else {
                                if (j == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                    logger.info("GetWMSInfo OUT,获取wms信息失败,获取返配退货增加城市库存失败,任务编号 出参 backNo:{}", returningOrderHeader.getBackNo());
                                    smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "获取wms仓库退货信息失败,增加城市库存失败!任务编号" + returningOrderHeader.getBackNo());
                                }
                            }
                        }
                    }
                }
            }
            log.info("\n********************* 返配上架相关业务已处理完毕 *********************");
        } catch (Exception e) {
            e.printStackTrace();
            returningOrderHeader.setHandleFlag("0");
            returningOrderHeader.setErrMessage(e.getMessage());
            returningOrderHeader.setHandleTime(new Date());
            this.wmsToAppOrderDAO.updateReturningOrderHeaderByOrderNo(returningOrderHeader);
            throw new RuntimeException("处理WMS返配上架单出现异常，处理失败");
        }
    }

    @Async
    @Transactional
    public void handleWtaCancelReturnOrderResultEnter(WtaCancelReturnOrderResultEnter returnOrderResultEnter, String returnNo) {
        try {
            if (returnOrderResultEnter.getIsCancel()) {
                // 修改回原订单的可退和已退！
                List<ReturnOrderGoodsInfo> returnOrderGoodsInfoList = returnOrderService.findReturnOrderGoodsInfoByOrderNumber(returnNo);
                if (AssertUtil.isNotEmpty(returnOrderGoodsInfoList)) {
                    returnOrderGoodsInfoList.forEach(returnOrderGoodsInfo -> appOrderService.updateReturnableQuantityAndReturnQuantityById(
                            returnOrderGoodsInfo.getReturnQty(), returnOrderGoodsInfo.getOrderGoodsId()));
                }
                //修改退货单状态
                returnOrderService.updateReturnOrderStatus(returnNo, AppReturnOrderStatus.CANCELED);
            } else {
                // 如果申请取消退货单失败退回为原来的退货申请状态
                returnOrderService.updateReturnOrderStatus(returnNo, AppReturnOrderStatus.PENDING_PICK_UP);

                ReturnOrderBaseInfo returnOrderBaseInfo = returnOrderService.queryByReturnNo(returnNo);
                // 推送取消订单失败的个人系统消息
//                        NoticePushUtils.pushApplyCancelReturnOrderInfo(returnNo);
                String info = "您取消的退货单" + returnNo + "取消失败，请联系管理人员！";
                smsAccountService.commonSendSms(returnOrderBaseInfo.getCreatorPhone(), info);
            }
            returnOrderResultEnter.setHandleTime(new Date());
            returnOrderResultEnter.setHandleFlag("1");
            this.wmsToAppOrderDAO.updateReturnOrderResultReturn(returnOrderResultEnter);
        } catch (Exception e) {
            log.info("handleWtaCancelReturnOrderResultEnter 发生未知异常，处理失败!\n {}", e);
            e.printStackTrace();
            returnOrderResultEnter.setHandleFlag("0");
            returnOrderResultEnter.setErrorMessage(e.getMessage());
            returnOrderResultEnter.setHandleTime(new Date());
            this.wmsToAppOrderDAO.updateReturnOrderResultReturn(returnOrderResultEnter);
            throw new RuntimeException("handleWtaCancelReturnOrderResultEnter 发生未知异常，处理失败!\n {}", e);
        }

    }
}
