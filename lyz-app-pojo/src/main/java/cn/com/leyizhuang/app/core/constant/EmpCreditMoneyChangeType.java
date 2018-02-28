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
    FIXEDAMOUNT_ADJUSTMENT("FIXEDAMOUNT_ADJUSTMENT","固定额度调整"),
    AVALIABLED_CHANGE_BY_FIXE("AVALIABLED_CHANGE_BY_FIXE","可用额度因固定额度调整修改"),
    AVALIABLED_CHANGE_BY_TEMP("AVALIABLED_CHANGE_BY_TEMP","可用额度因临时额度调整修改"),
    ADMIN_RECHARGE("ADMIN_RECHARGE","管理员修改"),
    ORDER_REPAYMENT("ORDER_REPAYMENT","订单还款"),
    TEMPORARY_CLEAR("TEMPORARY_CLEAR","临时额度清零");
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
