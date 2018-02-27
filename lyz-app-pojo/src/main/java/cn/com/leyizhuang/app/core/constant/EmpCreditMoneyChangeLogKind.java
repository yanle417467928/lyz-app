package cn.com.leyizhuang.app.core.constant;

/**
 * @author Jerry.Ren
 * create 2018-02-27 11:59
 * desc: 导购信用金变更记录日志类型
 **/
public enum EmpCreditMoneyChangeLogKind {

    SYS_ADJUSTMENT("sysAdjustment", "系统调整"),
    ORD_ADJUSTMENT("ordAdjustment", "订单调整");

    private String value;
    private String desc;

    EmpCreditMoneyChangeLogKind(String value, String desc) {
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
