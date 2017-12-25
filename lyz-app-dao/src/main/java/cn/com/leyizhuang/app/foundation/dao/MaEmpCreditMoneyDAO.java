package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.guide.*;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MaEmpCreditMoneyDAO {

    void update(GuideCreditMoney guideCreditMoneyVO);

    void clearTempCreditLimit(Long id);

    List<GuideCreditChangeDetailDO> queryAvailableCreditMoneyChangePage(Long id);

    List<GuideCreditChangeDetailDO> queryTempCreditMoneyChangePage(Long id);

    List<GuideCreditChangeDetailDO> queryFixedCreditMoneyChangePage(Long id);

    void saveCreditChange(GuideCreditChangeDetailDO guideCreditChangeDetailDO);

    void saveCreditLimitAvailableChange( GuideAvailableCreditChange guideAvailableCreditChange);

    void saveTempCreditLimitChange(GuideTempCreditChange guideTempCreditChange);

    void saveFixedCreditLimitChange( GuideFixedCreditChange GuideFixedCreditChange);

    void clearAllTempCredit();
}
