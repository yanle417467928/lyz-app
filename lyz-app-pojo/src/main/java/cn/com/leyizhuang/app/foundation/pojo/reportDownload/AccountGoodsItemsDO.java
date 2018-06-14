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
    //出货时间
    private String shippingTime;
    //退货时间
    private String returnTime;
    //出货时间
    private String orderTime;
    //下单/退单 创建时间
    private String createTime;
    //订单号
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
    private String goodsLineType = "";
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
    //退单号
    private String returnNumber;
    //收货人姓名
    private String receiver;
    //收货人电话
    private String receiverPhone;
    //门店类型
    private StoreType storeTypes;
    //顾客姓名
    private String customerPhone;
    //顾客姓名
    private String companyName;
    //申请退货时间
    private String applyReturnTime;
    //经销单价
    private Double jxPrice = 0.00;
    //经销总价
    private Double totalJxPrice = 0.00;
    //下单人
    private String creatorName;
    //单位
    private String goodsUnit;
    //备注
    private String remark;
    //楼盘信息
    private String estateInfo;
    //是否结清
    private String isPayUp;
    //零售单价
    private Double retailPrice;
    //零售总价
    private Double totalRetailPrice;
    //会员价
    private Double vipPrice;
    //会员总价
    private Double totalVipPrice;

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
