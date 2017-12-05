package cn.com.leyizhuang.app.core.constant;

/**
 * @author GenerationRoad
 * @date 2017/11/20
 */
public enum PaymentDataStatus {

    /**
     * 支付订单：有 ,充值订单：有 ,还款订单：有
     */
    WAIT_PAY("等待支付"),
    /**
     * 支付订单：有 ,充值订单：有 ,还款订单：有
     */
    TRADE_SUCCESS("支付成功"),
    /**
     * 支付订单：有 ,充值订单：有 ,还款订单：有
     */
    TRADE_FAIL("支付失败"),
    /**
     * 退货订单：有
     */
    WAIT_REFUND("等待退款"),
    /**
     * 退货订单：有
     */
    REFUND_SUCCESS("退款成功"),
    /**
     * 退货订单：有
     */
    REFUND_FAIL("退款失败");

    private final String value;

    PaymentDataStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
