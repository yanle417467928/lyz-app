package cn.com.leyizhuang.app.remote.queue;

import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.queue.MqOrderChannel;
import com.gexin.fastjson.JSON;
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
        orderChannel.sendOrder().send(MessageBuilder.withPayload(order).build());
        log.info("发送订单信息:{}", JSON.toJSONString(order));
    }

}
