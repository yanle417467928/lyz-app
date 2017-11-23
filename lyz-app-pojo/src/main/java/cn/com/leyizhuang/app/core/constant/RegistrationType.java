package cn.com.leyizhuang.app.core.constant;

/**
 * 会员注册方式枚举
 *
 * @author Richard
 * Created on 2017/07/14.
 */
public enum RegistrationType {

    IMPORT("导入"), APP("APP注册"), MANAGER("管理员注册"), QR_CODE("扫二维码注册");
    private final String value;

    RegistrationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
