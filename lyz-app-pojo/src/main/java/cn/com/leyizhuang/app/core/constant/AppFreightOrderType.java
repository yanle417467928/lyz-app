package cn.com.leyizhuang.app.core.constant;

/**
 * 运费单类型
 *
 * @author Richard
 * Created on 2017/3/24.
 */
public enum AppFreightOrderType {

    ORDER("ORDER", "订单"), RETURN_ORDER("RETURN_ORDER", "退单");

    private final String value;
    private final String description;

    AppFreightOrderType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static AppFreightOrderType getAppFreightOrderTypeByValue(String value) {
        for (AppFreightOrderType orderType : AppFreightOrderType.values()) {
            if (orderType.getValue().equals(value)) {
                return orderType;
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
