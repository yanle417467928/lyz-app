package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppConstant;
import cn.com.leyizhuang.app.core.constant.BillStatusEnum;
import cn.com.leyizhuang.app.core.constant.OnlinePayType;
import cn.com.leyizhuang.app.core.utils.DateUtil;
import cn.com.leyizhuang.app.foundation.dao.BillInfoDAO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentGoodsDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRuleDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.response.BillInfoResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.BillRepaymentGoodsInfoResponse;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.BillInfoService;
import cn.com.leyizhuang.app.foundation.service.BillRuleService;
import cn.com.leyizhuang.app.foundation.service.PaymentDataService;
import cn.com.leyizhuang.common.util.CountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/6/26
 */
@Service
public class BillInfoServiceImpl implements BillInfoService {

    private static final Logger logger = LoggerFactory.getLogger(BillInfoServiceImpl.class);

    @Autowired
    private BillRuleService billRuleService;

    @Autowired
    private BillInfoDAO billInfoDAO;

    @Autowired
    private PaymentDataService paymentDataService;

    @Autowired
    private AppOrderService orderService;

    @Override
    public List<BillRepaymentGoodsDetailsDO> computeInterestAmount(Long storeId, List<BillRepaymentGoodsDetailsDO> goodsDetailsDOList) {

        if (null == goodsDetailsDOList || goodsDetailsDOList.size() == 0) {
            return goodsDetailsDOList;
        }

        if (null == storeId){
            return goodsDetailsDOList;
        }
        //根据门店ID查询账单规则
        BillRuleDO billRuleDO = this.billRuleService.getBillRuleByStoreId(storeId);
        if (null == billRuleDO || null == billRuleDO.getInterestRate() || billRuleDO.getInterestRate().equals(0D)){
            return goodsDetailsDOList;
        }
        //利息
        Double interestRate = billRuleDO.getInterestRate();
        //还款截至日
        Integer repaymentDeadlineDate = billRuleDO.getRepaymentDeadlineDate();
        if (null == repaymentDeadlineDate || repaymentDeadlineDate == 0) {
            repaymentDeadlineDate = 1;
        }

        //逾期天数
        Integer overdueDays = DateUtil.getDifferenceFatalism(repaymentDeadlineDate);

        //计算利息（欠款金额 * 利息 * 逾期天数 * 利息单位）
        for (BillRepaymentGoodsDetailsDO goodsDetailsDO: goodsDetailsDOList) {
            goodsDetailsDO.setInterestAmount(CountUtil.mul(goodsDetailsDO.getOrderCreditMoney(), interestRate, overdueDays, AppConstant.INTEREST_RATE_UNIT));
        }

        return goodsDetailsDOList;
    }

    @Override
    public BillRepaymentInfoDO findBillRepaymentInfoByRepaymentNo(String repaymentNo) {
        return this.billInfoDAO.findBillRepaymentInfoByRepaymentNo(repaymentNo);
    }

    @Override
    @Transactional
    public void handleBillRepaymentAfterOnlinePayUp(String repaymentNo, OnlinePayType onlinePayType) {

        //更改账单收款信息
        BillRepaymentInfoDO billRepaymentInfoDO = this.billInfoDAO.findBillRepaymentInfoByRepaymentNo(repaymentNo);
        billRepaymentInfoDO.setOnlinePayType(onlinePayType);
        billRepaymentInfoDO.setIsPaid(Boolean.TRUE);
        billRepaymentInfoDO.setRepaymentTime(new Date());
        this.billInfoDAO.updateBillRepaymentInfo(billRepaymentInfoDO);

        //订单账单更新是否已付清、付清时间
        List<BillRepaymentGoodsDetailsDO> goodsDetailsDOS = this.billInfoDAO.findRepaymentGoodsDetailsByRepaymentNo(repaymentNo);
        if (null != goodsDetailsDOS && goodsDetailsDOS.size() > 0){
            for (BillRepaymentGoodsDetailsDO goodsDetails : goodsDetailsDOS) {
                if ("order".equals(goodsDetails.getOrderType())) {
                    OrderBillingDetails orderBillingDetails = new OrderBillingDetails();
                    orderBillingDetails.setOrderNumber(goodsDetails.getOrderNo());
                    orderBillingDetails.setIsPayUp(Boolean.TRUE);
                    orderBillingDetails.setPayUpTime(new Date());
                    orderService.updateOrderBillingDetails(orderBillingDetails);
                }
            }
        }

        //更新账单信息
        BillInfoDO billInfoDO = this.billInfoDAO.findBillInfoByBillNo(billRepaymentInfoDO.getBillNo());
        billInfoDO.setCurrentPaidAmount(CountUtil.add(billInfoDO.getCurrentPaidAmount(), billRepaymentInfoDO.getOnlinePayAmount()));
        billInfoDO.setPriorPaidInterestAmount(CountUtil.add(billInfoDO.getPriorPaidInterestAmount(), billRepaymentInfoDO.getTotalInterestAmount()));
        if (billInfoDO.getStatus() == BillStatusEnum.ALREADY_OUT){
            billInfoDO.setCurrentUnpaidAmount(CountUtil.sub(billInfoDO.getCurrentUnpaidAmount(), billRepaymentInfoDO.getOnlinePayAmount()));
            if (billInfoDO.getCurrentUnpaidAmount() < AppConstant.PAY_UP_LIMIT){
                billInfoDO.setStatus(BillStatusEnum.HISTORY);
            }
        }
        this.billInfoDAO.updateBillInfo(billInfoDO);

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

    public BillInfoResponse lookBill(String starTime, String endTime, Long storeid,Integer page,Integer size) throws Exception {
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
        BillInfoDO billInfoDO = billInfoDAO.findBillByStatus(BillStatusEnum.ALREADY_OUT);

        if (billInfoDO == null ){
            // 已出账单不存在 处理未出账单
            billInfoDO = billInfoDAO.findBillByStatus(BillStatusEnum.NOT_OUT);

            if (billInfoDO == null){
                return response;
            }
        }

        if (billInfoDO.getBillStartDate() == null){
            return response;
        }

        if (billInfoDO.getBillEndDate() == null){
            return response;
        }

        LocalDateTime billStartTime = null; // 账单开始时间
        LocalDateTime billEndTime = null;   // 账单结束时间
        Instant instant = billInfoDO.getBillStartDate().toInstant();
        ZoneId zone = ZoneId.systemDefault();
        billStartTime = instant.atZone(zone).toLocalDateTime();
        Instant instant2 = billInfoDO.getBillEndDate().toInstant();
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
        currentPaidOrderDetails = billInfoDAO.getCurrentOrderDetails(billStartTime,billEndTime,true);
        // 本期未还订单
        currentNotPayOrderDetails = billInfoDAO.getCurrentOrderDetails(billStartTime,billEndTime,false);
        // 上期未还订单
        beforNotPayOrderDetails = billInfoDAO.getCurrentOrderDetails(null,billStartTime,false);


        currentPaid = this.AddAllCreditMoney(currentPaidOrderDetails);
        currentNotPay = this.AddAllCreditMoney(currentNotPayOrderDetails);
        beforNotPay = this.AddAllCreditMoney(beforNotPayOrderDetails);

        currentPaidOrderDetails.addAll(currentNotPayOrderDetails); // 合并本期已还和未还订单
        currentBillAmount = this.AddAllPositiveCreditMoney(currentPaidOrderDetails);
        currentAdjustmentAmount = this.AddAllNegativeCreditMoney(currentPaidOrderDetails);

        // 计算滞纳金
        List<BillRepaymentGoodsDetailsDO> goodsDetailsDOList = BillRepaymentGoodsInfoResponse.transfer(beforNotPayOrderDetails);
        this.computeInterestAmount(storeid,goodsDetailsDOList);


        // 账单总金额
        billTotalAmount = CountUtil.add(currentBillAmount,currentAdjustmentAmount,beforNotPay,fees);

        response.setBillTotalAmount(billTotalAmount);
        response.setCurrentBillAmount(currentBillAmount);
        response.setCurrentAdjustmentAmount(currentAdjustmentAmount);
        response.setCurrentPaidAmount(currentPaid);
        response.setCurrentUnpaidAmount(currentNotPay);
        response.setPriorNotPaidBillAmount(beforNotPay);
        response.setPriorNotPaidInterestAmount(fees);


        return response;
    }

    /**
     * 求和所有信用金
     * @param list
     * @return
     */
    public Double AddAllCreditMoney(List<BillRepaymentGoodsInfoResponse> list){
        Double totalCreditMoney = 0D;
        if (list == null){
            return totalCreditMoney;
        }

        for (BillRepaymentGoodsInfoResponse response : list){
            totalCreditMoney = CountUtil.add(totalCreditMoney,response.getOrderCreditMoney());
        }

        return totalCreditMoney;
    }

    /**
     * 求和所以正向信用金
     * @param list
     * @return
     */
    public Double AddAllPositiveCreditMoney(List<BillRepaymentGoodsInfoResponse> list){
        Double totalCreditMoney = 0D;
        if (list == null){
            return totalCreditMoney;
        }

        for (BillRepaymentGoodsInfoResponse response : list){
            if(response.getOrderCreditMoney() > 0){
                totalCreditMoney = CountUtil.add(totalCreditMoney,response.getOrderCreditMoney());
            }
        }

        return totalCreditMoney;
    }

    /**
     * 求和所有负向信用金
     * @param list
     * @return
     */
    public Double AddAllNegativeCreditMoney(List<BillRepaymentGoodsInfoResponse> list){
        Double totalCreditMoney = 0D;
        if (list == null){
            return totalCreditMoney;
        }

        for (BillRepaymentGoodsInfoResponse response : list){
            if (response.getOrderCreditMoney() < 0){
                totalCreditMoney = CountUtil.add(totalCreditMoney,response.getOrderCreditMoney());
            }
        }

        return totalCreditMoney;
    }

    /**
     * 求和所有滞纳金
     * @param list
     * @return
     */
    public Double AddAllNegative(List<BillRepaymentGoodsInfoResponse> list){
        Double interestAmount = 0D;
        if (list == null){
            return interestAmount;
        }

        for (BillRepaymentGoodsInfoResponse response : list){
            if (response.getOrderCreditMoney() < 0){
                interestAmount = CountUtil.add(interestAmount,response.getInterestAmount());
            }
        }

        return interestAmount;
    }
}
