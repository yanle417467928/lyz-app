package cn.com.leyizhuang.app.web.controller.deliveryClerk;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.getui.NoticePushUtils;
import cn.com.leyizhuang.app.core.utils.SmsUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.core.utils.oss.FileUploadOSSUtils;
import cn.com.leyizhuang.app.foundation.pojo.EmpCreditMoney;
import cn.com.leyizhuang.app.foundation.pojo.EmpCreditMoneyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.response.ArrearsAuditResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderArrearageInfoResponse;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.service.impl.SmsAccountServiceImpl;
import cn.com.leyizhuang.app.remote.queue.SinkSender;
import cn.com.leyizhuang.common.core.constant.ArrearsAuditStatus;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.SmsAccount;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.CountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author GenerationRoad
 * @date 2017/11/23
 */
@RestController
@RequestMapping(value = "/app/orderArrive")
public class OrderArriveController {

    private static final Logger logger = LoggerFactory.getLogger(OrderArriveController.class);

    @Autowired
    private AppOrderService appOrderServiceImpl;

    @Autowired
    private ArrearsAuditService arrearsAuditServiceImpl;

    @Autowired
    private OrderAgencyFundService orderAgencyFundServiceImpl;

    @Autowired
    private OrderDeliveryInfoDetailsService orderDeliveryInfoDetailsServiceImpl;

    @Autowired
    private AppEmployeeService appEmployeeService;

    @Autowired
    private SinkSender sinkSender;

    @Autowired
    private CommonService CommonServiceImpl;

    @Autowired
    private SmsAccountServiceImpl smsAccountService;

    /**
     * @param
     * @return
     * @throws
     * @title 配送员确认订单送达
     * @descripe
     * @author GenerationRoad
     * @date 2017/11/23
     */
    @PostMapping(value = "/confirm", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> confirmOrderArrive(Long userId, Integer identityType, String orderNo, String pickUpCode, String collectionAmount,
                                                String upstairsFee, String paymentMethod, String remarks, HttpServletRequest request) {
        logger.info("confirmOrderArrive CALLED,配送员确认订单送达，入参 userId:{} identityType:{} " +
                        "orderNo:{} pickUpCode:{} collectionAmount:{} paymentMethod:{}, remarks:{} request:{}",
                userId, identityType, orderNo, pickUpCode, collectionAmount, paymentMethod, remarks, request);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("confirmOrderArrive OUT,配送员确认订单送达失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType && identityType != 1) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！", null);
            logger.info("confirmOrderArrive OUT,配送员确认订单送达失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == orderNo) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单号不能为空！", null);
            logger.info("confirmOrderArrive OUT,配送员确认订单送达失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == pickUpCode) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "取货码不能为空！", null);
            logger.info("confirmOrderArrive OUT,配送员确认订单送达失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (paymentMethod.contains("POS")) {
            paymentMethod = "POS";
        }
        if (paymentMethod.contains("现金")) {
            paymentMethod = "现金";
        }
        if (null == collectionAmount || "".equals(collectionAmount.trim())) {
            collectionAmount = "0";
        }
        Double amount;
        try {
            amount = Double.parseDouble(collectionAmount);
        } catch (Exception e) {
            amount = 0D;
        }
        Double upstairFee = 0D;
        if (null != upstairsFee && !"".equals(upstairsFee)){
            try {
                upstairFee = Double.parseDouble(upstairsFee);
            } catch (Exception e) {
                upstairFee = 0D;
            }
        }

        try {
            //根据订单号查询订单信息
            OrderTempInfo orderTempInfo = this.appOrderServiceImpl.getOrderInfoByOrderNo(orderNo);
            //根据订单号查询订单账单信息
            OrderBillingDetails billingDetails = this.appOrderServiceImpl.getOrderBillingDetail(orderNo);
            if (null == orderTempInfo || null == billingDetails || !(orderTempInfo.getOrderStatus().equals(AppOrderStatus.PENDING_RECEIVE.getValue()))) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单信息错误！", null);
                logger.info("confirmOrderArrive OUT,配送员确认订单送达失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            //查询是否有欠款待审核
            List<ArrearsAuditStatus> arrearsAuditStatusList = new ArrayList<ArrearsAuditStatus>();
            arrearsAuditStatusList.add(ArrearsAuditStatus.AUDITING);
            List<ArrearsAuditResponse> arrearsAuditResponseList = this.arrearsAuditServiceImpl.findByUserIdAndOrderNoAndStatus(userId, orderNo, arrearsAuditStatusList);
            if (null != arrearsAuditResponseList && arrearsAuditResponseList.size() > 0) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "你有欠款审核还在审核中，请不要重复提交！", null);
                logger.info("confirmOrderArrive OUT,配送员确认订单送达失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            //验证取货码是否正确
            String pickCode = orderTempInfo.getPickUpCode();

            if ("9999".equals(pickUpCode) || pickCode.equals(pickUpCode)) {
                amount = null == amount ? 0D : amount;
                Double collectionAmountOrder = null == orderTempInfo.getCollectionAmount() ? 0D : orderTempInfo.getCollectionAmount();
                Double ownManey = null == orderTempInfo.getOwnMoney() ? 0D : orderTempInfo.getOwnMoney();
                //判断是否货到付款--如果是订单欠款必须付清
                if (OnlinePayType.CASH_DELIVERY.equals(billingDetails.getOnlinePayType()) && amount < ownManey) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "货到付款的订单必须付清欠款！", null);
                    logger.info("confirmOrderArrive OUT,配送员确认订单送达失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }

                //判断配送员代收金额是否大于导购输入的代收金额
                if (amount > collectionAmountOrder) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收款金额不能大于代收金额！", null);
                    logger.info("confirmOrderArrive OUT,配送员确认订单送达失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (amount < 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收款金额不能小于0！", null);
                    logger.info("confirmOrderArrive OUT,配送员确认订单送达失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                //上传图片
//            String picture = "";
//            for (int i = 0; i < files.length; i++) {
//                picture += FileUploadOSSUtils.uploadProfilePhoto(files[i], "logistics/photo");
//                picture += ",";
//            }
            /*
             * 因为图片只能上传一张，修改传入参数为request
             * GenerationRoad
             */
                StringBuilder picture = new StringBuilder();
                CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                        request.getSession().getServletContext());
                if (multipartResolver.isMultipart(request)) {
                    // 转换成多部分request
                    MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                    // 取得request中的所有文件名
                    Iterator<String> iter = multiRequest.getFileNames();
                    int i = 0;
                    while (iter.hasNext()) {
                        // 取得上传文件
                        MultipartFile f = multiRequest.getFile(iter.next());
                        if (f != null) {
                            // 取得当前上传文件的文件名称
                            String myFileName = f.getOriginalFilename();
                            // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
                            if (!"".equals(myFileName.trim())) {
                                // 定义上传路径
                                if (!iter.hasNext()) {
                                    picture.append(FileUploadOSSUtils.uploadProfilePhoto(f, "order/photo"));
                                } else {
                                    picture.append(FileUploadOSSUtils.uploadProfilePhoto(f, "order/photo")).append(",");
                                }
                                i += 1;
                                if (i > 2) {
                                    break;
                                }
                            }
                        }
                    }
                }

                OrderArrearsAuditDO orderArrearsAuditDO = null;
                OrderBillingPaymentDetails paymentDetails = null;
                OrderBillingDetails orderBillingDetails = null;
                EmpCreditMoneyChangeLog empCreditMoneyChangeLog = null;
                OrderAgencyFundDO orderAgencyFundDO = null;
                OrderDeliveryInfoDetails orderDeliveryInfoDetails = null;
                OrderBaseInfo orderBaseInfo = null;
                String receiptNumber = null;
                Double credit = 0D;
                //获取导购信用金
                EmpCreditMoney empCreditMoney = appEmployeeService.findEmpCreditMoneyByEmpId(orderTempInfo.getSellerId());
                if (null == empCreditMoney) {
                    empCreditMoney = new EmpCreditMoney();
                    empCreditMoney.setCreditLimitAvailable(0D);
                }

                //判断订单是否有欠款
                if (ownManey > 0) {
                    if (ownManey > amount) {//欠款金额 > 收款金额
                        //创建欠款审核
                        orderArrearsAuditDO = new OrderArrearsAuditDO();
                        orderArrearsAuditDO.setOrderInfo(userId, orderNo, collectionAmountOrder, ownManey);
                        orderArrearsAuditDO.setCustomerAndSeller(orderTempInfo.getCustomerName(), orderTempInfo.getCustomerPhone(), orderTempInfo.getSellerId(),
                                orderTempInfo.getSellerName(), orderTempInfo.getSellerPhone());
                        orderArrearsAuditDO.setDistributionInfo(orderTempInfo.getShippingAddress(), LocalDateTime.now());
                        if (amount.equals(collectionAmountOrder)) {
                            orderArrearsAuditDO.setArrearsAuditInfo(paymentMethod, amount, remarks, ArrearsAuditStatus.AUDIT_PASSED);
                            if (amount > 0D) {
                                //生成收款单号
                                receiptNumber = OrderUtils.generateReceiptNumber(orderTempInfo.getCityId());
                                //创建收款记录
                                paymentDetails = new OrderBillingPaymentDetails(Calendar.getInstance().getTime(), orderTempInfo.getOrderId(),
                                        Calendar.getInstance().getTime(), OrderBillingPaymentType.getOrderBillingPaymentTypeByDescription(paymentMethod),
                                        paymentMethod, orderNo, PaymentSubjectType.DELIVERY_CLERK,
                                        PaymentSubjectType.DELIVERY_CLERK.getDescription(), amount, "", receiptNumber);
                                paymentDetails.setPaymentSubjectId(userId);
                                //修改订单欠款为0
                                orderBillingDetails = new OrderBillingDetails();
                                orderBillingDetails.setOrderNumber(orderNo);
                                orderBillingDetails.setArrearage(CountUtil.sub(ownManey, amount));
                                orderBillingDetails.setIsPayUp(false);
                                orderBillingDetails.setPayUpTime(new Date());
                                if (OrderBillingPaymentType.POS.equals(paymentDetails.getPayType())) {
                                    orderBillingDetails.setDeliveryCash(0D);
                                    orderBillingDetails.setDeliveryPos(amount);
                                } else if (OrderBillingPaymentType.CASH.equals(paymentDetails.getPayType())) {
                                    orderBillingDetails.setDeliveryCash(amount);
                                    orderBillingDetails.setDeliveryPos(0D);
                                }
                                credit = amount;
                                //返还信用金后导购信用金额度
                                Double creditMoney = CountUtil.add(empCreditMoney.getCreditLimitAvailable() + amount);

                                //记录导购信用金变更日志
                                empCreditMoneyChangeLog = new EmpCreditMoneyChangeLog();
                                empCreditMoneyChangeLog.setEmpId(orderTempInfo.getSellerId());
                                empCreditMoneyChangeLog.setCreateTime(new Date());
                                empCreditMoneyChangeLog.setCreditLimitAvailableChangeAmount(amount);
                                empCreditMoneyChangeLog.setCreditLimitAvailableAfterChange(creditMoney);
                                empCreditMoneyChangeLog.setReferenceNumber(orderNo);
                                empCreditMoneyChangeLog.setChangeType(EmpCreditMoneyChangeType.ORDER_REPAYMENT);
                                empCreditMoneyChangeLog.setChangeTypeDesc("订单还款");
                                empCreditMoneyChangeLog.setOperatorId(userId);
                                empCreditMoneyChangeLog.setOperatorType(AppIdentityType.DELIVERY_CLERK);
                            }
                        } else {
                            orderArrearsAuditDO.setArrearsAuditInfo(paymentMethod, amount, remarks, ArrearsAuditStatus.AUDITING);
                        }
                        orderArrearsAuditDO.setPicture(picture.toString());
                        this.arrearsAuditServiceImpl.save(orderArrearsAuditDO);

                        //加上楼费
                        if (upstairFee > 0D) {
                            orderBillingDetails = new OrderBillingDetails();
                            orderBillingDetails.setUpstairsFee(upstairFee);
                            this.appOrderServiceImpl.updateOwnMoneyByOrderNo(orderBillingDetails);
                        }


                        if (!(amount.equals(collectionAmountOrder))) {
                            //短信提醒
                            String info = "您有新的欠款审核单，请及时处理。谢谢！";
                            String content = "";
                            try {
                                content = URLEncoder.encode(info, "GB2312");
                                System.err.println(content);
                            } catch (Exception e) {
                                e.printStackTrace();
                                logger.info("confirmOrderArrive EXCEPTION，提醒短信发送失败");
                                logger.warn("{}", e);
                            }
                            SmsAccount account = smsAccountService.findOne();
                            String returnCode;
                            try {
                                returnCode = SmsUtils.sendMessageQrCode(account.getEncode(), account.getEnpass(), account.getUserName(), orderTempInfo.getSellerPhone(), content);
                            } catch (IOException e) {
                                logger.info("confirmOrderArrive EXCEPTION，提醒短信发送失败");
                                logger.warn("{}", e);
                            } catch (Exception e) {
                                logger.info("confirmOrderArrive EXCEPTION，提醒短信发送失败");
                                logger.warn("{}", e);
                            }
                            //发送推送新消息给导购
                            NoticePushUtils.pushApplyArrearageInfo(orderArrearsAuditDO.getSellerId());

                            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "欠款审核提交成功,正在审核中!", null);
                            logger.info("confirmOrderArrive OUT,配送员确认订单送达申请欠款审核，出参 resultDTO:{}", resultDTO);
                            return resultDTO;
                        }
                    } else { //欠款金额 <= 收款金额
                        if (ownManey > 0) {
                            //生成收款单号
                            receiptNumber = OrderUtils.generateReceiptNumber(orderTempInfo.getCityId());
                            //创建收款记录
                            paymentDetails = new OrderBillingPaymentDetails(Calendar.getInstance().getTime(), orderTempInfo.getOrderId(),
                                    Calendar.getInstance().getTime(), OrderBillingPaymentType.getOrderBillingPaymentTypeByDescription(paymentMethod),
                                    paymentMethod, orderNo, PaymentSubjectType.DELIVERY_CLERK,
                                    PaymentSubjectType.DELIVERY_CLERK.getDescription(), ownManey, "", receiptNumber);
                            paymentDetails.setPaymentSubjectId(userId);
//                        this.appOrderServiceImpl.savePaymentDetails(paymentDetails);

                            //修改订单欠款为0
                            orderBillingDetails = new OrderBillingDetails();
                            orderBillingDetails.setOrderNumber(orderNo);
                            orderBillingDetails.setArrearage(0D);
                            orderBillingDetails.setIsPayUp(true);
                            orderBillingDetails.setPayUpTime(new Date());
                            if (OrderBillingPaymentType.POS.equals(paymentDetails.getPayType())) {
                                orderBillingDetails.setDeliveryCash(0D);
                                orderBillingDetails.setDeliveryPos(ownManey);
                            } else if (OrderBillingPaymentType.CASH.equals(paymentDetails.getPayType())) {
                                orderBillingDetails.setDeliveryCash(ownManey);
                                orderBillingDetails.setDeliveryPos(0D);
                            }
//                        this.appOrderServiceImpl.updateOwnMoneyByOrderNo(orderBillingDetails);
                            if (!(OnlinePayType.CASH_DELIVERY.equals(billingDetails.getOnlinePayType()))) {
                                credit = ownManey;
                                //返还信用金后导购信用金额度
                                Double creditMoney = CountUtil.add(empCreditMoney.getCreditLimitAvailable() + ownManey);

    //                        //修改导购信用额度
    //                        Integer affectLine = appEmployeeService.unlockGuideCreditByUserIdAndGuideCreditAndVersion(orderTempInfo.getSellerId(), ownManey, empCreditMoney.getLastUpdateTime());
    //                        if (affectLine > 0) {
                                //记录导购信用金变更日志
                                empCreditMoneyChangeLog = new EmpCreditMoneyChangeLog();
                                empCreditMoneyChangeLog.setEmpId(orderTempInfo.getSellerId());
                                empCreditMoneyChangeLog.setCreateTime(new Date());
                                empCreditMoneyChangeLog.setCreditLimitAvailableChangeAmount(ownManey);
                                empCreditMoneyChangeLog.setCreditLimitAvailableAfterChange(creditMoney);
                                empCreditMoneyChangeLog.setReferenceNumber(orderNo);
                                empCreditMoneyChangeLog.setChangeType(EmpCreditMoneyChangeType.ORDER_REPAYMENT);
                                empCreditMoneyChangeLog.setChangeTypeDesc("订单还款");
                                empCreditMoneyChangeLog.setOperatorId(userId);
                                empCreditMoneyChangeLog.setOperatorType(AppIdentityType.DELIVERY_CLERK);
                                //保存日志
    //                            appEmployeeService.addEmpCreditMoneyChangeLog(empCreditMoneyChangeLog);
    //                        }
                            }
                        }
                    }
                }

                //判断是否有代收款
                if (amount > 0D) {
                    Double returnMoney = CountUtil.sub(amount - ownManey);
                    if (returnMoney < 0) {
                        returnMoney = 0D;
                    }
                    //生成代收款记录
                    orderAgencyFundDO = new OrderAgencyFundDO();
                    orderAgencyFundDO.setOrderInfo(userId, orderNo, collectionAmountOrder);
                    orderAgencyFundDO.setCustomerAndSeller(orderTempInfo.getCustomerName(), orderTempInfo.getCustomerPhone(),
                            orderTempInfo.getSellerId(), orderTempInfo.getSellerName(), orderTempInfo.getSellerPhone());
                    orderAgencyFundDO.setAgencyFundInfo(paymentMethod, amount, returnMoney, remarks);
//                this.orderAgencyFundServiceImpl.save(orderAgencyFundDO);
                }

                //生成订单物流详情
           /* AppEmployee appEmployee = this.appEmployeeServiceImpl.findDeliveryClerkNoByUserId(userId);
            String deliveryClerkNo = "";
            if (null != appEmployee && null != appEmployee.getDeliveryClerkNo()){
                deliveryClerkNo = appEmployee.getDeliveryClerkNo();
            }*/
                orderDeliveryInfoDetails = new OrderDeliveryInfoDetails();
                orderDeliveryInfoDetails.setDeliveryInfo(orderNo, LogisticStatus.CONFIRM_ARRIVAL, "确认到货！", "送达",
                        orderTempInfo.getOperatorNo(), picture.toString(), "", "");
//            this.orderDeliveryInfoDetailsServiceImpl.addOrderDeliveryInfoDetails(orderDeliveryInfoDetails);

                //修改订单状态
                orderBaseInfo = new OrderBaseInfo();
                orderBaseInfo.setOrderNumber(orderNo);
                orderBaseInfo.setStatus(AppOrderStatus.FINISHED);
                orderBaseInfo.setDeliveryStatus(LogisticStatus.CONFIRM_ARRIVAL);
//            this.appOrderServiceImpl.updateOrderStatusByOrderNo(orderBaseInfo);

                orderBillingDetails.setUpstairsFee(upstairFee);

                this.CommonServiceImpl.confirmOrderArrive(paymentDetails, orderBillingDetails, empCreditMoneyChangeLog,
                        orderAgencyFundDO, orderDeliveryInfoDetails, orderBaseInfo, orderTempInfo.getSellerId(), credit, empCreditMoney.getLastUpdateTime());

                //将收款记录录入拆单消息队列
                if (null != receiptNumber) {
                    this.sinkSender.sendOrderReceipt(receiptNumber);
                }
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                logger.info("confirmOrderArrive OUT,配送员确认订单送达成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }else{
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "取货码输入错误！", null);
                logger.info("confirmOrderArrive OUT,配送员确认订单送达失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，配送员确认订单送达失败", null);
            logger.warn("confirmOrderArrive EXCEPTION,配送员确认订单送达失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }



    /**
     * @param
     * @return
     * @throws
     * @title 配送员跳转订单送达页面
     * @descripe
     * @author GenerationRoad
     * @date 2017/12/19
     */
    @PostMapping(value = "/skip", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> skipOrderArrive(Long userId, Integer identityType, String orderNo) {
        logger.info("skipOrderArrive CALLED,配送员跳转订单送达页面，入参 userId:{} identityType:{} orderNo:{}", userId, identityType, orderNo);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("skipOrderArrive OUT,配送员跳转订单送达页面失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType && identityType != 1) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！", null);
            logger.info("skipOrderArrive OUT,配送员跳转订单送达页面失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == orderNo) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单号不能为空！", null);
            logger.info("skipOrderArrive OUT,配送员跳转订单送达页面失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        OrderArrearageInfoResponse orderArrearageInfoResponse = this.appOrderServiceImpl.getOrderArrearageInfo(orderNo);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, orderArrearageInfoResponse);
        logger.info("skipOrderArrive OUT,配送员跳转订单送达页面成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

}
