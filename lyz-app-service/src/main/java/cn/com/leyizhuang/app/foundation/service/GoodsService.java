package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dto.GoodsDTO;
import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.vo.OrderGoodsVO;
import cn.com.leyizhuang.app.foundation.vo.management.MaBuyProductCouponGoodsResponse;
import cn.com.leyizhuang.app.foundation.vo.management.goods.MaGoodsVO;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Set;

/**
 * @author GenerationRoad
 * @date 2017/9/6
 */
public interface GoodsService {

    PageInfo<GoodsDO> queryPage(Integer page, Integer size);

    GoodsDO managerSaveGoods(GoodsDTO goodsDTO);

    PageInfo<UserGoodsResponse> findGoodsListByCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType,Integer page,Integer size);

    GoodsDO queryById(Long id);

    void batchRemove(List<Long> longs);

    List<GoodsCategoryResponse> findGoodsCategoryListByCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType,String goodsBrand,String specification,String goodsType);

    List<GoodsBrandResponse> findGoodsBrandListByCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType,String categorySecond,String specification,String goodType);

    List<GoodsTypeResponse> findGoodsTypeListByCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType, String categorySecond, String specification, String goodsBrand);

    List<GoodsSpecificationResponse> findGoodsSpecificationListByCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType, String categorySecond, String goodsBrand, String goodType);

    GoodsDetailResponse findGoodsDetailByGoodsId(Long userId, Long goodsId, Integer identityType);

    List<UserGoodsResponse> findGoodsCollectListByUserIdAndIdentityType(Long userId, Integer identityType);

    PageInfo<UserGoodsResponse> findGoodsListByIsHotAndUserIdAndIdentityType(Long userId, Integer identityType,Integer page, Integer size);

    List<UserGoodsResponse> findGoodsOftenListByUserIdAndIdentityType(Long userId, Integer identityType);

    List<UserGoodsResponse> searchByUserIdAndKeywordsAndIdentityType(Long userId, String keywords, Integer identityType);

    void addCollectGoodsByUserIdAndGoodsIdAndIdentityType(Long userId, Long goodsId, Integer identityType);

    void removeCollectGoodsByUserIdAndGoodsIdAndIdentityType(Long userId, Long goodsId, Integer identityType);

    GoodsDO findGoodsById(Long id);

    List<GoodsPrice> getGoodsPriceByCustomerAndGoodsId(Long userId, List<Long> goodsIds);

    List<GoodsPrice> getGoodsPriceByEmployeeAndGoodsId(Long userId, List<Long> goodsIds);

    PageInfo<UserGoodsResponse> filterGoods(Long userId, AppIdentityType type, String firstCategoryCode, Long secondCategoryId,
                                            Long brandId, Long typeId, String specification, Integer page, Integer size);

    List<OrderGoodsSimpleResponse> findGoodsListByEmployeeIdAndGoodsIdList(Long userId, List<Long> goodsIds);

    List<OrderGoodsSimpleResponse> findGoodsListByEmployeeIdAndGoodsSkuList(Long userId,List<String> goodsSku);

    List<OrderGoodsSimpleResponse> findGoodsListByCustomerIdAndGoodsIdList(Long userId, List<Long> goodsIds);

    GoodsPrice findGoodsPriceByProductCouponIdAndUserId(Long userId, Long pcId, Integer qty);

    GoodsDO queryBySku(String sku);

    Boolean existGoodsBrandByGoodsIdAndBrandName(Long id, String brandName);

    List<String> findCompanyFlagListById(List<Long> goodsIdList);

    List<String> findGoodsByCompanyFlagAndIds(List<Long> goodsIds,List<String> companyFlagList);

    /**
     * HQ同步添加商品
     * @param goodsDO   商品类
     */
    void saveSynchronize(GoodsDO goodsDO);

    /**
     * HQ同步修改商品
     * @param goodsDO   商品类
     */
    void modifySynchronize(GoodsDO goodsDO);


    /**
     * HQ同步删除商品
     * @param sku   物料编码
     */
    void deleteSynchronize(String sku);

    List<OrderGoodsVO> findOrderGoodsVOListByUserIdAndIdentityTypeAndGoodsIds(Long userId, Integer identityType, Set<Long> goodsIdSet);

    List<OrderGoodsVO> findOrderGoodsVOListByUserIdAndIdentityTypeAndGoodsIdsMinPrice(Long userId, Integer identityType, Set<Long> goodsIdSet);

    PageInfo<GoodsDO> queryGoodsPageByInfo(Integer page, Integer size,String queryGoodsInfo);

    PageInfo<GoodsDO> screenGoodsGrid(Integer page, Integer size,Long brandCode,String categoryCode,String companyCode);

    PageInfo<MaBuyProductCouponGoodsResponse> screenGoodsGrid(Integer page, Integer size,
                                                              Long brandCode,String categoryCode,
                                                              String companyCode,String productType,Long storeId);

    void updateGoods(MaGoodsVO goodsVO);

    Boolean isExistSkuName(String skuName,Long id);

    Boolean isExistSortId(Long sortId,Long id);

    /**
     * 后台购买产品券查询商品信息
     * @param storeId   门店id
     * @return
     */
    List<MaBuyProductCouponGoodsResponse> findMaStoreGoodsByStoreId(Long storeId);

    /**
     * 后台买券专用
     * @param storeId
     * @param cusId
     * @param empId
     * @return
     */
    List<MaBuyProductCouponGoodsResponse> findGoodsForBuyCoupon(Long storeId,Long cusId,Long empId,String queryGoodsInfo,String priceType);

    List<MaBuyProductCouponGoodsResponse> findZGMaStoreGoodsByStoreIdAndPricceType(Long storeId, Long cusId, Long sellerId, String queryGoodsInfo, String priceType);
    /**
     * 后台购买产品券条件查询商品信息
     * @param storeId
     * @param brandCode
     * @param categoryCode
     * @param companyCode
     * @return
     */
    List<MaBuyProductCouponGoodsResponse> screenMaGoodsGrid(Long storeId,Long brandCode,String categoryCode,String companyCode);

    /**
     * 搜索条件查询商品
     * @param storeId
     * @param queryGoodsInfo
     * @return
     */
    List<MaBuyProductCouponGoodsResponse> queryGoodsPageByStoreIdAndInfo(Long storeId,String queryGoodsInfo);

    PageInfo<UserGoodsResponse> findGoodsListByCustomerIdAndIdentityTypeAndUserRank(Long userId, AppIdentityType identityType, String firstCategoryCode, Long secondCategoryId,
                                                                                    Long brandId, Long typeId, String specification,String keywords, Integer page, Integer size);
    List<UserGoodsResponse> findGoodsListByCustomerIdAndIdentityTypeAndUserRankListMa(Long userId, AppIdentityType identityType, String firstCategoryCode, Long secondCategoryId,
                                                               Long brandId, Long typeId, String specification,String keywords);

    PageInfo<GoodsDO> getGoodsBykeywordsAndCompanyAndBrandCodeAndCategoryCodeAndStoreId(Integer page, Integer size, String keywords,String companyCode, Long brandCode,
                                                                                          String categoryCode, Long storeId);

    List<String> getGoodsSkuNameListByGoodsIdList(List<Long> noPriceGoodsIdList);

    GoodsDO findGoodsByUserIdAndIdentityType(Long userId, AppIdentityType identityType, Long gid);

    List<GoodsDO> findGoodsListBySkuList(List<String> internalCodeList);

    GoodsDO findBySku(String sku);

    Boolean isFWGoods(Long goodsId);

    PageInfo<UserGoodsResponse> findGoodsListBySellerIdAndIdentityTypeAndRankCode(Long userId, AppIdentityType identityType,
                                                                                  String rankCode, String keywords, String firstCategoryCode,
                                                                                  Long secondCategoryId, Long brandId, Long typeId,
                                                                                  String specification,Integer page, Integer size);

    GoodsDetailResponse findSellerZGGoodsDetailByGoodsId(Long userId, Long goodsId, Integer identityType, String rankCode);

    List<GoodsBrandResponse> findGoodsBrandListByCategoryCodeAndUserIdAndIdentityTypeAndUserRank(String categoryCode, Long userId, Integer identityType,String categorySecond,String specification,String goodType, String rankCode);

    List<GoodsSpecificationResponse> findGoodsSpecificationListByCategoryCodeAndUserIdAndUserRank(String categoryCode, Long userId, Integer identityType, String categorySecond, String goodsBrand, String goodType, String rankCode);

    List<GoodsCategoryResponse> findGoodsCategoryListByCategoryCodeAndUserIdAndUserRank(String categoryCode, Long userId, Integer identityType,String goodsBrand,String specification,String goodsType, String rankCode);

    List<GoodsTypeResponse> findGoodsTypeListByCategoryCodeAndUserIdAndUserRank(String categoryCode, Long userId, Integer identityType, String categorySecond, String specification, String goodsBrand, String rankCode);

}
