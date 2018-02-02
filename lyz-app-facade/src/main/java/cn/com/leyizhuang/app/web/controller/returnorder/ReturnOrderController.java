package cn.com.leyizhuang.app.web.controller.returnorder;

import cn.com.leyizhuang.app.core.bean.GridDataVO;
import cn.com.leyizhuang.app.core.config.AlipayConfig;
import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.pay.wechat.refund.WeChatRefundService;
import cn.com.leyizhuang.app.core.utils.SmsUtils;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.core.utils.oss.FileUploadOSSUtils;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwCancelOrderRequest;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwCancelReturnOrderRequest;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwReturnOrder;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderBaseInf;
import cn.com.leyizhuang.app.foundation.pojo.request.CustomerSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.GoodsSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.service.impl.SmsAccountServiceImpl;
import cn.com.leyizhuang.app.remote.queue.SinkSender;
import cn.com.leyizhuang.app.remote.webservice.ICallWms;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.core.constant.OperationReasonType;
import cn.com.leyizhuang.common.foundation.pojo.SmsAccount;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.AssertUtil;
import cn.com.leyizhuang.common.util.CountUtil;
import cn.com.leyizhuang.common.util.TimeTransformUtils;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
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
import java.io.IOException;
import java.net.URLEncoder;
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
    private AppCustomerService customerService;
    @Resource
    private AppEmployeeService employeeService;
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
    private StorePreDepositLogService storePreDepositLogService;
    @Resource
    private StoreCreditMoneyLogService storeCreditMoneyLogService;
    @Resource
    private AppEmployeeService appEmployeeService;
    @Resource
    private AppCustomerService appCustomerService;
    @Resource
    private ProductCouponService productCouponService;
    @Resource
    private LeBiVariationLogService leBiVariationLogService;
    @Resource
    private CashCouponService cashCouponService;
    @Resource
    private ReturnOrderService returnOrderService;
    @Resource
    private CityService cityService;
    @Resource
    private OrderDeliveryInfoDetailsService orderDeliveryInfoDetailsService;
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
    private WeChatRefundService weChatRefundService;
    @Resource
    private SmsAccountServiceImpl smsAccountService;

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
                Map<Object, Object> maps = returnOrderService.cancelOrderUniversal(response, userId, identityType, orderNumber, reasonInfo, remarksInfo, orderBaseInfo, orderBillingDetails);
                //获取退单基础表信息
                ReturnOrderBaseInfo returnOrderBaseInfo = (ReturnOrderBaseInfo) maps.get("returnOrderBaseInfo");
                String code = (String) maps.get("code");
                if ("SUCCESS".equals(code)) {
                    //如果是待收货、门店自提单则需要返回第三方支付金额
                    if (orderBaseInfo.getDeliveryStatus().equals(AppDeliveryType.SELF_TAKE) && orderBaseInfo.getStatus().equals(AppOrderStatus.PENDING_RECEIVE)) {
                        if (null != orderBillingDetails.getOnlinePayType()) {
                            if ("支付宝".equals(orderBillingDetails.getOnlinePayType().getDescription())) {
                                //支付宝退款
                                this.returnAlipayMoney(orderNumber, orderBillingDetails.getOnlinePayAmount(), returnOrderBaseInfo.getRoid());

                            } else if ("微信".equals(orderBillingDetails.getOnlinePayType().getDescription())) {
                                //微信退款方法类
                                Map<String, String> map = weChatRefundService.wechatReturnMoney(response, userId, identityType, orderBillingDetails.getOnlinePayAmount(), orderNumber, returnOrderBaseInfo.getReturnNo());
                                this.returnWeChatMoney(returnOrderBaseInfo.getRoid(), returnOrderBaseInfo.getReturnNo(), map);
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
    public ResultDTO<Object> refusedOrder(HttpServletRequest req, HttpServletResponse response, Long userId, Integer identityType,
                                          String orderNumber, String reasonInfo, String remarksInfo, @RequestParam(value = "pictures",
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
            //*******************处理上传图骗***********************
            List<String> pictureUrls = new ArrayList<>();
            String returnPic = null;
            if (pictures != null) {
                for (MultipartFile picture : pictures) {
                    String url = FileUploadOSSUtils.uploadProfilePhoto(picture, "returnOrder/reason/");
                    pictureUrls.add(url);
                }
                returnPic = org.apache.commons.lang.StringUtils.strip(pictureUrls.toString(), "[]");
            }
            //记录退单头信息
            returnOrderBaseInfo.setOrderId(orderBaseInfo.getId());
            returnOrderBaseInfo.setOrderNo(orderNumber);
            returnOrderBaseInfo.setOrderTime(orderBaseInfo.getCreateTime());
            returnOrderBaseInfo.setStoreId(orderBaseInfo.getStoreId());
            returnOrderBaseInfo.setStoreCode(orderBaseInfo.getStoreCode());
            returnOrderBaseInfo.setStoreStructureCode(orderBaseInfo.getStoreStructureCode());
            returnOrderBaseInfo.setReturnTime(new Date());
            returnOrderBaseInfo.setReturnNo(returnNumber);
            returnOrderBaseInfo.setReturnPic(returnPic);
            returnOrderBaseInfo.setReturnType(ReturnOrderType.REFUSED_RETURN);
            //退款金额
            Double returnPrice = 0.00;
            //获取订单商品
            List<OrderGoodsInfo> orderGoodsInfoList = appOrderService.getOrderGoodsInfoByOrderNumber(orderNumber);

            for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                returnPrice += (orderGoodsInfo.getOrderQuantity() * orderGoodsInfo.getPromotionSharePrice());
            }
            returnOrderBaseInfo.setReturnPrice(returnPrice);
            returnOrderBaseInfo.setRemarksInfo(remarksInfo);
            returnOrderBaseInfo.setCreatorId(userId);
            returnOrderBaseInfo.setCreatorIdentityType(AppIdentityType.getAppIdentityTypeByValue(identityType));
            AppEmployee employee = employeeService.findById(userId);
            returnOrderBaseInfo.setCreatorName(employee.getName());
            returnOrderBaseInfo.setCreatorPhone(employee.getMobile());
            returnOrderBaseInfo.setCustomerId(orderBaseInfo.getCustomerId());
            returnOrderBaseInfo.setCustomerName(orderBaseInfo.getCustomerName());
            returnOrderBaseInfo.setReasonInfo(reasonInfo);
            returnOrderBaseInfo.setOrderType(orderBaseInfo.getOrderType());
            returnOrderBaseInfo.setReturnStatus(AppReturnOrderStatus.FINISHED);
            //保存退单头信息
            returnOrderService.saveReturnOrderBaseInfo(returnOrderBaseInfo);
            //获取退单头id
            Long returnOrderId = returnOrderBaseInfo.getRoid();

            Date date = new Date();
            //创建退货商品实体类
            ReturnOrderGoodsInfo returnGoodsInfo = new ReturnOrderGoodsInfo();
            List<ReturnOrderGoodsInfo> returnOrderGoodsInfos = new ArrayList<>(orderGoodsInfoList.size());
            for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                //记录退单商品
                returnGoodsInfo.setRoid(returnOrderId);
                returnGoodsInfo.setOrderGoodsId(orderGoodsInfo.getId());
                returnGoodsInfo.setReturnNo(returnNumber);
                returnGoodsInfo.setSku(orderGoodsInfo.getSku());
                returnGoodsInfo.setSkuName(orderGoodsInfo.getSkuName());
                returnGoodsInfo.setRetailPrice(orderGoodsInfo.getRetailPrice());
                returnGoodsInfo.setVipPrice(orderGoodsInfo.getVIPPrice());
                returnGoodsInfo.setWholesalePrice(orderGoodsInfo.getWholesalePrice());
                returnGoodsInfo.setSettlementPrice(orderGoodsInfo.getSettlementPrice());
                returnGoodsInfo.setReturnPrice(orderGoodsInfo.getReturnPrice());
                returnGoodsInfo.setReturnQty(orderGoodsInfo.getOrderQuantity());
                returnGoodsInfo.setGoodsLineType(orderGoodsInfo.getGoodsLineType());
                returnGoodsInfo.setCompanyFlag(orderGoodsInfo.getCompanyFlag());
                returnOrderGoodsInfos.add(returnGoodsInfo);
                //保存退单商品信息
                returnOrderService.saveReturnOrderGoodsInfo(returnGoodsInfo);
                //更改订单头商品已退数量和可退数量
                returnOrderService.updateReturnableQuantityAndReturnQuantityById(orderGoodsInfo.getReturnableQuantity(), 0, orderGoodsInfo.getId());
                for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                    Integer affectLine;
                    //获取现有库存量
                    CityInventory cityInventory = cityService.findCityInventoryByCityIdAndGoodsId(orderBaseInfo.getCityId(), orderGoodsInfo.getGid());
                    //退还库存量
                    if ("顾客".equals(AppIdentityType.getAppIdentityTypeByValue(identityType).getDescription())) {
                        affectLine = cityService.updateCityInventoryByCustomerIdAndGoodsIdAndInventoryAndVersion(userId, orderGoodsInfo.getGid(), orderGoodsInfo.getOrderQuantity(), cityInventory.getLastUpdateTime());
                    } else {
                        affectLine = cityService.updateCityInventoryByEmployeeIdAndGoodsIdAndInventoryAndVersion(userId, orderGoodsInfo.getGid(), orderGoodsInfo.getOrderQuantity(), cityInventory.getLastUpdateTime());
                    }
                    if (affectLine > 0) {
                        //记录城市库存变更日志
                        CityInventoryAvailableQtyChangeLog cityInventoryAvailableQtyChangeLog = new CityInventoryAvailableQtyChangeLog();
                        cityInventoryAvailableQtyChangeLog.setCityId(orderBaseInfo.getCityId());
                        cityInventoryAvailableQtyChangeLog.setCityName(orderBaseInfo.getCityName());
                        cityInventoryAvailableQtyChangeLog.setGid(orderGoodsInfo.getGid());
                        cityInventoryAvailableQtyChangeLog.setSku(orderGoodsInfo.getSku());
                        cityInventoryAvailableQtyChangeLog.setSkuName(orderGoodsInfo.getSkuName());
                        cityInventoryAvailableQtyChangeLog.setChangeTime(date);
                        cityInventoryAvailableQtyChangeLog.setChangeQty(orderGoodsInfo.getOrderQuantity());
                        cityInventoryAvailableQtyChangeLog.setAfterChangeQty((cityInventory.getAvailableIty() + orderGoodsInfo.getOrderQuantity()));
                        cityInventoryAvailableQtyChangeLog.setChangeType(CityInventoryAvailableQtyChangeType.HOUSE_DELIVERY_ORDER_RETURN);
                        cityInventoryAvailableQtyChangeLog.setChangeTypeDesc("拒签退货");
                        cityInventoryAvailableQtyChangeLog.setReferenceNumber(orderNumber);
                        //保存记录
                        cityService.addCityInventoryAvailableQtyChangeLog(cityInventoryAvailableQtyChangeLog);
                        break;
                    } else {
                        if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "系统繁忙，请稍后再试!", null);
                            logger.info("refusedOrder OUT,拒签退货失败，出参 resultDTO:{}", resultDTO);
                            return resultDTO;
                        }
                    }
                }
            }
            //创建退单退款总记录实体
            ReturnOrderBilling returnOrderBilling = new ReturnOrderBilling();
            returnOrderBilling.setRoid(returnOrderId);
            returnOrderBilling.setReturnNo(returnNumber);
            returnOrderBilling.setPreDeposit(orderBillingDetails.getCusPreDeposit() == null ? 0.00 : orderBillingDetails.getCusPreDeposit());
            returnOrderBilling.setCreditMoney(orderBillingDetails.getEmpCreditMoney() == null ? 0.00 : orderBillingDetails.getEmpCreditMoney());
            returnOrderBilling.setStPreDeposit(orderBillingDetails.getStPreDeposit() == null ? 0.00 : orderBillingDetails.getStPreDeposit());
            returnOrderBilling.setStCreditMoney(orderBillingDetails.getStoreCreditMoney() == null ? 0.00 : orderBillingDetails.getStoreCreditMoney());
            returnOrderBilling.setStSubvention(orderBillingDetails.getStoreSubvention() == null ? 0.00 : orderBillingDetails.getStoreSubvention());
            returnOrderBilling.setOnlinePay(orderBillingDetails.getOnlinePayAmount() == null ? 0.00 : orderBillingDetails.getOnlinePayAmount());
            returnOrderBilling.setCash(0.00);
            //添加保存退单退款总记录
            returnOrderService.saveReturnOrderBilling(returnOrderBilling);

            //********************************返还虚拟货币********************************
            if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.CUSTOMER)) {
                //返回乐币
                if (orderBillingDetails.getLebiQuantity() != null && orderBillingDetails.getLebiQuantity() > 0) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //获取顾客当前乐币数量
                        CustomerLeBi customerLeBi = appCustomerService.findCustomerLebiByCustomerId(orderBaseInfo.getCreatorId());
                        //返还乐币后顾客乐币数量
                        Integer lebiTotal = (customerLeBi.getQuantity() + orderBillingDetails.getLebiQuantity());
                        //更改顾客乐币数量
                        Integer affectLine = leBiVariationLogService.updateLeBiQtyByUserId(lebiTotal, customerLeBi.getLastUpdateTime(), orderBaseInfo.getCreatorId());
                        if (affectLine > 0) {
                            //记录乐币日志
                            CustomerLeBiVariationLog leBiVariationLog = new CustomerLeBiVariationLog();
                            leBiVariationLog.setCusId(orderBaseInfo.getCreatorId());
                            leBiVariationLog.setVariationQuantity(orderBillingDetails.getLebiQuantity());
                            leBiVariationLog.setAfterVariationQuantity(lebiTotal);
                            leBiVariationLog.setVariationTime(date);
                            leBiVariationLog.setLeBiVariationType(LeBiVariationType.RETURN_ORDER);
                            leBiVariationLog.setVariationTypeDesc("拒签退货");
                            leBiVariationLog.setOrderNum(orderNumber);
                            //保存日志
                            leBiVariationLogService.addCustomerLeBiVariationLog(leBiVariationLog);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "系统繁忙，请稍后再试!", null);
                                logger.info("refusedOrder OUT,拒签退货失败，出参 resultDTO:{}", resultDTO);
                                return resultDTO;
                            }
                        }
                    }
                }
                //返回顾客预存款
                if (orderBillingDetails.getCusPreDeposit() != null && orderBillingDetails.getCusPreDeposit() > 0) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //获取顾客预存款
                        CustomerPreDeposit customerPreDeposit = appCustomerService.findByCusId(orderBaseInfo.getCreatorId());
                        //返还预存款后顾客预存款金额
                        Double cusPreDeposit = (customerPreDeposit.getBalance() + orderBillingDetails.getCusPreDeposit());
                        //更改顾客预存款金额
                        Integer affectLine = appCustomerService.updateDepositByUserIdAndVersion(orderBaseInfo.getCreatorId(), orderBillingDetails.getCusPreDeposit(), customerPreDeposit.getLastUpdateTime());
                        if (affectLine > 0) {
                            //记录预存款日志
                            CusPreDepositLogDO cusPreDepositLogDO = new CusPreDepositLogDO();
                            cusPreDepositLogDO.setCreateTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            cusPreDepositLogDO.setChangeMoney(orderBillingDetails.getCusPreDeposit());
                            cusPreDepositLogDO.setOrderNumber(orderNumber);
                            cusPreDepositLogDO.setChangeType(CustomerPreDepositChangeType.RETURN_ORDER);
                            cusPreDepositLogDO.setChangeTypeDesc("拒签退货返还");
                            cusPreDepositLogDO.setCusId(orderBaseInfo.getCreatorId());
                            cusPreDepositLogDO.setOperatorId(userId);
                            cusPreDepositLogDO.setOperatorType(AppIdentityType.DELIVERY_CLERK);
                            cusPreDepositLogDO.setBalance(cusPreDeposit);
                            cusPreDepositLogDO.setDetailReason("拒签退货");
                            cusPreDepositLogDO.setTransferTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            cusPreDepositLogDO.setMerchantOrderNumber(null);
                            //保存日志
                            appCustomerService.addCusPreDepositLog(cusPreDepositLogDO);

                            ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                            returnOrderBillingDetail.setCreateTime(new Date());
                            returnOrderBillingDetail.setRoid(returnOrderBaseInfo.getRoid());
                            returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.CUS_PREPAY);
                            returnOrderBillingDetail.setReturnMoney(orderBillingDetails.getCusPreDeposit());
                            returnOrderBillingDetail.setIntoAmountTime(new Date());
                            returnOrderBillingDetail.setReplyCode(null);
                            returnOrderBillingDetail.setRefundNumber(OrderUtils.getRefundNumber());
                            returnOrderService.saveReturnOrderBillingDetail(returnOrderBillingDetail);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "系统繁忙，请稍后再试!", null);
                                logger.info("refusedOrder OUT,拒签退货失败，出参 resultDTO:{}", resultDTO);
                                return resultDTO;
                            }
                        }
                    }
                }
            }
            if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.SELLER)) {
                //返回门店预存款
                if (orderBillingDetails.getStPreDeposit() != null && orderBillingDetails.getStPreDeposit() > 0) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //获取门店预存款
                        StorePreDeposit storePreDeposit = storePreDepositLogService.findStoreByUserId(orderBaseInfo.getSalesConsultId());
                        //返还预存款后门店预存款金额
                        Double stPreDeposit = (storePreDeposit.getBalance() + orderBillingDetails.getStPreDeposit());
                        //修改门店预存款
                        Integer affectLine = storePreDepositLogService.updateStPreDepositByUserIdAndVersion(stPreDeposit, userId, storePreDeposit.getLastUpdateTime());
                        if (affectLine > 0) {
                            //记录门店预存款变更日志
                            StPreDepositLogDO stPreDepositLogDO = new StPreDepositLogDO();
                            stPreDepositLogDO.setCreateTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            stPreDepositLogDO.setChangeMoney(orderBillingDetails.getStPreDeposit());
                            stPreDepositLogDO.setRemarks("拒签退货返还门店预存款");
                            stPreDepositLogDO.setOrderNumber(orderNumber);
                            stPreDepositLogDO.setChangeType(StorePreDepositChangeType.RETURN_ORDER);
                            stPreDepositLogDO.setStoreId(storePreDeposit.getStoreId());
                            stPreDepositLogDO.setOperatorId(userId);
                            stPreDepositLogDO.setOperatorType(AppIdentityType.DELIVERY_CLERK);
                            stPreDepositLogDO.setBalance(stPreDeposit);
                            stPreDepositLogDO.setDetailReason("拒签退货");
                            stPreDepositLogDO.setTransferTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            //保存日志
                            storePreDepositLogService.save(stPreDepositLogDO);

                            ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                            returnOrderBillingDetail.setCreateTime(new Date());
                            returnOrderBillingDetail.setRoid(returnOrderBaseInfo.getRoid());
                            returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.ST_PREPAY);
                            returnOrderBillingDetail.setReturnMoney(orderBillingDetails.getStPreDeposit());
                            returnOrderBillingDetail.setIntoAmountTime(new Date());
                            returnOrderBillingDetail.setReplyCode(null);
                            returnOrderBillingDetail.setRefundNumber(OrderUtils.getRefundNumber());
                            returnOrderService.saveReturnOrderBillingDetail(returnOrderBillingDetail);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "系统繁忙，请稍后再试!", null);
                                logger.info("refusedOrder OUT,拒签退货失败，出参 resultDTO:{}", resultDTO);
                                return resultDTO;
                            }
                        }
                    }
                }
                //返回导购信用额度
                if (orderBillingDetails.getEmpCreditMoney() != null && orderBillingDetails.getEmpCreditMoney() > 0) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //获取导购信用金
                        EmpCreditMoney empCreditMoney = appEmployeeService.findEmpCreditMoneyByEmpId(orderBaseInfo.getSalesConsultId());
                        //返还信用金后导购信用金额度
                        Double creditMoney = (empCreditMoney.getCreditLimitAvailable() + orderBillingDetails.getEmpCreditMoney());
                        //修改导购信用额度
                        Integer affectLine = appEmployeeService.unlockGuideCreditByUserIdAndGuideCreditAndVersion(userId, orderBillingDetails.getEmpCreditMoney(), empCreditMoney.getLastUpdateTime());

                        if (affectLine > 0) {
                            //记录导购信用金变更日志
                            EmpCreditMoneyChangeLog empCreditMoneyChangeLog = new EmpCreditMoneyChangeLog();
                            empCreditMoneyChangeLog.setEmpId(orderBaseInfo.getSalesConsultId());
                            empCreditMoneyChangeLog.setCreateTime(date);
                            empCreditMoneyChangeLog.setCreditLimitAvailableChangeAmount(orderBillingDetails.getEmpCreditMoney());
                            empCreditMoneyChangeLog.setCreditLimitAvailableAfterChange(creditMoney);
                            empCreditMoneyChangeLog.setReferenceNumber(orderNumber);
                            empCreditMoneyChangeLog.setChangeType(EmpCreditMoneyChangeType.RETURN_ORDER);
                            empCreditMoneyChangeLog.setChangeTypeDesc("拒签退货返还信用金");
                            empCreditMoneyChangeLog.setOperatorId(userId);
                            empCreditMoneyChangeLog.setOperatorType(AppIdentityType.DELIVERY_CLERK);
                            //保存日志
                            appEmployeeService.addEmpCreditMoneyChangeLog(empCreditMoneyChangeLog);
                            break;
                        }else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "系统繁忙，请稍后再试!", null);
                                logger.info("refusedOrder OUT,拒签退货失败，出参 resultDTO:{}", resultDTO);
                                return resultDTO;
                            }
                        }
                    }
                }
            }
            if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.DECORATE_MANAGER)) {
                for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                    //返回门店预存款
                    if (orderBillingDetails.getStPreDeposit() != null && orderBillingDetails.getStPreDeposit() > 0) {
                        //获取门店预存款
                        StorePreDeposit storePreDeposit = storePreDepositLogService.findStoreByUserId(orderBaseInfo.getCreatorId());
                        //返还预存款后门店预存款金额
                        Double stPreDeposit = (storePreDeposit.getBalance() + orderBillingDetails.getStPreDeposit());
                        //修改门店预存款
                        Integer affectLine = storePreDepositLogService.updateStPreDepositByUserIdAndVersion(stPreDeposit, userId, storePreDeposit.getLastUpdateTime());
                        if (affectLine > 0) {
                            //记录门店预存款变更日志
                            StPreDepositLogDO stPreDepositLogDO = new StPreDepositLogDO();
                            stPreDepositLogDO.setCreateTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            stPreDepositLogDO.setChangeMoney(orderBillingDetails.getStPreDeposit());
                            stPreDepositLogDO.setRemarks("拒签退货返还门店预存款");
                            stPreDepositLogDO.setOrderNumber(orderNumber);
                            stPreDepositLogDO.setChangeType(StorePreDepositChangeType.RETURN_ORDER);
                            stPreDepositLogDO.setStoreId(storePreDeposit.getStoreId());
                            stPreDepositLogDO.setOperatorId(userId);
                            stPreDepositLogDO.setOperatorType(AppIdentityType.DELIVERY_CLERK);
                            stPreDepositLogDO.setBalance(stPreDeposit);
                            stPreDepositLogDO.setDetailReason("取消订单");
                            stPreDepositLogDO.setTransferTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            //保存日志
                            storePreDepositLogService.save(stPreDepositLogDO);

                            ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                            returnOrderBillingDetail.setCreateTime(new Date());
                            returnOrderBillingDetail.setRoid(returnOrderBaseInfo.getRoid());
                            returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.ST_PREPAY);
                            returnOrderBillingDetail.setReturnMoney(orderBillingDetails.getStPreDeposit());
                            returnOrderBillingDetail.setIntoAmountTime(new Date());
                            returnOrderBillingDetail.setReplyCode(null);
                            returnOrderBillingDetail.setRefundNumber(OrderUtils.getRefundNumber());
                            returnOrderService.saveReturnOrderBillingDetail(returnOrderBillingDetail);
                            break;
                        }else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "系统繁忙，请稍后再试!", null);
                                logger.info("refusedOrder OUT,拒签退货失败，出参 resultDTO:{}", resultDTO);
                                return resultDTO;
                            }
                        }
                    }
                }
                //返回门店信用金（装饰公司）
                if (orderBillingDetails.getStoreCreditMoney() != null && orderBillingDetails.getStoreCreditMoney() > 0) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //查询门店信用金
                        StoreCreditMoney storeCreditMoney = storeCreditMoneyLogService.findStoreCreditMoneyByUserId(orderBaseInfo.getCreatorId());
                        //返还后门店信用金额度
                        Double creditMoney = (storeCreditMoney.getCreditLimitAvailable() + orderBillingDetails.getStoreCreditMoney());
                        //修改门店可用信用金
                        Integer affectLine = appStoreService.updateStoreCreditByUserIdAndVersion(userId, orderBillingDetails.getStoreCreditMoney(), storeCreditMoney.getLastUpdateTime());
                        if (affectLine > 0) {
                            //记录门店信用金变更日志
                            StoreCreditMoneyChangeLog storeCreditMoneyChangeLog = new StoreCreditMoneyChangeLog();
                            storeCreditMoneyChangeLog.setStoreId(storeCreditMoney.getStoreId());
                            storeCreditMoneyChangeLog.setCreateTime(date);
                            storeCreditMoneyChangeLog.setChangeAmount(orderBillingDetails.getStoreCreditMoney());
                            storeCreditMoneyChangeLog.setCreditLimitAvailableAfterChange(creditMoney);
                            storeCreditMoneyChangeLog.setReferenceNumber(orderNumber);
                            storeCreditMoneyChangeLog.setChangeType(StoreCreditMoneyChangeType.RETURN_ORDER);
                            storeCreditMoneyChangeLog.setChangeTypeDesc("拒签退货返还门店信用金");
                            storeCreditMoneyChangeLog.setOperatorId(userId);
                            storeCreditMoneyChangeLog.setOperatorType(AppIdentityType.DELIVERY_CLERK);
                            storeCreditMoneyChangeLog.setRemark("拒签退货");
                            //保存日志
                            appStoreService.addStoreCreditMoneyChangeLog(storeCreditMoneyChangeLog);
                            break;
                        }else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "系统繁忙，请稍后再试!", null);
                                logger.info("refusedOrder OUT,拒签退货失败，出参 resultDTO:{}", resultDTO);
                                return resultDTO;
                            }
                        }
                    }
                }
                //返回门店现金返利（装饰公司）
                if (AssertUtil.isNotEmpty(orderBillingDetails.getStoreSubvention())) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //获取门店现金返利
                        StoreSubvention storeSubvention = appStoreService.findStoreSubventionByEmpId(orderBaseInfo.getCreatorId());
                        //返还后门店现金返利余额
                        Double subvention = (storeSubvention.getBalance() + orderBillingDetails.getStoreSubvention());
                        //修改门店现金返利
                        Integer affectLine = appStoreService.updateStoreSubventionByUserIdAndVersion(orderBillingDetails.getStoreSubvention(), userId, storeSubvention.getLastUpdateTime());
                        if (affectLine > 0) {
                            //记录门店现金返利变更日志
                            StoreSubventionChangeLog storeSubventionChangeLog = new StoreSubventionChangeLog();
                            storeSubventionChangeLog.setStoreId(storeSubvention.getStoreId());
                            storeSubventionChangeLog.setCreateTime(date);
                            storeSubventionChangeLog.setChangeAmount(orderBillingDetails.getStoreSubvention());
                            storeSubventionChangeLog.setBalance(subvention);
                            storeSubventionChangeLog.setReferenceNumber(orderNumber);
                            storeSubventionChangeLog.setChangeType(StoreSubventionChangeType.RETURN_ORDER);
                            storeSubventionChangeLog.setChangeTypeDesc("拒签退货返还门店现金返利");
                            storeSubventionChangeLog.setOperatorId(userId);
                            storeSubventionChangeLog.setOperatorType(AppIdentityType.DELIVERY_CLERK);
                            storeSubventionChangeLog.setRemark("拒签退货");
                            //保存日志
                            appStoreService.addStoreSubventionChangeLog(storeSubventionChangeLog);
                            break;
                        }else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "系统繁忙，请稍后再试!", null);
                                logger.info("refusedOrder OUT,拒签退货失败，出参 resultDTO:{}", resultDTO);
                                return resultDTO;
                            }
                        }
                    }
                }
            }
            //*******************************退券*********************************
            //获取订单使用产品券
            List<OrderCouponInfo> orderProductCouponList = productCouponService.findOrderCouponByCouponTypeAndOrderId(orderBaseInfo.getId(), OrderCouponType.PRODUCT_COUPON);
            if (AssertUtil.isNotEmpty(orderProductCouponList)) {
                for (OrderCouponInfo orderProductCoupon : orderProductCouponList) {
                    //查询使用产品券信息
                    CustomerProductCoupon customerProductCoupon = productCouponService.findCusProductCouponByCouponId(orderProductCoupon.getCouponId());
                    //创建新的产品券
                    customerProductCoupon.setLastUpdateTime(new Date());
                    customerProductCoupon.setIsUsed(Boolean.FALSE);
                    //修改原产品券是否使用和修改时间
                    productCouponService.updateCustomerProductCoupon(customerProductCoupon);

                    //增加日志
                    CustomerProductCouponChangeLog changeLog = new CustomerProductCouponChangeLog();
                    if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.CUSTOMER)) {
                        changeLog.setCusId(userId);
                    } else if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.SELLER)) {
                        changeLog.setCusId(orderBaseInfo.getCustomerId());
                    }
                    changeLog.setCouponId(orderProductCoupon.getCouponId());
                    changeLog.setChangeType(CustomerProductCouponChangeType.RETURN_ORDER);
                    changeLog.setChangeTypeDesc(CustomerProductCouponChangeType.RETURN_ORDER.getDescription());
                    changeLog.setReferenceNumber(orderNumber);
                    changeLog.setOperatorId(userId);
                    changeLog.setOperatorIp(null);
                    changeLog.setOperatorType(AppIdentityType.getAppIdentityTypeByValue(identityType));
                    changeLog.setUseTime(new Date());
                    // 日志变更保存
                    productCouponService.addCustomerProductCouponChangeLog(changeLog);

                    ReturnOrderProductCoupon returnOrderProductCoupon = new ReturnOrderProductCoupon();
                    returnOrderProductCoupon.setOrderNo(orderBaseInfo.getOrderNumber());
                    returnOrderProductCoupon.setRoid(returnOrderBaseInfo.getRoid());
                    returnOrderProductCoupon.setReturnNo(returnOrderBaseInfo.getReturnNo());
                    returnOrderProductCoupon.setGid(null);
                    returnOrderProductCoupon.setPcid(orderProductCoupon.getCouponId());
                    returnOrderProductCoupon.setQty(1);
                    returnOrderProductCoupon.setReturnQty(1);
                    returnOrderProductCoupon.setSku(orderProductCoupon.getSku());
                    returnOrderProductCoupon.setPurchasePrice(orderProductCoupon.getPurchasePrice());
                    returnOrderProductCoupon.setIsReturn(Boolean.TRUE);

                    returnOrderService.saveReturnOrderProductCoupon(returnOrderProductCoupon);
                }
            }
            //获取订单使用现金券
            List<OrderCouponInfo> orderCashCouponList = productCouponService.findOrderCouponByCouponTypeAndOrderId(orderBaseInfo.getCreatorId(), OrderCouponType.CASH_COUPON);
            if (AssertUtil.isNotEmpty(orderCashCouponList)) {
                for (OrderCouponInfo orderCashCoupon : orderCashCouponList) {
                    //查询现金券原信息
                    CustomerCashCoupon customerCashCoupon = cashCouponService.findCusCashCouponByCouponId(orderCashCoupon.getCouponId());
                    customerCashCoupon.setLastUpdateTime(new Date());
                    customerCashCoupon.setIsUsed(Boolean.FALSE);
                    //修改原现金券是否使用和修改时间
                    cashCouponService.updateCustomerCashCoupon(customerCashCoupon);

                    //记录现金券变更日志
                    CustomerCashCouponChangeLog customerCashCouponChangeLog = new CustomerCashCouponChangeLog();
                    if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.CUSTOMER)) {
                        customerCashCouponChangeLog.setCusId(orderBaseInfo.getCreatorId());
                    } else if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.SELLER)) {
                        customerCashCouponChangeLog.setCusId(orderBaseInfo.getCustomerId());
                    }
                    customerCashCouponChangeLog.setUseTime(date);
                    customerCashCouponChangeLog.setCouponId(orderCashCoupon.getCouponId());
                    customerCashCouponChangeLog.setReferenceNumber(orderNumber);
                    customerCashCouponChangeLog.setChangeType(CustomerCashCouponChangeType.CANCEL_ORDER);
                    customerCashCouponChangeLog.setChangeTypeDesc("拒签退单返还");
                    customerCashCouponChangeLog.setOperatorId(userId);
                    customerCashCouponChangeLog.setOperatorType(AppIdentityType.DELIVERY_CLERK);
                    customerCashCouponChangeLog.setRemark("拒签退单");
                    //保存日志
                    appCustomerService.addCustomerCashCouponChangeLog(customerCashCouponChangeLog);

                    ReturnOrderCashCoupon returnOrderCashCoupon = new ReturnOrderCashCoupon();
                    returnOrderCashCoupon.setRoid(returnOrderBaseInfo.getRoid());
                    returnOrderCashCoupon.setOrderNo(orderBaseInfo.getOrderNumber());
                    returnOrderCashCoupon.setCcid(orderCashCoupon.getCouponId());
                    returnOrderCashCoupon.setPurchasePrice(orderCashCoupon.getPurchasePrice());
                    returnOrderCashCoupon.setIsReturn(Boolean.TRUE);

                    returnOrderService.saveReturnOrderCashCoupon(returnOrderCashCoupon);
                }
            }
            //********************************退经销差价退还*************************
            AppStore appStore = appStoreService.findStoreByUserIdAndIdentityType(userId, identityType);

            if (AssertUtil.isNotEmpty(appStore) && appStore.getStoreType().equals(StoreType.FX) || appStore.getStoreType().equals(StoreType.JM)) {
                commonService.deductionOrderJxPriceDifferenceRefund(returnOrderBaseInfo, orderBaseInfo, returnOrderGoodsInfos);
            }

            //********************************退第三方支付**************************
            //如果是待收货、门店自提单则需要返回第三方支付金额
            if (orderBaseInfo.getDeliveryStatus().equals(AppDeliveryType.SELF_TAKE) && orderBaseInfo.getStatus().equals(AppOrderStatus.PENDING_RECEIVE)) {
                if ("支付宝".equals(orderBillingDetails.getOnlinePayType().getDescription())) {
                    //支付宝退款
                    this.returnAlipayMoney(orderNumber, orderBillingDetails.getOnlinePayAmount(), returnOrderId);

                } else if ("微信".equals(orderBillingDetails.getOnlinePayType().getDescription())) {
                    //微信退款方法类
                    Map<String, String> map = weChatRefundService.wechatReturnMoney(response, userId, identityType, orderBillingDetails.getOnlinePayAmount(), orderNumber, returnNumber);
                    this.returnWeChatMoney(returnOrderId, returnNumber, map);
                } else if ("银联".equals(orderBillingDetails.getOnlinePayType().getDescription())) {
                    //创建退单退款详情实体
                    ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                    returnOrderBillingDetail.setCreateTime(new Date());
                    returnOrderBillingDetail.setRoid(returnOrderId);
                    returnOrderBillingDetail.setRefundNumber(returnNumber);
                    //TODO 时间待定
                    returnOrderBillingDetail.setIntoAmountTime(new Date());
                    //TODO 第三方回复码
                    returnOrderBillingDetail.setReplyCode("");
                    returnOrderBillingDetail.setReturnMoney(orderBillingDetails.getOnlinePayAmount());
                    returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.UNION_PAY);
                }

            }
            //获取物流状态明细
            OrderDeliveryInfoDetails orderDeliveryInfoDetails = orderDeliveryInfoDetailsService.findByOrderNumberAndLogisticStatus(orderNumber, LogisticStatus.SEALED_CAR);
            //记录物流明细表
            OrderDeliveryInfoDetails newOrderDeliveryInfoDetails = new OrderDeliveryInfoDetails();
            newOrderDeliveryInfoDetails.setOrderNo(orderNumber);
            newOrderDeliveryInfoDetails.setLogisticStatus(LogisticStatus.REJECT);
            newOrderDeliveryInfoDetails.setCreateTime(date);
            newOrderDeliveryInfoDetails.setDescription("拒签");
            newOrderDeliveryInfoDetails.setOperationType("拒签退货");
            newOrderDeliveryInfoDetails.setOperatorNo(orderDeliveryInfoDetails.getOperatorNo());
            newOrderDeliveryInfoDetails.setWarehouseNo(orderDeliveryInfoDetails.getWarehouseNo());
            newOrderDeliveryInfoDetails.setTaskNo(orderDeliveryInfoDetails.getTaskNo());

            orderDeliveryInfoDetailsService.addOrderDeliveryInfoDetails(newOrderDeliveryInfoDetails);
            //修改订单状态为拒签,物流状态拒签
            appOrderService.updateOrderStatusAndDeliveryStatusByOrderNo(AppOrderStatus.REJECTED, LogisticStatus.REJECT, orderBaseInfo.getOrderNumber());
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
                AppCustomer customer = customerService.findById(userId);
                returnOrderBaseInfo.setCreatorName(customer.getName());
                returnOrderBaseInfo.setCreatorPhone(customer.getMobile());
            } else {
                AppEmployee employee = appEmployeeService.findById(userId);
                returnOrderBaseInfo.setCreatorName(employee.getName());
                returnOrderBaseInfo.setCreatorPhone(employee.getMobile());
                if (identityType == 0) {
                    AppCustomer customer = customerService.findById(param.getCusId());
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
            SalesConsult salesConsult = employeeService.findSellerByUserIdAndIdentityType(userId, identityType);
            AtwReturnOrder atwReturnOrder = AtwReturnOrder.transform(returnOrderBaseInfo, returnOrderLogisticInfo, appStore, order, goodsInfos.size(), salesConsult);
            appToWmsOrderService.saveAtwReturnOrder(atwReturnOrder);
            //发送退货单到wms
            callWms.sendToWmsReturnOrderAndGoods(returnNo);
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
            if (null == returnOrderLogisticInfo) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单缺少收货物流信息！", null);
                logger.info("getReturnOrderDetail OUT,查看退货单详情失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            detailResponse = new ReturnOrderDetailResponse();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            //设置基础信息
            detailResponse.setReturnNumber(returnBaseInfo.getReturnNo());
            detailResponse.setReturnTime(sdf.format(returnBaseInfo.getReturnTime()));
            detailResponse.setTotalReturnPrice(returnBaseInfo.getReturnPrice());
            detailResponse.setReasonInfo(returnBaseInfo.getReasonInfo());
            detailResponse.setReturnStatus(null != returnBaseInfo.getReturnStatus() ? returnBaseInfo.getReturnStatus().getDescription() : null);
            detailResponse.setReturnType(null != returnBaseInfo.getReturnType() ? returnBaseInfo.getReturnType().getDescription() : null);
            detailResponse.setDeliveryType(null != returnOrderLogisticInfo.getDeliveryType() ? returnOrderLogisticInfo.getDeliveryType().getValue() : null);

            //取货方式（上门取货，送货到店）
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
            AtwCancelReturnOrderRequest atwCancelOrderRequest = AtwCancelReturnOrderRequest.transform(returnOrderBaseInfo);
            appToWmsOrderService.saveAtwCancelReturnOrderRequest(atwCancelOrderRequest);
            //发送取消退货单到WMS
            callWms.sendToWmsCancelOrder(returnNumber);
            // 修改回原订单的可退和已退！
            returnOrderService.updateReturnOrderStatus(returnNumber, AppReturnOrderStatus.CANCELED);
            List<ReturnOrderGoodsInfo> returnOrderGoodsInfoList = returnOrderService.findReturnOrderGoodsInfoByOrderNumber(returnNumber);
            returnOrderGoodsInfoList.forEach(returnOrderGoodsInfo -> appOrderService.updateReturnableQuantityAndReturnQuantityById(
                    returnOrderGoodsInfo.getReturnQty(), returnOrderGoodsInfo.getOrderGoodsId()));

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
                } else if (AppDeliveryType.HOUSE_DELIVERY.equals(orderLogisticsInfo.getDeliveryType()) && identityType == 6) {
                    OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
                    //下订单的id 是否和当前顾客的ID一致
                    if (null != orderBaseInfo && null != orderBaseInfo.getCreatorId()) {
                        if (orderBaseInfo.getCreatorId().equals(userId)) {
                            AppStore store = appStoreService.findStoreByUserIdAndIdentityType(userId, identityType);
                            orderLogisticsInfo.setDeliveryType(AppDeliveryType.HOUSE_PICK);
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

    /**
     * 支付宝退款
     *
     * @param orderNumber   订单号
     * @param money         退款金额
     * @param returnOrderId 退单id
     * @throws AlipayApiException
     */
    public void returnAlipayMoney(String orderNumber, Double money, Long returnOrderId) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.serverUrl, AlipayConfig.appId, AlipayConfig.privateKey, AlipayConfig.format, AlipayConfig.charset, AlipayConfig.aliPublicKey, AlipayConfig.signType);
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizContent("{" +
                "\"out_trade_no\":\"" + orderNumber + "\"," +
                "\"refund_amount\":" + money + "," +
                "  }");
        AlipayTradeRefundResponse aliPayResponse = alipayClient.execute(request);
        if (aliPayResponse.isSuccess()) {
            //创建退单退款详情实体
            ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
            returnOrderBillingDetail.setCreateTime(new Date());
            returnOrderBillingDetail.setRoid(returnOrderId);
            returnOrderBillingDetail.setRefundNumber(OrderUtils.getRefundNumber());
            returnOrderBillingDetail.setIntoAmountTime(aliPayResponse.getGmtRefundPay());
            returnOrderBillingDetail.setReplyCode(aliPayResponse.getTradeNo());
            returnOrderBillingDetail.setReturnMoney(Double.valueOf(aliPayResponse.getRefundFee()));
            returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.ALIPAY);
            returnOrderService.saveReturnOrderBillingDetail(returnOrderBillingDetail);
        } else {
            logger.info("refusedOrder OUT,支付宝退款失败！，出参 resultDTO:{}");
            throw new RuntimeException("支付宝退款失败");
        }
    }

    /**
     * 微信退款
     *
     * @param returnOrderId 退单id
     * @param returnNumber  退单号
     * @param map           微信返回值
     */
    public void returnWeChatMoney(Long returnOrderId, String returnNumber, Map<String, String> map) {
        if ("SUCCESS".equals(map.get("code"))) {
            //创建退单退款详情实体
            ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
            returnOrderBillingDetail.setCreateTime(new Date());
            returnOrderBillingDetail.setRoid(returnOrderId);
            returnOrderBillingDetail.setRefundNumber(returnNumber);
            returnOrderBillingDetail.setIntoAmountTime(new Date());
            returnOrderBillingDetail.setReplyCode(map.get("number"));
            returnOrderBillingDetail.setReturnMoney(Double.valueOf(map.get("money")));
            returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.WE_CHAT);
            returnOrderService.saveReturnOrderBillingDetail(returnOrderBillingDetail);
        } else {
            logger.info("refusedOrder OUT,微信退款失败！，出参 resultDTO:{}");
            throw new RuntimeException("微信退款失败");
        }


    }

    private ReturnOrderGoodsResponse transform(OrderGoodsInfo goodsInfo) {
        ReturnOrderGoodsResponse returnOrderGoodsResponse = new ReturnOrderGoodsResponse();
        returnOrderGoodsResponse.setId(goodsInfo.getId());
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

    public void cancelOrderToWms(HttpServletRequest req, HttpServletResponse response, String orderNumber, Boolean isCancel) {
        if (StringUtils.isBlank(orderNumber)) {
            logger.info("cancelOrderToWms OUT,WMS回传订单号为空，取消订单失败，出参 ResultDTO:{}");
            return;
        }
        //获取订单头信息
        OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
        //获取订单账目明细
        OrderBillingDetails orderBillingDetails = appOrderService.getOrderBillingDetail(orderNumber);
        //获取取消订单相关参数
        CancelOrderParametersDO cancelOrderParametersDO = cancelOrderParametersService.findCancelOrderParametersByOrderNumber(orderNumber);
        if (isCancel) {
            //调用取消订单通用方法
            Map<Object, Object> maps = returnOrderService.cancelOrderUniversal(response, cancelOrderParametersDO.getUserId(), cancelOrderParametersDO.getIdentityType(), orderNumber, cancelOrderParametersDO.getReasonInfo(), cancelOrderParametersDO.getRemarksInfo(), orderBaseInfo, orderBillingDetails);
            ReturnOrderBaseInfo returnOrderBaseInfo = (ReturnOrderBaseInfo) maps.get("returnOrderBaseInfo");
            if (maps.get("code").equals("SUCCESS")) {
                //发送退单拆单消息到拆单消息队列
                sinkSender.sendReturnOrder(returnOrderBaseInfo.getReturnNo());
                //修改取消订单处理状态
                cancelOrderParametersService.updateCancelStatusByOrderNumber(orderNumber);
                logger.info("cancelOrderToWms OUT,取消订单成功");
                return;
            } else {
                logger.info("getReturnOrderList OUT,取消订单失败");
            }
        } else {
            logger.info("cancelOrderToWms CALLED,发送提货码，入参 mobile:{}", orderBaseInfo.getCreatorPhone());
            if (null == orderBaseInfo.getCreatorPhone() || orderBaseInfo.getCreatorPhone().equalsIgnoreCase("") || orderBaseInfo.getCreatorPhone().trim().length() != 11) {
                logger.info("cancelOrderToWms OUT,发送提货码失败，出参 ResultDTO:{}");
            }
            String info = "您取消的订单" + orderNumber + "，取消失败，请联系管理人员！";
            logger.info("取消失败订单号:{}", orderNumber);
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
}
