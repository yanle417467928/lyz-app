package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppConstant;
import cn.com.leyizhuang.app.foundation.dao.MaDecorativeCompanyCreditDAO;
import cn.com.leyizhuang.app.foundation.pojo.StoreCreditMoneyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyCredit;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanySubvention;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.MaDecorativeCompanyCreditService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class MaDecorativeCompanyCreditServiceImpl implements MaDecorativeCompanyCreditService {
    @Resource
    private MaDecorativeCompanyCreditDAO maDecorativeCompanyCreditDAO;
    @Resource
    private AppStoreService appStoreService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDecorativeCompanyCreditAndSubvention(DecorativeCompanyInfo decorativeCompanyInfo, StoreCreditMoneyChangeLog storeCreditMoneyChangeLog) {
        Date date = new Date();
        DecorativeCompanyCredit decorativeCompanyCreditBefore = this.findDecorativeCompanyCreditByStoreId(decorativeCompanyInfo.getStoreId());
        DecorativeCompanySubvention decorativeCompanySubventionBefore = this.findDecorativeCompanySubventionByStoreId(decorativeCompanyInfo.getStoreId());

        if (null != decorativeCompanyInfo.getCredit()) {
            DecorativeCompanyCredit decorativeCompanyCredit = new DecorativeCompanyCredit();
            decorativeCompanyCredit.setStoreId(decorativeCompanyInfo.getStoreId());
            decorativeCompanyCredit.setCreditLimit(decorativeCompanyInfo.getCreditLimit());
            decorativeCompanyCredit.setCredit(decorativeCompanyInfo.getCredit());
            if (null == decorativeCompanyCreditBefore) {
                decorativeCompanyCredit.setCreateTime(date);
                storeCreditMoneyChangeLog.setChangeAmount(decorativeCompanyInfo.getCredit().doubleValue());
                decorativeCompanyCredit.setCreditLastUpdateTime(date);
                this.saveDecorativeCompanyCredit(decorativeCompanyCredit, storeCreditMoneyChangeLog);
            } else {
                decorativeCompanyCredit.setCreditLastUpdateTime(decorativeCompanyInfo.getCreditLastUpdateTime());
                storeCreditMoneyChangeLog.setChangeAmount(decorativeCompanyInfo.getCredit().subtract(decorativeCompanyCreditBefore.getCredit()).doubleValue());
                this.updateDecorativeCompanyCredit(decorativeCompanyCredit, storeCreditMoneyChangeLog);
            }
        }
        if (null != decorativeCompanyInfo.getSponsorship()) {
            DecorativeCompanySubvention decorativeCompanySubvention = new DecorativeCompanySubvention();
            decorativeCompanySubvention.setStoreId(decorativeCompanyInfo.getStoreId());
            decorativeCompanySubvention.setSponsorship(decorativeCompanyInfo.getSponsorship());
            if (null == decorativeCompanySubventionBefore) {
                decorativeCompanySubvention.setCreateTime(date);
                decorativeCompanySubvention.setSponsorshipLastUpdateTime(date);
                this.saveDecorativeCompanySubvention(decorativeCompanySubvention);
            } else {
                decorativeCompanySubvention.setSponsorshipLastUpdateTime(decorativeCompanyInfo.getSponsorshipLastUpdateTime());
                this.updateDecorativeCompanySubvention(decorativeCompanySubvention);
            }
        }

    }


    @Override
    public void updateDecorativeCompanyCredit(DecorativeCompanyCredit decorativeCompanyCredit, StoreCreditMoneyChangeLog storeCreditMoneyChangeLog) {
        for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
            if (null != decorativeCompanyCredit) {
                int affectLine = maDecorativeCompanyCreditDAO.updateDecorativeCompanyCredit(decorativeCompanyCredit);
                if (affectLine > 0) {
                    //保存日志
                    appStoreService.addStoreCreditMoneyChangeLog(storeCreditMoneyChangeLog);
                    break;
                }
            }
        }
    }

    @Override
    public void updateDecorativeCompanySubvention(DecorativeCompanySubvention decorativeCompanySubvention) {
        for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
            if (null != decorativeCompanySubvention) {
                int affectLine = maDecorativeCompanyCreditDAO.updateDecorativeCompanySubvention(decorativeCompanySubvention);
                if (affectLine > 0) {
                    break;
                }
            }
        }
    }

    @Override
    public DecorativeCompanyCredit findDecorativeCompanyCreditByStoreId(Long id) {
        return maDecorativeCompanyCreditDAO.findDecorativeCompanyCreditByStoreId(id);
    }


    @Override
    public DecorativeCompanySubvention findDecorativeCompanySubventionByStoreId(Long id) {
        return maDecorativeCompanyCreditDAO.findDecorativeCompanySubventionByStoreId(id);
    }


    @Override
    public void saveDecorativeCompanyCredit(DecorativeCompanyCredit decorativeCompanyCredit, StoreCreditMoneyChangeLog storeCreditMoneyChangeLog) {
        this.maDecorativeCompanyCreditDAO.saveDecorativeCompanyCredit(decorativeCompanyCredit);
        appStoreService.addStoreCreditMoneyChangeLog(storeCreditMoneyChangeLog);

    }

    @Override
    public void saveDecorativeCompanySubvention(DecorativeCompanySubvention decorativeCompanySubvention) {
        this.maDecorativeCompanyCreditDAO.saveDecorativeCompanySubvention(decorativeCompanySubvention);
    }

}
