package cn.com.leyizhuang.app.core.constant;

import java.util.Objects;

/**
 * Created by caiyu on 2018/3/31.
 */
public enum OrderLifecycleType {
    PAYED("PAYED","支付"),
    CANCEL_ORDER("CANCEL_ORDER", "取消订单"),
    NORMAL_RETURN("NORMAL_RETURN", "正常退货"),
    CANCELED("CANCELED", "已取消"),
    REJECTED("REJECTED", "拒签"),
    FINISHED("FINISHED","已完成"),
    SEALED_CAR("SEALED_CAR","已出货");


    private String value;

    private String description;

    OrderLifecycleType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static OrderLifecycleType getOrderLifecycleTypeByValue(String value) {
        for (OrderLifecycleType lifecycleType : OrderLifecycleType.values()) {
            if (Objects.equals(value, lifecycleType.getValue())) {
                return lifecycleType;
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

    public static OrderLifecycleType getOrderLifecycleTypeDescription(String description) {
        for (OrderLifecycleType lifecycleType : OrderLifecycleType.values()) {
            if (Objects.equals(description, lifecycleType.getDescription())) {
                return lifecycleType;
            }
        }
        return null;
    }
}
