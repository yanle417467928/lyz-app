package cn.com.leyizhuang.app.remote.queue;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqMessage;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqMessageType;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqOrderChannel;
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

    public void sendOrder(String orderNumber) {
        log.info("sendOrder,发送需拆单订单到拆单队列,Begin\n 订单号:{}", orderNumber);
        if (StringUtils.isNotBlank(orderNumber)) {
            MqMessage message = new MqMessage();
            message.setType(MqMessageType.ORDER);
            message.setContent(JSON.toJSONString(orderNumber));
            orderChannel.sendOrder().send(MessageBuilder.withPayload(message).build());
        }
        log.info("sendOrder,发送需拆单订单到拆单队列,End", JSON.toJSONString(orderNumber));
    }

    public void sendRechargeReceipt(String rechargeNo) {
        log.info("sendRechargeReceipt,发送充值收款信息到拆单队列,Begin\n 充值单号:{}", rechargeNo);
        if (StringUtils.isNotBlank(rechargeNo)) {
            MqMessage message = new MqMessage();
            message.setType(MqMessageType.RECHARGE_RECEIPT);
            message.setContent(JSON.toJSONString(rechargeNo));
            orderChannel.sendOrder().send(MessageBuilder.withPayload(message).build());
        }
        log.info("sendRechargeReceipt,发送充值收款信息到拆单队列,End", JSON.toJSONString(rechargeNo));
    }


}
