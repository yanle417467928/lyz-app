package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.goods.GoodsBrand;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.SimpaleGoodsBrandParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaGoodsBrandDAO {
    List<GoodsBrand> findAllGoodsBrand();

    List<SimpaleGoodsBrandParam> queryGoodsBrandList();

    List<GoodsBrand> findGoodsBrandById(Long pid);

    List<GoodsBrand> findGoodsBrandByName(String queryStoreInfo);

    void save(GoodsBrand goodsBrand);

    Boolean isExistBrandName(String brandName);

    Boolean editIsExistBrandName(@Param(value = "brandName") String brandName,@Param(value = "id") Long id);

    Boolean isExistSort(Long sortId);

    Boolean editIsExistSort(@Param(value = "sortId") Long sortId,@Param(value = "id") Long id);

    GoodsBrand queryGoodsBrandVOById(Long goodsBrandId);

    void update(GoodsBrand goodsBrand);

    void delete(Long id);
}
