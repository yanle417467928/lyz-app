package cn.com.leyizhuang.app.core.constant;

/**
 * 顾客产品券变更类型
 *
 * @author Richard
 * Created on 2017/12/01.
 */
public enum CustomerProductCouponChangeType {

    PLACE_ORDER("PLACE_ORDER", "订单消费"),
    RETURN_ORDER("RETURN_ORDER", "退单返还"),
    CANCEL_ORDER("CANCEL_ORDER", "取消订单返还"),
    MANUAL_GRANT("MANUAL_GRANT", "管理员手动发放"),
    MANUAL_DISABLE("MANUAL_DISABLE", "管理员手动停用"),
    BUY_COUPON("BUY_COUPON", "购买产品券");

    private final String value;
    private final String description;

    CustomerProductCouponChangeType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static CustomerProductCouponChangeType getCustomerProductCouponChangeTypeByValue(String value) {
        for (CustomerProductCouponChangeType changeType : CustomerProductCouponChangeType.values()) {
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
