package cn.com.leyizhuang.app.core.remote.ebs;

import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderBaseInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderCouponInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderGoodsInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderReceiptInf;

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
}
