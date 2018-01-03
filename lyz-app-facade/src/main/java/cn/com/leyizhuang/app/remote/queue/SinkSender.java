package cn.com.leyizhuang.app.remote.queue;

import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.queue.MqMessage;
import cn.com.leyizhuang.app.foundation.pojo.queue.MqMessageType;
import cn.com.leyizhuang.app.foundation.pojo.queue.MqOrderChannel;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;

/**
 * @author Created on 2017-12-26 18:16
 **/
@EnableBinding(value = {MqOrderChannel.class})
@Slf4j
public class SinkSender {


    @Resource
    private MqOrderChannel orderChannel;

    public void sendOrder(OrderBaseInfo order) {
        log.info("发送需拆单订单到拆单队列,Begin\n 订单号:{}", order.getOrderNumber());
        MqMessage message = new MqMessage();
        message.setType(MqMessageType.ORDER);
        message.setContent(JSON.toJSONString(order));
        orderChannel.sendOrder().send(MessageBuilder.withPayload(message).build());
        log.info("发送需拆单订单到拆单队列,End", JSON.toJSONString(order));
    }


}
