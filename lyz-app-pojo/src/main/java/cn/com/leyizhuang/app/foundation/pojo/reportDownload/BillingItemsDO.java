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
    private Double totalGoodsPrice;
    //会员折扣
    private Double memberDiscount;
    //订单折扣
    private Double promotionDiscount;
    //配送费
    private Double freight;
    //优惠券折扣
    private Double cashCouponDiscount;
    //产品券折扣
    private Double productCouponDiscount;
    //应付总额
    private Double amountPayable;
    //微信
    private Double weChat;
    //支付宝
    private Double alipay;
    //银联
    private Double unionPay;
    //门店现金
    private Double storeCash;
    //门店POS
    private Double storePosMoney;
    //配送现金
    private Double deliveryCash;
    //配送POS
    private Double deliveryPos;
    //其他
    private Double storeOtherMoney;
    //门店预存款
    private Double stPreDeposit;
    //顾客预存款
    private Double cusPreDeposit;
    //支付总额
    private Double totalPay;
    //退单号
    private String returnNumber;

    public void setStoreType(StoreType storeType){
        this.storeType = storeType.getDescription();
    }

    public void setDeliveryType(AppDeliveryType deliveryType){
        this.deliveryType = deliveryType.getDescription();
    }

}
