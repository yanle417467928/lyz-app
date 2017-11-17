package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.vo.CityVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaCityDAO {

    List<CityVO> findAllCityVO();

    CityVO findCityVOById(Long cityId);
}
