package cn.com.leyizhuang.app.foundation.service.datatransfer.impl;

import cn.com.leyizhuang.app.core.constant.AppGoodsLineType;
import cn.com.leyizhuang.app.core.constant.DataTransferExceptionType;
import cn.com.leyizhuang.app.core.exception.DataTransferException;
import cn.com.leyizhuang.app.foundation.dao.GoodsDAO;
import cn.com.leyizhuang.app.foundation.dao.OrderDAO;
import cn.com.leyizhuang.app.foundation.dao.transferdao.TransferDAO;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrder;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrderGoods;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.service.datatransfer.OrderGoodsTransferService;
import cn.com.leyizhuang.common.util.CountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static cn.com.leyizhuang.app.core.constant.DataTransferExceptionType.NDT;

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

    private volatile Long transferNum = 0L;

    // 线程池
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Transactional
    public List<OrderGoodsInfo> transferOne(OrderBaseInfo orderBaseInfo,List<TdOrder> tdOrders) throws Exception {
        logger.info("开始转换订单商品数据  ≡(▔﹏▔)≡ ⊙﹏⊙∥ ˋ︿ˊ (=‵′=) 一.一 ￣﹏￣||| >﹏<~ "+orderBaseInfo.getOrderNumber());

        // 根据主单号 找到旧订单分单
        //List<TdOrder> tdOrders = transferDAO.findOrderAllFieldByOrderNumber(orderBaseInfo.getOrderNumber());

        if (tdOrders == null || tdOrders.size() == 0){
            //throw new Exception("订单商品转行异常，找不到旧订单 订单号："+ orderBaseInfo.getOrderNumber());
            throw new DataTransferException("订单商品转行异常，找不到旧订单 订单号："+ orderBaseInfo.getOrderNumber(), DataTransferExceptionType.NDT);
        }else {

            List<OrderGoodsInfo> allList = new ArrayList<>();

            for (TdOrder order : tdOrders) {
                // 新订单商品记录
                List<OrderGoodsInfo> orderGoodsInfoList = new ArrayList<>();
                List<OrderGoodsInfo> productGoodsinfo = new ArrayList<>();

                // 不处理运费单
                if (order.getOrderNumber().contains("YF")){
                    continue;
                }

                // 找到分单下的商品
                List<TdOrderGoods> tdOrderGoodsList = transferDAO.findTdorderGoodsByTdOrderId(order.getId());
                // 找到分单下的赠品
                List<TdOrderGoods> tdGiftGoodsList = transferDAO.findTdorderGoodsByPresentId(order.getId());

                // 合并
                tdOrderGoodsList.addAll(tdGiftGoodsList);

                // 转换每一个商品
                for (TdOrderGoods tdOrderGoods : tdOrderGoodsList) {
                    // 找到新商品
                    GoodsDO goodsDO = goodsDAO.queryBySku(tdOrderGoods.getSku());

                    List<OrderGoodsInfo> orderGoodsInfoList1 = this.transferOne(tdOrderGoods, order, orderBaseInfo, goodsDO);

                    if (orderGoodsInfoList1 == null || orderGoodsInfoList1.size() == 0){
                        //throw  new Exception("订单商品转行异常,商品转换异常 商品id: "+ goodsDO.getGid());
                        throw new DataTransferException("订单商品转行异常，找不到旧订单 订单号："+ orderBaseInfo.getOrderNumber(), DataTransferExceptionType.NDT);
                    }

                    for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList1) {
                        if (orderGoodsInfo.getGoodsLineType().equals(AppGoodsLineType.PRODUCT_COUPON)) {
                            productGoodsinfo.add(orderGoodsInfo);
                        } else {
                            orderGoodsInfoList.add(orderGoodsInfo);
                        }
                    }

                }

                // 分摊
                orderGoodsInfoList = dutch(orderGoodsInfoList, order);
                orderGoodsInfoList.addAll(productGoodsinfo);
                allList.addAll(orderGoodsInfoList);
            }


            //this.saveOrderGoodsInfo(orderGoodsInfoList);

            return allList;
        }
    }

    public void transferAll() {

        LocalDateTime startTime = LocalDateTime.now();
        logger.info("开始转换订单商品数据  ≡(▔﹏▔)≡ ⊙﹏⊙∥ ˋ︿ˊ (=‵′=) 一.一 ￣﹏￣||| >﹏<~");
        // 获取所有主单号
        List<OrderBaseInfo> orderList = transferDAO.findNewOrderNumber();
        logger.info("一共有订单：" + orderList.size() + "条！");

        //List<OrderBaseInfo> orderList = transferDAO.findNewOrderNumberTest();

        for (OrderBaseInfo orderBaseInfo : orderList) {
            logger.info("订单号：" + orderBaseInfo.getOrderNumber());

            // 根据主单号 找到旧订单分单
            List<TdOrder> tdOrders = transferDAO.findOrderAllFieldByOrderNumber(orderBaseInfo.getOrderNumber());


            // 新订单商品记录
            List<OrderGoodsInfo> orderGoodsInfoList = new ArrayList<>();
            List<OrderGoodsInfo> productGoodsinfo = new ArrayList<>();

            for (TdOrder order : tdOrders) {

                if (order.getOrderNumber().contains("YF")){
                    continue;
                }

                // 找到分单下的本品
                List<TdOrderGoods> tdOrderGoodsList = transferDAO.findTdorderGoodsByTdOrderId(order.getId());
                // 找到分单下的赠品
                List<TdOrderGoods> tdGiftGoodsList = transferDAO.findTdorderGoodsByPresentId(order.getId());

                // 合并
                tdOrderGoodsList.addAll(tdGiftGoodsList);

                // 转换每一个本品
                for (TdOrderGoods tdOrderGoods : tdOrderGoodsList) {
                    // 找到新商品
                    GoodsDO goodsDO = goodsDAO.queryBySku(tdOrderGoods.getSku());

                    List<OrderGoodsInfo> orderGoodsInfoList1 = this.transferOne(tdOrderGoods, order, orderBaseInfo, goodsDO);

                    for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList1) {
                        if (orderGoodsInfo.getGoodsLineType().equals(AppGoodsLineType.PRODUCT_COUPON)) {
                            productGoodsinfo.add(orderGoodsInfo);
                        } else {
                            orderGoodsInfoList.add(orderGoodsInfo);
                        }
                    }
                }

                // 分摊
                orderGoodsInfoList = dutch(orderGoodsInfoList, order);
            }


            orderGoodsInfoList.addAll(productGoodsinfo);

            // 插入
//            for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
//                // 检查是否存在
//                Boolean flag = transferDAO.isExitTdOrderGoodsLine(orderGoodsInfo.getOrderNumber(), orderGoodsInfo.getGid(), orderGoodsInfo.getGoodsLineType().getValue());
//
//                if (!flag) {
//                    transferNum ++;
//                    orderDAO.saveOrderGoodsInfo(orderGoodsInfo);
//                }
//            }

            this.saveOrderGoodsInfoAsync(orderGoodsInfoList);
            //this.saveOrderGoodsInfo(orderGoodsInfoList);
        }
        LocalDateTime endTime = LocalDateTime.now();

        Duration duration = Duration.between(startTime, endTime);
        logger.info("一共有订单：" + orderList.size() + "条！");
        logger.info("一共转换记录" + transferNum + "条,耗时：" + duration.toMinutes() + "分钟！");
        logger.info("≡(▔﹏▔)≡ ⊙﹏⊙∥ ˋ︿ˊ (=‵′=) 一.一 ￣﹏￣||| >﹏<~");
    }


    public List<OrderGoodsInfo> transferOne(TdOrderGoods tdOrderGoods, TdOrder tdOrder, OrderBaseInfo orderBaseInfo, GoodsDO goodsDO) {

        List<OrderGoodsInfo> goodsInfoList = new ArrayList<>();
        Long couponNumber = tdOrderGoods.getCouponNumber() == null ? 0L : tdOrderGoods.getCouponNumber();
        Long oderQty = tdOrderGoods.getQuantity() == null ? 0L : tdOrderGoods.getQuantity();
        Long status = tdOrder.getStatusId() == null ? 0L : tdOrder.getStatusId();
        String companyFlag = tdOrderGoods.getBrandTitle();

        if (oderQty > couponNumber) {
            OrderGoodsInfo goodsInfo = new OrderGoodsInfo();

            goodsInfo.setOid(orderBaseInfo.getId());
            goodsInfo.setOrderNumber(tdOrder.getMainOrderNumber());

            if (tdOrderGoods.getTdOrderId() != null) {
                goodsInfo.setGoodsLineType(AppGoodsLineType.GOODS);
                goodsInfo.setRetailPrice(tdOrderGoods.getPrice());
                goodsInfo.setVIPPrice(tdOrderGoods.getRealPrice());
                goodsInfo.setSettlementPrice(tdOrderGoods.getRealPrice());
                goodsInfo.setReturnPriority(1);
                goodsInfo.setWholesalePrice(tdOrderGoods.getJxPrice());
            } else if (tdOrderGoods.getPresentedListId() != null) {
                goodsInfo.setGoodsLineType(AppGoodsLineType.PRESENT);
                goodsInfo.setRetailPrice(tdOrderGoods.getGiftPrice());
                goodsInfo.setVIPPrice(tdOrderGoods.getGiftPrice());
                goodsInfo.setSettlementPrice(tdOrderGoods.getGiftPrice());
                goodsInfo.setReturnPriority(2);
                goodsInfo.setWholesalePrice(0.00);
            }
            goodsInfo.setGid(goodsDO.getGid());
            goodsInfo.setSku(tdOrderGoods.getSku());
            goodsInfo.setSkuName(tdOrderGoods.getGoodsTitle());


//            String activityId = tdOrderGoods.getActivityId();
//            if (activityId != null && activityId.contains("_")) {
//                activityId = activityId.substring(0, activityId.indexOf("_") - 1);
//                goodsInfo.setPromotionId(activityId);
//            } else {
//                goodsInfo.setPromotionId(activityId);
//            }

            goodsInfo.setIsPriceShare(true);
            goodsInfo.setPromotionSharePrice(0D);
            goodsInfo.setLbSharePrice(0D);
            goodsInfo.setCashCouponSharePrice(0D);
            goodsInfo.setCashReturnSharePrice(0D);
            goodsInfo.setReturnPrice(0D);
            goodsInfo.setIsReturnable(true);

            goodsInfo.setOrderQuantity(Integer.valueOf((int) (oderQty - couponNumber)));


            if (status <= 4) {
                goodsInfo.setShippingQuantity(0);
            } else {
                goodsInfo.setShippingQuantity(Integer.valueOf((int) (oderQty - couponNumber)));
            }

            goodsInfo.setReturnQuantity(0);
            goodsInfo.setReturnableQuantity(Integer.valueOf((int) (oderQty - couponNumber)));
            goodsInfo.setPriceItemId(null);

            if (companyFlag.contains("华润")) {
                goodsInfo.setCompanyFlag("HR");
            } else if (companyFlag.contains("乐易装")) {
                goodsInfo.setCompanyFlag("LYZ");
            } else if (companyFlag.contains("莹润")) {
                goodsInfo.setCompanyFlag("YR");
            }

            goodsInfo.setIsEvaluation(false);
            goodsInfo.setCoverImageUri(goodsDO.getCoverImageUri());

            goodsInfoList.add(goodsInfo);
        }

        if (couponNumber > 0) {
            // 产品券

            OrderGoodsInfo goodsInfo2 = new OrderGoodsInfo();

            goodsInfo2.setOid(orderBaseInfo.getId());
            goodsInfo2.setOrderNumber(tdOrder.getMainOrderNumber());

            goodsInfo2.setGoodsLineType(AppGoodsLineType.PRODUCT_COUPON);
            goodsInfo2.setRetailPrice(tdOrderGoods.getPrice());
            goodsInfo2.setVIPPrice(tdOrderGoods.getRealPrice());
            goodsInfo2.setSettlementPrice(tdOrderGoods.getRealPrice());
            goodsInfo2.setReturnPriority(1);

            goodsInfo2.setGid(goodsDO.getGid());
            goodsInfo2.setSku(tdOrderGoods.getSku());
            goodsInfo2.setSkuName(tdOrderGoods.getGoodsTitle());
            goodsInfo2.setWholesalePrice(0.00);
            goodsInfo2.setPromotionId(null);
            goodsInfo2.setIsPriceShare(false);
            goodsInfo2.setPromotionSharePrice(0D);
            goodsInfo2.setLbSharePrice(0D);
            goodsInfo2.setCashCouponSharePrice(0D);
            goodsInfo2.setCashReturnSharePrice(0D);
            goodsInfo2.setReturnPrice(0D);
            goodsInfo2.setIsReturnable(true);

            goodsInfo2.setOrderQuantity(couponNumber.intValue());

            if (status <= 4) {
                goodsInfo2.setShippingQuantity(0);
            } else {
                goodsInfo2.setShippingQuantity(Integer.valueOf((couponNumber.intValue())));
            }

            goodsInfo2.setReturnQuantity(0);
            goodsInfo2.setReturnableQuantity(Integer.valueOf(couponNumber.intValue()));
            goodsInfo2.setPriceItemId(null);
            if (companyFlag.contains("华润")) {
                goodsInfo2.setCompanyFlag("HR");
            } else if (companyFlag.contains("乐易装")) {
                goodsInfo2.setCompanyFlag("LYZ");
            } else if (companyFlag.contains("莹润")) {
                goodsInfo2.setCompanyFlag("YR");
            }
            goodsInfo2.setIsEvaluation(false);
            goodsInfo2.setCoverImageUri(goodsDO.getCoverImageUri());

            goodsInfoList.add(goodsInfo2);
        }
        return goodsInfoList;
    }

    private List<OrderGoodsInfo> dutch(List<OrderGoodsInfo> orderGoodsInfoList, TdOrder order) {

        Double totalPrice = 0D;
        Double totalActivitySubPrice = order.getActivitySubPrice() == null ? 0 : order.getActivitySubPrice();
        Double totalCashCoupon = order.getCashCoupon();

        List<OrderGoodsInfo> dutchList = new ArrayList<>();

        for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
            totalPrice = CountUtil.add(totalPrice, CountUtil.mul(orderGoodsInfo.getSettlementPrice(), orderGoodsInfo.getOrderQuantity()));
            if (orderGoodsInfo.getGoodsLineType().equals(AppGoodsLineType.PRESENT)) {
                totalActivitySubPrice = CountUtil.add(totalActivitySubPrice, CountUtil.mul(orderGoodsInfo.getSettlementPrice(), orderGoodsInfo.getOrderQuantity()));
            }
        }

        try {
            orderGoodsInfoList = this.dutchPrice(orderGoodsInfoList, totalPrice, totalActivitySubPrice, totalCashCoupon);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("订单商品转换失败，发送异常");
        }


        return orderGoodsInfoList;
    }

    public List<OrderGoodsInfo> dutchPrice(List<OrderGoodsInfo> goodsInfs, Double totalPrice, Double activitySubPrice, Double cashCouponPrice) throws Exception {
        Double dutchedPrice = 0.00;
        Double cashCouponDutchedPrice = 0.00;

        if (activitySubPrice.equals(0.00) && cashCouponPrice.equals(0.00)) {
            for (int i = 0; i < goodsInfs.size(); i++) {
                OrderGoodsInfo goods = goodsInfs.get(i);

                Double returnPrice = goods.getSettlementPrice();
                goods.setReturnPrice(returnPrice);
            }

            return goodsInfs;
        }

        for (int i = 0; i < goodsInfs.size(); i++) {

            OrderGoodsInfo goods = goodsInfs.get(i);

            Double price = goods.getSettlementPrice();

            Double dutchPrice = 0.00;
            Double cashCouponDutchPrice = 0.00;
            Double returnPrice = 0.00;
            if (i != (goodsInfs.size() - 1)) {
                dutchPrice = CountUtil.mul2((price / totalPrice), activitySubPrice);
                goods.setPromotionSharePrice(dutchPrice);

                cashCouponDutchPrice = CountUtil.mul2(((price / totalPrice)), cashCouponPrice);
                goods.setCashCouponSharePrice(cashCouponDutchPrice);

                // 记录
                dutchedPrice = CountUtil.add(CountUtil.mul(dutchPrice, goods.getOrderQuantity()),dutchedPrice);
                cashCouponDutchedPrice = CountUtil.add(CountUtil.mul(cashCouponDutchPrice, goods.getOrderQuantity()),cashCouponDutchedPrice);

                returnPrice = CountUtil.sub(goods.getSettlementPrice(),dutchPrice,cashCouponDutchPrice);
            } else {
                dutchPrice = CountUtil.sub(activitySubPrice, dutchedPrice);
                cashCouponDutchPrice = CountUtil.sub(cashCouponPrice, cashCouponDutchedPrice);

                goods.setPromotionSharePrice(CountUtil.div((dutchPrice), goods.getOrderQuantity()));
                goods.setCashCouponSharePrice(CountUtil.div(cashCouponDutchPrice, goods.getOrderQuantity()));

                returnPrice = CountUtil.sub(goods.getSettlementPrice(),CountUtil.div((dutchPrice), goods.getOrderQuantity()),CountUtil.div(cashCouponDutchPrice, goods.getOrderQuantity()));
            }

            goods.setReturnPrice(returnPrice);
        }

        return goodsInfs;
    }

    private void saveOrderGoodsInfo(final List<OrderGoodsInfo> orderGoodsInfoList) {
        Integer num = 0;
        // 插入
        for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
            // 检查是否存在
            Boolean flag = transferDAO.isExitTdOrderGoodsLine(orderGoodsInfo.getOrderNumber(), orderGoodsInfo.getGid(), orderGoodsInfo.getGoodsLineType().getValue());

            if (!flag) {
                num ++;
                orderDAO.saveOrderGoodsInfo(orderGoodsInfo);
            }
        }
        logger.info("条数："+ num);
    }

    private void saveOrderGoodsInfoAsync(final List<OrderGoodsInfo> orderGoodsInfoList) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                // 插入
                for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                    // 检查是否存在
                    Boolean flag = transferDAO.isExitTdOrderGoodsLine(orderGoodsInfo.getOrderNumber(), orderGoodsInfo.getGid(), orderGoodsInfo.getGoodsLineType().getValue());
                    if (!flag) {
                        transferNum++;
                        orderDAO.saveOrderGoodsInfo(orderGoodsInfo);
                    }
                }

                logger.info("条数："+ transferNum);
            }
        });

    }
}
