package cn.com.leyizhuang.app.core.constant;

/**
 * @author CrazyApeDX
 *         Created on 2017/3/24.
 */
public enum IdentityTypeEnum {

    MEMBER("会员"), RETAIL("零售");

    private final String value;

    IdentityTypeEnum(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
