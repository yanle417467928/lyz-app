package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.core.constant.PaymentSubjectType;
import cn.com.leyizhuang.app.core.constant.RechargeAccountType;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dto.CreditBillingDTO;
import cn.com.leyizhuang.app.foundation.dto.DecorationCompanyCreditBillingDTO;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.WithdrawRefundInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorationCompanyCreditBillingDO;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorationCompanyCreditBillingDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeOrder;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeReceiptInfo;
import cn.com.leyizhuang.app.foundation.service.RechargeService;
import cn.com.leyizhuang.app.foundation.service.WithdrawService;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorationCompanyCreditBillingDetailsVO;
import cn.com.leyizhuang.app.foundation.service.MaDecorationCompanyCreditBillingService;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorationCompanyCreditBillingVO;
import cn.com.leyizhuang.app.remote.queue.MaSinkSender;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.CountUtil;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/3/15
 */
@RestController
@RequestMapping(value = MaDecorationCompanyCreditBillingRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaDecorationCompanyCreditBillingRestController extends BaseRestController{
    protected static final String PRE_URL = "/rest/decorationCompany/creditBilling";

    private final Logger logger = LoggerFactory.getLogger(MaDecorationCompanyCreditBillingRestController.class);

    @Autowired
    private MaDecorationCompanyCreditBillingService maDecorationCompanyCreditBillingService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private MaSinkSender sinkSender;

    /**
     * @title   获取装饰公司账单
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/3/19
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<DecorationCompanyCreditBillingDetailsVO> restCreditOrderPageGird(Integer offset, Integer size, String keywords, Long storeId, String startTime, String endTime) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<DecorationCompanyCreditBillingDetailsVO> pageInfo = this.maDecorationCompanyCreditBillingService
                .getDecorationCompanyCreditOrder(page, size, storeId,  startTime, endTime, keywords);
        return new GridDataVO<DecorationCompanyCreditBillingDetailsVO>().transform(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * @title   获取装饰公司所选订单信用金总额
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/3/16
     */
    @GetMapping(value = "/billInfo")
    public Double getBillInfo(Long storeId, @RequestParam(value = "orderNumbers[]", required = false) String[] orderNumbers) {
        logger.info("getBillInfo, 获取装饰公司所选订单信用金总额");
        try {
            List<String> orderNumberList = new ArrayList<>();
            if (null != orderNumbers && orderNumbers.length > 0) {
                for (int i = 0; i < orderNumbers.length; i++) {
                    orderNumberList.add(orderNumbers[i]);
                }
            }
            if (orderNumberList.size() == 0) {
                orderNumberList.add("加数据防止报错");
            }
            Double allCreditMoney = this.maDecorationCompanyCreditBillingService.getBillAllCreditMoney(storeId, orderNumberList);

            if (null == allCreditMoney) {
                allCreditMoney = 0D;
            }
            //查询登录用户门店权限的门店ID
            logger.info("getBillInfo ,获取装饰公司所选订单信用金总额", allCreditMoney);
            return allCreditMoney;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("getBillInfo, EXCEPTION,发生未知错误，获取装饰公司所选订单信用金总额失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * @title   创建装饰公司信用金账单
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/3/19
     */
    @PostMapping(value = "/create/creditBilling")
    public ResultDTO<String> createCreditBilling(@RequestParam(value = "orderNumbers[]", required = false) String[] orderNumbers, @Valid DecorationCompanyCreditBillingDTO creditBillingDTO, BindingResult result) {
        logger.info("创建装饰公司信用金账单 createCreditBilling 入参 orderNumbers{}，result", orderNumbers, result);
        if (!result.hasErrors()) {
            try {
                if (null != creditBillingDTO && creditBillingDTO.getBillAmount() != null && creditBillingDTO.getBillAmount() != 0D){
                    this.maDecorationCompanyCreditBillingService.createCreditBilling(orderNumbers, creditBillingDTO);
                } else {
                    logger.info("创建装饰公司信用金账单 createCreditBilling");
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "变更金额不能为零！", null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                List<ObjectError> allErrors = result.getAllErrors();
                logger.warn("页面提交的数据有错误 createCreditBilling：errors = {}", errorMsgToHtml(allErrors));
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, e.getMessage(), null);
            }
            logger.info("创建装饰公司信用金账单成功", null,null);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        }else {
            List<ObjectError> allErrors = result.getAllErrors();
            logger.warn("页面提交的数据有错误 createCreditBilling：errors = {}", errorMsgToHtml(allErrors));
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }

    @GetMapping(value = "/page")
    public GridDataVO<DecorationCompanyCreditBillingVO> restCreditBillingPageGird(Integer offset, Integer size, String keywords, Long storeId, String startTime, String endTime, Boolean isPayOff) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<DecorationCompanyCreditBillingVO> pageInfo = this.maDecorationCompanyCreditBillingService
                .getDecorationCompanyCreditBilling(page, size, storeId,  startTime, endTime, keywords, isPayOff);
        return new GridDataVO<DecorationCompanyCreditBillingVO>().transform(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * @title   装饰公司信用金账单还款
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/3/20
     */
    @PostMapping(value = "/repayment/creditBilling")
    public ResultDTO<String> repaymentCreditBilling(@Valid CreditBillingDTO creditBillingDTO, BindingResult result) {
        logger.info("装饰公司信用金账单还款 repaymentCreditBilling 入参 creditBillingDTO{}，result", creditBillingDTO, result);
        if (!result.hasErrors()) {
            try {
                if (null != creditBillingDTO && creditBillingDTO.getAmount() != 0D){
                    Boolean flag = this.maDecorationCompanyCreditBillingService.repaymentCreditBilling(creditBillingDTO.getId(), creditBillingDTO.getAmount(), creditBillingDTO.getPaymentType());
                    if (flag) {
                        DecorationCompanyCreditBillingDO creditBillingDO = this.maDecorationCompanyCreditBillingService.getCreditBillingById(creditBillingDTO.getId());

                        if (creditBillingDTO.getAmount() > 0) {

                            //生成充值单
                            RechargeOrder rechargeOrder = rechargeService.createCreditRechargeOrder(creditBillingDO, creditBillingDTO.getAmount(), creditBillingDTO.getPaymentType());
                            rechargeService.saveRechargeOrder(rechargeOrder);
                            //创建充值单收款
                            RechargeReceiptInfo receiptInfo = rechargeService.createCreditRechargeReceiptInfo(creditBillingDO, creditBillingDTO.getAmount(), creditBillingDTO.getPaymentType());
                            rechargeService.saveRechargeReceiptInfo(receiptInfo);

                            //将收款记录入拆单消息队列
                            sinkSender.sendCreditRechargeReceipt(receiptInfo.getReceiptNumber());
                        } else {
                            //生成提现退款信息
                            WithdrawRefundInfo withdrawRefundInfo = new WithdrawRefundInfo();
                            withdrawRefundInfo.setCreateTime(new Date());
                            withdrawRefundInfo.setWithdrawNo(creditBillingDO.getCreditBillingNo());
                            withdrawRefundInfo.setRefundNumber(OrderUtils.getRefundNumber());
                            withdrawRefundInfo.setWithdrawChannel(OrderBillingPaymentType.getOrderBillingPaymentTypeByValue(creditBillingDTO.getPaymentType()));
                            withdrawRefundInfo.setWithdrawChannelDesc(OrderBillingPaymentType.getOrderBillingPaymentTypeByValue(creditBillingDTO.getPaymentType()).getDescription());
                            withdrawRefundInfo.setWithdrawAccountType(RechargeAccountType.ST_CREDIT);
                            withdrawRefundInfo.setWithdrawAccountTypeDesc(withdrawRefundInfo.getWithdrawAccountType().getDescription());
                            withdrawRefundInfo.setWithdrawAmount(CountUtil.mul(creditBillingDTO.getAmount(), -1));
                            withdrawRefundInfo.setWithdrawSubjectType(PaymentSubjectType.DECORATE_MANAGER);
                            withdrawRefundInfo.setWithdrawSubjectTypeDesc(withdrawRefundInfo.getWithdrawSubjectType().getDescription());
                            withdrawService.saveWithdrawRefundInfo(withdrawRefundInfo);

                            //提现退款接口信息发送EBS
                            sinkSender.sendWithdrawRefund(withdrawRefundInfo.getRefundNumber());
                        }

                        logger.info("装饰公司信用金账单还款成功，  repaymentCreditBilling", null,null);
                        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                    }
                } else {
                    logger.info("装饰公司信用金账单还款失败 createCreditBilling");
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "变更金额不能为零！", null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                List<ObjectError> allErrors = result.getAllErrors();
                logger.warn("页面提交的数据有错误 repaymentCreditBilling：errors = {}", errorMsgToHtml(allErrors));
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, e.getMessage(), null);
            }
            logger.warn("装饰公司信用金账单还款失败，  repaymentCreditBilling", null,null);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, null, null);
        }else {
            List<ObjectError> allErrors = result.getAllErrors();
            logger.warn("页面提交的数据有错误 repaymentCreditBilling：errors = {}", errorMsgToHtml(allErrors));
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }

}
