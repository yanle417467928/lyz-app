package cn.com.leyizhuang.app.core.config;

/**
 * @author GenerationRoad
 * @date 2017/11/21
 */
public class AlipayConfig {

    // 商户的私钥
    public static String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCnPEGEzu4BoQUmqFmMD3aXroNaeZ8bD752zLB8KxEaCy2P6TOyRXDe2meUWKxlieCbbOUhjWSFLKuln9e2r7ma8KZgBH6qLLYK4h/wS++zCoAa8uW8M2vS+WNEwgn3aeniTDb/z2t7njrYittmYhRtGKdxTEzyy19CorFEjhQvym0rjKqhO4DTmEsEhH12skbYYpc4tCvksjANwQBPLqj+UY5/JKiYdvgntOv1dT1qJpwu0qNDkUl/aHaUDh2qXYw1b610TMhzPQa9M04jV7kvgVvUYz7YfuPPN5FNoq0mTaMbI768XvHDzl9PrH0QVfI6XBV+zBkGPwTilJOVKp2vAgMBAAECggEAfG1HrkGsMggU5gtR/PSwXvDYCGP/LkpLguaO1QPSCJeSgcWX0ib6cMT9VhGXy0CHnfyqiolB5hgzadqAzAr4xMASedQ+Te1QUM6nxskpAgRpBiP9LEbyDZnB8zGP6sah3t6n+TLhnKtAy0adMRL+caFrS8/iXK9WJCUaUZCIknkLZ/Th3+WgNhgb6sWQVViLoFegE2K3GrFWkN1XqEi+P7IyQok3lzQiP7gUi1TT9PEB7txcte8fBzLq4yBToLdFEqvSJXy8dJVMt+wt0PRJMsMpuDvDfUUVLSSRi6+7on2Lqlo3ZxbA7D/G7S2oCK3WDYCI4azj7i7cfsxJqebcaQKBgQDnYo3jlGnWA3scZXSRkX/mVz1rhyqP+BEGaIxKWNchWIwB3UtGFlTQCf5UW7h19o9z5auPbruvKoaVAQwQIplmNmEzHqbMNhQdmOpNpZf1U7T3P6rWkxMd5Y7xBtnN3qMmlSjQx9VHjwuX7nTM1I3MyGivIMgJvQ+Ku5EnLpgnUwKBgQC5Bq1Wwxw6gnPvRjevZh5qjxqYQ/VmyTRuipKYdWx1jwPybYIotTKDfVIwHRvQmokuqPnS6ZFiH1I2N1G2wFIfZwpg5W70Px/nu8mq3IX3SHUxBdzW1TJ4n/bkEy4TnfNLQHF6XhMLSndhmgKRGpttZrO1fDB6REhwFEmT/EPwtQKBgCiPPhAGlAbP1WTE5AogVyspX7rZlrxl35Yf/XeK2ysdOm2ZPPisMXHGTq665TrIXZ0TRV9/Kacq7SxxYovxhLJGFgO/+70QPbZyd5/kxx5Z7f7pSikS3ub+tm/Jo6RSwoag3ua6IMjKal3XjuHY4IJlHvST6nzKh07qUidsI3/BAoGAbq9M4JtFdsb1Xc0uoLHBPlOvQClfU4bH49znx2ZvZIASh2L1Oy7yMfC1Su0O56KasBwLnx10iDNedepv6phwSDLW5ZI65sggaPtQr/LN/VkOKi5spuPRFpHzGwtSRA/4LCs391DxqY55jhyYrn8xPafzl3zcbzgL8Whhf7KoXgUCgYAX72dvQHYuK3oKCzahZoc0UsVPrd88p+hc0y47mGNo9AoGA6Iw/vQGUSRXcohHhyWeiyPCZ/SiPxM9b2yjEUPCe8pqqxM7k0losJoJk4fvDk/mL/V7b9IcQY60xXwFQe8JnWQsodtxPnsnpm7tSjR9v/OgWzGOCd4ZbaEzWBbt4w==";

    // 支付宝的公钥，无需修改该值
    public static String aliPublicKey  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

    // 字符编码格式 目前支持 gbk 或 utf-8
    public static String charset = "UTF-8";

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
