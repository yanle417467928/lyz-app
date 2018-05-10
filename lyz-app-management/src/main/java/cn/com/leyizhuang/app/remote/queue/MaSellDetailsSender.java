package cn.com.leyizhuang.app.remote.queue;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqMessage;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqMessageType;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqSellDetailsChannel;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;


/**
 * 销量统计生产者
 * Created by panjie on 2018/1/24.
 */
@EnableBinding(value = {MqSellDetailsChannel.class})
@Slf4j
public class MaSellDetailsSender {

    @Resource
    private  MqSellDetailsChannel mqSellDetailsChannel;

    /**
     * 发送销量明细到 HQ
     * @param data
     */
    public void sendSellDetailsToHQ(String data) {
        log.info("发送销量明细数据 to HQ");
        if (StringUtils.isNotBlank(data)){

            mqSellDetailsChannel.sendDetailsToHQ().send(MessageBuilder.withPayload(data).build());
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
