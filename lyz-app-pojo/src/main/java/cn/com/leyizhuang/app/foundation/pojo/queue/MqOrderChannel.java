package cn.com.leyizhuang.app.foundation.pojo.queue;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

/**
 * @author Created on 2017-12-27 8:37
 **/
@Component
public interface MqOrderChannel {

    String RECEIVE_ORDER = "receiveOrder";

    @Input("receiveOrder")
    SubscribableChannel receiveOrder();

    String SEND_ORDER = "sendOrder";

    @Output("sendOrder")
    MessageChannel sendOrder();


}
