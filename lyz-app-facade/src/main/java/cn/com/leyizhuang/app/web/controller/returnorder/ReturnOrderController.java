package cn.com.leyizhuang.app.web.controller.returnorder;

import cn.com.leyizhuang.app.core.bean.GridDataVO;
import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.pay.wechat.refund.OnlinePayRefundService;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.oss.FileUploadOSSUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.CancelOrderParametersDO;
import cn.com.leyizhuang.app.foundation.pojo.SalesConsult;
import cn.com.leyizhuang.app.foundation.pojo.activity.ActBaseDO;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwCancelOrderRequest;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwCancelReturnOrderRequest;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwReturnOrder;
import cn.com.leyizhuang.app.foundation.pojo.request.CustomerSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.GoodsSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.service.impl.SmsAccountServiceImpl;
import cn.com.leyizhuang.app.remote.queue.SinkSender;
import cn.com.leyizhuang.app.remote.webservice.ICallWms;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.core.constant.OperationReasonType;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.AssertUtil;
import cn.com.leyizhuang.common.util.CountUtil;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Jerry.Ren
 * Notes: 退货单接口
 * Created with IntelliJ IDEA.
 * Date: 2017/12/4.
 * Time: 9:34.
 */

@RestController
@RequestMapping("/app/returnOrder")
public class ReturnOrderController {

    private static final Logger logger = LoggerFactory.getLogger(ReturnOrderController.class);

    @Resource
    private AppOrderService appOrderService;
    @Resource
    private AppCustomerService appCustomerService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private ReturnOrderDeliveryDetailsService returnOrderDeliveryDetailsService;
    @Resource
    private OperationReasonsService operationReasonsServiceImpl;
    @Resource
    private DeliveryAddressService deliveryAddressService;
    @Resource
    private AppStoreService appStoreService;
    @Resource
    private AppEmployeeService appEmployeeService;
    @Resource
    private ProductCouponService productCouponService;
    @Resource
    private ReturnOrderService returnOrderService;
    @Resource
    private AppToWmsOrderService appToWmsOrderService;
    @Resource
    private ICallWms callWms;
    @Resource
    private CommonService commonService;
    @Resource
    private CancelOrderParametersService cancelOrderParametersService;
    @Resource
    private SinkSender sinkSender;
    @Resource
    private OnlinePayRefundService onlinePayRefundService;
    @Resource
    private SmsAccountServiceImpl smsAccountService;
    @Resource
    private AppActService appActService;
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
            if (null == orderBaseInfo) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此订单！", null);
                logger.info("cancelOrder OUT,取消订单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == orderBillingDetails) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此订单费用明细，请联系管理员！", null);
                logger.info("cancelOrder OUT,取消订单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (((AppOrderStatus.UNPAID.equals(orderBaseInfo.getStatus()) || AppOrderStatus.PENDING_SHIPMENT.equals(orderBaseInfo.getStatus()) && orderBaseInfo.getDeliveryType() == AppDeliveryType.HOUSE_DELIVERY))
                    || ((AppOrderStatus.PENDING_RECEIVE == orderBaseInfo.getStatus() || AppOrderStatus.UNPAID == orderBaseInfo.getStatus()) &&
                    AppDeliveryType.SELF_TAKE == orderBaseInfo.getDeliveryType())) {
                //判断收货类型和订单状态
                if (orderBaseInfo.getDeliveryStatus().equals(AppDeliveryType.HOUSE_DELIVERY)) {
                    //创建取消订单参数存储类
                    CancelOrderParametersDO cancelOrderParametersDO = new CancelOrderParametersDO();
                    cancelOrderParametersDO.setOrderNumber(orderNumber);
                    cancelOrderParametersDO.setIdentityType(identityType);
                    cancelOrderParametersDO.setUserId(userId);
                    cancelOrderParametersDO.setReasonInfo(reasonInfo);
                    cancelOrderParametersDO.setRemarksInfo(remarksInfo);
                    cancelOrderParametersDO.setCancelStatus(CancelProcessingStatus.SEND_WMS);
                    cancelOrderParametersService.addCancelOrderParameters(cancelOrderParametersDO);

                    // 发送到wms通知WMS
                    AtwCancelOrderRequest atwCancelOrderRequest = AtwCancelOrderRequest.transform(orderNumber, reasonInfo, orderBaseInfo.getStatus());
                    appToWmsOrderService.saveAtwCancelOrderRequest(atwCancelOrderRequest);
                    callWms.sendToWmsCancelOrder(orderNumber);
                    //修改订单状态为取消中
                    orderBaseInfo.setStatus(AppOrderStatus.CANCELING);
                    appOrderService.updateOrderStatusByOrderNo(orderBaseInfo);
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "取消订单提交成功，等待确认！", null);
                    logger.info("canselOrder OUT,取消订单提交成功！，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                //调用取消订单通用方法
                Map<Object, Object> maps = returnOrderService.cancelOrderUniversal(userId, identityType, orderNumber, reasonInfo, remarksInfo, orderBaseInfo, orderBillingDetails);
                //获取退单基础表信息
                ReturnOrderBaseInfo returnOrderBaseInfo = (ReturnOrderBaseInfo) maps.get("returnOrderBaseInfo");
                String code = (String) maps.get("code");
                if ("SUCCESS".equals(code)) {
                    //如果是待收货、门店自提单则需要返回第三方支付金额
                    if (orderBaseInfo.getDeliveryStatus().equals(AppDeliveryType.SELF_TAKE) && orderBaseInfo.getStatus().equals(AppOrderStatus.PENDING_RECEIVE)) {
                        if (null != orderBillingDetails.getOnlinePayType()) {
                            if ("支付宝".equals(orderBillingDetails.getOnlinePayType().getDescription())) {
                                //支付宝退款
                                onlinePayRefundService.alipayRefundRequest(userId, identityType, orderNumber, returnOrderBaseInfo.getReturnNo(), orderBillingDetails.getOnlinePayAmount());

                            } else if ("微信".equals(orderBillingDetails.getOnlinePayType().getDescription())) {
                                //微信退款方法类
                                Map<String, String> map = onlinePayRefundService.wechatReturnMoney(userId, identityType, orderBillingDetails.getOnlinePayAmount(), orderNumber, returnOrderBaseInfo.getReturnNo());
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
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                    logger.info("getReturnOrderList OUT,取消订单成功，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                } else {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "取消订单失败，请联系管理员！", null);
                    logger.info("getReturnOrderList OUT,取消订单失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此订单状态不能取消！", null);
                logger.info("cancelOrder OUT,取消订单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
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
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO<Object> refusedOrder(Long userId, Integer identityType, String orderNumber, String reasonInfo, String remarksInfo, @RequestParam(value = "pictures",
            required = false) MultipartFile[] pictures) {
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
            //*******************处理上传图片***********************
            List<String> pictureUrls = new ArrayList<>();
            String returnPic = null;
            if (pictures != null) {
                for (MultipartFile picture : pictures) {
                    String url = FileUploadOSSUtils.uploadProfilePhoto(picture, "returnOrder/reason/");
                    pictureUrls.add(url);
                }
                returnPic = org.apache.commons.lang.StringUtils.strip(pictureUrls.toString(), "[]");
            }
            //拒签退货记录及返还虚拟货币等
            Map<Object, Object> maps = returnOrderService.refusedOrder(logger, userId, identityType, orderNumber, reasonInfo, remarksInfo, orderBaseInfo, orderBillingDetails, returnPic);

            String code = (String) maps.get("code");
            ReturnOrderBaseInfo returnOrderBaseInfo = null;
            if ("SUCCESS".equals(code)) {
                returnOrderBaseInfo = (ReturnOrderBaseInfo) maps.get("returnOrderBaseInfo");
                //获取退单基础表信息
                //********************************退第三方支付**************************
                //如果是待收货、门店自提单则需要返回第三方支付金额
                if (orderBaseInfo.getDeliveryStatus().equals(AppDeliveryType.SELF_TAKE) && orderBaseInfo.getStatus().equals(AppOrderStatus.PENDING_RECEIVE)) {
                    if ("支付宝".equals(orderBillingDetails.getOnlinePayType().getDescription())) {
                        //支付宝退款
                        onlinePayRefundService.alipayRefundRequest(userId, identityType, orderNumber, returnOrderBaseInfo.getReturnNo(), orderBillingDetails.getOnlinePayAmount());

                    } else if ("微信".equals(orderBillingDetails.getOnlinePayType().getDescription())) {
                        //微信退款方法类
                        onlinePayRefundService.wechatReturnMoney(userId, identityType, orderBillingDetails.getOnlinePayAmount(), orderNumber, returnOrderBaseInfo.getReturnNo());
                    } else if ("银联".equals(orderBillingDetails.getOnlinePayType().getDescription())) {
                        //创建退单退款详情实体
                        ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                        returnOrderBillingDetail.setCreateTime(new Date());
                        returnOrderBillingDetail.setRoid(returnOrderBaseInfo.getRoid());
                        returnOrderBillingDetail.setRefundNumber(null);
                        //TODO 时间待定
                        returnOrderBillingDetail.setIntoAmountTime(new Date());
                        //TODO 第三方回复码
                        returnOrderBillingDetail.setReplyCode("");
                        returnOrderBillingDetail.setReturnMoney(orderBillingDetails.getOnlinePayAmount());
                        returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.UNION_PAY);
                    }
                }
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常，拒签退货失败，请联系管理员！", null);
                logger.info("refusedOrder OUT,拒签退货失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            //发送拆单消息到消息队列
            sinkSender.sendReturnOrder(returnOrderBaseInfo.getReturnNo());
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
        if (null == param.getReturnGoodsInfo() || param.getReturnGoodsInfo().isEmpty()) {
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
            OrderBaseInfo order = appOrderService.getOrderByOrderNumber(orderNo);
            //不是已完成订单不可申请退货
            if (!AppOrderStatus.FINISHED.equals(order.getStatus())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此订单状态不可退货!", "");
                logger.warn("createReturnOrder OUT,用户申请退货创建退货单失败,出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            //*******************处理上传图骗***********************
            List<String> pictureUrls = new ArrayList<>();
            String returnPic = null;
            if (AssertUtil.isNotEmpty(pictures)) {
                for (MultipartFile picture : pictures) {
                    String url = FileUploadOSSUtils.uploadProfilePhoto(picture, "returnOrder/reason/");
                    pictureUrls.add(url);
                }
                returnPic = org.apache.commons.lang.StringUtils.strip(pictureUrls.toString(), "[]");
            }
            //********************创建退货单基础信息******************
            //记录原订单信息

            ReturnOrderBaseInfo returnOrderBaseInfo = returnOrderService.createReturnOrderBaseInfo(order.getId(), order.getOrderNumber(),
                    order.getCreateTime(), param.getRemarksInfo(), userId, identityType, param.getReasonInfo(), returnPic, order.getOrderType(),
                    order.getStoreId(), order.getStoreCode(), order.getStoreStructureCode());
            if (identityType == 6) {
                AppCustomer customer = appCustomerService.findById(param.getUserId());
                returnOrderBaseInfo.setCustomerId(customer.getCusId());
                returnOrderBaseInfo.setCustomerName(customer.getName());
                returnOrderBaseInfo.setCustomerPhone(customer.getMobile());
                returnOrderBaseInfo.setCustomerType(customer.getCustomerType());
                returnOrderBaseInfo.setCreatorName(customer.getName());
                returnOrderBaseInfo.setCreatorPhone(customer.getMobile());
            } else {
                AppEmployee employee = appEmployeeService.findById(userId);
                returnOrderBaseInfo.setCreatorName(employee.getName());
                returnOrderBaseInfo.setCreatorPhone(employee.getMobile());
                if (identityType == 0) {
                    AppCustomer customer = appCustomerService.findById(param.getCusId());
                    if (AssertUtil.isNotEmpty(customer)) {
                        returnOrderBaseInfo.setCustomerId(customer.getCusId());
                        returnOrderBaseInfo.setCustomerName(customer.getName());
                        returnOrderBaseInfo.setCustomerPhone(customer.getMobile());
                        returnOrderBaseInfo.setCustomerType(customer.getCustomerType());
                    }
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

            if (AssertUtil.isNotEmpty(orderGoodsInfoList)) {
                for (GoodsSimpleInfo simpleInfo : simpleInfos) {
                    for (OrderGoodsInfo goodsInfo : orderGoodsInfoList) {
                        if (goodsInfo.getId().equals(simpleInfo.getId())) {
                            if (simpleInfo.getGoodsLineType().equals(goodsInfo.getGoodsLineType().getValue())) {
                                if (simpleInfo.getQty() > goodsInfo.getReturnableQuantity()) {
                                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "退货数量不可大于可退数量!", "");
                                    logger.warn("createReturnOrder OUT,用户申请退货创建退货单失败,出参 resultDTO:{}", resultDTO);
                                    return resultDTO;
                                }
                                ReturnOrderGoodsInfo returnOrderGoodsInfo = transform(goodsInfo, simpleInfo.getQty(), returnNo);
                                //设置原订单可退数量 减少
                                goodsInfo.setReturnableQuantity(goodsInfo.getReturnableQuantity() - simpleInfo.getQty());
                                //设置原订单退货数量 增加
                                goodsInfo.setReturnQuantity(goodsInfo.getReturnQuantity() + simpleInfo.getQty());
                                goodsInfos.add(returnOrderGoodsInfo);
                                returnTotalGoodsPrice = CountUtil.add(returnTotalGoodsPrice,
                                        CountUtil.mul(goodsInfo.getReturnPrice(), simpleInfo.getQty()));
                                break;
                            }
                        }
                    }
                }
                //设置退货商品总金额
                returnOrderBaseInfo.setReturnPrice(returnTotalGoodsPrice);
            }

            //********************创建退货单退券信息*********************************
            //创建退券对象
            List<ReturnOrderProductCoupon> productCouponList = new ArrayList<>();
            //获取订单使用产品
            List<OrderCouponInfo> orderProductCouponList = productCouponService.findOrderCouponByCouponTypeAndOrderId(order.getId(), OrderCouponType.PRODUCT_COUPON);
            if (AssertUtil.isNotEmpty(orderProductCouponList)) {
                for (OrderCouponInfo couponInfo : orderProductCouponList) {
                    for (ReturnOrderGoodsInfo goodsInfo : goodsInfos) {
                        if (AppGoodsLineType.PRODUCT_COUPON.equals(goodsInfo.getGoodsLineType()) &&
                                goodsInfo.getSku().equals(couponInfo.getSku())) {

                            ReturnOrderProductCoupon productCoupon = new ReturnOrderProductCoupon();
                            productCoupon.setGid(goodsInfo.getGid());
                            productCoupon.setIsReturn(Boolean.FALSE);
                            productCoupon.setOrderNo(orderNo);
                            productCoupon.setPcid(couponInfo.getCouponId());
                            productCoupon.setQty(1);
                            productCoupon.setSku(goodsInfo.getSku());
                            productCoupon.setReturnQty(1);
                            productCoupon.setReturnNo(returnNo);
                            productCouponList.add(productCoupon);
                        }
                    }
                }
            }
            //******************* 创建退货单金额信息 ************************
            List<OrderBillingPaymentDetails> orderPaymentDetails = appOrderService.
                    getOrderBillingDetailListByOrderNo(orderNo);
            ReturnOrderBilling returnOrderBilling = new ReturnOrderBilling();
            //退款优先级
            //顾客：现金POS ——> 第三方支付 ——> 预存款 ——> 未提货产品券
            //导购：现金POS ——> 第三方支付 ——> 门店预存款 ——> 未提货产品券
            //装饰经理：第三方支付 ——> 门店预存款 ——> 门店信用金 ——> 门店现金返利

            //取现金支付和预存款
            Double cashPosPrice = 0.00;
            Double onlinePayPrice = 0.00;
            Double tempPrice = 0.00;

            if (AssertUtil.isNotEmpty(orderPaymentDetails)) {
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
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单缺少账单支付信息!", "");
                logger.warn("createReturnOrder OUT,用户申请退货创建退货单失败,出参 resultDTO:{}", resultDTO);
                return resultDTO;
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
                    if (null != billingDetails && billingDetails.getCusPreDeposit() != null) {
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
            //保存发送wms退货单头
            AppStore appStore = appStoreService.findStoreByUserIdAndIdentityType(userId, identityType);
            SalesConsult salesConsult = appEmployeeService.findSellerByUserIdAndIdentityType(userId, identityType);
            AtwReturnOrder atwReturnOrder = AtwReturnOrder.transform(returnOrderBaseInfo, returnOrderLogisticInfo, appStore, order, goodsInfos.size(), salesConsult);
            appToWmsOrderService.saveAtwReturnOrder(atwReturnOrder);
            //发送退货单到wms
            callWms.sendToWmsReturnOrderAndGoods(returnNo);
            //发送退单拆单消息到拆单消息队列
            sinkSender.sendReturnOrder(returnOrderBaseInfo.getReturnNo());
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
    public ResultDTO getReturnOrderList(Long userId, Integer identityType, Integer page, Integer size) {

        logger.info("getReturnOrderList CALLED,获取用户退货单列表，入参 userID:{}, identityType:{}, showStatus{},page:{},size:{}", userId, identityType, page, size);

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
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getReturnOrderList OUT,获取用户退货单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getReturnOrderList OUT,获取用户退货单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            //查询所有退单表
            PageInfo<ReturnOrderBaseInfo> baseInfo = returnOrderService.findReturnOrderListByUserIdAndIdentityType(userId, identityType, page, size);
            List<ReturnOrderBaseInfo> baseInfoList = baseInfo.getList();
            //创建一个返回对象list
            List<ReturnOrderListResponse> returnOrderListResponses = new ArrayList<>();

            for (ReturnOrderBaseInfo returnBaseInfo : baseInfoList) {
                //创建有个存放图片地址的list
                List<String> goodsImgList = new ArrayList<>();
                //创建一个返回类（借用订单返回对象）
                ReturnOrderListResponse response = new ReturnOrderListResponse();
                //获取订单商品
                List<ReturnOrderGoodsInfo> returnGoodsInfoList = returnOrderService.findReturnOrderGoodsInfoByOrderNumber(returnBaseInfo.getReturnNo());
                //遍历订单商品
                int count = 0;
                for (ReturnOrderGoodsInfo returnGoodsInfo : returnGoodsInfoList) {
                    goodsImgList.add(goodsService.queryBySku(returnGoodsInfo.getSku()).getCoverImageUri());
                    count = count + returnGoodsInfo.getReturnQty();
                }
                response.setReturnNo(returnBaseInfo.getReturnNo());
                response.setStatus(returnBaseInfo.getReturnStatus().toString());
                response.setStatusDesc(returnBaseInfo.getReturnStatus().getDescription());
                response.setCount(count);
                response.setReturnPrice(returnBaseInfo.getReturnPrice());
                response.setReturnType(returnBaseInfo.getReturnType().getDescription());
                response.setGoodsImgList(goodsImgList);
                if (identityType == 0) {
                    CustomerSimpleInfo customer = new CustomerSimpleInfo();
                    customer.setCustomerId(returnBaseInfo.getCustomerId());
                    customer.setCustomerName(returnBaseInfo.getCustomerName());
                    customer.setCustomerPhone(returnBaseInfo.getCustomerPhone());
                    response.setCustomer(customer);
                }
                //添加到返回类list中
                returnOrderListResponses.add(response);

            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    new GridDataVO<ReturnOrderListResponse>().transform(returnOrderListResponses, baseInfo));
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
            if (null == returnBaseInfo) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查到该订单！", null);
                logger.info("getReturnOrderDetail OUT,查看退货单详情失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            ReturnOrderDetailResponse detailResponse = null;

            //获取原订单收货/自提门店地址
            ReturnOrderLogisticInfo returnOrderLogisticInfo = returnOrderService.getReturnOrderLogisticeInfo(returnNumber);
//            if (null == returnOrderLogisticInfo) {
//                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单缺少收货物流信息！", null);
//                logger.info("getReturnOrderDetail OUT,查看退货单详情失败，出参 resultDTO:{}", resultDTO);
//                return resultDTO;
//            }
            detailResponse = new ReturnOrderDetailResponse();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //设置基础信息
            detailResponse.setReturnNumber(returnBaseInfo.getReturnNo());
            detailResponse.setReturnTime(sdf.format(returnBaseInfo.getReturnTime()));
            detailResponse.setTotalReturnPrice(returnBaseInfo.getReturnPrice());
            detailResponse.setReasonInfo(returnBaseInfo.getReasonInfo());
            detailResponse.setReturnStatus(null != returnBaseInfo.getReturnStatus() ? returnBaseInfo.getReturnStatus().getDescription() : null);
            detailResponse.setReturnType(null != returnBaseInfo.getReturnType() ? returnBaseInfo.getReturnType().getDescription() : null);
            //取货方式（上门取货，送货到店）
            if (AssertUtil.isNotEmpty(returnOrderLogisticInfo)) {
                detailResponse.setDeliveryType(null != returnOrderLogisticInfo.getDeliveryType() ? returnOrderLogisticInfo.getDeliveryType().getValue() : null);
                if (AppDeliveryType.RETURN_STORE.equals(returnOrderLogisticInfo.getDeliveryType())) {
                    detailResponse.setBookingStoreName(returnOrderLogisticInfo.getReturnStoreName());
                    AppStore appStore = appStoreService.findByStoreCode(returnOrderLogisticInfo.getReturnStoreCode());
                    if (appStore != null) {
                        detailResponse.setStoreDetailedAddress(appStore.getDetailedAddress());
                        detailResponse.setBookingStorePhone(appStore.getPhone());
                    }
                } else {
                    detailResponse.setDeliveryTime(returnOrderLogisticInfo.getDeliveryTime());
                    detailResponse.setReceiver(returnOrderLogisticInfo.getRejecter());
                    detailResponse.setReceiverPhone(returnOrderLogisticInfo.getRejecterPhone());
                    detailResponse.setShippingAddress(returnOrderLogisticInfo.getReturnFullAddress());
                }
            }
            detailResponse.setGoodsList(returnOrderService.getReturnOrderGoodsDetails(returnNumber));
            int count = 0;
            Double totalReturnPrice = 0.00;
            //获取订单商品
            List<ReturnOrderGoodsInfo> returnGoodsInfoList = returnOrderService.findReturnOrderGoodsInfoByOrderNumber(returnNumber);
            //遍历订单商品，算出总商品数量和退货商品总价
            for (ReturnOrderGoodsInfo returnGoodsInfo : returnGoodsInfoList) {
                count = count + returnGoodsInfo.getReturnQty();
                totalReturnPrice = CountUtil.add(totalReturnPrice, CountUtil.mul(returnGoodsInfo.getReturnQty(), returnGoodsInfo.getReturnPrice()));
            }
            detailResponse.setReturnQty(count);
            detailResponse.setTotalReturnPrice(totalReturnPrice);

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
            List<ReturnOrderDeliveryDetail> returnOrderDeliveryDetail = returnOrderDeliveryDetailsService.queryListByReturnOrderNumber(returnNumber);
            if (AssertUtil.isEmpty(returnOrderDeliveryDetail)) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "无物流信息暂不可取消！", null);
                logger.info("cancelReturnOrder OUT,用户取消退货单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            for (ReturnOrderDeliveryDetail orderDeliveryDetail : returnOrderDeliveryDetail) {
                if (ReturnLogisticStatus.PICKUP_COMPLETE.equals(orderDeliveryDetail.getReturnLogisticStatus())) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "已退货完成，不可取消！", null);
                    logger.info("cancelReturnOrder OUT,用户取消退货单失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }
            AtwCancelReturnOrderRequest atwCancelOrderRequest = AtwCancelReturnOrderRequest.transform(returnOrderBaseInfo);
            appToWmsOrderService.saveAtwCancelReturnOrderRequest(atwCancelOrderRequest);
            //发送取消退货单到WMS
            callWms.sendToWmsCancelReturnOrder(returnNumber);
            returnOrderService.updateReturnOrderStatus(returnNumber, AppReturnOrderStatus.CANCELING);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("cancelReturnOrder OUT,用户取消退货单成功,等待wms返回取消结果，出参 resultDTO:{}", resultDTO);
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
            if (null != orderGoodsInfoList && !orderGoodsInfoList.isEmpty()) {
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
                if (null != giftListResponseGoods && !giftListResponseGoods.isEmpty()) {
                    for (ReturnOrderGoodsResponse returnOrderGoodsResponse : returnOrderGoodsList) {
                        for (GiftListResponseGoods giftListResponseGood : giftListResponseGoods) {
                            if (giftListResponseGood.getGoodsId().equals(returnOrderGoodsResponse.getGoodsId())) {
                                returnOrderGoodsResponse.setCoverImageUri(giftListResponseGood.getCoverImageUri());
                                break;
                            }
                        }
                    }
                }
                //按照退货优先级排个序
                returnOrderGoodsList.sort((o1, o2) -> o2.getReturnPriority().compareTo(o1.getReturnPriority()));
            }

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    AssertUtil.isNotEmpty(returnOrderGoodsList) ? returnOrderGoodsList : null);
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
            if (AssertUtil.isNotEmpty(orderLogisticsInfo)) {
                if (AppDeliveryType.SELF_TAKE.equals(orderLogisticsInfo.getDeliveryType())) {
                    AppIdentityType identityType1 = AppIdentityType.getAppIdentityTypeByValue(identityType);
                    DeliveryAddressResponse defaultDeliveryAddress = deliveryAddressService.getDefaultDeliveryAddressByUserIdAndIdentityType(userId, identityType1);
                    if (null == defaultDeliveryAddress) {
                        defaultDeliveryAddress = deliveryAddressService.getTopDeliveryAddressByUserIdAndIdentityType
                                (userId, AppIdentityType.getAppIdentityTypeByValue(identityType));
                    }
                    orderLogisticsInfo = transform(orderLogisticsInfo, defaultDeliveryAddress);
                    //如果是送货上门，退货到门店的地址就是订单门店地址
                } else if (AppDeliveryType.HOUSE_DELIVERY.equals(orderLogisticsInfo.getDeliveryType())) {
                    OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
                    //下订单的id 是否和当前顾客的ID一致
                    if (null != orderBaseInfo) {
                        AppStore store = appStoreService.findStoreByUserIdAndIdentityType(userId, identityType);
                        if (store != null) {
                            orderLogisticsInfo.setDeliveryType(AppDeliveryType.HOUSE_PICK);
                            orderLogisticsInfo.setBookingStoreCode(store.getStoreCode());
                            orderLogisticsInfo.setBookingStoreName(store.getStoreName());
                            orderLogisticsInfo.setBookingStoreAddress(store.getDetailedAddress());
                        } else {
                            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "当前用户未查询到门店信息！", null);
                            logger.info("getReturnOrderDeliveryType OUT,获取用户退货方式和取货地址失败，出参 resultDTO:{}", resultDTO);
                            return resultDTO;
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
        orderLogisticsInfo.setDeliveryType(AppDeliveryType.RETURN_STORE);
        orderLogisticsInfo.setReceiver(defaultDeliveryAddress.getDeliveryName());
        orderLogisticsInfo.setReceiverPhone(defaultDeliveryAddress.getDeliveryPhone());
        orderLogisticsInfo.setDeliveryCity(defaultDeliveryAddress.getDeliveryCity());
        orderLogisticsInfo.setDeliveryCounty(defaultDeliveryAddress.getDeliveryCounty());
        orderLogisticsInfo.setDeliveryStreet(defaultDeliveryAddress.getDeliveryStreet());
        orderLogisticsInfo.setDetailedAddress(defaultDeliveryAddress.getDetailedAddress());
        orderLogisticsInfo.setResidenceName(defaultDeliveryAddress.getVillageName());
        String shippingAddress = defaultDeliveryAddress.getDeliveryCity().trim() +
                defaultDeliveryAddress.getDeliveryCounty().trim() +
                defaultDeliveryAddress.getDeliveryStreet().trim() +
                defaultDeliveryAddress.getVillageName().trim() +
                defaultDeliveryAddress.getDetailedAddress().trim();
        orderLogisticsInfo.setShippingAddress(shippingAddress);
        return orderLogisticsInfo;
    }

    private ReturnOrderGoodsResponse transform(OrderGoodsInfo goodsInfo) {
        ReturnOrderGoodsResponse returnOrderGoodsResponse = new ReturnOrderGoodsResponse();
        returnOrderGoodsResponse.setId(goodsInfo.getId());
        returnOrderGoodsResponse.setGoodsId(goodsInfo.getGid());
        returnOrderGoodsResponse.setSku(goodsInfo.getSku());
        returnOrderGoodsResponse.setSkuName(goodsInfo.getSkuName());
        returnOrderGoodsResponse.setRetailPrice(goodsInfo.getRetailPrice());
        returnOrderGoodsResponse.setReturnPrice(goodsInfo.getReturnPrice());
        returnOrderGoodsResponse.setOrderQuantity(goodsInfo.getOrderQuantity());
        returnOrderGoodsResponse.setReturnableQuantity(goodsInfo.getReturnableQuantity());
        returnOrderGoodsResponse.setPromotionId(goodsInfo.getPromotionId());
        returnOrderGoodsResponse.setReturnPriority(goodsInfo.getReturnPriority());
        returnOrderGoodsResponse.setGoodsLine(goodsInfo.getGoodsLineType().getValue());

        ActBaseDO actBaseDO = appActService.findById(Long.parseLong(goodsInfo.getPromotionId()));
        returnOrderGoodsResponse.setPromotionTitle(null != actBaseDO ? actBaseDO.getTitle() : "无促销");
        return returnOrderGoodsResponse;
    }

    private ReturnOrderGoodsInfo transform(OrderGoodsInfo goodsInfo, Integer qty, String returnNo) {
        ReturnOrderGoodsInfo returnOrderGoodsInfo = new ReturnOrderGoodsInfo();
        returnOrderGoodsInfo.setOrderGoodsId(goodsInfo.getId());
        returnOrderGoodsInfo.setGid(goodsInfo.getGid());
        returnOrderGoodsInfo.setRetailPrice(goodsInfo.getRetailPrice());
        returnOrderGoodsInfo.setReturnPrice(goodsInfo.getReturnPrice());
        returnOrderGoodsInfo.setReturnQty(qty);
        returnOrderGoodsInfo.setReturnNo(returnNo);
        returnOrderGoodsInfo.setSku(goodsInfo.getSku());
        returnOrderGoodsInfo.setSkuName(goodsInfo.getSkuName());
        returnOrderGoodsInfo.setVipPrice(goodsInfo.getVIPPrice());
        returnOrderGoodsInfo.setWholesalePrice(goodsInfo.getWholesalePrice());
        returnOrderGoodsInfo.setGoodsLineType(goodsInfo.getGoodsLineType());
        returnOrderGoodsInfo.setSettlementPrice(goodsInfo.getSettlementPrice());
        returnOrderGoodsInfo.setCompanyFlag(goodsInfo.getCompanyFlag());
        return returnOrderGoodsInfo;
    }


}
