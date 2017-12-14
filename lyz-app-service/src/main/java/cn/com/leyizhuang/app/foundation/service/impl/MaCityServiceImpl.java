package cn.com.leyizhuang.app.foundation.service.impl;


import cn.com.leyizhuang.app.foundation.dao.MaCityDAO;
import cn.com.leyizhuang.app.foundation.service.MaCityService;
import cn.com.leyizhuang.app.foundation.vo.CityVO;
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
@Transactional
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
    public List<CityVO> findCitysList() {
        List<CityVO> cityList = maCityDAO.findAllCityVO();
        return cityList;
    }

    @Override
    public CityVO queryCityVOById(Long cityId) {
        if (cityId != null) {
            return maCityDAO.findCityVOById(cityId);
        }
        return null;
    }
}
