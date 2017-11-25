package cn.com.leyizhuang.app.web.controller.settlement;

import cn.com.leyizhuang.app.core.constant.ApplicationConstant;
import cn.com.leyizhuang.app.core.constant.PaymentDataStatus;
import cn.com.leyizhuang.app.core.pay.wechat.sign.WechatPrePay;
import cn.com.leyizhuang.app.core.pay.wechat.util.WechatUtil;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.pojo.PaymentDataDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.service.AppCustomerService;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.PaymentDataService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.CountUtil;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.SortedMap;

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

    @Autowired
    private PaymentDataService paymentDataServiceImpl;

    @Autowired
    private AppCustomerService appCustomerServiceImpl;

    @Autowired
    private AppStoreService appStoreServiceImpl;

    @Resource
    private AppOrderService appOrderService;

    /**
     * 微信充值预存款
     * @param req
     * @param userId
     * @param identityType
     * @param money
     * @param cityId
     * @return
     */
    @RequestMapping(value = "wechat/recharge", method = RequestMethod.POST)
    public ResultDTO<Object> wechatPreDepositRecharge(HttpServletRequest req,Long userId, Integer identityType, Double money, Long cityId) {

        logger.info("wechatPreDepositRecharge CALLED,微信充值预存款，入参 userId:{} identityType:{} money{} cityId{}", userId, identityType, money, cityId);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("wechatPreDepositRecharge OUT,微信充值预存款，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("wechatPreDepositRecharge OUT,微信充值预存款，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == money && money <= 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "金额不正确！", null);
            logger.info("wechatPreDepositRecharge OUT,微信充值预存款，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == cityId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市信息不能为空！", null);
            logger.info("wechatPreDepositRecharge OUT,微信充值预存款，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        String totlefee = CountUtil.retainTwoDecimalPlaces(money);
        Double  totlefeeParse = Double.parseDouble(totlefee);
        String outTradeNo = OrderUtils.generateRechargeNumber(cityId);
        String subject = "预存款充值";

        PaymentDataDO paymentDataDO = new PaymentDataDO(userId, outTradeNo, identityType, ApplicationConstant.alipayReturnUrlAsnyc, subject,
                Double.parseDouble(totlefee), PaymentDataStatus.WAIT_PAY, "微信支付", "");
        this.paymentDataServiceImpl.save(paymentDataDO);

        try {
            SortedMap<String, Object> secondSignMap = (SortedMap<String, Object>) WechatPrePay.wechatSign(outTradeNo,new BigDecimal(totlefeeParse),req);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,secondSignMap);
            logger.info("wechatPreDepositRecharge OUT,支付宝充值预存款成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,微信充值预存款!", null);
            logger.warn("wechatPreDepositRecharge EXCEPTION,微信充值预存款，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }







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
            //获取响应的信息<为XML格式>转化成Map
            String result = WechatUtil.streamToString(request.getInputStream());
            Map resultMap = WechatUtil.doXMLParse(result);

            if (resultMap != null) {
                //状态是否成功
                if ("SUCCESS".equalsIgnoreCase(resultMap.get("result_code").toString())) {
                    //是否匹配签名
                    if (WechatUtil.verifyNotify(resultMap)) {
                        //返回响应成功的讯息
                        response.getWriter().write(WechatUtil.setXML("SUCCESS","OK"));
                        //取出map中的参数，订单号
                        String outTradeNo = resultMap.get("out_trade_no").toString();
                        //订单金额
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
