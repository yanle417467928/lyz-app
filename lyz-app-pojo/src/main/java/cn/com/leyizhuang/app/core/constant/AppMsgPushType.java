package cn.com.leyizhuang.app.core.constant;

/**
 * 推送类型
 *
 * @author Richard
 * Created on 2017-10-20 18:09
 **/
public enum AppMsgPushType {
    LOGISTIC("LOGISTIC", "物流信息"),
    SYSTEM_NOTICE("SYSTEM_NOTICE", "系统公告"),
    PROMOTION("PROMOTION ", "促销活动");

    private final String value;
    private final String description;


    AppMsgPushType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static AppMsgPushType getAppMsgPushTypeByValue(String value) {
        for (AppMsgPushType pushType : AppMsgPushType.values()) {
            if (value.equals(pushType.getValue())) {
                return pushType;
            }
        }
        return null;
    }
}
