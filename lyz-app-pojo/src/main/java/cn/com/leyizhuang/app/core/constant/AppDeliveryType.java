package cn.com.leyizhuang.app.core.constant;

import java.util.Objects;

/**
 * 配送方式枚举
 *
 * @author Richard
 * Created on 2017-11-08 10:41
 **/
public enum AppDeliveryType {

    //**************************正向配送方式******************************
    SELF_TAKE("SELF_TAKE", "门店自提"), HOUSE_DELIVERY("HOUSE_DELIVERY", "送货上门"),PRODUCT_COUPON("PRODUCT_COUPON","购买产品券"),

    //**************************反向配送方式******************************
    RETURN_STORE("SELF_DELIVERY", "退货到店"), HOUSE_PICK("HOUSE_PICK", "工地取货");
    private String value;

    private String description;

    AppDeliveryType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static AppDeliveryType getAppDeliveryTypeByValue(String value) {
        for (AppDeliveryType appDeliveryType : AppDeliveryType.values()) {
            if (Objects.equals(value, appDeliveryType.getValue())) {
                return appDeliveryType;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
