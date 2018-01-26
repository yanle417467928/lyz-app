package cn.com.leyizhuang.app.foundation.pojo.remote.queue;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

/**
 * 销量明细传输通道
 * Created by panjie on 2018/1/24.
 */
@Component
public interface MqSellDetailsChannel {

    String RECEIVE_DETAILS = "receiveDetails";

    @Input("receiveDetails")
    SubscribableChannel receiveDetails();

    String SEND_DETAILS = "sendDetails";

    @Output("sendDetails")
    MessageChannel sendDetails();

    String SEND_DETAILS_TOHQ = "sendDetailsToHQ";

    @Output("sendDetailsToHQ")
    MessageChannel sendDetailsToHQ();

}
