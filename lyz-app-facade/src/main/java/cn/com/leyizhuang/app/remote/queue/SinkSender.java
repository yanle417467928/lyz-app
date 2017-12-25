package cn.com.leyizhuang.app.remote.queue;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

/**
 * 消息生产者
 *
 * @author Richard
 * Created on 2017-12-25 11:42
 **/
@Component
public interface SinkSender {

    @Output(Sink.INPUT)
    MessageChannel output();
}
