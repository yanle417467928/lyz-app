package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppConstant;
import cn.com.leyizhuang.app.core.constant.BillStatusEnum;
import cn.com.leyizhuang.app.core.constant.OnlinePayType;
import cn.com.leyizhuang.app.core.constant.PaymentDataStatus;
import cn.com.leyizhuang.app.core.utils.DateUtil;
import cn.com.leyizhuang.app.foundation.dao.BillInfoDAO;
import cn.com.leyizhuang.app.foundation.pojo.PaymentDataDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentGoodsDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRuleDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.response.BillInfoResponse;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.BillInfoService;
import cn.com.leyizhuang.app.foundation.service.BillRuleService;
import cn.com.leyizhuang.app.foundation.service.PaymentDataService;
import cn.com.leyizhuang.common.util.CountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/6/26
 */
@Service
public class BillInfoServiceImpl implements BillInfoService {

    @Autowired
    private BillRuleService billRuleService;

    @Autowired
    private BillInfoDAO billInfoDAO;

    @Autowired
    private PaymentDataService paymentDataService;

    @Autowired
    private AppOrderService orderService;

    public void lookBillRepayment(){

    }

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
    public void handleBillRepaymentAfterOnlinePayUp(String repaymentNo, String tradeNo, String tradeStatus, OnlinePayType onlinePayType) {
        List<PaymentDataDO> paymentDataList = paymentDataService.findByOrderNoAndTradeStatus(repaymentNo, PaymentDataStatus.TRADE_SUCCESS);
        PaymentDataDO paymentData = paymentDataList.get(0);

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

    public BillInfoResponse lookBill(String starTime, String endTime, Long storeid,Integer page,Integer size){

        return null;
    }
}
