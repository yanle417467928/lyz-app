package cn.com.leyizhuang.app.core.constant;

/**
 * 导购信用额度变更类型
 *
 * @author Richard
 * Created on 2017/12/01.
 */
public enum EmpCreditMoneyChangeType {

    PLACE_ORDER("PLACE_ORDER", "订单消费"),
    RETURN_ORDER("RETURN_ORDER","退单返还"),
    CANCEL_ORDER("CANCEL_ORDER","取消订单返还"),
    TEMPORARY_ADJUSTMENT("TEMPORARY_ADJUSTMENT","临时额度调整"),
    ADMIN_RECHARGE("ADMIN_RECHARGE","管理员修改");

    private final String value;
    private final String description;

    EmpCreditMoneyChangeType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static EmpCreditMoneyChangeType getEmpCreditMoneyChangeTypeByValue(String value) {
        for (EmpCreditMoneyChangeType changeType : EmpCreditMoneyChangeType.values()) {
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