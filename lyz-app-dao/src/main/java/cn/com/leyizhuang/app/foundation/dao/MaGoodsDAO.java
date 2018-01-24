package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.goods.GoodsShippingInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderGoodsInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface MaGoodsDAO {

    void saveGoodsShippingInfo(GoodsShippingInfo goodsShippingInfo);
}

