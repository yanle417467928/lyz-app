package cn.com.leyizhuang.app.web.controller.returnorder;

import cn.com.leyizhuang.app.core.config.AlipayConfig;
import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.core.utils.oss.FileUploadOSSUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.CustomerProductCoupon;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.GoodsSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.web.controller.wechatpay.WeChatPayController;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.core.constant.OperationReasonType;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.CountUtil;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @Resource
    private ProductCouponService productCouponService;

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
    @PostMapping(value = "/cancel/order", produces = "application/json;charset=UTF-8")
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
     * 用户申请退货创建退货单
     *
     * @param param    创建退货单参数
     * @param pictures 退货单问题描述图片
     * @return 是否成功
     */
    @PostMapping(value = "/create", produces = "application/json;charset=UTF-8")
    public ResultDTO createReturnOrder(@RequestBody ReturnOrderCreateParam param, @RequestParam(value = "pictures",
            required = false) MultipartFile[] pictures) {

        logger.info("createReturnOrder CALLED,用户申请退货创建退货单，入参 param:{}, pictures:{}", param, pictures);

        ResultDTO<Object> resultDTO;
        if (null == param.getUserId()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("createReturnOrder OUT,用户申请退货创建退货单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == param.getIdentityType()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("createReturnOrder OUT,用户申请退货创建退货单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (param.getIdentityType() == 0 && param.getCusId() == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "代下单客户ID不能为空!", "");
            logger.warn("createReturnOrder OUT,用户申请退货创建退货单失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == param.getReturnDeliveryInfo()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "物流信息不可为空", null);
            logger.info("createReturnOrder OUT,用户申请退货创建退货单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (param.getReturnGoodsInfo().isEmpty()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有选择退货商品！", null);
            logger.info("createReturnOrder OUT,用户申请退货创建退货单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(param.getOrderNo())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "原订单编号不可为空", null);
            logger.info("createReturnOrder OUT,用户申请退货创建退货单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        //判断创单人身份是否合法
        if (!(param.getIdentityType() == 0 || param.getIdentityType() == 6 || param.getIdentityType() == 2)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "创单人身份不合法!", "");
            logger.warn("createReturnOrder OUT,用户申请退货创建退货单失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {

            Long userId = param.getUserId();
            Integer identityType = param.getIdentityType();
            String orderNo = param.getOrderNo();

            //*******************处理上传图骗***********************
            List<String> pictureUrls = new ArrayList<>();
            String returnPic = null;
            if (pictures != null) {
                for (MultipartFile picture : pictures) {
                    String url = FileUploadOSSUtils.uploadProfilePhoto(picture, "returnOrder/evaluation/");
                    pictureUrls.add(url);
                }
                returnPic = org.apache.commons.lang.StringUtils.strip(pictureUrls.toString(), "[]");
            }
            //********************创建退货单基础信息******************
            //记录原订单信息
            OrderBaseInfo order = appOrderService.getOrderByOrderNumber(orderNo);
            ReturnOrderBaseInfo returnOrderBaseInfo = returnOrderService.createReturnOrderBaseInfo(order.getId(), order.getOrderNumber(),
                    order.getCreateTime(), param.getRemarksInfo(), userId, identityType, param.getReasonInfo(), returnPic, order.getOrderType());
            if (identityType == 0) {
                AppCustomer customer = customerService.findById(param.getCusId());
                if (customer != null) {
                    returnOrderBaseInfo.setCustomerId(customer.getCusId());
                    returnOrderBaseInfo.setCustomerName(customer.getName());
                    returnOrderBaseInfo.setCustomerType(customer.getCustomerType());
                }
            }
            //******************* 创建退货单物流信息 ************************
            ReturnOrderLogisticInfo returnOrderLogisticInfo = returnOrderService.createReturnOrderLogisticInfo(param.getReturnDeliveryInfo());
            String returnNo = returnOrderBaseInfo.getReturnNo();
            returnOrderLogisticInfo.setReturnNO(returnNo);

            //******************* 创建退货单商品信息 ************************
            List<ReturnOrderGoodsInfo> goodsInfos = new ArrayList<>();
            List<GoodsSimpleInfo> simpleInfos = param.getReturnGoodsInfo();

            Double returnTotalGoodsPrice = 0D;
            //获取原单商品信息
            List<OrderGoodsInfo> orderGoodsInfoList = appOrderService.getOrderGoodsInfoByOrderNumber(orderNo);

            if (orderGoodsInfoList != null) {
                for (OrderGoodsInfo goodsInfo : orderGoodsInfoList) {
                    for (GoodsSimpleInfo simpleInfo : simpleInfos) {
                        if (goodsInfo.getId().equals(simpleInfo.getId())) {
                            if (simpleInfo.getGoodsLineType().equals(goodsInfo.getGoodsLineType().getDescription())) {
                                ReturnOrderGoodsInfo returnOrderGoodsInfo = transform(goodsInfo, simpleInfo.getNum(), returnNo);
                                //设置原订单可退数量 减少
                                goodsInfo.setReturnableQuantity(goodsInfo.getReturnableQuantity() - simpleInfo.getNum());
                                //设置原订单退货数量 增加
                                goodsInfo.setReturnQuantity(goodsInfo.getReturnQuantity() + simpleInfo.getNum());
                                goodsInfos.add(returnOrderGoodsInfo);
                                returnTotalGoodsPrice = CountUtil.add(returnTotalGoodsPrice,
                                        CountUtil.mul(goodsInfo.getReturnPrice(), simpleInfo.getNum()));
                            }
                        }
                    }
                }
            }

            //********************创建退货单退券信息*********************************
            //创建退券对象
            List<ReturnOrderProductCoupon> productCouponList = new ArrayList<>();
            //获取订单使用产品
            List<OrderCouponInfo> orderProductCouponList = productCouponService.findOrderCouponByCouponTypeAndUserId(userId, OrderCouponType.PRODUCT_COUPON);
            if (!orderProductCouponList.isEmpty()) {
                for (OrderCouponInfo couponInfo : orderProductCouponList) {
                    //查询使用产品券信息
                    CustomerProductCoupon customerProductCoupon = productCouponService.findCusProductCouponByCouponId(couponInfo.getCouponId());

                    ReturnOrderProductCoupon productCoupon = new ReturnOrderProductCoupon();
                    productCoupon.setGid(customerProductCoupon.getGoodsId());
                    productCoupon.setIsReturn(Boolean.FALSE);
                    productCoupon.setOrderNo(orderNo);
                    productCoupon.setPcid(customerProductCoupon.getId());
                    productCoupon.setQty(1);
                    productCoupon.setReturnQty(1);
                    productCoupon.setReturnNo(returnNo);
                    productCouponList.add(productCoupon);
                }
            }
            //******************* 创建退货单金额信息 ************************
            List<OrderBillingPaymentDetails> orderPaymentDetails = appOrderService.
                    getOrderBillingDetailByOrderNo(orderNo);
            ReturnOrderBilling returnOrderBilling = new ReturnOrderBilling();
            //退款优先级
            //顾客：现金POS ——> 第三方支付 ——> 预存款 ——> 未提货产品券
            //导购：现金POS ——> 第三方支付 ——> 门店预存款 ——> 未提货产品券
            //装饰经理：第三方支付 ——> 门店预存款 ——> 门店信用金 ——> 门店现金返利

            //取现金支付和预存款
            Double cashPosPrice = 0.00;
            Double onlinePayPrice = 0.00;
            Double tempPrice = 0.00;

            if (!orderPaymentDetails.isEmpty()) {
                for (OrderBillingPaymentDetails paymentDetails : orderPaymentDetails) {
                    if (OrderBillingPaymentType.CASH.equals(paymentDetails.getPayType())) {
                        cashPosPrice = CountUtil.add(cashPosPrice, paymentDetails.getAmount());
                    } else if (OrderBillingPaymentType.POS.equals(paymentDetails.getPayType())) {
                        cashPosPrice = CountUtil.add(cashPosPrice, paymentDetails.getAmount());
                    } else if (OrderBillingPaymentType.ALIPAY.equals(paymentDetails.getPayType())) {
                        onlinePayPrice = CountUtil.add(cashPosPrice, paymentDetails.getAmount());
                    } else if (OrderBillingPaymentType.WE_CHAT.equals(paymentDetails.getPayType())) {
                        onlinePayPrice = CountUtil.add(cashPosPrice, paymentDetails.getAmount());
                    } else if (OrderBillingPaymentType.UNION_PAY.equals(paymentDetails.getPayType())) {
                        onlinePayPrice = CountUtil.add(cashPosPrice, paymentDetails.getAmount());
                    }
                }
            }
            //判断退款是否小于现金支付
            if (returnTotalGoodsPrice <= cashPosPrice) {
                returnOrderBilling.setCash(returnTotalGoodsPrice);
            } else {
                //大于就判断减去再判断第三方支付
                returnOrderBilling.setCash(cashPosPrice);
                tempPrice = CountUtil.sub(returnTotalGoodsPrice, cashPosPrice);
                //如果小于第三方支付
                if (tempPrice <= onlinePayPrice) {
                    returnOrderBilling.setOnlinePay(tempPrice);
                } else {
                    //大于第三方再判断预存款支付
                    returnOrderBilling.setOnlinePay(onlinePayPrice);
                    tempPrice = CountUtil.sub(tempPrice, onlinePayPrice);
                    OrderBillingDetails billingDetails = appOrderService.getOrderBillingDetail(orderNo);
                    if (billingDetails.getCusPreDeposit() != null) {
                        if (identityType == 6) {
                            //小于预存款，顾客结束
                            if (tempPrice <= billingDetails.getCusPreDeposit()) {
                                returnOrderBilling.setPreDeposit(tempPrice);
                            }
                        } else {
                            //导购小于门店预存款
                            if (tempPrice <= billingDetails.getStPreDeposit()) {
                                returnOrderBilling.setPreDeposit(tempPrice);
                            } else {
                                //如果大于再判断导购信用金
                                returnOrderBilling.setPreDeposit(billingDetails.getStPreDeposit());
                                tempPrice = CountUtil.sub(tempPrice, billingDetails.getStPreDeposit());
                                if (identityType == 0) {
                                    //小于导购信用金，导购结束
                                    if (tempPrice <= billingDetails.getEmpCreditMoney()) {
                                        returnOrderBilling.setCreditMoney(tempPrice);
                                    }
                                } else {
                                    //如果大于就判断装饰公司门店信用金
                                    if (tempPrice <= billingDetails.getStoreCreditMoney()) {
                                        returnOrderBilling.setStCreditMoney(tempPrice);
                                    } else {
                                        returnOrderBilling.setStCreditMoney(billingDetails.getStoreCreditMoney());
                                        tempPrice = CountUtil.sub(tempPrice, billingDetails.getStoreCreditMoney());
                                        //如果大于就判断装饰公司门店现金返利,经理结束
                                        if (tempPrice <= billingDetails.getStoreSubvention()) {
                                            returnOrderBilling.setStSubvention(tempPrice);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            returnOrderService.saveReturnOrderRelevantInfo(returnOrderBaseInfo, returnOrderLogisticInfo, goodsInfos, returnOrderBilling,
                    productCouponList, orderGoodsInfoList);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("createOrder OUT,退货单创建成功,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，用户申请退货创建退货单失败", null);
            logger.warn("createReturnOrder EXCEPTION,用户申请退货创建退货单失败，出参 resultDTO:{}", resultDTO);
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
            if (returnOrderDeliveryDetail == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "无物流信息暂不可取消！", null);
                logger.info("cancelReturnOrder OUT,用户取消退货单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (ReturnLogisticStatus.PICKUP_COMPLETE.equals(returnOrderDeliveryDetail.getReturnLogisticStatus())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "已退货完成，不可取消！", null);
                logger.info("cancelReturnOrder OUT,用户取消退货单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            returnOrderBaseInfo.setReturnStatus(AppReturnOrderStatus.CANCELED);
            returnOrderService.saveReturnOrderBaseInfo(returnOrderBaseInfo);

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("cancelReturnOrder OUT,用户取消退货单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;

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
            //产品券可退，返回促销标题，现金券，乐币，不可退
            //创建返回对象集合
            List<ReturnOrderGoodsResponse> returnOrderGoodsList = null;
            //获取订单商品数量促销信息
            List<OrderGoodsInfo> orderGoodsInfoList = appOrderService.getOrderGoodsInfoByOrderNumber(orderNumber);
            if (!orderGoodsInfoList.isEmpty()) {
                returnOrderGoodsList = new ArrayList<>(orderGoodsInfoList.size());
                //将订单商品信息设置到返回对象中
                for (OrderGoodsInfo goodsInfo : orderGoodsInfoList) {
                    if (goodsInfo.getIsReturnable()) {
                        ReturnOrderGoodsResponse returnOrderGoodsResponse = transform(goodsInfo);
                        returnOrderGoodsList.add(returnOrderGoodsResponse);
                    }
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
                            orderLogisticsInfo.setDeliveryType(AppDeliveryType.RETURN_STORE);
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
        orderLogisticsInfo.setDeliveryType(AppDeliveryType.HOUSE_PICK);
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
                defaultDeliveryAddress.getVillageName() +
                defaultDeliveryAddress.getDetailedAddress();
        orderLogisticsInfo.setShippingAddress(shippingAddress);
        return orderLogisticsInfo;
    }

    private ReturnOrderGoodsResponse transform(OrderGoodsInfo goodsInfo) {
        ReturnOrderGoodsResponse returnOrderGoodsResponse = new ReturnOrderGoodsResponse();
        returnOrderGoodsResponse.setSku(goodsInfo.getSku());
        returnOrderGoodsResponse.setSkuName(goodsInfo.getSkuName());
        returnOrderGoodsResponse.setRetailPrice(goodsInfo.getRetailPrice());
        returnOrderGoodsResponse.setReturnPrice(goodsInfo.getReturnPrice());
        returnOrderGoodsResponse.setOrderQuantity(goodsInfo.getOrderQuantity());
        returnOrderGoodsResponse.setReturnableQuantity(goodsInfo.getReturnableQuantity());
        returnOrderGoodsResponse.setPromotionId(goodsInfo.getPromotionId());
        returnOrderGoodsResponse.setReturnPriority(goodsInfo.getReturnPriority());
        //TODO 查促销表的标题

        returnOrderGoodsResponse.setPromotionTitle("查询促销标题");
        returnOrderGoodsResponse.setGoodsLine(goodsInfo.getGoodsLineType().getValue());
        return returnOrderGoodsResponse;
    }

    private ReturnOrderGoodsInfo transform(OrderGoodsInfo goodsInfo, Integer qty, String returnNo) {
        ReturnOrderGoodsInfo returnOrderGoodsInfo = new ReturnOrderGoodsInfo();
        returnOrderGoodsInfo.setGid(goodsInfo.getId());
        returnOrderGoodsInfo.setRetailPrice(goodsInfo.getRetailPrice());
        returnOrderGoodsInfo.setReturnPrice(goodsInfo.getReturnPrice());
        returnOrderGoodsInfo.setReturnQty(qty);
        returnOrderGoodsInfo.setReturnNo(returnNo);
        returnOrderGoodsInfo.setSku(goodsInfo.getSku());
        returnOrderGoodsInfo.setSkuName(goodsInfo.getSkuName());
        returnOrderGoodsInfo.setVipPrice(goodsInfo.getVIPPrice());
        returnOrderGoodsInfo.setWholesalePrice(goodsInfo.getWholesalePrice());
        returnOrderGoodsInfo.setGoodsLineType(goodsInfo.getGoodsLineType());
        return returnOrderGoodsInfo;
    }
}
