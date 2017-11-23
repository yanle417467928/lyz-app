package cn.com.leyizhuang.app.core.constant;

import java.util.Objects;

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

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static AppCustomerCreateType getAppCustomerCreateTypeByValue(String value) {
        for (AppCustomerCreateType appCustomerCreateType : AppCustomerCreateType.values()) {
            if (Objects.equals(value, appCustomerCreateType.getValue())) {
                return appCustomerCreateType;
            }
        }
        return null;
    }
}
