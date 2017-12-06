package cn.com.leyizhuang.app.web.controller.returnorder;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderLogisticsInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.OperationReasonsResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.ReturnOrderDetailResponse;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBilling;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBillingDetail;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.web.controller.wechatpay.WeChatPayController;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.core.constant.OperationReasonType;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.CountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Notes: 退货单接口
 * Created with IntelliJ IDEA.
 * Date: 2017/12/4.
 * Time: 9:34.
 */

@RestController
@RequestMapping("/app/returnOrder")
public class ReturnOrderController {

    private static final Logger logger = LoggerFactory.getLogger(ReturnOrderController.class);


    @Autowired
    private ReturnOrderService returnOrderService;

    @Autowired
    @Resource
    private ReturnOrderService appReturnOrderService;

    @Resource
    private AppOrderService appOrderService;

    @Autowired
    private AppCustomerService customerService;

    @Autowired
    private AppEmployeeService employeeService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private AppStoreService appStoreService;


    @Autowired
    private OperationReasonsService operationReasonsServiceImpl;

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
     * 取消订单
     *
     * @param userId       用户id
     * @param identityType 用户类型
     * @param orderNumber  订单号
     * @param reasonInfo   取消原因
     * @param remarksInfo  备注
     * @return 成功或失败
     */
    public ResultDTO<Object> canselOrder(HttpServletRequest req, HttpServletResponse response, Long userId, Integer identityType, String orderNumber, String reasonInfo, String remarksInfo) {
        logger.info("canselOrder CALLED,取消订单，入参 userId:{} identityType:{} orderNumber:{} reasonInfo:{} remarksInfo:{}",
                userId, identityType, orderNumber, reasonInfo, remarksInfo);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("canselOrder OUT,取消订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("canselOrder OUT,取消订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(orderNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单号不能为空！", null);
            logger.info("canselOrder OUT,取消订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(reasonInfo)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "取消原因不能为空！", null);
            logger.info("canselOrder OUT,取消订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
            if (null == orderBaseInfo) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此订单！", null);
                logger.info("canselOrder OUT,取消订单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (!orderBaseInfo.getDeliveryStatus().equals("RECEIVED") &&
                    !orderBaseInfo.getDeliveryStatus().equals("ALREADY_POSITIONED") &&
                    !orderBaseInfo.getDeliveryStatus().equals("PICKING_GOODS") &&
                    !orderBaseInfo.getDeliveryStatus().equals("LOADING")) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此订单已配送或已签收不能取消订单！", null);
                logger.info("canselOrder OUT,取消订单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            //获取订单账目明细
            OrderBillingDetails orderBillingDetails = appOrderService.getOrderBillingDetail(orderNumber);
            //微信退款方法类
            WeChatPayController wechat = new WeChatPayController();
            //获取退单号
            String returnNumber = OrderUtils.getReturnNumber();
            //创建退单头
            ReturnOrderBaseInfo returnOrderBaseInfo = new ReturnOrderBaseInfo();

            if (null != orderBillingDetails) {
                if ("ALIPAY".equals(orderBillingDetails.getOnlinePayType())) {

                    //创建退单退款详情实体
                    ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                    //TODO 退单id
                    returnOrderBillingDetail.setRoid(null);
                    returnOrderBillingDetail.setRefundNumber(returnNumber);
                    //TODO 时间待定
                    returnOrderBillingDetail.setIntoAmountTime(new Date());
                    //TODO 第三方回复码
                    returnOrderBillingDetail.setReplyCode("");
                    returnOrderBillingDetail.setReturnMoney(orderBillingDetails.getOnlinePayAmount());
                    returnOrderBillingDetail.setReturnPayType(OnlinePayType.ALIPAY);
                } else if ("WE_CHAT".equals(orderBillingDetails.getOnlinePayType())) {
                    wechat.wechatReturnMoney(req, response, userId, identityType, orderBillingDetails.getOnlinePayAmount(), orderNumber, returnNumber);
                    //创建退单退款详情实体
                    ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                    //TODO 退单id
                    returnOrderBillingDetail.setRoid(null);
                    returnOrderBillingDetail.setRefundNumber(returnNumber);
                    //TODO 时间待定
                    returnOrderBillingDetail.setIntoAmountTime(new Date());
                    //TODO 第三方回复码
                    returnOrderBillingDetail.setReplyCode("");
                    returnOrderBillingDetail.setReturnMoney(orderBillingDetails.getOnlinePayAmount());
                    returnOrderBillingDetail.setReturnPayType(OnlinePayType.WE_CHAT);
                } else if ("UNION_PAY".equals(orderBillingDetails.getOnlinePayAmount())) {
                    //创建退单退款详情实体
                    ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                    //TODO 退单id
                    returnOrderBillingDetail.setRoid(null);
                    returnOrderBillingDetail.setRefundNumber(returnNumber);
                    //TODO 时间待定
                    returnOrderBillingDetail.setIntoAmountTime(new Date());
                    //TODO 第三方回复码
                    returnOrderBillingDetail.setReplyCode("");
                    returnOrderBillingDetail.setReturnMoney(orderBillingDetails.getOnlinePayAmount());
                    returnOrderBillingDetail.setReturnPayType(OnlinePayType.UNION_PAY);
                }

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
                //创建退货商品实体类
                ReturnOrderGoodsInfo returnGoodsInfo = new ReturnOrderGoodsInfo();
                for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                    returnPrice += (orderGoodsInfo.getOrderQuantity() * orderGoodsInfo.getSharePrice());
                    //TODO 退单id
                    returnGoodsInfo.setRoid(null);
                    returnGoodsInfo.setReturnNo(returnNumber);
                    returnGoodsInfo.setSku(orderGoodsInfo.getSku());
                    returnGoodsInfo.setSkuName(orderGoodsInfo.getSkuName());
                    returnGoodsInfo.setRetailPrice(orderGoodsInfo.getRetailPrice());
                    returnGoodsInfo.setVipPrice(orderGoodsInfo.getVIPPrice());
                    returnGoodsInfo.setWholesalePrice(orderGoodsInfo.getWholesalePrice());
                    returnGoodsInfo.setReturnPrice(orderGoodsInfo.getReturnPrice());
                    returnGoodsInfo.setReturnQty(orderGoodsInfo.getOrderQuantity());
                    //保存退单商品信息
                    returnOrderService.saveReturnOrderGoodsInfo(returnGoodsInfo);
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
                //TODO 需要更改为枚举状态
                returnOrderBaseInfo.setReturnStatus(AppReturnOrderStatus.FINISHED);
                //保存退单头信息
                returnOrderService.saveReturnOrderBaseInfo(returnOrderBaseInfo);
                //获取退单头id
                Long returnOrderId = returnOrderBaseInfo.getRoid();
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


            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，取消订单失败", null);
            logger.warn("canselOrder EXCEPTION,取消订单失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 获取用户退货单列表
     *
     * @param userId 用户id
     * @param identityType 用户身份
     * @return 退货单列表
     */
    @RequestMapping(value = "/list", produces = "application/json;charset=UTF-8")
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
            List<ReturnOrderBaseInfo> baseInfo = appReturnOrderService.findReturnOrderListByUserIdAndIdentityType(userId, identityType, showStatus);

            //创建一个返回对象list
            List<OrderListResponse> orderListResponses = new ArrayList<>();

            for (ReturnOrderBaseInfo returnBaseInfo : baseInfo) {
                //创建有个存放图片地址的list
                List<String> goodsImgList = new ArrayList<>();
                //创建一个返回类（借用订单返回对象）
                OrderListResponse orderListResponse = new OrderListResponse();
                //获取订单商品
                List<ReturnOrderGoodsInfo> returnGoodsInfoList = appReturnOrderService.findReturnOrderGoodsInfoByOrderNumber(returnBaseInfo.getReturnNo());
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
    @RequestMapping(value = "/detail", produces = "application/json;charset=UTF-8")
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
            ReturnOrderBaseInfo returnBaseInfo = appReturnOrderService.queryByReturnNo(returnNumber);
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
                detailResponse.setGoodsList(appReturnOrderService.getReturnOrderGoodsDetails(returnNumber));
                int count = 0;
                Double totalReturnPrice = 0.00;
                //获取订单商品
                List<ReturnOrderGoodsInfo> returnGoodsInfoList = appReturnOrderService.findReturnOrderGoodsInfoByOrderNumber(returnBaseInfo.getReturnNo());
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

}
