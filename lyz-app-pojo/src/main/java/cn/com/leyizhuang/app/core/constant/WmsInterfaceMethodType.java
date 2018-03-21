package cn.com.leyizhuang.app.core.constant;

/**
 * @author Jerry.Ren
 * create 2018-02-26 13:52
 * desc: WMS重传的单类型
 **/
public enum WmsInterfaceMethodType {

    ORDER("order", "传输订单"),
    RE_ORDER("returnOrder", "传输退单"),
    C_ORDER("cancelOrder", "传输取消订单"),
    C_RE_ORDER("cancelReturnOrder", "传输取消退单"),
    RE_ORDER_ENTER("returnOrderEnter", "传输退单收货确认");

    private final String value;
    private final String description;


    WmsInterfaceMethodType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }


    @Override
    public String toString() {
        return this.getValue() + ":" + this.getDescription();
    }
}
