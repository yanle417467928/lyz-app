package cn.com.leyizhuang.app.core.constant;

/**
 * @author Jerry.Ren
 * create 2018-05-14 9:39
 * desc: 装饰公司贷方主体(指未付款方)
 **/

public enum LoanSubjectType {

    FIT("FIT", "装饰公司"), ZG("ZG", "专供");

    private String value;

    private String description;

    LoanSubjectType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static LoanSubjectType getFitLoanSubjectTypeByValue(String value) {
        for (LoanSubjectType loanSubjectType : LoanSubjectType.values()) {
            if (value.equalsIgnoreCase(loanSubjectType.getValue())) {
                return loanSubjectType;
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
