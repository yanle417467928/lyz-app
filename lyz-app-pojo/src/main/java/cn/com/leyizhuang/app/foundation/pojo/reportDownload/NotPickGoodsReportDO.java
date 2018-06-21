package cn.com.leyizhuang.app.foundation.pojo.reportDownload;

import cn.com.leyizhuang.app.core.constant.AppCustomerType;
import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.CouponGetType;
import cn.com.leyizhuang.app.core.constant.StoreType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jerry.Ren
 * create 2018-03-30 14:23
 * desc: 未提货报表实体
 **/

@Getter
@Setter
@ToString
public class NotPickGoodsReportDO {

    //城市
    private String cityName = "";
    //门店名称
    private String storeName = "";
    //门店类型
    private String storeType = "";
    //自提单类型
    private String pickType = "";
    //购买时间
    private String buyTime = "";
    //过期时间
    private String effectiveTime = "";
    //顾客id
    private Long customerId;
    //顾客名
    private String customerName = "";
    //顾客电话
    private String customerPhone = "";
    //顾客类型
    private String customerType = "";
    //导购名称
    private String sellerName = "";
    //商品编码
    private String sku = "";
    //商品名
    private String skuName = "";
    //品牌
    private String brandName = "";
    //数量
    private Integer quantity ;
    //购买时价格
    private Double buyPrice = 0D;
    //经销价
    private Double wholesalePrice = 0D;
    //购买总价
    private Double totalBuyPrice = 0D;
    //关联单号
    private String referenceNumber = "";
    // 是否结清
    private String isPayUp = "";
    // 产品类型
    private String goodsLineType = "";
    // 出货时间
    private String shippingTime = "";

    public void setPickType(String deliveryType) {
        AppDeliveryType type1 = AppDeliveryType.getAppDeliveryTypeByValue(deliveryType);
        if (null != type1) {
            this.pickType = type1.getDescription();
        } else {
            this.pickType = CouponGetType.valueOf(deliveryType).getDescription();
        }
    }

    public void setStoreType(StoreType storeType) {
        this.storeType = storeType.getDescription();
    }

    public void setCustomerType(AppCustomerType appCustomerType) {
        this.customerType = appCustomerType.getDescription();
    }
}
