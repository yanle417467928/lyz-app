package cn.com.leyizhuang.app.core.constant;

/**
 * 门店（装饰公司）信用额度变更类型
 *
 * @author Richard
 * Created on 2017/12/01.
 */
public enum StoreCreditMoneyChangeType {

    PLACE_ORDER("PLACE_ORDER", "订单消费"),
    RETURN_ORDER("RETURN_ORDER","退单返还"),
    CANCEL_ORDER("CANCEL_ORDER","取消订单返还"),
    ADMIN_RECHARGE("ADMIN_RECHARGE","管理员修改");

    private final String value;
    private final String description;

    StoreCreditMoneyChangeType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static StoreCreditMoneyChangeType getStoreCreditMoneyChangeTypeByValue(String value) {
        for (StoreCreditMoneyChangeType changeType : StoreCreditMoneyChangeType.values()) {
            if (value.equals(changeType.getValue())) {
                return changeType;
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
