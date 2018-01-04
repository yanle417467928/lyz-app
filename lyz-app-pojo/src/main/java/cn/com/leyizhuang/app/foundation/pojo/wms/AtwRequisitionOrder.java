package cn.com.leyizhuang.app.foundation.pojo.wms;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderLogisticsInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Jerry.Ren
 * Notes:APP~WMS订单下传接口表
 * Created with IntelliJ IDEA.
 * Date: 2017/12/21.
 * Time: 10:51.
 */
@Getter
@Setter
@ToString
public class AtwRequisitionOrder {

    private Long id;

    /**
     * 创建时间（本条数据创建时间）
     */
    private Date createTime;
    /**
     * 门店地址
     */
    private String diySiteAddress;
    /**
     * 门店编码
     * diy_site_id字面上的意义是门店ID，但因为历史原因这个标签必须传递门店编码
     */
    private String diySiteId;
    /**
     * 门店名称
     */
    private String diySiteTitle;
    /**
     * 门店电话
     */
    private String diySiteTel;
    /**
     * 备注信息
     */
    private String remarkInfo;
    /**
     * 顾客姓名
     */
    private String customerName;
    /**
     * 原单号（主单号）
     */
    private String orderNumber;
    /**
     * 预约送货时间段
     */
    private String receiveTimeQuantum;
    /**
     * 收货人地址
     */
    private String receiveAddress;
    /**
     * 收货人姓名
     */
    private String receiveName;
    /**
     * 收货人电话
     */
    private String receivePhone;
    /**
     * 城市
     */
    private String city;
    /**
     * 详细地址
     */
    private String detailAddress;
    /**
     * 行政区划
     */
    private String disctrict;
    /**
     * 省
     */
    private String province;
    /**
     * 街道
     */
    private String subdistrict;
    /**
     * 下单时间
     */
    private Date orderTime;
    /**
     * 导购电话
     */
    private String sellerTel;
    /**
     * 商品总数
     */
    private Integer goodsQuantity;
    /**
     * 上楼费
     */
    private Double upstairsAll;
    /**
     * 导购真实姓名
     */
    private String sellerName;
    /**
     * 运费
     */
    private Double deliveryFee;
    /**
     * 调色费
     */
    private Double colorFee;
    /**
     * 表示所有的折扣金额，当前包括：满减促销折扣，现金券折扣，产品券折扣和会员差价折扣
     */
    private Double discount;
    /**
     * 表示第三方支付金额
     */
    private Double otherPayed;
    /**
     * 表示用户已经使用的预存款金额
     */
    private Double balanceUsed;
    /**
     * 表示是否是主家收货
     */
    private Boolean memberReceiver;
    /**
     * 应收金额
     */
    private Double unpayed;
    /**
     * 商品总金额
     */
    private Double totalGoodsPrice;
    /**
     * 导购填写代收金额
     */
    private Double agencyRefund;
    /**
     * 接口传输标识
     */
    private Boolean sendFlag;
    /**
     * 接口错误信息
     */
    private String errorMessage;
    /**
     * wms收到信息时间
     */
    private Date sendTime;

    public static AtwRequisitionOrder transform(OrderBaseInfo orderBaseInfo, OrderLogisticsInfo logisticsInfo, AppStore store,
                                                OrderBillingDetails orderBillingDetails, int goodsQuantity) {
        AtwRequisitionOrder requisitionOrder = new AtwRequisitionOrder();
        requisitionOrder.setCreateTime(new Date());
        requisitionOrder.setDiySiteAddress(store.getDetailedAddress());
        requisitionOrder.setDiySiteId(store.getStoreCode());
        requisitionOrder.setDiySiteTitle(store.getStoreName());
        requisitionOrder.setDiySiteTel(store.getPhone());
        requisitionOrder.setRemarkInfo(orderBaseInfo.getRemark());
        requisitionOrder.setCustomerName(orderBaseInfo.getCreatorName());
        requisitionOrder.setOrderNumber(orderBaseInfo.getOrderNumber());
        requisitionOrder.setReceiveTimeQuantum(logisticsInfo.getDeliveryTime());
        requisitionOrder.setReceiveAddress(logisticsInfo.getDetailedAddress());
        requisitionOrder.setReceiveName(logisticsInfo.getResidenceName());
        requisitionOrder.setReceivePhone(logisticsInfo.getReceiverPhone());
        requisitionOrder.setCity(orderBaseInfo.getCityName());
        requisitionOrder.setDetailAddress(logisticsInfo.getShippingAddress());
        requisitionOrder.setDisctrict(logisticsInfo.getDeliveryCounty());
        requisitionOrder.setProvince(logisticsInfo.getDeliveryCity());
        requisitionOrder.setSubdistrict(logisticsInfo.getDeliveryStreet());
        requisitionOrder.setSellerTel(orderBaseInfo.getSalesConsultPhone());
        requisitionOrder.setGoodsQuantity(goodsQuantity);
        requisitionOrder.setUpstairsAll(orderBillingDetails.getUpstairsFee());
        requisitionOrder.setSellerName(orderBaseInfo.getSalesConsultName());
        requisitionOrder.setDeliveryFee(orderBillingDetails.getFreight());
//        requisitionOrder.setColorFee(); 没有调色费
        requisitionOrder.setDiscount(orderBillingDetails.getMemberDiscount() + orderBillingDetails.getCashCouponDiscount() +
                orderBillingDetails.getPromotionDiscount() + orderBillingDetails.getLebiCashDiscount() + orderBillingDetails.getPromotionDiscount());
        requisitionOrder.setOtherPayed(orderBillingDetails.getOnlinePayAmount());
        if (orderBaseInfo.getCreatorIdentityType() == AppIdentityType.CUSTOMER) {
            requisitionOrder.setBalanceUsed(orderBillingDetails.getCusPreDeposit());
        } else {
            requisitionOrder.setBalanceUsed(orderBillingDetails.getStPreDeposit());
        }
        requisitionOrder.setMemberReceiver(logisticsInfo.getIsOwnerReceiving());
        requisitionOrder.setUnpayed(orderBillingDetails.getAmountPayable());
        requisitionOrder.setTotalGoodsPrice(orderBaseInfo.getTotalGoodsPrice());
        requisitionOrder.setAgencyRefund(orderBillingDetails.getCollectionAmount());
        return requisitionOrder;
    }
}
