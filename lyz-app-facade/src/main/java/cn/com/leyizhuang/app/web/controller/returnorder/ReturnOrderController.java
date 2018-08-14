package cn.com.leyizhuang.app.web.controller.returnorder;

import cn.com.leyizhuang.app.core.bean.GridDataVO;
import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.lock.RedisLock;
import cn.com.leyizhuang.app.core.pay.wechat.refund.OnlinePayRefundService;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.oss.FileUploadOSSUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.CancelOrderParametersDO;
import cn.com.leyizhuang.app.foundation.pojo.CustomerProductCoupon;
import cn.com.leyizhuang.app.foundation.pojo.SalesConsult;
import cn.com.leyizhuang.app.foundation.pojo.activity.ActBaseDO;
import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwCancelOrderRequest;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwCancelReturnOrderRequest;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwRequisitionOrderGoods;
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
import org.apache.commons.collections.map.HashedMap;
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
    @Resource
    private CityService cityService;

    @Resource
    private RedisLock redisLock;

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
            if (redisLock.lock(AppLock.CANCEL_ORDER, orderNumber, 30)) {
            //获取订单头信息
                OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
                String orderStatus = orderBaseInfo.getStatus().getValue();
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
                Double storePos = orderBillingDetails.getStorePosMoney() == null ? 0 : orderBillingDetails.getStorePosMoney();
                Double storeOther = orderBillingDetails.getStoreOtherMoney() == null ? 0 : orderBillingDetails.getStoreOtherMoney();
                Double storeCash = orderBillingDetails.getStoreCash() == null ? 0 : orderBillingDetails.getStoreCash();
                Double storeTotalMoney = CountUtil.add(storeCash, storeOther, storePos);
                if (orderBaseInfo.getDeliveryType().equals(AppDeliveryType.SELF_TAKE) && storeTotalMoney > 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此订单已收款，无法取消！", null);
                    logger.info("cancelOrder OUT,取消订单失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }

            /*2018-04-08 generation 取消订单判断状态为待支付、配送代发货，自提待取货*/
                if (AppOrderStatus.UNPAID.equals(orderBaseInfo.getStatus())
                        || (orderBaseInfo.getDeliveryType().equals(AppDeliveryType.HOUSE_DELIVERY) && AppOrderStatus.PENDING_SHIPMENT.equals(orderBaseInfo.getStatus()))
                        || (orderBaseInfo.getDeliveryType().equals(AppDeliveryType.SELF_TAKE) && AppOrderStatus.PENDING_RECEIVE.equals(orderBaseInfo.getStatus()))) {
                    //判断收货类型和订单状态
                    if (orderBaseInfo.getDeliveryType().equals(AppDeliveryType.HOUSE_DELIVERY) && AppOrderStatus.PENDING_SHIPMENT.equals(orderBaseInfo.getStatus())) {
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
                        AtwCancelOrderRequest atwCancelOrderRequest = AtwCancelOrderRequest.transform(reasonInfo, orderBaseInfo);
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
                    Date date = new Date();
                    if ("SUCCESS".equals(code)) {
                        //如果是待收货、门店自提单则需要返回第三方支付金额
                        if (orderBaseInfo.getDeliveryType().equals(AppDeliveryType.SELF_TAKE)) {
                            if (null != orderBillingDetails.getOnlinePayType()) {
                                if (OnlinePayType.ALIPAY.equals(orderBillingDetails.getOnlinePayType())) {
                                    //支付宝退款
                                    Map<String, String> map = onlinePayRefundService.alipayRefundRequest(userId, identityType, orderNumber, returnOrderBaseInfo.getReturnNo(), orderBillingDetails.getOnlinePayAmount(), returnOrderBaseInfo.getRoid());
                                    if ("FAILURE".equals(map.get("code"))) {
                                        returnOrderService.updateReturnOrderBaseInfoByReturnNo(returnOrderBaseInfo.getReturnNo(), AppReturnOrderStatus.PENDING_REFUND);
                                    }
                                } else if (OnlinePayType.WE_CHAT.equals(orderBillingDetails.getOnlinePayType())) {
                                    //微信退款方法类
                                    Map<String, String> map = onlinePayRefundService.wechatReturnMoney(userId, identityType, orderBillingDetails.getOnlinePayAmount(), orderNumber, returnOrderBaseInfo.getReturnNo(), returnOrderBaseInfo.getRoid());
                                    if ("FAILURE".equals(map.get("code"))) {
                                        returnOrderService.updateReturnOrderBaseInfoByReturnNo(returnOrderBaseInfo.getReturnNo(), AppReturnOrderStatus.PENDING_REFUND);
                                    }
                                } else if (OnlinePayType.UNION_PAY.equals(orderBillingDetails.getOnlinePayType())) {
                                    //银联支付退款
                                    Map<String, String> map = onlinePayRefundService.unionPayReturnMoney(userId, identityType, orderBillingDetails.getOnlinePayAmount(), orderNumber, returnOrderBaseInfo.getReturnNo(),
                                            returnOrderBaseInfo.getRoid());
                                    if ("FAILURE".equals(map.get("code"))) {
                                        returnOrderService.updateReturnOrderBaseInfoByReturnNo(returnOrderBaseInfo.getReturnNo(), AppReturnOrderStatus.PENDING_REFUND);
                                    }
                                }
                            }
                        }

                        //如果是待发货的门店自提单发送退单拆单消息到拆单消息队列
                    /*2018-04-08 generation 改为待收货的自提单*/
//                    if (orderBaseInfo.getStatus().equals(AppOrderStatus.PENDING_SHIPMENT) && orderBaseInfo.getDeliveryType().equals(AppDeliveryType.SELF_TAKE)){
                        if (orderStatus.equals(AppOrderStatus.PENDING_RECEIVE.getValue()) && orderBaseInfo.getDeliveryType().equals(AppDeliveryType.SELF_TAKE)) {
                            sinkSender.sendReturnOrder(returnOrderBaseInfo.getReturnNo());
                        }
                        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                        logger.info("cancelOrder OUT,取消订单成功，出参 resultDTO:{}", resultDTO);
                        return resultDTO;
                    }else if ("repeat".equals(code)) {
                        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此单已取消，不能重复取消，请联系管理员！", null);
                        logger.info("refusedOrder OUT,取消订单失败，出参 resultDTO:{}", resultDTO);
                        return resultDTO;
                    }else {
                        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "取消订单失败，请联系管理员！", null);
                        logger.info("cancelOrder OUT,取消订单失败，出参 resultDTO:{}", resultDTO);
                        return resultDTO;
                    }
                } else {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此订单状态不能取消！", null);
                    logger.info("cancelOrder OUT,取消订单失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "取消订单正在处理中，请稍候!", null);
                logger.warn("cancelOrder OUT,取消订单重复提交，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，取消订单失败", null);
            logger.warn("cancelOrder EXCEPTION,取消订单失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }finally {
            redisLock.unlock(AppLock.CANCEL_ORDER, orderNumber);
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
        if (AppIdentityType.DELIVERY_CLERK.getValue() != identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此身份类型不能进行此操作", null);
            logger.info("refusedOrder OUT,拒签退货失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            if (redisLock.lock(AppLock.REFUSE_ORDER, orderNumber, 30)) {
                //获取订单头信息
                OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
                if (AppOrderStatus.REJECTED == orderBaseInfo.getStatus()){
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此订单已拒签, 不能重复拒签！", null);
                    logger.info("refusedOrder OUT,此订单已拒签, 拒签退货失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                ReturnOrderBaseInfo returnOrderBaseInfo2 = returnOrderService.queryByOrdNo(orderNumber);
                if (null != returnOrderBaseInfo2){
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此订单已拒签, 不能重复拒签！", null);
                    logger.info("refusedOrder OUT,此订单已拒签, 拒签退货失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
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
                //拒签退货记录
                Map<Object, Object> maps = returnOrderService.saveRefusedOrder(userId, identityType, orderNumber, reasonInfo, remarksInfo, orderBaseInfo, orderBillingDetails, returnPic);
                String code = (String) maps.get("code");
                ReturnOrderBaseInfo returnOrderBaseInfo = null;
                if ("SUCCESS".equals(code)) {
                    returnOrderBaseInfo = (ReturnOrderBaseInfo) maps.get("returnOrderBaseInfo");
                    List<ReturnOrderGoodsInfo> returnOrderGoodsInfos = (List<ReturnOrderGoodsInfo>) maps.get("returnOrderGoodsInfos");

                    //发送拆单消息到消息队列
//                sinkSender.sendReturnOrder(returnOrderBaseInfo.getReturnNo());

                    AppEmployee employee = appEmployeeService.findById(userId);
                    Date date = new Date();
                    //保存发送wms退货单头
                    OrderLogisticsInfo orderLogisticsInfo = appOrderService.getOrderLogistice(orderNumber);
                    AtwReturnOrder atwReturnOrder = new AtwReturnOrder();
                    atwReturnOrder.setCreateTime(date);
                    atwReturnOrder.setDiySiteAddress(null);
                    atwReturnOrder.setDiySiteId(null);
                    atwReturnOrder.setDiySiteTitle(null);
                    atwReturnOrder.setDiySiteTel(null);
                    atwReturnOrder.setRemarkInfo("拒签退货: " + returnOrderBaseInfo.getReasonInfo());
                    atwReturnOrder.setOrderNumber(orderNumber);
                    atwReturnOrder.setReturnNumber(returnOrderBaseInfo.getReturnNo());
                    atwReturnOrder.setReturnTime(date);
                    atwReturnOrder.setStatusId(returnOrderBaseInfo.getReturnStatus().getValue());
                    atwReturnOrder.setDeliverTypeTitle(AppDeliveryType.HOUSE_DELIVERY.getDescription());
                    atwReturnOrder.setReturnPrice(returnOrderBaseInfo.getReturnPrice());
                    atwReturnOrder.setShoppingAddress(orderLogisticsInfo.getShippingAddress());
                    atwReturnOrder.setSellerRealName(orderBaseInfo.getSalesConsultName());
                    atwReturnOrder.setGoodsLineQuantity(returnOrderGoodsInfos.size());
                    atwReturnOrder.setCreator(returnOrderBaseInfo.getCreatorName());
                    atwReturnOrder.setCreatorPhone(returnOrderBaseInfo.getCreatorPhone());
                    atwReturnOrder.setRejecter(orderLogisticsInfo.getReceiver());
                    atwReturnOrder.setRejecterPhone(orderLogisticsInfo.getReceiverPhone());
                    atwReturnOrder.setRejecterAddress(orderLogisticsInfo.getDetailedAddress());
                    atwReturnOrder.setDeliveryClerkNo(employee.getDeliveryClerkNo());
                    atwReturnOrder.setSendFlag(null);
                    atwReturnOrder.setSendTime(null);
                    appToWmsOrderService.saveAtwReturnOrder(atwReturnOrder);
                    //保存发送WMS退货商品详情
                    if (null != returnOrderGoodsInfos && returnOrderGoodsInfos.size() > 0) {
                        for (ReturnOrderGoodsInfo goodsInfo : returnOrderGoodsInfos) {
                            AtwRequisitionOrderGoods orderGoods = null;
                            if ("ZS-002".equals(orderBaseInfo.getStoreCode()) || "MR004".equals(orderBaseInfo.getStoreCode())) {
                                orderGoods = AtwRequisitionOrderGoods.transform(returnOrderBaseInfo.getReturnNo(), goodsInfo.getSku(),
                                        goodsInfo.getSkuName(), goodsInfo.getSettlementPrice(), goodsInfo.getReturnQty(), goodsInfo.getCompanyFlag());
                            } else {
                                orderGoods = AtwRequisitionOrderGoods.transform(returnOrderBaseInfo.getReturnNo(), goodsInfo.getSku(),
                                        goodsInfo.getSkuName(), goodsInfo.getRetailPrice(), goodsInfo.getReturnQty(), goodsInfo.getCompanyFlag());
                            }
                            appToWmsOrderService.saveAtwRequisitionOrderGoods(orderGoods);
                        }
                    }
                    //发送退货单到wms
                    callWms.sendToWmsReturnOrderAndGoods(returnOrderBaseInfo.getReturnNo());
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                    logger.info("refusedOrder OUT,拒签退货成功，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                } else if ("repeat".equals(code)){
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此单已拒签，不能重复拒签，请联系管理员！", null);
                    logger.info("refusedOrder OUT,拒签退货失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }  else {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常，拒签退货失败，请联系管理员！", null);
                    logger.info("refusedOrder OUT,拒签退货失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "拒签退货正在处理中，请稍候!", null);
                logger.warn("refusedOrder OUT,拒签退货重复提交，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，拒签退货失败", null);
            logger.warn("refusedOrder EXCEPTION,拒签退货失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        } finally {
            redisLock.unlock(AppLock.REFUSE_ORDER, orderNumber);
        }
    }

    /**
     * 用户申请退货创建退货单
     * @param param   创建退货单参数
     *
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
            if (redisLock.lock(AppLock.NORMAL_RETURN, param.getOrderNo(), 30)) {
            Long userId = param.getUserId();
            Integer identityType = param.getIdentityType();
            String orderNo = param.getOrderNo();
            OrderBaseInfo order = appOrderService.getOrderByOrderNumber(orderNo);
            OrderBillingDetails orderBillingDetails = appOrderService.getOrderBillingDetail(orderNo);
            //不是已完成订单不可申请退货
            if (!AppOrderStatus.FINISHED.equals(order.getStatus())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此订单状态不可退货!", "");
                logger.warn("createReturnOrder OUT,用户申请退货创建退货单失败,出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if ((!orderBillingDetails.getIsPayUp() && AppOrderSubjectType.FIT != order.getOrderSubjectType())
                    || (orderBillingDetails.getArrearage() > 0D && AppOrderSubjectType.FIT == order.getOrderSubjectType())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此订单还未付清欠款不可退货!", "");
                logger.warn("createReturnOrder OUT,用户申请退货创建退货单失败,出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            //校验门店自提单只能退门店
            if (AppDeliveryType.SELF_TAKE.equals(order.getDeliveryType())) {
                if (param.getReturnDeliveryInfo().getDeliveryType().equalsIgnoreCase(AppDeliveryType.HOUSE_PICK.getValue())) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "自提单请选择退回门店!", "");
                    logger.warn("createReturnOrder OUT,用户申请退货创建退货单失败,出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }

            // 判断订单是否超过退货时间
            int flag = appOrderService.checkOrderReturnCondition(orderNo);
            if (flag != 0){
                // 不能退货
                if (flag == -1){
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此订单不可退货!", "");
                    logger.warn("createReturnOrder OUT,用户申请退货创建退货单失败,出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }else if(flag == 1){
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此订单出货超过3个月不可退货!", "");
                    logger.warn("createReturnOrder OUT,用户申请退货创建退货单失败,出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
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
            returnOrderBaseInfo.setSalesManagerId(order.getSalesManagerId());
            returnOrderBaseInfo.setSalesManagerStoreId(order.getSalesManagerStoreId());

            //******************* 创建退货单物流信息 ************************
            ReturnOrderLogisticInfo returnOrderLogisticInfo = returnOrderService.createReturnOrderLogisticInfo(param.getReturnDeliveryInfo());
            String returnNo = returnOrderBaseInfo.getReturnNo();
            returnOrderLogisticInfo.setReturnNO(returnNo);

            //******************* 创建退货单商品信息 ************************
            List<ReturnOrderGoodsInfo> goodsInfos = new ArrayList<>();
            List<GoodsSimpleInfo> simpleInfos = param.getReturnGoodsInfo();

            Double returnTotalGoodsPrice = 0D;

            //判断是否整单是产品券
            Boolean isReturnAllProCoupon = true;
            //获取原单商品信息
            List<OrderGoodsInfo> orderGoodsInfoList = appOrderService.getOrderGoodsInfoByOrderNumber(orderNo);
            //判断总商品数
            int totalGoodsQty = orderGoodsInfoList.stream().mapToInt(OrderGoodsInfo::getOrderQuantity).sum();
            //判断退商品数
            int totalReturnQty = simpleInfos.stream().mapToInt(GoodsSimpleInfo::getQty).sum();

            if (AssertUtil.isNotEmpty(orderGoodsInfoList)) {
                for (GoodsSimpleInfo simpleInfo : simpleInfos) {
                    for (OrderGoodsInfo goodsInfo : orderGoodsInfoList) {
                        logger.info("前台传入的商品行ID和我查询的商品行ID进行比较, 出参 goodsInfo.getId():{},simpleInfo.getId()", goodsInfo.getId(), simpleInfo.getId());
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
                                //如果不是产品券就要算进退总价里,并且仅退产品券订单条件失败
                                if (!AppGoodsLineType.PRODUCT_COUPON.equals(goodsInfo.getGoodsLineType())) {
                                    returnTotalGoodsPrice = CountUtil.add(returnTotalGoodsPrice,
                                            CountUtil.mul(goodsInfo.getReturnPrice(), simpleInfo.getQty()));
                                    isReturnAllProCoupon = false;
                                }
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
            /***********************退买券订单的券****************************/
            //如果是退券只有订单商品中可查询到要退的券
            if (AppOrderType.COUPON.equals(order.getOrderType())) {
                //根据买券订单查到该单的券
                List<CustomerProductCoupon> customerProductCouponList = appCustomerService.findProductCouponsByGetOrderNumber(orderNo);
                if (AssertUtil.isNotEmpty(customerProductCouponList)) {
                    //goodsInfos里面的券每一种券有数量并未制定某张券
                    for (ReturnOrderGoodsInfo returnOrderGoodsInfo : goodsInfos) {
                        int index = 0;
                        for (CustomerProductCoupon productCoupon : customerProductCouponList) {
                            if (productCoupon.getGoodsLineId().equals(returnOrderGoodsInfo.getOrderGoodsId())) {
                                if (index == returnOrderGoodsInfo.getReturnQty()) {
                                    break;
                                }
                                ReturnOrderProductCoupon returnOrderProductCoupon = new ReturnOrderProductCoupon();
                                returnOrderProductCoupon.setGid(productCoupon.getGoodsId());
                                returnOrderProductCoupon.setIsReturn(Boolean.FALSE);
                                returnOrderProductCoupon.setOrderNo(orderNo);
                                returnOrderProductCoupon.setPcid(productCoupon.getId());
                                returnOrderProductCoupon.setQty(1);
                                returnOrderProductCoupon.setSku(returnOrderGoodsInfo.getSku());
                                returnOrderProductCoupon.setReturnQty(1);
                                returnOrderProductCoupon.setPurchasePrice(productCoupon.getBuyPrice());
                                returnOrderProductCoupon.setReturnNo(returnNo);
                                productCouponList.add(returnOrderProductCoupon);
                                index++;
                            }
                        }
                        if (index != returnOrderGoodsInfo.getReturnQty()){
                            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, returnOrderGoodsInfo.getSkuName() + "可退数量不足！", "");
                            logger.warn("createReturnOrder OUT,用户申请退货创建退货单失败,出参 resultDTO:{}", resultDTO);
                            return resultDTO;
                        }
                    }
                }
            }
            /***********************退正常退单使用的券*****************************/
            //获取订单使用产品(这里面的券一张券是一行)
            List<OrderCouponInfo> orderProductCouponList = productCouponService.findOrderCouponByCouponTypeAndOrderId(order.getId(), OrderCouponType.PRODUCT_COUPON);
            if (AssertUtil.isNotEmpty(orderProductCouponList)) {
                //goodsInfos里面的券每一种券有数量并未制定某张券
                for (ReturnOrderGoodsInfo goodsInfo : goodsInfos) {
                    if (AppGoodsLineType.PRODUCT_COUPON.equals(goodsInfo.getGoodsLineType())) {
                        //这里取退的券的数量.来返还适合订单使用同种SKU的券
                        int index = 0;
                        for (OrderCouponInfo orderCouponInfo : orderProductCouponList) {
                            if (goodsInfo.getSku().equals(orderCouponInfo.getSku())) {
                                if (index == goodsInfo.getReturnQty()) {
                                    break;
                                }
                                ReturnOrderProductCoupon productCoupon = new ReturnOrderProductCoupon();
                                productCoupon.setGid(goodsInfo.getGid());
                                productCoupon.setIsReturn(Boolean.FALSE);
                                productCoupon.setOrderNo(orderNo);
                                productCoupon.setPcid(orderCouponInfo.getCouponId());
                                productCoupon.setQty(1);
                                productCoupon.setSku(goodsInfo.getSku());
                                productCoupon.setReturnQty(1);
                                productCoupon.setPurchasePrice(orderCouponInfo.getPurchasePrice());
                                productCoupon.setReturnNo(returnNo);
                                productCoupon.setGoodsSign(orderCouponInfo.getGoodsSign());
                                productCouponList.add(productCoupon);
                                index++;
                            }
                        }
                    }
                }
            }
            //******************* 创建退货单金额信息 ************************
            List<OrderBillingPaymentDetails> orderPaymentDetails = appOrderService.
                    getOrderBillingDetailListByOrderNo(orderNo);

            OrderBillingDetails billingDetails = appOrderService.getOrderBillingDetail(orderNo);
            //初始化退货账单信息
            ReturnOrderBilling returnOrderBilling = new ReturnOrderBilling(
                    returnNo, 0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D);

            /**************/
            //2018-04-03 generation 加盟门店自提单退货不用判断账单支付信息
            Boolean jmSelfTakeOrder = false;
            AppStore store = this.appStoreService.findById(returnOrderBaseInfo.getStoreId());
            if (null != store && store.getStoreType() == StoreType.JM && AppDeliveryType.SELF_TAKE.equals(order.getDeliveryType())) {
                jmSelfTakeOrder = true;
                returnOrderBaseInfo.setReturnStatus(AppReturnOrderStatus.FINISHED);
            }
            /**************/

            //退款优先级:
            //顾客：现金POS ——> 第三方支付 ——> 预存款 ——> 未提货产品券
            //导购：现金POS ——> 第三方支付 ——> 门店预存款 ——> 未提货产品券
            //装饰经理：第三方支付 ——> 门店预存款 ——> 导购门店预存款 ——> 门店信用金 ——> 门店现金返利

            //取现金支付和预存款
            Double cashPosPrice = 0D;
            Double onlinePayPrice = 0D;
            Double tempPrice = 0D;
            Double customerPrePay = 0D;
            Double storePrePay = 0D;
            Double storeCredit = 0D;
            Double storeSubvention = 0D;
            Double sellerStoreDeposit = 0D;
            //退单扣除运费
            Boolean hasFreight = true;

            if (AssertUtil.isNotEmpty(orderPaymentDetails)) {
                for (OrderBillingPaymentDetails paymentDetails : orderPaymentDetails) {
                    if (OrderBillingPaymentType.CASH.equals(paymentDetails.getPayType())) {
                        cashPosPrice = CountUtil.add(cashPosPrice, paymentDetails.getAmount());
                    } else if (OrderBillingPaymentType.POS.equals(paymentDetails.getPayType())) {
                        cashPosPrice = CountUtil.add(cashPosPrice, paymentDetails.getAmount());
                    } else if (OrderBillingPaymentType.OTHER.equals(paymentDetails.getPayType())) {
                        cashPosPrice = CountUtil.add(cashPosPrice, paymentDetails.getAmount());
                    } else if (OrderBillingPaymentType.ALIPAY.equals(paymentDetails.getPayType())) {
                        onlinePayPrice = paymentDetails.getAmount();
                        returnOrderBilling.setOnlinePayType(OnlinePayType.ALIPAY);
                    } else if (OrderBillingPaymentType.WE_CHAT.equals(paymentDetails.getPayType())) {
                        onlinePayPrice = paymentDetails.getAmount();
                        returnOrderBilling.setOnlinePayType(OnlinePayType.WE_CHAT);
                    } else if (OrderBillingPaymentType.UNION_PAY.equals(paymentDetails.getPayType())) {
                        onlinePayPrice = paymentDetails.getAmount();
                        returnOrderBilling.setOnlinePayType(OnlinePayType.UNION_PAY);
                    } else if (OrderBillingPaymentType.CUS_PREPAY.equals(paymentDetails.getPayType())) {
                        customerPrePay = paymentDetails.getAmount();
                    } else if (OrderBillingPaymentType.ST_PREPAY.equals(paymentDetails.getPayType())) {
                        storePrePay = paymentDetails.getAmount();
                    } else if (OrderBillingPaymentType.STORE_CREDIT_MONEY.equals(paymentDetails.getPayType())) {
                        storeCredit = paymentDetails.getAmount();
                    } else if (OrderBillingPaymentType.SELLER_ST_PREPAY.equals(paymentDetails.getPayType())){
                        sellerStoreDeposit = paymentDetails.getAmount();
                    }
                }

                if ((store.getStoreType() == StoreType.ZS) && (billingDetails.getIsPayUp() != null) && billingDetails.getIsPayUp()){
                    storePrePay = CountUtil.add(storePrePay, storeCredit);
                    storeCredit = 0D;
                }

                //多次退货计算退款方式,已退款的退款方式不再退
                ReturnOrderBilling billing = this.returnOrderService.getAllReturnPriceByOrderNo(orderNo);
                if (null != billing){
                    cashPosPrice = CountUtil.sub(cashPosPrice,billing.getCash());
                    onlinePayPrice = CountUtil.sub(onlinePayPrice,billing.getOnlinePay());
                    customerPrePay = CountUtil.sub(customerPrePay,billing.getPreDeposit());
                    storePrePay = CountUtil.sub(storePrePay, billing.getStPreDeposit());
                    storeCredit = CountUtil.sub(storeCredit, billing.getStCreditMoney());
                    sellerStoreDeposit = CountUtil.sub(sellerStoreDeposit,billing.getSellerStoreDeposit());
                }

                //买卷订单现金、POS支付退顾客预存款
                //2018-08-03 买卷订单改回为原路退
//                if (AppOrderType.COUPON.equals(order.getOrderType()) && cashPosPrice > 0) {
//                    customerPrePay = CountUtil.add(customerPrePay,cashPosPrice);
//                    cashPosPrice = 0D;
//                }

                //整单退,不退运费
                if (totalGoodsQty == totalReturnQty) {
                    Double temp = 0D;
                    if (identityType == 6 || identityType == 0) {
                        if (customerPrePay >= billingDetails.getFreight()) {
                            returnOrderBilling.setPreDeposit(hasFreight ? CountUtil.sub(customerPrePay, billingDetails.getFreight()) : customerPrePay);
                            hasFreight = false;
                        } else {
                            temp = CountUtil.sub(billingDetails.getFreight(), customerPrePay);
                            returnOrderBilling.setPreDeposit(customerPrePay);
                        }
                        if (storePrePay >= temp) {
                            returnOrderBilling.setStPreDeposit(hasFreight ? CountUtil.sub(storePrePay, temp) : storePrePay);
                            hasFreight = false;
                        } else {
                            temp = CountUtil.sub(temp, storePrePay);
                            returnOrderBilling.setStPreDeposit(storePrePay);
                        }
                        if (onlinePayPrice >= temp) {
                            returnOrderBilling.setOnlinePay(hasFreight ? CountUtil.sub(onlinePayPrice, temp) : onlinePayPrice);
                            hasFreight = false;
                        } else {
                            temp = CountUtil.sub(temp, onlinePayPrice);
                            returnOrderBilling.setOnlinePay(onlinePayPrice);
                        }
                        if (sellerStoreDeposit >= temp){
                            returnOrderBilling.setSellerStoreDeposit(hasFreight ? CountUtil.sub(sellerStoreDeposit, temp) : sellerStoreDeposit);
                            hasFreight = false;
                        } else {
                            temp = CountUtil.sub(temp, sellerStoreDeposit);
                            returnOrderBilling.setSellerStoreDeposit(sellerStoreDeposit);
                        }
                        if (cashPosPrice >= temp) {
                            returnOrderBilling.setCash(hasFreight ? CountUtil.sub(cashPosPrice, temp) : cashPosPrice);
                            hasFreight = false;
                        }

                    } else if (identityType == 2) {
                        if (storeCredit >= billingDetails.getFreight()) {
                            returnOrderBilling.setStCreditMoney(hasFreight ? CountUtil.sub(storeCredit, billingDetails.getFreight()) : storeCredit);
                        } else {
                            temp = CountUtil.sub(billingDetails.getFreight(), storeCredit);
                            returnOrderBilling.setStCreditMoney(storeCredit);
                        }
                        if (storePrePay >= temp) {
                            returnOrderBilling.setStPreDeposit(hasFreight ? CountUtil.sub(storePrePay, temp) : storePrePay);
                            hasFreight = false;
                        } else {
                            temp = CountUtil.sub(temp, storePrePay);
                            returnOrderBilling.setStPreDeposit(storePrePay);
                        }
                        if (onlinePayPrice > temp) {
                            returnOrderBilling.setOnlinePay(hasFreight ? CountUtil.sub(onlinePayPrice, temp) : onlinePayPrice);
                            hasFreight = false;
                        }else {
                            temp = CountUtil.sub(temp, onlinePayPrice);
                            returnOrderBilling.setOnlinePay(onlinePayPrice);
                        }
                        if (sellerStoreDeposit >= temp){
                            returnOrderBilling.setSellerStoreDeposit(hasFreight ? CountUtil.sub(sellerStoreDeposit, temp) : sellerStoreDeposit);
                            hasFreight = false;
                        }
                    }
                    Double totalPrice = CountUtil.add(customerPrePay, storePrePay, onlinePayPrice, cashPosPrice, sellerStoreDeposit, storeCredit);
                    returnOrderBaseInfo.setReturnPrice(CountUtil.sub(totalPrice, billingDetails.getFreight()));
                } else {

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
                            //小于预存款，顾客结束
                            if (tempPrice <= customerPrePay) {
                                returnOrderBilling.setPreDeposit(tempPrice);
                            } else {
                                returnOrderBilling.setPreDeposit(customerPrePay);
                                tempPrice = CountUtil.sub(tempPrice, customerPrePay);
                                //导购小于门店预存款，导购结束
                                if (tempPrice <= storePrePay) {
                                    returnOrderBilling.setStPreDeposit(tempPrice);
                                } else {
                                    //大于门店预存款再判断 代付款导购 门店预存款
                                    returnOrderBilling.setStPreDeposit(storePrePay);
                                    tempPrice = CountUtil.sub(tempPrice, storePrePay);
                                    if (tempPrice <= sellerStoreDeposit) {
                                        returnOrderBilling.setSellerStoreDeposit(tempPrice);
                                    } else {
                                        //大于导购门店预存款再判断门店门店信用金
                                        returnOrderBilling.setSellerStoreDeposit(sellerStoreDeposit);
                                        tempPrice = CountUtil.sub(tempPrice, sellerStoreDeposit);

                                        //如果大于就判断装饰公司门店信用金
                                        if (tempPrice <= storeCredit) {
                                            returnOrderBilling.setStCreditMoney(tempPrice);
//                            } else {
//                                returnOrderBilling.setStCreditMoney(billingDetails.getStoreCreditMoney());
//                                tempPrice = CountUtil.sub(tempPrice, billingDetails.getStoreCreditMoney());
//                                //如果大于就判断装饰公司门店现金返利,经理结束
//                                if (tempPrice <= billingDetails.getStoreSubvention()) {
//                                    returnOrderBilling.setStSubvention(tempPrice);
//                                }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
//            else {
//                if (!isReturnAllProCoupon && !jmSelfTakeOrder) {
//                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单缺少账单支付信息!", "");
//                    logger.warn("createReturnOrder OUT,用户申请退货创建退货单失败,出参 resultDTO:{}", resultDTO);
//                    return resultDTO;
//                }
//            }
            AtwReturnOrder atwReturnOrder = null;
            //只有配送单退货才发WMS.
            if (AppDeliveryType.HOUSE_DELIVERY.equals(order.getDeliveryType())) {
                //保存发送wms退货单头
                AppStore appStore = appStoreService.findStoreByUserIdAndIdentityType(userId, identityType);
                SalesConsult salesConsult = appEmployeeService.findSellerByUserIdAndIdentityType(userId, identityType);
                atwReturnOrder = AtwReturnOrder.transform(returnOrderBaseInfo, returnOrderLogisticInfo, appStore, order, goodsInfos.size(), salesConsult);
            }
            returnOrderService.saveReturnOrderRelevantInfo(returnOrderBaseInfo, returnOrderLogisticInfo, goodsInfos, returnOrderBilling,
                    productCouponList, orderGoodsInfoList, atwReturnOrder);
            //如果是买券订单直接处理退款退货
            if (AppOrderType.COUPON.equals(order.getOrderType())) {
                City city = cityService.findById(order.getCityId());
                HashedMap maps = returnOrderService.couponReturnOrderProcessing(returnNo, city.getNumber());

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
                            }
                        }
                    }

                    //修改订单处理状态
                    returnOrderService.updateReturnOrderStatus(returnNo, AppReturnOrderStatus.FINISHED);
                    //*****************************保存订单生命周期信息***************************
                    OrderLifecycle orderLifecycle = new OrderLifecycle();
                    orderLifecycle.setOid(order.getId());
                    orderLifecycle.setOrderNumber(order.getOrderNumber());
                    orderLifecycle.setOperation(OrderLifecycleType.NORMAL_RETURN);
                    orderLifecycle.setPostStatus(AppOrderStatus.FINISHED);
                    orderLifecycle.setOperationTime(new Date());
                    returnOrderService.saveOrderLifecycle(orderLifecycle);
                    //********************************保存退单生命周期信息***********************
                    ReturnOrderLifecycle returnOrderLifecycle = new ReturnOrderLifecycle();
                    returnOrderLifecycle.setRoid(returnOrderBaseInfo.getRoid());
                    returnOrderLifecycle.setReturnNo(returnOrderBaseInfo.getReturnNo());
                    returnOrderLifecycle.setOperation(OrderLifecycleType.NORMAL_RETURN);
                    returnOrderLifecycle.setPostStatus(AppReturnOrderStatus.FINISHED);
                    returnOrderLifecycle.setOperationTime(new Date());
                    returnOrderService.saveReturnOrderLifecycle(returnOrderLifecycle);
                    //发送退单拆单消息到拆单消息队列
                    sinkSender.sendReturnOrder(returnNo);
                    logger.info("cancelOrderToWms OUT,买券正常退货成功");
                }else {
                    returnOrderService.updateReturnOrderStatus(returnNo, AppReturnOrderStatus.PENDING_REFUND);
                }
            }
            //只有配送单退货才发WMS.在返配上架后发EBS
            if (AppDeliveryType.HOUSE_DELIVERY.equals(order.getDeliveryType())) {
                //发送退货单到wms
                callWms.sendToWmsReturnOrderAndGoods(returnNo);
            }

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("createOrder OUT,退货单创建成功,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "退货单创建正在处理中，请稍候!", null);
                logger.warn("createOrder OUT,退货单创建重复提交，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，用户申请退货创建退货单失败", null);
            logger.warn("createReturnOrder EXCEPTION,用户申请退货创建退货单失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }finally {
            redisLock.unlock(AppLock.NORMAL_RETURN, param.getOrderNo());
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
                //获取原订单的配送方式
                OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(returnBaseInfo.getOrderNo());
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
                response.setOrderDeliveryType(orderBaseInfo.getDeliveryType());
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
            detailResponse = new ReturnOrderDetailResponse();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //设置基础信息
            detailResponse.setReturnNumber(returnBaseInfo.getReturnNo());
            detailResponse.setReturnTime(sdf.format(returnBaseInfo.getReturnTime()));
            detailResponse.setTotalReturnPrice(returnBaseInfo.getReturnPrice());
            detailResponse.setReasonInfo(returnBaseInfo.getReasonInfo());
            detailResponse.setReturnOrderStatus(returnBaseInfo.getReturnStatus());
            detailResponse.setReturnStatusDesc(null != returnBaseInfo.getReturnStatus() ? returnBaseInfo.getReturnStatus().getDescription() : null);
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
            detailResponse.setOrderNo(returnBaseInfo.getOrderNo());

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
            //分别判断是普通退货单，还是买券退货，还是自提单
            ReturnOrderBaseInfo returnOrderBaseInfo = returnOrderService.queryByReturnNo(returnNumber);
            //分别判断是普通退货单，还是买券退货，还是自提单
            OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(returnOrderBaseInfo.getOrderNo());
            if (AppDeliveryType.SELF_TAKE.equals(orderBaseInfo.getDeliveryType()) ||
                    AppOrderType.COUPON.equals(orderBaseInfo.getOrderType())) {
                // 修改回原订单的可退和已退！
                List<ReturnOrderGoodsInfo> returnOrderGoodsInfoList = returnOrderService.findReturnOrderGoodsInfoByOrderNumber(returnNumber);
                returnOrderGoodsInfoList.forEach(returnOrderGoodsInfo -> appOrderService.updateReturnableQuantityAndReturnQuantityById(
                        returnOrderGoodsInfo.getReturnQty(), returnOrderGoodsInfo.getOrderGoodsId()));

                //修改退货单状态
                returnOrderService.updateReturnOrderStatus(returnNumber, AppReturnOrderStatus.CANCELED);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                logger.info("cancelReturnOrder OUT,用户取消退货单成功,等待wms返回取消结果，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
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
                } else if (ReturnLogisticStatus.PICKING_GOODS.equals(orderDeliveryDetail.getReturnLogisticStatus())) {
                    String smsMsg = "【乐易装】请注意有退货单已被客户取消了,退单号:"
                            + returnOrderBaseInfo.getReturnNo() +
                            ",如有疑问,请登陆APP与客户联系。";
                    AppEmployee appEmployee = appEmployeeService.findDeliveryByClerkNo(orderDeliveryDetail.getPickersNumber());
                    if (AssertUtil.isNotEmpty(appEmployee) && null != appEmployee.getMobile()) {
                        smsAccountService.commonSendSms(appEmployee.getMobile(), smsMsg);
                    }
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
            //如果是门店自提，只返回自提门店信息
            if (AssertUtil.isNotEmpty(orderLogisticsInfo)) {
//                if (AppDeliveryType.SELF_TAKE.equals(orderLogisticsInfo.getDeliveryType())) {
//                    AppIdentityType identityType1 = AppIdentityType.getAppIdentityTypeByValue(identityType);
//                    DeliveryAddressResponse defaultDeliveryAddress = deliveryAddressService.getDefaultDeliveryAddressByUserIdAndIdentityType(userId, identityType1);
//                    if (null == defaultDeliveryAddress) {
//                        defaultDeliveryAddress = deliveryAddressService.getTopDeliveryAddressByUserIdAndIdentityType
//                                (userId, AppIdentityType.getAppIdentityTypeByValue(identityType));
//                    }
//                    orderLogisticsInfo = transform(orderLogisticsInfo, defaultDeliveryAddress);
//                    //如果是送货上门，退货到门店的地址就是订单门店地址
//                }
//                else
                if (AppDeliveryType.HOUSE_DELIVERY.equals(orderLogisticsInfo.getDeliveryType())) {
                    OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
                    //下订单的id 是否和当前顾客的ID一致
                    if (null != orderBaseInfo) {
                        if (identityType != 2) {
                            AppStore store = appStoreService.findByStoreCode(orderBaseInfo.getStoreCode());
                            if (store != null) {
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
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, orderLogisticsInfo);
            logger.info("getReturnOrderDetail OUT,获取用户退货方式和取货地址成功，出参 resultDTO:{}", resultDTO);
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
        returnOrderGoodsResponse.setOrderQuantity(goodsInfo.getOrderQuantity());
        returnOrderGoodsResponse.setReturnableQuantity(goodsInfo.getReturnableQuantity());
        returnOrderGoodsResponse.setPromotionId(goodsInfo.getPromotionId());
        returnOrderGoodsResponse.setReturnPriority(goodsInfo.getReturnPriority());
        returnOrderGoodsResponse.setGoodsLine(goodsInfo.getGoodsLineType().getValue());
        returnOrderGoodsResponse.setReturnPrice(goodsInfo.getReturnPrice());
        if (null != goodsInfo.getPromotionId()) {
            ActBaseDO actBaseDO = appActService.findById(Long.parseLong(goodsInfo.getPromotionId()));
            returnOrderGoodsResponse.setPromotionTitle(null != actBaseDO ? actBaseDO.getTitle() : "无促销");
        }
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
        returnOrderGoodsInfo.setGoodsSign(goodsInfo.getGoodsSign());
        return returnOrderGoodsInfo;
    }


}
