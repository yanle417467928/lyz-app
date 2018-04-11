package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.vo.AppAdminStoreInventoryVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * App后台 门店库存管理DAO层
 *
 * @author Richard
 * Created on 2017-05-08 14:38
 **/
@Repository
public interface AppAdminStoreInventoryDAO {

    StoreInventory queryByStoreId(Long storeId);

    List<AppAdminStoreInventoryVO> queryListVO(@Param("keywords") String keywords,@Param("list") List<Long> storeIds);

    List<AppAdminStoreInventoryVO> queryListByStoreId(Long storeId);
}
