package cn.com.leyizhuang.app.core.constant;

/**
 * App 充值单状态枚举
 *
 * @author CrazyApeDX
 * Created on 2017/3/24.
 */
public enum AppRechargeOrderStatus {

    /**
     * 待付款
     */
    UNPAID("UNPAID", "待付款"),

    /**
     * 已付款
     */
    PAID("PAID", "已付款");


    private final String value;
    private final String description;

    AppRechargeOrderStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static AppRechargeOrderStatus getAppRechargeOrderStatusByValue(String value) {
        for (AppRechargeOrderStatus status : AppRechargeOrderStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

}
