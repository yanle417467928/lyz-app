package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.management.store.*;
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

    PageInfo<StoreInvoicingInf> queryInvoicingPage(Integer page, Integer size, String keywords, String structureCode, Long storeId, String endDateTime, List<Long> storeIds);

    List<StoreInvoicingInf> queryInvoicingList(String keywords, String structureCode, Long storeId, String endDateTime, List<Long> storeIds);

    Integer queryStoreInitialrealQty(String storeCode , String sku);

    PageInfo<StoreReturnAndRequireGoodsInf> queryStoresGoodCheckingPageVO(Integer page, Integer size,String structureCode, Long storeId, String queryInfo, List<Long> storeIds);
}

