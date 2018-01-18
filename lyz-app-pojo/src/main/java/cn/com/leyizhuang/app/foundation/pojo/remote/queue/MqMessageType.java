package cn.com.leyizhuang.app.foundation.pojo.remote.queue;

/**
 * 消息队列消息类型
 *
 * @author Richard
 * Created on 2018-01-02 8:59
 **/
public enum MqMessageType {

    ORDER("ORDER", "订单头"), ORDER_GOODS("ORDER_GOODS", "订单产品"), ORDER_RECEIPT("ORDER_RECEIPT", "订单收款"),
    RECHARGE_RECEIPT("RECHARGE_RECEIPT", "充值收款");

    private String value;

    private String description;

    MqMessageType(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
