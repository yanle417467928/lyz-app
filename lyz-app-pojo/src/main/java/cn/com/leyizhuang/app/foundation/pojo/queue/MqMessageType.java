package cn.com.leyizhuang.app.foundation.pojo.queue;

/**
 * 消息队列消息类型
 *
 * @author Richard
 * Created on 2018-01-02 8:59
 **/
public enum MqMessageType {

    ORDER("ORDER","订单头"),ORDER_GOODS("ORDER_GOODS","订单产品");

    private String value;

    private String description;

    MqMessageType(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
