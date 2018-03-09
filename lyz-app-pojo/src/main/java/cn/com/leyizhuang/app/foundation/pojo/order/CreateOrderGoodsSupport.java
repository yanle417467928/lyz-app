package cn.com.leyizhuang.app.foundation.pojo.order;

import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderGoodsInf;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建订单商品信息辅助类
 *
 * @author Richard
 * Created on 2017-12-28 16:07
 **/
@Getter
@Setter
@ToString
public class CreateOrderGoodsSupport implements Serializable {

    private static final long serialVersionUID = -50925446210356708L;

    /**
     * 订单商品信息（包含本品+产品券商品）
     */
    List<OrderGoodsInfo> orderGoodsInfoList = new ArrayList<>();
    /**
     * 订单产品券商品信息
     */
    List<OrderGoodsInfo> productCouponGoodsList = new ArrayList<>();

    /**
     * 订单商品本品信息
     */
    List<OrderGoodsInfo> pureOrderGoodsInfo = new ArrayList<>();
    /**
     * 用来存放最终要检核库存的商品和商品数量的Map
     */
    Map<Long, Integer> inventoryCheckMap = new HashMap<>();
    /**
     * 订单商品零售总价
     */
    Double goodsTotalPrice = 0D;
    /**
     * 订单会员折扣
     */
    Double memberDiscount = 0D;
    /**
     * 订单促销折扣
     */
    Double promotionDiscount = 0D;
}
