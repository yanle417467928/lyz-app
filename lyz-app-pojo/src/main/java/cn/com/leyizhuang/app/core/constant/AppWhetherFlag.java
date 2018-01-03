package cn.com.leyizhuang.app.core.constant;

/**
 * “是”，“否” flag
 *
 * @author Richard
 * Created on 2018-01-03 11:15
 **/
public enum AppWhetherFlag {

    Y("Y", "是"), N("N", "否");

    private String value;
    private String description;

    AppWhetherFlag(String value, String description) {
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
