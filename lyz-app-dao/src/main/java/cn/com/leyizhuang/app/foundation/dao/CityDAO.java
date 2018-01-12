package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.management.city.CityDeliveryTime;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * lyz-app-facade用户数据仓库
 *
 * @author Richard
 * Created on 2017-09-19 11:26
 **/
@Repository
public interface CityDAO {
    List<City> findAll();

    City findByCityNumber(String cityNumber);

    City findById(Long cityId);

    void save(City city);

    int updateCityInventoryByCustomerIdAndIdentityTypeAndInventory(
            @Param("userId") Long userId, @Param("gid") Long index, @Param("qty") Integer integer);

    int updateCityInventoryByEmployeeIdAndIdentityTypeAndInventory(
            @Param("userId") Long userId, @Param("gid") Long index, @Param("qty") Integer integer);

    void updateCityInventoryByCustomerIdAndGoodsIdAndInventory(
            @Param("userId") Long userId, @Param("gid") Long index, @Param("qty") Integer integer);

    void updateCityInventoryByEmployeeIdAndGoodsIdAndInventory(
            @Param("userId") Long userId, @Param("gid") Long index, @Param("qty") Integer integer);

    void modifyCity(City city);

    void deleteCityByCode(String code);

    Boolean existGoodsCityInventory(@Param("cityId") Long cityId, @Param("gid") Long gid, @Param("qty") Integer qty);

    List<CityDeliveryTime> findCityDeliveryTimeByCityId(Long cityId);

    Integer updateCityInventoryByCityIdAndGoodsIdAndInventory(@Param(value = "cityId") Long cityId,
                                                              @Param(value = "goodsId") Long goodsId,
                                                              @Param(value = "inventory") Integer inventory,
                                                              @Param(value = "version") Timestamp version);

    CityInventory findCityInventoryByCityIdAndGoodsId(@Param(value = "cityId") Long cityId, @Param(value = "goodsId") Long goodsId);

    void addCityInventoryAvailableQtyChangeLog(CityInventoryAvailableQtyChangeLog log);

    City findCityByUserIdAndIdentityType(@Param(value = "userId") Long userId,
                                         @Param(value = "identityType") AppIdentityType identityType);
}
