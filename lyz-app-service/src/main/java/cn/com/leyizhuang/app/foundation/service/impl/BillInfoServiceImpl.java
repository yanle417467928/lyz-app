package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.exception.LockStorePreDepositException;
import cn.com.leyizhuang.app.core.exception.SystemBusyException;
import cn.com.leyizhuang.app.core.utils.DateUtil;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dao.BillInfoDAO;
import cn.com.leyizhuang.app.foundation.dao.BillRepaymentDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.StoreCreditMoney;
import cn.com.leyizhuang.app.foundation.pojo.StoreCreditMoneyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentGoodsDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRuleDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.request.BillorderDetailsRequest;
import cn.com.leyizhuang.app.foundation.pojo.response.BillHistoryListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.BillInfoResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.BillRepaymentGoodsInfoResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.BillRepaymentResponse;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.common.util.CountUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/6/26
 */
@Service("billInfoService")
public class BillInfoServiceImpl implements BillInfoService {

    private static final Logger logger = LoggerFactory.getLogger(BillInfoServiceImpl.class);

    @Autowired
    private BillRuleService billRuleService;

    @Autowired
    private BillInfoDAO billInfoDAO;

    @Autowired
    private BillRepaymentDAO billRepaymentDAO;

    @Autowired
    private PaymentDataService paymentDataService;

    @Autowired
    private AppOrderService orderService;

    @Autowired
    private AppStoreService appStoreService;

    @Autowired
    private MaDecorativeCompanyCreditService maDecorativeCompanyCreditService;


    @Override
    public List<BillRepaymentGoodsDetailsDO> computeInterestAmount(Long storeId, List<BillRepaymentGoodsDetailsDO> goodsDetailsDOList) {

        if (null == goodsDetailsDOList || goodsDetailsDOList.size() == 0) {
            return goodsDetailsDOList;
        }

        if (null == storeId) {
            return goodsDetailsDOList;
        }
        //根据门店ID查询账单规则
        BillRuleDO billRuleDO = this.billRuleService.getBillRuleByStoreId(storeId);
        if (null == billRuleDO || null == billRuleDO.getInterestRate() || billRuleDO.getInterestRate().equals(0D)) {
            return goodsDetailsDOList;
        }
        //利息
        Double interestRate = billRuleDO.getInterestRate();
        //还款截至日
        Integer repaymentDeadlineDate = billRuleDO.getRepaymentDeadlineDate();
        if (null == repaymentDeadlineDate || repaymentDeadlineDate == 0) {
            repaymentDeadlineDate = 1;
        }
        //还款截至日
        Integer billDate = billRuleDO.getBillDate();
        if (null == billDate || billDate == 0) {
            billDate = 1;
        }


        //计算利息（欠款金额 * 利息 * 逾期天数 * 利息单位）
        for (BillRepaymentGoodsDetailsDO goodsDetailsDO : goodsDetailsDOList) {
            //逾期天数
            Integer overdueDays = DateUtil.getDifferDays(DateUtil.getDifferenceFatalism(billDate, repaymentDeadlineDate, goodsDetailsDO.getShipmentTime()), new Date());
            if (overdueDays < 0) {
                overdueDays = 0;
            }
            goodsDetailsDO.setInterestAmount(CountUtil.mul(goodsDetailsDO.getOrderCreditMoney(), interestRate, overdueDays, AppConstant.INTEREST_RATE_UNIT));
        }

        return goodsDetailsDOList;
    }

    @Override
    public List<BillRepaymentGoodsInfoResponse> computeInterestAmount2(Long storeId, List<BillRepaymentGoodsInfoResponse> goodsDetailsList) {

        if (null == goodsDetailsList || goodsDetailsList.size() == 0) {
            return goodsDetailsList;
        }

        if (null == storeId) {
            return goodsDetailsList;
        }
        //根据门店ID查询账单规则
        BillRuleDO billRuleDO = this.billRuleService.getBillRuleByStoreId(storeId);
        if (null == billRuleDO || null == billRuleDO.getInterestRate() || billRuleDO.getInterestRate().equals(0D)) {
            return goodsDetailsList;
        }
        //利息
        Double interestRate = billRuleDO.getInterestRate();
        //还款截至日
        Integer repaymentDeadlineDate = billRuleDO.getRepaymentDeadlineDate();
        if (null == repaymentDeadlineDate || repaymentDeadlineDate == 0) {
            repaymentDeadlineDate = 1;
        }
        //还款截至日
        Integer billDate = billRuleDO.getBillDate();
        if (null == billDate || billDate == 0) {
            billDate = 1;
        }


        //计算利息（欠款金额 * 利息 * 逾期天数 * 利息单位）
        for (BillRepaymentGoodsInfoResponse goodsDetails : goodsDetailsList) {
            if ("order".equals(goodsDetails.getOrderType())) {
                //逾期天数
                Integer overdueDays = DateUtil.getDifferDays(DateUtil.getDifferenceFatalism(billDate, repaymentDeadlineDate, goodsDetails.getShipmentTime()), new Date());
                if (overdueDays < 0) {
                    overdueDays = 0;
                }
                goodsDetails.setInterestAmount(CountUtil.mul(goodsDetails.getOrderCreditMoney(), interestRate, overdueDays, AppConstant.INTEREST_RATE_UNIT));
            }
        }

        return goodsDetailsList;
    }

    @Override
    public BillRepaymentInfoDO findBillRepaymentInfoByRepaymentNo(String repaymentNo) {
        return this.billInfoDAO.findBillRepaymentInfoByRepaymentNo(repaymentNo);
    }

    @Override
    @Transactional
    public void handleBillRepaymentAfterOnlinePayUp(String repaymentNo, OnlinePayType onlinePayType, Integer identityType) {

        //更改账单收款信息
        BillRepaymentInfoDO billRepaymentInfoDO = this.billInfoDAO.findBillRepaymentInfoByRepaymentNo(repaymentNo);
        billRepaymentInfoDO.setOnlinePayType(onlinePayType);
        billRepaymentInfoDO.setIsPaid(Boolean.TRUE);
        billRepaymentInfoDO.setRepaymentTime(new Date());
        this.billInfoDAO.updateBillRepaymentInfo(billRepaymentInfoDO);

        BillInfoDO billInfoDO = this.billInfoDAO.findBillInfoByBillNo(billRepaymentInfoDO.getBillNo());

        //根据门店ID查询账单规则
        BillRuleDO billRuleDO = this.billRuleService.getBillRuleByStoreId(billInfoDO.getStoreId());
        if (null == billRuleDO) {
            billRuleDO = new BillRuleDO();
        }
        //还款截至日
        Integer repaymentDeadlineDate = billRuleDO.getRepaymentDeadlineDate();
        if (null == repaymentDeadlineDate || repaymentDeadlineDate == 0) {
            repaymentDeadlineDate = 1;
        }
        //账期日
        Integer billDate = billRuleDO.getBillDate();
        if (null == billDate || billDate == 0) {
            billDate = 1;
        }

        //订单账单更新是否已付清、付清时间
        List<BillRepaymentGoodsDetailsDO> goodsDetailsDOS = this.billInfoDAO.findRepaymentGoodsDetailsByRepaymentNo(repaymentNo);
        Double priorPaidBillAmount = 0D;
        if (null != goodsDetailsDOS && goodsDetailsDOS.size() > 0) {
            for (BillRepaymentGoodsDetailsDO goodsDetails : goodsDetailsDOS) {
                if ("order".equals(goodsDetails.getOrderType())) {
                    OrderBillingDetails orderBillingDetails = new OrderBillingDetails();
                    orderBillingDetails.setOrderNumber(goodsDetails.getOrderNo());
                    orderBillingDetails.setIsPayUp(Boolean.TRUE);
                    orderBillingDetails.setPayUpTime(new Date());
                    orderService.updateOrderBillingDetails(orderBillingDetails);
                }
                //逾期天数
                Integer overdueDays = DateUtil.getDifferDays(DateUtil.getDifferenceFatalism(billDate, repaymentDeadlineDate, goodsDetails.getShipmentTime()), new Date());

                if (overdueDays > 0) {
                    priorPaidBillAmount = CountUtil.add(priorPaidBillAmount, goodsDetails.getOrderCreditMoney());
                }
            }
        }

        //更新账单信息
        billInfoDO.setCurrentPaidAmount(CountUtil.add(billInfoDO.getCurrentPaidAmount(), billRepaymentInfoDO.getOnlinePayAmount()));
        billInfoDO.setPriorPaidInterestAmount(CountUtil.add(billInfoDO.getPriorPaidInterestAmount(), billRepaymentInfoDO.getTotalInterestAmount()));
        billInfoDO.setPriorPaidBillAmount(CountUtil.add(billInfoDO.getPriorPaidBillAmount(), priorPaidBillAmount));
        if (billInfoDO.getStatus() == BillStatusEnum.ALREADY_OUT) {
            billInfoDO.setCurrentUnpaidAmount(CountUtil.sub(billInfoDO.getCurrentUnpaidAmount(), billRepaymentInfoDO.getOnlinePayAmount()));
            if (billInfoDO.getCurrentUnpaidAmount() < AppConstant.PAY_UP_LIMIT) {
                billInfoDO.setStatus(BillStatusEnum.HISTORY);
            }
        }


        this.billInfoDAO.updateBillInfo(billInfoDO);


        //加信用金
        Double amount = CountUtil.sub(billRepaymentInfoDO.getTotalRepaymentAmount(), billRepaymentInfoDO.getTotalInterestAmount());
        for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
            StoreCreditMoney storeCreditMoney = this.appStoreService.findStoreCreditMoneyByStoreId(billInfoDO.getStoreId());
            if (null != storeCreditMoney) {
                int affectLine = appStoreService.updateStoreCreditByStoreIdAndVersion(
                        billInfoDO.getStoreId(), amount, storeCreditMoney.getLastUpdateTime());
                if (affectLine > 0) {
                    StoreCreditMoneyChangeLog log = new StoreCreditMoneyChangeLog();
                    log.setStoreId(storeCreditMoney.getStoreId());
                    log.setChangeAmount(amount);
                    log.setCreditLimitAvailableAfterChange(storeCreditMoney.getCreditLimitAvailable() + amount);
                    log.setCreateTime(Calendar.getInstance().getTime());
                    log.setChangeType(StoreCreditMoneyChangeType.REPAYMENT);
                    log.setChangeTypeDesc(StoreCreditMoneyChangeType.REPAYMENT.getDescription());
                    log.setOperatorId(billRepaymentInfoDO.getRepaymentUserId());
                    log.setOperatorType(AppIdentityType.getAppIdentityTypeByValue(identityType));
                    log.setOperatorIp("");
                    log.setReferenceNumber(billRepaymentInfoDO.getRepaymentNo());
                    appStoreService.addStoreCreditMoneyChangeLog(log);
                    break;
                } else {
                    if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                        throw new SystemBusyException("系统繁忙，请稍后再试!");
                    }
                }
            } else {
                throw new LockStorePreDepositException("没有找到该门店的信用金信息!");
            }
        }
    }

    @Override
    @Transactional
    public void updateBillRepaymentInfo(BillRepaymentInfoDO billRepaymentInfoDO) {
        this.billInfoDAO.updateBillRepaymentInfo(billRepaymentInfoDO);
    }

    @Override
    public List<BillRepaymentGoodsDetailsDO> findRepaymentGoodsDetailsByRepaymentNo(String repaymentNo) {
        return this.billInfoDAO.findRepaymentGoodsDetailsByRepaymentNo(repaymentNo);
    }

    @Override
    public BillInfoDO findBillInfoByBillNo(String billNo) {
        return this.billInfoDAO.findBillInfoByBillNo(billNo);
    }

    @Override
    public void updateBillInfo(BillInfoDO billInfoDO) {
        this.billInfoDAO.updateBillInfo(billInfoDO);
    }

    /**
     * 查看账单
     *
     * @param starTime 开始时间
     * @param endTime  结束时间
     * @param storeid  门店id
     * @param page     预留
     * @param size     预留
     * @return
     * @throws Exception
     */
    public BillInfoResponse lookBill(LocalDateTime starTime, LocalDateTime endTime, Long storeid, Integer page, Integer size) throws Exception {
        BillInfoResponse response = new BillInfoResponse(); // 返回结果
        // 初始化
        response.setBillTotalAmount(0D);
        response.setCurrentBillAmount(0D);
        response.setCurrentUnpaidAmount(0D);
        response.setCurrentPaidAmount(0D);
        response.setCurrentAdjustmentAmount(0D);
        response.setPriorNotPaidInterestAmount(0D);
        response.setPriorNotPaidBillAmount(0D);
        List<BillRepaymentGoodsInfoResponse> list = new ArrayList<>();
        response.setNotPayOrderDetails(list);
        response.setPaidOrderDetails(list);

        LocalDateTime now = LocalDateTime.now(); //当前时间
        // 查询已出账单
        BillInfoDO billInfoDO = billInfoDAO.findBillByStatus(BillStatusEnum.ALREADY_OUT, storeid);

        if (billInfoDO == null) {
            // 已出账单不存在 处理未出账单
            billInfoDO = billInfoDAO.findBillByStatus(BillStatusEnum.NOT_OUT, storeid);

            if (billInfoDO == null) {
                billInfoDO = this.createBillInfo(storeid);
            }
        }

        response = BillInfoDO.transfer(billInfoDO);

        if (billInfoDO.getBillStartDate() == null) {
            return response;
        }

        if (billInfoDO.getBillEndDate() == null) {
            return response;
        }

        LocalDateTime billStartTime = null; // 账单开始时间
        LocalDateTime billEndTime = null;   // 账单结束时间
        Instant instant = billInfoDO.getBillStartDate().toInstant();
        ZoneId zone = ZoneId.systemDefault();
        billStartTime = instant.atZone(zone).toLocalDateTime();
        Instant instant2 = billInfoDO.getBillEndDate().toInstant();
        billEndTime = instant2.atZone(zone).toLocalDateTime();

        // 设置客户选择时间范围
        if (starTime != null && starTime.isAfter(billStartTime) && starTime.isBefore(billEndTime)) {
            billStartTime = starTime;
        }
        if (endTime != null && endTime.isAfter(billStartTime) && endTime.isBefore(billEndTime)) {
            billEndTime = endTime;
        }

        // 获取本期未还订单，本期已还订单，上期未还订单
        List<BillRepaymentGoodsInfoResponse> currentPaidOrderDetails = new ArrayList<>();
        List<BillRepaymentGoodsInfoResponse> currentNotPayOrderDetails = new ArrayList<>();
        List<BillRepaymentGoodsInfoResponse> beforNotPayOrderDetails = new ArrayList<>();
        Double billTotalAmount = 0D; // 账单总金额：本期账单金额+上期未还账单金额+上期滞纳金+本期调整金额
        Double currentBillAmount = 0D; // 本期账单金额
        Double currentAdjustmentAmount = 0D; // 本期调整金额
        Double currentPaid = 0D; // 本期已还
        Double currentNotPay = 0D; // 本期未还
        Double beforNotPay = 0D;   // 上期未还
        Double fees = 0D; // 滞纳金

        // 本期已还订单
        currentPaidOrderDetails = billInfoDAO.getCurrentOrderDetails(billStartTime, billEndTime, true, storeid);
        // 本期未还订单
        currentNotPayOrderDetails = billInfoDAO.getCurrentOrderDetails(billStartTime, billEndTime, false, storeid);
        // 上期未还订单
        beforNotPayOrderDetails = billInfoDAO.getCurrentOrderDetails(null, billStartTime, false, storeid);


        currentPaid = this.AddAllCreditMoney(currentPaidOrderDetails);
        currentNotPay = this.AddAllCreditMoney(currentNotPayOrderDetails);
        beforNotPay = this.AddAllCreditMoney(beforNotPayOrderDetails);

        // 将订单结果注入response
        response.setPaidOrderDetails(currentPaidOrderDetails);
        // 合并未还清结果集
        beforNotPayOrderDetails.addAll(currentNotPayOrderDetails);
        // TODO 排序
        response.setNotPayOrderDetails(beforNotPayOrderDetails);

        currentPaidOrderDetails.addAll(currentNotPayOrderDetails); // 合并本期已还和未还订单
        currentBillAmount = this.AddAllPositiveCreditMoney(currentPaidOrderDetails);
        currentAdjustmentAmount = this.AddAllNegativeCreditMoney(currentPaidOrderDetails);

        // 计算滞纳金
        beforNotPayOrderDetails = this.computeInterestAmount2(storeid, beforNotPayOrderDetails);
        fees = this.AddAllInterestAmount(beforNotPayOrderDetails);

        // 账单总金额
        billTotalAmount = CountUtil.add(currentBillAmount, currentAdjustmentAmount, beforNotPay, fees);

        response.setBillTotalAmount(billTotalAmount);
        response.setCurrentBillAmount(currentBillAmount);
        response.setCurrentAdjustmentAmount(currentAdjustmentAmount);
        response.setCurrentPaidAmount(currentPaid);
        response.setCurrentUnpaidAmount(currentNotPay);
        response.setPriorNotPaidBillAmount(beforNotPay);
        response.setPriorNotPaidInterestAmount(fees);

        return response;
    }

    @Override
    public PageInfo<BillHistoryListResponse> findBillHistoryListByEmpId(Long empId, Integer identityType, Integer page, Integer size) {
        if (empId != null) {
            PageHelper.startPage(page, size);
            List<BillHistoryListResponse> billHistoryList = this.billInfoDAO.findBillHistoryListByEmpId(empId);
            return new PageInfo<>(billHistoryList);
        }
        return null;
    }

    @Override
    public BillInfoResponse findBillHistoryDetail(String billNo) {
        BillInfoDO billInfoDO = this.billInfoDAO.findBillInfoByBillNo(billNo);
        BillInfoResponse billInfoResponse = BillInfoDO.transfer(billInfoDO);
        if (null != billInfoResponse) {
            List<BillRepaymentInfoDO> billRepaymentInfoDOList = this.billInfoDAO.findBillRepaymentInfoByBillNo(billNo);
            for (BillRepaymentInfoDO repaymentInfoDO : billRepaymentInfoDOList) {
                BillRepaymentResponse billRepaymentResponse = new BillRepaymentResponse();
                billRepaymentResponse.setRepaymentTime(repaymentInfoDO.getRepaymentTime());
                billRepaymentResponse.setTotalRepaymentAmount(repaymentInfoDO.getTotalRepaymentAmount());
                List<BillRepaymentGoodsDetailsDO> goodsDetailsDOList = this.billInfoDAO.findRepaymentGoodsDetailsByRepaymentNo(repaymentInfoDO.getRepaymentNo());
                if (null != goodsDetailsDOList && goodsDetailsDOList.size() > 0) {
                    List<BillRepaymentGoodsInfoResponse> billRepaymentDetails = new ArrayList<>();
                    goodsDetailsDOList.forEach(goodsDetailsDO -> {
                        billRepaymentDetails.add(BillRepaymentGoodsInfoResponse.transform(goodsDetailsDO));
                    });
                    billRepaymentResponse.setBillRepaymentDetails(billRepaymentDetails);
                }
            }
            return billInfoResponse;
        }
        return null;
    }

    @Override
    public List<BillRepaymentInfoDO> findBillRepaymentInfoByBillNo(String billNo) {
        return this.billInfoDAO.findBillRepaymentInfoByBillNo(billNo);
    }

    @Override
    public List<BillRepaymentGoodsDetailsDO> findRepaymentGoodsDetailsByBillNo(String billNo) {
        return this.billInfoDAO.findRepaymentGoodsDetailsByBillNo(billNo);
    }

    @Override
    @Transactional
    public BillInfoDO createBillInfo(Long storeId) {
        AppStore store = this.appStoreService.findById(storeId);
        if (null == store) {
            return null;
        }
        //根据门店ID查询账单规则
        BillRuleDO billRuleDO = this.billRuleService.getBillRuleByStoreId(storeId);
        if (null == billRuleDO){
            return null;
        }
        //还款截至日
        Integer repaymentDeadlineDate = billRuleDO.getRepaymentDeadlineDate();
        if (null == repaymentDeadlineDate || repaymentDeadlineDate == 0) {
            repaymentDeadlineDate = 1;
        }
        //账期日
        Integer billDate = billRuleDO.getBillDate();
        if (null == billDate || billDate == 0) {
            billDate = 1;
        }
        Integer nowDay = DateUtil.getDate();
        Date billStartDate = new Date();
        if (nowDay < billDate) {
            billStartDate = DateUtil.getBillDate(billStartDate, -1, billDate);
        } else {
            billStartDate = DateUtil.getBillDate(billStartDate, 0, billDate);
        }

        BillInfoDO billInfo = new BillInfoDO();
        billInfo.setBillNo(OrderUtils.generateBillNo(store.getStoreCode()));
        billInfo.setBillName(store.getStoreName() + DateUtil.getMonthByDate(billStartDate) + "月账单");
        billInfo.setStoreId(storeId);
        billInfo.setBillStartDate(billStartDate);
        billInfo.setBillEndDate(DateUtil.getNextMonthByDateBeforOne(billStartDate));
        billInfo.setRepaymentDeadlineDate(DateUtil.getDifferenceFatalism(billDate, repaymentDeadlineDate, new Date()));
        billInfo.setBillTotalAmount(0D);
        billInfo.setCurrentBillAmount(0D);
        billInfo.setCurrentAdjustmentAmount(0D);
        billInfo.setCurrentPaidAmount(0D);
        billInfo.setCurrentUnpaidAmount(0D);
        billInfo.setPriorPaidBillAmount(0D);
        billInfo.setPriorPaidInterestAmount(0D);
        billInfo.setStatus(BillStatusEnum.NOT_OUT);
        billInfo.setCreateTime(new Date());
        billInfo.setCreateUserId(0L);
        billInfo.setCreateUserName("系统");
        this.billInfoDAO.saveBillInfo(billInfo);
        return billInfo;
    }

    @Override
    public void saveBillInfo(BillInfoDO billInfo) {
        this.billInfoDAO.saveBillInfo(billInfo);
    }

    @Override
    public void updateBillStatus(Long storeId, BillStatusEnum beforeStatus, BillStatusEnum afterStatus) {
        this.billInfoDAO.updateBillStatus(storeId, beforeStatus, afterStatus);
    }

    @Override
    @Transactional
    public void handleBillInfoInBillDate(Long storeId) {
        String nowStr = DateUtil.getDateStr(DateUtil.getbeforMonthByDate(new Date()));
        BillInfoDO billInfo = this.billInfoDAO.findBillInfoByBillStartDateAndStoreIdAndStatus(storeId, nowStr, BillStatusEnum.NOT_OUT);
        if (null != billInfo) {
            LocalDateTime billStartTime = null; // 账单开始时间
            LocalDateTime billEndTime = null;   // 账单结束时间
            Instant instant = billInfo.getBillStartDate().toInstant();
            ZoneId zone = ZoneId.systemDefault();
            billStartTime = instant.atZone(zone).toLocalDateTime();
            Instant instant2 = billInfo.getBillEndDate().toInstant();
            billEndTime = instant2.atZone(zone).toLocalDateTime();

            // 获取本期未还订单，本期已还订单，上期未还订单
            List<BillRepaymentGoodsInfoResponse> currentPaidOrderDetails = new ArrayList<>();
            List<BillRepaymentGoodsInfoResponse> currentNotPayOrderDetails = new ArrayList<>();
            List<BillRepaymentGoodsInfoResponse> beforNotPayOrderDetails = new ArrayList<>();
            Double billTotalAmount = 0D; // 账单总金额：本期账单金额+上期未还账单金额+上期滞纳金+本期调整金额
            Double currentBillAmount = 0D; // 本期账单金额
            Double currentAdjustmentAmount = 0D; // 本期调整金额
            Double currentPaid = 0D; // 本期已还
            Double currentNotPay = 0D; // 本期未还
            Double beforNotPay = 0D;   // 上期未还
            Double fees = 0D; // 滞纳金

            // 本期已还订单
            currentPaidOrderDetails = billInfoDAO.getCurrentOrderDetails(billStartTime,billEndTime,true, storeId);
            // 本期未还订单
            currentNotPayOrderDetails = billInfoDAO.getCurrentOrderDetails(billStartTime,billEndTime,false, storeId);
            // 上期未还订单
            beforNotPayOrderDetails = billInfoDAO.getCurrentOrderDetails(null,billStartTime,false, storeId);


            currentNotPay = this.AddAllCreditMoney(currentNotPayOrderDetails);
            beforNotPay = this.AddAllCreditMoney(beforNotPayOrderDetails);

            currentPaidOrderDetails.addAll(currentNotPayOrderDetails); // 合并本期已还和未还订单
            currentBillAmount = this.AddAllPositiveCreditMoney(currentPaidOrderDetails);
            currentAdjustmentAmount = this.AddAllNegativeCreditMoney(currentPaidOrderDetails);

            // 计算滞纳金
            beforNotPayOrderDetails = this.computeInterestAmount2(storeId,beforNotPayOrderDetails);
            fees = this.AddAllInterestAmount(beforNotPayOrderDetails);

            // 账单总金额
            billTotalAmount = CountUtil.add(currentBillAmount,currentAdjustmentAmount,beforNotPay,fees);
            billInfo.setBillTotalAmount(billTotalAmount);
            billInfo.setCurrentBillAmount(currentBillAmount);
            billInfo.setCurrentAdjustmentAmount(currentAdjustmentAmount);
            billInfo.setCurrentUnpaidAmount(currentNotPay);
            billInfo.setStatus(BillStatusEnum.ALREADY_OUT);
            this.billInfoDAO.updateBillInfo(billInfo);
        }

    }

    @Override
    public BillInfoDO findBillInfoByBillStartDateAndStoreIdAndStatus(Long storeId, String billStartDate, BillStatusEnum status) {
        return this.billInfoDAO.findBillInfoByBillStartDateAndStoreIdAndStatus(storeId, billStartDate, status);
    }

    /**
     * 求和所有信用金
     *
     * @param list
     * @return
     */
    public Double AddAllCreditMoney(List<BillRepaymentGoodsInfoResponse> list) {
        Double totalCreditMoney = 0D;
        if (list == null) {
            return totalCreditMoney;
        }

        for (BillRepaymentGoodsInfoResponse response : list) {
            totalCreditMoney = CountUtil.add(totalCreditMoney, response.getOrderCreditMoney());
        }

        return totalCreditMoney;
    }

    /**
     * 求和所以正向信用金
     *
     * @param list
     * @return
     */
    public Double AddAllPositiveCreditMoney(List<BillRepaymentGoodsInfoResponse> list) {
        Double totalCreditMoney = 0D;
        if (list == null) {
            return totalCreditMoney;
        }

        for (BillRepaymentGoodsInfoResponse response : list) {
            if (response.getOrderCreditMoney() > 0) {
                totalCreditMoney = CountUtil.add(totalCreditMoney, response.getOrderCreditMoney());
            }
        }

        return totalCreditMoney;
    }

    /**
     * 求和所有负向信用金
     *
     * @param list
     * @return
     */
    public Double AddAllNegativeCreditMoney(List<BillRepaymentGoodsInfoResponse> list) {
        Double totalCreditMoney = 0D;
        if (list == null) {
            return totalCreditMoney;
        }

        for (BillRepaymentGoodsInfoResponse response : list) {
            if (response.getOrderCreditMoney() < 0) {
                totalCreditMoney = CountUtil.add(totalCreditMoney, response.getOrderCreditMoney());
            }
        }

        return totalCreditMoney;
    }

    /**
     * 求和所有滞纳金
     *
     * @param list
     * @return
     */
    public Double AddAllInterestAmount(List<BillRepaymentGoodsInfoResponse> list) {
        Double interestAmount = 0D;
        if (list == null) {
            return interestAmount;
        }

        for (BillRepaymentGoodsInfoResponse response : list) {
            interestAmount = CountUtil.add(interestAmount, response.getInterestAmount());
        }

        return interestAmount;
    }

    /**
     * 计算应该支付金额
     * @param orderDetails
     * @return
     */
    public Double calculatePayAmount(Long storeId,List<BillorderDetailsRequest> orderDetails){
        Double totalAmount = 0D;
        Double totalOrderAmount = 0D;
        Double totalReturnAmount = 0D;
        Double fees = 0D;
        List<Long> orderIds = new ArrayList<>();
        List<Long> returnIds = new ArrayList<>();

        if (orderDetails == null || orderDetails.size() == 0){
            return  totalAmount;
        }

        // 分离订单和退单
        for (BillorderDetailsRequest request : orderDetails){
            if (request.getOrderType().equals("order")){
                orderIds.add(request.getId());
            }else if (request.getOrderType().equals("return")){
                returnIds.add(request.getId());
            }
        }

        // 获取订单
        List<BillRepaymentGoodsInfoResponse> billOrderList = billInfoDAO.getCurrentOrderDetailsByOrderNo(orderIds,storeId);
        // 获取退单
        List<BillRepaymentGoodsInfoResponse> billReturnOrderList = billInfoDAO.getCurrentOrderDetailsByReturnNo(returnIds,storeId);

        // 计算滞纳金
        billOrderList = this.computeInterestAmount2(storeId,billOrderList);
        totalOrderAmount = this.AddAllCreditMoney(billOrderList);
        totalReturnAmount = this.AddAllCreditMoney(billReturnOrderList);
        fees = this.AddAllInterestAmount(billOrderList);

        totalAmount = CountUtil.add(totalOrderAmount,totalReturnAmount,fees);

        return totalAmount;
    }

    /**
     * 账单还款支付
     * @param storeId 门店id
     * @param userId 还款人id
     * @param repaymentSystem 还款系统 app ; manage
     * @param billorderDetailsRequests 前台返回用户选择的订单集合
     * @param stPreDeposit 门店预存款
     * @param cash 现金
     * @param pos pos
     * @param totalRepaymentAmount 订单总信用金
     * @param posNumber pos流水号
     * @param other 其他收款
     * @param billNo 账单号
     * @throws Exception
     */
    @Transactional
    public void createRepayMentInfo(Long storeId,Long userId,String repaymentSystem,List<BillorderDetailsRequest> billorderDetailsRequests,
                                    Double stPreDeposit,Double cash,Double pos,Double totalRepaymentAmount,
                                    String posNumber,Double other,
                                    String billNo) throws Exception {
        if (billorderDetailsRequests != null || billorderDetailsRequests.size() > 0){
            //查询账单
            BillInfoDO billInfoDO = billInfoDAO.findBillInfoByBillNo(billNo);

            if (billInfoDO == null){
                throw new Exception("账单不存在");
            }

            // 账单规则
            BillRuleDO ruleDO = billRuleService.getBillRuleByStoreId(storeId);

            if (billInfoDO == null){
                throw new Exception("账单规则不存在");
            }

            //创建还款头信息
            BillRepaymentInfoDO repaymentInfoDO = new BillRepaymentInfoDO();
            repaymentInfoDO.setBillId(billInfoDO.getId());
            repaymentInfoDO.setBillNo(billNo);
            repaymentInfoDO.setRepaymentNo(OrderUtils.generateRepaymentNo());
            repaymentInfoDO.setRepaymentUserId(userId);
            repaymentInfoDO.setRepaymentSystem(repaymentSystem);
            repaymentInfoDO.setRepaymentTime(new Date());
            repaymentInfoDO.setCreateTime(new Date());
            repaymentInfoDO.setPreDeposit(stPreDeposit);
            repaymentInfoDO.setCashMoney(cash);
            repaymentInfoDO.setPosMoney(pos);
            repaymentInfoDO.setPosNumber(posNumber);
            repaymentInfoDO.setOtherMoney(other);
            repaymentInfoDO.setTotalRepaymentAmount(totalRepaymentAmount);

            if (totalRepaymentAmount.equals(CountUtil.add(stPreDeposit,cash,pos,other))){
                repaymentInfoDO.setIsPaid(true);
            }else{
                repaymentInfoDO.setIsPaid(false);
            }

            repaymentInfoDO.setInterestRate(ruleDO.getInterestRate());

            List<Long> orderIds = new ArrayList<>();
            for (BillorderDetailsRequest request : billorderDetailsRequests){
                orderIds.add(request.getId());
            }

            // 本次还款订单
            List<BillRepaymentGoodsInfoResponse> billOrderList = billInfoDAO.getCurrentOrderDetailsByOrderNo(orderIds,storeId);
            // 本次还款中上期订单
            List<BillRepaymentGoodsInfoResponse> pirorOrderList = new ArrayList<>();
            // 账单开始时间
            Date billStartTime = billInfoDO.getBillStartDate();
            for (BillRepaymentGoodsInfoResponse goodsInfoResponse : billOrderList){
                if (goodsInfoResponse.getShipmentTime().before(billStartTime)){
                    pirorOrderList.add(goodsInfoResponse);
                }
            }
            // 上期金额
            Double priorOrderTotalAmount = this.AddAllCreditMoney(pirorOrderList);

            // 计算滞纳金
            billOrderList = this.computeInterestAmount2(storeId,billOrderList);
            // 总滞纳金
            Double totalIntersAmount = this.AddAllInterestAmount(billOrderList);
            repaymentInfoDO.setTotalInterestAmount(totalIntersAmount);

            // 保存还款头信息
            billRepaymentDAO.saveBillRepayment(repaymentInfoDO);

            List<BillRepaymentGoodsDetailsDO> billRepaymentGoodsDetailsDOList = new ArrayList<>();
            billRepaymentGoodsDetailsDOList = BillRepaymentGoodsInfoResponse.transfer(billOrderList);

            for (BillRepaymentGoodsDetailsDO detailsDO: billRepaymentGoodsDetailsDOList){
                detailsDO.setRepaymentNo(repaymentInfoDO.getRepaymentNo());
                detailsDO.setRepaymentId(repaymentInfoDO.getId());

                billRepaymentDAO.saveBillRepaymentGoodsDetails(detailsDO);
            }

            if (totalRepaymentAmount.equals(CountUtil.add(stPreDeposit,cash,pos,other))){
                // 更新账单数据
                Double curentPaidAmount = billInfoDO.getCurrentPaidAmount() == null ? 0D : billInfoDO.getCurrentPaidAmount(); // 本期已还金额
                Double priorpaidAmount = billInfoDO.getPriorPaidBillAmount() == null ? 0D : billInfoDO.getPriorPaidBillAmount(); // 已还上期金额
                Double priorpaidInterestAmount = billInfoDO.getPriorPaidBillAmount() == null ? 0D : billInfoDO.getPriorPaidBillAmount(); // 已还滞纳金

                curentPaidAmount = CountUtil.add(curentPaidAmount,totalRepaymentAmount);
                priorpaidAmount = CountUtil.add(priorOrderTotalAmount,priorpaidAmount);
                priorpaidInterestAmount = CountUtil.add(priorpaidInterestAmount,totalIntersAmount);

                billInfoDO.setCurrentPaidAmount(curentPaidAmount);
                billInfoDO.setPriorPaidBillAmount(priorpaidAmount);
                billInfoDO.setPriorPaidInterestAmount(priorpaidInterestAmount);

                billInfoDAO.updateBillInfo(billInfoDO);

                // 刷新原订单付清状态
                for (BillRepaymentGoodsDetailsDO goodsDetails : billRepaymentGoodsDetailsDOList) {
                    if ("order".equals(goodsDetails.getOrderType())) {
                        OrderBillingDetails orderBillingDetails = new OrderBillingDetails();
                        orderBillingDetails.setOrderNumber(goodsDetails.getOrderNo());
                        orderBillingDetails.setIsPayUp(Boolean.TRUE);
                        orderBillingDetails.setPayUpTime(new Date());
                        orderService.updateOrderBillingDetails(orderBillingDetails);
                    }
                }

            }

        }
    }
}
