package cn.com.leyizhuang.app.core.constant;

import java.util.Objects;

/**
 * 账单支付方式枚举
 *
 * @author Richard
 * Created on 2017-10-25 10:12
 **/
public enum OrderBillingPaymentType {

    CUS_PREPAY("CUS_PREPAY", "顾客预存款"),
    ST_PREPAY("ST_PREPAY", "门店预存款"),
    ALIPAY("ALIPAY", "支付宝"),
    WE_CHAT("WE_CHAT", "微信"),
    UNION_PAY("UNION_PAY", "银联"),
    POS("POS", "POS"),
    CASH("CASH", "现金"),
    OTHER("OTHER", "门店其它（对公转账）"),
    TRANSFER_ACCOUNTS("TRANSFER_ACCOUNTS", "转账"),
    STORE_CREDIT_MONEY("STORE_CREDIT_MONEY", "门店信用金");


    private String value;

    private String description;

    OrderBillingPaymentType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static OrderBillingPaymentType getOrderBillingPaymentTypeByValue(String value) {
        for (OrderBillingPaymentType paymentType : OrderBillingPaymentType.values()) {
            if (Objects.equals(value, paymentType.getValue())) {
                return paymentType;
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

    public static OrderBillingPaymentType getOrderBillingPaymentTypeByDescription(String description) {
        for (OrderBillingPaymentType paymentType : OrderBillingPaymentType.values()) {
            if (Objects.equals(description, paymentType.getDescription())) {
                return paymentType;
            }
        }
        return null;
    }
}
