package cn.com.leyizhuang.app.web.controller.alipay;

import cn.com.leyizhuang.app.core.constant.PaymentDataStatus;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.pojo.PaymentDataDO;
import cn.com.leyizhuang.app.foundation.service.PaymentDataService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author GenerationRoad
 * @date 2017/11/16
 */
@RestController
@RequestMapping(value = "/app/alipay")
public class AliPayController {
    private static final Logger logger = LoggerFactory.getLogger(AliPayController.class);

    @Autowired
    private PaymentDataService paymentDataServiceImpl;


    /**  
     * @title   支付宝充值预存款
     * @descripe
     * @param
     * @return 
     * @throws 
     * @author GenerationRoad
     * @date 2017/11/20
     */
    @RequestMapping(value = "/recharge/pay", method = RequestMethod.POST)
    public ResultDTO<Object> PreDepositRecharge(Long userId, Integer identityType, Double money, Long cityId){

        logger.info("PreDepositRecharge CALLED,支付宝充值预存款，入参 userId:{} identityType:{} money{} cityId{}", userId, identityType, money, cityId);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("PreDepositRecharge OUT,支付宝充值预存款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("PreDepositRecharge OUT,支付宝充值预存款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == money && money <= 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "金额不正确！", null);
            logger.info("PreDepositRecharge OUT,支付宝充值预存款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == cityId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市信息不能为空！", null);
            logger.info("PreDepositRecharge OUT,支付宝充值预存款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Double totlefee = CountUtil.HALF_UP_SCALE_2(money);
        String outTradeNo = "CZ" + OrderUtils.generateOrderNumber(cityId);
        String subject = "预存款充值";
        PaymentDataDO paymentDataDO = new PaymentDataDO(userId, outTradeNo, identityType, "http://192.168.0.245:9999/app/alipay/return/async", subject,
                totlefee, PaymentDataStatus.WAIT_PAY, "支付宝", "");
        this.paymentDataServiceImpl.save(paymentDataDO);

        //serverUrl 非空，请求服务器地址（调试：http://openapi.alipaydev.com/gateway.do 线上：https://openapi.alipay.com/gateway.do ）
        //appId 非空，应用ID
        //privateKey 非空，私钥
        AlipayClient alipayClient = new DefaultAlipayClient(
                "https://openapi.alipay.com/gateway.do", "2016030801195560",
                "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCnPEGEzu4BoQUmqFmMD3aXroNaeZ8bD752zLB8KxEaCy2P6TOyRXDe2meUWKxlieCbbOUhjWSFLKuln9e2r7ma8KZgBH6qLLYK4h/wS++zCoAa8uW8M2vS+WNEwgn3aeniTDb/z2t7njrYittmYhRtGKdxTEzyy19CorFEjhQvym0rjKqhO4DTmEsEhH12skbYYpc4tCvksjANwQBPLqj+UY5/JKiYdvgntOv1dT1qJpwu0qNDkUl/aHaUDh2qXYw1b610TMhzPQa9M04jV7kvgVvUYz7YfuPPN5FNoq0mTaMbI768XvHDzl9PrH0QVfI6XBV+zBkGPwTilJOVKp2vAgMBAAECggEAfG1HrkGsMggU5gtR/PSwXvDYCGP/LkpLguaO1QPSCJeSgcWX0ib6cMT9VhGXy0CHnfyqiolB5hgzadqAzAr4xMASedQ+Te1QUM6nxskpAgRpBiP9LEbyDZnB8zGP6sah3t6n+TLhnKtAy0adMRL+caFrS8/iXK9WJCUaUZCIknkLZ/Th3+WgNhgb6sWQVViLoFegE2K3GrFWkN1XqEi+P7IyQok3lzQiP7gUi1TT9PEB7txcte8fBzLq4yBToLdFEqvSJXy8dJVMt+wt0PRJMsMpuDvDfUUVLSSRi6+7on2Lqlo3ZxbA7D/G7S2oCK3WDYCI4azj7i7cfsxJqebcaQKBgQDnYo3jlGnWA3scZXSRkX/mVz1rhyqP+BEGaIxKWNchWIwB3UtGFlTQCf5UW7h19o9z5auPbruvKoaVAQwQIplmNmEzHqbMNhQdmOpNpZf1U7T3P6rWkxMd5Y7xBtnN3qMmlSjQx9VHjwuX7nTM1I3MyGivIMgJvQ+Ku5EnLpgnUwKBgQC5Bq1Wwxw6gnPvRjevZh5qjxqYQ/VmyTRuipKYdWx1jwPybYIotTKDfVIwHRvQmokuqPnS6ZFiH1I2N1G2wFIfZwpg5W70Px/nu8mq3IX3SHUxBdzW1TJ4n/bkEy4TnfNLQHF6XhMLSndhmgKRGpttZrO1fDB6REhwFEmT/EPwtQKBgCiPPhAGlAbP1WTE5AogVyspX7rZlrxl35Yf/XeK2ysdOm2ZPPisMXHGTq665TrIXZ0TRV9/Kacq7SxxYovxhLJGFgO/+70QPbZyd5/kxx5Z7f7pSikS3ub+tm/Jo6RSwoag3ua6IMjKal3XjuHY4IJlHvST6nzKh07qUidsI3/BAoGAbq9M4JtFdsb1Xc0uoLHBPlOvQClfU4bH49znx2ZvZIASh2L1Oy7yMfC1Su0O56KasBwLnx10iDNedepv6phwSDLW5ZI65sggaPtQr/LN/VkOKi5spuPRFpHzGwtSRA/4LCs391DxqY55jhyYrn8xPafzl3zcbzgL8Whhf7KoXgUCgYAX72dvQHYuK3oKCzahZoc0UsVPrd88p+hc0y47mGNo9AoGA6Iw/vQGUSRXcohHhyWeiyPCZ/SiPxM9b2yjEUPCe8pqqxM7k0losJoJk4fvDk/mL/V7b9IcQY60xXwFQe8JnWQsodtxPnsnpm7tSjR9v/OgWzGOCd4ZbaEzWBbt4w==",
                "json", "UTF-8",
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB",
                "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setSubject(subject);
        model.setOutTradeNo(outTradeNo);
        model.setTimeoutExpress("30m");
        model.setTotalAmount(totlefee + "");
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl("http://192.168.0.245:9999/app/alipay/return/async");
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
//            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, response.getBody());
            logger.info("PreDepositRecharge OUT,支付宝充值预存款成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,支付宝充值预存款失败!", null);
            logger.warn("PreDepositRecharge EXCEPTION,支付宝充值预存款失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    @RequestMapping(value = "/return/async")
    public String AlipayReturnAsync(HttpServletRequest request){

        try {
            //获取支付宝POST过来反馈信息
            Map<String,String> params = new HashMap<String,String>();
            Map requestParams = request.getParameterMap();
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
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
            boolean flag = AlipaySignature.rsaCheckV1(params, "alipaypublicKey", "charset", "RSA2");

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

                List<PaymentDataDO> paymentDataDOList = this.paymentDataServiceImpl.findByOutTradeNoAndTradeStatus(out_trade_no, PaymentDataStatus.WAIT_PAY);
                if (null != paymentDataDOList && paymentDataDOList.size() == 1) {
                    PaymentDataDO paymentDataDO = paymentDataDOList.get(0);
                    if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
                        if (Double.parseDouble(total_fee) == paymentDataDO.getTotalFee()) {
                            paymentDataDO.setTradeNo(trade_no);
                            paymentDataDO.setTradeStatus(PaymentDataStatus.TRADE_SUCCESS);
                            this.paymentDataServiceImpl.updateByTradeStatusIsWaitPay(paymentDataDO);
                            return "success";
                        }
                    }
                    paymentDataDO.setTradeStatus(PaymentDataStatus.TRADE_FAIL);
                    this.paymentDataServiceImpl.updateByTradeStatusIsWaitPay(paymentDataDO);
                }
            }
        }catch (AlipayApiException e){
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "fail";
    }
}
