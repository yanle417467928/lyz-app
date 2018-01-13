package cn.com.leyizhuang.app.foundation.service.impl;


import cn.com.leyizhuang.app.foundation.dao.MaStoreDAO;
import cn.com.leyizhuang.app.foundation.dto.StorePreDepositDTO;
import cn.com.leyizhuang.app.foundation.pojo.StorePreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.store.SimpleStoreParam;
import cn.com.leyizhuang.app.foundation.pojo.management.store.StoreDO;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.service.MaStorePreDepositLogService;
import cn.com.leyizhuang.app.foundation.service.MaStoreService;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorativeCompanyDetailVO;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.SimpleDecorativeCompany;
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
    public List<SimpleStoreParam> findStoreList() {
        List<SimpleStoreParam> allStoreList = this.mastoreDAO.findStoresList();
        return allStoreList;
    }


    @Override
    public List<SimpleStoreParam> findAllStorelist() {
        List<SimpleStoreParam> allStoreList = this.mastoreDAO.findAllStorelist();
        return allStoreList;
    }


    @Override
    public List<SimpleStoreParam> findAllStoresListByCityId(Long cityId) {
        List<SimpleStoreParam> storeList = this.mastoreDAO.findAllStoresListByCityId(cityId);
        return storeList;
    }

    @Override
    public List<SimpleStoreParam> findStoresListByCityId(Long cityId) {
        List<SimpleStoreParam> storeList = this.mastoreDAO.findStoresListByCityId(cityId);
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
    public List<StoreVO> findCompanyStoresList() {
        return mastoreDAO.findCompanyStoresList();
    }

    @Override
    public List<StoreVO> findCompanyStoresListByCityId(Long cityId) {
        return mastoreDAO.findCompanyStoresListByCityId(cityId);
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
    public PageInfo<StorePreDepositVO> findAllStorePredeposit(Integer page, Integer size, Long cityId, String keywords, String storeType) {
        PageHelper.startPage(page, size);
        List<StorePreDepositVO> list = this.mastoreDAO.findAllStorePredeposit(cityId, keywords, storeType);
        return new PageInfo<>(list);
    }

    @Override
    public StorePreDepositVO queryStorePredepositByStoreId(Long storeId) {
        return this.mastoreDAO.queryStorePredepositByStoreId(storeId);
    }

    @Override
    public void changeStorePredepositByStoreId(StorePreDepositDTO storePreDepositDTO) {
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

}
