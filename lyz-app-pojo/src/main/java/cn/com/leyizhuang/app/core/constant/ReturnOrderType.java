package cn.com.leyizhuang.app.core.constant;

/**
 * 退单类型枚举
 * Created by caiyu on 2017/12/5.
 */
public enum  ReturnOrderType {

    CANCEL_RETURN(1, "取消退货"), REFUSED_RETURN(2, "拒签退货"), NORMAL_RETURN(3, "正常退货");

    private final int value;
    private final String description;

    ReturnOrderType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static ReturnOrderType getreturnOrderType(Integer value) {
        for (ReturnOrderType returnOrderType : ReturnOrderType.values()) {
            if (value == returnOrderType.getValue()) {
                return returnOrderType;
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
