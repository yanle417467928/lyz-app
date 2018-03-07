package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppGoodsLineType;
import cn.com.leyizhuang.app.core.exception.DutchException;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.service.AppCashReturnDutchService;
import cn.com.leyizhuang.common.util.CountUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by panjie on 2018/1/18.
 */
@Service
public class AppCashReturnDutchServiceImpl implements AppCashReturnDutchService{

    @Override
    public List<OrderGoodsInfo> cashReturnDutch(Double leBiAmount, List<OrderGoodsInfo> goodsInfs){

        if (leBiAmount == null || leBiAmount == 0.00){
            return goodsInfs;
        }

        if(goodsInfs == null || goodsInfs.size() == 0){
            return goodsInfs;
        }

        // 商品总价
        Double totalPrice = 0.00;
        for (OrderGoodsInfo goods : goodsInfs){
            Integer qty = goods.getOrderQuantity();
            Double price = goods.getSettlementPrice();

            if(qty == null || qty < 1 || price == null || price == 0){
                //  抛异常
                throw new DutchException("现金券分摊，商品数量和单价有误！");
            }

            if(goods.getGoodsLineType().equals(AppGoodsLineType.GOODS)){
                totalPrice = (price * qty) + totalPrice;
            }
        }

        try {
            goodsInfs = dutchPrice(goodsInfs,totalPrice,leBiAmount);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return goodsInfs;
    }

    public List<OrderGoodsInfo> dutchPrice(List<OrderGoodsInfo> goodsInfs,Double totalPrice,Double amount) throws Exception {

        // 记录N-1次分摊金额 采用倒挤法
        Double dutchedPrice = 0.00;

        for (int i = 0 ; i < goodsInfs.size() ; i++){
            OrderGoodsInfo goods = goodsInfs.get(i);

            /** 扣除促销分摊金额、乐币分摊金额 避免退货单价出现负数 **/
            Double price = CountUtil.sub(goods.getSettlementPrice(),goods.getPromotionSharePrice());

            if (i != (goodsInfs.size()-1)){
                Double dutchPrice = CountUtil.mul2((price/totalPrice),amount);

                Double sharePrice = null == goods.getCashCouponSharePrice() ? 0.00 : goods.getCashCouponSharePrice();
                goods.setCashReturnSharePrice(dutchPrice + sharePrice);

                // 记录
                dutchedPrice += CountUtil.mul(dutchPrice , goods.getOrderQuantity());
            }else{
                Double dutchPrice = CountUtil.sub(amount,dutchedPrice);

                Double sharePrice = null == goods.getCashCouponSharePrice() ? 0.00 : goods.getCashCouponSharePrice();
                goods.setCashReturnSharePrice(CountUtil.div((dutchPrice + sharePrice),goods.getOrderQuantity()));
            }
        }

        return goodsInfs;
    }
}
