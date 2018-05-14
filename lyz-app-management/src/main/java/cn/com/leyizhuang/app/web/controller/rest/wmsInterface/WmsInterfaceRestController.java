package cn.com.leyizhuang.app.web.controller.rest.wmsInterface;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.core.wechat.refund.MaOnlinePayRefundService;
import cn.com.leyizhuang.app.foundation.pojo.CancelOrderParametersDO;
import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderLifecycle;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.WtaCancelOrderResultEnter;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.WtaReturningOrderGoods;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.WtaReturningOrderHeader;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.WtaShippingOrderHeader;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.*;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.remote.queue.MaSellDetailsSender;
import cn.com.leyizhuang.app.remote.queue.MaSinkSender;
import cn.com.leyizhuang.app.web.controller.rest.BaseRestController;
import cn.com.leyizhuang.app.web.controller.views.wmsInterface.WmsInterfaceViewController;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author GenerationRoad
 * @date 2018/4/28
 */
@RestController
@RequestMapping(value = WmsInterfaceRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class WmsInterfaceRestController extends BaseRestController {
    protected static final String PRE_URL = "/rest/admin/wmsInterface";

    private final Logger logger = LoggerFactory.getLogger(WmsInterfaceViewController.class);

    @Autowired
    private WmsToAppOrderService wmsToAppOrderService;
    @Resource
    private AppOrderService appOrderService;
    @Resource
    private ReturnOrderService returnOrderService;
    @Resource
    private MaSinkSender sinkSender;
    @Resource
    private CancelOrderParametersService cancelOrderParametersService;
    @Resource
    private MaOnlinePayRefundService onlinePayRefundService;
    @Resource
    private SmsAccountService smsAccountService;
    @Resource
    private MaSellDetailsSender sellDetailsSender;
    @Resource
    private CityService cityService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private ReturnOrderDeliveryDetailsService returnOrderDeliveryDetailsService;


    @GetMapping(value = "/handle/deliveryStatus/{orderNumber}")
    public String handleDeliveryStatus(ModelMap map, @PathVariable(value = "orderNumber") String orderNumber) {
        this.wmsToAppOrderService.handleWtaOrderLogistics(orderNumber);
        return "sueesess";
    }

    @GetMapping(value = "/handle/orderShipping/{orderNumber}/{taskNo}")
    public String handleOrderShipping(ModelMap map, @PathVariable(value = "orderNumber") String orderNumber, @PathVariable(value = "taskNo") String taskNo) {
        this.wmsToAppOrderService.handleWtaShippingOrder(orderNumber, taskNo);
        return "sueesess";
    }

    @GetMapping(value = "/handle/orderShipping/all")
    public String handleOrderShippingALL() {
        List<WtaShippingOrderHeader> shippingOrderHeaders = this.wmsToAppOrderService.getAllWtaShippingOrderHeader();
        if (null != shippingOrderHeaders) {
            for (WtaShippingOrderHeader shippingOrderHeader : shippingOrderHeaders) {
                this.wmsToAppOrderService.handleWtaShippingOrder(shippingOrderHeader.getOrderNo(), shippingOrderHeader.getTaskNo());
            }
        }

        return "sueesess";
    }

    @GetMapping(value = "/handle/warehouseAllocation/{allocationNo}")
    public String handleWtaWarehouseAllocation(ModelMap map, @PathVariable(value = "allocationNo") String allocationNo) {
        this.wmsToAppOrderService.handleWtaWarehouseAllocation(allocationNo);
        return "sueesess";
    }

    @GetMapping(value = "/handle/warehousePurchase/{recNo}")
    public String handleWtaWarehousePurchase(ModelMap map, @PathVariable(value = "recNo") String recNo) {
        this.wmsToAppOrderService.handleWtaWarehousePurchase(recNo);
        return "sueesess";
    }

    @GetMapping(value = "/handle/wtaWarehouseWholeSplitToUnit/{directNo}/{sku}/{dsku}")
    public String handleWtaWarehouseWholeSplitToUnit(ModelMap map, @PathVariable(value = "directNo") String directNo, @PathVariable(value = "sku") String sku, @PathVariable(value = "dsku") String dsku) {
        this.wmsToAppOrderService.handlingWtaWarehouseWholeSplitToUnitAsync(directNo, sku, dsku);
        return "sueesess";
    }

    @GetMapping(value = "/handle/wtaWarehouseReportDamageAndOverflow/{wasteNo}/{wasteId}")
    public String handleWtaWarehouseReportDamageAndOverflow(ModelMap map, @PathVariable(value = "wasteNo") String wasteNo, @PathVariable(value = "wasteId") Long wasteId) {
        this.wmsToAppOrderService.handlingWtaWarehouseReportDamageAndOverflowAsync(wasteNo, wasteId);
        return "sueesess";
    }

    @GetMapping(value = "/handle/cancelOrder/{orderNo}")
    public String updateWtaCancelOrderResult(ModelMap map, @PathVariable(value = "orderNo") String orderNo) {
        WtaCancelOrderResultEnter orderResultEnter = this.wmsToAppOrderService.getWtaCancelOrderResult(orderNo);
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
                        onlinePayRefundService.alipayRefundRequest(cancelOrderParametersDO.getUserId(), cancelOrderParametersDO.getIdentityType(), cancelOrderParametersDO.getOrderNumber(), returnOrderBaseInfo.getReturnNo(), orderBillingDetails.getOnlinePayAmount());
                    } else if (OnlinePayType.WE_CHAT.equals(orderBillingDetails.getOnlinePayType())) {
                        //微信退款方法类
                        onlinePayRefundService.wechatReturnMoney(cancelOrderParametersDO.getUserId(), cancelOrderParametersDO.getIdentityType(), orderBillingDetails.getOnlinePayAmount(), cancelOrderParametersDO.getOrderNumber(), returnOrderBaseInfo.getReturnNo());
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
        return "sueesess";
    }

    @GetMapping(value = "/handle/returningOrder/{returnNo}/{recNo}")
    public String handleReturningOrderHeader(ModelMap map, @PathVariable(value = "returnNo") String returnNo, @PathVariable(value = "recNo") String recNo) {
        WtaReturningOrderHeader returningOrderHeader = this.wmsToAppOrderService.getReturningOrderHeaderByReturnNo(returnNo, recNo);
        try {
            if (null != returningOrderHeader) {
                if (OrderUtils.validationReturnOrderNumber(returningOrderHeader.getPoNo())) {

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
                                        Map<String, String> mapPay = onlinePayRefundService.alipayRefundRequest(
                                                returnOrderBaseInfo.getCreatorId(), returnOrderBaseInfo.getCreatorIdentityType().getValue(), returnOrderBaseInfo.getOrderNo(), returnOrderBaseInfo.getReturnNo(), returnOrderBilling.getOnlinePay());
                                    } else if (OnlinePayType.WE_CHAT.equals(returnOrderBilling.getOnlinePayType())) {
                                        //微信退款方法类
                                        Map<String, String> mapPay = onlinePayRefundService.wechatReturnMoney(
                                                returnOrderBaseInfo.getCreatorId(), returnOrderBaseInfo.getCreatorIdentityType().getValue(), returnOrderBilling.getOnlinePay(), returnOrderBaseInfo.getOrderNo(), returnOrderBaseInfo.getReturnNo());
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
                            returnOrderService.saveOrderLifecycle(orderLifecycle);
                            //********************************保存退单生命周期信息***********************
                            ReturnOrderLifecycle returnOrderLifecycle = new ReturnOrderLifecycle();
                            returnOrderLifecycle.setRoid(returnOrderBaseInfo.getRoid());
                            returnOrderLifecycle.setReturnNo(returnOrderBaseInfo.getReturnNo());
                            returnOrderLifecycle.setOperation(OrderLifecycleType.REJECTED);
                            returnOrderLifecycle.setPostStatus(AppReturnOrderStatus.FINISHED);
                            returnOrderLifecycle.setOperationTime(new Date());
                            returnOrderService.saveReturnOrderLifecycle(returnOrderLifecycle);

                            //****************************更新物流表的返配上架时间*******************************
                            returnOrderService.updateReturnLogisticInfoOfBackTime(returnOrderBaseInfo.getReturnNo());

                            //保存物流信息
                            ReturnOrderDeliveryDetail returnOrderDeliveryDetail = ReturnOrderDeliveryDetail.transform(returningOrderHeader);
                            returnOrderDeliveryDetailsService.addReturnOrderDeliveryInfoDetails(returnOrderDeliveryDetail);

                            // rabbitMq 记录退单销量
                            sellDetailsSender.sendReturnOrderSellDetailsTOManagement(returningOrderHeader.getPoNo());
                            //发送退单拆单消息到拆单消息队列
                            sinkSender.sendReturnOrder(returnOrderBaseInfo.getReturnNo());
                            logger.info("cancelOrderToWms OUT,正常退货成功");
                            returningOrderHeader.setHandleFlag("1");
                            returningOrderHeader.setHandleTime(new Date());
                            this.wmsToAppOrderService.updateReturningOrderHeaderByOrderNo(returningOrderHeader);
                        } else {
                            logger.info("getReturnOrderList OUT,正常退货失败,业务处理出现异常!");
                            smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "正常退货业务逻辑处理失败!order:" + returnNo);
                            returningOrderHeader.setHandleFlag("0");
                            returningOrderHeader.setErrMessage("正常退货失败,业务处理出现异常");
                            returningOrderHeader.setHandleTime(new Date());
                            this.wmsToAppOrderService.updateReturningOrderHeaderByOrderNo(returningOrderHeader);
                            throw new RuntimeException();
                        }
                    } else {
                        logger.info("getReturnOrderList OUT,正常退货失败,业务处理出现异常!");
                        smsAccountService.commonSendSms(AppConstant.WMS_ERR_MOBILE, "正常退货业务逻辑处理失败!order:" + returnNo);
                        returningOrderHeader.setHandleFlag("0");
                        returningOrderHeader.setErrMessage("正常退货失败,业务处理出现异常");
                        returningOrderHeader.setHandleTime(new Date());
                        this.wmsToAppOrderService.updateReturningOrderHeaderByOrderNo(returningOrderHeader);
                        throw new RuntimeException();
                    }
                } else {
                    City city = cityService.findCityByWarehouseNo(returningOrderHeader.getWhNo());
                    if (null == city) {
                        returningOrderHeader.setErrMessage("城市信息中没有查询到仓库为" + returningOrderHeader.getWhNo() + "的数据!");
                        returningOrderHeader.setHandleFlag("0");
                        returningOrderHeader.setHandleTime(new Date());
                        this.wmsToAppOrderService.updateReturningOrderHeaderByOrderNo(returningOrderHeader);
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
                                log.setChangeType(CityInventoryAvailableQtyChangeType.HOUSE_DELIVERY_ORDER_RETURN);
                                log.setChangeTypeDesc(CityInventoryAvailableQtyChangeType.HOUSE_DELIVERY_ORDER_RETURN.getDescription());
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
        } catch (Exception e) {
            e.printStackTrace();
            returningOrderHeader.setHandleFlag("0");
            returningOrderHeader.setErrMessage(e.getMessage());
            returningOrderHeader.setHandleTime(new Date());
            this.wmsToAppOrderService.updateReturningOrderHeaderByOrderNo(returningOrderHeader);
        }
        return "sueesess";
    }
}
