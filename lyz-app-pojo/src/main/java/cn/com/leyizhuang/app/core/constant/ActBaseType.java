package cn.com.leyizhuang.app.core.constant;

/**
 * 促销基础类型
 * Created by panjie on 2017/11/22.
 */
public enum ActBaseType {

    COMMON("COMMON","普通促销"),
    ACCUMULATE("ACCUMULATE","累计促销"),
    ZGFRIST("ZGFRIST","专供首单促销"),
    LADDER("LADDER","阶梯促销");

    private final String value;
    private final String description;

    ActBaseType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static ActBaseType getActBaseTypeByValue(String value) {
        for (ActBaseType type : ActBaseType.values()) {
            if (value == type.getValue()) {
                return type;
            }
        }
        return null;
    }
}
