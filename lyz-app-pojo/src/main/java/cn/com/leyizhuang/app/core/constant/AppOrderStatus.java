package cn.com.leyizhuang.app.core.constant;

/**
 * App 订单状态枚举
 *
 * @author CrazyApeDX
 * Created on 2017/3/24.
 */
public enum AppOrderStatus {

    SHIPMENT("SHIPMENT", "出货"), COUPON("COUPON", "买券");

    private final String value;
    private final String description;

    AppOrderStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static AppOrderStatus getAppOrderTypeByValue(String value) {
        for (AppOrderStatus orderType : AppOrderStatus.values()) {
            if (value == orderType.getValue()) {
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
