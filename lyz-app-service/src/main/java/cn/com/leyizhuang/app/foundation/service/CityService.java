package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.City;

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

    int lockCityInventoryByUserIdAndIdentityTypeAndInventory(Long userId, Integer identityType, Map<Long,Integer> cityInventory);

    void unlockCityInventoryByUserIdAndIdentityTypeAndInventory(Long userId, Integer identityType, Map<Long,Integer> cityInventory);
}
