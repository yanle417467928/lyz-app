package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppRechargeOrderStatus;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeReceiptInfo;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderBaseInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderCouponInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderGoodsInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderReceiptInf;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.DeliverySimpleInfo;
import cn.com.leyizhuang.app.foundation.service.AppSeparateOrderService;
import cn.com.leyizhuang.app.foundation.service.CommonService;
import cn.com.leyizhuang.app.foundation.service.RechargeService;
import cn.com.leyizhuang.app.foundation.service.TransactionalSupportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 事务辅助实现类
 *
 * @author Richard
 * Created on 2017-12-28 15:07
 **/
@Service
public class TransactionalSupportServiceImpl implements TransactionalSupportService {

    @Resource
    private CommonService commonService;

    @Resource
    private AppSeparateOrderService separateOrderService;

    @Resource
    private RechargeService rechargeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrderBusiness(DeliverySimpleInfo deliverySimpleInfo, Map<Long, Integer> inventoryCheckMap, Long cityId, Integer identityType,
                                    Long userId, Long customerId, List<Long> cashCouponList, List<OrderCouponInfo> orderProductCouponInfoList,
                                    OrderBillingDetails orderBillingDetails, OrderBaseInfo orderBaseInfo, OrderLogisticsInfo orderLogisticsInfo,
                                    List<OrderGoodsInfo> orderGoodsInfoList, List<OrderCouponInfo> orderCouponInfoList, List<OrderBillingPaymentDetails> paymentDetails, String ipAddress) {
        //******* 检查库存和与账单支付金额是否充足,如果充足就扣减相应的数量
        commonService.reduceInventoryAndMoney(deliverySimpleInfo, inventoryCheckMap, cityId, identityType,
                userId, customerId, cashCouponList, orderProductCouponInfoList, orderBillingDetails, orderBaseInfo.getOrderNumber(), ipAddress);
        //******* 持久化订单相关实体信息  *******
        commonService.saveAndHandleOrderRelevantInfo(orderBaseInfo, orderLogisticsInfo, orderGoodsInfoList, orderCouponInfoList, orderBillingDetails, paymentDetails);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSeparateOrderRelevatnInf(List<OrderBaseInf> orderBaseInfList, List<OrderGoodsInf> orderGoodsInfList,
                                             List<OrderCouponInf> couponInfList, List<OrderReceiptInf> receiptInfList) {
        //循环保存分单基础信息
        for (OrderBaseInf baseInf : orderBaseInfList) {
            separateOrderService.saveOrderBaseInf(baseInf);
            for (OrderGoodsInf info : orderGoodsInfList
                    ) {
                if (info.getProductType() == baseInf.getProductType()) {
                    info.setOrderHeaderId(baseInf.getOrderHeaderId());
                }

            }
        }
        //保存分单商品信息
        for (OrderGoodsInf goodsInf : orderGoodsInfList) {
            separateOrderService.saveOrderGoodsInf(goodsInf);
        }

        //保存订单券信息
        for (OrderCouponInf couponInf : couponInfList) {
            separateOrderService.saveOrderCouponInf(couponInf);
        }

        //保存收款信息
        for (OrderReceiptInf receiptInf : receiptInfList) {
            separateOrderService.saveOrderReceiptInf(receiptInf);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleRechargeOrderRelevantInfoAfterOnlinePauUp(RechargeReceiptInfo receiptInfo, String rechargeNo) {
        rechargeService.saveRechargeReceiptInfo(receiptInfo);
        //更新充值单相关信息
        rechargeService.updateRechargeOrderStatusAndPayUpTime(rechargeNo, new Date(), AppRechargeOrderStatus.PAID);
    }
}
