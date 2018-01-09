package cn.com.leyizhuang.app.core.constant;

/**
 * 订单券类型
 *
 * @author caiyu
 * @date 2017/12/7
 */
public enum OrderCouponType {
    PRODUCT_COUPON("产品券", 1),
    CASH_COUPON("现金券", 2);

    private final String value;
    private final Integer seq;

    OrderCouponType(String value, Integer seq) {
        this.value = value;
        this.seq = seq;
    }

    public String getValue() {
        return value;
    }

    public Integer getSeq() {
        return seq;
    }
}
