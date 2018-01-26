package cn.com.leyizhuang.app.remote.queue;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqMessage;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqMessageType;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqSellDetailsChannel;
import com.gexin.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;

/**
 * Created by panjie on 2018/1/25.
 */
@EnableBinding(value = {MqSellDetailsChannel.class})
@Slf4j
public class SellDetailsSender {

    @Resource
    private  MqSellDetailsChannel mqSellDetailsChannel;

    public void sendOrderSellDetailsTOManagement(String orderNumber){

        log.info("发送下单销量明细数据，单号："+orderNumber);
        if (StringUtils.isNotBlank(orderNumber)){
            MqMessage message = new MqMessage();
            message.setType(MqMessageType.SELL_ORDER_DETAILS);
            message.setContent(JSON.toJSONString(orderNumber));
            mqSellDetailsChannel.sendDetails().send(MessageBuilder.withPayload(message).build());
        }
    }

    public void sendReturnOrderSellDetailsTOManagement(String ReturnOrderNumber){

        log.info("发送退单销量明细数据，单号："+ReturnOrderNumber);
        if (StringUtils.isNotBlank(ReturnOrderNumber)){
            MqMessage message = new MqMessage();
            message.setType(MqMessageType.SELL_RETURN_ORDER_DETAILS);
            message.setContent(JSON.toJSONString(ReturnOrderNumber));
            mqSellDetailsChannel.sendDetails().send(MessageBuilder.withPayload(message).build());
        }
    }
}
