package cn.com.leyizhuang.app.core.constant;

import java.util.Objects;

/**
 * @author CrazyApeDX
 * Created on 2017/3/24.
 */
public enum AppOrderType {

    SHIPMENT("SHIPMENT", "出货"), COUPON("COUPON", "买券");

    private final String value;
    private final String description;

    AppOrderType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static AppOrderType getAppOrderTypeByValue(String value) {
        for (AppOrderType orderType : AppOrderType.values()) {
            if (Objects.equals(value, orderType.getValue())) {
                return orderType;
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
