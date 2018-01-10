package cn.com.leyizhuang.app.remote.queue;

import cn.com.leyizhuang.app.core.constant.AppGoodsLineType;
import cn.com.leyizhuang.app.core.constant.AppOrderSubjectType;
import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import cn.com.leyizhuang.app.core.constant.ProductType;
import cn.com.leyizhuang.app.core.utils.JsonUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqMessage;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqOrderChannel;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderBaseInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderGoodsInf;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.AppSeparateOrderService;
import cn.com.leyizhuang.app.foundation.service.TransactionalSupportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

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



    ObjectMapper objectMapper = new ObjectMapper();

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
                    }else{
                        separateOrderService.separateOrder(orderNumber);
                    }
                } catch (IOException e) {
                    log.warn("消息格式错误!");
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        log.info("消费拆单消息队列中的消息 End");
    }

}
