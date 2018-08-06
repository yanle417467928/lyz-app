package cn.com.leyizhuang.app.core.constant.remote;

/**
 * @Author Richard
 * @Date 2018/8/6 14:50
 */
public enum ProductCouponSubjectType {
    CUSTOMER_PRODUCT_COUPON("CUSTOMER_PRODUCT_COUPON", "顾客产品券"), STORE_PRODUCT_COUPON("STORE_PRODUCT_COUPON", "门店产品券");

    private final String value;
    private final String description;


    ProductCouponSubjectType(String value,String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static ProductCouponSubjectType getProductCouponSubjectTypeValue(String value) {
        for (ProductCouponSubjectType type : ProductCouponSubjectType.values()) {
            if (value == type.getValue()) {
                return type;
            }
        }
        return null;
    }
}
