package cn.com.leyizhuang.app.core.constant;

import java.util.Objects;

/**
 * 配送方式枚举
 *
 * @author Richard
 * Created on 2017-11-08 10:41
 **/
public enum AppDeliveryType {

    SELF_TAKE("SELF_TAKE", "门店自提"), HOUSE_DELIVERY("HOUSE_DELIVERY", "送货上门");

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
