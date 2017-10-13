package cn.com.leyizhuang.app.core.constant;

/**
 * App后台管理导航菜单类型枚举
 *
 * @author Richard
 *         Created on 2017-05-08 10:31
 **/
public enum AppAdminResourceType {
    MENU("菜单"),BUTTON("按钮");

    private String value;

    AppAdminResourceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
