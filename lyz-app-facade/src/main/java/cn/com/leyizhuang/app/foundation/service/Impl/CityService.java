package cn.com.leyizhuang.app.foundation.service.Impl;

import cn.com.leyizhuang.app.foundation.dao.CityDAO;
import cn.com.leyizhuang.app.foundation.pojo.City;
import cn.com.leyizhuang.app.foundation.service.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 城市API实现
 *
 * @author Richard
 * Created on 2017-09-21 14:25
 **/
@Service
public class CityService implements ICityService{

    @Autowired
    private CityDAO cityDAO;
    @Override
    public List<City> findAll() {
        return cityDAO.findAll();
    }
}
