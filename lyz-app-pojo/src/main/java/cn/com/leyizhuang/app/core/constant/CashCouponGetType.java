package cn.com.leyizhuang.app.core.constant;

/**
 * 优惠券获取方式
 *
 * @author CrazyApeDX
 * Created on 2017/3/24.
 */
public enum CashCouponGetType {

    BUY(0, "购买"), MANUAL_GRANT(1, "手动发放"), CANCEL_ORDER(2, "取消订单退回");

    private final int value;
    private final String description;

    CashCouponGetType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

}
