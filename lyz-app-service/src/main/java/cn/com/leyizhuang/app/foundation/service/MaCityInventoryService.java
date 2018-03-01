package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.management.city.MaCityInventory;
import cn.com.leyizhuang.app.foundation.pojo.management.city.MaCityInventoryChange;

import java.util.Date;

public interface MaCityInventoryService {

    void addInventoryChangeLog(MaCityInventoryChange cityInventoryChange);

    MaCityInventory findCityInventoryByCityIdAndGoodsId(Long cityId, Long goodsId);

    void updateCityInventory(Long cityId, Long goodsId, Integer goodsQty, Date date);
}

