package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsCategoryDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaGoodsCategoryDAO {
    List<GoodsCategoryDO> findAllGoodsCategory();

    List<GoodsCategoryDO> findGoodsCategorySelection();

    List<GoodsCategoryDO> findGoodsCategoryByPid(Long pid);

    List<GoodsCategoryDO> findGoodsCategoryByPcode(String queryStoreInfo);

    void save(GoodsCategoryDO goodsCategoryDO);

    Boolean isExistCategoryName(String categoryName);

    GoodsCategoryDO queryGoodsCategoryVOById(Long goodsCategoryId);

    void update(GoodsCategoryDO goodsCategoryDO);

    List<GoodsCategoryDO> findEditGoodsCategory();

}
