package cn.com.leyizhuang.app.core.wechat.refund;

import cn.com.leyizhuang.app.core.config.onlinePay.AlipayConfig;
import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.pay.sdk.AcpService;
import cn.com.leyizhuang.app.core.pay.sdk.LogUtil;
import cn.com.leyizhuang.app.core.pay.sdk.SDKConfig;
import cn.com.leyizhuang.app.core.pay.sdk.SDKConstants;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.core.wechat.sign.WechatPrePay;
import cn.com.leyizhuang.app.foundation.pojo.PaymentDataDO;
import cn.com.leyizhuang.app.foundation.pojo.remote.alipay.AlipayRefund;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBillingDetail;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.PaymentDataService;
import cn.com.leyizhuang.app.foundation.service.ReturnOrderService;
import cn.com.leyizhuang.common.util.AssertUtil;
import cn.com.leyizhuang.common.util.CountUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by caiyu on 2018/1/30.
 */
@Service
public class MaOnlinePayRefundService {

    private static final Logger logger = LoggerFactory.getLogger(MaOnlinePayRefundService.class);

    @Resource
    private AppOrderService appOrderService;

    @Resource
    private PaymentDataService paymentDataService;

    @Resource
    private ReturnOrderService returnOrderService;

    /**
     * 微信退款方法
     *
     * @param userId
     * @param identityType
     * @param money
     * @return
     */
    public Map<String, String> wechatReturnMoney(Long userId, Integer identityType, Double money, String orderNumber, String returnNumber) {
        Map<String, String> map = new HashMap<>();
        List<PaymentDataDO> paymentDataDOList = this.paymentDataService.findByOrderNoAndTradeStatus(orderNumber, PaymentDataStatus.TRADE_SUCCESS);
        if (AssertUtil.isEmpty(paymentDataDOList)) {
            map.put("code", "FAILURE");
            map.put("msg", "未查询到支付信息");
            return map;
        }
        PaymentDataDO dataDO = paymentDataDOList.get(0);
        if (money > dataDO.getTotalFee()) {
            map.put("code", "FAILURE");
            map.put("msg", "退款金额不能超过订单金额");
            return map;
        }

        Double totlefee = appOrderService.getAmountPayableByOrderNumber(orderNumber);
        String totlefeeFormat = CountUtil.retainTwoDecimalPlaces(totlefee);
        Double totlefeeParse = Double.parseDouble(totlefeeFormat);
        String subject = "订单退款";


        PaymentDataDO paymentDataDO = new PaymentDataDO(userId, orderNumber, returnNumber, identityType, null,
                money, PaymentDataStatus.WAIT_REFUND, OnlinePayType.WE_CHAT, subject);
        this.paymentDataService.save(paymentDataDO);

        try {
            Map<String, Object> resultMap = WechatPrePay.wechatRefundSign(
                    dataDO.getOutTradeNo(), returnNumber, new BigDecimal(totlefeeParse), new BigDecimal(money));
            logger.debug("******微信退款签名***** OUT, 出参 sign:{}", resultMap);
            if (resultMap != null) {
                //状态是否成功
                if ("SUCCESS".equalsIgnoreCase(resultMap.get("return_code").toString())) {
                    if ("SUCCESS".equalsIgnoreCase(resultMap.get("result_code").toString())) {
//                        response.getWriter().write(WechatUtil.setXML("SUCCESS", null));
                        //取出map中的参数，订单号
                        String outTradeNo = resultMap.get("out_trade_no").toString();
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
                        paymentDataDO.setTradeNo(tradeNo);
                        paymentDataDO.setNotifyTime(new Date());
                        paymentDataDO.setTradeStatus(PaymentDataStatus.REFUND_SUCCESS);
                        this.paymentDataService.updateByTradeStatusIsWaitRefund(paymentDataDO);

                        //创建退单退款详情实体
                        ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                        ReturnOrderBaseInfo returnOrderBaseInfo = returnOrderService.queryByReturnNo(returnNumber);
                        returnOrderBillingDetail.setCreateTime(new Date());
                        returnOrderBillingDetail.setRoid(returnOrderBaseInfo.getRoid());
                        returnOrderBillingDetail.setReturnNo(outRefundNo);
                        returnOrderBillingDetail.setRefundNumber(OrderUtils.getRefundNumber());
                        returnOrderBillingDetail.setIntoAmountTime(new Date());
                        returnOrderBillingDetail.setReplyCode(map.get("number"));
                        returnOrderBillingDetail.setReturnMoney(money);
                        returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.WE_CHAT);
                        returnOrderBillingDetail.setReturnSubjectId(dataDO.getUserId());
                        returnOrderService.saveReturnOrderBillingDetail(returnOrderBillingDetail);

                        map.put("code", "SUCCESS");
                        map.put("number", tradeNo);
                        map.put("money", refundFee);
                        return map;
                    }
                }
            }
//            response.getWriter().write(WechatUtil.setXML("FAIL", "参数格式校验错误"));
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
     * 支付宝退款接口
     *
     * @param orderNo      订单支付时传入的商户订单号,不能和 trade_no同时为空。
     * @param refundNo     退单号
     * @param refundAmount 需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
     * @return 将提示信息返回
     * @throws AlipayApiException
     * @throws IllegalAccessException
     */
    public Map<String, String> alipayRefundRequest(Long userId, Integer identityType, String orderNo, String refundNo, double refundAmount) {
        //取得支付宝交易流水号
        List<PaymentDataDO> paymentDataDOList = this.paymentDataService.findByOrderNoAndTradeStatus(orderNo, PaymentDataStatus.TRADE_SUCCESS);
        PaymentDataDO dataDO = paymentDataDOList.get(0);
        Map<String, String> map = new HashMap<>();
        if (refundAmount > dataDO.getTotalFee()) {
            map.put("code", "FAILURE");
            map.put("msg", "退款金额不能超过订单金额");
            return map;
        }
        String remarks = "订单退款";

        PaymentDataDO paymentDataDO = new PaymentDataDO(userId, orderNo, refundNo, identityType, null,
                refundAmount, PaymentDataStatus.WAIT_REFUND, OnlinePayType.ALIPAY, remarks);
        this.paymentDataService.save(paymentDataDO);
        //serverUrl 非空，请求服务器地址（调试：http://openapi.alipaydev.com/gateway.do 线上：https://openapi.alipay.com/gateway.do ）
        //appId 非空，应用ID
        //privateKey 非空，私钥
        AlipayClient alipayClient = new DefaultAlipayClient(
                AlipayConfig.serverUrl, AlipayConfig.appId,
                AlipayConfig.privateKey, AlipayConfig.format, AlipayConfig.charset,
                AlipayConfig.aliPublicKey, AlipayConfig.signType);
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayRefund alipayRefund = new AlipayRefund();
        //这个是商户的订单号
        alipayRefund.setOut_trade_no(dataDO.getOutTradeNo());
        //这个是支付宝的订单号
        alipayRefund.setTrade_no(dataDO.getTradeNo());
        //退款金额
        alipayRefund.setRefund_amount(refundAmount);
        //退款说明
        alipayRefund.setRefund_reason(remarks);
        alipayRefund.setOut_request_no(refundNo);
        request.setBizContent(JSONObject.toJSONString(alipayRefund));
        try {
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {

                paymentDataDO.setTradeNo(response.getTradeNo());
                paymentDataDO.setNotifyTime(new Date());
                paymentDataDO.setTradeStatus(PaymentDataStatus.REFUND_SUCCESS);
                this.paymentDataService.updateByTradeStatusIsWaitRefund(paymentDataDO);

                //创建退单退款详情实体
                ReturnOrderBaseInfo returnOrderBaseInfo = returnOrderService.queryByReturnNo(refundNo);
                ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                Date date = new Date();
                returnOrderBillingDetail.setCreateTime(date);
                returnOrderBillingDetail.setRoid(returnOrderBaseInfo.getRoid());
                returnOrderBillingDetail.setReturnNo(refundNo);
                returnOrderBillingDetail.setRefundNumber(OrderUtils.getRefundNumber());
                returnOrderBillingDetail.setIntoAmountTime(date);
                returnOrderBillingDetail.setReplyCode(response.getTradeNo());
                returnOrderBillingDetail.setReturnMoney(Double.valueOf(response.getSendBackFee()));
                returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.ALIPAY);
                returnOrderBillingDetail.setReturnSubjectId(dataDO.getUserId());
                returnOrderService.saveReturnOrderBillingDetail(returnOrderBillingDetail);

                map.put("code", "SUCCESS");

                return map;
            } else {
                paymentDataDO.setRemarks(response.getMsg());
                paymentDataDO.setTradeStatus(PaymentDataStatus.REFUND_FAIL);
                this.paymentDataService.updateByTradeStatusIsWaitRefund(paymentDataDO);
                map.put("code", "FAILURE");
                map.put("msg", response.getMsg());
                return map;
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            map.put("code", "FAILURE");
            map.put("msg", "支付宝退款接口发生异常");
            return map;
        }
    }

    /**
     * 银联支付退款
     *
     * @param userId       用户id
     * @param identityType 用户身份
     * @param money        退款金额
     * @param orderNo      订单号
     * @param refundNo     退单号
     * @param roid         退单 id
     * @return
     */
    public Map<String, String> unionPayReturnMoney(Long userId, Integer identityType, Double money, String orderNo,
                                                   String refundNo, Long roid) {
        Map<String, String> map = new HashMap<>();
        List<PaymentDataDO> paymentDataDOList = this.paymentDataService.findByOrderNoAndTradeStatus(orderNo, PaymentDataStatus.TRADE_SUCCESS);
        if (AssertUtil.isEmpty(paymentDataDOList)) {
            map.put("code", "FAILURE");
            map.put("msg", "未查询到支付信息");
            return map;
        }
        PaymentDataDO dataDO = paymentDataDOList.get(0);
        if (money > dataDO.getTotalFee()) {
            map.put("code", "FAILURE");
            map.put("msg", "退款金额不能超过订单金额");
            return map;
        }
        String subject = "订单退款";
        PaymentDataDO paymentDataDO = new PaymentDataDO(userId, orderNo, refundNo, identityType, null,
                money, PaymentDataStatus.WAIT_REFUND, OnlinePayType.UNION_PAY, subject);
        this.paymentDataService.save(paymentDataDO);
        //前台页面传过来的
        String txnAmt = Integer.valueOf(new Double(money * 100).intValue()).toString();

        try {
            Map<String, String> data = new HashMap<String, String>();

            /********** 银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改 ***********/

            /**
             * 版本号
             */
            data.put("version", SDKConfig.getConfig().getVersion());
            /**
             * 字符集编码 可以使用UTF-8,GBK两种方式
             */
            data.put("encoding", AppConstant.UNION_PAY_ENCODING);
            /**
             * 签名方法
             */
            data.put("signMethod", SDKConfig.getConfig().getSignMethod());
            /**
             * 交易类型 04-退货
             */
            data.put("txnType", "04");
            /**
             * 交易子类型  默认00
             */
            data.put("txnSubType", "00");
            /**
             * 业务类型
             */
            data.put("bizType", "000201");
            /**
             * 渠道类型，07-PC，08-手机
             */
            data.put("channelType", "08");

            /****************** 商户接入参数 *****************/

            /**
             * 商户号码，请改成自己申请的商户号
             */
            data.put("merId", AppConstant.UNION_PAY_MERCHANT_ID);
            /**
             * 接入类型，商户接入固定填0，不需修改
             */
            data.put("accessType", "0");
            /**
             * 商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费
             */
            data.put("orderId", refundNo);
            /**
             * 订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
             */
            data.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
            /**
             * 交易币种（境内商户一般是156 人民币）
             */
            data.put("currencyCode", "156");
            /**
             * 退货金额，单位分，不要带小数点。退货金额小于等于原消费金额，当小于的时候可以多次退货至退货累计金额等于原消费金额
             */
            data.put("txnAmt", txnAmt);
            /**
             * 后台通知地址
             */
            data.put("backUrl", ApplicationConstant.unionPayRefundAsyncBack);
            /**
             * 原消费交易返回的的queryId
             */
            data.put("origQryId", dataDO.getTradeNo());

            /****** 请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->*******/

            //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
            Map<String, String> reqData = AcpService.sign(data, AppConstant.UNION_PAY_ENCODING);

            //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
            String url = SDKConfig.getConfig().getBackRequestUrl();

            //发送post请求到银联发起退款申请
            Map<String, String> rspData = AcpService.post(reqData, url, AppConstant.UNION_PAY_ENCODING);

            if (!rspData.isEmpty()) {
                if (AcpService.validate(rspData, AppConstant.UNION_PAY_ENCODING)) {
                    LogUtil.writeLog("验证签名成功");
                    String respCode = rspData.get("respCode");
                    String orderId = rspData.get("orderId");
                    logger.info("银联退款响应报文，退单号 orderId:{}", orderId);
                    //银联流水号
                    String queryId = rspData.get("queryId");
                    logger.info("银联退款响应报文,银联流水号, 出参 queryId:{}", queryId);
                    //申请退单金额
                    String refundFee = rspData.get("txnAmt");
                    logger.info("银联退款响应报文, 退单金额 refundFee:{}", refundFee);
                    //银联响应信息
                    String respMsg = rspData.get("respMsg");
                    logger.info("银联退款响应报文, 响应消息 respMsg:{}", respMsg);
                    if (("00").equals(respCode)) {
                        //交易已受理(不代表交易已成功），等待接收后台通知更新订单状态,也可以主动发起 查询交易确定交易状态。
                        paymentDataDO.setTradeNo(queryId);
                        paymentDataDO.setNotifyTime(new Date());
                        paymentDataDO.setTradeStatus(PaymentDataStatus.REFUND_SUCCESS);
                        this.paymentDataService.updateByTradeStatusIsWaitRefund(paymentDataDO);

                        //创建退单退款详情实体
                        ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                        returnOrderBillingDetail.setCreateTime(Calendar.getInstance().getTime());
                        returnOrderBillingDetail.setRoid(roid);
                        returnOrderBillingDetail.setReturnNo(orderId);
                        returnOrderBillingDetail.setRefundNumber(OrderUtils.getRefundNumber());
                        returnOrderBillingDetail.setIntoAmountTime(Calendar.getInstance().getTime());
                        returnOrderBillingDetail.setReplyCode(respCode);
                        returnOrderBillingDetail.setReturnMoney(money);
                        returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.WE_CHAT);
                        returnOrderBillingDetail.setReturnSubjectId(dataDO.getUserId());
                        returnOrderService.saveReturnOrderBillingDetail(returnOrderBillingDetail);
                        map.put("code", "SUCCESS");
                        String reqMessage = this.genHtmlResult(reqData);
                        String rspMessage = this.genHtmlResult(rspData);
                        logger.info("请求报文:<br/>" + reqMessage + "<br/>" + "应答报文:</br>" + rspMessage + "");
                        return map;
                    } else if (("03").equals(respCode) ||
                            ("04").equals(respCode) ||
                            ("05").equals(respCode)) {
                        //后续需发起交易状态查询交易确定交易状态
                        //TODO
                        map.put("code", "FAILURE");
                        return map;
                    } else {
                        //其他应答码为失败请排查原因
                        paymentDataDO.setRemarks(rspData.get("return_msg").toString());
                        paymentDataDO.setTradeStatus(PaymentDataStatus.REFUND_FAIL);
                        this.paymentDataService.updateByTradeStatusIsWaitRefund(paymentDataDO);
                        map.put("code", "FAILURE");
                        return map;
                    }
                } else {
                    LogUtil.writeErrorLog("验证签名失败");
                    //TODO 检查验证签名失败的原因
                    map.put("code", "FAILURE");
                    return map;
                }
            } else {
                //未返回正确的http状态
                LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
                map.put("code", "FAILURE");
                return map;
            }

        }/* catch (IOException e) {
            e.printStackTrace();
            logger.info("响应银联退款异步回调接口失败");
            map.put("code", "FAILURE");
            return map;
        }*/ catch (Exception e) {
            e.printStackTrace();
            logger.info("响应银联退款异步回调接口失败");
            map.put("code", "FAILURE");
            return map;
        }
    }

    /**
     * 组装请求，返回报文字符串用于显示
     *
     * @param data
     * @return
     */
    public static String genHtmlResult(Map<String, String> data) {

        TreeMap<String, String> tree = new TreeMap<String, String>();
        Iterator<Map.Entry<String, String>> it = data.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> en = it.next();
            tree.put(en.getKey(), en.getValue());
        }
        it = tree.entrySet().iterator();
        StringBuffer sf = new StringBuffer();
        while (it.hasNext()) {
            Map.Entry<String, String> en = it.next();
            String key = en.getKey();
            String value = en.getValue();
            if ("respCode".equals(key)) {
                sf.append("<b>" + key + SDKConstants.EQUAL + value + "</br></b>");
            } else
                sf.append(key + SDKConstants.EQUAL + value + "</br>");
        }
        return sf.toString();
    }

}
