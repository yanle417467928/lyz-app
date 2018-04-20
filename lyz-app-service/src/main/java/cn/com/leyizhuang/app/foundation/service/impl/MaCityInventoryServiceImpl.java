package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaCityInventoryDAO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventory;
import cn.com.leyizhuang.app.foundation.pojo.management.city.MaCityInventory;
import cn.com.leyizhuang.app.foundation.pojo.management.city.MaCityInventoryChange;
import cn.com.leyizhuang.app.foundation.service.MaCityInventoryService;
import cn.com.leyizhuang.common.util.AssertUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class MaCityInventoryServiceImpl implements MaCityInventoryService {

    @Resource
    private MaCityInventoryDAO maCityInventoryDAO;

    @Override
    public void addInventoryChangeLog(MaCityInventoryChange cityInventoryChange) {
        maCityInventoryDAO.addInventoryChangeLog(cityInventoryChange);
    }

    @Override
    public MaCityInventory findCityInventoryByCityIdAndGoodsId(Long cityId, Long goodsId) {
        return maCityInventoryDAO.findCityInventoryByCityIdAndGoodsId(cityId, goodsId);
    }

    @Override
    public void updateCityInventory(Long cityId, Long goodsId, Integer goodsQty, Date date) {
        maCityInventoryDAO.updateCityInventory(cityId, goodsId, goodsQty, date);
    }

    @Override
    public List<CityInventory> findCityInventoryListByCityIdAndSkuList(Long cityId, List<String> internalCodeList) {
        if (null != cityId && AssertUtil.isNotEmpty(internalCodeList)) {
            return maCityInventoryDAO.findCityInventoryListByCityIdAndSkuList(cityId, internalCodeList);
        }
        return null;
    }
}
