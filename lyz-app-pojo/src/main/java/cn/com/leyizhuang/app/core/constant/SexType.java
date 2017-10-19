package cn.com.leyizhuang.app.core.constant;

import java.util.Objects;

/**
 * @author CrazyApeDX
 *         Created on 2017/3/24.
 */
public enum SexType {
    MALE("男"), FEMALE("女"),SECRET("保密");

    private final String value;

    SexType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

    public static SexType getSexTypeByValue(String value){
        for (SexType type:
             SexType.values()) {
            if (Objects.equals(value, type.getValue())){
                return type;
            }
        }
        return null;
    }
}
