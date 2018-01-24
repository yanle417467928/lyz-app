package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaCityInventoryDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.city.MaCityInventory;
import cn.com.leyizhuang.app.foundation.pojo.management.city.MaCityInventoryChange;
import cn.com.leyizhuang.app.foundation.service.MaCityInventoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;

@Service
public class MaCityInventoryServiceImpl implements MaCityInventoryService {

    @Resource
    private MaCityInventoryDAO maCityInventoryDAO;

    @Override
    public void addInventoryChangeLog(MaCityInventoryChange cityInventoryChange) {
        maCityInventoryDAO.addInventoryChangeLog(cityInventoryChange);
    }

    @Override
    public MaCityInventory findCityInventoryByCityIdAndGoodsId(Long cityId, Long goodsId){
        return maCityInventoryDAO.findCityInventoryByCityIdAndGoodsId(cityId,goodsId);
    }

    @Override
    public void updateCityInventory(Long cityId,Long goodsId,Integer goodsQty,Date date){
        maCityInventoryDAO.updateCityInventory(cityId,goodsId,goodsQty,date);
    }
}
