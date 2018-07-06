package cn.com.leyizhuang.app.web.controller.unionpay;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.pay.unionpay.sdk.AcpService;
import cn.com.leyizhuang.app.core.pay.unionpay.sdk.LogUtil;
import cn.com.leyizhuang.app.core.pay.unionpay.sdk.SDKConfig;
import cn.com.leyizhuang.app.core.pay.unionpay.sdk.SDKConstants;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.pojo.PaymentDataDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeReceiptInfo;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.remote.queue.SinkSender;
import cn.com.leyizhuang.app.remote.webservice.ICallWms;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.CountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Description: 银联支付控制器
 * @Author Richard
 * @Date 2018/6/8 11:11
 */
@RestController
@RequestMapping("/app/unionpay")
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
    private SinkSender sinkSender;

    @Resource
    private AppOrderService orderService;

    @Resource
    private CommonService commonService;

    @Resource
    private ICallWms iCallWms;

    @Autowired
    private BillInfoService billInfoService;

    /**
     * 订单银联支付
     *
     * @param userId        用户id
     * @param identityType  用户身份
     * @param payableAmount 支付金额（单位 RMB 元）
     * @param orderNumber   订单号
     * @param response      响应对象
     * @return 银联支付请求html
     */
    @PostMapping(value = "/order/pay/html", produces = "application/json")
    public ResultDTO orderUnionPayHtml(Long userId, Integer identityType, Double payableAmount, String orderNumber, HttpServletResponse response) {

        logger.info("orderUnionPay CALLED,订单银联支付信息提交,入参 userId:{}, identityType:{}, payableAmount:{}, orderNumber:{}",
                userId, identityType, payableAmount, orderNumber);
        ResultDTO<String> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("orderUnionPay OUT,订单银联支付信息提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("orderUnionPay OUT,订单银联支付信息提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (!(null != payableAmount && payableAmount > 0)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "支付金额不正确！", null);
            logger.info("orderUnionPay OUT,订单银联支付信息提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == orderNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单号不能为空！", null);
            logger.info("orderUnionPay OUT,订单银联支付信息提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            String totalFee = CountUtil.retainTwoDecimalPlaces(payableAmount);
            String outTradeNo = orderNumber.replaceAll("_", "");
            PaymentDataDO paymentData = new PaymentDataDO();
            paymentData.setUserId(userId);
            paymentData.setOnlinePayType(OnlinePayType.UNION_PAY);
            paymentData.setPaymentType(PaymentDataType.ORDER);
            paymentData.setPaymentTypeDesc(PaymentDataType.ORDER.getDescription());
            paymentData.setAppIdentityType(AppIdentityType.getAppIdentityTypeByValue(identityType));
            paymentData.setCreateTime(LocalDateTime.now());
            paymentData.setOutTradeNo(outTradeNo);
            paymentData.setOrderNumber(orderNumber);
            paymentData.setTotalFee(Double.parseDouble(totalFee));
            paymentData.setTradeStatus(PaymentDataStatus.WAIT_PAY);
            paymentData.setNotifyUrl(AppApplicationConstant.unionPayAsyncUrlBack);
            paymentDataService.save(paymentData);

            //生成银联支付请求html
            String html = this.generatePaymentHtml(payableAmount, outTradeNo);

            LogUtil.writeLog("打印请求HTML，此为请求报文，为联调排查问题的依据：" + html);

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, html);
            response.getWriter().write(resultDTO.getContent());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("银联支付订单发生异常:" + e);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "银联支付订单发生异常", null);
            return resultDTO;
        }
    }


    /**
     * 银联支付异步通知(后台通知)
     *
     * @param req  请求参数
     * @param resp 返回参数
     */
    @RequestMapping(value = "/return/async/back")
    public void unionPayReturnAsyncBack(HttpServletRequest req, HttpServletResponse resp) {

        LogUtil.writeLog("unionPayReturnAsyncBack,银联支付接收后台通知开始");
        try {
            String encoding = req.getParameter(SDKConstants.param_encoding);
            // 获取银联通知服务器发送的后台通知参数
            Map<String, String> requestParam = getAllRequestParam(req);
            LogUtil.printRequestLog(requestParam);
            //重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
            if (!AcpService.validate(requestParam, encoding)) {
                LogUtil.writeLog("unionPayReturnAsyncBack,银联支付后台异步通知验证签名结果[失败].");
                //验签失败，需解决验签问题
            } else {
                LogUtil.writeLog("unionPayReturnAsyncBack,银联支付后台异步通知验证签名结果[成功].");
                //【注：为了安全验签成功才应该写商户的成功处理逻辑】交易成功，更新商户订单状态

                /**
                 * 商户订单号
                 */
                String orderId = requestParam.get("orderId");
                /**
                 * 支付结果码
                 */
                String respCode = requestParam.get("respCode");
                /**
                 * 支付金额 (单位 分)
                 */
                Integer settleAmt = Integer.parseInt(requestParam.get("settleAmt"));

                /**
                 * 银联流水号
                 */
                String queryId = requestParam.get("queryId");


                LogUtil.writeLog("订单号orderId:" + orderId);
                LogUtil.writeLog("支付结果码respCode:" + respCode);
                //respCode=00、A6表示支付成功
                // TODO: 2018/6/11
                if (StringUtils.isNotBlank(respCode) && (respCode.equalsIgnoreCase("00") ||
                        respCode.equalsIgnoreCase("A6"))) {

                    List<PaymentDataDO> paymentDataDOList = this.paymentDataService.findByOutTradeNoAndTradeStatus(orderId, PaymentDataStatus.WAIT_PAY);
                    PaymentDataDO paymentDataDO;
                    /**
                     * 如果没有找到商户订单号，向银联返回响应失败信息
                     */
                    if (null != paymentDataDOList && paymentDataDOList.size() > 0) {
                        paymentDataDO = paymentDataDOList.get(0);
                    } else {
                        resp.setStatus(500);
                        resp.getWriter().write("error");
                        logger.error("银联支付后台回调接口处理失败，商户订单号:{} 未找到！", orderId);
                        return;
                    }
                    //如果已处理就跳过处理代码
                    if (!orderId.contains("CZ")) {
                        List<PaymentDataDO> paymentDataList = this.paymentDataService.findByOrderNoAndTradeStatus(paymentDataDO.getOrderNumber(), PaymentDataStatus.TRADE_SUCCESS);
                        if (null != paymentDataList && paymentDataList.size() > 0) {
                            logger.warn("unionPayReturnAsyncBack,银联支付后台回调已处理，响应银联结果 result:{}", "ok");
                            resp.setStatus(200);
                            resp.getWriter().print("ok");
                            return;
                        }
                    }
                    /**
                     * 根据单号格式判断业务类型， 并进行响应的回调处理
                     */
                    if (orderId.contains("CZ")) {
                        //充值加预存款和日志
                        if (null != paymentDataDO.getId() && new Double(paymentDataDO.getTotalFee() * 100).intValue() == settleAmt) {
                            paymentDataDO.setTradeNo(queryId);
                            paymentDataDO.setTradeStatus(PaymentDataStatus.TRADE_SUCCESS);
                            paymentDataDO.setNotifyTime(new Date());
                            this.paymentDataService.updateByTradeStatusIsWaitPay(paymentDataDO);
                            //创建充值单收款
                            RechargeReceiptInfo receiptInfo = rechargeService.createOnlinePayRechargeReceiptInfo(paymentDataDO, respCode);
                            //保存充值收款记录,并更新充值单相关信息
                            supportService.handleRechargeOrderRelevantInfoAfterOnlinePauUp(receiptInfo, orderId);
                            logger.info("unionPayReturnAsyncBack ,银联支付后台回调，支付数据记录信息 paymentDataDO:{}",
                                    paymentDataDO);
                            if (paymentDataDO.getPaymentType().equals(PaymentDataType.CUS_PRE_DEPOSIT)) {
                                customerService.preDepositRecharge(paymentDataDO, CustomerPreDepositChangeType.UNIONPAY_RECHARGE);
                            } else if (paymentDataDO.getPaymentType().equals(PaymentDataType.ST_PRE_DEPOSIT)
                                    || paymentDataDO.getPaymentType().equals(PaymentDataType.DEC_PRE_DEPOSIT)) {
                                storeService.preDepositRecharge(paymentDataDO, StorePreDepositChangeType.UNIONPAY_RECHARGE);
                            }
                            //将收款记录入拆单消息队列
                            sinkSender.sendRechargeReceipt(orderId);
                            logger.info("unionPayReturnAsyncBack OUT,银联支付后台回调处理成功，出参 result:{}", "ok");
                        }
                    } else if (orderId.contains("HK")) {
                        if (null != paymentDataDO.getId() && new Double(paymentDataDO.getTotalFee() * 100).intValue() == settleAmt) {
                            paymentDataDO.setTradeNo(orderId);
                            paymentDataDO.setTradeStatus(PaymentDataStatus.TRADE_SUCCESS);
                            paymentDataDO.setNotifyTime(new Date());
                            this.paymentDataService.updateByTradeStatusIsWaitPay(paymentDataDO);
                            logger.info("unionPayReturnAsyncBack ,银联支付后台回调，支付数据记录信息 paymentDataDO:{}",
                                    paymentDataDO);
                            String orderNumber = orderId.replaceAll("HK", "_XN");
                            Double money = paymentDataDO.getTotalFee();
                            orderService.saveAliPayOrderBillingPaymentDetails(orderNumber, money, queryId, orderId);

                            //2018-05-03 13:28:24 Jerry.Ren 修改这里收款拆单到Controller最后发送消息队列
                            sinkSender.sendOrderReceipt(orderId);
                            logger.info("unionPayReturnAsyncBack OUT,银联支付后台回调处理成功，出参 result:{}", "ok");
                        }
                    } else if (paymentDataDO.getOrderNumber().contains("XN")) {
                        if (null != paymentDataDO.getId() && new Double(paymentDataDO.getTotalFee() * 100).intValue() == settleAmt) {
                            String orderNumber = paymentDataDO.getOrderNumber();
                            paymentDataDO.setTradeNo(queryId);
                            paymentDataDO.setTradeStatus(PaymentDataStatus.TRADE_SUCCESS);
                            paymentDataDO.setNotifyTime(new Date());
                            this.paymentDataService.updateByTradeStatusIsWaitPay(paymentDataDO);
                            logger.info("unionPayReturnAsyncBack ,银联支付后台回调，支付数据记录信息 paymentDataDO:{}",
                                    paymentDataDO);
                            //处理第三方支付成功之后订单相关事务
                            commonService.handleOrderRelevantBusinessAfterOnlinePayUp(orderNumber, orderId, respCode, OnlinePayType.UNION_PAY);
                            //发送订单到拆单消息队列
                            sinkSender.sendOrder(orderNumber);

                            logger.info("unionPayReturnAsyncBack OUT,银联支付后台回调处理成功，出参 result:{}", "success");
                            //发送订单到WMS
                            OrderBaseInfo baseInfo = orderService.getOrderByOrderNumber(orderNumber);
                            if (baseInfo.getDeliveryType() == AppDeliveryType.HOUSE_DELIVERY) {
                                iCallWms.sendToWmsRequisitionOrderAndGoods(orderNumber);
                            }
                        }
                    } else if (paymentDataDO.getPaymentType() == PaymentDataType.BILLPAY) {
                        if (null != paymentDataDO.getId() && new Double(paymentDataDO.getTotalFee() * 100).intValue() == settleAmt) {
                            String orderNumber = paymentDataDO.getOrderNumber();
                            paymentDataDO.setTradeNo(queryId);
                            paymentDataDO.setTradeStatus(PaymentDataStatus.TRADE_SUCCESS);
                            paymentDataDO.setNotifyTime(new Date());
                            this.paymentDataService.updateByTradeStatusIsWaitPay(paymentDataDO);
                            logger.info("alipayReturnAsync ,银联支付回调接口，支付数据记录信息 paymentDataDO:{}",
                                    paymentDataDO);
                            //处理第三方支付成功之后订单相关事务
                            billInfoService.handleBillRepaymentAfterOnlinePayUp(orderNumber, OnlinePayType.UNION_PAY, paymentDataDO.getAppIdentityType().getValue());
                            //将收款记录入拆单消息队列
                            sinkSender.sendRechargeReceipt(orderNumber);
                            logger.warn("alipayReturnAsync OUT,银联支付回调接口处理成功，出参 result:{}", "success");
                        }
                    }
                    LogUtil.writeLog("unionPayReturnAsyncBack接收后台通知结束");
                    //返回给银联服务器http 200  状态码
                    resp.setStatus(200);
                    resp.getWriter().print("ok");
                    return;
                } else {
                    logger.warn("unionPayReturnAsyncBack OUT,银联支付后台回调处理成功，订单支付失败，商户订单号:{}", orderId);
                    //返回给银联服务器http 200  状态码
                    resp.setStatus(200);
                    resp.getWriter().print("ok");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("{}", e);
            resp.setStatus(500);
            try {
                resp.getWriter().write("fail");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 前台通知
     *
     * @param req
     * @param resp
     */
    @RequestMapping(value = "/return/async/front")
    public void unionPayReturnAsyncFront(HttpServletRequest req, HttpServletResponse resp) {

        LogUtil.writeLog("unionPayReturnAsyncFront IN,银联支付前台回调通知开始");
        try {
            String encoding = req.getParameter(SDKConstants.param_encoding);
            // 获取银联通知服务器发送的后台通知参数
            Map<String, String> reqParam = getAllRequestParam(req);
            LogUtil.printRequestLog(reqParam);

            //重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
            if (!AcpService.validate(reqParam, encoding)) {
                LogUtil.writeLog("验证签名结果[失败].");
                //验签失败，需解决验签问题

            } else {
                LogUtil.writeLog("验证签名结果[成功].");
                //【注：为了安全验签成功才应该写商户的成功处理逻辑】交易成功，更新商户订单状态
                /**
                 * 商户订单号
                 */
                String orderId = reqParam.get("orderId");
                /**
                 * 支付结果码
                 */
                String respCode = reqParam.get("respCode");
                /**
                 * 支付金额
                 */
                Double settleAmt = Double.parseDouble(reqParam.get("settleAmt"));

                //判断respCode=00、A6后，对涉及资金类的交易，请再发起查询接口查询，确定交易成功后更新数据库。
            }
            LogUtil.writeLog("unionPayReturnAsyncFront OUT，银联支付前台回调通知结束");
            //返回给银联服务器http 200  状态码
            resp.getWriter().print("ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("{}", e);
            throw new RuntimeException("银联支付前台回调接口处理失败:" + e);
        }
    }


    /**
     * 获取请求参数中所有的信息
     *
     * @param request
     * @return
     */
    public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
                //在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
                //System.out.println("ServletUtil类247行  temp数据的键=="+en+"     值==="+value);
                if (null == res.get(en) || "".equals(res.get(en))) {
                    res.remove(en);
                }
            }
        }
        return res;
    }

    /**
     * 生成银联支付请求html
     *
     * @param payableAmount 支付金额（单位元）
     * @param outTradeNo    商户订单号
     * @return
     */
    protected String generatePaymentHtml(Double payableAmount, String outTradeNo) {
        //加载配置文件
        SDKConfig.getConfig().loadPropertiesFromSrc();// 从classpath加载acp_sdk.properties文件

        //前台页面传过来的
        String txnAmt = Integer.valueOf(new Double(payableAmount * 100).intValue()).toString();

        Map<String, String> requestData = new HashMap<String, String>();


        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/

        requestData.put("version", SDKConfig.getConfig().getVersion());     //版本号，全渠道默认值

        requestData.put("encoding", AppConstant.UNION_PAY_ENCODING);   //字符集编码，可以使用UTF-8,GBK两种方式

        requestData.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法

        requestData.put("txnType", "01");                 //交易类型 ，01：消费

        requestData.put("txnSubType", "01");              //交易子类型， 01：自助消费

        requestData.put("bizType", "000201");             //业务类型，B2C网关支付，手机wap支付

        requestData.put("channelType", "08");             //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板  08：手机


        /***商户接入参数***/
        requestData.put("merId", AppConstant.UNION_PAY_MERCHANT_ID);                //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号

        requestData.put("accessType", "0");               //接入类型，0：直连商户

        requestData.put("orderId", outTradeNo);            //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则

        requestData.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));        //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效

        requestData.put("currencyCode", "156");           //交易币种（境内商户一般是156 人民币）

        requestData.put("txnAmt", txnAmt);                   //交易金额，单位分，不要带小数点

        //requestData.put("reqReserved", "透传字段");              //请求方保留域，如需使用请启用即可；透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节。出现&={}[]符号时可能导致查询接口应答报文解析失败，建议尽量只传字母数字并使用|分割，或者可以最外层做一次base64编码(base64编码之后出现的等号不会导致解析失败可以不用管)。


        //前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址

        //如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限

        //异步通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知

        requestData.put("frontUrl", AppApplicationConstant.unionPayAsyncUrlFront);


        //后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知

        //后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知

        //注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码

        //    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。

        //    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败

        requestData.put("backUrl", AppApplicationConstant.unionPayAsyncUrlBack);

        // 订单超时时间。

        // 超过此时间后，除网银交易外，其他交易银联系统会拒绝受理，提示超时。 跳转银行网银交易如果超时后交易成功，会自动退款，大约5个工作日金额返还到持卡人账户。

        // 此时间建议取支付时的北京时间加15分钟。

        // 超过超时时间调查询接口应答origRespCode不是A6或者00的就可以判断为失败。

        requestData.put("payTimeout", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date().getTime() + 15 * 60 * 1000));


        //////////////////////////////////////////////////

        //

        //       报文中特殊用法请查看 PCwap网关跳转支付特殊用法.txt

        //

        //////////////////////////////////////////////////


        /**请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面**/

        Map<String, String> submitFromData = AcpService.sign(requestData, AppConstant.UNION_PAY_ENCODING);  //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。


        String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();  //获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl

        String html = AcpService.createAutoFormHtml(requestFrontUrl, submitFromData, AppConstant.UNION_PAY_ENCODING);   //生成自动跳转的Html表单
        return html;
    }


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


    /**
     * @title   银联支付账单还款
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/6/28
     */
    @PostMapping(value = "/bill/pay/html", produces = "application/json")
    public ResultDTO billUnionPayHtml(Long userId, Integer identityType, String repaymentNo, HttpServletResponse response) {

        logger.info("billUnionPayHtml CALLED,银联支付账单还款信息提交,入参 userId:{}, identityType:{}, repaymentNo:{}",
                userId, identityType, repaymentNo);
        ResultDTO<String> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("billUnionPayHtml OUT,银联支付账单还款信息提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("billUnionPayHtml OUT,银联支付账单还款信息提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == repaymentNo) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单号不能为空！", null);
            logger.info("billUnionPayHtml OUT,银联支付账单还款信息提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            BillRepaymentInfoDO billRepaymentInfoDO = this.billInfoService.findBillRepaymentInfoByRepaymentNo(repaymentNo);

            if (null == billRepaymentInfoDO || null == billRepaymentInfoDO.getBillNo() || null == billRepaymentInfoDO.getOnlinePayAmount() ||
                    billRepaymentInfoDO.getOnlinePayAmount() < AppConstant.PAY_UP_LIMIT) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "支付金额错误！", null);
                logger.info("billUnionPayHtml OUT,银联支付账单还款信息提交失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null != billRepaymentInfoDO.getIsPaid() && billRepaymentInfoDO.getIsPaid()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "已支付，请勿重复提交！", null);
                logger.info("billUnionPayHtml OUT,银联支付账单还款信息提交失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            String outTradeNo = OrderUtils.generatePayNumber();
            PaymentDataDO paymentData = new PaymentDataDO();
            paymentData.setUserId(userId);
            paymentData.setOnlinePayType(OnlinePayType.UNION_PAY);
            paymentData.setPaymentType(PaymentDataType.BILLPAY);
            paymentData.setPaymentTypeDesc(PaymentDataType.BILLPAY.getDescription());
            paymentData.setAppIdentityType(AppIdentityType.getAppIdentityTypeByValue(identityType));
            paymentData.setCreateTime(LocalDateTime.now());
            paymentData.setOutTradeNo(outTradeNo);
            paymentData.setOrderNumber(repaymentNo);
            paymentData.setTotalFee(billRepaymentInfoDO.getOnlinePayAmount());
            paymentData.setTradeStatus(PaymentDataStatus.WAIT_PAY);
            paymentData.setNotifyUrl(AppApplicationConstant.unionPayAsyncUrlBack);
            paymentData.setRemarks("账单还款");
            paymentDataService.save(paymentData);

            //生成银联支付请求html
            String html = this.generatePaymentHtml(billRepaymentInfoDO.getOnlinePayAmount(), outTradeNo);

            LogUtil.writeLog("打印请求HTML，此为请求报文，为联调排查问题的依据：" + html);

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, html);
            response.getWriter().write(resultDTO.getContent());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("银联支付账单还款信息提交发生异常:" + e);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "银联支付账单还款信息提交异常", null);
            return resultDTO;
        }
    }




}