package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.store.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author liuh
 * @date 2018/1/15
 */
@Repository
public interface MaStoreInventoryDAO {
    void addInventoryChangeLog(MaStoreInventoryChange storeInventoryChange);

    void addRealInventoryChangeLog(MaStoreRealInventoryChange storeRealInventoryChange);

    MaStoreInventory findStoreInventoryByStoreIdAndGoodsId(@Param(value = "storeId") Long storeId,@Param(value = "goodsId")  Long goodsId);

    int updateStoreInventory (@Param(value = "storeId")Long storeId,@Param(value = "goodsId")Long goodsId,@Param(value = "goodsQty")Integer goodsQty,@Param(value = "date")Date date);

    int updateStoreInventoryAndAvailableIty (@Param(value = "storeId")Long storeId,@Param(value = "goodsId")Long goodsId,@Param(value = "goodsQty")Integer goodsQty,@Param(value = "goodsAvailableIty")Integer goodsAvailableIty,@Param(value = "date")Date date);

    void saveStoreInventory(MaStoreInventory storeInventory);

    List<StoreReturnAndRequireGoodsInf> queryStoresGoodRequirePageVO(@Param(value = "structureCode")String structureCode,@Param(value = "storeId") Long storeId, @Param(value = "queryInfo")String queryInfo,@Param(value = "list") List<Long> storeIds);

    List<StoreReturnAndRequireGoodsInf> queryStoresGoodReturnPageVO(@Param(value = "structureCode")String structureCode, @Param(value = "storeId") Long storeId, @Param(value = "queryInfo") String queryInfo,@Param(value = "list") List<Long> storeIds);

    List<StoreReturnAndRequireGoodsInf> queryStoresGoodCheckingPageVO(@Param(value = "structureCode")String structureCode, @Param(value = "storeId") Long storeId, @Param(value = "queryInfo") String queryInfo,@Param(value = "list") List<Long> storeIds);

    List<StoreInvoicingInf> queryInvoicingPage(@Param(value = "keywords")String keywords, @Param(value = "structureCode")String structureCode, @Param(value = "storeId") Long storeId, @Param(value = "endDateTime") String endDateTime, @Param(value = "list") List<Long> storeIds);

    Integer queryStoreInitialrealQty(@Param(value = "storeCode")String storeCode , @Param(value = "sku")String sku);
}
