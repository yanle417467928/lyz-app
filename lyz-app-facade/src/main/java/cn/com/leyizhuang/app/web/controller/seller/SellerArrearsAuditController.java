package cn.com.leyizhuang.app.web.controller.seller;

import cn.com.leyizhuang.app.core.bean.GridDataVO;
import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.pojo.EmpCreditMoney;
import cn.com.leyizhuang.app.foundation.pojo.EmpCreditMoneyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.PaymentDataDO;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.response.ArrearageListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.RepaymentDetailResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.RepaymentMoneyListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.SellerArrearsAuditResponse;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.remote.queue.SinkSender;
import cn.com.leyizhuang.common.core.constant.ArrearsAuditStatus;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.CountUtil;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/24
 */
@RestController
@RequestMapping(value = "/app/seller/arrearsAudit")
public class SellerArrearsAuditController {

    private static final Logger logger = LoggerFactory.getLogger(cn.com.leyizhuang.app.web.controller.deliveryClerk.ArrearsAuditController.class);

    @Autowired
    private ArrearsAuditService arrearsAuditServiceImpl;

    @Autowired
    private AppOrderService appOrderServiceImpl;

    @Autowired
    private OrderAgencyFundService orderAgencyFundServiceImpl;

    @Autowired
    private OrderDeliveryInfoDetailsService orderDeliveryInfoDetailsServiceImpl;

    @Autowired
    private PaymentDataService paymentDataService;

    @Autowired
    private AppEmployeeService appEmployeeService;

    @Autowired
    private SinkSender sinkSender;

    @Autowired
    private CommonService CommonServiceImpl;


    /**
     * @param
     * @return
     * @throws
     * @title 导购获取待审核欠款申请列表
     * @descripe
     * @author GenerationRoad
     * @date 2017/11/24
     */
    @PostMapping(value = "/auditing/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getArrearsAuditListBySeller(Long userId, Integer identityType) {
        logger.info("getArrearsAuditListBySeller CALLED,导购获取待审核欠款申请列表，入参 userId:{} identityType:{}", userId, identityType);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("getArrearsAuditListBySeller OUT,导购获取待审核欠款申请列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType && identityType != 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！", null);
            logger.info("getArrearsAuditListBySeller OUT,导购获取待审核欠款申请列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        List<ArrearsAuditStatus> arrearsAuditStatusList = new ArrayList<ArrearsAuditStatus>();
        arrearsAuditStatusList.add(ArrearsAuditStatus.AUDITING);
        PageInfo<SellerArrearsAuditResponse> arrearsAuditResponseList = this.arrearsAuditServiceImpl.findBySellerIdAndStatus(userId, arrearsAuditStatusList,null,null);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, arrearsAuditResponseList.getList());
        logger.info("getArrearsAuditListBySeller OUT,导购获取待审核欠款申请列表成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

    /**
     * @param
     * @return
     * @throws
     * @title 导购获取已审核欠款申请列表
     * @descripe
     * @author GenerationRoad
     * @date 2017/11/24
     */
    @PostMapping(value = "/audited/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> findArrearsAuditedListBySeller(Long userId, Integer identityType,Integer page, Integer size) {
        logger.info("findArrearsAuditedListBySeller CALLED,导购获取已审核欠款申请列表，入参 userId:{} identityType:{},page:{},size:{}", userId, identityType,page,size);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("findArrearsAuditedListBySeller OUT,导购获取已审核欠款申请列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType && identityType != 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！", null);
            logger.info("findArrearsAuditedListBySeller OUT,导购获取已审核欠款申请列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("findArrearsAuditedListBySeller OUT,导购获取已审核欠款申请列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("findArrearsAuditedListBySeller OUT,导购获取已审核欠款申请列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        List<ArrearsAuditStatus> arrearsAuditStatusList = new ArrayList<ArrearsAuditStatus>();
        arrearsAuditStatusList.add(ArrearsAuditStatus.AUDIT_NO);
        arrearsAuditStatusList.add(ArrearsAuditStatus.AUDIT_PASSED);
        PageInfo<SellerArrearsAuditResponse> arrearsAuditResponseList = this.arrearsAuditServiceImpl.findBySellerIdAndStatus(userId, arrearsAuditStatusList, page, size);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, new GridDataVO<SellerArrearsAuditResponse>().transform(arrearsAuditResponseList));
        logger.info("findArrearsAuditedListBySeller OUT,导购获取已审核欠款申请列表成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

    /**
     * @param
     * @return
     * @throws
     * @title 导购审批欠款申请
     * @descripe
     * @author GenerationRoad
     * @date 2017/11/27
     */
    @PostMapping(value = "/audit", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> audit(Long userId, Integer identityType, Long arrearsAuditId, Boolean result) {
        logger.info("audit CALLED,导购审批欠款申请，入参 userId:{} identityType:{} arrearsAuditId:{} result:{}", userId, identityType, arrearsAuditId, result);
        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("audit OUT,导购审批欠款申请失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == identityType && identityType != 0) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！",
                        null);
                logger.info("audit OUT,导购审批欠款申请失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            if (null == arrearsAuditId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "审核单id不能为空！",
                        null);
                logger.info("audit OUT,导购审批欠款申请失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == result) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "审核结果不能为空！",
                        null);
                logger.info("audit OUT,导购审批欠款申请失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            OrderArrearsAuditDO orderArrearsAuditDO = this.arrearsAuditServiceImpl.findById(arrearsAuditId);
            if (null == orderArrearsAuditDO || !(orderArrearsAuditDO.getStatus().equals(ArrearsAuditStatus.AUDITING))) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "欠款审核单信息错误！",
                        null);
                logger.info("audit OUT,导购审批欠款申请失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            String orderNo = orderArrearsAuditDO.getOrderNumber();
            OrderTempInfo orderTempInfo = this.appOrderServiceImpl.getOrderInfoByOrderNo(orderNo);
            if (null == orderTempInfo) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单信息错误！",
                        null);
                logger.info("audit OUT,导购审批欠款申请失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;

            }
            if (result) {

                Double collectionAmount = null == orderArrearsAuditDO.getRealMoney() ? 0D : orderArrearsAuditDO.getRealMoney();
                Double collectionAmountOrder = null == orderTempInfo.getCollectionAmount() ? 0D : orderTempInfo.getCollectionAmount();
                //生成代收款记录
                OrderAgencyFundDO orderAgencyFundDO = new OrderAgencyFundDO();
                orderAgencyFundDO.setOrderInfo(orderArrearsAuditDO.getUserId(), orderNo, collectionAmountOrder);
                orderAgencyFundDO.setCustomerAndSeller(orderTempInfo.getCustomerName(), orderTempInfo.getCustomerPhone(),
                        orderTempInfo.getSellerId(), orderTempInfo.getSellerName(), orderTempInfo.getSellerPhone());
                orderAgencyFundDO.setAgencyFundInfo(orderArrearsAuditDO.getPaymentMethod(), collectionAmount, 0D, orderArrearsAuditDO.getRemarks());
//                this.orderAgencyFundServiceImpl.save(orderAgencyFundDO);

                //生成收款单号
                String receiptNumber = OrderUtils.generateReceiptNumber(orderTempInfo.getCityId());

                //创建收款记录
                OrderBillingPaymentDetails paymentDetails = new OrderBillingPaymentDetails(Calendar.getInstance().getTime(),
                        orderTempInfo.getOrderId(), Calendar.getInstance().getTime(), OrderBillingPaymentType.getOrderBillingPaymentTypeByDescription(orderArrearsAuditDO.getPaymentMethod()),
                        orderArrearsAuditDO.getPaymentMethod(), orderNo, PaymentSubjectType.DELIVERY_CLERK,
                        PaymentSubjectType.DELIVERY_CLERK.getDescription(), collectionAmount, null, receiptNumber);
                //paymentDetails.setConstructor(orderTempInfo.getOrderId(), "实际货币", orderArrearsAuditDO.getPaymentMethod(), orderNo, collectionAmount, "");
//                this.appOrderServiceImpl.savePaymentDetails(paymentDetails);

                //修改订单欠款
                OrderBillingDetails orderBillingDetails = new OrderBillingDetails();
                orderBillingDetails.setOrderNumber(orderNo);
                orderBillingDetails.setArrearage(CountUtil.sub(orderTempInfo.getOwnMoney(), collectionAmount));
                if (orderBillingDetails.getArrearage() > 0D){
                    orderBillingDetails.setIsPayUp(false);
                    orderBillingDetails.setPayUpTime(new Date());
                } else {
                    orderBillingDetails.setIsPayUp(true);
                }
                if (OrderBillingPaymentType.CASH.equals(paymentDetails.getPayType())) {
                    orderBillingDetails.setDeliveryCash(collectionAmount);
                    orderBillingDetails.setDeliveryPos(0D);
                } else if (OrderBillingPaymentType.POS.equals(paymentDetails.getPayType())) {
                    orderBillingDetails.setDeliveryCash(0D);
                    orderBillingDetails.setDeliveryPos(collectionAmount);
                }
//                this.appOrderServiceImpl.updateOwnMoneyByOrderNo(orderBillingDetails);

                //获取导购信用金
                EmpCreditMoney empCreditMoney = appEmployeeService.findEmpCreditMoneyByEmpId(orderTempInfo.getSellerId());

                //返还信用金后导购信用金额度
                Double creditMoney = CountUtil.add(empCreditMoney.getCreditLimitAvailable() + collectionAmount);

                //修改导购信用额度
                Integer affectLine = appEmployeeService.unlockGuideCreditByUserIdAndGuideCreditAndVersion(userId, collectionAmount, empCreditMoney.getLastUpdateTime());
                EmpCreditMoneyChangeLog empCreditMoneyChangeLog = null;
                if (affectLine > 0) {
                    //记录导购信用金变更日志
                    empCreditMoneyChangeLog = new EmpCreditMoneyChangeLog();
                    empCreditMoneyChangeLog.setEmpId(userId);
                    empCreditMoneyChangeLog.setCreateTime(new Date());
                    empCreditMoneyChangeLog.setCreditLimitAvailableChangeAmount(collectionAmount);
                    empCreditMoneyChangeLog.setCreditLimitAvailableAfterChange(creditMoney);
                    empCreditMoneyChangeLog.setReferenceNumber(orderNo);
                    empCreditMoneyChangeLog.setChangeType(EmpCreditMoneyChangeType.ORDER_REPAYMENT);
                    empCreditMoneyChangeLog.setChangeTypeDesc("订单还款");
                    empCreditMoneyChangeLog.setOperatorId(userId);
                    empCreditMoneyChangeLog.setOperatorType(AppIdentityType.SELLER);
                    //保存日志
//                    appEmployeeService.addEmpCreditMoneyChangeLog(empCreditMoneyChangeLog);

                }
                //生成订单物流详情
                OrderDeliveryInfoDetails orderDeliveryInfoDetails = new OrderDeliveryInfoDetails();
                orderDeliveryInfoDetails.setDeliveryInfo(orderNo, LogisticStatus.CONFIRM_ARRIVAL, "确认到货！", "送达", orderTempInfo.getOperatorNo(), orderArrearsAuditDO.getPicture(), "", "");
//                this.orderDeliveryInfoDetailsServiceImpl.addOrderDeliveryInfoDetails(orderDeliveryInfoDetails);

                //修改订单状态
                OrderBaseInfo orderBaseInfo = new OrderBaseInfo();
                orderBaseInfo.setOrderNumber(orderNo);
                orderBaseInfo.setStatus(AppOrderStatus.FINISHED);
                orderBaseInfo.setDeliveryStatus(LogisticStatus.CONFIRM_ARRIVAL);
//                this.appOrderServiceImpl.updateOrderStatusByOrderNo(orderBaseInfo);

                orderArrearsAuditDO.setStatus(ArrearsAuditStatus.AUDIT_PASSED);
                orderArrearsAuditDO.setUpdateTime(LocalDateTime.now());
//                this.arrearsAuditServiceImpl.updateStatusById(orderArrearsAuditDO);

                this.CommonServiceImpl.sellerAudit(orderAgencyFundDO, paymentDetails, orderBillingDetails, empCreditMoneyChangeLog, orderDeliveryInfoDetails,
                        orderBaseInfo, orderArrearsAuditDO);

                //将收款记录录入拆单消息队列
                this.sinkSender.sendOrderReceipt(receiptNumber);
            } else {
                orderArrearsAuditDO.setStatus(ArrearsAuditStatus.AUDIT_NO);
                orderArrearsAuditDO.setUpdateTime(LocalDateTime.now());
                this.arrearsAuditServiceImpl.updateStatusById(orderArrearsAuditDO);
            }

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("audit OUT,导购审批欠款申请成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,导购审批欠款申请失败!", null);
            logger.warn("audit EXCEPTION,导购审批欠款申请失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 导购欠款查询
     *
     * @param userID       用户id
     * @param identityType 用户类型
     * @return 欠款列表
     */
    @PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> findArrearsListByUserId(Long userID, Integer identityType) {
        logger.info("findArrearsListByUserId CALLED,导购欠款查询，入参 userId:{} identityType:{}", userID, identityType);
        ResultDTO<Object> resultDTO;

        if (null == userID) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("findArrearsListByUserId OUT,导购欠款查询失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！",
                    null);
            logger.info("findArrearsListByUserId OUT,导购欠款查询失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (identityType != 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！",
                    null);
            logger.info("findArrearsListByUserId OUT,导购欠款查询失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<ArrearageListResponse> arrearageListResponseList = arrearsAuditServiceImpl.findArrearsListByUserId(userID);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, arrearageListResponseList);
            logger.info("findArrearsListByUserId OUT,导购欠款查询成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,导购欠款查询失败!", null);
            logger.warn("findArrearsListByUserId EXCEPTION,导购欠款查询失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 导购查询还款记录
     *
     * @param userID       用户id
     * @param identityType 用户类型
     * @return 返回还款记录列表
     */
    @PostMapping(value = "/repayment/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getRepaymentMondyList(Long userID, Integer identityType,Integer page, Integer size) {
        logger.info("getRepaymentMondyList CALLED,导购查询还款记录，入参 userId:{} identityType:{},page:{},size:{}", userID, identityType,page,size);
        ResultDTO<Object> resultDTO;

        if (null == userID) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("getRepaymentMondyList OUT,导购查询还款记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！",
                    null);
            logger.info("getRepaymentMondyList OUT,导购查询还款记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (identityType != 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！",
                    null);
            logger.info("getRepaymentMondyList OUT,导购查询还款记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getRepaymentMondyList OUT,导购查询还款记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getRepaymentMondyList OUT,导购查询还款记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            PageInfo<OrderBillingPaymentDetails> orderBillingPaymentDetailsList = arrearsAuditServiceImpl.getRepaymentMondyList(userID, page, size);

            List<RepaymentMoneyListResponse> repaymentMoneyListResponseList = new ArrayList<>();
            for (OrderBillingPaymentDetails orderBillingPaymentDetails : orderBillingPaymentDetailsList.getList()) {
                OrderBaseInfo orderBaseInfo = appOrderServiceImpl.getOrderByOrderNumber(orderBillingPaymentDetails.getOrderNumber());
                RepaymentMoneyListResponse repaymentMoneyListResponse = new RepaymentMoneyListResponse();
                repaymentMoneyListResponse.setRepaymentTime(sdf.format(orderBillingPaymentDetails.getPayTime()));
                repaymentMoneyListResponse.setRepaymentMoney(orderBillingPaymentDetails.getAmount());
                repaymentMoneyListResponse.setOrderNumber(orderBillingPaymentDetails.getOrderNumber());
                repaymentMoneyListResponse.setCustomerName(orderBaseInfo.getCustomerName());
                repaymentMoneyListResponseList.add(repaymentMoneyListResponse);
            }
            PageInfo<RepaymentMoneyListResponse> pageInfo = new PageInfo<>(repaymentMoneyListResponseList);
            pageInfo.setEndRow(orderBillingPaymentDetailsList.getEndRow());
            pageInfo.setNextPage(orderBillingPaymentDetailsList.getNextPage());
            pageInfo.setPageNum(orderBillingPaymentDetailsList.getPageNum());
            pageInfo.setPages(orderBillingPaymentDetailsList.getPages());
            pageInfo.setPageSize(orderBillingPaymentDetailsList.getPageSize());
            pageInfo.setPrePage(orderBillingPaymentDetailsList.getPrePage());
            pageInfo.setSize(orderBillingPaymentDetailsList.getSize());
            pageInfo.setStartRow(orderBillingPaymentDetailsList.getStartRow());
            pageInfo.setTotal(orderBillingPaymentDetailsList.getTotal());
            pageInfo.setNavigateFirstPage(orderBillingPaymentDetailsList.getNavigateFirstPage());
            pageInfo.setNavigatepageNums(orderBillingPaymentDetailsList.getNavigatepageNums());
            pageInfo.setNavigateLastPage(orderBillingPaymentDetailsList.getNavigateLastPage());
            pageInfo.setNavigatePages(orderBillingPaymentDetailsList.getNavigatePages());
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, new GridDataVO<RepaymentMoneyListResponse>().transform(pageInfo));
            logger.info("getRepaymentMondyList OUT,导购查询还款记录成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,导购查询还款记录失败!", null);
            logger.warn("getRepaymentMondyList EXCEPTION,导购查询还款记录失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 导购查询还款记录详情
     *
     * @param userID       用户id
     * @param identityType 用户类型
     * @param orderNumber  订单号
     * @return 返回还款记录详情
     */
    @PostMapping(value = "/repayment/detail", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getRepaymentMondyDetail(Long userID, Integer identityType, String orderNumber) {
        logger.info("getRepaymentMondyDetail CALLED,导购查询还款记录详情，入参 userId:{} identityType:{} orderNumber:{}", userID, identityType, orderNumber);
        ResultDTO<Object> resultDTO;

        if (null == userID) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("getRepaymentMondyDetail OUT,导购查询还款记录详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！",
                    null);
            logger.info("getRepaymentMondyDetail OUT,导购查询还款记录详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(orderNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单号不能为空！",
                    null);
            logger.info("getRepaymentMondyDetail OUT,导购查询还款记录详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (identityType != 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！",
                    null);
            logger.info("getRepaymentMondyDetail OUT,导购查询还款记录详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            //转换日期
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            //查询订单头
            OrderBaseInfo orderBaseInfo = appOrderServiceImpl.getOrderByOrderNumber(orderNumber);
            if (null == orderBaseInfo) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查到此订单！",
                        null);
                logger.info("getRepaymentMondyDetail OUT,导购查询还款记录详情失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            String outTradeNo = orderNumber.replaceAll("_XN", "_HK");
            //查询支付信息
            PaymentDataDO paymentDataDO = paymentDataService.findPaymentDataDOByOutTradeNo(outTradeNo);
            if (null == paymentDataDO) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查到此还款记录",
                        null);
                logger.info("getRepaymentMondyDetail OUT,导购查询还款记录详情失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            RepaymentDetailResponse repaymentDetailResponse = new RepaymentDetailResponse();
            //设值
            repaymentDetailResponse.setRepaymentMoney(paymentDataDO.getTotalFee());
            repaymentDetailResponse.setRepaymentNumber(paymentDataDO.getOutTradeNo());
            repaymentDetailResponse.setOrderNumber(paymentDataDO.getOrderNumber());
            repaymentDetailResponse.setRepaymentTime(df.format(paymentDataDO.getCreateTime()));
            repaymentDetailResponse.setRepaymentType(paymentDataDO.getOnlinePayType().getDescription());
            if (orderBaseInfo.getCreatorIdentityType().getValue() == 6) {
                repaymentDetailResponse.setCustomerName(orderBaseInfo.getCreatorName());
                repaymentDetailResponse.setCustomerPhone(orderBaseInfo.getCreatorPhone());
            } else if (orderBaseInfo.getCreatorIdentityType().getValue() == 0) {
                repaymentDetailResponse.setCustomerName(orderBaseInfo.getCustomerName());
                repaymentDetailResponse.setCustomerPhone(orderBaseInfo.getCustomerPhone());
            }

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, repaymentDetailResponse);
            logger.info("getRepaymentMondyDetail OUT,导购查询还款记录详情成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,导购查询还款记录详情失败!", null);
            logger.warn("getRepaymentMondyDetail EXCEPTION,导购查询还款记录详情失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}
