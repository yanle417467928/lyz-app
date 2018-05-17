package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.core.constant.AppOrderStatus;
import cn.com.leyizhuang.app.foundation.pojo.request.CustomerSimpleInfo;
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
     * 代下单顾客信息
     */
    private CustomerSimpleInfo customer;
    /**
     * 订单创建时间
     */
    private String createTime;
    /**
     * 订单头状态
     */
    private AppOrderStatus status;
    /**
     * 订单状态描述
     */
    private String statusDesc;
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
     * 楼盘信息
     */
    private String estateInfo;
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
     * 订单是否已评价
     */
    private Boolean isEvaluated;
    /**
     * 产品星级
     */
    private int productStar;
    /**
     * 物流星级
     */
    private int logisticsStar;
    /**
     * 服务星级
     */
    private int serviceStars;
    /**
     * 商品list
     */
    private List<OrderGoodsListResponse> goodsList;
    /**
     * 取货码
     */
    private String pickUpCode;

    //************导购获取详情***********
    /**
     * 是否主家收货
     */
    private Boolean isOwnerReceiving;
    /**
     * 顾客姓名
     */
    private String customerName;
    /**
     * 顾客电话
     */
    private String customerPhone;

    /**
     * 订单备注
     */
    private String remark;
    //门店名称（装饰公司名称）
    private String storeName;
}
