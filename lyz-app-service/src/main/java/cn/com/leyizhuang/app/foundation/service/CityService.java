package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.management.city.CityDeliveryTime;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 城市API
 *
 * @author Richard
 * Created on 2017-09-21 14:25
 **/
public interface CityService {

    List<City> findAll();

    City findByCityNumber(String cityNumber);

    City findById(Long cityId);

    void save(City city);

    int lockCityInventoryByUserIdAndIdentityTypeAndInventory(Long userId, Integer identityType, Map<Long, Integer> cityInventory);

    void unlockCityInventoryByUserIdAndIdentityTypeAndInventory(Long userId, Integer identityType, Map<Long, Integer> cityInventory);

    void updateCityInventoryByCusIdAndGoodsIdAndGoodsQty(Long userId,Long gid,Integer goodsQty);

    void updateCityInventoryByEmpIdAndGoodsIdAndGoodsQty(Long userId,Long gid,Integer goodsQty);

    void modifyCity(City city);

    void deleteCityByCode(String code);

    List<CityDeliveryTime> findCityDeliveryTimeByCityId(Long cityId);

    Integer lockCityInventoryByCityIdAndGoodsIdAndInventory(Long cityId, Long goodsId, Integer inventory, Timestamp version);

    CityInventory findCityInventoryByCityIdAndGoodsId(Long cityId, Long goodsId);

    void addCityInventoryAvailableQtyChangeLog(CityInventoryAvailableQtyChangeLog log);

    City findCityByUserIdAndIdentityType(Long userId, AppIdentityType appIdentityType);

    Integer updateCityInventoryByCustomerIdAndGoodsIdAndInventoryAndVersion(Long userId, Long index, Integer integer, Date version);

    Integer updateCityInventoryByEmployeeIdAndGoodsIdAndInventoryAndVersion(Long userId, Long index, Integer integer, Date version);

    CityInventory findCityInventoryByCityCodeAndSku(String cityCode, String sku);

    Integer lockCityInventoryByCityCodeAndSkuAndInventory(String cityCode, String sku, Integer inventory, Timestamp lastUpdateTime);
}
