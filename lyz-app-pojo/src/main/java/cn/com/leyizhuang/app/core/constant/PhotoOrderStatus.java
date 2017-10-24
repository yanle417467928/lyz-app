package cn.com.leyizhuang.app.core.constant;

/**
 * @author GenerationRoad
 * @date 2017/10/24
 */
public enum PhotoOrderStatus {

    PENDING ("待处理"), PLACORDER("已下单"), PAYED("已支付"),  FINISH("已完成");

    private final String value;

    PhotoOrderStatus(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

}
