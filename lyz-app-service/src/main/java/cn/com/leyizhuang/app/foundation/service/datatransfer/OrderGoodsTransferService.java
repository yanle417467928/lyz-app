package cn.com.leyizhuang.app.foundation.service.datatransfer;

import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;

/**
 * Created by panjie on 2018/3/26.
 */
public interface OrderGoodsTransferService {

    void transferAll();

    void transferOne(OrderBaseInfo orderBaseInfo) throws Exception;
}
