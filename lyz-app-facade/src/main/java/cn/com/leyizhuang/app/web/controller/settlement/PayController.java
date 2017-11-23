package cn.com.leyizhuang.app.web.controller.settlement;

import cn.com.leyizhuang.app.core.pay.wechat.util.WechatUtil;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

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
    private AppOrderService appOrderService;

    /**
     * 接受微信调用后返回参数的回调接口
     *
     * @param request
     * @param response
     */
    @PostMapping(value = "/wechat/return/async", produces = "application/json;charset=UTF-8")
    public void wechatReturnSync(HttpServletRequest request, HttpServletResponse response) {

        logger.info("wechatReturnSync CALLED,接受微信调用后返回参数的回调接口，入参 request:{},response:{}", request, response);

        try {
            String result = WechatUtil.streamToString(request.getInputStream());
            Map resultMap = WechatUtil.doXMLParse(result);

            if (resultMap != null) {
                if ("SUCCESS".equalsIgnoreCase(resultMap.get("result_code").toString())) {
                    if (WechatUtil.verifyNotify(resultMap)) {

                        String outTradeNo = resultMap.get("out_trade_no").toString();
                        String totalFee = resultMap.get("total_fee").toString();

                        //判断是否是充值订单
                        if (outTradeNo.contains("")) {

                        } else {
                            OrderBaseInfo order = appOrderService.getOrderByOrderNumber(outTradeNo);
                        }
                    }
                }
            }
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
            logger.warn("{}", e);
        }
    }
}
