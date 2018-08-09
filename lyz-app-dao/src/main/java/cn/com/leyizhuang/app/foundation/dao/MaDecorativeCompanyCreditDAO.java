package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyCredit;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanySubvention;
import cn.com.leyizhuang.app.foundation.pojo.user.StCreditChangeRule;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaDecorativeCompanyCreditDAO {

   int updateDecorativeCompanyCredit(DecorativeCompanyCredit decorativeCompanyCredit);

   int  updateDecorativeCompanySubvention(DecorativeCompanySubvention decorativeCompanySubvention);

   DecorativeCompanyCredit findDecorativeCompanyCreditByStoreId(Long id);

   DecorativeCompanySubvention findDecorativeCompanySubventionByStoreId(Long id);

   void saveDecorativeCompanyCredit(DecorativeCompanyCredit decorativeCompanyCredit);

   void  saveDecorativeCompanySubvention(DecorativeCompanySubvention decorativeCompanySubvention);

   List<StCreditChangeRule> findStCreditRules();
}
