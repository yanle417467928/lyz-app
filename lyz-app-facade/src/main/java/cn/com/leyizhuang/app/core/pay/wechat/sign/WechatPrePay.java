package cn.com.leyizhuang.app.core.pay.wechat.sign;

import cn.com.leyizhuang.app.core.constant.ApplicationConstant;
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
 * @author Jerry.Ren
 * Date: 2017/11/8.
 * Time: 16:55.
 */

public class WechatPrePay {
    public static Map<String, Object> wechatSign(String sn, BigDecimal totalAmount, HttpServletRequest request) {
        //设置相关参数
        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        parameterMap.put("appid", WechatUtil.APPID);
        parameterMap.put("mch_id", WechatUtil.MCH_ID);
        parameterMap.put("nonce_str", WechatUtil.getNonceStr());

        if (sn.contains("XN")) {
            parameterMap.put("body", "乐易装交易线上支付");
        } else {
            parameterMap.put("body", "乐易装电子钱包充值");
        }

        parameterMap.put("out_trade_no", sn);
        parameterMap.put("fee_type", "CNY");
        BigDecimal total = totalAmount.multiply(new BigDecimal(100));
        DecimalFormat df = new DecimalFormat("0");
        parameterMap.put("total_fee", df.format(total));
        String ip = request.getRemoteAddr();
        ip = "0:0:0:0:0:0:0:1".equalsIgnoreCase(ip) ? "127.0.0.1" : ip;
        parameterMap.put("spbill_create_ip", ip);
        parameterMap.put("notify_url", ApplicationConstant.wechatReturnUrlAsnyc);
        parameterMap.put("trade_type", "APP");
        String sign = WechatUtil.createSign("UTF-8", parameterMap);
        parameterMap.put("sign", sign);

        //装Map参数转化成xml文件，微信接受XML类型
        String requestXML = WechatUtil.getRequestXml(parameterMap);
        String result = WechatUtil.httpsRequest("https://api.mch.weixin.qq.com/pay/unifiedorder", "POST", requestXML);
        //微信根据参数返回一个xml文件，解析成Map,完成第二次参数配置
        Map<String, String> map = null;
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
}
