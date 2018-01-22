package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;

import java.util.List;

/**
 * 现金返利分摊
 * Created by panjie on 2018/1/18.
 */
public interface AppCashReturnDutchService {

    /**
     * 现金返利分摊
     * @param leBiAmount
     * @param goodsInfs
     * @return
     */
    List<OrderGoodsInfo> cashReturnDutch(Double leBiAmount, List<OrderGoodsInfo> goodsInfs);
}
