package cn.com.leyizhuang.app.core.constant;

import java.util.Objects;

/**
 * App 自提单订单状态枚举
 *
 * @author CrazyApeDX
 * Created on 2017/3/24.
 */
public enum AppSelfTakeOrderStatus {

    UNPAID("UNPAID", 1, "待付款"), PENDING_RECEIVE("PENDING_RECEIVE", 2, "待收货"),
    FINISHED("FINISHED", 3, "已完成"), CLOSED("CLOSED", 4, "已结案"), CANCELED("CANCELED", 5, "已取消");


    private final String value;
    private Integer seq;
    private final String description;

    AppSelfTakeOrderStatus(String value, Integer seq, String description) {
        this.value = value;
        this.seq = seq;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static AppSelfTakeOrderStatus getAppDeliveryOrderStatusByValue(String value) {
        for (AppSelfTakeOrderStatus status : AppSelfTakeOrderStatus.values()) {
            if (Objects.equals(value, status.getValue())) {
                return status;
            }
        }
        return null;
    }

}
