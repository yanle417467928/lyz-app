package cn.com.leyizhuang.app.core.constant;

/**
 * 乐币变动类型枚举
 * Created by caiyu on 2017/11/8.
 */
public enum LeBiVariationType {
    SIGN(1, "签到"), ADMINISTRATORS_UPDATE(2, "管理员修改"), CANCEL_ORDER(3, "取消订单返还"), RETURN_ORDER(4, "退货返还"), ORDER(5, "订单使用");
    private final int value;
    private final String description;

    LeBiVariationType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static LeBiVariationType getLeBiVariationType(Integer value) {
        for (LeBiVariationType leBiVariationType : LeBiVariationType.values()) {
            if (value == leBiVariationType.getValue()) {
                return leBiVariationType;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }


}
