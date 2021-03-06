package cn.com.leyizhuang.app.foundation.pojo.remote.queue;

import java.util.Objects;

/**
 * 消息队列消息类型
 *
 * @author Richard
 * Created on 2018-01-02 8:59
 **/

public enum MqMessageType {

    ORDER("ORDER", "订单头"),
    ORDER_GOODS("ORDER_GOODS", "订单产品"),
    ORDER_RECEIPT("ORDER_RECEIPT", "订单收款"),
    ORDER_REFUND("ORDER_REFUND", "订单退款"),
    RECHARGE_RECEIPT("RECHARGE_RECEIPT", "充值收款"),
    ALLOCATION_INBOUND("ALLOCATION_INBOUND", "调拨入库"),
    ALLOCATION_OUTBOUND("ALLOCATION_OUTBOUND", "调拨出库"),
    RETURN_ORDER("RETURN_ORDER", "退单头"),
    SELL_DETAILS_HQ("SELL_DETAILS_HQ", "销量数据"),
    SELL_ORDER_DETAILS("SELL_ORDER_DETAILS", "发送下单销量明细到后台"),
    SELL_RETURN_ORDER_DETAILS("SELL_RETURN_ORDER_DETAILS", "发送退单销量明细到后台"),
    ORDER_RECEIVE("ORDER_RECEIVE", "自提单发货"),
    RETURN_ORDER_RECEIPT("RETURN_ORDER_RECEIPT", "自提单退单收货"),
    WITHDRAW_REFUND("WITHDRAW_REFUND", "提现退款"),
    ORDER_RECEIVABLE("ORDER_RECEIVABLE","订单应收"),
    ORDER_RETIRED("ORDER_RECEIVABLE","退单应退"),
    CREDIT_RECHARGE_RECEIPT("CREDIT_RECHARGE_RECEIPT", "信用金充值"),
    KD_SELL_SEND("KD_SELL_SEND", "金蝶销退货发送");


    private String value;

    private String description;

    MqMessageType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static MqMessageType getMqMessageTypeByValue(String value) {
        for (MqMessageType mqMessageType : MqMessageType.values()) {
            if (Objects.equals(value, mqMessageType.getValue())) {
                return mqMessageType;
            }
        }
        return null;
    }
}
