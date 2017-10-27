package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;

/**
 * @author GenerationRoad
 * @date 2017/10/26
 */
public interface GoodsPriceService {
    void save(GoodsPrice goodsPrice);

    void modify(GoodsPrice goodsPrice);

    void delete(GoodsPrice goodsPrice);

    GoodsPrice findGoodsPrice(Long priceLineId);
}
