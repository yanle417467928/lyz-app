package cn.com.leyizhuang.app.core.constant;

import java.util.Objects;

/**
 *  App 配送单订单状态枚举
 * @author CrazyApeDX
 * Created on 2017/3/24.
 */
public enum AppDeliveryOrderStatus {

    UNPAID("UNPAID",1, "待付款"), PENDING_SHIPMENT("PENDING_SHIPMENT",2, "待发货"),PENDING_RECEIVE("PENDING_RECEIVE",3,"待收货"),
    FINISHED("FINISHED",4,"已完成"),CLOSED("CLOSED",5,"已结案"),CANCELED("CANCELED",6,"已取消"),REJECTED("REJECTED",7,"拒签");



    private final String value;
    private final Integer seq;
    private final String description;

    AppDeliveryOrderStatus(String value, Integer seq, String description) {
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

    public static AppDeliveryOrderStatus getAppDeliveryOrderStatusByValue(String value) {
        for (AppDeliveryOrderStatus status : AppDeliveryOrderStatus.values()) {
            if (Objects.equals(value, status.getValue())) {
                return status;
            }
        }
        return null;
    }

}
