package cn.com.leyizhuang.app.web.controller.settlement;

import cn.com.leyizhuang.app.core.constant.ApplicationConstant;
import cn.com.leyizhuang.app.core.constant.PaymentDataStatus;
import cn.com.leyizhuang.app.core.constant.PaymentDataType;
import cn.com.leyizhuang.app.core.pay.wechat.sign.WechatPrePay;
import cn.com.leyizhuang.app.core.pay.wechat.util.WechatUtil;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.pojo.PaymentDataDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.service.AppCustomerService;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.PaymentDataService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.CountUtil;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * @author Jerry.Ren
 * Notes:第三方支付调用接口
 * Created with IntelliJ IDEA.
 * Date: 2017/11/8.
 * Time: 17:43.
 */
@RestController
@RequestMapping("/app/pay")
public class PayController {

    private static final Logger logger = LoggerFactory.getLogger(PayController.class);

    @Resource
    private PaymentDataService paymentDataServiceImpl;

    @Resource
    private AppCustomerService appCustomerServiceImpl;

    @Resource
    private AppStoreService appStoreServiceImpl;

    @Resource
    private AppOrderService appOrderService;

    /**
     * 微信支付订单
     *
     * @param req          请求对象
     * @param userId       用户id
     * @param identityType 用户身份类型
     * @param money        支付金额
     * @param orderNo      订单号
     * @return 微信支付请求签名
     */
    @RequestMapping(value = "/wechat/online", method = RequestMethod.POST)
    public ResultDTO<Object> wechatOnlinePayment(HttpServletRequest req, Long userId, Integer identityType, Double money, String orderNo) {

        logger.info("wechatOnlinePayment CALLED,微信支付订单，入参 userId:{} identityType:{} money{} orderNo{}", userId, identityType, money, orderNo);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("wechatOnlinePayment OUT,微信支付订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("wechatOnlinePayment OUT,微信支付订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == money || money <= 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "金额不正确！", null);
            logger.info("wechatOnlinePayment OUT,微信支付订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == orderNo) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单不能为空！", null);
            logger.info("wechatOnlinePayment OUT,微信支付订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Double totlefee = appOrderService.getAmountPayableByOrderNumber(orderNo);
        if (totlefee == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查到该订单！", null);
            logger.info("wechatOnlinePayment OUT,微信支付订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (!totlefee.equals(money)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "支付金额与订单金额不匹配！", null);
            logger.info("wechatOnlinePayment OUT,微信支付订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        String totlefeeFormat = CountUtil.retainTwoDecimalPlaces(totlefee);
        Double totlefeeParse = Double.parseDouble(totlefeeFormat);
        String subject = "微信订单支付";

        PaymentDataDO paymentDataDO = new PaymentDataDO(userId, orderNo, identityType, ApplicationConstant.wechatReturnUrlAsnyc, subject,
                totlefeeParse, PaymentDataStatus.WAIT_PAY, "微信支付", "订单支付");
        this.paymentDataServiceImpl.save(paymentDataDO);

        try {
            SortedMap<String, Object> secondSignMap = (SortedMap<String, Object>) WechatPrePay.wechatSign(orderNo, new BigDecimal(totlefeeParse), req);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, secondSignMap);
            logger.info("wechatOnlinePayment OUT,微信支付订单成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,微信支付订单失败!", null);
            logger.warn("wechatOnlinePayment EXCEPTION,微信支付订单失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }

    }

    /**
     * 微信充值预存款
     * @param req 请求对象
     * @param userId 用户id
     * @param identityType 用户身份类型
     * @param money 支付金额
     * @param cityId 用户所在城市Id
     * @return 微信支付请求签名
     */
    @RequestMapping(value = "/wechat/recharge", method = RequestMethod.POST)
    public ResultDTO<Object> wechatPreDepositRecharge(HttpServletRequest req,Long userId, Integer identityType, Double money, Long cityId) {

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
        String totlefee = CountUtil.retainTwoDecimalPlaces(money);
        Double  totlefeeParse = Double.parseDouble(totlefee);
        String outTradeNo = OrderUtils.generateRechargeNumber(cityId);
        String subject = "预存款充值";

        PaymentDataDO paymentDataDO = new PaymentDataDO(userId, outTradeNo, identityType, ApplicationConstant.wechatReturnUrlAsnyc, subject,
                Double.parseDouble(totlefee), PaymentDataStatus.WAIT_PAY, "微信支付", "");
        this.paymentDataServiceImpl.save(paymentDataDO);

        try {
            SortedMap<String, Object> secondSignMap = (SortedMap<String, Object>) WechatPrePay.wechatSign(outTradeNo,new BigDecimal(totlefeeParse),req);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, secondSignMap);
            logger.info("wechatPreDepositRecharge OUT,微信充值预存款成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,微信充值预存款!", null);
            logger.warn("wechatPreDepositRecharge EXCEPTION,微信充值预存款失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 接受微信调用后返回参数的回调接口
     *
     * @param request 请求对象
     * @param response 响应对象
     */
    @PostMapping(value = "/wechat/return/async", produces = "application/json;charset=UTF-8")
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

                        //判断是否是充值订单
                        if (outTradeNo.contains("")) {

                        } else {
                            OrderBaseInfo order = appOrderService.getOrderByOrderNumber(outTradeNo);
                        }
                        List<PaymentDataDO> paymentDataDOList = this.paymentDataServiceImpl.findByOutTradeNoAndTradeStatus(outTradeNo, PaymentDataStatus.WAIT_PAY);
                        if (null != paymentDataDOList && paymentDataDOList.size() == 1) {
                            PaymentDataDO paymentDataDO = paymentDataDOList.get(0);
                            if ("SUCCESS".equals(tradeStatus) || "TRADE_SUCCESS".equals(tradeStatus)) {
                                if (paymentDataDO.getTotalFee().equals(Double.parseDouble(totalFee))) {
                                    paymentDataDO.setTradeNo(tradeNo);
                                    paymentDataDO.setTradeStatus(PaymentDataStatus.TRADE_SUCCESS);
                                    this.paymentDataServiceImpl.updateByTradeStatusIsWaitPay(paymentDataDO);

                                    //充值加预存款和日志
                                    if (paymentDataDO.getPaymentType().equals(PaymentDataType.CUS_PRE_DEPOSIT)) {
                                        this.appCustomerServiceImpl.preDepositRecharge(paymentDataDO, PreDepositChangeType.ALIPAY_RECHARGE);
                                    } else if (paymentDataDO.getPaymentType().equals(PaymentDataType.ST_PRE_DEPOSIT)
                                            || paymentDataDO.getPaymentType().equals(PaymentDataType.DEC_PRE_DEPOSIT)) {
                                        this.appStoreServiceImpl.preDepositRecharge(paymentDataDO, PreDepositChangeType.ALIPAY_RECHARGE);
                                    }
                                }
                            }
                            paymentDataDO.setTradeStatus(PaymentDataStatus.TRADE_FAIL);
                            this.paymentDataServiceImpl.updateByTradeStatusIsWaitPay(paymentDataDO);
                        }
                    } else {
                        response.getWriter().write(WechatUtil.setXML("FAIL", "签名失败"));
                    }
                } else {
                    logger.warn("{}", resultMap.get("err_code").toString());
                    logger.warn("{}", resultMap.get("err_code_des").toString());
                }
            }
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
            logger.warn("{}", e);
        }
    }
}
