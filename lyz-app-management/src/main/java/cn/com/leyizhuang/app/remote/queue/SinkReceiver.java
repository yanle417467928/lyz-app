package cn.com.leyizhuang.app.remote.queue;

import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.queue.MqMessage;
import cn.com.leyizhuang.app.foundation.pojo.queue.MqOrderChannel;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import java.io.IOException;

/**
 * @author Richard
 * @create 2017/12/27.
 */
@EnableBinding(value = {MqOrderChannel.class})
@Slf4j
public class SinkReceiver {

    ObjectMapper objectMapper = new ObjectMapper();

    @StreamListener(value = MqOrderChannel.RECEIVE_ORDER)
    public void receiveOrder(MqMessage message) {
        try {
            OrderBaseInfo order = objectMapper.readValue(message.getContent(), OrderBaseInfo.class);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

}
