package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.management.guide.GuideCreditChangeDetailDO;
import cn.com.leyizhuang.app.foundation.pojo.management.guide.GuideCreditMoney;
import cn.com.leyizhuang.app.foundation.pojo.management.guide.GuideCreditMoneyDetail;
import cn.com.leyizhuang.app.foundation.vo.management.guide.GuideCreditChangeDetailVO;
import com.github.pagehelper.PageInfo;

import java.util.Map;


public interface MaEmpCreditMoneyService {


    void update(GuideCreditMoney guideCreditMoneyVO);

    void clearTempCreditLimit(Long id);

    PageInfo<GuideCreditChangeDetailDO> queryAvailableCreditMoneyChangePage(Integer page, Integer size, Long id);

    PageInfo<GuideCreditChangeDetailDO> queryTempCreditMoneyChangePage(Integer page, Integer size, Long id);

    PageInfo<GuideCreditChangeDetailDO> queryFixedCreditMoneyChangePage(Integer page, Integer size, Long id);

    void saveCreditChange(GuideCreditChangeDetailVO guideCreditChangeDetailVO);

    Map<String,Long> saveCreditMoneyChange(GuideCreditMoneyDetail guideCreditMoneyDetail);

    void clearAllTempCredit();

}
