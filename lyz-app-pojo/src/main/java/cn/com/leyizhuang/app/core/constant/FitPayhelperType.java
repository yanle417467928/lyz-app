package cn.com.leyizhuang.app.core.constant;

/**
 * @author Jerry.Ren
 * create 2018-05-14 10:06
 * desc:代付方主体类型(指支付方)
 **/
public enum FitPayhelperType {

    SELLER_MANAGER("SELLER_MANAGER", "客户经理");

    private String value;

    private String description;

    FitPayhelperType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
