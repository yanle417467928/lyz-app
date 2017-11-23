package cn.com.leyizhuang.app.core.constant;

/**
 * App后台管理导航菜单类型枚举
 *
 * @author Richard
 * Created on 2017-05-08 10:31
 **/
public enum AppAdminResourceType {
    MENU("MENU", "菜单"), BUTTON("BUTTON", "按钮");

    private String value;
    private String description;

    AppAdminResourceType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static AppAdminResourceType getAppAdminResourceTypeByValue(String value) {
        for (AppAdminResourceType resourceType : AppAdminResourceType.values()) {
            if (value.equals(resourceType.getValue())) {
                return resourceType;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }


}
