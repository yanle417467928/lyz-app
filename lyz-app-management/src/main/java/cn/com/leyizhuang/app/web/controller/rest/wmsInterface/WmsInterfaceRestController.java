package cn.com.leyizhuang.app.web.controller.rest.wmsInterface;

import cn.com.leyizhuang.app.core.constant.OnlinePayType;
import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.core.wechat.refund.MaOnlinePayRefundService;
import cn.com.leyizhuang.app.foundation.pojo.CancelOrderParametersDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.WtaCancelOrderResultEnter;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.WtaShippingOrderHeader;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBillingDetail;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.remote.queue.MaSinkSender;
import cn.com.leyizhuang.app.web.controller.rest.BaseRestController;
import cn.com.leyizhuang.app.web.controller.views.wmsInterface.WmsInterfaceViewController;
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
        if (null != shippingOrderHeaders){
            for (WtaShippingOrderHeader shippingOrderHeader: shippingOrderHeaders) {
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

    @GetMapping(value = "/handle/warehousePurchase/{purchaseNo}")
    public String handleWtaWarehousePurchase(ModelMap map, @PathVariable(value = "purchaseNo") String purchaseNo) {
        this.wmsToAppOrderService.handleWtaWarehousePurchase(purchaseNo);
        return "sueesess";
    }

    @GetMapping(value = "/handle/wtaWarehouseWholeSplitToUnit/{directNo}/{sku}/{dsku}")
    public String handleWtaWarehouseWholeSplitToUnit(ModelMap map, @PathVariable(value = "directNo") String directNo, @PathVariable(value = "sku") String sku, @PathVariable(value = "dsku") String dsku) {
        this.wmsToAppOrderService.handlingWtaWarehouseWholeSplitToUnitAsync(directNo,sku,dsku);
        return "sueesess";
    }

    @GetMapping(value = "/handle/wtaWarehouseReportDamageAndOverflow/{wasteNo}/{wasteId}")
    public String handleWtaWarehouseReportDamageAndOverflow(ModelMap map, @PathVariable(value = "wasteNo") String wasteNo,@PathVariable(value = "wasteId") Long wasteId) {
        this.wmsToAppOrderService.handlingWtaWarehouseReportDamageAndOverflowAsync(wasteNo,wasteId);
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
        } else{
            String info = "您取消的订单" + orderResultEnter.getOrderNo() + "，取消失败，请联系管理人员！";
            logger.info("取消失败订单号:{}", orderResultEnter.getOrderNo());
            smsAccountService.commonSendSms(orderBaseInfo.getCreatorPhone(), info);
        }
        return "sueesess";
    }
}
