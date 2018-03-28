package cn.com.leyizhuang.app.core.constant;

/**
 * @author Created on 2018-03-28 11:41
 **/
public enum DataTransferExceptionType {
    NDT("NDT", "没有需要迁移的数据"),
    DMNF("DMNF", "订单装饰经理信息没有找到"),
    CNF("CNF", "订单顾客信息没有找到"),
    SNF("SNF", "订单导购信息没有找到"),
    STNF("STNF", "订单门店信息没有找到"),
    NOTORDERDATA("NOTORDERDATA","订单账单没有找到"),
    UNKNOWN("UNKNOWN", "未知异常信息");

    private final String value;
    private final String desc;

    DataTransferExceptionType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

}
