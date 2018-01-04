package cn.com.leyizhuang.app.web.controller.alipay;

import cn.com.leyizhuang.app.core.config.AlipayConfig;
import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.pojo.PaymentDataDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderArrearsAuditDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.remote.webservice.ICallWms;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.CountUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author GenerationRoad
 * @date 2017/11/16
 */
@RestController
@RequestMapping(value = "/app/alipay")
public class AliPayController {
    private static final Logger logger = LoggerFactory.getLogger(AliPayController.class);

    @Resource
    private PaymentDataService paymentDataService;

    @Resource
    private AppCustomerService appCustomerServiceImpl;

    @Resource
    private AppStoreService appStoreServiceImpl;

    @Resource
    private ArrearsAuditService arrearsAuditService;

    @Resource
    private AppOrderService appOrderService;

    @Resource
    private CommonService commonService;

    @Resource
    private ICallWms iCallWms;
    /**
     * 支付宝充值生成充值单
     *
     * @param userId       用户id
     * @param identityType 身份类型
     * @param money        金额
     * @param cityId       城市id
     * @return 支付宝客户端调取所需参数
     */
    @PostMapping(value = "/recharge/pay", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> preDepositRecharge(Long userId, Integer identityType, Double money, Long cityId) {

        logger.info("PreDepositRecharge CALLED,支付宝充值预存款提交数据，入参 userId:{} identityType:{} money:{} cityId:{}", userId, identityType, money, cityId);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("PreDepositRecharge OUT,支付宝充值预存款提交数据失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("PreDepositRecharge OUT,支付宝充值预存款提交数据失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == money && money <= 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "金额不正确！", null);
            logger.info("PreDepositRecharge OUT,支付宝充值预存款提交数据失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == cityId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市信息不能为空！", null);
            logger.info("PreDepositRecharge OUT,支付宝充值预存款提交数据失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            String totalFee = CountUtil.retainTwoDecimalPlaces(money);
            String outTradeNo = OrderUtils.generateRechargeNumber(cityId);
            PaymentDataDO paymentDataDO = new PaymentDataDO(userId, outTradeNo, null, identityType, AppApplicationConstant.alipayReturnUrlAsync,
                    Double.parseDouble(totalFee), PaymentDataStatus.WAIT_PAY, OnlinePayType.ALIPAY, "");
            this.paymentDataService.save(paymentDataDO);

            //serverUrl 非空，请求服务器地址（调试：http://openapi.alipaydev.com/gateway.do 线上：https://openapi.alipay.com/gateway.do ）
            //appId 非空，应用ID
            //privateKey 非空，私钥
            AlipayClient alipayClient = new DefaultAlipayClient(
                    AlipayConfig.serverUrl, AlipayConfig.appId,
                    AlipayConfig.privateKey, AlipayConfig.format, AlipayConfig.charset,
                    AlipayConfig.aliPublicKey, AlipayConfig.signType);
            //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
            AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
            //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setSubject(paymentDataDO.getPaymentType().getDescription());
            model.setOutTradeNo(outTradeNo);
            model.setTimeoutExpress("30m");
            model.setTotalAmount(totalFee);
            model.setProductCode(AlipayConfig.productCode);
            request.setBizModel(model);
            request.setNotifyUrl(AppApplicationConstant.alipayReturnUrlAsync);

            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            // System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, response.getBody());
            logger.info("PreDepositRecharge OUT,支付宝充值预存款提交数据成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,支付宝充值预存款提交数据失败!", null);
            logger.warn("PreDepositRecharge EXCEPTION,支付宝充值预存款提交数据失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 订单支付宝支付信息提交
     *
     * @param userId        用户id
     * @param identityType  身份类型
     * @param payableAmount 金额
     * @param orderNumber   订单号
     * @return 支付宝客户端调用相关信息
     */
    @PostMapping(value = "/order/pay", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> orderAlipay(Long userId, Integer identityType, Double payableAmount, String orderNumber) {

        logger.info("orderAlipay CALLED,订单支付宝支付信息提交,入参 userId:{}, identityType:{}, payableAmount:{}, orderNumber:{}",
                userId, identityType, payableAmount, orderNumber);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("orderAlipay OUT,订单支付宝支付信息提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("orderAlipay OUT,订单支付宝支付信息提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (!(null != payableAmount && payableAmount > 0)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "支付金额不正确！", null);
            logger.info("orderAlipay OUT,订单支付宝支付信息提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == orderNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单号不能为空！", null);
            logger.info("orderAlipay OUT,订单支付宝支付信息提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        String totalFee = CountUtil.retainTwoDecimalPlaces(payableAmount);
        PaymentDataDO paymentData = new PaymentDataDO();
        paymentData.setUserId(userId);
        paymentData.setOnlinePayType(OnlinePayType.ALIPAY);
        paymentData.setPaymentType(PaymentDataType.ORDER);
        paymentData.setPaymentTypeDesc(PaymentDataType.ORDER.getDescription());
        paymentData.setAppIdentityType(AppIdentityType.getAppIdentityTypeByValue(identityType));
        paymentData.setCreateTime(LocalDateTime.now());
        paymentData.setOutTradeNo(orderNumber);
        paymentData.setOrderNumber(orderNumber);
        paymentData.setTotalFee(Double.parseDouble(totalFee));
        paymentData.setTradeStatus(PaymentDataStatus.WAIT_PAY);
        paymentData.setNotifyUrl(AppApplicationConstant.alipayReturnUrlAsync);
        this.paymentDataService.save(paymentData);

        //serverUrl 非空，请求服务器地址（调试：http://openapi.alipaydev.com/gateway.do 线上：https://openapi.alipay.com/gateway.do ）
        //appId 非空，应用ID
        //privateKey 非空，私钥
        AlipayClient alipayClient = new DefaultAlipayClient(
                AlipayConfig.serverUrl, AlipayConfig.appId,
                AlipayConfig.privateKey, AlipayConfig.format, AlipayConfig.charset,
                AlipayConfig.aliPublicKey, AlipayConfig.signType);
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setSubject(paymentData.getPaymentType().getDescription());
        model.setOutTradeNo(orderNumber);
        model.setTimeoutExpress("30m");
        model.setTotalAmount(totalFee);
        model.setProductCode(AlipayConfig.productCode);
        request.setBizModel(model);
        request.setNotifyUrl(AppApplicationConstant.alipayReturnUrlAsync);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            //System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, response.getBody());
            logger.info("orderAlipay OUT,订单支付宝支付信息提交成功,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,支付宝充值预存款提交数据失败!", null);
            logger.warn("orderAlipay EXCEPTION,订单支付宝支付信息提交失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 支付宝异步通知
     *
     * @param request 支付宝请求我方携带的参数
     * @return 我方响应支付宝请求返回的结果
     */
    @RequestMapping(value = "/return/async")
    public String alipayReturnAsync(HttpServletRequest request) {

        logger.info("alipayReturnAsync OUT,支付宝支付回调接口，入参 request:{}", request);
        try {
            //获取支付宝POST过来反馈信息
            Map<String, String> params = new HashMap<String, String>();
            Map requestParams = request.getParameterMap();
            for (Iterator iterator = requestParams.keySet().iterator(); iterator.hasNext(); ) {
                String name = (String) iterator.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i]
                            : valueStr + values[i] + ",";
                }
                //乱码解决，这段代码在出现乱码时使用。
                //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
                params.put(name, valueStr);
            }
            //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
            //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
            boolean flag = AlipaySignature.rsaCheckV1(params, AlipayConfig.aliPublicKey, AlipayConfig.charset, AlipayConfig.signType);

            String out_trade_no = null;
            String trade_no = null;
            String trade_status = null;
            String total_fee = null;

            if (flag) {
                out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
                // 支付宝交易号
                trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
                // 交易状态
                trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
                total_fee = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

                logger.info("alipayReturnAsync,支付宝支付回调接口，入参 out_trade_no:{}, trade_no:{}, trade_status:{}, total_fee:{}",
                        out_trade_no, trade_no, trade_status, total_fee);

                List<PaymentDataDO> paymentDataList = this.paymentDataService.findByOutTradeNoAndTradeStatus(out_trade_no, PaymentDataStatus.TRADE_SUCCESS);
                if (null != paymentDataList && paymentDataList.size() > 0) {
                    logger.warn("alipayReturnAsync,支付宝支付回调接口，响应支付宝结果 result:{}", "success");
                    return "success";
                }
                if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {

                    PaymentDataDO paymentDataDO = new PaymentDataDO();
                    List<PaymentDataDO> paymentDataDOList = this.paymentDataService.findByOutTradeNoAndTradeStatus(out_trade_no, PaymentDataStatus.WAIT_PAY);
                    if (null != paymentDataDOList && paymentDataDOList.size() > 0) {
                        paymentDataDO = paymentDataDOList.get(0);
                    }
                    if (out_trade_no.contains("_CZ")) {
                        //充值加预存款和日志
                        if (null != paymentDataDO.getId() && paymentDataDO.getTotalFee().equals(Double.parseDouble(total_fee))) {
                            paymentDataDO.setTradeNo(trade_no);
                            paymentDataDO.setTradeStatus(PaymentDataStatus.TRADE_SUCCESS);
                            paymentDataDO.setNotifyTime(new Date());
                            this.paymentDataService.updateByTradeStatusIsWaitPay(paymentDataDO);
                            logger.info("alipayReturnAsync ,支付宝支付回调接口，支付数据记录信息 paymentDataDO:{}",
                                    paymentDataDO);
                            if (paymentDataDO.getPaymentType().equals(PaymentDataType.CUS_PRE_DEPOSIT)) {
                                this.appCustomerServiceImpl.preDepositRecharge(paymentDataDO, CustomerPreDepositChangeType.ALIPAY_RECHARGE);
                            } else if (paymentDataDO.getPaymentType().equals(PaymentDataType.ST_PRE_DEPOSIT)
                                    || paymentDataDO.getPaymentType().equals(PaymentDataType.DEC_PRE_DEPOSIT)) {
                                this.appStoreServiceImpl.preDepositRecharge(paymentDataDO, StorePreDepositChangeType.ALIPAY_RECHARGE);
                            }
                            logger.warn("alipayReturnAsync OUT,支付宝支付回调接口处理成功，出参 result:{}", "success");
                            return "success";
                        }

                    } else if (out_trade_no.contains("_HK")) {
                        if (null != paymentDataDO.getId() && paymentDataDO.getTotalFee().equals(Double.parseDouble(total_fee))) {
                            paymentDataDO.setTradeNo(trade_no);
                            paymentDataDO.setTradeStatus(PaymentDataStatus.TRADE_SUCCESS);
                            paymentDataDO.setNotifyTime(new Date());
                            this.paymentDataService.updateByTradeStatusIsWaitPay(paymentDataDO);
                            logger.info("alipayReturnAsync ,支付宝支付回调接口，支付数据记录信息 paymentDataDO:{}",
                                    paymentDataDO);
                            String orderNumber = out_trade_no.replaceAll("_HK", "_XN");
                            Double money = paymentDataDO.getTotalFee();
                            appOrderService.saveOrderBillingPaymentDetails(orderNumber, money, trade_no, out_trade_no);
                            logger.warn("alipayReturnAsync OUT,支付宝支付回调接口处理成功，出参 result:{}", "success");
                            return "success";
                        }
                    } else if (out_trade_no.contains("_XN")) {
                        if (null != paymentDataDO.getId() && paymentDataDO.getTotalFee().equals(Double.parseDouble(total_fee))) {
                            paymentDataDO.setTradeNo(trade_no);
                            paymentDataDO.setTradeStatus(PaymentDataStatus.TRADE_SUCCESS);
                            paymentDataDO.setNotifyTime(new Date());
                            this.paymentDataService.updateByTradeStatusIsWaitPay(paymentDataDO);
                            logger.info("alipayReturnAsync ,支付宝支付回调接口，支付数据记录信息 paymentDataDO:{}",
                                    paymentDataDO);
                            //发送订单到WMS
                            OrderBaseInfo baseInfo = appOrderService.getOrderByOrderNumber(out_trade_no);
                            if (baseInfo.getDeliveryType() == AppDeliveryType.HOUSE_DELIVERY) {
                                iCallWms.sendToWmsRequisitionOrderAndGoods(out_trade_no);
                            }
                            commonService.handleOrderRelevantBusinessAfterOnlinePayUp(out_trade_no, trade_no, trade_status, OnlinePayType.ALIPAY);
                            logger.warn("alipayReturnAsync OUT,支付宝支付回调接口处理成功，出参 result:{}", "success");
                            return "success";
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("{}", e);
            throw new RuntimeException("支付宝支付回调接口处理失败");
        }
        logger.warn("alipayReturnAsync Exception,支付宝支付回调接口处理失败，出参 result:{}", "fail");
        return "fail";
    }

    /**
     * 支付宝欠款还款
     *
     * @param userId       用户id
     * @param identityType 用户类型
     * @param orderNumber  订单号
     * @return 支付宝客户端调用相关信息
     */
    @PostMapping(value = "/repayment/pay", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> aliPayDebtRepayments(Long userId, Integer identityType, String orderNumber) {

        logger.info("AliPayDebtRepayments CALLED,支付宝欠款还款，入参 userId:{} identityType:{} orderNumber:{}", userId, identityType, orderNumber);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("AliPayDebtRepayments OUT,支付宝欠款还款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("AliPayDebtRepayments OUT,支付宝欠款还款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(orderNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单编号不能为空！", null);
            logger.info("AliPayDebtRepayments OUT,支付宝欠款还款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        OrderArrearsAuditDO orderArrearsAuditDO = arrearsAuditService.findArrearsByUserIdAndOrderNumber(userId, orderNumber);
        if (null == orderArrearsAuditDO) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此欠款记录！", null);
            logger.info("AliPayDebtRepayments OUT,支付宝欠款还款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        String totalFee = CountUtil.retainTwoDecimalPlaces(orderArrearsAuditDO.getOrderMoney());
        String outTradeNo = orderNumber.replaceAll("_XN", "_HK");
        PaymentDataDO paymentDataDO = new PaymentDataDO();
        paymentDataDO.setUserId(userId);
        paymentDataDO.setOutTradeNo(outTradeNo);
        if (outTradeNo.contains("_HK")) {
            paymentDataDO.setPaymentType(PaymentDataType.REPAYMENT);
        }
        paymentDataDO.setOrderNumber(orderNumber);
        paymentDataDO.setAppIdentityType(AppIdentityType.getAppIdentityTypeByValue(identityType));
        paymentDataDO.setNotifyUrl(AppApplicationConstant.alipayReturnUrlAsync);
        paymentDataDO.setPaymentType(PaymentDataType.REPAYMENT);
        paymentDataDO.setPaymentTypeDesc(PaymentDataType.REPAYMENT.getDescription());
        paymentDataDO.setTotalFee(Double.parseDouble(totalFee));
        paymentDataDO.setTradeStatus(PaymentDataStatus.WAIT_PAY);
        paymentDataDO.setOnlinePayType(OnlinePayType.ALIPAY);
        paymentDataDO.setCreateTime(LocalDateTime.now());
        this.paymentDataService.save(paymentDataDO);

        //serverUrl 非空，请求服务器地址（调试：http://openapi.alipaydev.com/gateway.do 线上：https://openapi.alipay.com/gateway.do ）
        //appId 非空，应用ID
        //privateKey 非空，私钥
        AlipayClient alipayClient = new DefaultAlipayClient(
                AlipayConfig.serverUrl, AlipayConfig.appId,
                AlipayConfig.privateKey, AlipayConfig.format, AlipayConfig.charset,
                AlipayConfig.aliPublicKey, AlipayConfig.signType);
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setSubject(paymentDataDO.getPaymentType().getDescription());
        model.setOutTradeNo(outTradeNo);
        model.setTimeoutExpress("30m");
        model.setTotalAmount(totalFee);
        model.setProductCode(AlipayConfig.productCode);
        request.setBizModel(model);
        request.setNotifyUrl(AppApplicationConstant.alipayReturnUrlAsync);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
//            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, response.getBody());
            logger.info("AliPayDebtRepayments OUT,支付宝欠款还款成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,支付宝欠款还款失败!", null);
            logger.warn("AliPayDebtRepayments EXCEPTION,支付宝欠款还款失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

}
