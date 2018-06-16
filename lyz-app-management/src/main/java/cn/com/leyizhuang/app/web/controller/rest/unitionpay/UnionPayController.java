package cn.com.leyizhuang.app.web.controller.rest.unitionpay;


import cn.com.leyizhuang.app.core.pay.sdk.LogUtil;
import cn.com.leyizhuang.app.foundation.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @Description: 银联支付控制器
 * @Author Richard
 * @Date 2018/6/8 11:11
 */
@RestController
@RequestMapping("/ma/unionpay")
public class UnionPayController {

    private static final Logger logger = LoggerFactory.getLogger(UnionPayController.class);


    @Resource
    private PaymentDataService paymentDataService;

    @Resource
    private RechargeService rechargeService;

    @Resource
    private TransactionalSupportService supportService;

    @Resource
    private AppCustomerService customerService;

    @Resource
    private AppStoreService storeService;


    @Resource
    private AppOrderService orderService;

    @Resource
    private CommonService commonService;

    /**
     * 银联退款异步通知(后台通知)
     *
     * @param req  请求参数
     * @param resp 返回参数
     */
    @RequestMapping(value = "/refund/async/back")
    public void unionPayRefundAsyncBack(HttpServletRequest req, HttpServletResponse resp) {

        LogUtil.writeLog("unionPayReturnAsyncBack,银联支付接收后台通知开始");
        try {
            resp.getWriter().write("ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}