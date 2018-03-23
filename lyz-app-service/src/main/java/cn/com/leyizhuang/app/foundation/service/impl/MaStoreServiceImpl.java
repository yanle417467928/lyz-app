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
import cn.com.leyizhuang.app.foundation.vo.management.store.StoreDetailVO;
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
    public PageInfo<StoreVO> queryPageVO(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<StoreVO> pageStoreList = this.mastoreDAO.findAllVO();
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
    public List<SimpleStoreParam> findStoresListByCityId(Long cityId,List<Long> storeIds) {
        List<SimpleStoreParam> storeList = this.mastoreDAO.findStoresListByCityId(cityId,storeIds);
        return storeList;
    }

    @Override
    public List<SimpleStoreParam> findStoresListByCityIdExcludeStoreId(Long cityId,Long storeId) {
        List<SimpleStoreParam> storeList = this.mastoreDAO.findStoresListByCityIdExcludeStoreId(cityId,storeId);
        return storeList;
    }

    @Override
    public StoreDetailVO queryStoreVOById(Long storeId) {
        StoreDetailVO storeVO = this.mastoreDAO.findStoresVOById(storeId);
        return storeVO;
    }

    @Override
    public PageInfo<StoreVO> queryStoreListByCityId(Integer page, Integer size, Long cityId) {
        PageHelper.startPage(page, size);
        List<StoreVO> pageStoreList = this.mastoreDAO.queryStoreListByCityId(cityId);
        return new PageInfo<>(pageStoreList);
    }

    @Override
    public PageInfo<StoreVO> findStoresListByCondition(Integer page, Integer size, String enabled, Long cityId) {
        PageHelper.startPage(page, size);
        if("-1".equals(enabled)){
            enabled=null;
        }
        if(-1==cityId){
            cityId=null;
        }
        List<StoreVO> pageStoreList = this.mastoreDAO.findStoresListByCondition(enabled, cityId);
        return new PageInfo<>(pageStoreList);
    }

    @Override
    public PageInfo<StoreVO> findStoresListByStoreInfo(Integer page, Integer size, String queryStoreInfo) {
        PageHelper.startPage(page, size);
        List<StoreVO> pageStoreList = this.mastoreDAO.findStoresListByStoreInfo(queryStoreInfo);
        return new PageInfo<>(pageStoreList);
    }

    @Override
    public void update(Long storeId,Boolean  isSelfDelivery ){
        if (null != storeId && null != isSelfDelivery) {
            mastoreDAO.update(storeId,isSelfDelivery);
        }
    }


    @Override
    public PageInfo<StoreDO> queryDecorativeCompanyPageVO(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<StoreDO> pageStoreList = this.mastoreDAO.queryDecorativeCompanyPageVO();
        return new PageInfo<>(pageStoreList);
    }


    @Override
    public PageInfo<StoreDO> findDecorativeByCondition(Integer page, Integer size, String enabled, Long cityId) {
        PageHelper.startPage(page, size);
        if("-1".equals(enabled)){
            enabled=null;
        }
        if(-1==cityId){
            cityId=null;
        }
        List<StoreDO> pageStoreList = this.mastoreDAO.findDecorativeByCondition(enabled, cityId);
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
         DecorativeCompanyDetailVO decorativeCompanyVO=  DecorativeCompanyDetailVO.transform(storeDO);
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
    public DecorativeCompanyInfo queryDecorativeCompanyCreditById(Long decorativeCompanyId) {
        DecorativeCompanyInfo decorativeCompanyVO = this.mastoreDAO.findDecorativeCreditById(decorativeCompanyId);
        return decorativeCompanyVO;
    }

    @Override
    public PageInfo<DecorativeCompanyInfo> findDecorativeCreditByInfo(Integer page, Integer size, String queryDecorativeCreditInfo) {
        PageHelper.startPage(page, size);
        List<DecorativeCompanyInfo>  pageDecorativeCompanyList = this.mastoreDAO.findDecorativeCreditByInfo(queryDecorativeCreditInfo);
        return new PageInfo<>(pageDecorativeCompanyList);
    }


    @Override
    public PageInfo<DecorativeCompanyInfo> findDecorativeCreditByCondition(Integer page, Integer size, String enabled, Long cityId) {
        PageHelper.startPage(page, size);
        if("-1".equals(enabled)){
            enabled=null;
        }
        if(-1==cityId){
            cityId=null;
        }
        List<DecorativeCompanyInfo> pageDecorativeCompanyList = this.mastoreDAO.findDecorativeCreditByCondition(enabled, cityId);
        return new PageInfo<>(pageDecorativeCompanyList);
    }

    @Override
    public List<StoreVO> findDecorativeCompanyList(List<Long> storeIds) {
        return mastoreDAO.findDecorativeCompanyList(storeIds);
    }

    @Override
    public List<StoreVO> findCompanyStoresListByCityId(Long cityId,List<Long> storeIds) {
        return mastoreDAO.findCompanyStoresListByCityId(cityId,storeIds);
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
    public void changeStorePredepositByStoreId(StorePreDepositDTO storePreDepositDTO) throws Exception{
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
    public MaStoreInfo findStoreByStoreCode(String code){
       return mastoreDAO.findStoreByStoreCode(code);
    }

    @Override
    public StorePreDeposit findByStoreId(Long storeId){
        return mastoreDAO.findByStoreId(storeId);
    }

    @Override
    public Integer updateStPreDepositByUserIdAndVersion(Double money,Long userId, Date version){
        return mastoreDAO.updateStPreDepositByUserIdAndVersion(money, userId, version);
    }

    @Override
    public void saveStorePreDepositLog(StPreDepositLogDO stPreDepositLogDO){
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
    public List<SimpleStoreParam> findStoresListByStoreId(List<Long> storeIds) {
        return this.mastoreDAO.findStoresListByStoreId(storeIds);
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
    public Long findCityIdByStoreId(Long storeId) {
        return this.mastoreDAO.findCityIdByStoreId(storeId);
    }

    @Override
    public List<SimpleStoreParam> findStoresListByCityIdAndStoreType(Long cityId, String storeType, List<Long> storeIds) {
        return this.mastoreDAO.findStoresListByCityIdAndStoreType(cityId, storeType, storeIds);
    }

}
