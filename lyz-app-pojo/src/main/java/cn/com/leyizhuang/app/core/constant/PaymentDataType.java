package cn.com.leyizhuang.app.core.constant;

/**
 * @author GenerationRoad
 * @date 2017/11/18
 */
public enum PaymentDataType {
    ST_PRE_DEPOSIT("ST_PRE_DEPOSIT","门店预存款充值"),
    CUS_PRE_DEPOSIT("CUS_PRE_DEPOSIT","客户预存款充值"),
    DEC_PRE_DEPOSIT("DEC_PRE_DEPOSIT","装饰公司预存款充值"),
    ORDER("ORDER","订单支付"),
    REPAYMENT("REPAYMENT","欠款还款"),
    BILLPAY("BILLPAY","账单还款");


    private final String value;

    private final String description;


    PaymentDataType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }
}
