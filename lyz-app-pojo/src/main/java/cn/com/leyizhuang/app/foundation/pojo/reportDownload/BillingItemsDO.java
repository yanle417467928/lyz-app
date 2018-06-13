package cn.com.leyizhuang.app.foundation.pojo.reportDownload;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.StoreType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GenerationRoad
 * @date 2018/4/2
 */
@Getter
@Setter
@ToString
public class BillingItemsDO {
    //城市
    private String cityName;
    //门店名称
    private String storeName;
    //门店类型
    private String storeType;
    //下单/退单时间
    private String orderTime;
    //订/退单号
    private String orderNumber;
    //顾客姓名
    private String customerName;
    //导购姓名
    private String sellerName;
    //配送方式
    private String deliveryType;
    //出/退货状态
    private String deliveryStatus;
    //送/退货地址
    private String shippingAddress;
    // 商品总额
    private Double totalGoodsPrice = 0.00;
    //会员折扣
    private Double memberDiscount = 0.00;
    //订单折扣
    private Double promotionDiscount = 0.00;
    //乐币折扣
    private Double lbDiscount = 0.00;

    //配送费
    private Double freight = 0.00;
    //优惠券折扣
    private Double cashCouponDiscount = 0.00;
    //产品券折扣
    private Double productCouponDiscount = 0.00;
    //应付总额
    private Double amountPayable = 0.00;
    //微信
    private Double weChat = 0.00;
    //支付宝
    private Double alipay = 0.00;
    //银联
    private Double unionPay = 0.00;
    //门店现金
    private Double storeCash = 0.00;
    //门店POS
    private Double storePosMoney = 0.00;
    //配送现金
    private Double deliveryCash = 0.00;
    //配送POS
    private Double deliveryPos = 0.00;
    //其他
    private Double storeOtherMoney = 0.00;
    //门店预存款
    private Double stPreDeposit = 0.00;
    //顾客预存款
    private Double cusPreDeposit = 0.00;
    //支付总额
    private Double totalPay = 0.00;
    //退单号
    private String returnNumber = "";
    //退还门店
    private Double returnStore = 0.00;
    //退单状态
    private String returnStatus = "";
    //订单状态
    private String orderStatus = "";

    public void setStoreType(StoreType storeType){
        this.storeType = storeType.getDescription();
    }

    public void setDeliveryType(AppDeliveryType deliveryType){
        this.deliveryType = deliveryType.getDescription();
    }

}
