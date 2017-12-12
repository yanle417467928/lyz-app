package cn.com.leyizhuang.app.web.controller.returnorder;

import cn.com.leyizhuang.app.core.config.AlipayConfig;
import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.web.controller.wechatpay.WeChatPayController;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.core.constant.OperationReasonType;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.CountUtil;
import cn.com.leyizhuang.common.util.TimeTransformUtils;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Jerry.Ren
 *         Notes: 退货单接口
 *         Created with IntelliJ IDEA.
 *         Date: 2017/12/4.
 *         Time: 9:34.
 */

@RestController
@RequestMapping("/app/returnOrder")
public class ReturnOrderController {

    private static final Logger logger = LoggerFactory.getLogger(ReturnOrderController.class);

    @Resource
    private ReturnOrderService returnOrderService;
    @Resource
    private AppOrderService appOrderService;
    @Resource
    private AppCustomerService customerService;
    @Resource
    private AppEmployeeService employeeService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private AppStoreService appStoreService;
    @Resource
    private ReturnOrderDeliveryDetailsService returnOrderDeliveryDetailsService;
    @Resource
    private OperationReasonsService operationReasonsServiceImpl;
    @Resource
    private DeliveryAddressService deliveryAddressService;


    /**
     * 取消订单
     *
     * @param userId       用户id
     * @param identityType 用户类型
     * @param orderNumber  订单号
     * @param reasonInfo   取消原因
     * @param remarksInfo  备注
     * @return 成功或失败
     */
    @PostMapping(value = "/cansel/order", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> cancelOrder(HttpServletRequest req, HttpServletResponse response, Long userId, Integer identityType,
                                         String orderNumber, String reasonInfo, String remarksInfo) {
        logger.info("cancelOrder CALLED,取消订单，入参 userId:{} identityType:{} orderNumber:{} reasonInfo:{} remarksInfo:{}",
                userId, identityType, orderNumber, reasonInfo, remarksInfo);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("cancelOrder OUT,取消订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("cancelOrder OUT,取消订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(orderNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单号不能为空！", null);
            logger.info("cancelOrder OUT,取消订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(reasonInfo)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "取消原因不能为空！", null);
            logger.info("cancelOrder OUT,取消订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            //获取订单头信息
            OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
            //获取订单账目明细
            OrderBillingDetails orderBillingDetails = appOrderService.getOrderBillingDetail(orderNumber);
            //获取退单号
            String returnNumber = OrderUtils.getReturnNumber();
            //创建退单头
            ReturnOrderBaseInfo returnOrderBaseInfo = new ReturnOrderBaseInfo();
            if (null == orderBaseInfo) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此订单！", null);
                logger.info("cancelOrder OUT,取消订单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if ("送货上门".equals(AppDeliveryType.HOUSE_DELIVERY.getDescription())) {
                if (!"等待物流接收".equals(orderBaseInfo.getDeliveryStatus().getDescription()) &&
                        !"已接收".equals(orderBaseInfo.getDeliveryStatus().getDescription()) &&
                        !"已定位".equals(orderBaseInfo.getDeliveryStatus().getDescription()) &&
                        !"已拣货".equals(orderBaseInfo.getDeliveryStatus().getDescription()) &&
                        !"已装车".equals(orderBaseInfo.getDeliveryStatus().getDescription())) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此订单已配送或已签收不能取消订单！", null);
                    logger.info("cancelOrder OUT,取消订单失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }

            if (null == orderBillingDetails) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此订单费用明细，请联系管理员！", null);
                logger.info("canselOrder OUT,取消订单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            //记录退单头信息
            returnOrderBaseInfo.setOrderId(orderBaseInfo.getId());
            returnOrderBaseInfo.setOrderNo(orderNumber);
            returnOrderBaseInfo.setOrderTime(orderBaseInfo.getCreateTime());
            returnOrderBaseInfo.setReturnTime(new Date());
            returnOrderBaseInfo.setReturnNo(returnNumber);
            returnOrderBaseInfo.setReturnType(ReturnOrderType.CANCEL_RETURN);
            //退款金额
            Double returnPrice = 0.00;
            //获取订单商品
            List<OrderGoodsInfo> orderGoodsInfoList = appOrderService.getOrderGoodsInfoByOrderNumber(orderNumber);

            for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                returnPrice += (orderGoodsInfo.getOrderQuantity() * orderGoodsInfo.getSharePrice());
            }
            returnOrderBaseInfo.setReturnPrice(returnPrice);
            returnOrderBaseInfo.setRemarksInfo(remarksInfo);
            returnOrderBaseInfo.setCreatorId(userId);
            returnOrderBaseInfo.setCreatorIdentityType(AppIdentityType.getAppIdentityTypeByValue(identityType));
            if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.CUSTOMER)) {
                AppCustomer customer = customerService.findById(userId);
                returnOrderBaseInfo.setCreatorPhone(customer.getMobile());
            } else {
                AppEmployee employee = employeeService.findById(userId);
                returnOrderBaseInfo.setCreatorPhone(employee.getMobile());
                if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.SELLER)) {
                    returnOrderBaseInfo.setCustomerId(orderBaseInfo.getCustomerId());
                    returnOrderBaseInfo.setCustomerName(orderBaseInfo.getCustomerName());
                }
            }
            returnOrderBaseInfo.setReasonInfo(reasonInfo);
            returnOrderBaseInfo.setOrderType(orderBaseInfo.getOrderType());
            returnOrderBaseInfo.setReturnStatus(AppReturnOrderStatus.FINISHED);
            //保存退单头信息
            returnOrderService.saveReturnOrderBaseInfo(returnOrderBaseInfo);
            //获取退单头id
            Long returnOrderId = returnOrderBaseInfo.getRoid();
            //判断收货类型和订单状态
            if (orderBaseInfo.getStatus().equals(AppOrderStatus.PENDING_RECEIVE) && orderBaseInfo.getDeliveryStatus().equals(AppDeliveryType.HOUSE_DELIVERY)) {
                //TODO 通知WMS

                //修改订单状态为取消中
                orderBaseInfo.setStatus(AppOrderStatus.CANCELING);
                appOrderService.updateOrderStatusByOrderNo(orderBaseInfo);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "取消订单提交成功，等待确认！", null);
                logger.info("canselOrder OUT,取消订单提交成功！，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }


            //记录退单其他相关信息
            returnOrderService.canselOrder(orderGoodsInfoList, returnOrderId, userId, identityType, returnNumber, orderNumber, orderBillingDetails, orderBaseInfo);


            //********************************退第三方支付**************************
            //如果是待收货、门店自提单则需要返回第三方支付金额
            if (orderBaseInfo.getDeliveryStatus().equals(AppDeliveryType.SELF_TAKE) && orderBaseInfo.getStatus().equals(AppOrderStatus.PENDING_RECEIVE)) {
                if ("ALIPAY".equals(orderBillingDetails.getOnlinePayType())) {
                    //支付宝退款
                    AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.serverUrl, AlipayConfig.appId, AlipayConfig.privateKey, AlipayConfig.format, AlipayConfig.charset, AlipayConfig.aliPublicKey, AlipayConfig.signType);
                    AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
                    request.setBizContent("{" +
                            "\"out_trade_no\":\"" + orderNumber + "\"," +
                            "\"refund_amount\":" + orderBillingDetails.getOnlinePayAmount() + "," +
                            "  }");
                    AlipayTradeRefundResponse aliPayResponse = alipayClient.execute(request);
                    if (aliPayResponse.isSuccess()) {
                        //创建退单退款详情实体
                        ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                        returnOrderBillingDetail.setRoid(returnOrderId);
                        returnOrderBillingDetail.setRefundNumber(OrderUtils.getRefundNumber());
                        returnOrderBillingDetail.setIntoAmountTime(aliPayResponse.getGmtRefundPay());
                        returnOrderBillingDetail.setReplyCode(aliPayResponse.getTradeNo());
                        returnOrderBillingDetail.setReturnMoney(Double.valueOf(aliPayResponse.getRefundFee()));
                        returnOrderBillingDetail.setReturnPayType(OnlinePayType.ALIPAY);
                        returnOrderService.saveReturnOrderBillingDetail(returnOrderBillingDetail);
                    } else {
                        System.out.println("调用失败");
                    }


                } else if ("WE_CHAT".equals(orderBillingDetails.getOnlinePayType())) {
                    //微信退款方法类
                    WeChatPayController wechat = new WeChatPayController();
                    wechat.wechatReturnMoney(req, response, userId, identityType, orderBillingDetails.getOnlinePayAmount(), orderNumber, returnNumber);
                    //创建退单退款详情实体
                    ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                    returnOrderBillingDetail.setRoid(returnOrderId);
                    returnOrderBillingDetail.setRefundNumber(returnNumber);
                    //TODO 时间待定
                    returnOrderBillingDetail.setIntoAmountTime(new Date());
                    //TODO 第三方回复码
                    returnOrderBillingDetail.setReplyCode("");
                    returnOrderBillingDetail.setReturnMoney(orderBillingDetails.getOnlinePayAmount());
                    returnOrderBillingDetail.setReturnPayType(OnlinePayType.WE_CHAT);
                    //TODO 保存退款记录

                } else if ("UNION_PAY".equals(orderBillingDetails.getOnlinePayAmount())) {
                    //创建退单退款详情实体
                    ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                    returnOrderBillingDetail.setRoid(returnOrderId);
                    returnOrderBillingDetail.setRefundNumber(returnNumber);
                    //TODO 时间待定
                    returnOrderBillingDetail.setIntoAmountTime(new Date());
                    //TODO 第三方回复码
                    returnOrderBillingDetail.setReplyCode("");
                    returnOrderBillingDetail.setReturnMoney(orderBillingDetails.getOnlinePayAmount());
                    returnOrderBillingDetail.setReturnPayType(OnlinePayType.UNION_PAY);
                }

            }
            //修改订单状态为已取消
            appOrderService.updateOrderStatusByOrderNoAndStatus(AppOrderStatus.CANCELED, orderBaseInfo.getOrderNumber());

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("getReturnOrderList OUT,取消订单成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，取消订单失败", null);
            logger.warn("cancelOrder EXCEPTION,取消订单失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }


    /**
     * 拒签退货
     *
     * @param userId       用户id
     * @param identityType 用户类型
     * @param orderNumber  订单号
     * @param reasonInfo   取消原因
     * @param remarksInfo  备注
     * @return 成功或失败
     */
    @PostMapping(value = "/refused/order", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> refusedOrder(HttpServletRequest req, HttpServletResponse response, Long userId, Integer identityType,
                                          String orderNumber, String reasonInfo, String remarksInfo) {
        logger.info("refusedOrder CALLED,拒签退货，入参 userId:{} identityType:{} orderNumber:{} reasonInfo:{} remarksInfo:{}",
                userId, identityType, orderNumber, reasonInfo, remarksInfo);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("refusedOrder OUT,拒签退货失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("refusedOrder OUT,拒签退货失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(orderNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单号不能为空！", null);
            logger.info("refusedOrder OUT,拒签退货失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(reasonInfo)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "取消原因不能为空！", null);
            logger.info("refusedOrder OUT,拒签退货失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            //获取订单头信息
            OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
            //获取订单账目明细
            OrderBillingDetails orderBillingDetails = appOrderService.getOrderBillingDetail(orderNumber);
            //获取退单号
            String returnNumber = OrderUtils.getReturnNumber();
            //创建退单头
            ReturnOrderBaseInfo returnOrderBaseInfo = new ReturnOrderBaseInfo();
            if (null == orderBaseInfo) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此订单！", null);
                logger.info("refusedOrder OUT,拒签退货失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            if (null == orderBillingDetails) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此订单费用明细，请联系管理员！", null);
                logger.info("refusedOrder OUT,拒签退货失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            //记录退单头信息
            returnOrderBaseInfo.setOrderId(orderBaseInfo.getId());
            returnOrderBaseInfo.setOrderNo(orderNumber);
            returnOrderBaseInfo.setOrderTime(orderBaseInfo.getCreateTime());
            returnOrderBaseInfo.setReturnTime(new Date());
            returnOrderBaseInfo.setReturnNo(returnNumber);
            returnOrderBaseInfo.setReturnType(ReturnOrderType.REFUSED_RETURN);
            //退款金额
            Double returnPrice = 0.00;
            //获取订单商品
            List<OrderGoodsInfo> orderGoodsInfoList = appOrderService.getOrderGoodsInfoByOrderNumber(orderNumber);

            for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                returnPrice += (orderGoodsInfo.getOrderQuantity() * orderGoodsInfo.getSharePrice());
            }
            returnOrderBaseInfo.setReturnPrice(returnPrice);
            returnOrderBaseInfo.setRemarksInfo(remarksInfo);
            returnOrderBaseInfo.setCreatorId(userId);
            returnOrderBaseInfo.setCreatorIdentityType(AppIdentityType.getAppIdentityTypeByValue(identityType));
            AppEmployee employee = employeeService.findById(userId);
            returnOrderBaseInfo.setCreatorPhone(employee.getMobile());
            if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.SELLER)) {
                returnOrderBaseInfo.setCustomerId(orderBaseInfo.getCustomerId());
                returnOrderBaseInfo.setCustomerName(orderBaseInfo.getCustomerName());
            }

            returnOrderBaseInfo.setReasonInfo(reasonInfo);
            returnOrderBaseInfo.setOrderType(orderBaseInfo.getOrderType());
            returnOrderBaseInfo.setReturnStatus(AppReturnOrderStatus.FINISHED);
            //保存退单头信息
            returnOrderService.saveReturnOrderBaseInfo(returnOrderBaseInfo);
            //获取退单头id
            Long returnOrderId = returnOrderBaseInfo.getRoid();

            //记录退单其他相关信息
            returnOrderService.refusedOrder(orderGoodsInfoList, returnOrderId, userId, returnNumber, orderNumber, orderBillingDetails, orderBaseInfo);


            //********************************退第三方支付**************************
            //如果是待收货、门店自提单则需要返回第三方支付金额
            if (orderBaseInfo.getDeliveryStatus().equals(AppDeliveryType.SELF_TAKE) && orderBaseInfo.getStatus().equals(AppOrderStatus.PENDING_RECEIVE)) {
                if ("ALIPAY".equals(orderBillingDetails.getOnlinePayType())) {
                    //支付宝退款
                    AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.serverUrl, AlipayConfig.appId, AlipayConfig.privateKey, AlipayConfig.format, AlipayConfig.charset, AlipayConfig.aliPublicKey, AlipayConfig.signType);
                    AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
                    request.setBizContent("{" +
                            "\"out_trade_no\":\"" + orderNumber + "\"," +
                            "\"refund_amount\":" + orderBillingDetails.getOnlinePayAmount() + "," +
                            "  }");
                    AlipayTradeRefundResponse aliPayResponse = alipayClient.execute(request);
                    if (aliPayResponse.isSuccess()) {
                        //创建退单退款详情实体
                        ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                        returnOrderBillingDetail.setRoid(returnOrderId);
                        returnOrderBillingDetail.setRefundNumber(OrderUtils.getRefundNumber());
                        returnOrderBillingDetail.setIntoAmountTime(aliPayResponse.getGmtRefundPay());
                        returnOrderBillingDetail.setReplyCode(aliPayResponse.getTradeNo());
                        returnOrderBillingDetail.setReturnMoney(Double.valueOf(aliPayResponse.getRefundFee()));
                        returnOrderBillingDetail.setReturnPayType(OnlinePayType.ALIPAY);
                        returnOrderService.saveReturnOrderBillingDetail(returnOrderBillingDetail);
                    } else {
                        System.out.println("调用失败");
                    }


                } else if ("WE_CHAT".equals(orderBillingDetails.getOnlinePayType())) {
                    //微信退款方法类
                    WeChatPayController wechat = new WeChatPayController();
                    wechat.wechatReturnMoney(req, response, userId, identityType, orderBillingDetails.getOnlinePayAmount(), orderNumber, returnNumber);
                    //创建退单退款详情实体
                    ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                    returnOrderBillingDetail.setRoid(returnOrderId);
                    returnOrderBillingDetail.setRefundNumber(returnNumber);
                    //TODO 时间待定
                    returnOrderBillingDetail.setIntoAmountTime(new Date());
                    //TODO 第三方回复码
                    returnOrderBillingDetail.setReplyCode("");
                    returnOrderBillingDetail.setReturnMoney(orderBillingDetails.getOnlinePayAmount());
                    returnOrderBillingDetail.setReturnPayType(OnlinePayType.WE_CHAT);
                    //TODO 保存退款记录

                } else if ("UNION_PAY".equals(orderBillingDetails.getOnlinePayAmount())) {
                    //创建退单退款详情实体
                    ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                    returnOrderBillingDetail.setRoid(returnOrderId);
                    returnOrderBillingDetail.setRefundNumber(returnNumber);
                    //TODO 时间待定
                    returnOrderBillingDetail.setIntoAmountTime(new Date());
                    //TODO 第三方回复码
                    returnOrderBillingDetail.setReplyCode("");
                    returnOrderBillingDetail.setReturnMoney(orderBillingDetails.getOnlinePayAmount());
                    returnOrderBillingDetail.setReturnPayType(OnlinePayType.UNION_PAY);
                }

            }
            //修改订单状态为已取消
            appOrderService.updateOrderStatusByOrderNoAndStatus(AppOrderStatus.REJECTED, orderBaseInfo.getOrderNumber());

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("refusedOrder OUT,拒签退货成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，拒签退货失败", null);
            logger.warn("refusedOrder EXCEPTION,拒签退货失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }


    /**
     * 获取用户退货单列表
     *
     * @param userId       用户id
     * @param identityType 用户身份
     * @return 退货单列表
     */
    @PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResultDTO getReturnOrderList(Long userId, Integer identityType, Integer showStatus) {

        logger.info("getReturnOrderList CALLED,获取用户退货单列表，入参 userID:{}, identityType:{}, showStatus{}", userId, identityType, showStatus);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getReturnOrderList OUT,获取用户退货单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("getReturnOrderList OUT,获取用户退货单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            //查询所有退单表
            List<ReturnOrderBaseInfo> baseInfo = returnOrderService.findReturnOrderListByUserIdAndIdentityType(userId, identityType, showStatus);

            //创建一个返回对象list
            List<OrderListResponse> orderListResponses = new ArrayList<>();

            for (ReturnOrderBaseInfo returnBaseInfo : baseInfo) {
                //创建有个存放图片地址的list
                List<String> goodsImgList = new ArrayList<>();
                //创建一个返回类（借用订单返回对象）
                OrderListResponse orderListResponse = new OrderListResponse();
                //获取订单商品
                List<ReturnOrderGoodsInfo> returnGoodsInfoList = returnOrderService.findReturnOrderGoodsInfoByOrderNumber(returnBaseInfo.getReturnNo());
                //遍历订单商品
                int count = 0;
                for (ReturnOrderGoodsInfo returnGoodsInfo : returnGoodsInfoList) {
                    goodsImgList.add(goodsService.queryBySku(returnGoodsInfo.getSku()).getCoverImageUri());
                    count = count + returnGoodsInfo.getReturnQty();
                }
                // TODO 是否要加上参加促销的标题？
                orderListResponse.setOrderNo(returnBaseInfo.getReturnNo());
                orderListResponse.setStatus(returnBaseInfo.getReturnStatus().getDescription());
                orderListResponse.setCount(count);
                orderListResponse.setPrice(returnBaseInfo.getReturnPrice());
                orderListResponse.setGoodsImgList(goodsImgList);
                //添加到返回类list中
                orderListResponses.add(orderListResponse);

            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, orderListResponses);
            logger.info("getReturnOrderList OUT,获取用户退货单列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取用户退货单列表失败", null);
            logger.warn("getReturnOrderList EXCEPTION,获取用户退货单列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 查看退货单详情
     *
     * @param userId       身份id
     * @param identityType 身份类型
     * @param returnNumber 退单号
     * @return 退单信息
     */
    @PostMapping(value = "/detail", produces = "application/json;charset=UTF-8")
    public ResultDTO getReturnOrderDetail(Long userId, Integer identityType, String returnNumber) {

        logger.info("getReturnOrderDetail CALLED,查看退货单详情，入参 userID:{}, identityType:{}, returnNumber:{}", userId, identityType, returnNumber);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("getReturnOrderDetail OUT,查看退货单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("getReturnOrderDetail OUT,查看退货单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(returnNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "退单号不能为空！", null);
            logger.info("getReturnOrderDetail OUT,查看退货单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            //查退单
            ReturnOrderBaseInfo returnBaseInfo = returnOrderService.queryByReturnNo(returnNumber);
            ReturnOrderDetailResponse detailResponse = null;
            if (returnBaseInfo != null) {

                //获取原订单收货/自提门店地址
                OrderLogisticsInfo orderLogisticsInfo = appOrderService.getOrderLogistice(returnBaseInfo.getOrderNo());

                detailResponse = new ReturnOrderDetailResponse();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                //设置基础信息
                detailResponse.setReturnStatus(returnBaseInfo.getReturnStatus().getDescription());
                detailResponse.setReturnNumber(returnBaseInfo.getReturnNo());
                detailResponse.setReturnTime(sdf.format(returnBaseInfo.getReturnTime()));
                detailResponse.setTotalReturnPrice(returnBaseInfo.getReturnPrice());
                //取货方式（上门取货，送货到店）
                if (AppDeliveryType.SELF_TAKE.equals(orderLogisticsInfo.getDeliveryType())) {
                    detailResponse.setBookingStoreName(orderLogisticsInfo.getBookingStoreName());
                    AppStore appStore = appStoreService.findByStoreCode(orderLogisticsInfo.getBookingStoreCode());
                    detailResponse.setBookingStorePhone(appStore.getPhone());
                    detailResponse.setStoreDetailedAddress(appStore.getDetailedAddress());
                } else {
                    detailResponse.setDeliveryTime(orderLogisticsInfo.getDeliveryTime());
                    detailResponse.setReceiver(orderLogisticsInfo.getReceiver());
                    detailResponse.setReceiverPhone(orderLogisticsInfo.getReceiverPhone());
                    detailResponse.setShippingAddress(orderLogisticsInfo.getShippingAddress());
                }
                detailResponse.setGoodsList(returnOrderService.getReturnOrderGoodsDetails(returnNumber));
                int count = 0;
                Double totalReturnPrice = 0.00;
                //获取订单商品
                List<ReturnOrderGoodsInfo> returnGoodsInfoList = returnOrderService.findReturnOrderGoodsInfoByOrderNumber(returnBaseInfo.getReturnNo());
                //遍历订单商品，算出总商品数量和退货商品总价
                for (ReturnOrderGoodsInfo returnGoodsInfo : returnGoodsInfoList) {
                    count = count + returnGoodsInfo.getReturnQty();
                    totalReturnPrice = CountUtil.add(totalReturnPrice, CountUtil.mul(returnGoodsInfo.getReturnQty(), returnGoodsInfo.getReturnPrice()));
                }
                detailResponse.setReturnQty(count);
                detailResponse.setTotalReturnPrice(totalReturnPrice);
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, detailResponse);
            logger.info("getReturnOrderDetail OUT,查看退货单详情成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，查看退货单详情失败", null);
            logger.warn("getReturnOrderDetail EXCEPTION,查看退货单详情失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @param
     * @return
     * @throws
     * @title 获取退货订单原因列表
     * @descripe
     * @author GenerationRoad
     * @date 2017/11/21
     */
    @PostMapping(value = "/returnReasons", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getReturnReasons(Long userId, Integer identityType) {
        logger.info("getReturnReasons CALLED,获取退货订单原因列表，入参 userId:{} identityType:{}", userId, identityType);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("getReturnReasons OUT,获取退货订单原因列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！", null);
            logger.info("getReturnReasons OUT,获取退货订单原因列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        List<OperationReasonsResponse> operationReasonsServiceList = this.operationReasonsServiceImpl.findAllByType(OperationReasonType.RETURN);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, operationReasonsServiceList);
        logger.info("getReturnReasons OUT,获取退货订单原因列表成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

    /**
     * 用户取消退货单
     *
     * @param userId       用户id
     * @param identityType 用户身份
     * @param returnNumber 退单号
     * @return 是否成功
     */
    @PostMapping(value = "/cancel", produces = "application/json;charset=UTF-8")
    public ResultDTO cancelReturnOrder(Long userId, Integer identityType, String returnNumber) {

        logger.info("cancelReturnOrder CALLED,用户取消退货单，入参 userID:{}, identityType:{}, returnNumber:{}", userId, identityType, returnNumber);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("cancelReturnOrder OUT,用户取消退货单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("cancelReturnOrder OUT,用户取消退货单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(returnNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "退单号不能为空！", null);
            logger.info("cancelReturnOrder OUT,用户取消退货单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            //分别判断是普通退货单，还是买券退货，还是
            ReturnOrderBaseInfo returnOrderBaseInfo = returnOrderService.queryByReturnNo(returnNumber);
            //判断物流是否已取货，买券无判断
            ReturnOrderDeliveryDetail returnOrderDeliveryDetail = returnOrderDeliveryDetailsService.getReturnLogisticStatusDetail(returnNumber);
            if (ReturnLogisticStatus.PICKUP_COMPLETE.equals(returnOrderDeliveryDetail.getReturnLogisticStatus())) {
                //TODO
            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，用户取消退货单失败", null);
            logger.warn("cancelReturnOrder EXCEPTION,用户取消退货单失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 用户点击退货获取可退商品列表
     *
     * @param userId       用户id
     * @param identityType 用户身份
     * @param orderNumber  订单
     * @return 可退商品列表，订单门店
     */
    @PostMapping(value = "/goods/returnable", produces = "application/json;charset=UTF-8")
    public ResultDTO getReturnOrderGoodsList(Long userId, Integer identityType, String orderNumber) {

        logger.info("getReturnOrderGoodsList CALLED,用户点击退货获取可退商品列表，入参 userID:{}, identityType:{}, orderNumber:{}", userId, identityType, orderNumber);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("getReturnOrderGoodsList OUT,用户点击退货获取可退商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("getReturnOrderGoodsList OUT,用户点击退货获取可退商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(orderNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单号不能为空！", null);
            logger.info("getReturnOrderGoodsList OUT,用户点击退货获取可退商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            //产品券可退，促销商品必须退赠品，返回促销标题，现金券，乐币，不可退，如果是预存款和第三方支付，优先顺序？
            //创建返回对象集合
            List<ReturnOrderGoodsResponse> returnOrderGoodsList = null;
            //获取订单商品数量促销信息
            List<OrderGoodsInfo> orderGoodsInfoList = appOrderService.getOrderGoodsInfoByOrderNumber(orderNumber);
            if (!orderGoodsInfoList.isEmpty()) {
                returnOrderGoodsList = new ArrayList<>(orderGoodsInfoList.size());
                //将订单商品信息设置到返回对象中
                for (OrderGoodsInfo goodsInfo : orderGoodsInfoList) {
                    ReturnOrderGoodsResponse returnOrderGoodsResponse = new ReturnOrderGoodsResponse();
                    returnOrderGoodsResponse.setSku(goodsInfo.getSku());
                    returnOrderGoodsResponse.setSkuName(goodsInfo.getSkuName());
                    returnOrderGoodsResponse.setRetailPrice(goodsInfo.getRetailPrice());
                    returnOrderGoodsResponse.setReturnPrice(goodsInfo.getReturnPrice());
                    returnOrderGoodsResponse.setOrderQuantity(goodsInfo.getOrderQuantity());
                    returnOrderGoodsResponse.setReturnableQuantity(goodsInfo.getReturnableQuantity());
                    returnOrderGoodsResponse.setPromotionId(goodsInfo.getPromotionId());
                    //TODO 查促销表的标题
                    returnOrderGoodsResponse.setPromotionTitle("查询促销标题");
                    returnOrderGoodsResponse.setGoodsLine(goodsInfo.getGoodsLineType().getDescription());
                    returnOrderGoodsList.add(returnOrderGoodsResponse);
                }
                //获取订单商品详细信息包含图片，
                List<GiftListResponseGoods> giftListResponseGoods = appOrderService.getOrderGoodsDetails(orderNumber);
                if (!giftListResponseGoods.isEmpty()) {
                    for (ReturnOrderGoodsResponse returnOrderGoodsResponse : returnOrderGoodsList) {
                        for (GiftListResponseGoods giftListResponseGood : giftListResponseGoods) {
                            if (giftListResponseGood.getCoverImageUri().equals(returnOrderGoodsResponse.getCoverImageUri())) {
                                returnOrderGoodsResponse.setCoverImageUri(giftListResponseGood.getCoverImageUri());
                                break;
                            }
                        }
                    }
                }
                //按照退货优先级排个序
                returnOrderGoodsList.sort((o1, o2) -> o1.getReturnPriority().compareTo(o2.getReturnPriority()));
            }

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    returnOrderGoodsList != null && returnOrderGoodsList.size() > 0 ? returnOrderGoodsList : null);
            logger.info("getReturnOrderDetail OUT,用户点击退货获取可退商品列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;

        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，用户点击退货获取可退商品列表失败", null);
            logger.warn("getReturnOrderGoodsList EXCEPTION,用户点击退货获取可退商品列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 获取用户退货方式和取货地址
     *
     * @param userId       用户id
     * @param identityType 用户身份类型
     * @param orderNumber  订单号
     * @return 返回退货门店和退货地址
     */
    @PostMapping(value = "/delivery/address", produces = "application/json;charset=UTF-8")
    public ResultDTO getReturnOrderDeliveryType(Long userId, Integer identityType, String orderNumber) {

        logger.info("getReturnOrderDeliveryType CALLED,获取用户退货方式和取货地址，入参 userID:{}, identityType:{}, orderNumber:{}", userId, identityType, orderNumber);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("getReturnOrderDeliveryType OUT,获取用户退货方式和取货地址失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("getReturnOrderDeliveryType OUT,获取用户退货方式和取货地址失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(orderNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单号不能为空！", null);
            logger.info("getReturnOrderDeliveryType OUT,获取用户退货方式和取货地址失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            //获取原订单收货/自提门店地址
            OrderLogisticsInfo orderLogisticsInfo = appOrderService.getOrderLogistice(orderNumber);
            //如果是门店自提，取货地址就取顾客默认地址
            if (orderLogisticsInfo != null) {
                if (StringUtils.isBlank(orderLogisticsInfo.getShippingAddress())) {
                    AppIdentityType identityType1 = AppIdentityType.getAppIdentityTypeByValue(identityType);
                    DeliveryAddressResponse defaultDeliveryAddress = deliveryAddressService.getDefaultDeliveryAddressByUserIdAndIdentityType(userId, identityType1);
                    if (null == defaultDeliveryAddress) {
                        defaultDeliveryAddress = deliveryAddressService.getTopDeliveryAddressByUserIdAndIdentityType
                                (userId, AppIdentityType.getAppIdentityTypeByValue(identityType));
                    }
                    orderLogisticsInfo = transform(orderLogisticsInfo, defaultDeliveryAddress);
                    //如果是送货上门，退货到门店的地址就是订单门店地址
                } else if (StringUtils.isBlank(orderLogisticsInfo.getBookingStoreAddress()) && identityType == 6) {
                    OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
                    //下订单的id 是否和当前顾客的ID一致
                    if (null != orderBaseInfo && null != orderBaseInfo.getCreatorId()) {
                        if (orderBaseInfo.getCreatorId().equals(userId)) {
                            AppStore store = appStoreService.findStoreByUserIdAndIdentityType(userId, identityType);
                            orderLogisticsInfo.setBookingStoreCode(store.getStoreCode());
                            orderLogisticsInfo.setBookingStoreName(store.getStoreName());
                            orderLogisticsInfo.setBookingStoreAddress(store.getDetailedAddress());
                        }
                    }
                }
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, orderLogisticsInfo);
            logger.info("getReturnOrderDetail OUT,用户点击退货获取可退商品列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取用户退货方式和取货地址失败", null);
            logger.warn("getReturnOrderDeliveryType EXCEPTION,获取用户退货方式和取货地址失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    private OrderLogisticsInfo transform(OrderLogisticsInfo orderLogisticsInfo, DeliveryAddressResponse defaultDeliveryAddress) {
        orderLogisticsInfo.setResidenceName(defaultDeliveryAddress.getDeliveryName());
        orderLogisticsInfo.setReceiverPhone(defaultDeliveryAddress.getDeliveryPhone());
        orderLogisticsInfo.setDeliveryCity(defaultDeliveryAddress.getDeliveryCity());
        orderLogisticsInfo.setDeliveryCounty(defaultDeliveryAddress.getDeliveryCounty());
        orderLogisticsInfo.setDeliveryStreet(defaultDeliveryAddress.getDeliveryStreet());
        orderLogisticsInfo.setDetailedAddress(defaultDeliveryAddress.getDetailedAddress());
        orderLogisticsInfo.setResidenceName(defaultDeliveryAddress.getVillageName());
        String shippingAddress = defaultDeliveryAddress.getDeliveryCity() +
                defaultDeliveryAddress.getDeliveryCounty() +
                defaultDeliveryAddress.getDeliveryStreet() +
                defaultDeliveryAddress.getDetailedAddress();
        orderLogisticsInfo.setShippingAddress(shippingAddress);
        return orderLogisticsInfo;
    }
}
