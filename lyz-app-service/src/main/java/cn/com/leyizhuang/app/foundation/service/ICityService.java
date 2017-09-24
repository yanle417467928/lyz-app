package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.City;

import java.util.List;

/**
 * 城市API
 *
 * @author Richard
 * Created on 2017-09-21 14:25
 **/
public interface ICityService {

    List<City> findAll();

    City findByCityNumber(String cityNumber);
}
