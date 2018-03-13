package cn.com.leyizhuang.app.core.constant.remote.webservice.ebs;

/**
 * 充值对象枚举
 *
 * @author Richard
 * Created on 2018-01-18 9:48
 **/
public enum ChargeObjType {
    CUSTOMER(1, "顾客充值"),
    STORE(2, "门店充值"),
    BOND(3, "保证金"),
    PRODUCT_COUPON(4, "购买产品券"),
    STORE_CREDIT(5, "装饰公司信用金");


    private int value;

    private String description;

    ChargeObjType(int value, String description) {
        this.value = value;
        this.description = description;
    }
}
