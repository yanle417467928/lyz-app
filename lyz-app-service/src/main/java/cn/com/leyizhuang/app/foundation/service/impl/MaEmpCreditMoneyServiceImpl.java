package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaEmpCreditMoneyDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.guide.*;
import cn.com.leyizhuang.app.foundation.vo.management.guide.GuideCreditChangeDetailVO;
import cn.com.leyizhuang.app.foundation.service.MaEmpCreditMoneyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("maEmpCreditMoneyService")
@Transactional
public class MaEmpCreditMoneyServiceImpl implements MaEmpCreditMoneyService {

    @Autowired
    private MaEmpCreditMoneyDAO maEmpCreditMoneyDAO;

    @Override
    public void update(GuideCreditMoney guideCreditMoneyVO){
        if (null != guideCreditMoneyVO) {
            this.maEmpCreditMoneyDAO.update(guideCreditMoneyVO);
        }
    }


    @Override
    public void clearTempCreditLimit(Long id){
        if (null != id) {
            this.maEmpCreditMoneyDAO.clearTempCreditLimit(id);
        }
    }

    @Override
    public PageInfo<GuideCreditChangeDetailDO> queryAvailableCreditMoneyChangePage(Integer page, Integer size, Long id){
        PageHelper.startPage(page, size);
        List<GuideCreditChangeDetailDO> pageCreditMoneyDOList = this.maEmpCreditMoneyDAO.queryAvailableCreditMoneyChangePage(id);
        return new PageInfo<>(pageCreditMoneyDOList);
    }

    @Override
    public PageInfo<GuideCreditChangeDetailDO> queryTempCreditMoneyChangePage(Integer page, Integer size, Long id){
        PageHelper.startPage(page, size);
        List<GuideCreditChangeDetailDO> pageCreditMoneyDOList = this.maEmpCreditMoneyDAO.queryTempCreditMoneyChangePage(id);
        return new PageInfo<>(pageCreditMoneyDOList);
    }

    @Override
    public PageInfo<GuideCreditChangeDetailDO> queryFixedCreditMoneyChangePage(Integer page, Integer size, Long id){
        PageHelper.startPage(page, size);
        List<GuideCreditChangeDetailDO> pageCreditMoneyDOList = this.maEmpCreditMoneyDAO.queryFixedCreditMoneyChangePage(id);
        return new PageInfo<>(pageCreditMoneyDOList);
    }

    @Override
    public void saveCreditChange(GuideCreditChangeDetailVO guideCreditChangeDetailVO){
        if(null!=guideCreditChangeDetailVO){
            this.maEmpCreditMoneyDAO.saveCreditChange(GuideCreditChangeDetailDO.transform(guideCreditChangeDetailVO));
        }
    }


    @Override
    public Map<String,Long> saveCreditMoneyChange(GuideCreditMoneyDetail guideCreditMoneyDetail){
        if(null!=guideCreditMoneyDetail){
            Map idMap = new HashMap();
            if(guideCreditMoneyDetail.getOriginalCreditLimitAvailable().compareTo(guideCreditMoneyDetail.getCreditLimitAvailable())!=0){
                BigDecimal  creditLimitAvailableChangeAmount = guideCreditMoneyDetail.getCreditLimitAvailable().subtract(guideCreditMoneyDetail.getOriginalCreditLimitAvailable());
                GuideAvailableCreditChange guideAvailableCreditChange = new GuideAvailableCreditChange(null,creditLimitAvailableChangeAmount,guideCreditMoneyDetail.getCreditLimitAvailable());
                this.maEmpCreditMoneyDAO.saveCreditLimitAvailableChange(guideAvailableCreditChange) ;
                idMap.put("creditLimitAvailableId",guideAvailableCreditChange.getId());
            }else{
                idMap.put("creditLimitAvailableId",null) ;
            }
            if(guideCreditMoneyDetail.getOriginalTempCreditLimit().compareTo(guideCreditMoneyDetail.getTempCreditLimit())!=0){
                BigDecimal  tempCreditLimitChangeAmount = guideCreditMoneyDetail.getTempCreditLimit().subtract(guideCreditMoneyDetail.getOriginalTempCreditLimit());
                GuideTempCreditChange guideTempCreditChange = new GuideTempCreditChange(null,tempCreditLimitChangeAmount,guideCreditMoneyDetail.getTempCreditLimit());
                this.maEmpCreditMoneyDAO.saveTempCreditLimitChange(guideTempCreditChange) ;
                idMap.put("tempCreditLimitId",guideTempCreditChange.getId());
            }else{
                idMap.put("tempCreditLimitId",null) ;
            }
            if(guideCreditMoneyDetail.getCreditLimit().compareTo(guideCreditMoneyDetail.getOriginalCreditLimit())!=0){
                BigDecimal  fixedCreditLimitChangeAmount = guideCreditMoneyDetail.getCreditLimit().subtract(guideCreditMoneyDetail.getOriginalCreditLimit());
                GuideFixedCreditChange GuideFixedCreditChange = new GuideFixedCreditChange(null,fixedCreditLimitChangeAmount,guideCreditMoneyDetail.getCreditLimit());
                this.maEmpCreditMoneyDAO.saveFixedCreditLimitChange(GuideFixedCreditChange);
                idMap.put("fixedCreditLimitId",GuideFixedCreditChange.getId());
            }else{
                idMap.put("fixedCreditLimitId",null) ;
            }
            return  idMap;
        }else{
            return  null;
        }
    }



    @Override
    public void clearAllTempCredit(){
        maEmpCreditMoneyDAO.clearAllTempCredit();
    }
}
