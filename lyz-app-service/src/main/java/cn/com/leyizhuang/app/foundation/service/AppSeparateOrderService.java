package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.AppOrderStatus;
import cn.com.leyizhuang.app.core.constant.LogisticStatus;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderBaseInf;
import cn.com.leyizhuang.app.foundation.pojo.request.GoodsIdQtyParam;
import cn.com.leyizhuang.app.foundation.pojo.request.OrderLockExpendRequest;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.BillingSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.DeliverySimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponseGoods;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderArrearageInfoResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderGoodsListResponse;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 拆单服务接口
 * 
 * @author Richard
 * @create 2018/01/04.
 */
public interface AppSeparateOrderService {


    Boolean isOrderExist(String orderNumber);

    void saveOrderBaseInf(OrderBaseInf baseInf);
}
