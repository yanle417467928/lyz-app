package cn.com.leyizhuang.app.core.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 运费变更类型
 * @author GenerationRoad
 * @date 2018/7/25
 */
public enum FreightChangeType {
    URGENT("URGENT", "加急费"), NIGHT("NIGHT", "夜间配送费"), MERGE("MERGE", "合并订单"), COLORING("COLORING", "调色费"), OTHER("OTHER", "其它");

    private final String value;
    private final String description;

    FreightChangeType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static FreightChangeType getFreightChangeTypeByValue(String value) {
        for (FreightChangeType freightChangeType : FreightChangeType.values()) {
            if (value.equalsIgnoreCase(freightChangeType.getValue())) {
                return freightChangeType;
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

    public static List<FreightChangeType> getFreightChangeTypes() {
        List<FreightChangeType> freightChangeTypes = new ArrayList<>();
        freightChangeTypes.add(FreightChangeType.URGENT);
        freightChangeTypes.add(FreightChangeType.NIGHT);
        freightChangeTypes.add(FreightChangeType.MERGE);
        freightChangeTypes.add(FreightChangeType.OTHER);
        return freightChangeTypes;
    }
}
