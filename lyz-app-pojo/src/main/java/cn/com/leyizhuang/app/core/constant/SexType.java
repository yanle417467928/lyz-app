package cn.com.leyizhuang.app.core.constant;

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


}