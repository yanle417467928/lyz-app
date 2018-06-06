package cn.com.leyizhuang.app.core.constant;

import java.util.Objects;

/**
 * @author GenerationRoad
 * @date 2018/5/24
 */
public enum FitCompayType {
    CASH("CASH", "现结"), MONTHLY("MONTHLY", "月结");

    private final String value;
    private final String description;

    FitCompayType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static FitCompayType getFitCompayTypeByValue(String value) {
        for (FitCompayType fitCompayType : FitCompayType.values()) {
            if (Objects.equals(value, fitCompayType.getValue())) {
                return fitCompayType;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

}
