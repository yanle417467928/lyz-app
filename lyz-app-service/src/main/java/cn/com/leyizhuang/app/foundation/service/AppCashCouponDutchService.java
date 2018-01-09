package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;

import java.util.List;

/**
 * 现金券分摊
 * Created by panjie on 2018/1/8.
 */
public interface AppCashCouponDutchService {

    List<OrderGoodsInfo> cashCouponDutch(List<Long> cashCouponIdList, List<OrderGoodsInfo> goodsInfs);
}
