package cn.com.leyizhuang.app.core.constant;

import java.util.Objects;

/**
 * @author Jerry.Ren
 * Notes: 退货单配送状态枚举
 * Created with IntelliJ IDEA.
 * Date: 2017/12/4.
 * Time: 10:27.
 * <SECTION>
 *   退货流程：
 *   初始退货单状态为‘带退货’————>wms接单后派发配送员取货
 *   退货单状态更改为‘退货中’————>配送员确认取货完成后（**若用户点取消退货**）
 *   ***退货单状态更改为‘已取消’————>结束***
 *   退货单状态更改为‘待退款’————>门店返配上架后退款
 *   退货单状态更改为‘已完成’————>结束
 *
 * </SECTION>
 *
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
    PENDING_PICK_UP(3, "待退货"),
    /**
     * 退货单：有
     */
    PENDING_REFUND(4, "待退款"),
    /**
     * 配送单:有  自提单:有  买券订单:有
     */
    FINISHED(5, "已完成"),
    /**
     * 退货单: 有
     */
    CANCELING(6, "取消中");

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
