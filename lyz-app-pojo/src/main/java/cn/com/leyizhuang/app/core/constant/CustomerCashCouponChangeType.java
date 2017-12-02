package cn.com.leyizhuang.app.core.constant;

/**
 * 顾客优惠券变更类型
 *
 * @author Richard
 * Created on 2017/12/01.
 */
public enum CustomerCashCouponChangeType {

    PLACE_ORDER("PLACE_ORDER", "订单消费"),
    RETURN_ORDER("RETURN_ORDER","退单返还"),
    CANCEL_ORDER("CANCEL_ORDER","取消订单返还"),
    MANUAL_GRANT("MANUAL_GRANT","管理员手动发放"),
    CUSTOMER_PICK_UP("CUSTOMER_PICK_UP","顾客领取");

    private final String value;
    private final String description;

    CustomerCashCouponChangeType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static CustomerCashCouponChangeType getCustomerCashCouponChangeTypeByValue(String value) {
        for (CustomerCashCouponChangeType changeType : CustomerCashCouponChangeType.values()) {
            if (value.equals(changeType.getValue())) {
                return changeType;
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
