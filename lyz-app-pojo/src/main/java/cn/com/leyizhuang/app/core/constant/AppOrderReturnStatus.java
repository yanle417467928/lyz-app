package cn.com.leyizhuang.app.core.constant;

import java.util.Objects;

/**
 * @author Jerry.Ren
 * Notes: 退货单配送状态枚举
 * Created with IntelliJ IDEA.
 * Date: 2017/12/4.
 * Time: 10:27.
 */

public enum AppOrderReturnStatus {

    /**
     * 退货单：有
     */
    RETURNING("RETURNING", "退货中"),
    /**
     * 配送单:有  自提单:有  买券订单:无
     */
    CANCELED("CANCELED", "已取消"),
    /**
     * 退货单：有
     */
    PENDING_PICK_UP("PENDING_PICK_UP", "待收货"),
    /**
     * 退货单：有
     */
    PENDING_REFUND("PENDING_REFUND", "待退款"),
    /**
     * 配送单:有  自提单:有  买券订单:有
     */
    FINISHED("FINISHED", "已完成");

    private final String value;
    private final String description;

    AppOrderReturnStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static AppOrderReturnStatus getAppOrderReturnStatusByValue(String value) {
        for (AppOrderReturnStatus status : AppOrderReturnStatus.values()) {
            if (Objects.equals(value, status.getValue())) {
                return status;
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
