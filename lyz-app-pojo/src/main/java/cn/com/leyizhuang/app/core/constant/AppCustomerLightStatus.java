package cn.com.leyizhuang.app.core.constant;

/**
 * Created with IntelliJ IDEA.
 * Created by Jerry.Ren
 * Date: 2017/10/9.
 * Time: 17:13.
 */

public enum AppCustomerLightStatus {

    NOT("无灯"),
    CLOSE("熄灯"),
    RED("红灯"),
    GREEN("绿灯"),
    YELLOW("黄灯");

    private final String value;

    AppCustomerLightStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
