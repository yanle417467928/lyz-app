package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsBrand;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsCategoryDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaGoodsBrandDAO {
    List<GoodsBrand> findAllGoodsBrand();

    List<GoodsBrand> findGoodsBrandById(Long pid);

    List<GoodsBrand> findGoodsBrandByName(String queryStoreInfo);

    void save(GoodsBrand goodsBrand);

    Boolean isExistBrandName(String brandName);

    GoodsBrand queryGoodsBrandVOById(Long goodsBrandId);

    void update(GoodsBrand goodsBrand);
}
