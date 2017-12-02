package cn.com.leyizhuang.app.core.constant;

/**
 * 促销条件类型
 * FQTY: 满数量、 FAMO: 满金额
 * Created by panjie on 2017/11/22.
 */
public enum ActConditionType {

    FQTY("FQTY","数量"),
    FAMO("FAMO","金额");

    private final String value;
    private final String description;

    ActConditionType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static ActConditionType getActConditionTypeByValue(String value) {
        for (ActConditionType type : ActConditionType.values()) {
            if (value == type.getValue()) {
                return type;
            }
        }
        return null;
    }

}
