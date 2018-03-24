package cn.com.leyizhuang.app.foundation.service.datatransfer.impl;

import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrderGoods;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.service.datatransfer.DataTransferService;

/**
 * 订单商品转换类
 *
 * @author Richard
 * @date 2018/3/24
 */
public class DataTransferServiceImpl implements DataTransferService {

    public OrderGoodsInfo transferOne(TdOrderGoods tdOrderGoods){
        OrderGoodsInfo goodsInfo = new OrderGoodsInfo();
        return goodsInfo;
    }
}
