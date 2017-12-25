package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.goods.GoodsCategoryDO;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.SimpleGoodsCategoryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaGoodsCategoryDAO {
    List<GoodsCategoryDO> findAllGoodsCategory();

    List<SimpleGoodsCategoryParam> findGoodsCategorySelection();

    List<GoodsCategoryDO> findGoodsCategoryByPid(Long pid);

    List<GoodsCategoryDO> findGoodsCategoryByPcode(String queryCategoryInfo);

    void save(GoodsCategoryDO goodsCategoryDO);

    Boolean isExistCategoryName(String categoryName);

    Boolean editIsExistCategoryName(@Param(value = "categoryName") String categoryName,@Param(value = "id") Long id);

    GoodsCategoryDO queryGoodsCategoryVOById(Long goodsCategoryId);

    void update(GoodsCategoryDO goodsCategoryDO);

    List<GoodsCategoryDO> findEditGoodsCategory();

    void delete(Long id);

    Boolean isExistSortId(Long sortId);

    Boolean editIsExistSortId(@Param(value = "sortId") Long sortId,@Param(value = "id") Long id);

}
