package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaStoreInventoryDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInventoryChange;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreRealInventoryChange;
import cn.com.leyizhuang.app.foundation.service.MaStoreInventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;


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
}
