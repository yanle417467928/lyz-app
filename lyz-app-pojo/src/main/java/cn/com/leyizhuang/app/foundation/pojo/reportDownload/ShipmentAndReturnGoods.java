package cn.com.leyizhuang.app.foundation.pojo.reportDownload;

import cn.com.leyizhuang.app.core.constant.AppCustomerType;
import cn.com.leyizhuang.app.core.constant.AppGoodsLineType;
import cn.com.leyizhuang.app.core.constant.StoreType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liuh
 * @date 2018/4/3
 */
@Getter
@Setter
@ToString
public class ShipmentAndReturnGoods {
    //城市
    private String cityName;
    //门店名称
    private String storeName;
    //门店类型
    private String storeType;
    //项目类型 出货 要货
    private String orderType;
    //订单号
    private String ordNo;
    //出退货时间
    private String operationTime;
    //订单日期
    private String createTime;
    //顾客id
    private Long customerId;
    //顾客电话
    private String customerPhone;
    //顾客姓名
    private String customerName;
    //顾客类型
    private String customerType;
    //导购姓名
    private String salesConsultName;
    //公司标识
    private String companyFlag;
    //商品编码
    private String sku;
    //商品名称
    private String skuName;
    //产品类型
    private String goodsLineType;
    //数量
    private Integer orderQty;
    //成交价
    private BigDecimal returnPrice;
    //成交总价
    private BigDecimal amount;
    //产品劵购买价格
    private BigDecimal purchasePrice;
    //相关单号
    private String referenceNumber;

    public void setCustomerType(AppCustomerType appCustomerType) {
        this.customerType = appCustomerType.getDescription();
    }

    public void setGoodsLineType(AppGoodsLineType appGoodsLineType) {
        this.goodsLineType = appGoodsLineType.getDescription();
    }

    public void setStoreType(StoreType storeType) {
        this.storeType = storeType.getDescription();
    }

    public static final List<ShipmentAndReturnGoods> transformList(List<ShipmentAndReturnGoods> shipmentAndReturnGoodsList) {
        if (null != shipmentAndReturnGoodsList && shipmentAndReturnGoodsList.size() > 0) {
            for (ShipmentAndReturnGoods shipmentAndReturnGoods : shipmentAndReturnGoodsList) {
                if ("产品券".equals(shipmentAndReturnGoods.getGoodsLineType()) && null != shipmentAndReturnGoods.getPurchasePrice() && null != shipmentAndReturnGoods.getOrderQty()) {
                    shipmentAndReturnGoods.setReturnPrice(shipmentAndReturnGoods.getPurchasePrice());
                    shipmentAndReturnGoods.setAmount(shipmentAndReturnGoods.getPurchasePrice().multiply(BigDecimal.valueOf(shipmentAndReturnGoods.getOrderQty())));
                } else if ("本品".equals(shipmentAndReturnGoods.getGoodsLineType()) && null != shipmentAndReturnGoods.getReturnPrice() && null != shipmentAndReturnGoods.getOrderQty()) {
                    shipmentAndReturnGoods.setAmount(shipmentAndReturnGoods.getReturnPrice().multiply(BigDecimal.valueOf(shipmentAndReturnGoods.getOrderQty())));
                } else if ("赠品".equals(shipmentAndReturnGoods.getGoodsLineType()) && null != shipmentAndReturnGoods.getReturnPrice() && null != shipmentAndReturnGoods.getOrderQty()) {
                    shipmentAndReturnGoods.setAmount(shipmentAndReturnGoods.getReturnPrice().multiply(BigDecimal.valueOf(shipmentAndReturnGoods.getOrderQty())));
                } else {
                    shipmentAndReturnGoods.setAmount(BigDecimal.ZERO);
                }
                if(shipmentAndReturnGoods.getReferenceNumber().startsWith("T")){
                    shipmentAndReturnGoods.setOrderType("退货");
                }else{
                    shipmentAndReturnGoods.setOrderType("要货");
                }
            }
            return shipmentAndReturnGoodsList;
        } else {
            return null;
        }
    }
}
