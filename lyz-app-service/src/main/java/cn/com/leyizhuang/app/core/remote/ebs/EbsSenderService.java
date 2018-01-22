package cn.com.leyizhuang.app.core.remote.ebs;

import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.*;

import java.util.List;

/**
 * EBS接口发送服务
 *
 * @author Richard
 * Created on 2018-01-13 14:22
 **/
public interface EbsSenderService {

    void sendOrderAndGoodsToEbsAndRecord(final OrderBaseInf orderInf, final List<OrderGoodsInf> goodsInfs);

    void sendOrderReceiptInfAndRecord(List<OrderReceiptInf> receiptInfs);

    void sendOrderCouponInfAndRecord(List<OrderCouponInf> orderCouponInfs);

    void sendRechargeReceiptInfAndRecord(RechargeReceiptInf receiptInf);

    void sendOrderJxPriceDifferenceReturnInfAndRecord(List<OrderJxPriceDifferenceReturnInf> jxPriceDifferenceReturnInfs);
}
