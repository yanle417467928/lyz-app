package cn.com.leyizhuang.app.web.controller.wechatpay;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.pay.wechat.sign.WechatPrePay;
import cn.com.leyizhuang.app.core.pay.wechat.util.WechatUtil;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.pojo.PaymentDataDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderArrearsAuditDO;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.CountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public ResultDTO<Object> orderWechatPay(HttpServletRequest req, Long userId, Integer identityType, Double amountPayable, String orderNumber) {

        logger.info("orderWechatPay CALLED,订单微信支付信息提交,入参 userId:{}, identityType:{}, money:{}, orderNumber:{}",
                userId, identityType, amountPayable, orderNumber);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("orderWechatPay OUT,微信支付订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("orderWechatPay OUT,微信支付订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == amountPayable || amountPayable <= 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "金额不正确！", null);
            logger.info("orderWechatPay OUT,微信支付订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == orderNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单不能为空！", null);
            logger.info("orderWechatPay OUT,微信支付订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Double totalFee = appOrderService.getAmountPayableByOrderNumber(orderNumber);
        if (totalFee == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查到该订单！", null);
            logger.info("orderWechatPay OUT,微信支付订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (!totalFee.equals(amountPayable)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "支付金额与订单金额不匹配！", null);
            logger.info("orderWechatPay OUT,微信支付订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        String totalFeeFormat = CountUtil.retainTwoDecimalPlaces(totalFee);
        Double totalFeeParse = Double.parseDouble(totalFeeFormat);


        PaymentDataDO paymentDataDO = new PaymentDataDO(userId, orderNumber, identityType, AppApplicationConstant.wechatReturnUrlAsnyc,
                totalFeeParse, PaymentDataStatus.WAIT_PAY, OnlinePayType.WE_CHAT, "订单支付");
        this.paymentDataService.save(paymentDataDO);

        try {
            SortedMap<String, Object> secondSignMap = (SortedMap<String, Object>) WechatPrePay.wechatSign(orderNumber, paymentDataDO.getPaymentTypeDesc(),
                    new BigDecimal(totalFeeParse), req);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, secondSignMap);
            logger.info("orderWechatPay OUT,微信支付订单成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,微信支付订单失败!", null);
            logger.warn("orderWechatPay EXCEPTION,微信支付订单失败，出参 resultDTO:{}", resultDTO);
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

        PaymentDataDO paymentDataDO = new PaymentDataDO(userId, outTradeNo, identityType, AppApplicationConstant.wechatReturnUrlAsnyc,
                totalFeeParse, PaymentDataStatus.WAIT_PAY, OnlinePayType.WE_CHAT, "");
        this.paymentDataService.save(paymentDataDO);

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
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "为查询到此欠款记录！", null);
            logger.info("wechatDebtRepayments OUT,微信欠款还款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        String totlefee = CountUtil.retainTwoDecimalPlaces(orderArrearsAuditDO.getOrderMoney());
        Double totlefeeParse = Double.parseDouble(totlefee);
        String outTradeNo = orderNumber.replaceAll("_XN", "_HK");

        PaymentDataDO paymentDataDO = new PaymentDataDO();
        paymentDataDO.setUserId(userId);
        paymentDataDO.setOutTradeNo(outTradeNo);
        if (outTradeNo.contains("_HK")) {
            paymentDataDO.setPaymentType(PaymentDataType.REPAYMENT);
        }
        paymentDataDO.setAppIdentityType(AppIdentityType.getAppIdentityTypeByValue(identityType));
        paymentDataDO.setNotifyUrl(AppApplicationConstant.alipayReturnUrlAsnyc);
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
     *
     * @param userId
     * @param identityType
     * @param money
     * @return
     */
    public Map<String, String> wechatReturnMoney(HttpServletRequest req, HttpServletResponse response, Long userId, Integer identityType, Double money, String orderNo, String refundNo) {
        Double totlefee = appOrderService.getAmountPayableByOrderNumber(orderNo);
        String totlefeeFormat = CountUtil.retainTwoDecimalPlaces(totlefee);
        Double totlefeeParse = Double.parseDouble(totlefeeFormat);
        String subject = "微信退款";

        Map<String, String> map = new HashMap<>();
        PaymentDataDO paymentDataDO = new PaymentDataDO(userId, refundNo, identityType, null,
                money, PaymentDataStatus.WAIT_REFUND, OnlinePayType.WE_CHAT, "微信退款");
        this.paymentDataService.save(paymentDataDO);

        try {
            SortedMap<String, Object> resultMap = (SortedMap<String, Object>) WechatPrePay.wechatRefundSign(
                    orderNo, refundNo, new BigDecimal(totlefeeParse), new BigDecimal(money), req);

            if (resultMap != null) {
                //状态是否成功
                if ("SUCCESS".equalsIgnoreCase(resultMap.get("result_code").toString())) {

                    response.getWriter().write(WechatUtil.setXML("SUCCESS", "OK"));
                    //取出map中的参数，订单号
                    String outTradeNo = resultMap.get("out_trade_no").toString();
                    //退单号
                    String outRefundNo = resultMap.get("out_refund_no").toString();
                    //微信交易号
                    String tradeNo = resultMap.get("refund_id").toString();
                    //订单金额
                    String totalFee = resultMap.get("total_fee").toString();
                    //退单金额
                    String refundFee = resultMap.get("refund_fee").toString();
                    //微信交易状态
                    String tradeStatus = resultMap.get("result_code").toString();

                    //修改退单状态
                    ReturnOrderBaseInfo returnOrderBaseInfo = returnOrderService.queryByReturnNo(outRefundNo);
                    returnOrderBaseInfo.setReturnStatus(AppReturnOrderStatus.FINISHED);
                    returnOrderService.modifyReturnOrderBaseInfo(returnOrderBaseInfo);
                    //修改退货单金额明细流水单号和时间
//                    returnOrderService.modifyOrderReturnBillingDetail(new ReturnOrderBillingDetail(tradeNo, Calendar.getInstance().getTime()));

                    List<PaymentDataDO> paymentDataDOList = this.paymentDataService.findByOutTradeNoAndTradeStatus(refundNo, PaymentDataStatus.WAIT_REFUND);
                    PaymentDataDO dataDO = paymentDataDOList.get(0);
                    dataDO.setTradeNo(tradeNo);
                    dataDO.setTradeStatus(PaymentDataStatus.REFUND_SUCCESS);
                    this.paymentDataService.updateByTradeStatusIsWaitPay(dataDO);

                    map.put("code", "SUCCESS");
                    map.put("number", tradeNo);
                    map.put("money", refundFee);
                    return map;
                } else {
                    PaymentDataDO paymentDataDO1 = new PaymentDataDO();
                    paymentDataDO1.setTradeStatus(PaymentDataStatus.REFUND_FAIL);
                    this.paymentDataService.updateByTradeStatusIsWaitPay(paymentDataDO);
                    response.getWriter().write(WechatUtil.setXML("FAIL", "签名失败"));
                    map.put("code", "FAILURE");
                    return map;
                }
            } else {
                logger.warn("{}", resultMap.get("err_code").toString());
                logger.warn("{}", resultMap.get("err_code_des").toString());
                map.put("code", "FAILURE");
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", "FAILURE");
            return map;
        }
    }

    /**
     * 接受微信调用后返回参数的回调接口
     *
     * @param request  请求对象
     * @param response 响应对象
     */
    @PostMapping(value = "/return/async", produces = "application/json;charset=UTF-8")
    public void wechatReturnSync(HttpServletRequest request, HttpServletResponse response) {

        logger.info("wechatReturnSync CALLED,接受微信调用后返回参数的回调接口，入参 request:{},response:{}", request, response);

        try {
            //获取响应的信息<为XML格式>转化成Map
            String result = WechatUtil.streamToString(request.getInputStream());
            Map resultMap = WechatUtil.doXMLParse(result);

            if (resultMap != null) {
                //状态是否成功
                if ("SUCCESS".equalsIgnoreCase(resultMap.get("result_code").toString())) {
                    //是否匹配证书
                    if (WechatUtil.verifyNotify(resultMap)) {
                        //返回响应成功的讯息
                        response.getWriter().write(WechatUtil.setXML("SUCCESS", "OK"));
                        //取出map中的参数，订单号
                        String outTradeNo = resultMap.get("out_trade_no").toString();
                        //微信交易号
                        String tradeNo = resultMap.get("transaction_id").toString();
                        //订单金额
                        String totalFee = resultMap.get("total_fee").toString();
                        //微信交易状态
                        String tradeStatus = resultMap.get("result_code").toString();
                        //转换金额为Double
                        Double totlefeeParse = Double.parseDouble(totalFee);

                        List<PaymentDataDO> paymentDataDOList = this.paymentDataService.findByOutTradeNoAndTradeStatus(outTradeNo, PaymentDataStatus.WAIT_PAY);

                        if (null != paymentDataDOList && paymentDataDOList.size() > 0) {
                            PaymentDataDO paymentDataDO = paymentDataDOList.get(0);
                            //判断是否是充值订单
                            if (outTradeNo.contains("_CZ")) {

                                appCustomerServiceImpl.preDepositRecharge(paymentDataDO, CustomerPreDepositChangeType.WECHAT_RECHARGE);

                            } else if (outTradeNo.contains("_HK")) {
                                String orderNumber = outTradeNo.replaceAll("_HK", "_XN");
                                appOrderService.saveOrderBillingPaymentDetails(orderNumber, totlefeeParse, tradeNo, outTradeNo);
                            } else {
                                if (null != paymentDataDO.getId() && paymentDataDO.getTotalFee().equals(Double.parseDouble(totalFee))) {
                                    paymentDataDO.setTradeNo(tradeNo);
                                    paymentDataDO.setTradeStatus(PaymentDataStatus.TRADE_SUCCESS);
                                    paymentDataDO.setNotifyTime(new Date());
                                    this.paymentDataService.updateByTradeStatusIsWaitPay(paymentDataDO);
                                    logger.info("alipayReturnAsync ,支付宝支付回调接口，支付数据记录信息 paymentDataDO:{}",
                                            paymentDataDO);
                                    commonService.handleOrderRelevantBusinessAfterOnlinePayUp(outTradeNo, tradeNo, tradeStatus, OnlinePayType.WE_CHAT);
                                }

                            }
                        }
                    } else {
                        response.getWriter().write(WechatUtil.setXML("FAIL", "签名失败"));
                    }
                } else {
                    logger.warn("支付是失败,错误码:{}", resultMap.get("err_code").toString());
                    logger.warn("错误码说明:{}", resultMap.get("err_code_des").toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("{}", e);
        }

    }

}
