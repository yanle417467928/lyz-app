package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import org.springframework.stereotype.Repository;

/**
 * @author GenerationRoad
 * @date 2017/10/26
 */
@Repository
public interface GoodsPriceDAO {

    void save(GoodsPrice goodsPrice);

    void modify(GoodsPrice goodsPrice);

    void delete(Long priceLineId);

    GoodsPrice findByPriceLineId(Long priceLineId);
}
