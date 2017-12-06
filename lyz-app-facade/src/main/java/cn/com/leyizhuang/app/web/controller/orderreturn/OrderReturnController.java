package cn.com.leyizhuang.app.web.controller.orderreturn;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.AppOrderReturnStatus;
import cn.com.leyizhuang.app.core.constant.OnlinePayType;
import cn.com.leyizhuang.app.core.constant.ReturnOrderType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBilling;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBillingDetail;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.AppCustomerService;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.ReturnOrderService;
import cn.com.leyizhuang.app.web.controller.wechatpay.WeChatPayController;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@RequestMapping("/app/return")
public class OrderReturnController {

    private static final Logger logger = LoggerFactory.getLogger(OrderReturnController.class);


    @Autowired
    private ReturnOrderService returnOrderService;

    @Autowired
    private AppOrderService appOrderService;

    @Autowired
    private AppCustomerService customerService;

    @Autowired
    private AppEmployeeService employeeService;

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
                returnOrderBaseInfo.setReturnStatus(AppOrderReturnStatus.FINISHED);
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
     * @param userId
     * @param identityType
     * @return
     */
    @RequestMapping(value = "/list")
    public ResultDTO getReturnOrderList(Long userId, Integer identityType) {

        logger.info("getReturnOrderList CALLED,获取用户退货单列表，入参 userID:{}, identityType:{}", userId, identityType);

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
//        List<ReturnOrderBaseInfo> baseInfos = appOrderReturnService.findOrderReturnListByUserIdAndIdentityType(userId,identityType);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        logger.info("getOrderLogisticsResponse OUT,配送员修改物流状态成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }
}
