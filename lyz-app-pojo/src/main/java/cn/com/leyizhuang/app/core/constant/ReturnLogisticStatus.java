package cn.com.leyizhuang.app.core.constant;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2017/12/6.
 * Time: 19:41.
 */

public enum ReturnLogisticStatus {

    RECEIVED(1, "已接收"),
    PICKING_GOODS(2, "取货中"),
    PICKUP_COMPLETE(3, "取货完成"),
    AGAIN_ON_SALE(4, "返配上架");

    private final int value;
    private final String description;

    ReturnLogisticStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static ReturnLogisticStatus getReturnLogisticStatusByValue(Integer value) {
        for (ReturnLogisticStatus returnLogisticStatus : ReturnLogisticStatus.values()) {
            if (value == returnLogisticStatus.getValue()) {
                return returnLogisticStatus;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
