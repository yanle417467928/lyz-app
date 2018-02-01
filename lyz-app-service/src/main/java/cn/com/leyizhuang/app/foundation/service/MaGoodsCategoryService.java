package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.management.goods.GoodsCategoryDO;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.SimpleGoodsCategoryParam;
import cn.com.leyizhuang.app.foundation.vo.management.goodscategory.GoodsCategoryVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface MaGoodsCategoryService {

    PageInfo<GoodsCategoryDO> queryPageVO(Integer page, Integer size);

    List<SimpleGoodsCategoryParam> findGoodsCategorySelection();

    PageInfo<GoodsCategoryDO> findGoodsCategoryByPid(Long pid,Integer offset, Integer size);

    PageInfo<GoodsCategoryDO> findGoodsCategoryByPcode(String queryCategoryInfo,Integer offset, Integer size);

   void save(GoodsCategoryVO goodsCategoryVO);

    Boolean isExistCategoryName(String categoryName);

    Boolean editIsExistCategoryName(String categoryName,Long id);

    GoodsCategoryVO queryGoodsCategoryVOById(Long goodsCategoryId);

    void  update(GoodsCategoryVO goodsCategoryVO);

    List<GoodsCategoryVO> findEditGoodsCategory();

    void delete(Long id);

    Boolean isExistSortId(Long sortId);

    Boolean editIsExistSortId(Long sortId,Long id);

    List<GoodsCategoryDO> findGoodsCategoryByPCategoryCode(String categoryCode);
}
