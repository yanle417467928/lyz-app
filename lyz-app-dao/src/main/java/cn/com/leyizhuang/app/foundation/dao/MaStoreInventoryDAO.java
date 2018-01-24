package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInventoryChange;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author liuh
 * @date 2018/1/15
 */
@Repository
public interface MaStoreInventoryDAO {
    void addInventoryChangeLog(MaStoreInventoryChange storeInventoryChange);

    MaStoreInventory findStoreInventoryByStoreCodeAndGoodsId(@Param(value = "storeId") Long storeId,@Param(value = "goodsId")  Long goodsId);

    void updateStoreInventory (@Param(value = "storeId")Long storeId,@Param(value = "goodsId")Long goodsId,@Param(value = "goodsQty")Integer goodsQty,@Param(value = "date")Date date);
}
