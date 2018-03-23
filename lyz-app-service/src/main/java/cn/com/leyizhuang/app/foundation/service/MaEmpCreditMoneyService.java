package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.management.guide.GuideCreditChangeDetail;
import cn.com.leyizhuang.app.foundation.pojo.management.guide.GuideCreditChangeDetailDO;
import cn.com.leyizhuang.app.foundation.pojo.management.guide.GuideCreditMoney;
import cn.com.leyizhuang.app.foundation.pojo.management.guide.GuideCreditMoneyDetail;
import com.github.pagehelper.PageInfo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


public interface MaEmpCreditMoneyService {


    void update(GuideCreditMoneyDetail guideCreditMoneyDetail,GuideCreditChangeDetail guideCreditChangeDetail);

    void saveEmpCreditMoney(GuideCreditMoneyDetail guideCreditMoneyDetail,GuideCreditChangeDetail guideCreditChangeDetail);

    int updateGuideCreditMoneyByRepayment(Long sellerId, BigDecimal availableCreditMoney,Date lastUpdateTime);

    void clearTempCreditLimit(GuideCreditMoneyDetail guideCreditMoneyDetail,GuideCreditChangeDetail guideCreditChangeDetail);

    void saveCreditMoneyChange(GuideCreditMoneyDetail guideCreditMoneyDetail,GuideCreditChangeDetail guideCreditChangeDetail);

    PageInfo<GuideCreditChangeDetailDO> queryAvailableCreditMoneyChangePage(Integer page, Integer size, Long id);

    PageInfo<GuideCreditChangeDetailDO> queryTempCreditMoneyChangePage(Integer page, Integer size, Long id);

    PageInfo<GuideCreditChangeDetailDO> queryFixedCreditMoneyChangePage(Integer page, Integer size, Long id);

    void saveCreditChange(GuideCreditChangeDetail guideCreditChangeDetail);

    Map<String,Long> saveAllCreditMoneyChange(GuideCreditMoneyDetail guideCreditMoneyDetail);

    void clearAllTempCredit();

    List<GuideCreditMoney> findAllGuideCreditMoney();

    void autoClearTempCreditMoney();

    GuideCreditMoney findGuideCreditMoneyAvailableByEmpId(Long sellerId);

}
