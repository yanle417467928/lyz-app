package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.vo.OrderGoodsVO;
import cn.com.leyizhuang.app.foundation.vo.management.MaBuyProductCouponGoodsResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Set;

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
            @Param(value = "categoryCode") String categoryCode, @Param(value = "userId") Long userId, @Param(value = "goodsBrand") String goodsBrand, @Param(value = "specification") String specification, @Param(value = "goodsType") String goodsType);

    List<GoodsCategoryResponse> findGoodsCategoryListByCategoryCodeAndEmployeeIdAndIdentityType(
            @Param(value = "categoryCode") String categoryCode, @Param(value = "userId") Long userId, @Param(value = "goodsBrand") String goodsBrand, @Param(value = "specification") String specification, @Param(value = "goodsType") String goodsType);

    List<GoodsBrandResponse> findGoodsBrandListByCategoryCodeAndCustomerIdAndIdentityType(
            @Param(value = "categoryCode") String categoryCode, @Param(value = "userId") Long userId, @Param(value = "categorySecond") String categorySecond, @Param(value = "specification") String specification, @Param(value = "goodsType") String goodsType);

    List<GoodsBrandResponse> findGoodsBrandListByCategoryCodeAndEmployeeIdAndIdentityType(
            @Param(value = "categoryCode") String categoryCode, @Param(value = "userId") Long userId, @Param(value = "categorySecond") String categorySecond, @Param(value = "specification") String specification, @Param(value = "goodsType") String goodsType);

    List<GoodsTypeResponse> findGoodsTypeListByCategoryCodeAndCustomerIdAndIdentityType(
            @Param(value = "categoryCode") String categoryCode, @Param(value = "userId") Long userId, @Param(value = "categorySecond") String categorySecond, @Param(value = "specification") String specification, @Param(value = "goodsBrand") String goodsBrand);

    List<GoodsTypeResponse> findGoodsTypeListByCategoryCodeAndEmployeeIdAndIdentityType(
            @Param(value = "categoryCode") String categoryCode, @Param(value = "userId") Long userId, @Param(value = "categorySecond") String categorySecond, @Param(value = "specification") String specification, @Param(value = "goodsBrand") String goodsBrand);


    List<GoodsSpecificationResponse> findGoodsSpecificationListByCategoryCodeAndCustomerIdAndIdentityType(
            @Param(value = "categoryCode") String categoryCode, @Param(value = "userId") Long userId, @Param(value = "categorySecond") String categorySecond , @Param(value = "goodsBrand") String goodsBrand, @Param(value = "goodsType") String goodsType );

    List<GoodsSpecificationResponse> findGoodsSpecificationListByCategoryCodeAndEmployeeIdAndIdentityType(
            @Param(value = "categoryCode") String categoryCode, @Param(value = "userId") Long userId, @Param(value = "categorySecond")  String categorySecond , @Param(value = "goodsBrand") String goodsBrand, @Param(value = "goodsType") String goodsType );

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

    GoodsDetailResponse findGoodsDetailByGoodsId(@Param("userId") Long userId, @Param("goodsId") Long goodsId,
                                                 @Param("type") AppIdentityType appIdentityType);

    GoodsDO findGoodsById(Long id);

    List<GoodsPrice> getGoodsPriceByCustomerAndGoodsId(@Param("userId") Long userId, @Param("list") List<Long> goodsIds);

    List<GoodsPrice> getGoodsPriceByEmployeeAndGoodsId(@Param("userId") Long userId, @Param("list") List<Long> goodsIds);

    List<UserGoodsResponse> filterGoodsCustomer(@Param(value = "userId") Long userId, @Param(value = "firstCategoryCode") String firstCategoryCode,
                                                @Param(value = "secondCategoryId") Long secondCategoryId, @Param(value = "brandId") Long brandId,
                                                @Param(value = "typeId") Long typeId, @Param(value = "specification") String specification);

    List<UserGoodsResponse> filterGoodsEmployee(@Param(value = "userId") Long userId, @Param(value = "firstCategoryCode") String firstCategoryCode,
                                                @Param(value = "secondCategoryId") Long secondCategoryId, @Param(value = "brandId") Long brandId,
                                                @Param(value = "typeId") Long typeId, @Param(value = "specification") String specification);

    List<OrderGoodsSimpleResponse> findGoodsListByEmployeeIdAndGoodsIdList(@Param("userId") Long userId, @Param("list") List<Long> goodsIds);

    List<OrderGoodsSimpleResponse> findGoodsListByCustomerIdAndGoodsIdList(@Param("userId") Long userId, @Param("list") List<Long> goodsIds);

    GoodsPrice findGoodsPriceByProductCouponIdAndUserId(@Param("cusId") Long cusId, @Param("pcId") Long pcId, @Param("qty") Integer qty);

    GoodsDO queryBySku(@Param("sku") String sku);

    Boolean existGoodsBrandByGoodsIdAndBrandName(@Param("gId") Long id, @Param("brandName") String brandName);

    List<String> findCompanyFlagListById(@Param(value = "list") List<Long> goodsIdList);

    List<String> findGoodsByCompanyFlagAndIds(@Param("GoodsIds") List<Long> GoodsIds,@Param("CompanyFlagList") List<String> CompanyFlagList);

    /**
     * HQ同步添加商品
     *
     * @param goodsDO 商品类
     */
    void saveSynchronize(GoodsDO goodsDO);

    /**
     * HQ同步修改商品
     *
     * @param goodsDO 商品类
     */
    void modifySynchronize(GoodsDO goodsDO);

    /**
     * HQ同步删除商品
     *
     * @param sku 物料编码
     */
    void deleteSynchronize(@Param("sku") String sku);


    void updateGoods(GoodsDO goodsDO);

    List<GoodsDO> queryGoodsPageByInfo(@Param(value = "queryGoodsInfo") String queryGoodsInfo);

    List<GoodsDO> screenGoodsGrid(@Param("brandCode") Long brandCode, @Param("categoryCode") String categoryCode, @Param("companyCode") String companyCode);

    List<MaBuyProductCouponGoodsResponse> screenGoodsGridBuyCoupon(@Param("brandCode") Long brandCode,
                                           @Param("categoryCode") String categoryCode,
                                           @Param("companyCode") String companyCode,
                                            @Param("productType") String productType,
                                             @Param("storeId") Long storeId);


    List<OrderGoodsVO> findOrderGoodsVOListByCustomerIdAndGoodsIds(@Param(value = "userId") Long userId,
                                                                   @Param(value = "goodsIdSet") Set<Long> goodsIdSet);

    List<OrderGoodsVO> findOrderGoodsVOListByEmpIdAndGoodsIds(@Param(value = "userId") Long userId,
                                                              @Param(value = "goodsIdSet") Set<Long> goodsIdSet);

    Boolean isExistSkuName(@Param(value = "skuName") String skuName, @Param(value = "id") Long id);

    Boolean isExistSortId(@Param(value = "sortId") Long sortId, @Param(value = "id") Long id);

    /**
     * 后台购买产品券查询商品信息
     *
     * @param storeId 门店id
     * @return
     */
    List<MaBuyProductCouponGoodsResponse> findMaStoreGoodsByStoreId(@Param("storeId") Long storeId);

    List<MaBuyProductCouponGoodsResponse> findMaStoreGoodsByStoreIdAndPricceType(@Param("storeId") Long storeId , @Param("priceType") String priceType,@Param("queryGoodsInfo") String queryGoodsInfo);

    List<MaBuyProductCouponGoodsResponse> findZGMaStoreGoodsByStoreIdAndPricceType(@Param("storeId") Long storeId , @Param("priceType") String priceType,@Param("queryGoodsInfo") String queryGoodsInfo);
    /**
     * 后台购买产品券条件查询商品信息
     *
     * @param storeId
     * @param brandCode
     * @param categoryCode
     * @param companyCode
     * @return
     */
    List<MaBuyProductCouponGoodsResponse> screenMaGoodsGrid(@Param("storeId") Long storeId, @Param("brandCode") Long brandCode, @Param("categoryCode") String categoryCode, @Param("companyCode") String companyCode);

    /**
     * 搜索条件查询商品
     *
     * @param storeId
     * @param queryGoodsInfo
     * @return
     */
    List<MaBuyProductCouponGoodsResponse> queryGoodsPageByStoreIdAndInfo(@Param("storeId") Long storeId, @Param("queryGoodsInfo") String queryGoodsInfo);

    List<UserGoodsResponse> findGoodsListByCustomerIdAndIdentityTypeAndUserRank(@Param(value = "userId") Long userId,
                                                                                @Param("identityType") AppIdentityType identityType,
                                                                                @Param("keywords") String keywords);

    List<GoodsDO> getGoodsBykeywordsAndCompanyAndBrandCodeAndCategoryCodeAndStoreId(
            @Param("keywords") String keywords, @Param("companyCode") String companyCode, @Param("brandCode") Long brandCode,
            @Param("categoryCode") String categoryCode, @Param("storeId") Long storeId
    );

    List<String> getGoodsSkuNameListByGoodsIdList(@Param(value = "noPriceGoodsIdList") List<Long> noPriceGoodsIdList);

    GoodsDO findGoodsByUserIdAndIdentityType(@Param("userId") Long userId, @Param("identityType") AppIdentityType identityType, @Param("gid") Long gid);

    List<GoodsDO> findGoodsListBySkuList(@Param(value = "internalCodeList") List<String> internalCodeList);

    GoodsDO findBySku(String sku);

    List<UserGoodsResponse> findGoodsListBySellerIdAndIdentityTypeAndRankCode(@Param(value = "userId") Long userId,
                                                                              @Param("identityType") AppIdentityType identityType,
                                                                              @Param("rankCode") String rankCode, @Param("keywords") String keywords);

    GoodsDetailResponse findSellerZGGoodsDetailByGoodsId(@Param("userId") Long userId, @Param("goodsId") Long goodsId,
                                     @Param("type") AppIdentityType appIdentityType, @Param("rankCode") String rankCode);

}
