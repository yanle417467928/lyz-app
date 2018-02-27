package cn.com.leyizhuang.app.foundation.service.impl;


import cn.com.leyizhuang.app.foundation.dao.MaCityDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.city.SimpleCityParam;
import cn.com.leyizhuang.app.foundation.service.MaCityService;
import cn.com.leyizhuang.app.foundation.vo.management.city.CityDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.city.CityVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * 城市API实现
 *
 * @author Richard
 * Created on 2017-09-21 14:25
 **/
@Service
public class MaCityServiceImpl implements MaCityService {

    @Resource
    private MaCityDAO maCityDAO;

    @Override
    public PageInfo<CityVO> queryPageVO(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<CityVO> cityList = maCityDAO.findAllCityVO();
        return new PageInfo<>(cityList);
    }

    @Override
    public List<SimpleCityParam> findCitysList() {
        List<SimpleCityParam> cityList = maCityDAO.findAllSimpleCityParam();
        return cityList;
    }

    @Override
    public CityDetailVO queryCityVOById(Long cityId) {
        if (cityId != null) {
            return maCityDAO.findCityVOById(cityId);
        }
        return null;
    }

    @Override
    public PageInfo<CityVO> queryPageVOByEnableIsTrue(Integer page, Integer size, String keywords) {
        PageHelper.startPage(page, size);
        List<CityVO> cityList = maCityDAO.findAllCityByEnableIsTrue(keywords);
        return new PageInfo<>(cityList);
    }
}
