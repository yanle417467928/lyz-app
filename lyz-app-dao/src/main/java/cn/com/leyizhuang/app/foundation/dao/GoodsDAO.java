package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.response.GoodsBrandResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.GoodsCategoryResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.GoodsTypeResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.UserGoodsResponse;
import cn.com.leyizhuang.app.foundation.pojo.vo.GoodsVO;
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

    List<GoodsVO> findGoodsListByCategoryCodeAndCustomerIdAndIdentityType(@Param(value = "categoryCode") String categoryCode,
                                                                          @Param(value = "userId") Long userId);

    GoodsDO queryById(Long id);

    List<GoodsVO> findGoodsListByCategoryCodeAndEmployeeIdAndIdentityType(@Param(value = "categoryCode") String categoryCode,
                                                                          @Param(value = "userId") Long userId);

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

    List<UserGoodsResponse> findGoodsListByIsHotAndCustomerIdAndIdentityType(Long userId);

    List<UserGoodsResponse> findGoodsListByIsHotAndEmployeeIdAndIdentityType(Long userId);

    List<UserGoodsResponse> findGoodsCollectListByCustomerIdAndIdentityType(Long userId);

    List<UserGoodsResponse> findGoodsCollectListByEmployeeIdAndIdentityType(Long userId);

    List<UserGoodsResponse> findGoodsOftenListByCustomerIdAndIdentityType(Long userId);

    List<UserGoodsResponse> findGoodsOftenListByEmployeeIdAndIdentityType(Long userId);
}
