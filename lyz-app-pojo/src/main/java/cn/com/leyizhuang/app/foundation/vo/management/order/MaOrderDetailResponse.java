package cn.com.leyizhuang.app.foundation.vo.management.order;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.AppOrderStatus;
import cn.com.leyizhuang.app.foundation.vo.management.goodscategory.MaOrderGoodsDetailResponse;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * 后台订单详情返回类
 * Created by caiyu on 2017/12/28.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaOrderDetailResponse {
    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 订单创建时间
     */
    private Date createTime;
    /**
     * 订单状态
     */
    private AppOrderStatus orderStatus;
    /**
     * 门店名称
     */
    private String storeName;
    /**
     * 导购姓名
     */
    private String sellerName;
    /**
     * 下单人id
     */
    private Long creatorId;
    /**
     * 下单人姓名
     */
    private String creatorName;
    /**
     * 顾客id
     */
    private Long customerId;
    /**
     * 顾客姓名
     */
    private String customerName;
    /**
     * 配送方式
     */
    private AppDeliveryType deliveryType;
    /**
     * 下单人身份类型
     */
    private AppIdentityType creatorIdentityType;

    /**
     * 收货人姓名
     */
    private String receiverName;
    /**
     * 收货人电话
     */
    private String receiverPhone;
    /**
     * 收货地址
     */
    private String shippingAddress;
    /**
     * 是否主家收货
     */
    private Boolean isOwnerReceiving;
    /**
     * 代收金额
     */
    private Double collectionAmount;
    /**
     * 发货时间（门店自提）
     */
    private Date shipTime;
    /**
     * 发货门店（门店自提）
     */
    private String shipStore;
    /**
     * 订单备注
     */
    private String orderRemarks;
    /**
     * 订单商品
     */
    private List<MaOrderGoodsDetailResponse> maOrderGoodsDetailResponseList;


}
