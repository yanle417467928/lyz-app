package cn.com.leyizhuang.app.foundation.service.impl;


import cn.com.leyizhuang.app.foundation.dao.MaStoreDAO;
import cn.com.leyizhuang.app.foundation.pojo.decorativeCompany.DecorativeCompanyInfo;
import cn.com.leyizhuang.app.foundation.pojo.store.StoreDO;
import cn.com.leyizhuang.app.foundation.service.MaStoreService;
import cn.com.leyizhuang.app.foundation.vo.DecorativeCompanyVO;
import cn.com.leyizhuang.app.foundation.vo.StoreVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MaStoreServiceImpl implements MaStoreService {

    @Autowired
    private MaStoreDAO mastoreDAO;

    @Override
    public PageInfo<StoreVO> queryPageVO(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<StoreVO> pageStoreList = this.mastoreDAO.findAllVO();
        return new PageInfo<>(pageStoreList);
    }

    @Override
    public List<StoreVO> findStoreList() {
        List<StoreVO> allStoreList = this.mastoreDAO.findStoresList();
        return allStoreList;
    }

    @Override
    public List<StoreVO> findStoresListByCityId(Long cityId) {
        List<StoreVO> storeList = this.mastoreDAO.findStoresListByCityId(cityId);
        return storeList;
    }

    @Override
    public StoreVO queryStoreVOById(Long storeId) {
        StoreVO storeVO = this.mastoreDAO.findStoresVOById(storeId);
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
     public DecorativeCompanyVO queryDecorativeCompanyById(Long decorativeCompanyId) {
        StoreDO storeDO = this.mastoreDAO.findDecorativeById(decorativeCompanyId);
        DecorativeCompanyVO decorativeCompanyVO=  DecorativeCompanyVO.transform(storeDO);
        return decorativeCompanyVO;
    }

    @Override
    public List<DecorativeCompanyVO> findDecorativeCompanyVOList() {
        List<StoreDO> decorativeCompanyDOList = this.mastoreDAO.queryDecorativeCompanyPageVO();
        List<DecorativeCompanyVO> decorativeCompanyVOList= DecorativeCompanyVO.transform(decorativeCompanyDOList);
        return decorativeCompanyVOList;
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

}
