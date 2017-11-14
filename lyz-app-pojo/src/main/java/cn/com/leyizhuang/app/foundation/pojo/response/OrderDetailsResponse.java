package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import lombok.*;

/**
 * 订单详情返回类
 * Created by caiyu on 2017/11/14.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsResponse {
    private String orderNumber;
    //订单创建时间
    private String createTime;
    //订单类型
    private String orderType;
    //订单头状态
    private String status;
    //订单物流状态
    private String deliveryStatus;
    //配送方式
    private String deliveryType;
    //收货地址全称
    private String shippingAddress;
    //运费
    private Double freight;
    //会员折扣
    private Double memberDiscount;
    //促销折扣
    private Double promotionDiscount;
    //乐币抵现金额
    private Double leBiCashDiscount;
    //优惠券抵现金额
    private Double couponDiscount;
    //产品券抵现金额
    private Double productCouponDiscount;
    //应付款
    private Double amountPayable;
    //现金返利
    private Double subvention;
    //信用金
    private Double creditMoney;
    //商品总金额
    private Double totalPrice;

}
