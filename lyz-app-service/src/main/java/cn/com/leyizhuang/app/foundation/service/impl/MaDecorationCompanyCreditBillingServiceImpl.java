package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.StoreCreditMoneyChangeType;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dao.MaDecorationCompanyCreditBillingDAO;
import cn.com.leyizhuang.app.foundation.dto.DecorationCompanyCreditBillingDTO;
import cn.com.leyizhuang.app.foundation.pojo.StoreCreditMoneyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorationCompanyCreditBillingDO;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorationCompanyCreditBillingDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyCredit;
import cn.com.leyizhuang.app.foundation.pojo.reportDownload.AccountGoodsItemsDO;
import cn.com.leyizhuang.app.foundation.service.MaDecorativeCompanyCreditService;
import cn.com.leyizhuang.app.foundation.service.MaStoreService;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorationCompanyCreditBillingDetailsVO;
import cn.com.leyizhuang.app.foundation.service.MaDecorationCompanyCreditBillingService;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorationCompanyCreditBillingVO;
import cn.com.leyizhuang.common.util.CountUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/3/15
 */
@Service
public class MaDecorationCompanyCreditBillingServiceImpl implements MaDecorationCompanyCreditBillingService {

    private final Logger logger = LoggerFactory.getLogger(MaDecorationCompanyCreditBillingServiceImpl.class);

    @Autowired
    private MaDecorationCompanyCreditBillingDAO maDecorationCompanyCreditBillingDAO;

    @Autowired
    private MaStoreService maStoreService;

    @Autowired
    private MaDecorativeCompanyCreditService maDecorativeCompanyCreditService;


    @Override
    public PageInfo<DecorationCompanyCreditBillingDetailsVO> getDecorationCompanyCreditOrder(Integer page, Integer size, Long storeId, String startTime, String endTime, String keywords) {
        PageHelper.startPage(page, size);
        if (null != startTime && !"".equals(startTime)) {
            startTime += " 00:00:00";
        }
        if (null != endTime && !"".equals(endTime)) {
            endTime += " 23:59:59";
        }
        List<DecorationCompanyCreditBillingDetailsVO> decorationCompanyCreditBillingDetailsVOS = this.maDecorationCompanyCreditBillingDAO.getDecorationCompanyCreditOrder(storeId, startTime, endTime, keywords);
        return new PageInfo<>(decorationCompanyCreditBillingDetailsVOS);
    }

    @Override
    public Double getBillAllCreditMoney(Long storeId, List<String> orderNumbers) {
        return this.maDecorationCompanyCreditBillingDAO.getBillAllCreditMoney(storeId, orderNumbers);
    }

    @Transactional
    @Override
    public void createCreditBilling(String[] orderNumbers, DecorationCompanyCreditBillingDTO creditBillingDTO) {

        List<String> orderNumberList = new ArrayList<>();
        if (null != orderNumbers && orderNumbers.length > 0) {
            for (int i = 0; i < orderNumbers.length; i++) {
                orderNumberList.add(orderNumbers[i]);
            }
        }
        if (orderNumberList.size() == 0) {
            orderNumberList.add("加数据防止报错");
        }

        DecorationCompanyCreditBillingDO creditBillingDO = DecorationCompanyCreditBillingDO.transform(creditBillingDTO);
        if (null != creditBillingDO) {
            //获取登录用户ID
            ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
            if (null != shiroUser) {
                creditBillingDO.setOperationId(shiroUser.getId());
            }
            Long cityId = this.maStoreService.findCityIdByStoreId(creditBillingDTO.getStoreId());
            //生成单号
            String creditBillingNo = OrderUtils.generateCreditBillingNo(cityId);
            creditBillingDO.setCreditBillingNo(creditBillingNo);

            List<DecorationCompanyCreditBillingDetailsDO> detailsDOS = this.maDecorationCompanyCreditBillingDAO.getCreditBillingDetailsByOrderNumber(orderNumberList, creditBillingDTO.getStoreId());
            if (null != detailsDOS) {
                for (int i = 0; i < detailsDOS.size(); i++) {
                    detailsDOS.get(i).setCreditBillingNo(creditBillingNo);
                    creditBillingDO.setBillAmount(CountUtil.add(creditBillingDO.getBillAmount(), detailsDOS.get(i).getCreditMoney()));
                }
                this.maDecorationCompanyCreditBillingDAO.saveCreditBillingDO(creditBillingDO);
                this.maDecorationCompanyCreditBillingDAO.batchSaveCreditBillingDetailsDO(detailsDOS);
            }
        }
    }

    @Override
    public PageInfo<DecorationCompanyCreditBillingVO> getDecorationCompanyCreditBilling(Integer page, Integer size, Long storeId, String startTime, String endTime, String keywords, Boolean isPayOff) {
        PageHelper.startPage(page, size);
        List<DecorationCompanyCreditBillingVO> companyCreditBillingVOS = this.maDecorationCompanyCreditBillingDAO.getDecorationCompanyCreditBilling(storeId, startTime, endTime, keywords, isPayOff);
        return new PageInfo<>(companyCreditBillingVOS);
    }

    @Override
    public DecorationCompanyCreditBillingVO getDecorationCompanyCreditBillingById(Long id) {
        return this.maDecorationCompanyCreditBillingDAO.getDecorationCompanyCreditBillingById(id);
    }

    @Override
    public List<DecorationCompanyCreditBillingDetailsVO> getDecorationCompanyCreditBillingDetailsByCreditBillingNo(String creditBillingNo) {
        return this.maDecorationCompanyCreditBillingDAO.getDecorationCompanyCreditBillingDetailsByCreditBillingNo(creditBillingNo);
    }

    @Override
    public Boolean repaymentCreditBilling(Long id, Double amount, String paymentType) {
        DecorationCompanyCreditBillingDO creditBillingDO = this.maDecorationCompanyCreditBillingDAO.getCreditBillingById(id);
        try {
            if (null != creditBillingDO && creditBillingDO.getIsPayOff() == false) {
                //获取登录用户ID
                ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();

                //加信用金
                DecorativeCompanyCredit decorativeCompanyCreditBefore = maDecorativeCompanyCreditService.findDecorativeCompanyCreditByStoreId(creditBillingDO.getStoreId());
                decorativeCompanyCreditBefore.setCredit(new BigDecimal(CountUtil.add(amount, decorativeCompanyCreditBefore.getCredit().doubleValue())));
                StoreCreditMoneyChangeLog storeCreditMoneyChangeLog = new StoreCreditMoneyChangeLog();
                storeCreditMoneyChangeLog.setChangeAmount(amount.doubleValue());
                storeCreditMoneyChangeLog.setChangeType(StoreCreditMoneyChangeType.REPAYMENT);
                storeCreditMoneyChangeLog.setChangeTypeDesc(StoreCreditMoneyChangeType.REPAYMENT.getDescription());
                storeCreditMoneyChangeLog.setCreateTime(new Date());
                storeCreditMoneyChangeLog.setCreditLimitAvailableAfterChange(decorativeCompanyCreditBefore.getCredit().doubleValue());
                storeCreditMoneyChangeLog.setStoreId(creditBillingDO.getStoreId());
                storeCreditMoneyChangeLog.setOperatorType(AppIdentityType.ADMINISTRATOR);
                if (null != shiroUser) {
                    creditBillingDO.setPayOffOperationId((null == creditBillingDO.getPayOffOperationId() ? "" : creditBillingDO.getPayOffOperationId()) + shiroUser.getId() + ",");
                    storeCreditMoneyChangeLog.setOperatorId(shiroUser.getId());
                }
                storeCreditMoneyChangeLog.setOperatorIp("");
                this.maDecorativeCompanyCreditService.updateDecorativeCompanyCredit(decorativeCompanyCreditBefore, storeCreditMoneyChangeLog);
                //修改账单
                creditBillingDO.setRepaidAmount(CountUtil.add(creditBillingDO.getRepaidAmount(), amount));
                if (creditBillingDO.getBillAmount().equals(creditBillingDO.getRepaidAmount())) {
                    creditBillingDO.setIsPayOff(true);
                }
                this.maDecorationCompanyCreditBillingDAO.updateCreditBillingDetails(creditBillingDO);

                return Boolean.TRUE;
            }

        } catch (Exception e) {
            logger.error("装饰公司账单还款失败，repaymentCreditBilling ", e);
        }
        return Boolean.FALSE;
    }

    @Override
    public DecorationCompanyCreditBillingDO getCreditBillingById(Long id) {
        return this.maDecorationCompanyCreditBillingDAO.getCreditBillingById(id);
    }

    @Override
    public DecorationCompanyCreditBillingDO getCreditBillingByCreditBillingNo(String creditBillingNo) {
        return this.maDecorationCompanyCreditBillingDAO.getCreditBillingByCreditBillingNo(creditBillingNo);
    }

    @Override
    public List<AccountGoodsItemsDO> findGoodsItemsDOAll(String creditBillingNo) {
        return this.maDecorationCompanyCreditBillingDAO.findGoodsItemsDOAll(creditBillingNo);
    }

}
