package cn.com.leyizhuang.app.foundation.service.impl;


import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.foundation.dao.MaStoreDAO;
import cn.com.leyizhuang.app.foundation.dto.StorePreDepositDTO;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.StPreDepositLogDO;
import cn.com.leyizhuang.app.foundation.pojo.StorePreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.SimpleDecorativeCompany;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.store.SimpleStoreParam;
import cn.com.leyizhuang.app.foundation.pojo.management.store.StoreDO;
import cn.com.leyizhuang.app.foundation.service.MaStorePreDepositLogService;
import cn.com.leyizhuang.app.foundation.service.MaStoreService;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorativeCompanyDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.FitCreditMoneyChangeLogVO;
import cn.com.leyizhuang.app.foundation.vo.management.store.StoreDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.store.StorePreDepositLogVO;
import cn.com.leyizhuang.app.foundation.vo.management.store.StorePreDepositVO;
import cn.com.leyizhuang.app.foundation.vo.management.store.StoreVO;
import cn.com.leyizhuang.common.core.exception.AppConcurrentExcp;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MaStoreServiceImpl implements MaStoreService {

    @Autowired
    private MaStoreDAO mastoreDAO;

    @Autowired
    private MaStorePreDepositLogService maStorePreDepositLogService;

    @Override
    public PageInfo<StoreVO> queryPageVO(Integer page, Integer size, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<StoreVO> pageStoreList = this.mastoreDAO.findAllVO(storeIds);
        return new PageInfo<>(pageStoreList);
    }

    @Override
    public List<SimpleStoreParam> findStoreList(List<Long> storeIds) {
        List<SimpleStoreParam> allStoreList = this.mastoreDAO.findStoresList(storeIds);
        return allStoreList;
    }


    @Override
    public List<SimpleStoreParam> findAllStorelist(List<Long> storeIds) {
        List<SimpleStoreParam> allStoreList = this.mastoreDAO.findAllStorelist(storeIds);
        return allStoreList;
    }


    @Override
    public List<SimpleStoreParam> findAllStoresListByCityId(Long cityId) {
        List<SimpleStoreParam> storeList = this.mastoreDAO.findAllStoresListByCityId(cityId);
        return storeList;
    }

    @Override
    public List<SimpleStoreParam> findStoresListByCityId(Long cityId, List<Long> storeIds) {
        List<SimpleStoreParam> storeList = this.mastoreDAO.findStoresListByCityId(cityId, storeIds);
        return storeList;
    }

    @Override
    public List<SimpleStoreParam> findStoresListByCityIdExcludeStoreId(Long storeId) {
        List<SimpleStoreParam> storeList = this.mastoreDAO.findStoresListByCityIdExcludeStoreId(storeId);
        return storeList;
    }

    @Override
    public StoreDetailVO queryStoreVOById(Long storeId) {
        StoreDetailVO storeVO = this.mastoreDAO.findStoresVOById(storeId);
        return storeVO;
    }

    @Override
    public PageInfo<StoreVO> queryStoreListByCityId(Integer page, Integer size, Long cityId, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<StoreVO> pageStoreList = this.mastoreDAO.queryStoreListByCityId(cityId, storeIds);
        return new PageInfo<>(pageStoreList);
    }

    @Override
    public PageInfo<StoreVO> findStoresListByCondition(Integer page, Integer size, String enabled, Long cityId, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        if ("-1".equals(enabled)) {
            enabled = null;
        }
        if (-1 == cityId) {
            cityId = null;
        }
        List<StoreVO> pageStoreList = this.mastoreDAO.findStoresListByCondition(enabled, cityId, storeIds);
        return new PageInfo<>(pageStoreList);
    }

    @Override
    public PageInfo<StoreVO> findStoresListByStoreInfo(Integer page, Integer size, String queryStoreInfo, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<StoreVO> pageStoreList = this.mastoreDAO.findStoresListByStoreInfo(queryStoreInfo, storeIds);
        return new PageInfo<>(pageStoreList);
    }

    @Override
    public void update(Long storeId, Boolean isSelfDelivery) {
        if (null != storeId && null != isSelfDelivery) {
            mastoreDAO.update(storeId, isSelfDelivery);
        }
    }


    @Override
    public PageInfo<StoreDO> queryDecorativeCompanyPageVO(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<StoreDO> pageStoreList = this.mastoreDAO.queryDecorativeCompanyPageVO();
        return new PageInfo<>(pageStoreList);
    }

    @Override
    public PageInfo<StoreDO> queryDecorativeCompanyList(Integer page, Integer size,List<Long> storeIds,Long cityId,String keywords, String fitCompayType) {
        PageHelper.startPage(page, size);
        List<StoreDO> pageStoreList = this.mastoreDAO.queryDecorativeCompanyList(storeIds,cityId,keywords, fitCompayType);
        return new PageInfo<>(pageStoreList);
    }


    @Override
    public PageInfo<StoreDO> findDecorativeByCondition(Integer page, Integer size, String enabled, Long cityId,String company) {
        PageHelper.startPage(page, size);
        if ("-1".equals(enabled)) {
            enabled = null;
        }
        if (-1 == cityId) {
            cityId = null;
        }
        List<StoreDO> pageStoreList = this.mastoreDAO.findDecorativeByCondition(enabled, cityId,company);
        return new PageInfo<>(pageStoreList);
    }


    @Override
    public PageInfo<StoreDO> findDecorativeByInfo(Integer page, Integer size, String queryDecorativeInfo) {
        PageHelper.startPage(page, size);
        List<StoreDO> pageStoreList = this.mastoreDAO.findDecorativeByInfo(queryDecorativeInfo);
        return new PageInfo<>(pageStoreList);
    }

    @Override
    public DecorativeCompanyDetailVO queryDecorativeCompanyById(Long decorativeCompanyId) {
        StoreDO storeDO = this.mastoreDAO.findDecorativeById(decorativeCompanyId);
        DecorativeCompanyDetailVO decorativeCompanyVO = DecorativeCompanyDetailVO.transform(storeDO);
        return decorativeCompanyVO;
    }

    @Override
    public List<SimpleDecorativeCompany> findDecorativeCompanyVOList() {
        List<SimpleDecorativeCompany> decorativeCompanyDOList = this.mastoreDAO.queryDecorativeCompanyVOList();
        return decorativeCompanyDOList;
    }

    @Override
    public PageInfo<DecorativeCompanyInfo> queryDecorativeCreditPageVO(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<DecorativeCompanyInfo> pageDecorativeCompanyList = this.mastoreDAO.findDecorativeCreditList();
        return new PageInfo<>(pageDecorativeCompanyList);
    }

    @Override
    public PageInfo<FitCreditMoneyChangeLogVO> queryDecorativeCreditChangePage(Integer page, Integer size, String keywords, String changeType, Long storeId) {
        PageHelper.startPage(page, size);
        if (null == storeId) {
            return null;
        }
        List<FitCreditMoneyChangeLogVO> pageDecorativeCompanyChangeList = this.mastoreDAO.queryDecorativeCreditChangePage(keywords, changeType, storeId);
        return new PageInfo<>(pageDecorativeCompanyChangeList);
    }

    @Override
    public DecorativeCompanyInfo queryDecorativeCompanyCreditById(Long decorativeCompanyId) {
        DecorativeCompanyInfo decorativeCompanyVO = this.mastoreDAO.findDecorativeCreditById(decorativeCompanyId);
        return decorativeCompanyVO;
    }

    @Override
    public PageInfo<DecorativeCompanyInfo> findDecorativeCreditByInfo(Integer page, Integer size, String queryDecorativeCreditInfo) {
        PageHelper.startPage(page, size);
        List<DecorativeCompanyInfo> pageDecorativeCompanyList = this.mastoreDAO.findDecorativeCreditByInfo(queryDecorativeCreditInfo);
        return new PageInfo<>(pageDecorativeCompanyList);
    }


    @Override
    public PageInfo<DecorativeCompanyInfo> findDecorativeCreditByCondition(Integer page, Integer size, String enabled, Long cityId) {
        PageHelper.startPage(page, size);
        if ("-1".equals(enabled)) {
            enabled = null;
        }
        if (-1 == cityId) {
            cityId = null;
        }
        List<DecorativeCompanyInfo> pageDecorativeCompanyList = this.mastoreDAO.findDecorativeCreditByCondition(enabled, cityId);
        return new PageInfo<>(pageDecorativeCompanyList);
    }

    @Override
    public List<StoreVO> findDecorativeCompanyList(List<Long> storeIds) {
        return mastoreDAO.findDecorativeCompanyList(storeIds);
    }

    @Override
    public List<StoreVO> findCompanyStoresListByCityId(Long cityId, List<Long> storeIds) {
        return mastoreDAO.findCompanyStoresListByCityId(cityId, storeIds);
    }

    @Override
    public List<StoreVO> findSelfDeliveryStoresListByCityId(Long cityId) {
        return mastoreDAO.findSelfDeliveryStoresListByCityId(cityId);
    }

    @Override
    public List<StoreVO> findSelfDeliveryStoresList() {
        return mastoreDAO.findSelfDeliveryStoresList();
    }

    @Override
    public PageInfo<StorePreDepositVO> findAllStorePredeposit(Integer page, Integer size, Long cityId, String keywords, String storeType, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<StorePreDepositVO> list = this.mastoreDAO.findAllStorePredeposit(cityId, keywords, storeType, storeIds);
        return new PageInfo<>(list);
    }

    @Override
    public StorePreDepositVO queryStorePredepositByStoreId(Long storeId) {
        return this.mastoreDAO.queryStorePredepositByStoreId(storeId);
    }

    @Override
    public void changeStorePredepositByStoreId(StorePreDepositDTO storePreDepositDTO) throws Exception {
        Long storeId = storePreDepositDTO.getStoreId();
        Double money = storePreDepositDTO.getChangeMoney();
        StorePreDeposit storePreDeposit = this.mastoreDAO.findByStoreId(storeId);
        if (null == storePreDeposit) {
            storePreDeposit = new StorePreDeposit();
            storePreDeposit.setBalance(money);
            storePreDeposit.setStoreId(storeId);
            storePreDeposit.setCreateTime(new Date());
            storePreDeposit.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
            this.mastoreDAO.savePreDeposit(storePreDeposit);
        } else {
            int row = this.mastoreDAO.updateDepositByStoreId(storeId, money, new Timestamp(System.currentTimeMillis()), storePreDeposit.getLastUpdateTime());
            if (1 != row) {
                throw new AppConcurrentExcp("账号余额信息过期！");
            }
        }
        this.maStorePreDepositLogService.save(storePreDepositDTO);
    }

    @Override
    public MaStoreInfo findStoreByStoreCode(String code) {
        return mastoreDAO.findStoreByStoreCode(code);
    }

    @Override
    public StorePreDeposit findByStoreId(Long storeId) {
        return mastoreDAO.findByStoreId(storeId);
    }

    @Override
    public Integer updateStPreDepositByUserIdAndVersion(Double money, Long userId, Date version) {
        return mastoreDAO.updateStPreDepositByUserIdAndVersion(money, userId, version);
    }

    @Override
    public void saveStorePreDepositLog(StPreDepositLogDO stPreDepositLogDO) {
        this.mastoreDAO.saveStorePreDepositLog(stPreDepositLogDO);
    }

    @Override
    public PageInfo<StoreVO> queryPageVO(Integer page, Integer size, Long cityId, String keywords, String storeType) {
        PageHelper.startPage(page, size);
        List<StoreVO> pageStoreList = this.mastoreDAO.findAllStoreVO(cityId, keywords, storeType);
        return new PageInfo<>(pageStoreList);
    }

    @Override
    public List<SimpleStoreParam> findStoresListByCityIdAndStoreId(Long cityId, List<Long> storeIds) {
        List<SimpleStoreParam> storeList = this.mastoreDAO.findStoresListByCityIdAndStoreId(cityId, storeIds);
        return storeList;
    }

    @Override
    public List<SimpleStoreParam> findSmallFitAndStoresListByCityIdAndStoreId(Long cityId, List<Long> storeIds) {
        List<SimpleStoreParam> storeList = this.mastoreDAO.findSmallFitAndStoresListByCityIdAndStoreId(cityId, storeIds);
        return storeList;
    }

    @Override
    public List<SimpleStoreParam> findStoresListByCityIdAndStoreIdList(Long cityId, List<Long> storeIds) {
        List<SimpleStoreParam> storeList = this.mastoreDAO.findStoresListByCityIdAndStoreIdList(cityId, storeIds);
        return storeList;
    }


    @Override
    public List<SimpleStoreParam> findSmallFitStoresListByStoreIdNotBillRule(List<Long> storeIds) {
        List<SimpleStoreParam> storeList = this.mastoreDAO.findSmallFitStoresListByStoreIdNotBillRule(storeIds);
        return storeList;
    }

    @Override
    public List<SimpleStoreParam> findStoresListByStoreId(List<Long> storeIds) {
        return this.mastoreDAO.findStoresListByStoreId(storeIds);
    }

    @Override
    public List<SimpleStoreParam> findFitAndStoresListByStoreId(Long cityId,String storeType,List<Long> storeIds) {
        return this.mastoreDAO.findFitAndStoresListByStoreId(cityId,storeType,storeIds);
    }

    @Override
    public AppStore findAppStoreByStoreId(Long storeId) {
        return this.mastoreDAO.findAppStoreByStoreId(storeId);
    }

    @Override
    public List<SimpleStoreParam> findStoresListByStoreIdAndStoreType(List<Long> storeIds, List<StoreType> storeTypes) {
        return this.mastoreDAO.findStoresListByStoreIdAndStoreType(storeIds, storeTypes);
    }

    @Override
    public List<SimpleStoreParam> findZSStoresListByCityIdAndStoreType(Long cityId,String storeType,List<Long> storeIds) {
        return this.mastoreDAO.findZSStoresListByCityIdAndStoreType(cityId, storeType,storeIds);
    }

    @Override
    public List<SimpleStoreParam> findSmallFitStoresListByStoreId(List<Long> storeIds) {
        return this.mastoreDAO.findSmallFitStoresListByStoreId(storeIds);
    }

    @Override
    public Long findCityIdByStoreId(Long storeId) {
        return this.mastoreDAO.findCityIdByStoreId(storeId);
    }

    @Override
    public List<SimpleStoreParam> findStoresListByCityIdAndStoreType(Long cityId, String storeType, List<Long> storeIds) {
        return this.mastoreDAO.findStoresListByCityIdAndStoreType(cityId, storeType, storeIds);
    }

    @Override
    public List<SimpleStoreParam> findStoresListByCityIdAndStoreType(Long cityId, String storeType) {
        if (null != cityId && null != storeType) {
            return this.mastoreDAO.findAllStoresListByCityIdAndStoreType(cityId, storeType);
        }
        return null;
    }

    @Override
    public List<Long> findStoresIdByStructureCode(String structureCode) {
        if ("-1".equals(structureCode)) {
            structureCode = null;
        }
        return this.mastoreDAO.findStoresIdByStructureCode(structureCode);
    }

    @Override
    public List<Long> findStoresIdByStructureCodeAndStoreType(String structureCode, String storeType) {
        if ("-1".equals(structureCode)) {
            structureCode = null;
        }
        if ("-1".equals(storeType)) {
            storeType = null;
        }
        if("JZSYBM".equals(structureCode)){
            structureCode="|JZC001|";
        }
        return this.mastoreDAO.findStoresIdByStructureCodeAndStoreType(structureCode, storeType);
    }

    @Override
    public List<Long> findFitCompanyIdByStoreId(List<Long> storeIds) {
        if(null !=storeIds && storeIds.size()>0){
            return this.mastoreDAO.findFitCompanyIdByStoreId(storeIds);
        }else{
            return  null;
        }
    }


    @Override
    public Boolean exsitStoreInCompany(Long storeId, String companyCode, String storeType) {
        if (null == storeId || null == companyCode || null == storeType) {
            return false;
        }
        return this.mastoreDAO.exsitStoreInCompany(storeId, companyCode, storeType);
    }

    @Override
    public List<Long> findFitCompanyIdBySellerId(Long id) {
        return this.mastoreDAO.findFitCompanyIdBySellerId(id);
    }


    @Override
    public List<SimpleStoreParam> findStoresListByCompanyCodeAndStoreType(String companyCode, String storeType, List<Long> storeIds) {
        if ("-1".equals(companyCode)) {
            companyCode = null;
        }
        if ("-1".equals(storeType)) {
            storeType = null;
        }
        if("JZSYBM".equals(companyCode)){
            companyCode="|JZC001|";
        }
        return this.mastoreDAO.findStoresListByCompanyCodeAndStoreType(companyCode, storeType, storeIds);
    }

    @Override
    public List<SimpleStoreParam> findStoresListByCompanyCodeAndStoreTypeForSale(String companyCode, String storeType, List<Long> storeIds) {
        if ("-1".equals(companyCode)) {
            companyCode = null;
        }
        if ("-1".equals(storeType)) {
            storeType = null;
        }
        if("JZSYBM".equals(companyCode)){
            companyCode="|JZC001|";
        }
        return this.mastoreDAO.findStoresListByCompanyCodeAndStoreTypeForSale(companyCode, storeType, storeIds);
    }

    @Override
    public FitCreditMoneyChangeLogVO queryLastDecorativeCreditChange(Long storeId){
        if (storeId == null){
            return null;
        }

        return mastoreDAO.queryLastDecorativeCreditChange(storeId);
    }

    @Override
    public StorePreDepositLogVO queryLastStoreChange(Long storeId){
        if (storeId == null){
            return null;
        }

        return mastoreDAO.queryLastStoreChange(storeId);
    }

}
