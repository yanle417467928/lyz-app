package cn.com.leyizhuang.app.core.constant;

/**
 * 调拨单枚举类型
 * Created by panjie on 2018/1/16.
 */
public enum AllocationType {

    NEW("NEW","新建"),
    OUTPUT("OUTPUT","已经出库"),
    INPUT("INPUT","已入库"),
    INVALID("INVALID","已作废");

    private final String value;
    private final String description;

    AllocationType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static AllocationType getAllocationTypeByValue(String value) {
        for (AllocationType type : AllocationType.values()) {
            if (value == type.getValue()) {
                return type;
            }
        }
        return null;
    }
}
