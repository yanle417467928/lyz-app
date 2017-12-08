package cn.com.leyizhuang.app.core.constant;

/**
 * Created by caiyu on 2017/12/7.
 */
public enum OrderCouponType {
    CASH_COUPON("现金券"), PRODUCT_COUPON("产品券");

    private final String value;

    OrderCouponType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
