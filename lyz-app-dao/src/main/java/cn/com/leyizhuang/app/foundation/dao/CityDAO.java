package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.City;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
            @Param("userId") Long userId,@Param("gid") Long index,@Param("qty") Integer integer);

    int updateCityInventoryByEmployeeIdAndIdentityTypeAndInventory(
            @Param("userId") Long userId,@Param("gid") Long index,@Param("qty") Integer integer);

    void updateCityInventoryByCustomerIdAndGoodsIdAndInventory(
            @Param("userId") Long userId,@Param("gid") Long index,@Param("qty") Integer integer);

    void updateCityInventoryByEmployeeIdAndGoodsIdAndInventory(
            @Param("userId") Long userId,@Param("gid") Long index,@Param("qty") Integer integer);
}
