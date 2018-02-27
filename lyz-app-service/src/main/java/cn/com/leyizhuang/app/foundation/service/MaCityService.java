package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.foundation.pojo.management.city.SimpleCityParam;
import cn.com.leyizhuang.app.foundation.vo.management.city.CityDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.city.CityVO;
import com.github.pagehelper.PageInfo;

import java.util.List;


public interface MaCityService {

    PageInfo<CityVO> queryPageVO(Integer page, Integer size);

    PageInfo<CityVO> queryDeliveryTimePageVO(Integer page, Integer size);

    CityDetailVO queryCityVOById(Long cityId);

    List<SimpleCityParam> findCitysList();

    PageInfo<CityVO> queryPageVOByEnableIsTrue(Integer page, Integer size, String keywords);
}
