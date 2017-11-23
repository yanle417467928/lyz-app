package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.vo.StoreVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface MaStoreService {

    PageInfo<StoreVO> queryPageVO(Integer page, Integer size);

    List<StoreVO> findStoreList();

    List<StoreVO> findStoresListByCityId(Long cityId);

    StoreVO queryStoreVOById(Long storeId);

    PageInfo<StoreVO> queryStoreListByCityId(Integer page, Integer size, Long cityId);

    PageInfo<StoreVO> findStoresListByEnable(Integer page, Integer size, Boolean enabled, Long cityId);

    PageInfo<StoreVO> findStoresListByStoreInfo(Integer page, Integer size, String queryStoreInfo);
}
