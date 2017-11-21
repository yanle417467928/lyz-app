package cn.com.leyizhuang.app.core.config;

/**
 * @author GenerationRoad
 * @date 2017/11/21
 */
public class AlipayConfig {

    // 商户的私钥
    public static String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCnPEGEzu4BoQUmqFmMD3aXroNaeZ8bD752zLB8KxEaCy2P6TOyRXDe2meUWKxlieCbbOUhjWSFLKuln9e2r7ma8KZgBH6qLLYK4h";

    // 支付宝的公钥，无需修改该值
    public static String aliPublicKey  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

    // 字符编码格式 目前支持 gbk 或 utf-8
    public static String charset = "utf-8";

    // 签名方式 不需修改
    public static String signType = "RSA2";

    //请求使用的编码格式
    public static String format = "json";

    //请求服务器地址（调试：http://openapi.alipaydev.com/gateway.do 线上：https://openapi.alipay.com/gateway.do ）
    public static String serverUrl = "https://openapi.alipay.com/gateway.do";

    //支付宝分配给开发者的应用ID
    public static String appId = "2016030801195560";

    //销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
    public static String productCode = "QUICK_MSECURITY_PAY";

}
