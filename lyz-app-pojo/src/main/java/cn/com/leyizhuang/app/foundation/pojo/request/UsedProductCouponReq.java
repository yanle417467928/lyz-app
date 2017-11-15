package cn.com.leyizhuang.app.foundation.pojo.request;

import cn.com.leyizhuang.app.foundation.pojo.order.GoodsSimpleInfo;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Jerry.Ren
 * Notes:使用现金券变更金额所需判断参数实体
 * Created with IntelliJ IDEA.
 * Date: 2017/11/13.
 * Time: 13:53.
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UsedProductCouponReq implements Serializable{


    /**
     * 顾客ID
     */
    private Long userId;
    /**
     * 身份类型
     */
    private Integer identityType;
    /**
     * 代下单顾客ID
     */
    private Long customerId;
    /**
     * 会员折扣
     */
    private Double memberDiscount;
    /**
     * 订单小计金额
     */
    private Double totalOrderAmount;
    /**
     * 使用的所有产品券
     */
    private List<GoodsIdQtyParam> productCouponsList;
    /**
     * 订单所有商品
     * 不在计算这儿判断没有参见促销的商品，在确认订单返回时必须判断
     */
//    private List<GoodsSimpleInfo> goodsList;
}
