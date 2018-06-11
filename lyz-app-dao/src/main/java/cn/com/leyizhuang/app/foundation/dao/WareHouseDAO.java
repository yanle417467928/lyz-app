package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.WareHouseDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Jerry.Ren
 * create 2018-02-22 17:06
 * desc:
 **/
@Repository
public interface WareHouseDAO {


    /**
     * 根据仓库编号查找仓库
     *
     * @param whNo
     * @return
     */
    WareHouseDO findByWareHouseNo(String whNo);

    List<WareHouseDO> findWareHouseByCityId(@Param("cityId") Long cityId);
}
