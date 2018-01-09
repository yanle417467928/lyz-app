package cn.com.leyizhuang.app.core.constant;

/**
 * 现金券枚举
 * Created by panjie on 2018/1/3.
 */
public enum AppCashCouponType {

    GENERAL("GENERAL","通用"),
    COMPANY("COMPANY","指定公司"),
    BRAND("BRAND","指定品牌"),
    GOODS("GOODS","指定商品")
    ;

    private final String value;
    private final String description;

    AppCashCouponType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static AppCashCouponType getAppCashCouponTypeByValue(String value) {
        for (AppCashCouponType type : AppCashCouponType.values()) {
            if (value == type.getValue()) {
                return type;
            }
        }
        return null;
    }
}
