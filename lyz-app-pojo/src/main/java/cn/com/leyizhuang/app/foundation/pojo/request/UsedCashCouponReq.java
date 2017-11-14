package cn.com.leyizhuang.app.foundation.pojo.request;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
public class UsedCashCouponReq implements Serializable {

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
     * 总价格
     */
//    private Double totalPrice;
    /**
     * 订单小计金额
     */
    private Double totalOrderAmount;
    /**
     * 订单折扣
     */
    private Double orderDiscount;
    /**
     * 使用的现金券ID和数量
     */
    private List<GoodsIdQtyParam> cashCouponsList;

}
