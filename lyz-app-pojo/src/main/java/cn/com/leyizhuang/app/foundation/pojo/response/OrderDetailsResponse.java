package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

import java.util.List;

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
    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 订单创建时间
     */
    private String createTime;
    /**
     * 订单头状态
     */
    private String status;
    /**
     * 支付方式
     */
    private String payType;
    /**
     * 配送方式
     */
    private String deliveryType;
    /**
     * 配送时间
     */
    private String deliveryTime;
    /**
     * 收货人姓名
     */
    private String receiver;
    /**
     * 收货人电话
     */
    private String receiverPhone;
    /**
     * 收货地址全称
     */
    private String shippingAddress;
    /**
     * 预约门店名称
     */
    private String bookingStoreName;
    /**
     * 预约门店电话
     */
    private String bookingStorePhone;
    /**
     * 门店详细地址
     */
    private String storeDetailedAddress;
    /**
     * 预约提货时间
     */
    private String bookingTime;
    /**
     * 导购账目明细
     */
    private SellerBillingDetailResponse sellerBillingDetailResponse;
    /**
     * 经理账目明细
     */
    private ManagerBillingDetailResponse managerBillingDetailResponse;
    /**
     * 会员账目明细
     */
    private CustomerBillingDetailResponse customerBillingDetailResponse;
    /**
     * 商品list
     */
    private List<GiftListResponseGoods> goodsList;


}
