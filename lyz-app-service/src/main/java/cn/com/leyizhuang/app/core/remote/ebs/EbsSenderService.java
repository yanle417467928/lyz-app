package cn.com.leyizhuang.app.core.remote.ebs;

import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.Allocation;
import cn.com.leyizhuang.app.foundation.pojo.management.returnOrder.MaStoreReturnOrderAppToEbsBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.webservice.ebs.MaOrderReceiveInf;
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

    /**
     * 发送【调拨单(出库)】信息到EBS，并保存发送结果
     * @param allocation
     */
    void sendAllocationToEBSAndRecord(final Allocation allocation);

    /**
     * 发送【调拨单(入库)】信息到EBS
     * @param allocation
     */
    void sendAllocationReceivedToEBSAndRecord(final Allocation allocation);


    void sendOrderJxPriceDifferenceReturnInfAndRecord(List<OrderJxPriceDifferenceReturnInf> jxPriceDifferenceReturnInfs);

   /* void sendOrderJxPriceDifferenceRefundInfAndRecord(List<ReturnOrderJxPriceDifferenceRefundInf> jxPriceDifferenceRefundInfs);*/

    void sendOrderReceiveInfAndRecord(MaOrderReceiveInf receiveInfs);

    void sendReturnOrderReceiptInfAndRecord( MaStoreReturnOrderAppToEbsBaseInfo maStoreReturnOrderAppToEbsBaseInfo);

    void sendReturnOrderAndReturnGoodsToEbsAndRecord(ReturnOrderBaseInf baseInf, List<ReturnOrderGoodsInf> returnOrderGoodsInfList);

    void sendReturnOrderCouponInfAndRecord(List<ReturnOrderCouponInf> returnOrderCouponInfList);

    void sendReturnOrderRefundInfAndRecord(List<ReturnOrderRefundInf> returnOrderRefundInfList);

    void sendReturnOrderJxPriceDifferenceRefundInfAndRecord(List<ReturnOrderJxPriceDifferenceRefundInf> jxPriceDifferenceRefundInfs);

    void sendOrderKeyInfAndRecord(OrderKeyInf orderFreightInf);
}
