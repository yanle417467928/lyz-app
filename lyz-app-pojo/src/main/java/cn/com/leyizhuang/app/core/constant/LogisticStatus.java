package cn.com.leyizhuang.app.core.constant;

/**
 *
 * @author caiyu
 * @date 2017/11/22
 */
public enum LogisticStatus {
    INITIAL(0,"等待物流接收"),
    RECEIVED(1, "已接收"),
    ALREADY_POSITIONED(2, "已定位"),
    PICKING_GOODS(3, "已拣货"),
    LOADING(4, "已装车"),
    SEALED_CAR(5, "已封车"),
    SENDING(6, "未投妥"),
    CONFIRM_ARRIVAL(7, "已签收"),
    REJECT(8,"拒签");

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
