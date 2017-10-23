package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/5
 */
@Repository
public interface GoodsDAO {

    List<GoodsDO> queryList();

    List<UserGoodsResponse> findGoodsListByCategoryCodeAndCustomerIdAndIdentityType(
            @Param(value = "categoryCode") String categoryCode, @Param(value = "userId") Long userId);

    GoodsDO queryById(Long id);

    List<UserGoodsResponse> findGoodsListByCategoryCodeAndEmployeeIdAndIdentityType(
            @Param(value = "categoryCode") String categoryCode, @Param(value = "userId") Long userId);

    void batchRemove(List<Long> longs);

    List<GoodsCategoryResponse> findGoodsCategoryListByCategoryCodeAndCustomerIdAndIdentityType(
            @Param(value = "categoryCode") String categoryCode, @Param(value = "userId") Long userId);

    List<GoodsCategoryResponse> findGoodsCategoryListByCategoryCodeAndEmployeeIdAndIdentityType(
            @Param(value = "categoryCode") String categoryCode, @Param(value = "userId") Long userId);

    List<GoodsBrandResponse> findGoodsBrandListByCategoryCodeAndCustomerIdAndIdentityType(
            @Param(value = "categoryCode") String categoryCode, @Param(value = "userId") Long userId);

    List<GoodsBrandResponse> findGoodsBrandListByCategoryCodeAndEmployeeIdAndIdentityType(
            @Param(value = "categoryCode") String categoryCode, @Param(value = "userId") Long userId);

    List<GoodsTypeResponse> findGoodsTypeListByCategoryCodeAndCustomerIdAndIdentityType(
            @Param(value = "categoryCode") String categoryCode, @Param(value = "userId") Long userId);

    List<GoodsTypeResponse> findGoodsTypeListByCategoryCodeAndEmployeeIdAndIdentityType(
            @Param(value = "categoryCode") String categoryCode, @Param(value = "userId") Long userId);

    GoodsDO findGoodsImageUriByGoodsId(Long goodsId);

    List<UserGoodsResponse> findGoodsListByIsHotAndCustomerIdAndIdentityType(Long userId);

    List<UserGoodsResponse> findGoodsListByIsHotAndEmployeeIdAndIdentityType(Long userId);

    List<UserGoodsResponse> searchByCustomerIdAndKeywords(
            @Param(value = "userId") Long userId, @Param(value = "keywords") String keywords);

    List<UserGoodsResponse> searchByEmployeeIdAndKeywords(
            @Param(value = "userId") Long userId, @Param(value = "keywords") String keywords);

    List<UserGoodsResponse> findGoodsCollectListByUserIdAndIdentityType(
            @Param("userId") Long userId, @Param("type") AppIdentityType identityType);

    List<UserGoodsResponse> findGoodsOftenListByUserIdAndIdentityType(
            @Param("userId") Long userId, @Param("type") AppIdentityType appIdentityType);

    void saveCollectGoodsByUserIdAndGoodsIdAndIdentityType(
            @Param("userId") Long userId, @Param("goodsId") Long goodsId, @Param("type") AppIdentityType appIdentityType);

    void deleteCollectGoodsByUserIdAndGoodsIdAndIdentityType(
            @Param("userId") Long userId, @Param("goodsId") Long goodsId, @Param("type") AppIdentityType appIdentityType);

    GoodsDetailResponse findGoodsDetailByGoodsId(@Param("userId") Long userId, @Param("goodsId") Long goodsId, @Param("type") AppIdentityType appIdentityType);

    GoodsDO findGoodsById(Long id);

    List<GoodsPrice> getGoodsPriceByCustomerAndGoodsId(@Param("userId") Long userId, @Param("list") List<Long> goodsIds);

    List<UserGoodsResponse> filterGoodsCustomer(@Param(value = "userId") Long userId, @Param(value = "firstCategoryCode") String firstCategoryCode,
                                                @Param(value = "secondCategoryId") Long secondCategoryId,@Param(value = "brandId") Long brandId,
                                                @Param(value = "typeId") Long typeId, @Param(value = "specification") String specification);

    List<UserGoodsResponse> filterGoodsEmployee(@Param(value = "userId") Long userId, @Param(value = "firstCategoryCode") String firstCategoryCode,
                                                @Param(value = "secondCategoryId") Long secondCategoryId,@Param(value = "brandId") Long brandId,
                                                @Param(value = "typeId") Long typeId, @Param(value = "specification") String specification);
}
