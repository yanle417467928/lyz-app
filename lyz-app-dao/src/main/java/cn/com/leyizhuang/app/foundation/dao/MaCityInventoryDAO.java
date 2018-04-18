package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventory;
import cn.com.leyizhuang.app.foundation.pojo.management.city.MaCityInventory;
import cn.com.leyizhuang.app.foundation.pojo.management.city.MaCityInventoryChange;
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
public interface MaCityInventoryDAO {

    void addInventoryChangeLog(MaCityInventoryChange cityInventoryChange);

    MaCityInventory findCityInventoryByCityIdAndGoodsId(@Param(value = "cityId") Long cityId, @Param(value = "goodsId") Long goodsId);

    void updateCityInventory(@Param(value = "cityId") Long cityId, @Param(value = "goodsId") Long goodsId, @Param(value = "goodsQty") Integer goodsQty, @Param(value = "date") Date date);

    List<CityInventory> findCityInventoryListByCityIdAndSkuList(@Param(value = "cityId") Long cityId,
                                                                @Param(value = "internalCodeList") List<String> internalCodeList);
}
