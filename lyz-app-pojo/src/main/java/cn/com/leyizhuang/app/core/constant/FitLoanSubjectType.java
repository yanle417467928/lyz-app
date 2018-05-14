package cn.com.leyizhuang.app.core.constant;

/**
 * @author Jerry.Ren
 * create 2018-05-14 9:39
 * desc: 装饰公司贷方主体(指未付款方)
 **/

public enum FitLoanSubjectType {

    FIT("FIT", "装饰公司"), ZG("ZG", "专供");

    private String value;

    private String description;

    FitLoanSubjectType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static FitLoanSubjectType getFitLoanSubjectTypeByValue(String value) {
        for (FitLoanSubjectType fitLoanSubjectType : FitLoanSubjectType.values()) {
            if (value.equalsIgnoreCase(fitLoanSubjectType.getValue())) {
                return fitLoanSubjectType;
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
