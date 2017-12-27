package cn.com.leyizhuang.app.remote.queue;

import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.queue.MqOrderChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

/**
 * @author Richard
 * @create 2017/12/27.
 */
@EnableBinding(value = {MqOrderChannel.class})
@Slf4j
public class SinkReceiver {


    @StreamListener(value = MqOrderChannel.RECEIVE_ORDER)
    public void receiveOrder(OrderBaseInfo order) {
        log.info("接收到的订单信息:{}",order);
    }

}
