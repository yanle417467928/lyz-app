package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.CityDAO;
import cn.com.leyizhuang.app.foundation.pojo.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 城市API实现
 *
 * @author Richard
 *         Created on 2017-09-21 14:25
 **/
@Service
public class CityServiceImpl implements cn.com.leyizhuang.app.foundation.service.CityService {

    @Autowired
    private CityDAO cityDAO;

    @Override
    public List<City> findAll() {
        return cityDAO.findAll();
    }

    @Override
    public City findByCityNumber(String cityNumber) {
        if (StringUtils.isNotBlank(cityNumber)) {
            return cityDAO.findByCityNumber(cityNumber);
        }
        return null;
    }

    @Override
    public City findById(Long cityId) {
        if (cityId != null) {
            return cityDAO.findById(cityId);
        }
        return null;
    }

    @Override
    public void save(City city) {
        cityDAO.save(city);
    }
}
