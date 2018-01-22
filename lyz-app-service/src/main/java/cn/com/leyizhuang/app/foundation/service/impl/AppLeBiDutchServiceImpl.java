package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppConstant;
import cn.com.leyizhuang.app.core.exception.DutchException;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.service.AppLeBiDutchService;
import cn.com.leyizhuang.common.util.CountUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by panjie on 2018/1/18.
 */
@Service
public class AppLeBiDutchServiceImpl implements AppLeBiDutchService {

     @Override
     public List<OrderGoodsInfo> LeBiDutch(Integer leBiQty,List<OrderGoodsInfo> goodsInfs){

         if (leBiQty == null || leBiQty == 0.00){
             return goodsInfs;
         }

         if(goodsInfs == null || goodsInfs.size() == 0){
             return goodsInfs;
         }

         // 商品总价
         Double totalPrice = 0.00;
         // 乐币汇率
         Double ratio = AppConstant.RMB_TO_LEBI_RATIO;

         Double subAmount = CountUtil.div(leBiQty,ratio);
         for (OrderGoodsInfo goods : goodsInfs){
             Integer qty = goods.getOrderQuantity();
             Double price = goods.getSettlementPrice();

             if(qty == null || qty < 1 || price == null || price == 0){
                 //  抛异常
                 throw new DutchException("乐币分摊，商品数量和单价有误！");
             }

             totalPrice = (price * qty) + totalPrice;
         }

         try {
             goodsInfs = dutchPrice(goodsInfs,totalPrice,subAmount);
         } catch (Exception e) {
             e.printStackTrace();
         }


         return goodsInfs;
     }

    public List<OrderGoodsInfo> dutchPrice(List<OrderGoodsInfo> goodsInfs,Double totalPrice,Double leBiAmount) throws Exception {

        // 记录N-1次分摊金额 采用倒挤法
        Double dutchedPrice = 0.00;

        for (int i = 0 ; i < goodsInfs.size() ; i++){
            OrderGoodsInfo goods = goodsInfs.get(i);
            Double price = goods.getSettlementPrice();

            if (i != (goodsInfs.size()-1)){
                Double dutchPrice = CountUtil.mul((price/totalPrice),leBiAmount);

                Double sharePrice = null == goods.getCashCouponSharePrice() ? 0.00 : goods.getCashCouponSharePrice();
                goods.setLbSharePrice(dutchPrice + sharePrice);

                // 记录
                dutchedPrice += CountUtil.mul(dutchPrice , goods.getOrderQuantity());
            }else{
                Double dutchPrice = CountUtil.sub(leBiAmount,dutchedPrice);

                Double sharePrice = null == goods.getCashCouponSharePrice() ? 0.00 : goods.getCashCouponSharePrice();
                goods.setLbSharePrice(CountUtil.div((dutchPrice + sharePrice),goods.getOrderQuantity()));
            }
        }

        return goodsInfs;
    }
}
