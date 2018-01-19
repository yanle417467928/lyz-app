package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;

import java.util.List;

/**
 * 乐币分摊
 * Created by panjie on 2018/1/18.
 */
public interface AppLeBiDutchService {
    /**
     * 乐币分摊
     * @param leBiQty 乐币数量
     * @param goodsInfs 分摊商品集合
     * @return
     */
    List<OrderGoodsInfo> LeBiDutch(Integer leBiQty, List<OrderGoodsInfo> goodsInfs);
}
