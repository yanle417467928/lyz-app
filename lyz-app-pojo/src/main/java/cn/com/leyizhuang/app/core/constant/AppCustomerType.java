package cn.com.leyizhuang.app.core.constant;

/**
 * APP顾客类型
 *
 * @author Richard
 * Created on 2017-10-25 10:12
 **/
public enum AppCustomerType {

    MEMBER("MEMBER","会员"),RETAIL("RETAIL","零售");

    private String value;

    private String description;

    AppCustomerType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static AppCustomerType getAppCustomerTypeByValue(String value) {
        for (AppCustomerType appCustomerType : AppCustomerType.values()) {
            if (value == appCustomerType.getValue()) {
                return appCustomerType;
            }
        }
        return null;
    }
}
