package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import com.github.pagehelper.PageInfo;

/**
 * @author GenerationRoad
 * @date 2017/10/26
 */
public interface GoodsPriceService {
    void save(GoodsPrice goodsPrice);

    void modify(GoodsPrice goodsPrice);

    void delete(GoodsPrice goodsPrice);

    GoodsPrice findGoodsPrice(Long priceLineId);

    PageInfo<GoodsPrice> queryPage(Integer page, Integer size, Long storeId);
}
