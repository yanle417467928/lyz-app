package cn.com.leyizhuang.app.core.constant;

import java.util.Objects;

/**
 * App 配送单订单状态枚举
 *
 * @author CrazyApeDX
 * Created on 2017/3/24.
 */
public enum AppOrderStatus {

    /**
     * 配送单:有 自提单:有 买券订单:无
     */
    UNPAID("UNPAID", "待付款"),
    /**
     * 配送单:有  自提单:无  买券订单:无
     */
    PENDING_SHIPMENT("PENDING_SHIPMENT", "待发货"),
    /**
     * 配送单:有  自提单:有  买券订单:无
     */
    PENDING_RECEIVE("PENDING_RECEIVE", "待收货"),
    /**
     * 配送单:有  自提单:有  买券订单:有
     */
    FINISHED("FINISHED", "已完成"),
    /**
     * 配送单:有  自提单:有  买券订单:有
     */
    CLOSED("CLOSED", "已结案"),
    /**
     * 配送单:有  自提单:有  买券订单:无
     */
    CANCELED("CANCELED", "已取消"),
    /**
     * 配送单:有  自提单:无  买券订单:无
     */
    REJECTED("REJECTED", "拒签"),
    /**
     * 配送单:有  自提单:有  买券订单:无
     */
    CANCELING("CANCELING","取消中");

    private final String value;
    private final String description;

    AppOrderStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static AppOrderStatus getAppDeliveryOrderStatusByValue(String value) {
        for (AppOrderStatus status : AppOrderStatus.values()) {
            if (Objects.equals(value, status.getValue())) {
                return status;
            }
        }
        return null;
    }

}
