package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
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

    void delete(Long priceLineId);

    GoodsPrice findByPriceLineId(Long priceLineId);

    List<GoodsPriceVO> findByStoreId(@Param("storeId") Long storeId, @Param("keywords") String keywords);

    Double findGoodsRetailPriceByGoodsIDAndStoreID(@Param("goodsID") Long goodsID,@Param("storeID") Long storeID);
}
