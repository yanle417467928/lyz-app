package cn.com.leyizhuang.app.core.constant;

/**
 * 顾客预存款变更类型
 *
 * @author Richard
 * Created on 2017/12/01.
 */
public enum CustomerPreDepositChangeType {

    PLACE_ORDER("PLACE_ORDER", "订单消费"),
    RETURN_ORDER("RETURN_ORDER","退单返还"),
    CANCEL_ORDER("CANCEL_ORDER","取消订单返还"),
    ALIPAY_RECHARGE("ALIPAY_RECHARGE","支付宝充值"),
    WECHAT_RECHARGE("WECHAT_RECHARGE","微信充值"),
    UNIONPAY_RECHARGE("UNIONPAY_RECHARGE","银联充值"),
    ADMIN_CHANGE("ADMIN_CHANGE","管理员修改");

    private final String value;
    private final String description;

    CustomerPreDepositChangeType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static CustomerPreDepositChangeType getCustomerPreDepositChangeTypeByValue(String value) {
        for (CustomerPreDepositChangeType changeType : CustomerPreDepositChangeType.values()) {
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
