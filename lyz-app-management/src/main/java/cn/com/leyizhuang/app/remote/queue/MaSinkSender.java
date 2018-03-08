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
 * Created by panjie on 2018/1/23.
 */
@EnableBinding(value = {MqOrderChannel.class})
@Slf4j
public class MaSinkSender {

    @Resource
    private MqOrderChannel orderChannel;

    public void sendAllocationToEBSAndRecord(String number) {
        log.info("sendAllocationToEBSAndRecord,发送门店调拨出库接口，调拨单号：" + number);
        if (StringUtils.isNotBlank(number)) {
            MqMessage message = new MqMessage();
            message.setType(MqMessageType.ALLOCATION_OUTBOUND);
            message.setContent(JSON.toJSONString(number));
            orderChannel.sendOrder().send(MessageBuilder.withPayload(message).build());
        }
    }

    public void sendAllocationReceivedToEBSAndRecord(String number) {
        log.info("sendAllocationToEBSAndRecord,发送门店调拨入库接口，调拨单号：" + number);
        if (StringUtils.isNotBlank(number)) {
            MqMessage message = new MqMessage();
            message.setType(MqMessageType.ALLOCATION_INBOUND);
            message.setContent(JSON.toJSONString(number));
            orderChannel.sendOrder().send(MessageBuilder.withPayload(message).build());
        }
    }


    public void sendStorePickUpReceivedToEBSAndRecord(String number) {
        log.info("sendStorePickUpReceivedToEBSAndRecord,发送门店自提单发货接口，调拨单号：" + number);
        if (null != number) {
            MqMessage message = new MqMessage();
            message.setType(MqMessageType.ORDER_RECEIVE);
            message.setContent(JSON.toJSONString(number));
            orderChannel.sendOrder().send(MessageBuilder.withPayload(message).build());
        }
    }


    public void sendStorePickUpReturnOrderReceiptToEBSAndRecord(String returnNumber) {
        log.info("sendStorePickUpReceivedToEBSAndRecord,发送门店自提单收货接口，调拨单号：" + returnNumber);
        if (null != returnNumber) {
            MqMessage message = new MqMessage();
            message.setType(MqMessageType.RETURN_ORDER_RECEIPT);
            message.setContent(JSON.toJSONString(returnNumber));
            orderChannel.sendOrder().send(MessageBuilder.withPayload(message).build());
        }
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


    /**
     * @param
     * @return
     * @throws
     * @title 订单收款消息队列
     * @descripe
     * @author GenerationRoad
     * @date 2018/3/2
     */
    public void sendOrderReceipt(String receiptNumber) {
        log.info("sendOrderReceipt,发送订单收款信息到拆单队列,Begin\n 收款单号:{}", receiptNumber);
        if (StringUtils.isNotBlank(receiptNumber)) {
            MqMessage message = new MqMessage();
            message.setType(MqMessageType.ORDER_RECEIPT);
            message.setContent(JSON.toJSONString(receiptNumber));
            orderChannel.sendOrder().send(MessageBuilder.withPayload(message).build());
        }
        log.info("sendOrderReceipt,发送订单收款信息到拆单队列,End", JSON.toJSONString(receiptNumber));
    }

    public void sendOrderRefund(String refundNumber) {
        log.info("refundNumber,发送订单退款信息到拆单队列,Begin\n 退款单号:{}", refundNumber);
        if (StringUtils.isNotBlank(refundNumber)) {
            MqMessage message = new MqMessage();
            message.setType(MqMessageType.ORDER_REFUND);
            message.setContent(JSON.toJSONString(refundNumber));
            orderChannel.sendOrder().send(MessageBuilder.withPayload(message).build());
        }
        log.info("sendOrderReceipt,发送订单退款信息到拆单队列,End", JSON.toJSONString(refundNumber));
    }
}
