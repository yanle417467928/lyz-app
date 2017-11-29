package cn.com.leyizhuang.app.core.constant;

/**
 * @author GenerationRoad
 * @date 2017/11/18
 */
public enum PaymentDataType {
    ST_PRE_DEPOSIT("门店预存款充值"), CUS_PRE_DEPOSIT("客户预存款充值"),
    DEC_PRE_DEPOSIT("装饰公司预存款充值"), ORDER("订单支付"), REPAYMENT("欠款还款");


    private final String value;

    PaymentDataType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
