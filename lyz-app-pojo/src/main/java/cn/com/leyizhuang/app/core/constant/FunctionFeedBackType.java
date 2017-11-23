package cn.com.leyizhuang.app.core.constant;

/**
 * @author CrazyApeDX
 * Created on 2017/3/24.
 */
public enum FunctionFeedBackType {

    FE("FE", "功能异常"), EP("EP", "体验问题"), FS("FS", "新功能建议"), OT("OT", "其它");

    private final String value;
    private final String description;

    FunctionFeedBackType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static FunctionFeedBackType getFunctionFeedBackTypeByValue(String value) {
        for (FunctionFeedBackType functionFeedBackType : FunctionFeedBackType.values()) {
            if (value.equalsIgnoreCase(functionFeedBackType.getValue())) {
                return functionFeedBackType;
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
