package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.WareHouseDO;

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
}
