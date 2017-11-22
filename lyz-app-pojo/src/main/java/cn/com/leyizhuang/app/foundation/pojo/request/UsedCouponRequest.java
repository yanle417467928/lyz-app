package cn.com.leyizhuang.app.foundation.pojo.request;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Jerry.Ren
 * Notes:使用现金券变更金额所需判断参数实体
 * Created with IntelliJ IDEA.
 * Date: 2017/11/13.
 * Time: 13:49.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UsedCouponRequest implements Serializable {

    /**
     * 顾客ID
     */
    private Long userId;
    /**
     * 代下单顾客ID
     */
    private Long customerId;
    /**
     * 身份类型
     */
    private Integer identityType;
    /**
     * 订单小计金额
     */
    private Double totalOrderAmount;
    /**
     * 会员折扣
     */
    private Double memberDiscount;
    /**
     * 使用的优惠券券ID和数量
     */
    private List<GoodsIdQtyParam> couponsList;
    /**
     * 使用的产品券ID和数量
     */
    private List<GoodsIdQtyParam> proCouponList;

}
