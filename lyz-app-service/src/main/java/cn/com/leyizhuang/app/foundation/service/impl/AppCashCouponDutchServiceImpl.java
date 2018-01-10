package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppCashCouponType;
import cn.com.leyizhuang.app.core.exception.DutchException;
import cn.com.leyizhuang.app.foundation.dao.CashCouponDAO;
import cn.com.leyizhuang.app.foundation.dao.GoodsDAO;
import cn.com.leyizhuang.app.foundation.pojo.CustomerCashCoupon;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.service.AppCashCouponDutchService;
import cn.com.leyizhuang.common.util.CountUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 现金券分摊
 * Created by panjie on 2018/1/8.
 */
@Service
public class AppCashCouponDutchServiceImpl implements AppCashCouponDutchService {

    @Resource
    private CashCouponDAO cashCouponDAO;

    @Resource
    private GoodsDAO goodsDAO;

    @Override
    @Transactional
    public List<OrderGoodsInfo> cashCouponDutch(List<Long> cashCouponIdList, List<OrderGoodsInfo> goodsInfs) throws Exception {
        if(cashCouponIdList == null || cashCouponIdList.size() == 0){
            return goodsInfs;
        }

        if(goodsInfs == null || goodsInfs.size() == 0){
            return goodsInfs;
        }

        List<CustomerCashCoupon>  customerCashCouponList = cashCouponDAO.queryCustomerCashCouponByIdList(cashCouponIdList);
        for (CustomerCashCoupon customerCashCoupon : customerCashCouponList){
            // 现金券面额
            Double denomination = customerCashCoupon.getDenomination();

            AppCashCouponType type = customerCashCoupon.getType();

            /**
             * 通用现金券
             */
            if (type.equals(AppCashCouponType.GENERAL)){

                // 商品总价
                Double totalPrice = 0.00;
                for (OrderGoodsInfo goods : goodsInfs){
                    Integer qty = goods.getOrderQuantity();
                    Double price = goods.getSettlementPrice();

                    if(qty == null || qty < 1 || price == null || price == 0){
                        //  抛异常
                        throw new DutchException("通用现金券分摊，商品数量和单价有误！");
                    }

                    totalPrice = (price * qty) + totalPrice;
                }

                goodsInfs = dutchPrice(goodsInfs,totalPrice,denomination);

            }
            /**
             * 公司券
             */
            else if (type.equals(AppCashCouponType.COMPANY)){
                // 创建一个集合装需要分摊的商品
                List<OrderGoodsInfo> dutchGoodsInfos = new ArrayList<>();

                // 获取指定公司标识
                List<String> companys = cashCouponDAO.queryCompanyFlagsByCcid(customerCashCoupon.getCcid());

                // 商品总价
                Double totalPrice = 0.00;

                for (int i = goodsInfs.size()-1; i >=0 ; i --){
                    OrderGoodsInfo goods = goodsInfs.get(i);

                    Integer qty = goods.getOrderQuantity();
                    Double price = goods.getSettlementPrice();

                    if(qty == null || qty < 1 || price == null || price == 0){
                        //  抛异常
                        throw new DutchException("公司现金券分摊，商品数量和单价有误！");
                    }

                    // 获取商品公司标识
                    GoodsDO goodsDO = goodsDAO.findGoodsById(goods.getGid());
                    if (goodsDO == null || goodsDO.getCompanyFlag() == null || goodsDO.getCompanyFlag().equals("")){
                        // 抛异常
                        throw new DutchException("公司现金券分摊，商品公司标识不存在！");
                    }
                    String companyFlag = goodsDO.getCompanyFlag();

                    if(companys.contains(companyFlag)){
                        totalPrice = (price * qty) + totalPrice;
                        dutchGoodsInfos.add(goods);
                        goodsInfs.remove(goods);
                    }
                }

                goodsInfs.addAll(this.dutchPrice(dutchGoodsInfos,totalPrice,denomination));
            }
            /**
             * 品牌券 分摊
             */
            else if (type.equals(AppCashCouponType.BRAND)){
                // 创建一个集合装需要分摊的商品
                List<OrderGoodsInfo> dutchGoodsInfos = new ArrayList<>();

                // 获取指定品牌
                List<Long> brandIds = cashCouponDAO.queryBrandIdsByCcid(customerCashCoupon.getCcid());

                // 商品总价
                Double totalPrice = 0.00;

                for (int i = goodsInfs.size()-1; i >=0 ; i --){
                    OrderGoodsInfo goods = goodsInfs.get(i);

                    Integer qty = goods.getOrderQuantity();
                    Double price = goods.getSettlementPrice();

                    if(qty == null || qty < 1 || price == null || price == 0){
                        //  抛异常
                        throw new DutchException("品牌现金券分摊，商品数量和单价有误！");
                    }

                    // 获取商品品牌id
                    GoodsDO goodsDO = goodsDAO.findGoodsById(goods.getGid());
                    if (goodsDO == null || goodsDO.getBrdId() == null){
                        // 抛异常
                        throw new DutchException("通用现金券分摊，商品品牌id不存在！");
                    }
                    Long brandId = goodsDO.getBrdId();

                    if(brandIds.contains(brandId)){
                        totalPrice = (price * qty) + totalPrice;
                        dutchGoodsInfos.add(goods);
                        goodsInfs.remove(goods);
                    }
                }

                goodsInfs.addAll(this.dutchPrice(dutchGoodsInfos,totalPrice,denomination));
            }
            /**
             * 指定商品券
             */
            else if (type.equals(AppCashCouponType.GOODS)){
                // 创建一个集合装需要分摊的商品
                List<OrderGoodsInfo> dutchGoodsInfos = new ArrayList<>();

                // 获取指定商品ids
                List<Long> goodsIds = cashCouponDAO.queryGoodsIdsByCcid(customerCashCoupon.getCcid());

                // 商品总价
                Double totalPrice = 0.00;

                for (int i = goodsInfs.size()-1; i >=0 ; i --){
                    OrderGoodsInfo goods = goodsInfs.get(i);

                    Integer qty = goods.getOrderQuantity();
                    Double price = goods.getSettlementPrice();

                    if(qty == null || qty < 1 || price == null || price == 0){
                        // 抛异常
                        throw new DutchException("指定商品现金券分摊，商品数量或单价信息有误！");
                    }

                    if(goodsIds.contains(goods.getGid())){
                        totalPrice = (price * qty) + totalPrice;
                        dutchGoodsInfos.add(goods);
                        goodsInfs.remove(goods);
                    }
                }

                goodsInfs.addAll(this.dutchPrice(dutchGoodsInfos,totalPrice,denomination));
            }
        }

        return goodsInfs;
    }

    public List<OrderGoodsInfo> dutchPrice(List<OrderGoodsInfo> goodsInfs,Double totalPrice,Double denomination) throws Exception {

        if(goodsInfs == null || goodsInfs.size() == 0){
            throw  new DutchException("现金券分摊有误！无本品");
        }

        // 记录N-1次分摊金额 采用倒挤法
        Double dutchedPrice = 0.00;

        for (int i = 0 ; i < goodsInfs.size() ; i++){
            OrderGoodsInfo goods = goodsInfs.get(i);
            Double price = goods.getSettlementPrice();

            if (i != (goodsInfs.size()-1)){
                Double dutchPrice = CountUtil.mul((price/totalPrice),denomination);

                Double sharePrice = null == goods.getCashCouponSharePrice() ? 0.00 : goods.getCashCouponSharePrice();
                goods.setCashCouponSharePrice(dutchPrice + sharePrice);

                // 记录
                dutchedPrice += dutchPrice * goods.getOrderQuantity();
            }else{
                Double dutchPrice = CountUtil.sub(denomination,dutchedPrice);

                Double sharePrice = null == goods.getCashCouponSharePrice() ? 0.00 : goods.getCashCouponSharePrice();
                goods.setCashCouponSharePrice(CountUtil.div((dutchPrice + sharePrice),goods.getOrderQuantity()));
            }
        }

        return goodsInfs;
    }

}
