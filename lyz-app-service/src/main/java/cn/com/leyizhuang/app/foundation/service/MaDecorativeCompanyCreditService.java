package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.StoreCreditMoneyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyCredit;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanySubvention;

public interface MaDecorativeCompanyCreditService {

    void updateDecorativeCompanyCredit(DecorativeCompanyCredit decorativeCompanyCredit,StoreCreditMoneyChangeLog storeCreditMoneyChangeLog);

    void updateDecorativeCompanySubvention(DecorativeCompanySubvention decorativeCompanySubvention);

    DecorativeCompanyCredit findDecorativeCompanyCreditByStoreId(Long id);
}
