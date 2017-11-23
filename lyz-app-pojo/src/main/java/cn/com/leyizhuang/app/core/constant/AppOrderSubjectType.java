package cn.com.leyizhuang.app.core.constant;

import java.util.Objects;

/**
 * App 订单下单主体类型枚举
 *
 * @author CrazyApeDX
 * Created on 2017/3/24.
 */
public enum AppOrderSubjectType {

    /**
     * 装饰公司
     */
    FIT("FIT", "装饰公司"),

    /**
     * 门店
     */
    STORE("STORE", "门店");



    private final String value;
    private final String description;

    AppOrderSubjectType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static AppOrderSubjectType getAppOrderSubjectTypeByValue(String value) {
        for (AppOrderSubjectType type : AppOrderSubjectType.values()) {
            if (Objects.equals(value, type.getValue())) {
                return type;
            }
        }
        return null;
    }

}
