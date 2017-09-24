package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.City;
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
}
