package cn.com.leyizhuang.app.remote.queue;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqMessage;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqMessageType;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqOrderChannel;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqSellDetailsChannel;
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

    @Resource
    private MqSellDetailsChannel sellDetailsChannel;

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

    public void sendReturnOrder(String returnNumber) {
        log.info("sendOrder,发送需拆单退单到拆单队列,Begin\n 退单号:{}", returnNumber);
        if (StringUtils.isNotBlank(returnNumber)) {
            MqMessage message = new MqMessage();
            message.setType(MqMessageType.RETURN_ORDER);
            message.setContent(JSON.toJSONString(returnNumber));
            orderChannel.sendOrder().send(MessageBuilder.withPayload(message).build());
        }
        log.info("sendOrder,发送需拆单退单到拆单队列,End", JSON.toJSONString(returnNumber));
    }

    /**
     * 提现退款发送消息队列
     *
     * @param refundNumber 退款单号
     */
    public void sendWithdrawRefund(String refundNumber) {
        log.info("sendWithdrawRefund,发送提现退款到拆单队列,Begin\n 退款单号:{}", refundNumber);
        if (StringUtils.isNotBlank(refundNumber)) {
            MqMessage message = new MqMessage();
            message.setType(MqMessageType.WITHDRAW_REFUND);
            message.setContent(JSON.toJSONString(refundNumber));
            orderChannel.sendOrder().send(MessageBuilder.withPayload(message).build());
        }
        log.info("sendWithdrawRefund,发送提现退款到拆单队列,End", JSON.toJSONString(refundNumber));
    }

    /**
     * 发送销售记录到
     */


    /**
     * @title  订单收款消息队列
     * @descripe
     * @param
     * @return
     * @throws
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

    /**
     * 方法废弃!此方法不通,在传入参数有异
     *
     * @param refundNumber 在队列的接受方,第一个传退款单据号,第二个传退货单
     */
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
