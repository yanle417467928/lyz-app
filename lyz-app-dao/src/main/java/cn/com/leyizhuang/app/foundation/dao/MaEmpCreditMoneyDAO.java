package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.guide.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
@Repository
public interface MaEmpCreditMoneyDAO {

    void update(GuideCreditMoney guideCreditMoneyVO);

    void  updateGuideCreditMoneyByRepayment(@Param(value = "sellerId") Long sellerId, @Param(value = "availableCreditMoney") BigDecimal availableCreditMoney);

    void clearTempCreditLimit(Long id);

    List<GuideCreditChangeDetailDO> queryAvailableCreditMoneyChangePage(Long id);

    List<GuideCreditChangeDetailDO> queryTempCreditMoneyChangePage(Long id);

    List<GuideCreditChangeDetailDO> queryFixedCreditMoneyChangePage(Long id);

    void saveCreditChange(GuideCreditChangeDetailDO guideCreditChangeDetailDO);

    void saveCreditLimitAvailableChange( GuideAvailableCreditChange guideAvailableCreditChange);

    void saveTempCreditLimitChange(GuideTempCreditChange guideTempCreditChange);

    void saveFixedCreditLimitChange( GuideFixedCreditChange GuideFixedCreditChange);

    void clearAllTempCredit();

    List<GuideCreditMoney> findAllGuideCreditMoney();

    GuideCreditMoney findGuideCreditMoneyAvailableByEmpId(Long sellerId);
}
