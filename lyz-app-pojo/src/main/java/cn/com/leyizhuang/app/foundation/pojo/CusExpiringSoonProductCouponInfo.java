package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.CouponGetType;
import cn.com.leyizhuang.app.core.constant.StoreType;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 将逾期产品券
 *
 * @author liuh
 * Created on 2017-09-29 10:00
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CusExpiringSoonProductCouponInfo implements Serializable {


    /**
     * 城市
     */
    private String cityName;

    /**
     * 用户名字
     */
    private String customerName;

    /**
     * 商品名称
     */
    private String  skuName;

    /**
     * 商品sku
     */
    private String  sku;

    /**
     *  数量
     */
    private Integer quantity;

    /**
     * 产品券获取途径
     */
    private String getType;

    /**
     * 获取时间
     */
    private String getTime;

    /**
     *  生效开始时间
     */
    private String effectiveStartTime;

    /**
     * 生效结束时间
     */
    private String effectiveEndTime;


    /**
     * 产生券的相关订单号
     */
    private String getOrderNumber;

    /**
     *  购买价格
     */
    private Double buyPrice;

    /**
     *  购买总价格
     */
    private Double totalPrice;


    /**
     * 购买门店id
     */
    private String storeName;

    /**
     * 门店类型
     */
    private String storeType;

    /**
     * 购买时归属导购
     */
    private String sellerName;

    /**
     * 商品标志（普通、专供）
     */
    private String goodsSign;


    public void setStoreType(StoreType storeType){
        this.storeType = storeType.getDescription();
    }

    public void setGetType(CouponGetType getType){
        this.getType = getType.getDescription();
    }

}
