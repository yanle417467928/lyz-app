package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.city.CityDeliveryTime;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventory;

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

    void modifyCity(City city);

    void deleteCityByCode(String code);

    List<CityDeliveryTime> findCityDeliveryTimeByCityId(Long cityId);

    Integer lockCityInventoryByCityIdAndGoodsIdAndInventory(Long cityId, Long goodsId, Integer inventory, Date version);

    CityInventory findCityInventoryByCityIdAndGoodsId(Long cityId, Long goodsId);
}
