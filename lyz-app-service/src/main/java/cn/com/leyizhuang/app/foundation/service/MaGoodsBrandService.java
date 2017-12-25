package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.management.goods.GoodsBrand;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.SimpaleGoodsBrandParam;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface MaGoodsBrandService {
    PageInfo<GoodsBrand> queryPageVO(Integer page, Integer size);

    PageInfo<GoodsBrand> findGoodsBrandById(Long id,Integer offset, Integer size);

    PageInfo<GoodsBrand> findGoodsBrandByName(String queryStoreInfo,Integer offset, Integer size);

    void save(GoodsBrand goodsBrand);

    Boolean isExistBrandName(String brandName);

    Boolean editIsExistBrandName(String brandName,Long id);

    Boolean isExistSort(Long sortId);

    Boolean editIsExistSort( Long sortId,Long id);

    GoodsBrand queryGoodsBrandVOById(Long goodsBrandId);

    void  update(GoodsBrand goodsBrand);

    List<SimpaleGoodsBrandParam> queryGoodsBrandList();

    void delete(Long id);
}
