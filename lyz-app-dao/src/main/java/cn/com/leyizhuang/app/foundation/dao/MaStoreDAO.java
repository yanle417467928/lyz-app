package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.vo.StoreVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaStoreDAO {

    List<StoreVO> findAllVO();

    List<StoreVO> findStoresListByCityId(Long cityId);

    StoreVO findStoresVOById(Long storeId);

    List<StoreVO> queryStoreListByCityId(Long cityId);

    List<StoreVO> findStoresListByEnable(@Param("enabled") Boolean enabled, @Param("cityId") Long cityId);

    List<StoreVO> findStoresListByStoreInfo(String queryStoreInfo);
}
