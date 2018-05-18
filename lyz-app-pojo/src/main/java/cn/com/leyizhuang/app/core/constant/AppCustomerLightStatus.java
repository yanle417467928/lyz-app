package cn.com.leyizhuang.app.core.constant;

/**
 * Created with IntelliJ IDEA.
 * Created by Jerry.Ren
 * Date: 2017/10/9.
 * Time: 17:13.
 *
 * 2018年5月18日 17点16分 Jerry
 *
 * 修改 红灯——>铁粉（6个月内平均下单2单以上）
 *      绿灯——>粉丝（6个月内平均下单1单以上）
 *      黄灯——>会员（6个月内下单1-5单以内）
 *      熄灯（连续3个月未下单客户）
 *
 * 修改页面显示，修改枚举描述即可，枚举类型走原来数据格式
 *
 * 丢弃无灯（NOT）枚举值，由于有数据限制，与熄灯类同
 */

public enum AppCustomerLightStatus {

    NOT("熄灯"),
    CLOSE("熄灯"),
    RED("铁粉"),
    GREEN("粉丝"),
    YELLOW("会员");

    private final String value;

    AppCustomerLightStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
