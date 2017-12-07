package cn.com.leyizhuang.app.core.constant;

/**
 * 消息发送平台类型
 *
 * @author Richard
 * Created on 2017-10-20 18:09
 **/
public enum AppMsgSendSystemType {
    ALL("ALL", "全部"),
    ANDROID("ANDROID", "安卓"),
    IOS("IOS ", "苹果");

    private final String value;
    private final String description;


    AppMsgSendSystemType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static AppMsgSendSystemType getAppSystemTypeByValue(String value) {
        for (AppMsgSendSystemType systemType : AppMsgSendSystemType.values()) {
            if (value.equals(systemType.getValue())) {
                return systemType;
            }
        }
        return null;
    }
}
