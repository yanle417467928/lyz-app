package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.StoreCreditMoneyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyCredit;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanySubvention;

public interface MaDecorativeCompanyCreditService {

    void updateDecorativeCompanyCredit(DecorativeCompanyCredit decorativeCompanyCredit,StoreCreditMoneyChangeLog storeCreditMoneyChangeLog);

    void updateDecorativeCompanySubvention(DecorativeCompanySubvention decorativeCompanySubvention);

    void updateDecorativeCompanyCreditAndSubvention(DecorativeCompanyInfo decorativeCompanyInfo, StoreCreditMoneyChangeLog storeCreditMoneyChangeLog);

    DecorativeCompanyCredit findDecorativeCompanyCreditByStoreId(Long id);

    DecorativeCompanySubvention findDecorativeCompanySubventionByStoreId(Long id);

    void saveDecorativeCompanyCredit(DecorativeCompanyCredit decorativeCompanyCredit,StoreCreditMoneyChangeLog storeCreditMoneyChangeLog);

    void saveDecorativeCompanySubvention(DecorativeCompanySubvention decorativeCompanySubvention);

}
