package cn.com.leyizhuang.app.core.constant;

import java.util.Objects;

/**
 * 充值账户类型
 *
 * @author Richard
 * Created on 2017-10-25 10:12
 **/
public enum RechargeAccountType {

    CUS_PREPAY("CUS_PREPAY", "顾客预存款"),
    ST_PREPAY("ST_PREPAY", "门店预存款"),
    PRODUCT_COUPON("PRODUCT_COUPON", "产品券"),
    BOND("BOND", "保证金");

    private String value;

    private String description;

    RechargeAccountType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static RechargeAccountType getRechargeAccountTypeByValue(String value) {
        for (RechargeAccountType paymentType : RechargeAccountType.values()) {
            if (Objects.equals(value, paymentType.getValue())) {
                return paymentType;
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
