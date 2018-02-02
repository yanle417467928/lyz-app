package cn.com.leyizhuang.app.core.pay.wechat.refund;

import cn.com.leyizhuang.app.core.constant.OnlinePayType;
import cn.com.leyizhuang.app.core.constant.PaymentDataStatus;
import cn.com.leyizhuang.app.core.pay.wechat.sign.WechatPrePay;
import cn.com.leyizhuang.app.core.pay.wechat.util.WechatUtil;
import cn.com.leyizhuang.app.foundation.pojo.PaymentDataDO;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.PaymentDataService;
import cn.com.leyizhuang.common.util.CountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by caiyu on 2018/1/30.
 */
@Service
public class WeChatRefundService {

    private static final Logger logger = LoggerFactory.getLogger(WeChatRefundService.class);

    @Resource
    private AppOrderService appOrderService;

    @Resource
    private PaymentDataService paymentDataService;
    /**
     * 微信退款方法
     *
     * @param userId
     * @param identityType
     * @param money
     * @return
     */
    public Map<String, String> wechatReturnMoney(HttpServletResponse response, Long userId, Integer identityType, Double money, String orderNo, String refundNo) {
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
}
