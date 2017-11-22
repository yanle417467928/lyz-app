package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.foundation.vo.CityVO;
import com.github.pagehelper.PageInfo;

import java.util.List;


public interface MaCityService {

    PageInfo<CityVO> queryPageVO(Integer page, Integer size);

    CityVO queryCityVOById(Long cityId);

    List<CityVO> findCitysList();
}
