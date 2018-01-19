package cn.com.leyizhuang.app.remote.queue;

import cn.com.leyizhuang.app.core.utils.JsonUtils;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqMessage;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqOrderChannel;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.AppSeparateOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author Richard
 * @create 2017/12/27.
 */
@EnableBinding(value = {MqOrderChannel.class})
@Slf4j
@Component
public class SinkReceiver {


    @Resource
    private AppOrderService orderService;

    @Resource
    private AppSeparateOrderService separateOrderService;


    private ObjectMapper objectMapper = new ObjectMapper();

    @StreamListener(value = MqOrderChannel.RECEIVE_ORDER)
    public void receiveOrder(MqMessage message) {
        log.info("消费拆单消息队列中的消息 Begin");
        log.info("消息类型:{}", message.getType());
        log.info("消息内容:\n{}", JsonUtils.formatJson(message.getContent()));

        switch (message.getType()) {
            case ORDER:
                try {
                    String orderNumber = objectMapper.readValue(message.getContent(), String.class);
                    Boolean isExist = separateOrderService.isOrderExist(orderNumber);
                    if (isExist) {
                        log.info("该订单已拆单，不能重复拆单!");
                    } else {
                        //拆单
                        separateOrderService.separateOrder(orderNumber);
                        //拆单完成之后发送订单和订单商品信息到EBS
                        separateOrderService.sendOrderBaseInfAndOrderGoodsInf(orderNumber);
                        //发送订单券儿信息
                        separateOrderService.sendOrderCouponInf(orderNumber);
                        //发送订单收款信息
                        separateOrderService.sendOrderReceiptInf(orderNumber);
                    }
                } catch (IOException e) {
                    log.warn("消息格式错误!");
                    e.printStackTrace();
                } catch (Exception e) {
                    log.warn("{}", e);
                    e.printStackTrace();
                }
                break;
            case RECHARGE_RECEIPT:
                try {
                    String rechargeNo = objectMapper.readValue(message.getContent(), String.class);
                    Boolean isExist = separateOrderService.isRechargeReceiptExist(rechargeNo);
                    if (isExist) {
                        log.info("该充值单已拆单，不能重复拆单!");
                    } else {
                        //拆单
                        separateOrderService.separateRechargeReceipt(rechargeNo);

                        //发送充值收款信息
                        separateOrderService.sendRechargeReceiptInf(rechargeNo);
                    }
                } catch (IOException e) {
                    log.warn("消息格式错误!");
                    e.printStackTrace();
                } catch (Exception e) {
                    log.warn("{}", e);
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        log.info("消费拆单消息队列中的消息 End");
    }

}
