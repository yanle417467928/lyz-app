package cn.com.leyizhuang.app.foundation.vo.management.order;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.AppOrderStatus;
import cn.com.leyizhuang.app.foundation.vo.management.goodscategory.MaOrderGoodsDetailResponse;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * 后台装饰公司订单详情返回类
 * Created by caiyu on 2017/12/28.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaCompanyOrderDetailResponse {
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
     * 装饰公司名称
     */
    private String companyName;
    /**
     * 装饰公司编码
     */
    private String companyCode;
    /**
     * 下单人id
     */
    private Long creatorId;
    /**
     * 下单人姓名
     */
    private String creatorName;
    /**
     * 关联料单审核单号
     */
    private String auditNumber;
    /**
     * 配送方式
     */
    private AppDeliveryType deliveryType;
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
     * 订单备注
     */
    private String orderRemarks;
    /**
     * 订单商品
     */
    private List<MaOrderGoodsDetailResponse> maOrderGoodsDetailResponseList;



}
