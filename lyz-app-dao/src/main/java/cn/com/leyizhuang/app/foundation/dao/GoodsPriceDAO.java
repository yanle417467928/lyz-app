package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponseGoods;
import cn.com.leyizhuang.app.foundation.vo.GoodsPriceVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/10/26
 */
@Repository
public interface GoodsPriceDAO {

    void save(GoodsPrice goodsPrice);

    void modify(GoodsPrice goodsPrice);

    void update(GoodsPrice goodsPrice);

    void updateByEbs(GoodsPrice goodsPrice);

    void delete(Long priceLineId);

    GoodsPrice findByPriceLineId(Long priceLineId);

    List<GoodsPriceVO> findByStoreId(@Param("storeId") Long storeId, @Param("keywords") String keywords);

    Double findGoodsRetailPriceByGoodsIDAndStoreID(@Param("goodsID") Long goodsID, @Param("storeID") Long storeID);

    List<GiftListResponseGoods> findCustomerGoodsPriceListByGoodsIdsAndUserIdAndIdentityType(@Param(value = "list") List<Long> goodsIdList,
                                                                                             @Param(value = "userId") Long userId);

    List<GiftListResponseGoods> findEmployeeGoodsPriceListByGoodsIdsAndUserIdAndIdentityType(@Param(value = "list") List<Long> goodsIdList,
                                                                                             @Param(value = "userId") Long userId);

    /**
     * 根据商品id + 门店id 查询商品价格
     *
     * @param goodsID 商品id
     * @param storeID 门店id
     * @return
     */
    GoodsPrice findGoodsPriceByGoodsIDAndStoreID(@Param("goodsID") Long goodsID, @Param("storeID") Long storeID,@Param("cusId") Long cusId);
    /**
     * 根据商品id + 门店id 查询商品价格
     *
     * @param goodsID 商品id
     * @param storeID 门店id
     * @return
     */
    GoodsPrice findGoodsPriceByGoodsIDAndStoreIDAndEmpId(@Param("goodsID") Long goodsID, @Param("storeID") Long storeID,@Param("empId") Long empId);

    GoodsPrice findGoodsPriceByTypeAndStoreIDAndSku(@Param("priceType") String priceType, @Param("storeId") Long storeID,@Param("sku") String sku);

    List<GiftListResponseGoods> findCustomerGoodsPriceListByGoodsIdsAndUserId(@Param(value = "list") List<Long> goodsIdList,
                                                                              @Param(value = "userId") Long userId);

    List<GoodsPrice> findGoodsPriceListByStoreIdAndSkuList(@Param(value = "storeId") Long storeId,
                                                           @Param(value = "internalCodeList") List<String> internalCodeList);

    List<GoodsPrice> findGoodsPriceListByStoreIdAndPriceType(@Param(value = "storeId")Long storeId, @Param(value = "priceType")String priceType);

    List<GoodsPrice> findGoodsPriceListByStoreIdAndSkuAndpriceType(@Param(value = "storeId")Long storeId,@Param(value = "priceType")String priceType,@Param(value = "sku")String sku);

    GoodsPrice finGoodsPriceByLineId(@Param("lineId") Long lineId);

    void saveBackupsGoodsPrice(GoodsPrice goodsPrice);

    void delGoodsPriceListByStoreIdAndSkuAndpriceType(@Param(value = "storeId")Long storeId,@Param(value = "priceType")String priceType,@Param(value = "sku")String sku);

    void delGoodsPriceListByLineId(@Param(value = "lineId")Long lineId);
}
