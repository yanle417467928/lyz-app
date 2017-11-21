package cn.com.leyizhuang.app.core.constant;

/**
 * @author GenerationRoad
 * @date 2017/11/20
 */
public enum PaymentDataStatus {
    WAIT_PAY("等待支付"), TRADE_SUCCESS("支付成功"),
    TRADE_FAIL("支付失败");


    private final String value;

    PaymentDataStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
