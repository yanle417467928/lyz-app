package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaCityAvailableItyDAO;
import cn.com.leyizhuang.app.foundation.service.MaCityAvailableItyService;
import cn.com.leyizhuang.app.foundation.vo.management.city.CityInventoryVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2017/12/28.
 * Time: 13:45.
 */
@Service
public class MaCityAvailableItyServiceImpl implements MaCityAvailableItyService {

    @Resource
    private MaCityAvailableItyDAO maCityAvailableItyDAO;

    @Override
    public PageInfo<CityInventoryVO> queryPageVO(Integer page, Integer size, String keywords,Long cityId) {
        PageHelper.startPage(page, size);
        List<CityInventoryVO> cityList = maCityAvailableItyDAO.findCityInventoryList(keywords,cityId);
        return new PageInfo<>(cityList);
    }
}
