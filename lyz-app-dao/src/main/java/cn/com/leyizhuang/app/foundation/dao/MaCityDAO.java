package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.city.SimpleCityParam;
import cn.com.leyizhuang.app.foundation.vo.management.city.CityDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.city.CityVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaCityDAO {

    List<CityVO> findAllCityVO();

    List<SimpleCityParam> findAllSimpleCityParam();

    CityDetailVO findCityVOById(Long cityId);
}
