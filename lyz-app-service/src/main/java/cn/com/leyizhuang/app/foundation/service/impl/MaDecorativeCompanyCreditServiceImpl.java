package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppConstant;
import cn.com.leyizhuang.app.foundation.dao.MaDecorativeCompanyCreditDAO;
import cn.com.leyizhuang.app.foundation.pojo.StoreCreditMoneyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyCredit;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanySubvention;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.MaDecorativeCompanyCreditService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MaDecorativeCompanyCreditServiceImpl implements MaDecorativeCompanyCreditService {
    @Resource
    private MaDecorativeCompanyCreditDAO maDecorativeCompanyCreditDAO;
    @Resource
    private AppStoreService appStoreService;


    @Override
    public void updateDecorativeCompanyCredit(DecorativeCompanyCredit decorativeCompanyCredit,StoreCreditMoneyChangeLog storeCreditMoneyChangeLog) {
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
}
