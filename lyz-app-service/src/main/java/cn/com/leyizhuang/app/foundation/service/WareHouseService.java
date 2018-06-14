package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.WareHouseDO;

import java.util.List;

/**
 * @author Jerry.Ren
 * create 2018-02-22 16:58
 * desc:
 **/
public interface WareHouseService {

    /**
     * 根据仓库编号查找仓库
     *
     * @param whNo
     * @return
     */
    WareHouseDO findByWareHouseNo(String whNo);

    List<WareHouseDO> findWareHouseByCityId(Long cityId);
}
