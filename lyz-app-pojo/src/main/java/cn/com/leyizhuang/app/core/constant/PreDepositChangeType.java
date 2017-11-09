package cn.com.leyizhuang.app.core.constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author GenerationRoad
 * @date 2017/11/7
 */
public enum  PreDepositChangeType {


   /*充值*/ ALIPAY_RECHARGE("支付宝"), WECHAT_RECHARGE("微信"), ONLINE_RECHARGE("线上充值"), UNIONPAY_RECHARGE("银联"), WITHDRAWALS("提现"),

   /*消费*/  PURCHASE_VOLUME("购买产品卷"), PLACE_ORDER("下单消费"), CANCELLATION_ORDER("订单取消退款"), RETURN_ORDER("订单退货退款");
    //以后再增加,增加请把下面两个方法也加上

    private final String value;
    PreDepositChangeType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }



    /**
     * @title   获取充值类型
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/11/7
     */
    public static List<PreDepositChangeType> getRechargeType(){
        List<PreDepositChangeType> preDepositChangeTypeList = new ArrayList<>();
        preDepositChangeTypeList.add(PreDepositChangeType.ALIPAY_RECHARGE);
        preDepositChangeTypeList.add(PreDepositChangeType.WECHAT_RECHARGE);
        preDepositChangeTypeList.add(PreDepositChangeType.UNIONPAY_RECHARGE);
        preDepositChangeTypeList.add(PreDepositChangeType.ONLINE_RECHARGE);
        preDepositChangeTypeList.add(PreDepositChangeType.WITHDRAWALS);
        return preDepositChangeTypeList;
    }

    /**
     * @title   获取消费类型
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/11/7
     */
    public static List<PreDepositChangeType> getConsumptionType(){
        List<PreDepositChangeType> preDepositChangeTypeList = new ArrayList<>();
        preDepositChangeTypeList.add(PreDepositChangeType.PURCHASE_VOLUME);
        preDepositChangeTypeList.add(PreDepositChangeType.PLACE_ORDER);
        preDepositChangeTypeList.add(PreDepositChangeType.CANCELLATION_ORDER);
        preDepositChangeTypeList.add(PreDepositChangeType.RETURN_ORDER);
        return preDepositChangeTypeList;
    }

}
