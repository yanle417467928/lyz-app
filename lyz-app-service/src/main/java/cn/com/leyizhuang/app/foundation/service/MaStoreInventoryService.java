package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInventoryChange;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreRealInventoryChange;
import cn.com.leyizhuang.app.foundation.pojo.management.store.StoreReturnAndRequireGoodsInf;
import com.github.pagehelper.PageInfo;

import java.util.Date;
import java.util.List;


public interface MaStoreInventoryService {

    void addInventoryChangeLog(MaStoreInventoryChange storeInventoryChange);

    void addRealInventoryChangeLog(MaStoreRealInventoryChange storeRealInventoryChange);

    MaStoreInventory findStoreInventoryByStoreIdAndGoodsId(Long storeId,Long goodsId);

    int updateStoreInventory(Long storeId,Long goodsId,Integer goodsQty,Date date);

    int updateStoreInventoryAndAvailableIty(Long storeId,Long goodsId,Integer goodsQty,Integer goodsAvailableIty,Date date);

    void saveStoreInventory(MaStoreInventory storeInventory);

    PageInfo<StoreReturnAndRequireGoodsInf> queryStoresGoodRequirePageVO(Integer page, Integer size, String structureCode, Long storeId, String queryInfo, List<Long> storeIds);

    PageInfo<StoreReturnAndRequireGoodsInf> queryStoresGoodReturnPageVO(Integer page, Integer size,String structureCode, Long storeId, String queryInfo, List<Long> storeIds);
}

