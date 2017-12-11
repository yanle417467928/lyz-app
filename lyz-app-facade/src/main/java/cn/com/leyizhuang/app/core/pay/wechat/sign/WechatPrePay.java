package cn.com.leyizhuang.app.core.pay.wechat.sign;

import cn.com.leyizhuang.app.core.constant.AppApplicationConstant;
import cn.com.leyizhuang.app.core.pay.wechat.util.WechatUtil;
import org.jdom.JDOMException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * 微信支付调用接口之前的签名设置
 *
 * @author Jerry.Ren
 * Date: 2017/11/8.
 * Time: 16:55.
 */

public class WechatPrePay {
    /**
     * 微信下单参数签名
     *
     * @param sn          订单号
     * @param totalAmount 订单金额
     * @param request
     * @return 签名证书
     */
    public static Map<String, Object> wechatSign(String sn, String payType, BigDecimal totalAmount, HttpServletRequest request) {
        //设置相关参数
        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        parameterMap.put("appid", WechatUtil.APPID);
        parameterMap.put("mch_id", WechatUtil.MCH_ID);
        parameterMap.put("nonce_str", WechatUtil.getNonceStr());
        parameterMap.put("body", payType);
        parameterMap.put("out_trade_no", sn);
        parameterMap.put("fee_type", "CNY");
        BigDecimal total = totalAmount.multiply(new BigDecimal(100));
        DecimalFormat df = new DecimalFormat("0");
        parameterMap.put("total_fee", df.format(total));
        String ip = request.getRemoteAddr();
        ip = "0:0:0:0:0:0:0:1".equalsIgnoreCase(ip) ? "127.0.0.1" : ip;
        parameterMap.put("spbill_create_ip", ip);
        parameterMap.put("notify_url", AppApplicationConstant.wechatReturnUrlAsnyc);
        parameterMap.put("trade_type", "APP");
        String sign = WechatUtil.createSign("UTF-8", parameterMap);
        parameterMap.put("sign", sign);

        //装Map参数转化成xml文件，微信接受XML类型
        String requestXML = WechatUtil.getRequestXml(parameterMap);
        String result = WechatUtil.httpsRequest("https://api.mch.weixin.qq.com/pay/unifiedorder", "POST", requestXML);
        //微信根据参数返回一个xml文件，解析成Map,完成第二次参数配置
        Map map = null;
        SortedMap<String, Object> secondSignMap = null;
        try {
            map = WechatUtil.doXMLParse(result);

            secondSignMap = new TreeMap<String, Object>();
            secondSignMap.put("appid", WechatUtil.APPID);
            secondSignMap.put("partnerid", WechatUtil.MCH_ID);
            secondSignMap.put("prepayid", map.get("prepay_id"));
            secondSignMap.put("package", "Sign=WXPay");
            secondSignMap.put("noncestr", WechatUtil.getRandomString(32));
            secondSignMap.put("timestamp", System.currentTimeMillis() / 1000);
            secondSignMap.put("sign", WechatUtil.createSign("UTF-8", secondSignMap));

        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //页面可根据此Map参数调用微信支付页面
        return secondSignMap;
    }

    /**
     * 微信退款参数签名
     *
     * @param sn         商户原订单号
     * @param rn         商户退单号
     * @param totalfee   订单总金额，单位为分，只能为整。
     * @param refoundfee 退款金额
     * @param request    请求对象
     * @return 证书
     */
    public static Map<String, Object> wechatRefundSign(String sn, String rn, BigDecimal totalfee, BigDecimal refoundfee, HttpServletRequest request) {

        //设置相关参数
        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        //基础参数：appid,商户号,随机字符串
        parameterMap.put("appid", WechatUtil.APPID);
        parameterMap.put("mch_id", WechatUtil.MCH_ID);
        parameterMap.put("nonce_str", WechatUtil.getNonceStr());
        //商户原单号,商户退单号
        parameterMap.put("out_trade_no", sn);
        parameterMap.put("out_refund_no", rn);

        DecimalFormat df = new DecimalFormat("0");
        BigDecimal total = totalfee.multiply(new BigDecimal(100));
        BigDecimal refound = refoundfee.multiply(new BigDecimal(100));
        //金额：支付总金额和退单金额
        parameterMap.put("total_fee", df.format(total));
        parameterMap.put("total_fee", df.format(refound));
        //签名
        String sign = WechatUtil.createSign("UTF-8", parameterMap);
        parameterMap.put("sign", sign);

        //装Map参数转化成xml文件，微信接受XML类型
        String requestXML = WechatUtil.getRequestXml(parameterMap);
        //这里需要传入微信商户本地证书
        String result = null;
        try {
            result = WechatUtil.refundBySslPost("https://api.mch.weixin.qq.com/secapi/pay/refund", requestXML);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //微信根据参数返回一个xml文件，解析成Map
        Map map = null;
        SortedMap<String, Object> secondSignMap = null;
        try {
            map = WechatUtil.doXMLParse(result);
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
        //返回信息中包含微信退款成功失败信息
        return map;
    }
}
