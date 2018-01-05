package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.AppOrderStatus;
import cn.com.leyizhuang.app.core.constant.LogisticStatus;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderBaseInf;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponseGoods;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderArrearageInfoResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderGoodsListResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 拆单数据仓库
 * @author Richard
 * @date 2018/01/04
 */
@Repository
public interface AppSeparateOrderDAO {


    Boolean isOrderExist(@Param(value = "orderNumber") String orderNumber);

    void saveOrderBaseInf(OrderBaseInf baseInf);
}
