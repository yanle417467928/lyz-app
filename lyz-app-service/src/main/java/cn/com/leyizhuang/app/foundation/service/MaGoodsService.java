package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.response.GoodsBrandResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.GoodsSpecificationResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.GoodsTypeResponse;
import cn.com.leyizhuang.app.foundation.vo.management.goods.GoodsResponseVO;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/30
 */
public interface MaGoodsService {

    List<GoodsResponseVO> findGoodsByCidAndCusId(Long cusId, List<Long> cids);

    List<GoodsResponseVO> findGoodsByCidAndEmpId(Long empId, List<Long> cids);

    List<GoodsResponseVO> findGoodsByMultiConditionQueryAndCusId(Long cusId, Long cids,String categoryType, Long brandString,String specificationString, Long goodsTypeString);

    List<GoodsResponseVO> findGoodsByMultiConditionQueryAndEmpId(Long empId, Long cids,String categoryType, Long brandString,String specificationString, Long goodsTypeString);

    GoodsDO findGoodsById(Long id);

    List<GoodsResponseVO> findGoodsByCidAndCusIdAndUserRank(Long cusId);

    List<GoodsBrandResponse> findGoodsBrandListByCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType, String categorySecond, String specification, String goodType);

    List<GoodsTypeResponse> findGoodsTypeListByCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType, String categorySecond, String specification, String goodsBrand);

    List<GoodsSpecificationResponse> findGoodsSpecificationListByCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType, String categorySecond, String goodsBrand, String goodType);

    List<GoodsDO> findGoodsListByGidList(List<Long> internalGidList);
}
