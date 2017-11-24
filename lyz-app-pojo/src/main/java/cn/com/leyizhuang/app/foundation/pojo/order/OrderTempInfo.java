package cn.com.leyizhuang.app.foundation.pojo.order;

import lombok.*;

/**
 * 订单信息临时类
 * @author GenerationRoad
 * @date 2017/11/23
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderTempInfo {

    //订单id
    private Long orderId;
    //订单号
    private String orderNo;
    //取货码
    private String pickUpCode;
    //订单状态
    private String orderStatus;
    //代收金额
    private Double collectionAmount;
    //欠款金额
    private Double ownMoney;
    //配送方式：送货上门、门店自提
    private String deliveryType;
    //会员姓名
    private String customerName;
    //会员电话
    private String customerPhone;
    //导购id
    private Long sellerId;
    //导购姓名
    private String sellerName;
    //导购电话
    private String sellerPhone;
    //配送地址
    private String shippingAddress;
    //操作人(配送员)编号
    private String operatorNo;

}
