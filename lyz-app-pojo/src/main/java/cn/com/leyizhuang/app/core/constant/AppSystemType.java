package cn.com.leyizhuang.app.core.constant;

/**
 * 手机操作系统类型
 *
 * @author Richard
 * Created on 2017-10-20 18:09
 **/
public enum AppSystemType {

    ANDROID("ANDROID", "安卓"),
    IOS("IOS", "苹果");

    private final String value;
    private final String description;


    AppSystemType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static AppSystemType getAppSystemTypeByValue(String value) {
        for (AppSystemType systemType : AppSystemType.values()) {
            if (value.equals(systemType.getValue())) {
                return systemType;
            }
        }
        return null;
    }
}
