package cn.com.leyizhuang.app.core.constant;

/**
 * @author GenerationRoad
 * @date 2017/11/27
 */
public enum StoreSubventionChangeType {

    PLACE_ORDER("PLACE_ORDER","订单消费"),
    RETURN_ORDER("RETURN_ORDER","退单退回"),
    CANCEL_ORDER("CANCEL_ORDER","取消订单退回"),
    ADMIN_CHANGE("ADMIN_CHANGE","管理员修改");


    private final String value;
    private final String description;

    StoreSubventionChangeType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
