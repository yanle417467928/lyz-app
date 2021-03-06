package cn.com.leyizhuang.app.web.controller.wechatpay;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.pay.wechat.sign.WechatPrePay;
import cn.com.leyizhuang.app.core.pay.wechat.util.WechatUtil;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.pojo.PaymentDataDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderArrearsAuditDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeOrder;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeReceiptInfo;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.remote.queue.SinkSender;
import cn.com.leyizhuang.app.remote.webservice.ICallWms;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.CountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Jerry.Ren
 * Notes:第三方支付调用接口
 * Created with IntelliJ IDEA.
 * Date: 2017/11/8.
 * Time: 17:43.
 */
@RestController
@RequestMapping("/app/wechatpay")
public class WeChatPayController {

    private static final Logger logger = LoggerFactory.getLogger(WeChatPayController.class);

    @Resource
    private PaymentDataService paymentDataService;

    @Resource
    private AppCustomerService appCustomerServiceImpl;

    @Resource
    private AppOrderService appOrderService;

    @Resource
    private ArrearsAuditService arrearsAuditService;

    @Resource
    private ReturnOrderService returnOrderService;

    @Resource
    private CommonService commonService;

    @Resource
    private AppStoreService appStoreService;

    @Resource
    private ICallWms iCallWms;

    @Resource
    private SinkSender sinkSender;

    @Resource
    private RechargeService rechargeService;

    @Resource
    private CityService cityService;

    @Resource
    private TransactionalSupportService supportService;

    @Resource
    private ProductCouponService productCouponService;

    @Autowired
    private BillInfoService billInfoService;

    /**
     * 微信支付订单
     *
     * @param req           请求对象
     * @param userId        用户id
     * @param identityType  用户身份类型
     * @param amountPayable 支付金额
     * @param orderNumber   订单号
     * @return 微信支付请求签名
     */
    @RequestMapping(value = "/order/pay", method = RequestMethod.POST)
    public ResultDTO<Object> orderWeChatPay(HttpServletRequest req, Long userId, Integer identityType, Double amountPayable, String orderNumber) {

        logger.info("orderWeChatPay CALLED,订单微信支付信息提交,入参 userId:{}, identityType:{}, money:{}, orderNumber:{}",
                userId, identityType, amountPayable, orderNumber);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("orderWeChatPay OUT,微信支付订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("orderWeChatPay OUT,微信支付订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == amountPayable || amountPayable <= 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "金额不正确！", null);
            logger.info("orderWeChatPay OUT,微信支付订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == orderNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单不能为空！", null);
            logger.info("orderWeChatPay OUT,微信支付订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Double totalFee = appOrderService.getAmountPayableByOrderNumber(orderNumber);
        if (totalFee == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查到该订单！", null);
            logger.info("orderWeChatPay OUT,微信支付订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
//        if (!totalFee.equals(amountPayable)) {
//            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "支付金额与订单金额不匹配！", null);
//            logger.info("orderWeChatPay OUT,微信支付订单失败，出参 resultDTO:{}", resultDTO);
//            return resultDTO;
//        }
        String totalFeeFormat = CountUtil.retainTwoDecimalPlaces(totalFee);
        Double totalFeeParse = Double.parseDouble(totalFeeFormat);
        String outTradeNo = OrderUtils.generatePayNumber();

        PaymentDataDO paymentDataDO = new PaymentDataDO(userId, outTradeNo, orderNumber, identityType, AppApplicationConstant.wechatReturnUrlAsnyc,
                totalFeeParse, PaymentDataStatus.WAIT_PAY, OnlinePayType.WE_CHAT, "订单支付");
        this.paymentDataService.save(paymentDataDO);

        try {
            SortedMap<String, Object> secondSignMap = (SortedMap<String, Object>) WechatPrePay.wechatSign(outTradeNo, paymentDataDO.getPaymentTypeDesc(),
                    new BigDecimal(totalFeeParse), req);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, secondSignMap);
            logger.info("orderWeChatPay OUT,微信支付订单成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,微信支付订单失败!", null);
            logger.warn("orderWeChatPay EXCEPTION,微信支付订单失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }

    }

    /**
     * 微信充值预存款
     *
     * @param req          请求对象
     * @param userId       用户id
     * @param identityType 用户身份类型
     * @param money        支付金额
     * @param cityId       用户所在城市Id
     * @return 微信支付请求签名
     */
    @RequestMapping(value = "/recharge/pay", method = RequestMethod.POST)
    public ResultDTO<Object> wechatPreDepositRecharge(HttpServletRequest req, Long userId, Integer identityType, Double money, Long cityId) {

        logger.info("wechatPreDepositRecharge CALLED,微信充值预存款，入参 userId:{} identityType:{} money{} cityId{}", userId, identityType, money, cityId);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("wechatPreDepositRecharge OUT,微信充值预存款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("wechatPreDepositRecharge OUT,微信充值预存款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == money || money <= 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "金额不正确！", null);
            logger.info("wechatPreDepositRecharge OUT,微信充值预存款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == cityId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市信息不能为空！", null);
            logger.info("wechatPreDepositRecharge OUT,微信充值预存款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        String totalFee = CountUtil.retainTwoDecimalPlaces(money);
        Double totalFeeParse = Double.parseDouble(totalFee);
        String outTradeNo = OrderUtils.generateRechargeNumber(cityId);

        PaymentDataDO paymentDataDO = new PaymentDataDO(userId, outTradeNo, null, identityType, AppApplicationConstant.wechatReturnUrlAsnyc,
                totalFeeParse, PaymentDataStatus.WAIT_PAY, OnlinePayType.WE_CHAT, "");
        this.paymentDataService.save(paymentDataDO);

        RechargeOrder rechargeOrder = rechargeService.createRechargeOrder(identityType, userId, money, outTradeNo);
        rechargeService.saveRechargeOrder(rechargeOrder);

        try {
            SortedMap<String, Object> secondSignMap = (SortedMap<String, Object>) WechatPrePay.wechatSign(outTradeNo,
                    paymentDataDO.getPaymentTypeDesc(), new BigDecimal(totalFeeParse), req);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, secondSignMap);
            logger.info("wechatPreDepositRecharge OUT,微信充值预存款成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,微信充值预存款!", null);
            logger.warn("wechatPreDepositRecharge EXCEPTION,微信充值预存款失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 微信欠款还款
     *
     * @param userId
     * @param identityType
     * @param orderNumber
     * @return
     */
    @PostMapping(value = "/repayment/pay", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> wechatDebtRepayments(HttpServletRequest req, Long userId, Integer identityType, String orderNumber) {

        logger.info("wechatDebtRepayments CALLED,微信欠款还款，入参 userId:{} identityType:{} orderNumber:{}", userId, identityType, orderNumber);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("wechatDebtRepayments OUT,微信欠款还款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("wechatDebtRepayments OUT,微信欠款还款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(orderNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单编号不能为空！", null);
            logger.info("wechatDebtRepayments OUT,微信欠款还款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        OrderArrearsAuditDO orderArrearsAuditDO = arrearsAuditService.findArrearsByUserIdAndOrderNumber(userId, orderNumber);
        if (null == orderArrearsAuditDO) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此欠款记录！", null);
            logger.info("wechatDebtRepayments OUT,微信欠款还款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        OrderBillingDetails orderBillingDetails = appOrderService.getOrderBillingDetail(orderNumber);
        if (null == orderBillingDetails){
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "为查询到此订单账单明细！", null);
            logger.info("wechatDebtRepayments OUT,微信欠款还款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Double money = CountUtil.sub(orderArrearsAuditDO.getOrderMoney(),orderArrearsAuditDO.getRealMoney());
        if (!orderBillingDetails.getArrearage().equals(money)){
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "欠款金额有误，请联系管理员核查！", null);
            logger.info("wechatDebtRepayments OUT,微信欠款还款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        String totlefee = CountUtil.retainTwoDecimalPlaces(orderBillingDetails.getArrearage());
        Double totlefeeParse = Double.parseDouble(totlefee);
        String outTradeNo = orderNumber.replaceAll("_XN", "_HK");
        PaymentDataDO paymentData = paymentDataService.findPaymentDataDOByOrderNumber(orderNumber);
        if (null != paymentData){
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该订单已经使用过第三方支付，不能再进行支付！", null);
            logger.info("wechatDebtRepayments OUT,微信欠款还款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        PaymentDataDO paymentDataDO = new PaymentDataDO();
        paymentDataDO.setUserId(userId);
        paymentDataDO.setOutTradeNo(outTradeNo);
        paymentDataDO.setOrderNumber(orderNumber);
        if (outTradeNo.contains("_HK")) {
            paymentDataDO.setPaymentType(PaymentDataType.REPAYMENT);
        }
        paymentDataDO.setAppIdentityType(AppIdentityType.getAppIdentityTypeByValue(identityType));
        paymentDataDO.setNotifyUrl(AppApplicationConstant.wechatReturnUrlAsnyc);
        paymentDataDO.setPaymentType(PaymentDataType.REPAYMENT);
        paymentDataDO.setPaymentTypeDesc(PaymentDataType.REPAYMENT.getDescription());
        paymentDataDO.setTotalFee(Double.parseDouble(totlefee));
        paymentDataDO.setTradeStatus(PaymentDataStatus.WAIT_PAY);
        paymentDataDO.setOnlinePayType(OnlinePayType.WE_CHAT);
        paymentDataDO.setCreateTime(LocalDateTime.now());
        this.paymentDataService.save(paymentDataDO);

        try {
            SortedMap<String, Object> secondSignMap = (SortedMap<String, Object>) WechatPrePay.wechatSign(outTradeNo, paymentDataDO.getPaymentTypeDesc(),
                    new BigDecimal(totlefeeParse), req);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, secondSignMap);
            logger.info("wechatDebtRepayments OUT,微信欠款还款成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,微信欠款还款失败!", null);
            logger.warn("wechatDebtRepayments EXCEPTION,微信欠款还款失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 微信退款方法
     * 已移动到 onlinePayService
     * @param userId
     * @param identityType
     * @param money
     * @return
     */
    @Deprecated
    public Map<String, String> wechatReturnMoney(HttpServletRequest req, HttpServletResponse response, Long userId, Integer identityType, Double money, String orderNo, String refundNo) {
        Double totlefee = appOrderService.getAmountPayableByOrderNumber(orderNo);
        String totlefeeFormat = CountUtil.retainTwoDecimalPlaces(totlefee);
        Double totlefeeParse = Double.parseDouble(totlefeeFormat);
        String subject = "订单退款";

        Map<String, String> map = new HashMap<>();
        PaymentDataDO paymentDataDO = new PaymentDataDO(userId, orderNo, refundNo, identityType, null,
                money, PaymentDataStatus.WAIT_REFUND, OnlinePayType.WE_CHAT, subject);
        this.paymentDataService.save(paymentDataDO);

        try {
            Map<String, Object> resultMap = WechatPrePay.wechatRefundSign(
                    orderNo, refundNo, new BigDecimal(totlefeeParse), new BigDecimal(money));
            logger.debug("******微信退款签名***** OUT, 出参 sign:{}", resultMap);
            if (resultMap != null) {
                //状态是否成功
                if ("SUCCESS".equalsIgnoreCase(resultMap.get("return_code").toString())) {
                    if ("SUCCESS".equalsIgnoreCase(resultMap.get("result_code").toString())) {
                        response.getWriter().write(WechatUtil.setXML("SUCCESS", null));
                        //取出map中的参数，订单号
                        String outTradeNo = resultMap.get("outTradeNo").toString();
                        logger.debug("******微信返回参数订单号***** OUT, 出参 outTradeNo:{}", outTradeNo);
                        //退单号
                        String outRefundNo = resultMap.get("out_refund_no").toString();
                        logger.debug("******微信返回参数退单号***** OUT, 出参 outRefundNo:{}", outRefundNo);
                        //微信交易号
                        String tradeNo = resultMap.get("refund_id").toString();
                        logger.debug("******微信返回参数退单交易号***** OUT, 出参 tradeNo:{}", tradeNo);
                        //订单金额
                        String totalFee = resultMap.get("total_fee").toString();
                        logger.debug("******微信返回参数订单金额***** OUT, 出参 totalFee:{}", totalFee);
                        //申请退单金额
                        String refundFee = resultMap.get("refund_fee").toString();
                        logger.debug("******微信返回参数退单金额***** OUT, 出参 refundFee:{}", refundFee);
                        //微信交易状态
                        String tradeStatus = resultMap.get("result_code").toString();
                        logger.debug("******微信返回参数交易状态***** OUT, 出参 tradeStatus:{}", tradeStatus);
                        //修改退单状态
                        returnOrderService.updateReturnOrderStatus(outRefundNo, AppReturnOrderStatus.FINISHED);

                        paymentDataDO.setTradeNo(tradeNo);
                        paymentDataDO.setNotifyTime(new Date());
                        paymentDataDO.setTradeStatus(PaymentDataStatus.REFUND_SUCCESS);
                        this.paymentDataService.updateByTradeStatusIsWaitRefund(paymentDataDO);

                        map.put("code", "SUCCESS");
                        map.put("number", tradeNo);
                        map.put("money", refundFee);
                        return map;
                    }
                }
            }
            response.getWriter().write(WechatUtil.setXML("FAIL", "参数格式校验错误"));
            paymentDataDO.setRemarks(resultMap.get("return_msg").toString());
            paymentDataDO.setTradeStatus(PaymentDataStatus.REFUND_FAIL);
            this.paymentDataService.updateByTradeStatusIsWaitRefund(paymentDataDO);
            logger.warn("{}", resultMap.get("err_code").toString());
            logger.warn("{}", resultMap.get("err_code_des").toString());
            map.put("code", "FAILURE");
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", "FAILURE");
            return map;
        }
    }

    /**
     * 微信回调接口
     *
     * @param request  请求对象
     * @param response 响应对象
     */
    @PostMapping(value = "/return/async", produces = "application/json;charset=UTF-8")
    public void weChatReturnSync(HttpServletRequest request, HttpServletResponse response) {

        logger.info("weChatReturnSync CALLED,微信支付异步回调接口，入参 request:{},response:{}", request, response);

        try {
            //获取响应的信息<为XML格式>转化成Map
            String result = WechatUtil.streamToString(request.getInputStream());
            Map resultMap = WechatUtil.doXMLParse(result);

            if (resultMap != null) {
                //状态是否成功
                if ("SUCCESS".equalsIgnoreCase(resultMap.get("result_code").toString())) {
                    //是否匹配证书
                    if (WechatUtil.verifyNotify(resultMap)) {
                        logger.info("weChatReturnSync,微信支付异步回调接口,回调参数:{}", resultMap);
                        //取出map中的参数，订单号
                        String outTradeNo = resultMap.get("out_trade_no").toString();
                        logger.info("weChatReturnSync,微信支付异步回调接口,订单号:{}", outTradeNo);
                        //微信交易号
                        String tradeNo = resultMap.get("transaction_id").toString();
                        logger.info("weChatReturnSync,微信支付异步回调接口,交易号:{}", tradeNo);
                        //订单金额
                        String totalFee = resultMap.get("total_fee").toString();
                        logger.info("weChatReturnSync,微信支付异步回调接口,订单金额:{}", totalFee);
                        //微信交易状态
                        String tradeStatus = resultMap.get("result_code").toString();
                        logger.info("weChatReturnSync,微信支付异步回调接口,交易状态:{}", tradeStatus);
                        //转换金额为Double
                        Double totalFeeParse = CountUtil.div(Double.parseDouble(totalFee), 100D);

                        List<PaymentDataDO> paymentDataDOList = this.paymentDataService.findByOutTradeNoAndTradeStatus(outTradeNo, PaymentDataStatus.WAIT_PAY);

                        if (null != paymentDataDOList && paymentDataDOList.size() > 0) {
                            PaymentDataDO paymentDataDO = paymentDataDOList.get(0);
                            //微信支付异步多次通知可能造成下面业务操作重复时解除下面封印
//                            if (PaymentDataStatus.TRADE_SUCCESS.equals(paymentDataDO.getOnlinePayType())){
//                                return;
//                            }

                            //如果已处理就跳过处理代码
                            if (!outTradeNo.contains("_CZ")) {
                                List<PaymentDataDO> paymentDataList = this.paymentDataService.findByOrderNoAndTradeStatus(paymentDataDO.getOrderNumber(), PaymentDataStatus.TRADE_SUCCESS);
                                if (null != paymentDataList && paymentDataList.size() > 0) {
                                    logger.warn("weChatReturnSync,微信支付异步回调接口");
                                    return ;
                                }
                            }
                            //判断是否是充值订单
                            if (outTradeNo.contains("_CZ")) {
                                logger.info("weChatReturnSync,微信支付异步回调接口,回调单据类型:{}", "预存款充值");
                                if (null != paymentDataDO.getId() && paymentDataDO.getTotalFee().equals(totalFeeParse)) {
                                    paymentDataDO.setTradeNo(tradeNo);
                                    paymentDataDO.setTradeStatus(PaymentDataStatus.TRADE_SUCCESS);
                                    paymentDataDO.setNotifyTime(new Date());
                                    this.paymentDataService.updateByTradeStatusIsWaitPay(paymentDataDO);
                                    //创建充值单收款
                                    RechargeReceiptInfo receiptInfo = rechargeService.createOnlinePayRechargeReceiptInfo(paymentDataDO, tradeStatus);
                                    //保存充值收款记录,并更新充值单相关信息
                                    supportService.handleRechargeOrderRelevantInfoAfterOnlinePauUp(receiptInfo, outTradeNo);
                                    logger.info("weChatReturnSync ,微信支付异步回调接口，支付数据记录信息:{}",
                                            paymentDataDO);
                                    if (paymentDataDO.getPaymentType().equals(PaymentDataType.CUS_PRE_DEPOSIT)) {
                                        this.appCustomerServiceImpl.preDepositRecharge(paymentDataDO, CustomerPreDepositChangeType.WECHAT_RECHARGE);
                                    } else if (paymentDataDO.getPaymentType().equals(PaymentDataType.ST_PRE_DEPOSIT)
                                            || paymentDataDO.getPaymentType().equals(PaymentDataType.DEC_PRE_DEPOSIT)) {
                                        this.appStoreService.preDepositRecharge(paymentDataDO, StorePreDepositChangeType.WECHAT_RECHARGE);
                                    }
                                    //将收款记录入拆单消息队列
                                    sinkSender.sendRechargeReceipt(outTradeNo);
                                }
                            } else if (outTradeNo.contains("_HK")) {
                                logger.info("weChatReturnSync,微信支付异步回调接口,回调单据类型:{}", "欠款还款");
                                if (null != paymentDataDO.getId() && paymentDataDO.getTotalFee().equals(totalFeeParse)) {
                                    paymentDataDO.setTradeNo(tradeNo);
                                    paymentDataDO.setTradeStatus(PaymentDataStatus.TRADE_SUCCESS);
                                    paymentDataDO.setNotifyTime(new Date());
                                    this.paymentDataService.updateByTradeStatusIsWaitPay(paymentDataDO);
                                    logger.info("weChatReturnSync ,微信支付异步回调接口，支付数据记录信息:{}",
                                            paymentDataDO);
                                    String orderNumber = outTradeNo.replaceAll("_HK", "_XN");
                                    appOrderService.saveWeChatOrderBillingPaymentDetails(orderNumber, totalFeeParse, tradeNo, outTradeNo);
                                    //2018-05-03 13:28:24 Jerry.Ren 修改这里收款拆单到Controller最后发送消息队列
                                    sinkSender.sendOrderReceipt(outTradeNo);
                                }
                            } else if (paymentDataDO.getOrderNumber().contains("_XN")) {
                                logger.info("weChatReturnSync,微信支付异步回调接口,回调单据类型:{}", "订单");
                                if (null != paymentDataDO.getId() && paymentDataDO.getTotalFee().equals(totalFeeParse)) {
                                    String orderNumber = paymentDataDO.getOrderNumber();
                                    paymentDataDO.setTradeNo(tradeNo);
                                    paymentDataDO.setTradeStatus(PaymentDataStatus.TRADE_SUCCESS);
                                    paymentDataDO.setNotifyTime(new Date());
                                    this.paymentDataService.updateByTradeStatusIsWaitPay(paymentDataDO);
                                    logger.info("weChatReturnSync ,微信支付异步回调接口，支付数据记录信息:{}",
                                            paymentDataDO);
                                    //处理第三方支付成功之后订单相关事务
                                    commonService.handleOrderRelevantBusinessAfterOnlinePayUp(orderNumber, tradeNo, tradeStatus, OnlinePayType.WE_CHAT);

                                    //发送订单到拆单消息队列
                                    sinkSender.sendOrder(orderNumber);

                                    //发送订单到WMS
                                    OrderBaseInfo baseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
                                    if (baseInfo.getDeliveryType() == AppDeliveryType.HOUSE_DELIVERY) {
                                        iCallWms.sendToWmsRequisitionOrderAndGoods(orderNumber);
                                    }

                                    // 激活订单赠送的产品券
                                    // productCouponService.activateCusProductCoupon(outTradeNo);
                                }
                            } else if (paymentDataDO.getPaymentType() == PaymentDataType.BILLPAY) {
                                logger.info("weChatReturnSync,微信支付异步回调接口,回调单据类型:{}", "账单还款");
                                if (null != paymentDataDO.getId() && paymentDataDO.getTotalFee().equals(totalFeeParse)) {
                                    String orderNumber = paymentDataDO.getOrderNumber();
                                    paymentDataDO.setTradeNo(tradeNo);
                                    paymentDataDO.setTradeStatus(PaymentDataStatus.TRADE_SUCCESS);
                                    paymentDataDO.setNotifyTime(new Date());
                                    this.paymentDataService.updateByTradeStatusIsWaitPay(paymentDataDO);
                                    logger.info("weChatReturnSync ,微信支付异步回调接口，支付数据记录信息 paymentDataDO:{}",
                                            paymentDataDO);
                                    //处理第三方支付成功之后订单相关事务
                                    billInfoService.handleBillRepaymentAfterOnlinePayUp(orderNumber, OnlinePayType.WE_CHAT, paymentDataDO.getAppIdentityType().getValue());
                                    //将收款记录入拆单消息队列
                                    sinkSender.sendRechargeReceipt(orderNumber);
                                    logger.warn("weChatReturnSync OUT,微信支付异步回调接口处理成功，出参 result:{}", "success");
                                }
                            }
                            //返回响应成功的讯息
                            response.getWriter().write(WechatUtil.setXML("SUCCESS", null));
                            logger.info("weChatReturnSync,微信支付异步回调接口,回调成功! ");
                        } else {
                            logger.warn("weChatReturnSync,微信支付异步回调接口,没有找到该订单:{}", outTradeNo);
                            response.getWriter().write(WechatUtil.setXML("FAIL", "outTradeNo不存在!"));
                        }
                    } else {
                        logger.warn("weChatReturnSync,微信支付异步回调接口,签名失败");
                        response.getWriter().write(WechatUtil.setXML("FAIL", "签名失败"));
                    }
                } else {
                    logger.warn("支付失败,错误码:{}", resultMap.get("err_code").toString());
                    logger.warn("错误码说明:{}", resultMap.get("err_code_des").toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("{}", e);
        }
    }

    /**
     * @title   微信支付账单还款
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/6/28
     */
    @RequestMapping(value = "/bill/pay", method = RequestMethod.POST)
    public ResultDTO<Object> billWeChatPay(HttpServletRequest req, Long userId, Integer identityType, String repaymentNo) {

        logger.info("billWeChatPay CALLED,微信支付账单还款信息提交,入参 userId:{}, identityType:{}, repaymentNo:{}",
                userId, identityType, repaymentNo);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("billWeChatPay OUT,微信支付账单还款信息提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("billWeChatPay OUT,微信支付账单还款信息提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == repaymentNo) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收款单号不能为空！", null);
            logger.info("billWeChatPay OUT,微信支付账单还款信息提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        BillRepaymentInfoDO billRepaymentInfoDO = this.billInfoService.findBillRepaymentInfoByRepaymentNo(repaymentNo);

        if (null == billRepaymentInfoDO || null == billRepaymentInfoDO.getBillNo() || null == billRepaymentInfoDO.getOnlinePayAmount() ||
                billRepaymentInfoDO.getOnlinePayAmount() < AppConstant.PAY_UP_LIMIT) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "支付金额错误！", null);
            logger.info("billWeChatPay OUT,微信支付账单还款信息提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null != billRepaymentInfoDO.getIsPaid() && billRepaymentInfoDO.getIsPaid()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "已支付，请勿重复提交！", null);
            logger.info("billWeChatPay OUT,微信支付账单还款信息提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Double totalFeeParse = billRepaymentInfoDO.getOnlinePayAmount();
        String outTradeNo = OrderUtils.generatePayNumber();

        PaymentDataDO paymentDataDO = new PaymentDataDO(userId, outTradeNo, repaymentNo, identityType, AppApplicationConstant.wechatReturnUrlAsnyc,
                totalFeeParse, PaymentDataStatus.WAIT_PAY, OnlinePayType.WE_CHAT, "账单还款");
        paymentDataDO.setPaymentType(PaymentDataType.BILLPAY);
        paymentDataDO.setPaymentTypeDesc(PaymentDataType.BILLPAY.getDescription());
        this.paymentDataService.save(paymentDataDO);

        try {
            SortedMap<String, Object> secondSignMap = (SortedMap<String, Object>) WechatPrePay.wechatSign(outTradeNo, paymentDataDO.getPaymentTypeDesc(),
                    new BigDecimal(totalFeeParse), req);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, secondSignMap);
            logger.info("billWeChatPay OUT,微信支付账单还款信息提交成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,微信支付账单还款信息提交失败!", null);
            logger.warn("billWeChatPay EXCEPTION,微信支付账单还款信息提交失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }

    }
}
