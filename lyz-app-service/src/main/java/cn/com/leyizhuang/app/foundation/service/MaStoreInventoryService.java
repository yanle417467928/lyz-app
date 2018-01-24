package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInventoryChange;

import java.sql.Timestamp;
import java.util.Date;


public interface MaStoreInventoryService {

    void addInventoryChangeLog(MaStoreInventoryChange storeInventoryChange);

    MaStoreInventory findStoreInventoryByStoreCodeAndGoodsId(Long storeId,Long goodsId);

    void updateStoreInventory(Long storeId,Long goodsId,Integer goodsQty,Date date);
}

