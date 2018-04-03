package cn.com.leyizhuang.app.foundation.pojo.reportDownload;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.AppGoodsLineType;
import cn.com.leyizhuang.app.core.constant.StoreType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GenerationRoad
 * @date 2018/3/30
 */
@Getter
@Setter
@ToString
public class AccountGoodsItemsDO {
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
    //产品编码
    private String sku;
    //产品名称
    private String skuName;
    //产品标识
    private String companyFlag;
    //产品类型
    private String goodsLineType;
    //数量
    private Integer quantity;
    //结算单价
    private Double settlementPrice;
    //结算总价
    private Double settlementTotlePrice;
    //成交单价
    private Double promotionSharePrice;
    //成交总价
    private Double promotionShareTotlePrice;
    //单个产品经销差价
    private Double wholesalePrice;
    //总经销差价
    private Double wholesaleTotlePrice;

    public void setGoodsLineType(AppGoodsLineType goodsLineType){
        this.goodsLineType = goodsLineType.getDescription();
    }

    public void setStoreType(StoreType storeType){
        this.storeType = storeType.getDescription();
    }

    public void setDeliveryType(AppDeliveryType deliveryType){
        this.deliveryType = deliveryType.getDescription();
    }



}
