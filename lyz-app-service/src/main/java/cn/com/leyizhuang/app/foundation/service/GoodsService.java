package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dto.GoodsDTO;
import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.vo.management.goods.MaGoodsVO;
import cn.com.leyizhuang.app.foundation.vo.OrderGoodsVO;
import cn.com.leyizhuang.app.foundation.vo.management.MaBuyProductCouponGoodsResponse;
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

    List<GoodsCategoryResponse> findGoodsCategoryListByCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType);

    List<GoodsBrandResponse> findGoodsBrandListByCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType);

    List<GoodsTypeResponse> findGoodsTypeListByCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType);

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

    List<OrderGoodsSimpleResponse> findGoodsListByCustomerIdAndGoodsIdList(Long userId, List<Long> goodsIds);

    GoodsPrice findGoodsPriceByProductCouponIdAndUserId(Long userId, Long pcId, Integer qty);

    GoodsDO queryBySku(String sku);

    Boolean existGoodsBrandByGoodsIdAndBrandName(Long id, String brandName);

    List<String> findCompanyFlagListById(List<Long> goodsIdList);

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

    PageInfo<GoodsDO> queryGoodsPageByInfo(Integer page, Integer size,String queryGoodsInfo);

    PageInfo<GoodsDO> screenGoodsGrid(Integer page, Integer size,Long brandCode,String categoryCode,String companyCode);

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
}
