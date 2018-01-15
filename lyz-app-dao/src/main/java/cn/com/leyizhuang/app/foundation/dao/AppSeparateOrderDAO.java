package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderBaseInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderCouponInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderGoodsInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderReceiptInf;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 拆单数据仓库
 *
 * @author Richard
 * @date 2018/01/04
 */
@Repository
public interface AppSeparateOrderDAO {


    Boolean isOrderExist(@Param(value = "orderNumber") String orderNumber);

    void saveOrderBaseInf(OrderBaseInf baseInf);

    void saveOrderGoodsInf(OrderGoodsInf goodsInf);

    void saveOrderCouponInf(OrderCouponInf couponInf);

    void saveOrderReceiptInf(OrderReceiptInf receiptInf);

    void updateOrderBaseInfoSendFlagAndErrorMessageAndSendTime(@Param(value = "orderNumber") String orderNumber,
                                                               @Param(value = "flag") AppWhetherFlag flag,
                                                               @Param(value = "errorMsg") String errorMsg,
                                                               @Param(value = "sendTime") Date sendTime);

    void updateOrderGoodsInfoSendFlagAndErrorMessageAndSendTime(@Param(value = "orderLineId") Long orderLineId,
                                                                @Param(value = "flag") AppWhetherFlag flag,
                                                                @Param(value = "errorMsg") String errorMsg,
                                                                @Param(value = "sendTime") Date sendTime);

    List<OrderBaseInf> getPendingSendOrderBaseInf(String orderNumber);

    List<OrderGoodsInf> getOrderGoodsInfByOrderNumber(String orderNumber);
}
