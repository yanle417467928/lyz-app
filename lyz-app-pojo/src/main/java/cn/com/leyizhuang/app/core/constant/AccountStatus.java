package cn.com.leyizhuang.app.core.constant;

/**
 * @author CrazyApeDX
 *         Created on 2017/3/24.
 */
public enum AccountStatus {

    ENABLE(Boolean.TRUE,"启用"), DISABLE(Boolean.FALSE,"禁用");

    private final Boolean value;
    private final String info;
    AccountStatus(Boolean value, String info) {
        this.value = value;
        this.info = info;
    }
    public String getInfo() {
        return info;
    }
    public Boolean getValue(){
        return value;
    }
}
