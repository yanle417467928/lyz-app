package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInventoryChange;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreRealInventoryChange;

import java.util.Date;


public interface MaStoreInventoryService {

    void addInventoryChangeLog(MaStoreInventoryChange storeInventoryChange);

    void addRealInventoryChangeLog(MaStoreRealInventoryChange storeRealInventoryChange);

    MaStoreInventory findStoreInventoryByStoreIdAndGoodsId(Long storeId,Long goodsId);

    int updateStoreInventory(Long storeId,Long goodsId,Integer goodsQty,Date date);

    int updateStoreInventoryAndAvailableIty(Long storeId,Long goodsId,Integer goodsQty,Integer goodsAvailableIty,Date date);

    void saveStoreInventory(MaStoreInventory storeInventory);
}

