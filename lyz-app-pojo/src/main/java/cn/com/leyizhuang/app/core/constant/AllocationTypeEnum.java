package cn.com.leyizhuang.app.core.constant;

/**
 * @author Jerry.Ren
 * Notes: 调拨单状态枚举
 * Created with IntelliJ IDEA.
 * Date: 2018/1/3.
 * Time: 10:25.
 */

public enum AllocationTypeEnum {

    NEW(1, "新建"),

    SENT(2, "已出库"),

    ENTERED(3, "已入库"),

    CANCELLED(4, "已作废");

    private int value;

    private String name;

    private AllocationTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int value) {
        for (AllocationTypeEnum type : AllocationTypeEnum.values()) {
            if (type.getValue() == value) {
                return type.getName();
            }
        }
        return "未知";
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

}
