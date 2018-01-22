package cn.com.leyizhuang.app.core.constant.remote.webservice.ebs;

/**
 * 充值银行清单
 *
 * @author Richard
 * Created on 2018-01-18 9:48
 **/
public enum ChargeType {
    ICBC(1, "工商银行");


    private int value;

    private String description;

    ChargeType(int value, String description) {
        this.value = value;
        this.description = description;
    }
}
