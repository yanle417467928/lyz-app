package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.city.CityDeliveryTime;
import cn.com.leyizhuang.app.foundation.vo.CityDeliveryTimeVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface MaCityDeliveryTimeService {
    PageInfo<CityDeliveryTime> queryPage(Integer page, Integer size, Long cityId);

    Boolean judgmentTime(String startTime,String endTime,Long cityId,Long id);

    void save(CityDeliveryTimeVO cityDeliveryTimeVO);

    CityDeliveryTimeVO queryById(Long id);

    void update(CityDeliveryTimeVO cityDeliveryTimeVO);

    Boolean judgmentTime(String startTime,String endTime,Long cityId);

}
