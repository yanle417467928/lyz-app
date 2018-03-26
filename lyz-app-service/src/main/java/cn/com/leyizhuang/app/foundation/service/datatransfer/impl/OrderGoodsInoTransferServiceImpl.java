package cn.com.leyizhuang.app.foundation.service.datatransfer.impl;

import cn.com.leyizhuang.app.core.constant.AppGoodsLineType;
import cn.com.leyizhuang.app.core.exception.DutchException;
import cn.com.leyizhuang.app.foundation.dao.GoodsDAO;
import cn.com.leyizhuang.app.foundation.dao.OrderDAO;
import cn.com.leyizhuang.app.foundation.dao.transferdao.TransferDAO;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrder;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrderGoods;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.service.datatransfer.OrderGoodsTransferService;
import cn.com.leyizhuang.app.foundation.service.impl.AppActServiceImpl;
import cn.com.leyizhuang.common.util.CountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单商品转换类
 * Created by panjie on 2018/3/24.
 */
@Service
public class OrderGoodsInoTransferServiceImpl implements OrderGoodsTransferService {

    private static final Logger logger = LoggerFactory.getLogger(OrderGoodsInoTransferServiceImpl.class);

    @Autowired
    private TransferDAO transferDAO;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private GoodsDAO goodsDAO;


    public void transferAll() {

        // 获取所有主单号
        List<OrderBaseInfo> orderList = transferDAO.findNewOrderNumber();

        //List<OrderBaseInfo> orderList = transferDAO.findNewOrderNumberTest();

        for (OrderBaseInfo orderBaseInfo : orderList) {
            // 根据主单号 找到旧订单分单
            List<TdOrder> tdOrders = transferDAO.findOrderByOrderNumber(orderBaseInfo.getOrderNumber());


            // 新订单商品记录
            List<OrderGoodsInfo> orderGoodsInfoList = new ArrayList<>();

            for (TdOrder order : tdOrders) {
                // 找到分单下的商品

                List<TdOrderGoods> tdOrderGoodsList = transferDAO.findTdorderGoodsByTdOrderId(order.getId());

                // 转换每一个商品
                for (TdOrderGoods tdOrderGoods : tdOrderGoodsList) {
                    // 找到新商品
                    GoodsDO goodsDO = goodsDAO.queryBySku(tdOrderGoods.getSku());

                    List<OrderGoodsInfo> orderGoodsInfoList1 = this.transferOne(tdOrderGoods, order, orderBaseInfo, goodsDO);

                    orderGoodsInfoList.addAll(orderGoodsInfoList1);

                }
            }

            // 插入
            for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                // 检查是否存在
                Boolean flag = transferDAO.isExitTdOrderGoodsLine(orderGoodsInfo.getOrderNumber(), orderGoodsInfo.getGid(), orderGoodsInfo.getGoodsLineType().getValue());

                if (!flag) {
                    orderDAO.saveOrderGoodsInfo(orderGoodsInfo);
                }
            }
        }


    }


    public List<OrderGoodsInfo> transferOne(TdOrderGoods tdOrderGoods, TdOrder tdOrder, OrderBaseInfo orderBaseInfo, GoodsDO goodsDO) {

        List<OrderGoodsInfo> goodsInfoList = new ArrayList<>();
        OrderGoodsInfo goodsInfo = new OrderGoodsInfo();

        goodsInfo.setOid(orderBaseInfo.getId());
        goodsInfo.setOrderNumber(tdOrder.getMainOrderNumber());

        if (tdOrderGoods.getTdOrderId() != null) {
            goodsInfo.setGoodsLineType(AppGoodsLineType.GOODS);
            goodsInfo.setRetailPrice(tdOrderGoods.getPrice());
            goodsInfo.setVIPPrice(tdOrderGoods.getPrice());
            goodsInfo.setSettlementPrice(tdOrderGoods.getPrice());
            goodsInfo.setReturnPriority(1);

        } else if (tdOrderGoods.getPresentedListId() != null) {
            goodsInfo.setGoodsLineType(AppGoodsLineType.PRESENT);
            goodsInfo.setRetailPrice(tdOrderGoods.getGiftPrice());
            goodsInfo.setVIPPrice(tdOrderGoods.getGiftPrice());
            goodsInfo.setSettlementPrice(tdOrderGoods.getGiftPrice());
            goodsInfo.setReturnPriority(2);
        }
        goodsInfo.setGid(goodsDO.getGid());
        goodsInfo.setSku(tdOrderGoods.getSku());
        goodsInfo.setSkuName(tdOrderGoods.getGoodsTitle());
        goodsInfo.setWholesalePrice(tdOrderGoods.getJxPrice());
        goodsInfo.setPromotionId(tdOrderGoods.getActivityId());
        goodsInfo.setIsPriceShare(true);
        goodsInfo.setPromotionSharePrice(0D);
        goodsInfo.setLbSharePrice(0D);
        goodsInfo.setCashCouponSharePrice(0D);
        goodsInfo.setCashReturnSharePrice(0D);
        goodsInfo.setRetailPrice(0D);
        goodsInfo.setIsReturnable(true);

        Long couponNumber = tdOrderGoods.getCouponNumber() == null ? 0L : tdOrderGoods.getCouponNumber();
        Long oderQty = tdOrderGoods.getQuantity() == null ? 0L : tdOrderGoods.getQuantity();
        goodsInfo.setOrderQuantity(Integer.valueOf((int) (oderQty - couponNumber)));

        Long status = tdOrder.getStatusId() == null ? 0L : tdOrder.getStatusId();
        if (status <= 4) {
            goodsInfo.setShippingQuantity(0);
        } else {
            goodsInfo.setShippingQuantity(Integer.valueOf((int) (oderQty - couponNumber)));
        }

        goodsInfo.setReturnQuantity(0);
        goodsInfo.setReturnableQuantity(oderQty.intValue());
        goodsInfo.setPriceItemId(null);
        goodsInfo.setCompanyFlag(tdOrderGoods.getBrandTitle());
        goodsInfo.setIsEvaluation(false);
        goodsInfo.setCoverImageUri(tdOrderGoods.getGoodsCoverImageUri());

        goodsInfoList.add(goodsInfo);

        if (couponNumber > 0) {
            // 产品券

            OrderGoodsInfo goodsInfo2 = new OrderGoodsInfo();

            goodsInfo2.setOid(orderBaseInfo.getId());
            goodsInfo2.setOrderNumber(tdOrder.getMainOrderNumber());

            goodsInfo2.setGoodsLineType(AppGoodsLineType.PRODUCT_COUPON);
            goodsInfo2.setRetailPrice(tdOrderGoods.getPrice());
            goodsInfo2.setVIPPrice(tdOrderGoods.getPrice());
            goodsInfo2.setSettlementPrice(tdOrderGoods.getPrice());
            goodsInfo2.setReturnPriority(1);

            goodsInfo2.setGid(goodsDO.getGid());
            goodsInfo2.setSku(tdOrderGoods.getSku());
            goodsInfo2.setSkuName(tdOrderGoods.getGoodsTitle());
            goodsInfo2.setWholesalePrice(tdOrderGoods.getJxPrice());
            goodsInfo2.setPromotionId(tdOrderGoods.getActivityId());
            goodsInfo2.setIsPriceShare(true);
            goodsInfo2.setPromotionSharePrice(0D);
            goodsInfo2.setLbSharePrice(0D);
            goodsInfo2.setCashCouponSharePrice(0D);
            goodsInfo2.setCashReturnSharePrice(0D);
            goodsInfo2.setRetailPrice(0D);
            goodsInfo2.setIsReturnable(true);

            goodsInfo2.setOrderQuantity(couponNumber.intValue());

            if (status <= 4) {
                goodsInfo2.setShippingQuantity(0);
            } else {
                goodsInfo2.setShippingQuantity(Integer.valueOf((couponNumber.intValue())));
            }

            goodsInfo2.setReturnQuantity(0);
            goodsInfo2.setReturnableQuantity(oderQty.intValue());
            goodsInfo2.setPriceItemId(null);
            goodsInfo2.setCompanyFlag(tdOrderGoods.getBrandTitle());
            goodsInfo2.setIsEvaluation(false);
            goodsInfo2.setCoverImageUri(tdOrderGoods.getGoodsCoverImageUri());

            goodsInfoList.add(goodsInfo2);
        }
        return goodsInfoList;
    }

    private List<OrderGoodsInfo> dutch(List<OrderGoodsInfo> orderGoodsInfoList, TdOrder order) {

        Double totalPrice = 0D;
        Double totalActivitySubPrice = order.getActivitySubPrice() == null ? 0 : order.getActivitySubPrice();
        Double totalCashCoupon = order.getCashCoupon();
        List<OrderGoodsInfo> dutchOrderGoodsList = new ArrayList<>();

        for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
            if (!orderGoodsInfo.getGoodsLineType().equals(AppGoodsLineType.PRODUCT_COUPON)) {
                dutchOrderGoodsList.add(orderGoodsInfo);

                totalPrice = CountUtil.add(totalPrice,CountUtil.mul(orderGoodsInfo.getSettlementPrice(),orderGoodsInfo.getOrderQuantity()));
                if (orderGoodsInfo.getGoodsLineType().equals(AppGoodsLineType.PRESENT)) {
                    totalActivitySubPrice = CountUtil.add(totalActivitySubPrice, CountUtil.mul(orderGoodsInfo.getSettlementPrice(),orderGoodsInfo.getOrderQuantity()));
                }
            }
        }

        orderGoodsInfoList.removeAll(dutchOrderGoodsList);
        try {
            orderGoodsInfoList.addAll(this.dutchPrice(orderGoodsInfoList,totalPrice,totalActivitySubPrice,totalCashCoupon));
        } catch (Exception e) {
            e.printStackTrace();
        }


        return orderGoodsInfoList;
    }

    public List<OrderGoodsInfo> dutchPrice(List<OrderGoodsInfo> goodsInfs, Double totalPrice, Double activitySubPrice, Double cashCouponPrice) throws Exception {
        Double dutchedPrice = 0.00;
        Double cashCouponDutchedPrice = 0.00;

        if (activitySubPrice == 0.00 && cashCouponPrice == 0.00){
            return goodsInfs;
        }

        for (int i = 0; i < goodsInfs.size(); i++) {
            OrderGoodsInfo goods = goodsInfs.get(i);

            Double price = goods.getPromotionSharePrice();

            Double dutchPrice = 0.00;
            Double cashCouponDutchPrice = 0.00;

            if (i != (goodsInfs.size() - 1)) {
                dutchPrice = CountUtil.mul2((price / totalPrice), activitySubPrice);
                goods.setPromotionSharePrice(dutchPrice);

                cashCouponDutchPrice = CountUtil.mul2(((price / totalPrice)), cashCouponDutchedPrice);
                goods.setCashCouponSharePrice(cashCouponDutchPrice);

                // 记录
                dutchedPrice += CountUtil.mul(dutchPrice, goods.getOrderQuantity());
                cashCouponDutchedPrice += CountUtil.mul(cashCouponDutchPrice,goods.getOrderQuantity());


            } else {
                dutchPrice = CountUtil.sub(activitySubPrice, dutchedPrice);
                cashCouponDutchPrice = CountUtil.sub(cashCouponPrice,cashCouponDutchedPrice);

                goods.setPromotionSharePrice(CountUtil.div((dutchPrice), goods.getOrderQuantity()));
                goods.setCashCouponSharePrice(CountUtil.div(cashCouponDutchPrice,goods.getOrderQuantity()));
            }

            Double returnPrice = CountUtil.div(goods.getSettlementPrice(),dutchedPrice,cashCouponDutchPrice);
            goods.setReturnPrice(returnPrice);
        }

        return goodsInfs;
    }
}
