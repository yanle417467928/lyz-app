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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("maEmpCreditMoneyService")
public class MaEmpCreditMoneyServiceImpl implements MaEmpCreditMoneyService {

    @Autowired
    private MaEmpCreditMoneyDAO maEmpCreditMoneyDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(GuideCreditMoneyDetail guideCreditMoneyDetail,GuideCreditChangeDetailVO guideCreditChangeDetailVO){
        if (null != guideCreditMoneyDetail) {
            //得到更新后的可用额度
            BigDecimal CreditLimitChangeAmount =guideCreditMoneyDetail.getCreditLimit().subtract(guideCreditMoneyDetail.getOriginalCreditLimit());
            BigDecimal TempCreditLimitChangeAmount =guideCreditMoneyDetail.getTempCreditLimit().subtract(guideCreditMoneyDetail.getOriginalTempCreditLimit());
            BigDecimal AllChangeAmount = CreditLimitChangeAmount.add(TempCreditLimitChangeAmount);
            guideCreditMoneyDetail.setCreditLimitAvailable(guideCreditMoneyDetail.getOriginalCreditLimitAvailable().add(AllChangeAmount));
            //更新导购信用金
            GuideCreditMoney guideCreditMoneyVO = new GuideCreditMoney();
            guideCreditMoneyVO.setCreditLimit(guideCreditMoneyDetail.getCreditLimit());
            guideCreditMoneyVO.setCreditLimitAvailable(guideCreditMoneyDetail.getCreditLimitAvailable());
            guideCreditMoneyVO.setEmpId(guideCreditMoneyDetail.getEmpId());
            guideCreditMoneyVO.setTempCreditLimit(guideCreditMoneyDetail.getTempCreditLimit());
            this.maEmpCreditMoneyDAO.update(guideCreditMoneyVO);
            //更新变更详情父表与子表
            this.saveCreditMoneyChange(guideCreditMoneyDetail,guideCreditChangeDetailVO);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearTempCreditLimit(GuideCreditMoneyDetail guideCreditMoneyDetail,GuideCreditChangeDetailVO guideCreditChangeDetailVO){
        if (null != guideCreditMoneyDetail.getEmpId()) {
            this.maEmpCreditMoneyDAO.clearTempCreditLimit(guideCreditMoneyDetail.getEmpId());
        }else{
            throw  new RuntimeException("无法清空该员工的临时额度,reason：empId为空");
        }
        //得到存入变更字表的id
        guideCreditMoneyDetail.setCreditLimitAvailable(guideCreditMoneyDetail.getOriginalCreditLimitAvailable().subtract(guideCreditMoneyDetail.getOriginalTempCreditLimit()));
        Map<String,Long> idList  =this.saveAllCreditMoneyChange(guideCreditMoneyDetail);
        Long tempCreditChangeId = idList.get("tempCreditLimitId");
        Long availableCreditChangId = idList.get("creditLimitAvailableId");
        //补全额度改变明细VO
        guideCreditChangeDetailVO.setAvailableCreditChangId(new GuideAvailableCreditChange(availableCreditChangId,null,null));
        guideCreditChangeDetailVO.setTempCreditChangeId(new GuideTempCreditChange(tempCreditChangeId,null,null));
        //存入额度变更明细主表
        this.saveCreditChange(guideCreditChangeDetailVO);
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
            GuideCreditChangeDetailDO guideCreditChangeDetailDO =GuideCreditChangeDetailDO.transform(guideCreditChangeDetailVO);
            guideCreditChangeDetailDO.setOperatorType("BACKGOUND");
            guideCreditChangeDetailDO.setCreateTime(new Date());
            this.maEmpCreditMoneyDAO.saveCreditChange(guideCreditChangeDetailDO);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCreditMoneyChange(GuideCreditMoneyDetail guideCreditMoneyDetail,GuideCreditChangeDetailVO guideCreditChangeDetailVO){
        //得到金额变更字表ID集合
        Map<String,Long> idList  = this.saveAllCreditMoneyChange(guideCreditMoneyDetail);
        Long tempCreditChangeId = idList.get("tempCreditLimitId");
        Long fixedCreditChangeId = idList.get("fixedCreditLimitId");
        Long availableCreditChangId = idList.get("creditLimitAvailableId");
        //更新变更详情VO
        guideCreditChangeDetailVO.setAvailableCreditChangId(new GuideAvailableCreditChange(availableCreditChangId,null,null));
        guideCreditChangeDetailVO.setFixedCreditChangeId(new GuideFixedCreditChange(fixedCreditChangeId,null,null));
        guideCreditChangeDetailVO.setTempCreditChangeId(new GuideTempCreditChange(tempCreditChangeId,null,null));
        this.saveCreditChange(guideCreditChangeDetailVO);
        }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String,Long> saveAllCreditMoneyChange(GuideCreditMoneyDetail guideCreditMoneyDetail){
        if(null!=guideCreditMoneyDetail){
            Map idMap = new HashMap();
            if(null==guideCreditMoneyDetail.getOriginalTempCreditLimit()){
                guideCreditMoneyDetail.setOriginalTempCreditLimit(BigDecimal.ZERO);
            }
            if(null==guideCreditMoneyDetail.getOriginalCreditLimitAvailable()){
                guideCreditMoneyDetail.setOriginalCreditLimitAvailable(BigDecimal.ZERO);
            }
            if(null==guideCreditMoneyDetail.getOriginalCreditLimit()){
                guideCreditMoneyDetail.setOriginalCreditLimit(BigDecimal.ZERO);
            }
            //判断可用额度是否有改变 有保存 无返回空
            if(null!=guideCreditMoneyDetail.getCreditLimitAvailable()&&guideCreditMoneyDetail.getOriginalCreditLimitAvailable().compareTo(guideCreditMoneyDetail.getCreditLimitAvailable())!=0){
                BigDecimal  creditLimitAvailableChangeAmount = guideCreditMoneyDetail.getCreditLimitAvailable().subtract(guideCreditMoneyDetail.getOriginalCreditLimitAvailable());
                GuideAvailableCreditChange guideAvailableCreditChange = new GuideAvailableCreditChange(null,creditLimitAvailableChangeAmount,guideCreditMoneyDetail.getCreditLimitAvailable());
                this.maEmpCreditMoneyDAO.saveCreditLimitAvailableChange(guideAvailableCreditChange) ;
                idMap.put("creditLimitAvailableId",guideAvailableCreditChange.getId());
            }else{
                idMap.put("creditLimitAvailableId",null) ;
            }
            //判断临时额度是否有改变 有保存 无返回空
            if(null!=guideCreditMoneyDetail.getTempCreditLimit()&&guideCreditMoneyDetail.getOriginalTempCreditLimit().compareTo(guideCreditMoneyDetail.getTempCreditLimit())!=0){
                 BigDecimal   tempCreditLimitChangeAmount = guideCreditMoneyDetail.getTempCreditLimit().subtract(guideCreditMoneyDetail.getOriginalTempCreditLimit());
                GuideTempCreditChange guideTempCreditChange = new GuideTempCreditChange(null,tempCreditLimitChangeAmount,guideCreditMoneyDetail.getTempCreditLimit());
                this.maEmpCreditMoneyDAO.saveTempCreditLimitChange(guideTempCreditChange) ;
                idMap.put("tempCreditLimitId",guideTempCreditChange.getId());
            }else{
                idMap.put("tempCreditLimitId",null) ;
            }
            //判断固定额度是否有改变 有保存 无返回空
            if(null!=guideCreditMoneyDetail.getCreditLimit()&&guideCreditMoneyDetail.getCreditLimit().compareTo(guideCreditMoneyDetail.getOriginalCreditLimit())!=0){
                BigDecimal   fixedCreditLimitChangeAmount = guideCreditMoneyDetail.getCreditLimit().subtract(guideCreditMoneyDetail.getOriginalCreditLimit());
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
    @Transactional(rollbackFor = Exception.class)
    public void autoClearTempCreditMoney(){
        List<GuideCreditMoney> guideCreditMoneyList = this.findAllGuideCreditMoney();
        if(guideCreditMoneyList.size()==0){
            throw new RuntimeException("查找到导购信用额度列表为空");
        }
        for(GuideCreditMoney guideCreditMoney : guideCreditMoneyList){
            if(guideCreditMoney.getTempCreditLimit().compareTo(BigDecimal.ZERO)!=0){
                GuideCreditMoneyDetail guideCreditMoneyDetail = new GuideCreditMoneyDetail();
                guideCreditMoneyDetail.setEmpId(guideCreditMoney.getEmpId());
                guideCreditMoneyDetail.setOriginalCreditLimitAvailable(guideCreditMoney.getCreditLimitAvailable());
                guideCreditMoneyDetail.setCreditLimitAvailable(guideCreditMoney.getCreditLimitAvailable().subtract(guideCreditMoney.getTempCreditLimit()));
                guideCreditMoneyDetail.setOriginalTempCreditLimit(guideCreditMoney.getTempCreditLimit());
                guideCreditMoneyDetail.setTempCreditLimit(BigDecimal.ZERO);
                //得到所有存入变更额度的id
                Map<String,Long> idList  =saveAllCreditMoneyChange(guideCreditMoneyDetail);
                Long tempCreditChangeId = idList.get("tempCreditLimitId");
                Long fixedCreditChangeId = idList.get("fixedCreditLimitId");
                Long availableCreditChangId = idList.get("creditLimitAvailableId");
                //补全导购信用金变更详情VO
                GuideCreditChangeDetailVO guideCreditChangeDetailVO = new GuideCreditChangeDetailVO();
                guideCreditChangeDetailVO.setEmpId(guideCreditMoneyDetail.getEmpId());
                guideCreditChangeDetailVO.setChangeTypeDesc("后台临时额度自动清零");
                guideCreditChangeDetailVO.setAvailableCreditChangId(new GuideAvailableCreditChange(availableCreditChangId,null,null));
                guideCreditChangeDetailVO.setFixedCreditChangeId(new GuideFixedCreditChange(fixedCreditChangeId,null,null));
                guideCreditChangeDetailVO.setTempCreditChangeId(new GuideTempCreditChange(tempCreditChangeId,null,null));
                this.saveCreditChange(guideCreditChangeDetailVO);
            }
        }
        this.clearAllTempCredit();
    }


    @Override
    public void clearAllTempCredit(){
        maEmpCreditMoneyDAO.clearAllTempCredit();
    }

    @Override
    public List<GuideCreditMoney> findAllGuideCreditMoney() {
        List<GuideCreditMoney> guideCreditMoneyList = this.maEmpCreditMoneyDAO.findAllGuideCreditMoney();
        return guideCreditMoneyList;
    }
}
