package cn.com.leyizhuang.app.core.constant;

/**
 *
 * @author caiyu
 * @date 2017/11/22
 */
public enum LogisticStatus {

    RECEIVED(0, "已接收"),
    ALREADY_POSITIONED(1, "已定位"),
    PICKING_GOODS(2, "已拣货"),
    LOADING(3, "已装车"),
    SEALED_CAR(4, "已封车"),
    SENDING(5, "未投妥"),
    CONFIRM_ARRIVAL(6, "已签收"),
    REJECT(7,"拒签");

    private final int value;
    private final String description;

    LogisticStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static LogisticStatus getLogisticStatusByValue(Integer value) {
        for (LogisticStatus logisticStatus : LogisticStatus.values()) {
            if (value == logisticStatus.getValue()) {
                return logisticStatus;
            }
        }
        return null;
    }
}
