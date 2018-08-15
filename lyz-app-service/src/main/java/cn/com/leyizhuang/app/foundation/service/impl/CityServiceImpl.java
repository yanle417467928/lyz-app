package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.CityDAO;
import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.management.city.CityDeliveryTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 城市API实现
 *
 * @author Richard
 * Created on 2017-09-21 14:25
 **/
@Service
public class CityServiceImpl implements cn.com.leyizhuang.app.foundation.service.CityService {

    @Resource
    private CityDAO cityDAO;

    @Override
    public List<City> findAll() {
        return cityDAO.findAll();
    }

    @Override
    public City findByCityNumber(String cityNumber) {
        if (StringUtils.isNotBlank(cityNumber)) {
            return cityDAO.findByCityNumber(cityNumber);
        }
        return null;
    }

    @Override
    public City findById(Long cityId) {
        if (cityId != null) {
            return cityDAO.findById(cityId);
        }
        return null;
    }

    @Override
    public void save(City city) {
        cityDAO.save(city);
    }

    @Override
//    @Transactional
    public int lockCityInventoryByUserIdAndIdentityTypeAndInventory(Long userId, Integer identityType, Map<Long, Integer> cityInventory) {
        if (null != userId && !cityInventory.isEmpty()) {
            if (identityType == 6) {
                for (Long index : cityInventory.keySet()) {
                    int result = cityDAO.updateCityInventoryByCustomerIdAndIdentityTypeAndInventory(userId, index, cityInventory.get(index));
                    if (result == 0) {
                        return 0;
                    }
                }
            }
            if (identityType == 2) {
                for (Long index : cityInventory.keySet()) {
                    int result = cityDAO.updateCityInventoryByEmployeeIdAndIdentityTypeAndInventory(userId, index, cityInventory.get(index));
                    if (result == 0) {
                        return 0;
                    }
                }
            }
        }
        return 1;
    }

    @Override
//    @Transactional
    public void unlockCityInventoryByUserIdAndIdentityTypeAndInventory(Long userId, Integer identityType, Map<Long, Integer> cityInventory) {
        if (null != userId && !cityInventory.isEmpty()) {
            if (identityType == 6) {
                for (Long index : cityInventory.keySet()) {
                    cityDAO.updateCityInventoryByCustomerIdAndGoodsIdAndInventory(userId, index, cityInventory.get(index));
                }
            }
            if (identityType == 2) {
                for (Long index : cityInventory.keySet()) {
                    cityDAO.updateCityInventoryByEmployeeIdAndGoodsIdAndInventory(userId, index, cityInventory.get(index));
                }
            }
        }
    }

    @Override
    @Transactional
    public void updateCityInventoryByCusIdAndGoodsIdAndGoodsQty(Long userId, Long gid, Integer goodsQty) {
        cityDAO.updateCityInventoryByCustomerIdAndGoodsIdAndInventory(userId, gid, goodsQty);
    }

    @Override
    @Transactional
    public void updateCityInventoryByEmpIdAndGoodsIdAndGoodsQty(Long userId, Long gid, Integer goodsQty) {
        cityDAO.updateCityInventoryByEmployeeIdAndGoodsIdAndInventory(userId, gid, goodsQty);
    }

    @Override
    public void modifyCity(City city) {
        cityDAO.modifyCity(city);
    }

    @Override
    public void deleteCityByCode(String code) {
        cityDAO.deleteCityByCode(code);
    }

    @Override
    public List<CityDeliveryTime> findCityDeliveryTimeByCityId(Long cityId) {
        if (null != cityId) {
            return cityDAO.findCityDeliveryTimeByCityId(cityId);
        }
        return null;
    }

    @Override
    public List<CityDeliveryTime> findCityDeliveryTimeByCityName(String cityName) {
        if (null != cityName) {
            return cityDAO.findCityDeliveryTimeByCityName(cityName);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer lockCityInventoryByCityIdAndGoodsIdAndInventory(Long cityId, Long goodsId, Integer inventory, Timestamp version) {
        if (null != cityId && null != goodsId && null != inventory) {
            return cityDAO.updateCityInventoryByCityIdAndGoodsIdAndInventory(cityId, goodsId, inventory, version);
        }
        return null;
    }

    @Override
    public CityInventory findCityInventoryByCityIdAndGoodsId(Long cityId, Long goodsId) {
        if (null != cityId && null != goodsId) {
            return cityDAO.findCityInventoryByCityIdAndGoodsId(cityId, goodsId);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCityInventoryAvailableQtyChangeLog(CityInventoryAvailableQtyChangeLog log) {
        if (null != log) {
            cityDAO.addCityInventoryAvailableQtyChangeLog(log);
        }
    }

    @Override
    public City findCityByUserIdAndIdentityType(Long userId, AppIdentityType identityType) {
        if (null != userId && null != identityType) {
            return cityDAO.findCityByUserIdAndIdentityType(userId, identityType);
        }
        return null;
    }

    @Override
    public Integer updateCityInventoryByCustomerIdAndGoodsIdAndInventoryAndVersion(Long userId, Long index, Integer integer, Date version) {
        if (null != userId && null != index && null != integer) {
            return cityDAO.updateCityInventoryByCustomerIdAndGoodsIdAndInventoryAndVersion(userId, index, integer, version);
        }
        return null;
    }

    @Override
    public Integer updateCityInventoryByEmployeeIdAndGoodsIdAndInventoryAndVersion(Long userId, Long index, Integer integer, Date version) {
        if (null != userId && null != index && null != integer) {
            return cityDAO.updateCityInventoryByEmployeeIdAndGoodsIdAndInventoryAndVersion(userId, index, integer, version);
        }
        return null;
    }

    @Override
    public Integer updateCityInventoryByCityIdAndGoodsIdAndInventoryAndVersion(Long cityId, Long index, Integer integer, Date version) {
        if (null != cityId && null != index && null != integer) {
            return cityDAO.updateCityInventoryByCityIdAndGoodsIdAndInventoryAndVersion(cityId, index, integer, version);
        }
        return null;
    }

    @Override
    public CityInventory findCityInventoryByCityCodeAndSku(String cityCode, String sku) {
        if (StringUtils.isNotBlank(cityCode) && StringUtils.isNotBlank(sku)) {
            return cityDAO.findCityInventoryByCityCodeAndSku(cityCode, sku);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer lockCityInventoryByCityCodeAndSkuAndInventory(String cityCode, String sku, Integer inventory, Timestamp lastUpdateTime) {
        if (StringUtils.isNotBlank(cityCode) && StringUtils.isNotBlank(sku) && null != inventory) {
            return cityDAO.updateCityInventoryByCityCodeAndSkuAndInventory(cityCode, sku, inventory, lastUpdateTime);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCityInventory(CityInventory cityInventory) {
        if (null != cityInventory) {
            cityDAO.saveCityInventory(cityInventory);
        }
    }

    @Override
    public City findCityByWarehouseNo(String warehouseNo) {
        if (StringUtils.isNotBlank(warehouseNo)) {
            return cityDAO.findCityByWarehouseNo(warehouseNo);
        }
        return null;
    }

    @Override
    public CityInventory findCityInventoryByCityIdAndSku(Long cityId, String sku) {
        if (null != cityId && StringUtils.isNotBlank(sku)) {
            return cityDAO.findCityInventoryByCityIdAndSku(cityId, sku);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer lockCityInventoryByCityIdAndSkuAndInventory(Long cityId, String sku, Integer changeInventory, Timestamp lastUpdateTime) {
        if (null != cityId && StringUtils.isNotBlank(sku) && null != changeInventory && null != lastUpdateTime) {
            return cityDAO.updateCityInventoryByCityIdAndSkuAndInventory(cityId, sku, changeInventory, lastUpdateTime);
        }
        return null;
    }
}
