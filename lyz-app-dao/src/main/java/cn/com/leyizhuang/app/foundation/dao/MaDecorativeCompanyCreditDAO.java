package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyCredit;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanySubvention;
import org.springframework.stereotype.Repository;

@Repository
public interface MaDecorativeCompanyCreditDAO {

   void updateDecorativeCompanyCredit(DecorativeCompanyCredit decorativeCompanyCredit);

   void  updateDecorativeCompanySubvention(DecorativeCompanySubvention decorativeCompanySubvention);
}
