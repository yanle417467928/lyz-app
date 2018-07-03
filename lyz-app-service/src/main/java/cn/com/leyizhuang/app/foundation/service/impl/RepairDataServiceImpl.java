package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.exception.LockStorePreDepositException;
import cn.com.leyizhuang.app.core.exception.SystemBusyException;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dao.AppEmployeeDAO;
import cn.com.leyizhuang.app.foundation.dao.OrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderJxPriceDifferenceReturnDetails;
import cn.com.leyizhuang.app.foundation.pojo.order.ReturnOrderJxPriceDifferenceRefundDetails;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.PromotionSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.SellerCreditMoneyResponse;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.user.EmpCreditChangeRule;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.CountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by 12421 on 2018/6/27.
 */
@Service
public class RepairDataServiceImpl implements RepairDataService {

    private static final Logger logger = LoggerFactory.getLogger(RepairDataServiceImpl.class);

    @Resource
    private OrderDAO orderDAO;

    @Resource
    private AppOrderService appOrderService;

    @Resource
    private AppActServiceImpl appActService;

    @Resource
    private ReturnOrderService returnOrderService;

    @Resource
    private AppStoreService storeService;

    @Autowired
    private AppEmployeeDAO employeeDAO;

    @Resource
    private AppEmployeeService employeeService;

    /**
     * 未返还经销差价的 买券订单经销差价返还 以及扣除
     */
    public void repairProductReturnJxPrice(String flag){
        List<String> orderNos = orderDAO.getOrderJmProduct();

        logger.info("一共有产生提货 未返经销差价 产品券订单 "+ orderNos.size() +"单");

        //循环处理
        for (String ordNo : orderNos){
            // TODO 处理
            try {
                processingOneOrder(ordNo,flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void processingOneOrder(String ordNo,String flag) throws Exception {
        OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(ordNo);
        logger.info("订单："+ordNo);
        // 取得此订单 产品券行
        List<OrderGoodsInfo> orderGoodsInfos = orderDAO.getOrderGoodsInfoByOrderNumber(ordNo);
        List<OrderJxPriceDifferenceReturnDetails> detailsList = new ArrayList<>(20);
        List<ReturnOrderJxPriceDifferenceRefundDetails> returnDetailsList = new ArrayList<>(20);
        for (OrderGoodsInfo goodsInfo : orderGoodsInfos){

            // 产品券 返/扣除 经销差价
            if (goodsInfo.getGoodsLineType().equals(AppGoodsLineType.PRODUCT_COUPON)){
                logger.info("产品券："+goodsInfo.getSku() +" 门店id:"+ orderBaseInfo.getStoreId());
                // 结算价
                Double settlementPrice = goodsInfo.getSettlementPrice() == null ? 0.00 : goodsInfo.getSettlementPrice();
                // 经销价
                Double jxPrice = goodsInfo.getWholesalePrice() == null ? 0.00 : goodsInfo.getWholesalePrice();
                // 差价
                Double subPrice = CountUtil.sub(settlementPrice,jxPrice);

                // 取本品券数量
                Integer bpQty = orderDAO.getBpProductByOrderNo(goodsInfo.getOrderNumber(),goodsInfo.getSku());
                Long proId = null;
                if (goodsInfo.getPromotionId() != null){
                    proId = Long.valueOf(goodsInfo.getPromotionId());
                }

                List<PromotionSimpleInfo> promotionSimpleInfoList = new ArrayList<>();
                if (proId != null){
                    PromotionSimpleInfo promotionSimpleInfo = new PromotionSimpleInfo();
                    promotionSimpleInfo.setPromotionId(proId);
                }
                Map<Long, Double> map = appActService.returnGcActIdAndJXDiscunt(promotionSimpleInfoList);

                logger.info("本品数量："+bpQty + " 购买数量："+ goodsInfo.getOrderQuantity());
                if (bpQty > goodsInfo.getOrderQuantity()){
                    throw new Exception("本品券数量 大于 使用数量");
                }else if (bpQty > 0){
                    // 返还经销差价

                    OrderJxPriceDifferenceReturnDetails details = new OrderJxPriceDifferenceReturnDetails();
                    if (null != goodsInfo.getPromotionId()) {
                        Long actId = Long.valueOf(goodsInfo.getPromotionId());
                        if (map.containsKey(actId)) {
                            Double gcDiscount = map.get(actId);
                            subPrice = CountUtil.sub(subPrice,gcDiscount);

                            details.setAmount((goodsInfo.getSettlementPrice() - goodsInfo.getWholesalePrice() - gcDiscount) * bpQty);
                            details.setUnitPrice(goodsInfo.getSettlementPrice() - goodsInfo.getWholesalePrice() - gcDiscount);
                        } else {
                            details.setAmount((goodsInfo.getSettlementPrice() - goodsInfo.getWholesalePrice()) * bpQty);
                            details.setUnitPrice(goodsInfo.getSettlementPrice() - goodsInfo.getWholesalePrice());
                        }
                    } else {
                        details.setAmount((goodsInfo.getSettlementPrice() - goodsInfo.getWholesalePrice()) * bpQty);
                        details.setUnitPrice(goodsInfo.getSettlementPrice() - goodsInfo.getWholesalePrice());
                    }
                    details.setCreateTime(new Date());
                    details.setOrderNumber(orderBaseInfo.getOrderNumber());
                    details.setQuantity(goodsInfo.getOrderQuantity());
                    details.setReceiptNumber(OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
                    details.setSku(goodsInfo.getSku());
                    details.setStoreId(orderBaseInfo.getStoreId());
                    details.setStoreCode(orderBaseInfo.getStoreCode());
                    // TODO 保存券类型
                    details.setGoodsLineType(AppGoodsLineType.PRODUCT_COUPON);
                    detailsList.add(details);
                    logger.info("返 ：结算价："+ settlementPrice + " 经销价: "+ jxPrice + " 差价："+ details.getUnitPrice()+ "数量："+bpQty +" 返差价总额："+ details.getAmount() );

                    // 扣除经销差价

                    List<ReturnOrderBaseInfo> returnOrderBaseInfoList = returnOrderService.getReturnBaseinfoByOrderNo(ordNo);


                    for (ReturnOrderBaseInfo returnOrderBaseInfo : returnOrderBaseInfoList){
                        //取退货数量
                        Integer returnBpQty = orderDAO.getBpProductByReturnNo(returnOrderBaseInfo.getReturnNo(),goodsInfo.getSku());

                        if (subPrice > AppConstant.DOUBLE_ZERO && returnBpQty > 0){
                            ReturnOrderJxPriceDifferenceRefundDetails returnDetails = new ReturnOrderJxPriceDifferenceRefundDetails();
                            returnDetails.setAmount(CountUtil.mul(subPrice,returnBpQty));
                            returnDetails.setCreateTime(new Date());
                            returnDetails.setRoid(returnOrderBaseInfo.getRoid());
                            returnDetails.setOrderNumber(returnOrderBaseInfo.getOrderNo());
                            returnDetails.setReturnNumber(returnOrderBaseInfo.getReturnNo());
                            returnDetails.setReturnQty(returnBpQty);
                            returnDetails.setSku(goodsInfo.getSku());
                            returnDetails.setStoreCode(returnOrderBaseInfo.getStoreCode());
                            returnDetails.setStoreId(returnOrderBaseInfo.getStoreId());
                            returnDetails.setUnitPrice(subPrice);
                            returnDetails.setRefundNumber(OrderUtils.getRefundNumber());
                            returnDetails.setGoodsLineType(AppGoodsLineType.PRODUCT_COUPON);
                            returnDetailsList.add(returnDetails);
                            logger.info("退单："+returnOrderBaseInfo.getReturnNo() + " 产品："+ goodsInfo.getSku());
                            logger.info("扣除：结算价："+ settlementPrice + " 经销价: "+ jxPrice + " 差价："+ subPrice+ "本品数量："+returnBpQty +" 返差价总额："+ returnDetails.getAmount() );
                        }

                    }

                }

            }

        }

        if (flag.equals("go")){
            returnJxSubPrice(detailsList,returnDetailsList,orderBaseInfo);
        }

    }

    @Transactional
    public void returnJxSubPrice(List<OrderJxPriceDifferenceReturnDetails> list1,
                                 List<ReturnOrderJxPriceDifferenceRefundDetails> list2,OrderBaseInfo orderBaseInfo){

        for (OrderJxPriceDifferenceReturnDetails details: list1){
            //判断 是否已经返还
            Boolean flag = orderDAO.isReturnJxSubPrice(orderBaseInfo.getOrderNumber(),details.getSku(),AppGoodsLineType.PRODUCT_COUPON.getValue());

            if (!flag){
                // 保存记录
                appOrderService.saveOrderJxPriceDifferenceReturnDetails(details);

                Double totalSubPrice = details.getAmount() == null ? 0D : details.getAmount();
                // 返预存款
                StorePreDeposit preDeposit = storeService.findStorePreDepositByUserIdAndIdentityType(orderBaseInfo.getCreatorId(), orderBaseInfo.getCreatorIdentityType().getValue());
                if (null != preDeposit) {
                    int affectLine = storeService.updateStoreDepositByStoreIdAndStoreDeposit(
                            preDeposit.getStoreId(), -totalSubPrice, preDeposit.getLastUpdateTime());
                    if (affectLine > 0) {
                        StPreDepositLogDO log = new StPreDepositLogDO();
                        log.setStoreId(preDeposit.getStoreId());
                        log.setChangeMoney(totalSubPrice);
                        log.setBalance(preDeposit.getBalance() + totalSubPrice);
                        log.setCreateTime(LocalDateTime.now());
                        log.setOrderNumber(orderBaseInfo.getOrderNumber());
                        log.setOperatorId(orderBaseInfo.getCreatorId());
                        log.setOperatorType(AppIdentityType.getAppIdentityTypeByValue(orderBaseInfo.getCreatorIdentityType().getValue()));
                        log.setOperatorIp("");
                        log.setChangeType(StorePreDepositChangeType.JX_PRICE_DIFFERENCE_RETURN);
                        log.setChangeTypeDesc(StorePreDepositChangeType.JX_PRICE_DIFFERENCE_RETURN.getDescription());
                        storeService.addStPreDepositLog(log);

                        logger.info("返还预存款成功："+orderBaseInfo.getOrderNumber() + "  "+ details.getSku() +"  $"+totalSubPrice);
                    } else {
                        throw new SystemBusyException("系统繁忙，请稍后再试!");
                    }
                } else {
                    throw new LockStorePreDepositException("没有找到该导购所在门店的预存款信息!"+orderBaseInfo.getStoreId());
                }

            }else{
                logger.info("重复返还："+orderBaseInfo.getOrderNumber() + "  "+ details.getSku());
            }

        }


        for (ReturnOrderJxPriceDifferenceRefundDetails details:list2){

            // 扣除
            Boolean flag = orderDAO.isBuckleJxSubPrice(details.getReturnNumber(),details.getSku(),AppGoodsLineType.PRODUCT_COUPON.getValue());

            if (!flag){
                //保存记录
                returnOrderService.saveReturnOrderJxPriceDifferenceRefundDetails(details);

                StorePreDeposit preDeposit = storeService.findStorePreDepositByStoreId(orderBaseInfo.getStoreId());
                Double totalSubPrice = details.getAmount() == null ? 0D : details.getAmount();
                if (null != preDeposit) {
                    int affectLine = storeService.updateStoreDepositByStoreIdAndStoreDeposit(
                            orderBaseInfo.getStoreId(), totalSubPrice, preDeposit.getLastUpdateTime());
                    if (affectLine > 0) {
                        StPreDepositLogDO log = new StPreDepositLogDO();
                        log.setStoreId(preDeposit.getStoreId());
                        log.setChangeMoney(totalSubPrice);
                        log.setBalance(preDeposit.getBalance() - totalSubPrice);
                        log.setCreateTime(LocalDateTime.now());
                        log.setOrderNumber(details.getReturnNumber());
                        log.setOperatorId(orderBaseInfo.getCreatorId());
                        log.setOperatorType(orderBaseInfo.getCreatorIdentityType());
                        //TODO 退单中是否加退单操作人Ip
                        log.setOperatorIp("");
                        log.setChangeType(StorePreDepositChangeType.JX_PRICE_DIFFERENCE_DEDUCTION);
                        log.setChangeTypeDesc(StorePreDepositChangeType.JX_PRICE_DIFFERENCE_DEDUCTION.getDescription());
                        storeService.addStPreDepositLog(log);
                        break;
                    } else {
                        throw new SystemBusyException("系统繁忙，请稍后再试!");
                    }
                } else {
                    throw new LockStorePreDepositException("没有找到该导购所在门店的预存款信息!");
                }
            }else {
                logger.info("重复扣除："+details.getReturnNumber() + "  "+ details.getSku());
            }
        }
    }

    /**
     * 刷新规则下所以的导购信用额度 及变更日志
     * @param flag
     */
    public void repairAllRuleScope(String flag){
        List<EmpCreditChangeRule> list = employeeDAO.findAllRule();

        for (EmpCreditChangeRule rule :list){
            this.repairEmpCredit(rule.getEmpId(),flag);
        }
    }

    public ResultDTO repairEmpCredit(Long empId, String flag){

        AppEmployee employee = employeeDAO.findById(empId);
        if (employee == null) {
            return null;
        }

        //读取变更规则
        EmpCreditChangeRule rule = employeeDAO.findChangeRuleByempId(empId);

        if (rule == null){
            logger.info("没有规则，不能调整导购余额及日志");
            return null;
        }

        logger.info("导购：" + employee.getName());

        // 找到导购可用额度变更日期 倒序
        List<EmpAvailableCreditMoneyChangeLog> logList = employeeDAO.getEmpAvailableCreditMoneyChangeLogByEmpIdOrderByCreateTimeDesc(empId);

        // 导购当前额度
        SellerCreditMoneyResponse creditMoneyResponse = employeeDAO.findCreditMoneyBalanceByUserId(empId);
        Double balance = creditMoneyResponse.getAvailableBalance() == null ? 0D : creditMoneyResponse.getAvailableBalance();

        // 第一条 取得日志余额
        EmpAvailableCreditMoneyChangeLog log = logList.get(0);
        Double logBlance = log.getCreditLimitAvailableAfterChange();
        Double ruleBalance = rule.getChangeMoney();

        if (ruleBalance == null){
            logger.info("改变规则为null empId:"+empId);
            return null;
        }
        logger.info("日志余额：" + logBlance + "真实余额："+ balance +" 待变更为："+ruleBalance);

        for (int i = 0 ; i< logList.size() ; i++){
            EmpAvailableCreditMoneyChangeLog changeLog = logList.get(i);
            if (changeLog != null) {

                Double changeAmount = changeLog.getCreditLimitAvailableChangeAmount() == null ? 0 : changeLog.getCreditLimitAvailableChangeAmount();
                Double afterAmount = changeLog.getCreditLimitAvailableAfterChange() == null ? 0 : changeLog.getCreditLimitAvailableAfterChange();
                Double amount =  CountUtil.sub(ruleBalance, changeAmount);

                logger.info(" 规则余额"+ ruleBalance +" 改变值：" + changeAmount + " 改变后："+ ruleBalance+ " 原改变后：" + afterAmount);

                if (i == 0){
                    changeLog.setCreditLimitAvailableAfterChange(ruleBalance);
                    ruleBalance = amount;
                }else {
                    changeLog.setCreditLimitAvailableAfterChange(ruleBalance);
                    ruleBalance = amount;
                }
            }
        }

        if (flag.equalsIgnoreCase("go")) {
            //改变额度
            Double ruleChangeMoney = rule.getChangeMoney();
            Double changeMoney = CountUtil.sub(ruleChangeMoney,balance);

            EmpCreditMoney empCreditMoney = employeeService.findEmpCreditMoneyByEmpId(empId);

            if (empCreditMoney == null){
                logger.info("empId:"+empId +"导购账户为空");
                return null;
            }

            int affectLine = employeeService.lockGuideCreditByUserIdAndCredit(
                    empId, changeMoney, empCreditMoney.getLastUpdateTime());

            if (affectLine > 0) {
                logger.info("修改余额成功！empId:"+empId);
                //改变日志
                for (EmpAvailableCreditMoneyChangeLog changeLog : logList) {
                    employeeDAO.updateEmpAvailableCreditMoneyChangeLog(changeLog);
                }
            }
        }

        return null;
    }
}
