package cn.com.leyizhuang.app.core.constant;

/**
 * APP顾客创建类型
 *
 * @author Richard
 * Created on 2017-10-25 9:57
 **/
public enum AppCustomerCreateType {

    APP_REGISTRY("APP_REGISTRY", "APP注册"), ADMIN_CREATE("ADMIN_CREATE", "后台创建");

    private String value;

    private String description;

    AppCustomerCreateType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static AppCustomerCreateType getAppCustomerCreateTypeByValue(String value) {
        for (AppCustomerCreateType appCustomerCreateType : AppCustomerCreateType.values()) {
            if (value == appCustomerCreateType.getValue()) {
                return appCustomerCreateType;
            }
        }
        return null;
    }
}
