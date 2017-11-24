package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsCategoryDO;
import cn.com.leyizhuang.app.foundation.vo.GoodsCategoryVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface MaGoodsCategoryService {

    PageInfo<GoodsCategoryDO> queryPageVO(Integer page, Integer size);

    List<GoodsCategoryVO> findGoodsCategorySelection();

    PageInfo<GoodsCategoryDO> findGoodsCategoryByPid(Long pid,Integer offset, Integer size);

    PageInfo<GoodsCategoryDO> findGoodsCategoryByPcode(String queryStoreInfo,Integer offset, Integer size);

   void save(GoodsCategoryVO goodsCategoryVO);

    Boolean isExistCategoryName(String categoryName);

    GoodsCategoryVO queryGoodsCategoryVOById(Long goodsCategoryId);

    void  update(GoodsCategoryVO goodsCategoryVO);
}
