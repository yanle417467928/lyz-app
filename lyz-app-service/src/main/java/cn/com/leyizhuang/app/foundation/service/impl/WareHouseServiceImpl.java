package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.WareHouseDAO;
import cn.com.leyizhuang.app.foundation.pojo.WareHouseDO;
import cn.com.leyizhuang.app.foundation.service.WareHouseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Jerry.Ren
 * create 2018-02-22 17:05
 * desc:
 **/
@Service
public class WareHouseServiceImpl implements WareHouseService {

    @Resource
    private WareHouseDAO wareHouseDAO;

    @Override
    public WareHouseDO findByWareHouseNo(String whNo) {

        if (StringUtils.isNotBlank(whNo)) {
            return wareHouseDAO.findByWareHouseNo(whNo);
        }
        return null;
    }

    @Override
    public List<WareHouseDO> findWareHouseByCityId(Long cityId) {
        return this.wareHouseDAO.findWareHouseByCityId(cityId);
    }
}
