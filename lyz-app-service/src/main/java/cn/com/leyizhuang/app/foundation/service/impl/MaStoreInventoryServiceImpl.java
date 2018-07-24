package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaStoreInventoryDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.store.*;
import cn.com.leyizhuang.app.foundation.service.MaStoreInventoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Service
public class MaStoreInventoryServiceImpl implements MaStoreInventoryService {

    @Resource
    private MaStoreInventoryDAO maStoreInventoryDAO;

    @Override
    public void addInventoryChangeLog(MaStoreInventoryChange storeInventoryChange) {
        maStoreInventoryDAO.addInventoryChangeLog(storeInventoryChange);
    }

    @Override
    public void addRealInventoryChangeLog(MaStoreRealInventoryChange storeRealInventoryChange) {
        maStoreInventoryDAO.addRealInventoryChangeLog(storeRealInventoryChange);
    }

    @Override
    public MaStoreInventory findStoreInventoryByStoreIdAndGoodsId(Long storeId,Long goodsId){
        return maStoreInventoryDAO.findStoreInventoryByStoreIdAndGoodsId(storeId,goodsId);
    }

    @Override
    public int updateStoreInventory(Long storeId,Long goodsId,Integer goodsQty,Date date){
        return maStoreInventoryDAO.updateStoreInventory(storeId,goodsId,goodsQty,date);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStoreInventoryAndAvailableIty(Long storeId,Long goodsId,Integer goodsQty,Integer goodsAvailableIty,Date date){
        return maStoreInventoryDAO.updateStoreInventoryAndAvailableIty(storeId,goodsId,goodsQty,goodsAvailableIty,date);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveStoreInventory(MaStoreInventory storeInventory){
         this.maStoreInventoryDAO.saveStoreInventory(storeInventory);
    }


    @Override
    public PageInfo<StoreReturnAndRequireGoodsInf> queryStoresGoodRequirePageVO(Integer page, Integer size, String structureCode, Long storeId, String queryInfo, List<Long> storeIds){
        if(null == storeIds || storeIds.size() ==0){
            return null;
        }
        if(null !=queryInfo){
            queryInfo =queryInfo.trim();
        }
        PageHelper.startPage(page, size);
        List<StoreReturnAndRequireGoodsInf>  storesGoodRequireList = maStoreInventoryDAO.queryStoresGoodRequirePageVO(structureCode,storeId,queryInfo,storeIds);
        return new PageInfo<>(storesGoodRequireList);
    }


    @Override
    public PageInfo<StoreReturnAndRequireGoodsInf> queryStoresGoodReturnPageVO(Integer page, Integer size,String structureCode, Long storeId, String queryInfo, List<Long> storeIds){
        if(null == storeIds || storeIds.size() ==0){
             return null;
        }
        if(null !=queryInfo){
            queryInfo =queryInfo.trim();
        }
        PageHelper.startPage(page, size);
        List<StoreReturnAndRequireGoodsInf>  storesGoodReturnList = maStoreInventoryDAO.queryStoresGoodReturnPageVO(structureCode,storeId,queryInfo,storeIds);
        return  new PageInfo<>(storesGoodReturnList);
    }

    @Override
    public PageInfo<StoreReturnAndRequireGoodsInf> queryStoresGoodCheckingPageVO(Integer page, Integer size,String structureCode, Long storeId, String queryInfo, List<Long> storeIds){
        if(null == storeIds || storeIds.size() ==0){
            return null;
        }
        if(null !=queryInfo){
            queryInfo =queryInfo.trim();
        }
        PageHelper.startPage(page, size);
        List<StoreReturnAndRequireGoodsInf>  storesGoodReturnList = maStoreInventoryDAO.queryStoresGoodCheckingPageVO(structureCode,storeId,queryInfo,storeIds);
        return  new PageInfo<>(storesGoodReturnList);
    }

    @Override
    public PageInfo<StoreInvoicingInf> queryInvoicingPage(Integer page, Integer size, String keywords, String structureCode, Long storeId, String endDateTime, List<Long> storeIds){
        if(null == storeIds || storeIds.size() ==0){
            return null;
        }
        if(null !=keywords){
            keywords =keywords.trim();
        }
        if(null !=endDateTime && !"".equals(endDateTime)){
            endDateTime +=" 23:59:59";
        }
        PageHelper.startPage(page, size);
        List<StoreInvoicingInf>  storesGoodReturnList = maStoreInventoryDAO.queryInvoicingPage(keywords,structureCode,storeId,endDateTime,storeIds);
        return  new PageInfo<>(storesGoodReturnList);
    }

    @Override
    public Integer queryStoreInitialrealQty(String storeCode , String sku){
        if(null ==storeCode||null==sku){
           return  null;
        }
        Integer  storeInitialrealQty = maStoreInventoryDAO.queryStoreInitialrealQty(storeCode,sku);
        return storeInitialrealQty;
    }
}
