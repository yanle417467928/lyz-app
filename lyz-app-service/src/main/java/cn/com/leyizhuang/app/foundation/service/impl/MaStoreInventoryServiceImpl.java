package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaStoreInventoryDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInventoryChange;
import cn.com.leyizhuang.app.foundation.service.MaStoreInventoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
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
    public MaStoreInventory findStoreInventoryByStoreCodeAndGoodsId(Long storeId,Long goodsId){
        return maStoreInventoryDAO.findStoreInventoryByStoreCodeAndGoodsId(storeId,goodsId);
    }

    @Override
    public void updateStoreInventory(Long storeId,Long goodsId,Integer goodsQty,Date date){
        maStoreInventoryDAO.updateStoreInventory(storeId,goodsId,goodsQty,date);
    }
}
