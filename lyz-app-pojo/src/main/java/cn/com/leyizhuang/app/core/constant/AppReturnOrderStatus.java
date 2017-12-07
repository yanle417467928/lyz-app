package cn.com.leyizhuang.app.core.constant;

import java.util.Objects;

/**
 * @author Jerry.Ren
 * Notes: 退货单配送状态枚举
 * Created with IntelliJ IDEA.
 * Date: 2017/12/4.
 * Time: 10:27.
 */

public enum AppReturnOrderStatus {

    /**
     * 退货单：有
     */
    RETURNING(1, "退货中"),
    /**
     * 配送单:有  自提单:有  买券订单:无
     */
    CANCELED(2, "已取消"),
    /**
     * 退货单：有
     */
    PENDING_PICK_UP(3, "待收货"),
    /**
     * 退货单：有
     */
    PENDING_REFUND(4, "待退款"),
    /**
     * 配送单:有  自提单:有  买券订单:有
     */
    FINISHED(5, "已完成");

    private final Integer value;
    private final String description;

    AppReturnOrderStatus(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public static AppReturnOrderStatus getAppOrderReturnStatusByValue(Integer value) {
        for (AppReturnOrderStatus status : AppReturnOrderStatus.values()) {
            if (Objects.equals(status.getValue(), value)) {
                return status;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

}
