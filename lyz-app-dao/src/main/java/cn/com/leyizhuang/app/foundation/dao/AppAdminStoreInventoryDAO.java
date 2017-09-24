package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.StoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.vo.AppAdminStoreInventoryVO;
import cn.com.leyizhuang.common.foundation.dao.BaseDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * App后台 门店库存管理DAO层
 *
 * @author Richard
 *         Created on 2017-05-08 14:38
 **/
@Repository
public interface AppAdminStoreInventoryDAO extends BaseDAO<StoreInventory> {

    List<StoreInventory> queryByStoreId(Long storeId);
    List<AppAdminStoreInventoryVO> queryListVO();



}
