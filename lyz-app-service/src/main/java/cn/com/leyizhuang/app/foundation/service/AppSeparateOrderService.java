package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderBaseInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderCouponInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderGoodsInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderReceiptInf;

import java.util.Date;
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

    void saveOrderGoodsInf(OrderGoodsInf goodsInf);

    void separateOrder(String orderNumber);

    void saveOrderCouponInf(OrderCouponInf couponInf);

    void saveOrderReceiptInf(OrderReceiptInf receiptInf);

    void updateOrderBaseInfoSendFlagAndErrorMessageAndSendTime(String orderNumber, AppWhetherFlag flag, String errorMsg, Date sendTime);

    void updateOrderGoodsInfoSendFlagAndErrorMessageAndSendTime(Long orderLineId, AppWhetherFlag flag, String errorMsg, Date sendTime);

    void sendOrderBaseInfAndOrderGoodsInf(String orderNumber);

    void sendOrderReceiptInf(String orderNumber);

    void sendOrderCouponInf(String orderNumber);

    void updateOrderCouponFlagAndSendTimeAndErrorMsg(List<Long> couponInfIds, String msg, Date sendTime, AppWhetherFlag flag);

    void updateOrderGoodsInfByOrderNumber(String orderNumber, AppWhetherFlag flag, String message, Date sendTime);

    void updateOrderReceiptFlagAndSendTimeAndErrorMsg(List<Long> receiptInfIds, String msg, Date sendTime, AppWhetherFlag flag);
}
