package cn.com.leyizhuang.app.core.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 门店预存款变更类型
 *
 * @author Richard
 * Created on 2017/12/01.
 */
public enum StorePreDepositChangeType {

    PLACE_ORDER("PLACE_ORDER", "订单消费"),
    RETURN_ORDER("RETURN_ORDER", "退单返还"),
    CANCEL_ORDER("CANCEL_ORDER", "取消订单返还"),
    ALIPAY_RECHARGE("ALIPAY_RECHARGE", "支付宝充值"),
    WECHAT_RECHARGE("WECHAT_RECHARGE", "微信充值"),
    UNIONPAY_RECHARGE("UNIONPAY_RECHARGE", "银联充值"),
    ADMIN_RECHARGE("ADMIN_RECHARGE", "管理员充值"),
    ADMIN_CHANGE("ADMIN_CHANGE", "管理员修改"),
    JX_PRICE_DIFFERENCE_RETURN("JX_PRICE_DIFFERENCE_RETURN", "经销差价返还"),
    JX_PRICE_DIFFERENCE_DEDUCTION("JX_PRICE_DIFFERENCE_DEDUCTION", "经销差价扣除");

    private final String value;
    private final String description;

    StorePreDepositChangeType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static StorePreDepositChangeType getStorePreDepositChangeTypeByValue(String value) {
        for (StorePreDepositChangeType changeType : StorePreDepositChangeType.values()) {
            if (value.equals(changeType.getValue())) {
                return changeType;
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

    public static List<StorePreDepositChangeType> getRechargeType() {
        List<StorePreDepositChangeType> rechargeList = new ArrayList<>();
        rechargeList.add(ALIPAY_RECHARGE);
        rechargeList.add(WECHAT_RECHARGE);
        rechargeList.add(UNIONPAY_RECHARGE);
        return rechargeList;
    }

    public static List<StorePreDepositChangeType> getConsumptionType() {
        List<StorePreDepositChangeType> consumptionList = new ArrayList<>();
        consumptionList.add(PLACE_ORDER);
        consumptionList.add(RETURN_ORDER);
        consumptionList.add(CANCEL_ORDER);
        return consumptionList;
    }

}
