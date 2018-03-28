package cn.com.leyizhuang.app.foundation.service.datatransfer;

import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrder;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;

import java.util.List;

/**
 * Created by panjie on 2018/3/26.
 */
public interface OrderGoodsTransferService {

    void transferAll();

    List<OrderGoodsInfo> transferOne(OrderBaseInfo orderBaseInfo,List<TdOrder> tdOrders) throws Exception;
}
