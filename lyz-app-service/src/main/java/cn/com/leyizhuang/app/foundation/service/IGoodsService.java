package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.GoodsDO;
import cn.com.leyizhuang.app.foundation.dto.GoodsDTO;
import cn.com.leyizhuang.app.foundation.pojo.response.GoodsBrandResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.GoodsCategoryResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.GoodsTypeResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.UserGoodsResponse;
import cn.com.leyizhuang.app.foundation.vo.GoodsVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/6
 */
public interface IGoodsService {

    PageInfo<GoodsDO> queryPage(Integer page, Integer size);

    GoodsDO managerSaveGoods(GoodsDTO goodsDTO);

    List<UserGoodsResponse> findGoodsListByCategoryCodeAndUserIdAndIdentityType(String categoryCode,Long userId, Integer identityType);

    GoodsDO queryById(Long id);

    void batchRemove(List<Long> longs);

    List<GoodsCategoryResponse> findGoodsCategoryListByCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType);

    List<GoodsBrandResponse> findGoodsBrandListByCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType);

    List<GoodsTypeResponse> findGoodsTypeListByCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType);

    GoodsDO findGoodsImageUriByGoodsCode(String goodsCode);

    List<UserGoodsResponse> findGoodsCollectListByUserIdAndIdentityType(Long userId, Integer identityType);

    List<UserGoodsResponse> findGoodsListByIsHotAndUserIdAndIdentityType(Long userId, Integer identityType);

    List<UserGoodsResponse> findGoodsOftenListByUserIdAndIdentityType(Long userId, Integer identityType);

    List<UserGoodsResponse> searchByUserIdAndKeywordsAndIdentityType(Long userId, String keywords,Integer identityType);

    void addCollectGoodsByUserIdAndGoodsIdAndIdentityType(Long userId, Long goodsId, Integer identityType);

    void removeCollectGoodsByUserIdAndGoodsIdAndIdentityType(Long userId, Long goodsId, Integer identityType);
}
