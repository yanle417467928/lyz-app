package cn.com.leyizhuang.app.core.pay.wechat.refund;

import cn.com.leyizhuang.app.core.config.AlipayConfig;
import cn.com.leyizhuang.app.core.constant.OnlinePayType;
import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.core.constant.PaymentDataStatus;
import cn.com.leyizhuang.app.core.pay.wechat.sign.WechatPrePay;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.pojo.PaymentDataDO;
import cn.com.leyizhuang.app.foundation.pojo.remote.alipay.AlipayRefund;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBillingDetail;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.PaymentDataService;
import cn.com.leyizhuang.app.foundation.service.ReturnOrderService;
import cn.com.leyizhuang.app.remote.queue.SinkSender;
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
import java.util.*;

/**
 * Created by caiyu on 2018/1/30.
 */
@Service
public class OnlinePayRefundService {

    private static final Logger logger = LoggerFactory.getLogger(OnlinePayRefundService.class);

    @Resource
    private AppOrderService appOrderService;

    @Resource
    private PaymentDataService paymentDataService;

    @Resource
    private ReturnOrderService returnOrderService;

    @Resource
    private SinkSender sinkSender;
    /**
     * 微信退款方法
     *
     * @param userId
     * @param identityType
     * @param money
     * @return
     */
    public Map<String, String> wechatReturnMoney(Long userId, Integer identityType, Double money, String orderNo, String refundNo,Long roid) {
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

        String totlefeeFormat = CountUtil.retainTwoDecimalPlaces(dataDO.getTotalFee());
        Double totlefeeParse = Double.parseDouble(totlefeeFormat);
        String subject = "订单退款";


        PaymentDataDO paymentDataDO = new PaymentDataDO(userId, orderNo, refundNo, identityType, null,
                money, PaymentDataStatus.WAIT_REFUND, OnlinePayType.WE_CHAT, subject);
        this.paymentDataService.save(paymentDataDO);

        try {
            Map<String, Object> resultMap = WechatPrePay.wechatRefundSign(
                    dataDO.getOutTradeNo(), refundNo, new BigDecimal(totlefeeParse), new BigDecimal(money));
            logger.debug("******微信退款签名***** OUT, 出参 resultMap:{}", resultMap);
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
                        returnOrderBillingDetail.setCreateTime(Calendar.getInstance().getTime());
                        returnOrderBillingDetail.setRoid(roid);
                        returnOrderBillingDetail.setReturnNo(outRefundNo);
                        returnOrderBillingDetail.setRefundNumber(OrderUtils.getRefundNumber());
                        returnOrderBillingDetail.setIntoAmountTime(Calendar.getInstance().getTime());
                        returnOrderBillingDetail.setReplyCode(tradeNo);
                        returnOrderBillingDetail.setReturnMoney(money);
                        returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.WE_CHAT);
                        returnOrderBillingDetail.setReturnSubjectId(dataDO.getUserId());
                        returnOrderService.saveReturnOrderBillingDetail(returnOrderBillingDetail);

                        //发退款到EBS
//                        sinkSender.sendOrderRefund(returnOrderBillingDetail.getRefundNumber());
                        map.put("code", "SUCCESS");
                        map.put("number", tradeNo);
                        map.put("money", String.valueOf(money));
                        return map;
                    }
                }
            }
//            response.getWriter().write(WechatUtil.setXML("FAIL", "参数格式校验错误"));
            paymentDataDO.setRemarks(resultMap.get("return_msg").toString());
            paymentDataDO.setTradeStatus(PaymentDataStatus.REFUND_FAIL);
            this.paymentDataService.updateByTradeStatusIsWaitRefund(paymentDataDO);
            logger.warn("{}", resultMap.get("return_code").toString());
            logger.warn("{}", resultMap.get("return_msg").toString());
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
    public Map<String, String> alipayRefundRequest(Long userId, Integer identityType, String orderNo, String refundNo, double refundAmount,Long roid) {
        //取得支付宝交易流水号
        Map<String, String> map = new HashMap<>();
        List<PaymentDataDO> paymentDataDOList = this.paymentDataService.findByOrderNoAndTradeStatus(orderNo, PaymentDataStatus.TRADE_SUCCESS);
        if (AssertUtil.isEmpty(paymentDataDOList)) {
            map.put("code", "FAILURE");
            map.put("msg", "未查询到支付信息");
            return map;
        }
        PaymentDataDO dataDO = paymentDataDOList.get(0);
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
                ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                Date date = new Date();
                returnOrderBillingDetail.setCreateTime(date);
                returnOrderBillingDetail.setRoid(roid);
                returnOrderBillingDetail.setReturnNo(refundNo);
                returnOrderBillingDetail.setRefundNumber(OrderUtils.getRefundNumber());
                returnOrderBillingDetail.setIntoAmountTime(date);
                returnOrderBillingDetail.setReplyCode(response.getTradeNo());
                returnOrderBillingDetail.setReturnMoney(Double.valueOf(response.getRefundFee()));
                returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.ALIPAY);
                returnOrderBillingDetail.setReturnSubjectId(dataDO.getUserId());
                returnOrderService.saveReturnOrderBillingDetail(returnOrderBillingDetail);

                //发退款到EBS
//                sinkSender.sendOrderRefund(returnOrderBillingDetail.getRefundNumber());
                map.put("code", "SUCCESS");
                map.put("number", response.getTradeNo());
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

}
