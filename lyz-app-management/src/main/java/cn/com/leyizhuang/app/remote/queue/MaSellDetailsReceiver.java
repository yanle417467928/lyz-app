package cn.com.leyizhuang.app.remote.queue;

import cn.com.leyizhuang.app.core.utils.JsonUtils;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqMessage;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqSellDetailsChannel;
import cn.com.leyizhuang.app.foundation.service.StatisticsSellDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 销量明细消费者
 * Created by panjie on 2018/1/25.
 */
@EnableBinding(value = {MqSellDetailsChannel.class})
@Slf4j
@Component
public class MaSellDetailsReceiver {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Resource
    private StatisticsSellDetailsService statisticsSellDetailsService;

    @StreamListener(value = MqSellDetailsChannel.RECEIVE_DETAILS)
    public void receiverSellDetails(MqMessage message){
        log.info("销售明细消息队列中的消息 Begin");
        log.info("消息类型:{}", message.getType());
        log.info("消息内容:\n{}", JsonUtils.formatJson(message.getContent()));

        switch (message.getType()) {
            case SELL_ORDER_DETAILS:
                try {
                    String orderNumber = objectMapper.readValue(message.getContent(), String.class);

                    // 新增下单明细
                    statisticsSellDetailsService.addOrderSellDetails(orderNumber);
                } catch (IOException e) {
                    log.warn("消息格式错误!");
                    e.printStackTrace();
                    // 记录日志
                    statisticsSellDetailsService.recordeErrorLog(message.getContent(),e.getMessage());
                } catch (Exception e) {
                    log.warn("{}", e);
                    e.printStackTrace();

                    // 记录日志
                    statisticsSellDetailsService.recordeErrorLog(message.getContent(),e.getMessage());
                }
                break;
            case SELL_RETURN_ORDER_DETAILS:
                try {
                    String returnOrderNumber = objectMapper.readValue(message.getContent(), String.class);

                    // 新增退单明细
                    statisticsSellDetailsService.addReturnOrderSellDetails(returnOrderNumber);
                } catch (IOException e) {
                    log.warn("消息格式错误!");
                    e.printStackTrace();
                    // 记录日志
                    statisticsSellDetailsService.recordeErrorLog(message.getContent(),e.getMessage());
                } catch (Exception e) {
                    log.warn("{}", e);
                    e.printStackTrace();

                    // 记录日志
                    statisticsSellDetailsService.recordeErrorLog(message.getContent(),e.getMessage());
                }
                break;
            default:
                break;
        }
        log.info("销售明细消息队列中的消息 End");
    }
}
