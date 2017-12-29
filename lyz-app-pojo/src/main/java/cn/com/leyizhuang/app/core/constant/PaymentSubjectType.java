package cn.com.leyizhuang.app.core.constant;

/**
 * 付款主体类型
 *
 * @author Richard
 * @date 2017/12/29
 */
public enum PaymentSubjectType {
    CUSTOMER("CUSTOMER", "顾客"),
    DECORATE_MANAGER("DECORATE_MANAGER", "装饰公司经理"),
    SELLER("SELLER", "导购"),
    DELIVERY_CLERK("DELIVERY_CLERK", "配送员"),
    STORE("STORE", "门店");


    private final String value;

    private final String description;


    PaymentSubjectType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }
}
