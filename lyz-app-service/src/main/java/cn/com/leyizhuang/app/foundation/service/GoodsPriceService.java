package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponseGoods;
import cn.com.leyizhuang.app.foundation.vo.GoodsPriceVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/10/26
 */
public interface GoodsPriceService {
    void save(GoodsPrice goodsPrice);

    void modify(GoodsPrice goodsPrice);

    void delete(GoodsPrice goodsPrice);

    GoodsPrice findGoodsPrice(Long priceLineId);

    PageInfo<GoodsPriceVO> queryPage(Integer page, Integer size, Long storeId, String keywords);

    Double findGoodsRetailPriceByGoodsIDAndStoreID(Long goodsID, Long storeID);

    List<GiftListResponseGoods> findGoodsPriceListByGoodsIdsAndUserIdAndIdentityType(List<Long> goodsIdList, Long userId,
                                                                                     AppIdentityType identityType);
    /**
     * 根据商品id + 门店id 查询商品价格
     * @param goodsID   商品id
     * @param storeID   门店id
     * @return
     */
    GoodsPrice findGoodsPriceByGoodsIDAndStoreID(Long goodsID,Long storeID,Long cusId);


    List<GiftListResponseGoods> findGoodsPriceListByGoodsIdsAndUserId(List<Long> goodsIdList, Long userId,
                                                                                     AppIdentityType identityType);

    List<GoodsPrice> findGoodsPriceListByStoreIdAndSkuList(Long storeId, List<String> internalCodeList);

    List<GoodsPrice> findGoodsPriceListByStoreIdAndPriceType(Long storeId, String priceType);
}
