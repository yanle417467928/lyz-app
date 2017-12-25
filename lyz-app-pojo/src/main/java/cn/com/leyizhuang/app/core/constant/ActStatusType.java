package cn.com.leyizhuang.app.core.constant;

/**
 * 促销状态枚举类
 * Created by panjie on 2017/12/18.
 */
public enum ActStatusType {

    NEW("NEW","新建"),
    PUBLISH("PUBLISH","发布"),
    INVALID("INVALID","失效");

    private final String value;
    private final String description;

    ActStatusType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static ActStatusType getActStatusTypeByValue(String value) {
        for (ActStatusType type : ActStatusType.values()) {
            if (value == type.getValue()) {
                return type;
            }
        }
        return null;
    }
}
