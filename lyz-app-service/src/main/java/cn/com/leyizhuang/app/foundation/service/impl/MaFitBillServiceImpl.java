package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppConstant;
import cn.com.leyizhuang.app.core.constant.BillStatusEnum;
import cn.com.leyizhuang.app.core.utils.DateUtil;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dao.BillInfoDAO;
import cn.com.leyizhuang.app.foundation.dao.MaFitBillDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentGoodsDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRuleDO;
import cn.com.leyizhuang.app.foundation.pojo.response.BillInfoResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.BillRepaymentGoodsInfoResponse;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.BillRepaymentGoodsDetailsVO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.BillRepaymentInfoVO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.MaFitBillVO;
import cn.com.leyizhuang.common.util.CountUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.mockito.internal.matchers.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author liuh
 * @date 2018/3/15
 */
@Service
public class MaFitBillServiceImpl implements MaFitBillService {

    private final Logger logger = LoggerFactory.getLogger(MaFitBillServiceImpl.class);

    @Autowired
    private MaFitBillDAO maFitBillDAO;

    @Autowired
    private BillRuleService billRuleService;

    @Autowired
    private BillInfoDAO billInfoDAO;

    @Autowired
    private PaymentDataService paymentDataService;

    @Autowired
    private AppOrderService orderService;

    @Autowired
    private AppStoreService appStoreService;


    @Override
    public PageInfo<MaFitBillVO> getFitNotOutBill(Integer page, Integer size, List<Long> storeIds, String keywords) {
        PageHelper.startPage(page, size);
        List<MaFitBillVO> fitBillVOList = this.maFitBillDAO.getFitNotOutBill(storeIds, keywords);
        return new PageInfo<>(fitBillVOList);
    }

    @Override
    public PageInfo<MaFitBillVO> getHistoryFitBill(Integer page, Integer size, List<Long> storeIds, String keywords) {
        PageHelper.startPage(page, size);
        List<MaFitBillVO> fitBillVOList = this.maFitBillDAO.getHistoryFitBill(storeIds, keywords);
        List<MaFitBillVO> fitBillVOLists  =   MaFitBillVO.transform(fitBillVOList);
        return new PageInfo<>(fitBillVOLists);
    }

    @Override
    public PageInfo<BillRepaymentGoodsInfoResponse> getNoPayOrderBillByBillNo(Integer page, Integer size, Long storeId, String startTime, String endTime, String orderNo) {
        PageHelper.startPage(page, size);
        // 本期未还订单
        startTime = startTime.trim();
        endTime = endTime.trim();
        if (null != startTime && !"".equals(startTime)) {
            startTime +=" 00:00:00";
        }
        if (null != endTime && !"".equals(endTime)) {
            endTime +=" 23:59:59";
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime endTimeL = null;
        if (null != endTime && !"".equals(endTime)) {
            endTimeL = LocalDateTime.parse(endTime, df);
        }
        LocalDateTime startTimeL = null;
        if (null != startTime && !"".equals(startTime)) {
            startTimeL = LocalDateTime.parse(startTime, df);
        }

        BillInfoDO billInfoDO = billInfoDAO.findBillByStatus(BillStatusEnum.ALREADY_OUT, storeId);
        if (billInfoDO == null) {
            // 已出账单不存在 处理未出账单
            billInfoDO = billInfoDAO.findBillByStatus(BillStatusEnum.NOT_OUT, storeId);

            if (billInfoDO == null) {
                billInfoDO = this.createBillInfo(storeId);
            }
        }
        LocalDateTime billStartTime = null; // 账单开始时间
        LocalDateTime billEndTime = null;   // 账单结束时间
        Instant instant = billInfoDO.getBillStartDate().toInstant();
        ZoneId zone = ZoneId.systemDefault();
        //billStartTime = instant.atZone(zone).toLocalDateTime();
        Instant instant2 = billInfoDO.getBillEndDate().toInstant();
        billEndTime = instant2.atZone(zone).toLocalDateTime();
        // 设置客户选择时间范围
        if (startTimeL != null && startTimeL.isAfter(billStartTime) && startTimeL.isBefore(billEndTime)) {
            billStartTime = startTimeL;
        }
        if (endTimeL != null && endTimeL.isAfter(billStartTime) && endTimeL.isBefore(billEndTime)) {
            billEndTime = endTimeL;
        }


        List<BillRepaymentGoodsInfoResponse> currentNotPayOrderDetails = new ArrayList<>();
        if(null!=billStartTime && null != billEndTime){
            currentNotPayOrderDetails = maFitBillDAO.getCurrentOrderDetails(billStartTime, billEndTime, false, storeId, orderNo);
        }else if(null == billStartTime && null != billEndTime){
            currentNotPayOrderDetails = maFitBillDAO.getCurrentOrderDetails(null, billEndTime, false, storeId, orderNo);
        }else{
            currentNotPayOrderDetails = maFitBillDAO.getCurrentOrderDetails(null, null, false, storeId, orderNo);
        }

    /*    // 计算滞纳金
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
        response.setPriorNotPaidInterestAmount(fees);*/

        return new PageInfo<>(currentNotPayOrderDetails);
    }

    @Override
    public PageInfo<BillRepaymentInfoVO> getbillRepaymentInfoByBillNo(Integer page, Integer size, String billNo, String startTime, String endTime, String repaymentNo) {
        PageHelper.startPage(page, size);
        if (null != startTime) {
            startTime.trim();
            if ("".equals(startTime)) {
                startTime = null;
            } else {
                startTime += " 00:00:00";
            }
        }
        if (null != endTime) {
            endTime.trim();
            if ("".equals(endTime)) {
                endTime = null;
            } else {
                endTime += " 23:59:59";
            }
        }
        repaymentNo = repaymentNo.trim();
        if ("".equals(repaymentNo)) {
            repaymentNo = null;
        }
        List<BillRepaymentInfoDO> billRepaymentInfoDOList = this.maFitBillDAO.getbillRepaymentInfoByBillNo(billNo, startTime, endTime, repaymentNo);
        return new PageInfo<>(BillRepaymentInfoVO.transform(billRepaymentInfoDOList));
    }

    @Override
    public PageInfo<BillRepaymentGoodsDetailsVO> getbillRepaymentOrderInfoByBillNo(Integer page, Integer size, String repaymentNo) {
        PageHelper.startPage(page, size);
        List<BillRepaymentGoodsDetailsDO> billRepaymentInfoDOList = this.maFitBillDAO.getbillRepaymentOrderInfoByBillNo(repaymentNo);
        return new PageInfo<>(BillRepaymentGoodsDetailsVO.transform(billRepaymentInfoDOList));
    }

    @Override
    public BillInfoDO getFitBillByBillNo(String billNo) {
        if (null == billNo) {
            return null;
        }
        BillInfoDO maFitBillVO = this.maFitBillDAO.getFitBillByBillNo(billNo);
        return maFitBillVO;
    }

    @Override
    public void saveBillRepaymentInfo(BillRepaymentInfoDO billRepaymentInfoDO) {
        if (null == billRepaymentInfoDO) {
            this.maFitBillDAO.saveBillRepaymentInfo(billRepaymentInfoDO);
        }
    }

    @Override
    public void saveBillRepaymentGoodsDetails(BillRepaymentGoodsDetailsDO billRepaymentGoodsDetailsDO) {
        if (null == billRepaymentGoodsDetailsDO) {
            this.maFitBillDAO.saveBillRepaymentGoodsDetails(billRepaymentGoodsDetailsDO);
        }
    }

    @Override
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
/*        List<BillRepaymentGoodsInfoResponse> list = new ArrayList<>();
        response.setNotPayOrderDetails(list);
        response.setPaidOrderDetails(list);*/

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
        //response.setPaidOrderDetails(currentPaidOrderDetails);
        // 合并未还清结果集
        beforNotPayOrderDetails.addAll(currentNotPayOrderDetails);
        // TODO 排序
        //response.setNotPayOrderDetails(beforNotPayOrderDetails);

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
    public BillInfoDO createBillInfo(Long storeId) {
        AppStore store = this.appStoreService.findById(storeId);
        if (null == store) {
            return null;
        }
        //根据门店ID查询账单规则
        BillRuleDO billRuleDO = this.billRuleService.getBillRuleByStoreId(storeId);
        if (null == billRuleDO) {
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
            //逾期天数
            Integer overdueDays = DateUtil.getDifferDays(DateUtil.getDifferenceFatalism(billDate, repaymentDeadlineDate, goodsDetails.getShipmentTime()), new Date());
            if (overdueDays < 0) {
                overdueDays = 0;
            }
            goodsDetails.setInterestAmount(CountUtil.mul(goodsDetails.getOrderCreditMoney(), interestRate, overdueDays, AppConstant.INTEREST_RATE_UNIT));
        }

        return goodsDetailsList;
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

}
